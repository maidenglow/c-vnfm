
'use strict';
angular.module("app.descriptors")
.directive("nfvNsdEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/nsd/nsd-template.html',
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
        		console.log('nfvNsdEditor ngModel: ', $scope.ngModel);
        		
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
                                    $scope.ngModel = model.nsd;
                                    console.log('ngModel: ', $scope.ngModel);
                                    
                                }   
  	              		});
  	              	});
  	      			//$q end
                }
                
        	};
        	
        	$scope.parseFormData = function (data) {
        		
        		console.log('data : ', data);
            	var param = {};
            	for (var key in data) {
            		if (data[key].constructor != Object) {
            			param[key] = data[key];
            		}
            	}
            	
            	//vnfd_key
            	param["vnfd_key"] = [];
            	for (var vnfd = 0; vnfd < data.vnfd.vnfd_info.length; vnfd++) {
            		param["vnfd_key"].push(data.vnfd.vnfd_info[vnfd].key);
            	}
            	            	
            	//vnffgd_key
            	param["vnffgd_key"] = [];
            	for (var vnffgd = 0; vnffgd < data.vnffgd.vnffgd_info.length; vnffgd++) {
            		param["vnffgd_key"].push(data.vnffgd.vnffgd_info[vnffgd].key);
            	}            	
            	
            	//vld_key
            	param["vld_key"] = [];
            	for (var vld = 0; vld < data.vld.vld_info.length; vld++) {
            		param["vld_key"].push(data.vld.vld_info[vld].key);
            	}            	
            	
            	//lifecycle_key
            	param["lifecycle_key"] = [];
            	for (var lifecycle = 0; lifecycle < data.lifecycle_event.lifecycle_event_info.length; lifecycle++) {
            		param["lifecycle_key"].push(data.lifecycle_event.lifecycle_event_info[lifecycle].key);
            	}            	
            	
            	//vnfdep_key
            	param["vnfdep_key"] = [];
            	for (var vnfdep = 0; vnfdep < data.vnf_dependency.vnf_dependency_info.length; vnfdep++) {
            		param["vnfdep_key"].push(data.vnf_dependency.vnf_dependency_info[vnfdep].key);
            	}   
            	            	
            	//monitoring_key
            	param["monitoring_key"] = [];
            	for (var monitoring = 0; monitoring < data.monitoring_parameter.monitoring_parameter_info.length; monitoring++) {
            		param["monitoring_key"].push(data.monitoring_parameter.monitoring_parameter_info[monitoring].key);
            	}            	
            	
            	//sdf_key
            	param["sdf_key"] = [];
            	for (var sdf = 0; sdf < data.service_deployment_flavour.service_deployment_flavour_info.length; sdf++) {
            		param["sdf_key"].push(data.service_deployment_flavour.service_deployment_flavour_info[sdf].key);
            	}           	
            	
            	//policy_key
            	param["policy_key"] = [];
            	for (var policy = 0; policy < data.auto_scale_policy.auto_scale_policy_info.length; policy++) {
            		param["policy_key"].push(data.auto_scale_policy.auto_scale_policy_info[policy].key);
            	}
            	
            	//connectioin_point_key
            	param["connectioin_point_key"] = [];
            	for (var cp = 0; cp < data.connection_point.connection_point_info.length; cp++) {
            		param["connectioin_point_key"].push(data.connection_point.connection_point_info[cp].key);
            	}
            	
            	//pnfd_key
            	param["pnfd_key"] = [];
            	for (var pnfd = 0; pnfd < data.pnfd.pnfd_info.length; pnfd++) {
            		param["pnfd_key"].push(data.pnfd.pnfd_info[pnfd].key);
            	}
            	   
            	console.log('######################################');
            	console.log('security_key', data.nsd_security.nsd_security_info.key);
            	//security_key
            	param["security_key"] = data.nsd_security.nsd_security_info.key;
/*            	param["security_key"] = [];
            	for (var security = 0; security < data.nsd_security.nsd_security_info.length; security++) {
            		param["security_key"].push(data.nsd_security.nsd_security_info[security].key);
            	}*/
            	
            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=");
        		console.log('formData : ', formData);
            	return formData;
            };
        	
        	            
            $scope.isValidNSD = function (nsd) {
            	var valid = true;
            	if (nsd.vnf_dependency == undefined || nsd.vnf_dependency.vnf_dependency_info == undefined || nsd.vnf_dependency.vnf_dependency_info.length < 1||
            		nsd.service_deployment_flavour == undefined || nsd.service_deployment_flavour.service_deployment_flavour_info == undefined || nsd.service_deployment_flavour.service_deployment_flavour_info.length < 1||
            		nsd.connection_point == undefined || nsd.connection_point.connection_point_info == undefined || nsd.connection_point.connection_point_info.length < 1
            	) {
            		valid = valid && false;
            	}
            	console.log('isValidNSD: ', valid);
            	return valid;
            };
        	
        	$scope.saveNew = function () {
        		console.log('nfvNsdEditor saveNew: ', $scope.ngModel);
        		if (!$scope.isValidNSD($scope.ngModel)) {
        			return false;
        		}

        		$scope.nsdId = $scope.ngModel.id;
        		
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
                		$scope.selectedNode.id = $scope.nsdId;
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
        		        "version": "",
        		        "vnfd": {
        		            "vnfd_info": [
        		            ]
        		        },
        		        "vnffgd": {
        		            "vnffgd_info": [
        		            ]
        		        },
        		        "vld": {
        		            "vld_info": [
        		            ]
        		        },
        		        "lifecycle_event": {
        		            "lifecycle_event_info": [
        		            ]
        		        },
        		        "vnf_dependency": {
        		            "vnf_dependency_info": [
        		            ]
        		        },
        		        "monitoring_parameter": {
        		            "monitoring_parameter_info": [
        		            ]
        		        },
        		        "service_deployment_flavour": {
        		            "service_deployment_flavour_info": [
        		            ]
        		        },
        		        "auto_scale_policy": {
        		            "auto_scale_policy_info": [
        		            ]
        		        },
        		        "connection_point": {
        		            "connection_point_info": [
        		            ]
        		        },
        		        "pnfd": {
        		            "pnfd_info": [
        		            ]
        		        }
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): nsd -', data);
            	if (data.category == 'nsd') {
            		$scope.addNew();
            	}
            });
            
            $rootScope.$on('reloadDetailDataNSD', function (event, data) {
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