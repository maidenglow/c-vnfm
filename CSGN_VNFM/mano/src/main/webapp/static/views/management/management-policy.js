"use strict";
angular.module("app.management", []).factory("ManagementPolicyAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VERIFICATION_URL.MANAGEMENT_POLICY_LIST, {
    }, {
    	update: {
            method: "PUT",
            isArray: false,
            headers: {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
    	}
    })
}]).controller("ManagementPolicyCtrl", 
		["$scope", "$q", "DescriptorTreeViewAPI", "ManagementPolicyAPI", 
		 function($scope, $q, DescriptorTreeViewAPI, ManagementPolicyAPI) {
			
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

    $scope.energyEfficiencyValue = true;
    $scope.isModified = false;
    $scope.handleWidth = "auto";
    $scope.labelWidth = "auto";
    $scope.label = "OFF";
    $scope.animate = true;
    $scope.radioOff = false;
    $scope.trueValue = true;
    $scope.falseValue = false;
    $scope.onColor = "primary";
    $scope.offColor = "warning";

    $scope.selectManagementPolicy = function (policy) {
    	var event = window.event;
        var target = event.target || (event.srcElement || document);
        var targetElement;
        if (target.tagName != 'A') {
        	targetElement = angular.element(target).parents('a');
        } else {
        	targetElement = angular.element(target);
        }
        $scope.reqData = undefined;
    	$scope.safeApply(function () {
    		for (var i = 0; i < $scope.Policies.length; i++) {
    			$scope.Policies[i].isSelected = false;
    		}
    		policy.isSelected = true;
    		$scope.reqData = {
		        "mnt_policy_no": policy.mnt_policy_no,
		        "mnt_policy_id": policy.mnt_policy_id,
		        "mnt_policy_nm": policy.mnt_policy_nm,
		        "mnt_policy_value": policy.mnt_policy_value
    		};
    		$scope.orignValue = policy.mnt_policy_value + '';
        	console.log('reqData.policy: ', $scope.reqData);
        	targetElement.blur();
    	});
    };
    
    $scope.toggleCheckbox = function (checkValue) {
    	console.log('checkValue: ', checkValue);
    	$scope.safeApply(function () {
        	if (checkValue) {
    			$scope.reqData.mnt_policy_value = '1';
    		    $scope.label = "OFF";
        	} else {
        		$scope.reqData.mnt_policy_value = '0';
        	    $scope.label = "ON";
        	}
    	});
    	
    };
    
    $scope.$watch('reqData', function (nv, ov) {
    	if (nv == undefined || nv === ov) {
    		return;
    	}
    	if (ov != undefined) {
    		console.log('mnt_policy_nm: ', $scope.reqData.mnt_policy_nm, ' orignValue: ', $scope.orignValue, ' nv: ', nv.mnt_policy_value);
    	}
    	
    	if (ov != undefined && ov.mnt_policy_nm != nv.mnt_policy_nm) {
    	    $scope.isModified = false;
    	} else if (ov != undefined) {
    	    $scope.isModified = ($scope.orignValue != nv.mnt_policy_value);
    	}
    }, true);
    
    $scope.saveModify = function () {
    	$q.all([ManagementPolicyAPI.update($.param($scope.reqData)).$promise]).then(function (result) {
    		console.log('reqData: ', $scope.reqData, ' result: ', result);
    	    $scope.isModified = false;
    		$scope.init($scope.reqData.mnt_policy_no);
    	});
    };
    
    $scope.init = function(selectedPolicyNo) {

        $q.all([ManagementPolicyAPI.get().$promise]).then(function(result) {
        	
        	$scope.Policies = [];

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
    			$scope.Policies = model.mnt_policy_list.mnt_policy;
    			for (var i = 0; i < $scope.Policies.length; i++) {
    				if ($scope.Policies[i].mnt_policy_nm == "energy_efficiency") {
    					$scope.energyEfficiencyValue = $scope.Policies[i].mnt_policy_value == '1';
    				}
    				if (selectedPolicyNo != undefined && selectedPolicyNo == $scope.Policies[i].mnt_policy_no) {
    					$scope.selectManagementPolicy($scope.Policies[i]);
    				}
    			}
				if (selectedPolicyNo == undefined && $scope.Policies.length > 0) {
					$scope.selectManagementPolicy($scope.Policies[0]);
				}
    			console.log('policy: ', $scope.Policies);
    		}
        });
    };
    return $scope;
}]);