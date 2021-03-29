'use strict'

angular.module('app.instancesService', [])

.factory('ComputeServersAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.SERVERS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			},
			{
				update:
					method: "PUT"
			}
])

.factory('ComputeServerActionsAPI', [
	'$http'
	'$resource'
	'CONST_RESTFUL_API'
	($http, $resource, CONST_RESTFUL_API) ->
		return $resource CONST_RESTFUL_API.COMPUTE_URL.SERVER_ACTIONS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			},
			{
				action: {
					method: "POST"
				}
			}
])

.factory('ComputeFlavorsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.FLAVORS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			},
			{
				update:
					method: "PUT"
			}
])

.factory('ComputeImagesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.IMAGES,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			}
])

.factory('ComputeLimitAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.LIMIT
])

.factory('InstanceLimitService', [
	'$rootScope'
	'ComputeLimitAPI'
	(	
		$rootScope
		ComputeLimitAPI
	) ->

		#### limit ####
		limit = null

		#### Instances ####
		instances = {
			max: 0
			used: 0
			percent: ->
				percent = percentage @max, @used
				if isNaN percent
					return 0
				return percent
		}
		
		#### VCPUs ####
		vcpus = {
			max: 0
			used: 0
			percent: ->
				percent = percentage @max, @used
				if isNaN percent
					return 0
				return percent 
		}
		
		#### RAM ####
		ram = {
			max: 0
			used: 0
			percent: ->
				percent = percentage @max, @used
				if isNaN percent
					return 0
				return percent
		}

		#### Init ####
		init = (limit) ->

			@limit = limit

			#### Instances ####
			instances.max  = limit.absolute.maxTotalInstances
			instances.used = limit.absolute.totalInstancesUsed

			#### VCPUs ####
			vcpus.max  = limit.absolute.maxTotalCores
			vcpus.used = limit.absolute.totalCoresUsed

			#### RAM ####
			ram.max  = limit.absolute.maxTotalRAMSize
			ram.used = limit.absolute.totalRAMUsed

			return

		#### Percentage ####
		percentage = (max, used) ->
			try
				return Math.round(((used / max) * 100), 2)
			catch error
				return 0

		return {
			init: init
			limit: limit
			instances: instances
			vcpus: vcpus
			ram: ram
		}
])

.factory('ComputeAvailabilityZonesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.AVAILABILITY_ZONES,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
			}
])


.factory('ComputeFloatingIpPoolsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.COMPUTE_URL.FLOATING_IP_POOLS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				poolName : "@poolName"
			}
])