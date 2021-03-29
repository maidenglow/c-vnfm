'use strict'

angular.module('app.alarmsService', ['ngResource'])

.factory('SystemAlarmsAPI', [
	'$resource'
	'CONST_RESTFUL_API'
	($resource, CONST_RESTFUL_API) ->
		return $resource CONST_RESTFUL_API.SYSTEM_URL.ALARMS,
			{
				id: "@alarmId"
			},
			{
				search: {
				 	method: 'GET'
				 	params: {
				 		"pageSize": 200
				 		"alarmId": @alarmId
				 	}
				 	isArray: true
				}
				update: {
					method: 'PUT'
				}
			}
])

.factory('AlarmsSync', [
	'$rootScope'
	'$timeout'
	'SystemAlarmsAPI'
	($rootScope, $timeout, SystemAlarmsAPI) ->

		#### Variables ####
		updateInterval = 3000
		maxLen = 100

		#### Alarm ####
		alarm = {
			alarmId: null
		}

		#### Alarms ####
		alarms = []

		#### deleteAlarm ####
		deleteAlarm = (item) ->
			defaultParam = {
				alarmStatus: 'AS002'
			}
			angular.extend(item, defaultParam)
			SystemAlarmsAPI.update item,
				(response) ->
					if response
						alarms.splice(alarms.indexOf(item), 1)

			#### broadcast ####
			$rootScope.$broadcast 'alarms:data-change', alarms

		#### alarmId ####
		alarmId = null

		#### Update ####
		update = ->
			# console.log 'update ->', alarmId
			SystemAlarmsAPI.search { 'alarmId': alarmId },
				(response) ->
					if response and response.length > 0
						for item in response
							haveNot = true
							for _alarm in alarms
								if _alarm.alarmId is item.alarmId
									haveNot = !haveNot
									break

							alarms.push item if haveNot

							if alarms.length > maxLen
								alarms.splice(0, (alarms.length - maxLen))

						#### update alarmId ####
						alarmId = response[response.length - 1].alarmId

						#### broadcast ####
						$rootScope.$broadcast 'alarms:data-change', alarms

					#### update trigger ####
					$timeout update, updateInterval

			return # end of update function

		update()

		return {
			getAlarms: -> alarms
			deleteAlarm: (alarm) -> deleteAlarm(alarm)
		}
])
