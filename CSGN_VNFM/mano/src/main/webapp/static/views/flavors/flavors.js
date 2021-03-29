"use strict";
angular.module("app.flavors", [])
.factory("flavorsService", ["$http", "CONST_RESTFUL_API", function($http, CONST_RESTFUL_API) {
    return {
        getList: function() {
            return $http.get(CONST_RESTFUL_API.prefixURL + "/flavors/list.do").success(function(data) {
                return data
            }).error(function(data, status, headers, config) {
                return void 0
            })
        }
    }
}]).factory("FlavorAccessAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.FLAVORS_ACCESS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    })
}]).factory("FlavorExtrasAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.FLAVORS_EXTRAS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id" 
    }, {
        "delete": {
            method: "delete",
            params: {
                key: "@key"
            }
        }
    })
}]).factory("TenantsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.IDENTITY_URL.TENANTS, {})
}]).controller("FlavorsCtrl", ["$scope", "$modal", "$http", "$q", "logger", "$timeout", "flavorsService", "ComputeFlavorsAPI", 
                               function($scope, $modal, $http, $q, logger, $timeout, flavorsService, ComputeFlavorsAPI) {
    var createFlavor, deleteFlavor, findIndex, getFlavor, getFlavors, refreshFlavor, updateFlavor;
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
    	        $scope.selectedNode = data.info;
    	        $scope.flavor = data.info;
    	        $scope.flavor.spec = data.spec;
    	        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	$scope.selectedNode = data;
    	$scope.flavor = data;
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
         	$scope.flavor = $scope.selectedNode;
         } else if ($scope.selectedNode != undefined && $scope.selectedNode.category && ($scope.selectedNode.id == undefined || $scope.selectedNode.id == '')) {

         	$scope.selectedNode.id = "";
         	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

         	$scope.editMode = 'XML';
             $scope.safeApply();
         }
 	},  
    
    $scope.flavors, $scope.flavor, $scope.selectItem = function(flavor) {
        return $scope.flavor = flavor
    }, $scope.isSelected = function(flavor) {
        return $scope.flavor === flavor
    }, $scope.init = function() {
        return $q.all([getFlavors().$promise]).then(function(result) {
            var flavors;
            return flavors = result[0], $scope.flavors = flavors, flavors && flavors[0] ? $scope.selectItem(flavors[0]) : void 0
        })
    }, getFlavors = function(flavor) {
        return ComputeFlavorsAPI.query(flavor, function(response) {
            $scope.flavors = response
        })
    }, getFlavor = function(flavor) {
        return ComputeFlavorsAPI.get(flavor, function(response) {
            var idx, newflavor;
            $scope.flavor = response, idx = findIndex(flavor), idx > -1 ? (newflavor = response, newflavor.$$hashKey = $scope.flavors[idx].$$hashKey, $scope.flavors[idx] = newflavor) : $scope.flavors.splice(0, 0, response)
        })
    }, createFlavor = function(flavor) {
        return ComputeFlavorsAPI.save(flavor, function(response) {
            logger.logSuccess("Flavor가 생성되었습니다."), getFlavor({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshFlavor(response.id)
                }, 1e3)
            })
        })
    }, updateFlavor = function(flavor) {
        ComputeFlavorsAPI.update(flavor, function() {
            var idx;
            logger.logSuccess("Flavor가 수정되었습니다."), idx = findIndex(flavor), $scope.flavor = flavor, flavor.$$hashKey = $scope.flavors[idx].$$hashKey, $scope.flavors[idx] = flavor
        })
    }, deleteFlavor = function(flavor) {
        return ComputeFlavorsAPI["delete"](flavor, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(flavor), $scope.flavors.splice(idx, 1), $scope.flavors.length > 0 && $scope.selectItem($scope.flavors[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, refreshFlavor = function(id) {
        return ComputeFlavorsAPI.get({
            id: id
        }, function(response) {
            var flavor, idx;
            idx = findIndex(response), flavor = response, flavor.$$hashKey = $scope.flavors[idx].$$hashKey, idx > -1 && ($scope.flavors[idx] = flavor), $scope.selectItem($scope.flavors[idx])
        })
    }, findIndex = function(flavor) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.flavors, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === flavor.id) return idx;
        return -1
    }, $scope.createFlavorForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/create.html",
            controller: "CreateFlavorModalCtrl"
        }), modalInstance.result.then(function(flavor) {
            createFlavor(flavor)
        }, function() {})
    }, $scope.deleteFlavorForm = function(flavor) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/confirm.html",
            controller: function($scope, $modalInstance, flavor) {
                $scope.ok = function() {
                    return $modalInstance.close(flavor)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                flavor: function() {
                    return flavor
                }
            }
        }), modalInstance.result.then(function(flavor) {
            deleteFlavor(flavor)
        }, function() {})
    }, $scope.editFlavorForm = function(flavor) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/edit.html",
            controller: "EditFlavorModalCtrl",
            resolve: {
                flavor: function() {
                    return flavor
                }
            }
        }), modalInstance.result.then(function(flavor) {
            updateFlavor(flavor)
        }, function() {})
    }, $scope.getDetailFlavorExtrasForm = function(flavor) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/detail_extras.html",
            controller: "DetailFlavorExtrasModalCtrl",
            size: "lg",
            resolve: {
                flavor: function() {
                    return flavor
                }
            }
        }), modalInstance.result.then(function() {}, function() {})
    }
}]).controller("CreateFlavorModalCtrl", ["$scope", "$modalInstance", "$q", "flavorsService", "TenantsAPI", function($scope, $modalInstance, $q, flavorsService, TenantsAPI) {
    var findIndex, original;
    $scope.flavor = {
        name: null,
        id: null,
        ram: null,
        vcpus: null,
        disk: null,
        ephemeral: null,
        swap: null,
        projects: []
    }, $scope.search = {
        project: "",
        allProject: ""
    }, original = angular.copy($scope.flavor), $scope.revert = function() {
        $scope.flavor = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.flavor, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.flavor, original)
    }, $scope.submit = function(flavor) {
        flavor["OS-FLV-EXT-DATA:ephemeral"] = flavor.ephemeral, flavor["public"] = !0, flavor.projects.length > 0 && (flavor["public"] = !1), $modalInstance.close(flavor), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([TenantsAPI.get().$promise]).then(function(result) {
            var tenants;
            return tenants = result[0], $scope.allprojects = tenants.tenants
        })
    }, $scope.init(), findIndex = function(project) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.flavor.projects, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === project.id) return idx;
        return -1
    }, $scope.addProject = function(item) {
        $scope.flavor.projects.push(item)
    }, $scope.removeProject = function(item) {
        var idx;
        idx = findIndex(item), idx >= 0 && $scope.flavor.projects.splice(idx, 1)
    }, $scope.searchAllProjects = function(item) {
        var project, _i, _len, _ref;
        for (_ref = $scope.flavor.projects, _i = 0, _len = _ref.length; _len > _i; _i++)
            if (project = _ref[_i], item.id === project.id) return !1;
        return 0 === $scope.search.allProject.length ? !0 : item.name.indexOf($scope.search.allProject) >= 0
    }, $scope.searchProjects = function(item) {
        return 0 === $scope.search.project.length ? !0 : item.name.indexOf($scope.search.project) >= 0
    }
}]).controller("EditFlavorModalCtrl", ["$scope", "$modalInstance", "$q", "flavor", "FlavorAccessAPI", "TenantsAPI", function($scope, $modalInstance, $q, flavor, FlavorAccessAPI, TenantsAPI) {
    var findIndex, flavorAccessReq, original;
    $scope.flavor = angular.copy(flavor), $scope.flavor.ephemeral = flavor["OS-FLV-EXT-DATA:ephemeral"], $scope.flavor.projects || ($scope.flavor.projects = []), $scope.search = {
        project: "",
        allProject: ""
    }, original = angular.copy($scope.flavor), $scope.revert = function() {
        $scope.flavor = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid
    }, $scope.submit = function(flavor) {
        flavor["OS-FLV-EXT-DATA:ephemeral"] = flavor.ephemeral, flavor["public"] = !0, flavor.projects.length > 0 && (flavor["public"] = !1), $modalInstance.close(flavor), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, flavorAccessReq = {
        id: flavor.id
    }, $scope.init = function() {
        return $q.all([TenantsAPI.get().$promise, FlavorAccessAPI.get(flavorAccessReq).$promise]).then(function(result) {
            var myproject, project, projectList, tenants, _i, _j, _len, _len1, _ref;
            for (tenants = result[0], $scope.allprojects = tenants.tenants, $scope.flavor.projects = [], projectList = result[1].flavor_access ? result[1].flavor_access : [], _ref = $scope.allprojects, _i = 0, _len = _ref.length; _len > _i; _i++)
                for (project = _ref[_i], _j = 0, _len1 = projectList.length; _len1 > _j; _j++) myproject = projectList[_j], project.id === myproject.tenant_id && $scope.flavor.projects.push(project);
            return void 0
        })
    }, $scope.init(), findIndex = function(project) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.flavor.projects, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === project.id) return idx;
        return -1
    }, $scope.addProject = function(item) {
        $scope.flavor.projects.push(item)
    }, $scope.removeProject = function(item) {
        var idx;
        idx = findIndex(item), idx >= 0 && $scope.flavor.projects.splice(idx, 1)
    }, $scope.searchAllProjects = function(item) {
        var project, _i, _len, _ref;
        for (_ref = $scope.flavor.projects, _i = 0, _len = _ref.length; _len > _i; _i++)
            if (project = _ref[_i], item.id === project.id) return !1;
        return 0 === $scope.search.allProject.length ? !0 : item.name.indexOf($scope.search.allProject) >= 0
    }, $scope.searchProjects = function(item) {
        return 0 === $scope.search.project.length ? !0 : item.name.indexOf($scope.search.project) >= 0
    }
}]).controller("DetailFlavorExtrasModalCtrl", ["$scope", "$modal", "$modalInstance", "$q", "logger", "flavor", "FlavorExtrasAPI", function($scope, $modal, $modalInstance, $q, logger, flavor, FlavorExtrasAPI) {
    var createFlavorExtras, deleteFlavorExtras, extras_request, getFlavorExtras, original, updateFlavorExtras;
    $scope.extras = null, $scope.flavor = angular.copy(flavor), original = angular.copy($scope.flavor), $scope.revert = function() {
        $scope.flavor = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.flavor, original)
    }, $scope.submit = function(flavor) {
        $modalInstance.close(flavor), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, extras_request = {
        id: flavor.id
    }, $scope.init = function() {
        return $q.all([FlavorExtrasAPI.get(extras_request).$promise]).then(function(result) {
            return $scope.extras = result[0]
        })
    }, $scope.init(), getFlavorExtras = function(flavor) {
        return FlavorExtrasAPI.get(flavor, function(response) {
            $scope.extras = response
        })
    }, createFlavorExtras = function(extras) {
        return FlavorExtrasAPI.save(extras, function() {
            logger.logSuccess("Flavor 추가 사양이 생성되었습니다."), getFlavorExtras(flavor)
        })
    }, updateFlavorExtras = function(extras) {
        return FlavorExtrasAPI.save(extras, function() {
            logger.logSuccess("Flavor 추가 사양이 수정되었습니다."), getFlavorExtras(flavor)
        })
    }, deleteFlavorExtras = function(key) {
        var extras;
        return extras = {
            id: flavor.id,
            key: key
        }, FlavorExtrasAPI["delete"](extras, function() {
            logger.logSuccess("Flavor 추가 사양이 삭제되었습니다."), getFlavorExtras(flavor)
        })
    }, $scope.getKeys = function(obj) {
        var key, ret, val;
        ret = [];
        for (key in obj) val = obj[key], 0 !== key.indexOf("$") && ret.push(key);
        return ret
    }, $scope.CreateFlavorExtrasForm = function(flavor) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/create_extras.html",
            controller: "CreateFlavorExtrasModalCtrl",
            resolve: {
                flavor: function() {
                    return flavor
                }
            }
        }), modalInstance.result.then(function(extras) {
            createFlavorExtras(extras)
        }, function() {})
    }, $scope.UpdateFlavorExtrasForm = function(flavor, key, val) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/edit_extras.html",
            controller: "EditFlavorExtrasModalCtrl",
            resolve: {
                extras: function() {
                    return {
                        id: flavor.id,
                        key: key,
                        value: val
                    }
                }
            }
        }), modalInstance.result.then(function(extras) {
            updateFlavorExtras(extras)
        }, function() {})
    }, $scope.DeleteFlavorExtrasForm = function(key) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/flavors/confirm.html",
            controller: function($scope, $modalInstance) {
                $scope.ok = function() {
                    return $modalInstance.close(key)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                flavor: function() {
                    return flavor
                }
            }
        }), modalInstance.result.then(function(key) {
            deleteFlavorExtras(key)
        }, function() {})
    }
}]).controller("CreateFlavorExtrasModalCtrl", ["$scope", "$modalInstance", "$q", "flavor", "FlavorExtrasAPI", function($scope, $modalInstance, $q, flavor) {
    var original;
    $scope.extras = {
        id: flavor.id,
        keys: null
    }, original = angular.copy($scope.extras), $scope.revert = function() {
        $scope.flavor = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid
    }, $scope.submit = function(extras) {
        $modalInstance.close(extras), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {
            return $scope.keylists = [{
                name: "Quota: Read bytes",
                value: "quota:read_bytes_sec"
            }, {
                name: "Quota: Write bytes",
                value: "quota:write_bytes_sec"
            }, {
                name: "Quota: CPU",
                value: "quota:cpu_quota"
            }, {
                name: "Quota: CPU period",
                value: "quota:cpu_period"
            }, {
                name: "Quota: Inbound average",
                value: "quota:inbound_average"
            }, {
                name: "Quota: Outbound average",
                value: "quota:outbound_average"
            }, {
                name: "다른키",
                value: "custom"
            }], $scope.keylists.length > 0 ? $scope.extras.keys = $scope.keylists[0].value : void 0
        })
    }, $scope.init()
}]).controller("EditFlavorExtrasModalCtrl", ["$scope", "$modalInstance", "$q", "extras", "FlavorExtrasAPI", function($scope, $modalInstance, $q, extras) {
    var original;
    $scope.extras = extras, $scope.extras.keys = "custom", original = angular.copy($scope.extras), $scope.revert = function() {
        $scope.flavor = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canSubmit = function() {
        return $scope.form.$valid
    }, $scope.submit = function(extras) {
        $modalInstance.close(extras), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]);
