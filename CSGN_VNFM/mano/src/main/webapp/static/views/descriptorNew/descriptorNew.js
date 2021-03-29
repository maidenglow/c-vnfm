"use strict";
angular.module("app.descriptorsNew", [])
.directive("treeModel",function($compile){
	return{restrict:"A",link:function(b,h,c){
		var a=c.treeId,g=c.treeModel,e=c.nodeLabel||"label",d=c.nodeChildren||"children",e='<ul><li data-ng-repeat="node in '+g+'"><i class="collapsed" data-ng-show="node.'+d+'.length && node.collapsed" data-ng-click="'+a+'.selectNodeHead(node)"></i><i class="expanded" data-ng-show="node.'+d+'.length && !node.collapsed" data-ng-click="'+a+'.selectNodeHead(node)"></i><i class="normal" data-ng-hide="node.'+
d+'.length"></i> <span data-ng-class="node.selected" data-ng-click="'+a+'.selectNodeLabel(node)">{{node.'+e+'}}</span><div data-ng-hide="node.collapsed" data-tree-id="'+a+'" data-tree-model="node.'+d+'" data-node-id='+(c.nodeId||"id")+" data-node-label="+e+" data-node-children="+d+"></div></li></ul>";a&&g&&(c.angularTreeview&&(b[a]=b[a]||{},b[a].selectNodeHead=b[a].selectNodeHead||function(a){a.collapsed=!a.collapsed},b[a].selectNodeLabel=b[a].selectNodeLabel||function(c){b[a].currentNode&&b[a].currentNode.selected&&
(b[a].currentNode.selected=void 0);
c.selected="selected";
b[a].currentNode=c}),
h.html('').append($compile(e)(b)))}
}})
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
}]).factory("ComputeOnBoardAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VNFD_URL.DESCRIPTORS, {
        tenantId: CONST_RESTFUL_API.VNFD_URL.TENANT_ID,
        name: "@name"
    })
}]).controller("descriptorViewCtrl", ["$scope", "logger", "$q", "$timeout", "$modal", "flavorsService",
                                  "CONST_RESTFUL_API","ComputeOnBoardAPI","$rootScope",
                                  function($scope, logger, $q, $timeout, $modal,flavorsService,
                                		  CONST_RESTFUL_API,ComputeOnBoardAPI,$rootScope ) {
	var createDescriptor, deleteDescriptor, findIndex, getDescriptor, getDescriptors/*, refreshFlavor*/ ;

	
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
    	        $scope.onboard = data;
    	        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	$scope.selectedNode = data;
    	$scope.onboard = data;
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
         	$scope.onboard = $scope.selectedNode;
         } else if ($scope.selectedNode != undefined && $scope.selectedNode.category && ($scope.selectedNode.id == undefined || $scope.selectedNode.id == '')) {

         	$scope.selectedNode.id = "";
         	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

         	$scope.editMode = 'XML';
             $scope.safeApply();
         }
 	},                 
	$scope.onboards, $scope.onboard, $scope.selectItem = function(onboard) {
        return $scope.onboard = onboard
    }, $scope.isSelected = function(onboard) {
        return $scope.onboard === onboard
    }, $scope.init = function() {
        return $q.all([getDescriptors().$promise]).then(function(result) {
            var onboards;
            return onboards = result[0], $scope.onboards = onboards, onboards && onboards[0] ? $scope.selectItem(onboards[0]) : void 0
        })
    }, getDescriptors = function(onboard) {
        return ComputeOnBoardAPI.query(onboard, function(response) {
        	var onboards;
            $scope.onboards = response,onboards = response, onboards && onboards[0] ? $scope.selectItem(onboards[0]) : void 0
        })
    }, getDescriptor = function(onboard) {
        return ComputeOnBoardAPI.get(onboard, function(response) {
            var idx, newonboard;
            $scope.onboard = response, idx = findIndex(onboard), idx > -1 ? (newonboard = response, newonboard.$$hashKey = $scope.onboards[idx].$$hashKey, $scope.fonboards[idx] = newonboard) : $scope.onboards.splice(0, 0, response)
        })
    }, $scope.deleteDescriptorForm = function(onboard) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/descriptorNew/confirm.html",
            controller: function($scope, $modalInstance, onboard) {
            	$scope.onboard = onboard;
                $scope.ok = function() {
                    return $modalInstance.close(onboard)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
            	onboard: function() {
                    return onboard
                }
            }
        }), modalInstance.result.then(function(onboard) {
        	onboard.contents="";
        	deleteDescriptor(onboard);
        }, function() {})
    }, deleteDescriptor = function(onboard) {
        return ComputeOnBoardAPI["delete"](onboard, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(onboard),console.log(idx), $scope.onboards.splice(idx, 1), $scope.onboards.length > 0 && $scope.selectItem($scope.onboards[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, findIndex = function(onboard) {
        var item, _i,  _ref;
        _ref = $scope.onboards;
        for (_i = 0; _ref.length > _i; _i++){
        	item = _ref[_i];
        	console.log(_i+"::"+_ref[_i]+ "::"+item);
            if ( item.id === onboard.id) return _i;
        }
        return -1
    },$scope.selectFile = function(){
        $("#id_file").click();
    },$scope.onFileSelect = function($files) {
        createDescriptor($files[0]);
    },createDescriptor = function(file) {
        var modalInstance;
        modalInstance = $modal.open({
            backdrop: "static",
            templateUrl: "views/images/uploading.html",
            controller: function($scope, $modalInstance, $upload, file) {
                $scope.uplodingProgressPercent = "0", $scope.upload = $upload.upload({
                    url: CONST_RESTFUL_API.VNFD_URL.DESCRIPTOR_UPLOAD,
                    method: "POST",
                    file: file,
                    fileFormDataName: "file",
                    data: { "\"tenantid\"": "\""+CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID+"\"" },
                    sendDataAsJson: !0
                }).progress(function(evt) {
                    return void($scope.uplodingProgressPercent = parseInt(100 * evt.loaded / evt.total) + "")
                }).success(function(response) {
                	response.success ? (logger.logSuccess("파일이 저장되었습니다."),getDescriptors()) : logger.logError("파일저장에 실패하였습니다. " + response.fault)
                    $modalInstance.close(response)
                    
                })
                .error(function (err) {
                    alert('Error occured during upload'+err);
                })
            },resolve: {
                file: function() {
                    return file;
                }
            }
        }), modalInstance.result.then(function(data) {
//            logger.logSuccess("파일이 저장되었습니다."+data)
        }, function() {})
    }
}]);
