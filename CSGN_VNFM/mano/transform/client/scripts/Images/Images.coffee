'use strict'

angular.module('app.images', ['angularFileUpload'])

.controller('ImagesController', [
	'$scope'
	'$modal'
	'logger'
	'$upload'
	'ImageImagesAPI'
	'CONST_RESTFUL_API'
	($scope, $modal, logger, $upload, ImageImagesAPI, CONST_RESTFUL_API) ->

		STATUS_CHOICES = 
			'active' : true
			'saving' : null
			'queued' : null
			'pending_delete' : null
			'killed' : false
			'deleted' : false
			
		#========================================
		# Functions
		#========================================

		########################
		# get Images
		########################
		getImages = (params) ->
			ImageImagesAPI.query params,
				(response) ->
					$scope.images = response
					return

		########################
		# get Image
		########################
		getImage = (image) ->
			ImageImagesAPI.get image,
				(response) ->
					$scope.image = response
					return

		########################
		# Create Image
		########################
		createImage = (image) ->
	
			
			modalInstance = $modal.open(
				backdrop : 'static'
				templateUrl: "views/images/uploading.html"
				controller: ($scope, $modalInstance, $upload, image) ->
					$scope.uplodingProgressPercent = "0"
					$scope.upload = $upload.upload({
						url: CONST_RESTFUL_API.IMAGE_URL.IMAGES_UPLOAD
						method: 'POST'
						file: image.file
						fileFormDataName: 'file'
						data: image
						sendDataAsJson: true,
					})
					.progress((evt) ->
						$scope.uplodingProgressPercent = parseInt(100.0 * evt.loaded / evt.total) + "";
						console.log('progress: ' + parseInt(100.0 * evt.loaded / evt.total) + '% file :'+ evt.config.file.name)
					)
					.success((data, status, headers, config)->
						
						console.log "success Ok"
						$modalInstance.close data
						
						return
					)


					return
				resolve:
					image: ->
						image
			)
			
			
			modalInstance.result.then(
				(data) ->
					console.log "Result Ok"
					logger.logSuccess "이미지가 생성되었습니다."
					$scope.image = data
					$scope.images.splice 0, 0, data
					
					if $scope.isActionProgress(data.status)
						refreshImage data.id
					return
				, ->
					console.log "Result Cancel"
					return
			)

			
			return

		########################
		# Update Image
		########################
		updateImage = (image) ->
			ImageImagesAPI.update image,
				(response) ->
					logger.logSuccess "이미지가 수정되었습니다."
					$scope.image = response
					return
			return

		########################
		# Delete Image
		########################
		deleteImage = (image) ->
			ImageImagesAPI.delete image,
				(response) ->
					if response.success
						logger.logSuccess "삭제되었습니다."
						$scope.images.splice(findIndex(image), 1)
						$scope.selectImage $scope.images[0] if $scope.images.length > 0
						return
					else
						logger.logError '삭제를 실패하였습니다. ' + response.fault
						return
					return

		########################
		# Refresh Image	
		########################
		refreshImage = (id) ->
			ImageImagesAPI.get { 'id': id },
				(response) ->
					if $scope.isActionProgress(response.status) == true
						$timeout(->
							refreshInstance(id)
						, 2500)
					else
						idx = findIndex(response)
						
						if idx > -1
							refreshedImage = response
							refreshedImage["$$hashKey"] = $scope.images[idx]["$$hashKey"]
							$scope.images[idx] = refreshedImage
							$scope.image = $scope.images[idx]
					return
					
		########################
		# Find Index
		########################
		findIndex = (image) ->
			for item, idx in $scope.images
				if item.id is image.id
					return idx
			return -1

		#========================================
		# Scope
		#========================================

		########################
		# Images
		########################
		$scope.images = []

		getImages().$promise.then ->
			if $scope.images and $scope.images.length > 0
				$scope.image = $scope.images[0]
				
			for image in $scope.images
					if $scope.isActionProgress(image.status)
						refreshImage image.id


		########################
		# isActionProgress
		########################
		$scope.isActionProgress = (status) ->
			if !status
				return false 
				 
			if(typeof STATUS_CHOICES[status.toLowerCase()] is 'undefined' || STATUS_CHOICES[status.toLowerCase()]  == null)
				return true;
			else
				return false;
				
		########################
		# Image Status Class
		########################
		$scope.imageStatusClass = (image) ->
			if image.status is 'active'
				clazz = 'label label-success'
			else
			 	clazz = 'label label-danger'

		########################
		# Image
		########################
		$scope.selectImage = (image) ->
			#getImage(image)
			$scope.image = image
			return

		$scope.isSelected = (image) ->
			$scope.image is image

		########################
		# Create Image Form
		########################
		$scope.createImageForm = ->
			modalInstance = $modal.open(
				templateUrl: "views/images/create.html"
				controller: 'CreateImageModalCtrl'
			)

			modalInstance.result.then (
				(image) ->
					createImage image
					return
				), ->
					console.log "Modal dismissed at : " + new Date()

				return

			return

		########################
		# Update Image Form
		########################
		$scope.updateImageForm = (image) ->
			modalInstance = $modal.open(
				templateUrl: "views/images/update.html"
				controller: ($scope, $modalInstance, image) ->
					$scope.image = image

					$scope.submit = ->
						$modalInstance.close image

					$scope.cancel = ->
						$modalInstance.dismiss 'cancel'

				resolve:
					image: ->
						image
			)

			modalInstance.result.then(
				(image) ->
					updateImage image
					return
			)

			return

		########################
		# Delete Image Form
		########################
		$scope.deleteImageForm = (image) ->
			modalInstance = $modal.open(
				templateUrl: "views/images/confirm.html"
				controller: ($scope, $modalInstance, image) ->
					$scope.ok = ->
						$modalInstance.close image

					$scope.cancel = ->
						$modalInstance.dismiss "Cancel"

					return
				resolve:
					image: ->
						image
			)

			modalInstance.result.then(
				(image) ->
					deleteImage image
					return
				, ->
					console.log "Result Cancel"
					return
			)

			return

		return
])

########################
# Create Image Modal Ctrl
########################
.controller('CreateImageModalCtrl', [
	'$scope'
	'$modalInstance'
	(
		$scope, $modalInstance
	) ->

		$scope.image =
			name: ''
			properties: {
				description: ''
			}
			file: null
			disk_format: ''
			architecture: ''
			min_disk: 0
			min_ram: 0
			is_public: false
			protected: false

		original = angular.copy($scope.image)

		dump =
			#name: 'Default Image Name'
			name: ''
			properties: {
				description: ''
				#description: 'Description'
			}
			file: null
			disk_format: ''
			architecture: ''
			min_disk: 0
			min_ram: 0
			is_public: false
			protected: false

		$scope.image = dump

		########################
		# File
		########################
		$scope.onFileSelect = ($files) ->
			$scope.image.file = $files[0]
			return

		$scope.revert = ->
			$scope.image = angular.copy(original)
			$scope.form.$setPristine()
			return

		$scope.canRevert = ->
			return !angular.equals($scope.image, original) || !$scope.form.$pristine

		$scope.canSubmit = ->
			return $scope.form.$valid && !angular.equals($scope.image, original)

		$scope.submit = (image) ->
			$modalInstance.close image
			$scope.revert()
			return

		$scope.cancel = ->
	    	$modalInstance.dismiss 'cancel'

		return # end of CreateImageModalCtrl
])
