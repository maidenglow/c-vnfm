"use strict";
angular.module("app.descriptors", [])
.factory("DescriptorTreeViewAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_TREEVIEW_META, {})
}]).factory("OptionTreeViewAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.OPTION_TREEVIEW_META, {})
}]).factory("DescriptorTreeMain", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_TREE_MAIN, {})
}]).factory("DescriptorTreeSub", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_TREE_SUB, {})
}]).factory("DescriptorTreeOption", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_TREE_OPTION, {})
}]).factory("VLDatviewAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.VLD_ATVIEW_META, {})
}]).factory("VMatviewAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.VM_ATVIEW_META, {})
}]).factory("DescriptorXml", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_XML, {
    	category: "@category",
        id: "@id"
    })
}]).factory("DescriptorAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR, {
    	category: "@category",
        id: "@id"
    }, {
    	save: {
            method: "POST",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	},
    	update: {
            method: "PUT",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	},
    	del: {
            method: "DELETE",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	}
    })
}]).factory("DescriptorOptionsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.DESCRIPTOR_URL.DESCRIPTOR_OPTIONS, {
    	id: "@id",
    	md: "@md"
    }, {
    	update: {
            method: "PUT",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	},
    	del: {
            method: "PUT",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	}
    })
}])
.directive('checkImage', function ($q) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            attrs.$observe('ngSrc', function (ngSrc) {
                var deferred = $q.defer();
                var image = new Image();
                image.onerror = function () {
                    deferred.resolve(false);
                    element.attr('src', '/mano/static/images/default0.png'); // set default image
                    //element.attr('text', 'dddddddddddddddddddddddddddddddd');
                };
                image.onload = function () {
                    deferred.resolve(true);
                };
                image.src = ngSrc;
                return deferred.promise;
            });
        }
    };
})    

.directive("nfvCheckboxList", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        replace: true,
        template: '<div ng-init="init()">' +
          		  '<div class="checkbox" ng-repeat="item in items">' +
	              '	<label>' +
	              '		<input type="checkbox" name="{{groupName}}" value="{{item.key}}" ng-model="item.checked" ng-change="checkedChange()">' +
	              '		{{item.title}}' +
	              '	</label>' +
	              '</div>' +
	              '</div>',
        scope: {
            ngModel: "=ngModel",
            items: "=items",
            groupName: "=groupName"
        },
        controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
        	
        	console.log('nfvCheckboxList controller init');
        	
        	
        	$scope.checkedChange = function () {

				$scope.ngModel.splice(0, $scope.ngModel.length);
				for (var i = 0; i < $scope.items.length; i++) {
					if ($scope.items[i].checked) {
						$scope.ngModel.push({
							"item": $scope.items[i].title,
							"key": $scope.items[i].key
						});
					}

        		}
        	};
        	$scope.init = function () {
        		console.log('nfvCheckboxList init: ');
        		if ($scope.ngModel.constructor != Array) {
        			$scope.ngModel = [$scope.ngModel];
        		}
        		for (var i = 0; i < $scope.items.length; i++) {
					var checked = false;
					for (var m = 0; m < $scope.ngModel.length; m++) {
						if ($scope.ngModel[m].key == $scope.items[i].key) {
							checked = true;
							break;
						}
					}
					$scope.items[i].checked = checked;

        		}
        	};
        }],
        link: function(scope, element, attrs) {

        	console.log('nfvCheckboxList link');
        	
//                	element.on('change', function(event) {
//                		event.preventDefault();
//             	        console.log('nfvCheckboxList:click selecteNode');
//                	 });
        }
    }
}]).directive("nfvRadioList", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        replace: true,
        template: '<div ng-init="init()">' +
        		  '<div class="radio" ng-if="allowNone">' +
	              '	<label>' +
	              '		<input type="radio" name="{{groupName}}" value="" ng-model="ngModel.key" ng-change="checkedChangeNone()">' +
	              '		None' +
	              '	</label>' +
	              '</div>' +
          		  '<div class="radio" ng-repeat="item in items">' +
	              '	<label>' +
	              '		<input type="radio" name="{{groupName}}" value="{{item.key}}" ng-model="ngModel.key" ng-change="checkedChange()">' +
	              '		{{item.title}}' +
	              '	</label>' +
	              '</div>' +
	              '</div>',
        scope: {
            ngModel: "=ngModel",
            items: "=items",
            groupName: "=groupName",
            allowNone: "=allowNone"
        },
        controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
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
        	console.log('nfvCheckboxList controller init');
        	
        	$scope.$watch("ngModel", function (nv, ov) {
        		if (nv == undefined || nv == undefined) {
        			$scope.ngModel = {
							"item": '',
							"key": ''
						};
        		}
        	}, true)

        	$scope.checkedChangeNone = function () {

        		if ($scope.allowNone == true) {
					$scope.ngModel = {
							"item": '',
							"key": ''
						};
				}
        	};
        	$scope.checkedChange = function () {

				for (var i = 0; i < $scope.items.length; i++) {
					if ($scope.ngModel.key == $scope.items[i].key) {
						$scope.ngModel = {
							"item": $scope.items[i].title,
							"key": $scope.items[i].key
						};
						break;
					}

        		}
        	};
        	$scope.init = function () {
        		console.log('nfvCheckboxList init: ');
        		console.log('allowNone: ', $scope.allowNone);
        		
        		if ($scope.allowNone == true && 
        				($scope.ngModel == undefined || $scope.ngModel.key == undefined)) {
        			$scope.ngModel = {
							"item": '',
							"key": ''
						};
        		}
        	};
        }],
        link: function(scope, element, attrs) {

        	console.log('nfvCheckboxList link');
        	
//                	element.on('change', function(event) {
//                		event.preventDefault();
//             	        console.log('nfvCheckboxList:click selecteNode');
//                	 });
        }
    }
}]).controller("ActiveViewCtrl", ["$scope", "logger", "$q", "$timeout", "$modal", "DescriptorTreeViewAPI", 
                                  "OptionTreeViewAPI", "VLDatviewAPI", "VMatviewAPI", "DescriptorXml", "DescriptorAPI","DescriptorOptionsAPI",
                                  "DescriptorTreeMain", "DescriptorTreeSub", "DescriptorTreeOption", "$rootScope",
                                  function($scope, logger, $q, $timeout, $modal, DescriptorTreeViewAPI, 
                                		  OptionTreeViewAPI, VLDatviewAPI, VMatviewAPI, DescriptorXml, DescriptorAPI, DescriptorOptionsAPI,
                                		  DescriptorTreeMain, DescriptorTreeSub, DescriptorTreeOption, $rootScope ) {
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
    	console.log('emit::selectNodeDescriptor(): ', data);
    	$scope.safeApply(function () {
    	        $scope.selectedNode = data;
    	        });
    });
    $scope.$on('reloadTreedata', function (event) {
    	console.log('emit::reloadTreedata(): ');
    	$scope.init();
    });
    $scope.$on('addNewSubitem', function (event, data) {
    	console.log('emit::addNewSubitem(): ', data);
    	$scope.selectedNode = data;
    	$scope.viewMode = 1;
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
    
	return $scope.init = function() {

        $q.all([DescriptorTreeMain.get().$promise/*, DescriptorTreeSub.get().$promise, 
                DescriptorTreeOption.get().$promise*/]).then(function(result) {
            console.log(result);
            console.log('DescriptorTreeViewAPI then');


            $scope.descriptorOptions = {};
            $scope.treeOptionData = [];
            $scope.treeAllData = [];
            $scope.optionMenuList = [];

            if (result[0] != undefined) {
                var strXml = '';
                for (key in result[0]) {
                    if (key == "$promise") {
                        break;
                    }
                    strXml += result[0][key];
                }

                var x2js = new X2JS();
                var model = x2js.xml_str2json( strXml );
                if (model.tmenu_all.tmenu.constructor != Array) {
                    model.tmenu_all.tmenu = [model.tmenu_all.tmenu];
                }

                for (var i = 0; i < model.tmenu_all.tmenu.length; i++) {
                    if (model.tmenu_all.tmenu[i].children.child.constructor != Array &&
                        model.tmenu_all.tmenu[i].children.child.title == '' &&
                        model.tmenu_all.tmenu[i].children.child.key == '') {
                        model.tmenu_all.tmenu[i].children.child = undefined;
                    } else if (model.tmenu_all.tmenu[i].children.child.constructor != Array) {
                        model.tmenu_all.tmenu[i].children.child = [model.tmenu_all.tmenu[i].children.child];
                    }
                    $scope.treeAllData.push(model.tmenu_all.tmenu[i]);
                    
                    var optionTitle = model.tmenu_all.tmenu[i].title.toLowerCase() + "_list";
                    $scope.descriptorOptions[optionTitle] = model.tmenu_all.tmenu[i].children.child;

                }

            }
            
            if ($scope.selectedNode == undefined) {
                $timeout(function () {
                    $scope.selectedNode = {"category": "vld", "id": ""};
                }, 2000);
            }
        });
    },
    $scope.selectedData,
    
    $scope.viewMode = 0,	// 0: Active View, 1: Detail View(XML)
    $scope.editMode = 'XML',	// 'XML': XML View, 'MODIFY': Modify Mode, 'ADD': Add New Mode
    
    $scope.selectedNode,
    $scope.reloadDetailData = function () {

        if ($scope.selectedNode != undefined && $scope.selectedNode.id != undefined && $scope.selectedNode.id != '' &&
        		$scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
        	//TODO: if 에서 category 값에 따라 API 를 통해 가져오기
			$scope.selectedData = undefined;
		  
		  if($scope.selectedNode.category == "options" || $scope.optionMenuList.indexOf($scope.selectedNode.id) >= 0){
		    console.log("options start!!!!!");
  			    //$q start
    			$q.all([DescriptorOptionsAPI.get({id:$scope.selectedNode.id.toLowerCase(), md:'get'}).$promise]).then(function(result) {
    			
                	$scope.viewMode = 1;
        			$scope.editMode = 'MODIFY';
                	
                	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.id.toLowerCase() + '0.png';
        			$scope.detailImageView = '';
                	console.log('result: ', result);
                	if (result[0] != undefined) {
              			var strXml = '';
              			for (key in result[0]) {
              				if (key == "$promise") {
              					break;
              				}
              				strXml += result[0][key];
              			}
              			
              			console.log('result[0]: ', result[0]);
              		
              			var x2js = new X2JS();
              			$scope.selectedData = x2js.xml_str2json( strXml );
              			console.log('selectedData: ', $scope.selectedData);
              		}

            	  $scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.id.toLowerCase() + '0.png';
            	  $scope.editMode = 'XML';

                	//descriptor 선택시 viewmode로 전환되지 않아 강제로 1값 넣어 줌. 클릭시 바로 viewmode로 변경됨.
            	  $scope.viewMode='1';
                	//이미지 apply
                  
                    $scope.safeApply();
            	});
    			//$q end
    			
          } else {
        	  console.log("main content start!!!!!");
        	//$q start
  			//$scope.$broadcast('reloadDetailData', $scope.selectedNode);
  			//$q end
        	  var reloadPath = 'reloadDetailData'+$scope.selectedNode.category.toUpperCase();
        	  console.log('reloadPath : ', reloadPath);
        	  var selectedNodeObject = $scope.selectedNode;
        	  

        	  //이미지 호출이 되지 않아서 이곳에서 지정해 줌. jykim
        	  $scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';
        	  $scope.editMode = 'XML';

            	//descriptor 선택시 viewmode로 전환되지 않아 강제로 1값 넣어 줌. 클릭시 바로 viewmode로 변경됨.
        	  $scope.viewMode='1';
            	//이미지 apply
              $scope.safeApply();
        	  
        	  //해당 category에 있는 js를 맵핑하여 호출.
        	  $rootScope.$broadcast(reloadPath, selectedNodeObject);

          }
      

			
			
        } else if ($scope.selectedNode != undefined && $scope.selectedNode.category && ($scope.selectedNode.id == undefined || $scope.selectedNode.id == '')) {

        	$scope.selectedNode.id = "";
        	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

        	$scope.editMode = 'XML';
            $scope.safeApply();
        }
	},
    $scope.viewModeToggle = function () {
    	$scope.viewMode = $scope.viewMode == 0 ? 1 : 0;
    },
    $scope.treeData,
//            $scope.treeRootConfig,
    $scope.treeOptionData,
//            $scope.treeOptionConfig,
    $scope.activeViewOnLoad = function () {
    	
        console.log('activeViewOnLoad init');
//                console.log('go: ', go != undefined);
        
    },
    $scope.detailViewOnLoad = function () {
    	
        console.log('detailViewOnLoad init');
    }
}]);
