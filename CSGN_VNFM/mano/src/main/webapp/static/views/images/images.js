
"use strict";
angular.module("app.images", ["angularFileUpload"]).controller("ImagesController", ["$scope", "$modal", "$timeout", "logger", "$upload", "ImageImagesAPI", "ImageUploadAPI", "CONST_RESTFUL_API", function($scope, $modal, $timeout, logger, $upload, ImageImagesAPI, ImageUploadAPI, CONST_RESTFUL_API) {
    var STATUS_CHOICES, createImage, uploadImage, deleteImage, findIndex, getImage, getImages, refreshImage, updateImage;
    STATUS_CHOICES = {
        active: !0,
        saving: null,
        queued: null,
        pending_delete: null,
        killed: !1,
        deleted: !1
    }, getImages = function(params) {
        return ImageImagesAPI.query(params, function(response) {
            $scope.images = response
        })
    }, getImage = function(image) {
        return ImageImagesAPI.get(image, function(response) {
            $scope.image = response
        })
    }, createImage = function(image) {
        var modalInstance; 
        modalInstance = $modal.open({
            backdrop: "static",
            templateUrl: "views/images/uploading.html",
            controller: function($scope, $modalInstance, $upload, image) {
                $scope.uplodingProgressPercent = "0", $scope.upload = $upload.upload({
                    url: CONST_RESTFUL_API.IMAGE_URL.IMAGES_UPLOAD,
                    method: "POST",
                    file: image.file,
                    fileFormDataName: "file",
                    data: image,
                    sendDataAsJson: !0
                }).progress(function(evt) {
                    return void($scope.uplodingProgressPercent = parseInt(100 * evt.loaded / evt.total) + "")
                }).success(function(data) {
                    $modalInstance.close(data)
                })
            },
            resolve: {
                image: function() {
                    return image
                }
            }
        }), modalInstance.result.then(function(data) {
            logger.logSuccess("이미지가 생성되었습니다."), $scope.image = data, $scope.images.splice(0, 0, data), $scope.isActionProgress(data.status) && refreshImage(data.id)
        }, function() {})
    }, uploadImage = function(image) {
        var modalInstance;
        modalInstance = $modal.open({
            backdrop: "static",
            templateUrl: "views/images/uploading.html",
            controller: function($scope, $modalInstance, $upload, image) {
                $scope.uplodingProgressPercent = "0", $scope.upload = $upload.upload({
                    url: CONST_RESTFUL_API.IMAGE_URL.IMAGES_UPLOADS,
                    method: "POST",
                    file: image.file,
                    fileFormDataName: "file",
                    data: image,
                    sendDataAsJson: !0
                }).progress(function(evt) {
                    return void($scope.uplodingProgressPercent = parseInt(100 * evt.loaded / evt.total) + "")
                }).success(function(data) {
                    $modalInstance.close(data)
                })
            },
            resolve: {
                image: function() {
                    return image
                }
            }
        }), modalInstance.result.then(function(data) {
            logger.logSuccess("파일이 업로드에 성공하였습니다."), $scope.image = data/*$scope.image = data, $scope.images.splice(0, 0, data), $scope.isActionProgress(data.status) && refreshImage(data.id)*/
        }, function() {})
    }, updateImage = function(image) {
        ImageImagesAPI.update(image, function(response) {
            logger.logSuccess("이미지가 수정되었습니다."), $scope.image = response
        })
//    }, deleteImage = function(image) {
//        return ImageUploadAPI["delete"](image, function(response) {
//            return logger.logSuccess("파일 삭제에 성공하였습니다."), $scope.image = response;//response.success ? (logger.logSuccess("삭제되었습니다."), $scope.images.splice(findIndex(image), 1), void($scope.images.length > 0 && $scope.selectImage($scope.images[0]))) : void logger.logError("삭제를 실패하였습니다. " + response.fault)
//        })
    }, deleteImage = function(image) {
        return ImageImagesAPI["delete"](image, function(response) {
        	getImages().$promise.then(function() {
                var image, _i, _len, _ref, _results;
                for ($scope.images && $scope.images.length > 0 && ($scope.image = $scope.images[0]), _ref = $scope.images, _results = [], _i = 0, _len = _ref.length; _len > _i; _i++) image = _ref[_i], _results.push($scope.isActionProgress(image.status) ? refreshImage(image.id) : void 0);
                return _results
            })
            return logger.logSuccess("삭제되었습니다."), $scope.image = response;refreshImage(response.id);console("refreshImage");//$scope.images.splice(findIndex(image), 1), void($scope.images.length > 0 && $scope.selectImage($scope.images[0]))) : void logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
//    }, deleteImage = function(image) {
//        return ImageImagesAPI["delete"](image, function(response) {
//            return response.success ? (logger.logSuccess("삭제되었습니다."), $scope.images.splice(findIndex(image), 1), void($scope.images.length > 0 && $scope.selectImage($scope.images[0]))) : void logger.logError("삭제를 실패하였습니다. " + response.fault)
//        })
    }, refreshImage = function(id) {
        return ImageImagesAPI.get({
            id: id
        }, function(response) {
            var idx, refreshedImage;
            $scope.isActionProgress(response.status) === !0 ? $timeout(function() {
                return refreshImage(id)
            }, 2500) : (idx = findIndex(response), idx > -1 && (refreshedImage = response, refreshedImage.$$hashKey = $scope.images[idx].$$hashKey, $scope.images[idx] = refreshedImage, $scope.image = $scope.images[idx]))
        })
    }, findIndex = function(image) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.images, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === image.id) return idx;
        return -1
    }, $scope.images = [], getImages().$promise.then(function() {
        var image, _i, _len, _ref, _results;
        for ($scope.images && $scope.images.length > 0 && ($scope.image = $scope.images[0]), _ref = $scope.images, _results = [], _i = 0, _len = _ref.length; _len > _i; _i++) image = _ref[_i], _results.push($scope.isActionProgress(image.status) ? refreshImage(image.id) : void 0);
        return _results
    }), $scope.isActionProgress = function(status) {
        return status && ("undefined" == typeof STATUS_CHOICES[status.toLowerCase()] || null === STATUS_CHOICES[status.toLowerCase()]) ? !0 : !1
    }, $scope.imageStatusClass = function(image) {
        var clazz;
        return clazz = "active" === image.status ? "label label-success" : "label label-danger"
    }, $scope.selectImage = function(image) {
        $scope.image = image
    }, $scope.isSelected = function(image) {
        return $scope.image === image
        
    }, $scope.selectFile = function(){
        $("#id_file").click();
    }, $scope.onFileSelect = function($files) {
        $scope.image.file = $files[0]
        uploadImage($scope.image);
    }, $scope.createImageForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/images/create.html",
            controller: "CreateImageModalCtrl"
        }), modalInstance.result.then(function(image) {
            createImage(image)
        }, function() {
            return void 0
        })
    }, $scope.updateImageForm = function(image) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/images/update.html",
            controller: function($scope, $modalInstance, image) {
                return $scope.image = image, $scope.submit = function() {
                    return $modalInstance.close(image)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("cancel")
                }
            },
            resolve: {
                image: function() {
                    return image
                }
            }
        }), modalInstance.result.then(function(image) {
            updateImage(image)
        })
    }, $scope.deleteImageForm = function(image) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/images/confirm.html",
            controller: function($scope, $modalInstance, image) {
            	$scope.image = image;
                $scope.ok = function() {
                    return $modalInstance.close(image)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                image: function() {
                    return image
                }
            }
        }), modalInstance.result.then(function(image) {
            deleteImage(image)
        }, function() {})
    }
}]).controller("CreateImageModalCtrl", ["$scope", "$modalInstance", function($scope, $modalInstance) {
    var dump, original;
    $scope.image = {
        name: "",
        properties: {
            description: ""
        },
        file: null,
        disk_format: "",
        architecture: "",
        min_disk: 0,
        min_ram: 0,
        is_public: !1,
        "protected": !1
    }, original = angular.copy($scope.image), dump = {
        name: "",
        properties: {
            description: ""
        },
        file: null,
        disk_format: "",
        architecture: "",
        min_disk: 0,
        min_ram: 0,
        is_public: !1,
        "protected": !1
    }, $scope.image = dump, $scope.onFileSelect = function($files) {
        $scope.image.file = $files[0]
    }, $scope.revert = function() {
        $scope.image = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.image, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.image, original)
    }, $scope.submit = function(image) {
        $modalInstance.close(image), $scope.revert()
    }, $scope.cancel = function() {
        return $modalInstance.dismiss("cancel")
    }
}]);