"use strict";
angular.module("app.verification", []).factory("VerificationHostAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VERIFICATION_URL.VERIFICATION_HOST_LIST, {})
}]).factory("VerificationComputeAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VERIFICATION_URL.VERIFICATION_COMPUTE_LIST, {})
}]).factory("VerificationRequestAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VERIFICATION_URL.VERIFICATION_REQUEST, {
    	"PrePost": "@PrePost",
    	"Menu": "@Menu",
    	"DescriptorID": "@DescriptorID",
    	"Property": "@Property",
    	"PolicyID": "@PolicyID",
    	"HostID": "@HostID"
    })
}])





.filter("filterAllocatedVnf", [function() {
    return function(items, host) {
    	var filteredItems = [];
    	if (items == undefined) {
    		return filteredItems;
    	}
    	for (var i = 0; i < items.length; i++) {
    		if (items[i].allocated_vnf == undefined || items[i].allocated_vnf == '') {
    			items[i]["allocated_vnf"] = {
    					"allocated_vnf_info": []
    			}
    		}
    		if (items[i].allocated_vnf.allocated_vnf_info.constructor != Array) {
    			items[i].allocated_vnf.allocated_vnf_info = [items[i].allocated_vnf.allocated_vnf_info];
    		}
    		if (items[i].id == host) {
    			filteredItems = items[i].allocated_vnf.allocated_vnf_info;
    			break;
    		}
    	}
    	
        return filteredItems;
    }
}]).filter("filterComponent", [function() {
    return function(items, host) {
    	
    	console.log('#################items################');
    	console.log('itesm : ', items);
    	
    	var filteredItems = [];
    	if (items == undefined) {
    		return filteredItems;
    	}
    	for (var i = 0; i < items.length; i++) {
    		if (items[i].id == host) {
    			if (items[i].allocated_vnf == undefined || items[i].allocated_vnf == '') {
        			items[i]["allocated_vnf"] = {
        					"allocated_vnf_info": []
        			}
        		}
        		if (items[i].allocated_vnf.allocated_vnf_info.constructor != Array) {
        			items[i].allocated_vnf.allocated_vnf_info = [items[i].allocated_vnf.allocated_vnf_info];
        		}
    			for (var a = 0; a < items[i].allocated_vnf.allocated_vnf_info.length; a++) {
    				if (items[i].allocated_vnf.allocated_vnf_info[a].id != undefined && items[i].allocated_vnf.allocated_vnf_info[a].id != "") {
        				filteredItems.push({
        					"id": items[i].allocated_vnf.allocated_vnf_info[a].id,
        					"status": items[i].allocated_vnf.allocated_vnf_info[a].status//,
        					//"resultClass": 'btn-success'
        				});
    				}
    			}
    			
    			if (items[i].violated_vnf == undefined || items[i].violated_vnf == '') {
        			items[i]["violated_vnf"] = {
        					"violated_vnf_info": []
        			}
        		}
        		if (items[i].violated_vnf.violated_vnf_info.constructor != Array) {
        			items[i].violated_vnf.violated_vnf_info = [items[i].violated_vnf.violated_vnf_info];
        		}
    			for (var v = 0; v < items[i].violated_vnf.violated_vnf_info.length; v++) {
    				if (items[i].violated_vnf.violated_vnf_info[v].id != undefined && items[i].violated_vnf.violated_vnf_info[v].id != "") {
    					if (items[i].violated_resource.item.constructor != Array) {
    						items[i].violated_resource.item = [items[i].violated_resource.item];
    					}
        				filteredItems.push({
        					"id": items[i].violated_vnf.violated_vnf_info[v].id,
        					"status": items[i].violated_vnf.violated_vnf_info[v].status//,
        					//"violation_reason": items[i].violated_resource.item.join("."),
        					//"resultClass": 'btn-danger'
        				});
    				}
    			}
    			break;
    		}
    	}
    	
        return filteredItems;
    }
}]).controller("VerificationCtrl", 
		["$scope", "$q", "$modal", "DescriptorTreeViewAPI", "ManagementPolicyAPI", "SystemSystemsAPI",
		 "VerificationHostAPI", "VerificationComputeAPI", "VerificationRequestAPI", "$timeout", "logger", 
		 function($scope, $q, $modal, DescriptorTreeViewAPI, ManagementPolicyAPI, SystemSystemsAPI,
				 VerificationHostAPI, VerificationComputeAPI, VerificationRequestAPI, $timeout, logger) {
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
    $scope.reqData = {
    		"PrePost": "RA",
    		"Menu": "NSD",
        	"descriptor": "",
        	"property": "1"
    };
	
	$scope.checkedPolicies = [];
	$scope.verifications = [{
    			"name": "Resource Availability-NSD",
        		"PrePost": "RA",
        		"Menu": "NSD",
    			"isSelected": true
    		},
    		{
    			"name": "Resource Availability-VNFD",
        		"PrePost": "RA",
        		"Menu": "VNFD",
    			"isSelected": false
			},
    		{
    			"name": "Resource Consistency-NSD",
        		"PrePost": "RC",
        		"Menu": "NSD",
    			"isSelected": false
			},
    		{
    			"name": "Resource Consistency-VNFD",
        		"PrePost": "RC",
        		"Menu": "VNFD",
    			"isSelected": false
			}];
	
	$scope.property = [{
		"title": "ALL",
		"key": "1"
	},
	{
		"title": "Deployment Flavour",
		"key": "2"
	},
	{
		"title": "Affinity",
		"key": "3"
	},
	{
		"title": "Dependency",
		"key": "4"
	}/*,
	{
		"title": "Auto Scaling",
		"key": "5"
	}*/];
	$scope.selectedProperty = {
			"title": "ALL",
			"key": "1"
		};
	$scope.selectedDescriptor = {
			"item": "",
			"key": ""
		};
	$scope.selectedHost = {
			"title": "All",
			"item": "All",
			"key": "all"
		};

	$scope.requestVerification = function () {
		var policyIDs = [];
		for (var i = 0; i < $scope.checkedPolicies.length; i++) {
			policyIDs.push($scope.checkedPolicies[i].key);
		}
		$scope.mustSelectVerification = false;
		$scope.mustSelectDescriptor = false;
		$scope.mustSelectProperty = false;
		$scope.mustOneMorePolicies = false;
		$scope.mustSelectHost = false;

		if ($scope.reqData.PrePost == undefined || $scope.reqData.PrePost == '') {
			$scope.mustSelectVerification = true;
		}

		if ($scope.reqData.Menu == undefined || $scope.reqData.Menu == '') {
			$scope.mustSelectVerification = true;
		}

		console.log('$scope.selectedDescriptor : ', $scope.selectedDescriptor);
		if ($scope.selectedDescriptor == undefined || $scope.selectedDescriptor.key == undefined || $scope.selectedDescriptor.key == '') {
			$scope.mustSelectDescriptor = true;
		}

		if ($scope.selectedProperty == undefined || $scope.selectedProperty.key == undefined || $scope.selectedProperty.key == '') {
			$scope.mustSelectProperty = true;
		}
		
		if (policyIDs == undefined || policyIDs.length < 1) {
			$scope.mustOneMorePolicies = true;
		}
		
		if ($scope.selectedHost == undefined || $scope.selectedHost.key == undefined || $scope.selectedHost.key == '') {
			$scope.mustSelectHost = true;
		}
		if ($scope.mustSelectVerification || $scope.mustSelectDescriptor ||
			$scope.mustSelectProperty || $scope.mustOneMorePolicies || $scope.mustSelectHost) {
			return;
		}
		var reqData = {
		    	"PrePost": $scope.reqData.PrePost,
		    	"Menu": $scope.reqData.Menu,
		    	"DescriptorID": $scope.selectedDescriptor.key,
		    	"Property": $scope.selectedProperty.key,
		    	"PolicyID": policyIDs.join(','),
		    	"HostID": $scope.selectedHost.key
		    };		
		
		console.log('api request reqData : ', reqData);
		
		$q.all([VerificationRequestAPI.get(reqData).$promise,
		        VerificationComputeAPI.get().$promise]).then(function (result) {
			console.log('VerificationRequestAPI responce:', result[0]);

			$scope.resultData = {
					"result": {},
					"computeGroupList": []
			};
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
    			
    			console.log('model', model);
    			
    			if(model == null){
    				console.log('호출이 정상적이지 않습니다. 에러 알람을 띄웁니다.');
    				logger.logError("에러가 발생하였습니다.<br/>response msg : " + strXml);
    			}
    			
    			
    			
    			if (model.verisdnfv_response.result.result_info.constructor != Array) {
    				model.verisdnfv_response.result.result_info = [model.verisdnfv_response.result.result_info];
    			}
    			for (var r = 0; r < model.verisdnfv_response.result.result_info.length; r++) {

        			if (model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan == undefined ||
        					model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan == '')	{
        				model.verisdnfv_response.result.result_info[r].green_zone["schedule_plan"] = {
        						"schedule_plan_info": []
        				};
        			}
        			if (model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info.constructor != Array)	{
        				model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info = [model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info];
        			}
        			
        			for (var c = 0; c < model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info.length; c++) {
						if (model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c].component == undefined ||
								model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c].component == '')	{
							model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c]["component"] = {
									"component_info": []
							};
						}
        				if (model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c].component.component_info.constructor != Array) {
        					model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c].component.component_info = [model.verisdnfv_response.result.result_info[r].green_zone.schedule_plan.schedule_plan_info[c].component.component_info];
        				}
        			}

        			
        			//deadzone
        			
        			if (model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan == undefined ||
        					model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan == '')	{
        				model.verisdnfv_response.result.result_info[r].dead_zone["schedule_plan"] = {
        						"schedule_plan_info": []
        				};
        			}
        			if (model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info.constructor != Array)	{
        				model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info = [model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info];
        			}
        			
        			for (var c = 0; c < model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info.length; c++) {
        				if (model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c].component == undefined ||
								model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c].component == '')	{
							model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c]["component"] = {
									"component_info": []
							};
						}
        				if (model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c].component.component_info.constructor != Array) {
        					model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c].component.component_info = [model.verisdnfv_response.result.result_info[r].dead_zone.schedule_plan.schedule_plan_info[c].component.component_info];
        				}
        			}
    			}

    			$scope.resultData.result = model.verisdnfv_response.result;

    			console.log('result : ',$scope.resultData.result);
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
    			
    			console.log('model.compute_group_list.compute_group.length : ', model.compute_group_list.compute_group.length);
    			if(model.compute_group_list.compute_group.length == undefined){
    				$scope.resultData.computeGroupList = [model.compute_group_list.compute_group];
    			} else{
    				$scope.resultData.computeGroupList = model.compute_group_list.compute_group;
    			}   			
    			

    			
    			
    			for(var i = 0; i<$scope.resultData.computeGroupList.length; i++){
    				console.log(' i : ', i);
    				console.log('computeGroupList[i] : ' ,$scope.resultData.computeGroupList[i].compute[3]);
    				console.log('computeGroupList[i] : ' ,$scope.resultData.computeGroupList[i].compute.length);
    				
    				if($scope.resultData.computeGroupList[i].compute.length ==undefined){
    					$scope.resultData.computeGroupList[i].compute = [$scope.resultData.computeGroupList[i].compute];
    				}
    				
    				
    				for(var j = 0; j<$scope.resultData.computeGroupList[i].compute.length; j++){
    					console.log('j : ', j);
    					console.log('$scope.resultData.computeGroupList[i].compute[j] :', $scope.resultData.computeGroupList[i].compute[j]);
    					console.log('$scope.resultData.computeGroupList[i].compute[j].image : ', $scope.resultData.computeGroupList[i].compute[j].image);
    					
    					if($scope.resultData.computeGroupList[i].compute[j].image=='vnfd_mme'||
    							$scope.resultData.computeGroupList[i].compute[j].image=='vnfd_pgw_dp'||
    							$scope.resultData.computeGroupList[i].compute[j].image=='vnfd_pgw_cp'||
    							$scope.resultData.computeGroupList[i].compute[j].image=='vnfd_sgw_dp'||
    							$scope.resultData.computeGroupList[i].compute[j].image=='vnfd_sgw_cp')
    					{
    						
    						console.log('해당 이미지는 epc 이미지 입니다. : ', $scope.resultData.computeGroupList[i].compute[j].image);    	
    						$scope.resultData.computeGroupList[i].compute[j].resultClass = "btn-info";
    						$scope.resultData.computeGroupList[i].compute[j].resultType = "epc";
    						
    					}else{
    						$scope.resultData.computeGroupList[i].compute[j].resultClass = "btn-custom";
    						$scope.resultData.computeGroupList[i].compute[j].resultType = "none";
    					}
    					
    					
    				}
    			}
    			
    			console.log('##################');
    			console.log('$scope.resultData.computeGroupList : ', $scope.resultData.computeGroupList);
    			console.log('$scope.resultData.computeGroupList.length : ', $scope.resultData.computeGroupList.length);
    			
    			
    		}
        	var modalInstance;
        	var item = $scope.resultData;
        	var passData = $scope.reqData;
        	var selectedDescriptorData = $scope.selectedDescriptor.key;
        	

        	
            modalInstance = $modal.open({
                templateUrl: 'views/verification/result.html',
                size: "lg",
                controller: function($scope, $modalInstance, item) {
                	$scope.viewinfo = {
                		"deploymentFlavourId": 2,
                		"resultType": 0,
                		"greenZoneIndex": 0,
                		"deadZoneIndex": 0,
                		"changedFlavourId": 2
                	};
                	
                	$scope.$watch("viewinfo", function (nv, ov) {
                		if (nv == undefined || nv == ov) {
                			return;
                		}

                		$scope.changeViewInfo();
                	}, true);
                	
                	$scope.requestData = passData;
                	$scope.selectedDescriptorData = selectedDescriptorData;
                	
                	console.log('passData : ', $scope.requestData);
                	console.log('selectedDescriptorData :' , $scope.selectedDescriptorData);
                	
                	
                	$scope.changeViewInfo = function () {
                		
                		console.log('deadZoneIndex : ', $scope.viewinfo.deadZoneIndex);
                		console.log('result : ', $scope.result);
                		
                		//$scope.viewinfo.greenZoneIndex=0;
                		//$scope.viewinfo.deadZoneIndex=0;
                		
                		console.log('flavourId : ', $scope.viewinfo.deploymentFlavourId, 'changedFlavourId : ', $scope.viewinfo.changedFlavourId);
                		
                		if($scope.viewinfo.deploymentFlavourId != $scope.viewinfo.changedFlavourId){
                			$scope.viewinfo.greenZoneIndex = 0
                			$scope.viewinfo.deadZoneIndex = 0;
                			$scope.viewinfo.changedFlavourId = $scope.viewinfo.deploymentFlavourId;
                		}
                		
                		$scope.componentDeadMap = {};
                		if ($scope.viewinfo.resultType == 1) {
                			for (var i = 0; i < $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info.length; i++){
                				var host = $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].id;
                				$scope.componentDeadMap[host] = {
                					"components": []
                				};

								if ($scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf == undefined || $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf == '') {
									$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i]["allocated_vnf"] = {
											"allocated_vnf_info": []
									}
								}
								if ($scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf.allocated_vnf_info.constructor != Array) {
									$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf.allocated_vnf_info = [$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf.allocated_vnf_info];
								}
                				for(var a = 0; a < $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf.allocated_vnf_info.length; a++){
                					var allocated_vnf_info = $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].allocated_vnf.allocated_vnf_info[a];
                					
                					console.log('###allocated_vnf_info1 : ', allocated_vnf_info);
                					
                					if (allocated_vnf_info.id != undefined && allocated_vnf_info.id != '') {
                						$scope.componentDeadMap[host].components.push({
                							"id": allocated_vnf_info.id,
                							"instance_id":allocated_vnf_info.instance_id,
                							"status": allocated_vnf_info.status,
                							"resultClass": "btn-success"
                						});
                					}
                				}

								if ($scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf == undefined || $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf == '') {
									$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i]["violated_vnf"] = {
											"violated_vnf_info": []
									}
								}
								if ($scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf.violated_vnf_info.constructor != Array) {
									$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf.violated_vnf_info = [$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf.violated_vnf_info];
								}
                				for(a = 0; a < $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf.violated_vnf_info.length; a++){
                					var violated_vnf_info = $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_vnf.violated_vnf_info[a];
                					
                					console.log('###violated_vnf_info2 : ', violated_vnf_info);
                					
                					if (violated_vnf_info.id != undefined && violated_vnf_info.id != '') {
                						if ($scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_resource.item.constructor != Array) {
                							$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_resource.item = [$scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_resource.item];
                						}
                						
                						
                						//TODO danger만 놔두고 success 없애기..
                						console.log('$scope.componentDeadMap[host].components.length : ', $scope.componentDeadMap[host].components.length);
                						
                						for(var z=0; z<$scope.componentDeadMap[host].components.length; z++){
                							console.log('$scope.componentDeadMap[host].components : ', $scope.componentDeadMap[host].components[z]);
                							
                							if($scope.componentDeadMap[host].components[z].id == violated_vnf_info.id && 
                									$scope.componentDeadMap[host].components[z].instance_id == violated_vnf_info.instance_id){
                								console.log('파란것과 빨간 것의 값이 같습니다.');
                								
                								$scope.componentDeadMap[host].components.splice(z,1);  								
                								
                							}               							
                							
                						}    
                						
                						console.log('$scope.componentDeadMap[host].components 전 : ', $scope.componentDeadMap[host].components);               						
                						
                						
                						$scope.componentDeadMap[host].components.push({
                							"id": violated_vnf_info.id,
                							"instance_id":violated_vnf_info.instance_id,
                							"status": violated_vnf_info.status,
        									"violation_reason": $scope.result.result_info[$scope.viewinfo.deploymentFlavourId].dead_zone.schedule_plan.schedule_plan_info[$scope.viewinfo.deadZoneIndex].component.component_info[i].violated_resource.item.join("<br />"),
                							"resultClass": "btn-danger"
                						});
                						
                						
                						console.log('$scope.componentDeadMap[host].components 후 : ', $scope.componentDeadMap[host].components);

                						
                						
                					}
                				}
                			}
                		}
                		
                		$timeout(function () {
                			$('.btn-danger[data-toggle="tooltip"]').tooltipster({
                				"contentAsHTML": true
                			});
                		}, 500);
                	};
                	
                	$scope.computeGroupList = item.computeGroupList;
                	$scope.result = item.result;
                	$scope.init = function () {
                		$scope.changeViewInfo();
                    	
                	};
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

            	
            }, function() {})
		});
	};
	$scope.descriptorOptions = [];
    $scope.selectVerification = function (verification) {
    	
    	console.log('verification : ', verification);    	
    	
    	$scope.selectedDescriptor={
    			"item":"",
    			"key":""
    	};
    	
    	console.log('selectedDescriptor : ', $scope.selectedDescriptor);
    	
    	var event = window.event;
        var target = event.target || (event.srcElement || document);
        var targetElement;
        if (target.tagName != 'A') {
        	targetElement = angular.element(target).parents('a');
        } else {
        	targetElement = angular.element(target);
        }
    	$scope.safeApply(function () {
    		for (var i = 0; i < $scope.verifications.length; i++) {
    			$scope.verifications[i].isSelected = false;
    		}
    		verification.isSelected = true;
    		
    		console.log('verification: ', verification);
        	$scope.reqData.PrePost = verification.PrePost;
        	$scope.reqData.Menu = verification.Menu;
        	
        	console.log('reqData.verification: ', $scope.reqData.verification);
        	targetElement.blur();
    	});
    };
    $scope.init = function() {

        $q.all([DescriptorTreeViewAPI.get().$promise,
                ManagementPolicyAPI.get().$promise,
                VerificationHostAPI.get().$promise]).then(function(result) {
            console.log('DescriptorTreeViewAPI then');
//            console.log('result[1] ', JSON.stringify(result[1].vm_list, null, 4));
            $scope.treeData = result[0].tmenu_all.tmenu;

        	$scope.descriptorOptions = [];
        	
        	for (var m = 0; m < result[0].tmenu_all.tmenu.length; m++) {
        		if (result[0].tmenu_all.tmenu[m].children.child.constructor != Array &&
        				result[0].tmenu_all.tmenu[m].children.child.title == '' &&
        				result[0].tmenu_all.tmenu[m].children.child.key == '') {
        			result[0].tmenu_all.tmenu[m].children.child = undefined;
        		} else if (result[0].tmenu_all.tmenu[m].children.child.constructor != Array) {
        			result[0].tmenu_all.tmenu[m].children.child = [result[0].tmenu_all.tmenu[m].children.child];
        		}
        		

        		if (result[0].tmenu_all.tmenu[m].title == 'VNFD' || result[0].tmenu_all.tmenu[m].title == 'NSD') {
        			var optionTitle = result[0].tmenu_all.tmenu[m].title.toLowerCase();
        			$scope.descriptorOptions[optionTitle] = [];
        			for (var i = 0; i < result[0].tmenu_all.tmenu[m].children.child.length; i++) {
        				$scope.descriptorOptions[optionTitle].push({
        					"key" : result[0].tmenu_all.tmenu[m].children.child[i].title,
        					"title" : result[0].tmenu_all.tmenu[m].children.child[i].title
        				});
        			}
            	
        		}
				
            }
        	
        	$scope.Policies = [];

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
    			$scope.Policies = model.mnt_policy_list.mnt_policy;
    			console.log('policy: ', $scope.Policies);
    			$scope.descriptorOptions["policy"] = []; 
    			for (var p = 0; p < $scope.Policies.length; p++) {
    				$scope.descriptorOptions["policy"].push({
    					"title": $scope.Policies[p].mnt_policy_id,
    					"item": $scope.Policies[p].mnt_policy_id,
    					"key": $scope.Policies[p].mnt_policy_id
    				});
    			}
    		}
    		
    		if (result[2] != undefined) {
    			var strXml = '';
    			for (key in result[2]) {
    				if (key == "$promise") {
    					break;
    				}
    				strXml += result[2][key];
    			}
    			
    			var x2js = new X2JS();
    			var model = x2js.xml_str2json( strXml );
    			$scope.hostList = model.host_list.host;
    			console.log('hostList: ', $scope.hostList);
    			$scope.descriptorOptions["host_list"] = []; 
    			for (var h = 0; h < $scope.hostList.length; h++) {
    				$scope.descriptorOptions["host_list"].push({
    					"title": $scope.hostList[h].item,
    					"item": $scope.hostList[h].item,
    					"key": $scope.hostList[h].key
    				});
    			}
    		}
        });
    };
    return $scope;
}]);