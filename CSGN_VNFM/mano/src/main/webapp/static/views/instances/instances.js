
"use strict";
angular.module("app.instances", [])
	.controller("InstancesCtrl", ["$scope", "$window", "$modal", "logger", "$q", "$timeout", "$location", "ComputeServersAPI", "ComputeServerActionsAPI", "ComputeFlavorsAPI", "ComputeImagesAPI", "ComputeLimitAPI", "NetworkingFloatingIpsAPI", "TelemetryInstancesAPI", "ComputeServersSnapShotAPI", "ComputeConsoleAPI", "ComputeServersGroupAPI", 
	                      function($scope, $window, $modal, logger, $q, $timeout, $location, ComputeServersAPI, ComputeServerActionsAPI, ComputeFlavorsAPI, ComputeImagesAPI, ComputeLimitAPI, NetworkingFloatingIpsAPI, TelemetryInstancesAPI, ComputeServersSnapShotAPI, ComputeConsoleAPI, ComputeServersGroupAPI) {
    var date,STATUS_CHOICES, createBackup, restoreInstance, createFloatingIp, createInstance, deleteFloatingIp, deleteInstance, deleteInstances, findIndex, findIndexX, findIndexY, getInstance, getInstanceListener, getInstances,getInstanceGroup, getTelemetryInstance, promise, refreshInstance;
    
    $scope.safeApply = function (fn) {
        var phase = this.$root.$$phase;
        if (phase == '$apply' || phase == '$digest') {
            if (fn && (typeof(fn) === 'function')) {
                fn();
            }
        } else {
            this.$apply(fn);
        }
    };
    $scope.$on('selectNodeDescriptor', function (event, data) {
    	$scope.safeApply(function () {
    	        $scope.selectedNode = data;
    	        if(data.info){
    	        	$scope.instance = data.info;
    	        	$scope.instance.actionLog = data.actionLog;
    	        }else {
    	        	$scope.instance = data;
    	        }
        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	$scope.selectedNode = data;
    	$scope.instance = data;
    });
    
    $scope.$watch('selectedNode', function (nv, ov) {
    	if (nv == undefined || nv === ov || (ov != undefined &&
    			(ov.id != undefined && nv.id != undefined && ov.id == nv.id) &&
    			(ov.category != undefined && nv.category != undefined && ov.category == nv.category))) {
            return;
        }
    	$scope.reloadDetailData();
    	
    	if ($scope.selectedNode.id) {
        	var selectedElement = angular.element('#treeRoot').find('.is-leaf-node-type-icon.highlight');
        	if (selectedElement && angular.element(selectedElement).find('span.is-leaf-node').text() != $scope.selectedNode.id) {
        		angular.element(selectedElement).removeClass('highlight');
        		selectedElement = undefined;
        	} 
        	if (!selectedElement) {
        		var leafNodes = angular.element('#treeRoot span.is-leaf-node');
        		for(var i = 0; i < leafNodes.length; i++) {
        			if(angular.element(leafNodes[i]).text() == $scope.selectedNode.id) {
        				angular.element(leafNodes[i]).parent().addClass('highlight');
        				break;
        			}
        		}
        	}
        	
    	} else if ($scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
    		var selectedElement = angular.element('#treeRoot').find('.highlight');
    		angular.element(selectedElement).removeClass('highlight');
    		var hasChildNodes = angular.element('#treeRoot').find('.node-type-icon>span.label').not('.is-leaf-node');
    		for (var i = 0; i < hasChildNodes.length; i++) {
    			if (angular.element(hasChildNodes[i]).text().toUpperCase() == $scope.selectedNode.category.toUpperCase()) {
    				angular.element(hasChildNodes[i]).parent().addClass('highlight');
    				break;
    			}
    		}
    	}
    }, true);
	                 
     $scope.reloadDetailData = function () {

         if ($scope.selectedNode != undefined && $scope.selectedNode.id != undefined && $scope.selectedNode.id != '' &&
         		$scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
         	//TODO: if 에서 category 값에 따라 API 를 통해 가져오기
 			$scope.selectedData = undefined;
 		  
         	var selectedNodeObject = $scope.selectedNode;
         	$scope.instance = $scope.selectedNode;
         } else if ($scope.selectedNode != undefined && $scope.selectedNode.category && ($scope.selectedNode.id == undefined || $scope.selectedNode.id == '')) {

         	$scope.selectedNode.id = "";
         	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

         	$scope.editMode = 'XML';
            $scope.safeApply();
         }
    }, STATUS_CHOICES = {
        active: !0,
        shutoff: !0,
        suspended: !0,
        paused: !0,
        error: !1
    }, $scope.init = function() {
    	
        return $q.all([getInstances().$promise]).then(function() {
            var instance, instances, _i, _len;
            for (instances = $scope.instances, instances && instances.length > 0 
            		&& $scope.selectInstance(instances[0]), _i = 0, _len = instances.length; _len > _i; _i++) instance = instances[_i]
            		, $scope.isActionProgress(instance.status) && refreshInstance(instance.id);
            return;
        })
    }, getInstances = function(instance) {
        return ComputeServersAPI.query(instance, function(response) {
            $scope.instances = response
        })
    }, getInstanceListener = function(instance) {
        return ComputeServersAPI.get(instance, function(response) {
        	if(response.status == 'active'){
	            var idx, idx2, refreshedInstance;
	            $scope.instance = response, idx = findIndexX(instance), idx2 = findIndexY(instance), idx > -1 ? 
            		($scope.instances[idx].children.child[idx2].info = response,$scope.instance = response)
//            		(refreshedInstance = response, refreshedInstance.$$hashKey = $scope.instances[idx].$$hashKey, $scope.instances[idx].children.child[idx2] = refreshedInstance)
            		: null
        	}else {
        		return $timeout(function() {
	                return getInstanceListener(response)}, 1e3)
        	}
        })
    }, getInstance = function(instance) {
    	var param = new Object();
    	param.id = instance.id;
        return ComputeServersAPI.get(param, function(response) {
        	if(response.status == 'reboot' || response.status == 'migrating'){
        		 var idx, idx2, refreshedInstance;
 	            $scope.instance = response, idx = findIndexX(instance), idx2 = findIndexY(instance), idx > -1 ? 
             		($scope.instances[idx].children.child[idx2].info = response,$scope.instance = response,getInstanceListener(param))
             		: null
        		
        	}else if(response.status != instance.status){
	            var idx, idx2, refreshedInstance;
	            $scope.instance = response, idx = findIndexX(instance), idx2 = findIndexY(instance), idx > -1 ? 
            		($scope.instances[idx].children.child[idx2].info = response,$scope.instance = response)
//            		(refreshedInstance = response, refreshedInstance.$$hashKey = $scope.instances[idx].$$hashKey, $scope.instances[idx].children.child[idx2] = refreshedInstance)
            		: null;
        	}else {
        		return $timeout(function() {
	                return getInstance(response)}, 1e3)
        	}
        })
    }, getInstanceGroup = function(instance) {
    	var param = new Object();
    	param.id = instance.id;
//    	param.name = instance.name;
    	param.status = instance.status;
        return ComputeServersGroupAPI.get(param, function(response) {
            var idx, refreshedInstance;
            if(response.status != instance.status){
            	idx = findIndex(instance), idx > -1 ?($scope.instances[idx] = response,$scope.instance = response): null
            }else{
	    		return $timeout(function() {
	                return getInstanceGroup(response)}, 1e3)
        	}
        })
/*    }, createInstance = function(instance) {
        return ComputeServersAPI.save(instance, function(response) {
        	response.success ? logger.logSuccess("인스턴스가 생성되었습니다.") : logger.logError("인스턴스가 생성 실패 : "+ response.fault), getInstances({
//                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshInstance(response.id)
                }, 1e3)
            })
        })*/    
        
    }, createInstance = function(instance) {
    	var param = new Object();
		param.id = instance.flavor.name;
		param.status = 'Inactive';
		
        return ComputeServersAPI.save(instance, function(response) {
        	
        	response.success ? logger.logSuccess("인스턴스가 생성되었습니다.") : logger.logError("인스턴스가 생성 실패 : "+ response.fault), getInstances({
//                id: response.id
            }).$promise.then(function() {
            	/*if(response.status == 'active'){
                	idx = findIndex(instance), idx > -1 ?($scope.instances[idx] = response,$scope.instance = response): null
                }else{
    	    		return $timeout(function() {
    	                return getInstanceGroup(param)}, 1e3)
            	}*/
                return $timeout(function() {
                    return getInstanceGroup(param)
                }, 1e3)
            })
        })
    }, deleteInstances = function(instance) {
    	var data = new Object();
    	data.id = instance.id;
        return ComputeServersAPI["delete"](data, function(response) {
        	response.success ? logger.log("삭제 되었습니다."): logger.logError("삭제를 실패하였습니다. " + response.fault),
			getInstances({
            }).$promise.then(function() {
            })
//            var idx;
//            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(instance), $scope.instances(idx, 1), $scope.instances.length > 0 && $scope.selectInstance($scope.instances[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, deleteInstance = function(instance) {
        return ComputeServersAPI["delete"](instance, function(response) {
            var idx,idx2;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndexX(instance), idx2 = findIndexY(instance), $scope.instances[idx].children.child.splice(idx2, 1), $scope.instances.length > 0 && $scope.selectInstance($scope.instances[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, refreshInstance = function(id) {
        return ComputeServersAPI.get({
            id: id
        }, function(response) {
            var idx, refreshedInstance;
            $scope.isActionProgress(response.status) ? $timeout(function() {
                return refreshInstance(id)
            }, 2500) : (idx = findIndex(response), idx > -1 && (refreshedInstance = response, refreshedInstance.$$hashKey = $scope.instances[idx].$$hashKey, $scope.instances[idx] = refreshedInstance, $scope.selectInstance($scope.instances[idx])))
        })
    }, findIndex = function(instance) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.instances, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.title === instance.title) return idx;
        return -1
    }, findIndexX = function(instance) {
        var idx, item, _i, _len, _ref , _j, _len2, _ref2;
        _ref = $scope.instances;
        for ( idx = _i = 0, _len = _ref.length; _len > _i; idx = _i++){
        	_ref2 = _ref[idx].children.child;
        	for (_j = 0, _len2 = _ref2.length; _len2 > _j; _j++){
        		item = _ref2[_j];
        		if (item.id === instance.id) return idx;
    		}
		}
        return -1
    }, findIndexY = function(instance) {
    	var item, _i, _len, _ref , _j, _len2, _ref2;
    	_ref = $scope.instances
        for (_i = 0, _len = _ref.length; _len > _i; _i++){
        	_ref2 = _ref[_i].children.child;
        	for (_j = 0, _len2 = _ref2.length; _len2 > _j; _j++){
        		item = _ref2[_j];
        		if (item.id === instance.id) return _j;
    		}
		}
        return -1
    }, createBackup = function(router) {
    	router.title = $scope.instance.id;
        return ComputeServersSnapShotAPI.save(router, function(response) {
            logger.logSuccess("snapshot이 생성되었습니다.")
        })
    }, restoreInstance = function(instance) {
    	instance.name = "";
        return ComputeServersSnapShotAPI.save(instance, function(response) {
            logger.logSuccess("인스턴스가 생성되었습니다."), getInstances({
//                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshInstance(response.id)
                }, 1e3)
            })
        })
    }, createFloatingIp = function(floatingip, instance) {
        return NetworkingFloatingIpsAPI.save(floatingip, function(response) {
            var idx, item, key, list, _ref;
            if (response.success) {
                if (logger.log("유동 아이피 연결에 성공하였습니다."), idx = findIndex(instance), idx > -1 && $scope.instances[idx].addresses && $scope.instances[idx].addresses.addresses) {
                    _ref = $scope.instances[idx].addresses.addresses;
                    for (key in _ref) list = _ref[key], item = {
                        addr: response.data.floating_ip_address,
                        "OS-EXT-IPS-MAC:mac_addr": "",
                        "OS-EXT-IPS:type": "floating",
                        version: ""
                    }, list.push(item)
                }
            } else logger.logError("유동 아이피 연결에 실패하였습니다. " + response.message)
        })
    }, deleteFloatingIp = function(instance) {
        return NetworkingFloatingIpsAPI["delete"]({
            id: instance.id
        }, function(response) {
            var idx, itm, itmIdx, key, list, _i, _len, _ref;
            if (response.success) {
                if (logger.log("유동 아이피 연결해제에 성공하였습니다."), idx = findIndex(instance), idx > -1 && $scope.instances[idx].addresses && $scope.instances[idx].addresses.addresses) {
                    _ref = $scope.instances[idx].addresses.addresses;
                    for (key in _ref)
                        for (list = _ref[key], itmIdx = _i = 0, _len = list.length; _len > _i; itmIdx = ++_i) itm = list[itmIdx], response.data.floating_ip_address === itm.addr && $scope.instances[idx].addresses.addresses[key].splice(itmIdx, 1)
                }
            } else logger.logError("유동 아이피 연결해제에 실패하였습니다. " + response.message)
        })
    }, promise = null/*, getTelemetryInstance = function() {
    	if(!$scope.instance.id){
    		return null;
    	}
        $timeout.cancel(promise), $scope.instance && TelemetryInstancesAPI.get({
            instanceId: $scope.instance.id
        }, function(response) {
            var telemetryInstance;
            telemetryInstance = response, 
            $scope.telemetryInstance = telemetryInstance, 
            $scope.instanceUsedCpuPer = telemetryInstance.usedCpuPer, 
            $scope.instanceUsedMemoryPer = telemetryInstance.usedMemoryPer
        }), "/instances/index" === $location.path() && (promise = $timeout(getTelemetryInstance, 5e3))
    },*/ $scope.instances,$scope.isActionProgress = function(status) {
        return status && $scope.instance.type != "vnf" && "undefined" == typeof STATUS_CHOICES[status.toLowerCase()] ? !0 : !1
    }, $scope.instanceStatusClass = function(instance) {
        var clazz;
        return clazz = "ACTIVE" === instance.status ? "label label-success" : "label label-danger"
    }, $scope.instance, $scope.selectInstance = function(instance) {
        return $scope.instance = instance
    }, $scope.isSelected = function(instance) {
        return $scope.instance === instance
    }, $scope.telemetryInstance, $scope.instanceUsedCpuPer = 0, $scope.instanceUsedMemoryPer = 0, $scope.createInstanceForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/create.html",
            controller: "CreateInstanceModalCtrl",
            size: "lg"
        }), modalInstance.result.then(function(instance) {
            createInstance(instance)
        }, function() {})
    }, $scope.deleteInstances = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/confirm.html",
            controller: function($scope, $modalInstance, instance) {
            	$scope.instance = instance;
                $scope.ok = function() {
                    $modalInstance.close(instance)
                }, $scope.cancel = function() {
                    $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                instance: function() {
                    return $scope.instance
                }
            }
        }), modalInstance.result.then(function(instance) {
            deleteInstances(instance)
        }, function() {})
    }, $scope.deleteInstance = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/confirm.html",
            controller: function($scope, $modalInstance, instance) {
                $scope.ok = function() {
                    $modalInstance.close(instance)
                }, $scope.cancel = function() {
                    $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                instance: function() {
                    return $scope.instance
                }
            }
        }), modalInstance.result.then(function(instance) {
            deleteInstance(instance)
        }, function() {})
    }, $scope.instanceAction = function(action, instance) {
    	
        return ComputeServerActionsAPI.action({
            action: action
        }, instance, function(response) {
            response.success ? (logger.log("정상처리 되었습니다."), ((instance.type!= null && instance.type == "vnf") || action=="SCALE_OUT" || action=="SCALE_IN") ?getInstanceGroup(instance): getInstance(instance)) : logger.logError("처리를 실패하였습니다. " + response.fault)
        })
    }, $scope.createBackupForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/create_snapshot.html",
            controller: "CreateRouterModalCtrl"
        }), modalInstance.result.then(function(backup) {
        	createBackup(backup)
        }, function() {})
    }, $scope.createRestoreForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/confirm2.html",
            controller: function($scope, $modalInstance, instance) {
                $scope.ok = function() {
                    $modalInstance.close(instance)
                }, $scope.cancel = function() {
                    $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                instance: function() {
                    return $scope.instance
                }
            }
        }), modalInstance.result.then(function(instance) {
            restoreInstance(instance)
        }, function() {})
    }, $scope.liveMigrationForm = function(instance) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/live_migration.html",
            controller: "LiveMigrationModalCtrl",
            resolve: {
                instance: function() {
                    return instance
                }
            }
        }), modalInstance.result.then(function(instance) {
        	getInstanceListener(instance);
        }, function() {})
    }, $scope.consoleForm = function(instance) {
    	var param = new Object();
    	param.id = instance.id;
    	return ComputeConsoleAPI.get(param, function(response) {
            $window.open(response.url, "", "width=640, height=480");
        })
    },$scope.createFloatingIPForm = function(instance) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/create_floatingips.html",
            controller: "CreateFloatingIPModalCtrl",
            resolve: {
                instance: function() {
                    return instance
                }
            }
        }), modalInstance.result.then(function(floatingip) {
            createFloatingIp(floatingip, instance)
        }, function() {})
    }, $scope.deleteFloatingIPForm = function(instance) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/disassociate.html",
            controller: function($scope, $modalInstance, instance) {
                var instance_name;
                instance_name = instance.name ? instance.name : instance.id.substring(0, 8), $scope.msg = instance_name, $scope.ok = function() {
                    return $modalInstance.close(instance)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                instance: function() {
                    return instance
                }
            }
        }), modalInstance.result.then(function(instance) {
            deleteFloatingIp(instance)
        }, function() {})
    }, $scope.$watch("instance", function(newVal, oldVal) {
        return void 0
    }), $scope.$on("$destroy", function() {
        return $timeout.cancel(promise)
    })
}]).controller("CreateInstanceModalCtrl", ["$scope", "$modalInstance", "$q", "ComputeLimitAPI", "ComputeOnBoardAPI", "ComputeImagesAPI", "ComputeAvailabilityZonesAPI", "InstanceLimitService", "NetworkingNetworkAPI", function($scope, $modalInstance, $q, ComputeLimitAPI, ComputeOnBoardAPI, ComputeImagesAPI, ComputeAvailabilityZonesAPI, InstanceLimitService, NetworkingNetworkAPI) {
    var getLimit, original, percentage;
    $scope.instance = {
        availability_zone: null,
        flavor: null,
        usebackup: {use : false}
        
    }, original = angular.copy($scope.instance), getLimit = function(limit) {
        return ComputeLimitAPI.get(limit, function(response) {
            return $scope.limit = response, InstanceLimitService.init(response), $scope.limit = InstanceLimitService.limit, $scope.instances = InstanceLimitService.instances, $scope.vcpus = InstanceLimitService.vcpus, $scope.ram = InstanceLimitService.ram
        })
    }, getLimit(), $scope.revert = function() {
        $scope.instance = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.instance, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.instance, original) //&& $scope.selected_networks.length > 0
    }, $scope.submit = function(instance) {
//        instance.flavor && (instance.flavorRef = instance.flavor.id), instance.key_name = "eluon-key", instance.availability_zone && (instance.availability_zone = instance.availability_zone.id)
         $modalInstance.close(instance), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.changeFlavor = function(flavor) {
    	$scope.instance.flavor = flavor;
//        $scope.allProgress()
    }, $scope.changeZone = function(zone) {
    	$scope.instance.availability_zone = zone;
    }, $scope.init = function() {
        return $q.all([ComputeOnBoardAPI.query().$promise, ComputeAvailabilityZonesAPI.query().$promise]).then(function(result) {
            var availability_zones, flavors ;
            return flavors = result[0], $scope.flavors = flavors, flavors.length > 0 && ($scope.instance.flavor = flavors[0]), availability_zones = result[1], $scope.availability_zones = availability_zones, availability_zones.length > 0 && ($scope.instance.availability_zone = availability_zones[0])//, $scope.allProgress()
        })
    }, $scope.init()
//    , percentage = function(max, used) {
//        return Math.round(used / max * 100)
//    }, $scope.calProgress = function(max, used, add) {
//        var addPer, addType, per, progressbars;
//        return progressbars = [], per = percentage(max, used), progressbars.push({
//            value: per,
//            type: "success"
//        }), addPer = percentage(max, used + add), addType = "info", addPer > 70 && 90 >= addPer ? addType = "waring" : addPer > 90 && (addType = "danger"), addPer > 100 && (addPer = 100), addPer -= per, progressbars.push({
//            value: addPer,
//            type: addType
//        }), progressbars
//    }, $scope.allProgress = function() {
//        $scope.calInstanceProgress(), $scope.calVcpusProgress(), $scope.calRamsProgress()
//    }, $scope.calInstanceProgress = function() {
//        var add, max, progress, used;
//        max = $scope.instances.max, used = $scope.instances.used, add = 1, progress = $scope.calProgress(max, used, add), $scope.instanceStacked ? ($scope.instanceStacked[1].value = progress[1].value, $scope.instanceStacked[1].type = progress[1].type) : $scope.instanceStacked = progress
//    }, $scope.calVcpusProgress = function() {
//        var add, max, progress, used;
//        max = $scope.vcpus.max, used = $scope.vcpus.used, add = $scope.instance.flavor.vcpus, progress = $scope.calProgress(max, used, add), $scope.vcpusStacked ? ($scope.vcpusStacked[1].value = progress[1].value, $scope.vcpusStacked[1].type = progress[1].type) : $scope.vcpusStacked = progress
//    }, $scope.calRamsProgress = function() {
//        var add, max, progress, used;
//        max = $scope.ram.max, used = $scope.ram.used, add = $scope.instance.flavor.ram, progress = $scope.calProgress(max, used, add), $scope.ramsStacked ? ($scope.ramsStacked[1].value = progress[1].value, $scope.ramsStacked[1].type = progress[1].type) : $scope.ramsStacked = progress
//    }
}]).controller("CreateRouterModalCtrl", ["$scope", "$modalInstance", "$q", function($scope, $modalInstance, $q) {
    var original;
    $scope.router = {
        name: null
    }, original = angular.copy($scope.router), $scope.revert = function() {
        $scope.router = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.router, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.router, original)
    }, $scope.submit = function(router) {
        $modalInstance.close(router), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]).controller("LiveMigrationModalCtrl", ["$timeout", "$route","$rootScope", "$scope", "$modalInstance", "$modal", "$q", "logger", "LiveMigrationInstanceListAPI", "LiveMigrationRequestAPI", "instance", 
function($timeout, $route, $rootScope, $scope, $modalInstance, $modal, $q, logger, LiveMigrationInstanceListAPI, LiveMigrationRequestAPI, instance) {
    
	$scope.show = "default";
	$scope.instanceId = instance.id;
	$scope.localHypervisor = instance['OS-EXT-SRV-ATTR:hypervisor_hostname'];
//	$scope.zoneName = instance['OS-EXT-AZ:availability_zone'];
//	$scope.localHypervisor = instance.hypervisor_hostname;
	$scope.HypervisorList =[];
	
	$scope.cancel = function() {
        $modalInstance.dismiss("cancel");
        //$route.reload();
    };
	
    
    $scope.init = function() {
    	var param = new Object();
    	param.zoneName = instance['OS-EXT-AZ:availability_zone'];
      $q.all([LiveMigrationInstanceListAPI.query(param).$promise]).then(function(result) {        	
    	  $scope.HypervisorList =[];
    	  for(var i=0; i < 12; i++){
    		  if(result[0][0][i] != null){
	    		  if(result[0][0][i].hypervisor != $scope.localHypervisor){
	    			  $scope.HypervisorList.push(result[0][0][i]);
	    		  }
    		  }
    	  }
        })
    };
    
    $scope.init();
    
    
	$scope.reqData = {
    		"instanceId": $scope.instanceId,
        	"hyperId": null,
        	"blockmigration":false
    };
	
	$scope.submit = function(){
		  $scope.show="progress";
		  $q.all([LiveMigrationRequestAPI.get($scope.reqData).$promise]).then(function(result) {        	
	        	
	    	  if(result[0].result == true){
	    		  $scope.show="success";
	    	      
          		$timeout(function () {
          			$route.reload();
        		}, 1000);	    		  
	    		  
	    	  } else if(result[0].result == false){
	    		  $scope.show="fail";
	    		  $scope.message = result[0].message;
	    	  } else{
	    		  $scope.show="error";
	    	  }
	        }
		  , function(reason) {
			  // error: handle the error if possible and
			  //        resolve promiseB with newPromiseOrValue,
			  //        otherwise forward the rejection to promiseB
			  $scope.show="error";
/*			  if (canHandle(reason)) {
			   // handle the error and recover
				  console.log('test2 : ', reason);
			   return newPromiseOrValue;
			  }
			  console.log('test3 : ', reason);
			  return $q.reject(reason);*/
			}		  
		  )
		
	};
    
	 $scope.canSubmit = function() {
		 var submitCheck = false;
		 
		 if($scope.reqData.hyperId != null /*&& $scope.reqData.blockmigration*/){
			 submitCheck = true;
		 }
		 
		 if($scope.show != "default"){
			 submitCheck = false;
		 }
		 
		 return submitCheck
	    };
	
}]).controller("ConsoleModalCtrl", ["$scope", "$modalInstance", "$q", "instance", "ComputeConsoleAPI", function($scope, $modalInstance, $q, instance, ComputeConsoleAPI) {
    var original;
    $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return ComputeConsoleAPI.query(instance, function(response) {
        })
    }, $scope.init()
}]).controller("CreateFloatingIPModalCtrl", ["$rootScope", "$scope", "$modalInstance", "$modal", "$q", "logger", "NetworkingFloatingIpsAPI", "ComputeFloatingIpPoolsAPI", "ComputeServersAPI", "NetworkingPortAPI", "instance", 
function($rootScope, $scope, $modalInstance, $modal, $q, logger, NetworkingFloatingIpsAPI, ComputeFloatingIpPoolsAPI, ComputeServersAPI, NetworkingPortAPI, instance) {
    var allocatePool, getFloatingIps, original;
    $scope.floatingIps = [], 
    $scope.instances = [], 
    $scope.floatingip = {
        ipAddress: null,
        port: null,
        port_id: null,
        id: null
    }, 
    original = angular.copy($scope.floatingip), 
    
    $scope.revert = function() {
        $scope.network = angular.copy(original), $scope.form.$setPristine()
    }, 
    
    $scope.canRevert = function() {
        return !angular.equals($scope.network, original) || !$scope.form.$pristine
    }, 
    
    $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.network, original)
    }, 
    
    $scope.submit = function(floatingip) {
        floatingip.id = floatingip.ipAddress.id, 
        floatingip.port_id = floatingip.port.id, 
        floatingip.ipAddress = null, 
        floatingip.port = null, 
        $modalInstance.close(floatingip), $scope.revert()
    }, 
    
    $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, 
    
    
    $scope.$on('someEvent', function(event, data) { console.log(data); });
    $rootScope.$on('someEvent', function(event, args) {console.log(data); });
    
    $rootScope.$on("CallInitPage", function(){
    	console.log('CallInitPage request ::');
        $scope.init();
     });
    
    $scope.init = function() {
        return $q.all([NetworkingFloatingIpsAPI.query().$promise, ComputeServersAPI.query().$promise, NetworkingPortAPI.query().$promise]).then(function(result) {
        	var floatingIps, itm, port, ports, server, servers, tmpInstance, _i, _len, _results;
            for (floatingIps = result[0], $scope.floatingIps = function() {
                    var _i, _len, _results;
                    for (_results = [], _i = 0, _len = floatingIps.length; _len > _i; _i++) itm = floatingIps[_i], null === itm.port_id && _results.push(itm);
                    return _results
                }(), servers = result[1], ports = result[2], _results = [], _i = 0, _len = servers.length; _len > _i; _i++) server = servers[_i], _results.push(function() {
                var _j, _len1, _results1;
                for (_results1 = [], _j = 0, _len1 = ports.length; _len1 > _j; _j++) port = ports[_j], server.id === port.device_id ? (tmpInstance = {
                    id: port.id,
                    name: server.name + " : " + port.fixed_ips[0].ip_address
                }, $scope.instances.push(tmpInstance), _results1.push(instance.id === server.id ? $scope.floatingip.port = tmpInstance : void 0)) : _results1.push(void 0);
                return _results1
            }());
            return _results
        })
    },
    
    $scope.init(), 
    
    getFloatingIps = function(instance) {
        return NetworkingFloatingIpsAPI.query(instance, function(response) {
            var floatingIps, itm;
            floatingIps = response, $scope.floatingIps = function() {
                var _i, _len, _results;
                for (_results = [], _i = 0, _len = floatingIps.length; _len > _i; _i++) itm = floatingIps[_i], null === itm.port_id && _results.push(itm);
                return _results
            }()
        })
    },
    
    allocatePool = function(poolName) {
        return ComputeFloatingIpPoolsAPI.save({
            poolName: poolName
        }, function(response) {
            response.success ? (logger.log("유동 아이피(" + response.data.ip + ")가 할당되었습니다."), getFloatingIps()) : logger.logError("유동 아이피가 할당에 실패하였습니다. " + response.message)
        })
    }, 
    
    $scope.createFloatingIpPoolForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/manage_floatingips_pool.html",
            controller: "CreateFloatingIpPoolModalCtrl"
        }), modalInstance.result.then(function(poolName) {
            allocatePool(poolName)
        }, function() {})
    }
}])

.controller("CreateFloatingIpPoolModalCtrl", ["$scope", "$modalInstance", "$q", "ComputeFloatingIpPoolsAPI", "NetworkingQuotasAPI", function($scope, $modalInstance, $q, ComputeFloatingIpPoolsAPI, NetworkingQuotasAPI) {
    var original, percentage;
    $scope.pools = [], $scope.pool = null, $scope.quotas = null, original = angular.copy($scope.pool), $scope.revert = function() {
        $scope.pool = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.pool, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.pool, original)
    }, $scope.submit = function(pool) {
        $modalInstance.close(pool), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([ComputeFloatingIpPoolsAPI.query().$promise, NetworkingQuotasAPI.get().$promise]).then(function(result) {
            var pools;
            return pools = result[0], $scope.pools = pools, $scope.pools.length > 0 && ($scope.pool = $scope.pools[0]), $scope.quotas = result[1], $scope.allProgress()
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
        $scope.calNetQuotasProgress()
    }, $scope.calNetQuotasProgress = function() {
        var add, max, progress, used;
        null !== $scope.quotas && (max = $scope.quotas.floatingip, used = $scope.quotas.floatingip_used, add = 1, progress = $scope.calProgress(max, used, add), $scope.quotasStacked ? ($scope.quotasStacked[1].value = progress[1].value, $scope.quotasStacked[1].type = progress[1].type) : $scope.quotasStacked = progress)
    }
}]);
    