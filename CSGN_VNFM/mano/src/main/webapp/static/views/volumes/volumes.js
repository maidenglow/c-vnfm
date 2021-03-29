
"use strict";
angular.module("app.volumes", []).controller("VolumsCtrl", ["$scope", "$modal", "$http", "logger", "$timeout", "$q", "VolumesAPI", "VolumeExtendAPI", "VolumeAttachmentAPI", "VolumeSnapshotAPI", "TenantsAPI", "ComputeServersAPI", function($scope, $modal, $http, logger, $timeout, $q, VolumesAPI, VolumeExtendAPI, VolumeAttachmentAPI, VolumeSnapshotAPI, TenantsAPI, ComputeServersAPI) {
    var STATUS_CHOICES, attachVolumeAttachment, createVolume, createVolumeSnapshot, deleteVolume, findIndex, getServers, getVolume, getVolumes, lookupProjectName, refreshVolume, updateVolume, updateVolumeSize;
    STATUS_CHOICES = {
        "in-use": !0,
        available: !0,
        error: !1
    }, $scope.projects = [], $scope.volumes, $scope.volume, $scope.selectItem = function(volume) {
        return $scope.volume = volume
    }, $scope.isSelected = function(volume) {
        return $scope.volume === volume
    }, $scope.itemStatusClass = function(item) {
        var clazz;
        return clazz = "available" === item.status || "in-use" === item.status ? "label label-success" : "label label-danger"
    }, $scope.init = function() {
        return $q.all([getVolumes().$promise, TenantsAPI.get().$promise, getServers().$promise]).then(function(result) {
            var volume, volumes, _i, _j, _len, _len1, _results;
            for ($scope.projects = result[1].tenants, volumes = $scope.volumes, _i = 0, _len = volumes.length; _len > _i; _i++) volume = volumes[_i], lookupProjectName(volume);
            for (volumes && volumes.length > 0 && $scope.selectItem(volumes[0]), _results = [], _j = 0, _len1 = volumes.length; _len1 > _j; _j++) volume = volumes[_j], _results.push($scope.isActionProgress(volume.status) ? refreshVolume(volume.id, volume.status) : void 0);
            return _results
        })
    }, lookupProjectName = function(volume) {
        var project, _i, _len, _ref;
        for (_ref = $scope.projects, _i = 0, _len = _ref.length; _len > _i; _i++) project = _ref[_i], project.id === volume["os-vol-tenant-attr:tenant_id"] && (volume.project_name = project.name)
    }, getServers = function(server) {
        return ComputeServersAPI.query(server, function(response) {
            $scope.servers = response
        })
    }, getVolumes = function(volume) {
        return VolumesAPI.query(volume, function(response) {
            $scope.volumes = response
        })
    }, getVolume = function(volume) {
        return VolumesAPI.get(volume, function(response) {
            var idx, refreshedVolume;
            $scope.volume = response, idx = findIndex(volume), idx > -1 ? (refreshedVolume = response, lookupProjectName(refreshedVolume), refreshedVolume.$$hashKey = $scope.volumes[idx].$$hashKey, $scope.volumes[idx] = refreshedVolume) : (refreshedVolume = response, lookupProjectName(refreshedVolume), $scope.volumes.splice(0, 0, refreshedVolume))
        })
    }, createVolume = function(volume) {
        return VolumesAPI.save(volume, function(response) {
            logger.logSuccess("볼륨이 생성되었습니다."), getVolume({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshVolume(response.id, response.status)
                }, 1e3)
            })
        })
    }, updateVolume = function(volume) {
        VolumesAPI.update(volume, function() {
            logger.logSuccess("볼륨이 수정되었습니다."), $scope.volume.display_name = volume.display_name, $scope.volume.display_description = volume.display_description
        })
    }, updateVolumeSize = function(volume) {
        VolumeExtendAPI.save(volume, function(response) {
            response.success ? ($scope.volume.size = volume.size, logger.logSuccess("볼륨확장에 성공하였습니다.")) : logger.logError("볼륨확장에 실패하였습니다.")
        })
    }, deleteVolume = function(volume) {
        return VolumesAPI["delete"](volume, function(response) {
            response.success ? (logger.log("삭제 되었습니다."), getVolume({
                id: volume.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshVolume(volume.id, volume.status)
                }, 1e3)
            })) : logger.logError("삭제를 실패하였습니다. <br/>" + response.fault)
        })
    }, refreshVolume = function(id, status) {
        return VolumesAPI.get({
            id: id
        }, function(response) {
            var idx, refreshedVolume;
            "deleting" === status && "undefined" == typeof response.id && (idx = findIndex({
                id: id
            }), idx > -1 && ($scope.volumes.splice(idx, 1), $scope.volumes.length > 0 && $scope.selectItem($scope.volumes[0]))), $scope.isActionProgress(response.status) ? (idx = findIndex(response), idx > -1 ? $timeout(function() {
                return refreshVolume(id, response.status)
            }, 2500) : (idx = findIndex({
                id: id
            }), idx > -1 && ($scope.volumes.splice(idx, 1), $scope.volumes.length > 0 && $scope.selectItem($scope.volumes[0])))) : (idx = findIndex(response), idx > -1 && (refreshedVolume = response, lookupProjectName(refreshedVolume), refreshedVolume.$$hashKey = $scope.volumes[idx].$$hashKey, $scope.volumes[idx] = refreshedVolume, $scope.selectItem($scope.volumes[idx])))
        })
    }, findIndex = function(volume) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.volumes, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === volume.id) return idx;
        return -1
    }, attachVolumeAttachment = function(volumeAttachment) {
        return VolumeAttachmentAPI.save(volumeAttachment, function(response) {
            logger.logSuccess("" + response.device + "의 인스턴스 " + volumeAttachment.serverName + "에 볼륨 " + volumeAttachment.volumeName + "를 연결합니다. ")
        })
    }, createVolumeSnapshot = function(snapshot) {
        return VolumeSnapshotAPI.save(snapshot, function() {
            logger.logSuccess("볼륨 스냅샷이 생성되었습니다.")
        })
    }, $scope.isActionProgress = function(status) {
        return status && "undefined" == typeof STATUS_CHOICES[status.toLowerCase()] ? !0 : !1
    }, $scope.volumeStatusClass = function(instance) {
        var clazz;
        return clazz = "available" === instance.status ? "label label-success" : "label label-danger"
    }, $scope.getServerName = function(serverId) {
        var item, _i, _len, _ref;
        if ($scope.servers)
            for (_ref = $scope.servers, _i = 0, _len = _ref.length; _len > _i; _i++)
                if (item = _ref[_i], item.id === serverId) return item.name;
        return ""
    }, $scope.createVolumeForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/create.html",
            controller: "CreateVolumeModalCtrl",
            size: "lg"
        }), modalInstance.result.then(function(volume) {
            createVolume(volume)
        }, function() {})
    }, $scope.deleteVolumeForm = function(volume) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/confirm.html",
            controller: function($scope, $modalInstance, volume) {
                $scope.ok = function() {
                    return $modalInstance.close(volume)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                volume: function() {
                    return volume
                }
            }
        }), modalInstance.result.then(function(volume) {
            deleteVolume(volume)
        }, function() {})
    }, $scope.editVolumeBasicForm = function(volume) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/edit_basic.html",
            controller: "EditVolumeBasicModalCtrl",
            size: "lg",
            resolve: {
                volume: function() {
                    return volume
                }
            }
        }), modalInstance.result.then(function(volume) {
            updateVolume(volume)
        }, function() {})
    }, $scope.editVolumeSizeForm = function(volume) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/edit_size.html",
            controller: "EditVolumeSizeModalCtrl",
            size: "lg",
            resolve: {
                volume: function() {
                    return volume
                }
            }
        }), modalInstance.result.then(function(volume) {
            updateVolumeSize(volume)
        }, function() {})
    }, $scope.editVolumeAttachForm = function(volume) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/edit_attach.html",
            controller: "EditVolumeAttachModalCtrl",
            size: "lg",
            resolve: {
                volume: function() {
                    return volume
                }
            }
        }), modalInstance.result.then(function(volumeAttachment) {
            attachVolumeAttachment(volumeAttachment)
        }, function() {})
    }, $scope.createVolumeSnapshotForm = function(volume) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/volumes/create_snapshot.html",
            controller: "CreateVolumeSnapshotModalCtrl",
            size: "lg",
            resolve: {
                volume: function() {
                    return volume
                }
            }
        }), modalInstance.result.then(function(snapshot) {
            createVolumeSnapshot(snapshot)
        }, function() {})
    }
}]).controller("CreateVolumeModalCtrl", ["$scope", "$modalInstance", "$q", "VolumesAPI", "VolumeTypesAPI", "ComputeImagesAPI", "ComputeAvailabilityZonesAPI", "VolumeLimitsAPI", function($scope, $modalInstance, $q, VolumesAPI, VolumeTypesAPI, ComputeImagesAPI, ComputeAvailabilityZonesAPI, VolumeLimitsAPI) {
    var original, percentage;
    $scope.volume = {
        display_name: null,
        display_description: null,
        volume_type: null,
        size: null,
        volume: null,
        source_volid: null,
        image: null,
        imageRef: null,
        source: null,
        availability_zone: null
    }, original = angular.copy($scope.volume), $scope.limits = {
        gigabytes: null,
        counts: null
    }, $scope.revert = function() {
        $scope.volume = angular.copy(original), $scope.calVolumeGBytesProgress(), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.volume, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.volume, original)
    }, $scope.submit = function(instance) {
        $modalInstance.close(instance), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([VolumeTypesAPI.query().$promise, VolumesAPI.query().$promise, ComputeImagesAPI.query().$promise, ComputeAvailabilityZonesAPI.query().$promise, VolumeLimitsAPI.get().$promise]).then(function(result) {
            var availability_zones, images, limits, volumeTypes, volumes;
            return volumeTypes = result[0], $scope.volumeTypes = volumeTypes, volumes = result[1], $scope.volumes = volumes, volumes.length > 0 && ($scope.volume.volume = volumes[0]), images = result[2], $scope.images = images, images.length > 0 && ($scope.volume.image = images[0]), availability_zones = result[3], $scope.availability_zones = availability_zones, $scope.sources = [{
                type: "empty",
                name: "소스없음, 빈 볼륨"
            }, {
                type: "image",
                name: "이미지"
            }, {
                type: "volume",
                name: "볼륨"
            }], $scope.sources.length > 0 && ($scope.volume.source = $scope.sources[0]), limits = result[4], $scope.limits.gigabytes = {
                max: limits.maxTotalVolumeGigabytes,
                used: limits.totalGigabytesUsed
            }, $scope.limits.counts = {
                max: limits.maxTotalVolumes,
                used: limits.totalVolumesUsed
            }, $scope.allProgress()
        })
    }, $scope.init(), $scope.changeVolumeType = function() {
        $scope.volume.volume_type = $scope.volume.type.name
    }, $scope.setVolumeSize = function(size) {
        $scope.volume.size || ($scope.volume.size = size, $scope.calVolumeGBytesProgress()), $scope.volume.size < size && ($scope.volume.size = size, $scope.calVolumeGBytesProgress())
    }, $scope.changeSource = function() {
        var imageSize;
        $scope.volume.source_volid = null, $scope.volume.imageRef = null, "image" === $scope.volume.source.type ? (imageSize = $scope.volume.image["OS-EXT-IMG-SIZE:size"] / 1024 / 1024 / 1024, $scope.setVolumeSize(1 > imageSize ? 1 : Math.round(imageSize + .5)), $scope.volume.imageRef = $scope.volume.image.id) : "volume" === $scope.volume.source.type && ($scope.setVolumeSize($scope.volume.volume.size), $scope.volume.source_volid = $scope.volume.volume.id)
    }, $scope.changeImage = function() {
        var imageSize;
        imageSize = $scope.volume.image["OS-EXT-IMG-SIZE:size"] / 1024 / 1024 / 1024, $scope.setVolumeSize(1 > imageSize ? 1 : Math.round(imageSize + .5)), $scope.volume.imageRef = $scope.volume.image.id
    }, $scope.changeVolume = function() {
        $scope.setVolumeSize($scope.volume.volume.size), $scope.volume.source_volid = $scope.volume.volume.id
    }, $scope.changeAvailabilityZone = function() {
        $scope.volume.availability_zone = $scope.volume.availability_zone_select.zoneName
    }, percentage = function(max, used) {
        return Math.round(used / max * 100)
    }, $scope.calProgress = function(max, used, add) {
        var addPer, addType, per, progressbars;
        return progressbars = [], per = percentage(max, used), progressbars.push({
            value: per,
            type: "success"
        }), addPer = percentage(max, used + add), addType = "info", addPer > 70 && 90 >= addPer ? addType = "waring" : addPer > 90 && (addType = "danger"), addPer > 100 && (addPer = 100), addPer -= per, progressbars.push({
            value: addPer,
            type: addType
        }), progressbars
    }, $scope.allProgress = function() {
        $scope.calVolumeGBytesProgress(), $scope.calVolumeUsedCountsProgress()
    }, $scope.calVolumeGBytesProgress = function() {
        var add, max, progress, used;
        max = $scope.limits.gigabytes.max, used = $scope.limits.gigabytes.used, add = parseInt($scope.volume.size), progress = $scope.calProgress(max, used, add), $scope.volumeUsedGBytesStacked ? ($scope.volumeUsedGBytesStacked[1].value = progress[1].value, $scope.volumeUsedGBytesStacked[1].type = progress[1].type) : $scope.volumeUsedGBytesStacked = progress
    }, $scope.calVolumeUsedCountsProgress = function() {
        var add, max, progress, used;
        max = $scope.limits.counts.max, used = $scope.limits.counts.used, add = 1, progress = $scope.calProgress(max, used, add), $scope.volumeUsedCountsStacked ? ($scope.volumeUsedCountsStacked[1].value = progress[1].value, $scope.volumeUsedCountsStacked[1].type = progress[1].type) : $scope.volumeUsedCountsStacked = progress
    }
}]).controller("EditVolumeBasicModalCtrl", ["$scope", "$modalInstance", "$q", "volume", function($scope, $modalInstance, $q, volume) {
    var original;
    $scope.volume = {
        id: volume.id,
        display_name: volume.display_name,
        display_description: volume.display_description
    }, original = angular.copy($scope.volume), $scope.revert = function() {
        $scope.volume = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.volume, original)
    }, $scope.submit = function(volume) {
        $modalInstance.close(volume), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]).controller("EditVolumeSizeModalCtrl", ["$scope", "$modalInstance", "$q", "volume", "VolumeLimitsAPI", function($scope, $modalInstance, $q, volume, VolumeLimitsAPI) {
    var original, percentage;
    $scope.volume = {
        id: volume.id,
        display_name: volume.display_name
    }, $scope.min_volume_size = volume.size, original = angular.copy($scope.volume), $scope.limits = {
        gigabytes: null,
        counts: null
    }, $scope.revert = function() {
        $scope.volume = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.volume, original)
    }, $scope.submit = function(instance) {
        $modalInstance.close(instance), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([VolumeLimitsAPI.get().$promise]).then(function(result) {
            var limits;
            return limits = result[0], $scope.limits.gigabytes = {
                max: limits.maxTotalVolumeGigabytes,
                used: limits.totalGigabytesUsed
            }, $scope.limits.counts = {
                max: limits.maxTotalVolumes,
                used: limits.totalVolumesUsed
            }, $scope.allProgress()
        })
    }, $scope.init(), percentage = function(max, used) {
        return Math.round(used / max * 100)
    }, $scope.calProgress = function(max, used, add) {
        var addPer, addType, per, progressbars;
        return progressbars = [], per = percentage(max, used), progressbars.push({
            value: per,
            type: "success"
        }), addPer = percentage(max, used + add), addType = "info", addPer > 70 && 90 >= addPer ? addType = "waring" : addPer > 90 && (addType = "danger"), addPer > 100 && (addPer = 100), addPer -= per, progressbars.push({
            value: addPer,
            type: addType
        }), progressbars
    }, $scope.allProgress = function() {
        $scope.calVolumeGBytesProgress()
    }, $scope.calVolumeGBytesProgress = function() {
        var add, max, progress, used;
        max = $scope.limits.gigabytes.max, used = $scope.limits.gigabytes.used, add = parseInt($scope.volume.size), progress = $scope.calProgress(max, used, add), $scope.volumeUsedGBytesStacked ? ($scope.volumeUsedGBytesStacked[1].value = progress[1].value, $scope.volumeUsedGBytesStacked[1].type = progress[1].type) : $scope.volumeUsedGBytesStacked = progress
    }
}]).controller("EditVolumeAttachModalCtrl", ["$scope", "$modalInstance", "$q", "volume", "ComputeServersAPI", function($scope, $modalInstance, $q, volume, ComputeServersAPI) {
    var original;
    $scope.volumeAttachment = {
        instance: null,
        serverId: null,
        serverName: null,
        volumeId: volume.id,
        volumeName: volume.display_name,
        device: ""
    }, $scope.volume = volume, original = angular.copy($scope.volumeattachment), $scope.revert = function() {
        $scope.volumeattachment = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.volumeAttachment, original)
    }, $scope.submit = function(volumeAttachment) {
        $modalInstance.close(volumeAttachment), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([ComputeServersAPI.query().$promise]).then(function(result) {
            var instances;
            return instances = result[0], void($scope.instances = instances)
        })
    }, $scope.init(), $scope.changeInstance = function() {
        $scope.volumeAttachment.instance ? ($scope.volumeAttachment.serverId = $scope.volumeAttachment.instance.id, $scope.volumeAttachment.serverName = $scope.volumeAttachment.instance.name) : ($scope.volumeAttachment.serverId = null, $scope.volumeAttachment.serverName = null)
    }, $scope.getServerName = function(serverId) {
        var item, _i, _len, _ref;
        if ($scope.instances)
            for (_ref = $scope.instances, _i = 0, _len = _ref.length; _len > _i; _i++)
                if (item = _ref[_i], item.id === serverId) return item.name;
        return ""
    }
}]).controller("CreateVolumeSnapshotModalCtrl", ["$scope", "$modalInstance", "$q", "volume", "VolumeLimitsAPI", function($scope, $modalInstance, $q, volume, VolumeLimitsAPI) {
    var original, percentage;
    $scope.volume = volume, $scope.snapshot = {
        display_name: null,
        display_description: null,
        volume_id: volume.id
    }, original = angular.copy($scope.snapshot), $scope.limits = {
        gigabytes: null,
        counts: null
    }, $scope.revert = function() {
        $scope.snapshot = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.snapshot, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.volume, original)
    }, $scope.submit = function(snapshot) {
        $modalInstance.close(snapshot), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([VolumeLimitsAPI.get().$promise]).then(function(result) {
            var limits;
            return limits = result[0], $scope.limits.gigabytes = {
                max: limits.maxTotalVolumeGigabytes,
                used: limits.totalGigabytesUsed
            }, $scope.limits.counts = {
                max: limits.maxTotalVolumes,
                used: limits.totalVolumesUsed
            }, $scope.allProgress()
        })
    }, $scope.init(), percentage = function(max, used) {
        return Math.round(used / max * 100)
    }, $scope.calProgress = function(max, used, add) {
        var addPer, addType, per, progressbars;
        return progressbars = [], per = percentage(max, used), progressbars.push({
            value: per,
            type: "success"
        }), addPer = percentage(max, used + add), addType = "info", addPer > 70 && 90 >= addPer ? addType = "waring" : addPer > 90 && (addType = "danger"), addPer > 100 && (addPer = 100), addPer -= per, progressbars.push({
            value: addPer,
            type: addType
        }), progressbars
    }, $scope.allProgress = function() {
        $scope.calVolumeGBytesProgress(), $scope.calVolumeUsedCountsProgress()
    }, $scope.calVolumeGBytesProgress = function() {
        var add, max, progress, used;
        max = $scope.limits.gigabytes.max, used = $scope.limits.gigabytes.used, add = 0, progress = $scope.calProgress(max, used, add), $scope.volumeUsedGBytesStacked ? ($scope.volumeUsedGBytesStacked[1].value = progress[1].value, $scope.volumeUsedGBytesStacked[1].type = progress[1].type) : $scope.volumeUsedGBytesStacked = progress
    }, $scope.calVolumeUsedCountsProgress = function() {
        var add, max, progress, used;
        max = $scope.limits.counts.max, used = $scope.limits.counts.used, add = 1, progress = $scope.calProgress(max, used, add), $scope.volumeUsedCountsStacked ? ($scope.volumeUsedCountsStacked[1].value = progress[1].value, $scope.volumeUsedCountsStacked[1].type = progress[1].type) : $scope.volumeUsedCountsStacked = progress
    }
}]);