'use strict'

angular.module('app.systemsService', [])

.factory('SystemSystemsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.SYSTEM_URL.SYSTEMS,
			{
				systemId: "@systemId"
			},
			{
				update:
					method: "PUT"
			}
])
