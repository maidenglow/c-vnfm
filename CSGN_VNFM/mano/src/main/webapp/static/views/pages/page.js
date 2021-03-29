"use strict";
angular.module("app.logins", [])
.factory("ComputeLoginAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.LOGIN)
}]).controller('SigninCtrl', ['$rootScope', '$scope', '$route', '$http', "$cookies",'$location','ComputeLoginAPI', 'logger',
                                                        	function($rootScope, $scope, $route, $http, $cookies, $location, ComputeLoginAPI, logger) {
	$scope.error = $route.current.params.error;
	$cookies.login= null;
	$scope.user = {
			userid : null,
			pass : null
	};
	if ($scope.error) {
		$rootScope.alert(true, '로그인 하셔야 합니다.');
	}
	$scope.fnSignin = function(user) {
		return ComputeLoginAPI.save(user, function(response) {
			response.success ? (logger.log("인증에 성공하였습니다.."),$cookies.login = "auth",$cookies.user = user.userid, $rootScope.$broadcast("user:data-change", user.userid), $location.url('/dashboard/dashboard')): logger.logError("인증에 실패하였습니다. " + response.fault);
//			console.log(response);
//            $scope.instances = response
        })
		
		/*if ($scope.form.$invalid) return;

		var request = $http({
			method : 'POST',
			url : '/mano/compute/login',
			params : user
		});

		request.success(function(data) {
			var access_token = data.access_token;
			$cookieStore.put('access_token', access_token);
			$location.url('/');
		});*/
	};
}
]);