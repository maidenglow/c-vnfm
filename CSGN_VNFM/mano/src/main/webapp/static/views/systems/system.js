
"use strict";
angular.module("app.systems", [])
.factory("ZoneManageAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.ZONES, {
    	tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    })
}]).factory("ZoneSystemManageAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.ZONE_MANAGE_HOST, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        zoneId: "@zoneId",
        name: "@name"
    })
}]).factory("SystemManageAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.SYSTEMS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    })
}]).controller("CreateZoneModalCtrl", ["$scope", "$modalInstance", "$q", function($scope, $modalInstance, $q) {
    var original;
    $scope.system = {
        name: null
    }, original = angular.copy($scope.system), $scope.revert = function() {
        $scope.system = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.system, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.system, original)
    }, $scope.submit = function(Zone) {
        $modalInstance.close(Zone), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]).controller("CreateSystemModalCtrl", ["$scope", "$modalInstance", "$q", "createInfo", "SystemManageAPI", function($scope, $modalInstance, $q, createInfo, SystemManageAPI) {
    var original, getInstances,findLIndex, findRIndex;
//    $scope.items ,$scope.selectItems,
    
    $scope.inputType = 'password';

    // Hide & show password function
    $scope.hideShowPassword = function(){
      if ($scope.inputType == 'password')
        $scope.inputType = 'text';
      else
        $scope.inputType = 'password';
    };
    
    $scope.data={
    	id : null,
    	items : [],
    	selectItems : [],
    	pass : null
    },
    $scope.revert = function() {
        $scope.data = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.data, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.data, original) && ($scope.data.items.length == 0 && $scope.data.selectItems.length == 0)
    }, $scope.submit = function(data) {
        $modalInstance.close(data), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.deleteRight = function(scope) {
            var idx, item, _i, _len, _ref;
            for ($scope.data.items.push(scope), _ref = $scope.data.selectItems, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
                if (item = _ref[idx], item === scope) {
                    $scope.data.selectItems.splice(idx, 1);
                    break;
                }
    }, $scope.deleteLeft = function(scope) {
        var idx, item, _i, _len, _ref;
        for ($scope.data.selectItems.push(scope), _ref = $scope.data.items, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item === scope) {
                $scope.data.items.splice(idx, 1);
                break;
            }
    }, $scope.init = function() {
    	 return $q.all([getInstances(createInfo).$promise]).then(function() {
            
         })
    }, getInstances = function(createInfo) {
    	return SystemManageAPI.get(createInfo, function(response) {
    		$scope.data = response;
    		$scope.data.id = createInfo.id;
    		original = angular.copy($scope.data)
    	})
    }, $scope.init()
}]).controller("SystemCtrl", ["$scope", "$modal", "logger", "$location", "$timeout", "SystemSystemsAPI", "ZoneManageAPI", "ZoneSystemManageAPI", "SystemManageAPI",
                              function($scope, $modal, logger, $location, $timeout, SystemSystemsAPI, ZoneManageAPI, ZoneSystemManageAPI, SystemManageAPI) {
    var getSystem, getSystems, interval, refreshSystem, timer, createZone, createSystem, findIndex, zoneId;
    
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
//    	        $scope.system = data;
    	        if(data.info){
    	        	$scope.system = null;
    	        	$scope.item = null;
    	        	$scope.item = data.info;
    	        	zoneId = data.zoneId;
    	        	$scope.createInfo.id = data.zoneId;
    			}else {
    				$scope.system = data;
    				$scope.item = data;
    				zoneId = data.id;
    				$scope.createInfo.id = data.id;
    			}
    	        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	$scope.selectedNode = data;
    	$scope.system = data;
    	$scope.item = data.info;
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
         	$scope.system = $scope.selectedNode;
         } else if ($scope.selectedNode != undefined && $scope.selectedNode.category && ($scope.selectedNode.id == undefined || $scope.selectedNode.id == '')) {

         	$scope.selectedNode.id = "";
         	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

         	$scope.editMode = 'XML';
            $scope.safeApply();
         }
 	}
    
    return $scope.init = function() {
        getSystems().$promise.then(function() {
            var system, systems;
            
            systems = $scope.systems, systems && systems.length > 0 && (system = systems[0],$scope.item=system, $scope.selectItem(system), zoneId = system.id, $scope.createInfo.id = system.id, refreshSystem())
        })
    }, getSystems = function(system) {
        return SystemSystemsAPI.query(system, function(response) {
            $scope.systems = response
        })
    }, getSystem = function(system) {
    	var param = new Object();
    	param.id = system.id;
        return SystemSystemsAPI.get(param, function(response) {
            $scope.system = response
        })
    }, timer = null, interval = 3e3, refreshSystem = function() {
        return $timeout.cancel(timer), "/systems/index" === $location.path() ? (getSystem($scope.system)/*, timer = $timeout(refreshSystem, interval)*/) : void 0
    }, $scope.systems, $scope.itemStatusClass = function(system) {
        return "Y" === system.status ? "label label-success" : "label label-danger"
    }, $scope.system, $scope.item, $scope.createInfo = {
            id: null,
            hosts: []
    },$scope.selectItem = function(system) {
//    	console("system======="+system);
        return $scope.system = system
    }, $scope.isSelected = function(system) {
        return $scope.system === system
    }, createZone = function(system) {
        return ZoneManageAPI.save(system, function(response) {
        	response.success ?(logger.logSuccess("zone이 생성되었습니다."),
            $scope.systems = null,
            getSystems().$promise.then(function() {
                return $timeout(function() {
//                    return refreshSystem()
                }, 1e3)
            })): logger.logError(response.fault);
        })
    }, createSystem = function(data) {
        return SystemManageAPI.save(data, function(response) {
        	response.success ?(logger.logSuccess("host 정보를 변경하였습니다."),
            $scope.systems = null,
            getSystems(null).$promise.then(function() {
                return $timeout(function() {
                    return refreshSystem()
                }, 1e3)
            })): logger.logError(response.fault);
        })
    }, findIndex = function(system) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.systems, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === system.id) return idx;
        return -1
    }, $scope.deleteSystem = function(item) {
    	var param = new Object();
    	
    	param.zoneId = zoneId;
    	param.name = item.hypervisor_hostname;
        return ZoneSystemManageAPI["delete"](param, function(response) {
        	$scope.systems = null,
            getSystems(null).$promise.then(function() {
                return $timeout(function() {
                    return refreshSystem()
                }, 1e3)
            })
//            var idx;
//            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(system), $scope.systems.splice(idx, 1), $scope.systems.length > 0 && $scope.selectItem($scope.systems[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, $scope.deleteZone = function(system) {
    	if(!$scope.system){
    		logger.log("zone을 선택해 주세요")
    		return;
    	}
        return ZoneManageAPI["delete"](system, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(system), $scope.systems.splice(idx, 1), $scope.systems.length > 0 && $scope.selectItem($scope.systems[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, $scope.createZoneForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/systems/create.html",
            controller: "CreateZoneModalCtrl"
        }), modalInstance.result.then(function(system) {
        	createZone(system)
        }, function() {})
    }, $scope.manageSystemForm = function(createInfo) {
        var modalInstance,modalInstance2;
        modalInstance = $modal.open({
            templateUrl: "views/systems/create_hypervisor.html",
            controller: "CreateSystemModalCtrl",
            resolve: {
            	createInfo: function() {
                    return createInfo
                }
            }
        }), modalInstance.result.then(function(response) {
//        	createSystem(response)
        	modalInstance2 = $modal.open({
        		templateUrl: "views/systems/create2.html",
                controller: "CreateSystemModalCtrl",
                resolve: {
                	createInfo: function() {
                        return createInfo
                    }
                }
            }), modalInstance2.result.then(function(response1) {
            	response.pass = response1.pass;
            	createSystem(response)
            }, function() {})
        }, function() {})
    }, $scope.$on("$destroy", function() {
        return $timeout.cancel(timer)
    })
}]);
