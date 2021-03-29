"use strict";
angular.module("app.scaling", []).factory("ScaleGroupsListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_GROUP_LIST, {})
}]).factory("ScaleImageListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_IMAGE_LIST, {})
}]).factory("ScaleFlavorListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_FLAVOR_LIST, {})
}]).factory("ScaleNetworkListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_NETWORK_LIST, {})
}]).factory("ScaleKeypairListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_KEYPAIR_LIST, {})
}]).factory("ScalePostAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.SCALE_URL.SCALE_UPDATE, {
    }, {
    	update: {
            method: "POST",
            isArray: false,
            headers: {'Content-Type':'application/json; charset=UTF-8'}//json으로 해주어야 함.!!
    	}
    })
}])

.controller("ScalingCtrl", 
		["$scope", "$q", "$modal", "ScaleGroupsListAPI", "ScaleImageListAPI", "ScaleFlavorListAPI", "ScaleNetworkListAPI", "ScaleKeypairListAPI", "ScalePostAPI", "$timeout", 
		 function($scope, $q, $modal, ScaleGroupsListAPI, ScaleImageListAPI, ScaleFlavorListAPI, ScaleNetworkListAPI, ScaleKeypairListAPI, ScalePostAPI, $timeout) {

			
			//groupList를 닮을 객체
			var groupListData;
			//왼쪽 메뉴에서 선택된 값을 담을 객체
			var selectedValData;
			//서버로 post 요청을 할 값 객체
			var reqValData;
			//서버로 부터 전달받은 groupList를 파싱하여 담은 객체.
			var json;
			
			//var data = new Object();
			$scope.init = function(selectIndex) {

				//0=view, 1=edit 
				$scope.viewMode= 0;				
				//서버 update할 데이터.
				$scope.reqData={};
				
				console.log('#####start init Scaling######');
				console.log('selectIndex : ' , selectIndex);

				$q.all([ScaleGroupsListAPI.query().$promise,
				        ScaleImageListAPI.query().$promise,
				        ScaleFlavorListAPI.query().$promise,
				        ScaleNetworkListAPI.query().$promise,
				        ScaleKeypairListAPI.query().$promise
				        ]).then(function(result) {

				        	json = JSON.stringify( result[0], function( key, value ) {
				        		if( key === "$$hashKey" ) {
				        			return undefined;
				        		}

				        		console.log('value : ', value);
				        		
				        		if (value==null){
				        			//console.log('공백을 리턴합니다.');
				        			return "";
				        		} else {
				        			//console.log('원래 값을 리턴 합니다.');
				        			return value;
				        		}
				        		
				        		
				        	});

				        	console.log('groupList json : ', json);

				        	groupListData = angular.fromJson(json);
				        	selectedValData = angular.fromJson(json)[0];
				        	reqValData = angular.fromJson(json)[0];
				        					        	
				        	console.log('groupListData : ', groupListData);
				        	console.log('selectedValData', selectedValData);
				        	console.log('reqValData', reqValData);				        	

				        	//groupList는 실제 데이터를 표출하기 위한 객체					        	
				        	$scope.groupList = groupListData;	
				        	
				        	//selectIndex가 없는 경우 : 첫 호출.
				        	if($scope.selectIndex==undefined){
				        		console.log('select index가 없습니다.');
				        		$scope.selectedVal = selectedValData;
					        	$scope.reqData=reqValData;
				        	} else {
				        		console.log('select index : ', selectIndex);
				        		$scope.selectedVal = angular.fromJson(json)[selectIndex];
					        	$scope.reqData=angular.fromJson(json)[selectIndex];
				        	}

				        	
				        	$scope.imageList = result[1];
				        	$scope.flavorList = result[2];
				        	$scope.networkList = result[3];		
				        	$scope.keypairList = result[4];


				        	console.log('$scope.groupList', $scope.groupList);
				        	console.log('$scope.selectedVal', $scope.selectedVal);
				        	console.log('$scope.reqData', $scope.reqData);				        	
				        	
				        	console.log('$scope.imageList', $scope.imageList);
				        	console.log('$scope.flavorList', $scope.flavorList);
				        	console.log('$scope.networkList', $scope.networkList);
				        	console.log('$scope.keypairList', $scope.keypairList);	

				        });

			};

			$scope.selectGroup = function(index){

				console.log('Select Group Index : ', index);
				
				$scope.selectIndex = index;
				
				//다른 그룹을 선택하면 ,viewMode를 0으로 변경함.
				$scope.viewMode='0';
	        	//화면에 보여질 값을 바꿔줌.
	        	$scope.selectedVal = angular.fromJson(json)[index];
	        	//서버에 post할 값을 바꿔줌.
	        	$scope.reqData=angular.fromJson(json)[index];				
				
// 굳이 타임아웃을 하지 않아도 됨.
/*				setTimeout(function () {
					console.log('timeout : : : : : : : :');
					$scope.$apply(function () {

						$scope.selectedVal = item;	
						$scope.reqData=item;

					});				        
				}, 100);*/
			};			


			$scope.isSelected = function(selectGroupName) {
				console.log('sendedGroupName : ', selectGroupName);
				console.log(' is selected return val: ', $scope.selectedVal.group_name == selectGroupName);
				return $scope.selectedVal.group_name == selectGroupName;
			};

			
			//scale post request 부분.
			$scope.requestScale = function(){

				$scope.postVal = {
					//configuration
					"imageCheck" : false,
					"flavorCheck" : false,
					"networkCheck" : false,
					"keypairCheck": false,
					
					//scale 값 3개중 1개는 반드시 있어야 함.
					"scaleInCheck": false,
					"scaleOutCheck":false,
					
					//scale out의 값이 in값 보다 커야 함.
					"inOutValueCheck":"default"
				};
				
				console.log('###request post reqData##############');
				console.log('$scope.reqData : ', $scope.reqData);

				
				if($scope.reqData.img_name == null || $scope.reqData.img_name==undefined || $scope.reqData.img_name ==""){
					console.log('img_name 이 null 입니다.');
					$scope.postVal.imageCheck = true;
				}
				
				if($scope.reqData.flavor_name == null || $scope.reqData.flavor_name==undefined || $scope.reqData.flavor_name ==""){
					console.log('img_name 이 null 입니다.');
					$scope.postVal.imageCheck = true;
				}
				
				if($scope.reqData.network_info == null || $scope.reqData.network_info == undefined || $scope.reqData.network_info.length <1){
					console.log('network_info 가 null 입니다.');
					$scope.postVal.networkCheck = true;
				}
				
				
				
				if($scope.reqData.in_cpu==""&& $scope.reqData.in_memory=="" && $scope.reqData.in_cps ==""){
					console.log('scale in 이 null 입니다.');
					$scope.postVal.scaleInCheck = true;
				}

				if($scope.reqData.out_cpu==""&& $scope.reqData.out_memory=="" && $scope.reqData.out_cps ==""){
					console.log('scale out 이 null 입니다.');
					$scope.postVal.scaleOutCheck = true;
				}
				
				if($scope.reqData.in_cpu > $scope.reqData.out_cpu ){
					console.log('scale in cpu 가 scale out cpu 보다 큽니다.');
					$scope.postVal.inOutValueCheck = "cpu";
				}
				
				if($scope.reqData.in_memory > $scope.reqData.out_memory ){
					console.log('scale in memory 가 scale out memory 보다 큽니다.');
					$scope.postVal.inOutValueCheck = "memory";
				}
				
				if($scope.reqData.in_cps > $scope.reqData.out_cps ){
					console.log('scale in cps 가 scale out cps 보다 큽니다.');
					$scope.postVal.inOutValueCheck = "cps";
				}
				
				
				console.log('$scope.postVal : ', $scope.postVal);
				console.log('post request reqData : ', $scope.reqData);
			
				if($scope.postVal.imageCheck == false  && $scope.postVal.flavorCheck==false && $scope.postVal.networkCheck == false 
						//&& $scope.postVal.keypairCHeck == false
						&& $scope.postVal.scaleInCheck == false && $scope.postVal.scaleOutCheck == false
						&& $scope.postVal.inOutValueCheck =="default"
						){
					
					console.log('post request를 진행 합니다.');
				
			    	$q.all([ScalePostAPI.update($scope.reqData).$promise]).then(function (result) {
			    		console.log('reqData : ', $scope.reqData, ' result : ', result, 'selectIndex : ', $scope.selectIndex);

			    		//서버 호출하여 업데이트한 데이터를 다시 가져 온다.
			    		$scope.init($scope.selectIndex);
			    	});
					
					
				} else {					
					console.log('유효하지 않은 값이 있습니다.');					
				}
				
				
				
			};

			//radio 변경된 부분의 값을 set 하는 부분.
			$scope.idSetter = function(item, option){
				console.log('item : ', item);
				console.log('option', option);

				if(option=='image'){
					$scope.reqData.img_id=item.id;
					$scope.reqData.img_name=item.name;					
				} else if(option=='flavor'){
					$scope.reqData.flavor_id=item.id;
					$scope.reqData.flavor_name=item.name;	
				} else if (option=='keypair'){
					$scope.reqData.keypair_name=item.name;	
				}

				//console.log('changed radioCheck : ', $scope.radioCheck);

			};

			//editmode, viewmode 전환
			$scope.viewModeToggle = function () {
				$scope.viewMode = $scope.viewMode == 0 ? 1 : 0;
				console.log('$scope.viewMode : ', $scope.viewMode );
			};

			//checkbox 확인.
			$scope.isChecked = function(networkName){
				var match = false;
				for(var i=0 ; i < $scope.selectedVal.network_info.length; i++) {
					if($scope.selectedVal.network_info[i].network_name == networkName){
						match = true;
					}
				}
				return match;
			};


			//network checkbox 값 변경 용도.
			$scope.sync = function(bool, item){
				
				console.log('##network value change');
				console.log('bool : ', bool);
				console.log('item : ', item);
				
				var networkVal = {
						"network_id":item.id,
						"network_name":item.name						
				};
				
				console.log('networkVal : ', networkVal);
				
				if(bool){
					// add item
					//$scope.data.push(item);
					$scope.reqData.network_info.push(networkVal);
				} else {
					// remove item
					for(var i=0 ; i < $scope.reqData.network_info.length; i++) {
						if($scope.reqData.network_info[i].id == networkVal.id){
							//$scope.data.splice(i,1);
							$scope.reqData.network_info.splice(i,1);
						}
					}      
				}
				
				console.log('request networkVal : ', $scope.reqData.network_info);
				
			};


		}])
