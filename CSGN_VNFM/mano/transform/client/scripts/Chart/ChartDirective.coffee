'use strict'

angular.module('app.chart.directives', [])

.directive('gaugeChart', [ -> # The online docs seems outdate, i.e. not working
    return {
        restrict: 'A'
        scope:
            data: '='
            options: '='
        link: (scope, ele, attrs) ->
            data = scope.data
            options = scope.options

            gauge = new Gauge(ele[0]).setOptions(options)
            gauge.maxValue = data.maxValue
            gauge.animationSpeed = data.animationSpeed
            gauge.set(data.val)
    }
])

.directive('customGaugeChart', [ -> # The online docs seems outdate, i.e. not working
    return {
        restrict: 'A'
        scope:
        	max: '='
        	val: '='
        	options: '='
        link: (scope, ele, attrs) ->
        
            #### function ####
            # to number
            toNumber = (val) ->
                return 0 if not val
                return val if typeof val is 'number'
                try
                    return (Number) val.replace(/[^0-9.]/gi, '')
                catch error
                    console.error error
                    return 0

            # variables
            max = toNumber scope.max
            val = toNumber scope.val
            options = scope.options

            gauge = new Gauge(ele[0]).setOptions(options)
            gauge.animationSpeed = 20
            gauge.maxValue = max
            gauge.set(val)

			# watch max
            scope.$watch 'max', (newValue, oldValue) ->
            	if newValue and newValue isnt oldValue
            		gauge.maxValue = toNumber(newValue)

            # watch val
            scope.$watch 'val', (newValue, oldValue) ->
            	if newValue and newValue isnt oldValue
            		gauge.set(toNumber(newValue))
    }
])

.directive('flotChart', [ ->
    return {
        restrict: 'A'
        scope:
            data: '='
            options: '='
        link: (scope, ele, attrs) ->
            data = scope.data
            options = scope.options

            # console.log data
            # console.log options

            plot = $.plot(ele[0], data, options);

    }
])
.directive('flotChartRealtime', [ ->
    return {
        restrict: 'A'
        link: (scope, ele, attrs) ->
            data = []
            totalPoints = 300
            getRandomData = ->
                data = data.slice(1)  if data.length > 0

                # Do a random walk
                while data.length < totalPoints
                    prev = (if data.length > 0 then data[data.length - 1] else 50)
                    y = prev + Math.random() * 10 - 5
                    if y < 0
                        y = 0
                    else y = 100  if y > 100
                    data.push y

                # Zip the generated y values with the x values
                res = []
                i = 0

                while i < data.length
                    res.push [
                        i
                        data[i]
                    ]
                    ++i
                res

            update = ->
                plot.setData [getRandomData()]

                # Since the axes don't change, we don't need to call plot.setupGrid()
                plot.draw()
                setTimeout update, updateInterval
                return
            data = []
            totalPoints = 300
            updateInterval = 200
            plot = $.plot(ele[0], [getRandomData()],
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
                grid:
                    hoverable: true
                    borderWidth: 1
                    borderColor: '#eeeeee'
                colors: ["#70b1cf"]
            )
            update()

    }
])
.directive('sparkline', [ ->
    return {
        restrict: 'A'
        scope:
            data: '='
            options: '='
        link: (scope, ele, attrs) ->
            data = scope.data
            options = scope.options
            sparkResize = undefined

            sparklineDraw = ->
                ele.sparkline(data, options)

            $(window).resize( (e) ->
                clearTimeout(sparkResize)
                sparkResize = setTimeout(sparklineDraw, 200)
            )

            sparklineDraw()
    }
])
.directive('morrisChart', [ ->
    return {
        restrict: 'A'
        scope:
            data: '='
            type: '='
            options: '='
        link: (scope, ele, attrs) ->
            data = scope.data
            type = scope.type

            switch type
                when 'line'
                    options = angular.extend({ element: ele[0], data: data }, scope.options)
                    new Morris.Line( options )

                when 'area'
                    options = angular.extend({ element: ele[0], data: data }, scope.options)
                    new Morris.Area( options )

                when 'bar'
                    options = angular.extend({ element: ele[0], data: data }, scope.options)
                    new Morris.Bar( options )

                when 'donut'
                    options = angular.extend({ element: ele[0], data: data }, scope.options)
                    if options.formatter
                        func = new Function('y', 'data', options.formatter)
                        options.formatter = func
                    new Morris.Donut( options )

    }
])
