'use strict'

angular.module('app.identityService', ['ngResource'])

.factory('IdentityOSKSADMServicesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		return $resource CONST_RESTFUL_API.IDENTITY_URL.OS_KSADM_SERVICES
])
