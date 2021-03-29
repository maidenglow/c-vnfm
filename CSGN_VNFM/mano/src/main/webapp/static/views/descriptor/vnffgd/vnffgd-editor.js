
'use strict';
angular.module("app.descriptors")
.directive("nfvVnffgdEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/vnffgd/vnffgd-template.html',
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
        		console.log('nfvVnffgdEditor ngModel: ', $scope.ngModel);
        		
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
  	              			$scope.ngModel = model.vnffgd;
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
            	
            	// vld_version_no
            	param["vld_version_no"] = [];
            	for (var vld = 0; vld < data.vld_list.vld.length; vld++) {
            		param["vld_version_no"].push(data.vld_list.vld[vld].key);
            	} 
            	
            	// vnfd_version_no
            	param["vnfd_version_no"] = [];
            	for (var vnfd = 0; vnfd < data.vnfd_list.vnfd.length; vnfd++) {
            		param["vnfd_version_no"].push(data.vnfd_list.vnfd[vnfd].key);
            	}   
            	
            	// nfp_key
            	param["nfp_key"] = [];
            	for (var nfp = 0; nfp < data.nfp_list.nfp.length; nfp++) {
            		param["nfp_key"].push(data.nfp_list.nfp[nfp].key);
            	}   
            	
            	// cp_key
            	param["cp_key"] = [];
            	for (var cp = 0; cp < data.connection_list.connection.length; cp++) {
            		param["cp_key"].push(data.connection_list.connection[cp].key);
            	}

            	// security
            	param["security"] = data.vnffgd_security.key;
            	
        		var formData = $.param(param).replaceAll("_key%5B%5D=", "_key=");
        		formData = formData.replaceAll().replaceAll("_version_no%5B%5D=", "_version_no=");
            	return formData;
            };
        	
        	            
            $scope.isValidVNFFGD = function (vnffgd) {
            	var valid = true;
            	if (vnffgd.connection_list == undefined || vnffgd.connection_list.connection == undefined || vnffgd.connection_list.connection.length < 2||
            			vnffgd.vld_list == undefined || vnffgd.vld_list.vld == undefined || vnffgd.vld_list.vld.length < 1 ||
            			vnffgd.vnfd_list == undefined || vnffgd.vnfd_list.vnfd == undefined || vnffgd.vnfd_list.vnfd.length < 1
            			
            	) {
            		valid = valid && false;
            	}
            	console.log('isValidVNFFGD: ', valid);
            	return valid;
            };
        	
        	$scope.saveNew = function () {
        		console.log('nfvVnffgdEditor saveNew: ', $scope.ngModel);
        		if (!$scope.isValidVNFFGD($scope.ngModel)) {
        			return false;
        		}

        		$scope.vnffgdId = $scope.ngModel.id;
        		
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
                		$scope.selectedNode.id = $scope.vnffgdId;
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
        		        "number_of_endpoints": "",
        		        "number_of_virtual_links": "",
        		        "vld_list": {
        		            "vld": [
        		            ]
        		        },
        		        "nfp_list": {
        		            "nfp": [
        		            ]
        		        },
        		        "connection_list": {
        		            "connection": [
        		            ]
        		        },
        		        "descriptor_version": "",
        		        "vnfd_list": {
        		            "vnfd": [
        		            ]
        		        },
        		        "vnffgd_security": {
        		            "key": "",
        		            "item": ""
        		        }
            		};
            	$scope.selectedNode.id = "";
            	//$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.category.toLowerCase() + '0.png';

            	$scope.editMode = 'ADD';
            };

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): vnffgd -', data);
            	if (data.category == 'vnffgd') {
            		$scope.addNew();
            	}
            });

            $rootScope.$on('reloadDetailDataVNFFGD', function (event, data) {
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