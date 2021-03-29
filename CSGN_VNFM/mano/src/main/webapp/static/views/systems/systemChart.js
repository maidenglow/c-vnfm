
"use strict";
angular.module("app.systemsChart", []).directive("systemFlotChartRealtime", ["$timeout", function($timeout) {
    return {
        restrict: "A",
        scope: {
            percentage: "="
        },
        link: function(scope, ele) {
            var data, getChartData, init, percentage, plot, promise, toNumber, totalPoints, update, updateInterval;
            return promise = null, updateInterval = 5e3, data = [], totalPoints = 150, percentage = scope.percentage, init = function() {
                var idx, _results;
                for (idx = 0, _results = []; totalPoints > idx;) data.push(0), _results.push(++idx);
                return _results
            }, toNumber = function(val) {
                var error;
                if (!val) return 0;
                if ("number" == typeof val) return val;
                try {
                    return Number(val.replace(/[^0-9.]/gi, ""))
                } catch (_error) {
                    return error = _error, 0
                }
            }, getChartData = function() {
                var i, res;
                for (data.length > 0 && (data = data.slice(1)), data.push(toNumber(percentage)), res = [], i = 0; i < data.length;) res.push([i, data[i]]), ++i;
                return res
            }, update = function() {
                $timeout.cancel(promise), plot.setData([getChartData()]), plot.draw(), promise = $timeout(update, updateInterval)
            }, plot = $.plot(ele[0], [getChartData()], {
                series: {
                    lines: {
                        show: !0,
                        fill: !0
                    },
                    shadowSize: 0
                },
                yaxis: {
                    min: 0,
                    max: 100
                },
                xaxis: {
                    show: !1,
                    min: 0,
                    max: totalPoints
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 1,
                    borderColor: "#eeeeee"
                },
                tooltip: !0,
                tooltipOpts: {
                    content: "%y%"
                },
                colors: ["#70b1cf"]
            }), init(), scope.$watch("percentage", function(newVal) {
                return newVal ? (percentage = toNumber(newVal), update()) : void 0
            }), scope.$on("$destroy", function() {
                return $timeout.cancel(promise)
            })
        }
    }
}]);
