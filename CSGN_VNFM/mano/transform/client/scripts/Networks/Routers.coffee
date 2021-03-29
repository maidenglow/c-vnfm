'use strict'

angular.module('app.routers', [])

.controller('RoutersCtrl', [
	'$scope'
	'$modal'
	'logger'
	'$q'
	'$timeout'
	'NetworkingRouterAPI'
	'NetworkingNetworkAPI'
	'NetworkingRouterGatewayAPI'
	'NetworkingRouterInterfaceAPI'
	'TenantsAPI'
	'NetworkingPortAPI'
	(
		$scope
		$modal
		logger
		$q
		$timeout
		NetworkingRouterAPI
		NetworkingNetworkAPI
		NetworkingRouterGatewayAPI
		NetworkingRouterInterfaceAPI
		TenantsAPI
		NetworkingPortAPI
	) ->

		networks = []
		projects = []
		ports = []

		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getRouters().$promise
				NetworkingNetworkAPI.query().$promise
				TenantsAPI.get().$promise
				getPorts().$promise
			]).then((result) ->

				#### networks ####
				networks = result[1]

				#### projects ####
				projects = result[2].tenants

				#### ports ####
				ports = result[3]

				#### routers ####
				routers = $scope.routers
				for router in routers
					lookupNetworkName(router)
					lookupProjectName(router)
					lookupPortInfo(router)



				$scope.selectItem routers[0] if routers and routers.length > 0
			)

		#========================================
		# Functions
		#========================================

		########################
		# lookup Project Name
		########################
		lookupProjectName = (router) ->
			for project in projects
				if project.id is router.tenant_id
					router.project_name = project.name
			return

		########################
		# lookup Network Name
		########################
		lookupNetworkName = (router) ->
			if router.external_gateway_info
				for network in networks
					if network.id is router.external_gateway_info.network_id
						router.external_gateway_info.network_name = network.name
			return

		########################
		# lookup Port Info
		########################
		lookupPortInfo = (router,flush = false) ->
			if(typeof router.ports is 'undefined' || flush)
				router.ports = []

			for port in ports
				if router.id is port.device_id
					router.ports.push port
			return

		refreshPortInfo = () ->
			getPorts().$promise.then((response) ->
				ports = response
				routers = $scope.routers
				for router in routers
					lookupPortInfo(router,true)
			)

			return

		########################
		# get Routers
		########################
		getRouters = (router) ->
			NetworkingRouterAPI.query router,
				(response) ->
					$scope.routers = response
					return

		########################
		# get Router
		########################
		getRouter = (router) ->
			NetworkingRouterAPI.get router,
				(response) ->
					$scope.router = response
					idx = findIndex(router)
					if idx > -1
						newRouter = response
						newRouter["$$hashKey"] = $scope.routers[idx]["$$hashKey"]
						lookupNetworkName(newRouter)
						$scope.routers[idx] = newRouter
						$scope.selectItem newRouter
					else
						$scope.routers.splice 0, 0, response
					return

		########################
		# create Router
		########################
		createRouter = (router) ->
			NetworkingRouterAPI.save router,
				(response) ->
					logger.logSuccess "라우터가 생성되었습니다."
					getRouter({ id: response.id })
						.$promise.then ->
							# Refresh Router
							$timeout(->
								refreshRouter(response.id)
							, 1000)
					return

		########################
		# update Router
		########################
		updateRouter = (router) ->
			NetworkingRouterAPI.update router,
				(response) ->
					logger.logSuccess "게이트웨이정보가 설정되었습니다."
					getRouter({ id: response.id })
						.$promise.then ->
							# Refresh Router
							$timeout(->
								refreshRouter(response.id)
							, 1000)
					return

		########################
		# delete Router
		########################
		deleteRouter = (router) ->
			NetworkingRouterAPI.delete router,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						idx = findIndex(router)
						$scope.routers.splice idx, 1
						$scope.selectItem $scope.routers[0] if $scope.routers.length > 0
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
					return

		########################
		# Refresh Router
		########################
		refreshRouter = (id) ->
			NetworkingRouterAPI.get { 'id': id },
				(response) ->
					if response.status != 'ACTIVE'
						$timeout(->
							refreshRouter(id)
						, 2500)
					else
						idx = findIndex(response)
						router = response
						router["$$hashKey"] = $scope.routers[idx]["$$hashKey"]
						$scope.routers[idx] = router if idx > -1
						$scope.selectItem $scope.routers[idx]
					return

		########################
		# add Gateway Router
		########################
		addGatewayRouter = (router) ->
			NetworkingRouterGatewayAPI.save router,
				(response) ->
					routerId = response.router.id
					logger.logSuccess "게이트웨이정보가 설정되었습니다."
					getRouter({ id: routerId }).$promise.then((response) ->
						refreshPortInfo();
						lookupNetworkName($scope.router)
						lookupProjectName($scope.router)
					)
					return

		########################
		# delete Gateway Router
		########################
		deleteGatewayRouter = (router) ->
			NetworkingRouterGatewayAPI.delete router,
				(response) ->
					routerId = response.router.id
					logger.logSuccess "게이트웨이정보가 삭제되었습니다."
					getRouter({ id: routerId }).$promise.then((response) ->
						refreshPortInfo()
						lookupNetworkName($scope.router)
						lookupProjectName($scope.router)
					)
					return

		########################
		# add Router Interface
		########################
		addRouterInterface = (router_interface) ->
			NetworkingRouterInterfaceAPI.save router_interface,
				(response) ->
					if response.success
						logger.logSuccess "인터페이스가 추가 되었습니다."
						refreshPortInfo();
					else
						logger.logError "인터페이스 생성시 오류가 발생하였습니다. <br/>" + response.message
					return

		########################
		# delete Router Interface
		########################
		deleteRouterInterface = (router, port) ->
			param = angular.copy(port)
			param.router_id = router.id
			NetworkingRouterInterfaceAPI.delete param,
				(response) ->
					if response.success
						logger.logSuccess "인터페이스가 삭제되었습니다."
						refreshPortInfo();
					else
						logger.logError "인터페이스 삭제시 오류가 발생하였습니다. <br/>" + response.message
					return


		########################
		# Find Index
		########################
		findIndex = (router) ->
			for item, idx in $scope.routers
				if item.id is router.id
					return idx
			return -1

		########################
		# get Ports
		########################
		getPorts = () ->
			NetworkingPortAPI.query {},
				(response) ->
					return response

		########################
		# get Port
		########################
		getPort = (port) ->
			NetworkingPortAPI.get port,
				(response) ->
					return response

		#========================================
		# $Scope
		#========================================

		########################
		# Routers
		########################
		$scope.routers

		$scope.itemStatusClass = (router) ->
			if router.status is 'ACTIVE'
				clazz = 'label label-success'
			else
				clazz = 'label label-danger'

		########################
		# Router
		########################
		$scope.router

		$scope.selectItem = (router) ->
			$scope.router = router

		$scope.isSelected = (router) ->
			$scope.router is router

		#========================================
		# Button Event
		#========================================

		########################
		# Create Router Form
		########################
		$scope.createRouterForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/router/create.html'
				controller: 'CreateRouterModalCtrl'
			)

			modalInstance.result.then(
				(router) ->
					createRouter router
					return
				, ->
					return
			)

			return # // end create Router Form

		########################
		# Manage Gateway Form
		########################
		$scope.manageGatewayForm = (router) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/router/manage_gateway.html'
				controller: 'ManageGatewayModalCtrl'
				resolve:
					router: ->
						router
			)

			modalInstance.result.then(
				(router) ->
					addGatewayRouter router
					return
				, ->
					return
			)

			return # // end manage Gateway Form

		########################
		# Delete Router Form
		########################
		$scope.deleteRouterForm = (router) ->
			modalInstance = $modal.open(
				templateUrl: "views/networks/router/confirm.html"
				controller: ($scope, $modalInstance, router) ->
					$scope.ok = ->
						$modalInstance.close router

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					router: ->
						router
			)

			modalInstance.result.then(
				(router) ->
					deleteRouter router
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return # // end Delete Flavor Form


		########################
		# Delete Gateway Form
		########################
		$scope.deleteGatewayForm = (router) ->
			modalInstance = $modal.open(
				templateUrl: "views/networks/router/confirm.html"
				controller: ($scope, $modalInstance, router) ->
					$scope.ok = ->
						$modalInstance.close router

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					router: ->
						router
			)

			modalInstance.result.then(
				(router) ->
					router.external_gateway_info = {}
					deleteGatewayRouter router
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return # // end Delete Flavor Form



		########################
		# Create Inteface Form
		########################
		$scope.createIntefaceForm = (router) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/router/create_interface.html'
				controller: 'CreateInterfaceModalCtrl'
				resolve:
					router: ->
						router
			)

			modalInstance.result.then(
				(router_interface) ->
					console.log router_interface
					addRouterInterface router_interface
					return
				, ->
					return
			)

			return # // end create Router Form

		########################
		# Delete Router Interface Form
		########################
		$scope.deleteRouterInterfaceForm = (router,port) ->

			modalInstance = $modal.open(
				templateUrl: "views/networks/networks/confirm.html"
				controller: ($scope, $modalInstance, port) ->
					port_name = if port.name then port.name else port.id.substring(0,8)
					$scope.msg = "선택한 인터페이스 (" + port_name + ")을"

					$scope.ok = ->
						$modalInstance.close port

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					port: ->
						port
			)

			modalInstance.result.then(
				(port) ->
					deleteRouterInterface router,port
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return

		return	# end of RouterCtrl
])



########################
# Create Router
########################
.controller('CreateRouterModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	(
		$scope
		$modalInstance
		$q
	) ->

		#========================================
		# Init Form
		#========================================

		# router
		$scope.router =
			name: null

		original = angular.copy($scope.router)


		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.router = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.router, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.router, original)

		$scope.submit = (router) ->
			$modalInstance.close router
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return

		#========================================
		# Form Supporters
		#========================================

		# ---- init ----
		$scope.init = ->
			return $q.all([
			]).then((result) ->
			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================


		return # end of CreateRouterModalCtrl
])




########################
# Manage Gateway
########################
.controller('ManageGatewayModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'router'
	'NetworkingNetworkAPI'
	(
		$scope
		$modalInstance
		$q
		router
		NetworkingNetworkAPI
	) ->

		#========================================
		# Init Form
		#========================================

		# router
		$scope.router = angular.copy(router)

		original = angular.copy($scope.router)

		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.router = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.router, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.router, original)

		$scope.submit = (router) ->
			router.external_gateway_info =
				network_id	: router.select_external_gateway_info.id
			$modalInstance.close router
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return

		#========================================
		# Form Supporters
		#========================================

		# ---- init ----
		$scope.init = ->
			return $q.all([
				NetworkingNetworkAPI.query().$promise
			]).then((result) ->

				#### networks ####
				$scope.networks = result[0];
			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================


		return # end of CreateRouterModalCtrl
])




########################
# Create Interface
########################
.controller('CreateInterfaceModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'router'
	'NetworkingSubnetAPI'
	(
		$scope
		$modalInstance
		$q
		router
		NetworkingSubnetAPI
	) ->

		#========================================
		# Init Form
		#========================================

		$scope.router_interface =
			router_id : router.id
			router_name : router.name
			ip_address : ""

		$scope.subnets = []

		original = angular.copy($scope.router_interface)



		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.router = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.router_interface, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.router_interface, original)

		$scope.submit = (router_interface) ->
			$modalInstance.close router_interface
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return

		#========================================
		# Form Supporters
		#========================================

		# ---- init ----
		$scope.init = ->
			return $q.all([
				NetworkingSubnetAPI.query().$promise
			]).then((result) ->

				$scope.subnets = result[0];
			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================


		return # end of CreateInterfaceModalCtrl
])

