
'use strict';
angular.module("app.descriptors")
.directive("nfvCvnfEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/cvnf/cvnf-template.html',
        replace: false,
        scope: {
            ngModel: "=ngModel",
            options: "=ngDescriptorOptions",
            editMode: "=ngEditMode",
            selectedNode: "=selectedNode",
            detailXMLView: "=detailXmlView"
        },
        controller: ["$scope", "logger", "$q", "$timeout", "$modal", "DescriptorTreeViewAPI", 
                     "OptionTreeViewAPI", "DescriptorXml", "DescriptorAPI","DescriptorOptionsAPI", "$rootScope",
                     function($scope, logger, $q, $timeout, $modal, DescriptorTreeViewAPI, 
                   		  OptionTreeViewAPI, DescriptorXml, DescriptorAPI, DescriptorOptionsAPI, $rootScope) {
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
        		console.log('nfvCvnfEditor ngModel: ', $scope.ngModel);
        		
        	}

        	$scope.$watch('detailXMLView', function (nv, ov) {
        	        console.log('detailXMLView:', nv);
        	     
        	        if(nv==undefined&&ov==undefined){
        	        	$scope.reloadDetailData();
        	        }
        	        
        	}, true)
        	
/*        	$scope.$watch('detailImageView', function (nv, ov) {
        	        console.log('detailImageView:', nv);
        	     
        	        if(nv==undefined&&ov==undefined){
        	        	$scope.reloadDetailData();
        	        }
        	        
        	}, true)*/
        	
            $scope.reloadDetailData = function () {

                if ($scope.selectedNode != undefined && $scope.selectedNode.id != undefined && $scope.selectedNode.id != '' &&
                		$scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
                	//TODO: if 에서 category 값에 따라 API 를 통해 가져오기
        			$scope.ngModel = undefined;
      			  
        			console.log('scope : ', $scope);
        			console.log('scope category :', $scope.selectedNode.category.toUpperCase());
        			console.log('scope id: ', $scope.selectedNode.id);
        			
        			
        			var category = $scope.selectedNode.category.toUpperCase();
        			var id = $scope.selectedNode.id;
        			var selectedNode = $scope.selectedNode;
        			
        			//$q start
  	      			$q.all([DescriptorXml.get({
  	      				"category": category,
  	      				"id": id
  	      			}).$promise, DescriptorAPI.get(selectedNode).$promise]).then(function(result) {
  	      			
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
                                    $scope.ngModel = model.cvnf;
                                    console.log('ngModel: ', $scope.ngModel);
                                    
                                }   
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
            	            	
            	//param["id"] = data.vnf_flavour_id_reference+'_'+data.vnf_reference_key.split('|')[1];
            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=");
            	return formData;
            };
        	
        	            
            $scope.isValidCVNF = function (cvnf) {
            	var valid = true;
            	if (cvnf == undefined || cvnf.number_of_instances == undefined) {
            		valid = valid && false;
            	}
            	
        		$scope.mustSelectVnfFlavour= false;
        		$scope.mustSelectVnfReference = false;
            	            	
            	if(cvnf.vnf_flavour_id_reference ==undefined || cvnf.vnf_flavour_id_reference ==''){
            		valid = valid && false;
            		$scope.mustSelectVnfFlavour = true;
            	}
        		
            	if(cvnf.vnf_reference_key ==undefined || cvnf.vnf_reference_key ==''){
            		valid = valid && false;
            		$scope.mustSelectVnfReference = true;
            	}         	
        		
        		
            	console.log('isValidCVNF: ', valid);
            	return valid;
            };
        	
        	$scope.saveNew = function () {
        		console.log('nfvCvnfEditor saveNew: ', $scope.ngModel);
        		if (!$scope.isValidCVNF($scope.ngModel)) {
        			return false;
        		}

        		//$scope.cvnfId = $scope.ngModel.id;
        		$scope.cvnfId = $scope.ngModel.vnf_flavour_id_reference+'_'+$scope.ngModel.vnf_reference_key.split('|')[1];
        		console.log('cvnfId : ', $scope.cvnfId);
        		$scope.ngModel.id = $scope.cvnfId;
        		
        		var formData = $scope.parseFormData($scope.ngModel);

    			$q.all([DescriptorAPI.save({
    				"category": $scope.selectedNode.category,
    				"id": $scope.ngModel.id
    				}, formData).$promise]).then(function(result) {
            		console.log('result[0]:', result[0]);

            		$scope.editMode = "XML";
            		$scope.detailXMLView = "";
            		$scope.$emit('reloadTreedata');
                    $timeout(function () {
                		$scope.selectedNode.id = $scope.cvnfId;
                    }, 2000);
            	});
        	};
        	
        	$scope.saveModify = function () {
        		console.log("category", $scope.selectedNode.category);
        		//console.log('nfvcvnfEditor saveModify: ', $scope.ngModel.cvnf);
        		
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
        		        "id": ""
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): cvnf -', data);
            	if (data.category == 'cvnf') {
            		$scope.addNew();
            	}
            });
            
            $rootScope.$on('reloadDetailDataCVNF', function (event, data) {
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
            
        }]
    }
}]);