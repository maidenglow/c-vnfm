'use strict'

angular.module('app.interceptors', [])

.factory('DefaultErrorInterceptor', [
	'$injector'
	'logger'
	($injector, logger) ->
		return (promise) ->
			return promise.then(
				null,
				(error) ->
					logger.logError "에러가 발생하였습니다.<br/>status: #{error.status}<br/>statusText: #{error.statusText}"
					return promise
			)
])

.config([
	'$httpProvider'
	($httpProvider) ->
		$httpProvider.responseInterceptors.push('DefaultErrorInterceptor');
])