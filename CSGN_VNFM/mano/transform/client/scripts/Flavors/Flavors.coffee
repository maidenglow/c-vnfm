'use strict'

angular.module('app.flavors', [])

.factory('flavorsService', [
	'$http'
	'CONST_RESTFUL_API'
	($http, CONST_RESTFUL_API) ->
		getList: ->
			$http.get CONST_RESTFUL_API.prefixURL + '/flavors/list.do'
				.success(
					(data, status, headers, config) ->
						data
				)
				.error(
					(data, status, headers, config) ->
						console.log status, headers, config
				)
])


.factory('FlavorAccessAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.FLAVORS_ACCESS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			}
])


.factory('FlavorExtrasAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.FLAVORS_EXTRAS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			},
			{
				delete:
					method : 'delete'
					params: 
						key: "@key"
			}
])

.factory('TenantsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.IDENTITY_URL.TENANTS,
			{
			}
])


.controller('FlavorsCtrl', [
	'$scope'
	'$modal'
	'$http'
	'$q'
	'logger'
	'$timeout'
	'flavorsService'
	'ComputeFlavorsAPI'
	($scope, $modal, $http, $q, logger, $timeout, flavorsService, ComputeFlavorsAPI) ->

		#========================================
		# Scope
		#========================================
		
		

			
		########################
		# Flavors
		########################
		$scope.flavors



		########################
		# Flavor
		########################
		$scope.flavor

		$scope.selectItem = (flavor) ->
			$scope.flavor = flavor

		$scope.isSelected = (flavor) ->
			$scope.flavor is flavor


		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getFlavors().$promise
			]).then((result) ->

				#### flavors ####
				flavors = result[0]
				$scope.flavors = flavors
				$scope.selectItem flavors[0] if flavors and flavors[0]
			)

		#========================================
		# Functions
		#========================================
		
		########################
		# get Flavor
		########################
		getFlavors = (flavor) ->
			ComputeFlavorsAPI.query flavor,
				(response) ->
					$scope.flavors = response
					return
		########################
		# get Flavor
		########################
		getFlavor = (flavor) ->
			ComputeFlavorsAPI.get flavor,
				(response) ->
					$scope.flavor = response
					idx = findIndex(flavor)
					if idx > -1
						newflavor = response
						newflavor["$$hashKey"] = $scope.flavors[idx]["$$hashKey"]
						$scope.flavors[idx] = newflavor
					else
						$scope.flavors.splice 0, 0, response
					return
					
		########################
		# create Flavor
		########################
		createFlavor = (flavor) ->
			ComputeFlavorsAPI.save flavor,
				(response) ->
					logger.logSuccess "Flavor가 생성되었습니다."
					getFlavor({ id: response.id })
						.$promise.then ->
							# Refresh Flavor
							$timeout(->
								refreshFlavor(response.id)
							, 1000)
					return

		########################
		# Update Flavor
		########################
		updateFlavor = (flavor) ->
			ComputeFlavorsAPI.update flavor,
				(response) ->
					logger.logSuccess "Flavor가 수정되었습니다."
					idx = findIndex(flavor)
					$scope.flavor = flavor
					console.log(flavor)
					flavor["$$hashKey"] = $scope.flavors[idx]["$$hashKey"]
					$scope.flavors[idx] = flavor
					return
			return

		########################
		# delete Flavor
		########################
		deleteFlavor = (flavor) ->
			ComputeFlavorsAPI.delete flavor,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						idx = findIndex(flavor)
						$scope.flavors.splice idx, 1
						$scope.selectItem $scope.flavors[0] if $scope.flavors.length > 0
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
					return
					
					
		########################
		# Refresh Flavor	
		########################
		refreshFlavor = (id) ->
			ComputeFlavorsAPI.get { 'id': id },
				(response) ->
					idx = findIndex(response)
					flavor = response
					flavor["$$hashKey"] = $scope.flavors[idx]["$$hashKey"]
					$scope.flavors[idx] = flavor if idx > -1
					$scope.selectItem $scope.flavors[idx]
					return

		########################
		# Find Flavor
		########################
		findIndex = (flavor) ->
			for item, idx in $scope.flavors
				if item.id is flavor.id
					return idx
			return -1

		########################
		# Create Flavor Form
		########################
		$scope.createFlavorForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/flavors/create.html'
				controller: 'CreateFlavorModalCtrl'
			)

			modalInstance.result.then(
				(flavor) ->
					createFlavor flavor
					return
				, ->
					return
			)

			return # // end create Flavor Form

				
		########################
		# Delete Flavor Form
		########################
		$scope.deleteFlavorForm = (flavor) ->
			modalInstance = $modal.open(
				templateUrl: "views/flavors/confirm.html"
				controller: ($scope, $modalInstance, flavor) ->
					$scope.ok = ->
						$modalInstance.close flavor

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					flavor: ->
						flavor
			)

			modalInstance.result.then(
				(flavor) ->
					deleteFlavor flavor
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return # // end Delete Flavor Form
			

		########################
		# Edit Flavor Form
		########################
		$scope.editFlavorForm = (flavor) ->
			modalInstance = $modal.open(
				templateUrl: 'views/flavors/edit.html'
				controller: 'EditFlavorModalCtrl'
				resolve:
					flavor: ->
						flavor
			)

			modalInstance.result.then(
				(flavor) ->
					#console.log(flavor)
					updateFlavor flavor
					return
				, ->
					return
			)

			return # // end edit Flavor Form

		########################
		# List Flavor Extras Form
		########################
		$scope.getDetailFlavorExtrasForm = (flavor) ->
			modalInstance = $modal.open(
				templateUrl: 'views/flavors/detail_extras.html'
				controller: 'DetailFlavorExtrasModalCtrl'
				size: 'lg'
				resolve:
					flavor: ->
						flavor
			)

			modalInstance.result.then(
				(flavor) ->
					return
				, ->
					return
			)

			return # // end List Flavor Extras Form

		return
])



########################
# Create Flavors
########################
.controller('CreateFlavorModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'flavorsService'
	'TenantsAPI'
	(
		$scope
		$modalInstance
		$q
		flavorsService
		TenantsAPI
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# flavor
		$scope.flavor =
			name: null
			id: null
			ram: null
			vcpus: null
			disk: null
			ephemeral : null
			swap: null
			projects : []

		
		$scope.search = 
			project : ""
			allProject : ""
		
		original = angular.copy($scope.flavor)
		

		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.flavor = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.flavor, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.flavor, original)

		$scope.submit = (flavor) ->
			flavor["OS-FLV-EXT-DATA:ephemeral"] = flavor.ephemeral
			flavor.public = true;
			if flavor.projects.length > 0 
				flavor.public = false
			$modalInstance.close flavor
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
				TenantsAPI.get().$promise
			]).then((result) ->
			
				tenants = result[0];
				$scope.allprojects = tenants.tenants
				
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
		
		########################
		# Find Project Index
		########################
		findIndex = (project) ->
			for item, idx in $scope.flavor.projects
				if item.id is project.id
					return idx
			return -1
			
		$scope.addProject = (item) ->
			$scope.flavor.projects.push item 
			return
		
		$scope.removeProject = (item) ->
			idx = findIndex(item)
			if idx >=0
				$scope.flavor.projects.splice idx, 1
			return
			
		$scope.searchAllProjects = (item) ->
			
			for project in $scope.flavor.projects
				if item.id is project.id
					return false;
					 
			if $scope.search.allProject.length == 0
				return true
			else
				return item.name.indexOf($scope.search.allProject) >= 0


		$scope.searchProjects = (item) ->
			if $scope.search.project.length == 0
				return true
			else
				return item.name.indexOf($scope.search.project) >= 0

		
		return # end of CreateFlavorsModalCtrl
])



########################
# Edit Flavors
########################
.controller('EditFlavorModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'flavor'
	'FlavorAccessAPI'
	'TenantsAPI'
	(
		$scope
		$modalInstance
		$q
		flavor
		FlavorAccessAPI
		TenantsAPI
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# flavor
		$scope.flavor = angular.copy(flavor)
		$scope.flavor.ephemeral = flavor["OS-FLV-EXT-DATA:ephemeral"]
		
		$scope.flavor.projects = [] if !$scope.flavor.projects 
		
		$scope.search = 
			project : ""
			allProject : ""
			
			
		original = angular.copy($scope.flavor)
		
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.flavor = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canSubmit = ->
			return $scope.form.$valid

		$scope.submit = (flavor) ->
			flavor["OS-FLV-EXT-DATA:ephemeral"] = flavor.ephemeral
			flavor.public = true;
			if flavor.projects.length > 0 
				flavor.public = false
			console.log(flavor)
				
			$modalInstance.close flavor
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return	
			
		#========================================
		# Form Supporters
		#========================================
		
		flavorAccessReq = 
			id : flavor.id

		# ---- init ----
		$scope.init = ->
			return $q.all([
				TenantsAPI.get().$promise
				FlavorAccessAPI.get(flavorAccessReq).$promise
			]).then((result) ->

				tenants = result[0];
				$scope.allprojects = tenants.tenants
				
				
				$scope.flavor.projects = [];
				
				if result[1].flavor_access
					projectList = result[1].flavor_access
				else
					projectList = []
					
				for project in $scope.allprojects
					for myproject in projectList
						if project.id is myproject.tenant_id
							$scope.flavor.projects.push project
					
				console.log($scope.flavor.projects);

			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
		
		########################
		# Find Project Index
		########################
		findIndex = (project) ->
			for item, idx in $scope.flavor.projects
				if item.id is project.id
					return idx
			return -1
			
		$scope.addProject = (item) ->
			$scope.flavor.projects.push item 
			return
		
		$scope.removeProject = (item) ->
			idx = findIndex(item)
			if idx >=0
				$scope.flavor.projects.splice idx, 1
			return
			
		$scope.searchAllProjects = (item) ->
			
			for project in $scope.flavor.projects
				if item.id is project.id
					return false;
					 
			if $scope.search.allProject.length == 0
				return true
			else
				return item.name.indexOf($scope.search.allProject) >= 0


		$scope.searchProjects = (item) ->
			if $scope.search.project.length == 0
				return true
			else
				return item.name.indexOf($scope.search.project) >= 0
		
		return # end of EditFlavorsModalCtrl
])



########################
# Detail Flavor Extras Modal Ctrl
########################
.controller('DetailFlavorExtrasModalCtrl', [
	'$scope'
	'$modal'
	'$modalInstance'
	'$q'
	'logger'
	'flavor'
	'FlavorExtrasAPI'
	(
		$scope
		$modal
		$modalInstance
		$q
		logger
		flavor
		FlavorExtrasAPI
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# flavor
		$scope.extras = null;
		$scope.flavor = angular.copy(flavor)
			
		original = angular.copy($scope.flavor)
		
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.flavor = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.flavor, original)

		$scope.submit = (flavor) ->
			$modalInstance.close flavor
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return	
			
		#========================================
		# Form Supporters
		#========================================

		extras_request = 
			id : flavor.id


		# ---- init ----
		$scope.init = ->
			return $q.all([
				FlavorExtrasAPI.get(extras_request).$promise
			]).then((result) ->
			
				$scope.extras = result[0];
				
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
		########################
		# get Flavor Extras
		########################
		getFlavorExtras = (flavor) ->
			FlavorExtrasAPI.get flavor,
				(response) ->
					$scope.extras = response
					return
					
		########################
		# create Flavor Extras
		########################
		createFlavorExtras = (extras) ->
			FlavorExtrasAPI.save extras,
				(response) ->
					logger.logSuccess "Flavor 추가 사양이 생성되었습니다."
					getFlavorExtras(flavor)
					return
					
		########################
		# update Flavor Extras
		########################
		updateFlavorExtras = (extras) ->
			FlavorExtrasAPI.save extras,
				(response) ->
					logger.logSuccess "Flavor 추가 사양이 수정되었습니다."
					getFlavorExtras(flavor)
					return
					
		########################
		# delete Flavor Extras
		########################
		deleteFlavorExtras = (key) ->
			extras = 
				id : flavor.id
				key : key
				
			FlavorExtrasAPI.delete extras,
				(response) ->
					logger.logSuccess "Flavor 추가 사양이 삭제되었습니다."
					getFlavorExtras(flavor)
					return
					
		$scope.getKeys = (obj) ->
			ret = []
			for key, val of obj
				if key.indexOf("$") != 0
					ret.push(key)
			return ret

		########################
		# Create Flavor Extras Form
		########################
		$scope.CreateFlavorExtrasForm = (flavor) ->
			modalInstance = $modal.open(
				templateUrl: 'views/flavors/create_extras.html'
				controller: 'CreateFlavorExtrasModalCtrl'
				resolve:
					flavor: ->
						flavor
			)

			modalInstance.result.then(
				(extras) ->
					createFlavorExtras(extras);
					return
				, ->
					return
			)

			return # // end Create Flavor Extras Form
			
			
		########################
		# Update Flavor Extras Form
		########################
		$scope.UpdateFlavorExtrasForm = (flavor,key,val) ->
		
			modalInstance = $modal.open(
				templateUrl: 'views/flavors/edit_extras.html'
				controller: 'EditFlavorExtrasModalCtrl'
				resolve:
					extras: ->
						id : flavor.id
						key : key
						value : val
			)

			modalInstance.result.then(
				(extras) ->
					updateFlavorExtras(extras);
					return
				, ->
					return
			)

			return # // end Update Flavor Extras Form
			
		########################
		# Delete Flavor Extras Form
		########################
		$scope.DeleteFlavorExtrasForm = (key) ->

			modalInstance = $modal.open(
				templateUrl: "views/flavors/confirm.html"
				controller: ($scope, $modalInstance, flavor) ->
					$scope.ok = ->
						$modalInstance.close key

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					flavor: ->
						flavor
			)

			modalInstance.result.then(
				(key) ->
					deleteFlavorExtras key
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return # // end Delete Flavor Extras Form

		
		return # end of DetailFlavorExtrasModalCtrl
])



########################
# Create Flavor Extras Modal Ctrl
########################
.controller('CreateFlavorExtrasModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'flavor'
	'FlavorExtrasAPI'
	(
		$scope
		$modalInstance
		$q
		flavor
		FlavorExtrasAPI
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# flavor
		$scope.extras =
			id : flavor.id
			keys : null
		
		original = angular.copy($scope.extras)
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.flavor = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canSubmit = ->
			return $scope.form.$valid

		$scope.submit = (extras) ->
			$modalInstance.close extras
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
			
				$scope.keylists = [
					{
						name : "Quota: Read bytes"
						value : "quota:read_bytes_sec"
					},
					{
						name : "Quota: Write bytes"
						value : "quota:write_bytes_sec"
					},
					{
						name : "Quota: CPU"
						value : "quota:cpu_quota"
					},
					{
						name : "Quota: CPU period"
						value : "quota:cpu_period"
					},
					{
						name : "Quota: Inbound average"
						value : "quota:inbound_average"
					},
					{
						name : "Quota: Outbound average"
						value : "quota:outbound_average"
					},
					{
						name : "다른키"
						value : "custom"
					}
				]
				
				$scope.extras.keys = $scope.keylists[0].value if $scope.keylists.length > 0;
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================


		
		return # end of CreateFlavorExtrasModalCtrl
])




########################
# Edit Flavor Extras Modal Ctrl
########################
.controller('EditFlavorExtrasModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'extras'
	'FlavorExtrasAPI'
	(
		$scope
		$modalInstance
		$q
		extras
		FlavorExtrasAPI
	) ->
	
		#========================================
		# Init Form
		#========================================
		
		# flavor
		$scope.extras = extras
		
		$scope.extras.keys = "custom";
		
		original = angular.copy($scope.extras)
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.flavor = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canSubmit = ->
			return $scope.form.$valid

		$scope.submit = (extras) ->
			$modalInstance.close extras
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


		
		return # end of EditFlavorExtrasModalCtrl
])
