
'use strict';
angular.module("app.descriptors")
.directive("nfvUjpEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/ujp/ujp-template.html',
        replace: false,
        scope: {
            security_data: "=ngModel",
            editMode: "=editMode"
        },
        controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
        	if ($scope.security_data != undefined) {
        		console.log('nfvVldEditor security: ', $scope.security_data);
        	}
        }]
    }
}]);