'use strict'

app = angular.module('app.alarms', [])

.controller('AlarmsCtrl', [
	'$scope'
	'AlarmsSync'
	($scope, AlarmsSync) ->

		#========================================
		# $Scope
		#========================================

		#### alarms ####
		$scope.alarms = AlarmsSync.getAlarms()

		#========================================
		# $Scope Event
		#========================================

		#### Delete Alarm ####
		$scope.deleteAlarm = (item) ->
			AlarmsSync.deleteAlarm item

		#### $scope.$on ####		
		$scope.$on 'alarms:data-change', (event, data) ->
			$scope.alarms = data

		return # end of AlarmsCtrl
])

.controller('AlarmsHeaderCtrl', [
	'$scope'
	'AlarmsSync'
	($scope, AlarmsSync) ->

		#========================================
		# $Scope
		#========================================

		#### alarms ####
		$scope.alarms = AlarmsSync.getAlarms()

		#========================================
		# $Scope Event
		#========================================

		#### Delete Alarm ####
		$scope.deleteAlarm = (item) ->
			AlarmsSync.deleteAlarm item

		#### $scope.$on ####
		'''		
		$scope.$on 'alarms:data-change', (event, data) ->
			$scope.alarms = data
		'''

		return # end of AlarmsCtrl
])
