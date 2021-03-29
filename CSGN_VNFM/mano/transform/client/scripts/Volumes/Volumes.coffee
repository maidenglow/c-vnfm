'use strict'

angular.module('app.volumes', [])


.controller('VolumsCtrl', [
	'$scope'
	'$modal'
	'$http'
	'logger'
	'$timeout'
	'$q'
	'VolumesAPI'
	'VolumeExtendAPI'
	'VolumeAttachmentAPI'
	'VolumeSnapshotAPI'
	'TenantsAPI'
	'ComputeServersAPI'
	(
		$scope
		$modal
		$http
		logger
		$timeout
		$q
		VolumesAPI
		VolumeExtendAPI
		VolumeAttachmentAPI
		VolumeSnapshotAPI
		TenantsAPI
		ComputeServersAPI
	) ->


		STATUS_CHOICES =
			'in-use' : true
			'available' : true
			#'creating' : false
			'error' : false


		#========================================
		# Scope
		#========================================
		$scope.projects = []
		$scope.volumes

		########################
		# Volume
		########################
		$scope.volume

		$scope.selectItem = (volume) ->
			$scope.volume = volume

		$scope.isSelected = (volume) ->
			$scope.volume is volume

		########################
		# Volume Status Class
		########################
		$scope.itemStatusClass = (item) ->
			if item.status is 'available' || item.status is 'in-use'
				clazz = 'label label-success'
			else
			 	clazz = 'label label-danger'


		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getVolumes().$promise
				TenantsAPI.get().$promise
				getServers().$promise
			]).then((result) ->

				#### projects ####
				$scope.projects = result[1].tenants


				#### volumes ####
				volumes = $scope.volumes
				for volume in volumes
					lookupProjectName(volume)

				$scope.selectItem volumes[0] if volumes and volumes.length > 0

				for volume in volumes
					if $scope.isActionProgress(volume.status)
						refreshVolume(volume.id, volume.status)
			)



		#========================================
		# Functions
		#========================================

		########################
		# lookup Project Name
		########################
		lookupProjectName = (volume) ->
			for project in $scope.projects
				if project.id is volume['os-vol-tenant-attr:tenant_id']
					volume.project_name = project.name
			return

		########################
		# get Servers
		########################
		getServers = (server) ->
			ComputeServersAPI.query server,
				(response) ->
					$scope.servers = response
					return

		########################
		# get Volumes
		########################
		getVolumes = (volume) ->
			VolumesAPI.query volume,
				(response) ->
					$scope.volumes = response
					return
		########################
		# get Volume
		########################
		getVolume = (volume) ->
			VolumesAPI.get volume,
				(response) ->
					$scope.volume = response
					idx = findIndex(volume)
					if idx > -1
						refreshedVolume = response
						lookupProjectName refreshedVolume
						refreshedVolume["$$hashKey"] = $scope.volumes[idx]["$$hashKey"]
						$scope.volumes[idx] = refreshedVolume
					else
						refreshedVolume = response
						lookupProjectName refreshedVolume
						$scope.volumes.splice 0, 0, refreshedVolume
					return

		########################
		# create Volume
		########################
		createVolume = (volume) ->
			VolumesAPI.save volume,
				(response) ->
					logger.logSuccess "볼륨이 생성되었습니다."
					getVolume({ id: response.id })
						.$promise.then ->
							# Refresh Volume
							$timeout(->
								refreshVolume(response.id,response.status)
							, 1000)
					return

		########################
		# Update Volume
		########################
		updateVolume = (volume) ->
			VolumesAPI.update volume,
				(response) ->
					logger.logSuccess "볼륨이 수정되었습니다."
					$scope.volume.display_name = volume.display_name
					$scope.volume.display_description = volume.display_description
					return
			return

		########################
		# Update Volume
		########################
		updateVolumeSize = (volume) ->
			VolumeExtendAPI.save volume,
				(response) ->
					if response.success
						$scope.volume.size = volume.size
						logger.logSuccess "볼륨확장에 성공하였습니다."
					else
						logger.logError "볼륨확장에 실패하였습니다."
					return
			return



		########################
		# delete Volume
		########################
		deleteVolume = (volume) ->
			VolumesAPI.delete volume,
				(response) ->
					if response.success
						logger.log '삭제 되었습니다.'
						getVolume({ id: volume.id })
							.$promise.then ->
								# Refresh Volume
								$timeout(->
									refreshVolume(volume.id,volume.status)
								, 1000)
						#refreshVolume volume.id
						#idx = findIndex(volume)
						#$scope.volumes.splice idx, 1
						#$scope.selectItem $scope.volumes[0] if $scope.volumes.length > 0
					else
						logger.logError '삭제를 실패하였습니다. <br/>' + response.fault
					return

		########################
		# Refresh Volume
		########################
		refreshVolume = (id, status) ->
			VolumesAPI.get { 'id': id },
				(response) ->

					if(status == "deleting" && typeof response.id  is 'undefined')
						idx = findIndex({ 'id': id })
						if idx > -1
							$scope.volumes.splice idx, 1
							$scope.selectItem $scope.volumes[0] if $scope.volumes.length > 0


					if $scope.isActionProgress(response.status)
						idx = findIndex(response)
						if idx > -1
							$timeout(->
								refreshVolume(id,response.status)
							, 2500)
						else
							idx = findIndex({ 'id': id })
							if idx > -1
								$scope.volumes.splice idx, 1
								$scope.selectItem $scope.volumes[0] if $scope.volumes.length > 0
					else
						idx = findIndex(response)
						if idx > -1
							refreshedVolume = response
							lookupProjectName refreshedVolume
							refreshedVolume["$$hashKey"] = $scope.volumes[idx]["$$hashKey"]
							$scope.volumes[idx] = refreshedVolume
							$scope.selectItem $scope.volumes[idx]
					return

		########################
		# Find Index
		########################
		findIndex = (volume) ->
			for item, idx in $scope.volumes
				if item.id is volume.id
					return idx
			return -1

		########################
		# Find Index
		########################
		attachVolumeAttachment = (volumeAttachment) ->
			VolumeAttachmentAPI.save volumeAttachment,
				(response) ->
					logger.logSuccess "" + response.device + "의 인스턴스 " + volumeAttachment.serverName + "에 볼륨 " + volumeAttachment.volumeName + "를 연결합니다. "
					return

		########################
		# create Volume Snapshot
		########################
		createVolumeSnapshot = (snapshot) ->
			VolumeSnapshotAPI.save snapshot,
				(response) ->
					logger.logSuccess "볼륨 스냅샷이 생성되었습니다."
					return

		#========================================
		# $Scope
		#========================================
		$scope.isActionProgress = (status) ->
			if !status
				return false

			if(typeof STATUS_CHOICES[status.toLowerCase()] is 'undefined')
				return true;
			else
				return false;

		$scope.volumeStatusClass = (instance) ->
			if instance.status is 'available'
				clazz = 'label label-success'
			else
				clazz = 'label label-danger'

		$scope.getServerName = (serverId) ->
			if ($scope.servers)
				for item in $scope.servers
					console.log(serverId);
					console.log(item);
					if item.id is serverId
						return item.name
			return ""
		#========================================
		# Button Event
		#========================================

		########################
		# Create Volume Form
		########################
		$scope.createVolumeForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/volumes/create.html'
				controller: 'CreateVolumeModalCtrl'
				size: 'lg'
			)

			modalInstance.result.then(
				(volume) ->
					createVolume volume
					return
				, ->
					return
			)

			return # // end create Volume Form


		########################
		# Delete Volume Form
		########################
		$scope.deleteVolumeForm = (volume) ->
			modalInstance = $modal.open(
				templateUrl: "views/volumes/confirm.html"
				controller: ($scope, $modalInstance, volume) ->
					$scope.ok = ->
						$modalInstance.close volume

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					volume: ->
						volume
			)

			modalInstance.result.then(
				(volume) ->
					deleteVolume volume
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return

		########################
		# Edit Volume Basic Form
		########################
		$scope.editVolumeBasicForm = (volume) ->
			modalInstance = $modal.open(
				templateUrl: 'views/volumes/edit_basic.html'
				controller: 'EditVolumeBasicModalCtrl'
				size: 'lg'
				resolve:
					volume: ->
						volume
			)

			modalInstance.result.then(
				(volume) ->
					#console.log(volume)
					updateVolume volume
					return
				, ->
					return
			)

			return # // end edit Volume Form

		########################
		# Edit Volume Size Form
		########################
		$scope.editVolumeSizeForm = (volume) ->
			modalInstance = $modal.open(
				templateUrl: 'views/volumes/edit_size.html'
				controller: 'EditVolumeSizeModalCtrl'
				size: 'lg'
				resolve:
					volume: ->
						volume
			)

			modalInstance.result.then(
				(volume) ->
					updateVolumeSize volume
					return
				, ->
					return
			)

			return # // end edit Volume Form

		########################
		# Edit Volume Attach Form
		########################
		$scope.editVolumeAttachForm = (volume) ->
			modalInstance = $modal.open(
				templateUrl: 'views/volumes/edit_attach.html'
				controller: 'EditVolumeAttachModalCtrl'
				size: 'lg'
				resolve:
					volume: ->
						volume
			)

			modalInstance.result.then(
				(volumeAttachment) ->
					attachVolumeAttachment volumeAttachment
					return
				, ->
					return
			)

			return # // end edit Volume Form

		########################
		# Create Volume Snapshot Form
		########################
		$scope.createVolumeSnapshotForm = (volume) ->
			modalInstance = $modal.open(
				templateUrl: 'views/volumes/create_snapshot.html'
				controller: 'CreateVolumeSnapshotModalCtrl'
				size: 'lg'
				resolve:
					volume: ->
						volume
			)

			modalInstance.result.then(
				(snapshot) ->
					createVolumeSnapshot snapshot
					return
				, ->
					return
			)

			return # // end edit Volume Form

		return
])


########################
# Create Volume
########################
.controller('CreateVolumeModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'VolumesAPI'
	'VolumeTypesAPI'
	'ComputeImagesAPI'
	'ComputeAvailabilityZonesAPI'
	'VolumeLimitsAPI'
	(
		$scope
		$modalInstance
		$q
		VolumesAPI
		VolumeTypesAPI
		ComputeImagesAPI
		ComputeAvailabilityZonesAPI
		VolumeLimitsAPI
	) ->

		#========================================
		# Init Form
		#========================================

		# volume
		$scope.volume =
			display_name: null
			display_description: null
			volume_type: null
			size: null
			volume: null
			source_volid : null
			image: null
			imageRef : null
			source: null
			availability_zone: null

		original = angular.copy($scope.volume)

		#### Limit ###
		$scope.limits =
			gigabytes: null
			counts: null


		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.volume = angular.copy(original)
			$scope.calVolumeGBytesProgress()
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.volume, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.volume, original)

		$scope.submit = (instance) ->
			$modalInstance.close instance
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
				VolumeTypesAPI.query().$promise
				VolumesAPI.query().$promise
				ComputeImagesAPI.query().$promise
				ComputeAvailabilityZonesAPI.query().$promise
				VolumeLimitsAPI.get().$promise
			]).then((result) ->

				#### Volume Types ####
				volumeTypes = result[0]
				$scope.volumeTypes = volumeTypes
				## $scope.volume.type = volumeTypes[0] if volumeTypes.length > 0

				#### Volume ####
				volumes = result[1]
				$scope.volumes = volumes
				$scope.volume.volume = volumes[0] if volumes.length > 0


				#### Images  ####
				images = result[2]
				$scope.images = images
				$scope.volume.image = images[0] if images.length > 0


				#### Availability Zone ####
				availability_zones = result[3]
				$scope.availability_zones = availability_zones

				#### Sources ####
				$scope.sources = [
					{
						type : 'empty',
						name : '소스없음, 빈 볼륨'
					},
					{
						type : 'image',
						name : '이미지'
					},
					{
						type : 'volume',
						name : '볼륨'
					}
				]
				$scope.volume.source = $scope.sources[0] if $scope.sources.length > 0

				limits = result[4]
				$scope.limits.gigabytes = {
					max : limits.maxTotalVolumeGigabytes
					used : limits.totalGigabytesUsed
				}

				$scope.limits.counts = {
					max : limits.maxTotalVolumes
					used : limits.totalVolumesUsed
				}



				$scope.allProgress()
			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================
		$scope.changeVolumeType = ->
			$scope.volume.volume_type = $scope.volume.type.name
			return

		$scope.setVolumeSize = (size) ->
			if !$scope.volume.size
				$scope.volume.size = size
				$scope.calVolumeGBytesProgress()
			if $scope.volume.size < size
				$scope.volume.size = size
				$scope.calVolumeGBytesProgress()
			return

		$scope.changeSource = ->
			$scope.volume.source_volid = null;
			$scope.volume.imageRef = null

			if $scope.volume.source.type == "image"
				imageSize = $scope.volume.image["OS-EXT-IMG-SIZE:size"] / 1024 / 1024 / 1024
				if imageSize < 1
					$scope.setVolumeSize(1)
				else
					$scope.setVolumeSize(Math.round(imageSize + 0.5))
				$scope.volume.imageRef = $scope.volume.image.id

			else if $scope.volume.source.type == "volume"
				$scope.setVolumeSize($scope.volume.volume.size)
				$scope.volume.source_volid = $scope.volume.volume.id


			return

		$scope.changeImage = ->
			imageSize = $scope.volume.image["OS-EXT-IMG-SIZE:size"] / 1024 / 1024 / 1024
			if imageSize < 1
				$scope.setVolumeSize(1)
			else
				$scope.setVolumeSize(Math.round(imageSize + 0.5))
			$scope.volume.imageRef = $scope.volume.image.id
			return

		$scope.changeVolume = ->
			$scope.setVolumeSize($scope.volume.volume.size)
			$scope.volume.source_volid = $scope.volume.volume.id
			return

		$scope.changeAvailabilityZone = ->
			$scope.volume.availability_zone = $scope.volume.availability_zone_select.zoneName

			return

		#### Percentage ####
		percentage = (max, used) -> Math.round((used / max) * 100)

		# Calculrator Progress
		$scope.calProgress = (max, used, add) ->
			progressbars = []

			# 퍼센트 계산
			per = percentage max, used

			# 기본 프로그레스바
			progressbars.push({
				value: per
				type: 'success'
			})

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
			$scope.calVolumeGBytesProgress()
			$scope.calVolumeUsedCountsProgress()
			return

		# calculrate volume bytes progress
		$scope.calVolumeGBytesProgress = ->
			max = $scope.limits.gigabytes.max
			used = $scope.limits.gigabytes.used
			add = parseInt($scope.volume.size)

			progress = $scope.calProgress(max, used, add)


			if $scope.volumeUsedGBytesStacked
				$scope.volumeUsedGBytesStacked[1].value = progress[1].value
				$scope.volumeUsedGBytesStacked[1].type = progress[1].type
			else
				$scope.volumeUsedGBytesStacked = progress

			return

		# calculrate volume count progress
		$scope.calVolumeUsedCountsProgress = ->
			max = $scope.limits.counts.max
			used = $scope.limits.counts.used
			add = 1

			progress = $scope.calProgress(max, used, add)

			if $scope.volumeUsedCountsStacked
				$scope.volumeUsedCountsStacked[1].value = progress[1].value
				$scope.volumeUsedCountsStacked[1].type = progress[1].type
			else
				$scope.volumeUsedCountsStacked = progress

			return



		return # end of createVolumeModalCtrl
])







########################
# Edit Volume Basic Info
########################
.controller('EditVolumeBasicModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'volume'
	(
		$scope
		$modalInstance
		$q
		volume
	) ->

		#========================================
		# Init Form
		#========================================

		# volume
		$scope.volume =
			id: volume.id
			display_name: volume.display_name
			display_description: volume.display_description


		original = angular.copy($scope.volume)



		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.volume = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.volume, original)

		$scope.submit = (volume) ->
			$modalInstance.close volume
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



		return # end of editVolumeBasicModalCtrl
])





########################
# Edit Volume Size Info
########################
.controller('EditVolumeSizeModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'volume'
	'VolumeLimitsAPI'
	(
		$scope
		$modalInstance
		$q
		volume
		VolumeLimitsAPI
	) ->

		#========================================
		# Init Form
		#========================================

		# volume
		$scope.volume =
			id: volume.id
			display_name: volume.display_name

		$scope.min_volume_size = volume.size

		original = angular.copy($scope.volume)

		#### Limit ###
		$scope.limits =
			gigabytes: null
			counts: null


		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.volume = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.volume, original)

		$scope.submit = (instance) ->
			$modalInstance.close instance
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
			#$scope.form.size = volume.size
			return $q.all([
				VolumeLimitsAPI.get().$promise
			]).then((result) ->

				limits = result[0]
				$scope.limits.gigabytes = {
					max : limits.maxTotalVolumeGigabytes
					used : limits.totalGigabytesUsed
				}

				$scope.limits.counts = {
					max : limits.maxTotalVolumes
					used : limits.totalVolumesUsed
				}

				$scope.allProgress()
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
			progressbars.push({
				value: per
				type: 'success'
			})

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
			$scope.calVolumeGBytesProgress()
			return

		# calculrate volume bytes progress
		$scope.calVolumeGBytesProgress = ->
			max = $scope.limits.gigabytes.max
			used = $scope.limits.gigabytes.used
			add = parseInt($scope.volume.size)

			progress = $scope.calProgress(max, used, add)


			if $scope.volumeUsedGBytesStacked
				$scope.volumeUsedGBytesStacked[1].value = progress[1].value
				$scope.volumeUsedGBytesStacked[1].type = progress[1].type
			else
				$scope.volumeUsedGBytesStacked = progress

			return


		return # end of editVolumeSizeModalCtrl
])



########################
# Edit Volume Attach
########################
.controller('EditVolumeAttachModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'volume'
	'ComputeServersAPI'
	(
		$scope
		$modalInstance
		$q
		volume
		ComputeServersAPI
	) ->

		#========================================
		# Init Form
		#========================================
		$scope.volumeAttachment =
			instance : null
			serverId : null
			serverName : null
			volumeId : volume.id
			volumeName : volume.display_name
			device : ""

		# volume
		$scope.volume = volume


		original = angular.copy($scope.volumeattachment)



		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.volumeattachment = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.volumeAttachment, original)

		$scope.submit = (volumeAttachment) ->
			$modalInstance.close volumeAttachment
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
				ComputeServersAPI.query().$promise
			]).then((result) ->

				#### instance ####
				instances = result[0]
				$scope.instances = instances

				console.log(instances);

			)

		$scope.init()

		#========================================
		# Form Functions
		#========================================

		$scope.changeInstance = ->
			if $scope.volumeAttachment.instance
				$scope.volumeAttachment.serverId = $scope.volumeAttachment.instance.id
				$scope.volumeAttachment.serverName = $scope.volumeAttachment.instance.name
			else
				$scope.volumeAttachment.serverId = null
				$scope.volumeAttachment.serverName = null
			return

		$scope.getServerName = (serverId) ->
			if ($scope.instances)
				for item in $scope.instances
					console.log(serverId);
					console.log(item);
					if item.id is serverId
						return item.name
			return ""

		return # end of editVolumeAttachModalCtrl
])



########################
# Create Volume Snapshot
########################
.controller('CreateVolumeSnapshotModalCtrl', [
	'$scope'
	'$modalInstance'
	'$q'
	'volume'
	'VolumeLimitsAPI'
	(
		$scope
		$modalInstance
		$q
		volume
		VolumeLimitsAPI
	) ->

		#========================================
		# Init Form
		#========================================

		# volume
		$scope.volume = volume

		# snapshot
		$scope.snapshot =
			display_name: null
			display_description: null
			volume_id: volume.id


		original = angular.copy($scope.snapshot)

		#### Limit ###
		$scope.limits =
			gigabytes: null
			counts: null


		#========================================
		# Form Buttons
		#========================================
		$scope.revert = ->
			$scope.snapshot = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.snapshot, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.volume, original)

		$scope.submit = (snapshot) ->
			$modalInstance.close snapshot
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
				VolumeLimitsAPI.get().$promise
			]).then((result) ->

				limits = result[0]
				$scope.limits.gigabytes = {
					max : limits.maxTotalVolumeGigabytes
					used : limits.totalGigabytesUsed
				}

				$scope.limits.counts = {
					max : limits.maxTotalVolumes
					used : limits.totalVolumesUsed
				}



				$scope.allProgress()
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
			progressbars.push({
				value: per
				type: 'success'
			})

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
			$scope.calVolumeGBytesProgress()
			$scope.calVolumeUsedCountsProgress()
			return

		# calculrate volume bytes progress
		$scope.calVolumeGBytesProgress = ->
			max = $scope.limits.gigabytes.max
			used = $scope.limits.gigabytes.used
			add = 0

			progress = $scope.calProgress(max, used, add)


			if $scope.volumeUsedGBytesStacked
				$scope.volumeUsedGBytesStacked[1].value = progress[1].value
				$scope.volumeUsedGBytesStacked[1].type = progress[1].type
			else
				$scope.volumeUsedGBytesStacked = progress

			return

		# calculrate volume count progress
		$scope.calVolumeUsedCountsProgress = ->
			max = $scope.limits.counts.max
			used = $scope.limits.counts.used
			add = 1

			progress = $scope.calProgress(max, used, add)

			if $scope.volumeUsedCountsStacked
				$scope.volumeUsedCountsStacked[1].value = progress[1].value
				$scope.volumeUsedCountsStacked[1].type = progress[1].type
			else
				$scope.volumeUsedCountsStacked = progress

			return



		return # end of createVolumeSnapshotModalCtrl
])