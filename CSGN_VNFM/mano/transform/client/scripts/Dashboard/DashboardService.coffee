'use strict'

angular.module('app.dashboardService', ['ngResource'])

.factory('DashboardIdentityAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		return $resource CONST_RESTFUL_API.DASHBOARD_URL.IDENTITY
])
