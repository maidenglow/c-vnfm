'use strict';

angular.module('app.directives', [])

.directive('imgHolder', [ ->
    return {
        restrict: 'A'
        link: (scope, ele, attrs) ->
            Holder.run(
                images: ele[0]
            )
    }
])


# add class for specific pages
.directive('customPage', () ->
    return {
        restrict: "A"
        controller: [
            '$scope', '$element', '$location'
            ($scope, $element, $location) ->
                path = ->
                    return $location.path()

                addBg = (path) ->
                    # remove all the classes
                    $element.removeClass('body-wide body-lock')

                    # add certain class based on path
                    switch path
                        when '/404', '/pages/404', '/pages/500', '/pages/signin', '/pages/signup', '/pages/forgot-password' then $element.addClass('body-wide')
                        when '/pages/lock-screen' then $element.addClass('body-wide body-lock')

                addBg( $location.path() )

                $scope.$watch(path, (newVal, oldVal) ->
                    if newVal is oldVal
                        return
                    addBg($location.path())
                )
        ]
    }
)

# switch stylesheet file
.directive('uiColorSwitch', [ ->
    return {
        restrict: 'A'
        link: (scope, ele, attrs) ->
            ele.find('.color-option').on('click', (event)->
                $this = $(this)
                hrefUrl = undefined

                style = $this.data('style')
                if style is 'loulou'
                    hrefUrl = 'styles/main.css'
                    $('link[href^="styles/main"]').attr('href',hrefUrl)
                else if style
                    style = '-' + style
                    hrefUrl = 'styles/main' + style + '.css'
                    $('link[href^="styles/main"]').attr('href',hrefUrl)
                else
                    return false

                event.preventDefault()
            )
    }
])


# history back button
.directive('goBack', [ ->
    return {
        restrict: "A"
        controller: [
            '$scope', '$element', '$window'
            ($scope, $element, $window) ->
                $element.on('click', ->
                    $window.history.back()
                )
        ]
    }
])

# resize fit
.directive('resizefit', [
	'$window'
	($window) ->
		return (scope, elem, attrs) ->
			win = angular.element $window

			#### 보정값 ####
			#CORRECTION_VALUE = 458
			CORRECTION_VALUE = 458
			correctionValue = CORRECTION_VALUE
			correctionValue = attrs.adjustValue if attrs.adjustValue

			scope.getWindowDemensions = ->
				return {
					'h': win.height()
					'w': win.width()
					'fit': win.height() - correctionValue
				}

			#### resize function ####
			resize = (fit) ->
				scope.style = ->
					'min-height': fit + 'px'
					'max-height': fit + 'px'

			### 최출호출 ###
			resize((win.height() - correctionValue))

			scope.$watch(scope.getWindowDemensions,
				(newValue, oldValue) ->
					if newValue is oldValue
						return

					scope.windowHeight = newValue.h
					scope.windowWidth = newValue.w

					resize(newValue.fit)
					
					'''
					scope.style = ->
						'min-height': (newValue.fit) + 'px'
						'max-height': (newValue.fit) + 'px'
					'''

					return
			, true)
			
			win.bind 'resize', (event) ->
				scope.$apply()
				return
])
