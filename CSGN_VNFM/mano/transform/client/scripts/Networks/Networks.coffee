'use strict'

angular.module('app.networks', [])

.controller('NetworksCtrl', [
	'$scope'
	'$modal'
	'logger'
	'$q'
	'$timeout'
	'NetworkingNetworkAPI'
	'NetworkingSubnetAPI'
	'NetworkingPortAPI'
	(
		$scope
		$modal
		logger
		$q
		$timeout
		NetworkingNetworkAPI
		NetworkingSubnetAPI
		NetworkingPortAPI
	) ->


		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getNetworks().$promise
				getPorts().$promise
			]).then((result) ->

				#### ports ####
				ports = result[1]
				
				#### networks ####
				networks = $scope.networks
				for network in networks
					network.ports = []
					for port in ports
						if network.id is port.network_id
							network.ports.push port
				$scope.selectItem networks[0] if networks and networks.length > 0
			)

		#========================================
		# Functions
		#========================================

		########################
		# get Networks
		########################
		getNetworks = (network) ->
			NetworkingNetworkAPI.query network,
				(response) ->
					$scope.networks = response
					return

		########################
		# get Network
		########################
		getNetwork = (network) ->
			NetworkingNetworkAPI.get network,
				(response) ->
					$scope.network = response
					idx = findIndex(network)
					if idx > -1
						network = response
						network["$$hashKey"] = $scope.networks[idx]["$$hashKey"]
						$scope.networks[idx] = network
					else
						$scope.networks.splice 0, 0, response
					return

		########################
		# create Network
		########################
		createNetwork = (network) ->
			NetworkingNetworkAPI.save network,
				(response) ->
					if response.success
						logger.logSuccess "네트워크가 생성되었습니다."
						getNetwork({ id: response.data.id })
							.$promise.then ->
								# Refresh Network
								$timeout(->
									refreshNetwork(response.data.id)
								, 1000)
					else
						logger.logError "네트워크 생성에 실패하였습니다.<br/>" + response.message
					return
					
		########################
		# update Network
		########################
		updateNetwork = (network) ->
			NetworkingNetworkAPI.update network,
				(response) ->
					logger.logSuccess "네트워크가 수정되었습니다."
					getNetwork({ id: response.id })
						.$promise.then ->
							# Refresh Network
							$timeout(->
								refreshNetwork(response.id)
							, 1000)
					return

		########################
		# delete Network
		########################
		deleteNetwork = (network) ->
			NetworkingNetworkAPI.delete network,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						idx = findIndex(network)
						$scope.networks.splice idx, 1
						$scope.selectItem $scope.networks[0] if $scope.networks.length > 0
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
					return

		########################
		# Refresh Network	
		########################
		refreshNetwork = (id) ->
			NetworkingNetworkAPI.get { 'id': id },
				(response) ->
					if response.status is 'BUILD'
						$timeout(->
							refreshNetwork(id)
						, 2500)
					else
						idx = findIndex(response)
						if idx > -1
							network = response
							network["$$hashKey"] = $scope.networks[idx]["$$hashKey"]
							$scope.networks[idx] = network
							$scope.selectItem $scope.networks[idx]
					return

		########################
		# Find Index
		########################
		findIndex = (network) ->
			for item, idx in $scope.networks
				if item.id is network.id
					return idx
			return -1

		########################
		# create Subnet
		########################
		createSubnet = (subnet) ->
			NetworkingSubnetAPI.save subnet,
				(response) ->
					if response.success
						logger.logSuccess "서브넷이 생성되었습니다."
						getNetwork({ id: response.data.network_id })
							.$promise.then ->
								# Refresh Network
								$timeout(->
									refreshNetwork(response.data.network_id)
								, 1000)
					else
						logger.logError "서브넷 생성에 실패하였습니다.<br/>" + response.message
					return
					
		########################
		# update Subnet
		########################
		updateSubnet = (subnet) ->
			NetworkingSubnetAPI.update subnet,
				(response) ->
					if response.NeutronError
						logger.logError "서브넷 정보 수정에 실패하였습니다.<br/>" + response.NeutronError
					else
						logger.logSuccess "서브넷 정보가 수정되었습니다."
						getNetwork({ id: response.subnet.network_id })
							.$promise.then ->
								# Refresh Network
								$timeout(->
									refreshNetwork(response.subnet.network_id)
								, 1000)
					return
				

		########################
		# delete Subnet
		########################
		deleteSubnet = (subnet) ->
			NetworkingSubnetAPI.delete subnet,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						getNetwork({
							id : subnet.network_id
						})
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
					return

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
					
		########################
		# update Port
		########################
		updatePort = (port) ->
			NetworkingPortAPI.update port,
				(response) ->
					logger.logSuccess "포트정보가 수정되었습니다."
					port = response
					idx = findIndex({"id" : port.network_id})
					if idx > -1
						for iport, seq in $scope.networks[idx].ports
							if port.id is iport.id
								$scope.networks[idx].ports[seq] = port
					return

		#========================================
		# $Scope
		#========================================

		########################
		# Networks
		########################
		$scope.networks

		$scope.itemStatusClass = (network) ->
			if network.status is 'ACTIVE'
				clazz = 'label label-success'
			else
				clazz = 'label label-danger'

		########################
		# Network
		########################
		$scope.network

		$scope.selectItem = (network) ->
			$scope.network = network

		$scope.isSelected = (network) ->
			$scope.network is network

		#========================================
		# Button Event
		#========================================

		########################
		# Create Network Form
		########################
		$scope.createNetworkForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/create.html'
				controller: 'CreateNetworkModalCtrl'
			)

			modalInstance.result.then(
				(network) ->
					createNetwork network
					return
				, ->
					return
			)

			return # // end create Network Form

		########################
		# Edit Network Form
		########################
		$scope.editNetworkForm = (network) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/edit_network.html'
				controller: 'EditNetworkModalCtrl'
				resolve:
					network: ->
						network
			)

			modalInstance.result.then(
				(network) ->
					updateNetwork network
					return
				, ->
					return
			)

			return # // end create Network Form

		########################
		# Delete Network Form
		########################
		$scope.deleteNetworkForm = (network) ->
			modalInstance = $modal.open(
				templateUrl: "views/networks/networks/confirm.html"
				controller: ($scope, $modalInstance, network) ->
					$scope.ok = ->
						$modalInstance.close network

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					network: ->
						network
			)
			
			modalInstance.result.then(
				(network) ->
					deleteNetwork network
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return
			
		########################
		# Create Subnet Form
		########################
		$scope.createSubnetForm = (network) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/create_subnet.html'
				controller: 'CreateSubnetModalCtrl'
				resolve:
					network: ->
						network
			)

			modalInstance.result.then(
				(subnet) ->
					createSubnet subnet
					return
				, ->
					return
			)

			return # // end create Subnet Form
			
		########################
		# Edit Subnet Form
		########################
		$scope.editSubnetForm = (subnet) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/edit_subnet.html'
				controller: 'EditSubnetModalCtrl'
				resolve:
					subnet: ->
						subnet
			)

			modalInstance.result.then(
				(subnet) ->
					updateSubnet subnet
					return
				, ->
					return
			)

			return # // end create Subnet Form
			
		########################
		# Delete Subnet Form
		########################
		$scope.deleteSubnetForm = (subnet) ->
			
			modalInstance = $modal.open(
				templateUrl: "views/networks/networks/confirm.html"
				controller: ($scope, $modalInstance, subnet) ->
					subnet_name = if subnet.name then subnet.name else subnet.id.substring(0,8)
					$scope.msg = "선택한 서브넷 (" + subnet_name + ")을"
					
					$scope.ok = -> 
						$modalInstance.close subnet

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					subnet: ->
						subnet
			)
			
			modalInstance.result.then(
				(subnet) ->
					deleteSubnet subnet
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return
			
		########################
		# Edit Port Form
		########################
		$scope.editPortForm = (port) ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/edit_port.html'
				controller: 'EditPortModalCtrl'
				resolve:
					port: ->
						port
			)

			modalInstance.result.then(
				(port) ->
					updatePort port
					return
				, ->
					return
			)

			return # // end create Port Form

		return	# end of NetworkCtrl
])



########################
# Create Network
########################
.controller('CreateNetworkModalCtrl', [
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
		$scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/
		$scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		
		$scope.stage = 'step1'
		# network
		$scope.network =
			name: null
			admin_state_up : true
			create_subnet : true
			enable_dhcp : true
			allocation_pools_text : ""
			dns_nameservers_text : ""
			host_routes_text : ""
		
		original = angular.copy($scope.network)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.network = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.network, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			if $scope.stage isnt 'step2'
				return true
			return $scope.form.$valid && !angular.equals($scope.network, original)

		$scope.submit = (network) ->
			if $scope.stage is 'step1'
				 $scope.stage = 'step2'
				 return
			
			if $scope.stage is 'step2'
				 if $scope.form.$valid
				 	$scope.stage = 'step3'
				 return
				 	
			if network.create_subnet
				network.neutronSubnets = [];
				network.neutronSubnets[0] = 
					cidr: network.cidr
					allocation_pools : []
					dns_nameservers: []
					enable_dhcp: network.enable_dhcp
					gateway_ip: network.gateway_ip
					host_routes: []
					ip_version: network.ip_version
					name: network.subnet_name
				if network.no_gateway
					network.neutronSubnets[0].gateway_ip = null
					
				for pool in network.allocation_pools_text.split("\n")
					if pool
						item = pool.split(",")
						network.neutronSubnets[0].allocation_pools.push {
							'start' : $.trim(item[0]),
							'end' : $.trim(item[1])
						}
				
				for dns_nameserver in network.dns_nameservers_text.split("\n")
					if dns_nameserver
						network.neutronSubnets[0].dns_nameservers.push dns_nameserver
				
				for host_route in network.host_routes_text.split("\n")
					if host_route
						item = host_route.split(",")
						network.neutronSubnets[0].host_routes.push {
							'destination' : $.trim(item[0]),
							'nexthop' : $.trim(item[1])
						}
			
			$modalInstance.close network
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
				$scope.network.ip_version = "4"
				$scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
		$scope.changeIpVersion = () ->
			if $scope.network.ip_version == "4"
				$scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
			else if $scope.network.ip_version == "6"
				$scope.cidr_pattern = /^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*(\/(d|dd|1[0-1]d|12[0-8]))$/
			else
				;
			return
		
		$scope.setStage = (stage) -> 
			if $scope.stage is 'step3'
				$scope.stage = stage
			else
				if stage is 'step3'
					if $scope.form.$valid
						$scope.stage = stage
					else
						$scope.stage = 'step2'
				else
					$scope.stage = stage
			return
			
		$scope.next = -> 
			if $scope.stage is 'step1'
				 $scope.stage = 'step2'
			else if $scope.stage is 'step2'
				 if $scope.form.$valid
				 	$scope.stage = 'step3'
			return
				
		
		return # end of CreateNetworkModalCtrl
])


########################
# Edit Network
########################
.controller('EditNetworkModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'network'
	(
		$scope
		$modalInstance
		$q
		network
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# network
		$scope.network = angular.copy(network)
		
		original = angular.copy($scope.network)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.network = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.network, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.network, original)

		$scope.submit = (network) ->
			$modalInstance.close network
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
				
		
		return # end of EditNetworkModalCtrl
])


########################
# Create Subnet
########################
.controller('CreateSubnetModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'network'
	(
		$scope
		$modalInstance
		$q
		network
	) ->
	
		#========================================
		# Init Form
		#========================================
		$scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/
		$scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		
		$scope.stage = 'step1'
		# subnet
		$scope.subnet =
			name: null
			network_id : network.id
			enable_dhcp : true
			allocation_pools : []
			dns_nameservers : []
			host_routes : []
			allocation_pools_text : ""
			dns_nameservers_text : ""
			host_routes_text : ""
		
		original = angular.copy($scope.subnet)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.network = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.network, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			if $scope.stage isnt 'step1'
				return true
			return $scope.form.$valid && !angular.equals($scope.network, original)

		$scope.submit = (subnet) ->
			
			if $scope.stage is 'step1'
				 if $scope.form.$valid
				 	$scope.stage = 'step2'
				 return
				 
			subnet.allocation_pools = []
			subnet.dns_nameservers = []
			subnet.host_routes = []

			if subnet.no_gateway
				subnet.gateway_ip = null
				
			for pool in subnet.allocation_pools_text.split("\n")
				if pool
					item = pool.split(",")
					subnet.allocation_pools.push {
						'start' : $.trim(item[0]),
						'end' : $.trim(item[1])
					}
			
			for dns_nameserver in subnet.dns_nameservers_text.split("\n")
				if dns_nameserver
					subnet.dns_nameservers.push dns_nameserver
			
			for host_route in subnet.host_routes_text.split("\n")
				if host_route
					item = host_route.split(",")
					subnet.host_routes.push {
						'destination' : $.trim(item[0]),
						'nexthop' : $.trim(item[1])
					}
			
			$modalInstance.close subnet
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
				$scope.subnet.ip_version = "4"
				$scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
		$scope.changeIpVersion = () ->
			if $scope.subnet.ip_version == "4"
				$scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
			else if $scope.subnet.ip_version == "6"
				$scope.cidr_pattern = /^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*(\/(d|dd|1[0-1]d|12[0-8]))$/
			else
				;
			return
		
		$scope.setStage = (stage) -> 
			if $scope.stage is 'step2'
				$scope.stage = stage
			else
				if stage is 'step2'
					if $scope.form.$valid
						$scope.stage = stage
					else
						$scope.stage = 'step1'
				else
					$scope.stage = stage
			return
			
		$scope.next = -> 
			if $scope.stage is 'step1'
			 if $scope.form.$valid
			 	$scope.stage = 'step3'
			return
				
		
		return # end of CreateSubnetModalCtrl
])


########################
# Edit Subnet
########################
.controller('EditSubnetModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'subnet'
	(
		$scope
		$modalInstance
		$q
		subnet
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		$scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/
		$scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		$scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/
		
		$scope.stage = 'step1'
		# subnet
		$scope.subnet = angular.copy(subnet)
		$scope.subnet.allocation_pools = null
		$scope.subnet.no_gateway = if subnet.gateway_ip then false else true 
		
		$scope.subnet.dns_nameservers_text = $scope.subnet.dns_nameservers.join("\n")
		host_route_array = []
		for host_route in $scope.subnet.host_routes
			host_route_array.push(host_route.destination + ", " + host_route.nexthop)
		$scope.subnet.host_routes_text = host_route_array.join("\n")
		
		original = angular.copy($scope.subnet)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.network = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.network, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			if $scope.stage isnt 'step1'
				return true
			return $scope.form.$valid && !angular.equals($scope.network, original)

		$scope.submit = (subnet) ->
			
			if $scope.stage is 'step1'
				 if $scope.form.$valid
				 	$scope.stage = 'step2'
				 return
				 
			subnet.dns_nameservers = []
			subnet.host_routes = []

			if subnet.no_gateway
				subnet.gateway_ip = null
				
			for dns_nameserver in subnet.dns_nameservers_text.split("\n")
				if dns_nameserver
					subnet.dns_nameservers.push dns_nameserver
			
			for host_route in subnet.host_routes_text.split("\n")
				if host_route
					item = host_route.split(",")
					subnet.host_routes.push {
						'destination' : $.trim(item[0]),
						'nexthop' : $.trim(item[1])
					}
			
			$modalInstance.close subnet
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
		
		$scope.setStage = (stage) -> 
			if $scope.stage is 'step2'
				$scope.stage = stage
			else
				if stage is 'step2'
					if $scope.form.$valid
						$scope.stage = stage
					else
						$scope.stage = 'step1'
				else
					$scope.stage = stage
			return
			
		$scope.next = -> 
			if $scope.stage is 'step1'
			 if $scope.form.$valid
			 	$scope.stage = 'step3'
			return
				
		
		return # end of EditSubnetModalCtrl
])



########################
# Edit Port 
########################
.controller('EditPortModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'port'
	(
		$scope
		$modalInstance
		$q
		port
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# network
		$scope.port = angular.copy(port)
		
		original = angular.copy($scope.port)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.network = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.network, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.network, original)

		$scope.submit = (port) ->
			$modalInstance.close port
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
		
				
		
		return # end of CreateNetworkModalCtrl
])
