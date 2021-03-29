'use strict';

angular.module('app.filters', [])

.factory("fileSizeCalculrate", [
	'$rootScope'
	($rootScope) ->
		fileSizeCalculrator =
			bytes: (bytes, precision) ->
				"-" if isNaN(parseFloat(bytes)) or not isFinite(bytes)
				precision = 1 if typeof precision is "undefined"
				units = [
					"bytes"
					"kB"
					"MB"
					"GB"
					"TB"
					"PB"
				]
				number = Math.floor(Math.log(bytes) / Math.log(1024))
				(bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + " " + units[number]
	
])

.filter("bytes", [
	'fileSizeCalculrate'
	(fileSizeCalculrate) ->
		return (bytes, type, precision) ->
			fileSizeCalculrate.bytes(bytes, precision)
])

.filter("reverse", [
	-> (items) -> items.slice().reverse()
])

'''
fileSize = (bytes, precision) ->
	"-" if isNaN(parseFloat(bytes)) or not isFinite(bytes)
	precision = 1 if typeof precision is "undefined"
	units = [
		"bytes"
		"kB"
		"MB"
		"GB"
		"TB"
		"PB"
	]
	console.log 'weight ->', weight
	
	number = Math.floor(Math.log(bytes) / Math.log(1024))
	(bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + " " + units[number]
'''