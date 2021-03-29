
'use strict';
angular.module("app.descriptors")
.factory("NfviImageListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_IMAGE_LIST, {})}])
.directive("nfvNfviEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/nfvi/nfvi-template.html',
        replace: false,
        scope: {
            ngModel: "=ngModel",
            options: "=ngDescriptorOptions",
            editMode: "=ngEditMode",
            selectedNode: "=selectedNode",
            detailXMLView: "=detailXmlView"
        },
        controller: ["$scope", "logger", "$q", "$timeout", "$modal", "DescriptorTreeViewAPI", 
                     "OptionTreeViewAPI", "DescriptorXml", "DescriptorAPI","DescriptorOptionsAPI", "NfviImageListAPI", "$rootScope",
                     function($scope, logger, $q, $timeout, $modal, DescriptorTreeViewAPI, 
                   		  OptionTreeViewAPI, DescriptorXml, DescriptorAPI, DescriptorOptionsAPI, NfviImageListAPI, $rootScope) {
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
            if ($scope.ngModel != undefined) {
        		console.log('nfvNfviEditor ngModel: ', $scope.ngModel);
        		
        	}

        	$scope.$watch('detailXMLView', function (nv, ov) {
    	        console.log('detailXMLView:', nv);
    	     
    	        if(nv==undefined&&ov==undefined){
    	        	$scope.reloadDetailData();
    	        }
    	        
    	}, true)

            $scope.reloadDetailData = function () {

                if ($scope.selectedNode != undefined && $scope.selectedNode.id != undefined && $scope.selectedNode.id != '' &&
                		$scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
                	//TODO: if 에서 category 값에 따라 API 를 통해 가져오기
        			$scope.ngModel = undefined;
      			  
        			//$q start
  	      			$q.all([DescriptorXml.get({
  	      				"category": $scope.selectedNode.category.toUpperCase(),
  	      				"id": $scope.selectedNode.id
  	      			}).$promise, DescriptorAPI.get($scope.selectedNode).$promise, NfviImageListAPI.query().$promise]).then(function(result) {
  	      			
  	              		console.log('result[0]:', result[0]);
  	              		
  
  	              		$scope.safeApply(function () {
  	              		        $scope.viewMode = 1;
                                $scope.editMode = 'XML';

                                $scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

                                $scope.detailXMLView = '';

                                var detailXMLView = '';
                                if (result[0] != undefined) {
                                    for (key in result[0]) {
                                        if (key == "$promise") {
                                            break;
                                        }
                                        detailXMLView += result[0][key];
                                    }
                                    $scope.detailXMLView = detailXMLView;
                                }
                                
                                
                                console.log('$scope.imageList : ', $scope.imageList);
                                
                                if (result[1] != undefined) {
                                    var strXml = '';
                                    for (key in result[1]) {
                                        if (key == "$promise") {
                                            break;
                                        }
                                        strXml += result[1][key];
                                    }

                                    var x2js = new X2JS();
                                    var model = x2js.xml_str2json( strXml );
                                    $scope.ngModel = model.nfvi;
                                    console.log('ngModel: ', $scope.ngModel);
                                }  
                                
                                $scope.imageList = result[2];
                                console.log('$scope.ngModel : ', $scope.ngModel);
                                console.log('result[2]:', result[2]);
  	              		});
  	              	});
  	      			//$q end
                }
                
        	};
        	
        	$scope.parseFormData = function (data) {
            	var param = {};
            	for (var key in data) {
            		if (data[key].constructor != Object) {
            			param[key] = data[key];
            		}
            	}
            	// image_list
            	param["image_name"] = [];
            	for (var image = 0; image < data.image_list.image.length; image++) {
            		param["image_name"].push(data.image_list.image[image].item);
            	}
            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=").replaceAll("_name%5B%5D=", "_name=");
        		console.log('parseFormData : ', formData);
        		
            	return formData;
            };
        	
        	            
            $scope.isValidNFVI = function (nfvi) {
            	var valid = true;
            	if (nfvi.image_list == undefined || nfvi.image_list.image == undefined || nfvi.image_list.image.length < 1
            	) {
            		valid = valid && false;
            	}
            	console.log('isValidNFVI: ', valid);
            	return valid;
            };
        	
        	$scope.saveNew = function () {
        		
        		
        		if($scope.ngModel.image_list.image.length < 1){
        			return false;
        		}
        		
        		console.log('nfvNfviEditor saveNew: ', $scope.ngModel);
        		if (!$scope.isValidNFVI($scope.ngModel)) {
        			return false;
        		}

        		$scope.nfviId = $scope.ngModel.id;
        		
        		var formData = $scope.parseFormData($scope.ngModel);
        		console.log('formData : ', formData);
        		
        		
    			$q.all([DescriptorAPI.save({
    				"category": $scope.selectedNode.category,
    				"id": $scope.ngModel.id
    				}, formData).$promise]).then(function(result) {
            		console.log('result[0]:', result[0]);

            		$scope.editMode = "XML";
            		$scope.detailXMLView = "";
            		$scope.$emit('reloadTreedata');
                    $timeout(function () {
                		$scope.selectedNode.id = $scope.nfviId;
                    }, 2000);
            	});
        	};
        	
        	$scope.saveModify = function () {
        		if($scope.ngModel.image_list.image.length < 1){
        			return false;
        		}
        		
        		console.log("category", $scope.selectedNode.category);
        		//console.log('nfvVldEditor saveModify: ', $scope.ngModel.vld);
        		
    			var formData = $scope.parseFormData($scope.ngModel);
    			
    			$q.all([DescriptorAPI.update($scope.selectedNode, formData).$promise]).then(function(result) {
            		console.log('result[0]:', result[0]);

                	$scope.reloadDetailData();

            	});
        	};
        	
        	$scope.modifySelectedItem = function () {
            	if ($scope.selectedNode == undefined) {
            		return false;
            	}
            	$scope.editMode = 'MODIFY';
            };
            
            $scope.addNew = function () {
            

            	$scope.ngModel = {
        		        "id": "",
        		        "image_list": {
        		            "image": [
        		            ]
        		        }
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): nfvi -', data);
            	if (data.category == 'nfvi') {
            		$scope.addNew();
            	}
            });
            
            $rootScope.$on('reloadDetailDataNFVI', function (event, data) {
            	console.log('emit::reloadDetailData(): selectedNode -', data);
            	$scope.selectedNode = data;
                $scope.reloadDetailData();
            });
            
            $scope.deleteSelectedItem = function () {
            	console.log('deleteSelectedItem(): ', $scope.selectedNode, $scope.ngModel);
            	var item = $scope.selectedNode;
            	var modalInstance;
                modalInstance = $modal.open({
                    templateUrl: "views/flavors/confirm.html",
                    controller: function($scope, $modalInstance, item) {
                        $scope.ok = function() {
                            return $modalInstance.close(item)
                        }, $scope.cancel = function() {
                            return $modalInstance.dismiss("Cancel")
                        }
                    },
                    resolve: {
                        item: function() {
                            return item;
                        }
                    }
                }), modalInstance.result.then(function(item) {
                	console.log('modalInstance(): item ', item);

            		$q.all([DescriptorAPI.del($scope.selectedNode).$promise]).then(function(result) {
                		console.log('result[0]:', result[0]);

                		$scope.ngModel = undefined;

                		$scope.editMode = "XML";
                		$scope.detailXMLView = "";
                		$scope.$emit('reloadTreedata');
                        $timeout(function () {
                    		$scope.selectedNode.id = "";
                        }, 2000);
                        
                	});
                	
                }, function() {})
            };
            
            
            
          //image checkbox 값 변경 용도.
        	$scope.sync = function(bool, imageValue){
        		
        		console.log('##image value change');
        		console.log('bool : ', bool);
        		console.log('image : ', imageValue);
        		
        		console.log($scope.ngModel);
        		
        		//name으로만 맵핑하기 때문에 굳이 key가 필요 없다.
        		var image = {
        				"item":imageValue.name						
        		};
        		
        		console.log('image : ', image);
        		
        		if(bool){
        			// add item
        			console.log('image를 추가 합니다.', image);
        			console.log('$scope.ngModel.image_list : ', $scope.ngModel.image_list);
        			$scope.ngModel.image_list.image.push(image);
        		} else {
        			// remove item
        			for(var i=0 ; i < $scope.ngModel.image_list.image.length; i++) {
        				console.log('$scope.ngModel.image_list.image[i].item : ', $scope.ngModel.image_list.image[i].item);
        				console.log('imageValue.name : ',imageValue.name);
        				if($scope.ngModel.image_list.image[i].item == imageValue.name){
        					//$scope.data.splice(i,1);
        					$scope.ngModel.image_list.image.splice(i,1);
        				}
        			}      
        		}
        		
        		console.log('request imageVal : ', $scope.ngModel.image_list);
        		
        	};
        	
        	//checkbox 확인.
        	$scope.isChecked = function(imageName){
        		
        		var match = false;
        		
        		console.log('imageName : ', imageName);
        		console.log('ngmodel is Checked : ', $scope.ngModel.image_list.image);
        		
        		//선택된 이미지가 1개인 경우, array가 아닌 object로 만들어 지기 때문에 예외처리로 강제 array로 변경해 준다.
        		if($scope.ngModel.image_list.image.length == undefined){
        			$scope.ngModel.image_list.image = [$scope.ngModel.image_list.image];
        		}
        		
        		console.log('$scope.ngModel.image_list.image.length : ', $scope.ngModel.image_list.image.length);
        		
        		for(var i=0 ; i < $scope.ngModel.image_list.image.length; i++) {
        			console.log('image[i].name : ', $scope.ngModel.image_list.image[i].item);
        			if($scope.ngModel.image_list.image[i].item == imageName){
        				match = true;
        			}
        		}
        		return match;
        	};       

        }]
    };
    
    
	
}]);