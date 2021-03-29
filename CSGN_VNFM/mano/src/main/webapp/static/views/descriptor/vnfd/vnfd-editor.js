
'use strict';
angular.module("app.descriptors")
.directive("nfvVnfdEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/vnfd/vnfd-template.html',
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
        		console.log('nfvVnfdEditor ngModel: ', $scope.ngModel);
        		
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
                                    $scope.ngModel = model.vnfd;
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
            	       		
        		//vdu_key
            	param["vdu_key"] = [];
            	for (var vdu = 0; vdu < data.vdu.vdu_info.length; vdu++) {
            		param["vdu_key"].push(data.vdu.vdu_info[vdu].key);
            	}
            	
        		//virtual_link_key
            	param["virtual_link_key"] = [];
            	for (var vld = 0; vld < data.virtual_link.virtual_link_info.length; vld++) {
            		param["virtual_link_key"].push(data.virtual_link.virtual_link_info[vld].key);
            	}
            	
        		//connection_point_key
            	param["connection_point_key"] = [];
            	for (var cp = 0; cp < data.connection_point.connection_point_info.length; cp++) {
            		param["connection_point_key"].push(data.connection_point.connection_point_info[cp].key);
            	}
            	
        		//lifecycle_key
            	param["lifecycle_key"] = [];
            	for (var lifecycle = 0; lifecycle < data.lifecycle_event.lifecycle_event_info.length; lifecycle++) {
            		param["lifecycle_key"].push(data.lifecycle_event.lifecycle_event_info[lifecycle].key);
            	}
            	
            	//dependency_key
            	param["dependency_key"] = [];
            	for (var vnfd = 0; vnfd < data.dependency.dependency_info.length; vnfd++) {
            		param["dependency_key"].push(data.dependency.dependency_info[vnfd].key);
            	}            	
            	
        		//monitoring_key
            	param["monitoring_key"] = [];
            	for (var monitoring = 0; monitoring < data.monitoring_parameter.monitoring_parameter_info.length; monitoring++) {
            		param["monitoring_key"].push(data.monitoring_parameter.monitoring_parameter_info[monitoring].key);
            	}
            	
        		//flavour_key
            	param["flavour_key"] = [];
            	for (var df = 0; df < data.deployment_flavour.deployment_flavour_info.length; df++) {
            		param["flavour_key"].push(data.deployment_flavour.deployment_flavour_info[df].key);
            		console.log('test : ', data.deployment_flavour.deployment_flavour_info[df].key);
            	}            	
            	
        		//policy_key
            	param["policy_key"] = [];
            	for (var policy = 0; policy < data.auto_scale_policy.auto_scale_policy_info.length; policy++) {
            		param["policy_key"].push(data.auto_scale_policy.auto_scale_policy_info[policy].key);
            	}            	
            	
        		//security_key
            	param["security_key"] = [];
            	for (var security = 0; security < data.manifest_file_security.manifest_file_security_info.length; security++) {
            		param["security_key"].push(data.manifest_file_security.manifest_file_security_info[security].key);
            	}
            	
            	            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=");
        		console.log('formData : ', formData);
        		
            	return formData;
            };
        	
        	            
            $scope.isValidVNFD = function (vnfd) {
            	var valid = true;
            	if (vnfd.vdu == undefined || vnfd.vdu.vdu_info == undefined || vnfd.vdu.vdu_info.length < 1 ||
            			vnfd.connection_point == undefined || vnfd.connection_point.connection_point_info == undefined || vnfd.connection_point.connection_point_info.length < 1 ||
            			vnfd.deployment_flavour == undefined || vnfd.deployment_flavour.deployment_flavour_info == undefined || vnfd.deployment_flavour.deployment_flavour_info.length < 1) {
            		valid = valid && false;
            	}
            	console.log('isValidVNFD: ', valid);
            	return valid;
            };
        	
        	$scope.saveNew = function () {
        		console.log('nfvVnfdEditor saveNew: ', $scope.ngModel);
        		if (!$scope.isValidVNFD($scope.ngModel)) {
        			return false;
        		}

        		$scope.vnfdId = $scope.ngModel.id;
        		
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
                		$scope.selectedNode.id = $scope.vnfdId;
                    }, 2000);
            	});
        	};
        	
        	$scope.saveModify = function () {
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
        		        "vendor": "",
        		        "descriptor_version": "",
        		        "version": "",
        		        "vdu": {
        		            "vdu_info": [
        		            ]
        		        },
        		        "virtual_link": {
        		            "virtual_link_info": [
        		            ]
        		        },
        		        "connection_point": {
        		            "connection_point_info": [
        		            ]
        		        },
        		        "lifecycle_event": {
        		            "lifecycle_event_info": [
        		            ]
        		        },
        		        "dependency": {
        		            "dependency_info": [
        		            ]
        		        },
        		        "monitoring_parameter": {
        		            "monitoring_parameter_info": [
        		            ]
        		        },
        		        "deployment_flavour": {
        		            "deployment_flavour_info": [
        		            ]
        		        },
        		        "auto_scale_policy": {
        		            "auto_scale_policy_info": [
        		            ]
        		        },
        		        "manifest_file": "",
        		        "manifest_file_security": {
        		            "manifest_file_security_info": [
        		            ]
        		        }
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): vnfd -', data);
            	if (data.category == 'vnfd') {
            		$scope.addNew();
            	}
            });
            
            $rootScope.$on('reloadDetailDataVNFD', function (event, data) {
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