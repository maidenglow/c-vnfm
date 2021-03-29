'use strict'

angular.module('app.systems', [])

.controller('SystemCtrl', [
	'$scope'
	'$location'
	'$timeout'
	'SystemSystemsAPI'
	(
		$scope
		$location
		$timeout
		SystemSystemsAPI
	) ->
	
		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			getSystems().$promise.then ->
				systems = $scope.systems
				if systems and systems.length > 0
					system = systems[0]
					$scope.selectItem system
					refreshSystem()
				return
			return

		#========================================
		# Functions
		#========================================

		########################
		# get Systems
		########################
		getSystems = (system) ->
			SystemSystemsAPI.query system,
				(response) ->
					$scope.systems = response
					return

		########################
		# get System
		########################
		getSystem = (system) ->
			SystemSystemsAPI.get system,
				(response) ->
					$scope.system = response
					return

		########################
		# refresh System
		########################
		timer = null
		interval = 3000
		refreshSystem = ->
			$timeout.cancel timer
			if '/systems/index' is $location.path() 
				console.log 'path is matched!!!!'
				getSystem($scope.system)
				timer = $timeout refreshSystem, interval

		#========================================
		# $Scope
		#========================================
		
		########################
		# Systems
		########################
		$scope.systems
		
		$scope.itemStatusClass = (system) ->
			if system.status is 'Y'
				'label label-success'
			else
				'label label-danger'

		########################
		# System
		########################
		$scope.system

		$scope.selectItem = (system) ->
			$scope.system = system

		$scope.isSelected = (system) ->
			$scope.system is system

		#========================================
		# $Event
		#========================================
		#### Location Change Event ####
		$scope.$on '$destroy', ->
			console.log 'system controller $destroy !!!!'
			$timeout.cancel timer

])