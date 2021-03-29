'use strict'

angular.module('app.telemetrysService', [])

.factory('TelemetryInstancesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.TELEMETRY.INSTANCE,
			{
				instanceId: "@instanceId"
			}
])
