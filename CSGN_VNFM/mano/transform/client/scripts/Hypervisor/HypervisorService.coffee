'use strict'

angular.module('app.hypervisorService', [])

.factory('HypervisorAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.HYPERVISOR,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			}
])

.factory('HypervisorStatsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.HYPERVISOR_STATS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
			}
])