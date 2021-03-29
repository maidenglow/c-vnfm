"use strict";
angular.module("app.repository", [])
.factory("ComputeRepositoryAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VNFD_URL.REPOSITORYS, {
        tenantId: CONST_RESTFUL_API.VNFD_URL.TENANT_ID,
        name: "@name"
    })
}]).controller("RepositoryCtrl", ["$scope", "$modal", "$http", "$q", "logger", "$timeout", "flavorsService", "ComputeRepositoryAPI","CONST_RESTFUL_API", 
                               function($scope, $modal, $http, $q, logger, $timeout, flavorsService, ComputeRepositoryAPI,CONST_RESTFUL_API) {
    var createFlavor, deleteFlavor, findIndex, findIndexX, findIndexY, findIndexItem, getFlavor, getFlavors, refreshFlavor, updateFlavor,uploadImage, deleteImage;
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
    		console.log('selectNodeDescriptor');
    			
    	        $scope.selectedNode = data;
    	        $scope.flavor = data;
    	        if(data.info){
    	        	$scope.items = [];
    	        	for(var i = 0; i < data.info.length ; i++)
    	        	$scope.items.push(data.info[i]);
    			}else {
    				$scope.items = null;
    			}
    	        $scope.item = null;
    	        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	console.log('addNewSubitem');
    	$scope.selectedNode = data;
    	$scope.flavor = data;
    	$scope.items = data.info;
    	
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
     }, $scope.selectFile = function(){
         $("#id_file").click();
     }, $scope.onFileSelect = function($files) {
         $scope.flavor.file = $files[0]
         uploadImage($scope.flavor);
     }, $scope.deleteImageForm = function(item) {
         var modalInstance;
         modalInstance = $modal.open({
             templateUrl: "views/repository/confirm.html",
             controller: function($scope, $modalInstance, item) {
            	 $scope.item = item;
                 $scope.ok = function() {
                     return $modalInstance.close(item)
                 }, $scope.cancel = function() {
                     return $modalInstance.dismiss("Cancel")
                 }
             },
             resolve: {
            	 item: function() {
                     return item
                 }
             }
         }), modalInstance.result.then(function(item) {
             deleteImage(item)
         }, function() {})
     }, uploadImage = function(flavor) {
         var modalInstance;
         modalInstance = $modal.open({
             backdrop: "static",
             templateUrl: "views/images/uploading.html",
             controller: function($scope, $modalInstance, $upload, flavor) {
                 $scope.uplodingProgressPercent = "0", $scope.upload = $upload.upload({
                     url: CONST_RESTFUL_API.VNFD_URL.REPOSITORY_UPLOAD,
                     method: "POST",
                     file: flavor.file,
                     fileFormDataName: "file",
                     data: flavor,
                     sendDataAsJson: !0
                 }).progress(function(evt) {
                     return void($scope.uplodingProgressPercent = parseInt(100 * evt.loaded / evt.total) + "")
                 }).success(function(data) {
//                	 console.log("1");
//                	 console.log(data);
                     $modalInstance.close(data)
                 })
             },
             resolve: {
            	 flavor: function() {
                     return flavor
                 }
             }
         }), modalInstance.result.then(function(data) {
//        	 console.log("2");
//        	 console.log(data);
        	 
        	 var idx, idx2;
        	 logger.logSuccess("파일이 업로드에 성공하였습니다."),idx = findIndexX($scope.flavor), idx2 = findIndexY($scope.flavor), idx > -1 ? 
     		 ($scope.flavors[idx].children.child[idx2].info = data,$scope.items = data)
        	 :null
//             logger.logSuccess("파일이 업로드에 성공하였습니다."), idx = findIndex($scope.flavor),console.log(idx),$scope.flavors[idx].info = data.info; $scope.items = data.info;//$scope.flavor.info.add(data);/*$scope.image = data, $scope.images.splice(0, 0, data), $scope.isActionProgress(data.status) && refreshImage(data.id)*/
         }, function() {})
     }, deleteImage = function(item) {
         return ComputeRepositoryAPI["delete"](item, function(response) {
        	 
        	 var idx, idx2, idx3;
        	 logger.logSuccess("삭제 되었습니다."),idx3 = findIndexItem(item), $scope.items.splice(idx3, 1),idx = findIndexX($scope.flavor), idx2 = findIndexY($scope.flavor),console.log(idx),console.log(idx2), idx > -1 ? 
     		 ($scope.flavors[idx].children.child[idx2].info = $scope.items
     		 )
        	 :null
        	 
        	 
//        	 response.success ? (logger.log("삭제 되었습니다."), idx = findIndexItem(item),console.log(idx), $scope.items.splice(idx, 1), $scope.items.length > 0 ) 
//        			 : logger.logError("삭제를 실패하였습니다. " + response.fault)
        	 
//         	getFlavors().$promise.then(function() {
//                 var flavor, _i, _len, _ref, _results;
//                 for ($scope.flavors && $scope.flavors.length > 0 && ($scope.flavor = $scope.flavors[0]), _ref = $scope.flavors, _results = [], _i = 0, _len = _ref.length; _len > _i; _i++) flavor = _ref[_i];
//                 return _results
//             })
//             return logger.logSuccess("삭제되었습니다.")//, $scope.image = response;console.log("refreshImage");refreshImage(response.id);console("refreshImage");//$scope.images.splice(findIndex(image), 1), void($scope.images.length > 0 && $scope.selectImage($scope.images[0]))) : void logger.logError("삭제를 실패하였습니다. " + response.fault)
         })
 	},$scope.flavors, $scope.flavor= {
 	        id: "",
 	        file: null,
 	        title: "",
 	        parent: "",
 	        type: "",
 	        info: null
    }, $scope.items, $scope.item, $scope.selectItem = function(item) {
        return $scope.item = item
    }, $scope.isSelected = function(item) {
        return $scope.item === item
    }, $scope.init = function() {
        return $q.all([getFlavors().$promise]).then(function(result) {
            var flavors;
            return flavors = result[0], $scope.flavors = flavors;//, flavors && flavors[0] ? $scope.selectItem(flavors[0]) : void 0
        })
    }, getFlavors = function(flavor) {
        return ComputeRepositoryAPI.query(flavor, function(response) {
            $scope.flavors = response
        })
    }, getFlavor = function(flavor) {
        return ComputeRepositoryAPI.get(flavor, function(response) {
            var idx, newflavor;
            $scope.flavor = response, idx = findIndex(flavor), idx > -1 ? (newflavor = response, newflavor.$$hashKey = $scope.flavors[idx].$$hashKey, $scope.flavors[idx] = newflavor) : $scope.flavors.splice(0, 0, response)
        })
    }, createFlavor = function(flavor) {
        return ComputeRepositoryAPI.save(flavor, function(response) {
            logger.logSuccess("Flavor가 생성되었습니다."), getFlavor({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshFlavor(response.id)
                }, 1e3)
            })
        })
    }, updateFlavor = function(flavor) {
        ComputeRepositoryAPI.update(flavor, function() {
            var idx;
            logger.logSuccess("Flavor가 수정되었습니다."), idx = findIndex(flavor), $scope.flavor = flavor, flavor.$$hashKey = $scope.flavors[idx].$$hashKey, $scope.flavors[idx] = flavor
        })
    }, deleteFlavor = function(flavor) {
        return ComputeRepositoryAPI["delete"](flavor, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(flavor), $scope.flavors.splice(idx, 1), $scope.flavors.length > 0 && $scope.selectItem($scope.flavors[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, refreshFlavor = function(id) {
        return ComputeRepositoryAPI.get({
            id: id
        }, function(response) {
            var flavor, idx;
            idx = findIndex(response), flavor = response, flavor.$$hashKey = $scope.flavors[idx].$$hashKey, idx > -1 && ($scope.flavors[idx] = flavor), $scope.selectItem($scope.flavors[idx])
        })
    }, findIndex = function(flavor) {
        var  item, _i, _len, _ref;
        _ref = $scope.flavors
        for (_i = 0, _len = _ref.length; _len > _i; _i++)
            if (item = _ref[_i], item.id === flavor.id) return _i;
        return -1
    }, findIndexX = function(instance) {
        var  item, _i, _ref , _j,  _ref2;
        _ref = $scope.flavors;
        for ( _i = 0; _i <_ref.length; _i++){
        	_ref2 = _ref[_i].children.child;
        	for (_j = 0; _j < _ref2.length ; _j++){
        		item = _ref2[_j];
//        		console.log("1");
//        		console.log(_i+"::"+_j)
//        		console.log(item);
        		if (item.title === instance.title && item.parent === instance.parent ) return _i;
    		}
		}
        return -1
    }, findIndexY = function(instance) {
    	var item, _i,  _ref , _j, _ref2;
    	_ref = $scope.flavors
        for (_i = 0; _i < _ref.length; _i++){
        	_ref2 = _ref[_i].children.child;
        	for (_j = 0; _j < _ref2.length; _j++){
        		item = _ref2[_j];
//        		console.log("2");
//        		console.log(_i+"::"+_j)
//        		console.log(item);
        		if (item.title === instance.title && item.parent === instance.parent) return _j;
    		}
		}
        return -1
    }, findIndexItem = function(item) {
        var  _item, _i, _len, _ref;
        _ref = $scope.items
        for (_i = 0, _len = _ref.length; _len > _i; _i++)
            if (_item = _ref[_i], _item.name === item.name) return _i;
        return -1
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
    }
}]);
