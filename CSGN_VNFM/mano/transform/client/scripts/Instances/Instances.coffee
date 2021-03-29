'use strict'

angular.module('app.instances', [])

.controller('InstancesCtrl', [
	'$scope'
	'$modal'
	'logger'
	'$q'
	'$timeout'
	'$location'
	'ComputeServersAPI'
	'ComputeServerActionsAPI'
	'ComputeFlavorsAPI'
	'ComputeImagesAPI'
	'ComputeLimitAPI'
	'NetworkingFloatingIpsAPI'
	'TelemetryInstancesAPI'
	(
		$scope
		$modal
		logger
		$q
		$timeout
		$location
		ComputeServersAPI
		ComputeServerActionsAPI
		ComputeFlavorsAPI
		ComputeImagesAPI
		ComputeLimitAPI
		NetworkingFloatingIpsAPI
		TelemetryInstancesAPI
	) ->

		STATUS_CHOICES = 
			'active' : true
			'shutoff' : true
			'suspended' : true
			'paused' : true
			'error' : false
			
		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getInstances().$promise
				#ComputeLimitAPI.get().$promise
				# getLimit().$promise
			]).then((result) ->

				#### instances ####
				instances = $scope.instances
				$scope.selectInstance instances[0] if instances and instances.length > 0
				
				
				for instance in instances
					if $scope.isActionProgress(instance.status)
						refreshInstance instance.id
						
				# get telemetry instance
				getTelemetryInstance()
			)

		#========================================
		# Functions
		#========================================

		########################
		# get Instances
		########################
		getInstances = (instance) ->
			ComputeServersAPI.query instance,
				(response) ->
					$scope.instances = response
					return

		########################
		# get Instance
		########################
		getInstance = (instance) ->
			ComputeServersAPI.get instance,
				(response) ->
					$scope.instance = response
					idx = findIndex(instance)
					if idx > -1
						refreshedInstance = response
						refreshedInstance["$$hashKey"] = $scope.instances[idx]["$$hashKey"]
						$scope.instances[idx] = refreshedInstance
					else
						$scope.instances.splice 0, 0, response
					return

		########################
		# create Instance
		########################
		createInstance = (instance) ->
			ComputeServersAPI.save instance,
				(response) ->
					logger.logSuccess "인스턴스가 생성되었습니다."
					getInstance({ id: response.id })
						.$promise.then ->
							# Refresh Instance
							$timeout(->
								refreshInstance(response.id)
							, 1000)
					return

		########################
		# delete Instance
		########################
		deleteInstance = (instance) ->
			ComputeServersAPI.delete instance,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						idx = findIndex(instance)
						$scope.instances.splice idx, 1
						$scope.selectInstance $scope.instances[0] if $scope.instances.length > 0
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
					return

		########################
		# Refresh Instance	
		########################
		refreshInstance = (id) ->
			ComputeServersAPI.get { 'id': id },
				(response) ->
					#if response.status is 'BUILD'
					if $scope.isActionProgress(response.status)
						$timeout(->
							refreshInstance(id)
						, 2500)
					else
						idx = findIndex(response)
						
						if idx > -1
							refreshedInstance = response
							refreshedInstance["$$hashKey"] = $scope.instances[idx]["$$hashKey"]
							$scope.instances[idx] = refreshedInstance
							$scope.selectInstance $scope.instances[idx]
					return

		########################
		# Find Index
		########################
		findIndex = (instance) ->
			for item, idx in $scope.instances
				if item.id is instance.id
					return idx
			return -1
			
		########################
		# Create Floating Ip
		########################
		createFloatingIp = (floatingip,instance) ->
			NetworkingFloatingIpsAPI.save floatingip,
				(response) ->
					if response.success
						logger.log '유동 아이피 연결에 성공하였습니다.'
						idx = findIndex(instance)	
						if idx > -1	
							if $scope.instances[idx].addresses && $scope.instances[idx].addresses.addresses
								for key, list of $scope.instances[idx].addresses.addresses
									item = 
										'addr' : response.data.floating_ip_address
										'OS-EXT-IPS-MAC:mac_addr' : ''
										'OS-EXT-IPS:type' : 'floating'
										'version' : ''
									list.push item
										
					else
						logger.logError '유동 아이피 연결에 실패하였습니다. ' + response.message
					return
					
		########################
		# Delete Floating Ip
		########################
		deleteFloatingIp = (instance) ->
			NetworkingFloatingIpsAPI.delete {
					'id' : instance.id
				},
				(response) ->
					if response.success
						logger.log '유동 아이피 연결해제에 성공하였습니다.'
						idx = findIndex(instance)	
						if idx > -1	
							if $scope.instances[idx].addresses && $scope.instances[idx].addresses.addresses
								for key, list of $scope.instances[idx].addresses.addresses
									for itm, itmIdx in list
										if response.data.floating_ip_address == itm.addr
											$scope.instances[idx].addresses.addresses[key].splice itmIdx, 1
					else
						logger.logError '유동 아이피 연결해제에 실패하였습니다. ' + response.message
					return

		########################
		# get Telemetry Instance
		########################
		promise = null
		getTelemetryInstance = ->
			console.log 'getTelemetryInstance!!!!'

			$timeout.cancel promise

			if $scope.instance
				TelemetryInstancesAPI.get { 'instanceId': $scope.instance.id },
					(response) ->
						telemetryInstance = response
						$scope.telemetryInstance = telemetryInstance
						$scope.instanceUsedCpuPer = telemetryInstance.usedCpuPer
						$scope.instanceUsedMemoryPer = telemetryInstance.usedMemoryPer
						return

			if '/instances/index' is $location.path()
				promise = $timeout getTelemetryInstance, 5000

			return

		#========================================
		# $Scope
		#========================================

		########################
		# Instances
		########################
		$scope.instances

		$scope.isActionProgress = (status) ->
			if !status
				return false 
				 
			if(typeof STATUS_CHOICES[status.toLowerCase()] is 'undefined')
				return true;
			else
				return false;
				
		$scope.instanceStatusClass = (instance) ->
			if instance.status is 'ACTIVE'
				clazz = 'label label-success'
			else
				clazz = 'label label-danger'

		########################
		# Instance
		########################
		$scope.instance

		$scope.selectInstance = (instance) ->
			$scope.instance = instance

		$scope.isSelected = (instance) ->
			$scope.instance is instance
			
		########################
		# Telemetry Instance
		########################
		$scope.telemetryInstance
		
		$scope.instanceUsedCpuPer = 0
		$scope.instanceUsedMemoryPer = 0

		#========================================
		# Button Event
		#========================================

		########################
		# Create Instance Form
		########################
		$scope.createInstanceForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/instances/create.html'
				controller: 'CreateInstanceModalCtrl'
				size: 'lg'
			)

			modalInstance.result.then(
				(instance) ->
					createInstance instance
					return
				, ->
					return
			)

			return # // end create Instance Form

		########################
		# Delete Instance
		########################
		$scope.deleteInstance = (instance) ->
			modalInstance = $modal.open(
				templateUrl: 'views/instances/confirm.html'
				controller: ($scope, $modalInstance, instance) ->
					$scope.ok = ->
						$modalInstance.close instance
						return

					$scope.cancel = ->
						$modalInstance.dismiss 'Cancel'
						return

					return
				resolve:
					instance: ->
						$scope.instance
			)

			modalInstance.result.then(
				(instance) ->
					deleteInstance instance
					return
				, ->
					return
			)

			return # end deleteInstance

		########################
		# Instance Action
		########################
		$scope.instanceAction = (action, instance) ->
			ComputeServerActionsAPI.action { 'action': action },
				instance
				(response) ->
					if response.success
						logger.log '정상처리 되었습니다.'
						getInstance instance
						return
					else
						logger.logError '처리를 실패하였습니다. ' + response.fault
						return
						
						
		########################
		# Create Floating IP Form
		########################
		$scope.createFloatingIPForm = (instance) ->
			modalInstance = $modal.open(
				templateUrl: 'views/instances/create_floatingips.html'
				controller: 'CreateFloatingIPModalCtrl'
				resolve:
					instance: ->
						instance
			)

			modalInstance.result.then(
				(floatingip) ->
					createFloatingIp(floatingip,instance)
					return
				, ->
					return
			)

			return # // end  ceate Floating IP Form
			
		########################
		# Delete FloatingIP Form
		########################
		$scope.deleteFloatingIPForm = (instance) ->
			
			modalInstance = $modal.open(
				templateUrl: "views/instances/disassociate.html"
				controller: ($scope, $modalInstance, instance) ->
					instance_name = if instance.name then instance.name else instance.id.substring(0,8)
					$scope.msg = instance_name
					
					$scope.ok = -> 
						$modalInstance.close instance

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					instance: ->
						instance
			)
			
			modalInstance.result.then(
				(instance) ->
					deleteFloatingIp instance
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return
			
		#========================================
		# Scope Event
		#========================================
		
		########################
		# $watch instance
		########################
		$scope.$watch 'instance', (newVal, oldVal) ->
			console.log '$scope.$watch instance!!!'
			if newVal and newVal isnt oldVal
				console.log 'instance is changed!!!!!!!!!', $scope.instance

		########################
		# $destroy
		########################
		$scope.$on '$destroy', ->
			$timeout.cancel promise

		return	# end of InstancesCtrl
])

########################
# Create Instance
########################
.controller('CreateInstanceModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'ComputeLimitAPI'
	'ComputeFlavorsAPI'
	'ComputeImagesAPI'
	'ComputeAvailabilityZonesAPI'
	'InstanceLimitService'
	'NetworkingNetworkAPI'
	(
		$scope
		$modalInstance
		$q
		ComputeLimitAPI
		ComputeFlavorsAPI
		ComputeImagesAPI
		ComputeAvailabilityZonesAPI
		InstanceLimitService
		NetworkingNetworkAPI
	) ->

		#========================================
		# Init Form
		#========================================
		$scope.selected_networks = []
		$scope.networks = []
        
		# instance
		$scope.instance =
			name: null
			availability_zone: null
			flavor: null
			image: null
			networks : []

		original = angular.copy($scope.instance)

		########################
		# Limit
		########################
		getLimit = (limit) ->
			ComputeLimitAPI.get limit,
				(response) ->
					$scope.limit = response
					InstanceLimitService.init response
					$scope.limit = InstanceLimitService.limit
					$scope.instances = InstanceLimitService.instances
					$scope.vcpus = InstanceLimitService.vcpus
					$scope.ram = InstanceLimitService.ram

		getLimit()

		#========================================
		# Form Buttons
		#========================================

		$scope.revert = ->
			$scope.instance = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.instance, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.instance, original) && $scope.selected_networks.length > 0

		$scope.submit = (instance) ->
			if instance.flavor
				instance.flavorRef = instance.flavor.id
				
			if instance.image
				instance.imageRef = instance.image.id
			
			instance.key_name = "keys"
				
			if instance.availability_zone
				instance.availability_zone = instance.availability_zone.id
				
			instance.networks =  ({uuid : network.id} for network in $scope.selected_networks)
			
				
			$modalInstance.close instance
			$scope.revert()
			return

		$scope.cancel = ->
			$modalInstance.dismiss 'cancel'
			return

		#========================================
		# Form Event
		#========================================
		$scope.changeFlavor = ->
			$scope.allProgress()
			return

		#========================================
		# Form Supporters
		#========================================

		# ---- init ----
		$scope.init = ->
			return $q.all([
				ComputeFlavorsAPI.query().$promise
				ComputeImagesAPI.query().$promise
				ComputeAvailabilityZonesAPI.query().$promise
				NetworkingNetworkAPI.query().$promise
			]).then((result) ->
			
				#### Flavors ####
				flavors = result[0]
				$scope.flavors = flavors
				$scope.instance.flavor = flavors[0] if flavors.length > 0

				#### Images ####
				images = result[1]
				$scope.images = images
				$scope.instance.image = images[0] if images.length > 0

				#### Availability Zone ####
				availability_zones = result[2]
				$scope.availability_zones = availability_zones
				$scope.availability_zone = availability_zones[0].zoneName if availability_zones.length > 0
				
				#### Networks ####
				networks = result[3]
				$scope.networks = networks

				$scope.allProgress()
			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================
		
		$scope.removeNetworkItem = (scope) ->
			$scope.networks.push scope
			for item, idx in $scope.selected_networks
				if item.id == scope.id
					$scope.selected_networks.splice(idx,1)
					break;
			return
            
		$scope.addNetworkItem = (scope) ->
			$scope.selected_networks.push scope
			for item, idx in $scope.networks
				if item.id == scope.id
					$scope.networks.splice(idx,1)
					break;
			return

		#### Percentage ####
		percentage = (max, used) -> Math.round((used / max) * 100)

		# Calculrator Progress
		$scope.calProgress = (max, used, add) ->
			progressbars = []

			# 퍼센트 계산
			per = percentage max, used

			# 기본 프로그레스바
			progressbars.push({ value: per, type: 'success' })

			# 추가 퍼센트를 계산
			# ※ 100% 값 초과를 확인하여 위하여 기존값을 이용한 연산이 추가된다.
			addPer = percentage max, (used + add)

			addType = 'info'
			if addPer > 70 and addPer <= 90
				addType = 'waring'
			else if addPer > 90
				addType = 'danger'

			if addPer > 100
				addPer = 100

			addPer -= per

			# 추가 프로그레스바
			progressbars.push({
				value: addPer
				type: addType
			})

			return progressbars

		#========================================
		# Progressbar
		#========================================

		# get All progress
		$scope.allProgress = ->
			$scope.calInstanceProgress()
			$scope.calVcpusProgress()
			$scope.calRamsProgress()
			return

		# calculrate instance progress
		$scope.calInstanceProgress = ->
			max = $scope.instances.max
			used = $scope.instances.used
			add = 1

			progress = $scope.calProgress(max, used, add)

			if $scope.instanceStacked
				$scope.instanceStacked[1].value = progress[1].value
				$scope.instanceStacked[1].type = progress[1].type
			else
				$scope.instanceStacked = progress

			return

		# calculrate vcpus progress
		$scope.calVcpusProgress = ->
			max = $scope.vcpus.max
			used = $scope.vcpus.used
			add = $scope.instance.flavor.vcpus

			progress = $scope.calProgress(max, used, add)

			if $scope.vcpusStacked
				$scope.vcpusStacked[1].value = progress[1].value
				$scope.vcpusStacked[1].type = progress[1].type
			else
				$scope.vcpusStacked = progress

			return

		# calculrate rams progress
		$scope.calRamsProgress = ->
			max = $scope.ram.max
			used = $scope.ram.used
			add = $scope.instance.flavor.ram

			progress = $scope.calProgress(max, used, add)

			if $scope.ramsStacked
				$scope.ramsStacked[1].value = progress[1].value
				$scope.ramsStacked[1].type = progress[1].type
			else
				$scope.ramsStacked = progress

			return
			
			
		return # end of createInstanceModalCtrl
])





########################
# Create Floating IP Form
########################
.controller('CreateFloatingIPModalCtrl', [
	'$scope'
	'$modalInstance'
	'$modal'
	'$q'
	'logger'
	'NetworkingFloatingIpsAPI'
	'ComputeFloatingIpPoolsAPI'
	'ComputeServersAPI'
	'NetworkingPortAPI'
	'instance'
	(
		$scope
		$modalInstance
		$modal
		$q
		logger
		NetworkingFloatingIpsAPI
		ComputeFloatingIpPoolsAPI
		ComputeServersAPI
		NetworkingPortAPI
		instance
	) ->
	
		#========================================
		# Init Form
		#========================================

		$scope.floatingIps = []
		$scope.instances = []
		
		# network
		$scope.floatingip = 
			ipAddress : null
			port : null
			port_id : null
			id : null
		
		original = angular.copy($scope.floatingip)
		
		
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

		$scope.submit = (floatingip) ->
			floatingip.id = floatingip.ipAddress.id
			floatingip.port_id = floatingip.port.id
			floatingip.ipAddress = null
			floatingip.port = null
			$modalInstance.close floatingip
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
				NetworkingFloatingIpsAPI.query().$promise
				ComputeServersAPI.query().$promise
				NetworkingPortAPI.query().$promise
			]).then((result) ->
			
				floatingIps = result[0];
				$scope.floatingIps = ( itm for itm in floatingIps when itm.port_id == null) ;
				
				servers = result[1];
				ports = result[2];
				
				for server in servers
					for port in ports
						if server.id == port.device_id
							tmpInstance = 
								id : port.id
								name : server.name + " : " + port.fixed_ips[0].ip_address 
							$scope.instances.push tmpInstance
							
							if instance.id == server.id
								$scope.floatingip.port = tmpInstance
			)
							
		$scope.init()
		
		
		#========================================
		# Functions
		#========================================
		
		
		########################
		# get Instances
		########################
		getFloatingIps = (instance) ->
			NetworkingFloatingIpsAPI.query instance,
				(response) ->
					floatingIps = response;
					$scope.floatingIps = ( itm for itm in floatingIps when itm.port_id == null) ;
					return
					
		########################
		# allocate Pool
		########################
		allocatePool = (poolName) ->
			ComputeFloatingIpPoolsAPI.save {
					'poolName' : poolName
				},
				(response) ->
					if response.success
						logger.log '유동 아이피(' + response.data.ip + ')가 할당되었습니다.'
						getFloatingIps();
					else
						logger.logError '유동 아이피가 할당에 실패하였습니다. ' + response.message
					return
		
		#========================================
		# Form Functions
		#========================================
		########################
		# Manage Floating Ips Pool Form
		########################
		$scope.createFloatingIpPoolForm = () ->
			modalInstance = $modal.open(
				templateUrl: 'views/instances/manage_floatingips_pool.html'
				controller: 'CreateFloatingIpPoolModalCtrl'
			)

			modalInstance.result.then(
				(poolName) ->
					allocatePool poolName
					return
				, ->
					return
			)

			return # // end  createFloatingIpPoolForm
		
		return # end of CreateFloatingIPModalCtrl
])





########################
# Create Floating IP Pool Form
########################
.controller('CreateFloatingIpPoolModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'ComputeFloatingIpPoolsAPI'
	'NetworkingQuotasAPI'
	(
		$scope
		$modalInstance
		$q
		ComputeFloatingIpPoolsAPI
		NetworkingQuotasAPI
	) ->
	
		#========================================
		# Init Form
		#========================================

		$scope.pools = []
		$scope.pool = null
		$scope.quotas = null
		original = angular.copy($scope.pool)
		
		
		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.pool = angular.copy(original)
			$scope.form.$setPristine()
			return
			
		$scope.canRevert = ->
			return !angular.equals($scope.pool, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.pool, original)

		$scope.submit = (pool) ->
			$modalInstance.close pool
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
				ComputeFloatingIpPoolsAPI.query().$promise
				NetworkingQuotasAPI.get().$promise
			]).then((result) ->
			
				pools = result[0];
				$scope.pools = pools
				$scope.pool = $scope.pools[0] if $scope.pools.length > 0		
				
				$scope.quotas = result[1];	
				
				$scope.allProgress();
			)
							
		$scope.init()
		
		#========================================
		# Form Functions
		#========================================
	
		#### Percentage ####
		percentage = (max, used) -> Math.round((used / max) * 100)

		# Calculrator Progress
		$scope.calProgress = (max, used, add) ->
			progressbars = []

			# 퍼센트 계산
			per = percentage max, used

			# 기본 프로그레스바
			progressbars.push({ value: per, type: 'success' })

			# 추가 퍼센트를 계산
			# ※ 100% 값 초과를 확인하여 위하여 기존값을 이용한 연산이 추가된다.
			addPer = percentage max, (used + add)

			addType = 'info'
			if addPer > 70 and addPer <= 90
				addType = 'waring'
			else if addPer > 90
				addType = 'danger'

			if addPer > 100
				addPer = 100

			addPer -= per

			# 추가 프로그레스바
			progressbars.push({
				value: addPer
				type: addType
			})

			return progressbars

		#========================================
		# Progressbar
		#========================================

		# get All progress
		$scope.allProgress = ->
			$scope.calNetQuotasProgress()
			return

		# calculrate NetQuotas progress
		$scope.calNetQuotasProgress = ->
			if $scope.quotas == null
				return
				
			max = $scope.quotas.floatingip
			used = $scope.quotas.floatingip_used
			add = 1

			progress = $scope.calProgress(max, used, add)

			if $scope.quotasStacked
				$scope.quotasStacked[1].value = progress[1].value
				$scope.quotasStacked[1].type = progress[1].type
			else
				$scope.quotasStacked = progress

			return
		
		return # end of CreateFloatingIPPoolModalCtrl
])
