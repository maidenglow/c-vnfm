'use strict'

angular.module('app.imagesService', ['ngResource'])

.factory('ImageImagesAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		return $resource CONST_RESTFUL_API.IMAGE_URL.IMAGES,
			{
				id: "@id"
			},
			{
				update: {
					method: 'PUT'
				}
			}
])
