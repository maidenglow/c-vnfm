(function() {
    "use strict";
    var app;
    app = angular.module("app.alarms", [])
    .controller("AlarmsCtrl", ["$cookies", "$scope", "AlarmsSync", function($cookies, $scope, AlarmsSync) {
        $scope.alarms = AlarmsSync.getAlarms(), $scope.deleteAlarm = function(item) {
            return AlarmsSync.deleteAlarm(item)
        }, $scope.$on("alarms:data-change", function(event, data) {
            return $scope.alarms = data;
        })
    }]).controller("HeaderCtrl", ["$cookies", "$scope", function($cookies, $scope) {
    	$scope.user = $cookies.user;
        $scope.$on("user:data-change", function(event, data) {
            return $scope.user = data;
        })
    }]).controller("AlarmsHeaderCtrl", ["$scope", "AlarmsSync", function($scope, AlarmsSync) {
        $scope.alarms = AlarmsSync.getAlarms(), $scope.deleteAlarm = function(item) {
            return AlarmsSync.deleteAlarm(item)
        }, $scope.$on("alarms:data-change", function(event, data) {
            return $scope.alarms = data;
        })
    }]).controller("HistorysCtrl", ["$timeout", "$scope", "VnfmHistoryAPI", function($timeout,$scope, VnfmHistoryAPI) {
    	var updateInterval, update;
        $scope.historys,updateInterval = 3e3
        ,$scope.init = function() {
        }, update =  function() {
        	VnfmHistoryAPI.query({
            }, function(response) {
            	var historys;
                $scope.historys = response;
                //jch::dev
                return $timeout(update, updateInterval)
            })
        },update()
    }])
}).call(this),
    function() {
        "use strict";
        angular.module("app.alarmsService", ["ngResource"])
        .factory("SystemAlarmsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.SYSTEM_URL.ALARMS, {
                id: "@alarmId"
            }, {
                search: {
                    method: "GET",
                    params: {
                        pageSize: 200,
                        alarmId: this.alarmId
                    },
                    isArray: !0
                },
                update: {
                    method: "PUT"
                }
            })
        }]).factory("VnfmHistoryAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.VNFD_URL.HISTORYS)
        }]).factory("AlarmsSync", ["$rootScope", "$timeout", "SystemAlarmsAPI", function($rootScope, $timeout, SystemAlarmsAPI) {
            var alarm, alarmId, alarms, deleteAlarm, maxLen, update, updateInterval, refleshAlarm, playAudio;
            return updateInterval = 3e3, maxLen = 100, alarm = {
                alarmId: null
            }, alarms = [], playAudio = function(item) {
            	var soundFile;
            	if(item == 3){
            		soundFile = "audio/all_critical.wav";
            	}else if(item == 2){
            		soundFile = "audio/all_major.wav";
            	}else if(item == 1){
            		soundFile = "audio/all_minor.wav";
            	}else {
            		soundFile = "audio/all_clear.wav";
            	}
                var audio = new Audio(soundFile);
                audio.play();
            }, deleteAlarm = function(item) {
                var defaultParam;
                return defaultParam = {
                    alarmStatus: "AS002"
                }, angular.extend(item, defaultParam), SystemAlarmsAPI.update(item, function(response) {
                    return response ? alarms.splice(alarms.indexOf(item), 1) : void 0
                }), $rootScope.$broadcast("alarms:data-change", alarms)
            }, alarmId = null, update = function() {
                SystemAlarmsAPI.search({
                    alarmId: null
                }, function(response) {
                	
                	if(response.length > 0){
                    	/*alarmId = response[response.length - 1].alarmId,*/ /*$rootScope.$broadcast("alarms:data-change", alarms)*/
                    	var alarrmGrade = 0, _i, _len;
                    	for (_i = 0, _len = response.length; _len > _i; _i++) {
                    		if(response[_i].alarmGrade == "AG001"){
                    			if(alarrmGrade < 3){
                    				alarrmGrade = 3;
                    			}
//                    			console.log("1 : "+alarrmGrade)
                    			break;
                    		}else if(response[_i].alarmGrade == "AG002"){
                    			if(alarrmGrade < 2){
                    				alarrmGrade = 2;
                    			}
//                    			console.log("2 : "+alarrmGrade)
                    		}else if(response[_i].alarmGrade == "AG003"){
                    			if(alarrmGrade < 1){
                    				alarrmGrade = 1;
                    			}
//                    			console.log("3 : "+alarrmGrade)
                    		}
                    	}
                    	if(alarrmGrade != 0)
            			playAudio(alarrmGrade);
                	}
                	
                	if(response.length == 0){
                    	if(alarms.length > 0 ){
                    		playAudio(0);
	                    	alarms = response;
	                    	alarmId = null;
	                    	alarms =response;
	                    	$rootScope.$broadcast("alarms:data-change", alarms);
	                    	return $timeout(update, updateInterval);
                    	}
                    }
                	alarms =response; $rootScope.$broadcast("alarms:data-change", alarms);
                    var haveNot, item, _alarm, _i, _j, _len, _len1,_response;
//                    if (response && response.length > 0) {
//                    	/*for (_i = alarms.length, _len = alarms.length; _len < _i; _i--) {
//                            for (item = alarms[_i], haveNot = !0, _j = 0, _len1 = response.length; _len1 > _j; _j++){
//                                if (_response = response[_j], _response.alarmId === item.alarmId) {
//                                    haveNot = !haveNot;
//                                    break
//                                }
//                            }
//                            haveNot && alarms.splice(i,1);
//                        }*/
//                        for (_i = 0, _len = response.length; _len > _i; _i++) {
//                            for (item = response[_i], haveNot = !0, _j = 0, _len1 = alarms.length; _len1 > _j; _j++){
//                                if (_alarm = alarms[_j], _alarm.alarmId === item.alarmId) {
//                                    haveNot = !haveNot;
//                                    break
//                                }
//                            }
//                            haveNot && alarms.push(item), alarms.length > maxLen && alarms.splice(0, alarms.length - maxLen)
//                        }
//                        /*alarmId = response[response.length - 1].alarmId,*/ $rootScope.$broadcast("alarms:data-change", alarms)
//                    }
                	//jch:dev
                    return $timeout(update, updateInterval)
                })
            }, update(), {
                getAlarms: function() {
                	console.log('getAlarms');
                    return alarms
                },
                deleteAlarm: function(alarm) {
                    return deleteAlarm(alarm)
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.chart.ctrls", []).controller("chartCtrl", ["$scope", function($scope) {
            return $scope.easypiechart = {
                percent: 65,
                options: {
                    animate: {
                        duration: 1e3,
                        enabled: !0
                    },
                    barColor: $scope.color.primary,
                    lineCap: "round",
                    size: 180,
                    lineWidth: 5
                }
            }, $scope.easypiechart2 = {
                percent: 35,
                options: {
                    animate: {
                        duration: 1e3,
                        enabled: !0
                    },
                    barColor: $scope.color.success,
                    lineCap: "round",
                    size: 180,
                    lineWidth: 10
                }
            }, $scope.easypiechart3 = {
                percent: 68,
                options: {
                    animate: {
                        duration: 1e3,
                        enabled: !0
                    },
                    barColor: $scope.color.info,
                    lineCap: "square",
                    size: 180,
                    lineWidth: 20,
                    scaleLength: 0
                }
            }, $scope.gaugeChart1 = {
                data: {
                    maxValue: 3e3,
                    animationSpeed: 40,
                    val: 1375
                },
                options: {
                    lines: 12,
                    angle: 0,
                    lineWidth: .47,
                    pointer: {
                        length: .6,
                        strokeWidth: .03,
                        color: "#000000"
                    },
                    limitMax: "false",
                    strokeColor: "#E0E0E0",
                    generateGradient: !0,
                    percentColors: [
                        [0, $scope.color.success],
                        [1, $scope.color.success]
                    ]
                }
            }, $scope.gaugeChart2 = {
                data: {
                    maxValue: 3e3,
                    animationSpeed: 45,
                    val: 1200
                },
                options: {
                    lines: 12,
                    angle: 0,
                    lineWidth: .47,
                    pointer: {
                        length: .6,
                        strokeWidth: .03,
                        color: "#464646"
                    },
                    limitMax: "true",
                    colorStart: "#7ACBEE",
                    colorStop: "#7ACBEE",
                    strokeColor: "#F1F1F1",
                    generateGradient: !0,
                    percentColors: [
                        [0, $scope.color.info],
                        [1, $scope.color.info]
                    ]
                }
            }, $scope.gaugeChart3 = {
                data: {
                    maxValue: 3e3,
                    animationSpeed: 50,
                    val: 1100
                },
                options: {
                    lines: 12,
                    angle: 0,
                    lineWidth: .47,
                    pointer: {
                        length: .6,
                        strokeWidth: .03,
                        color: "#464646"
                    },
                    limitMax: "true",
                    colorStart: "#FF7857",
                    colorStop: "#FF7857",
                    strokeColor: "#F1F1F1",
                    generateGradient: !0,
                    percentColors: [
                        [0, $scope.color.danger],
                        [1, $scope.color.danger]
                    ]
                }
            }
        }]).controller("morrisChartCtrl", ["$scope", function($scope) {
            var barColor, barData, comboColor, comboData, donutColor, donutData, mainColor, mainData, simpleColor, simpleData;
            return mainData = [{
                month: "2013-01",
                xbox: 294e3,
                will: 136e3,
                playstation: 244e3
            }, {
                month: "2013-02",
                xbox: 228e3,
                will: 335e3,
                playstation: 127e3
            }, {
                month: "2013-03",
                xbox: 199e3,
                will: 159e3,
                playstation: 13e4
            }, {
                month: "2013-04",
                xbox: 174e3,
                will: 16e4,
                playstation: 82e3
            }, {
                month: "2013-05",
                xbox: 255e3,
                will: 318e3,
                playstation: 82e3
            }, {
                month: "2013-06",
                xbox: 298400,
                will: 401800,
                playstation: 98600
            }, {
                month: "2013-07",
                xbox: 37e4,
                will: 225e3,
                playstation: 159e3
            }, {
                month: "2013-08",
                xbox: 376700,
                will: 303600,
                playstation: 13e4
            }, {
                month: "2013-09",
                xbox: 527800,
                will: 301e3,
                playstation: 119400
            }], mainColor = [$scope.color.infoAlt, $scope.color.danger, $scope.color.success], $scope.main = {
                data: mainData,
                type: "area",
                options: {
                    xkey: "month",
                    ykeys: ["xbox", "will", "playstation"],
                    labels: ["xbox", "will", "playstation"],
                    lineColors: mainColor,
                    lineWidth: 0,
                    behaveLikeLine: !0,
                    pointSize: 0
                }
            }, simpleData = [{
                year: "2008",
                value: 20
            }, {
                year: "2009",
                value: 10
            }, {
                year: "2010",
                value: 5
            }, {
                year: "2011",
                value: 5
            }, {
                year: "2012",
                value: 20
            }, {
                year: "2013",
                value: 19
            }], simpleColor = [$scope.color.primary], $scope.simple1 = {
                data: simpleData,
                type: "line",
                options: {
                    xkey: "year",
                    ykeys: ["value"],
                    labels: ["Value"],
                    lineWidth: "2",
                    lineColors: simpleColor
                }
            }, $scope.simple2 = {
                data: simpleData,
                type: "area",
                options: {
                    xkey: "year",
                    ykeys: ["value"],
                    labels: ["Value"],
                    lineWidth: "2",
                    lineColors: simpleColor
                }
            }, comboData = [{
                month: "1",
                a: 20,
                b: 30
            }, {
                month: "2",
                a: 30,
                b: 20
            }, {
                month: "3",
                a: 20,
                b: 10
            }, {
                month: "4",
                a: 10,
                b: 20
            }, {
                month: "5",
                a: 20,
                b: 30
            }, {
                month: "6",
                a: 30,
                b: 20
            }, {
                month: "7",
                a: 20,
                b: 10
            }, {
                month: "8",
                a: 10,
                b: 20
            }, {
                month: "9",
                a: 20,
                b: 30
            }, {
                month: "10",
                a: 30,
                b: 20
            }, {
                month: "11",
                a: 20,
                b: 10
            }, {
                month: "12",
                a: 10,
                b: 20
            }], comboColor = [$scope.color.success, $scope.color.danger, $scope.color.infoAlt], $scope.combo1 = {
                data: comboData,
                type: "line",
                options: {
                    xkey: "month",
                    ykeys: ["a", "b"],
                    labels: ["Value A", "Value B"],
                    lineWidth: "2",
                    lineColors: comboColor
                }
            }, $scope.combo2 = {
                data: comboData,
                type: "area",
                options: {
                    xkey: "month",
                    ykeys: ["a", "b"],
                    labels: ["Value A", "Value B"],
                    lineWidth: "2",
                    lineColors: comboColor
                }
            }, barData = [{
                year: "2008",
                a: 20,
                b: 16,
                c: 12
            }, {
                year: "2009",
                a: 10,
                b: 22,
                c: 30
            }, {
                year: "2010",
                a: 5,
                b: 14,
                c: 20
            }, {
                year: "2011",
                a: 5,
                b: 12,
                c: 19
            }, {
                year: "2012",
                a: 20,
                b: 19,
                c: 13
            }, {
                year: "2013",
                a: 28,
                b: 22,
                c: 20
            }], barColor = [$scope.color.infoAlt, $scope.color.success, $scope.color.warning], $scope.bar1 = {
                data: barData,
                type: "bar",
                options: {
                    xkey: "year",
                    ykeys: ["a", "b", "c"],
                    labels: ["Value A", "Value B", "Value C"],
                    barColors: barColor
                }
            }, $scope.bar2 = {
                data: barData,
                type: "bar",
                options: {
                    xkey: "year",
                    ykeys: ["a", "b", "c"],
                    labels: ["Value A", "Value B", "Value C"],
                    barColors: barColor,
                    stacked: !0
                }
            }, donutColor = [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger], donutData = [{
                label: "Download Sales",
                value: 12
            }, {
                label: "In-Store Sales",
                value: 30
            }, {
                label: "Mail-Order Sales",
                value: 20
            }, {
                label: "Online Sales",
                value: 19
            }], $scope.donut1 = {
                data: donutData,
                type: "donut",
                options: {
                    xkey: "year"
                }
            }, $scope.donut2 = {
                data: donutData,
                type: "donut",
                options: {
                    xkey: "year",
                    colors: donutColor
                }
            }, $scope.donut3 = {
                data: donutData,
                type: "donut",
                options: {
                    xkey: "year",
                    formatter: "return '$' + y;"
                }
            }
        }]).controller("flotChartCtrl", ["$scope", function($scope) {
            var areaChart, barChart, lineChart1;
            return lineChart1 = {}, lineChart1.data1 = [
                [1, 15],
                [2, 20],
                [3, 14],
                [4, 10],
                [5, 10],
                [6, 20],
                [7, 28],
                [8, 26],
                [9, 22]
            ], $scope.line1 = {}, $scope.line1.data = [{
                data: lineChart1.data1,
                label: "Trend"
            }], $scope.line1.options = {
                series: {
                    lines: {
                        show: !0,
                        fill: !0,
                        fillColor: {
                            colors: [{
                                opacity: 0
                            }, {
                                opacity: .3
                            }]
                        }
                    },
                    points: {
                        show: !0,
                        lineWidth: 2,
                        fill: !0,
                        fillColor: "#ffffff",
                        symbol: "circle",
                        radius: 5
                    }
                },
                colors: [$scope.color.primary, $scope.color.infoAlt],
                tooltip: !0,
                tooltipOpts: {
                    defaultTheme: !1
                },
                grid: {
                    hoverable: !0,
                    clickable: !0,
                    tickColor: "#f9f9f9",
                    borderWidth: 1,
                    borderColor: "#eeeeee"
                },
                xaxis: {
                    ticks: [
                        [1, "Jan."],
                        [2, "Feb."],
                        [3, "Mar."],
                        [4, "Apr."],
                        [5, "May"],
                        [6, "June"],
                        [7, "July"],
                        [8, "Aug."],
                        [9, "Sept."],
                        [10, "Oct."],
                        [11, "Nov."],
                        [12, "Dec."]
                    ]
                }
            }, areaChart = {}, areaChart.data1 = [
                [2007, 15],
                [2008, 20],
                [2009, 10],
                [2010, 5],
                [2011, 5],
                [2012, 20],
                [2013, 28]
            ], areaChart.data2 = [
                [2007, 15],
                [2008, 16],
                [2009, 22],
                [2010, 14],
                [2011, 12],
                [2012, 19],
                [2013, 22]
            ], $scope.area = {}, $scope.area.data = [{
                data: areaChart.data1,
                label: "Value A",
                lines: {
                    fill: !0
                }
            }, {
                data: areaChart.data2,
                label: "Value B",
                points: {
                    show: !0
                },
                yaxis: 2
            }], $scope.area.options = {
                series: {
                    lines: {
                        show: !0,
                        fill: !1
                    },
                    points: {
                        show: !0,
                        lineWidth: 2,
                        fill: !0,
                        fillColor: "#ffffff",
                        symbol: "circle",
                        radius: 5
                    },
                    shadowSize: 0
                },
                grid: {
                    hoverable: !0,
                    clickable: !0,
                    tickColor: "#f9f9f9",
                    borderWidth: 1,
                    borderColor: "#eeeeee"
                },
                colors: [$scope.color.success, $scope.color.infoAlt],
                tooltip: !0,
                tooltipOpts: {
                    defaultTheme: !1
                },
                xaxis: {
                    mode: "time"
                },
                yaxes: [{}, {
                    position: "right"
                }]
            }, barChart = {}, barChart.data1 = [
                [2008, 20],
                [2009, 10],
                [2010, 5],
                [2011, 5],
                [2012, 20],
                [2013, 28]
            ], barChart.data2 = [
                [2008, 16],
                [2009, 22],
                [2010, 14],
                [2011, 12],
                [2012, 19],
                [2013, 22]
            ], barChart.data3 = [
                [2008, 12],
                [2009, 30],
                [2010, 20],
                [2011, 19],
                [2012, 13],
                [2013, 20]
            ], $scope.barChart = {}, $scope.barChart.data = [{
                label: "Value A",
                data: barChart.data1
            }, {
                label: "Value B",
                data: barChart.data2
            }, {
                label: "Value C",
                data: barChart.data3
            }], $scope.barChart.options = {
                series: {
                    stack: !0,
                    bars: {
                        show: !0,
                        fill: 1,
                        barWidth: .3,
                        align: "center",
                        horizontal: !1,
                        order: 1
                    }
                },
                grid: {
                    hoverable: !0,
                    borderWidth: 1,
                    borderColor: "#eeeeee"
                },
                tooltip: !0,
                tooltipOpts: {
                    defaultTheme: !1
                },
                colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger]
            }, $scope.pieChart = {}, $scope.pieChart.data = [{
                label: "Download Sales",
                data: 12
            }, {
                label: "In-Store Sales",
                data: 30
            }, {
                label: "Mail-Order Sales",
                data: 20
            }, {
                label: "Online Sales",
                data: 19
            }], $scope.pieChart.options = {
                series: {
                    pie: {
                        show: !0
                    }
                },
                legend: {
                    show: !0
                },
                grid: {
                    hoverable: !0,
                    clickable: !0
                },
                colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger],
                tooltip: !0,
                tooltipOpts: {
                    content: "%p.0%, %s",
                    defaultTheme: !1
                }
            }, $scope.donutChart = {}, $scope.donutChart.data = [{
                label: "Download Sales",
                data: 12
            }, {
                label: "In-Store Sales",
                data: 30
            }, {
                label: "Mail-Order Sales",
                data: 20
            }, {
                label: "Online Sales",
                data: 19
            }], $scope.donutChart.options = {
                series: {
                    pie: {
                        show: !0,
                        innerRadius: .5
                    }
                },
                legend: {
                    show: !0
                },
                grid: {
                    hoverable: !0,
                    clickable: !0
                },
                colors: [$scope.color.success, $scope.color.info, $scope.color.warning, $scope.color.danger],
                tooltip: !0,
                tooltipOpts: {
                    content: "%p.0%, %s",
                    defaultTheme: !1
                }
            }, $scope.donutChart2 = {}, $scope.donutChart2.data = [{
                label: "Download Sales",
                data: 12
            }, {
                label: "In-Store Sales",
                data: 30
            }, {
                label: "Mail-Order Sales",
                data: 20
            }, {
                label: "Online Sales",
                data: 19
            }, {
                label: "Direct Sales",
                data: 15
            }], $scope.donutChart2.options = {
                series: {
                    pie: {
                        show: !0,
                        innerRadius: .45
                    }
                },
                legend: {
                    show: !1
                },
                grid: {
                    hoverable: !0,
                    clickable: !0
                },
                colors: ["#1BB7A0", "#39B5B9", "#52A3BB", "#619CC4", "#6D90C5"],
                tooltip: !0,
                tooltipOpts: {
                    content: "%p.0%, %s",
                    defaultTheme: !0
                }
            }
        }]).controller("sparklineCtrl", ["$scope", function($scope) {
            return $scope.demoData1 = {
                data: [3, 1, 2, 2, 4, 6, 4, 5, 2, 4, 5, 3, 4, 6, 4, 7],
                options: {
                    type: "line",
                    lineColor: "#fff",
                    highlightLineColor: "#fff",
                    fillColor: $scope.color.success,
                    spotColor: !1,
                    minSpotColor: !1,
                    maxSpotColor: !1,
                    width: "100%",
                    height: "150px"
                }
            }, $scope.simpleChart1 = {
                data: [3, 1, 2, 3, 5, 3, 4, 2],
                options: {
                    type: "line",
                    lineColor: $scope.color.primary,
                    fillColor: "#fafafa",
                    spotColor: !1,
                    minSpotColor: !1,
                    maxSpotColor: !1
                }
            }, $scope.simpleChart2 = {
                data: [3, 1, 2, 3, 5, 3, 4, 2],
                options: {
                    type: "bar",
                    barColor: $scope.color.primary
                }
            }, $scope.simpleChart3 = {
                data: [3, 1, 2, 3, 5, 3, 4, 2],
                options: {
                    type: "pie",
                    sliceColors: [$scope.color.primary, $scope.color.success, $scope.color.info, $scope.color.infoAlt, $scope.color.warning, $scope.color.danger]
                }
            }, $scope.tristateChart1 = {
                data: [1, 2, -3, -5, 3, 1, -4, 2],
                options: {
                    type: "tristate",
                    posBarColor: $scope.color.success,
                    negBarColor: $scope.color.danger
                }
            }, $scope.largeChart1 = {
                data: [3, 1, 2, 3, 5, 3, 4, 2],
                options: {
                    type: "line",
                    lineColor: $scope.color.info,
                    highlightLineColor: "#fff",
                    fillColor: $scope.color.info,
                    spotColor: !1,
                    minSpotColor: !1,
                    maxSpotColor: !1,
                    width: "100%",
                    height: "150px"
                }
            }, $scope.largeChart2 = {
                data: [3, 1, 2, 3, 5, 3, 4, 2],
                options: {
                    type: "bar",
                    barColor: $scope.color.success,
                    barWidth: 10,
                    width: "100%",
                    height: "150px"
                }
            }, $scope.largeChart3 = {
                data: [3, 1, 2, 3, 5],
                options: {
                    type: "pie",
                    sliceColors: [$scope.color.primary, $scope.color.success, $scope.color.info, $scope.color.infoAlt, $scope.color.warning, $scope.color.danger],
                    width: "150px",
                    height: "150px"
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.chart.directives", []).directive("gaugeChart", [function() {
            return {
                restrict: "A",
                scope: {
                    data: "=",
                    options: "="
                },
                link: function(scope, ele) {
                    var data, gauge, options;
                    return data = scope.data, options = scope.options, gauge = new Gauge(ele[0]).setOptions(options), gauge.maxValue = data.maxValue, gauge.animationSpeed = data.animationSpeed, gauge.set(data.val)
                }
            }
        }]).directive("customGaugeChart", [function() {
            return {
                restrict: "A",
                scope: {
                    max: "=",
                    val: "=",
                    options: "="
                },
                link: function(scope, ele) {
                    var gauge, max, options, toNumber, val;
                    return toNumber = function(val) {
                        var error;
                        if (!val) return 0;
                        if ("number" == typeof val) return val;
                        try {
                            return Number(val.replace(/[^0-9.]/gi, ""))
                        } catch (_error) {
                            return error = _error, 0
                        }
                    }, max = toNumber(scope.max), val = toNumber(scope.val), options = scope.options, gauge = new Gauge(ele[0]).setOptions(options), gauge.animationSpeed = 20, gauge.maxValue = max, gauge.set(val), scope.$watch("max", function(newValue, oldValue) {
                        return newValue && newValue !== oldValue ? gauge.maxValue = toNumber(newValue) : void 0
                    }), scope.$watch("val", function(newValue, oldValue) {
                        return newValue && newValue !== oldValue ? gauge.set(toNumber(newValue)) : void 0
                    })
                }
            }
        }]).directive("flotChart", [function() {
            return {
                restrict: "A",
                scope: {
                    data: "=",
                    options: "="
                },
                link: function(scope, ele) {
                    var data, options, plot;
                    return data = scope.data, options = scope.options, plot = $.plot(ele[0], data, options)
                }
            }
        }]).directive("flotChartRealtime", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    var data, getRandomData, plot, totalPoints, update, updateInterval;
                    return data = [], totalPoints = 300, getRandomData = function() {
                        var i, prev, res, y;
                        for (data.length > 0 && (data = data.slice(1)); data.length < totalPoints;) prev = data.length > 0 ? data[data.length - 1] : 50, y = prev + 10 * Math.random() - 5, 0 > y ? y = 0 : y > 100 && (y = 100), data.push(y);
                        for (res = [], i = 0; i < data.length;) res.push([i, data[i]]), ++i;
                        return res
                    }, update = function() {
                        plot.setData([getRandomData()]), plot.draw(), setTimeout(update, updateInterval)
                    }, data = [], totalPoints = 300, updateInterval = 200, plot = $.plot(ele[0], [getRandomData()], {
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
                            show: !1
                        },
                        grid: {
                            hoverable: !0,
                            borderWidth: 1,
                            borderColor: "#eeeeee"
                        },
                        colors: ["#70b1cf"]
                    }), update()
                }
            }
        }]).directive("sparkline", [function() {
            return {
                restrict: "A",
                scope: {
                    data: "=",
                    options: "="
                },
                link: function(scope, ele) {
                    var data, options, sparkResize, sparklineDraw;
                    return data = scope.data, options = scope.options, sparkResize = void 0, sparklineDraw = function() {
                        return ele.sparkline(data, options)
                    }, $(window).resize(function() {
                        return clearTimeout(sparkResize), sparkResize = setTimeout(sparklineDraw, 200)
                    }), sparklineDraw()
                }
            }
        }]).directive("morrisChart", [function() {
            return {
                restrict: "A",
                scope: {
                    data: "=",
                    type: "=",
                    options: "="
                },
                link: function(scope, ele) {
                    var data, func, options, type;
                    switch (data = scope.data, type = scope.type) {
                        case "line":
                            return options = angular.extend({
                                element: ele[0],
                                data: data
                            }, scope.options), new Morris.Line(options);
                        case "area":
                            return options = angular.extend({
                                element: ele[0],
                                data: data
                            }, scope.options), new Morris.Area(options);
                        case "bar":
                            return options = angular.extend({
                                element: ele[0],
                                data: data
                            }, scope.options), new Morris.Bar(options);
                        case "donut":
                            return options = angular.extend({
                                element: ele[0],
                                data: data
                            }, scope.options), options.formatter && (func = new Function("y", "data", options.formatter), options.formatter = func), new Morris.Donut(options)
                    }
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.dashboard", []).controller("DashboardCtrl", ["$scope", "logger", "$q", "HypervisorStatsAPI", "IdentityOSKSADMServicesAPI", "ComputeServiceInfoAPI", "NetworkAgentInfoAPI" ,"EndpointInfoAPI" , 
                                                                         function($scope, logger, $q, HypervisorStatsAPI, IdentityOSKSADMServicesAPI, ComputeServiceInfoAPI, NetworkAgentInfoAPI, EndpointInfoAPI) {
            var getHypervisorStats, getOSKSADMServices, getComputeServices, getNetworkAgents, getEndpoints;
            return $scope.init = function() {
                return $q.all([getOSKSADMServices().$promise, getHypervisorStats().$promise,  getComputeServices().$promise, getNetworkAgents().$promise, getEndpoints().$promise]).then(function(result) {
                    var hypervisorStats, osKsadmServices;
                    return osKsadmServices = result[0], $scope.osKsadmServices = osKsadmServices, hypervisorStats = result[1], $scope.hypervisorStats = hypervisorStats, hypervisorStats ? ($scope.cpuChartMax = hypervisorStats.vcpus, $scope.cpuChartVal = hypervisorStats.vcpus_used, $scope.memoryChartMax = hypervisorStats.memory_mb, $scope.memoryChartVal = hypervisorStats.memory_mb_used, $scope.diskChartMax = hypervisorStats.local_gb, $scope.diskChartVal = hypervisorStats.local_gb_used) : void 0
                })
            }, getOSKSADMServices = function(service) {
                return IdentityOSKSADMServicesAPI.query(service, function(response) {
                    $scope.osKsadmServices = response;
                })
            }, getHypervisorStats = function(hypervisor) {
                return HypervisorStatsAPI.get(hypervisor, function(response) {
                    var hypervisorStats;
                    return hypervisorStats = response;
                })
            }, getComputeServices = function(computeServices) {
                return ComputeServiceInfoAPI.query(computeServices,function(response) {
                    $scope.computeServices = response;
                })
            }, getNetworkAgents = function(networkAgents) {
                return NetworkAgentInfoAPI.query(networkAgents,function(response) {
                	$scope.networkAgents = response;
                })
            }, getEndpoints = function(endpoints) {
                return EndpointInfoAPI.query(endpoints,function(response) {
                	$scope.endpoints = response;
                })
            }, $scope.osKsadmServices, $scope.hypervisorStats,$scope.computeServices,$scope.networkAgents, $scope.gaugeChart = {
                options: {
                    lines: 12,
                    angle: 0,
                    lineWidth: .47,
                    pointer: {
                        length: .6,
                        strokeWidth: .03,
                        color: "#000000"
                    },
                    limitMax: "false",
                    colorStart: "#A3C86D",
                    colorStop: "#A3C86D",
                    strokeColor: "#E0E0E0",
                    generateGradient: !0,
                    percentColors: [
                        [0, $scope.color.success],
                        [1, $scope.color.danger]
                    ]
                }
            }
        }])
    }.call(this),
    /*
    function() {
        "use strict";
        angular.module("app.test", []).controller("TestCtrl", ["$scope", function($scope) {
            
        	$scope.password = "qwer1234";
        	
            return $scope.init = function() {
                
            }, $scope.greet = function() {
                alert("clicked");
            }
        }])
    }.call(this),
    */
    function() {
        "use strict";
        angular.module("app.dashboardService", ["ngResource"]).factory("DashboardIdentityAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.DASHBOARD_URL.IDENTITY)
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.hypervisorService", []).factory("HypervisorAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.COMPUTE_URL.HYPERVISOR, {
                tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
                id: "@id"
            })
        }]).factory("HypervisorStatsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.COMPUTE_URL.HYPERVISOR_STATS, {
                tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
            })
        }]).factory("ComputeServiceInfoAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.COMPUTE_URL.COMPUTE_SERVICE_INFO)
        }]).factory("NetworkAgentInfoAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.COMPUTE_URL.NETWORK_AGENT_INFO)
        }]).factory("EndpointInfoAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.COMPUTE_URL.ENDPOINT_INFO)
        }])
        
        
    }.call(this),
    function() {
        "use strict";
        angular.module("app.identityService", ["ngResource"]).factory("IdentityOSKSADMServicesAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.IDENTITY_URL.OS_KSADM_SERVICES)
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.systemsService", []).factory("SystemSystemsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.SYSTEM_URL.SYSTEMS, {
                id: "@id"
            }, {
                update: {
                    method: "PUT"
                }
            })
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.task", []).factory("taskStorage", function() {
            var DEMO_TASKS, STORAGE_ID;
            return STORAGE_ID = "tasks", DEMO_TASKS = '[ {"title": "Finish homework", "completed": true}, {"title": "Make a call", "completed": true}, {"title": "Build a snowman :)", "completed": false}, {"title": "Apply for monster university!", "completed": false}, {"title": "Play games with friends", "completed": true}, {"title": "Shopping", "completed": false}, {"title": "One more dance", "completed": false}, {"title": "Try Google glass", "completed": false} ]', {
                get: function() {
                    return JSON.parse(localStorage.getItem(STORAGE_ID) || DEMO_TASKS)
                },
                put: function(tasks) {
                    return localStorage.setItem(STORAGE_ID, JSON.stringify(tasks))
                }
            }
        }).directive("taskFocus", ["$timeout", function($timeout) {
            return {
                link: function(scope, ele, attrs) {
                    return scope.$watch(attrs.taskFocus, function(newVal) {
                        return newVal ? $timeout(function() {
                            return ele[0].focus()
                        }, 0, !1) : void 0
                    })
                }
            }
        }]).controller("taskCtrl", ["$scope", "taskStorage", "filterFilter", "$rootScope", "logger", function($scope, taskStorage, filterFilter, $rootScope, logger) {
            var tasks;
            return tasks = $scope.tasks = taskStorage.get(), $scope.newTask = "", $scope.remainingCount = filterFilter(tasks, {
                completed: !1
            }).length, $scope.editedTask = null, $scope.statusFilter = {
                completed: !1
            }, $scope.filter = function(filter) {
                switch (filter) {
                    case "all":
                        return $scope.statusFilter = "";
                    case "active":
                        return $scope.statusFilter = {
                            completed: !1
                        };
                    case "completed":
                        return $scope.statusFilter = {
                            completed: !0
                        }
                }
            }, $scope.add = function() {
                var newTask;
                return newTask = $scope.newTask.trim(), 0 !== newTask.length ? (tasks.push({
                    title: newTask,
                    completed: !1
                }), logger.logSuccess('New task: "' + newTask + '" added'), taskStorage.put(tasks), $scope.newTask = "", $scope.remainingCount++) : void 0
            }, $scope.edit = function(task) {
                return $scope.editedTask = task
            }, $scope.doneEditing = function(task) {
                return $scope.editedTask = null, task.title = task.title.trim(), task.title ? logger.log("Task updated") : $scope.remove(task), taskStorage.put(tasks)
            }, $scope.remove = function(task) {
                var index;
                return $scope.remainingCount -= task.completed ? 0 : 1, index = $scope.tasks.indexOf(task), $scope.tasks.splice(index, 1), taskStorage.put(tasks), logger.logError("Task removed")
            }, $scope.completed = function(task) {
                return $scope.remainingCount += task.completed ? -1 : 1, taskStorage.put(tasks), task.completed ? $scope.remainingCount > 0 ? logger.log(1 === $scope.remainingCount ? "Almost there! Only " + $scope.remainingCount + " task left" : "Good job! Only " + $scope.remainingCount + " tasks left") : logger.logSuccess("Congrats! All done :)") : void 0
            }, $scope.clearCompleted = function() {
                return $scope.tasks = tasks = tasks.filter(function(val) {
                    return !val.completed
                }), taskStorage.put(tasks)
            }, $scope.markAll = function(completed) {
                return tasks.forEach(function(task) {
                    return task.completed = completed
                }), $scope.remainingCount = completed ? 0 : tasks.length, taskStorage.put(tasks), completed ? logger.logSuccess("Congrats! All done :)") : void 0
            }, $scope.$watch("remainingCount == 0", function(val) {
                return $scope.allChecked = val
            }), $scope.$watch("remainingCount", function(newVal) {
                return $rootScope.$broadcast("taskRemaining:changed", newVal)
            })
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.telemetrysService", []).factory("TelemetryInstancesAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
            return $resource(CONST_RESTFUL_API.TELEMETRY.INSTANCE, {
                instanceId: "@instanceId"
            })
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.ui.map", []).directive("uiJqvmap", [function() {
            return {
                restrict: "A",
                scope: {
                    options: "="
                },
                link: function(scope, ele) {
                    var options;
                    return options = scope.options, ele.vectorMap(options)
                }
            }
        }]).controller("jqvmapCtrl", ["$scope", function($scope) {
            var sample_data;
            return sample_data = {
                af: "16.63",
                al: "11.58",
                dz: "158.97",
                ao: "85.81",
                ag: "1.1",
                ar: "351.02",
                am: "8.83",
                au: "1219.72",
                at: "366.26",
                az: "52.17",
                bs: "7.54",
                bh: "21.73",
                bd: "105.4",
                bb: "3.96",
                by: "52.89",
                be: "461.33",
                bz: "1.43",
                bj: "6.49",
                bt: "1.4",
                bo: "19.18",
                ba: "16.2",
                bw: "12.5",
                br: "2023.53",
                bn: "11.96",
                bg: "44.84",
                bf: "8.67",
                bi: "1.47",
                kh: "11.36",
                cm: "21.88",
                ca: "1563.66",
                cv: "1.57",
                cf: "2.11",
                td: "7.59",
                cl: "199.18",
                cn: "5745.13",
                co: "283.11",
                km: "0.56",
                cd: "12.6",
                cg: "11.88",
                cr: "35.02",
                ci: "22.38",
                hr: "59.92",
                cy: "22.75",
                cz: "195.23",
                dk: "304.56",
                dj: "1.14",
                dm: "0.38",
                "do": "50.87",
                ec: "61.49",
                eg: "216.83",
                sv: "21.8",
                gq: "14.55",
                er: "2.25",
                ee: "19.22",
                et: "30.94",
                fj: "3.15",
                fi: "231.98",
                fr: "2555.44",
                ga: "12.56",
                gm: "1.04",
                ge: "11.23",
                de: "3305.9",
                gh: "18.06",
                gr: "305.01",
                gd: "0.65",
                gt: "40.77",
                gn: "4.34",
                gw: "0.83",
                gy: "2.2",
                ht: "6.5",
                hn: "15.34",
                hk: "226.49",
                hu: "132.28",
                is: "12.77",
                "in": "1430.02",
                id: "695.06",
                ir: "337.9",
                iq: "84.14",
                ie: "204.14",
                il: "201.25",
                it: "2036.69",
                jm: "13.74",
                jp: "5390.9",
                jo: "27.13",
                kz: "129.76",
                ke: "32.42",
                ki: "0.15",
                kr: "986.26",
                undefined: "5.73",
                kw: "117.32",
                kg: "4.44",
                la: "6.34",
                lv: "23.39",
                lb: "39.15",
                ls: "1.8",
                lr: "0.98",
                ly: "77.91",
                lt: "35.73",
                lu: "52.43",
                mk: "9.58",
                mg: "8.33",
                mw: "5.04",
                my: "218.95",
                mv: "1.43",
                ml: "9.08",
                mt: "7.8",
                mr: "3.49",
                mu: "9.43",
                mx: "1004.04",
                md: "5.36",
                mn: "5.81",
                me: "3.88",
                ma: "91.7",
                mz: "10.21",
                mm: "35.65",
                na: "11.45",
                np: "15.11",
                nl: "770.31",
                nz: "138",
                ni: "6.38",
                ne: "5.6",
                ng: "206.66",
                no: "413.51",
                om: "53.78",
                pk: "174.79",
                pa: "27.2",
                pg: "8.81",
                py: "17.17",
                pe: "153.55",
                ph: "189.06",
                pl: "438.88",
                pt: "223.7",
                qa: "126.52",
                ro: "158.39",
                ru: "1476.91",
                rw: "5.69",
                ws: "0.55",
                st: "0.19",
                sa: "434.44",
                sn: "12.66",
                rs: "38.92",
                sc: "0.92",
                sl: "1.9",
                sg: "217.38",
                sk: "86.26",
                si: "46.44",
                sb: "0.67",
                za: "354.41",
                es: "1374.78",
                lk: "48.24",
                kn: "0.56",
                lc: "1",
                vc: "0.58",
                sd: "65.93",
                sr: "3.3",
                sz: "3.17",
                se: "444.59",
                ch: "522.44",
                sy: "59.63",
                tw: "426.98",
                tj: "5.58",
                tz: "22.43",
                th: "312.61",
                tl: "0.62",
                tg: "3.07",
                to: "0.3",
                tt: "21.2",
                tn: "43.86",
                tr: "729.05",
                tm: 0,
                ug: "17.12",
                ua: "136.56",
                ae: "239.65",
                gb: "2258.57",
                us: "14624.18",
                uy: "40.71",
                uz: "37.72",
                vu: "0.72",
                ve: "285.21",
                vn: "101.99",
                ye: "30.02",
                zm: "15.69",
                zw: "5.57"
            }, $scope.worldMap = {
                map: "world_en",
                backgroundColor: null,
                color: "#ffffff",
                hoverOpacity: .7,
                selectedColor: "#666666",
                enableZoom: !0,
                showTooltip: !0,
                values: sample_data,
                scaleColors: ["#C8EEFF", "#006491"],
                normalizeFunction: "polynomial"
            }, $scope.USAMap = {
                map: "usa_en",
                backgroundColor: null,
                color: "#ffffff",
                hoverColor: "#999999",
                selectedColor: "#666666",
                enableZoom: !0,
                showTooltip: !0,
                selectedRegion: "MO"
            }, $scope.europeMap = {
                map: "europe_en",
                backgroundColor: null,
                color: "#ffffff",
                hoverOpacity: .7,
                hoverColor: "#999999",
                enableZoom: !1,
                showTooltip: !1,
                values: sample_data,
                scaleColors: ["#C8EEFF", "#006491"],
                normalizeFunction: "polynomial"
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.ui.directives", []).directive("uiTime", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    var checkTime, startTime;
                    return startTime = function() {
                        var h, m, s, t, time, today;
                        return today = new Date, h = today.getHours(), m = today.getMinutes(), s = today.getSeconds(), m = checkTime(m), s = checkTime(s), time = h + ":" + m + ":" + s, ele.html(time), t = setTimeout(startTime, 500)
                    }, checkTime = function(i) {
                        return 10 > i && (i = "0" + i), i
                    }, startTime()
                }
            }
        }]).directive("uiWeather", [function() {
            return {
                restrict: "A",
                link: function(scope, ele, attrs) {
                    var color, icon, skycons;
                    return color = attrs.color, icon = Skycons[attrs.icon], skycons = new Skycons({
                        color: color,
                        resizeClear: !0
                    }), skycons.add(ele[0], icon), skycons.play()
                }
            }
        }]).directive("uiNotCloseOnClick", [function() {
            return {
                restrict: "A",
                compile: function(ele) {
                    return ele.on("click", function(event) {
                        return event.stopPropagation()
                    })
                }
            }
        }]).directive("slimScroll", [function() {
            return {
                restrict: "A",
                link: function(scope, ele, attrs) {
                    return ele.slimScroll({
                        height: attrs.scrollHeight || "100%"
                    })
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.ui.services", []).factory("logger", [function() {
            var logIt;
            return toastr.options = {
                closeButton: !0,
                positionClass: "toast-bottom-right",
                timeOut: "3000"
            }, logIt = function(message, type) {
                return toastr[type](message)
            }, {
                log: function(message) {
                    logIt(message, "info")
                },
                logWarning: function(message) {
                    logIt(message, "warning")
                },
                logSuccess: function(message) {
                    logIt(message, "success")
                },
                logError: function(message) {
                    logIt(message, "error")
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app", ["ngCookies", "ngRoute", "ngAnimate", "ngMessages", "ui.bootstrap", "easypiechart", "mgo-angular-wizard", "textAngular", "ui.tree", "ngMap", "ngTagsInput", "app.controllers", "app.directives", "app.localization", "app.nav", "app.ui.directives", "app.ui.services", "app.task", "app.chart.directives", "app.filters", "app.interceptors", "app.alarms", "app.alarmsService", "app.dashboard",  "app.descriptors", "app.descriptorsNew", "app.hypervisorService", "app.identityService", "app.systems", "app.systemsService", "app.systemsChart", "app.telemetrysService", "app.instances", "app.instancesService", "app.instancesChart", "app.volumes", "app.volumesService", "app.networks", "app.networksService", "app.routers", "app.topology", "app.flavors", "app.repository", "app.images", "app.imagesService", "app.verification", "app.management", "frapontillo.bootstrap-switch", "app.scaling", "app.logins"]).constant("CONST_RESTFUL_API", {
            prefixURL: "/mano",
            SYSTEM_URL: {
                SYSTEMS: "/mano/system/systems/:id",
                ALARMS: "/mano/system/alarms/:id"
            },
            TELEMETRY: {
                INSTANCE: "/mano/telemetry/instances/:instanceId"
            },
            IDENTITY_URL: {
                TENANTS: "/mano/identity/tenants",
                OS_KSADM_SERVICES: "/mano/identity/OS-KSADM/services"
            },
            COMPUTE_URL: {
                TENANT_ID: "admin",
                SERVERGROUP : "/mano/compute/:tenantId/servergroup/:id", 
                SERVERS: "/mano/compute/:tenantId/servers/:id",
                SERVER_ACTIONS: "/mano/compute/:tenantId/servers/:id/action",
                FLAVORS: "/mano/compute/:tenantId/flavors/:id",
                FLAVORS_ACCESS: "/mano/compute/:tenantId/flavors/:id/os-flavor-access",
                FLAVORS_EXTRAS: "/mano/compute/:tenantId/flavors/:id/extras/:key",
                IMAGES: "/mano/compute/:tenantId/images/:id",
                LIMIT: "/mano/compute/limit",
                AVAILABILITY_ZONES: "/mano/compute/:tenantId/zones",
                HYPERVISOR: "/mano/compute/:tenantId/os-hypervisors",
                HYPERVISOR_STATS: "/mano/compute/:tenantId/os-hypervisors/statistics",
                FLOATING_IP_POOLS: "/mano/compute/:tenantId/os-floating-ip-pools/:poolName",
                ZONES: "/mano/compute/:tenantId/hostaggregate/:id",
            	ZONE_MANAGE_HOST: "/mano/compute/:tenantId/hostaggregate/:zoneId/host/:name",
        		SYSTEMS: "/mano/compute/:tenantId/hypervisors/:id",
    			SNAPSHOT: "/mano/compute/:title/snapshot/:name",
    			LOGIN: "/mano/compute/login",
				COMPUTE_SERVICE_INFO: "/mano/compute/serviceinfo",
				NETWORK_AGENT_INFO: "/mano/compute/networkagentinfo",
				ENDPOINT_INFO: "/mano/compute/endpointinfo"
            },
            VNFD_URL: {
                TENANT_ID: "admin",
                DESCRIPTOR_UPLOAD: "/mano/vnfd/descriptor/upload",
                DESCRIPTORS: "/mano/vnfd/descriptors/:id",
                REPOSITORYS: "/mano/vnfd/repositorys/:name",
                REPOSITORY_UPLOAD: "/mano/vnfd/repository/upload",
            	CONSOLE_VNC: "/mano/vnfd/console/:id",
            	HISTORYS: "/mano/vnfd/historys"
            },
            IMAGE_URL: {
                IMAGES: "/mano/image/images/:id",
                IMAGES_UPLOAD: "/mano/image/images",
                IMAGES_UPLOADS: "/mano/image/imageUploads"
            },
            BLOCKSTORAGE_URL: {
                VOLUME: "/mano/blockStorage/:tenantId/volumes/:id",
                VOLUME_EXTEND: "/mano/blockStorage/:tenantId/volumes/:id/extend",
                VOLUME_TYPE: "/mano/blockStorage/:tenantId/volume-types/:id",
                VOLUME_LIMITS: "/mano/blockStorage/:tenantId/limits",
                VOLUME_SNAPSHOT: "/mano/blockStorage/:tenantId/snapshots",
                VOLUME_ATTACHMENT: "/mano/compute/:tenantId/servers/:serverId/os-volume_attachments"
            },
            NETWORKING_URL: {
                NETWORK: "/mano/networking/networks/:id",
                ROUTER: "/mano/networking/routers/:id",
                ROUTER_GATEWAY: "/mano/networking/routers/:id/gateway",
                ROUTER_INTERFACE: "/mano/networking/routers/:id/interface",
                SUBNET: "/mano/networking/subnets/:id",
                PORT: "/mano/networking/ports/:id",
                FLOATING_IP: "/mano/networking/floatingip/:id",
                QUOTAS: "/mano/networking/:tenantId/quotas",
                NETWORK_TOPOLOGY: "/mano/networking/network_topology"
            },
            DESCRIPTOR_URL: {
            	DESCRIPTOR_TREEVIEW_META: "/mano/core/eluon/tmenu/json",
            	OPTION_TREEVIEW_META: "/mano/core/eluon/toption/json",
            	DESCRIPTOR_TREE_MAIN: "/mano/core/eluon/menu/main/xml",
            	DESCRIPTOR_TREE_SUB: "/mano/core/eluon/menu/sub/xml",
            	DESCRIPTOR_TREE_OPTION: "/mano/core/eluon/menu/option/xml",
            	VLD_ATVIEW_META: "/mano/core/eluon/NFV/VLD/atview/meta/json",
            	VM_ATVIEW_META: "/mano/core/eluon/NFV/VM/atview/meta/json",
            	DESCRIPTOR_XML: "/mano/core/eluon/NFV/:category/:id/xml",
            	DESCRIPTOR: "/mano/core/eluon/:category/:id",
            	DESCRIPTOR_OPTIONS: "/mano/core/eluon/:id/all/:md"
            },
            VERIFICATION_URL: {
            	MANAGEMENT_POLICY_LIST: "/mano/core/eluon/managementPolicyList",
            	VERIFICATION_HOST_LIST: "/mano/verisdnfv/host/all",
            	VERIFICATION_REQUEST: "/mano/verisdnfv/:PrePost/:Menu/:DescriptorID/:Property/:PolicyID/:HostID",
            	VERIFICATION_COMPUTE_LIST: "/mano/verisdnfv/compute/all"
            },
            LIVEMIGRATION_URL: {
            	INSTANCE_LIST: "/mano/livemigration/list/:zoneName",
            	LIVEMIGRATION_REQUEST: "/mano/livemigration/setmigration/:instanceId/:hyperId/:blockmigration"
            },
            SCALE_URL: {
            	SCALE_GROUP_LIST: "/mano/scale/admin/server/groups",
            	SCALE_IMAGE_LIST:"/mano/image/images",
            	SCALE_FLAVOR_LIST:"/mano/compute/admin/flavors",
            	SCALE_NETWORK_LIST:"/mano/networking/networks",
            	SCALE_KEYPAIR_LIST:"/mano/compute/admin/os-keypairs",
            	SCALE_UPDATE:"/mano/scale/admin/server/groups"
            }         
        }).config(["$routeProvider", function($routeProvider) {

            String.prototype.replaceAll = function (oldValue, newValue) {

                var retValue = this;

                while (retValue.indexOf(oldValue) >= 0) {
                    retValue = retValue.replace(oldValue, newValue);
                }

                return retValue;
            };
            
            var routes, setRoutes;
//            return routes = ["dashboard/dashboard", "systems/index", "instances/index", "networks/networks/index", "networks/router/index", 
//                             "networks/topology/index", "volumes/index", "flavors/index", "images/index", "repository/index", 
//                             "veryfy/index", "descriptor/index", "verification/index", "management/index", "scaling/index","descriptorNew/index"], setRoutes = function(route) {
            return routes = ["dashboard/dashboard", "systems/index", "instances/index", "networks/networks/index", "networks/router/index", 
                             "networks/topology/index", "volumes/index", "flavors/index", "images/index", "repository/index", 
                             "veryfy/index", "descriptorNew/index", "management/index","pages/signin","pages/signup"], setRoutes = function(route) {	
                var config, url;
                return url = "/" + route, config = {
                    templateUrl: "views/" + route + ".html"
                }, $routeProvider.when(url, config), $routeProvider
            }, routes.forEach(function(route) {
                return setRoutes(route)
            }), $routeProvider.when("/", {
                redirectTo: "/dashboard/dashboard"
            }).when("/404", {
                templateUrl: "views/pages/404.html"
            }).otherwise({
                redirectTo: "/404"
            })
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.nav", []).directive("toggleNavCollapsedMin", ["$rootScope", function($rootScope) {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    var app;
                    return app = $("#app"), ele.on("click", function(e) {
                        return app.hasClass("nav-collapsed-min") ? app.removeClass("nav-collapsed-min") : (app.addClass("nav-collapsed-min"), $rootScope.$broadcast("nav:reset")), e.preventDefault()
                    })
                }
            }
        }]).directive("collapseNav", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    var $a, $aRest, $app, $lists, $listsRest, $nav, $window, Timer, prevWidth, updateClass;
                    return $window = $(window), $lists = ele.find("ul").parent("li"), $lists.append('<i class="fa fa-caret-down icon-has-ul-h"></i><i class="fa fa-caret-right icon-has-ul"></i>'), $a = $lists.children("a"), $listsRest = ele.children("li").not($lists), $aRest = $listsRest.children("a"), $app = $("#app"), $nav = $("#nav-container"), $a.on("click", function(event) {
                        var $parent, $this;
                        return $app.hasClass("nav-collapsed-min") || $nav.hasClass("nav-horizontal") && $window.width() >= 768 ? !1 : ($this = $(this), $parent = $this.parent("li"), $lists.not($parent).removeClass("open").find("ul").slideUp(), $parent.toggleClass("open").find("ul").slideToggle(), event.preventDefault())
                    }), $aRest.on("click", function() {
                        return $lists.removeClass("open").find("ul").slideUp()
                    }), scope.$on("nav:reset", function() {
                        return $lists.removeClass("open").find("ul").slideUp()
                    }), Timer = void 0, prevWidth = $window.width(), updateClass = function() {
                        var currentWidth;
                        return currentWidth = $window.width(), 768 > currentWidth && $app.removeClass("nav-collapsed-min"), 768 > prevWidth && currentWidth >= 768 && $nav.hasClass("nav-horizontal") && $lists.removeClass("open").find("ul").slideUp(), prevWidth = currentWidth
                    }, $window.resize(function() {
                        var t;
                        return clearTimeout(t), t = setTimeout(updateClass, 300)
                    })
                }
            }
        }]).directive("highlightActive", [function() {
            return {
                restrict: "A",
                controller: ["$scope", "$element", "$attrs", "$location", function($scope, $element, $attrs, $location) {
                    var highlightActive, links, path;
                    return links = $element.find("a"), path = function() {
                        return $location.path()
                    }, highlightActive = function(links, path) {
                        return path = "#" + path, angular.forEach(links, function(link) {
                            var $li, $link, href;
                            return $link = angular.element(link), $li = $link.parent("li"), href = $link.attr("href"), $li.hasClass("active") && $li.removeClass("active"), 0 === path.indexOf(href) ? $li.addClass("active") : void 0
                        })
                    }, highlightActive(links, $location.path()), $scope.$watch(path, function(newVal, oldVal) {
                        return newVal !== oldVal ? highlightActive(links, $location.path()) : void 0
                    })
                }]
            }
        }]).directive("toggleOffCanvas", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    return ele.on("click", function() {
                        return $("#app").toggleClass("on-canvas")
                    })
                }
            }
        }]).directive("nfvTreeNav", ["$rootScope", function($rootScope) {
            return {
                restrict: "A",
                templateUrl: 'views/treenav.html',
                replace: false,
                scope: {
                    ngModel: "=ngModel"
                },
                controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
                	
                	if ($scope.ngModel != undefined && $scope.ngModel.constructor == Array) {
                		for(var x = 0; x < $scope.ngModel.length; x++) {
                			if ($scope.ngModel[x].children.child != undefined && $scope.ngModel[x].children.child.constructor == Array) {
                        		for(var i = 0; i < $scope.ngModel[x].children.child.length; i++) {
                        			
                        			if ($scope.ngModel[x].children.child[i].children != undefined &&
                        					$scope.ngModel[x].children.child[i].children.child != undefined &&
                        					$scope.ngModel[x].children.child[i].children.child.constructor != Array &&
                                			$scope.ngModel[x].children.child[i].children.child.key == '' &&
                                			$scope.ngModel[x].children.child[i].children.child.title == '') {
                        				
                                		// tmenu 를 신규 추가한 경우 빈 값을 가진 자식을 더미로 가져오는데 이를 표시할 필요는 없다. 따라서 노드 삭제  요청자 김재중, 작업자 김재훈 2015-11-11
                                    	$scope.ngModel[x].children.child[i].children.child = undefined;
                                    	
                                	} else if ($scope.ngModel[x].children.child[i].children != undefined &&
                                			$scope.ngModel[x].children.child[i].children.child != undefined &&
                                			$scope.ngModel[x].children.child[i].children.child.constructor != Array) {
//                                		console.log('nfvTreeNav.children.child.constructor == Array', $scope.ngModel[x].children.child[i].children.child.constructor == Array);
                                    	$scope.ngModel[x].children.child[i].children.child = [$scope.ngModel[x].children.child[i].children.child];
                                	}
                        		}
                        	} else if ($scope.ngModel[x].children.child != undefined && $scope.ngModel[x].children.child.constructor != Array &&
                        			$scope.ngModel[x].children.child.key == '' && $scope.ngModel[x].children.child.title == '') {
                        		
                        		// tmenu 를 신규 추가한 경우 빈 값을 가진 자식을 더미로 가져오는데 이를 표시할 필요는 없다. 따라서 노드 삭제  요청자 김재중, 작업자 김재훈 2015-11-11
                            	$scope.ngModel[x].children.child = undefined;
                            	
                        	} else if ($scope.ngModel[x].children.child != undefined && $scope.ngModel[x].children.child.constructor != Array) {

                            	$scope.ngModel[x].children.child = [$scope.ngModel[x].children.child];
                        	}
                		}
                	}
                	$scope.newSubItem = function (item) {
//                		console.log('newChildNode: ', item);
						$scope.$emit('addNewSubitem', {"category": item.title.toLowerCase(), "id": ""});
                		
                	};
                }]
            }
        }]).directive("nfvTreeNodeContent", ["$rootScope", function($rootScope) {
            return {
                restrict: "A",
                replace: true,
                template: '<a class="node-type-icon" ng-class="{\'is-leaf-node-type-icon\': !ngModel.isSmOption&&ngModel.children == undefined, ' +                
        		'\'is-smoption-node-type-icon\': ngModel.isSmOption&&ngModel.children == undefined,'+
                
    			'\'left-indent\': ngModel.children != undefined && ngModel.children.child == undefined} ">' +
            	'<i class="fa " ng-class="{\'fa-sitemap\': ngModel.children && !(ngModel.isOption), \'fa-cog\': (ngModel.children && ngModel.isOption)||(ngModel.isSmOption&&ngModel.isOption), ' +
            	
            	
            	'\'fa-minus\': !ngModel.isSmOption&&ngModel.children == undefined } "></i>' +	                	
            
            	//옵션 체크용.. 주석해 놓을것 jykim
            	//'<span>isOption : {{ngModel.isOption}}, isSmOption : {{ngModel.isSmOption}}, isChilderen : {{ngModel.children}}</span>'+
            		                	
            	'<span class="label" ng-class="{\'node-category\': ngModel.children, ' +
            	'\'label-smoption\':ngModel.children == undefined,'+
            	
            	'\'label-success\': ngModel.isSmOption==undefined && ngModel.children,'+
            	
            	'\'is-smoption-node\': ngModel.isSmOption && ngModel.children == undefined,'+
            	
            	'\'is-leaf-node\': !ngModel.isSmOption && ngModel.children == undefined } ">{{ngModel.show == undefined ? ngModel.title : ngModel.show}}</span>' +	                	
            	
            	'<span  ng-class="{\'node-category2\':ngModel.title}"     ng-show="!ngModel.title">{{ngModel.title}}</span>'+
            	
            	//'<input type="hidden" ng-class="{\'node-category3\':ngModel.title}" value={{ngModel.title}}>'+
            	
            	' <span ng-if="ngModel.children" class="badge">{{ngModel.children.child.length}}</span>'+
        	'</a>',
                scope: {
                    ngModel: "=ngModel"
                },
                controller: ['$scope', '$location', '$timeout', function($scope, $location, $timeout) {
//                	console.log('nfvTreeNodeContent controller', $scope.ngModel);
                	
                }],
                link: function(scope, element, attrs) {
//                	console.log('nfvTreeNodeContent link');
                	element.on('click', function(event) {
                	        // Prevent default dragging of selected content
                		event.preventDefault();
						element.addClass('highlight').parents('[data-nfv-tree-nav]').find('.highlight').not(element).removeClass('highlight');
						
						if (scope.ngModel.type == 'descriptor' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'flavor' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'repository' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'repositorys' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'hostAggregates' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'hypervisor' ) {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else if (scope.ngModel.type == 'vnf' || scope.ngModel.type == 'vnfc') {
							scope.$emit('selectNodeDescriptor', scope.ngModel);
						}else  if (scope.ngModel.children == undefined ) {
							var category = element.parents('li').parents('[ui-tree-node]').eq(0).find('.node-category2').eq(0).text();
							var id = scope.ngModel.title;
							scope.$emit('selectNodeDescriptor', {"category": category.toLowerCase(), "id": id});
						} else {
							var category = angular.element(element).find('.node-category2').text();
							scope.$emit('selectNodeDescriptor', {"category": category.toLowerCase(), "id": ''});
						}
                	 });
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.directives", []).directive("imgHolder", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    return Holder.run({
                        images: ele[0]
                    })
                }
            }
        }]).directive("customPage", function() {
            return {
                restrict: "A",
                controller: ["$scope", "$element", "$location", function($scope, $element, $location) {
                    var addBg, path;
                    return path = function() {
                        return $location.path()
                    }, addBg = function(path) {
                        switch ($element.removeClass("body-wide body-lock"), path) {
                            case "/404":
                            case "/pages/404":
                            case "/pages/500":
                            case "/pages/signin":
                            case "/pages/signup":
                            case "/pages/forgot-password":
                                return $element.addClass("body-wide");
                            case "/pages/lock-screen":
                                return $element.addClass("body-wide body-lock")
                        }
                    }, addBg($location.path()), $scope.$watch(path, function(newVal, oldVal) {
                        return newVal !== oldVal ? addBg($location.path()) : void 0
                    })
                }]
            }
        }).directive("uiColorSwitch", [function() {
            return {
                restrict: "A",
                link: function(scope, ele) {
                    return ele.find(".color-option").on("click", function(event) {
                        var $this, hrefUrl, style;
                        if ($this = $(this), hrefUrl = void 0, style = $this.data("style"), "loulou" === style) hrefUrl = "styles/main.css", $('link[href^="styles/main"]').attr("href", hrefUrl);
                        else {
                            if (!style) return !1;
                            style = "-" + style, hrefUrl = "styles/main" + style + ".css", $('link[href^="styles/main"]').attr("href", hrefUrl)
                        }
                        return event.preventDefault()
                    })
                }
            }
        }]).directive("goBack", [function() {
            return {
                restrict: "A",
                controller: ["$scope", "$element", "$window", function($scope, $element, $window) {
                    return $element.on("click", function() {
                        return $window.history.back()
                    })
                }]
            }
        }]).directive("resizefit", ["$window", function($window) {
            return function(scope, elem, attrs) {
                var CORRECTION_VALUE, correctionValue, resize, win;
                return win = angular.element($window), CORRECTION_VALUE = 458, correctionValue = CORRECTION_VALUE, attrs.adjustValue && (correctionValue = attrs.adjustValue), scope.getWindowDemensions = function() {
                    return {
                        h: win.height(),
                        w: win.width(),
                        fit: win.height() - correctionValue
                    }
                }, resize = function(fit) {
                    return scope.style = function() {
                        return {
                            "min-height": fit + "px",
                            "max-height": fit + "px"
                        }
                    }
                }, resize(win.height() - correctionValue), scope.$watch(scope.getWindowDemensions, function(newValue, oldValue) {
                    newValue !== oldValue && (scope.windowHeight = newValue.h, scope.windowWidth = newValue.w, resize(newValue.fit))
                }, !0), win.bind("resize", function() {
                    scope.$apply()
                })
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.filters", []).factory("fileSizeCalculrate", ["$rootScope", function() {
            var fileSizeCalculrator;
            return fileSizeCalculrator = {
                bytes: function(bytes, precision) {
                    var number, units;
                    return isNaN(parseFloat(bytes)) || !isFinite(bytes), "undefined" == typeof precision && (precision = 1), units = ["bytes", "kB", "MB", "GB", "TB", "PB"], number = Math.floor(Math.log(bytes) / Math.log(1024)), (bytes / Math.pow(1024, Math.floor(number))).toFixed(precision) + " " + units[number]
                }
            }
        }]).filter("bytes", ["fileSizeCalculrate", function(fileSizeCalculrate) {
            return function(bytes, type, precision) {
                return fileSizeCalculrate.bytes(bytes, precision)
            }
        }]).filter("reverse", [function() {
            return function(items) {
                return items.slice().reverse()
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.interceptors", []).factory("DefaultErrorInterceptor", ["$injector", "logger", function($injector, logger) {
            return function(promise) {
                return promise.then(null, function(error) {
                    return logger.logError("에러가 발생하였습니다.<br/>status: " + error.status + "<br/>statusText: " + error.statusText), promise
                })
            }
        }]).config(["$httpProvider", function($httpProvider) {
            return $httpProvider.responseInterceptors.push("DefaultErrorInterceptor")
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.localization", []).factory("localize", ["$http", "$rootScope", "$window", function($http, $rootScope, $window) {
            var localize;
            return localize = {
                language: "",
                url: void 0,
                resourceFileLoaded: !1,
                successCallback: function(data) {
                    return localize.dictionary = data, localize.resourceFileLoaded = !0, $rootScope.$broadcast("localizeResourcesUpdated")
                },
                setLanguage: function(value) {
                    return localize.language = value.toLowerCase().split("-")[0], localize.initLocalizedResources()
                },
                setUrl: function(value) {
                    return localize.url = value, localize.initLocalizedResources()
                },
                buildUrl: function() {
                    return localize.language || (localize.language = ($window.navigator.userLanguage || $window.navigator.language).toLowerCase(), localize.language = localize.language.split("-")[0]), "i18n/resources-locale_" + localize.language + ".js"
                },
                initLocalizedResources: function() {
                    var url;
                    return url = localize.url || localize.buildUrl(), $http({
                        method: "GET",
                        url: url,
                        cache: !1
                    }).success(localize.successCallback).error(function() {
                        return $rootScope.$broadcast("localizeResourcesUpdated")
                    })
                },
                getLocalizedString: function(value) {
                    var result, valueLowerCase;
                    return result = void 0, localize.dictionary && value ? (valueLowerCase = value.toLowerCase(), result = "" === localize.dictionary[valueLowerCase] ? value : localize.dictionary[valueLowerCase]) : result = value, result
                }
            }
        }]).directive("i18n", ["localize", function(localize) {
            var i18nDirective;
            return i18nDirective = {
                restrict: "EA",
                updateText: function(ele, input, placeholder) {
                    var result;
                    return result = void 0, "i18n-placeholder" === input ? (result = localize.getLocalizedString(placeholder), ele.attr("placeholder", result)) : input.length >= 1 ? (result = localize.getLocalizedString(input), ele.text(result)) : void 0
                },
                link: function(scope, ele, attrs) {
                    return scope.$on("localizeResourcesUpdated", function() {
                        return i18nDirective.updateText(ele, attrs.i18n, attrs.placeholder)
                    }), attrs.$observe("i18n", function(value) {
                        return i18nDirective.updateText(ele, value, attrs.placeholder)
                    })
                }
            }
        }]).controller("LangCtrl", ["$scope", "localize", function($scope, localize) {
            return $scope.lang = "한국어", $scope.setLang = function(lang) {
                switch (lang) {
                    case "English":
                        localize.setLanguage("EN-US");
                        break;
                    case "Espanol":
                        localize.setLanguage("ES-ES");
                        break;
                    case "日本語":
                        localize.setLanguage("JA-JP");
                        break;
                    case "中文":
                        localize.setLanguage("ZH-TW");
                        break;
                    case "Deutsch":
                        localize.setLanguage("DE-DE");
                        break;
                    case "francais":
                        localize.setLanguage("FR-FR");
                        break;
                    case "Italiano":
                        localize.setLanguage("IT-IT");
                        break;
                    case "Portugal":
                        localize.setLanguage("PT-BR");
                        break;
                    case "Русский язык":
                        localize.setLanguage("RU-RU");
                        break;
                    case "한국어":
                        localize.setLanguage("KO-KR")
                }
                return $scope.lang = lang
            }, $scope.getFlag = function() {
                var lang;
                switch (lang = $scope.lang) {
                    case "English":
                        return "flags-american";
                    case "Espanol":
                        return "flags-spain";
                    case "日本語":
                        return "flags-japan";
                    case "中文":
                        return "flags-china";
                    case "Deutsch":
                        return "flags-germany";
                    case "francais":
                        return "flags-france";
                    case "Italiano":
                        return "flags-italy";
                    case "Portugal":
                        return "flags-portugal";
                    case "Русский язык":
                        return "flags-russia";
                    case "한국어":
                        return "flags-korea"
                }
            }
        }])
    }.call(this),
    function() {
        "use strict";
        angular.module("app.controllers", []).controller("AppCtrl", ["$scope", "$rootScope", "$location", "$cookies", function($scope, $rootScope, $location, $cookies) {
            var $window;
            
            return $window = $(window), $scope.main = {
                brand: "Mano",
                name: "Admin"
            }, $scope.admin = {
                layout: "wide",
                menu: "horizontal",
                fixedHeader: !0,
                fixedSidebar: !0
            }, $scope.$watch("admin", function(newVal, oldVal) {
                return "horizontal" === newVal.menu && "vertical" === oldVal.menu ? void $rootScope.$broadcast("nav:reset") : newVal.fixedHeader === !1 && newVal.fixedSidebar === !0 ? (oldVal.fixedHeader === !1 && oldVal.fixedSidebar === !1 && ($scope.admin.fixedHeader = !0, $scope.admin.fixedSidebar = !0), void(oldVal.fixedHeader === !0 && oldVal.fixedSidebar === !0 && ($scope.admin.fixedHeader = !1, $scope.admin.fixedSidebar = !1))) : (newVal.fixedSidebar === !0 && ($scope.admin.fixedHeader = !0), void(newVal.fixedHeader === !1 && ($scope.admin.fixedSidebar = !1)))
            }, !0), $scope.color = {
                primary: "#1BB7A0",
                success: "#94B758",
                info: "#56BDF1",
                infoAlt: "#7F6EC7",
                warning: "#F3C536",
                danger: "#FA7B58",
                instances: "#16b0bc",
                vcpus: "#6bb71b",
                ram: "#df8e2b"
            }, $scope.isRouteChange = !0, $rootScope.$on("$routeChangeStart", function(event,next,current) {
        		if($cookies.login == null || $cookies.login != "auth"){
            		if(next.templateUrl == 'views/pages/signin.html'){
            		}else {
            			$location.path("/pages/signin");
            			return false;
            		}
            	}
        		if(next.templateUrl== 'views/pages/signin.html'){
        			$cookies.login = null;
        			$cookies.user = null;
            	}
                return $scope.isRouteChange = !0
            }), $rootScope.$on("$routeChangeSuccess", function() {
            	$scope.show = $cookies.login;
            	console.log($scope.show);
                return $scope.isRouteChange = !0
            }), $rootScope.$on("$routeChangeError", function() {
            	console.log('3');
                return $scope.isRouteChange = !0
            })
        }]).controller("HeaderCtrl", ["$scope", function($scope) {
            return $scope.introOptions = {
                steps: [{
                    element: "#step1",
                    intro: "<strong>Heads up!</strong> You can change the layout here",
                    position: "bottom"
                }, {
                    element: "#step2",
                    intro: "Select a different language",
                    position: "right"
                }, {
                    element: "#step3",
                    intro: "Runnable task App",
                    position: "left"
                }, {
                    element: "#step4",
                    intro: "Collapsed nav for both horizontal nav and vertical nav",
                    position: "right"
                }]
            }
        }]).controller("NavContainerCtrl", ["$scope", function() {}]).controller("NavCtrl", ["$scope", "taskStorage", "filterFilter", function($scope, taskStorage, filterFilter) {
            var tasks;
            return tasks = $scope.tasks = taskStorage.get(), $scope.taskRemainingCount = filterFilter(tasks, {
                completed: !1
            }).length, $scope.$on("taskRemaining:changed", function(event, count) {
                return $scope.taskRemainingCount = count
            })
        }])
    }.call(this), d3 = function() {
        function d3_number(x) {
            return null != x && !isNaN(x)
        }

        function d3_zipLength(d) {
            return d.length
        }

        function d3_range_integerScale(x) {
            for (var k = 1; x * k % 1;) k *= 10;
            return k
        }

        function d3_class(ctor, properties) {
            try {
                for (var key in properties) Object.defineProperty(ctor.prototype, key, {
                    value: properties[key],
                    enumerable: !1
                })
            } catch (e) {
                ctor.prototype = properties
            }
        }

        function d3_Map() {}

        function d3_Set() {}

        function d3_rebind(target, source, method) {
            return function() {
                var value = method.apply(source, arguments);
                return value === source ? target : value
            }
        }

        function d3_dispatch() {}

        function d3_dispatch_event(dispatch) {
            function event() {
                for (var l, z = listeners, i = -1, n = z.length; ++i < n;)(l = z[i].on) && l.apply(this, arguments);
                return dispatch
            }
            var listeners = [],
                listenerByName = new d3_Map;
            return event.on = function(name, listener) {
                var i, l = listenerByName.get(name);
                return arguments.length < 2 ? l && l.on : (l && (l.on = null, listeners = listeners.slice(0, i = listeners.indexOf(l)).concat(listeners.slice(i + 1)), listenerByName.remove(name)), listener && listeners.push(listenerByName.set(name, {
                    on: listener
                })), dispatch)
            }, event
        }

        function d3_eventCancel() {
            d3.event.stopPropagation(), d3.event.preventDefault()
        }

        function d3_eventSource() {
            for (var s, e = d3.event; s = e.sourceEvent;) e = s;
            return e
        }

        function d3_eventSuppress(target, type) {
            function off() {
                target.on(type, null)
            }
            target.on(type, function() {
                d3_eventCancel(), off()
            }, !0), setTimeout(off, 0)
        }

        function d3_eventDispatch(target) {
            for (var dispatch = new d3_dispatch, i = 0, n = arguments.length; ++i < n;) dispatch[arguments[i]] = d3_dispatch_event(dispatch);
            return dispatch.of = function(thiz, argumentz) {
                return function(e1) {
                    try {
                        var e0 = e1.sourceEvent = d3.event;
                        e1.target = target, d3.event = e1, dispatch[e1.type].apply(thiz, argumentz)
                    } finally {
                        d3.event = e0
                    }
                }
            }, dispatch
        }

        function d3_mousePoint(container, e) {
            var svg = container.ownerSVGElement || container;
            if (svg.createSVGPoint) {
                var point = svg.createSVGPoint();
                if (0 > d3_mouse_bug44083 && (d3_window.scrollX || d3_window.scrollY)) {
                    svg = d3.select(d3_document.body).append("svg").style("position", "absolute").style("top", 0).style("left", 0);
                    var ctm = svg[0][0].getScreenCTM();
                    d3_mouse_bug44083 = !(ctm.f || ctm.e), svg.remove()
                }
                return d3_mouse_bug44083 ? (point.x = e.pageX, point.y = e.pageY) : (point.x = e.clientX, point.y = e.clientY), point = point.matrixTransform(container.getScreenCTM().inverse()), [point.x, point.y]
            }
            var rect = container.getBoundingClientRect();
            return [e.clientX - rect.left - container.clientLeft, e.clientY - rect.top - container.clientTop]
        }

        function d3_arrayCopy(pseudoarray) {
            for (var i = -1, n = pseudoarray.length, array = []; ++i < n;) array.push(pseudoarray[i]);
            return array
        }

        function d3_arraySlice(pseudoarray) {
            return Array.prototype.slice.call(pseudoarray)
        }

        function d3_selection(groups) {
            return d3_arraySubclass(groups, d3_selectionPrototype), groups
        }

        function d3_selection_selector(selector) {
            return function() {
                return d3_select(selector, this)
            }
        }

        function d3_selection_selectorAll(selector) {
            return function() {
                return d3_selectAll(selector, this)
            }
        }

        function d3_selection_attr(name, value) {
            function attrNull() {
                this.removeAttribute(name)
            }

            function attrNullNS() {
                this.removeAttributeNS(name.space, name.local)
            }

            function attrConstant() {
                this.setAttribute(name, value)
            }

            function attrConstantNS() {
                this.setAttributeNS(name.space, name.local, value)
            }

            function attrFunction() {
                var x = value.apply(this, arguments);
                null == x ? this.removeAttribute(name) : this.setAttribute(name, x)
            }

            function attrFunctionNS() {
                var x = value.apply(this, arguments);
                null == x ? this.removeAttributeNS(name.space, name.local) : this.setAttributeNS(name.space, name.local, x)
            }
            return name = d3.ns.qualify(name), null == value ? name.local ? attrNullNS : attrNull : "function" == typeof value ? name.local ? attrFunctionNS : attrFunction : name.local ? attrConstantNS : attrConstant
        }

        function d3_collapse(s) {
            return s.trim().replace(/\s+/g, " ")
        }

        function d3_selection_classedRe(name) {
            return new RegExp("(?:^|\\s+)" + d3.requote(name) + "(?:\\s+|$)", "g")
        }

        function d3_selection_classed(name, value) {
            function classedConstant() {
                for (var i = -1; ++i < n;) name[i](this, value)
            }

            function classedFunction() {
                for (var i = -1, x = value.apply(this, arguments); ++i < n;) name[i](this, x)
            }
            name = name.trim().split(/\s+/).map(d3_selection_classedName);
            var n = name.length;
            return "function" == typeof value ? classedFunction : classedConstant
        }

        function d3_selection_classedName(name) {
            var re = d3_selection_classedRe(name);
            return function(node, value) {
                if (c = node.classList) return value ? c.add(name) : c.remove(name);
                var c = node.getAttribute("class") || "";
                value ? (re.lastIndex = 0, re.test(c) || node.setAttribute("class", d3_collapse(c + " " + name))) : node.setAttribute("class", d3_collapse(c.replace(re, " ")))
            }
        }

        function d3_selection_style(name, value, priority) {
            function styleNull() {
                this.style.removeProperty(name)
            }

            function styleConstant() {
                this.style.setProperty(name, value, priority)
            }

            function styleFunction() {
                var x = value.apply(this, arguments);
                null == x ? this.style.removeProperty(name) : this.style.setProperty(name, x, priority)
            }
            return null == value ? styleNull : "function" == typeof value ? styleFunction : styleConstant
        }

        function d3_selection_property(name, value) {
            function propertyNull() {
                delete this[name]
            }

            function propertyConstant() {
                this[name] = value
            }

            function propertyFunction() {
                var x = value.apply(this, arguments);
                null == x ? delete this[name] : this[name] = x
            }
            return null == value ? propertyNull : "function" == typeof value ? propertyFunction : propertyConstant
        }

        function d3_selection_dataNode(data) {
            return {
                __data__: data
            }
        }

        function d3_selection_filter(selector) {
            return function() {
                return d3_selectMatches(this, selector)
            }
        }

        function d3_selection_sortComparator(comparator) {
            return arguments.length || (comparator = d3.ascending),
                function(a, b) {
                    return !a - !b || comparator(a.__data__, b.__data__)
                }
        }

        function d3_noop() {}

        function d3_selection_on(type, listener, capture) {
            function onRemove() {
                var l = this[name];
                l && (this.removeEventListener(type, l, l.$),
                    delete this[name])
            }

            function onAdd() {
                var l = wrap(listener, d3_array(arguments));
                onRemove.call(this), this.addEventListener(type, this[name] = l, l.$ = capture), l._ = listener
            }

            function removeAll() {
                var match, re = new RegExp("^__on([^.]+)" + d3.requote(type) + "$");
                for (var name in this)
                    if (match = name.match(re)) {
                        var l = this[name];
                        this.removeEventListener(match[1], l, l.$), delete this[name]
                    }
            }
            var name = "__on" + type,
                i = type.indexOf("."),
                wrap = d3_selection_onListener;
            i > 0 && (type = type.substring(0, i));
            var filter = d3_selection_onFilters.get(type);
            return filter && (type = filter, wrap = d3_selection_onFilter), i ? listener ? onAdd : onRemove : listener ? d3_noop : removeAll
        }

        function d3_selection_onListener(listener, argumentz) {
            return function(e) {
                var o = d3.event;
                d3.event = e, argumentz[0] = this.__data__;
                try {
                    listener.apply(this, argumentz)
                } finally {
                    d3.event = o
                }
            }
        }

        function d3_selection_onFilter(listener, argumentz) {
            var l = d3_selection_onListener(listener, argumentz);
            return function(e) {
                var target = this,
                    related = e.relatedTarget;
                related && (related === target || 8 & related.compareDocumentPosition(target)) || l.call(target, e)
            }
        }

        function d3_selection_each(groups, callback) {
            for (var j = 0, m = groups.length; m > j; j++)
                for (var node, group = groups[j], i = 0, n = group.length; n > i; i++)(node = group[i]) && callback(node, i, j);
            return groups
        }

        function d3_selection_enter(selection) {
            return d3_arraySubclass(selection, d3_selection_enterPrototype), selection
        }

        function d3_Color() {}

        function d3_hsl(h, s, l) {
            return new d3_Hsl(h, s, l)
        }

        function d3_Hsl(h, s, l) {
            this.h = h, this.s = s, this.l = l
        }

        function d3_hsl_rgb(h, s, l) {
            function v(h) {
                return h > 360 ? h -= 360 : 0 > h && (h += 360), 60 > h ? m1 + (m2 - m1) * h / 60 : 180 > h ? m2 : 240 > h ? m1 + (m2 - m1) * (240 - h) / 60 : m1
            }

            function vv(h) {
                return Math.round(255 * v(h))
            }
            var m1, m2;
            return h = isNaN(h) ? 0 : (h %= 360) < 0 ? h + 360 : h, s = isNaN(s) ? 0 : 0 > s ? 0 : s > 1 ? 1 : s, l = 0 > l ? 0 : l > 1 ? 1 : l, m2 = .5 >= l ? l * (1 + s) : l + s - l * s, m1 = 2 * l - m2, d3_rgb(vv(h + 120), vv(h), vv(h - 120))
        }

        function d3_sgn(x) {
            return x > 0 ? 1 : 0 > x ? -1 : 0
        }

        function d3_acos(x) {
            return Math.acos(Math.max(-1, Math.min(1, x)))
        }

        function d3_asin(x) {
            return x > 1 ? π / 2 : -1 > x ? -π / 2 : Math.asin(x)
        }

        function d3_sinh(x) {
            return (Math.exp(x) - Math.exp(-x)) / 2
        }

        function d3_cosh(x) {
            return (Math.exp(x) + Math.exp(-x)) / 2
        }

        function d3_haversin(x) {
            return (x = Math.sin(x / 2)) * x
        }

        function d3_hcl(h, c, l) {
            return new d3_Hcl(h, c, l)
        }

        function d3_Hcl(h, c, l) {
            this.h = h, this.c = c, this.l = l
        }

        function d3_hcl_lab(h, c, l) {
            return isNaN(h) && (h = 0), isNaN(c) && (c = 0), d3_lab(l, Math.cos(h *= d3_radians) * c, Math.sin(h) * c)
        }

        function d3_lab(l, a, b) {
            return new d3_Lab(l, a, b)
        }

        function d3_Lab(l, a, b) {
            this.l = l, this.a = a, this.b = b
        }

        function d3_lab_rgb(l, a, b) {
            var y = (l + 16) / 116,
                x = y + a / 500,
                z = y - b / 200;
            return x = d3_lab_xyz(x) * d3_lab_X, y = d3_lab_xyz(y) * d3_lab_Y, z = d3_lab_xyz(z) * d3_lab_Z, d3_rgb(d3_xyz_rgb(3.2404542 * x - 1.5371385 * y - .4985314 * z), d3_xyz_rgb(-.969266 * x + 1.8760108 * y + .041556 * z), d3_xyz_rgb(.0556434 * x - .2040259 * y + 1.0572252 * z))
        }

        function d3_lab_hcl(l, a, b) {
            return l > 0 ? d3_hcl(Math.atan2(b, a) * d3_degrees, Math.sqrt(a * a + b * b), l) : d3_hcl(0 / 0, 0 / 0, l)
        }

        function d3_lab_xyz(x) {
            return x > .206893034 ? x * x * x : (x - 4 / 29) / 7.787037
        }

        function d3_xyz_lab(x) {
            return x > .008856 ? Math.pow(x, 1 / 3) : 7.787037 * x + 4 / 29
        }

        function d3_xyz_rgb(r) {
            return Math.round(255 * (.00304 >= r ? 12.92 * r : 1.055 * Math.pow(r, 1 / 2.4) - .055))
        }

        function d3_rgb(r, g, b) {
            return new d3_Rgb(r, g, b)
        }

        function d3_Rgb(r, g, b) {
            this.r = r, this.g = g, this.b = b
        }

        function d3_rgb_hex(v) {
            return 16 > v ? "0" + Math.max(0, v).toString(16) : Math.min(255, v).toString(16)
        }

        function d3_rgb_parse(format, rgb, hsl) {
            var m1, m2, name, r = 0,
                g = 0,
                b = 0;
            if (m1 = /([a-z]+)\((.*)\)/i.exec(format)) switch (m2 = m1[2].split(","), m1[1]) {
                case "hsl":
                    return hsl(parseFloat(m2[0]), parseFloat(m2[1]) / 100, parseFloat(m2[2]) / 100);
                case "rgb":
                    return rgb(d3_rgb_parseNumber(m2[0]), d3_rgb_parseNumber(m2[1]), d3_rgb_parseNumber(m2[2]))
            }
            return (name = d3_rgb_names.get(format)) ? rgb(name.r, name.g, name.b) : (null != format && "#" === format.charAt(0) && (4 === format.length ? (r = format.charAt(1), r += r, g = format.charAt(2), g += g, b = format.charAt(3), b += b) : 7 === format.length && (r = format.substring(1, 3), g = format.substring(3, 5), b = format.substring(5, 7)), r = parseInt(r, 16), g = parseInt(g, 16), b = parseInt(b, 16)), rgb(r, g, b))
        }

        function d3_rgb_hsl(r, g, b) {
            var h, s, min = Math.min(r /= 255, g /= 255, b /= 255),
                max = Math.max(r, g, b),
                d = max - min,
                l = (max + min) / 2;
            return d ? (s = .5 > l ? d / (max + min) : d / (2 - max - min), h = r == max ? (g - b) / d + (b > g ? 6 : 0) : g == max ? (b - r) / d + 2 : (r - g) / d + 4, h *= 60) : (h = 0 / 0, s = l > 0 && 1 > l ? 0 : h), d3_hsl(h, s, l)
        }

        function d3_rgb_lab(r, g, b) {
            r = d3_rgb_xyz(r), g = d3_rgb_xyz(g), b = d3_rgb_xyz(b);
            var x = d3_xyz_lab((.4124564 * r + .3575761 * g + .1804375 * b) / d3_lab_X),
                y = d3_xyz_lab((.2126729 * r + .7151522 * g + .072175 * b) / d3_lab_Y),
                z = d3_xyz_lab((.0193339 * r + .119192 * g + .9503041 * b) / d3_lab_Z);
            return d3_lab(116 * y - 16, 500 * (x - y), 200 * (y - z))
        }

        function d3_rgb_xyz(r) {
            return (r /= 255) <= .04045 ? r / 12.92 : Math.pow((r + .055) / 1.055, 2.4)
        }

        function d3_rgb_parseNumber(c) {
            var f = parseFloat(c);
            return "%" === c.charAt(c.length - 1) ? Math.round(2.55 * f) : f
        }

        function d3_functor(v) {
            return "function" == typeof v ? v : function() {
                return v
            }
        }

        function d3_identity(d) {
            return d
        }

        function d3_xhr_fixCallback(callback) {
            return 1 === callback.length ? function(error, request) {
                callback(null == error ? request : null)
            } : callback
        }

        function d3_dsv(delimiter, mimeType) {
            function dsv(url, row, callback) {
                arguments.length < 3 && (callback = row, row = null);
                var xhr = d3.xhr(url, mimeType, callback);
                return xhr.row = function(_) {
                    return arguments.length ? xhr.response(null == (row = _) ? response : typedResponse(_)) : row
                }, xhr.row(row)
            }

            function response(request) {
                return dsv.parse(request.responseText)
            }

            function typedResponse(f) {
                return function(request) {
                    return dsv.parse(request.responseText, f)
                }
            }

            function formatRow(row) {
                return row.map(formatValue).join(delimiter)
            }

            function formatValue(text) {
                return reFormat.test(text) ? '"' + text.replace(/\"/g, '""') + '"' : text
            }
            var reFormat = new RegExp('["' + delimiter + "\n]"),
                delimiterCode = delimiter.charCodeAt(0);
            return dsv.parse = function(text, f) {
                var o;
                return dsv.parseRows(text, function(row, i) {
                    if (o) return o(row, i - 1);
                    var a = new Function("d", "return {" + row.map(function(name, i) {
                        return JSON.stringify(name) + ": d[" + i + "]"
                    }).join(",") + "}");
                    o = f ? function(row, i) {
                        return f(a(row), i)
                    } : a
                })
            }, dsv.parseRows = function(text, f) {
                function token() {
                    if (I >= N) return EOF;
                    if (eol) return eol = !1, EOL;
                    var j = I;
                    if (34 === text.charCodeAt(j)) {
                        for (var i = j; i++ < N;)
                            if (34 === text.charCodeAt(i)) {
                                if (34 !== text.charCodeAt(i + 1)) break;
                                ++i
                            }
                        I = i + 2;
                        var c = text.charCodeAt(i + 1);
                        return 13 === c ? (eol = !0, 10 === text.charCodeAt(i + 2) && ++I) : 10 === c && (eol = !0), text.substring(j + 1, i).replace(/""/g, '"')
                    }
                    for (; N > I;) {
                        var c = text.charCodeAt(I++),
                            k = 1;
                        if (10 === c) eol = !0;
                        else if (13 === c) eol = !0, 10 === text.charCodeAt(I) && (++I, ++k);
                        else if (c !== delimiterCode) continue;
                        return text.substring(j, I - k)
                    }
                    return text.substring(j)
                }
                for (var t, eol, EOL = {}, EOF = {}, rows = [], N = text.length, I = 0, n = 0;
                    (t = token()) !== EOF;) {
                    for (var a = []; t !== EOL && t !== EOF;) a.push(t), t = token();
                    (!f || (a = f(a, n++))) && rows.push(a)
                }
                return rows
            }, dsv.format = function(rows) {
                if (Array.isArray(rows[0])) return dsv.formatRows(rows);
                var fieldSet = new d3_Set,
                    fields = [];
                return rows.forEach(function(row) {
                    for (var field in row) fieldSet.has(field) || fields.push(fieldSet.add(field))
                }), [fields.map(formatValue).join(delimiter)].concat(rows.map(function(row) {
                    return fields.map(function(field) {
                        return formatValue(row[field])
                    }).join(delimiter)
                })).join("\n")
            }, dsv.formatRows = function(rows) {
                return rows.map(formatRow).join("\n")
            }, dsv
        }

        function d3_timer_step() {
            for (var elapsed, now = Date.now(), t1 = d3_timer_queue; t1;) elapsed = now - t1.then, elapsed >= t1.delay && (t1.flush = t1.callback(elapsed)), t1 = t1.next;
            var delay = d3_timer_flush() - now;
            delay > 24 ? (isFinite(delay) && (clearTimeout(d3_timer_timeout), d3_timer_timeout = setTimeout(d3_timer_step, delay)), d3_timer_interval = 0) : (d3_timer_interval = 1, d3_timer_frame(d3_timer_step))
        }

        function d3_timer_flush() {
            for (var t0 = null, t1 = d3_timer_queue, then = 1 / 0; t1;) t1.flush ? (delete d3_timer_byId[t1.callback.id], t1 = t0 ? t0.next = t1.next : d3_timer_queue = t1.next) : (then = Math.min(then, t1.then + t1.delay), t1 = (t0 = t1).next);
            return then
        }

        function d3_formatPrefix(d, i) {
            var k = Math.pow(10, 3 * Math.abs(8 - i));
            return {
                scale: i > 8 ? function(d) {
                    return d / k
                } : function(d) {
                    return d * k
                },
                symbol: d
            }
        }

        function d3_format_precision(x, p) {
            return p - (x ? Math.ceil(Math.log(x) / Math.LN10) : 1)
        }

        function d3_format_typeDefault(x) {
            return x + ""
        }

        function d3_geo_streamGeometry(geometry, listener) {
            geometry && d3_geo_streamGeometryType.hasOwnProperty(geometry.type) && d3_geo_streamGeometryType[geometry.type](geometry, listener)
        }

        function d3_geo_streamLine(coordinates, listener, closed) {
            var coordinate, i = -1,
                n = coordinates.length - closed;
            for (listener.lineStart(); ++i < n;) coordinate = coordinates[i], listener.point(coordinate[0], coordinate[1]);
            listener.lineEnd()
        }

        function d3_geo_streamPolygon(coordinates, listener) {
            var i = -1,
                n = coordinates.length;
            for (listener.polygonStart(); ++i < n;) d3_geo_streamLine(coordinates[i], listener, 1);
            listener.polygonEnd()
        }

        function d3_geo_areaRingStart() {
            function nextPoint(λ, φ) {
                λ *= d3_radians, φ = φ * d3_radians / 2 + π / 4;
                var dλ = λ - λ0,
                    cosφ = Math.cos(φ),
                    sinφ = Math.sin(φ),
                    k = sinφ0 * sinφ,
                    u0 = d3_geo_areaRingU,
                    v0 = d3_geo_areaRingV,
                    u = cosφ0 * cosφ + k * Math.cos(dλ),
                    v = k * Math.sin(dλ);
                d3_geo_areaRingU = u0 * u - v0 * v, d3_geo_areaRingV = v0 * u + u0 * v, λ0 = λ, cosφ0 = cosφ, sinφ0 = sinφ
            }
            var λ00, φ00, λ0, cosφ0, sinφ0;
            d3_geo_area.point = function(λ, φ) {
                d3_geo_area.point = nextPoint, λ0 = (λ00 = λ) * d3_radians, cosφ0 = Math.cos(φ = (φ00 = φ) * d3_radians / 2 + π / 4), sinφ0 = Math.sin(φ)
            }, d3_geo_area.lineEnd = function() {
                nextPoint(λ00, φ00)
            }
        }

        function d3_geo_bounds(projectStream) {
            function boundPoint(x, y) {
                x0 > x && (x0 = x), x > x1 && (x1 = x), y0 > y && (y0 = y), y > y1 && (y1 = y)
            }

            function boundPolygonLineEnd() {
                bound.point = bound.lineEnd = d3_noop
            }
            var x0, y0, x1, y1, bound = {
                point: boundPoint,
                lineStart: d3_noop,
                lineEnd: d3_noop,
                polygonStart: function() {
                    bound.lineEnd = boundPolygonLineEnd
                },
                polygonEnd: function() {
                    bound.point = boundPoint
                }
            };
            return function(feature) {
                return y1 = x1 = -(x0 = y0 = 1 / 0), d3.geo.stream(feature, projectStream(bound)), [
                    [x0, y0],
                    [x1, y1]
                ]
            }
        }

        function d3_geo_centroidPoint(λ, φ) {
            if (!d3_geo_centroidDimension) {
                ++d3_geo_centroidW, λ *= d3_radians;
                var cosφ = Math.cos(φ *= d3_radians);
                d3_geo_centroidX += (cosφ * Math.cos(λ) - d3_geo_centroidX) / d3_geo_centroidW, d3_geo_centroidY += (cosφ * Math.sin(λ) - d3_geo_centroidY) / d3_geo_centroidW, d3_geo_centroidZ += (Math.sin(φ) - d3_geo_centroidZ) / d3_geo_centroidW
            }
        }

        function d3_geo_centroidRingStart() {
            var λ00, φ00;
            d3_geo_centroidDimension = 1, d3_geo_centroidLineStart(), d3_geo_centroidDimension = 2;
            var linePoint = d3_geo_centroid.point;
            d3_geo_centroid.point = function(λ, φ) {
                linePoint(λ00 = λ, φ00 = φ)
            }, d3_geo_centroid.lineEnd = function() {
                d3_geo_centroid.point(λ00, φ00), d3_geo_centroidLineEnd(), d3_geo_centroid.lineEnd = d3_geo_centroidLineEnd
            }
        }

        function d3_geo_centroidLineStart() {
            function nextPoint(λ, φ) {
                λ *= d3_radians;
                var cosφ = Math.cos(φ *= d3_radians),
                    x = cosφ * Math.cos(λ),
                    y = cosφ * Math.sin(λ),
                    z = Math.sin(φ),
                    w = Math.atan2(Math.sqrt((w = y0 * z - z0 * y) * w + (w = z0 * x - x0 * z) * w + (w = x0 * y - y0 * x) * w), x0 * x + y0 * y + z0 * z);
                d3_geo_centroidW += w, d3_geo_centroidX += w * (x0 + (x0 = x)), d3_geo_centroidY += w * (y0 + (y0 = y)), d3_geo_centroidZ += w * (z0 + (z0 = z))
            }
            var x0, y0, z0;
            d3_geo_centroidDimension > 1 || (1 > d3_geo_centroidDimension && (d3_geo_centroidDimension = 1, d3_geo_centroidW = d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0), d3_geo_centroid.point = function(λ, φ) {
                λ *= d3_radians;
                var cosφ = Math.cos(φ *= d3_radians);
                x0 = cosφ * Math.cos(λ), y0 = cosφ * Math.sin(λ), z0 = Math.sin(φ), d3_geo_centroid.point = nextPoint
            })
        }

        function d3_geo_centroidLineEnd() {
            d3_geo_centroid.point = d3_geo_centroidPoint
        }

        function d3_geo_cartesian(spherical) {
            var λ = spherical[0],
                φ = spherical[1],
                cosφ = Math.cos(φ);
            return [cosφ * Math.cos(λ), cosφ * Math.sin(λ), Math.sin(φ)]
        }

        function d3_geo_cartesianDot(a, b) {
            return a[0] * b[0] + a[1] * b[1] + a[2] * b[2]
        }

        function d3_geo_cartesianCross(a, b) {
            return [a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]]
        }

        function d3_geo_cartesianAdd(a, b) {
            a[0] += b[0], a[1] += b[1], a[2] += b[2]
        }

        function d3_geo_cartesianScale(vector, k) {
            return [vector[0] * k, vector[1] * k, vector[2] * k]
        }

        function d3_geo_cartesianNormalize(d) {
            var l = Math.sqrt(d[0] * d[0] + d[1] * d[1] + d[2] * d[2]);
            d[0] /= l, d[1] /= l, d[2] /= l
        }

        function d3_true() {
            return !0
        }

        function d3_geo_spherical(cartesian) {
            return [Math.atan2(cartesian[1], cartesian[0]), Math.asin(Math.max(-1, Math.min(1, cartesian[2])))]
        }

        function d3_geo_sphericalEqual(a, b) {
            return Math.abs(a[0] - b[0]) < ε && Math.abs(a[1] - b[1]) < ε
        }

        function d3_geo_clipPolygon(segments, compare, inside, interpolate, listener) {
            var subject = [],
                clip = [];
            if (segments.forEach(function(segment) {
                    if (!((n = segment.length - 1) <= 0)) {
                        var n, p0 = segment[0],
                            p1 = segment[n];
                        if (d3_geo_sphericalEqual(p0, p1)) {
                            listener.lineStart();
                            for (var i = 0; n > i; ++i) listener.point((p0 = segment[i])[0], p0[1]);
                            return void listener.lineEnd()
                        }
                        var a = {
                                point: p0,
                                points: segment,
                                other: null,
                                visited: !1,
                                entry: !0,
                                subject: !0
                            },
                            b = {
                                point: p0,
                                points: [p0],
                                other: a,
                                visited: !1,
                                entry: !1,
                                subject: !1
                            };
                        a.other = b, subject.push(a), clip.push(b), a = {
                            point: p1,
                            points: [p1],
                            other: null,
                            visited: !1,
                            entry: !1,
                            subject: !0
                        }, b = {
                            point: p1,
                            points: [p1],
                            other: a,
                            visited: !1,
                            entry: !0,
                            subject: !1
                        }, a.other = b, subject.push(a), clip.push(b)
                    }
                }), clip.sort(compare), d3_geo_clipPolygonLinkCircular(subject), d3_geo_clipPolygonLinkCircular(clip), subject.length) {
                if (inside)
                    for (var i = 1, e = !inside(clip[0].point), n = clip.length; n > i; ++i) clip[i].entry = e = !e;
                for (var current, points, point, start = subject[0];;) {
                    for (current = start; current.visited;)
                        if ((current = current.next) === start) return;
                    points = current.points, listener.lineStart();
                    do {
                        if (current.visited = current.other.visited = !0, current.entry) {
                            if (current.subject)
                                for (var i = 0; i < points.length; i++) listener.point((point = points[i])[0], point[1]);
                            else interpolate(current.point, current.next.point, 1, listener);
                            current = current.next
                        } else {
                            if (current.subject) {
                                points = current.prev.points;
                                for (var i = points.length; --i >= 0;) listener.point((point = points[i])[0], point[1])
                            } else interpolate(current.point, current.prev.point, -1, listener);
                            current = current.prev
                        }
                        current = current.other, points = current.points
                    } while (!current.visited);
                    listener.lineEnd()
                }
            }
        }

        function d3_geo_clipPolygonLinkCircular(array) {
            if (n = array.length) {
                for (var n, b, i = 0, a = array[0]; ++i < n;) a.next = b = array[i], b.prev = a, a = b;
                a.next = b = array[0], b.prev = a
            }
        }

        function d3_geo_clip(pointVisible, clipLine, interpolate) {
            return function(listener) {
                function point(λ, φ) {
                    pointVisible(λ, φ) && listener.point(λ, φ)
                }

                function pointLine(λ, φ) {
                    line.point(λ, φ)
                }

                function lineStart() {
                    clip.point = pointLine, line.lineStart()
                }

                function lineEnd() {
                    clip.point = point, line.lineEnd()
                }

                function pointRing(λ, φ) {
                    ringListener.point(λ, φ), ring.push([λ, φ])
                }

                function ringStart() {
                    ringListener.lineStart(), ring = []
                }

                function ringEnd() {
                    pointRing(ring[0][0], ring[0][1]), ringListener.lineEnd();
                    var segment, clean = ringListener.clean(),
                        ringSegments = buffer.buffer(),
                        n = ringSegments.length;
                    if (!n) return invisible = !0, invisibleArea += d3_geo_clipAreaRing(ring, -1), void(ring = null);
                    if (ring = null, 1 & clean) {
                        segment = ringSegments[0], visibleArea += d3_geo_clipAreaRing(segment, 1);
                        var point, n = segment.length - 1,
                            i = -1;
                        for (listener.lineStart(); ++i < n;) listener.point((point = segment[i])[0], point[1]);
                        return void listener.lineEnd()
                    }
                    n > 1 && 2 & clean && ringSegments.push(ringSegments.pop().concat(ringSegments.shift())), segments.push(ringSegments.filter(d3_geo_clipSegmentLength1))
                }
                var segments, visibleArea, invisibleArea, invisible, ring, line = clipLine(listener),
                    clip = {
                        point: point,
                        lineStart: lineStart,
                        lineEnd: lineEnd,
                        polygonStart: function() {
                            clip.point = pointRing, clip.lineStart = ringStart, clip.lineEnd = ringEnd, invisible = !1, invisibleArea = visibleArea = 0, segments = [], listener.polygonStart()
                        },
                        polygonEnd: function() {
                            clip.point = point, clip.lineStart = lineStart, clip.lineEnd = lineEnd, segments = d3.merge(segments), segments.length ? d3_geo_clipPolygon(segments, d3_geo_clipSort, null, interpolate, listener) : (-ε > visibleArea || invisible && -ε > invisibleArea) && (listener.lineStart(), interpolate(null, null, 1, listener), listener.lineEnd()), listener.polygonEnd(), segments = null
                        },
                        sphere: function() {
                            listener.polygonStart(), listener.lineStart(), interpolate(null, null, 1, listener), listener.lineEnd(), listener.polygonEnd()
                        }
                    },
                    buffer = d3_geo_clipBufferListener(),
                    ringListener = clipLine(buffer);
                return clip
            }
        }

        function d3_geo_clipSegmentLength1(segment) {
            return segment.length > 1
        }

        function d3_geo_clipBufferListener() {
            var line, lines = [];
            return {
                lineStart: function() {
                    lines.push(line = [])
                },
                point: function(λ, φ) {
                    line.push([λ, φ])
                },
                lineEnd: d3_noop,
                buffer: function() {
                    var buffer = lines;
                    return lines = [], line = null, buffer
                },
                rejoin: function() {
                    lines.length > 1 && lines.push(lines.pop().concat(lines.shift()))
                }
            }
        }

        function d3_geo_clipAreaRing(ring, invisible) {
            if (!(n = ring.length)) return 0;
            for (var n, x, y, i = 0, area = 0, p = ring[0], λ = p[0], φ = p[1], cosφ = Math.cos(φ), x0 = Math.atan2(invisible * Math.sin(λ) * cosφ, Math.sin(φ)), y0 = 1 - invisible * Math.cos(λ) * cosφ, x1 = x0; ++i < n;) p = ring[i], cosφ = Math.cos(φ = p[1]), x = Math.atan2(invisible * Math.sin(λ = p[0]) * cosφ, Math.sin(φ)), y = 1 - invisible * Math.cos(λ) * cosφ, Math.abs(y0 - 2) < ε && Math.abs(y - 2) < ε || (Math.abs(y) < ε || Math.abs(y0) < ε || (Math.abs(Math.abs(x - x0) - π) < ε ? y + y0 > 2 && (area += 4 * (x - x0)) : area += Math.abs(y0 - 2) < ε ? 4 * (x - x1) : ((3 * π + x - x0) % (2 * π) - π) * (y0 + y)), x1 = x0, x0 = x, y0 = y);
            return area
        }

        function d3_geo_clipSort(a, b) {
            return ((a = a.point)[0] < 0 ? a[1] - π / 2 - ε : π / 2 - a[1]) - ((b = b.point)[0] < 0 ? b[1] - π / 2 - ε : π / 2 - b[1])
        }

        function d3_geo_clipAntimeridianLine(listener) {
            var clean, λ0 = 0 / 0,
                φ0 = 0 / 0,
                sλ0 = 0 / 0;
            return {
                lineStart: function() {
                    listener.lineStart(), clean = 1
                },
                point: function(λ1, φ1) {
                    var sλ1 = λ1 > 0 ? π : -π,
                        dλ = Math.abs(λ1 - λ0);
                    Math.abs(dλ - π) < ε ? (listener.point(λ0, φ0 = (φ0 + φ1) / 2 > 0 ? π / 2 : -π / 2), listener.point(sλ0, φ0), listener.lineEnd(), listener.lineStart(), listener.point(sλ1, φ0), listener.point(λ1, φ0), clean = 0) : sλ0 !== sλ1 && dλ >= π && (Math.abs(λ0 - sλ0) < ε && (λ0 -= sλ0 * ε), Math.abs(λ1 - sλ1) < ε && (λ1 -= sλ1 * ε), φ0 = d3_geo_clipAntimeridianIntersect(λ0, φ0, λ1, φ1), listener.point(sλ0, φ0), listener.lineEnd(), listener.lineStart(), listener.point(sλ1, φ0), clean = 0), listener.point(λ0 = λ1, φ0 = φ1), sλ0 = sλ1
                },
                lineEnd: function() {
                    listener.lineEnd(), λ0 = φ0 = 0 / 0
                },
                clean: function() {
                    return 2 - clean
                }
            }
        }

        function d3_geo_clipAntimeridianIntersect(λ0, φ0, λ1, φ1) {
            var cosφ0, cosφ1, sinλ0_λ1 = Math.sin(λ0 - λ1);
            return Math.abs(sinλ0_λ1) > ε ? Math.atan((Math.sin(φ0) * (cosφ1 = Math.cos(φ1)) * Math.sin(λ1) - Math.sin(φ1) * (cosφ0 = Math.cos(φ0)) * Math.sin(λ0)) / (cosφ0 * cosφ1 * sinλ0_λ1)) : (φ0 + φ1) / 2
        }

        function d3_geo_clipAntimeridianInterpolate(from, to, direction, listener) {
            var φ;
            if (null == from) φ = direction * π / 2, listener.point(-π, φ), listener.point(0, φ), listener.point(π, φ), listener.point(π, 0), listener.point(π, -φ), listener.point(0, -φ), listener.point(-π, -φ), listener.point(-π, 0), listener.point(-π, φ);
            else if (Math.abs(from[0] - to[0]) > ε) {
                var s = (from[0] < to[0] ? 1 : -1) * π;
                φ = direction * s / 2, listener.point(-s, φ), listener.point(0, φ), listener.point(s, φ)
            } else listener.point(to[0], to[1])
        }

        function d3_geo_clipCircle(radius) {
            function visible(λ, φ) {
                return Math.cos(λ) * Math.cos(φ) > cr
            }

            function clipLine(listener) {
                var point0, c0, v0, v00, clean;
                return {
                    lineStart: function() {
                        v00 = v0 = !1, clean = 1
                    },
                    point: function(λ, φ) {
                        var point2, point1 = [λ, φ],
                            v = visible(λ, φ),
                            c = smallRadius ? v ? 0 : code(λ, φ) : v ? code(λ + (0 > λ ? π : -π), φ) : 0;
                        if (!point0 && (v00 = v0 = v) && listener.lineStart(), v !== v0 && (point2 = intersect(point0, point1), (d3_geo_sphericalEqual(point0, point2) || d3_geo_sphericalEqual(point1, point2)) && (point1[0] += ε, point1[1] += ε, v = visible(point1[0], point1[1]))), v !== v0) clean = 0, v ? (listener.lineStart(), point2 = intersect(point1, point0), listener.point(point2[0], point2[1])) : (point2 = intersect(point0, point1), listener.point(point2[0], point2[1]), listener.lineEnd()), point0 = point2;
                        else if (notHemisphere && point0 && smallRadius ^ v) {
                            var t;
                            c & c0 || !(t = intersect(point1, point0, !0)) || (clean = 0, smallRadius ? (listener.lineStart(), listener.point(t[0][0], t[0][1]), listener.point(t[1][0], t[1][1]), listener.lineEnd()) : (listener.point(t[1][0], t[1][1]), listener.lineEnd(), listener.lineStart(), listener.point(t[0][0], t[0][1])))
                        }!v || point0 && d3_geo_sphericalEqual(point0, point1) || listener.point(point1[0], point1[1]), point0 = point1, v0 = v, c0 = c
                    },
                    lineEnd: function() {
                        v0 && listener.lineEnd(), point0 = null
                    },
                    clean: function() {
                        return clean | (v00 && v0) << 1
                    }
                }
            }

            function intersect(a, b, two) {
                var pa = d3_geo_cartesian(a),
                    pb = d3_geo_cartesian(b),
                    n1 = [1, 0, 0],
                    n2 = d3_geo_cartesianCross(pa, pb),
                    n2n2 = d3_geo_cartesianDot(n2, n2),
                    n1n2 = n2[0],
                    determinant = n2n2 - n1n2 * n1n2;
                if (!determinant) return !two && a;
                var c1 = cr * n2n2 / determinant,
                    c2 = -cr * n1n2 / determinant,
                    n1xn2 = d3_geo_cartesianCross(n1, n2),
                    A = d3_geo_cartesianScale(n1, c1),
                    B = d3_geo_cartesianScale(n2, c2);
                d3_geo_cartesianAdd(A, B);
                var u = n1xn2,
                    w = d3_geo_cartesianDot(A, u),
                    uu = d3_geo_cartesianDot(u, u),
                    t2 = w * w - uu * (d3_geo_cartesianDot(A, A) - 1);
                if (!(0 > t2)) {
                    var t = Math.sqrt(t2),
                        q = d3_geo_cartesianScale(u, (-w - t) / uu);
                    if (d3_geo_cartesianAdd(q, A), q = d3_geo_spherical(q), !two) return q;
                    var z, λ0 = a[0],
                        λ1 = b[0],
                        φ0 = a[1],
                        φ1 = b[1];
                    λ0 > λ1 && (z = λ0, λ0 = λ1, λ1 = z);
                    var δλ = λ1 - λ0,
                        polar = Math.abs(δλ - π) < ε,
                        meridian = polar || ε > δλ;
                    if (!polar && φ0 > φ1 && (z = φ0, φ0 = φ1, φ1 = z), meridian ? polar ? φ0 + φ1 > 0 ^ q[1] < (Math.abs(q[0] - λ0) < ε ? φ0 : φ1) : φ0 <= q[1] && q[1] <= φ1 : δλ > π ^ (λ0 <= q[0] && q[0] <= λ1)) {
                        var q1 = d3_geo_cartesianScale(u, (-w + t) / uu);
                        return d3_geo_cartesianAdd(q1, A), [q, d3_geo_spherical(q1)]
                    }
                }
            }

            function code(λ, φ) {
                var r = smallRadius ? radius : π - radius,
                    code = 0;
                return -r > λ ? code |= 1 : λ > r && (code |= 2), -r > φ ? code |= 4 : φ > r && (code |= 8), code
            }
            var cr = Math.cos(radius),
                smallRadius = cr > 0,
                notHemisphere = Math.abs(cr) > ε,
                interpolate = d3_geo_circleInterpolate(radius, 6 * d3_radians);
            return d3_geo_clip(visible, clipLine, interpolate)
        }

        function d3_geo_clipView(x0, y0, x1, y1) {
            function corner(p, direction) {
                return Math.abs(p[0] - x0) < ε ? direction > 0 ? 0 : 3 : Math.abs(p[0] - x1) < ε ? direction > 0 ? 2 : 1 : Math.abs(p[1] - y0) < ε ? direction > 0 ? 1 : 0 : direction > 0 ? 3 : 2
            }

            function compare(a, b) {
                return comparePoints(a.point, b.point)
            }

            function comparePoints(a, b) {
                var ca = corner(a, 1),
                    cb = corner(b, 1);
                return ca !== cb ? ca - cb : 0 === ca ? b[1] - a[1] : 1 === ca ? a[0] - b[0] : 2 === ca ? a[1] - b[1] : b[0] - a[0]
            }

            function clipLine(a, b) {
                var dx = b[0] - a[0],
                    dy = b[1] - a[1],
                    t = [0, 1];
                return Math.abs(dx) < ε && Math.abs(dy) < ε ? x0 <= a[0] && a[0] <= x1 && y0 <= a[1] && a[1] <= y1 : d3_geo_clipViewT(x0 - a[0], dx, t) && d3_geo_clipViewT(a[0] - x1, -dx, t) && d3_geo_clipViewT(y0 - a[1], dy, t) && d3_geo_clipViewT(a[1] - y1, -dy, t) ? (t[1] < 1 && (b[0] = a[0] + t[1] * dx, b[1] = a[1] + t[1] * dy), t[0] > 0 && (a[0] += t[0] * dx, a[1] += t[0] * dy), !0) : !1
            }
            return function(listener) {
                function inside(point) {
                    var a = corner(point, -1),
                        i = insidePolygon([0 === a || 3 === a ? x0 : x1, a > 1 ? y1 : y0]);
                    return i
                }

                function insidePolygon(p) {
                    for (var wn = 0, n = polygon.length, y = p[1], i = 0; n > i; ++i)
                        for (var j = 1, v = polygon[i], m = v.length, a = v[0]; m > j; ++j) b = v[j], a[1] <= y ? b[1] > y && isLeft(a, b, p) > 0 && ++wn : b[1] <= y && isLeft(a, b, p) < 0 && --wn, a = b;
                    return 0 !== wn
                }

                function isLeft(a, b, c) {
                    return (b[0] - a[0]) * (c[1] - a[1]) - (c[0] - a[0]) * (b[1] - a[1])
                }

                function interpolate(from, to, direction, listener) {
                    var a = 0,
                        a1 = 0;
                    if (null == from || (a = corner(from, direction)) !== (a1 = corner(to, direction)) || comparePoints(from, to) < 0 ^ direction > 0) {
                        do listener.point(0 === a || 3 === a ? x0 : x1, a > 1 ? y1 : y0); while ((a = (a + direction + 4) % 4) !== a1)
                    } else listener.point(to[0], to[1])
                }

                function visible(x, y) {
                    return x >= x0 && x1 >= x && y >= y0 && y1 >= y
                }

                function point(x, y) {
                    visible(x, y) && listener.point(x, y)
                }

                function lineStart() {
                    clip.point = linePoint, polygon && polygon.push(ring = []), first = !0, v_ = !1, x_ = y_ = 0 / 0
                }

                function lineEnd() {
                    segments && (linePoint(x__, y__), v__ && v_ && bufferListener.rejoin(), segments.push(bufferListener.buffer())), clip.point = point, v_ && listener.lineEnd()
                }

                function linePoint(x, y) {
                    x = Math.max(-d3_geo_clipViewMAX, Math.min(d3_geo_clipViewMAX, x)), y = Math.max(-d3_geo_clipViewMAX, Math.min(d3_geo_clipViewMAX, y));
                    var v = visible(x, y);
                    if (polygon && ring.push([x, y]), first) x__ = x, y__ = y, v__ = v, first = !1, v && (listener.lineStart(), listener.point(x, y));
                    else if (v && v_) listener.point(x, y);
                    else {
                        var a = [x_, y_],
                            b = [x, y];
                        clipLine(a, b) ? (v_ || (listener.lineStart(), listener.point(a[0], a[1])), listener.point(b[0], b[1]), v || listener.lineEnd()) : (listener.lineStart(), listener.point(x, y))
                    }
                    x_ = x, y_ = y, v_ = v
                }
                var segments, polygon, ring, x__, y__, v__, x_, y_, v_, first, listener_ = listener,
                    bufferListener = d3_geo_clipBufferListener(),
                    clip = {
                        point: point,
                        lineStart: lineStart,
                        lineEnd: lineEnd,
                        polygonStart: function() {
                            listener = bufferListener, segments = [], polygon = []
                        },
                        polygonEnd: function() {
                            listener = listener_, (segments = d3.merge(segments)).length ? (listener.polygonStart(), d3_geo_clipPolygon(segments, compare, inside, interpolate, listener), listener.polygonEnd()) : insidePolygon([x0, y0]) && (listener.polygonStart(), listener.lineStart(), interpolate(null, null, 1, listener), listener.lineEnd(), listener.polygonEnd()), segments = polygon = ring = null
                        }
                    };
                return clip
            }
        }

        function d3_geo_clipViewT(num, denominator, t) {
            if (Math.abs(denominator) < ε) return 0 >= num;
            var u = num / denominator;
            if (denominator > 0) {
                if (u > t[1]) return !1;
                u > t[0] && (t[0] = u)
            } else {
                if (u < t[0]) return !1;
                u < t[1] && (t[1] = u)
            }
            return !0
        }

        function d3_geo_compose(a, b) {
            function compose(x, y) {
                return x = a(x, y), b(x[0], x[1])
            }
            return a.invert && b.invert && (compose.invert = function(x, y) {
                return x = b.invert(x, y), x && a.invert(x[0], x[1])
            }), compose
        }

        function d3_geo_resample(project) {
            function resample(stream) {
                function point(x, y) {
                    x = project(x, y), stream.point(x[0], x[1])
                }

                function lineStart() {
                    x0 = 0 / 0, resample.point = linePoint, stream.lineStart()
                }

                function linePoint(λ, φ) {
                    var c = d3_geo_cartesian([λ, φ]),
                        p = project(λ, φ);
                    resampleLineTo(x0, y0, λ0, a0, b0, c0, x0 = p[0], y0 = p[1], λ0 = λ, a0 = c[0], b0 = c[1], c0 = c[2], maxDepth, stream), stream.point(x0, y0)
                }

                function lineEnd() {
                    resample.point = point, stream.lineEnd()
                }

                function polygonLineStart() {
                    var λ00, φ00, x00, y00, a00, b00, c00;
                    lineStart(), resample.point = function(λ, φ) {
                        linePoint(λ00 = λ, φ00 = φ), x00 = x0, y00 = y0, a00 = a0, b00 = b0, c00 = c0, resample.point = linePoint
                    }, resample.lineEnd = function() {
                        resampleLineTo(x0, y0, λ0, a0, b0, c0, x00, y00, λ00, a00, b00, c00, maxDepth, stream), resample.lineEnd = lineEnd, lineEnd()
                    }
                }
                var λ0, x0, y0, a0, b0, c0, resample = {
                    point: point,
                    lineStart: lineStart,
                    lineEnd: lineEnd,
                    polygonStart: function() {
                        stream.polygonStart(), resample.lineStart = polygonLineStart
                    },
                    polygonEnd: function() {
                        stream.polygonEnd(), resample.lineStart = lineStart
                    }
                };
                return resample
            }

            function resampleLineTo(x0, y0, λ0, a0, b0, c0, x1, y1, λ1, a1, b1, c1, depth, stream) {
                var dx = x1 - x0,
                    dy = y1 - y0,
                    d2 = dx * dx + dy * dy;
                if (d2 > 4 * δ2 && depth--) {
                    var a = a0 + a1,
                        b = b0 + b1,
                        c = c0 + c1,
                        m = Math.sqrt(a * a + b * b + c * c),
                        φ2 = Math.asin(c /= m),
                        λ2 = Math.abs(Math.abs(c) - 1) < ε ? (λ0 + λ1) / 2 : Math.atan2(b, a),
                        p = project(λ2, φ2),
                        x2 = p[0],
                        y2 = p[1],
                        dx2 = x2 - x0,
                        dy2 = y2 - y0,
                        dz = dy * dx2 - dx * dy2;
                    (dz * dz / d2 > δ2 || Math.abs((dx * dx2 + dy * dy2) / d2 - .5) > .3) && (resampleLineTo(x0, y0, λ0, a0, b0, c0, x2, y2, λ2, a /= m, b /= m, c, depth, stream), stream.point(x2, y2), resampleLineTo(x2, y2, λ2, a, b, c, x1, y1, λ1, a1, b1, c1, depth, stream))
                }
            }
            var δ2 = .5,
                maxDepth = 16;
            return resample.precision = function(_) {
                return arguments.length ? (maxDepth = (δ2 = _ * _) > 0 && 16, resample) : Math.sqrt(δ2)
            }, resample
        }

        function d3_geo_projection(project) {
            return d3_geo_projectionMutator(function() {
                return project
            })()
        }

        function d3_geo_projectionMutator(projectAt) {
            function projection(point) {
                return point = projectRotate(point[0] * d3_radians, point[1] * d3_radians), [point[0] * k + δx, δy - point[1] * k]
            }

            function invert(point) {
                return point = projectRotate.invert((point[0] - δx) / k, (δy - point[1]) / k), point && [point[0] * d3_degrees, point[1] * d3_degrees]
            }

            function reset() {
                projectRotate = d3_geo_compose(rotate = d3_geo_rotation(δλ, δφ, δγ), project);
                var center = project(λ, φ);
                return δx = x - center[0] * k, δy = y + center[1] * k, projection
            }
            var project, rotate, projectRotate, δx, δy, projectResample = d3_geo_resample(function(x, y) {
                    return x = project(x, y), [x[0] * k + δx, δy - x[1] * k]
                }),
                k = 150,
                x = 480,
                y = 250,
                λ = 0,
                φ = 0,
                δλ = 0,
                δφ = 0,
                δγ = 0,
                preclip = d3_geo_clipAntimeridian,
                postclip = d3_identity,
                clipAngle = null,
                clipExtent = null;
            return projection.stream = function(stream) {
                    return d3_geo_projectionRadiansRotate(rotate, preclip(projectResample(postclip(stream))))
                }, projection.clipAngle = function(_) {
                    return arguments.length ? (preclip = null == _ ? (clipAngle = _, d3_geo_clipAntimeridian) : d3_geo_clipCircle((clipAngle = +_) * d3_radians), projection) : clipAngle
                }, projection.clipExtent = function(_) {
                    return arguments.length ? (clipExtent = _, postclip = null == _ ? d3_identity : d3_geo_clipView(_[0][0], _[0][1], _[1][0], _[1][1]), projection) : clipExtent
                }, projection.scale = function(_) {
                    return arguments.length ? (k = +_, reset()) : k
                }, projection.translate = function(_) {
                    return arguments.length ? (x = +_[0], y = +_[1], reset()) : [x, y]
                }, projection.center = function(_) {
                    return arguments.length ? (λ = _[0] % 360 * d3_radians, φ = _[1] % 360 * d3_radians, reset()) : [λ * d3_degrees, φ * d3_degrees]
                }, projection.rotate = function(_) {
                    return arguments.length ? (δλ = _[0] % 360 * d3_radians, δφ = _[1] % 360 * d3_radians, δγ = _.length > 2 ? _[2] % 360 * d3_radians : 0, reset()) : [δλ * d3_degrees, δφ * d3_degrees, δγ * d3_degrees]
                }, d3.rebind(projection, projectResample, "precision"),
                function() {
                    return project = projectAt.apply(this, arguments), projection.invert = project.invert && invert, reset()
                }
        }

        function d3_geo_projectionRadiansRotate(rotate, stream) {
            return {
                point: function(x, y) {
                    y = rotate(x * d3_radians, y * d3_radians), x = y[0], stream.point(x > π ? x - 2 * π : -π > x ? x + 2 * π : x, y[1])
                },
                sphere: function() {
                    stream.sphere()
                },
                lineStart: function() {
                    stream.lineStart()
                },
                lineEnd: function() {
                    stream.lineEnd()
                },
                polygonStart: function() {
                    stream.polygonStart()
                },
                polygonEnd: function() {
                    stream.polygonEnd()
                }
            }
        }

        function d3_geo_equirectangular(λ, φ) {
            return [λ, φ]
        }

        function d3_geo_rotation(δλ, δφ, δγ) {
            return δλ ? δφ || δγ ? d3_geo_compose(d3_geo_rotationλ(δλ), d3_geo_rotationφγ(δφ, δγ)) : d3_geo_rotationλ(δλ) : δφ || δγ ? d3_geo_rotationφγ(δφ, δγ) : d3_geo_equirectangular
        }

        function d3_geo_forwardRotationλ(δλ) {
            return function(λ, φ) {
                return λ += δλ, [λ > π ? λ - 2 * π : -π > λ ? λ + 2 * π : λ, φ]
            }
        }

        function d3_geo_rotationλ(δλ) {
            var rotation = d3_geo_forwardRotationλ(δλ);
            return rotation.invert = d3_geo_forwardRotationλ(-δλ), rotation
        }

        function d3_geo_rotationφγ(δφ, δγ) {
            function rotation(λ, φ) {
                var cosφ = Math.cos(φ),
                    x = Math.cos(λ) * cosφ,
                    y = Math.sin(λ) * cosφ,
                    z = Math.sin(φ),
                    k = z * cosδφ + x * sinδφ;
                return [Math.atan2(y * cosδγ - k * sinδγ, x * cosδφ - z * sinδφ), Math.asin(Math.max(-1, Math.min(1, k * cosδγ + y * sinδγ)))]
            }
            var cosδφ = Math.cos(δφ),
                sinδφ = Math.sin(δφ),
                cosδγ = Math.cos(δγ),
                sinδγ = Math.sin(δγ);
            return rotation.invert = function(λ, φ) {
                var cosφ = Math.cos(φ),
                    x = Math.cos(λ) * cosφ,
                    y = Math.sin(λ) * cosφ,
                    z = Math.sin(φ),
                    k = z * cosδγ - y * sinδγ;
                return [Math.atan2(y * cosδγ + z * sinδγ, x * cosδφ + k * sinδφ), Math.asin(Math.max(-1, Math.min(1, k * cosδφ - x * sinδφ)))]
            }, rotation
        }

        function d3_geo_circleInterpolate(radius, precision) {
            var cr = Math.cos(radius),
                sr = Math.sin(radius);
            return function(from, to, direction, listener) {
                null != from ? (from = d3_geo_circleAngle(cr, from), to = d3_geo_circleAngle(cr, to), (direction > 0 ? to > from : from > to) && (from += 2 * direction * π)) : (from = radius + 2 * direction * π, to = radius);
                for (var point, step = direction * precision, t = from; direction > 0 ? t > to : to > t; t -= step) listener.point((point = d3_geo_spherical([cr, -sr * Math.cos(t), -sr * Math.sin(t)]))[0], point[1])
            }
        }

        function d3_geo_circleAngle(cr, point) {
            var a = d3_geo_cartesian(point);
            a[0] -= cr, d3_geo_cartesianNormalize(a);
            var angle = d3_acos(-a[1]);
            return ((-a[2] < 0 ? -angle : angle) + 2 * Math.PI - ε) % (2 * Math.PI)
        }

        function d3_geo_graticuleX(y0, y1, dy) {
            var y = d3.range(y0, y1 - ε, dy).concat(y1);
            return function(x) {
                return y.map(function(y) {
                    return [x, y]
                })
            }
        }

        function d3_geo_graticuleY(x0, x1, dx) {
            var x = d3.range(x0, x1 - ε, dx).concat(x1);
            return function(y) {
                return x.map(function(x) {
                    return [x, y]
                })
            }
        }

        function d3_source(d) {
            return d.source
        }

        function d3_target(d) {
            return d.target
        }

        function d3_geo_interpolate(x0, y0, x1, y1) {
            var cy0 = Math.cos(y0),
                sy0 = Math.sin(y0),
                cy1 = Math.cos(y1),
                sy1 = Math.sin(y1),
                kx0 = cy0 * Math.cos(x0),
                ky0 = cy0 * Math.sin(x0),
                kx1 = cy1 * Math.cos(x1),
                ky1 = cy1 * Math.sin(x1),
                d = 2 * Math.asin(Math.sqrt(d3_haversin(y1 - y0) + cy0 * cy1 * d3_haversin(x1 - x0))),
                k = 1 / Math.sin(d),
                interpolate = d ? function(t) {
                    var B = Math.sin(t *= d) * k,
                        A = Math.sin(d - t) * k,
                        x = A * kx0 + B * kx1,
                        y = A * ky0 + B * ky1,
                        z = A * sy0 + B * sy1;
                    return [Math.atan2(y, x) * d3_degrees, Math.atan2(z, Math.sqrt(x * x + y * y)) * d3_degrees]
                } : function() {
                    return [x0 * d3_degrees, y0 * d3_degrees]
                };
            return interpolate.distance = d, interpolate
        }

        function d3_geo_lengthLineStart() {
            function nextPoint(λ, φ) {
                var sinφ = Math.sin(φ *= d3_radians),
                    cosφ = Math.cos(φ),
                    t = Math.abs((λ *= d3_radians) - λ0),
                    cosΔλ = Math.cos(t);
                d3_geo_lengthSum += Math.atan2(Math.sqrt((t = cosφ * Math.sin(t)) * t + (t = cosφ0 * sinφ - sinφ0 * cosφ * cosΔλ) * t), sinφ0 * sinφ + cosφ0 * cosφ * cosΔλ), λ0 = λ, sinφ0 = sinφ, cosφ0 = cosφ
            }
            var λ0, sinφ0, cosφ0;
            d3_geo_length.point = function(λ, φ) {
                λ0 = λ * d3_radians, sinφ0 = Math.sin(φ *= d3_radians), cosφ0 = Math.cos(φ), d3_geo_length.point = nextPoint
            }, d3_geo_length.lineEnd = function() {
                d3_geo_length.point = d3_geo_length.lineEnd = d3_noop
            }
        }

        function d3_geo_conic(projectAt) {
            var φ0 = 0,
                φ1 = π / 3,
                m = d3_geo_projectionMutator(projectAt),
                p = m(φ0, φ1);
            return p.parallels = function(_) {
                return arguments.length ? m(φ0 = _[0] * π / 180, φ1 = _[1] * π / 180) : [φ0 / π * 180, φ1 / π * 180]
            }, p
        }

        function d3_geo_conicEqualArea(φ0, φ1) {
            function forward(λ, φ) {
                var ρ = Math.sqrt(C - 2 * n * Math.sin(φ)) / n;
                return [ρ * Math.sin(λ *= n), ρ0 - ρ * Math.cos(λ)]
            }
            var sinφ0 = Math.sin(φ0),
                n = (sinφ0 + Math.sin(φ1)) / 2,
                C = 1 + sinφ0 * (2 * n - sinφ0),
                ρ0 = Math.sqrt(C) / n;
            return forward.invert = function(x, y) {
                var ρ0_y = ρ0 - y;
                return [Math.atan2(x, ρ0_y) / n, Math.asin((C - (x * x + ρ0_y * ρ0_y) * n * n) / (2 * n))]
            }, forward
        }

        function d3_geo_albersUsaInvert(projection, extent) {
            var a = projection(extent[0]),
                b = projection([.5 * (extent[0][0] + extent[1][0]), extent[0][1]]),
                c = projection([extent[1][0], extent[0][1]]),
                d = projection(extent[1]),
                dya = b[1] - a[1],
                dxa = b[0] - a[0],
                dyb = c[1] - b[1],
                dxb = c[0] - b[0],
                ma = dya / dxa,
                mb = dyb / dxb,
                cx = .5 * (ma * mb * (a[1] - c[1]) + mb * (a[0] + b[0]) - ma * (b[0] + c[0])) / (mb - ma),
                cy = (.5 * (a[0] + b[0]) - cx) / ma + .5 * (a[1] + b[1]),
                dx0 = d[0] - cx,
                dy0 = d[1] - cy,
                dx1 = a[0] - cx,
                dy1 = a[1] - cy,
                r0 = dx0 * dx0 + dy0 * dy0,
                r1 = dx1 * dx1 + dy1 * dy1,
                a0 = Math.atan2(dy0, dx0),
                a1 = Math.atan2(dy1, dx1);

            return function(coordinates) {
                var dx = coordinates[0] - cx,
                    dy = coordinates[1] - cy,
                    r = dx * dx + dy * dy,
                    a = Math.atan2(dy, dx);
                return r > r0 && r1 > r && a > a0 && a1 > a ? projection.invert(coordinates) : void 0
            }
        }

        function d3_geo_pathAreaRingStart() {
            function nextPoint(x, y) {
                d3_geo_pathAreaPolygon += y0 * x - x0 * y, x0 = x, y0 = y
            }
            var x00, y00, x0, y0;
            d3_geo_pathArea.point = function(x, y) {
                d3_geo_pathArea.point = nextPoint, x00 = x0 = x, y00 = y0 = y
            }, d3_geo_pathArea.lineEnd = function() {
                nextPoint(x00, y00)
            }
        }

        function d3_geo_pathBuffer() {
            function point(x, y) {
                buffer.push("M", x, ",", y, pointCircle)
            }

            function pointLineStart(x, y) {
                buffer.push("M", x, ",", y), stream.point = pointLine
            }

            function pointLine(x, y) {
                buffer.push("L", x, ",", y)
            }

            function lineEnd() {
                stream.point = point
            }

            function lineEndPolygon() {
                buffer.push("Z")
            }
            var pointCircle = d3_geo_pathCircle(4.5),
                buffer = [],
                stream = {
                    point: point,
                    lineStart: function() {
                        stream.point = pointLineStart
                    },
                    lineEnd: lineEnd,
                    polygonStart: function() {
                        stream.lineEnd = lineEndPolygon
                    },
                    polygonEnd: function() {
                        stream.lineEnd = lineEnd, stream.point = point
                    },
                    pointRadius: function(_) {
                        return pointCircle = d3_geo_pathCircle(_), stream
                    },
                    result: function() {
                        if (buffer.length) {
                            var result = buffer.join("");
                            return buffer = [], result
                        }
                    }
                };
            return stream
        }

        function d3_geo_pathCentroidPoint(x, y) {
            d3_geo_centroidDimension || (d3_geo_centroidX += x, d3_geo_centroidY += y, ++d3_geo_centroidZ)
        }

        function d3_geo_pathCentroidLineStart() {
            function nextPoint(x, y) {
                var dx = x - x0,
                    dy = y - y0,
                    z = Math.sqrt(dx * dx + dy * dy);
                d3_geo_centroidX += z * (x0 + x) / 2, d3_geo_centroidY += z * (y0 + y) / 2, d3_geo_centroidZ += z, x0 = x, y0 = y
            }
            var x0, y0;
            if (1 !== d3_geo_centroidDimension) {
                if (!(1 > d3_geo_centroidDimension)) return;
                d3_geo_centroidDimension = 1, d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0
            }
            d3_geo_pathCentroid.point = function(x, y) {
                d3_geo_pathCentroid.point = nextPoint, x0 = x, y0 = y
            }
        }

        function d3_geo_pathCentroidLineEnd() {
            d3_geo_pathCentroid.point = d3_geo_pathCentroidPoint
        }

        function d3_geo_pathCentroidRingStart() {
            function nextPoint(x, y) {
                var z = y0 * x - x0 * y;
                d3_geo_centroidX += z * (x0 + x), d3_geo_centroidY += z * (y0 + y), d3_geo_centroidZ += 3 * z, x0 = x, y0 = y
            }
            var x00, y00, x0, y0;
            2 > d3_geo_centroidDimension && (d3_geo_centroidDimension = 2, d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0), d3_geo_pathCentroid.point = function(x, y) {
                d3_geo_pathCentroid.point = nextPoint, x00 = x0 = x, y00 = y0 = y
            }, d3_geo_pathCentroid.lineEnd = function() {
                nextPoint(x00, y00)
            }
        }

        function d3_geo_pathContext(context) {
            function point(x, y) {
                context.moveTo(x, y), context.arc(x, y, pointRadius, 0, 2 * π)
            }

            function pointLineStart(x, y) {
                context.moveTo(x, y), stream.point = pointLine
            }

            function pointLine(x, y) {
                context.lineTo(x, y)
            }

            function lineEnd() {
                stream.point = point
            }

            function lineEndPolygon() {
                context.closePath()
            }
            var pointRadius = 4.5,
                stream = {
                    point: point,
                    lineStart: function() {
                        stream.point = pointLineStart
                    },
                    lineEnd: lineEnd,
                    polygonStart: function() {
                        stream.lineEnd = lineEndPolygon
                    },
                    polygonEnd: function() {
                        stream.lineEnd = lineEnd, stream.point = point
                    },
                    pointRadius: function(_) {
                        return pointRadius = _, stream
                    },
                    result: d3_noop
                };
            return stream
        }

        function d3_geo_pathCircle(radius) {
            return "m0," + radius + "a" + radius + "," + radius + " 0 1,1 0," + -2 * radius + "a" + radius + "," + radius + " 0 1,1 0," + 2 * radius + "z"
        }

        function d3_geo_pathProjectStream(project) {
            var resample = d3_geo_resample(function(λ, φ) {
                return project([λ * d3_degrees, φ * d3_degrees])
            });
            return function(stream) {
                return stream = resample(stream), {
                    point: function(λ, φ) {
                        stream.point(λ * d3_radians, φ * d3_radians)
                    },
                    sphere: function() {
                        stream.sphere()
                    },
                    lineStart: function() {
                        stream.lineStart()
                    },
                    lineEnd: function() {
                        stream.lineEnd()
                    },
                    polygonStart: function() {
                        stream.polygonStart()
                    },
                    polygonEnd: function() {
                        stream.polygonEnd()
                    }
                }
            }
        }

        function d3_geo_azimuthal(scale, angle) {
            function azimuthal(λ, φ) {
                var cosλ = Math.cos(λ),
                    cosφ = Math.cos(φ),
                    k = scale(cosλ * cosφ);
                return [k * cosφ * Math.sin(λ), k * Math.sin(φ)]
            }
            return azimuthal.invert = function(x, y) {
                var ρ = Math.sqrt(x * x + y * y),
                    c = angle(ρ),
                    sinc = Math.sin(c),
                    cosc = Math.cos(c);
                return [Math.atan2(x * sinc, ρ * cosc), Math.asin(ρ && y * sinc / ρ)]
            }, azimuthal
        }

        function d3_geo_conicConformal(φ0, φ1) {
            function forward(λ, φ) {
                var ρ = Math.abs(Math.abs(φ) - π / 2) < ε ? 0 : F / Math.pow(t(φ), n);
                return [ρ * Math.sin(n * λ), F - ρ * Math.cos(n * λ)]
            }
            var cosφ0 = Math.cos(φ0),
                t = function(φ) {
                    return Math.tan(π / 4 + φ / 2)
                },
                n = φ0 === φ1 ? Math.sin(φ0) : Math.log(cosφ0 / Math.cos(φ1)) / Math.log(t(φ1) / t(φ0)),
                F = cosφ0 * Math.pow(t(φ0), n) / n;
            return n ? (forward.invert = function(x, y) {
                var ρ0_y = F - y,
                    ρ = d3_sgn(n) * Math.sqrt(x * x + ρ0_y * ρ0_y);
                return [Math.atan2(x, ρ0_y) / n, 2 * Math.atan(Math.pow(F / ρ, 1 / n)) - π / 2]
            }, forward) : d3_geo_mercator
        }

        function d3_geo_conicEquidistant(φ0, φ1) {
            function forward(λ, φ) {
                var ρ = G - φ;
                return [ρ * Math.sin(n * λ), G - ρ * Math.cos(n * λ)]
            }
            var cosφ0 = Math.cos(φ0),
                n = φ0 === φ1 ? Math.sin(φ0) : (cosφ0 - Math.cos(φ1)) / (φ1 - φ0),
                G = cosφ0 / n + φ0;
            return Math.abs(n) < ε ? d3_geo_equirectangular : (forward.invert = function(x, y) {
                var ρ0_y = G - y;
                return [Math.atan2(x, ρ0_y) / n, G - d3_sgn(n) * Math.sqrt(x * x + ρ0_y * ρ0_y)]
            }, forward)
        }

        function d3_geo_mercator(λ, φ) {
            return [λ, Math.log(Math.tan(π / 4 + φ / 2))]
        }

        function d3_geo_mercatorProjection(project) {
            var clipAuto, m = d3_geo_projection(project),
                scale = m.scale,
                translate = m.translate,
                clipExtent = m.clipExtent;
            return m.scale = function() {
                var v = scale.apply(m, arguments);
                return v === m ? clipAuto ? m.clipExtent(null) : m : v
            }, m.translate = function() {
                var v = translate.apply(m, arguments);
                return v === m ? clipAuto ? m.clipExtent(null) : m : v
            }, m.clipExtent = function(_) {
                var v = clipExtent.apply(m, arguments);
                if (v === m) {
                    if (clipAuto = null == _) {
                        var k = π * scale(),
                            t = translate();
                        clipExtent([
                            [t[0] - k, t[1] - k],
                            [t[0] + k, t[1] + k]
                        ])
                    }
                } else clipAuto && (v = null);
                return v
            }, m.clipExtent(null)
        }

        function d3_geo_transverseMercator(λ, φ) {
            var B = Math.cos(φ) * Math.sin(λ);
            return [Math.log((1 + B) / (1 - B)) / 2, Math.atan2(Math.tan(φ), Math.cos(λ))]
        }

        function d3_svg_line(projection) {
            function line(data) {
                function segment() {
                    segments.push("M", interpolate(projection(points), tension))
                }
                for (var d, segments = [], points = [], i = -1, n = data.length, fx = d3_functor(x), fy = d3_functor(y); ++i < n;) defined.call(this, d = data[i], i) ? points.push([+fx.call(this, d, i), +fy.call(this, d, i)]) : points.length && (segment(), points = []);
                return points.length && segment(), segments.length ? segments.join("") : null
            }
            var x = d3_svg_lineX,
                y = d3_svg_lineY,
                defined = d3_true,
                interpolate = d3_svg_lineLinear,
                interpolateKey = interpolate.key,
                tension = .7;
            return line.x = function(_) {
                return arguments.length ? (x = _, line) : x
            }, line.y = function(_) {
                return arguments.length ? (y = _, line) : y
            }, line.defined = function(_) {
                return arguments.length ? (defined = _, line) : defined
            }, line.interpolate = function(_) {
                return arguments.length ? (interpolateKey = "function" == typeof _ ? interpolate = _ : (interpolate = d3_svg_lineInterpolators.get(_) || d3_svg_lineLinear).key, line) : interpolateKey
            }, line.tension = function(_) {
                return arguments.length ? (tension = _, line) : tension
            }, line
        }

        function d3_svg_lineX(d) {
            return d[0]
        }

        function d3_svg_lineY(d) {
            return d[1]
        }

        function d3_svg_lineLinear(points) {
            return points.join("L")
        }

        function d3_svg_lineLinearClosed(points) {
            return d3_svg_lineLinear(points) + "Z"
        }

        function d3_svg_lineStepBefore(points) {
            for (var i = 0, n = points.length, p = points[0], path = [p[0], ",", p[1]]; ++i < n;) path.push("V", (p = points[i])[1], "H", p[0]);
            return path.join("")
        }

        function d3_svg_lineStepAfter(points) {
            for (var i = 0, n = points.length, p = points[0], path = [p[0], ",", p[1]]; ++i < n;) path.push("H", (p = points[i])[0], "V", p[1]);
            return path.join("")
        }

        function d3_svg_lineCardinalOpen(points, tension) {
            return points.length < 4 ? d3_svg_lineLinear(points) : points[1] + d3_svg_lineHermite(points.slice(1, points.length - 1), d3_svg_lineCardinalTangents(points, tension))
        }

        function d3_svg_lineCardinalClosed(points, tension) {
            return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite((points.push(points[0]), points), d3_svg_lineCardinalTangents([points[points.length - 2]].concat(points, [points[1]]), tension))
        }

        function d3_svg_lineCardinal(points, tension) {
            return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite(points, d3_svg_lineCardinalTangents(points, tension))
        }

        function d3_svg_lineHermite(points, tangents) {
            if (tangents.length < 1 || points.length != tangents.length && points.length != tangents.length + 2) return d3_svg_lineLinear(points);
            var quad = points.length != tangents.length,
                path = "",
                p0 = points[0],
                p = points[1],
                t0 = tangents[0],
                t = t0,
                pi = 1;
            if (quad && (path += "Q" + (p[0] - 2 * t0[0] / 3) + "," + (p[1] - 2 * t0[1] / 3) + "," + p[0] + "," + p[1], p0 = points[1], pi = 2), tangents.length > 1) {
                t = tangents[1], p = points[pi], pi++, path += "C" + (p0[0] + t0[0]) + "," + (p0[1] + t0[1]) + "," + (p[0] - t[0]) + "," + (p[1] - t[1]) + "," + p[0] + "," + p[1];
                for (var i = 2; i < tangents.length; i++, pi++) p = points[pi], t = tangents[i], path += "S" + (p[0] - t[0]) + "," + (p[1] - t[1]) + "," + p[0] + "," + p[1]
            }
            if (quad) {
                var lp = points[pi];
                path += "Q" + (p[0] + 2 * t[0] / 3) + "," + (p[1] + 2 * t[1] / 3) + "," + lp[0] + "," + lp[1]
            }
            return path
        }

        function d3_svg_lineCardinalTangents(points, tension) {
            for (var p0, tangents = [], a = (1 - tension) / 2, p1 = points[0], p2 = points[1], i = 1, n = points.length; ++i < n;) p0 = p1, p1 = p2, p2 = points[i], tangents.push([a * (p2[0] - p0[0]), a * (p2[1] - p0[1])]);
            return tangents
        }

        function d3_svg_lineBasis(points) {
            if (points.length < 3) return d3_svg_lineLinear(points);
            var i = 1,
                n = points.length,
                pi = points[0],
                x0 = pi[0],
                y0 = pi[1],
                px = [x0, x0, x0, (pi = points[1])[0]],
                py = [y0, y0, y0, pi[1]],
                path = [x0, ",", y0];
            for (d3_svg_lineBasisBezier(path, px, py); ++i < n;) pi = points[i], px.shift(), px.push(pi[0]), py.shift(), py.push(pi[1]), d3_svg_lineBasisBezier(path, px, py);
            for (i = -1; ++i < 2;) px.shift(), px.push(pi[0]), py.shift(), py.push(pi[1]), d3_svg_lineBasisBezier(path, px, py);
            return path.join("")
        }

        function d3_svg_lineBasisOpen(points) {
            if (points.length < 4) return d3_svg_lineLinear(points);
            for (var pi, path = [], i = -1, n = points.length, px = [0], py = [0]; ++i < 3;) pi = points[i], px.push(pi[0]), py.push(pi[1]);
            for (path.push(d3_svg_lineDot4(d3_svg_lineBasisBezier3, px) + "," + d3_svg_lineDot4(d3_svg_lineBasisBezier3, py)), --i; ++i < n;) pi = points[i], px.shift(), px.push(pi[0]), py.shift(), py.push(pi[1]), d3_svg_lineBasisBezier(path, px, py);
            return path.join("")
        }

        function d3_svg_lineBasisClosed(points) {
            for (var path, pi, i = -1, n = points.length, m = n + 4, px = [], py = []; ++i < 4;) pi = points[i % n], px.push(pi[0]), py.push(pi[1]);
            for (path = [d3_svg_lineDot4(d3_svg_lineBasisBezier3, px), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, py)], --i; ++i < m;) pi = points[i % n], px.shift(), px.push(pi[0]), py.shift(), py.push(pi[1]), d3_svg_lineBasisBezier(path, px, py);
            return path.join("")
        }

        function d3_svg_lineBundle(points, tension) {
            var n = points.length - 1;
            if (n)
                for (var p, t, x0 = points[0][0], y0 = points[0][1], dx = points[n][0] - x0, dy = points[n][1] - y0, i = -1; ++i <= n;) p = points[i], t = i / n, p[0] = tension * p[0] + (1 - tension) * (x0 + t * dx), p[1] = tension * p[1] + (1 - tension) * (y0 + t * dy);
            return d3_svg_lineBasis(points)
        }

        function d3_svg_lineDot4(a, b) {
            return a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3]
        }

        function d3_svg_lineBasisBezier(path, x, y) {
            path.push("C", d3_svg_lineDot4(d3_svg_lineBasisBezier1, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier1, y), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier2, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier2, y), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, x), ",", d3_svg_lineDot4(d3_svg_lineBasisBezier3, y))
        }

        function d3_svg_lineSlope(p0, p1) {
            return (p1[1] - p0[1]) / (p1[0] - p0[0])
        }

        function d3_svg_lineFiniteDifferences(points) {
            for (var i = 0, j = points.length - 1, m = [], p0 = points[0], p1 = points[1], d = m[0] = d3_svg_lineSlope(p0, p1); ++i < j;) m[i] = (d + (d = d3_svg_lineSlope(p0 = p1, p1 = points[i + 1]))) / 2;
            return m[i] = d, m
        }

        function d3_svg_lineMonotoneTangents(points) {
            for (var d, a, b, s, tangents = [], m = d3_svg_lineFiniteDifferences(points), i = -1, j = points.length - 1; ++i < j;) d = d3_svg_lineSlope(points[i], points[i + 1]), Math.abs(d) < 1e-6 ? m[i] = m[i + 1] = 0 : (a = m[i] / d, b = m[i + 1] / d, s = a * a + b * b, s > 9 && (s = 3 * d / Math.sqrt(s), m[i] = s * a, m[i + 1] = s * b));
            for (i = -1; ++i <= j;) s = (points[Math.min(j, i + 1)][0] - points[Math.max(0, i - 1)][0]) / (6 * (1 + m[i] * m[i])), tangents.push([s || 0, m[i] * s || 0]);
            return tangents
        }

        function d3_svg_lineMonotone(points) {
            return points.length < 3 ? d3_svg_lineLinear(points) : points[0] + d3_svg_lineHermite(points, d3_svg_lineMonotoneTangents(points))
        }

        function d3_geom_hullCCW(i1, i2, i3, v) {
            var t, a, b, c, d, e, f;
            return t = v[i1], a = t[0], b = t[1], t = v[i2], c = t[0], d = t[1], t = v[i3], e = t[0], f = t[1], (f - b) * (c - a) - (d - b) * (e - a) > 0
        }

        function d3_geom_polygonInside(p, a, b) {
            return (b[0] - a[0]) * (p[1] - a[1]) < (b[1] - a[1]) * (p[0] - a[0])
        }

        function d3_geom_polygonIntersect(c, d, a, b) {
            var x1 = c[0],
                x3 = a[0],
                x21 = d[0] - x1,
                x43 = b[0] - x3,
                y1 = c[1],
                y3 = a[1],
                y21 = d[1] - y1,
                y43 = b[1] - y3,
                ua = (x43 * (y1 - y3) - y43 * (x1 - x3)) / (y43 * x21 - x43 * y21);
            return [x1 + ua * x21, y1 + ua * y21]
        }

        function d3_geom_voronoiTessellate(points, callback) {
            var Sites = {
                    list: points.map(function(v, i) {
                        return {
                            index: i,
                            x: v[0],
                            y: v[1]
                        }
                    }).sort(function(a, b) {
                        return a.y < b.y ? -1 : a.y > b.y ? 1 : a.x < b.x ? -1 : a.x > b.x ? 1 : 0
                    }),
                    bottomSite: null
                },
                EdgeList = {
                    list: [],
                    leftEnd: null,
                    rightEnd: null,
                    init: function() {
                        EdgeList.leftEnd = EdgeList.createHalfEdge(null, "l"), EdgeList.rightEnd = EdgeList.createHalfEdge(null, "l"), EdgeList.leftEnd.r = EdgeList.rightEnd, EdgeList.rightEnd.l = EdgeList.leftEnd, EdgeList.list.unshift(EdgeList.leftEnd, EdgeList.rightEnd)
                    },
                    createHalfEdge: function(edge, side) {
                        return {
                            edge: edge,
                            side: side,
                            vertex: null,
                            l: null,
                            r: null
                        }
                    },
                    insert: function(lb, he) {
                        he.l = lb, he.r = lb.r, lb.r.l = he, lb.r = he
                    },
                    leftBound: function(p) {
                        var he = EdgeList.leftEnd;
                        do he = he.r; while (he != EdgeList.rightEnd && Geom.rightOf(he, p));
                        return he = he.l
                    },
                    del: function(he) {
                        he.l.r = he.r, he.r.l = he.l, he.edge = null
                    },
                    right: function(he) {
                        return he.r
                    },
                    left: function(he) {
                        return he.l
                    },
                    leftRegion: function(he) {
                        return null == he.edge ? Sites.bottomSite : he.edge.region[he.side]
                    },
                    rightRegion: function(he) {
                        return null == he.edge ? Sites.bottomSite : he.edge.region[d3_geom_voronoiOpposite[he.side]]
                    }
                },
                Geom = {
                    bisect: function(s1, s2) {
                        var newEdge = {
                                region: {
                                    l: s1,
                                    r: s2
                                },
                                ep: {
                                    l: null,
                                    r: null
                                }
                            },
                            dx = s2.x - s1.x,
                            dy = s2.y - s1.y,
                            adx = dx > 0 ? dx : -dx,
                            ady = dy > 0 ? dy : -dy;
                        return newEdge.c = s1.x * dx + s1.y * dy + .5 * (dx * dx + dy * dy), adx > ady ? (newEdge.a = 1, newEdge.b = dy / dx, newEdge.c /= dx) : (newEdge.b = 1, newEdge.a = dx / dy, newEdge.c /= dy), newEdge
                    },
                    intersect: function(el1, el2) {
                        var e1 = el1.edge,
                            e2 = el2.edge;
                        if (!e1 || !e2 || e1.region.r == e2.region.r) return null;
                        var d = e1.a * e2.b - e1.b * e2.a;
                        if (Math.abs(d) < 1e-10) return null;
                        var el, e, xint = (e1.c * e2.b - e2.c * e1.b) / d,
                            yint = (e2.c * e1.a - e1.c * e2.a) / d,
                            e1r = e1.region.r,
                            e2r = e2.region.r;
                        e1r.y < e2r.y || e1r.y == e2r.y && e1r.x < e2r.x ? (el = el1, e = e1) : (el = el2, e = e2);
                        var rightOfSite = xint >= e.region.r.x;
                        return rightOfSite && "l" === el.side || !rightOfSite && "r" === el.side ? null : {
                            x: xint,
                            y: yint
                        }
                    },
                    rightOf: function(he, p) {
                        var e = he.edge,
                            topsite = e.region.r,
                            rightOfSite = p.x > topsite.x;
                        if (rightOfSite && "l" === he.side) return 1;
                        if (!rightOfSite && "r" === he.side) return 0;
                        if (1 === e.a) {
                            var dyp = p.y - topsite.y,
                                dxp = p.x - topsite.x,
                                fast = 0,
                                above = 0;
                            if (!rightOfSite && e.b < 0 || rightOfSite && e.b >= 0 ? above = fast = dyp >= e.b * dxp : (above = p.x + p.y * e.b > e.c, e.b < 0 && (above = !above), above || (fast = 1)), !fast) {
                                var dxs = topsite.x - e.region.l.x;
                                above = e.b * (dxp * dxp - dyp * dyp) < dxs * dyp * (1 + 2 * dxp / dxs + e.b * e.b), e.b < 0 && (above = !above)
                            }
                        } else {
                            var yl = e.c - e.a * p.x,
                                t1 = p.y - yl,
                                t2 = p.x - topsite.x,
                                t3 = yl - topsite.y;
                            above = t1 * t1 > t2 * t2 + t3 * t3
                        }
                        return "l" === he.side ? above : !above
                    },
                    endPoint: function(edge, side, site) {
                        edge.ep[side] = site, edge.ep[d3_geom_voronoiOpposite[side]] && callback(edge)
                    },
                    distance: function(s, t) {
                        var dx = s.x - t.x,
                            dy = s.y - t.y;
                        return Math.sqrt(dx * dx + dy * dy)
                    }
                },
                EventQueue = {
                    list: [],
                    insert: function(he, site, offset) {
                        he.vertex = site, he.ystar = site.y + offset;
                        for (var i = 0, list = EventQueue.list, l = list.length; l > i; i++) {
                            var next = list[i];
                            if (!(he.ystar > next.ystar || he.ystar == next.ystar && site.x > next.vertex.x)) break
                        }
                        list.splice(i, 0, he)
                    },
                    del: function(he) {
                        for (var i = 0, ls = EventQueue.list, l = ls.length; l > i && ls[i] != he; ++i);
                        ls.splice(i, 1)
                    },
                    empty: function() {
                        return 0 === EventQueue.list.length
                    },
                    nextEvent: function(he) {
                        for (var i = 0, ls = EventQueue.list, l = ls.length; l > i; ++i)
                            if (ls[i] == he) return ls[i + 1];
                        return null
                    },
                    min: function() {
                        var elem = EventQueue.list[0];
                        return {
                            x: elem.vertex.x,
                            y: elem.ystar
                        }
                    },
                    extractMin: function() {
                        return EventQueue.list.shift()
                    }
                };
            EdgeList.init(), Sites.bottomSite = Sites.list.shift();
            for (var newIntStar, lbnd, rbnd, llbnd, rrbnd, bisector, bot, top, temp, p, v, e, pm, newSite = Sites.list.shift();;)
                if (EventQueue.empty() || (newIntStar = EventQueue.min()), newSite && (EventQueue.empty() || newSite.y < newIntStar.y || newSite.y == newIntStar.y && newSite.x < newIntStar.x)) lbnd = EdgeList.leftBound(newSite), rbnd = EdgeList.right(lbnd), bot = EdgeList.rightRegion(lbnd), e = Geom.bisect(bot, newSite), bisector = EdgeList.createHalfEdge(e, "l"), EdgeList.insert(lbnd, bisector), p = Geom.intersect(lbnd, bisector), p && (EventQueue.del(lbnd), EventQueue.insert(lbnd, p, Geom.distance(p, newSite))), lbnd = bisector, bisector = EdgeList.createHalfEdge(e, "r"), EdgeList.insert(lbnd, bisector), p = Geom.intersect(bisector, rbnd), p && EventQueue.insert(bisector, p, Geom.distance(p, newSite)), newSite = Sites.list.shift();
                else {
                    if (EventQueue.empty()) break;
                    lbnd = EventQueue.extractMin(), llbnd = EdgeList.left(lbnd), rbnd = EdgeList.right(lbnd), rrbnd = EdgeList.right(rbnd), bot = EdgeList.leftRegion(lbnd), top = EdgeList.rightRegion(rbnd), v = lbnd.vertex, Geom.endPoint(lbnd.edge, lbnd.side, v), Geom.endPoint(rbnd.edge, rbnd.side, v), EdgeList.del(lbnd), EventQueue.del(rbnd), EdgeList.del(rbnd), pm = "l", bot.y > top.y && (temp = bot, bot = top, top = temp, pm = "r"), e = Geom.bisect(bot, top), bisector = EdgeList.createHalfEdge(e, pm), EdgeList.insert(llbnd, bisector), Geom.endPoint(e, d3_geom_voronoiOpposite[pm], v), p = Geom.intersect(llbnd, bisector), p && (EventQueue.del(llbnd), EventQueue.insert(llbnd, p, Geom.distance(p, bot))), p = Geom.intersect(bisector, rrbnd), p && EventQueue.insert(bisector, p, Geom.distance(p, bot))
                }
            for (lbnd = EdgeList.right(EdgeList.leftEnd); lbnd != EdgeList.rightEnd; lbnd = EdgeList.right(lbnd)) callback(lbnd.edge)
        }

        function d3_geom_quadtreeCompatX(d) {
            return d.x
        }

        function d3_geom_quadtreeCompatY(d) {
            return d.y
        }

        function d3_geom_quadtreeNode() {
            return {
                leaf: !0,
                nodes: [],
                point: null,
                x: null,
                y: null
            }
        }

        function d3_geom_quadtreeVisit(f, node, x1, y1, x2, y2) {
            if (!f(node, x1, y1, x2, y2)) {
                var sx = .5 * (x1 + x2),
                    sy = .5 * (y1 + y2),
                    children = node.nodes;
                children[0] && d3_geom_quadtreeVisit(f, children[0], x1, y1, sx, sy), children[1] && d3_geom_quadtreeVisit(f, children[1], sx, y1, x2, sy), children[2] && d3_geom_quadtreeVisit(f, children[2], x1, sy, sx, y2), children[3] && d3_geom_quadtreeVisit(f, children[3], sx, sy, x2, y2)
            }
        }

        function d3_interpolateRgb(a, b) {
            a = d3.rgb(a), b = d3.rgb(b);
            var ar = a.r,
                ag = a.g,
                ab = a.b,
                br = b.r - ar,
                bg = b.g - ag,
                bb = b.b - ab;
            return function(t) {
                return "#" + d3_rgb_hex(Math.round(ar + br * t)) + d3_rgb_hex(Math.round(ag + bg * t)) + d3_rgb_hex(Math.round(ab + bb * t))
            }
        }

        function d3_transform(m) {
            var r0 = [m.a, m.b],
                r1 = [m.c, m.d],
                kx = d3_transformNormalize(r0),
                kz = d3_transformDot(r0, r1),
                ky = d3_transformNormalize(d3_transformCombine(r1, r0, -kz)) || 0;
            r0[0] * r1[1] < r1[0] * r0[1] && (r0[0] *= -1, r0[1] *= -1, kx *= -1, kz *= -1), this.rotate = (kx ? Math.atan2(r0[1], r0[0]) : Math.atan2(-r1[0], r1[1])) * d3_degrees, this.translate = [m.e, m.f], this.scale = [kx, ky], this.skew = ky ? Math.atan2(kz, ky) * d3_degrees : 0
        }

        function d3_transformDot(a, b) {
            return a[0] * b[0] + a[1] * b[1]
        }

        function d3_transformNormalize(a) {
            var k = Math.sqrt(d3_transformDot(a, a));
            return k && (a[0] /= k, a[1] /= k), k
        }

        function d3_transformCombine(a, b, k) {
            return a[0] += k * b[0], a[1] += k * b[1], a
        }

        function d3_interpolateNumber(a, b) {
            return b -= a = +a,
                function(t) {
                    return a + b * t
                }
        }

        function d3_interpolateTransform(a, b) {
            var n, s = [],
                q = [],
                A = d3.transform(a),
                B = d3.transform(b),
                ta = A.translate,
                tb = B.translate,
                ra = A.rotate,
                rb = B.rotate,
                wa = A.skew,
                wb = B.skew,
                ka = A.scale,
                kb = B.scale;
            return ta[0] != tb[0] || ta[1] != tb[1] ? (s.push("translate(", null, ",", null, ")"), q.push({
                    i: 1,
                    x: d3_interpolateNumber(ta[0], tb[0])
                }, {
                    i: 3,
                    x: d3_interpolateNumber(ta[1], tb[1])
                })) : s.push(tb[0] || tb[1] ? "translate(" + tb + ")" : ""), ra != rb ? (ra - rb > 180 ? rb += 360 : rb - ra > 180 && (ra += 360), q.push({
                    i: s.push(s.pop() + "rotate(", null, ")") - 2,
                    x: d3_interpolateNumber(ra, rb)
                })) : rb && s.push(s.pop() + "rotate(" + rb + ")"), wa != wb ? q.push({
                    i: s.push(s.pop() + "skewX(", null, ")") - 2,
                    x: d3_interpolateNumber(wa, wb)
                }) : wb && s.push(s.pop() + "skewX(" + wb + ")"), ka[0] != kb[0] || ka[1] != kb[1] ? (n = s.push(s.pop() + "scale(", null, ",", null, ")"), q.push({
                    i: n - 4,
                    x: d3_interpolateNumber(ka[0], kb[0])
                }, {
                    i: n - 2,
                    x: d3_interpolateNumber(ka[1], kb[1])
                })) : (1 != kb[0] || 1 != kb[1]) && s.push(s.pop() + "scale(" + kb + ")"), n = q.length,
                function(t) {
                    for (var o, i = -1; ++i < n;) s[(o = q[i]).i] = o.x(t);
                    return s.join("")
                }
        }

        function d3_interpolateObject(a, b) {
            var k, i = {},
                c = {};
            for (k in a) k in b ? i[k] = d3_interpolateByName(k)(a[k], b[k]) : c[k] = a[k];
            for (k in b) k in a || (c[k] = b[k]);
            return function(t) {
                for (k in i) c[k] = i[k](t);
                return c
            }
        }

        function d3_interpolateString(a, b) {
            var m, i, j, n, o, s0 = 0,
                s1 = 0,
                s = [],
                q = [];
            for (a += "", b += "", d3_interpolate_number.lastIndex = 0, i = 0; m = d3_interpolate_number.exec(b); ++i) m.index && s.push(b.substring(s0, s1 = m.index)), q.push({
                i: s.length,
                x: m[0]
            }), s.push(null), s0 = d3_interpolate_number.lastIndex;
            for (s0 < b.length && s.push(b.substring(s0)), i = 0, n = q.length;
                (m = d3_interpolate_number.exec(a)) && n > i; ++i)
                if (o = q[i], o.x == m[0]) {
                    if (o.i)
                        if (null == s[o.i + 1])
                            for (s[o.i - 1] += o.x, s.splice(o.i, 1), j = i + 1; n > j; ++j) q[j].i--;
                        else
                            for (s[o.i - 1] += o.x + s[o.i + 1], s.splice(o.i, 2), j = i + 1; n > j; ++j) q[j].i -= 2;
                    else if (null == s[o.i + 1]) s[o.i] = o.x;
                    else
                        for (s[o.i] = o.x + s[o.i + 1], s.splice(o.i + 1, 1), j = i + 1; n > j; ++j) q[j].i--;
                    q.splice(i, 1), n--, i--
                } else o.x = d3_interpolateNumber(parseFloat(m[0]), parseFloat(o.x));
            for (; n > i;) o = q.pop(), null == s[o.i + 1] ? s[o.i] = o.x : (s[o.i] = o.x + s[o.i + 1], s.splice(o.i + 1, 1)), n--;
            return 1 === s.length ? null == s[0] ? q[0].x : function() {
                return b
            } : function(t) {
                for (i = 0; n > i; ++i) s[(o = q[i]).i] = o.x(t);
                return s.join("")
            }
        }

        function d3_interpolate(a, b) {
            for (var f, i = d3.interpolators.length; --i >= 0 && !(f = d3.interpolators[i](a, b)););
            return f
        }

        function d3_interpolateByName(name) {
            return "transform" == name ? d3_interpolateTransform : d3_interpolate
        }

        function d3_interpolateArray(a, b) {
            var i, x = [],
                c = [],
                na = a.length,
                nb = b.length,
                n0 = Math.min(a.length, b.length);
            for (i = 0; n0 > i; ++i) x.push(d3_interpolate(a[i], b[i]));
            for (; na > i; ++i) c[i] = a[i];
            for (; nb > i; ++i) c[i] = b[i];
            return function(t) {
                for (i = 0; n0 > i; ++i) c[i] = x[i](t);
                return c
            }
        }

        function d3_ease_clamp(f) {
            return function(t) {
                return 0 >= t ? 0 : t >= 1 ? 1 : f(t)
            }
        }

        function d3_ease_reverse(f) {
            return function(t) {
                return 1 - f(1 - t)
            }
        }

        function d3_ease_reflect(f) {
            return function(t) {
                return .5 * (.5 > t ? f(2 * t) : 2 - f(2 - 2 * t))
            }
        }

        function d3_ease_quad(t) {
            return t * t
        }

        function d3_ease_cubic(t) {
            return t * t * t
        }

        function d3_ease_cubicInOut(t) {
            if (0 >= t) return 0;
            if (t >= 1) return 1;
            var t2 = t * t,
                t3 = t2 * t;
            return 4 * (.5 > t ? t3 : 3 * (t - t2) + t3 - .75)
        }

        function d3_ease_poly(e) {
            return function(t) {
                return Math.pow(t, e)
            }
        }

        function d3_ease_sin(t) {
            return 1 - Math.cos(t * π / 2)
        }

        function d3_ease_exp(t) {
            return Math.pow(2, 10 * (t - 1))
        }

        function d3_ease_circle(t) {
            return 1 - Math.sqrt(1 - t * t)
        }

        function d3_ease_elastic(a, p) {
            var s;
            return arguments.length < 2 && (p = .45), arguments.length ? s = p / (2 * π) * Math.asin(1 / a) : (a = 1, s = p / 4),
                function(t) {
                    return 1 + a * Math.pow(2, 10 * -t) * Math.sin(2 * (t - s) * π / p)
                }
        }

        function d3_ease_back(s) {
            return s || (s = 1.70158),
                function(t) {
                    return t * t * ((s + 1) * t - s)
                }
        }

        function d3_ease_bounce(t) {
            return 1 / 2.75 > t ? 7.5625 * t * t : 2 / 2.75 > t ? 7.5625 * (t -= 1.5 / 2.75) * t + .75 : 2.5 / 2.75 > t ? 7.5625 * (t -= 2.25 / 2.75) * t + .9375 : 7.5625 * (t -= 2.625 / 2.75) * t + .984375
        }

        function d3_interpolateHcl(a, b) {
            a = d3.hcl(a), b = d3.hcl(b);
            var ah = a.h,
                ac = a.c,
                al = a.l,
                bh = b.h - ah,
                bc = b.c - ac,
                bl = b.l - al;
            return isNaN(bc) && (bc = 0, ac = isNaN(ac) ? b.c : ac), isNaN(bh) ? (bh = 0, ah = isNaN(ah) ? b.h : ah) : bh > 180 ? bh -= 360 : -180 > bh && (bh += 360),
                function(t) {
                    return d3_hcl_lab(ah + bh * t, ac + bc * t, al + bl * t) + ""
                }
        }

        function d3_interpolateHsl(a, b) {
            a = d3.hsl(a), b = d3.hsl(b);
            var ah = a.h,
                as = a.s,
                al = a.l,
                bh = b.h - ah,
                bs = b.s - as,
                bl = b.l - al;
            return isNaN(bs) && (bs = 0, as = isNaN(as) ? b.s : as), isNaN(bh) ? (bh = 0, ah = isNaN(ah) ? b.h : ah) : bh > 180 ? bh -= 360 : -180 > bh && (bh += 360),
                function(t) {
                    return d3_hsl_rgb(ah + bh * t, as + bs * t, al + bl * t) + ""
                }
        }

        function d3_interpolateLab(a, b) {
            a = d3.lab(a), b = d3.lab(b);
            var al = a.l,
                aa = a.a,
                ab = a.b,
                bl = b.l - al,
                ba = b.a - aa,
                bb = b.b - ab;
            return function(t) {
                return d3_lab_rgb(al + bl * t, aa + ba * t, ab + bb * t) + ""
            }
        }

        function d3_interpolateRound(a, b) {
            return b -= a,
                function(t) {
                    return Math.round(a + b * t)
                }
        }

        function d3_uninterpolateNumber(a, b) {
            return b = b - (a = +a) ? 1 / (b - a) : 0,
                function(x) {
                    return (x - a) * b
                }
        }

        function d3_uninterpolateClamp(a, b) {
            return b = b - (a = +a) ? 1 / (b - a) : 0,
                function(x) {
                    return Math.max(0, Math.min(1, (x - a) * b))
                }
        }

        function d3_layout_bundlePath(link) {
            for (var start = link.source, end = link.target, lca = d3_layout_bundleLeastCommonAncestor(start, end), points = [start]; start !== lca;) start = start.parent, points.push(start);
            for (var k = points.length; end !== lca;) points.splice(k, 0, end), end = end.parent;
            return points
        }

        function d3_layout_bundleAncestors(node) {
            for (var ancestors = [], parent = node.parent; null != parent;) ancestors.push(node), node = parent, parent = parent.parent;
            return ancestors.push(node), ancestors
        }

        function d3_layout_bundleLeastCommonAncestor(a, b) {
            if (a === b) return a;
            for (var aNodes = d3_layout_bundleAncestors(a), bNodes = d3_layout_bundleAncestors(b), aNode = aNodes.pop(), bNode = bNodes.pop(), sharedNode = null; aNode === bNode;) sharedNode = aNode, aNode = aNodes.pop(), bNode = bNodes.pop();
            return sharedNode
        }

        function d3_layout_forceDragstart(d) {
            d.fixed |= 2
        }

        function d3_layout_forceDragend(d) {
            d.fixed &= -7
        }

        function d3_layout_forceMouseover(d) {
            d.fixed |= 4, d.px = d.x, d.py = d.y
        }

        function d3_layout_forceMouseout(d) {
            d.fixed &= -5
        }

        function d3_layout_forceAccumulate(quad, alpha, charges) {
            var cx = 0,
                cy = 0;
            if (quad.charge = 0, !quad.leaf)
                for (var c, nodes = quad.nodes, n = nodes.length, i = -1; ++i < n;) c = nodes[i], null != c && (d3_layout_forceAccumulate(c, alpha, charges), quad.charge += c.charge, cx += c.charge * c.cx, cy += c.charge * c.cy);
            if (quad.point) {
                quad.leaf || (quad.point.x += Math.random() - .5, quad.point.y += Math.random() - .5);
                var k = alpha * charges[quad.point.index];
                quad.charge += quad.pointCharge = k, cx += k * quad.point.x, cy += k * quad.point.y
            }
            quad.cx = cx / quad.charge, quad.cy = cy / quad.charge
        }

        function d3_layout_hierarchyRebind(object, hierarchy) {
            return d3.rebind(object, hierarchy, "sort", "children", "value"), object.nodes = object, object.links = d3_layout_hierarchyLinks, object
        }

        function d3_layout_hierarchyChildren(d) {
            return d.children
        }

        function d3_layout_hierarchyValue(d) {
            return d.value
        }

        function d3_layout_hierarchySort(a, b) {
            return b.value - a.value
        }

        function d3_layout_hierarchyLinks(nodes) {
            return d3.merge(nodes.map(function(parent) {
                return (parent.children || []).map(function(child) {
                    return {
                        source: parent,
                        target: child
                    }
                })
            }))
        }

        function d3_layout_stackX(d) {
            return d.x
        }

        function d3_layout_stackY(d) {
            return d.y
        }

        function d3_layout_stackOut(d, y0, y) {
            d.y0 = y0, d.y = y
        }

        function d3_layout_stackOrderDefault(data) {
            return d3.range(data.length)
        }

        function d3_layout_stackOffsetZero(data) {
            for (var j = -1, m = data[0].length, y0 = []; ++j < m;) y0[j] = 0;
            return y0
        }

        function d3_layout_stackMaxIndex(array) {
            for (var k, i = 1, j = 0, v = array[0][1], n = array.length; n > i; ++i)(k = array[i][1]) > v && (j = i, v = k);
            return j
        }

        function d3_layout_stackReduceSum(d) {
            return d.reduce(d3_layout_stackSum, 0)
        }

        function d3_layout_stackSum(p, d) {
            return p + d[1]
        }

        function d3_layout_histogramBinSturges(range, values) {
            return d3_layout_histogramBinFixed(range, Math.ceil(Math.log(values.length) / Math.LN2 + 1))
        }

        function d3_layout_histogramBinFixed(range, n) {
            for (var x = -1, b = +range[0], m = (range[1] - b) / n, f = []; ++x <= n;) f[x] = m * x + b;
            return f
        }

        function d3_layout_histogramRange(values) {
            return [d3.min(values), d3.max(values)]
        }

        function d3_layout_treeSeparation(a, b) {
            return a.parent == b.parent ? 1 : 2
        }

        function d3_layout_treeLeft(node) {
            var children = node.children;
            return children && children.length ? children[0] : node._tree.thread
        }

        function d3_layout_treeRight(node) {
            var n, children = node.children;
            return children && (n = children.length) ? children[n - 1] : node._tree.thread
        }

        function d3_layout_treeSearch(node, compare) {
            var children = node.children;
            if (children && (n = children.length))
                for (var child, n, i = -1; ++i < n;) compare(child = d3_layout_treeSearch(children[i], compare), node) > 0 && (node = child);
            return node
        }

        function d3_layout_treeRightmost(a, b) {
            return a.x - b.x
        }

        function d3_layout_treeLeftmost(a, b) {
            return b.x - a.x
        }

        function d3_layout_treeDeepest(a, b) {
            return a.depth - b.depth
        }

        function d3_layout_treeVisitAfter(node, callback) {
            function visit(node, previousSibling) {
                var children = node.children;
                if (children && (n = children.length))
                    for (var child, n, previousChild = null, i = -1; ++i < n;) child = children[i], visit(child, previousChild), previousChild = child;
                callback(node, previousSibling)
            }
            visit(node, null)
        }

        function d3_layout_treeShift(node) {
            for (var child, shift = 0, change = 0, children = node.children, i = children.length; --i >= 0;) child = children[i]._tree, child.prelim += shift, child.mod += shift, shift += child.shift + (change += child.change)
        }

        function d3_layout_treeMove(ancestor, node, shift) {
            ancestor = ancestor._tree, node = node._tree;
            var change = shift / (node.number - ancestor.number);
            ancestor.change += change, node.change -= change, node.shift += shift, node.prelim += shift, node.mod += shift
        }

        function d3_layout_treeAncestor(vim, node, ancestor) {
            return vim._tree.ancestor.parent == node.parent ? vim._tree.ancestor : ancestor
        }

        function d3_layout_packSort(a, b) {
            return a.value - b.value
        }

        function d3_layout_packInsert(a, b) {
            var c = a._pack_next;
            a._pack_next = b, b._pack_prev = a, b._pack_next = c, c._pack_prev = b
        }

        function d3_layout_packSplice(a, b) {
            a._pack_next = b, b._pack_prev = a
        }

        function d3_layout_packIntersects(a, b) {
            var dx = b.x - a.x,
                dy = b.y - a.y,
                dr = a.r + b.r;
            return dr * dr - dx * dx - dy * dy > .001
        }

        function d3_layout_packSiblings(node) {
            function bound(node) {
                xMin = Math.min(node.x - node.r, xMin), xMax = Math.max(node.x + node.r, xMax), yMin = Math.min(node.y - node.r, yMin), yMax = Math.max(node.y + node.r, yMax)
            }
            if ((nodes = node.children) && (n = nodes.length)) {
                var nodes, a, b, c, i, j, k, n, xMin = 1 / 0,
                    xMax = -(1 / 0),
                    yMin = 1 / 0,
                    yMax = -(1 / 0);
                if (nodes.forEach(d3_layout_packLink), a = nodes[0], a.x = -a.r, a.y = 0, bound(a), n > 1 && (b = nodes[1], b.x = b.r, b.y = 0, bound(b), n > 2))
                    for (c = nodes[2], d3_layout_packPlace(a, b, c), bound(c), d3_layout_packInsert(a, c), a._pack_prev = c, d3_layout_packInsert(c, b), b = a._pack_next, i = 3; n > i; i++) {
                        d3_layout_packPlace(a, b, c = nodes[i]);
                        var isect = 0,
                            s1 = 1,
                            s2 = 1;
                        for (j = b._pack_next; j !== b; j = j._pack_next, s1++)
                            if (d3_layout_packIntersects(j, c)) {
                                isect = 1;
                                break
                            }
                        if (1 == isect)
                            for (k = a._pack_prev; k !== j._pack_prev && !d3_layout_packIntersects(k, c); k = k._pack_prev, s2++);
                        isect ? (s2 > s1 || s1 == s2 && b.r < a.r ? d3_layout_packSplice(a, b = j) : d3_layout_packSplice(a = k, b), i--) : (d3_layout_packInsert(a, c), b = c, bound(c))
                    }
                var cx = (xMin + xMax) / 2,
                    cy = (yMin + yMax) / 2,
                    cr = 0;
                for (i = 0; n > i; i++) c = nodes[i], c.x -= cx, c.y -= cy, cr = Math.max(cr, c.r + Math.sqrt(c.x * c.x + c.y * c.y));
                node.r = cr, nodes.forEach(d3_layout_packUnlink)
            }
        }

        function d3_layout_packLink(node) {
            node._pack_next = node._pack_prev = node
        }

        function d3_layout_packUnlink(node) {
            delete node._pack_next, delete node._pack_prev
        }

        function d3_layout_packTransform(node, x, y, k) {
            var children = node.children;
            if (node.x = x += k * node.x, node.y = y += k * node.y, node.r *= k, children)
                for (var i = -1, n = children.length; ++i < n;) d3_layout_packTransform(children[i], x, y, k)
        }

        function d3_layout_packPlace(a, b, c) {
            var db = a.r + c.r,
                dx = b.x - a.x,
                dy = b.y - a.y;
            if (db && (dx || dy)) {
                var da = b.r + c.r,
                    dc = dx * dx + dy * dy;
                da *= da, db *= db;
                var x = .5 + (db - da) / (2 * dc),
                    y = Math.sqrt(Math.max(0, 2 * da * (db + dc) - (db -= dc) * db - da * da)) / (2 * dc);
                c.x = a.x + x * dx + y * dy, c.y = a.y + x * dy - y * dx
            } else c.x = a.x + db, c.y = a.y
        }

        function d3_layout_clusterY(children) {
            return 1 + d3.max(children, function(child) {
                return child.y
            })
        }

        function d3_layout_clusterX(children) {
            return children.reduce(function(x, child) {
                return x + child.x
            }, 0) / children.length
        }

        function d3_layout_clusterLeft(node) {
            var children = node.children;
            return children && children.length ? d3_layout_clusterLeft(children[0]) : node
        }

        function d3_layout_clusterRight(node) {
            var n, children = node.children;
            return children && (n = children.length) ? d3_layout_clusterRight(children[n - 1]) : node
        }

        function d3_layout_treemapPadNull(node) {
            return {
                x: node.x,
                y: node.y,
                dx: node.dx,
                dy: node.dy
            }
        }

        function d3_layout_treemapPad(node, padding) {
            var x = node.x + padding[3],
                y = node.y + padding[0],
                dx = node.dx - padding[1] - padding[3],
                dy = node.dy - padding[0] - padding[2];
            return 0 > dx && (x += dx / 2, dx = 0), 0 > dy && (y += dy / 2, dy = 0), {
                x: x,
                y: y,
                dx: dx,
                dy: dy
            }
        }

        function d3_scaleExtent(domain) {
            var start = domain[0],
                stop = domain[domain.length - 1];
            return stop > start ? [start, stop] : [stop, start]
        }

        function d3_scaleRange(scale) {
            return scale.rangeExtent ? scale.rangeExtent() : d3_scaleExtent(scale.range())
        }

        function d3_scale_bilinear(domain, range, uninterpolate, interpolate) {
            var u = uninterpolate(domain[0], domain[1]),
                i = interpolate(range[0], range[1]);
            return function(x) {
                return i(u(x))
            }
        }

        function d3_scale_nice(domain, nice) {
            var dx, i0 = 0,
                i1 = domain.length - 1,
                x0 = domain[i0],
                x1 = domain[i1];
            return x0 > x1 && (dx = i0, i0 = i1, i1 = dx, dx = x0, x0 = x1, x1 = dx), (nice = nice(x1 - x0)) && (domain[i0] = nice.floor(x0), domain[i1] = nice.ceil(x1)), domain
        }

        function d3_scale_polylinear(domain, range, uninterpolate, interpolate) {
            var u = [],
                i = [],
                j = 0,
                k = Math.min(domain.length, range.length) - 1;
            for (domain[k] < domain[0] && (domain = domain.slice().reverse(), range = range.slice().reverse()); ++j <= k;) u.push(uninterpolate(domain[j - 1], domain[j])), i.push(interpolate(range[j - 1], range[j]));
            return function(x) {
                var j = d3.bisect(domain, x, 1, k) - 1;
                return i[j](u[j](x))
            }
        }

        function d3_scale_linear(domain, range, interpolate, clamp) {
            function rescale() {
                var linear = Math.min(domain.length, range.length) > 2 ? d3_scale_polylinear : d3_scale_bilinear,
                    uninterpolate = clamp ? d3_uninterpolateClamp : d3_uninterpolateNumber;
                return output = linear(domain, range, uninterpolate, interpolate), input = linear(range, domain, uninterpolate, d3_interpolate), scale
            }

            function scale(x) {
                return output(x)
            }
            var output, input;
            return scale.invert = function(y) {
                return input(y)
            }, scale.domain = function(x) {
                return arguments.length ? (domain = x.map(Number), rescale()) : domain
            }, scale.range = function(x) {
                return arguments.length ? (range = x, rescale()) : range
            }, scale.rangeRound = function(x) {
                return scale.range(x).interpolate(d3_interpolateRound)
            }, scale.clamp = function(x) {
                return arguments.length ? (clamp = x, rescale()) : clamp
            }, scale.interpolate = function(x) {
                return arguments.length ? (interpolate = x, rescale()) : interpolate
            }, scale.ticks = function(m) {
                return d3_scale_linearTicks(domain, m)
            }, scale.tickFormat = function(m, format) {
                return d3_scale_linearTickFormat(domain, m, format)
            }, scale.nice = function() {
                return d3_scale_nice(domain, d3_scale_linearNice), rescale()
            }, scale.copy = function() {
                return d3_scale_linear(domain, range, interpolate, clamp)
            }, rescale()
        }

        function d3_scale_linearRebind(scale, linear) {
            return d3.rebind(scale, linear, "range", "rangeRound", "interpolate", "clamp")
        }

        function d3_scale_linearNice(dx) {
            return dx = Math.pow(10, Math.round(Math.log(dx) / Math.LN10) - 1), dx && {
                floor: function(x) {
                    return Math.floor(x / dx) * dx
                },
                ceil: function(x) {
                    return Math.ceil(x / dx) * dx
                }
            }
        }

        function d3_scale_linearTickRange(domain, m) {
            var extent = d3_scaleExtent(domain),
                span = extent[1] - extent[0],
                step = Math.pow(10, Math.floor(Math.log(span / m) / Math.LN10)),
                err = m / span * step;
            return .15 >= err ? step *= 10 : .35 >= err ? step *= 5 : .75 >= err && (step *= 2), extent[0] = Math.ceil(extent[0] / step) * step, extent[1] = Math.floor(extent[1] / step) * step + .5 * step, extent[2] = step, extent
        }

        function d3_scale_linearTicks(domain, m) {
            return d3.range.apply(d3, d3_scale_linearTickRange(domain, m))
        }

        function d3_scale_linearTickFormat(domain, m, format) {
            var precision = -Math.floor(Math.log(d3_scale_linearTickRange(domain, m)[2]) / Math.LN10 + .01);
            return d3.format(format ? format.replace(d3_format_re, function(a, b, c, d, e, f, g, h, i, j) {
                return [b, c, d, e, f, g, h, i || "." + (precision - 2 * ("%" === j)), j].join("")
            }) : ",." + precision + "f")
        }

        function d3_scale_log(linear, base, log, pow) {
            function scale(x) {
                return linear(log(x))
            }
            return scale.invert = function(x) {
                return pow(linear.invert(x))
            }, scale.domain = function(x) {
                return arguments.length ? (x[0] < 0 ? (log = d3_scale_logn, pow = d3_scale_pown) : (log = d3_scale_logp, pow = d3_scale_powp), linear.domain(x.map(log)), scale) : linear.domain().map(pow)
            }, scale.base = function(_) {
                return arguments.length ? (base = +_, scale) : base
            }, scale.nice = function() {
                return linear.domain(d3_scale_nice(linear.domain(), d3_scale_logNice(base))), scale
            }, scale.ticks = function() {
                var extent = d3_scaleExtent(linear.domain()),
                    ticks = [];
                if (extent.every(isFinite)) {
                    var b = Math.log(base),
                        i = Math.floor(extent[0] / b),
                        j = Math.ceil(extent[1] / b),
                        u = pow(extent[0]),
                        v = pow(extent[1]),
                        n = base % 1 ? 2 : base;
                    if (log === d3_scale_logn)
                        for (ticks.push(-Math.pow(base, -i)); i++ < j;)
                            for (var k = n - 1; k > 0; k--) ticks.push(-Math.pow(base, -i) * k);
                    else {
                        for (; j > i; i++)
                            for (var k = 1; n > k; k++) ticks.push(Math.pow(base, i) * k);
                        ticks.push(Math.pow(base, i))
                    }
                    for (i = 0; ticks[i] < u; i++);
                    for (j = ticks.length; ticks[j - 1] > v; j--);
                    ticks = ticks.slice(i, j)
                }
                return ticks
            }, scale.tickFormat = function(n, format) {
                if (arguments.length < 2 && (format = d3_scale_logFormat), !arguments.length) return format;
                var e, b = Math.log(base),
                    k = Math.max(.1, n / scale.ticks().length),
                    f = log === d3_scale_logn ? (e = -1e-12, Math.floor) : (e = 1e-12, Math.ceil);
                return function(d) {
                    return d / pow(b * f(log(d) / b + e)) <= k ? format(d) : ""
                }
            }, scale.copy = function() {
                return d3_scale_log(linear.copy(), base, log, pow)
            }, d3_scale_linearRebind(scale, linear)
        }

        function d3_scale_logp(x) {
            return Math.log(0 > x ? 0 : x)
        }

        function d3_scale_powp(x) {
            return Math.exp(x)
        }

        function d3_scale_logn(x) {
            return -Math.log(x > 0 ? 0 : -x)
        }

        function d3_scale_pown(x) {
            return -Math.exp(-x)
        }

        function d3_scale_logNice(base) {
            base = Math.log(base);
            var nice = {
                floor: function(x) {
                    return Math.floor(x / base) * base
                },
                ceil: function(x) {
                    return Math.ceil(x / base) * base
                }
            };
            return function() {
                return nice
            }
        }

        function d3_scale_pow(linear, exponent) {
            function scale(x) {
                return linear(powp(x))
            }
            var powp = d3_scale_powPow(exponent),
                powb = d3_scale_powPow(1 / exponent);
            return scale.invert = function(x) {
                return powb(linear.invert(x))
            }, scale.domain = function(x) {
                return arguments.length ? (linear.domain(x.map(powp)), scale) : linear.domain().map(powb)
            }, scale.ticks = function(m) {
                return d3_scale_linearTicks(scale.domain(), m)
            }, scale.tickFormat = function(m, format) {
                return d3_scale_linearTickFormat(scale.domain(), m, format)
            }, scale.nice = function() {
                return scale.domain(d3_scale_nice(scale.domain(), d3_scale_linearNice))
            }, scale.exponent = function(x) {
                if (!arguments.length) return exponent;
                var domain = scale.domain();
                return powp = d3_scale_powPow(exponent = x), powb = d3_scale_powPow(1 / exponent), scale.domain(domain)
            }, scale.copy = function() {
                return d3_scale_pow(linear.copy(), exponent)
            }, d3_scale_linearRebind(scale, linear)
        }

        function d3_scale_powPow(e) {
            return function(x) {
                return 0 > x ? -Math.pow(-x, e) : Math.pow(x, e)
            }
        }

        function d3_scale_ordinal(domain, ranger) {
            function scale(x) {
                return range[((index.get(x) || index.set(x, domain.push(x))) - 1) % range.length]
            }

            function steps(start, step) {
                return d3.range(domain.length).map(function(i) {
                    return start + step * i
                })
            }
            var index, range, rangeBand;
            return scale.domain = function(x) {
                if (!arguments.length) return domain;
                domain = [], index = new d3_Map;
                for (var xi, i = -1, n = x.length; ++i < n;) index.has(xi = x[i]) || index.set(xi, domain.push(xi));
                return scale[ranger.t].apply(scale, ranger.a)
            }, scale.range = function(x) {
                return arguments.length ? (range = x, rangeBand = 0, ranger = {
                    t: "range",
                    a: arguments
                }, scale) : range
            }, scale.rangePoints = function(x, padding) {
                arguments.length < 2 && (padding = 0);
                var start = x[0],
                    stop = x[1],
                    step = (stop - start) / (Math.max(1, domain.length - 1) + padding);
                return range = steps(domain.length < 2 ? (start + stop) / 2 : start + step * padding / 2, step), rangeBand = 0, ranger = {
                    t: "rangePoints",
                    a: arguments
                }, scale
            }, scale.rangeBands = function(x, padding, outerPadding) {
                arguments.length < 2 && (padding = 0), arguments.length < 3 && (outerPadding = padding);
                var reverse = x[1] < x[0],
                    start = x[reverse - 0],
                    stop = x[1 - reverse],
                    step = (stop - start) / (domain.length - padding + 2 * outerPadding);
                return range = steps(start + step * outerPadding, step), reverse && range.reverse(), rangeBand = step * (1 - padding), ranger = {
                    t: "rangeBands",
                    a: arguments
                }, scale
            }, scale.rangeRoundBands = function(x, padding, outerPadding) {
                arguments.length < 2 && (padding = 0), arguments.length < 3 && (outerPadding = padding);
                var reverse = x[1] < x[0],
                    start = x[reverse - 0],
                    stop = x[1 - reverse],
                    step = Math.floor((stop - start) / (domain.length - padding + 2 * outerPadding)),
                    error = stop - start - (domain.length - padding) * step;
                return range = steps(start + Math.round(error / 2), step), reverse && range.reverse(), rangeBand = Math.round(step * (1 - padding)), ranger = {
                    t: "rangeRoundBands",
                    a: arguments
                }, scale
            }, scale.rangeBand = function() {
                return rangeBand
            }, scale.rangeExtent = function() {
                return d3_scaleExtent(ranger.a[0])
            }, scale.copy = function() {
                return d3_scale_ordinal(domain, ranger)
            }, scale.domain(domain)
        }

        function d3_scale_quantile(domain, range) {
            function rescale() {
                var k = 0,
                    q = range.length;
                for (thresholds = []; ++k < q;) thresholds[k - 1] = d3.quantile(domain, k / q);
                return scale
            }

            function scale(x) {
                return isNaN(x = +x) ? 0 / 0 : range[d3.bisect(thresholds, x)]
            }
            var thresholds;
            return scale.domain = function(x) {
                return arguments.length ? (domain = x.filter(function(d) {
                    return !isNaN(d)
                }).sort(d3.ascending), rescale()) : domain
            }, scale.range = function(x) {
                return arguments.length ? (range = x, rescale()) : range
            }, scale.quantiles = function() {
                return thresholds
            }, scale.copy = function() {
                return d3_scale_quantile(domain, range)
            }, rescale()
        }

        function d3_scale_quantize(x0, x1, range) {
            function scale(x) {
                return range[Math.max(0, Math.min(i, Math.floor(kx * (x - x0))))]
            }

            function rescale() {
                return kx = range.length / (x1 - x0), i = range.length - 1, scale
            }
            var kx, i;
            return scale.domain = function(x) {
                return arguments.length ? (x0 = +x[0], x1 = +x[x.length - 1], rescale()) : [x0, x1]
            }, scale.range = function(x) {
                return arguments.length ? (range = x, rescale()) : range
            }, scale.copy = function() {
                return d3_scale_quantize(x0, x1, range)
            }, rescale()
        }

        function d3_scale_threshold(domain, range) {
            function scale(x) {
                return range[d3.bisect(domain, x)]
            }
            return scale.domain = function(_) {
                return arguments.length ? (domain = _, scale) : domain
            }, scale.range = function(_) {
                return arguments.length ? (range = _, scale) : range
            }, scale.copy = function() {
                return d3_scale_threshold(domain, range)
            }, scale
        }

        function d3_scale_identity(domain) {
            function identity(x) {
                return +x
            }
            return identity.invert = identity, identity.domain = identity.range = function(x) {
                return arguments.length ? (domain = x.map(identity), identity) : domain
            }, identity.ticks = function(m) {
                return d3_scale_linearTicks(domain, m)
            }, identity.tickFormat = function(m, format) {
                return d3_scale_linearTickFormat(domain, m, format)
            }, identity.copy = function() {
                return d3_scale_identity(domain)
            }, identity
        }

        function d3_svg_arcInnerRadius(d) {
            return d.innerRadius
        }

        function d3_svg_arcOuterRadius(d) {
            return d.outerRadius
        }

        function d3_svg_arcStartAngle(d) {
            return d.startAngle
        }

        function d3_svg_arcEndAngle(d) {
            return d.endAngle
        }

        function d3_svg_lineRadial(points) {
            for (var point, r, a, i = -1, n = points.length; ++i < n;) point = points[i], r = point[0], a = point[1] + d3_svg_arcOffset, point[0] = r * Math.cos(a), point[1] = r * Math.sin(a);
            return points
        }

        function d3_svg_area(projection) {
            function area(data) {
                function segment() {
                    segments.push("M", interpolate(projection(points1), tension), L, interpolateReverse(projection(points0.reverse()), tension), "Z")
                }
                for (var d, x, y, segments = [], points0 = [], points1 = [], i = -1, n = data.length, fx0 = d3_functor(x0), fy0 = d3_functor(y0), fx1 = x0 === x1 ? function() {
                        return x
                    } : d3_functor(x1), fy1 = y0 === y1 ? function() {
                        return y
                    } : d3_functor(y1); ++i < n;) defined.call(this, d = data[i], i) ? (points0.push([x = +fx0.call(this, d, i), y = +fy0.call(this, d, i)]), points1.push([+fx1.call(this, d, i), +fy1.call(this, d, i)])) : points0.length && (segment(), points0 = [], points1 = []);
                return points0.length && segment(), segments.length ? segments.join("") : null
            }
            var x0 = d3_svg_lineX,
                x1 = d3_svg_lineX,
                y0 = 0,
                y1 = d3_svg_lineY,
                defined = d3_true,
                interpolate = d3_svg_lineLinear,
                interpolateKey = interpolate.key,
                interpolateReverse = interpolate,
                L = "L",
                tension = .7;
            return area.x = function(_) {
                return arguments.length ? (x0 = x1 = _, area) : x1
            }, area.x0 = function(_) {
                return arguments.length ? (x0 = _, area) : x0
            }, area.x1 = function(_) {
                return arguments.length ? (x1 = _, area) : x1
            }, area.y = function(_) {
                return arguments.length ? (y0 = y1 = _, area) : y1
            }, area.y0 = function(_) {
                return arguments.length ? (y0 = _, area) : y0
            }, area.y1 = function(_) {
                return arguments.length ? (y1 = _, area) : y1
            }, area.defined = function(_) {
                return arguments.length ? (defined = _, area) : defined
            }, area.interpolate = function(_) {
                return arguments.length ? (interpolateKey = "function" == typeof _ ? interpolate = _ : (interpolate = d3_svg_lineInterpolators.get(_) || d3_svg_lineLinear).key, interpolateReverse = interpolate.reverse || interpolate, L = interpolate.closed ? "M" : "L", area) : interpolateKey
            }, area.tension = function(_) {
                return arguments.length ? (tension = _, area) : tension
            }, area
        }

        function d3_svg_chordRadius(d) {
            return d.radius
        }

        function d3_svg_diagonalProjection(d) {
            return [d.x, d.y]
        }

        function d3_svg_diagonalRadialProjection(projection) {
            return function() {
                var d = projection.apply(this, arguments),
                    r = d[0],
                    a = d[1] + d3_svg_arcOffset;
                return [r * Math.cos(a), r * Math.sin(a)]
            }
        }

        function d3_svg_symbolSize() {
            return 64
        }

        function d3_svg_symbolType() {
            return "circle"
        }

        function d3_svg_symbolCircle(size) {
            var r = Math.sqrt(size / π);
            return "M0," + r + "A" + r + "," + r + " 0 1,1 0," + -r + "A" + r + "," + r + " 0 1,1 0," + r + "Z"
        }

        function d3_transition(groups, id) {
            return d3_arraySubclass(groups, d3_transitionPrototype), groups.id = id, groups
        }

        function d3_transition_tween(groups, name, value, tween) {
            var id = groups.id;
            return d3_selection_each(groups, "function" == typeof value ? function(node, i, j) {
                node.__transition__[id].tween.set(name, tween(value.call(node, node.__data__, i, j)))
            } : (value = tween(value), function(node) {
                node.__transition__[id].tween.set(name, value)
            }))
        }

        function d3_transition_text(b) {
            return null == b && (b = ""),
                function() {
                    this.textContent = b
                }
        }

        function d3_transitionNode(node, i, id, inherit) {
            var lock = node.__transition__ || (node.__transition__ = {
                    active: 0,
                    count: 0
                }),
                transition = lock[id];
            if (!transition) {
                var time = inherit.time;
                return transition = lock[id] = {
                    tween: new d3_Map,
                    event: d3.dispatch("start", "end"),
                    time: time,
                    ease: inherit.ease,
                    delay: inherit.delay,
                    duration: inherit.duration
                }, ++lock.count, d3.timer(function(elapsed) {
                    function start(elapsed) {
                        return lock.active > id ? stop() : (lock.active = id, event.start.call(node, d, i), transition.tween.forEach(function(key, value) {
                            (value = value.call(node, d, i)) && tweened.push(value)
                        }), tick(elapsed) || d3.timer(tick, 0, time), 1)
                    }

                    function tick(elapsed) {
                        if (lock.active !== id) return stop();
                        for (var t = (elapsed - delay) / duration, e = ease(t), n = tweened.length; n > 0;) tweened[--n].call(node, e);
                        return t >= 1 ? (stop(), event.end.call(node, d, i), 1) : void 0
                    }

                    function stop() {
                        return --lock.count ? delete lock[id] : delete node.__transition__, 1
                    }
                    var d = node.__data__,
                        ease = transition.ease,
                        event = transition.event,
                        delay = transition.delay,
                        duration = transition.duration,
                        tweened = [];
                    return elapsed >= delay ? start(elapsed) : d3.timer(start, delay, time), 1
                }, 0, time), transition
            }
        }

        function d3_svg_axisX(selection, x) {
            selection.attr("transform", function(d) {
                return "translate(" + x(d) + ",0)"
            })
        }

        function d3_svg_axisY(selection, y) {
            selection.attr("transform", function(d) {
                return "translate(0," + y(d) + ")"
            })
        }

        function d3_svg_axisSubdivide(scale, ticks, m) {
            if (subticks = [], m && ticks.length > 1) {
                for (var subticks, j, v, extent = d3_scaleExtent(scale.domain()), i = -1, n = ticks.length, d = (ticks[1] - ticks[0]) / ++m; ++i < n;)
                    for (j = m; --j > 0;)(v = +ticks[i] - j * d) >= extent[0] && subticks.push(v);
                for (--i, j = 0; ++j < m && (v = +ticks[i] + j * d) < extent[1];) subticks.push(v)
            }
            return subticks
        }

        function d3_time_utc() {
            this._ = new Date(arguments.length > 1 ? Date.UTC.apply(this, arguments) : arguments[0])
        }

        function d3_time_interval(local, step, number) {
            function round(date) {
                var d0 = local(date),
                    d1 = offset(d0, 1);
                return d1 - date > date - d0 ? d0 : d1
            }

            function ceil(date) {
                return step(date = local(new d3_time(date - 1)), 1), date
            }

            function offset(date, k) {
                return step(date = new d3_time(+date), k), date
            }

            function range(t0, t1, dt) {
                var time = ceil(t0),
                    times = [];
                if (dt > 1)
                    for (; t1 > time;) number(time) % dt || times.push(new Date(+time)), step(time, 1);
                else
                    for (; t1 > time;) times.push(new Date(+time)), step(time, 1);
                return times
            }

            function range_utc(t0, t1, dt) {
                try {
                    d3_time = d3_time_utc;
                    var utc = new d3_time_utc;
                    return utc._ = t0, range(utc, t1, dt)
                } finally {
                    d3_time = Date
                }
            }
            local.floor = local, local.round = round, local.ceil = ceil, local.offset = offset, local.range = range;
            var utc = local.utc = d3_time_interval_utc(local);
            return utc.floor = utc, utc.round = d3_time_interval_utc(round), utc.ceil = d3_time_interval_utc(ceil), utc.offset = d3_time_interval_utc(offset), utc.range = range_utc, local
        }

        function d3_time_interval_utc(method) {
            return function(date, k) {
                try {
                    d3_time = d3_time_utc;
                    var utc = new d3_time_utc;
                    return utc._ = date, method(utc, k)._
                } finally {
                    d3_time = Date
                }
            }
        }

        function d3_time_parse(date, template, string, j) {
            for (var c, p, i = 0, n = template.length, m = string.length; n > i;) {
                if (j >= m) return -1;
                if (c = template.charCodeAt(i++), 37 === c) {
                    if (p = d3_time_parsers[template.charAt(i++)], !p || (j = p(date, string, j)) < 0) return -1
                } else if (c != string.charCodeAt(j++)) return -1
            }
            return j
        }

        function d3_time_formatRe(names) {
            return new RegExp("^(?:" + names.map(d3.requote).join("|") + ")", "i")
        }

        function d3_time_formatLookup(names) {
            for (var map = new d3_Map, i = -1, n = names.length; ++i < n;) map.set(names[i].toLowerCase(), i);
            return map
        }

        function d3_time_formatPad(value, fill, width) {
            value += "";
            var length = value.length;
            return width > length ? new Array(width - length + 1).join(fill) + value : value
        }

        function d3_time_parseWeekdayAbbrev(date, string, i) {
            d3_time_dayAbbrevRe.lastIndex = 0;
            var n = d3_time_dayAbbrevRe.exec(string.substring(i));
            return n ? i += n[0].length : -1
        }

        function d3_time_parseWeekday(date, string, i) {
            d3_time_dayRe.lastIndex = 0;
            var n = d3_time_dayRe.exec(string.substring(i));
            return n ? i += n[0].length : -1
        }

        function d3_time_parseMonthAbbrev(date, string, i) {
            d3_time_monthAbbrevRe.lastIndex = 0;
            var n = d3_time_monthAbbrevRe.exec(string.substring(i));
            return n ? (date.m = d3_time_monthAbbrevLookup.get(n[0].toLowerCase()), i += n[0].length) : -1
        }

        function d3_time_parseMonth(date, string, i) {
            d3_time_monthRe.lastIndex = 0;
            var n = d3_time_monthRe.exec(string.substring(i));
            return n ? (date.m = d3_time_monthLookup.get(n[0].toLowerCase()), i += n[0].length) : -1
        }

        function d3_time_parseLocaleFull(date, string, i) {
            return d3_time_parse(date, d3_time_formats.c.toString(), string, i)
        }

        function d3_time_parseLocaleDate(date, string, i) {
            return d3_time_parse(date, d3_time_formats.x.toString(), string, i)
        }

        function d3_time_parseLocaleTime(date, string, i) {
            return d3_time_parse(date, d3_time_formats.X.toString(), string, i)
        }

        function d3_time_parseFullYear(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 4));
            return n ? (date.y = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseYear(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.y = d3_time_expandYear(+n[0]), i += n[0].length) : -1
        }

        function d3_time_expandYear(d) {
            return d + (d > 68 ? 1900 : 2e3)
        }

        function d3_time_parseMonthNumber(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.m = n[0] - 1, i += n[0].length) : -1
        }

        function d3_time_parseDay(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.d = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseHour24(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.H = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseMinutes(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.M = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseSeconds(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 2));
            return n ? (date.S = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseMilliseconds(date, string, i) {
            d3_time_numberRe.lastIndex = 0;
            var n = d3_time_numberRe.exec(string.substring(i, i + 3));
            return n ? (date.L = +n[0], i += n[0].length) : -1
        }

        function d3_time_parseAmPm(date, string, i) {
            var n = d3_time_amPmLookup.get(string.substring(i, i += 2).toLowerCase());
            return null == n ? -1 : (date.p = n, i)
        }

        function d3_time_zone(d) {
            var z = d.getTimezoneOffset(),
                zs = z > 0 ? "-" : "+",
                zh = ~~(Math.abs(z) / 60),
                zm = Math.abs(z) % 60;
            return zs + d3_time_formatPad(zh, "0", 2) + d3_time_formatPad(zm, "0", 2)
        }

        function d3_time_formatIsoNative(date) {
            return date.toISOString()
        }

        function d3_time_scale(linear, methods, format) {
            function scale(x) {
                return linear(x)
            }
            return scale.invert = function(x) {
                return d3_time_scaleDate(linear.invert(x))
            }, scale.domain = function(x) {
                return arguments.length ? (linear.domain(x), scale) : linear.domain().map(d3_time_scaleDate)
            }, scale.nice = function(m) {
                return scale.domain(d3_scale_nice(scale.domain(), function() {
                    return m
                }))
            }, scale.ticks = function(m, k) {
                var extent = d3_scaleExtent(scale.domain());
                if ("function" != typeof m) {
                    var span = extent[1] - extent[0],
                        target = span / m,
                        i = d3.bisect(d3_time_scaleSteps, target);
                    if (i == d3_time_scaleSteps.length) return methods.year(extent, m);
                    if (!i) return linear.ticks(m).map(d3_time_scaleDate);
                    Math.log(target / d3_time_scaleSteps[i - 1]) < Math.log(d3_time_scaleSteps[i] / target) && --i, m = methods[i], k = m[1], m = m[0].range
                }
                return m(extent[0], new Date(+extent[1] + 1), k)
            }, scale.tickFormat = function() {
                return format
            }, scale.copy = function() {
                return d3_time_scale(linear.copy(), methods, format)
            }, d3_scale_linearRebind(scale, linear)
        }

        function d3_time_scaleDate(t) {
            return new Date(t)
        }

        function d3_time_scaleFormat(formats) {
            return function(date) {
                for (var i = formats.length - 1, f = formats[i]; !f[1](date);) f = formats[--i];
                return f[0](date)
            }
        }

        function d3_time_scaleSetYear(y) {
            var d = new Date(y, 0, 1);
            return d.setFullYear(y), d
        }

        function d3_time_scaleGetYear(d) {
            var y = d.getFullYear(),
                d0 = d3_time_scaleSetYear(y),
                d1 = d3_time_scaleSetYear(y + 1);
            return y + (d - d0) / (d1 - d0)
        }

        function d3_time_scaleUTCSetYear(y) {
            var d = new Date(Date.UTC(y, 0, 1));
            return d.setUTCFullYear(y), d
        }

        function d3_time_scaleUTCGetYear(d) {
            var y = d.getUTCFullYear(),
                d0 = d3_time_scaleUTCSetYear(y),
                d1 = d3_time_scaleUTCSetYear(y + 1);
            return y + (d - d0) / (d1 - d0)
        }

        function d3_text(request) {
            return request.responseText
        }

        function d3_json(request) {
            return JSON.parse(request.responseText)
        }

        function d3_html(request) {
            var range = d3_document.createRange();
            return range.selectNode(d3_document.body), range.createContextualFragment(request.responseText)
        }

        function d3_xml(request) {
            return request.responseXML
        }
        var d3 = {
            version: "3.1.6"
        };
        Date.now || (Date.now = function() {
            return +new Date
        });
        var d3_document = document,
            d3_window = window;
        try {
            d3_document.createElement("div").style.setProperty("opacity", 0, "")
        } catch (error) {
            var d3_style_prototype = d3_window.CSSStyleDeclaration.prototype,
                d3_style_setProperty = d3_style_prototype.setProperty;
            d3_style_prototype.setProperty = function(name, value, priority) {
                d3_style_setProperty.call(this, name, value + "", priority)
            }
        }
        d3.ascending = function(a, b) {
            return b > a ? -1 : a > b ? 1 : a >= b ? 0 : 0 / 0
        }, d3.descending = function(a, b) {
            return a > b ? -1 : b > a ? 1 : b >= a ? 0 : 0 / 0
        }, d3.min = function(array, f) {
            var a, b, i = -1,
                n = array.length;
            if (1 === arguments.length) {
                for (; ++i < n && (null == (a = array[i]) || a != a);) a = void 0;
                for (; ++i < n;) null != (b = array[i]) && a > b && (a = b)
            } else {
                for (; ++i < n && (null == (a = f.call(array, array[i], i)) || a != a);) a = void 0;
                for (; ++i < n;) null != (b = f.call(array, array[i], i)) && a > b && (a = b)
            }
            return a
        }, d3.max = function(array, f) {
            var a, b, i = -1,
                n = array.length;
            if (1 === arguments.length) {
                for (; ++i < n && (null == (a = array[i]) || a != a);) a = void 0;
                for (; ++i < n;) null != (b = array[i]) && b > a && (a = b)
            } else {
                for (; ++i < n && (null == (a = f.call(array, array[i], i)) || a != a);) a = void 0;
                for (; ++i < n;) null != (b = f.call(array, array[i], i)) && b > a && (a = b)
            }
            return a
        }, d3.extent = function(array, f) {
            var a, b, c, i = -1,
                n = array.length;
            if (1 === arguments.length) {
                for (; ++i < n && (null == (a = c = array[i]) || a != a);) a = c = void 0;
                for (; ++i < n;) null != (b = array[i]) && (a > b && (a = b), b > c && (c = b))
            } else {
                for (; ++i < n && (null == (a = c = f.call(array, array[i], i)) || a != a);) a = void 0;
                for (; ++i < n;) null != (b = f.call(array, array[i], i)) && (a > b && (a = b), b > c && (c = b))
            }
            return [a, c]
        }, d3.sum = function(array, f) {
            var a, s = 0,
                n = array.length,
                i = -1;
            if (1 === arguments.length)
                for (; ++i < n;) isNaN(a = +array[i]) || (s += a);
            else
                for (; ++i < n;) isNaN(a = +f.call(array, array[i], i)) || (s += a);
            return s
        }, d3.mean = function(array, f) {
            var a, n = array.length,
                m = 0,
                i = -1,
                j = 0;
            if (1 === arguments.length)
                for (; ++i < n;) d3_number(a = array[i]) && (m += (a - m) / ++j);
            else
                for (; ++i < n;) d3_number(a = f.call(array, array[i], i)) && (m += (a - m) / ++j);
            return j ? m : void 0
        }, d3.quantile = function(values, p) {
            var H = (values.length - 1) * p + 1,
                h = Math.floor(H),
                v = +values[h - 1],
                e = H - h;
            return e ? v + e * (values[h] - v) : v
        }, d3.median = function(array, f) {
            return arguments.length > 1 && (array = array.map(f)), array = array.filter(d3_number), array.length ? d3.quantile(array.sort(d3.ascending), .5) : void 0
        }, d3.bisector = function(f) {
            return {
                left: function(a, x, lo, hi) {
                    for (arguments.length < 3 && (lo = 0), arguments.length < 4 && (hi = a.length); hi > lo;) {
                        var mid = lo + hi >>> 1;
                        f.call(a, a[mid], mid) < x ? lo = mid + 1 : hi = mid
                    }
                    return lo
                },
                right: function(a, x, lo, hi) {
                    for (arguments.length < 3 && (lo = 0), arguments.length < 4 && (hi = a.length); hi > lo;) {
                        var mid = lo + hi >>> 1;
                        x < f.call(a, a[mid], mid) ? hi = mid : lo = mid + 1
                    }
                    return lo
                }
            }
        };
        var d3_bisector = d3.bisector(function(d) {
            return d
        });
        d3.bisectLeft = d3_bisector.left, d3.bisect = d3.bisectRight = d3_bisector.right, d3.shuffle = function(array) {
            for (var t, i, m = array.length; m;) i = Math.random() * m-- | 0, t = array[m], array[m] = array[i], array[i] = t;
            return array
        }, d3.permute = function(array, indexes) {
            for (var permutes = [], i = -1, n = indexes.length; ++i < n;) permutes[i] = array[indexes[i]];
            return permutes
        }, d3.zip = function() {
            if (!(n = arguments.length)) return [];
            for (var i = -1, m = d3.min(arguments, d3_zipLength), zips = new Array(m); ++i < m;)
                for (var n, j = -1, zip = zips[i] = new Array(n); ++j < n;) zip[j] = arguments[j][i];
            return zips
        }, d3.transpose = function(matrix) {
            return d3.zip.apply(d3, matrix)
        }, d3.keys = function(map) {
            var keys = [];
            for (var key in map) keys.push(key);
            return keys
        }, d3.values = function(map) {
            var values = [];
            for (var key in map) values.push(map[key]);
            return values
        }, d3.entries = function(map) {
            var entries = [];
            for (var key in map) entries.push({
                key: key,
                value: map[key]
            });
            return entries
        }, d3.merge = function(arrays) {
            return Array.prototype.concat.apply([], arrays)
        }, d3.range = function(start, stop, step) {
            if (arguments.length < 3 && (step = 1, arguments.length < 2 && (stop = start, start = 0)), (stop - start) / step === 1 / 0) throw new Error("infinite range");
            var j, range = [],
                k = d3_range_integerScale(Math.abs(step)),
                i = -1;
            if (start *= k, stop *= k, step *= k, 0 > step)
                for (;
                    (j = start + step * ++i) > stop;) range.push(j / k);
            else
                for (;
                    (j = start + step * ++i) < stop;) range.push(j / k);
            return range
        }, d3.map = function(object) {
            var map = new d3_Map;
            for (var key in object) map.set(key, object[key]);
            return map
        }, d3_class(d3_Map, {
            has: function(key) {
                return d3_map_prefix + key in this
            },
            get: function(key) {
                return this[d3_map_prefix + key]
            },
            set: function(key, value) {
                return this[d3_map_prefix + key] = value
            },
            remove: function(key) {
                return key = d3_map_prefix + key, key in this && delete this[key]
            },
            keys: function() {
                var keys = [];
                return this.forEach(function(key) {
                    keys.push(key)
                }), keys
            },
            values: function() {
                var values = [];
                return this.forEach(function(key, value) {
                    values.push(value)
                }), values
            },
            entries: function() {
                var entries = [];
                return this.forEach(function(key, value) {
                    entries.push({
                        key: key,
                        value: value
                    })
                }), entries
            },
            forEach: function(f) {
                for (var key in this) key.charCodeAt(0) === d3_map_prefixCode && f.call(this, key.substring(1), this[key])
            }
        });
        var d3_map_prefix = "\x00",
            d3_map_prefixCode = d3_map_prefix.charCodeAt(0);
        d3.nest = function() {
            function map(mapType, array, depth) {
                if (depth >= keys.length) return rollup ? rollup.call(nest, array) : sortValues ? array.sort(sortValues) : array;
                for (var keyValue, object, setter, values, i = -1, n = array.length, key = keys[depth++], valuesByKey = new d3_Map; ++i < n;)(values = valuesByKey.get(keyValue = key(object = array[i]))) ? values.push(object) : valuesByKey.set(keyValue, [object]);
                return mapType ? (object = mapType(), setter = function(keyValue, values) {
                    object.set(keyValue, map(mapType, values, depth))
                }) : (object = {}, setter = function(keyValue, values) {
                    object[keyValue] = map(mapType, values, depth)
                }), valuesByKey.forEach(setter), object
            }

            function entries(map, depth) {
                if (depth >= keys.length) return map;
                var array = [],
                    sortKey = sortKeys[depth++];
                return map.forEach(function(key, keyMap) {
                    array.push({
                        key: key,
                        values: entries(keyMap, depth)
                    })
                }), sortKey ? array.sort(function(a, b) {
                    return sortKey(a.key, b.key)
                }) : array
            }
            var sortValues, rollup, nest = {},
                keys = [],
                sortKeys = [];
            return nest.map = function(array, mapType) {
                return map(mapType, array, 0)
            }, nest.entries = function(array) {
                return entries(map(d3.map, array, 0), 0)
            }, nest.key = function(d) {
                return keys.push(d), nest
            }, nest.sortKeys = function(order) {
                return sortKeys[keys.length - 1] = order, nest
            }, nest.sortValues = function(order) {
                return sortValues = order, nest
            }, nest.rollup = function(f) {
                return rollup = f, nest
            }, nest
        }, d3.set = function(array) {
            var set = new d3_Set;
            if (array)
                for (var i = 0; i < array.length; i++) set.add(array[i]);
            return set
        }, d3_class(d3_Set, {
            has: function(value) {
                return d3_map_prefix + value in this
            },
            add: function(value) {
                return this[d3_map_prefix + value] = !0, value
            },
            remove: function(value) {
                return value = d3_map_prefix + value, value in this && delete this[value]
            },
            values: function() {
                var values = [];
                return this.forEach(function(value) {
                    values.push(value)
                }), values
            },
            forEach: function(f) {
                for (var value in this) value.charCodeAt(0) === d3_map_prefixCode && f.call(this, value.substring(1))
            }
        }), d3.behavior = {}, d3.rebind = function(target, source) {
            for (var method, i = 1, n = arguments.length; ++i < n;) target[method = arguments[i]] = d3_rebind(target, source, source[method]);
            return target
        }, d3.dispatch = function() {
            for (var dispatch = new d3_dispatch, i = -1, n = arguments.length; ++i < n;) dispatch[arguments[i]] = d3_dispatch_event(dispatch);
            return dispatch
        }, d3_dispatch.prototype.on = function(type, listener) {
            var i = type.indexOf("."),
                name = "";
            if (i >= 0 && (name = type.substring(i + 1), type = type.substring(0, i)), type) return arguments.length < 2 ? this[type].on(name) : this[type].on(name, listener);
            if (2 === arguments.length) {
                if (null == listener)
                    for (type in this) this.hasOwnProperty(type) && this[type].on(name, null);
                return this
            }
        }, d3.event = null, d3.mouse = function(container) {
            return d3_mousePoint(container, d3_eventSource())
        };
        var d3_mouse_bug44083 = /WebKit/.test(d3_window.navigator.userAgent) ? -1 : 0,
            d3_array = d3_arraySlice;
        try {
            d3_array(d3_document.documentElement.childNodes)[0].nodeType
        } catch (e) {
            d3_array = d3_arrayCopy
        }
        var d3_arraySubclass = [].__proto__ ? function(array, prototype) {
            array.__proto__ = prototype
        } : function(array, prototype) {
            for (var property in prototype) array[property] = prototype[property]
        };
        d3.touches = function(container, touches) {
            return arguments.length < 2 && (touches = d3_eventSource().touches), touches ? d3_array(touches).map(function(touch) {
                var point = d3_mousePoint(container, touch);
                return point.identifier = touch.identifier, point
            }) : []
        }, d3.behavior.drag = function() {
            function drag() {
                this.on("mousedown.drag", mousedown).on("touchstart.drag", mousedown)
            }

            function mousedown() {
                function point() {
                    var p = target.parentNode;
                    return null != touchId ? d3.touches(p).filter(function(p) {
                        return p.identifier === touchId
                    })[0] : d3.mouse(p)
                }

                function dragmove() {
                    if (!target.parentNode) return dragend();
                    var p = point(),
                        dx = p[0] - origin_[0],
                        dy = p[1] - origin_[1];
                    moved |= dx | dy, origin_ = p, d3_eventCancel(), event_({
                        type: "drag",
                        x: p[0] + offset[0],
                        y: p[1] + offset[1],
                        dx: dx,
                        dy: dy
                    })
                }

                function dragend() {
                    event_({
                        type: "dragend"
                    }), moved && (d3_eventCancel(), d3.event.target === eventTarget && d3_eventSuppress(w, "click")), w.on(null != touchId ? "touchmove.drag-" + touchId : "mousemove.drag", null).on(null != touchId ? "touchend.drag-" + touchId : "mouseup.drag", null)
                }
                var offset, target = this,
                    event_ = event.of(target, arguments),
                    eventTarget = d3.event.target,
                    touchId = d3.event.touches ? d3.event.changedTouches[0].identifier : null,
                    origin_ = point(),
                    moved = 0,
                    w = d3.select(d3_window).on(null != touchId ? "touchmove.drag-" + touchId : "mousemove.drag", dragmove).on(null != touchId ? "touchend.drag-" + touchId : "mouseup.drag", dragend, !0);
                origin ? (offset = origin.apply(target, arguments), offset = [offset.x - origin_[0], offset.y - origin_[1]]) : offset = [0, 0], null == touchId && d3_eventCancel(), event_({
                    type: "dragstart"
                })
            }
            var event = d3_eventDispatch(drag, "drag", "dragstart", "dragend"),
                origin = null;
            return drag.origin = function(x) {
                return arguments.length ? (origin = x, drag) : origin
            }, d3.rebind(drag, event, "on")
        };
        var d3_select = function(s, n) {
                return n.querySelector(s)
            },
            d3_selectAll = function(s, n) {
                return n.querySelectorAll(s)
            },
            d3_selectRoot = d3_document.documentElement,
            d3_selectMatcher = d3_selectRoot.matchesSelector || d3_selectRoot.webkitMatchesSelector || d3_selectRoot.mozMatchesSelector || d3_selectRoot.msMatchesSelector || d3_selectRoot.oMatchesSelector,
            d3_selectMatches = function(n, s) {
                return d3_selectMatcher.call(n, s)
            };
        "function" == typeof Sizzle && (d3_select = function(s, n) {
            return Sizzle(s, n)[0] || null
        }, d3_selectAll = function(s, n) {
            return Sizzle.uniqueSort(Sizzle(s, n))
        }, d3_selectMatches = Sizzle.matchesSelector), d3.selection = function() {
            return d3_selectionRoot
        };
        var d3_selectionPrototype = d3.selection.prototype = [];
        d3_selectionPrototype.select = function(selector) {
            var subgroup, subnode, group, node, subgroups = [];
            "function" != typeof selector && (selector = d3_selection_selector(selector));
            for (var j = -1, m = this.length; ++j < m;) {
                subgroups.push(subgroup = []), subgroup.parentNode = (group = this[j]).parentNode;
                for (var i = -1, n = group.length; ++i < n;)(node = group[i]) ? (subgroup.push(subnode = selector.call(node, node.__data__, i)), subnode && "__data__" in node && (subnode.__data__ = node.__data__)) : subgroup.push(null)
            }
            return d3_selection(subgroups)
        }, d3_selectionPrototype.selectAll = function(selector) {
            var subgroup, node, subgroups = [];
            "function" != typeof selector && (selector = d3_selection_selectorAll(selector));
            for (var j = -1, m = this.length; ++j < m;)
                for (var group = this[j], i = -1, n = group.length; ++i < n;)(node = group[i]) && (subgroups.push(subgroup = d3_array(selector.call(node, node.__data__, i))), subgroup.parentNode = node);
            return d3_selection(subgroups)
        };
        var d3_nsPrefix = {
            svg: "http://www.w3.org/2000/svg",
            xhtml: "http://www.w3.org/1999/xhtml",
            xlink: "http://www.w3.org/1999/xlink",
            xml: "http://www.w3.org/XML/1998/namespace",
            xmlns: "http://www.w3.org/2000/xmlns/"
        };
        d3.ns = {
            prefix: d3_nsPrefix,
            qualify: function(name) {
                var i = name.indexOf(":"),
                    prefix = name;
                return i >= 0 && (prefix = name.substring(0, i), name = name.substring(i + 1)), d3_nsPrefix.hasOwnProperty(prefix) ? {
                    space: d3_nsPrefix[prefix],
                    local: name
                } : name
            }
        }, d3_selectionPrototype.attr = function(name, value) {
            if (arguments.length < 2) {
                if ("string" == typeof name) {
                    var node = this.node();
                    return name = d3.ns.qualify(name), name.local ? node.getAttributeNS(name.space, name.local) : node.getAttribute(name)
                }
                for (value in name) this.each(d3_selection_attr(value, name[value]));
                return this
            }
            return this.each(d3_selection_attr(name, value))
        }, d3.requote = function(s) {
            return s.replace(d3_requote_re, "\\$&")
        };
        var d3_requote_re = /[\\\^\$\*\+\?\|\[\]\(\)\.\{\}]/g;
        d3_selectionPrototype.classed = function(name, value) {
            if (arguments.length < 2) {
                if ("string" == typeof name) {
                    var node = this.node(),
                        n = (name = name.trim().split(/^|\s+/g)).length,
                        i = -1;
                    if (value = node.classList) {
                        for (; ++i < n;)
                            if (!value.contains(name[i])) return !1
                    } else
                        for (value = node.getAttribute("class"); ++i < n;)
                            if (!d3_selection_classedRe(name[i]).test(value)) return !1;

                    return !0
                }
                for (value in name) this.each(d3_selection_classed(value, name[value]));
                return this
            }
            return this.each(d3_selection_classed(name, value))
        }, d3_selectionPrototype.style = function(name, value, priority) {
            var n = arguments.length;
            if (3 > n) {
                if ("string" != typeof name) {
                    2 > n && (value = "");
                    for (priority in name) this.each(d3_selection_style(priority, name[priority], value));
                    return this
                }
                if (2 > n) return d3_window.getComputedStyle(this.node(), null).getPropertyValue(name);
                priority = ""
            }
            return this.each(d3_selection_style(name, value, priority))
        }, d3_selectionPrototype.property = function(name, value) {
            if (arguments.length < 2) {
                if ("string" == typeof name) return this.node()[name];
                for (value in name) this.each(d3_selection_property(value, name[value]));
                return this
            }
            return this.each(d3_selection_property(name, value))
        }, d3_selectionPrototype.text = function(value) {
            return arguments.length ? this.each("function" == typeof value ? function() {
                var v = value.apply(this, arguments);
                this.textContent = null == v ? "" : v
            } : null == value ? function() {
                this.textContent = ""
            } : function() {
                this.textContent = value
            }) : this.node().textContent
        }, d3_selectionPrototype.html = function(value) {
            return arguments.length ? this.each("function" == typeof value ? function() {
                var v = value.apply(this, arguments);
                this.innerHTML = null == v ? "" : v
            } : null == value ? function() {
                this.innerHTML = ""
            } : function() {
                this.innerHTML = value
            }) : this.node().innerHTML
        }, d3_selectionPrototype.append = function(name) {
            function append() {
                return this.appendChild(d3_document.createElementNS(this.namespaceURI, name))
            }

            function appendNS() {
                return this.appendChild(d3_document.createElementNS(name.space, name.local))
            }
            return name = d3.ns.qualify(name), this.select(name.local ? appendNS : append)
        }, d3_selectionPrototype.insert = function(name, before) {
            function insert(d, i) {
                return this.insertBefore(d3_document.createElementNS(this.namespaceURI, name), before.call(this, d, i))
            }

            function insertNS(d, i) {
                return this.insertBefore(d3_document.createElementNS(name.space, name.local), before.call(this, d, i))
            }
            return name = d3.ns.qualify(name), "function" != typeof before && (before = d3_selection_selector(before)), this.select(name.local ? insertNS : insert)
        }, d3_selectionPrototype.remove = function() {
            return this.each(function() {
                var parent = this.parentNode;
                parent && parent.removeChild(this)
            })
        }, d3_selectionPrototype.data = function(value, key) {
            function bind(group, groupData) {
                var i, node, nodeData, n = group.length,
                    m = groupData.length,
                    n0 = Math.min(n, m),
                    updateNodes = new Array(m),
                    enterNodes = new Array(m),
                    exitNodes = new Array(n);
                if (key) {
                    var keyValue, nodeByKeyValue = new d3_Map,
                        dataByKeyValue = new d3_Map,
                        keyValues = [];
                    for (i = -1; ++i < n;) keyValue = key.call(node = group[i], node.__data__, i), nodeByKeyValue.has(keyValue) ? exitNodes[i] = node : nodeByKeyValue.set(keyValue, node), keyValues.push(keyValue);
                    for (i = -1; ++i < m;) keyValue = key.call(groupData, nodeData = groupData[i], i), (node = nodeByKeyValue.get(keyValue)) ? (updateNodes[i] = node, node.__data__ = nodeData) : dataByKeyValue.has(keyValue) || (enterNodes[i] = d3_selection_dataNode(nodeData)), dataByKeyValue.set(keyValue, nodeData), nodeByKeyValue.remove(keyValue);
                    for (i = -1; ++i < n;) nodeByKeyValue.has(keyValues[i]) && (exitNodes[i] = group[i])
                } else {
                    for (i = -1; ++i < n0;) node = group[i], nodeData = groupData[i], node ? (node.__data__ = nodeData, updateNodes[i] = node) : enterNodes[i] = d3_selection_dataNode(nodeData);
                    for (; m > i; ++i) enterNodes[i] = d3_selection_dataNode(groupData[i]);
                    for (; n > i; ++i) exitNodes[i] = group[i]
                }
                enterNodes.update = updateNodes, enterNodes.parentNode = updateNodes.parentNode = exitNodes.parentNode = group.parentNode, enter.push(enterNodes), update.push(updateNodes), exit.push(exitNodes)
            }
            var group, node, i = -1,
                n = this.length;
            if (!arguments.length) {
                for (value = new Array(n = (group = this[0]).length); ++i < n;)(node = group[i]) && (value[i] = node.__data__);
                return value
            }
            var enter = d3_selection_enter([]),
                update = d3_selection([]),
                exit = d3_selection([]);
            if ("function" == typeof value)
                for (; ++i < n;) bind(group = this[i], value.call(group, group.parentNode.__data__, i));
            else
                for (; ++i < n;) bind(group = this[i], value);
            return update.enter = function() {
                return enter
            }, update.exit = function() {
                return exit
            }, update
        }, d3_selectionPrototype.datum = function(value) {
            return arguments.length ? this.property("__data__", value) : this.property("__data__")
        }, d3_selectionPrototype.filter = function(filter) {
            var subgroup, group, node, subgroups = [];
            "function" != typeof filter && (filter = d3_selection_filter(filter));
            for (var j = 0, m = this.length; m > j; j++) {
                subgroups.push(subgroup = []), subgroup.parentNode = (group = this[j]).parentNode;
                for (var i = 0, n = group.length; n > i; i++)(node = group[i]) && filter.call(node, node.__data__, i) && subgroup.push(node)
            }
            return d3_selection(subgroups)
        }, d3_selectionPrototype.order = function() {
            for (var j = -1, m = this.length; ++j < m;)
                for (var node, group = this[j], i = group.length - 1, next = group[i]; --i >= 0;)(node = group[i]) && (next && next !== node.nextSibling && next.parentNode.insertBefore(node, next), next = node);
            return this
        }, d3_selectionPrototype.sort = function(comparator) {
            comparator = d3_selection_sortComparator.apply(this, arguments);
            for (var j = -1, m = this.length; ++j < m;) this[j].sort(comparator);
            return this.order()
        }, d3_selectionPrototype.on = function(type, listener, capture) {
            var n = arguments.length;
            if (3 > n) {
                if ("string" != typeof type) {
                    2 > n && (listener = !1);
                    for (capture in type) this.each(d3_selection_on(capture, type[capture], listener));
                    return this
                }
                if (2 > n) return (n = this.node()["__on" + type]) && n._;
                capture = !1
            }
            return this.each(d3_selection_on(type, listener, capture))
        };
        var d3_selection_onFilters = d3.map({
            mouseenter: "mouseover",
            mouseleave: "mouseout"
        });
        d3_selection_onFilters.forEach(function(k) {
            "on" + k in d3_document && d3_selection_onFilters.remove(k)
        }), d3_selectionPrototype.each = function(callback) {
            return d3_selection_each(this, function(node, i, j) {
                callback.call(node, node.__data__, i, j)
            })
        }, d3_selectionPrototype.call = function(callback) {
            var args = d3_array(arguments);
            return callback.apply(args[0] = this, args), this
        }, d3_selectionPrototype.empty = function() {
            return !this.node()
        }, d3_selectionPrototype.node = function() {
            for (var j = 0, m = this.length; m > j; j++)
                for (var group = this[j], i = 0, n = group.length; n > i; i++) {
                    var node = group[i];
                    if (node) return node
                }
            return null
        };
        var d3_selection_enterPrototype = [];
        d3.selection.enter = d3_selection_enter, d3.selection.enter.prototype = d3_selection_enterPrototype, d3_selection_enterPrototype.append = d3_selectionPrototype.append, d3_selection_enterPrototype.insert = d3_selectionPrototype.insert, d3_selection_enterPrototype.empty = d3_selectionPrototype.empty, d3_selection_enterPrototype.node = d3_selectionPrototype.node, d3_selection_enterPrototype.select = function(selector) {
            for (var subgroup, subnode, upgroup, group, node, subgroups = [], j = -1, m = this.length; ++j < m;) {
                upgroup = (group = this[j]).update, subgroups.push(subgroup = []), subgroup.parentNode = group.parentNode;
                for (var i = -1, n = group.length; ++i < n;)(node = group[i]) ? (subgroup.push(upgroup[i] = subnode = selector.call(group.parentNode, node.__data__, i)), subnode.__data__ = node.__data__) : subgroup.push(null)
            }
            return d3_selection(subgroups)
        }, d3_selectionPrototype.transition = function() {
            var subgroup, node, id = d3_transitionInheritId || ++d3_transitionId,
                subgroups = [],
                transition = Object.create(d3_transitionInherit);
            transition.time = Date.now();
            for (var j = -1, m = this.length; ++j < m;) {
                subgroups.push(subgroup = []);
                for (var group = this[j], i = -1, n = group.length; ++i < n;)(node = group[i]) && d3_transitionNode(node, i, id, transition), subgroup.push(node)
            }
            return d3_transition(subgroups, id)
        }, d3.select = function(node) {
            var group = ["string" == typeof node ? d3_select(node, d3_document) : node];
            return group.parentNode = d3_selectRoot, d3_selection([group])
        }, d3.selectAll = function(nodes) {
            var group = d3_array("string" == typeof nodes ? d3_selectAll(nodes, d3_document) : nodes);
            return group.parentNode = d3_selectRoot, d3_selection([group])
        };
        var d3_selectionRoot = d3.select(d3_selectRoot);
        d3.behavior.zoom = function() {
            function zoom() {
                this.on("mousedown.zoom", mousedown).on("mousemove.zoom", mousemove).on(d3_behavior_zoomWheel + ".zoom", mousewheel).on("dblclick.zoom", dblclick).on("touchstart.zoom", touchstart).on("touchmove.zoom", touchmove).on("touchend.zoom", touchstart)
            }

            function location(p) {
                return [(p[0] - translate[0]) / scale, (p[1] - translate[1]) / scale]
            }

            function point(l) {
                return [l[0] * scale + translate[0], l[1] * scale + translate[1]]
            }

            function scaleTo(s) {
                scale = Math.max(scaleExtent[0], Math.min(scaleExtent[1], s))
            }

            function translateTo(p, l) {
                l = point(l), translate[0] += p[0] - l[0], translate[1] += p[1] - l[1]
            }

            function rescale() {
                x1 && x1.domain(x0.range().map(function(x) {
                    return (x - translate[0]) / scale
                }).map(x0.invert)), y1 && y1.domain(y0.range().map(function(y) {
                    return (y - translate[1]) / scale
                }).map(y0.invert))
            }

            function dispatch(event) {
                rescale(), d3.event.preventDefault(), event({
                    type: "zoom",
                    scale: scale,
                    translate: translate
                })
            }

            function mousedown() {
                function mousemove() {
                    moved = 1, translateTo(d3.mouse(target), l), dispatch(event_)
                }

                function mouseup() {
                    moved && d3_eventCancel(), w.on("mousemove.zoom", null).on("mouseup.zoom", null), moved && d3.event.target === eventTarget && d3_eventSuppress(w, "click.zoom")
                }
                var target = this,
                    event_ = event.of(target, arguments),
                    eventTarget = d3.event.target,
                    moved = 0,
                    w = d3.select(d3_window).on("mousemove.zoom", mousemove).on("mouseup.zoom", mouseup),
                    l = location(d3.mouse(target));
                d3_window.focus(), d3_eventCancel()
            }

            function mousewheel() {
                translate0 || (translate0 = location(d3.mouse(this))), scaleTo(Math.pow(2, .002 * d3_behavior_zoomDelta()) * scale), translateTo(d3.mouse(this), translate0), dispatch(event.of(this, arguments))
            }

            function mousemove() {
                translate0 = null
            }

            function dblclick() {
                var p = d3.mouse(this),
                    l = location(p),
                    k = Math.log(scale) / Math.LN2;
                scaleTo(Math.pow(2, d3.event.shiftKey ? Math.ceil(k) - 1 : Math.floor(k) + 1)), translateTo(p, l), dispatch(event.of(this, arguments))
            }

            function touchstart() {
                var touches = d3.touches(this),
                    now = Date.now();
                if (scale0 = scale, translate0 = {}, touches.forEach(function(t) {
                        translate0[t.identifier] = location(t)
                    }), d3_eventCancel(), 1 === touches.length) {
                    if (500 > now - touchtime) {
                        var p = touches[0],
                            l = location(touches[0]);
                        scaleTo(2 * scale), translateTo(p, l), dispatch(event.of(this, arguments))
                    }
                    touchtime = now
                }
            }

            function touchmove() {
                var touches = d3.touches(this),
                    p0 = touches[0],
                    l0 = translate0[p0.identifier];
                if (p1 = touches[1]) {
                    var p1, l1 = translate0[p1.identifier];
                    p0 = [(p0[0] + p1[0]) / 2, (p0[1] + p1[1]) / 2], l0 = [(l0[0] + l1[0]) / 2, (l0[1] + l1[1]) / 2], scaleTo(d3.event.scale * scale0)
                }
                translateTo(p0, l0), touchtime = null, dispatch(event.of(this, arguments))
            }
            var translate0, scale0, x0, x1, y0, y1, touchtime, translate = [0, 0],
                scale = 1,
                scaleExtent = d3_behavior_zoomInfinity,
                event = d3_eventDispatch(zoom, "zoom");
            return zoom.translate = function(x) {
                return arguments.length ? (translate = x.map(Number), rescale(), zoom) : translate
            }, zoom.scale = function(x) {
                return arguments.length ? (scale = +x, rescale(), zoom) : scale
            }, zoom.scaleExtent = function(x) {
                return arguments.length ? (scaleExtent = null == x ? d3_behavior_zoomInfinity : x.map(Number), zoom) : scaleExtent
            }, zoom.x = function(z) {
                return arguments.length ? (x1 = z, x0 = z.copy(), translate = [0, 0], scale = 1, zoom) : x1
            }, zoom.y = function(z) {
                return arguments.length ? (y1 = z, y0 = z.copy(), translate = [0, 0], scale = 1, zoom) : y1
            }, d3.rebind(zoom, event, "on")
        };
        var d3_behavior_zoomDelta, d3_behavior_zoomInfinity = [0, 1 / 0],
            d3_behavior_zoomWheel = "onwheel" in d3_document ? (d3_behavior_zoomDelta = function() {
                return -d3.event.deltaY * (d3.event.deltaMode ? 120 : 1)
            }, "wheel") : "onmousewheel" in d3_document ? (d3_behavior_zoomDelta = function() {
                return d3.event.wheelDelta
            }, "mousewheel") : (d3_behavior_zoomDelta = function() {
                return -d3.event.detail
            }, "MozMousePixelScroll");
        d3_Color.prototype.toString = function() {
            return this.rgb() + ""
        }, d3.hsl = function(h, s, l) {
            return 1 === arguments.length ? h instanceof d3_Hsl ? d3_hsl(h.h, h.s, h.l) : d3_rgb_parse("" + h, d3_rgb_hsl, d3_hsl) : d3_hsl(+h, +s, +l)
        };
        var d3_hslPrototype = d3_Hsl.prototype = new d3_Color;
        d3_hslPrototype.brighter = function(k) {
            return k = Math.pow(.7, arguments.length ? k : 1), d3_hsl(this.h, this.s, this.l / k)
        }, d3_hslPrototype.darker = function(k) {
            return k = Math.pow(.7, arguments.length ? k : 1), d3_hsl(this.h, this.s, k * this.l)
        }, d3_hslPrototype.rgb = function() {
            return d3_hsl_rgb(this.h, this.s, this.l)
        };
        var π = Math.PI,
            ε = 1e-6,
            d3_radians = π / 180,
            d3_degrees = 180 / π;
        d3.hcl = function(h, c, l) {
            return 1 === arguments.length ? h instanceof d3_Hcl ? d3_hcl(h.h, h.c, h.l) : h instanceof d3_Lab ? d3_lab_hcl(h.l, h.a, h.b) : d3_lab_hcl((h = d3_rgb_lab((h = d3.rgb(h)).r, h.g, h.b)).l, h.a, h.b) : d3_hcl(+h, +c, +l)
        };
        var d3_hclPrototype = d3_Hcl.prototype = new d3_Color;
        d3_hclPrototype.brighter = function(k) {
            return d3_hcl(this.h, this.c, Math.min(100, this.l + d3_lab_K * (arguments.length ? k : 1)))
        }, d3_hclPrototype.darker = function(k) {
            return d3_hcl(this.h, this.c, Math.max(0, this.l - d3_lab_K * (arguments.length ? k : 1)))
        }, d3_hclPrototype.rgb = function() {
            return d3_hcl_lab(this.h, this.c, this.l).rgb()
        }, d3.lab = function(l, a, b) {
            return 1 === arguments.length ? l instanceof d3_Lab ? d3_lab(l.l, l.a, l.b) : l instanceof d3_Hcl ? d3_hcl_lab(l.l, l.c, l.h) : d3_rgb_lab((l = d3.rgb(l)).r, l.g, l.b) : d3_lab(+l, +a, +b)
        };
        var d3_lab_K = 18,
            d3_lab_X = .95047,
            d3_lab_Y = 1,
            d3_lab_Z = 1.08883,
            d3_labPrototype = d3_Lab.prototype = new d3_Color;
        d3_labPrototype.brighter = function(k) {
            return d3_lab(Math.min(100, this.l + d3_lab_K * (arguments.length ? k : 1)), this.a, this.b)
        }, d3_labPrototype.darker = function(k) {
            return d3_lab(Math.max(0, this.l - d3_lab_K * (arguments.length ? k : 1)), this.a, this.b)
        }, d3_labPrototype.rgb = function() {
            return d3_lab_rgb(this.l, this.a, this.b)
        }, d3.rgb = function(r, g, b) {
            return 1 === arguments.length ? r instanceof d3_Rgb ? d3_rgb(r.r, r.g, r.b) : d3_rgb_parse("" + r, d3_rgb, d3_hsl_rgb) : d3_rgb(~~r, ~~g, ~~b)
        };
        var d3_rgbPrototype = d3_Rgb.prototype = new d3_Color;
        d3_rgbPrototype.brighter = function(k) {
            k = Math.pow(.7, arguments.length ? k : 1);
            var r = this.r,
                g = this.g,
                b = this.b,
                i = 30;
            return r || g || b ? (r && i > r && (r = i), g && i > g && (g = i), b && i > b && (b = i), d3_rgb(Math.min(255, Math.floor(r / k)), Math.min(255, Math.floor(g / k)), Math.min(255, Math.floor(b / k)))) : d3_rgb(i, i, i)
        }, d3_rgbPrototype.darker = function(k) {
            return k = Math.pow(.7, arguments.length ? k : 1), d3_rgb(Math.floor(k * this.r), Math.floor(k * this.g), Math.floor(k * this.b))
        }, d3_rgbPrototype.hsl = function() {
            return d3_rgb_hsl(this.r, this.g, this.b)
        }, d3_rgbPrototype.toString = function() {
            return "#" + d3_rgb_hex(this.r) + d3_rgb_hex(this.g) + d3_rgb_hex(this.b)
        };
        var d3_rgb_names = d3.map({
            aliceblue: "#f0f8ff",
            antiquewhite: "#faebd7",
            aqua: "#00ffff",
            aquamarine: "#7fffd4",
            azure: "#f0ffff",
            beige: "#f5f5dc",
            bisque: "#ffe4c4",
            black: "#000000",
            blanchedalmond: "#ffebcd",
            blue: "#0000ff",
            blueviolet: "#8a2be2",
            brown: "#a52a2a",
            burlywood: "#deb887",
            cadetblue: "#5f9ea0",
            chartreuse: "#7fff00",
            chocolate: "#d2691e",
            coral: "#ff7f50",
            cornflowerblue: "#6495ed",
            cornsilk: "#fff8dc",
            crimson: "#dc143c",
            cyan: "#00ffff",
            darkblue: "#00008b",
            darkcyan: "#008b8b",
            darkgoldenrod: "#b8860b",
            darkgray: "#a9a9a9",
            darkgreen: "#006400",
            darkgrey: "#a9a9a9",
            darkkhaki: "#bdb76b",
            darkmagenta: "#8b008b",
            darkolivegreen: "#556b2f",
            darkorange: "#ff8c00",
            darkorchid: "#9932cc",
            darkred: "#8b0000",
            darksalmon: "#e9967a",
            darkseagreen: "#8fbc8f",
            darkslateblue: "#483d8b",
            darkslategray: "#2f4f4f",
            darkslategrey: "#2f4f4f",
            darkturquoise: "#00ced1",
            darkviolet: "#9400d3",
            deeppink: "#ff1493",
            deepskyblue: "#00bfff",
            dimgray: "#696969",
            dimgrey: "#696969",
            dodgerblue: "#1e90ff",
            firebrick: "#b22222",
            floralwhite: "#fffaf0",
            forestgreen: "#228b22",
            fuchsia: "#ff00ff",
            gainsboro: "#dcdcdc",
            ghostwhite: "#f8f8ff",
            gold: "#ffd700",
            goldenrod: "#daa520",
            gray: "#808080",
            green: "#008000",
            greenyellow: "#adff2f",
            grey: "#808080",
            honeydew: "#f0fff0",
            hotpink: "#ff69b4",
            indianred: "#cd5c5c",
            indigo: "#4b0082",
            ivory: "#fffff0",
            khaki: "#f0e68c",
            lavender: "#e6e6fa",
            lavenderblush: "#fff0f5",
            lawngreen: "#7cfc00",
            lemonchiffon: "#fffacd",
            lightblue: "#add8e6",
            lightcoral: "#f08080",
            lightcyan: "#e0ffff",
            lightgoldenrodyellow: "#fafad2",
            lightgray: "#d3d3d3",
            lightgreen: "#90ee90",
            lightgrey: "#d3d3d3",
            lightpink: "#ffb6c1",
            lightsalmon: "#ffa07a",
            lightseagreen: "#20b2aa",
            lightskyblue: "#87cefa",
            lightslategray: "#778899",
            lightslategrey: "#778899",
            lightsteelblue: "#b0c4de",
            lightyellow: "#ffffe0",
            lime: "#00ff00",
            limegreen: "#32cd32",
            linen: "#faf0e6",
            magenta: "#ff00ff",
            maroon: "#800000",
            mediumaquamarine: "#66cdaa",
            mediumblue: "#0000cd",
            mediumorchid: "#ba55d3",
            mediumpurple: "#9370db",
            mediumseagreen: "#3cb371",
            mediumslateblue: "#7b68ee",
            mediumspringgreen: "#00fa9a",
            mediumturquoise: "#48d1cc",
            mediumvioletred: "#c71585",
            midnightblue: "#191970",
            mintcream: "#f5fffa",
            mistyrose: "#ffe4e1",
            moccasin: "#ffe4b5",
            navajowhite: "#ffdead",
            navy: "#000080",
            oldlace: "#fdf5e6",
            olive: "#808000",
            olivedrab: "#6b8e23",
            orange: "#ffa500",
            orangered: "#ff4500",
            orchid: "#da70d6",
            palegoldenrod: "#eee8aa",
            palegreen: "#98fb98",
            paleturquoise: "#afeeee",
            palevioletred: "#db7093",
            papayawhip: "#ffefd5",
            peachpuff: "#ffdab9",
            peru: "#cd853f",
            pink: "#ffc0cb",
            plum: "#dda0dd",
            powderblue: "#b0e0e6",
            purple: "#800080",
            red: "#ff0000",
            rosybrown: "#bc8f8f",
            royalblue: "#4169e1",
            saddlebrown: "#8b4513",
            salmon: "#fa8072",
            sandybrown: "#f4a460",
            seagreen: "#2e8b57",
            seashell: "#fff5ee",
            sienna: "#a0522d",
            silver: "#c0c0c0",
            skyblue: "#87ceeb",
            slateblue: "#6a5acd",
            slategray: "#708090",
            slategrey: "#708090",
            snow: "#fffafa",
            springgreen: "#00ff7f",
            steelblue: "#4682b4",
            tan: "#d2b48c",
            teal: "#008080",
            thistle: "#d8bfd8",
            tomato: "#ff6347",
            turquoise: "#40e0d0",
            violet: "#ee82ee",
            wheat: "#f5deb3",
            white: "#ffffff",
            whitesmoke: "#f5f5f5",
            yellow: "#ffff00",
            yellowgreen: "#9acd32"
        });
        d3_rgb_names.forEach(function(key, value) {
            d3_rgb_names.set(key, d3_rgb_parse(value, d3_rgb, d3_hsl_rgb))
        }), d3.functor = d3_functor, d3.xhr = function(url, mimeType, callback) {
            function respond() {
                var s = request.status;
                !s && request.responseText || s >= 200 && 300 > s || 304 === s ? dispatch.load.call(xhr, response.call(xhr, request)) : dispatch.error.call(xhr, request)
            }
            var xhr = {},
                dispatch = d3.dispatch("progress", "load", "error"),
                headers = {},
                response = d3_identity,
                request = new(d3_window.XDomainRequest && /^(http(s)?:)?\/\//.test(url) ? XDomainRequest : XMLHttpRequest);
            return "onload" in request ? request.onload = request.onerror = respond : request.onreadystatechange = function() {
                request.readyState > 3 && respond()
            }, request.onprogress = function(event) {
                var o = d3.event;
                d3.event = event;
                try {
                    dispatch.progress.call(xhr, request)
                } finally {
                    d3.event = o
                }
            }, xhr.header = function(name, value) {
                return name = (name + "").toLowerCase(), arguments.length < 2 ? headers[name] : (null == value ? delete headers[name] : headers[name] = value + "", xhr)
            }, xhr.mimeType = function(value) {
                return arguments.length ? (mimeType = null == value ? null : value + "", xhr) : mimeType
            }, xhr.response = function(value) {
                return response = value, xhr
            }, ["get", "post"].forEach(function(method) {
                xhr[method] = function() {
                    return xhr.send.apply(xhr, [method].concat(d3_array(arguments)))
                }
            }), xhr.send = function(method, data, callback) {
                if (2 === arguments.length && "function" == typeof data && (callback = data, data = null), request.open(method, url, !0), null == mimeType || "accept" in headers || (headers.accept = mimeType + ",*/*"), request.setRequestHeader)
                    for (var name in headers) request.setRequestHeader(name, headers[name]);
                return null != mimeType && request.overrideMimeType && request.overrideMimeType(mimeType), null != callback && xhr.on("error", callback).on("load", function(request) {
                    callback(null, request)
                }), request.send(null == data ? null : data), xhr
            }, xhr.abort = function() {
                return request.abort(), xhr
            }, d3.rebind(xhr, dispatch, "on"), 2 === arguments.length && "function" == typeof mimeType && (callback = mimeType, mimeType = null), null == callback ? xhr : xhr.get(d3_xhr_fixCallback(callback))
        }, d3.csv = d3_dsv(",", "text/csv"), d3.tsv = d3_dsv("	", "text/tab-separated-values");
        var d3_timer_interval, d3_timer_timeout, d3_timer_id = 0,
            d3_timer_byId = {},
            d3_timer_queue = null;
        d3.timer = function(callback, delay, then) {
            if (arguments.length < 3) {
                if (arguments.length < 2) delay = 0;
                else if (!isFinite(delay)) return;
                then = Date.now()
            }
            var timer = d3_timer_byId[callback.id];
            timer && timer.callback === callback ? (timer.then = then, timer.delay = delay) : d3_timer_byId[callback.id = ++d3_timer_id] = d3_timer_queue = {
                callback: callback,
                then: then,
                delay: delay,
                next: d3_timer_queue
            }, d3_timer_interval || (d3_timer_timeout = clearTimeout(d3_timer_timeout), d3_timer_interval = 1, d3_timer_frame(d3_timer_step))
        }, d3.timer.flush = function() {
            for (var elapsed, now = Date.now(), t1 = d3_timer_queue; t1;) elapsed = now - t1.then, t1.delay || (t1.flush = t1.callback(elapsed)), t1 = t1.next;
            d3_timer_flush()
        };
        var d3_timer_frame = d3_window.requestAnimationFrame || d3_window.webkitRequestAnimationFrame || d3_window.mozRequestAnimationFrame || d3_window.oRequestAnimationFrame || d3_window.msRequestAnimationFrame || function(callback) {
                setTimeout(callback, 17)
            },
            d3_format_decimalPoint = ".",
            d3_format_thousandsSeparator = ",",
            d3_format_grouping = [3, 3],
            d3_formatPrefixes = ["y", "z", "a", "f", "p", "n", "μ", "m", "", "k", "M", "G", "T", "P", "E", "Z", "Y"].map(d3_formatPrefix);
        d3.formatPrefix = function(value, precision) {
            var i = 0;
            return value && (0 > value && (value *= -1), precision && (value = d3.round(value, d3_format_precision(value, precision))), i = 1 + Math.floor(1e-12 + Math.log(value) / Math.LN10), i = Math.max(-24, Math.min(24, 3 * Math.floor((0 >= i ? i + 1 : i - 1) / 3)))), d3_formatPrefixes[8 + i / 3]
        }, d3.round = function(x, n) {
            return n ? Math.round(x * (n = Math.pow(10, n))) / n : Math.round(x)
        }, d3.format = function(specifier) {
            var match = d3_format_re.exec(specifier),
                fill = match[1] || " ",
                align = match[2] || ">",
                sign = match[3] || "",
                basePrefix = match[4] || "",
                zfill = match[5],
                width = +match[6],
                comma = match[7],
                precision = match[8],
                type = match[9],
                scale = 1,
                suffix = "",
                integer = !1;
            switch (precision && (precision = +precision.substring(1)), (zfill || "0" === fill && "=" === align) && (zfill = fill = "0", align = "=", comma && (width -= Math.floor((width - 1) / 4))), type) {
                case "n":
                    comma = !0, type = "g";
                    break;
                case "%":
                    scale = 100, suffix = "%", type = "f";
                    break;
                case "p":
                    scale = 100, suffix = "%", type = "r";
                    break;
                case "b":
                case "o":
                case "x":
                case "X":
                    basePrefix && (basePrefix = "0" + type.toLowerCase());
                case "c":
                case "d":
                    integer = !0, precision = 0;
                    break;
                case "s":
                    scale = -1, type = "r"
            }
            "#" === basePrefix && (basePrefix = ""), "r" != type || precision || (type = "g"), null != precision && ("g" == type ? precision = Math.max(1, Math.min(21, precision)) : ("e" == type || "f" == type) && (precision = Math.max(0, Math.min(20, precision)))), type = d3_format_types.get(type) || d3_format_typeDefault;
            var zcomma = zfill && comma;
            return function(value) {
                if (integer && value % 1) return "";
                var negative = 0 > value || 0 === value && 0 > 1 / value ? (value = -value, "-") : sign;
                if (0 > scale) {
                    var prefix = d3.formatPrefix(value, precision);
                    value = prefix.scale(value), suffix = prefix.symbol
                } else value *= scale;
                value = type(value, precision), !zfill && comma && (value = d3_format_group(value));
                var length = basePrefix.length + value.length + (zcomma ? 0 : negative.length),
                    padding = width > length ? new Array(length = width - length + 1).join(fill) : "";
                return zcomma && (value = d3_format_group(padding + value)), d3_format_decimalPoint && value.replace(".", d3_format_decimalPoint), negative += basePrefix, ("<" === align ? negative + value + padding : ">" === align ? padding + negative + value : "^" === align ? padding.substring(0, length >>= 1) + negative + value + padding.substring(length) : negative + (zcomma ? value : padding + value)) + suffix
            }
        };
        var d3_format_re = /(?:([^{])?([<>=^]))?([+\- ])?(#)?(0)?(\d+)?(,)?(\.-?\d+)?([a-z%])?/i,
            d3_format_types = d3.map({
                b: function(x) {
                    return x.toString(2)
                },
                c: function(x) {
                    return String.fromCharCode(x)
                },
                o: function(x) {
                    return x.toString(8)
                },
                x: function(x) {
                    return x.toString(16)
                },
                X: function(x) {
                    return x.toString(16).toUpperCase()
                },
                g: function(x, p) {
                    return x.toPrecision(p)
                },
                e: function(x, p) {
                    return x.toExponential(p)
                },
                f: function(x, p) {
                    return x.toFixed(p)
                },
                r: function(x, p) {
                    return (x = d3.round(x, d3_format_precision(x, p))).toFixed(Math.max(0, Math.min(20, d3_format_precision(x * (1 + 1e-15), p))))
                }
            }),
            d3_format_group = d3_identity;
        if (d3_format_grouping) {
            var d3_format_groupingLength = d3_format_grouping.length;
            d3_format_group = function(value) {
                for (var i = value.lastIndexOf("."), f = i >= 0 ? "." + value.substring(i + 1) : (i = value.length, ""), t = [], j = 0, g = d3_format_grouping[0]; i > 0 && g > 0;) t.push(value.substring(i -= g, i + g)), g = d3_format_grouping[j = (j + 1) % d3_format_groupingLength];
                return t.reverse().join(d3_format_thousandsSeparator || "") + f
            }
        }
        d3.geo = {}, d3.geo.stream = function(object, listener) {
            object && d3_geo_streamObjectType.hasOwnProperty(object.type) ? d3_geo_streamObjectType[object.type](object, listener) : d3_geo_streamGeometry(object, listener)
        };
        var d3_geo_streamObjectType = {
                Feature: function(feature, listener) {
                    d3_geo_streamGeometry(feature.geometry, listener)
                },
                FeatureCollection: function(object, listener) {
                    for (var features = object.features, i = -1, n = features.length; ++i < n;) d3_geo_streamGeometry(features[i].geometry, listener)
                }
            },
            d3_geo_streamGeometryType = {
                Sphere: function(object, listener) {
                    listener.sphere()
                },
                Point: function(object, listener) {
                    var coordinate = object.coordinates;
                    listener.point(coordinate[0], coordinate[1])
                },
                MultiPoint: function(object, listener) {
                    for (var coordinate, coordinates = object.coordinates, i = -1, n = coordinates.length; ++i < n;) coordinate = coordinates[i], listener.point(coordinate[0], coordinate[1])
                },
                LineString: function(object, listener) {
                    d3_geo_streamLine(object.coordinates, listener, 0)
                },
                MultiLineString: function(object, listener) {
                    for (var coordinates = object.coordinates, i = -1, n = coordinates.length; ++i < n;) d3_geo_streamLine(coordinates[i], listener, 0)
                },
                Polygon: function(object, listener) {
                    d3_geo_streamPolygon(object.coordinates, listener)
                },
                MultiPolygon: function(object, listener) {
                    for (var coordinates = object.coordinates, i = -1, n = coordinates.length; ++i < n;) d3_geo_streamPolygon(coordinates[i], listener)
                },
                GeometryCollection: function(object, listener) {
                    for (var geometries = object.geometries, i = -1, n = geometries.length; ++i < n;) d3_geo_streamGeometry(geometries[i], listener)
                }
            };
        d3.geo.area = function(object) {
            return d3_geo_areaSum = 0, d3.geo.stream(object, d3_geo_area), d3_geo_areaSum
        };
        var d3_geo_areaSum, d3_geo_areaRingU, d3_geo_areaRingV, d3_geo_area = {
            sphere: function() {
                d3_geo_areaSum += 4 * π
            },
            point: d3_noop,
            lineStart: d3_noop,
            lineEnd: d3_noop,
            polygonStart: function() {
                d3_geo_areaRingU = 1, d3_geo_areaRingV = 0, d3_geo_area.lineStart = d3_geo_areaRingStart
            },
            polygonEnd: function() {
                var area = 2 * Math.atan2(d3_geo_areaRingV, d3_geo_areaRingU);
                d3_geo_areaSum += 0 > area ? 4 * π + area : area, d3_geo_area.lineStart = d3_geo_area.lineEnd = d3_geo_area.point = d3_noop
            }
        };
        d3.geo.bounds = d3_geo_bounds(d3_identity), d3.geo.centroid = function(object) {
            d3_geo_centroidDimension = d3_geo_centroidW = d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0, d3.geo.stream(object, d3_geo_centroid);
            var m;
            return d3_geo_centroidW && Math.abs(m = Math.sqrt(d3_geo_centroidX * d3_geo_centroidX + d3_geo_centroidY * d3_geo_centroidY + d3_geo_centroidZ * d3_geo_centroidZ)) > ε ? [Math.atan2(d3_geo_centroidY, d3_geo_centroidX) * d3_degrees, Math.asin(Math.max(-1, Math.min(1, d3_geo_centroidZ / m))) * d3_degrees] : void 0
        };
        var d3_geo_centroidDimension, d3_geo_centroidW, d3_geo_centroidX, d3_geo_centroidY, d3_geo_centroidZ, d3_geo_centroid = {
                sphere: function() {
                    2 > d3_geo_centroidDimension && (d3_geo_centroidDimension = 2, d3_geo_centroidW = d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0)
                },
                point: d3_geo_centroidPoint,
                lineStart: d3_geo_centroidLineStart,
                lineEnd: d3_geo_centroidLineEnd,
                polygonStart: function() {
                    2 > d3_geo_centroidDimension && (d3_geo_centroidDimension = 2, d3_geo_centroidW = d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0), d3_geo_centroid.lineStart = d3_geo_centroidRingStart
                },
                polygonEnd: function() {
                    d3_geo_centroid.lineStart = d3_geo_centroidLineStart
                }
            },
            d3_geo_clipAntimeridian = d3_geo_clip(d3_true, d3_geo_clipAntimeridianLine, d3_geo_clipAntimeridianInterpolate),
            d3_geo_clipViewMAX = 1e9;
        d3.geo.projection = d3_geo_projection, d3.geo.projectionMutator = d3_geo_projectionMutator, (d3.geo.equirectangular = function() {
            return d3_geo_projection(d3_geo_equirectangular)
        }).raw = d3_geo_equirectangular.invert = d3_geo_equirectangular, d3.geo.rotation = function(rotate) {
            function forward(coordinates) {
                return coordinates = rotate(coordinates[0] * d3_radians, coordinates[1] * d3_radians), coordinates[0] *= d3_degrees, coordinates[1] *= d3_degrees, coordinates
            }
            return rotate = d3_geo_rotation(rotate[0] % 360 * d3_radians, rotate[1] * d3_radians, rotate.length > 2 ? rotate[2] * d3_radians : 0), forward.invert = function(coordinates) {
                return coordinates = rotate.invert(coordinates[0] * d3_radians, coordinates[1] * d3_radians), coordinates[0] *= d3_degrees, coordinates[1] *= d3_degrees, coordinates
            }, forward
        }, d3.geo.circle = function() {
            function circle() {
                var center = "function" == typeof origin ? origin.apply(this, arguments) : origin,
                    rotate = d3_geo_rotation(-center[0] * d3_radians, -center[1] * d3_radians, 0).invert,
                    ring = [];
                return interpolate(null, null, 1, {
                    point: function(x, y) {
                        ring.push(x = rotate(x, y)), x[0] *= d3_degrees, x[1] *= d3_degrees
                    }
                }), {
                    type: "Polygon",
                    coordinates: [ring]
                }
            }
            var angle, interpolate, origin = [0, 0],
                precision = 6;
            return circle.origin = function(x) {
                return arguments.length ? (origin = x, circle) : origin
            }, circle.angle = function(x) {
                return arguments.length ? (interpolate = d3_geo_circleInterpolate((angle = +x) * d3_radians, precision * d3_radians), circle) : angle
            }, circle.precision = function(_) {
                return arguments.length ? (interpolate = d3_geo_circleInterpolate(angle * d3_radians, (precision = +_) * d3_radians), circle) : precision
            }, circle.angle(90)
        }, d3.geo.distance = function(a, b) {
            var t, Δλ = (b[0] - a[0]) * d3_radians,
                φ0 = a[1] * d3_radians,
                φ1 = b[1] * d3_radians,
                sinΔλ = Math.sin(Δλ),
                cosΔλ = Math.cos(Δλ),
                sinφ0 = Math.sin(φ0),
                cosφ0 = Math.cos(φ0),
                sinφ1 = Math.sin(φ1),
                cosφ1 = Math.cos(φ1);
            return Math.atan2(Math.sqrt((t = cosφ1 * sinΔλ) * t + (t = cosφ0 * sinφ1 - sinφ0 * cosφ1 * cosΔλ) * t), sinφ0 * sinφ1 + cosφ0 * cosφ1 * cosΔλ)
        }, d3.geo.graticule = function() {
            function graticule() {
                return {
                    type: "MultiLineString",
                    coordinates: lines()
                }
            }

            function lines() {
                return d3.range(Math.ceil(X0 / DX) * DX, X1, DX).map(X).concat(d3.range(Math.ceil(Y0 / DY) * DY, Y1, DY).map(Y)).concat(d3.range(Math.ceil(x0 / dx) * dx, x1, dx).filter(function(x) {
                    return Math.abs(x % DX) > ε
                }).map(x)).concat(d3.range(Math.ceil(y0 / dy) * dy, y1, dy).filter(function(y) {
                    return Math.abs(y % DY) > ε
                }).map(y))
            }
            var x1, x0, X1, X0, y1, y0, Y1, Y0, x, y, X, Y, dx = 10,
                dy = dx,
                DX = 90,
                DY = 360,
                precision = 2.5;
            return graticule.lines = function() {
                return lines().map(function(coordinates) {
                    return {
                        type: "LineString",
                        coordinates: coordinates
                    }
                })
            }, graticule.outline = function() {
                return {
                    type: "Polygon",
                    coordinates: [X(X0).concat(Y(Y1).slice(1), X(X1).reverse().slice(1), Y(Y0).reverse().slice(1))]
                }
            }, graticule.extent = function(_) {
                return arguments.length ? graticule.majorExtent(_).minorExtent(_) : graticule.minorExtent()
            }, graticule.majorExtent = function(_) {
                return arguments.length ? (X0 = +_[0][0], X1 = +_[1][0], Y0 = +_[0][1], Y1 = +_[1][1], X0 > X1 && (_ = X0, X0 = X1, X1 = _), Y0 > Y1 && (_ = Y0, Y0 = Y1, Y1 = _), graticule.precision(precision)) : [
                    [X0, Y0],
                    [X1, Y1]
                ]
            }, graticule.minorExtent = function(_) {
                return arguments.length ? (x0 = +_[0][0], x1 = +_[1][0], y0 = +_[0][1], y1 = +_[1][1], x0 > x1 && (_ = x0, x0 = x1, x1 = _), y0 > y1 && (_ = y0, y0 = y1, y1 = _), graticule.precision(precision)) : [
                    [x0, y0],
                    [x1, y1]
                ]
            }, graticule.step = function(_) {
                return arguments.length ? graticule.majorStep(_).minorStep(_) : graticule.minorStep()
            }, graticule.majorStep = function(_) {
                return arguments.length ? (DX = +_[0], DY = +_[1], graticule) : [DX, DY]
            }, graticule.minorStep = function(_) {
                return arguments.length ? (dx = +_[0], dy = +_[1], graticule) : [dx, dy]
            }, graticule.precision = function(_) {
                return arguments.length ? (precision = +_, x = d3_geo_graticuleX(y0, y1, 90), y = d3_geo_graticuleY(x0, x1, precision), X = d3_geo_graticuleX(Y0, Y1, 90), Y = d3_geo_graticuleY(X0, X1, precision), graticule) : precision
            }, graticule.majorExtent([
                [-180, -90 + ε],
                [180, 90 - ε]
            ]).minorExtent([
                [-180, -80 - ε],
                [180, 80 + ε]
            ])
        }, d3.geo.greatArc = function() {
            function greatArc() {
                return {
                    type: "LineString",
                    coordinates: [source_ || source.apply(this, arguments), target_ || target.apply(this, arguments)]
                }
            }
            var source_, target_, source = d3_source,
                target = d3_target;
            return greatArc.distance = function() {
                return d3.geo.distance(source_ || source.apply(this, arguments), target_ || target.apply(this, arguments))
            }, greatArc.source = function(_) {
                return arguments.length ? (source = _, source_ = "function" == typeof _ ? null : _, greatArc) : source
            }, greatArc.target = function(_) {
                return arguments.length ? (target = _, target_ = "function" == typeof _ ? null : _, greatArc) : target
            }, greatArc.precision = function() {
                return arguments.length ? greatArc : 0
            }, greatArc
        }, d3.geo.interpolate = function(source, target) {
            return d3_geo_interpolate(source[0] * d3_radians, source[1] * d3_radians, target[0] * d3_radians, target[1] * d3_radians)
        }, d3.geo.length = function(object) {
            return d3_geo_lengthSum = 0, d3.geo.stream(object, d3_geo_length), d3_geo_lengthSum
        };
        var d3_geo_lengthSum, d3_geo_length = {
            sphere: d3_noop,
            point: d3_noop,
            lineStart: d3_geo_lengthLineStart,
            lineEnd: d3_noop,
            polygonStart: d3_noop,
            polygonEnd: d3_noop
        };
        (d3.geo.conicEqualArea = function() {
            return d3_geo_conic(d3_geo_conicEqualArea)
        }).raw = d3_geo_conicEqualArea, d3.geo.albersUsa = function() {
            function albersUsa(coordinates) {
                return projection(coordinates)(coordinates)
            }

            function projection(point) {
                var lon = point[0],
                    lat = point[1];
                return lat > 50 ? alaska : -140 > lon ? hawaii : 21 > lat ? puertoRico : lower48
            }
            var alaskaInvert, hawaiiInvert, puertoRicoInvert, lower48 = d3.geo.conicEqualArea().rotate([98, 0]).center([0, 38]).parallels([29.5, 45.5]),
                alaska = d3.geo.conicEqualArea().rotate([160, 0]).center([0, 60]).parallels([55, 65]),
                hawaii = d3.geo.conicEqualArea().rotate([160, 0]).center([0, 20]).parallels([8, 18]),
                puertoRico = d3.geo.conicEqualArea().rotate([60, 0]).center([0, 10]).parallels([8, 18]);
            return albersUsa.invert = function(coordinates) {
                return alaskaInvert(coordinates) || hawaiiInvert(coordinates) || puertoRicoInvert(coordinates) || lower48.invert(coordinates)
            }, albersUsa.scale = function(x) {
                return arguments.length ? (lower48.scale(x), alaska.scale(.6 * x), hawaii.scale(x), puertoRico.scale(1.5 * x), albersUsa.translate(lower48.translate())) : lower48.scale()
            }, albersUsa.translate = function(x) {
                if (!arguments.length) return lower48.translate();
                var dz = lower48.scale(),
                    dx = x[0],
                    dy = x[1];
                return lower48.translate(x), alaska.translate([dx - .4 * dz, dy + .17 * dz]), hawaii.translate([dx - .19 * dz, dy + .2 * dz]), puertoRico.translate([dx + .58 * dz, dy + .43 * dz]), alaskaInvert = d3_geo_albersUsaInvert(alaska, [
                    [-180, 50],
                    [-130, 72]
                ]), hawaiiInvert = d3_geo_albersUsaInvert(hawaii, [
                    [-164, 18],
                    [-154, 24]
                ]), puertoRicoInvert = d3_geo_albersUsaInvert(puertoRico, [
                    [-67.5, 17.5],
                    [-65, 19]
                ]), albersUsa
            }, albersUsa.scale(1e3)
        };
        var d3_geo_pathAreaSum, d3_geo_pathAreaPolygon, d3_geo_pathArea = {
                point: d3_noop,
                lineStart: d3_noop,
                lineEnd: d3_noop,
                polygonStart: function() {
                    d3_geo_pathAreaPolygon = 0, d3_geo_pathArea.lineStart = d3_geo_pathAreaRingStart
                },
                polygonEnd: function() {
                    d3_geo_pathArea.lineStart = d3_geo_pathArea.lineEnd = d3_geo_pathArea.point = d3_noop, d3_geo_pathAreaSum += Math.abs(d3_geo_pathAreaPolygon / 2)
                }
            },
            d3_geo_pathCentroid = {
                point: d3_geo_pathCentroidPoint,
                lineStart: d3_geo_pathCentroidLineStart,
                lineEnd: d3_geo_pathCentroidLineEnd,
                polygonStart: function() {
                    d3_geo_pathCentroid.lineStart = d3_geo_pathCentroidRingStart
                },
                polygonEnd: function() {
                    d3_geo_pathCentroid.point = d3_geo_pathCentroidPoint, d3_geo_pathCentroid.lineStart = d3_geo_pathCentroidLineStart, d3_geo_pathCentroid.lineEnd = d3_geo_pathCentroidLineEnd
                }
            };
        d3.geo.path = function() {
            function path(object) {
                return object && d3.geo.stream(object, projectStream(contextStream.pointRadius("function" == typeof pointRadius ? +pointRadius.apply(this, arguments) : pointRadius))), contextStream.result()
            }
            var projection, context, projectStream, contextStream, pointRadius = 4.5;
            return path.area = function(object) {
                return d3_geo_pathAreaSum = 0, d3.geo.stream(object, projectStream(d3_geo_pathArea)), d3_geo_pathAreaSum
            }, path.centroid = function(object) {
                return d3_geo_centroidDimension = d3_geo_centroidX = d3_geo_centroidY = d3_geo_centroidZ = 0, d3.geo.stream(object, projectStream(d3_geo_pathCentroid)), d3_geo_centroidZ ? [d3_geo_centroidX / d3_geo_centroidZ, d3_geo_centroidY / d3_geo_centroidZ] : void 0
            }, path.bounds = function(object) {
                return d3_geo_bounds(projectStream)(object)
            }, path.projection = function(_) {
                return arguments.length ? (projectStream = (projection = _) ? _.stream || d3_geo_pathProjectStream(_) : d3_identity, path) : projection
            }, path.context = function(_) {
                return arguments.length ? (contextStream = null == (context = _) ? new d3_geo_pathBuffer : new d3_geo_pathContext(_), path) : context
            }, path.pointRadius = function(_) {
                return arguments.length ? (pointRadius = "function" == typeof _ ? _ : +_, path) : pointRadius
            }, path.projection(d3.geo.albersUsa()).context(null)
        }, d3.geo.albers = function() {
            return d3.geo.conicEqualArea().parallels([29.5, 45.5]).rotate([98, 0]).center([0, 38]).scale(1e3)
        };
        var d3_geo_azimuthalEqualArea = d3_geo_azimuthal(function(cosλcosφ) {
            return Math.sqrt(2 / (1 + cosλcosφ))
        }, function(ρ) {
            return 2 * Math.asin(ρ / 2)
        });
        (d3.geo.azimuthalEqualArea = function() {
            return d3_geo_projection(d3_geo_azimuthalEqualArea)
        }).raw = d3_geo_azimuthalEqualArea;
        var d3_geo_azimuthalEquidistant = d3_geo_azimuthal(function(cosλcosφ) {
            var c = Math.acos(cosλcosφ);
            return c && c / Math.sin(c)
        }, d3_identity);
        (d3.geo.azimuthalEquidistant = function() {
            return d3_geo_projection(d3_geo_azimuthalEquidistant)
        }).raw = d3_geo_azimuthalEquidistant, (d3.geo.conicConformal = function() {
            return d3_geo_conic(d3_geo_conicConformal)
        }).raw = d3_geo_conicConformal, (d3.geo.conicEquidistant = function() {
            return d3_geo_conic(d3_geo_conicEquidistant)
        }).raw = d3_geo_conicEquidistant;
        var d3_geo_gnomonic = d3_geo_azimuthal(function(cosλcosφ) {
            return 1 / cosλcosφ
        }, Math.atan);
        (d3.geo.gnomonic = function() {
            return d3_geo_projection(d3_geo_gnomonic)
        }).raw = d3_geo_gnomonic, d3_geo_mercator.invert = function(x, y) {
            return [x, 2 * Math.atan(Math.exp(y)) - π / 2]
        }, (d3.geo.mercator = function() {
            return d3_geo_mercatorProjection(d3_geo_mercator)
        }).raw = d3_geo_mercator;
        var d3_geo_orthographic = d3_geo_azimuthal(function() {
            return 1
        }, Math.asin);
        (d3.geo.orthographic = function() {
            return d3_geo_projection(d3_geo_orthographic)
        }).raw = d3_geo_orthographic;
        var d3_geo_stereographic = d3_geo_azimuthal(function(cosλcosφ) {
            return 1 / (1 + cosλcosφ)
        }, function(ρ) {
            return 2 * Math.atan(ρ)
        });
        (d3.geo.stereographic = function() {
            return d3_geo_projection(d3_geo_stereographic)
        }).raw = d3_geo_stereographic, d3_geo_transverseMercator.invert = function(x, y) {
            return [Math.atan2(d3_sinh(x), Math.cos(y)), d3_asin(Math.sin(y) / d3_cosh(x))]
        }, (d3.geo.transverseMercator = function() {
            return d3_geo_mercatorProjection(d3_geo_transverseMercator)
        }).raw = d3_geo_transverseMercator, d3.geom = {}, d3.svg = {}, d3.svg.line = function() {
            return d3_svg_line(d3_identity)
        };
        var d3_svg_lineInterpolators = d3.map({
            linear: d3_svg_lineLinear,
            "linear-closed": d3_svg_lineLinearClosed,
            "step-before": d3_svg_lineStepBefore,
            "step-after": d3_svg_lineStepAfter,
            basis: d3_svg_lineBasis,
            "basis-open": d3_svg_lineBasisOpen,
            "basis-closed": d3_svg_lineBasisClosed,
            bundle: d3_svg_lineBundle,
            cardinal: d3_svg_lineCardinal,
            "cardinal-open": d3_svg_lineCardinalOpen,
            "cardinal-closed": d3_svg_lineCardinalClosed,
            monotone: d3_svg_lineMonotone
        });
        d3_svg_lineInterpolators.forEach(function(key, value) {
            value.key = key, value.closed = /-closed$/.test(key)
        });
        var d3_svg_lineBasisBezier1 = [0, 2 / 3, 1 / 3, 0],
            d3_svg_lineBasisBezier2 = [0, 1 / 3, 2 / 3, 0],
            d3_svg_lineBasisBezier3 = [0, 1 / 6, 2 / 3, 1 / 6];
        d3.geom.hull = function(vertices) {
            function hull(data) {
                if (data.length < 3) return [];
                var vertices, d, i, j, x1, y1, x2, y2, u, v, a, sp, fx = d3_functor(x),
                    fy = d3_functor(y),
                    n = data.length,
                    plen = n - 1,
                    points = [],
                    stack = [],
                    h = 0;
                if (fx === d3_svg_lineX && y === d3_svg_lineY) vertices = data;
                else
                    for (i = 0, vertices = []; n > i; ++i) vertices.push([+fx.call(this, d = data[i], i), +fy.call(this, d, i)]);
                for (i = 1; n > i; ++i)(vertices[i][1] < vertices[h][1] || vertices[i][1] == vertices[h][1] && vertices[i][0] < vertices[h][0]) && (h = i);
                for (i = 0; n > i; ++i) i !== h && (y1 = vertices[i][1] - vertices[h][1], x1 = vertices[i][0] - vertices[h][0], points.push({
                    angle: Math.atan2(y1, x1),
                    index: i
                }));
                for (points.sort(function(a, b) {
                        return a.angle - b.angle
                    }), a = points[0].angle, v = points[0].index, u = 0, i = 1; plen > i; ++i) {
                    if (j = points[i].index, a == points[i].angle) {
                        if (x1 = vertices[v][0] - vertices[h][0], y1 = vertices[v][1] - vertices[h][1], x2 = vertices[j][0] - vertices[h][0], y2 = vertices[j][1] - vertices[h][1], x1 * x1 + y1 * y1 >= x2 * x2 + y2 * y2) {
                            points[i].index = -1;
                            continue
                        }
                        points[u].index = -1
                    }
                    a = points[i].angle, u = i, v = j
                }
                for (stack.push(h), i = 0, j = 0; 2 > i; ++j) points[j].index > -1 && (stack.push(points[j].index), i++);
                for (sp = stack.length; plen > j; ++j)
                    if (!(points[j].index < 0)) {
                        for (; !d3_geom_hullCCW(stack[sp - 2], stack[sp - 1], points[j].index, vertices);) --sp;
                        stack[sp++] = points[j].index
                    }
                var poly = [];
                for (i = sp - 1; i >= 0; --i) poly.push(data[stack[i]]);
                return poly
            }
            var x = d3_svg_lineX,
                y = d3_svg_lineY;
            return arguments.length ? hull(vertices) : (hull.x = function(_) {
                return arguments.length ? (x = _, hull) : x
            }, hull.y = function(_) {
                return arguments.length ? (y = _, hull) : y
            }, hull)
        }, d3.geom.polygon = function(coordinates) {
            return coordinates.area = function() {
                for (var i = 0, n = coordinates.length, area = coordinates[n - 1][1] * coordinates[0][0] - coordinates[n - 1][0] * coordinates[0][1]; ++i < n;) area += coordinates[i - 1][1] * coordinates[i][0] - coordinates[i - 1][0] * coordinates[i][1];
                return .5 * area
            }, coordinates.centroid = function(k) {
                var a, c, i = -1,
                    n = coordinates.length,
                    x = 0,
                    y = 0,
                    b = coordinates[n - 1];
                for (arguments.length || (k = -1 / (6 * coordinates.area())); ++i < n;) a = b, b = coordinates[i], c = a[0] * b[1] - b[0] * a[1], x += (a[0] + b[0]) * c, y += (a[1] + b[1]) * c;
                return [x * k, y * k]
            }, coordinates.clip = function(subject) {
                for (var input, j, m, b, c, d, i = -1, n = coordinates.length, a = coordinates[n - 1]; ++i < n;) {
                    for (input = subject.slice(), subject.length = 0, b = coordinates[i], c = input[(m = input.length) - 1], j = -1; ++j < m;) d = input[j], d3_geom_polygonInside(d, a, b) ? (d3_geom_polygonInside(c, a, b) || subject.push(d3_geom_polygonIntersect(c, d, a, b)), subject.push(d)) : d3_geom_polygonInside(c, a, b) && subject.push(d3_geom_polygonIntersect(c, d, a, b)), c = d;
                    a = b
                }
                return subject
            }, coordinates
        }, d3.geom.delaunay = function(vertices) {
            var edges = vertices.map(function() {
                    return []
                }),
                triangles = [];
            return d3_geom_voronoiTessellate(vertices, function(e) {
                edges[e.region.l.index].push(vertices[e.region.r.index])
            }), edges.forEach(function(edge, i) {
                var v = vertices[i],
                    cx = v[0],
                    cy = v[1];
                edge.forEach(function(v) {
                    v.angle = Math.atan2(v[0] - cx, v[1] - cy)
                }), edge.sort(function(a, b) {
                    return a.angle - b.angle
                });
                for (var j = 0, m = edge.length - 1; m > j; j++) triangles.push([v, edge[j], edge[j + 1]])
            }), triangles
        }, d3.geom.voronoi = function(points) {
            function voronoi(data) {
                var points, d, i, polygons = data.map(function() {
                        return []
                    }),
                    fx = d3_functor(x),
                    fy = d3_functor(y),
                    n = data.length,
                    Z = 1e6;
                if (fx === d3_svg_lineX && fy === d3_svg_lineY) points = data;
                else
                    for (points = [], i = 0; n > i; ++i) points.push([+fx.call(this, d = data[i], i), +fy.call(this, d, i)]);
                if (d3_geom_voronoiTessellate(points, function(e) {
                        var s1, s2, x1, x2, y1, y2;
                        1 === e.a && e.b >= 0 ? (s1 = e.ep.r, s2 = e.ep.l) : (s1 = e.ep.l, s2 = e.ep.r), 1 === e.a ? (y1 = s1 ? s1.y : -Z, x1 = e.c - e.b * y1, y2 = s2 ? s2.y : Z, x2 = e.c - e.b * y2) : (x1 = s1 ? s1.x : -Z, y1 = e.c - e.a * x1, x2 = s2 ? s2.x : Z, y2 = e.c - e.a * x2);
                        var v1 = [x1, y1],
                            v2 = [x2, y2];
                        polygons[e.region.l.index].push(v1, v2), polygons[e.region.r.index].push(v1, v2)
                    }), polygons = polygons.map(function(polygon, i) {
                        var cx = points[i][0],
                            cy = points[i][1],
                            angle = polygon.map(function(v) {
                                return Math.atan2(v[0] - cx, v[1] - cy)
                            }),
                            order = d3.range(polygon.length).sort(function(a, b) {
                                return angle[a] - angle[b]
                            });
                        return order.filter(function(d, i) {
                            return !i || angle[d] - angle[order[i - 1]] > ε
                        }).map(function(d) {
                            return polygon[d]
                        })
                    }), polygons.forEach(function(polygon, i) {
                        var n = polygon.length;
                        if (!n) return polygon.push([-Z, -Z], [-Z, Z], [Z, Z], [Z, -Z]);
                        if (!(n > 2)) {
                            var p0 = points[i],
                                p1 = polygon[0],
                                p2 = polygon[1],
                                x0 = p0[0],
                                y0 = p0[1],
                                x1 = p1[0],
                                y1 = p1[1],
                                x2 = p2[0],
                                y2 = p2[1],
                                dx = Math.abs(x2 - x1),
                                dy = y2 - y1;
                            if (Math.abs(dy) < ε) {
                                var y = y1 > y0 ? -Z : Z;
                                polygon.push([-Z, y], [Z, y])
                            } else if (ε > dx) {
                                var x = x1 > x0 ? -Z : Z;
                                polygon.push([x, -Z], [x, Z])
                            } else {
                                var y = (x1 - x0) * (y2 - y1) > (x2 - x1) * (y1 - y0) ? Z : -Z,
                                    z = Math.abs(dy) - dx;
                                Math.abs(z) < ε ? polygon.push([0 > dy ? y : -y, y]) : (z > 0 && (y *= -1), polygon.push([-Z, y], [Z, y]))
                            }
                        }
                    }), clip)
                    for (i = 0; n > i; ++i) clip(polygons[i]);
                for (i = 0; n > i; ++i) polygons[i].point = data[i];
                return polygons
            }
            var clip, size = null,
                x = d3_svg_lineX,
                y = d3_svg_lineY;
            return arguments.length ? voronoi(points) : (voronoi.x = function(_) {
                return arguments.length ? (x = _, voronoi) : x
            }, voronoi.y = function(_) {
                return arguments.length ? (y = _, voronoi) : y
            }, voronoi.size = function(_) {
                return arguments.length ? (null == _ ? clip = null : (size = [+_[0], +_[1]], clip = d3.geom.polygon([
                    [0, 0],
                    [0, size[1]], size, [size[0], 0]
                ]).clip), voronoi) : size
            }, voronoi.links = function(data) {
                var points, d, i, graph = data.map(function() {
                        return []
                    }),
                    links = [],
                    fx = d3_functor(x),
                    fy = d3_functor(y),
                    n = data.length;
                if (fx === d3_svg_lineX && fy === d3_svg_lineY) points = data;
                else
                    for (i = 0; n > i; ++i) points.push([+fx.call(this, d = data[i], i), +fy.call(this, d, i)]);
                return d3_geom_voronoiTessellate(points, function(e) {
                    var l = e.region.l.index,
                        r = e.region.r.index;
                    graph[l][r] || (graph[l][r] = graph[r][l] = !0, links.push({
                        source: data[l],
                        target: data[r]
                    }))
                }), links
            }, voronoi.triangles = function(data) {
                if (x === d3_svg_lineX && y === d3_svg_lineY) return d3.geom.delaunay(data);
                var points, point, d, i, n, fx = d3_functor(x),
                    fy = d3_functor(y);
                for (i = 0, points = [], n = data.length; n > i; ++i) point = [+fx.call(this, d = data[i], i), +fy.call(this, d, i)], point.data = d, points.push(point);
                return d3.geom.delaunay(points).map(function(triangle) {
                    return triangle.map(function(point) {
                        return point.data
                    })
                })
            }, voronoi)
        };
        var d3_geom_voronoiOpposite = {
            l: "r",
            r: "l"
        };
        d3.geom.quadtree = function(points, x1, y1, x2, y2) {
            function quadtree(data) {
                function insert(n, d, x, y, x1, y1, x2, y2) {
                    if (!isNaN(x) && !isNaN(y))
                        if (n.leaf) {
                            var nx = n.x,
                                ny = n.y;
                            if (null != nx)
                                if (Math.abs(nx - x) + Math.abs(ny - y) < .01) insertChild(n, d, x, y, x1, y1, x2, y2);
                                else {
                                    var nPoint = n.point;
                                    n.x = n.y = n.point = null, insertChild(n, nPoint, nx, ny, x1, y1, x2, y2), insertChild(n, d, x, y, x1, y1, x2, y2)
                                } else n.x = x, n.y = y, n.point = d
                        } else insertChild(n, d, x, y, x1, y1, x2, y2)
                }

                function insertChild(n, d, x, y, x1, y1, x2, y2) {
                    var sx = .5 * (x1 + x2),
                        sy = .5 * (y1 + y2),
                        right = x >= sx,
                        bottom = y >= sy,
                        i = (bottom << 1) + right;
                    n.leaf = !1, n = n.nodes[i] || (n.nodes[i] = d3_geom_quadtreeNode()), right ? x1 = sx : x2 = sx, bottom ? y1 = sy : y2 = sy, insert(n, d, x, y, x1, y1, x2, y2)
                }
                var d, xs, ys, i, n, x1_, y1_, x2_, y2_, fx = d3_functor(x),
                    fy = d3_functor(y);
                if (null != x1) x1_ = x1, y1_ = y1, x2_ = x2, y2_ = y2;
                else if (x2_ = y2_ = -(x1_ = y1_ = 1 / 0), xs = [], ys = [], n = data.length, compat)
                    for (i = 0; n > i; ++i) d = data[i], d.x < x1_ && (x1_ = d.x), d.y < y1_ && (y1_ = d.y), d.x > x2_ && (x2_ = d.x), d.y > y2_ && (y2_ = d.y), xs.push(d.x), ys.push(d.y);
                else
                    for (i = 0; n > i; ++i) {
                        var x_ = +fx(d = data[i], i),
                            y_ = +fy(d, i);
                        x1_ > x_ && (x1_ = x_), y1_ > y_ && (y1_ = y_), x_ > x2_ && (x2_ = x_), y_ > y2_ && (y2_ = y_), xs.push(x_), ys.push(y_)
                    }
                var dx = x2_ - x1_,
                    dy = y2_ - y1_;
                dx > dy ? y2_ = y1_ + dx : x2_ = x1_ + dy;
                var root = d3_geom_quadtreeNode();
                if (root.add = function(d) {
                        insert(root, d, +fx(d, ++i), +fy(d, i), x1_, y1_, x2_, y2_)
                    }, root.visit = function(f) {
                        d3_geom_quadtreeVisit(f, root, x1_, y1_, x2_, y2_)
                    }, i = -1, null == x1) {
                    for (; ++i < n;) insert(root, data[i], xs[i], ys[i], x1_, y1_, x2_, y2_);
                    --i
                } else data.forEach(root.add);
                return xs = ys = data = d = null, root
            }
            var compat, x = d3_svg_lineX,
                y = d3_svg_lineY;
            return (compat = arguments.length) ? (x = d3_geom_quadtreeCompatX, y = d3_geom_quadtreeCompatY, 3 === compat && (y2 = y1, x2 = x1, y1 = x1 = 0), quadtree(points)) : (quadtree.x = function(_) {
                return arguments.length ? (x = _, quadtree) : x
            }, quadtree.y = function(_) {
                return arguments.length ? (y = _, quadtree) : y
            }, quadtree.size = function(_) {
                return arguments.length ? (null == _ ? x1 = y1 = x2 = y2 = null : (x1 = y1 = 0, x2 = +_[0], y2 = +_[1]), quadtree) : null == x1 ? null : [x2, y2]
            }, quadtree)
        }, d3.interpolateRgb = d3_interpolateRgb, d3.transform = function(string) {
            var g = d3_document.createElementNS(d3.ns.prefix.svg, "g");
            return (d3.transform = function(string) {
                g.setAttribute("transform", string);
                var t = g.transform.baseVal.consolidate();
                return new d3_transform(t ? t.matrix : d3_transformIdentity)
            })(string)
        }, d3_transform.prototype.toString = function() {
            return "translate(" + this.translate + ")rotate(" + this.rotate + ")skewX(" + this.skew + ")scale(" + this.scale + ")"
        };
        var d3_transformIdentity = {
            a: 1,
            b: 0,
            c: 0,
            d: 1,
            e: 0,
            f: 0
        };
        d3.interpolateNumber = d3_interpolateNumber, d3.interpolateTransform = d3_interpolateTransform, d3.interpolateObject = d3_interpolateObject, d3.interpolateString = d3_interpolateString;
        var d3_interpolate_number = /[-+]?(?:\d+\.?\d*|\.?\d+)(?:[eE][-+]?\d+)?/g;
        d3.interpolate = d3_interpolate, d3.interpolators = [function(a, b) {
            var t = typeof b;
            return ("string" === t || t !== typeof a ? d3_rgb_names.has(b) || /^(#|rgb\(|hsl\()/.test(b) ? d3_interpolateRgb : d3_interpolateString : b instanceof d3_Color ? d3_interpolateRgb : "object" === t ? Array.isArray(b) ? d3_interpolateArray : d3_interpolateObject : d3_interpolateNumber)(a, b)
        }], d3.interpolateArray = d3_interpolateArray;
        var d3_ease_default = function() {
                return d3_identity
            },
            d3_ease = d3.map({
                linear: d3_ease_default,
                poly: d3_ease_poly,
                quad: function() {
                    return d3_ease_quad
                },
                cubic: function() {
                    return d3_ease_cubic
                },
                sin: function() {
                    return d3_ease_sin
                },
                exp: function() {
                    return d3_ease_exp
                },
                circle: function() {
                    return d3_ease_circle
                },
                elastic: d3_ease_elastic,
                back: d3_ease_back,
                bounce: function() {
                    return d3_ease_bounce
                }
            }),
            d3_ease_mode = d3.map({
                "in": d3_identity,
                out: d3_ease_reverse,
                "in-out": d3_ease_reflect,
                "out-in": function(f) {
                    return d3_ease_reflect(d3_ease_reverse(f))
                }
            });
        d3.ease = function(name) {
            var i = name.indexOf("-"),
                t = i >= 0 ? name.substring(0, i) : name,
                m = i >= 0 ? name.substring(i + 1) : "in";
            return t = d3_ease.get(t) || d3_ease_default, m = d3_ease_mode.get(m) || d3_identity, d3_ease_clamp(m(t.apply(null, Array.prototype.slice.call(arguments, 1))))
        }, d3.interpolateHcl = d3_interpolateHcl, d3.interpolateHsl = d3_interpolateHsl, d3.interpolateLab = d3_interpolateLab, d3.interpolateRound = d3_interpolateRound, d3.layout = {}, d3.layout.bundle = function() {
            return function(links) {
                for (var paths = [], i = -1, n = links.length; ++i < n;) paths.push(d3_layout_bundlePath(links[i]));
                return paths
            }
        }, d3.layout.chord = function() {
            function relayout() {
                var k, x, x0, i, j, subgroups = {},
                    groupSums = [],
                    groupIndex = d3.range(n),
                    subgroupIndex = [];
                for (chords = [], groups = [], k = 0, i = -1; ++i < n;) {
                    for (x = 0, j = -1; ++j < n;) x += matrix[i][j];
                    groupSums.push(x), subgroupIndex.push(d3.range(n)), k += x
                }
                for (sortGroups && groupIndex.sort(function(a, b) {
                        return sortGroups(groupSums[a], groupSums[b])
                    }), sortSubgroups && subgroupIndex.forEach(function(d, i) {
                        d.sort(function(a, b) {
                            return sortSubgroups(matrix[i][a], matrix[i][b])
                        })
                    }), k = (2 * π - padding * n) / k, x = 0, i = -1; ++i < n;) {
                    for (x0 = x, j = -1; ++j < n;) {
                        var di = groupIndex[i],
                            dj = subgroupIndex[di][j],
                            v = matrix[di][dj],
                            a0 = x,
                            a1 = x += v * k;
                        subgroups[di + "-" + dj] = {
                            index: di,
                            subindex: dj,
                            startAngle: a0,
                            endAngle: a1,
                            value: v
                        }
                    }
                    groups[di] = {
                        index: di,
                        startAngle: x0,
                        endAngle: x,
                        value: (x - x0) / k
                    }, x += padding
                }
                for (i = -1; ++i < n;)
                    for (j = i - 1; ++j < n;) {
                        var source = subgroups[i + "-" + j],
                            target = subgroups[j + "-" + i];
                        (source.value || target.value) && chords.push(source.value < target.value ? {
                            source: target,
                            target: source
                        } : {
                            source: source,
                            target: target
                        })
                    }
                sortChords && resort()
            }

            function resort() {
                chords.sort(function(a, b) {
                    return sortChords((a.source.value + a.target.value) / 2, (b.source.value + b.target.value) / 2)
                })
            }
            var chords, groups, matrix, n, sortGroups, sortSubgroups, sortChords, chord = {},
                padding = 0;
            return chord.matrix = function(x) {
                return arguments.length ? (n = (matrix = x) && matrix.length, chords = groups = null, chord) : matrix
            }, chord.padding = function(x) {
                return arguments.length ? (padding = x, chords = groups = null, chord) : padding
            }, chord.sortGroups = function(x) {
                return arguments.length ? (sortGroups = x, chords = groups = null, chord) : sortGroups
            }, chord.sortSubgroups = function(x) {
                return arguments.length ? (sortSubgroups = x, chords = null, chord) : sortSubgroups
            }, chord.sortChords = function(x) {
                return arguments.length ? (sortChords = x, chords && resort(), chord) : sortChords
            }, chord.chords = function() {
                return chords || relayout(), chords
            }, chord.groups = function() {
                return groups || relayout(), groups
            }, chord
        }, d3.layout.force = function() {
            function repulse(node) {
                return function(quad, x1, _, x2) {
                    if (quad.point !== node) {
                        var dx = quad.cx - node.x,
                            dy = quad.cy - node.y,
                            dn = 1 / Math.sqrt(dx * dx + dy * dy);
                        if (theta > (x2 - x1) * dn) {
                            var k = quad.charge * dn * dn;
                            return node.px -= dx * k, node.py -= dy * k, !0
                        }
                        if (quad.point && isFinite(dn)) {
                            var k = quad.pointCharge * dn * dn;
                            node.px -= dx * k, node.py -= dy * k
                        }
                    }
                    return !quad.charge
                }
            }

            function dragmove(d) {
                d.px = d3.event.x, d.py = d3.event.y, force.resume()
            }
            var drag, alpha, distances, strengths, charges, force = {},
                event = d3.dispatch("start", "tick", "end"),
                size = [1, 1],
                friction = .9,
                linkDistance = d3_layout_forceLinkDistance,
                linkStrength = d3_layout_forceLinkStrength,
                charge = -30,
                gravity = .1,
                theta = .8,
                nodes = [],
                links = [];
            return force.tick = function() {
                if ((alpha *= .99) < .005) return event.end({
                    type: "end",
                    alpha: alpha = 0
                }), !0;
                var q, i, o, s, t, l, k, x, y, n = nodes.length,
                    m = links.length;
                for (i = 0; m > i; ++i) o = links[i], s = o.source, t = o.target, x = t.x - s.x, y = t.y - s.y, (l = x * x + y * y) && (l = alpha * strengths[i] * ((l = Math.sqrt(l)) - distances[i]) / l, x *= l, y *= l, t.x -= x * (k = s.weight / (t.weight + s.weight)), t.y -= y * k, s.x += x * (k = 1 - k), s.y += y * k);
                if ((k = alpha * gravity) && (x = size[0] / 2, y = size[1] / 2, i = -1, k))
                    for (; ++i < n;) o = nodes[i], o.x += (x - o.x) * k, o.y += (y - o.y) * k;
                if (charge)
                    for (d3_layout_forceAccumulate(q = d3.geom.quadtree(nodes), alpha, charges), i = -1; ++i < n;)(o = nodes[i]).fixed || q.visit(repulse(o));
                for (i = -1; ++i < n;) o = nodes[i], o.fixed ? (o.x = o.px, o.y = o.py) : (o.x -= (o.px - (o.px = o.x)) * friction, o.y -= (o.py - (o.py = o.y)) * friction);
                event.tick({
                    type: "tick",
                    alpha: alpha
                })
            }, force.nodes = function(x) {
                return arguments.length ? (nodes = x, force) : nodes
            }, force.links = function(x) {
                return arguments.length ? (links = x, force) : links
            }, force.size = function(x) {
                return arguments.length ? (size = x, force) : size
            }, force.linkDistance = function(x) {
                return arguments.length ? (linkDistance = "function" == typeof x ? x : +x, force) : linkDistance
            }, force.distance = force.linkDistance, force.linkStrength = function(x) {
                return arguments.length ? (linkStrength = "function" == typeof x ? x : +x, force) : linkStrength
            }, force.friction = function(x) {
                return arguments.length ? (friction = +x, force) : friction
            }, force.charge = function(x) {
                return arguments.length ? (charge = "function" == typeof x ? x : +x, force) : charge
            }, force.gravity = function(x) {
                return arguments.length ? (gravity = +x, force) : gravity
            }, force.theta = function(x) {
                return arguments.length ? (theta = +x, force) : theta
            }, force.alpha = function(x) {
                return arguments.length ? (x = +x, alpha ? alpha = x > 0 ? x : 0 : x > 0 && (event.start({
                    type: "start",
                    alpha: alpha = x
                }), d3.timer(force.tick)), force) : alpha
            }, force.start = function() {
                function position(dimension, size) {
                    for (var x, neighbors = neighbor(i), j = -1, m = neighbors.length; ++j < m;)
                        if (!isNaN(x = neighbors[j][dimension])) return x;
                    return Math.random() * size
                }

                function neighbor() {
                    if (!neighbors) {
                        for (neighbors = [], j = 0; n > j; ++j) neighbors[j] = [];
                        for (j = 0; m > j; ++j) {
                            var o = links[j];
                            neighbors[o.source.index].push(o.target), neighbors[o.target.index].push(o.source)
                        }
                    }
                    return neighbors[i]
                }
                var i, j, neighbors, o, n = nodes.length,
                    m = links.length,
                    w = size[0],
                    h = size[1];
                for (i = 0; n > i; ++i)(o = nodes[i]).index = i, o.weight = 0;
                for (i = 0; m > i; ++i) o = links[i], "number" == typeof o.source && (o.source = nodes[o.source]), "number" == typeof o.target && (o.target = nodes[o.target]), ++o.source.weight, ++o.target.weight;
                for (i = 0; n > i; ++i) o = nodes[i], isNaN(o.x) && (o.x = position("x", w)), isNaN(o.y) && (o.y = position("y", h)), isNaN(o.px) && (o.px = o.x), isNaN(o.py) && (o.py = o.y);
                if (distances = [], "function" == typeof linkDistance)
                    for (i = 0; m > i; ++i) distances[i] = +linkDistance.call(this, links[i], i);
                else
                    for (i = 0; m > i; ++i) distances[i] = linkDistance;
                if (strengths = [], "function" == typeof linkStrength)
                    for (i = 0; m > i; ++i) strengths[i] = +linkStrength.call(this, links[i], i);
                else
                    for (i = 0; m > i; ++i) strengths[i] = linkStrength;
                if (charges = [], "function" == typeof charge)
                    for (i = 0; n > i; ++i) charges[i] = +charge.call(this, nodes[i], i);
                else
                    for (i = 0; n > i; ++i) charges[i] = charge;
                return force.resume()
            }, force.resume = function() {
                return force.alpha(.1)
            }, force.stop = function() {
                return force.alpha(0)
            }, force.drag = function() {
                return drag || (drag = d3.behavior.drag().origin(d3_identity).on("dragstart.force", d3_layout_forceDragstart).on("drag.force", dragmove).on("dragend.force", d3_layout_forceDragend)), arguments.length ? void this.on("mouseover.force", d3_layout_forceMouseover).on("mouseout.force", d3_layout_forceMouseout).call(drag) : drag
            }, d3.rebind(force, event, "on")
        };
        var d3_layout_forceLinkDistance = 20,
            d3_layout_forceLinkStrength = 1;
        d3.layout.hierarchy = function() {
            function recurse(node, depth, nodes) {
                var childs = children.call(hierarchy, node, depth);
                if (node.depth = depth, nodes.push(node), childs && (n = childs.length)) {
                    for (var n, d, i = -1, c = node.children = [], v = 0, j = depth + 1; ++i < n;) d = recurse(childs[i], j, nodes), d.parent = node, c.push(d), v += d.value;
                    sort && c.sort(sort), value && (node.value = v)
                } else value && (node.value = +value.call(hierarchy, node, depth) || 0);
                return node
            }

            function revalue(node, depth) {
                var children = node.children,
                    v = 0;
                if (children && (n = children.length))
                    for (var n, i = -1, j = depth + 1; ++i < n;) v += revalue(children[i], j);
                else value && (v = +value.call(hierarchy, node, depth) || 0);
                return value && (node.value = v), v
            }

            function hierarchy(d) {
                var nodes = [];
                return recurse(d, 0, nodes), nodes
            }
            var sort = d3_layout_hierarchySort,
                children = d3_layout_hierarchyChildren,
                value = d3_layout_hierarchyValue;
            return hierarchy.sort = function(x) {
                return arguments.length ? (sort = x, hierarchy) : sort
            }, hierarchy.children = function(x) {
                return arguments.length ? (children = x, hierarchy) : children
            }, hierarchy.value = function(x) {
                return arguments.length ? (value = x, hierarchy) : value
            }, hierarchy.revalue = function(root) {
                return revalue(root, 0), root
            }, hierarchy
        }, d3.layout.partition = function() {
            function position(node, x, dx, dy) {
                var children = node.children;
                if (node.x = x, node.y = node.depth * dy, node.dx = dx, node.dy = dy, children && (n = children.length)) {
                    var n, c, d, i = -1;
                    for (dx = node.value ? dx / node.value : 0; ++i < n;) position(c = children[i], x, d = c.value * dx, dy), x += d
                }
            }

            function depth(node) {
                var children = node.children,
                    d = 0;
                if (children && (n = children.length))
                    for (var n, i = -1; ++i < n;) d = Math.max(d, depth(children[i]));
                return 1 + d
            }

            function partition(d, i) {
                var nodes = hierarchy.call(this, d, i);
                return position(nodes[0], 0, size[0], size[1] / depth(nodes[0])), nodes
            }
            var hierarchy = d3.layout.hierarchy(),
                size = [1, 1];
            return partition.size = function(x) {
                return arguments.length ? (size = x, partition) : size
            }, d3_layout_hierarchyRebind(partition, hierarchy)
        }, d3.layout.pie = function() {
            function pie(data) {
                var values = data.map(function(d, i) {
                        return +value.call(pie, d, i)
                    }),
                    a = +("function" == typeof startAngle ? startAngle.apply(this, arguments) : startAngle),
                    k = (("function" == typeof endAngle ? endAngle.apply(this, arguments) : endAngle) - a) / d3.sum(values),
                    index = d3.range(data.length);
                null != sort && index.sort(sort === d3_layout_pieSortByValue ? function(i, j) {
                    return values[j] - values[i]
                } : function(i, j) {
                    return sort(data[i], data[j])
                });
                var arcs = [];
                return index.forEach(function(i) {
                    var d;
                    arcs[i] = {
                        data: data[i],
                        value: d = values[i],
                        startAngle: a,
                        endAngle: a += d * k
                    }
                }), arcs
            }
            var value = Number,
                sort = d3_layout_pieSortByValue,
                startAngle = 0,
                endAngle = 2 * π;
            return pie.value = function(x) {
                return arguments.length ? (value = x, pie) : value
            }, pie.sort = function(x) {
                return arguments.length ? (sort = x, pie) : sort
            }, pie.startAngle = function(x) {
                return arguments.length ? (startAngle = x, pie) : startAngle
            }, pie.endAngle = function(x) {
                return arguments.length ? (endAngle = x, pie) : endAngle
            }, pie
        };
        var d3_layout_pieSortByValue = {};
        d3.layout.stack = function() {
            function stack(data, index) {
                var series = data.map(function(d, i) {
                        return values.call(stack, d, i)
                    }),
                    points = series.map(function(d) {
                        return d.map(function(v, i) {
                            return [x.call(stack, v, i), y.call(stack, v, i)]
                        })
                    }),
                    orders = order.call(stack, points, index);
                series = d3.permute(series, orders), points = d3.permute(points, orders);
                var i, j, o, offsets = offset.call(stack, points, index),
                    n = series.length,
                    m = series[0].length;
                for (j = 0; m > j; ++j)
                    for (out.call(stack, series[0][j], o = offsets[j], points[0][j][1]), i = 1; n > i; ++i) out.call(stack, series[i][j], o += points[i - 1][j][1], points[i][j][1]);
                return data
            }
            var values = d3_identity,
                order = d3_layout_stackOrderDefault,
                offset = d3_layout_stackOffsetZero,
                out = d3_layout_stackOut,
                x = d3_layout_stackX,
                y = d3_layout_stackY;
            return stack.values = function(x) {
                return arguments.length ? (values = x, stack) : values
            }, stack.order = function(x) {
                return arguments.length ? (order = "function" == typeof x ? x : d3_layout_stackOrders.get(x) || d3_layout_stackOrderDefault, stack) : order
            }, stack.offset = function(x) {
                return arguments.length ? (offset = "function" == typeof x ? x : d3_layout_stackOffsets.get(x) || d3_layout_stackOffsetZero, stack) : offset
            }, stack.x = function(z) {
                return arguments.length ? (x = z, stack) : x
            }, stack.y = function(z) {
                return arguments.length ? (y = z, stack) : y
            }, stack.out = function(z) {
                return arguments.length ? (out = z, stack) : out
            }, stack
        };
        var d3_layout_stackOrders = d3.map({
                "inside-out": function(data) {
                    var i, j, n = data.length,
                        max = data.map(d3_layout_stackMaxIndex),
                        sums = data.map(d3_layout_stackReduceSum),
                        index = d3.range(n).sort(function(a, b) {
                            return max[a] - max[b]
                        }),
                        top = 0,
                        bottom = 0,
                        tops = [],
                        bottoms = [];
                    for (i = 0; n > i; ++i) j = index[i], bottom > top ? (top += sums[j], tops.push(j)) : (bottom += sums[j], bottoms.push(j));
                    return bottoms.reverse().concat(tops)
                },
                reverse: function(data) {
                    return d3.range(data.length).reverse()
                },
                "default": d3_layout_stackOrderDefault
            }),
            d3_layout_stackOffsets = d3.map({
                silhouette: function(data) {
                    var i, j, o, n = data.length,
                        m = data[0].length,
                        sums = [],
                        max = 0,
                        y0 = [];
                    for (j = 0; m > j; ++j) {
                        for (i = 0, o = 0; n > i; i++) o += data[i][j][1];
                        o > max && (max = o), sums.push(o)
                    }
                    for (j = 0; m > j; ++j) y0[j] = (max - sums[j]) / 2;
                    return y0
                },
                wiggle: function(data) {
                    var i, j, k, s1, s2, s3, dx, o, o0, n = data.length,
                        x = data[0],
                        m = x.length,
                        y0 = [];
                    for (y0[0] = o = o0 = 0, j = 1; m > j; ++j) {
                        for (i = 0, s1 = 0; n > i; ++i) s1 += data[i][j][1];
                        for (i = 0, s2 = 0, dx = x[j][0] - x[j - 1][0]; n > i; ++i) {
                            for (k = 0, s3 = (data[i][j][1] - data[i][j - 1][1]) / (2 * dx); i > k; ++k) s3 += (data[k][j][1] - data[k][j - 1][1]) / dx;
                            s2 += s3 * data[i][j][1]
                        }
                        y0[j] = o -= s1 ? s2 / s1 * dx : 0, o0 > o && (o0 = o)
                    }
                    for (j = 0; m > j; ++j) y0[j] -= o0;
                    return y0
                },
                expand: function(data) {
                    var i, j, o, n = data.length,
                        m = data[0].length,
                        k = 1 / n,
                        y0 = [];
                    for (j = 0; m > j; ++j) {
                        for (i = 0, o = 0; n > i; i++) o += data[i][j][1];
                        if (o)
                            for (i = 0; n > i; i++) data[i][j][1] /= o;
                        else
                            for (i = 0; n > i; i++) data[i][j][1] = k
                    }
                    for (j = 0; m > j; ++j) y0[j] = 0;
                    return y0
                },
                zero: d3_layout_stackOffsetZero
            });
        d3.layout.histogram = function() {
            function histogram(data, i) {
                for (var bin, x, bins = [], values = data.map(valuer, this), range = ranger.call(this, values, i), thresholds = binner.call(this, range, values, i), i = -1, n = values.length, m = thresholds.length - 1, k = frequency ? 1 : 1 / n; ++i < m;) bin = bins[i] = [], bin.dx = thresholds[i + 1] - (bin.x = thresholds[i]), bin.y = 0;
                if (m > 0)
                    for (i = -1; ++i < n;) x = values[i], x >= range[0] && x <= range[1] && (bin = bins[d3.bisect(thresholds, x, 1, m) - 1], bin.y += k, bin.push(data[i]));
                return bins
            }
            var frequency = !0,
                valuer = Number,
                ranger = d3_layout_histogramRange,
                binner = d3_layout_histogramBinSturges;
            return histogram.value = function(x) {
                return arguments.length ? (valuer = x, histogram) : valuer
            }, histogram.range = function(x) {
                return arguments.length ? (ranger = d3_functor(x), histogram) : ranger
            }, histogram.bins = function(x) {
                return arguments.length ? (binner = "number" == typeof x ? function(range) {
                    return d3_layout_histogramBinFixed(range, x)
                } : d3_functor(x), histogram) : binner
            }, histogram.frequency = function(x) {
                return arguments.length ? (frequency = !!x, histogram) : frequency
            }, histogram
        }, d3.layout.tree = function() {
            function tree(d, i) {
                function firstWalk(node, previousSibling) {
                    var children = node.children,
                        layout = node._tree;
                    if (children && (n = children.length)) {
                        for (var n, previousChild, child, firstChild = children[0], ancestor = firstChild, i = -1; ++i < n;) child = children[i], firstWalk(child, previousChild), ancestor = apportion(child, previousChild, ancestor), previousChild = child;
                        d3_layout_treeShift(node);
                        var midpoint = .5 * (firstChild._tree.prelim + child._tree.prelim);
                        previousSibling ? (layout.prelim = previousSibling._tree.prelim + separation(node, previousSibling), layout.mod = layout.prelim - midpoint) : layout.prelim = midpoint
                    } else previousSibling && (layout.prelim = previousSibling._tree.prelim + separation(node, previousSibling))
                }

                function secondWalk(node, x) {
                    node.x = node._tree.prelim + x;
                    var children = node.children;
                    if (children && (n = children.length)) {
                        var n, i = -1;
                        for (x += node._tree.mod; ++i < n;) secondWalk(children[i], x)
                    }
                }

                function apportion(node, previousSibling, ancestor) {
                    if (previousSibling) {
                        for (var shift, vip = node, vop = node, vim = previousSibling, vom = node.parent.children[0], sip = vip._tree.mod, sop = vop._tree.mod, sim = vim._tree.mod, som = vom._tree.mod; vim = d3_layout_treeRight(vim), vip = d3_layout_treeLeft(vip), vim && vip;) vom = d3_layout_treeLeft(vom), vop = d3_layout_treeRight(vop), vop._tree.ancestor = node, shift = vim._tree.prelim + sim - vip._tree.prelim - sip + separation(vim, vip), shift > 0 && (d3_layout_treeMove(d3_layout_treeAncestor(vim, node, ancestor), node, shift), sip += shift, sop += shift), sim += vim._tree.mod, sip += vip._tree.mod, som += vom._tree.mod, sop += vop._tree.mod;
                        vim && !d3_layout_treeRight(vop) && (vop._tree.thread = vim, vop._tree.mod += sim - sop), vip && !d3_layout_treeLeft(vom) && (vom._tree.thread = vip, vom._tree.mod += sip - som, ancestor = node)
                    }
                    return ancestor
                }
                var nodes = hierarchy.call(this, d, i),
                    root = nodes[0];
                d3_layout_treeVisitAfter(root, function(node, previousSibling) {
                    node._tree = {
                        ancestor: node,
                        prelim: 0,
                        mod: 0,
                        change: 0,
                        shift: 0,
                        number: previousSibling ? previousSibling._tree.number + 1 : 0
                    }
                }), firstWalk(root), secondWalk(root, -root._tree.prelim);
                var left = d3_layout_treeSearch(root, d3_layout_treeLeftmost),
                    right = d3_layout_treeSearch(root, d3_layout_treeRightmost),
                    deep = d3_layout_treeSearch(root, d3_layout_treeDeepest),
                    x0 = left.x - separation(left, right) / 2,
                    x1 = right.x + separation(right, left) / 2,
                    y1 = deep.depth || 1;
                return d3_layout_treeVisitAfter(root, function(node) {
                    node.x = (node.x - x0) / (x1 - x0) * size[0], node.y = node.depth / y1 * size[1], delete node._tree
                }), nodes
            }
            var hierarchy = d3.layout.hierarchy().sort(null).value(null),
                separation = d3_layout_treeSeparation,
                size = [1, 1];
            return tree.separation = function(x) {
                return arguments.length ? (separation = x, tree) : separation
            }, tree.size = function(x) {
                return arguments.length ? (size = x, tree) : size
            }, d3_layout_hierarchyRebind(tree, hierarchy)
        }, d3.layout.pack = function() {
            function pack(d, i) {
                var nodes = hierarchy.call(this, d, i),
                    root = nodes[0];
                root.x = 0, root.y = 0, d3_layout_treeVisitAfter(root, function(d) {
                    d.r = Math.sqrt(d.value)
                }), d3_layout_treeVisitAfter(root, d3_layout_packSiblings);
                var w = size[0],
                    h = size[1],
                    k = Math.max(2 * root.r / w, 2 * root.r / h);
                if (padding > 0) {
                    var dr = padding * k / 2;
                    d3_layout_treeVisitAfter(root, function(d) {
                        d.r += dr
                    }), d3_layout_treeVisitAfter(root, d3_layout_packSiblings), d3_layout_treeVisitAfter(root, function(d) {
                        d.r -= dr
                    }), k = Math.max(2 * root.r / w, 2 * root.r / h)
                }
                return d3_layout_packTransform(root, w / 2, h / 2, 1 / k), nodes
            }
            var hierarchy = d3.layout.hierarchy().sort(d3_layout_packSort),
                padding = 0,
                size = [1, 1];
            return pack.size = function(x) {
                return arguments.length ? (size = x, pack) : size
            }, pack.padding = function(_) {
                return arguments.length ? (padding = +_, pack) : padding
            }, d3_layout_hierarchyRebind(pack, hierarchy)
        }, d3.layout.cluster = function() {
            function cluster(d, i) {
                var previousNode, nodes = hierarchy.call(this, d, i),
                    root = nodes[0],
                    x = 0;
                d3_layout_treeVisitAfter(root, function(node) {
                    var children = node.children;
                    children && children.length ? (node.x = d3_layout_clusterX(children), node.y = d3_layout_clusterY(children)) : (node.x = previousNode ? x += separation(node, previousNode) : 0,
                        node.y = 0, previousNode = node)
                });
                var left = d3_layout_clusterLeft(root),
                    right = d3_layout_clusterRight(root),
                    x0 = left.x - separation(left, right) / 2,
                    x1 = right.x + separation(right, left) / 2;
                return d3_layout_treeVisitAfter(root, function(node) {
                    node.x = (node.x - x0) / (x1 - x0) * size[0], node.y = (1 - (root.y ? node.y / root.y : 1)) * size[1]
                }), nodes
            }
            var hierarchy = d3.layout.hierarchy().sort(null).value(null),
                separation = d3_layout_treeSeparation,
                size = [1, 1];
            return cluster.separation = function(x) {
                return arguments.length ? (separation = x, cluster) : separation
            }, cluster.size = function(x) {
                return arguments.length ? (size = x, cluster) : size
            }, d3_layout_hierarchyRebind(cluster, hierarchy)
        }, d3.layout.treemap = function() {
            function scale(children, k) {
                for (var child, area, i = -1, n = children.length; ++i < n;) area = (child = children[i]).value * (0 > k ? 0 : k), child.area = isNaN(area) || 0 >= area ? 0 : area
            }

            function squarify(node) {
                var children = node.children;
                if (children && children.length) {
                    var child, score, n, rect = pad(node),
                        row = [],
                        remaining = children.slice(),
                        best = 1 / 0,
                        u = "slice" === mode ? rect.dx : "dice" === mode ? rect.dy : "slice-dice" === mode ? 1 & node.depth ? rect.dy : rect.dx : Math.min(rect.dx, rect.dy);
                    for (scale(remaining, rect.dx * rect.dy / node.value), row.area = 0;
                        (n = remaining.length) > 0;) row.push(child = remaining[n - 1]), row.area += child.area, "squarify" !== mode || (score = worst(row, u)) <= best ? (remaining.pop(), best = score) : (row.area -= row.pop().area, position(row, u, rect, !1), u = Math.min(rect.dx, rect.dy), row.length = row.area = 0, best = 1 / 0);
                    row.length && (position(row, u, rect, !0), row.length = row.area = 0), children.forEach(squarify)
                }
            }

            function stickify(node) {
                var children = node.children;
                if (children && children.length) {
                    var child, rect = pad(node),
                        remaining = children.slice(),
                        row = [];
                    for (scale(remaining, rect.dx * rect.dy / node.value), row.area = 0; child = remaining.pop();) row.push(child), row.area += child.area, null != child.z && (position(row, child.z ? rect.dx : rect.dy, rect, !remaining.length), row.length = row.area = 0);
                    children.forEach(stickify)
                }
            }

            function worst(row, u) {
                for (var r, s = row.area, rmax = 0, rmin = 1 / 0, i = -1, n = row.length; ++i < n;)(r = row[i].area) && (rmin > r && (rmin = r), r > rmax && (rmax = r));
                return s *= s, u *= u, s ? Math.max(u * rmax * ratio / s, s / (u * rmin * ratio)) : 1 / 0
            }

            function position(row, u, rect, flush) {
                var o, i = -1,
                    n = row.length,
                    x = rect.x,
                    y = rect.y,
                    v = u ? round(row.area / u) : 0;
                if (u == rect.dx) {
                    for ((flush || v > rect.dy) && (v = rect.dy); ++i < n;) o = row[i], o.x = x, o.y = y, o.dy = v, x += o.dx = Math.min(rect.x + rect.dx - x, v ? round(o.area / v) : 0);
                    o.z = !0, o.dx += rect.x + rect.dx - x, rect.y += v, rect.dy -= v
                } else {
                    for ((flush || v > rect.dx) && (v = rect.dx); ++i < n;) o = row[i], o.x = x, o.y = y, o.dx = v, y += o.dy = Math.min(rect.y + rect.dy - y, v ? round(o.area / v) : 0);
                    o.z = !1, o.dy += rect.y + rect.dy - y, rect.x += v, rect.dx -= v
                }
            }

            function treemap(d) {
                var nodes = stickies || hierarchy(d),
                    root = nodes[0];
                return root.x = 0, root.y = 0, root.dx = size[0], root.dy = size[1], stickies && hierarchy.revalue(root), scale([root], root.dx * root.dy / root.value), (stickies ? stickify : squarify)(root), sticky && (stickies = nodes), nodes
            }
            var stickies, hierarchy = d3.layout.hierarchy(),
                round = Math.round,
                size = [1, 1],
                padding = null,
                pad = d3_layout_treemapPadNull,
                sticky = !1,
                mode = "squarify",
                ratio = .5 * (1 + Math.sqrt(5));
            return treemap.size = function(x) {
                return arguments.length ? (size = x, treemap) : size
            }, treemap.padding = function(x) {
                function padFunction(node) {
                    var p = x.call(treemap, node, node.depth);
                    return null == p ? d3_layout_treemapPadNull(node) : d3_layout_treemapPad(node, "number" == typeof p ? [p, p, p, p] : p)
                }

                function padConstant(node) {
                    return d3_layout_treemapPad(node, x)
                }
                if (!arguments.length) return padding;
                var type;
                return pad = null == (padding = x) ? d3_layout_treemapPadNull : "function" == (type = typeof x) ? padFunction : "number" === type ? (x = [x, x, x, x], padConstant) : padConstant, treemap
            }, treemap.round = function(x) {
                return arguments.length ? (round = x ? Math.round : Number, treemap) : round != Number
            }, treemap.sticky = function(x) {
                return arguments.length ? (sticky = x, stickies = null, treemap) : sticky
            }, treemap.ratio = function(x) {
                return arguments.length ? (ratio = x, treemap) : ratio
            }, treemap.mode = function(x) {
                return arguments.length ? (mode = x + "", treemap) : mode
            }, d3_layout_hierarchyRebind(treemap, hierarchy)
        }, d3.random = {
            normal: function(μ, σ) {
                var n = arguments.length;
                return 2 > n && (σ = 1), 1 > n && (μ = 0),
                    function() {
                        var x, y, r;
                        do x = 2 * Math.random() - 1, y = 2 * Math.random() - 1, r = x * x + y * y; while (!r || r > 1);
                        return μ + σ * x * Math.sqrt(-2 * Math.log(r) / r)
                    }
            },
            logNormal: function() {
                var random = d3.random.normal.apply(d3, arguments);
                return function() {
                    return Math.exp(random())
                }
            },
            irwinHall: function(m) {
                return function() {
                    for (var s = 0, j = 0; m > j; j++) s += Math.random();
                    return s / m
                }
            }
        }, d3.scale = {}, d3.scale.linear = function() {
            return d3_scale_linear([0, 1], [0, 1], d3_interpolate, !1)
        }, d3.scale.log = function() {
            return d3_scale_log(d3.scale.linear().domain([0, Math.LN10]), 10, d3_scale_logp, d3_scale_powp)
        };
        var d3_scale_logFormat = d3.format(".0e");
        d3.scale.pow = function() {
            return d3_scale_pow(d3.scale.linear(), 1)
        }, d3.scale.sqrt = function() {
            return d3.scale.pow().exponent(.5)
        }, d3.scale.ordinal = function() {
            return d3_scale_ordinal([], {
                t: "range",
                a: [
                    []
                ]
            })
        }, d3.scale.category10 = function() {
            return d3.scale.ordinal().range(d3_category10)
        }, d3.scale.category20 = function() {
            return d3.scale.ordinal().range(d3_category20)
        }, d3.scale.category20b = function() {
            return d3.scale.ordinal().range(d3_category20b)
        }, d3.scale.category20c = function() {
            return d3.scale.ordinal().range(d3_category20c)
        };
        var d3_category10 = ["#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd", "#8c564b", "#e377c2", "#7f7f7f", "#bcbd22", "#17becf"],
            d3_category20 = ["#1f77b4", "#aec7e8", "#ff7f0e", "#ffbb78", "#2ca02c", "#98df8a", "#d62728", "#ff9896", "#9467bd", "#c5b0d5", "#8c564b", "#c49c94", "#e377c2", "#f7b6d2", "#7f7f7f", "#c7c7c7", "#bcbd22", "#dbdb8d", "#17becf", "#9edae5"],
            d3_category20b = ["#393b79", "#5254a3", "#6b6ecf", "#9c9ede", "#637939", "#8ca252", "#b5cf6b", "#cedb9c", "#8c6d31", "#bd9e39", "#e7ba52", "#e7cb94", "#843c39", "#ad494a", "#d6616b", "#e7969c", "#7b4173", "#a55194", "#ce6dbd", "#de9ed6"],
            d3_category20c = ["#3182bd", "#6baed6", "#9ecae1", "#c6dbef", "#e6550d", "#fd8d3c", "#fdae6b", "#fdd0a2", "#31a354", "#74c476", "#a1d99b", "#c7e9c0", "#756bb1", "#9e9ac8", "#bcbddc", "#dadaeb", "#636363", "#969696", "#bdbdbd", "#d9d9d9"];
        d3.scale.quantile = function() {
            return d3_scale_quantile([], [])
        }, d3.scale.quantize = function() {
            return d3_scale_quantize(0, 1, [0, 1])
        }, d3.scale.threshold = function() {
            return d3_scale_threshold([.5], [0, 1])
        }, d3.scale.identity = function() {
            return d3_scale_identity([0, 1])
        }, d3.svg.arc = function() {
            function arc() {
                var r0 = innerRadius.apply(this, arguments),
                    r1 = outerRadius.apply(this, arguments),
                    a0 = startAngle.apply(this, arguments) + d3_svg_arcOffset,
                    a1 = endAngle.apply(this, arguments) + d3_svg_arcOffset,
                    da = (a0 > a1 && (da = a0, a0 = a1, a1 = da), a1 - a0),
                    df = π > da ? "0" : "1",
                    c0 = Math.cos(a0),
                    s0 = Math.sin(a0),
                    c1 = Math.cos(a1),
                    s1 = Math.sin(a1);
                return da >= d3_svg_arcMax ? r0 ? "M0," + r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + -r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + r1 + "M0," + r0 + "A" + r0 + "," + r0 + " 0 1,0 0," + -r0 + "A" + r0 + "," + r0 + " 0 1,0 0," + r0 + "Z" : "M0," + r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + -r1 + "A" + r1 + "," + r1 + " 0 1,1 0," + r1 + "Z" : r0 ? "M" + r1 * c0 + "," + r1 * s0 + "A" + r1 + "," + r1 + " 0 " + df + ",1 " + r1 * c1 + "," + r1 * s1 + "L" + r0 * c1 + "," + r0 * s1 + "A" + r0 + "," + r0 + " 0 " + df + ",0 " + r0 * c0 + "," + r0 * s0 + "Z" : "M" + r1 * c0 + "," + r1 * s0 + "A" + r1 + "," + r1 + " 0 " + df + ",1 " + r1 * c1 + "," + r1 * s1 + "L0,0Z"
            }
            var innerRadius = d3_svg_arcInnerRadius,
                outerRadius = d3_svg_arcOuterRadius,
                startAngle = d3_svg_arcStartAngle,
                endAngle = d3_svg_arcEndAngle;
            return arc.innerRadius = function(v) {
                return arguments.length ? (innerRadius = d3_functor(v), arc) : innerRadius
            }, arc.outerRadius = function(v) {
                return arguments.length ? (outerRadius = d3_functor(v), arc) : outerRadius
            }, arc.startAngle = function(v) {
                return arguments.length ? (startAngle = d3_functor(v), arc) : startAngle
            }, arc.endAngle = function(v) {
                return arguments.length ? (endAngle = d3_functor(v), arc) : endAngle
            }, arc.centroid = function() {
                var r = (innerRadius.apply(this, arguments) + outerRadius.apply(this, arguments)) / 2,
                    a = (startAngle.apply(this, arguments) + endAngle.apply(this, arguments)) / 2 + d3_svg_arcOffset;
                return [Math.cos(a) * r, Math.sin(a) * r]
            }, arc
        };
        var d3_svg_arcOffset = -π / 2,
            d3_svg_arcMax = 2 * π - 1e-6;
        d3.svg.line.radial = function() {
            var line = d3_svg_line(d3_svg_lineRadial);
            return line.radius = line.x, delete line.x, line.angle = line.y, delete line.y, line
        }, d3_svg_lineStepBefore.reverse = d3_svg_lineStepAfter, d3_svg_lineStepAfter.reverse = d3_svg_lineStepBefore, d3.svg.area = function() {
            return d3_svg_area(d3_identity)
        }, d3.svg.area.radial = function() {
            var area = d3_svg_area(d3_svg_lineRadial);
            return area.radius = area.x, delete area.x, area.innerRadius = area.x0, delete area.x0, area.outerRadius = area.x1, delete area.x1, area.angle = area.y, delete area.y, area.startAngle = area.y0, delete area.y0, area.endAngle = area.y1, delete area.y1, area
        }, d3.svg.chord = function() {
            function chord(d, i) {
                var s = subgroup(this, source, d, i),
                    t = subgroup(this, target, d, i);
                return "M" + s.p0 + arc(s.r, s.p1, s.a1 - s.a0) + (equals(s, t) ? curve(s.r, s.p1, s.r, s.p0) : curve(s.r, s.p1, t.r, t.p0) + arc(t.r, t.p1, t.a1 - t.a0) + curve(t.r, t.p1, s.r, s.p0)) + "Z"
            }

            function subgroup(self, f, d, i) {
                var subgroup = f.call(self, d, i),
                    r = radius.call(self, subgroup, i),
                    a0 = startAngle.call(self, subgroup, i) + d3_svg_arcOffset,
                    a1 = endAngle.call(self, subgroup, i) + d3_svg_arcOffset;
                return {
                    r: r,
                    a0: a0,
                    a1: a1,
                    p0: [r * Math.cos(a0), r * Math.sin(a0)],
                    p1: [r * Math.cos(a1), r * Math.sin(a1)]
                }
            }

            function equals(a, b) {
                return a.a0 == b.a0 && a.a1 == b.a1
            }

            function arc(r, p, a) {
                return "A" + r + "," + r + " 0 " + +(a > π) + ",1 " + p
            }

            function curve(r0, p0, r1, p1) {
                return "Q 0,0 " + p1
            }
            var source = d3_source,
                target = d3_target,
                radius = d3_svg_chordRadius,
                startAngle = d3_svg_arcStartAngle,
                endAngle = d3_svg_arcEndAngle;
            return chord.radius = function(v) {
                return arguments.length ? (radius = d3_functor(v), chord) : radius
            }, chord.source = function(v) {
                return arguments.length ? (source = d3_functor(v), chord) : source
            }, chord.target = function(v) {
                return arguments.length ? (target = d3_functor(v), chord) : target
            }, chord.startAngle = function(v) {
                return arguments.length ? (startAngle = d3_functor(v), chord) : startAngle
            }, chord.endAngle = function(v) {
                return arguments.length ? (endAngle = d3_functor(v), chord) : endAngle
            }, chord
        }, d3.svg.diagonal = function() {
            function diagonal(d, i) {
                var p0 = source.call(this, d, i),
                    p3 = target.call(this, d, i),
                    m = (p0.y + p3.y) / 2,
                    p = [p0, {
                        x: p0.x,
                        y: m
                    }, {
                        x: p3.x,
                        y: m
                    }, p3];
                return p = p.map(projection), "M" + p[0] + "C" + p[1] + " " + p[2] + " " + p[3]
            }
            var source = d3_source,
                target = d3_target,
                projection = d3_svg_diagonalProjection;
            return diagonal.source = function(x) {
                return arguments.length ? (source = d3_functor(x), diagonal) : source
            }, diagonal.target = function(x) {
                return arguments.length ? (target = d3_functor(x), diagonal) : target
            }, diagonal.projection = function(x) {
                return arguments.length ? (projection = x, diagonal) : projection
            }, diagonal
        }, d3.svg.diagonal.radial = function() {
            var diagonal = d3.svg.diagonal(),
                projection = d3_svg_diagonalProjection,
                projection_ = diagonal.projection;
            return diagonal.projection = function(x) {
                return arguments.length ? projection_(d3_svg_diagonalRadialProjection(projection = x)) : projection
            }, diagonal
        }, d3.svg.symbol = function() {
            function symbol(d, i) {
                return (d3_svg_symbols.get(type.call(this, d, i)) || d3_svg_symbolCircle)(size.call(this, d, i))
            }
            var type = d3_svg_symbolType,
                size = d3_svg_symbolSize;
            return symbol.type = function(x) {
                return arguments.length ? (type = d3_functor(x), symbol) : type
            }, symbol.size = function(x) {
                return arguments.length ? (size = d3_functor(x), symbol) : size
            }, symbol
        };
        var d3_svg_symbols = d3.map({
            circle: d3_svg_symbolCircle,
            cross: function(size) {
                var r = Math.sqrt(size / 5) / 2;
                return "M" + -3 * r + "," + -r + "H" + -r + "V" + -3 * r + "H" + r + "V" + -r + "H" + 3 * r + "V" + r + "H" + r + "V" + 3 * r + "H" + -r + "V" + r + "H" + -3 * r + "Z"
            },
            diamond: function(size) {
                var ry = Math.sqrt(size / (2 * d3_svg_symbolTan30)),
                    rx = ry * d3_svg_symbolTan30;
                return "M0," + -ry + "L" + rx + ",0 0," + ry + " " + -rx + ",0Z"
            },
            square: function(size) {
                var r = Math.sqrt(size) / 2;
                return "M" + -r + "," + -r + "L" + r + "," + -r + " " + r + "," + r + " " + -r + "," + r + "Z"
            },
            "triangle-down": function(size) {
                var rx = Math.sqrt(size / d3_svg_symbolSqrt3),
                    ry = rx * d3_svg_symbolSqrt3 / 2;
                return "M0," + ry + "L" + rx + "," + -ry + " " + -rx + "," + -ry + "Z"
            },
            "triangle-up": function(size) {
                var rx = Math.sqrt(size / d3_svg_symbolSqrt3),
                    ry = rx * d3_svg_symbolSqrt3 / 2;
                return "M0," + -ry + "L" + rx + "," + ry + " " + -rx + "," + ry + "Z"
            }
        });
        d3.svg.symbolTypes = d3_svg_symbols.keys();
        var d3_transitionInheritId, d3_svg_symbolSqrt3 = Math.sqrt(3),
            d3_svg_symbolTan30 = Math.tan(30 * d3_radians),
            d3_transitionPrototype = [],
            d3_transitionId = 0,
            d3_transitionInherit = {
                ease: d3_ease_cubicInOut,
                delay: 0,
                duration: 250
            };
        d3_transitionPrototype.call = d3_selectionPrototype.call, d3_transitionPrototype.empty = d3_selectionPrototype.empty, d3_transitionPrototype.node = d3_selectionPrototype.node, d3.transition = function(selection) {
            return arguments.length ? d3_transitionInheritId ? selection.transition() : selection : d3_selectionRoot.transition()
        }, d3.transition.prototype = d3_transitionPrototype, d3_transitionPrototype.select = function(selector) {
            var subgroup, subnode, node, id = this.id,
                subgroups = [];
            "function" != typeof selector && (selector = d3_selection_selector(selector));
            for (var j = -1, m = this.length; ++j < m;) {
                subgroups.push(subgroup = []);
                for (var group = this[j], i = -1, n = group.length; ++i < n;)(node = group[i]) && (subnode = selector.call(node, node.__data__, i)) ? ("__data__" in node && (subnode.__data__ = node.__data__), d3_transitionNode(subnode, i, id, node.__transition__[id]), subgroup.push(subnode)) : subgroup.push(null)
            }
            return d3_transition(subgroups, id)
        }, d3_transitionPrototype.selectAll = function(selector) {
            var subgroup, subnodes, node, subnode, transition, id = this.id,
                subgroups = [];
            "function" != typeof selector && (selector = d3_selection_selectorAll(selector));
            for (var j = -1, m = this.length; ++j < m;)
                for (var group = this[j], i = -1, n = group.length; ++i < n;)
                    if (node = group[i]) {
                        transition = node.__transition__[id], subnodes = selector.call(node, node.__data__, i), subgroups.push(subgroup = []);
                        for (var k = -1, o = subnodes.length; ++k < o;) d3_transitionNode(subnode = subnodes[k], k, id, transition), subgroup.push(subnode)
                    }
            return d3_transition(subgroups, id)
        }, d3_transitionPrototype.filter = function(filter) {
            var subgroup, group, node, subgroups = [];
            "function" != typeof filter && (filter = d3_selection_filter(filter));
            for (var j = 0, m = this.length; m > j; j++) {
                subgroups.push(subgroup = []);
                for (var group = this[j], i = 0, n = group.length; n > i; i++)(node = group[i]) && filter.call(node, node.__data__, i) && subgroup.push(node)
            }
            return d3_transition(subgroups, this.id, this.time).ease(this.ease())
        }, d3_transitionPrototype.tween = function(name, tween) {
            var id = this.id;
            return arguments.length < 2 ? this.node().__transition__[id].tween.get(name) : d3_selection_each(this, null == tween ? function(node) {
                node.__transition__[id].tween.remove(name)
            } : function(node) {
                node.__transition__[id].tween.set(name, tween)
            })
        }, d3_transitionPrototype.attr = function(nameNS, value) {
            function attrNull() {
                this.removeAttribute(name)
            }

            function attrNullNS() {
                this.removeAttributeNS(name.space, name.local)
            }

            function attrTween(b) {
                return null == b ? attrNull : (b += "", function() {
                    var i, a = this.getAttribute(name);
                    return a !== b && (i = interpolate(a, b), function(t) {
                        this.setAttribute(name, i(t))
                    })
                })
            }

            function attrTweenNS(b) {
                return null == b ? attrNullNS : (b += "", function() {
                    var i, a = this.getAttributeNS(name.space, name.local);
                    return a !== b && (i = interpolate(a, b), function(t) {
                        this.setAttributeNS(name.space, name.local, i(t))
                    })
                })
            }
            if (arguments.length < 2) {
                for (value in nameNS) this.attr(value, nameNS[value]);
                return this
            }
            var interpolate = d3_interpolateByName(nameNS),
                name = d3.ns.qualify(nameNS);
            return d3_transition_tween(this, "attr." + nameNS, value, name.local ? attrTweenNS : attrTween)
        }, d3_transitionPrototype.attrTween = function(nameNS, tween) {
            function attrTween(d, i) {
                var f = tween.call(this, d, i, this.getAttribute(name));
                return f && function(t) {
                    this.setAttribute(name, f(t))
                }
            }

            function attrTweenNS(d, i) {
                var f = tween.call(this, d, i, this.getAttributeNS(name.space, name.local));
                return f && function(t) {
                    this.setAttributeNS(name.space, name.local, f(t))
                }
            }
            var name = d3.ns.qualify(nameNS);
            return this.tween("attr." + nameNS, name.local ? attrTweenNS : attrTween)
        }, d3_transitionPrototype.style = function(name, value, priority) {
            function styleNull() {
                this.style.removeProperty(name)
            }

            function styleString(b) {
                return null == b ? styleNull : (b += "", function() {
                    var i, a = d3_window.getComputedStyle(this, null).getPropertyValue(name);
                    return a !== b && (i = interpolate(a, b), function(t) {
                        this.style.setProperty(name, i(t), priority)
                    })
                })
            }
            var n = arguments.length;
            if (3 > n) {
                if ("string" != typeof name) {
                    2 > n && (value = "");
                    for (priority in name) this.style(priority, name[priority], value);
                    return this
                }
                priority = ""
            }
            var interpolate = d3_interpolateByName(name);
            return d3_transition_tween(this, "style." + name, value, styleString)
        }, d3_transitionPrototype.styleTween = function(name, tween, priority) {
            function styleTween(d, i) {
                var f = tween.call(this, d, i, d3_window.getComputedStyle(this, null).getPropertyValue(name));
                return f && function(t) {
                    this.style.setProperty(name, f(t), priority)
                }
            }
            return arguments.length < 3 && (priority = ""), this.tween("style." + name, styleTween)
        }, d3_transitionPrototype.text = function(value) {
            return d3_transition_tween(this, "text", value, d3_transition_text)
        }, d3_transitionPrototype.remove = function() {
            return this.each("end.transition", function() {
                var p;
                !this.__transition__ && (p = this.parentNode) && p.removeChild(this)
            })
        }, d3_transitionPrototype.ease = function(value) {
            var id = this.id;
            return arguments.length < 1 ? this.node().__transition__[id].ease : ("function" != typeof value && (value = d3.ease.apply(d3, arguments)), d3_selection_each(this, function(node) {
                node.__transition__[id].ease = value
            }))
        }, d3_transitionPrototype.delay = function(value) {
            var id = this.id;
            return d3_selection_each(this, "function" == typeof value ? function(node, i, j) {
                node.__transition__[id].delay = 0 | value.call(node, node.__data__, i, j)
            } : (value |= 0, function(node) {
                node.__transition__[id].delay = value
            }))
        }, d3_transitionPrototype.duration = function(value) {
            var id = this.id;
            return d3_selection_each(this, "function" == typeof value ? function(node, i, j) {
                node.__transition__[id].duration = Math.max(1, 0 | value.call(node, node.__data__, i, j))
            } : (value = Math.max(1, 0 | value), function(node) {
                node.__transition__[id].duration = value
            }))
        }, d3_transitionPrototype.each = function(type, listener) {
            var id = this.id;
            if (arguments.length < 2) {
                var inherit = d3_transitionInherit,
                    inheritId = d3_transitionInheritId;
                d3_transitionInheritId = id, d3_selection_each(this, function(node, i, j) {
                    d3_transitionInherit = node.__transition__[id], type.call(node, node.__data__, i, j)
                }), d3_transitionInherit = inherit, d3_transitionInheritId = inheritId
            } else d3_selection_each(this, function(node) {
                node.__transition__[id].event.on(type, listener)
            });
            return this
        }, d3_transitionPrototype.transition = function() {
            for (var subgroup, group, node, transition, id0 = this.id, id1 = ++d3_transitionId, subgroups = [], j = 0, m = this.length; m > j; j++) {
                subgroups.push(subgroup = []);
                for (var group = this[j], i = 0, n = group.length; n > i; i++)(node = group[i]) && (transition = Object.create(node.__transition__[id0]), transition.delay += transition.duration, d3_transitionNode(node, i, id1, transition)), subgroup.push(node)
            }
            return d3_transition(subgroups, id1)
        }, d3.svg.axis = function() {
            function axis(g) {
                g.each(function() {
                    var tickTransform, g = d3.select(this),
                        ticks = null == tickValues ? scale.ticks ? scale.ticks.apply(scale, tickArguments_) : scale.domain() : tickValues,
                        tickFormat = null == tickFormat_ ? scale.tickFormat ? scale.tickFormat.apply(scale, tickArguments_) : String : tickFormat_,
                        subticks = d3_svg_axisSubdivide(scale, ticks, tickSubdivide),
                        subtick = g.selectAll(".tick.minor").data(subticks, String),
                        subtickEnter = subtick.enter().insert("line", ".tick").attr("class", "tick minor").style("opacity", 1e-6),
                        subtickExit = d3.transition(subtick.exit()).style("opacity", 1e-6).remove(),
                        subtickUpdate = d3.transition(subtick).style("opacity", 1),
                        tick = g.selectAll(".tick.major").data(ticks, String),
                        tickEnter = tick.enter().insert("g", "path").attr("class", "tick major").style("opacity", 1e-6),
                        tickExit = d3.transition(tick.exit()).style("opacity", 1e-6).remove(),
                        tickUpdate = d3.transition(tick).style("opacity", 1),
                        range = d3_scaleRange(scale),
                        path = g.selectAll(".domain").data([0]),
                        pathUpdate = (path.enter().append("path").attr("class", "domain"), d3.transition(path)),
                        scale1 = scale.copy(),
                        scale0 = this.__chart__ || scale1;
                    this.__chart__ = scale1, tickEnter.append("line"), tickEnter.append("text");
                    var lineEnter = tickEnter.select("line"),
                        lineUpdate = tickUpdate.select("line"),
                        text = tick.select("text").text(tickFormat),
                        textEnter = tickEnter.select("text"),
                        textUpdate = tickUpdate.select("text");
                    switch (orient) {
                        case "bottom":
                            tickTransform = d3_svg_axisX, subtickEnter.attr("y2", tickMinorSize), subtickUpdate.attr("x2", 0).attr("y2", tickMinorSize), lineEnter.attr("y2", tickMajorSize), textEnter.attr("y", Math.max(tickMajorSize, 0) + tickPadding), lineUpdate.attr("x2", 0).attr("y2", tickMajorSize), textUpdate.attr("x", 0).attr("y", Math.max(tickMajorSize, 0) + tickPadding), text.attr("dy", ".71em").style("text-anchor", "middle"), pathUpdate.attr("d", "M" + range[0] + "," + tickEndSize + "V0H" + range[1] + "V" + tickEndSize);
                            break;
                        case "top":
                            tickTransform = d3_svg_axisX, subtickEnter.attr("y2", -tickMinorSize), subtickUpdate.attr("x2", 0).attr("y2", -tickMinorSize), lineEnter.attr("y2", -tickMajorSize), textEnter.attr("y", -(Math.max(tickMajorSize, 0) + tickPadding)), lineUpdate.attr("x2", 0).attr("y2", -tickMajorSize), textUpdate.attr("x", 0).attr("y", -(Math.max(tickMajorSize, 0) + tickPadding)), text.attr("dy", "0em").style("text-anchor", "middle"), pathUpdate.attr("d", "M" + range[0] + "," + -tickEndSize + "V0H" + range[1] + "V" + -tickEndSize);
                            break;
                        case "left":
                            tickTransform = d3_svg_axisY, subtickEnter.attr("x2", -tickMinorSize), subtickUpdate.attr("x2", -tickMinorSize).attr("y2", 0), lineEnter.attr("x2", -tickMajorSize), textEnter.attr("x", -(Math.max(tickMajorSize, 0) + tickPadding)), lineUpdate.attr("x2", -tickMajorSize).attr("y2", 0), textUpdate.attr("x", -(Math.max(tickMajorSize, 0) + tickPadding)).attr("y", 0), text.attr("dy", ".32em").style("text-anchor", "end"), pathUpdate.attr("d", "M" + -tickEndSize + "," + range[0] + "H0V" + range[1] + "H" + -tickEndSize);
                            break;
                        case "right":
                            tickTransform = d3_svg_axisY, subtickEnter.attr("x2", tickMinorSize), subtickUpdate.attr("x2", tickMinorSize).attr("y2", 0), lineEnter.attr("x2", tickMajorSize), textEnter.attr("x", Math.max(tickMajorSize, 0) + tickPadding), lineUpdate.attr("x2", tickMajorSize).attr("y2", 0), textUpdate.attr("x", Math.max(tickMajorSize, 0) + tickPadding).attr("y", 0), text.attr("dy", ".32em").style("text-anchor", "start"), pathUpdate.attr("d", "M" + tickEndSize + "," + range[0] + "H0V" + range[1] + "H" + tickEndSize)
                    }
                    if (scale.ticks) tickEnter.call(tickTransform, scale0), tickUpdate.call(tickTransform, scale1), tickExit.call(tickTransform, scale1), subtickEnter.call(tickTransform, scale0), subtickUpdate.call(tickTransform, scale1), subtickExit.call(tickTransform, scale1);
                    else {
                        var dx = scale1.rangeBand() / 2,
                            x = function(d) {
                                return scale1(d) + dx
                            };
                        tickEnter.call(tickTransform, x), tickUpdate.call(tickTransform, x)
                    }
                })
            }
            var tickFormat_, scale = d3.scale.linear(),
                orient = d3_svg_axisDefaultOrient,
                tickMajorSize = 6,
                tickMinorSize = 6,
                tickEndSize = 6,
                tickPadding = 3,
                tickArguments_ = [10],
                tickValues = null,
                tickSubdivide = 0;
            return axis.scale = function(x) {
                return arguments.length ? (scale = x, axis) : scale
            }, axis.orient = function(x) {
                return arguments.length ? (orient = x in d3_svg_axisOrients ? x + "" : d3_svg_axisDefaultOrient, axis) : orient
            }, axis.ticks = function() {
                return arguments.length ? (tickArguments_ = arguments, axis) : tickArguments_
            }, axis.tickValues = function(x) {
                return arguments.length ? (tickValues = x, axis) : tickValues
            }, axis.tickFormat = function(x) {
                return arguments.length ? (tickFormat_ = x, axis) : tickFormat_
            }, axis.tickSize = function(x, y) {
                if (!arguments.length) return tickMajorSize;
                var n = arguments.length - 1;
                return tickMajorSize = +x, tickMinorSize = n > 1 ? +y : tickMajorSize, tickEndSize = n > 0 ? +arguments[n] : tickMajorSize, axis
            }, axis.tickPadding = function(x) {
                return arguments.length ? (tickPadding = +x, axis) : tickPadding
            }, axis.tickSubdivide = function(x) {
                return arguments.length ? (tickSubdivide = +x, axis) : tickSubdivide
            }, axis
        };
        var d3_svg_axisDefaultOrient = "bottom",
            d3_svg_axisOrients = {
                top: 1,
                right: 1,
                bottom: 1,
                left: 1
            };
        d3.svg.brush = function() {
            function brush(g) {
                g.each(function() {
                    var e, g = d3.select(this),
                        bg = g.selectAll(".background").data([0]),
                        fg = g.selectAll(".extent").data([0]),
                        tz = g.selectAll(".resize").data(resizes, String);
                    g.style("pointer-events", "all").on("mousedown.brush", brushstart).on("touchstart.brush", brushstart), bg.enter().append("rect").attr("class", "background").style("visibility", "hidden").style("cursor", "crosshair"), fg.enter().append("rect").attr("class", "extent").style("cursor", "move"), tz.enter().append("g").attr("class", function(d) {
                        return "resize " + d
                    }).style("cursor", function(d) {
                        return d3_svg_brushCursor[d]
                    }).append("rect").attr("x", function(d) {
                        return /[ew]$/.test(d) ? -3 : null
                    }).attr("y", function(d) {
                        return /^[ns]/.test(d) ? -3 : null
                    }).attr("width", 6).attr("height", 6).style("visibility", "hidden"), tz.style("display", brush.empty() ? "none" : null), tz.exit().remove(), x && (e = d3_scaleRange(x), bg.attr("x", e[0]).attr("width", e[1] - e[0]), redrawX(g)), y && (e = d3_scaleRange(y), bg.attr("y", e[0]).attr("height", e[1] - e[0]), redrawY(g)), redraw(g)
                })
            }

            function redraw(g) {
                g.selectAll(".resize").attr("transform", function(d) {
                    return "translate(" + extent[+/e$/.test(d)][0] + "," + extent[+/^s/.test(d)][1] + ")"
                })
            }

            function redrawX(g) {
                g.select(".extent").attr("x", extent[0][0]), g.selectAll(".extent,.n>rect,.s>rect").attr("width", extent[1][0] - extent[0][0])
            }

            function redrawY(g) {
                g.select(".extent").attr("y", extent[0][1]), g.selectAll(".extent,.e>rect,.w>rect").attr("height", extent[1][1] - extent[0][1])
            }

            function brushstart() {
                function mouse() {
                    var touches = d3.event.changedTouches;
                    return touches ? d3.touches(target, touches)[0] : d3.mouse(target)
                }

                function keydown() {
                    32 == d3.event.keyCode && (dragging || (center = null, origin[0] -= extent[1][0], origin[1] -= extent[1][1], dragging = 2), d3_eventCancel())
                }

                function keyup() {
                    32 == d3.event.keyCode && 2 == dragging && (origin[0] += extent[1][0], origin[1] += extent[1][1], dragging = 0, d3_eventCancel())
                }

                function brushmove() {
                    var point = mouse(),
                        moved = !1;
                    offset && (point[0] += offset[0], point[1] += offset[1]), dragging || (d3.event.altKey ? (center || (center = [(extent[0][0] + extent[1][0]) / 2, (extent[0][1] + extent[1][1]) / 2]), origin[0] = extent[+(point[0] < center[0])][0], origin[1] = extent[+(point[1] < center[1])][1]) : center = null), resizingX && move1(point, x, 0) && (redrawX(g), moved = !0), resizingY && move1(point, y, 1) && (redrawY(g), moved = !0), moved && (redraw(g), event_({
                        type: "brush",
                        mode: dragging ? "move" : "resize"
                    }))
                }

                function move1(point, scale, i) {
                    var min, max, range = d3_scaleRange(scale),
                        r0 = range[0],
                        r1 = range[1],
                        position = origin[i],
                        size = extent[1][i] - extent[0][i];
                    return dragging && (r0 -= position, r1 -= size + position), min = Math.max(r0, Math.min(r1, point[i])), dragging ? max = (min += position) + size : (center && (position = Math.max(r0, Math.min(r1, 2 * center[i] - min))), min > position ? (max = min, min = position) : max = position), extent[0][i] !== min || extent[1][i] !== max ? (extentDomain = null, extent[0][i] = min, extent[1][i] = max, !0) : void 0
                }

                function brushend() {
                    brushmove(), g.style("pointer-events", "all").selectAll(".resize").style("display", brush.empty() ? "none" : null), d3.select("body").style("cursor", null), w.on("mousemove.brush", null).on("mouseup.brush", null).on("touchmove.brush", null).on("touchend.brush", null).on("keydown.brush", null).on("keyup.brush", null), event_({
                        type: "brushend"
                    }), d3_eventCancel()
                }
                var center, offset, target = this,
                    eventTarget = d3.select(d3.event.target),
                    event_ = event.of(target, arguments),
                    g = d3.select(target),
                    resizing = eventTarget.datum(),
                    resizingX = !/^(n|s)$/.test(resizing) && x,
                    resizingY = !/^(e|w)$/.test(resizing) && y,
                    dragging = eventTarget.classed("extent"),
                    origin = mouse(),
                    w = d3.select(d3_window).on("mousemove.brush", brushmove).on("mouseup.brush", brushend).on("touchmove.brush", brushmove).on("touchend.brush", brushend).on("keydown.brush", keydown).on("keyup.brush", keyup);
                if (dragging) origin[0] = extent[0][0] - origin[0], origin[1] = extent[0][1] - origin[1];
                else if (resizing) {
                    var ex = +/w$/.test(resizing),
                        ey = +/^n/.test(resizing);
                    offset = [extent[1 - ex][0] - origin[0], extent[1 - ey][1] - origin[1]], origin[0] = extent[ex][0], origin[1] = extent[ey][1]
                } else d3.event.altKey && (center = origin.slice());
                g.style("pointer-events", "none").selectAll(".resize").style("display", null), d3.select("body").style("cursor", eventTarget.style("cursor")), event_({
                    type: "brushstart"
                }), brushmove(), d3_eventCancel()
            }
            var extentDomain, event = d3_eventDispatch(brush, "brushstart", "brush", "brushend"),
                x = null,
                y = null,
                resizes = d3_svg_brushResizes[0],
                extent = [
                    [0, 0],
                    [0, 0]
                ];
            return brush.x = function(z) {
                return arguments.length ? (x = z, resizes = d3_svg_brushResizes[!x << 1 | !y], brush) : x
            }, brush.y = function(z) {
                return arguments.length ? (y = z, resizes = d3_svg_brushResizes[!x << 1 | !y], brush) : y
            }, brush.extent = function(z) {
                var x0, x1, y0, y1, t;
                return arguments.length ? (extentDomain = [
                    [0, 0],
                    [0, 0]
                ], x && (x0 = z[0], x1 = z[1], y && (x0 = x0[0], x1 = x1[0]), extentDomain[0][0] = x0, extentDomain[1][0] = x1, x.invert && (x0 = x(x0), x1 = x(x1)), x0 > x1 && (t = x0, x0 = x1, x1 = t), extent[0][0] = 0 | x0, extent[1][0] = 0 | x1), y && (y0 = z[0], y1 = z[1], x && (y0 = y0[1], y1 = y1[1]), extentDomain[0][1] = y0, extentDomain[1][1] = y1, y.invert && (y0 = y(y0), y1 = y(y1)), y0 > y1 && (t = y0, y0 = y1, y1 = t), extent[0][1] = 0 | y0, extent[1][1] = 0 | y1), brush) : (z = extentDomain || extent, x && (x0 = z[0][0], x1 = z[1][0], extentDomain || (x0 = extent[0][0], x1 = extent[1][0], x.invert && (x0 = x.invert(x0), x1 = x.invert(x1)), x0 > x1 && (t = x0, x0 = x1, x1 = t))), y && (y0 = z[0][1], y1 = z[1][1], extentDomain || (y0 = extent[0][1], y1 = extent[1][1], y.invert && (y0 = y.invert(y0), y1 = y.invert(y1)), y0 > y1 && (t = y0, y0 = y1, y1 = t))), x && y ? [
                    [x0, y0],
                    [x1, y1]
                ] : x ? [x0, x1] : y && [y0, y1])
            }, brush.clear = function() {
                return extentDomain = null, extent[0][0] = extent[0][1] = extent[1][0] = extent[1][1] = 0, brush
            }, brush.empty = function() {
                return x && extent[0][0] === extent[1][0] || y && extent[0][1] === extent[1][1]
            }, d3.rebind(brush, event, "on")
        };
        var d3_svg_brushCursor = {
                n: "ns-resize",
                e: "ew-resize",
                s: "ns-resize",
                w: "ew-resize",
                nw: "nwse-resize",
                ne: "nesw-resize",
                se: "nwse-resize",
                sw: "nesw-resize"
            },
            d3_svg_brushResizes = [
                ["n", "e", "s", "w", "nw", "ne", "se", "sw"],
                ["e", "w"],
                ["n", "s"],
                []
            ];
        d3.time = {};
        var d3_time = Date,
            d3_time_daySymbols = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        d3_time_utc.prototype = {
            getDate: function() {
                return this._.getUTCDate()
            },
            getDay: function() {
                return this._.getUTCDay()
            },
            getFullYear: function() {
                return this._.getUTCFullYear()
            },
            getHours: function() {
                return this._.getUTCHours()
            },
            getMilliseconds: function() {
                return this._.getUTCMilliseconds()
            },
            getMinutes: function() {
                return this._.getUTCMinutes()
            },
            getMonth: function() {
                return this._.getUTCMonth()
            },
            getSeconds: function() {
                return this._.getUTCSeconds()
            },
            getTime: function() {
                return this._.getTime()
            },
            getTimezoneOffset: function() {
                return 0
            },
            valueOf: function() {
                return this._.valueOf()
            },
            setDate: function() {
                d3_time_prototype.setUTCDate.apply(this._, arguments)
            },
            setDay: function() {
                d3_time_prototype.setUTCDay.apply(this._, arguments)
            },
            setFullYear: function() {
                d3_time_prototype.setUTCFullYear.apply(this._, arguments)
            },
            setHours: function() {
                d3_time_prototype.setUTCHours.apply(this._, arguments)
            },
            setMilliseconds: function() {
                d3_time_prototype.setUTCMilliseconds.apply(this._, arguments)
            },
            setMinutes: function() {
                d3_time_prototype.setUTCMinutes.apply(this._, arguments)
            },
            setMonth: function() {
                d3_time_prototype.setUTCMonth.apply(this._, arguments)
            },
            setSeconds: function() {
                d3_time_prototype.setUTCSeconds.apply(this._, arguments)
            },
            setTime: function() {
                d3_time_prototype.setTime.apply(this._, arguments)
            }
        };
        var d3_time_prototype = Date.prototype,
            d3_time_formatDateTime = "%a %b %e %X %Y",
            d3_time_formatDate = "%m/%d/%Y",
            d3_time_formatTime = "%H:%M:%S",
            d3_time_days = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
            d3_time_dayAbbreviations = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
            d3_time_months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            d3_time_monthAbbreviations = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
        d3.time.year = d3_time_interval(function(date) {
            return date = d3.time.day(date), date.setMonth(0, 1), date
        }, function(date, offset) {
            date.setFullYear(date.getFullYear() + offset)
        }, function(date) {
            return date.getFullYear()
        }), d3.time.years = d3.time.year.range, d3.time.years.utc = d3.time.year.utc.range, d3.time.day = d3_time_interval(function(date) {
            var day = new d3_time(1970, 0);
            return day.setFullYear(date.getFullYear(), date.getMonth(), date.getDate()), day
        }, function(date, offset) {
            date.setDate(date.getDate() + offset)
        }, function(date) {
            return date.getDate() - 1
        }), d3.time.days = d3.time.day.range, d3.time.days.utc = d3.time.day.utc.range, d3.time.dayOfYear = function(date) {
            var year = d3.time.year(date);
            return Math.floor((date - year - 6e4 * (date.getTimezoneOffset() - year.getTimezoneOffset())) / 864e5)
        }, d3_time_daySymbols.forEach(function(day, i) {
            day = day.toLowerCase(), i = 7 - i;
            var interval = d3.time[day] = d3_time_interval(function(date) {
                return (date = d3.time.day(date)).setDate(date.getDate() - (date.getDay() + i) % 7), date
            }, function(date, offset) {
                date.setDate(date.getDate() + 7 * Math.floor(offset))
            }, function(date) {
                var day = d3.time.year(date).getDay();
                return Math.floor((d3.time.dayOfYear(date) + (day + i) % 7) / 7) - (day !== i)
            });
            d3.time[day + "s"] = interval.range, d3.time[day + "s"].utc = interval.utc.range,
                d3.time[day + "OfYear"] = function(date) {
                    var day = d3.time.year(date).getDay();
                    return Math.floor((d3.time.dayOfYear(date) + (day + i) % 7) / 7)
                }
        }), d3.time.week = d3.time.sunday, d3.time.weeks = d3.time.sunday.range, d3.time.weeks.utc = d3.time.sunday.utc.range, d3.time.weekOfYear = d3.time.sundayOfYear, d3.time.format = function(template) {
            function format(date) {
                for (var c, p, f, string = [], i = -1, j = 0; ++i < n;) 37 === template.charCodeAt(i) && (string.push(template.substring(j, i)), null != (p = d3_time_formatPads[c = template.charAt(++i)]) && (c = template.charAt(++i)), (f = d3_time_formats[c]) && (c = f(date, null == p ? "e" === c ? " " : "0" : p)), string.push(c), j = i + 1);
                return string.push(template.substring(j, i)), string.join("")
            }
            var n = template.length;
            return format.parse = function(string) {
                var d = {
                        y: 1900,
                        m: 0,
                        d: 1,
                        H: 0,
                        M: 0,
                        S: 0,
                        L: 0
                    },
                    i = d3_time_parse(d, template, string, 0);
                if (i != string.length) return null;
                "p" in d && (d.H = d.H % 12 + 12 * d.p);
                var date = new d3_time;
                return date.setFullYear(d.y, d.m, d.d), date.setHours(d.H, d.M, d.S, d.L), date
            }, format.toString = function() {
                return template
            }, format
        };
        var d3_time_dayRe = d3_time_formatRe(d3_time_days),
            d3_time_dayAbbrevRe = d3_time_formatRe(d3_time_dayAbbreviations),
            d3_time_monthRe = d3_time_formatRe(d3_time_months),
            d3_time_monthLookup = d3_time_formatLookup(d3_time_months),
            d3_time_monthAbbrevRe = d3_time_formatRe(d3_time_monthAbbreviations),
            d3_time_monthAbbrevLookup = d3_time_formatLookup(d3_time_monthAbbreviations),
            d3_time_formatPads = {
                "-": "",
                _: " ",
                0: "0"
            },
            d3_time_formats = {
                a: function(d) {
                    return d3_time_dayAbbreviations[d.getDay()]
                },
                A: function(d) {
                    return d3_time_days[d.getDay()]
                },
                b: function(d) {
                    return d3_time_monthAbbreviations[d.getMonth()]
                },
                B: function(d) {
                    return d3_time_months[d.getMonth()]
                },
                c: d3.time.format(d3_time_formatDateTime),
                d: function(d, p) {
                    return d3_time_formatPad(d.getDate(), p, 2)
                },
                e: function(d, p) {
                    return d3_time_formatPad(d.getDate(), p, 2)
                },
                H: function(d, p) {
                    return d3_time_formatPad(d.getHours(), p, 2)
                },
                I: function(d, p) {
                    return d3_time_formatPad(d.getHours() % 12 || 12, p, 2)
                },
                j: function(d, p) {
                    return d3_time_formatPad(1 + d3.time.dayOfYear(d), p, 3)
                },
                L: function(d, p) {
                    return d3_time_formatPad(d.getMilliseconds(), p, 3)
                },
                m: function(d, p) {
                    return d3_time_formatPad(d.getMonth() + 1, p, 2)
                },
                M: function(d, p) {
                    return d3_time_formatPad(d.getMinutes(), p, 2)
                },
                p: function(d) {
                    return d.getHours() >= 12 ? "PM" : "AM"
                },
                S: function(d, p) {
                    return d3_time_formatPad(d.getSeconds(), p, 2)
                },
                U: function(d, p) {
                    return d3_time_formatPad(d3.time.sundayOfYear(d), p, 2)
                },
                w: function(d) {
                    return d.getDay()
                },
                W: function(d, p) {
                    return d3_time_formatPad(d3.time.mondayOfYear(d), p, 2)
                },
                x: d3.time.format(d3_time_formatDate),
                X: d3.time.format(d3_time_formatTime),
                y: function(d, p) {
                    return d3_time_formatPad(d.getFullYear() % 100, p, 2)
                },
                Y: function(d, p) {
                    return d3_time_formatPad(d.getFullYear() % 1e4, p, 4)
                },
                Z: d3_time_zone,
                "%": function() {
                    return "%"
                }
            },
            d3_time_parsers = {
                a: d3_time_parseWeekdayAbbrev,
                A: d3_time_parseWeekday,
                b: d3_time_parseMonthAbbrev,
                B: d3_time_parseMonth,
                c: d3_time_parseLocaleFull,
                d: d3_time_parseDay,
                e: d3_time_parseDay,
                H: d3_time_parseHour24,
                I: d3_time_parseHour24,
                L: d3_time_parseMilliseconds,
                m: d3_time_parseMonthNumber,
                M: d3_time_parseMinutes,
                p: d3_time_parseAmPm,
                S: d3_time_parseSeconds,
                x: d3_time_parseLocaleDate,
                X: d3_time_parseLocaleTime,
                y: d3_time_parseYear,
                Y: d3_time_parseFullYear
            },
            d3_time_numberRe = /^\s*\d+/,
            d3_time_amPmLookup = d3.map({
                am: 0,
                pm: 1
            });
        d3.time.format.utc = function(template) {
            function format(date) {
                try {
                    d3_time = d3_time_utc;
                    var utc = new d3_time;
                    return utc._ = date, local(utc)
                } finally {
                    d3_time = Date
                }
            }
            var local = d3.time.format(template);
            return format.parse = function(string) {
                try {
                    d3_time = d3_time_utc;
                    var date = local.parse(string);
                    return date && date._
                } finally {
                    d3_time = Date
                }
            }, format.toString = local.toString, format
        };
        var d3_time_formatIso = d3.time.format.utc("%Y-%m-%dT%H:%M:%S.%LZ");
        d3.time.format.iso = Date.prototype.toISOString && +new Date("2000-01-01T00:00:00.000Z") ? d3_time_formatIsoNative : d3_time_formatIso, d3_time_formatIsoNative.parse = function(string) {
            var date = new Date(string);
            return isNaN(date) ? null : date
        }, d3_time_formatIsoNative.toString = d3_time_formatIso.toString, d3.time.second = d3_time_interval(function(date) {
            return new d3_time(1e3 * Math.floor(date / 1e3))
        }, function(date, offset) {
            date.setTime(date.getTime() + 1e3 * Math.floor(offset))
        }, function(date) {
            return date.getSeconds()
        }), d3.time.seconds = d3.time.second.range, d3.time.seconds.utc = d3.time.second.utc.range, d3.time.minute = d3_time_interval(function(date) {
            return new d3_time(6e4 * Math.floor(date / 6e4))
        }, function(date, offset) {
            date.setTime(date.getTime() + 6e4 * Math.floor(offset))
        }, function(date) {
            return date.getMinutes()
        }), d3.time.minutes = d3.time.minute.range, d3.time.minutes.utc = d3.time.minute.utc.range, d3.time.hour = d3_time_interval(function(date) {
            var timezone = date.getTimezoneOffset() / 60;
            return new d3_time(36e5 * (Math.floor(date / 36e5 - timezone) + timezone))
        }, function(date, offset) {
            date.setTime(date.getTime() + 36e5 * Math.floor(offset))
        }, function(date) {
            return date.getHours()
        }), d3.time.hours = d3.time.hour.range, d3.time.hours.utc = d3.time.hour.utc.range, d3.time.month = d3_time_interval(function(date) {
            return date = d3.time.day(date), date.setDate(1), date
        }, function(date, offset) {
            date.setMonth(date.getMonth() + offset)
        }, function(date) {
            return date.getMonth()
        }), d3.time.months = d3.time.month.range, d3.time.months.utc = d3.time.month.utc.range;
        var d3_time_scaleSteps = [1e3, 5e3, 15e3, 3e4, 6e4, 3e5, 9e5, 18e5, 36e5, 108e5, 216e5, 432e5, 864e5, 1728e5, 6048e5, 2592e6, 7776e6, 31536e6],
            d3_time_scaleLocalMethods = [
                [d3.time.second, 1],
                [d3.time.second, 5],
                [d3.time.second, 15],
                [d3.time.second, 30],
                [d3.time.minute, 1],
                [d3.time.minute, 5],
                [d3.time.minute, 15],
                [d3.time.minute, 30],
                [d3.time.hour, 1],
                [d3.time.hour, 3],
                [d3.time.hour, 6],
                [d3.time.hour, 12],
                [d3.time.day, 1],
                [d3.time.day, 2],
                [d3.time.week, 1],
                [d3.time.month, 1],
                [d3.time.month, 3],
                [d3.time.year, 1]
            ],
            d3_time_scaleLocalFormats = [
                [d3.time.format("%Y"), d3_true],
                [d3.time.format("%B"), function(d) {
                    return d.getMonth()
                }],
                [d3.time.format("%b %d"), function(d) {
                    return 1 != d.getDate()
                }],
                [d3.time.format("%a %d"), function(d) {
                    return d.getDay() && 1 != d.getDate()
                }],
                [d3.time.format("%I %p"), function(d) {
                    return d.getHours()
                }],
                [d3.time.format("%I:%M"), function(d) {
                    return d.getMinutes()
                }],
                [d3.time.format(":%S"), function(d) {
                    return d.getSeconds()
                }],
                [d3.time.format(".%L"), function(d) {
                    return d.getMilliseconds()
                }]
            ],
            d3_time_scaleLinear = d3.scale.linear(),
            d3_time_scaleLocalFormat = d3_time_scaleFormat(d3_time_scaleLocalFormats);
        d3_time_scaleLocalMethods.year = function(extent, m) {
            return d3_time_scaleLinear.domain(extent.map(d3_time_scaleGetYear)).ticks(m).map(d3_time_scaleSetYear)
        }, d3.time.scale = function() {
            return d3_time_scale(d3.scale.linear(), d3_time_scaleLocalMethods, d3_time_scaleLocalFormat)
        };
        var d3_time_scaleUTCMethods = d3_time_scaleLocalMethods.map(function(m) {
                return [m[0].utc, m[1]]
            }),
            d3_time_scaleUTCFormats = [
                [d3.time.format.utc("%Y"), d3_true],
                [d3.time.format.utc("%B"), function(d) {
                    return d.getUTCMonth()
                }],
                [d3.time.format.utc("%b %d"), function(d) {
                    return 1 != d.getUTCDate()
                }],
                [d3.time.format.utc("%a %d"), function(d) {
                    return d.getUTCDay() && 1 != d.getUTCDate()
                }],
                [d3.time.format.utc("%I %p"), function(d) {
                    return d.getUTCHours()
                }],
                [d3.time.format.utc("%I:%M"), function(d) {
                    return d.getUTCMinutes()
                }],
                [d3.time.format.utc(":%S"), function(d) {
                    return d.getUTCSeconds()
                }],
                [d3.time.format.utc(".%L"), function(d) {
                    return d.getUTCMilliseconds()
                }]
            ],
            d3_time_scaleUTCFormat = d3_time_scaleFormat(d3_time_scaleUTCFormats);
        return d3_time_scaleUTCMethods.year = function(extent, m) {
            return d3_time_scaleLinear.domain(extent.map(d3_time_scaleUTCGetYear)).ticks(m).map(d3_time_scaleUTCSetYear)
        }, d3.time.scale.utc = function() {
            return d3_time_scale(d3.scale.linear(), d3_time_scaleUTCMethods, d3_time_scaleUTCFormat)
        }, d3.text = function() {
            return d3.xhr.apply(d3, arguments).response(d3_text)
        }, d3.json = function(url, callback) {
            return d3.xhr(url, "application/json", callback).response(d3_json)
        }, d3.html = function(url, callback) {
            return d3.xhr(url, "text/html", callback).response(d3_html)
        }, d3.xml = function() {
            return d3.xhr.apply(d3, arguments).response(d3_xml)
        }, d3
    }();
var Hogan = {};
! function(Hogan, useArrayBuffer) {
    function coerceToString(val) {
        return String(null === val || void 0 === val ? "" : val)
    }

    function hoganEscape(str) {
        return str = coerceToString(str), hChars.test(str) ? str.replace(rAmp, "&amp;").replace(rLt, "&lt;").replace(rGt, "&gt;").replace(rApos, "&#39;").replace(rQuot, "&quot;") : str
    }
    Hogan.Template = function(renderFunc, text, compiler, options) {
        this.r = renderFunc || this.r, this.c = compiler, this.options = options, this.text = text || "", this.buf = useArrayBuffer ? [] : ""
    }, Hogan.Template.prototype = {
        r: function() {
            return ""
        },
        v: hoganEscape,
        t: coerceToString,
        render: function(context, partials, indent) {
            return this.ri([context], partials || {}, indent)
        },
        ri: function(context, partials, indent) {
            return this.r(context, partials, indent)
        },
        rp: function(name, context, partials, indent) {
            var partial = partials[name];
            return partial ? (this.c && "string" == typeof partial && (partial = this.c.compile(partial, this.options)), partial.ri(context, partials, indent)) : ""
        },
        rs: function(context, partials, section) {
            var tail = context[context.length - 1];
            if (!isArray(tail)) return void section(context, partials, this);
            for (var i = 0; i < tail.length; i++) context.push(tail[i]), section(context, partials, this), context.pop()
        },
        s: function(val, ctx, partials, inverted, start, end, tags) {
            var pass;
            return isArray(val) && 0 === val.length ? !1 : ("function" == typeof val && (val = this.ls(val, ctx, partials, inverted, start, end, tags)), pass = "" === val || !!val, !inverted && pass && ctx && ctx.push("object" == typeof val ? val : ctx[ctx.length - 1]), pass)
        },
        d: function(key, ctx, partials, returnFound) {
            var names = key.split("."),
                val = this.f(names[0], ctx, partials, returnFound),
                cx = null;
            if ("." === key && isArray(ctx[ctx.length - 2])) return ctx[ctx.length - 1];
            for (var i = 1; i < names.length; i++) val && "object" == typeof val && names[i] in val ? (cx = val, val = val[names[i]]) : val = "";
            return returnFound && !val ? !1 : (returnFound || "function" != typeof val || (ctx.push(cx), val = this.lv(val, ctx, partials), ctx.pop()), val)
        },
        f: function(key, ctx, partials, returnFound) {
            for (var val = !1, v = null, found = !1, i = ctx.length - 1; i >= 0; i--)
                if (v = ctx[i], v && "object" == typeof v && key in v) {
                    val = v[key], found = !0;
                    break
                }
            return found ? (returnFound || "function" != typeof val || (val = this.lv(val, ctx, partials)), val) : returnFound ? !1 : ""
        },
        ho: function(val, cx, partials, text, tags) {
            var compiler = this.c,
                options = this.options;
            options.delimiters = tags;
            var text = val.call(cx, text);
            return text = null == text ? String(text) : text.toString(), this.b(compiler.compile(text, options).render(cx, partials)), !1
        },
        b: useArrayBuffer ? function(s) {
            this.buf.push(s)
        } : function(s) {
            this.buf += s
        },
        fl: useArrayBuffer ? function() {
            var r = this.buf.join("");
            return this.buf = [], r
        } : function() {
            var r = this.buf;
            return this.buf = "", r
        },
        ls: function(val, ctx, partials, inverted, start, end, tags) {
            var cx = ctx[ctx.length - 1],
                t = null;
            if (!inverted && this.c && val.length > 0) return this.ho(val, cx, partials, this.text.substring(start, end), tags);
            if (t = val.call(cx), "function" == typeof t) {
                if (inverted) return !0;
                if (this.c) return this.ho(t, cx, partials, this.text.substring(start, end), tags)
            }
            return t
        },
        lv: function(val, ctx, partials) {
            var cx = ctx[ctx.length - 1],
                result = val.call(cx);
            return "function" == typeof result && (result = coerceToString(result.call(cx)), this.c && ~result.indexOf("{{")) ? this.c.compile(result, this.options).render(cx, partials) : coerceToString(result)
        }
    };
    var rAmp = /&/g,
        rLt = /</g,
        rGt = />/g,
        rApos = /\'/g,
        rQuot = /\"/g,
        hChars = /[&<>\"\']/,
        isArray = Array.isArray || function(a) {
            return "[object Array]" === Object.prototype.toString.call(a)
        }
}("undefined" != typeof exports ? exports : Hogan),
function(Hogan) {
    function cleanTripleStache(token) {
        "}" === token.n.substr(token.n.length - 1) && (token.n = token.n.substring(0, token.n.length - 1))
    }

    function trim(s) {
        return s.trim ? s.trim() : s.replace(/^\s*|\s*$/g, "")
    }

    function tagChange(tag, text, index) {
        if (text.charAt(index) != tag.charAt(0)) return !1;
        for (var i = 1, l = tag.length; l > i; i++)
            if (text.charAt(index + i) != tag.charAt(i)) return !1;
        return !0
    }

    function buildTree(tokens, kind, stack, customTags) {
        for (var instructions = [], opener = null, token = null; tokens.length > 0;)
            if (token = tokens.shift(), "#" == token.tag || "^" == token.tag || isOpener(token, customTags)) stack.push(token), token.nodes = buildTree(tokens, token.tag, stack, customTags), instructions.push(token);
            else {
                if ("/" == token.tag) {
                    if (0 === stack.length) throw new Error("Closing tag without opener: /" + token.n);
                    if (opener = stack.pop(), token.n != opener.n && !isCloser(token.n, opener.n, customTags)) throw new Error("Nesting error: " + opener.n + " vs. " + token.n);
                    return opener.end = token.i, instructions
                }
                instructions.push(token)
            }
        if (stack.length > 0) throw new Error("missing closing tag: " + stack.pop().n);
        return instructions
    }

    function isOpener(token, tags) {
        for (var i = 0, l = tags.length; l > i; i++)
            if (tags[i].o == token.n) return token.tag = "#", !0
    }

    function isCloser(close, open, tags) {
        for (var i = 0, l = tags.length; l > i; i++)
            if (tags[i].c == close && tags[i].o == open) return !0
    }

    function esc(s) {
        return s.replace(rSlash, "\\\\").replace(rQuot, '\\"').replace(rNewline, "\\n").replace(rCr, "\\r")
    }

    function chooseMethod(s) {
        return ~s.indexOf(".") ? "d" : "f"
    }

    function walk(tree) {
        for (var code = "", i = 0, l = tree.length; l > i; i++) {
            var tag = tree[i].tag;
            "#" == tag ? code += section(tree[i].nodes, tree[i].n, chooseMethod(tree[i].n), tree[i].i, tree[i].end, tree[i].otag + " " + tree[i].ctag) : "^" == tag ? code += invertedSection(tree[i].nodes, tree[i].n, chooseMethod(tree[i].n)) : "<" == tag || ">" == tag ? code += partial(tree[i]) : "{" == tag || "&" == tag ? code += tripleStache(tree[i].n, chooseMethod(tree[i].n)) : "\n" == tag ? code += text('"\\n"' + (tree.length - 1 == i ? "" : " + i")) : "_v" == tag ? code += variable(tree[i].n, chooseMethod(tree[i].n)) : void 0 === tag && (code += text('"' + esc(tree[i]) + '"'))
        }
        return code
    }

    function section(nodes, id, method, start, end, tags) {
        return "if(_.s(_." + method + '("' + esc(id) + '",c,p,1),c,p,0,' + start + "," + end + ',"' + tags + '")){_.rs(c,p,function(c,p,_){' + walk(nodes) + "});c.pop();}"
    }

    function invertedSection(nodes, id, method) {
        return "if(!_.s(_." + method + '("' + esc(id) + '",c,p,1),c,p,1,0,0,"")){' + walk(nodes) + "};"
    }

    function partial(tok) {
        return '_.b(_.rp("' + esc(tok.n) + '",c,p,"' + (tok.indent || "") + '"));'
    }

    function tripleStache(id, method) {
        return "_.b(_.t(_." + method + '("' + esc(id) + '",c,p,0)));'
    }

    function variable(id, method) {
        return "_.b(_.v(_." + method + '("' + esc(id) + '",c,p,0)));'
    }

    function text(id) {
        return "_.b(" + id + ");"
    }
    var rIsWhitespace = /\S/,
        rQuot = /\"/g,
        rNewline = /\n/g,
        rCr = /\r/g,
        rSlash = /\\/g,
        tagTypes = {
            "#": 1,
            "^": 2,
            "/": 3,
            "!": 4,
            ">": 5,
            "<": 6,
            "=": 7,
            _v: 8,
            "{": 9,
            "&": 10
        };
    Hogan.scan = function(text, delimiters) {
        function addBuf() {
            buf.length > 0 && (tokens.push(new String(buf)), buf = "")
        }

        function lineIsWhitespace() {
            for (var isAllWhitespace = !0, j = lineStart; j < tokens.length; j++)
                if (isAllWhitespace = tokens[j].tag && tagTypes[tokens[j].tag] < tagTypes._v || !tokens[j].tag && null === tokens[j].match(rIsWhitespace), !isAllWhitespace) return !1;
            return isAllWhitespace
        }

        function filterLine(haveSeenTag, noNewLine) {
            if (addBuf(), haveSeenTag && lineIsWhitespace())
                for (var next, j = lineStart; j < tokens.length; j++) tokens[j].tag || ((next = tokens[j + 1]) && ">" == next.tag && (next.indent = tokens[j].toString()), tokens.splice(j, 1));
            else noNewLine || tokens.push({
                tag: "\n"
            });
            seenTag = !1, lineStart = tokens.length
        }

        function changeDelimiters(text, index) {
            var close = "=" + ctag,
                closeIndex = text.indexOf(close, index),
                delimiters = trim(text.substring(text.indexOf("=", index) + 1, closeIndex)).split(" ");
            return otag = delimiters[0], ctag = delimiters[1], closeIndex + close.length - 1
        }
        var len = text.length,
            IN_TEXT = 0,
            IN_TAG_TYPE = 1,
            IN_TAG = 2,
            state = IN_TEXT,
            tagType = null,
            tag = null,
            buf = "",
            tokens = [],
            seenTag = !1,
            i = 0,
            lineStart = 0,
            otag = "{{",
            ctag = "}}";
        for (delimiters && (delimiters = delimiters.split(" "), otag = delimiters[0], ctag = delimiters[1]), i = 0; len > i; i++) state == IN_TEXT ? tagChange(otag, text, i) ? (--i, addBuf(), state = IN_TAG_TYPE) : "\n" == text.charAt(i) ? filterLine(seenTag) : buf += text.charAt(i) : state == IN_TAG_TYPE ? (i += otag.length - 1, tag = tagTypes[text.charAt(i + 1)], tagType = tag ? text.charAt(i + 1) : "_v", "=" == tagType ? (i = changeDelimiters(text, i), state = IN_TEXT) : (tag && i++, state = IN_TAG), seenTag = i) : tagChange(ctag, text, i) ? (tokens.push({
            tag: tagType,
            n: trim(buf),
            otag: otag,
            ctag: ctag,
            i: "/" == tagType ? seenTag - ctag.length : i + otag.length
        }), buf = "", i += ctag.length - 1, state = IN_TEXT, "{" == tagType && ("}}" == ctag ? i++ : cleanTripleStache(tokens[tokens.length - 1]))) : buf += text.charAt(i);
        return filterLine(seenTag, !0), tokens
    }, Hogan.generate = function(tree, text, options) {
        var code = 'var _=this;_.b(i=i||"");' + walk(tree) + "return _.fl();";
        return options.asString ? "function(c,p,i){" + code + ";}" : new Hogan.Template(new Function("c", "p", "i", code), text, Hogan, options)
    }, Hogan.parse = function(tokens, text, options) {
        return options = options || {}, buildTree(tokens, "", [], options.sectionTags || [])
    }, Hogan.cache = {}, Hogan.compile = function(text, options) {
        options = options || {};
        var key = text + "||" + !!options.asString,
            t = this.cache[key];
        return t ? t : (t = this.generate(this.parse(this.scan(text, options.delimiters), text, options), text, options), this.cache[key] = t)
    }
}("undefined" != typeof exports ? exports : Hogan);
var gettext = function(text) {
        return text
    },
    Horizon = function() {
        var horizon = {},
            initFunctions = [];
        return horizon.addInitFunction = function(fn) {
            initFunctions.push(fn)
        }, horizon.init = function() {
            for (var i = 0; i < initFunctions.length; i += 1) initFunctions[i]();
            initFunctions = []
        }, horizon
    },
    horizon = new Horizon;
horizon.network_topology = {
        networktopology_url: null,
        model: null,
        svg: "#topology_canvas",
        svg_container: "#topologyCanvasContainer",
        post_messages: "#topologyMessages",
        network_tmpl: {
            small: "#topology_template > .network_container_small",
            normal: "#topology_template > .network_container_normal"
        },
        router_tmpl: {
            small: "#topology_template > .router_small",
            normal: "#topology_template > .router_normal"
        },
        instance_tmpl: {
            small: "#topology_template > .instance_small",
            normal: "#topology_template > .instance_normal"
        },
        balloon_tmpl: null,
        balloon_device_tmpl: null,
        balloon_port_tmpl: null,
        network_index: {},
        balloon_id: null,
        reload_duration: 1e4,
        draw_mode: "normal",
        network_height: 0,
        previous_message: null,
        element_properties: {
            normal: {
                network_width: 270,
                network_min_height: 400,
                top_margin: 80,
                default_height: 50,
                margin: 20,
                device_x: 98.5,
                device_width: 90,
                port_margin: 16,
                port_height: 6,
                port_width: 82,
                port_text_margin: {
                    x: 6,
                    y: -4
                },
                texts_bg_y: 32,
                type_y: 46,
                balloon_margin: {
                    x: 12,
                    y: -12
                }
            },
            small: {
                network_width: 100,
                network_min_height: 400,
                top_margin: 50,
                default_height: 20,
                margin: 30,
                device_x: 47.5,
                device_width: 20,
                port_margin: 5,
                port_height: 3,
                port_width: 32.5,
                port_text_margin: {
                    x: 0,
                    y: 0
                },
                texts_bg_y: 0,
                type_y: 0,
                balloon_margin: {
                    x: 12,
                    y: -30
                }
            },
            cidr_margin: 5,
            device_name_max_size: 9,
            device_name_suffix: ".."
        },
        init: function(networktopology_url) {
            var self = this;
            $(self.svg_container).spin({
                lines: 10,
                length: 15,
                width: 4,
                radius: 10,
                color: "#000",
                speed: .8,
                trail: 50
            }), 0 !== $("#networktopology").length && (self.networktopology_url = networktopology_url, self.color = d3.scale.category10(), self.balloon_tmpl = Hogan.compile($("#balloon_container").html()), self.balloon_device_tmpl = Hogan.compile($("#balloon_device").html()), self.balloon_port_tmpl = Hogan.compile($("#balloon_port").html()), $(document).on("click", "a.closeTopologyBalloon", function(e) {
                e.preventDefault(), self.delete_balloon()
            }).on("click", ".topologyBalloon", function(e) {
                e.stopPropagation()
            }).on("click", "a.vnc_window", function(e) {
                e.preventDefault();
                var vnc_window = window.open($(this).attr("href"), vnc_window, "width=760,height=560");
                self.delete_balloon()
            }).click(function() {
                self.delete_balloon()
            }), $(".toggleView > .btn").click(function() {
                self.draw_mode = $(this).data("value"), $("g.network").remove(), $.cookie("ntp_draw_mode", self.draw_mode), self.data_convert()
            }), $(window).on("message", function(e) {
                var message = $.parseJSON(e.originalEvent.data);
                self.previous_message !== message.message && (horizon.alert(message.type, message.message), horizon.autoDismissAlerts(), self.previous_message = message.message, self.delete_post_message(message.iframe_id), self.load_network_info(), setTimeout(function() {
                    self.previous_message = null
                }, 1e4))
            }), self.load_network_info())
        },
        load_network_info: function() {
            var self = this;
            0 !== $("#networktopology").length && $.getJSON(self.networktopology_url + "?" + $.now(), function(data) {
                self.model = data, self.data_convert(), setTimeout(function() {
                    self.load_network_info()
                }, self.reload_duration)
            })
        },
        select_draw_mode: function() {
            var self = this,
                draw_mode = $.cookie("ntp_draw_mode");
            !draw_mode || "normal" !== draw_mode && "small" !== draw_mode ? (self.draw_mode = self.model.networks.length * self.element_properties.normal.network_width > $("#topologyCanvas").width() ? "small" : "normal", $.cookie("ntp_draw_mode", self.draw_mode)) : self.draw_mode = draw_mode, $(".toggleView > .btn").each(function() {
                var $this = $(this);
                $this.hasClass(self.draw_mode) ? $this.addClass("active") : $this.removeClass("active")
            })
        },
        data_convert: function() {
            var self = this,
                model = self.model;
            $.each(model.networks, function(index, network) {
                self.network_index[network.id] = index
            }), self.select_draw_mode();
            var element_properties = self.element_properties[self.draw_mode];
            self.network_height = element_properties.top_margin, $.each([{
                model: model.routers,
                type: "router"
            }, {
                model: model.servers,
                type: "instance"
            }], function(index, devices) {
                var type = devices.type,
                    model = devices.model;
                $.each(model, function(index, device) {
                    device.type = type, device.ports = self.select_port(device.id);
                    var hasports = device.ports.length <= 0 ? !1 : !0;
                    device.parent_network = hasports ? self.select_main_port(device.ports).network_id : self.model.networks[0].id;
                    var height = element_properties.port_margin * (device.ports.length - 1);
                    device.height = "normal" === self.draw_mode && height > element_properties.default_height ? height : element_properties.default_height, device.pos_y = self.network_height, device.port_height = "small" === self.draw_mode && height > device.height ? 1 : element_properties.port_height, device.port_margin = "small" === self.draw_mode && height > device.height ? device.height / device.ports.length : element_properties.port_margin, self.network_height += device.height + element_properties.margin
                })
            }), $.each(model.networks, function(index, network) {
                network.devices = [], $.each([model.routers, model.servers], function(index, devices) {
                    $.each(devices, function(index, device) {
                        network.id === device.parent_network && network.devices.push(device)
                    })
                })
            }), self.network_height += element_properties.top_margin, self.network_height = self.network_height > element_properties.network_min_height ? self.network_height : element_properties.network_min_height, self.draw_topology()
        },
        draw_topology: function() {
            var self = this;
            if ($(self.svg_container).spin(!1), $(self.svg_container).removeClass("noinfo"), self.model.networks.length <= 0) return $("g.network").remove(), void $(self.svg_container).addClass("noinfo");
            var svg = d3.select(self.svg),
                element_properties = self.element_properties[self.draw_mode];
            svg.attr("width", self.model.networks.length * element_properties.network_width).attr("height", self.network_height); {
                var network = svg.selectAll("g.network").data(self.model.networks);
                network.enter().append("g").attr("class", "network").each(function(d) {
                    this.appendChild(d3.select(self.network_tmpl[self.draw_mode]).node().cloneNode(!0));
                    var $this = d3.select(this).select(".network-rect");
                    d.url ? $this.on("mouseover", function() {
                        $this.transition().style("fill", function() {
                            return d3.rgb(self.get_network_color(d.id)).brighter(.5)
                        })
                    }).on("mouseout", function() {
                        $this.transition().style("fill", function() {
                            return self.get_network_color(d.id)
                        })
                    }).on("click", function() {
                        window.location.href = d.url
                    }) : $this.classed("nourl", !0)
                })
            }
            network.attr("id", function(d) {
                return "id_" + d.id
            }).attr("transform", function(d, i) {
                return "translate(" + element_properties.network_width * i + ",0)"
            }).select(".network-rect").attr("height", function() {
                return self.network_height
            }).style("fill", function(d) {
                return self.get_network_color(d.id)
            }), network.select(".network-name").attr("x", function() {
                return self.network_height / 2
            }).text(function(d) {
                return d.name
            }), network.select(".network-cidr").attr("x", function() {
                return self.network_height - self.element_properties.cidr_margin
            }).text(function(d) {
                var cidr = $.map(d.neutronSubnets, function(n) {
                    return n.cidr
                });
                return cidr.join(", ")
            }), network.exit().remove();
            var device = network.selectAll("g.device").data(function(d) {
                    return d.devices
                }),
                device_enter = device.enter().append("g").attr("class", "device").each(function(d) {
                    var device_template = self[d.type + "_tmpl"][self.draw_mode];
                    this.appendChild(d3.select(device_template).node().cloneNode(!0))
                });
            device_enter.on("mouseenter", function(d) {
                var $this = $(this);
                self.show_balloon(d, $this)
            }).on("click", function() {
                d3.event.stopPropagation()
            }), device.attr("id", function(d) {
                return "id_" + d.id
            }).attr("transform", function(d) {
                return "translate(" + element_properties.device_x + "," + d.pos_y + ")"
            }).select(".frame").attr("height", function(d) {
                return d.height
            }), device.select(".texts_bg").attr("y", function(d) {
                return element_properties.texts_bg_y + d.height - element_properties.default_height
            }), device.select(".type").attr("y", function(d) {
                return element_properties.type_y + d.height - element_properties.default_height
            }), device.select(".name").text(function(d) {
                return self.string_truncate(d.name)
            }), device.each(function(d) {
                if ("BUILD" === d.status) d3.select(this).classed("loading", !0);
                else if ("deleting" === d.task) d3.select(this).classed("loading", !0), "bl_" + d.id === self.balloon_id && self.delete_balloon();
                else if (d3.select(this).classed("loading", !1), "bl_" + d.id === self.balloon_id) {
                    var $this = $(this);
                    self.show_balloon(d, $this)
                }
            }), device.exit().each(function(d) {
                "bl_" + d.id === self.balloon_id && self.delete_balloon()
            }).remove();
            var port = device.select("g.ports").selectAll("g.port").data(function(d) {
                    return d.ports
                }),
                port_enter = port.enter().append("g").attr("class", "port").attr("id", function(d) {
                    return "id_" + d.id
                });
            port_enter.append("line").attr("class", "port_line"), port_enter.append("text").attr("class", "port_text"), device.select("g.ports").each(function(d) {
                this._portdata = {}, this._portdata.ports_length = d.ports.length, this._portdata.parent_network = d.parent_network, this._portdata.device_height = d.height, this._portdata.port_height = d.port_height, this._portdata.port_margin = d.port_margin, this._portdata.left = 0, this._portdata.right = 0, $(this).mouseenter(function(e) {
                    e.stopPropagation()
                })
            }), port.each(function(d) {
                var index_diff = self.get_network_index(this.parentNode._portdata.parent_network) - self.get_network_index(d.network_id);
                this._index_diff = index_diff = index_diff >= 0 ? ++index_diff : index_diff, this._direction = this._index_diff < 0 ? "right" : "left", this._index = this.parentNode._portdata[this._direction]++
            }), port.attr("transform", function() {
                var x = "left" === this._direction ? 0 : element_properties.device_width,
                    ports_length = this.parentNode._portdata[this._direction],
                    distance = this.parentNode._portdata.port_margin,
                    y = (this.parentNode._portdata.device_height - (ports_length - 1) * distance) / 2 + this._index * distance;
                return "translate(" + x + "," + y + ")"
            }), port.select(".port_line").attr("stroke-width", function() {
                return this.parentNode.parentNode._portdata.port_height
            }).attr("stroke", function(d) {
                return self.get_network_color(d.network_id)
            }).attr("x1", 0).attr("y1", 0).attr("y2", 0).attr("x2", function() {
                var parent = this.parentNode,
                    width = (Math.abs(parent._index_diff) - 1) * element_properties.network_width + element_properties.port_width;
                return "left" === parent._direction ? -1 * width : width
            }), port.select(".port_text").attr("x", function() {
                var parent = this.parentNode;
                return "left" === parent._direction ? (d3.select(this).classed("left", !0), -1 * element_properties.port_text_margin.x) : (d3.select(this).classed("left", !1), element_properties.port_text_margin.x)
            }).attr("y", function() {
                return element_properties.port_text_margin.y
            }).text(function(d) {
                var ip_label = [];
                return $.each(d.fixed_ips, function() {
                    ip_label.push(this.ip_address)
                }), ip_label.join(",")
            }), port.exit().remove()
        },
        get_network_color: function(network_id) {
            return this.color(this.get_network_index(network_id))
        },
        get_network_index: function(network_id) {
            return this.network_index[network_id]
        },
        select_port: function(device_id) {
            return $.map(this.model.ports, function(port) {
                return port.device_id === device_id ? port : void 0
            })
        },
        select_main_port: function(ports) {
            var _self = this,
                main_port_index = 0,
                MAX_INT = 4294967295,
                min_port_length = MAX_INT;
            return $.each(ports, function(index, port) {
                var port_length = _self.sum_port_length(port.network_id, ports);
                min_port_length > port_length && (min_port_length = port_length, main_port_index = index)
            }), ports[main_port_index]
        },
        sum_port_length: function(network_id, ports) {
            var self = this,
                sum_port_length = 0,
                base_index = self.get_network_index(network_id);
            return $.each(ports, function(index, port) {
                sum_port_length += base_index - self.get_network_index(port.network_id)
            }), sum_port_length
        },
        string_truncate: function(string) {
            for (var self = this, str = string, max_size = self.element_properties.device_name_max_size, suffix = self.element_properties.device_name_suffix, bytes = 0, i = 0; i < str.length; i++)
                if (bytes += str.charCodeAt(i) <= 255 ? 1 : 2, bytes > max_size) {
                    str = str.substr(0, i) + suffix;
                    break
                }
            return str
        },
        delete_device: function(type, device_id) {
            var self = this,
                message = {
                    id: device_id
                };
            self.post_message(device_id, type, message)
        },
        delete_port: function(router_id, port_id) {
            var self = this,
                message = {
                    id: port_id
                };
            self.post_message(port_id, "router/" + router_id + "/", message)
        },
        show_balloon: function(d, element) {
            var self = this,
                element_properties = self.element_properties[self.draw_mode];
            self.balloon_id && self.delete_balloon();
            var balloon_tmpl = self.balloon_tmpl,
                device_tmpl = self.balloon_device_tmpl,
                port_tmpl = self.balloon_port_tmpl,
                balloon_id = "bl_" + d.id,
                ports = [];
            $.each(d.ports, function(i, port) {
                var object = {};
                object.id = port.id, object.router_id = port.device_id, object.url = port.url, object.port_status = port.status, object.port_status_css = "ACTIVE" === port.status ? "active" : "down";
                var ip_address = "";
                try {
                    ip_address = port.fixed_ips[0].ip_address
                } catch (e) {
                    ip_address = gettext("None")
                }
                var device_owner = "";
                try {
                    device_owner = port.device_owner.replace("network:", "")
                } catch (e) {
                    device_owner = gettext("None")
                }
                object.ip_address = ip_address, object.device_owner = device_owner, object.is_interface = "router_interface" === device_owner, ports.push(object)
            });
            var html_data = {
                balloon_id: balloon_id,
                id: d.id,
                url: d.url,
                name: d.name,
                type: d.type,
                delete_label: gettext("Delete"),
                status: d.status,
                status_class: "ACTIVE" === d.status ? "active" : "down",
                status_label: gettext("STATUS"),
                id_label: gettext("ID"),
                interfaces_label: gettext("Interfaces"),
                delete_interface_label: gettext("Delete Interface"),
                open_console_label: gettext("Open Console"),
                view_details_label: gettext("View Details")
            };
            if ("router" === d.type) html_data.delete_label = gettext("Delete Router"), html_data.view_details_label = gettext("View Router Details"), html_data.port = ports, html_data.add_interface_url = d.url + "addinterface", html_data.add_interface_label = gettext("Add Interface"), html = balloon_tmpl.render(html_data, {
                table1: device_tmpl,
                table2: ports.length > 0 ? port_tmpl : null
            });
            else {
                if ("instance" !== d.type) return;
                html_data.delete_label = gettext("Terminate Instance"), html_data.view_details_label = gettext("View Instance Details"), html_data.console_id = d.id, html_data.console = d.console, html = balloon_tmpl.render(html_data, {
                    table1: device_tmpl
                })
            }
            $(self.svg_container).append(html);
            var device_position = element.find(".frame"),
                x = device_position.position().left + element_properties.device_width + element_properties.balloon_margin.x,
                y = device_position.position().top + element_properties.balloon_margin.y;
            $("#" + balloon_id).css({
                left: x + "px",
                top: y + "px"
            }).show();
            var $balloon = $("#" + balloon_id);
            $balloon.offset().left + $balloon.outerWidth() > $(window).outerWidth() && $balloon.css({
                left: "0px"
            }).css({
                left: device_position.position().left - $balloon.outerWidth() - element_properties.balloon_margin.x + "px"
            }).addClass("leftPosition"), $balloon.find(".delete-device").click(function() {
                var $this = $(this);
                $this.prop("disabled", !0), d3.select("#id_" + $this.data("device-id")).classed("loading", !0), self.delete_device($this.data("type"), $this.data("device-id"))
            }), $balloon.find(".delete-port").click(function() {
                var $this = $(this);
                self.delete_port($this.data("router-id"), $this.data("port-id"))
            }), self.balloon_id = balloon_id
        },
        delete_balloon: function() {
            var self = this;
            self.balloon_id && ($("#" + self.balloon_id).remove(), self.balloon_id = null)
        },
        post_message: function(id, url, message) {
            var self = this,
                iframe_id = "ifr_" + id,
                iframe = $('<iframe width="500" height="300" />').attr("id", iframe_id).attr("src", url).appendTo(self.post_messages);
            iframe.on("load", function() {
                $(this).get(0).contentWindow.postMessage(JSON.stringify(message, null, 2), "*")
            })
        },
        delete_post_message: function(id) {
            $("#" + id).remove()
        }
    }, eval(function(p, a, c, k) {
        for (; c--;) k[c] && (p = p.replace(new RegExp("\\b" + c.toString(a) + "\\b", "g"), k[c]));
        return p
    }('$.1m({1w:b(e,t,n){b h(){3 e=o[0][0];3 t=o[o.8-1][0];3 n=(t-e)/a;3 r=[];r.6(o[0]);3 i=1;7=o[0];4=o[i];q(3 s=e+n;s<t+n;s+=n){9(s>t){s=t}$("#18").19(s);1a(s>4[0]){7=4;4=o[i++]}9(s==4[0]){r.6([s,4[1]]);7=4;4=o[i++]}11{3 u=(4[1]-7[1])/(4[0]-7[0]);16=u*s+(7[1]-u*7[0]);r.6([s,16])}}j r}b v(){3 n=[];p++;1b(c){14"1c":n=d.w(-1*p);y;14"1h":n=d.w(d.8/2-p/2,d.8/2+p/2);y;1d:n=d.w(0,p);y}9(!u){13=n[0][0];12=n[n.8-1][0];n=[];q(3 i=0;i<o.8;i++){9(o[i][0]>=13&&o[i][0]<=12){n.6(o[i])}}}t[r].x=p<a?n:o;g.1j(t);g.1i();9(p<a){15(v,f/a)}11{e.1g("1f")}}b m(i){3 s=[];s.6([i[0][0],k.1e.10(k,i.z(b(e){j e[1]}))]);s.6([i[0][0],17]);s.6([i[0][0],k.1k.10(k,i.z(b(e){j e[1]}))]);q(3 o=0;o<i.8;o++){s.6([i[o][0],17])}t[r].x=s;j $.1l(e,t,n)}3 r=0;q(3 i=0;i<t.8;i++){9(t[i].5){r=i}}3 s=t[r];3 o=s.x;3 u=t[r].1v?1x:1t;3 a=t[r].5&&t[r].5.1r||1q;3 f=t[r].5&&t[r].5.1p||1o;3 l=t[r].5&&t[r].5.1n||0;3 c=t[r].5&&t[r].5.1u||"1s";3 p=0;3 d=h();3 g=m(o);15(v,l);j g}})', 36, 70, "|||var|nPoint|animator|push|lPoint|length|if||function||||||||return|Math||||||for||||||slice|data|break|map|apply|else|laV|inV|case|setTimeout|curV|null|m2|html|while|switch|left|default|max|animatorComplete|trigger|center|draw|setData|min|plot|extend|start|1e3|duration|135|steps|right|false|direction|lines|plotAnimator|true".split("|"))),
    function(a, b, c) {
        ! function(a) {
            "function" == typeof define && define.amd ? define(["jquery"], a) : jQuery && !jQuery.fn.sparkline && a(jQuery)
        }(function(d) {
            "use strict";
            var f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, A, B, C, D, E, F, G, H, I, J, K, e = {},
                L = 0;
            f = function() {
                return {
                    common: {
                        type: "line",
                        lineColor: "#00f",
                        fillColor: "#cdf",
                        defaultPixelsPerValue: 3,
                        width: "auto",
                        height: "auto",
                        composite: !1,
                        tagValuesAttribute: "values",
                        tagOptionsPrefix: "spark",
                        enableTagOptions: !1,
                        enableHighlight: !0,
                        highlightLighten: 1.4,
                        tooltipSkipNull: !0,
                        tooltipPrefix: "",
                        tooltipSuffix: "",
                        disableHiddenCheck: !1,
                        numberFormatter: !1,
                        numberDigitGroupCount: 3,
                        numberDigitGroupSep: ",",
                        numberDecimalMark: ".",
                        disableTooltips: !1,
                        disableInteraction: !1
                    },
                    line: {
                        spotColor: "#f80",
                        highlightSpotColor: "#5f5",
                        highlightLineColor: "#f22",
                        spotRadius: 1.5,
                        minSpotColor: "#f80",
                        maxSpotColor: "#f80",
                        lineWidth: 1,
                        normalRangeMin: c,
                        normalRangeMax: c,
                        normalRangeColor: "#ccc",
                        drawNormalOnTop: !1,
                        chartRangeMin: c,
                        chartRangeMax: c,
                        chartRangeMinX: c,
                        chartRangeMaxX: c,
                        tooltipFormat: new h('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{y}}{{suffix}}')
                    },
                    bar: {
                        barColor: "#3366cc",
                        negBarColor: "#f44",
                        stackedBarColor: ["#3366cc", "#dc3912", "#ff9900", "#109618", "#66aa00", "#dd4477", "#0099c6", "#990099"],
                        zeroColor: c,
                        nullColor: c,
                        zeroAxis: !0,
                        barWidth: 4,
                        barSpacing: 1,
                        chartRangeMax: c,
                        chartRangeMin: c,
                        chartRangeClip: !1,
                        colorMap: c,
                        tooltipFormat: new h('<span style="color: {{color}}">&#9679;</span> {{prefix}}{{value}}{{suffix}}')
                    },
                    tristate: {
                        barWidth: 4,
                        barSpacing: 1,
                        posBarColor: "#6f6",
                        negBarColor: "#f44",
                        zeroBarColor: "#999",
                        colorMap: {},
                        tooltipFormat: new h('<span style="color: {{color}}">&#9679;</span> {{value:map}}'),
                        tooltipValueLookups: {
                            map: {
                                "-1": "Loss",
                                0: "Draw",
                                1: "Win"
                            }
                        }
                    },
                    discrete: {
                        lineHeight: "auto",
                        thresholdColor: c,
                        thresholdValue: 0,
                        chartRangeMax: c,
                        chartRangeMin: c,
                        chartRangeClip: !1,
                        tooltipFormat: new h("{{prefix}}{{value}}{{suffix}}")
                    },
                    bullet: {
                        targetColor: "#f33",
                        targetWidth: 3,
                        performanceColor: "#33f",
                        rangeColors: ["#d3dafe", "#a8b6ff", "#7f94ff"],
                        base: c,
                        tooltipFormat: new h("{{fieldkey:fields}} - {{value}}"),
                        tooltipValueLookups: {
                            fields: {
                                r: "Range",
                                p: "Performance",
                                t: "Target"
                            }
                        }
                    },
                    pie: {
                        offset: 0,
                        sliceColors: ["#3366cc", "#dc3912", "#ff9900", "#109618", "#66aa00", "#dd4477", "#0099c6", "#990099"],
                        borderWidth: 0,
                        borderColor: "#000",
                        tooltipFormat: new h('<span style="color: {{color}}">&#9679;</span> {{value}} ({{percent.1}}%)')
                    },
                    box: {
                        raw: !1,
                        boxLineColor: "#000",
                        boxFillColor: "#cdf",
                        whiskerColor: "#000",
                        outlierLineColor: "#333",
                        outlierFillColor: "#fff",
                        medianColor: "#f00",
                        showOutliers: !0,
                        outlierIQR: 1.5,
                        spotRadius: 1.5,
                        target: c,
                        targetColor: "#4a2",
                        chartRangeMax: c,
                        chartRangeMin: c,
                        tooltipFormat: new h("{{field:fields}}: {{value}}"),
                        tooltipFormatFieldlistKey: "field",
                        tooltipValueLookups: {
                            fields: {
                                lq: "Lower Quartile",
                                med: "Median",
                                uq: "Upper Quartile",
                                lo: "Left Outlier",
                                ro: "Right Outlier",
                                lw: "Left Whisker",
                                rw: "Right Whisker"
                            }
                        }
                    }
                }
            }, E = '.jqstooltip { position: absolute;left: 0px;top: 0px;visibility: hidden;background: rgb(0, 0, 0) transparent;background-color: rgba(0,0,0,0.6);filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000);-ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr=#99000000, endColorstr=#99000000)";color: white;font: 10px arial, san serif;text-align: left;white-space: nowrap;padding: 5px;border: 1px solid white;z-index: 10000;}.jqsfield { color: white;font: 10px arial, san serif;text-align: left;}', g = function() {
                var a, b;
                return a = function() {
                    this.init.apply(this, arguments)
                }, arguments.length > 1 ? (arguments[0] ? (a.prototype = d.extend(new arguments[0], arguments[arguments.length - 1]), a._super = arguments[0].prototype) : a.prototype = arguments[arguments.length - 1], arguments.length > 2 && (b = Array.prototype.slice.call(arguments, 1, -1), b.unshift(a.prototype), d.extend.apply(d, b))) : a.prototype = arguments[0], a.prototype.cls = a, a
            }, d.SPFormatClass = h = g({
                fre: /\{\{([\w.]+?)(:(.+?))?\}\}/g,
                precre: /(\w+)\.(\d+)/,
                init: function(a, b) {
                    this.format = a, this.fclass = b
                },
                render: function(a, b, d) {
                    var g, h, i, j, k, e = this,
                        f = a;
                    return this.format.replace(this.fre, function() {
                        var a;
                        return h = arguments[1], i = arguments[3], g = e.precre.exec(h), g ? (k = g[2], h = g[1]) : k = !1, j = f[h], j === c ? "" : i && b && b[i] ? (a = b[i], a.get ? b[i].get(j) || j : b[i][j] || j) : (n(j) && (j = d.get("numberFormatter") ? d.get("numberFormatter")(j) : s(j, k, d.get("numberDigitGroupCount"), d.get("numberDigitGroupSep"), d.get("numberDecimalMark"))), j)
                    })
                }
            }), d.spformat = function(a, b) {
                return new h(a, b)
            }, i = function(a, b, c) {
                return b > a ? b : a > c ? c : a
            }, j = function(a, c) {
                var d;
                return 2 === c ? (d = b.floor(a.length / 2), a.length % 2 ? a[d] : (a[d - 1] + a[d]) / 2) : a.length % 2 ? (d = (a.length * c + c) / 4, d % 1 ? (a[b.floor(d)] + a[b.floor(d) - 1]) / 2 : a[d - 1]) : (d = (a.length * c + 2) / 4, d % 1 ? (a[b.floor(d)] + a[b.floor(d) - 1]) / 2 : a[d - 1])
            }, k = function(a) {
                var b;
                switch (a) {
                    case "undefined":
                        a = c;
                        break;
                    case "null":
                        a = null;
                        break;
                    case "true":
                        a = !0;
                        break;
                    case "false":
                        a = !1;
                        break;
                    default:
                        b = parseFloat(a), a == b && (a = b)
                }
                return a
            }, l = function(a) {
                var b, c = [];
                for (b = a.length; b--;) c[b] = k(a[b]);
                return c
            }, m = function(a, b) {
                var c, d, e = [];
                for (c = 0, d = a.length; d > c; c++) a[c] !== b && e.push(a[c]);
                return e
            }, n = function(a) {
                return !isNaN(parseFloat(a)) && isFinite(a)
            }, s = function(a, b, c, e, f) {
                var g, h;
                for (a = (b === !1 ? parseFloat(a).toString() : a.toFixed(b)).split(""), g = (g = d.inArray(".", a)) < 0 ? a.length : g, g < a.length && (a[g] = f), h = g - c; h > 0; h -= c) a.splice(h, 0, e);
                return a.join("")
            }, o = function(a, b, c) {
                var d;
                for (d = b.length; d--;)
                    if ((!c || null !== b[d]) && b[d] !== a) return !1;
                return !0
            }, p = function(a) {
                var c, b = 0;
                for (c = a.length; c--;) b += "number" == typeof a[c] ? a[c] : 0;
                return b
            }, r = function(a) {
                return d.isArray(a) ? a : [a]
            }, q = function(b) {
                var c;
                a.createStyleSheet ? a.createStyleSheet().cssText = b : (c = a.createElement("style"), c.type = "text/css", a.getElementsByTagName("head")[0].appendChild(c), c["string" == typeof a.body.style.WebkitAppearance ? "innerText" : "innerHTML"] = b)
            }, d.fn.simpledraw = function(b, e, f, g) {
                var h, i;
                if (f && (h = this.data("_jqs_vcanvas"))) return h;
                if (d.fn.sparkline.canvas === !1) return !1;
                if (d.fn.sparkline.canvas === c) {
                    var j = a.createElement("canvas");
                    if (j.getContext && j.getContext("2d")) d.fn.sparkline.canvas = function(a, b, c, d) {
                        return new I(a, b, c, d)
                    };
                    else {
                        if (!a.namespaces || a.namespaces.v) return d.fn.sparkline.canvas = !1, !1;
                        a.namespaces.add("v", "urn:schemas-microsoft-com:vml", "#default#VML"), d.fn.sparkline.canvas = function(a, b, c) {
                            return new J(a, b, c)
                        }
                    }
                }
                return b === c && (b = d(this).innerWidth()), e === c && (e = d(this).innerHeight()), h = d.fn.sparkline.canvas(b, e, this, g), i = d(this).data("_jqs_mhandler"), i && i.registerCanvas(h), h
            }, d.fn.cleardraw = function() {
                var a = this.data("_jqs_vcanvas");
                a && a.reset()
            }, d.RangeMapClass = t = g({
                init: function(a) {
                    var b, c, d = [];
                    for (b in a) a.hasOwnProperty(b) && "string" == typeof b && b.indexOf(":") > -1 && (c = b.split(":"), c[0] = 0 === c[0].length ? -(1 / 0) : parseFloat(c[0]), c[1] = 0 === c[1].length ? 1 / 0 : parseFloat(c[1]), c[2] = a[b], d.push(c));
                    this.map = a, this.rangelist = d || !1
                },
                get: function(a) {
                    var d, e, f, b = this.rangelist;
                    if ((f = this.map[a]) !== c) return f;
                    if (b)
                        for (d = b.length; d--;)
                            if (e = b[d], e[0] <= a && e[1] >= a) return e[2];
                    return c
                }
            }), d.range_map = function(a) {
                return new t(a)
            }, u = g({
                init: function(a, b) {
                    var c = d(a);
                    this.$el = c, this.options = b, this.currentPageX = 0, this.currentPageY = 0, this.el = a, this.splist = [], this.tooltip = null, this.over = !1, this.displayTooltips = !b.get("disableTooltips"), this.highlightEnabled = !b.get("disableHighlight")
                },
                registerSparkline: function(a) {
                    this.splist.push(a), this.over && this.updateDisplay()
                },
                registerCanvas: function(a) {
                    var b = d(a.canvas);
                    this.canvas = a, this.$canvas = b, b.mouseenter(d.proxy(this.mouseenter, this)), b.mouseleave(d.proxy(this.mouseleave, this)), b.click(d.proxy(this.mouseclick, this))
                },
                reset: function(a) {
                    this.splist = [], this.tooltip && a && (this.tooltip.remove(), this.tooltip = c)
                },
                mouseclick: function(a) {
                    var b = d.Event("sparklineClick");
                    b.originalEvent = a, b.sparklines = this.splist, this.$el.trigger(b)
                },
                mouseenter: function(b) {
                    d(a.body).unbind("mousemove.jqs"), d(a.body).bind("mousemove.jqs", d.proxy(this.mousemove, this)), this.over = !0, this.currentPageX = b.pageX, this.currentPageY = b.pageY, this.currentEl = b.target, !this.tooltip && this.displayTooltips && (this.tooltip = new v(this.options), this.tooltip.updatePosition(b.pageX, b.pageY)), this.updateDisplay()
                },
                mouseleave: function() {
                    d(a.body).unbind("mousemove.jqs");
                    var f, g, b = this.splist,
                        c = b.length,
                        e = !1;
                    for (this.over = !1, this.currentEl = null, this.tooltip && (this.tooltip.remove(), this.tooltip = null), g = 0; c > g; g++) f = b[g], f.clearRegionHighlight() && (e = !0);
                    e && this.canvas.render()
                },
                mousemove: function(a) {
                    this.currentPageX = a.pageX, this.currentPageY = a.pageY, this.currentEl = a.target, this.tooltip && this.tooltip.updatePosition(a.pageX, a.pageY), this.updateDisplay()
                },
                updateDisplay: function() {
                    var h, i, j, k, l, a = this.splist,
                        b = a.length,
                        c = !1,
                        e = this.$canvas.offset(),
                        f = this.currentPageX - e.left,
                        g = this.currentPageY - e.top;
                    if (this.over) {
                        for (j = 0; b > j; j++) i = a[j], k = i.setRegionHighlight(this.currentEl, f, g), k && (c = !0);
                        if (c) {
                            if (l = d.Event("sparklineRegionChange"), l.sparklines = this.splist, this.$el.trigger(l), this.tooltip) {
                                for (h = "", j = 0; b > j; j++) i = a[j], h += i.getCurrentRegionTooltip();
                                this.tooltip.setContent(h)
                            }
                            this.disableHighlight || this.canvas.render()
                        }
                        null === k && this.mouseleave()
                    }
                }
            }), v = g({
                sizeStyle: "position: static !important;display: block !important;visibility: hidden !important;float: left !important;",
                init: function(b) {
                    var f, c = b.get("tooltipClassname", "jqstooltip"),
                        e = this.sizeStyle;
                    this.container = b.get("tooltipContainer") || a.body, this.tooltipOffsetX = b.get("tooltipOffsetX", 10), this.tooltipOffsetY = b.get("tooltipOffsetY", 12), d("#jqssizetip").remove(), d("#jqstooltip").remove(), this.sizetip = d("<div/>", {
                        id: "jqssizetip",
                        style: e,
                        "class": c
                    }), this.tooltip = d("<div/>", {
                        id: "jqstooltip",
                        "class": c
                    }).appendTo(this.container), f = this.tooltip.offset(), this.offsetLeft = f.left, this.offsetTop = f.top, this.hidden = !0, d(window).unbind("resize.jqs scroll.jqs"), d(window).bind("resize.jqs scroll.jqs", d.proxy(this.updateWindowDims, this)), this.updateWindowDims()
                },
                updateWindowDims: function() {
                    this.scrollTop = d(window).scrollTop(), this.scrollLeft = d(window).scrollLeft(), this.scrollRight = this.scrollLeft + d(window).width(), this.updatePosition()
                },
                getSize: function(a) {
                    this.sizetip.html(a).appendTo(this.container), this.width = this.sizetip.width() + 1, this.height = this.sizetip.height(), this.sizetip.remove()
                },
                setContent: function(a) {
                    return a ? (this.getSize(a), this.tooltip.html(a).css({
                        width: this.width,
                        height: this.height,
                        visibility: "visible"
                    }), this.hidden && (this.hidden = !1, this.updatePosition()), void 0) : (this.tooltip.css("visibility", "hidden"), void(this.hidden = !0))
                },
                updatePosition: function(a, b) {
                    if (a === c) {
                        if (this.mousex === c) return;
                        a = this.mousex - this.offsetLeft, b = this.mousey - this.offsetTop
                    } else this.mousex = a -= this.offsetLeft, this.mousey = b -= this.offsetTop;
                    this.height && this.width && !this.hidden && (b -= this.height + this.tooltipOffsetY, a += this.tooltipOffsetX, b < this.scrollTop && (b = this.scrollTop), a < this.scrollLeft ? a = this.scrollLeft : a + this.width > this.scrollRight && (a = this.scrollRight - this.width), this.tooltip.css({
                        left: a,
                        top: b
                    }))
                },
                remove: function() {
                    this.tooltip.remove(), this.sizetip.remove(), this.sizetip = this.tooltip = c, d(window).unbind("resize.jqs scroll.jqs")
                }
            }), F = function() {
                q(E)
            }, d(F), K = [], d.fn.sparkline = function(b, e) {
                return this.each(function() {
                    var h, i, f = new d.fn.sparkline.options(this, e),
                        g = d(this);
                    if (h = function() {
                            var e, h, i, j, k, l, m;
                            return "html" === b || b === c ? (m = this.getAttribute(f.get("tagValuesAttribute")), (m === c || null === m) && (m = g.html()), e = m.replace(/(^\s*<!--)|(-->\s*$)|\s+/g, "").split(",")) : e = b, h = "auto" === f.get("width") ? e.length * f.get("defaultPixelsPerValue") : f.get("width"), "auto" === f.get("height") ? f.get("composite") && d.data(this, "_jqs_vcanvas") || (j = a.createElement("span"), j.innerHTML = "a", g.html(j), i = d(j).innerHeight() || d(j).height(), d(j).remove(), j = null) : i = f.get("height"), f.get("disableInteraction") ? k = !1 : (k = d.data(this, "_jqs_mhandler"), k ? f.get("composite") || k.reset() : (k = new u(this, f), d.data(this, "_jqs_mhandler", k))), f.get("composite") && !d.data(this, "_jqs_vcanvas") ? void(d.data(this, "_jqs_errnotify") || (alert("Attempted to attach a composite sparkline to an element with no existing sparkline"), d.data(this, "_jqs_errnotify", !0))) : (l = new(d.fn.sparkline[f.get("type")])(this, e, f, h, i), l.render(), k && k.registerSparkline(l), void 0)
                        }, d(this).html() && !f.get("disableHiddenCheck") && d(this).is(":hidden") || !d(this).parents("body").length) {
                        if (!f.get("composite") && d.data(this, "_jqs_pending"))
                            for (i = K.length; i; i--) K[i - 1][0] == this && K.splice(i - 1, 1);
                        K.push([this, h]), d.data(this, "_jqs_pending", !0)
                    } else h.call(this)
                })
            }, d.fn.sparkline.defaults = f(), d.sparkline_display_visible = function() {
                var a, b, c, e = [];
                for (b = 0, c = K.length; c > b; b++) a = K[b][0], d(a).is(":visible") && !d(a).parents().is(":hidden") ? (K[b][1].call(a), d.data(K[b][0], "_jqs_pending", !1), e.push(b)) : !d(a).closest("html").length && !d.data(a, "_jqs_pending") && (d.data(K[b][0], "_jqs_pending", !1), e.push(b));
                for (b = e.length; b; b--) K.splice(e[b - 1], 1)
            }, d.fn.sparkline.options = g({
                init: function(a, b) {
                    var c, f, g, h;
                    this.userOptions = b = b || {}, this.tag = a, this.tagValCache = {}, f = d.fn.sparkline.defaults, g = f.common, this.tagOptionsPrefix = b.enableTagOptions && (b.tagOptionsPrefix || g.tagOptionsPrefix), h = this.getTagSetting("type"), c = h === e ? f[b.type || g.type] : f[h], this.mergedOptions = d.extend({}, g, c, b)
                },
                getTagSetting: function(a) {
                    var d, f, g, h, b = this.tagOptionsPrefix;
                    if (b === !1 || b === c) return e;
                    if (this.tagValCache.hasOwnProperty(a)) d = this.tagValCache.key;
                    else {
                        if (d = this.tag.getAttribute(b + a), d === c || null === d) d = e;
                        else if ("[" === d.substr(0, 1))
                            for (d = d.substr(1, d.length - 2).split(","), f = d.length; f--;) d[f] = k(d[f].replace(/(^\s*)|(\s*$)/g, ""));
                        else if ("{" === d.substr(0, 1))
                            for (g = d.substr(1, d.length - 2).split(","), d = {}, f = g.length; f--;) h = g[f].split(":", 2), d[h[0].replace(/(^\s*)|(\s*$)/g, "")] = k(h[1].replace(/(^\s*)|(\s*$)/g, ""));
                        else d = k(d);
                        this.tagValCache.key = d
                    }
                    return d
                },
                get: function(a, b) {
                    var f, d = this.getTagSetting(a);
                    return d !== e ? d : (f = this.mergedOptions[a]) === c ? b : f
                }
            }), d.fn.sparkline._base = g({
                disabled: !1,
                init: function(a, b, e, f, g) {
                    this.el = a, this.$el = d(a), this.values = b, this.options = e, this.width = f, this.height = g, this.currentRegion = c
                },
                initTarget: function() {
                    var a = !this.options.get("disableInteraction");
                    (this.target = this.$el.simpledraw(this.width, this.height, this.options.get("composite"), a)) ? (this.canvasWidth = this.target.pixelWidth, this.canvasHeight = this.target.pixelHeight) : this.disabled = !0
                },
                render: function() {
                    return this.disabled ? (this.el.innerHTML = "", !1) : !0
                },
                getRegion: function() {},
                setRegionHighlight: function(a, b, d) {
                    var g, e = this.currentRegion,
                        f = !this.options.get("disableHighlight");
                    return b > this.canvasWidth || d > this.canvasHeight || 0 > b || 0 > d ? null : (g = this.getRegion(a, b, d), e !== g ? (e !== c && f && this.removeHighlight(), this.currentRegion = g, g !== c && f && this.renderHighlight(), !0) : !1)
                },
                clearRegionHighlight: function() {
                    return this.currentRegion !== c ? (this.removeHighlight(), this.currentRegion = c, !0) : !1
                },
                renderHighlight: function() {
                    this.changeHighlight(!0)
                },
                removeHighlight: function() {
                    this.changeHighlight(!1)
                },
                changeHighlight: function() {},
                getCurrentRegionTooltip: function() {
                    var f, g, i, j, k, l, m, n, o, p, q, r, s, t, a = this.options,
                        b = "",
                        e = [];
                    if (this.currentRegion === c) return "";
                    if (f = this.getCurrentRegionFields(), q = a.get("tooltipFormatter")) return q(this, a, f);
                    if (a.get("tooltipChartTitle") && (b += '<div class="jqs jqstitle">' + a.get("tooltipChartTitle") + "</div>\n"), g = this.options.get("tooltipFormat"), !g) return "";
                    if (d.isArray(g) || (g = [g]), d.isArray(f) || (f = [f]), m = this.options.get("tooltipFormatFieldlist"), n = this.options.get("tooltipFormatFieldlistKey"), m && n) {
                        for (o = [], l = f.length; l--;) p = f[l][n], -1 != (t = d.inArray(p, m)) && (o[t] = f[l]);
                        f = o
                    }
                    for (i = g.length, s = f.length, l = 0; i > l; l++)
                        for (r = g[l], "string" == typeof r && (r = new h(r)), j = r.fclass || "jqsfield", t = 0; s > t; t++) f[t].isNull && a.get("tooltipSkipNull") || (d.extend(f[t], {
                            prefix: a.get("tooltipPrefix"),
                            suffix: a.get("tooltipSuffix")
                        }), k = r.render(f[t], a.get("tooltipValueLookups"), a), e.push('<div class="' + j + '">' + k + "</div>"));
                    return e.length ? b + e.join("\n") : ""
                },
                getCurrentRegionFields: function() {},
                calcHighlightColor: function(a, c) {
                    var f, g, h, j, d = c.get("highlightColor"),
                        e = c.get("highlightLighten");
                    if (d) return d;
                    if (e && (f = /^#([0-9a-f])([0-9a-f])([0-9a-f])$/i.exec(a) || /^#([0-9a-f]{2})([0-9a-f]{2})([0-9a-f]{2})$/i.exec(a))) {
                        for (h = [], g = 4 === a.length ? 16 : 1, j = 0; 3 > j; j++) h[j] = i(b.round(parseInt(f[j + 1], 16) * g * e), 0, 255);
                        return "rgb(" + h.join(",") + ")"
                    }
                    return a
                }
            }), w = {
                changeHighlight: function(a) {
                    var f, b = this.currentRegion,
                        c = this.target,
                        e = this.regionShapes[b];
                    e && (f = this.renderRegion(b, a), d.isArray(f) || d.isArray(e) ? (c.replaceWithShapes(e, f), this.regionShapes[b] = d.map(f, function(a) {
                        return a.id
                    })) : (c.replaceWithShape(e, f), this.regionShapes[b] = f.id))
                },
                render: function() {
                    var e, f, g, h, a = this.values,
                        b = this.target,
                        c = this.regionShapes;
                    if (this.cls._super.render.call(this)) {
                        for (g = a.length; g--;)
                            if (e = this.renderRegion(g))
                                if (d.isArray(e)) {
                                    for (f = [], h = e.length; h--;) e[h].append(), f.push(e[h].id);
                                    c[g] = f
                                } else e.append(), c[g] = e.id;
                        else c[g] = null;
                        b.render()
                    }
                }
            }, d.fn.sparkline.line = x = g(d.fn.sparkline._base, {
                type: "line",
                init: function(a, b, c, d, e) {
                    x._super.init.call(this, a, b, c, d, e), this.vertices = [], this.regionMap = [], this.xvalues = [], this.yvalues = [], this.yminmax = [], this.hightlightSpotId = null, this.lastShapeId = null, this.initTarget()
                },
                getRegion: function(a, b) {
                    var e, f = this.regionMap;
                    for (e = f.length; e--;)
                        if (null !== f[e] && b >= f[e][0] && b <= f[e][1]) return f[e][2];
                    return c
                },
                getCurrentRegionFields: function() {
                    var a = this.currentRegion;
                    return {
                        isNull: null === this.yvalues[a],
                        x: this.xvalues[a],
                        y: this.yvalues[a],
                        color: this.options.get("lineColor"),
                        fillColor: this.options.get("fillColor"),
                        offset: a
                    }
                },
                renderHighlight: function() {
                    var i, j, a = this.currentRegion,
                        b = this.target,
                        d = this.vertices[a],
                        e = this.options,
                        f = e.get("spotRadius"),
                        g = e.get("highlightSpotColor"),
                        h = e.get("highlightLineColor");
                    d && (f && g && (i = b.drawCircle(d[0], d[1], f, c, g), this.highlightSpotId = i.id, b.insertAfterShape(this.lastShapeId, i)), h && (j = b.drawLine(d[0], this.canvasTop, d[0], this.canvasTop + this.canvasHeight, h), this.highlightLineId = j.id, b.insertAfterShape(this.lastShapeId, j)))
                },
                removeHighlight: function() {
                    var a = this.target;
                    this.highlightSpotId && (a.removeShapeId(this.highlightSpotId), this.highlightSpotId = null), this.highlightLineId && (a.removeShapeId(this.highlightLineId), this.highlightLineId = null)
                },
                scanValues: function() {
                    var g, h, i, j, k, a = this.values,
                        c = a.length,
                        d = this.xvalues,
                        e = this.yvalues,
                        f = this.yminmax;
                    for (g = 0; c > g; g++) h = a[g], i = "string" == typeof a[g], j = "object" == typeof a[g] && a[g] instanceof Array, k = i && a[g].split(":"), i && 2 === k.length ? (d.push(Number(k[0])), e.push(Number(k[1])), f.push(Number(k[1]))) : j ? (d.push(h[0]), e.push(h[1]), f.push(h[1])) : (d.push(g), null === a[g] || "null" === a[g] ? e.push(null) : (e.push(Number(h)), f.push(Number(h))));
                    this.options.get("xvalues") && (d = this.options.get("xvalues")), this.maxy = this.maxyorg = b.max.apply(b, f), this.miny = this.minyorg = b.min.apply(b, f), this.maxx = b.max.apply(b, d), this.minx = b.min.apply(b, d), this.xvalues = d, this.yvalues = e, this.yminmax = f
                },
                processRangeOptions: function() {
                    var a = this.options,
                        b = a.get("normalRangeMin"),
                        d = a.get("normalRangeMax");
                    b !== c && (b < this.miny && (this.miny = b), d > this.maxy && (this.maxy = d)), a.get("chartRangeMin") !== c && (a.get("chartRangeClip") || a.get("chartRangeMin") < this.miny) && (this.miny = a.get("chartRangeMin")), a.get("chartRangeMax") !== c && (a.get("chartRangeClip") || a.get("chartRangeMax") > this.maxy) && (this.maxy = a.get("chartRangeMax")), a.get("chartRangeMinX") !== c && (a.get("chartRangeClipX") || a.get("chartRangeMinX") < this.minx) && (this.minx = a.get("chartRangeMinX")), a.get("chartRangeMaxX") !== c && (a.get("chartRangeClipX") || a.get("chartRangeMaxX") > this.maxx) && (this.maxx = a.get("chartRangeMaxX"))
                },
                drawNormalRange: function(a, d, e, f, g) {
                    var h = this.options.get("normalRangeMin"),
                        i = this.options.get("normalRangeMax"),
                        j = d + b.round(e - e * ((i - this.miny) / g)),
                        k = b.round(e * (i - h) / g);
                    this.target.drawRect(a, j, f, k, c, this.options.get("normalRangeColor")).append()
                },
                render: function() {
                    var k, l, m, n, o, p, q, r, s, u, v, w, y, z, A, B, C, D, E, F, G, H, I, J, K, a = this.options,
                        e = this.target,
                        f = this.canvasWidth,
                        g = this.canvasHeight,
                        h = this.vertices,
                        i = a.get("spotRadius"),
                        j = this.regionMap;
                    if (x._super.render.call(this) && (this.scanValues(), this.processRangeOptions(), I = this.xvalues, J = this.yvalues, this.yminmax.length && !(this.yvalues.length < 2))) {
                        for (n = o = 0, k = this.maxx - this.minx === 0 ? 1 : this.maxx - this.minx, l = this.maxy - this.miny === 0 ? 1 : this.maxy - this.miny, m = this.yvalues.length - 1, i && (4 * i > f || 4 * i > g) && (i = 0), i && (G = a.get("highlightSpotColor") && !a.get("disableInteraction"), (G || a.get("minSpotColor") || a.get("spotColor") && J[m] === this.miny) && (g -= b.ceil(i)), (G || a.get("maxSpotColor") || a.get("spotColor") && J[m] === this.maxy) && (g -= b.ceil(i), n += b.ceil(i)), (G || (a.get("minSpotColor") || a.get("maxSpotColor")) && (J[0] === this.miny || J[0] === this.maxy)) && (o += b.ceil(i), f -= b.ceil(i)), (G || a.get("spotColor") || a.get("minSpotColor") || a.get("maxSpotColor") && (J[m] === this.miny || J[m] === this.maxy)) && (f -= b.ceil(i))), g--, a.get("normalRangeMin") !== c && !a.get("drawNormalOnTop") && this.drawNormalRange(o, n, g, f, l), q = [], r = [q], z = A = null, B = J.length, K = 0; B > K; K++) s = I[K], v = I[K + 1], u = J[K], w = o + b.round((s - this.minx) * (f / k)), y = B - 1 > K ? o + b.round((v - this.minx) * (f / k)) : f, A = w + (y - w) / 2, j[K] = [z || 0, A, K], z = A, null === u ? K && (null !== J[K - 1] && (q = [], r.push(q)), h.push(null)) : (u < this.miny && (u = this.miny), u > this.maxy && (u = this.maxy), q.length || q.push([w, n + g]), p = [w, n + b.round(g - g * ((u - this.miny) / l))], q.push(p), h.push(p));
                        for (C = [], D = [], E = r.length, K = 0; E > K; K++) q = r[K], q.length && (a.get("fillColor") && (q.push([q[q.length - 1][0], n + g]), D.push(q.slice(0)), q.pop()), q.length > 2 && (q[0] = [q[0][0], q[1][1]]), C.push(q));
                        for (E = D.length, K = 0; E > K; K++) e.drawShape(D[K], a.get("fillColor"), a.get("fillColor")).append();
                        for (a.get("normalRangeMin") !== c && a.get("drawNormalOnTop") && this.drawNormalRange(o, n, g, f, l), E = C.length, K = 0; E > K; K++) e.drawShape(C[K], a.get("lineColor"), c, a.get("lineWidth")).append();
                        if (i && a.get("valueSpots"))
                            for (F = a.get("valueSpots"), F.get === c && (F = new t(F)), K = 0; B > K; K++) H = F.get(J[K]), H && e.drawCircle(o + b.round((I[K] - this.minx) * (f / k)), n + b.round(g - g * ((J[K] - this.miny) / l)), i, c, H).append();
                        i && a.get("spotColor") && null !== J[m] && e.drawCircle(o + b.round((I[I.length - 1] - this.minx) * (f / k)), n + b.round(g - g * ((J[m] - this.miny) / l)), i, c, a.get("spotColor")).append(), this.maxy !== this.minyorg && (i && a.get("minSpotColor") && (s = I[d.inArray(this.minyorg, J)], e.drawCircle(o + b.round((s - this.minx) * (f / k)), n + b.round(g - g * ((this.minyorg - this.miny) / l)), i, c, a.get("minSpotColor")).append()), i && a.get("maxSpotColor") && (s = I[d.inArray(this.maxyorg, J)], e.drawCircle(o + b.round((s - this.minx) * (f / k)), n + b.round(g - g * ((this.maxyorg - this.miny) / l)), i, c, a.get("maxSpotColor")).append())), this.lastShapeId = e.getLastShapeId(), this.canvasTop = n, e.render()
                    }
                }
            }), d.fn.sparkline.bar = y = g(d.fn.sparkline._base, w, {
                type: "bar",
                init: function(a, e, f, g, h) {
                    var u, v, w, x, z, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, j = parseInt(f.get("barWidth"), 10),
                        n = parseInt(f.get("barSpacing"), 10),
                        o = f.get("chartRangeMin"),
                        p = f.get("chartRangeMax"),
                        q = f.get("chartRangeClip"),
                        r = 1 / 0,
                        s = -(1 / 0);
                    for (y._super.init.call(this, a, e, f, g, h), A = 0, B = e.length; B > A; A++) O = e[A], u = "string" == typeof O && O.indexOf(":") > -1, (u || d.isArray(O)) && (J = !0, u && (O = e[A] = l(O.split(":"))), O = m(O, null), v = b.min.apply(b, O), w = b.max.apply(b, O), r > v && (r = v), w > s && (s = w));
                    this.stacked = J, this.regionShapes = {}, this.barWidth = j, this.barSpacing = n, this.totalBarWidth = j + n, this.width = g = e.length * j + (e.length - 1) * n, this.initTarget(), q && (H = o === c ? -(1 / 0) : o, I = p === c ? 1 / 0 : p), z = [], x = J ? [] : z;
                    var S = [],
                        T = [];
                    for (A = 0, B = e.length; B > A; A++)
                        if (J)
                            for (K = e[A], e[A] = N = [], S[A] = 0, x[A] = T[A] = 0, L = 0, M = K.length; M > L; L++) O = N[L] = q ? i(K[L], H, I) : K[L], null !== O && (O > 0 && (S[A] += O), 0 > r && s > 0 ? 0 > O ? T[A] += b.abs(O) : x[A] += O : x[A] += b.abs(O - (0 > O ? s : r)), z.push(O));
                        else O = q ? i(e[A], H, I) : e[A], O = e[A] = k(O), null !== O && z.push(O);
                    this.max = G = b.max.apply(b, z), this.min = F = b.min.apply(b, z), this.stackMax = s = J ? b.max.apply(b, S) : G, this.stackMin = r = J ? b.min.apply(b, z) : F, f.get("chartRangeMin") !== c && (f.get("chartRangeClip") || f.get("chartRangeMin") < F) && (F = f.get("chartRangeMin")), f.get("chartRangeMax") !== c && (f.get("chartRangeClip") || f.get("chartRangeMax") > G) && (G = f.get("chartRangeMax")), this.zeroAxis = D = f.get("zeroAxis", !0), E = 0 >= F && G >= 0 && D ? 0 : 0 == D ? F : F > 0 ? F : G, this.xaxisOffset = E, C = J ? b.max.apply(b, x) + b.max.apply(b, T) : G - F, this.canvasHeightEf = D && 0 > F ? this.canvasHeight - 2 : this.canvasHeight - 1, E > F ? (Q = J && G >= 0 ? s : G, P = (Q - E) / C * this.canvasHeight, P !== b.ceil(P) && (this.canvasHeightEf -= 2, P = b.ceil(P))) : P = this.canvasHeight, this.yoffset = P, d.isArray(f.get("colorMap")) ? (this.colorMapByIndex = f.get("colorMap"), this.colorMapByValue = null) : (this.colorMapByIndex = null, this.colorMapByValue = f.get("colorMap"), this.colorMapByValue && this.colorMapByValue.get === c && (this.colorMapByValue = new t(this.colorMapByValue))), this.range = C
                },
                getRegion: function(a, d) {
                    var f = b.floor(d / this.totalBarWidth);
                    return 0 > f || f >= this.values.length ? c : f
                },
                getCurrentRegionFields: function() {
                    var d, e, a = this.currentRegion,
                        b = r(this.values[a]),
                        c = [];
                    for (e = b.length; e--;) d = b[e], c.push({
                        isNull: null === d,
                        value: d,
                        color: this.calcColor(e, d, a),
                        offset: a
                    });
                    return c
                },
                calcColor: function(a, b, e) {
                    var i, j, f = this.colorMapByIndex,
                        g = this.colorMapByValue,
                        h = this.options;
                    return i = h.get(this.stacked ? "stackedBarColor" : 0 > b ? "negBarColor" : "barColor"), 0 === b && h.get("zeroColor") !== c && (i = h.get("zeroColor")), g && (j = g.get(b)) ? i = j : f && f.length > e && (i = f[e]), d.isArray(i) ? i[a % i.length] : i
                },
                renderRegion: function(a, e) {
                    var q, r, s, t, u, v, w, x, y, z, f = this.values[a],
                        g = this.options,
                        h = this.xaxisOffset,
                        i = [],
                        j = this.range,
                        k = this.stacked,
                        l = this.target,
                        m = a * this.totalBarWidth,
                        n = this.canvasHeightEf,
                        p = this.yoffset;
                    if (f = d.isArray(f) ? f : [f], w = f.length, x = f[0], t = o(null, f), z = o(h, f, !0), t) return g.get("nullColor") ? (s = e ? g.get("nullColor") : this.calcHighlightColor(g.get("nullColor"), g), q = p > 0 ? p - 1 : p, l.drawRect(m, q, this.barWidth - 1, 0, s, s)) : c;
                    for (u = p, v = 0; w > v; v++) {
                        if (x = f[v], k && x === h) {
                            if (!z || y) continue;
                            y = !0
                        }
                        r = j > 0 ? b.floor(n * (b.abs(x - h) / j)) + 1 : 1, h > x || x === h && 0 === p ? (q = u, u += r) : (q = p - r, p -= r), s = this.calcColor(v, x, a), e && (s = this.calcHighlightColor(s, g)), i.push(l.drawRect(m, q, this.barWidth - 1, r - 1, s, s))
                    }
                    return 1 === i.length ? i[0] : i
                }
            }), d.fn.sparkline.tristate = z = g(d.fn.sparkline._base, w, {
                type: "tristate",
                init: function(a, b, e, f, g) {
                    var h = parseInt(e.get("barWidth"), 10),
                        i = parseInt(e.get("barSpacing"), 10);
                    z._super.init.call(this, a, b, e, f, g), this.regionShapes = {}, this.barWidth = h, this.barSpacing = i, this.totalBarWidth = h + i, this.values = d.map(b, Number), this.width = f = b.length * h + (b.length - 1) * i, d.isArray(e.get("colorMap")) ? (this.colorMapByIndex = e.get("colorMap"), this.colorMapByValue = null) : (this.colorMapByIndex = null, this.colorMapByValue = e.get("colorMap"), this.colorMapByValue && this.colorMapByValue.get === c && (this.colorMapByValue = new t(this.colorMapByValue))), this.initTarget()
                },
                getRegion: function(a, c) {
                    return b.floor(c / this.totalBarWidth)
                },
                getCurrentRegionFields: function() {
                    var a = this.currentRegion;
                    return {
                        isNull: this.values[a] === c,
                        value: this.values[a],
                        color: this.calcColor(this.values[a], a),
                        offset: a
                    }
                },
                calcColor: function(a, b) {
                    var g, h, c = this.values,
                        d = this.options,
                        e = this.colorMapByIndex,
                        f = this.colorMapByValue;
                    return g = f && (h = f.get(a)) ? h : e && e.length > b ? e[b] : d.get(c[b] < 0 ? "negBarColor" : c[b] > 0 ? "posBarColor" : "zeroBarColor")
                },
                renderRegion: function(a, c) {
                    var g, h, i, j, k, l, d = this.values,
                        e = this.options,
                        f = this.target;
                    return g = f.pixelHeight, i = b.round(g / 2), j = a * this.totalBarWidth, d[a] < 0 ? (k = i, h = i - 1) : d[a] > 0 ? (k = 0, h = i - 1) : (k = i - 1, h = 2), l = this.calcColor(d[a], a), null !== l ? (c && (l = this.calcHighlightColor(l, e)), f.drawRect(j, k, this.barWidth - 1, h - 1, l, l)) : void 0
                }
            }), d.fn.sparkline.discrete = A = g(d.fn.sparkline._base, w, {
                type: "discrete",
                init: function(a, e, f, g, h) {
                    A._super.init.call(this, a, e, f, g, h), this.regionShapes = {}, this.values = e = d.map(e, Number), this.min = b.min.apply(b, e), this.max = b.max.apply(b, e), this.range = this.max - this.min, this.width = g = "auto" === f.get("width") ? 2 * e.length : this.width, this.interval = b.floor(g / e.length), this.itemWidth = g / e.length, f.get("chartRangeMin") !== c && (f.get("chartRangeClip") || f.get("chartRangeMin") < this.min) && (this.min = f.get("chartRangeMin")), f.get("chartRangeMax") !== c && (f.get("chartRangeClip") || f.get("chartRangeMax") > this.max) && (this.max = f.get("chartRangeMax")), this.initTarget(), this.target && (this.lineHeight = "auto" === f.get("lineHeight") ? b.round(.3 * this.canvasHeight) : f.get("lineHeight"))
                },
                getRegion: function(a, c) {
                    return b.floor(c / this.itemWidth)
                },
                getCurrentRegionFields: function() {
                    var a = this.currentRegion;
                    return {
                        isNull: this.values[a] === c,
                        value: this.values[a],
                        offset: a
                    }
                },
                renderRegion: function(a, c) {
                    var o, p, q, r, d = this.values,
                        e = this.options,
                        f = this.min,
                        g = this.max,
                        h = this.range,
                        j = this.interval,
                        k = this.target,
                        l = this.canvasHeight,
                        m = this.lineHeight,
                        n = l - m;
                    return p = i(d[a], f, g), r = a * j, o = b.round(n - n * ((p - f) / h)), q = e.get(e.get("thresholdColor") && p < e.get("thresholdValue") ? "thresholdColor" : "lineColor"), c && (q = this.calcHighlightColor(q, e)), k.drawLine(r, o, r, o + m, q)
                }
            }), d.fn.sparkline.bullet = B = g(d.fn.sparkline._base, {
                type: "bullet",
                init: function(a, d, e, f, g) {
                    var h, i, j;
                    B._super.init.call(this, a, d, e, f, g), this.values = d = l(d), j = d.slice(), j[0] = null === j[0] ? j[2] : j[0], j[1] = null === d[1] ? j[2] : j[1], h = b.min.apply(b, d), i = b.max.apply(b, d), h = e.get("base") === c ? 0 > h ? h : 0 : e.get("base"), this.min = h, this.max = i, this.range = i - h, this.shapes = {}, this.valueShapes = {}, this.regiondata = {}, this.width = f = "auto" === e.get("width") ? "4.0em" : f, this.target = this.$el.simpledraw(f, g, e.get("composite")), d.length || (this.disabled = !0), this.initTarget()
                },
                getRegion: function(a, b, d) {
                    var e = this.target.getShapeAt(a, b, d);
                    return e !== c && this.shapes[e] !== c ? this.shapes[e] : c
                },
                getCurrentRegionFields: function() {
                    var a = this.currentRegion;
                    return {
                        fieldkey: a.substr(0, 1),
                        value: this.values[a.substr(1)],
                        region: a
                    }
                },
                changeHighlight: function(a) {
                    var d, b = this.currentRegion,
                        c = this.valueShapes[b];
                    switch (delete this.shapes[c], b.substr(0, 1)) {
                        case "r":
                            d = this.renderRange(b.substr(1), a);
                            break;
                        case "p":
                            d = this.renderPerformance(a);
                            break;
                        case "t":
                            d = this.renderTarget(a)
                    }
                    this.valueShapes[b] = d.id, this.shapes[d.id] = b, this.target.replaceWithShape(c, d)
                },
                renderRange: function(a, c) {
                    var d = this.values[a],
                        e = b.round(this.canvasWidth * ((d - this.min) / this.range)),
                        f = this.options.get("rangeColors")[a - 2];
                    return c && (f = this.calcHighlightColor(f, this.options)), this.target.drawRect(0, 0, e - 1, this.canvasHeight - 1, f, f)
                },
                renderPerformance: function(a) {
                    var c = this.values[1],
                        d = b.round(this.canvasWidth * ((c - this.min) / this.range)),
                        e = this.options.get("performanceColor");
                    return a && (e = this.calcHighlightColor(e, this.options)), this.target.drawRect(0, b.round(.3 * this.canvasHeight), d - 1, b.round(.4 * this.canvasHeight) - 1, e, e)
                },
                renderTarget: function(a) {
                    var c = this.values[0],
                        d = b.round(this.canvasWidth * ((c - this.min) / this.range) - this.options.get("targetWidth") / 2),
                        e = b.round(.1 * this.canvasHeight),
                        f = this.canvasHeight - 2 * e,
                        g = this.options.get("targetColor");
                    return a && (g = this.calcHighlightColor(g, this.options)), this.target.drawRect(d, e, this.options.get("targetWidth") - 1, f - 1, g, g)
                },
                render: function() {
                    var c, d, a = this.values.length,
                        b = this.target;
                    if (B._super.render.call(this)) {
                        for (c = 2; a > c; c++) d = this.renderRange(c).append(), this.shapes[d.id] = "r" + c, this.valueShapes["r" + c] = d.id;
                        null !== this.values[1] && (d = this.renderPerformance().append(), this.shapes[d.id] = "p1", this.valueShapes.p1 = d.id), null !== this.values[0] && (d = this.renderTarget().append(), this.shapes[d.id] = "t0", this.valueShapes.t0 = d.id), b.render()
                    }
                }
            }), d.fn.sparkline.pie = C = g(d.fn.sparkline._base, {
                type: "pie",
                init: function(a, c, e, f, g) {
                    var i, h = 0;
                    if (C._super.init.call(this, a, c, e, f, g), this.shapes = {}, this.valueShapes = {}, this.values = c = d.map(c, Number), "auto" === e.get("width") && (this.width = this.height), c.length > 0)
                        for (i = c.length; i--;) h += c[i];
                    this.total = h, this.initTarget(), this.radius = b.floor(b.min(this.canvasWidth, this.canvasHeight) / 2)
                },
                getRegion: function(a, b, d) {
                    var e = this.target.getShapeAt(a, b, d);
                    return e !== c && this.shapes[e] !== c ? this.shapes[e] : c
                },
                getCurrentRegionFields: function() {
                    var a = this.currentRegion;
                    return {
                        isNull: this.values[a] === c,
                        value: this.values[a],
                        percent: this.values[a] / this.total * 100,
                        color: this.options.get("sliceColors")[a % this.options.get("sliceColors").length],
                        offset: a
                    }
                },
                changeHighlight: function(a) {
                    var b = this.currentRegion,
                        c = this.renderSlice(b, a),
                        d = this.valueShapes[b];
                    delete this.shapes[d], this.target.replaceWithShape(d, c), this.valueShapes[b] = c.id, this.shapes[c.id] = b
                },
                renderSlice: function(a, d) {
                    var n, o, p, q, r, e = this.target,
                        f = this.options,
                        g = this.radius,
                        h = f.get("borderWidth"),
                        i = f.get("offset"),
                        j = 2 * b.PI,
                        k = this.values,
                        l = this.total,
                        m = i ? 2 * b.PI * (i / 360) : 0;
                    for (q = k.length, p = 0; q > p; p++) {
                        if (n = m, o = m, l > 0 && (o = m + j * (k[p] / l)), a === p) return r = f.get("sliceColors")[p % f.get("sliceColors").length], d && (r = this.calcHighlightColor(r, f)), e.drawPieSlice(g, g, g - h, n, o, c, r);
                        m = o
                    }
                },
                render: function() {
                    var h, i, a = this.target,
                        d = this.values,
                        e = this.options,
                        f = this.radius,
                        g = e.get("borderWidth");
                    if (C._super.render.call(this)) {
                        for (g && a.drawCircle(f, f, b.floor(f - g / 2), e.get("borderColor"), c, g).append(), i = d.length; i--;) d[i] && (h = this.renderSlice(i).append(), this.valueShapes[i] = h.id, this.shapes[h.id] = i);
                        a.render()
                    }
                }
            }), d.fn.sparkline.box = D = g(d.fn.sparkline._base, {
                type: "box",
                init: function(a, b, c, e, f) {
                    D._super.init.call(this, a, b, c, e, f), this.values = d.map(b, Number), this.width = "auto" === c.get("width") ? "4.0em" : e, this.initTarget(), this.values.length || (this.disabled = 1)
                },
                getRegion: function() {
                    return 1
                },
                getCurrentRegionFields: function() {
                    var a = [{
                        field: "lq",
                        value: this.quartiles[0]
                    }, {
                        field: "med",
                        value: this.quartiles[1]
                    }, {
                        field: "uq",
                        value: this.quartiles[2]
                    }];
                    return this.loutlier !== c && a.push({
                        field: "lo",
                        value: this.loutlier
                    }), this.routlier !== c && a.push({
                        field: "ro",
                        value: this.routlier
                    }), this.lwhisker !== c && a.push({
                        field: "lw",
                        value: this.lwhisker
                    }), this.rwhisker !== c && a.push({
                        field: "rw",
                        value: this.rwhisker
                    }), a
                },
                render: function() {
                    var m, n, o, p, q, r, s, t, u, v, w, a = this.target,
                        d = this.values,
                        e = d.length,
                        f = this.options,
                        g = this.canvasWidth,
                        h = this.canvasHeight,
                        i = f.get("chartRangeMin") === c ? b.min.apply(b, d) : f.get("chartRangeMin"),
                        k = f.get("chartRangeMax") === c ? b.max.apply(b, d) : f.get("chartRangeMax"),
                        l = 0;
                    if (D._super.render.call(this)) {
                        if (f.get("raw")) f.get("showOutliers") && d.length > 5 ? (n = d[0], m = d[1], p = d[2], q = d[3], r = d[4], s = d[5], t = d[6]) : (m = d[0], p = d[1], q = d[2], r = d[3], s = d[4]);
                        else if (d.sort(function(a, b) {
                                return a - b
                            }), p = j(d, 1), q = j(d, 2), r = j(d, 3), o = r - p, f.get("showOutliers")) {
                            for (m = s = c, u = 0; e > u; u++) m === c && d[u] > p - o * f.get("outlierIQR") && (m = d[u]), d[u] < r + o * f.get("outlierIQR") && (s = d[u]);
                            n = d[0], t = d[e - 1]
                        } else m = d[0], s = d[e - 1];
                        this.quartiles = [p, q, r], this.lwhisker = m, this.rwhisker = s, this.loutlier = n, this.routlier = t, w = g / (k - i + 1), f.get("showOutliers") && (l = b.ceil(f.get("spotRadius")), g -= 2 * b.ceil(f.get("spotRadius")), w = g / (k - i + 1), m > n && a.drawCircle((n - i) * w + l, h / 2, f.get("spotRadius"), f.get("outlierLineColor"), f.get("outlierFillColor")).append(), t > s && a.drawCircle((t - i) * w + l, h / 2, f.get("spotRadius"), f.get("outlierLineColor"), f.get("outlierFillColor")).append()), a.drawRect(b.round((p - i) * w + l), b.round(.1 * h), b.round((r - p) * w), b.round(.8 * h), f.get("boxLineColor"), f.get("boxFillColor")).append(), a.drawLine(b.round((m - i) * w + l), b.round(h / 2), b.round((p - i) * w + l), b.round(h / 2), f.get("lineColor")).append(), a.drawLine(b.round((m - i) * w + l), b.round(h / 4), b.round((m - i) * w + l), b.round(h - h / 4), f.get("whiskerColor")).append(), a.drawLine(b.round((s - i) * w + l), b.round(h / 2), b.round((r - i) * w + l), b.round(h / 2), f.get("lineColor")).append(), a.drawLine(b.round((s - i) * w + l), b.round(h / 4), b.round((s - i) * w + l), b.round(h - h / 4), f.get("whiskerColor")).append(), a.drawLine(b.round((q - i) * w + l), b.round(.1 * h), b.round((q - i) * w + l), b.round(.9 * h), f.get("medianColor")).append(), f.get("target") && (v = b.ceil(f.get("spotRadius")), a.drawLine(b.round((f.get("target") - i) * w + l), b.round(h / 2 - v), b.round((f.get("target") - i) * w + l), b.round(h / 2 + v), f.get("targetColor")).append(), a.drawLine(b.round((f.get("target") - i) * w + l - v), b.round(h / 2), b.round((f.get("target") - i) * w + l + v), b.round(h / 2), f.get("targetColor")).append()), a.render()
                    }
                }
            }), G = g({
                init: function(a, b, c, d) {
                    this.target = a, this.id = b, this.type = c, this.args = d
                },
                append: function() {
                    return this.target.appendShape(this), this
                }
            }), H = g({
                _pxregex: /(\d+)(px)?\s*$/i,
                init: function(a, b, c) {
                    a && (this.width = a, this.height = b, this.target = c, this.lastShapeId = null, c[0] && (c = c[0]), d.data(c, "_jqs_vcanvas", this))
                },
                drawLine: function(a, b, c, d, e, f) {
                    return this.drawShape([
                        [a, b],
                        [c, d]
                    ], e, f)
                },
                drawShape: function(a, b, c, d) {
                    return this._genShape("Shape", [a, b, c, d])
                },
                drawCircle: function(a, b, c, d, e, f) {
                    return this._genShape("Circle", [a, b, c, d, e, f])
                },
                drawPieSlice: function(a, b, c, d, e, f, g) {
                    return this._genShape("PieSlice", [a, b, c, d, e, f, g])
                },
                drawRect: function(a, b, c, d, e, f) {
                    return this._genShape("Rect", [a, b, c, d, e, f])
                },
                getElement: function() {
                    return this.canvas
                },
                getLastShapeId: function() {
                    return this.lastShapeId
                },
                reset: function() {
                    alert("reset not implemented")
                },
                _insert: function(a, b) {
                    d(b).html(a)
                },
                _calculatePixelDims: function(a, b, c) {
                    var e;
                    e = this._pxregex.exec(b), this.pixelHeight = e ? e[1] : d(c).height(), e = this._pxregex.exec(a), this.pixelWidth = e ? e[1] : d(c).width()
                },
                _genShape: function(a, b) {
                    var c = L++;
                    return b.unshift(c), new G(this, c, a, b)
                },
                appendShape: function() {
                    alert("appendShape not implemented")
                },
                replaceWithShape: function() {
                    alert("replaceWithShape not implemented")
                },
                insertAfterShape: function() {
                    alert("insertAfterShape not implemented")
                },
                removeShapeId: function() {
                    alert("removeShapeId not implemented")
                },
                getShapeAt: function() {
                    alert("getShapeAt not implemented")
                },
                render: function() {
                    alert("render not implemented")
                }
            }), I = g(H, {
                init: function(b, e, f, g) {
                    I._super.init.call(this, b, e, f), this.canvas = a.createElement("canvas"), f[0] && (f = f[0]), d.data(f, "_jqs_vcanvas", this), d(this.canvas).css({
                        display: "inline-block",
                        width: b,
                        height: e,
                        verticalAlign: "top"
                    }), this._insert(this.canvas, f), this._calculatePixelDims(b, e, this.canvas), this.canvas.width = this.pixelWidth, this.canvas.height = this.pixelHeight, this.interact = g, this.shapes = {}, this.shapeseq = [], this.currentTargetShapeId = c, d(this.canvas).css({
                        width: this.pixelWidth,
                        height: this.pixelHeight
                    })
                },
                _getContext: function(a, b, d) {
                    var e = this.canvas.getContext("2d");
                    return a !== c && (e.strokeStyle = a), e.lineWidth = d === c ? 1 : d, b !== c && (e.fillStyle = b), e
                },
                reset: function() {
                    var a = this._getContext();
                    a.clearRect(0, 0, this.pixelWidth, this.pixelHeight), this.shapes = {}, this.shapeseq = [], this.currentTargetShapeId = c
                },
                _drawShape: function(a, b, d, e, f) {
                    var h, i, g = this._getContext(d, e, f);
                    for (g.beginPath(), g.moveTo(b[0][0] + .5, b[0][1] + .5), h = 1, i = b.length; i > h; h++) g.lineTo(b[h][0] + .5, b[h][1] + .5);
                    d !== c && g.stroke(), e !== c && g.fill(), this.targetX !== c && this.targetY !== c && g.isPointInPath(this.targetX, this.targetY) && (this.currentTargetShapeId = a)
                },
                _drawCircle: function(a, d, e, f, g, h, i) {
                    var j = this._getContext(g, h, i);
                    j.beginPath(), j.arc(d, e, f, 0, 2 * b.PI, !1), this.targetX !== c && this.targetY !== c && j.isPointInPath(this.targetX, this.targetY) && (this.currentTargetShapeId = a), g !== c && j.stroke(), h !== c && j.fill()
                },
                _drawPieSlice: function(a, b, d, e, f, g, h, i) {
                    var j = this._getContext(h, i);
                    j.beginPath(), j.moveTo(b, d), j.arc(b, d, e, f, g, !1), j.lineTo(b, d), j.closePath(), h !== c && j.stroke(), i && j.fill(), this.targetX !== c && this.targetY !== c && j.isPointInPath(this.targetX, this.targetY) && (this.currentTargetShapeId = a)
                },
                _drawRect: function(a, b, c, d, e, f, g) {
                    return this._drawShape(a, [
                        [b, c],
                        [b + d, c],
                        [b + d, c + e],
                        [b, c + e],
                        [b, c]
                    ], f, g)
                },
                appendShape: function(a) {
                    return this.shapes[a.id] = a, this.shapeseq.push(a.id), this.lastShapeId = a.id, a.id
                },
                replaceWithShape: function(a, b) {
                    var d, c = this.shapeseq;
                    for (this.shapes[b.id] = b, d = c.length; d--;) c[d] == a && (c[d] = b.id);
                    delete this.shapes[a]
                },
                replaceWithShapes: function(a, b) {
                    var e, f, g, c = this.shapeseq,
                        d = {};
                    for (f = a.length; f--;) d[a[f]] = !0;
                    for (f = c.length; f--;) e = c[f], d[e] && (c.splice(f, 1), delete this.shapes[e], g = f);
                    for (f = b.length; f--;) c.splice(g, 0, b[f].id), this.shapes[b[f].id] = b[f]
                },
                insertAfterShape: function(a, b) {
                    var d, c = this.shapeseq;
                    for (d = c.length; d--;)
                        if (c[d] === a) return c.splice(d + 1, 0, b.id), void(this.shapes[b.id] = b)
                },
                removeShapeId: function(a) {
                    var c, b = this.shapeseq;
                    for (c = b.length; c--;)
                        if (b[c] === a) {
                            b.splice(c, 1);
                            break
                        }
                    delete this.shapes[a]
                },
                getShapeAt: function(a, b, c) {
                    return this.targetX = b, this.targetY = c, this.render(), this.currentTargetShapeId
                },
                render: function() {
                    var e, f, g, a = this.shapeseq,
                        b = this.shapes,
                        c = a.length,
                        d = this._getContext();
                    for (d.clearRect(0, 0, this.pixelWidth, this.pixelHeight), g = 0; c > g; g++) e = a[g], f = b[e], this["_draw" + f.type].apply(this, f.args);
                    this.interact || (this.shapes = {}, this.shapeseq = [])
                }
            }), J = g(H, {
                init: function(b, c, e) {
                    var f;
                    J._super.init.call(this, b, c, e), e[0] && (e = e[0]), d.data(e, "_jqs_vcanvas", this), this.canvas = a.createElement("span"), d(this.canvas).css({
                        display: "inline-block",
                        position: "relative",
                        overflow: "hidden",
                        width: b,
                        height: c,
                        margin: "0px",
                        padding: "0px",
                        verticalAlign: "top"
                    }), this._insert(this.canvas, e), this._calculatePixelDims(b, c, this.canvas), this.canvas.width = this.pixelWidth, this.canvas.height = this.pixelHeight, f = '<v:group coordorigin="0 0" coordsize="' + this.pixelWidth + " " + this.pixelHeight + '" style="position:absolute;top:0;left:0;width:' + this.pixelWidth + "px;height=" + this.pixelHeight + 'px;"></v:group>', this.canvas.insertAdjacentHTML("beforeEnd", f), this.group = d(this.canvas).children()[0], this.rendered = !1, this.prerender = ""
                },
                _drawShape: function(a, b, d, e, f) {
                    var h, i, j, k, l, m, n, g = [];
                    for (n = 0, m = b.length; m > n; n++) g[n] = "" + b[n][0] + "," + b[n][1];
                    return h = g.splice(0, 1), f = f === c ? 1 : f, i = d === c ? ' stroked="false" ' : ' strokeWeight="' + f + 'px" strokeColor="' + d + '" ', j = e === c ? ' filled="false"' : ' fillColor="' + e + '" filled="true" ', k = g[0] === g[g.length - 1] ? "x " : "", l = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + " " + this.pixelHeight + '"  id="jqsshape' + a + '" ' + i + j + ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + "px;width:" + this.pixelWidth + 'px;padding:0px;margin:0px;"  path="m ' + h + " l " + g.join(", ") + " " + k + 'e"> </v:shape>'
                },
                _drawCircle: function(a, b, d, e, f, g, h) {
                    var i, j, k;
                    return b -= e, d -= e, i = f === c ? ' stroked="false" ' : ' strokeWeight="' + h + 'px" strokeColor="' + f + '" ', j = g === c ? ' filled="false"' : ' fillColor="' + g + '" filled="true" ', k = '<v:oval  id="jqsshape' + a + '" ' + i + j + ' style="position:absolute;top:' + d + "px; left:" + b + "px; width:" + 2 * e + "px; height:" + 2 * e + 'px"></v:oval>'
                },
                _drawPieSlice: function(a, d, e, f, g, h, i, j) {
                    var k, l, m, n, o, p, q, r;
                    if (g === h) return "";
                    if (h - g === 2 * b.PI && (g = 0, h = 2 * b.PI), l = d + b.round(b.cos(g) * f), m = e + b.round(b.sin(g) * f), n = d + b.round(b.cos(h) * f), o = e + b.round(b.sin(h) * f), l === n && m === o) {
                        if (h - g < b.PI) return "";
                        l = n = d + f, m = o = e
                    }
                    return l === n && m === o && h - g < b.PI ? "" : (k = [d - f, e - f, d + f, e + f, l, m, n, o], p = i === c ? ' stroked="false" ' : ' strokeWeight="1px" strokeColor="' + i + '" ', q = j === c ? ' filled="false"' : ' fillColor="' + j + '" filled="true" ', r = '<v:shape coordorigin="0 0" coordsize="' + this.pixelWidth + " " + this.pixelHeight + '"  id="jqsshape' + a + '" ' + p + q + ' style="position:absolute;left:0px;top:0px;height:' + this.pixelHeight + "px;width:" + this.pixelWidth + 'px;padding:0px;margin:0px;"  path="m ' + d + "," + e + " wa " + k.join(", ") + ' x e"> </v:shape>')
                },
                _drawRect: function(a, b, c, d, e, f, g) {
                    return this._drawShape(a, [
                        [b, c],
                        [b, c + e],
                        [b + d, c + e],
                        [b + d, c],
                        [b, c]
                    ], f, g)
                },
                reset: function() {
                    this.group.innerHTML = ""
                },
                appendShape: function(a) {
                    var b = this["_draw" + a.type].apply(this, a.args);
                    return this.rendered ? this.group.insertAdjacentHTML("beforeEnd", b) : this.prerender += b, this.lastShapeId = a.id, a.id
                },
                replaceWithShape: function(a, b) {
                    var c = d("#jqsshape" + a),
                        e = this["_draw" + b.type].apply(this, b.args);
                    c[0].outerHTML = e
                },
                replaceWithShapes: function(a, b) {
                    var g, c = d("#jqsshape" + a[0]),
                        e = "",
                        f = b.length;
                    for (g = 0; f > g; g++) e += this["_draw" + b[g].type].apply(this, b[g].args);
                    for (c[0].outerHTML = e, g = 1; g < a.length; g++) d("#jqsshape" + a[g]).remove()
                },
                insertAfterShape: function(a, b) {
                    var c = d("#jqsshape" + a),
                        e = this["_draw" + b.type].apply(this, b.args);
                    c[0].insertAdjacentHTML("afterEnd", e)
                },
                removeShapeId: function(a) {
                    var b = d("#jqsshape" + a);
                    this.group.removeChild(b[0])
                },
                getShapeAt: function(a) {
                    var d = a.id.substr(8);
                    return d
                },
                render: function() {
                    this.rendered || (this.group.innerHTML = this.prerender, this.rendered = !0)
                }
            })
        })
    }(document, Math),
    function(global) {
        "use strict";

        function circle(ctx, x, y, r) {
            ctx.beginPath(), ctx.arc(x, y, r, 0, TAU, !1), ctx.fill()
        }

        function line(ctx, ax, ay, bx, by) {
            ctx.beginPath(), ctx.moveTo(ax, ay), ctx.lineTo(bx, by), ctx.stroke()
        }

        function puff(ctx, t, cx, cy, rx, ry, rmin, rmax) {
            var c = Math.cos(t * TAU),
                s = Math.sin(t * TAU);
            rmax -= rmin, circle(ctx, cx - s * rx, cy + c * ry + .5 * rmax, rmin + (1 - .5 * c) * rmax)
        }

        function puffs(ctx, t, cx, cy, rx, ry, rmin, rmax) {
            var i;
            for (i = 5; i--;) puff(ctx, t + i / 5, cx, cy, rx, ry, rmin, rmax)
        }

        function cloud(ctx, t, cx, cy, cw, s, color) {
            t /= 3e4;
            var a = .21 * cw,
                b = .12 * cw,
                c = .24 * cw,
                d = .28 * cw;
            ctx.fillStyle = color, puffs(ctx, t, cx, cy, a, b, c, d), ctx.globalCompositeOperation = "destination-out", puffs(ctx, t, cx, cy, a, b, c - s, d - s), ctx.globalCompositeOperation = "source-over"
        }

        function sun(ctx, t, cx, cy, cw, s, color) {
            t /= 12e4;
            var i, p, cos, sin, a = .25 * cw - .5 * s,
                b = .32 * cw + .5 * s,
                c = .5 * cw - .5 * s;
            for (ctx.strokeStyle = color, ctx.lineWidth = s, ctx.lineCap = "round", ctx.lineJoin = "round", ctx.beginPath(), ctx.arc(cx, cy, a, 0, TAU, !1), ctx.stroke(), i = 8; i--;) p = (t + i / 8) * TAU, cos = Math.cos(p), sin = Math.sin(p), line(ctx, cx + cos * b, cy + sin * b, cx + cos * c, cy + sin * c)
        }

        function moon(ctx, t, cx, cy, cw, s, color) {
            t /= 15e3;
            var a = .29 * cw - .5 * s,
                b = .05 * cw,
                c = Math.cos(t * TAU),
                p = c * TAU / -16;
            ctx.strokeStyle = color, ctx.lineWidth = s, ctx.lineCap = "round", ctx.lineJoin = "round", cx += c * b, ctx.beginPath(), ctx.arc(cx, cy, a, p + TAU / 8, p + 7 * TAU / 8, !1), ctx.arc(cx + Math.cos(p) * a * TWO_OVER_SQRT_2, cy + Math.sin(p) * a * TWO_OVER_SQRT_2, a, p + 5 * TAU / 8, p + 3 * TAU / 8, !0), ctx.closePath(), ctx.stroke()
        }

        function rain(ctx, t, cx, cy, cw, s, color) {
            t /= 1350;
            var i, p, x, y, a = .16 * cw,
                b = 11 * TAU / 12,
                c = 7 * TAU / 12;
            for (ctx.fillStyle = color, i = 4; i--;) p = (t + i / 4) % 1, x = cx + (i - 1.5) / 1.5 * (1 === i || 2 === i ? -1 : 1) * a, y = cy + p * p * cw, ctx.beginPath(), ctx.moveTo(x, y - 1.5 * s), ctx.arc(x, y, .75 * s, b, c, !1), ctx.fill()
        }

        function sleet(ctx, t, cx, cy, cw, s, color) {
            t /= 750;
            var i, p, x, y, a = .1875 * cw;
            for (ctx.strokeStyle = color, ctx.lineWidth = .5 * s, ctx.lineCap = "round", ctx.lineJoin = "round", i = 4; i--;) p = (t + i / 4) % 1, x = Math.floor(cx + (i - 1.5) / 1.5 * (1 === i || 2 === i ? -1 : 1) * a) + .5, y = cy + p * cw, line(ctx, x, y - 1.5 * s, x, y + 1.5 * s)
        }

        function snow(ctx, t, cx, cy, cw, s, color) {
            t /= 3e3;
            var i, p, x, y, a = .16 * cw,
                b = .75 * s,
                u = t * TAU * .7,
                ux = Math.cos(u) * b,
                uy = Math.sin(u) * b,
                v = u + TAU / 3,
                vx = Math.cos(v) * b,
                vy = Math.sin(v) * b,
                w = u + 2 * TAU / 3,
                wx = Math.cos(w) * b,
                wy = Math.sin(w) * b;
            for (ctx.strokeStyle = color, ctx.lineWidth = .5 * s, ctx.lineCap = "round", ctx.lineJoin = "round", i = 4; i--;) p = (t + i / 4) % 1, x = cx + Math.sin((p + i / 4) * TAU) * a, y = cy + p * cw, line(ctx, x - ux, y - uy, x + ux, y + uy), line(ctx, x - vx, y - vy, x + vx, y + vy), line(ctx, x - wx, y - wy, x + wx, y + wy)
        }

        function fogbank(ctx, t, cx, cy, cw, s, color) {
            t /= 3e4;
            var a = .21 * cw,
                b = .06 * cw,
                c = .21 * cw,
                d = .28 * cw;
            ctx.fillStyle = color, puffs(ctx, t, cx, cy, a, b, c, d), ctx.globalCompositeOperation = "destination-out", puffs(ctx, t, cx, cy, a, b, c - s, d - s), ctx.globalCompositeOperation = "source-over"
        }

        function leaf(ctx, t, x, y, cw, s, color) {
            var a = cw / 8,
                b = a / 3,
                c = 2 * b,
                d = t % 1 * TAU,
                e = Math.cos(d),
                f = Math.sin(d);
            ctx.fillStyle = color, ctx.strokeStyle = color, ctx.lineWidth = s, ctx.lineCap = "round", ctx.lineJoin = "round", ctx.beginPath(), ctx.arc(x, y, a, d, d + Math.PI, !1), ctx.arc(x - b * e, y - b * f, c, d + Math.PI, d, !1), ctx.arc(x + c * e, y + c * f, b, d + Math.PI, d, !0), ctx.globalCompositeOperation = "destination-out", ctx.fill(), ctx.globalCompositeOperation = "source-over", ctx.stroke()
        }

        function swoosh(ctx, t, cx, cy, cw, s, index, total, color) {
            t /= 2500;
            var b, d, f, i, path = WIND_PATHS[index],
                a = (t + index - WIND_OFFSETS[index].start) % total,
                c = (t + index - WIND_OFFSETS[index].end) % total,
                e = (t + index) % total;
            if (ctx.strokeStyle = color, ctx.lineWidth = s, ctx.lineCap = "round", ctx.lineJoin = "round", 1 > a) {
                if (ctx.beginPath(), a *= path.length / 2 - 1, b = Math.floor(a), a -= b, b *= 2, b += 2, ctx.moveTo(cx + (path[b - 2] * (1 - a) + path[b] * a) * cw, cy + (path[b - 1] * (1 - a) + path[b + 1] * a) * cw), 1 > c) {
                    for (c *= path.length / 2 - 1, d = Math.floor(c), c -= d, d *= 2, d += 2, i = b; i !== d; i += 2) ctx.lineTo(cx + path[i] * cw, cy + path[i + 1] * cw);
                    ctx.lineTo(cx + (path[d - 2] * (1 - c) + path[d] * c) * cw, cy + (path[d - 1] * (1 - c) + path[d + 1] * c) * cw)
                } else
                    for (i = b; i !== path.length; i += 2) ctx.lineTo(cx + path[i] * cw, cy + path[i + 1] * cw);
                ctx.stroke()
            } else if (1 > c) {
                for (ctx.beginPath(), c *= path.length / 2 - 1, d = Math.floor(c), c -= d, d *= 2, d += 2, ctx.moveTo(cx + path[0] * cw, cy + path[1] * cw), i = 2; i !== d; i += 2) ctx.lineTo(cx + path[i] * cw, cy + path[i + 1] * cw);
                ctx.lineTo(cx + (path[d - 2] * (1 - c) + path[d] * c) * cw, cy + (path[d - 1] * (1 - c) + path[d + 1] * c) * cw), ctx.stroke()
            }
            1 > e && (e *= path.length / 2 - 1, f = Math.floor(e), e -= f, f *= 2, f += 2, leaf(ctx, t, cx + (path[f - 2] * (1 - e) + path[f] * e) * cw, cy + (path[f - 1] * (1 - e) + path[f + 1] * e) * cw, cw, s, color))
        }
        var requestInterval, cancelInterval;
        ! function() {
            var raf = global.requestAnimationFrame || global.webkitRequestAnimationFrame || global.mozRequestAnimationFrame || global.oRequestAnimationFrame || global.msRequestAnimationFrame,
                caf = global.cancelAnimationFrame || global.webkitCancelAnimationFrame || global.mozCancelAnimationFrame || global.oCancelAnimationFrame || global.msCancelAnimationFrame;
            raf && caf ? (requestInterval = function(fn) {
                function loop() {
                    handle.value = raf(loop), fn()
                }
                var handle = {
                    value: null
                };
                return loop(), handle
            }, cancelInterval = function(handle) {
                caf(handle.value)
            }) : (requestInterval = setInterval, cancelInterval = clearInterval)
        }();
        var KEYFRAME = 500,
            STROKE = .08,
            TAU = 2 * Math.PI,
            TWO_OVER_SQRT_2 = 2 / Math.sqrt(2),
            WIND_PATHS = [
                [-.75, -.18, -.7219, -.1527, -.6971, -.1225, -.6739, -.091, -.6516, -.0588, -.6298, -.0262, -.6083, .0065, -.5868, .0396, -.5643, .0731, -.5372, .1041, -.5033, .1259, -.4662, .1406, -.4275, .1493, -.3881, .153, -.3487, .1526, -.3095, .1488, -.2708, .1421, -.2319, .1342, -.1943, .1217, -.16, .1025, -.129, .0785, -.1012, .0509, -.0764, .0206, -.0547, -.012, -.0378, -.0472, -.0324, -.0857, -.0389, -.1241, -.0546, -.1599, -.0814, -.1876, -.1193, -.1964, -.1582, -.1935, -.1931, -.1769, -.2157, -.1453, -.229, -.1085, -.2327, -.0697, -.224, -.0317, -.2064, .0033, -.1853, .0362, -.1613, .0672, -.135, .0961, -.1051, .1213, -.0706, .1397, -.0332, .1512, .0053, .158, .0442, .1624, .0833, .1636, .1224, .1615, .1613, .1565, .1999, .15, .2378, .1402, .2749, .1279, .3118, .1147, .3487, .1015, .3858, .0892, .4236, .0787, .4621, .0715, .5012, .0702, .5398, .0766, .5768, .089, .6123, .1055, .6466, .1244, .6805, .144, .7147, .163, .75, .18],
                [-.75, 0, -.7033, .0195, -.6569, .0399, -.6104, .06, -.5634, .0789, -.5155, .0954, -.4667, .1089, -.4174, .1206, -.3676, .1299, -.3174, .1365, -.2669, .1398, -.2162, .1391, -.1658, .1347, -.1157, .1271, -.0661, .1169, -.017, .1046, .0316, .0903, .0791, .0728, .1259, .0534, .1723, .0331, .2188, .0129, .2656, -.0064, .3122, -.0263, .3586, -.0466, .4052, -.0665, .4525, -.0847, .5007, -.1002, .5497, -.113, .5991, -.124, .6491, -.1325, .6994, -.138, .75, -.14]
            ],
            WIND_OFFSETS = [{
                start: .36,
                end: .11
            }, {
                start: .56,
                end: .16
            }],
            Skycons = function(opts) {
                this.list = [], this.interval = null, this.color = opts && opts.color ? opts.color : "black", this.resizeClear = !(!opts || !opts.resizeClear)
            };
        Skycons.CLEAR_DAY = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            sun(ctx, t, .5 * w, .5 * h, s, s * STROKE, color)
        }, Skycons.CLEAR_NIGHT = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            moon(ctx, t, .5 * w, .5 * h, s, s * STROKE, color)
        }, Skycons.PARTLY_CLOUDY_DAY = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            sun(ctx, t, .625 * w, .375 * h, .75 * s, s * STROKE, color), cloud(ctx, t, .375 * w, .625 * h, .75 * s, s * STROKE, color)
        }, Skycons.PARTLY_CLOUDY_NIGHT = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            moon(ctx, t, .667 * w, .375 * h, .75 * s, s * STROKE, color), cloud(ctx, t, .375 * w, .625 * h, .75 * s, s * STROKE, color)
        }, Skycons.CLOUDY = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            cloud(ctx, t, .5 * w, .5 * h, s, s * STROKE, color)
        }, Skycons.RAIN = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            rain(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color), cloud(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color)
        }, Skycons.SLEET = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            sleet(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color), cloud(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color)
        }, Skycons.SNOW = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            snow(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color), cloud(ctx, t, .5 * w, .37 * h, .9 * s, s * STROKE, color)
        }, Skycons.WIND = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h);
            swoosh(ctx, t, .5 * w, .5 * h, s, s * STROKE, 0, 2, color), swoosh(ctx, t, .5 * w, .5 * h, s, s * STROKE, 1, 2, color)
        }, Skycons.FOG = function(ctx, t, color) {
            var w = ctx.canvas.width,
                h = ctx.canvas.height,
                s = Math.min(w, h),
                k = s * STROKE;
            fogbank(ctx, t, .5 * w, .32 * h, .75 * s, k, color), t /= 5e3;
            var a = Math.cos(t * TAU) * s * .02,
                b = Math.cos((t + .25) * TAU) * s * .02,
                c = Math.cos((t + .5) * TAU) * s * .02,
                d = Math.cos((t + .75) * TAU) * s * .02,
                n = .936 * h,
                e = Math.floor(n - .5 * k) + .5,
                f = Math.floor(n - 2.5 * k) + .5;
            ctx.strokeStyle = color, ctx.lineWidth = k, ctx.lineCap = "round", ctx.lineJoin = "round", line(ctx, a + .2 * w + .5 * k, e, b + .8 * w - .5 * k, e), line(ctx, c + .2 * w + .5 * k, f, d + .8 * w - .5 * k, f)
        }, Skycons.prototype = {
            add: function(el, draw) {
                var obj;
                "string" == typeof el && (el = document.getElementById(el)), null !== el && ("string" == typeof draw && (draw = draw.toUpperCase().replace(/-/g, "_"), draw = Skycons.hasOwnProperty(draw) ? Skycons[draw] : null), "function" == typeof draw && (obj = {
                    element: el,
                    context: el.getContext("2d"),
                    drawing: draw
                }, this.list.push(obj), this.draw(obj, KEYFRAME)))
            },
            set: function(el, draw) {
                var i;
                for ("string" == typeof el && (el = document.getElementById(el)), i = this.list.length; i--;)
                    if (this.list[i].element === el) return this.list[i].drawing = draw, void this.draw(this.list[i], KEYFRAME);
                this.add(el, draw)
            },
            remove: function(el) {
                var i;
                for ("string" == typeof el && (el = document.getElementById(el)), i = this.list.length; i--;)
                    if (this.list[i].element === el) return void this.list.splice(i, 1)
            },
            draw: function(obj, time) {
                var canvas = obj.context.canvas;
                this.resizeClear ? canvas.width = canvas.width : obj.context.clearRect(0, 0, canvas.width, canvas.height), obj.drawing(obj.context, time, this.color)
            },
            play: function() {
                var self = this;
                this.pause(), this.interval = requestInterval(function() {
                    var i, now = Date.now();
                    for (i = self.list.length; i--;) self.draw(self.list[i], now)
                }, 1e3 / 60)
            },
            pause: function() {
                this.interval && (cancelInterval(this.interval), this.interval = null)
            }
        }, global.Skycons = Skycons
    }(this), $.fn.spin = function(opts) {
        return this.each(function() {
            var $this = $(this),
                data = $this.data();
            data.spinner && (data.spinner.stop(), delete data.spinner), opts !== !1 && (data.spinner = new Spinner($.extend({
                color: $this.css("color")
            }, opts)).spin(this))
        }), this
    },
    function(a, b, c) {
        function g(a, c) {
            var e, d = b.createElement(a || "div");
            for (e in c) d[e] = c[e];
            return d
        }

        function h(a) {
            for (var b = 1, c = arguments.length; c > b; b++) a.appendChild(arguments[b]);
            return a
        }

        function j(a, b, c, d) {
            var g = ["opacity", b, ~~(100 * a), c, d].join("-"),
                h = .01 + c / d * 100,
                j = Math.max(1 - (1 - a) / b * (100 - h), a),
                k = f.substring(0, f.indexOf("Animation")).toLowerCase(),
                l = k && "-" + k + "-" || "";
            return e[g] || (i.insertRule("@" + l + "keyframes " + g + "{0%{opacity:" + j + "}" + h + "%{opacity:" + a + "}" + (h + .01) + "%{opacity:1}" + (h + b) % 100 + "%{opacity:" + a + "}100%{opacity:" + j + "}}", 0), e[g] = 1), g
        }

        function k(a, b) {
            var f, g, e = a.style;
            if (e[b] !== c) return b;
            for (b = b.charAt(0).toUpperCase() + b.slice(1), g = 0; g < d.length; g++)
                if (f = d[g] + b, e[f] !== c) return f
        }

        function l(a, b) {
            for (var c in b) a.style[k(a, c) || c] = b[c];
            return a
        }

        function m(a) {
            for (var b = 1; b < arguments.length; b++) {
                var d = arguments[b];
                for (var e in d) a[e] === c && (a[e] = d[e])
            }
            return a
        }

        function n(a) {
            for (var b = {
                    x: a.offsetLeft,
                    y: a.offsetTop
                }; a = a.offsetParent;) b.x += a.offsetLeft, b.y += a.offsetTop;
            return b
        }
        var f, d = ["webkit", "Moz", "ms", "O"],
            e = {},
            i = function() {
                var a = g("style");
                return h(b.getElementsByTagName("head")[0], a), a.sheet || a.styleSheet
            }(),
            o = {
                lines: 12,
                length: 7,
                width: 5,
                radius: 10,
                rotate: 0,
                color: "#000",
                speed: 1,
                trail: 100,
                opacity: .25,
                fps: 20,
                zIndex: 2e9,
                className: "spinner",
                top: "auto",
                left: "auto"
            },
            p = function q(a) {
                return this.spin ? void(this.opts = m(a || {}, q.defaults, o)) : new q(a)
            };
        p.defaults = {}, m(p.prototype, {
            spin: function(a) {
                this.stop();
                var h, i, b = this,
                    c = b.opts,
                    d = b.el = l(g(0, {
                        className: c.className
                    }), {
                        position: "relative",
                        zIndex: c.zIndex
                    }),
                    e = c.radius + c.length + c.width;
                if (a && (a.insertBefore(d, a.firstChild || null), i = n(a), h = n(d), l(d, {
                        left: ("auto" == c.left ? i.x - h.x + (a.offsetWidth >> 1) : c.left + e) + "px",
                        top: ("auto" == c.top ? i.y - h.y + (a.offsetHeight >> 1) : c.top + e) + "px"
                    })), d.setAttribute("aria-role", "progressbar"), b.lines(d, b.opts), !f) {
                    var j = 0,
                        k = c.fps,
                        m = k / c.speed,
                        o = (1 - c.opacity) / (m * c.trail / 100),
                        p = m / c.lines;
                    ! function q() {
                        j++;
                        for (var a = c.lines; a; a--) {
                            var e = Math.max(1 - (j + a * p) % m * o, c.opacity);
                            b.opacity(d, c.lines - a, e, c)
                        }
                        b.timeout = b.el && setTimeout(q, ~~(1e3 / k))
                    }()
                }
                return b
            },
            stop: function() {
                var a = this.el;
                return a && (clearTimeout(this.timeout), a.parentNode && a.parentNode.removeChild(a), this.el = c), this
            },
            lines: function(a, b) {
                function e(a, d) {
                    return l(g(), {
                        position: "absolute",
                        width: b.length + b.width + "px",
                        height: b.width + "px",
                        background: a,
                        boxShadow: d,
                        transformOrigin: "left",
                        transform: "rotate(" + ~~(360 / b.lines * c + b.rotate) + "deg) translate(" + b.radius + "px,0)",
                        borderRadius: (b.width >> 1) + "px"
                    })
                }
                for (var d, c = 0; c < b.lines; c++) d = l(g(), {
                    position: "absolute",
                    top: 1 + ~(b.width / 2) + "px",
                    transform: b.hwaccel ? "translate3d(0,0,0)" : "",
                    opacity: b.opacity,
                    animation: f && j(b.opacity, b.trail, c, b.lines) + " " + 1 / b.speed + "s linear infinite"
                }), b.shadow && h(d, l(e("#000", "0 0 4px #000"), {
                    top: "2px"
                })), h(a, h(d, e(b.color, "0 0 1px rgba(0,0,0,.1)")));
                return a
            },
            opacity: function(a, b, c) {
                b < a.childNodes.length && (a.childNodes[b].style.opacity = c)
            }
        }), ! function() {
            function a(a, b) {
                return g("<" + a + ' xmlns="urn:schemas-microsoft.com:vml" class="spin-vml">', b)
            }
            var b = l(g("group"), {
                behavior: "url(#default#VML)"
            });
            !k(b, "transform") && b.adj ? (i.addRule(".spin-vml", "behavior:url(#default#VML)"), p.prototype.lines = function(b, c) {
                function f() {
                    return l(a("group", {
                        coordsize: e + " " + e,
                        coordorigin: -d + " " + -d
                    }), {
                        width: e,
                        height: e
                    })
                }

                function k(b, e, g) {
                    h(i, h(l(f(), {
                        rotation: 360 / c.lines * b + "deg",
                        left: ~~e
                    }), h(l(a("roundrect", {
                        arcsize: 1
                    }), {
                        width: d,
                        height: c.width,
                        left: c.radius,
                        top: -c.width >> 1,
                        filter: g
                    }), a("fill", {
                        color: c.color,
                        opacity: c.opacity
                    }), a("stroke", {
                        opacity: 0
                    }))))
                }
                var j, d = c.length + c.width,
                    e = 2 * d,
                    g = 2 * -(c.width + c.length) + "px",
                    i = l(f(), {
                        position: "absolute",
                        top: g,
                        left: g
                    });
                if (c.shadow)
                    for (j = 1; j <= c.lines; j++) k(j, -2, "progid:DXImageTransform.Microsoft.Blur(pixelradius=2,makeshadow=1,shadowopacity=.3)");
                for (j = 1; j <= c.lines; j++) k(j);
                return h(b, i)
            }, p.prototype.opacity = function(a, b, c, d) {
                var e = a.firstChild;
                d = d.shadow && d.lines || 0, e && b + d < e.childNodes.length && (e = e.childNodes[b + d], e = e && e.firstChild, e = e && e.firstChild, e && (e.opacity = c))
            }) : f = k(b, "animation")
        }(), a.Spinner = p
    }(window, document);