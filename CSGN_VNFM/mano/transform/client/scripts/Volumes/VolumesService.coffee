'use strict'

angular.module('app.volumesService', [])


.factory('VolumesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			},
			{
				update:
					method: "PUT"
			}
])


.factory('VolumeExtendAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME_EXTEND,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			}
])






.factory('VolumeTypesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME_TYPE,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				id: "@id"
			}
])


.factory('VolumeLimitsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME_LIMITS,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
			}
])


.factory('VolumeSnapshotAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME_SNAPSHOT,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
			}
])

.factory('VolumeAttachmentAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		$resource CONST_RESTFUL_API.BLOCKSTORAGE_URL.VOLUME_ATTACHMENT,
			{
				tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
				serverId: "@serverId"
			}
])
