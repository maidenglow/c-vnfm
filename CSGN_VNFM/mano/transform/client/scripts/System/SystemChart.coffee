'use strict'

angular.module('app.systemsChart', [])

.directive('systemFlotChartRealtime', [
	'$timeout'
	($timeout) ->
		return {
		restrict: 'A'
		scope:
			percentage: '='
		link: (scope, ele, attrs) ->
		
			#### variables ####
			promise = null
			updateInterval = 5000

			data = []
			totalPoints = 150
			percentage = scope.percentage
			
			#### functions ####
			# init
			init = ->
				idx = 0
				while idx < totalPoints
					data.push 0
					++idx

			# to number
			toNumber = (val) ->
            	return 0 if not val
            	return val if typeof val is 'number'
            	try
            		return (Number) val.replace(/[^0-9.]/gi, '')
            	catch error
            		console.error error
            		return 0

			# get random data
			getChartData = ->
				data = data.slice(1)  if data.length > 0

				#while data.length < totalPoints
				data.push toNumber(percentage)

				# Zip the generated y values with the x values
				res = []
				i = 0

				while i < data.length
					res.push [i, data[i]]
					++i
				res

			update = ->
				#console.log 'percentage chart update!!! percentage => ', percentage
				$timeout.cancel promise

				plot.setData [getChartData()]

				# Since the axes don't change, we don't need to call plot.setupGrid()
				plot.draw()
				promise = $timeout update, updateInterval
				return

			plot = $.plot(ele[0], [getChartData()],
				series:
					lines:
						show: true
						fill: true
					shadowSize: 0
				yaxis:
					min: 0
					max: 100
				xaxis:
					show: false
					min: 0
					max: totalPoints
				grid:
					hoverable: true
					borderWidth: 1
					borderColor: '#eeeeee'
				tooltip: true
				tooltipOpts:
					content: "%y%"
				colors: ["#70b1cf"]
			)

			# initialize
			init()

			#update()

			# for lazy init
			scope.$watch 'percentage', (newVal, oldVal) ->
				# console.log 'percentage new value=', newVal, ', old value=', oldVal
				if newVal # and newVal isnt oldVal
					percentage = toNumber newVal
					update()
			
			# destroy
			scope.$on '$destroy', ->
				# console.log 'system chart directive destroy!!!!'
				$timeout.cancel promise
		}
])
