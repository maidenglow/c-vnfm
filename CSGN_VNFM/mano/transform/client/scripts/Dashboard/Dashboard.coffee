'use strict'

angular.module('app.dashboard', [])

.controller('DashboardCtrl', [
	'$scope'
	'logger'
	'$q'
	'HypervisorStatsAPI'
	'IdentityOSKSADMServicesAPI'
	(
		$scope
		logger
		$q
		HypervisorStatsAPI
		IdentityOSKSADMServicesAPI
	) ->

		#========================================
		# Initialization
		#========================================
		$scope.init = ->
			return $q.all([
				getOSKSADMServices().$promise
				getHypervisorStats().$promise
			]).then((result) ->
				osKsadmServices = result[0]
				$scope.osKsadmServices = osKsadmServices
				
				hypervisorStats = result[1]
				$scope.hypervisorStats = hypervisorStats

				#### ^Chart ####
				if hypervisorStats

					#### VCPU Chart ####
					$scope.cpuChartMax = hypervisorStats.vcpus
					$scope.cpuChartVal = hypervisorStats.vcpus_used

					#### Memory Chart ####
					$scope.memoryChartMax = hypervisorStats.memory_mb
					$scope.memoryChartVal = hypervisorStats.memory_mb_used

					#### Disk Chart ####
					$scope.diskChartMax = hypervisorStats.local_gb
					$scope.diskChartVal = hypervisorStats.local_gb_used

				#### $Chart ####
			)

		#========================================
		# Functions
		#========================================

		########################
		# get OS KSADM Services
		########################
		getOSKSADMServices = (service) ->
			IdentityOSKSADMServicesAPI.query service,
				(response) ->
					$scope.osKsadmServices = response
					return

		########################
		# get Hypervisor Statistics
		########################
		getHypervisorStats = (hypervisor) ->
			HypervisorStatsAPI.get hypervisor,
				(response) ->
					hypervisorStats = response

		#========================================
		# Scope
		#========================================
		
		# OS KSADM Services
		$scope.osKsadmServices

		# Hypervisor Statistics
		$scope.hypervisorStats
		
		#========================================
		# $Chart
		#========================================

		#### Gauge Chart Option ####
		$scope.gaugeChart =
			options:
                lines: 12
                angle: 0
                lineWidth: 0.47
                pointer:
                    length: 0.6
                    strokeWidth: 0.03
                    color: '#000000'
                limitMax: 'false'
                colorStart: '#A3C86D'
                colorStop: '#A3C86D',
                strokeColor: '#E0E0E0'
                generateGradient: true
                percentColors: [
                    [0.0, $scope.color.success]
                    [1.0, $scope.color.danger]
                ]
])
