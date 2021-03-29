
'use strict';
angular.module("app.descriptors")
.directive("nfvPolicyEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/policy/policy-template.html',
        replace: false,
        scope: {
            policy_data: "=ngModel",
            editMode: "=ngEditMode",
            selectedNode: "=selectedNode"
        },
        controller: ["$scope", "logger", "$q", "$timeout", "$modal", "DescriptorTreeViewAPI", 
                     "OptionTreeViewAPI", "DescriptorXml", "DescriptorAPI","DescriptorOptionsAPI",
                     function($scope, logger, $q, $timeout, $modal, DescriptorTreeViewAPI, 
                   		  OptionTreeViewAPI, DescriptorXml, DescriptorAPI, DescriptorOptionsAPI) {
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
            if ($scope.policy_data != undefined) {
        		console.log('nfvPolicyEditor policy_data: ', $scope.policy_data);
        		
        	}

            $scope.reloadDetailData = function () {

                if ($scope.selectedNode != undefined && $scope.selectedNode.id != undefined && $scope.selectedNode.id != '' &&
                		$scope.selectedNode.category != undefined && $scope.selectedNode.category != '') {
                	//TODO: if 에서 category 값에 따라 API 를 통해 가져오기
        			$scope.policy_data = undefined;
        			//$q start
        			$q.all([DescriptorOptionsAPI.get({id:$scope.selectedNode.id.toLowerCase(), md:'get'}).$promise]).then(function(result) {
        			
                    	$scope.viewMode = 1;
            			$scope.editMode = 'MODIFY';
                    	
                    	$scope.detailImageView = '/mano/static/images/'+ $scope.selectedNode.id.toLowerCase() + '0.png';
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
  	              			var model = x2js.xml_str2json( strXml );
  	              			$scope.policy_data = model.policy_list;
  	              			console.log('policy_data: ', $scope.policy_data);
  	              		}
						
                    	$scope.safeApply();
                    	
                	});
        			//$q end
                }
                
        	};
        	
        	
            $scope.parseFormOptionsData = function () {
            	
            	var formData = '';
            	
            	if($scope.selectedNode.category == 'options' && $scope.selectedNode.id == 'POLICY'){
            		
            		var data = $scope.policy_data;
            		
            		for (var policy_item = 0; policy_item < data.policy_item.length; policy_item++) {
                		var param = {};
                    	// cp_key
                    	param["policy_no"] = [];
                    	param["policy"] = [];
                    	param["checked"] = [];
                    	
                		param["policy_no"].push(data.policy_item[policy_item].policy_no);
                		param["policy"].push(data.policy_item[policy_item].policy);
                		param["checked"].push(data.policy_item[policy_item].checked);
                		formData += $.param(param).replaceAll("policy_no%5B%5D=", "policy_no=").replaceAll("policy%5B%5D=", "policy=").replaceAll("checked%5B%5D=", "checked=") + '&';
                	}
            		
            	} else {
            		
            		console.log("undefined selectedNode.id~!!!!");
            	}
            	
            	
            	return formData.trimRight('&');
            };
        	            
            $scope.isValidVLD = function (vld) {
            	var valid = true;
            	if (vld.connection_list == undefined || vld.connection_list.connection == undefined || vld.connection_list.connection.length < 2) {
            		valid = valid && false;
            	}
            	console.log('isValidVLD: ', valid);
            	return valid;
            };
        	
        	$scope.saveModify = function () {
        		console.log("category", $scope.selectedNode.category);
        		//console.log('nfvSecurityEditor saveModify: ', $scope.security_data.vld);

    			$q.all([DescriptorOptionsAPI.update(
    					{id:$scope.selectedNode.id.toLowerCase(), md:'put'}, 
    					$scope.parseFormOptionsData()).$promise]
    				).then(function(result) {
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
            

            $scope.$on('addNewSubitem', function (event, data) {
            	console.log('emit::addNewSubitem(): POLICY -', data);
            	if (data.category == 'options' && data.id == 'POLICY') {
            		$scope.addNewOption();
            	}
            });
            
            $scope.addNewOption = function () { //only for add-button action
            	
            	console.log('addNewOption');
            	
            	if ($scope.selectedNode.category == 'options' && $scope.selectedNode.id == 'POLICY'){
            		
            		$scope.policy_data.policy_item.push({
                		"policy_no": "",
                		"policy": "",
                		"checked": true
                	});
            		
            	} else {
            		
            		console.log("undefined selectedNode.id~!!!!");
            	}
            	
            };
            
            $scope.deleteSelectedItem = function () {
            	console.log('deleteSelectedItem(): ', $scope.selectedNode, $scope.policy_data);
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

            		$q.all([DescriptorOptionsAPI.del(
        					{id:$scope.selectedNode.id.toLowerCase(), md:'delete'}, 
        					$scope.parseFormOptionsData()).$promise]
        				).then(function(result) {
                		console.log('result[0]:', result[0]);

                		$scope.reloadDetailData();
                		
                	});
                	
                }, function() {})
            };
            
        }]
    }
}]);