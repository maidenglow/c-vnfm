
'use strict';
angular.module("app.descriptors")
.directive("nfvVduEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/vdu/vdu-template.html',
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
        		console.log('nfvVduEditor ngModel: ', $scope.ngModel);
        		
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
  	      			}).$promise, DescriptorAPI.get($scope.selectedNode).$promise]).then(function(result) {
  	      			
  	              		console.log('result[0]:', result[0]);
  	                  	$scope.viewMode = 1;
  	          			$scope.editMode = 'XML';
  	                  	
  	                  	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';
  
  	          			$scope.detailXMLView = '';
  	          			
  	              		if (result[0] != undefined) {
  	              			for (key in result[0]) {
  	              				if (key == "$promise") {
  	              					break;
  	              				}
  	              				$scope.detailXMLView += result[0][key];
  	              			}
  	              			
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
  	              			$scope.ngModel = model.vdu;
  	              			console.log('ngModel: ', $scope.ngModel);
  	              		}
  
  	              		$scope.safeApply();
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
            	// lifecycle_event_key
            	param["lifecycle_event_key"] = [];
            	for (var le = 0; le < data.lifecycle_event_list.lifecycle_event.length; le++) {
            		param["lifecycle_event_key"].push( data.lifecycle_event_list.lifecycle_event[le].key);
            	}
            	// vnfc_key
            	param["vnfc_key"] = [];
            	for (var vn = 0; vn < data.vnfc_list.vnfc.length; vn++) {
            		param["vnfc_key"].push(data.vnfc_list.vnfc[vn].key);
            	}
            	// monitoring_parameter_key
            	param["monitoring_parameter_key"] = [];
            	for (var vn = 0; vn < data.monitoring_parameter_list.monitoring_parameter.length; vn++) {
            		param["monitoring_parameter_key"].push(data.monitoring_parameter_list.monitoring_parameter[vn].key);
            	}
            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=");
            	return formData;
            };
        	
            /*
            $scope.isValidVDU = function (vdu) {
            	var valid = true;
            	if (vdu.connection_list == undefined || vdu.connection_list.connection == undefined || vdu.connection_list.connection.length < 2) {
            		valid = valid && false;
            	}
            	console.log('isValidVDU: ', valid);
            	return valid;
            };
        	*/
            
        	$scope.saveNew = function () {
        		console.log('nfvVduEditor saveNew: ', $scope.ngModel);
        		/*
        		if (!$scope.isValidVDU($scope.ngModel)) {
        			return false;
        		}
        		*/
        		
        		$scope.vduId = $scope.ngModel.id;
        		
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
                		$scope.selectedNode.id = $scope.vduId;
                    }, 2000);
            	});
        	};
        	
        	$scope.saveModify = function () {
        		console.log("category", $scope.selectedNode.category);
        		//console.log('nfvVduEditor saveModify: ', $scope.ngModel.vdu);
        		
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
        		        "vm_image": "",
        		        "computation_requirement": "",
        		        "virtual_memory_resource_element": "",
        		        "virtual_network_bandwidth_resource": "",
        		        "lifecycle_event_list": {
        		            "lifecycle_event": [
        		            ]
        		        },
        		        "constraint": "",
        		        "high_availability": "",
        		        "scale_in_out": "",
        		        "vnfc_list": {
        		            "vnfc": [
        		            ]
        		        },
        		        "monitoring_parameter_list": {
        		            "monitoring_parameter": [
        		            ]
        		        }
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): vdu -', data);
            	if (data.category == 'vdu') {
            		$scope.addNew();
            	}
            });

            $rootScope.$on('reloadDetailDataVDU', function (event, data) {
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