
'use strict';
angular.module("app.descriptors")
.directive("nfvNfcEditor", ["$rootScope", function($rootScope) {
    return {
        restrict: "A",
        templateUrl: 'views/descriptor/nfc/nfc-template.html',
        replace: false,
        scope: {
            vld: "=ngModel",
            options: "=ngDescriptorOptions",
            editMode: "=editMode"
        },
        controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
        	if ($scope.vld != undefined) {
        		console.log('nfvVldEditor vld: ', $scope.vld);
        	}
        }]
    }
}]);