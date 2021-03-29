
"use strict";
angular.module("app.routers", []).controller("RoutersCtrl", ["$scope", "$modal", "logger", "$q", "$timeout", "NetworkingRouterAPI", "NetworkingNetworkAPI", "NetworkingRouterGatewayAPI", "NetworkingRouterInterfaceAPI", "TenantsAPI", "NetworkingPortAPI", function($scope, $modal, logger, $q, $timeout, NetworkingRouterAPI, NetworkingNetworkAPI, NetworkingRouterGatewayAPI, NetworkingRouterInterfaceAPI, TenantsAPI, NetworkingPortAPI) {
    var addGatewayRouter, addRouterInterface, createRouter, deleteGatewayRouter, deleteRouter, deleteRouterInterface, findIndex, getPort, getPorts, getRouter, getRouters, lookupNetworkName, lookupPortInfo, lookupProjectName, networks, ports, projects, refreshPortInfo, refreshRouter, updateRouter;
    networks = [], projects = [], ports = [], $scope.init = function() {
        return $q.all([getRouters().$promise, NetworkingNetworkAPI.query().$promise, TenantsAPI.get().$promise, getPorts().$promise]).then(function(result) {
            var router, routers, _i, _len;
            for (networks = result[1], projects = result[2].tenants, ports = result[3], routers = $scope.routers, _i = 0, _len = routers.length; _len > _i; _i++) router = routers[_i], lookupNetworkName(router), lookupProjectName(router), lookupPortInfo(router);
            return routers && routers.length > 0 ? $scope.selectItem(routers[0]) : void 0
        })
    }, lookupProjectName = function(router) {
        var project, _i, _len;
        for (_i = 0, _len = projects.length; _len > _i; _i++) project = projects[_i], project.id === router.tenant_id && (router.project_name = project.name)
    }, lookupNetworkName = function(router) {
        var network, _i, _len;
        if (router.external_gateway_info)
            for (_i = 0, _len = networks.length; _len > _i; _i++) network = networks[_i], network.id === router.external_gateway_info.network_id && (router.external_gateway_info.network_name = network.name)
    }, lookupPortInfo = function(router, flush) {
        var port, _i, _len;
        for (null == flush && (flush = !1), ("undefined" == typeof router.ports || flush) && (router.ports = []), _i = 0, _len = ports.length; _len > _i; _i++) port = ports[_i], router.id === port.device_id && router.ports.push(port)
    }, refreshPortInfo = function() {
        getPorts().$promise.then(function(response) {
            var router, routers, _i, _len, _results;
            for (ports = response, routers = $scope.routers, _results = [], _i = 0, _len = routers.length; _len > _i; _i++) router = routers[_i], _results.push(lookupPortInfo(router, !0));
            return _results
        })
    }, getRouters = function(router) {
        return NetworkingRouterAPI.query(router, function(response) {
            $scope.routers = response
        })
    }, getRouter = function(router) {
        return NetworkingRouterAPI.get(router, function(response) {
            var idx, newRouter;
            $scope.router = response, idx = findIndex(router), idx > -1 ? (newRouter = response, newRouter.$$hashKey = $scope.routers[idx].$$hashKey, lookupNetworkName(newRouter), $scope.routers[idx] = newRouter, $scope.selectItem(newRouter)) : $scope.routers.splice(0, 0, response)
        })
    }, createRouter = function(router) {
        return NetworkingRouterAPI.save(router, function(response) {
            logger.logSuccess("라우터가 생성되었습니다."), getRouter({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshRouter(response.id)
                }, 1e3)
            })
        })
    }, updateRouter = function(router) {
        return NetworkingRouterAPI.update(router, function(response) {
            logger.logSuccess("게이트웨이정보가 설정되었습니다."), getRouter({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshRouter(response.id)
                }, 1e3)
            })
        })
    }, deleteRouter = function(router) {
        return NetworkingRouterAPI["delete"](router, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(router), $scope.routers.splice(idx, 1), $scope.routers.length > 0 && $scope.selectItem($scope.routers[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, refreshRouter = function(id) {
        return NetworkingRouterAPI.get({
            id: id
        }, function(response) {
            var idx, router;
            "ACTIVE" !== response.status ? $timeout(function() {
                return refreshRouter(id)
            }, 2500) : (idx = findIndex(response), router = response, router.$$hashKey = $scope.routers[idx].$$hashKey, idx > -1 && ($scope.routers[idx] = router), $scope.selectItem($scope.routers[idx]))
        })
    }, addGatewayRouter = function(router) {
        return NetworkingRouterGatewayAPI.save(router, function(response) {
            var routerId;
            routerId = response.router.id, logger.logSuccess("게이트웨이정보가 설정되었습니다."), getRouter({
                id: routerId
            }).$promise.then(function() {
                return refreshPortInfo(), lookupNetworkName($scope.router), lookupProjectName($scope.router)
            })
        })
    }, deleteGatewayRouter = function(router) {
        return NetworkingRouterGatewayAPI["delete"](router, function(response) {
            var routerId;
            routerId = response.router.id, logger.logSuccess("게이트웨이정보가 삭제되었습니다."), getRouter({
                id: routerId
            }).$promise.then(function() {
                return refreshPortInfo(), lookupNetworkName($scope.router), lookupProjectName($scope.router)
            })
        })
    }, addRouterInterface = function(router_interface) {
        return NetworkingRouterInterfaceAPI.save(router_interface, function(response) {
            response.success ? (logger.logSuccess("인터페이스가 추가 되었습니다."), refreshPortInfo()) : logger.logError("인터페이스 생성시 오류가 발생하였습니다. <br/>" + response.message)
        })
    }, deleteRouterInterface = function(router, port) {
        var param;
        return param = angular.copy(port), param.router_id = router.id, NetworkingRouterInterfaceAPI["delete"](param, function(response) {
            response.success ? (logger.logSuccess("인터페이스가 삭제되었습니다."), refreshPortInfo()) : logger.logError("인터페이스 삭제시 오류가 발생하였습니다. <br/>" + response.message)
        })
    }, findIndex = function(router) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.routers, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === router.id) return idx;
        return -1
    }, getPorts = function() {
        return NetworkingPortAPI.query({}, function(response) {
            return response
        })
    }, getPort = function(port) {
        return NetworkingPortAPI.get(port, function(response) {
            return response
        })
    }, $scope.routers, $scope.itemStatusClass = function(router) {
        var clazz;
        return clazz = "ACTIVE" === router.status ? "label label-success" : "label label-danger"
    }, $scope.router, $scope.selectItem = function(router) {
        return $scope.router = router
    }, $scope.isSelected = function(router) {
        return $scope.router === router
    }, $scope.createRouterForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/create.html",
            controller: "CreateRouterModalCtrl"
        }), modalInstance.result.then(function(router) {
            createRouter(router)
        }, function() {})
    }, $scope.manageGatewayForm = function(router) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/manage_gateway.html",
            controller: "ManageGatewayModalCtrl",
            resolve: {
                router: function() {
                    return router
                }
            }
        }), modalInstance.result.then(function(router) {
            addGatewayRouter(router)
        }, function() {})
    }, $scope.deleteRouterForm = function(router) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/confirm.html",
            controller: function($scope, $modalInstance, router) {
                $scope.ok = function() {
                    return $modalInstance.close(router)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                router: function() {
                    return router
                }
            }
        }), modalInstance.result.then(function(router) {
            deleteRouter(router)
        }, function() {})
    }, $scope.deleteGatewayForm = function(router) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/confirm.html",
            controller: function($scope, $modalInstance, router) {
                $scope.ok = function() {
                    return $modalInstance.close(router)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                router: function() {
                    return router
                }
            }
        }), modalInstance.result.then(function(router) {
            router.external_gateway_info = {}, deleteGatewayRouter(router)
        }, function() {})
    }, $scope.createIntefaceForm = function(router) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/create_interface.html",
            controller: "CreateInterfaceModalCtrl",
            resolve: {
                router: function() {
                    return router
                }
            }
        }), modalInstance.result.then(function(router_interface) {
            addRouterInterface(router_interface)
        }, function() {})
    }, $scope.deleteRouterInterfaceForm = function(router, port) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/confirm.html",
            controller: function($scope, $modalInstance, port) {
                var port_name;
                port_name = port.name ? port.name : port.id.substring(0, 8), $scope.msg = "선택한 인터페이스 (" + port_name + ")을", $scope.ok = function() {
                    return $modalInstance.close(port)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                port: function() {
                    return port
                }
            }
        }), modalInstance.result.then(function(port) {
            deleteRouterInterface(router, port)
        }, function() {})
    }
}]).controller("CreateRouterModalCtrl", ["$scope", "$modalInstance", "$q", function($scope, $modalInstance, $q) {
    var original;
    $scope.router = {
        name: null
    }, original = angular.copy($scope.router), $scope.revert = function() {
        $scope.router = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.router, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.router, original)
    }, $scope.submit = function(router) {
        $modalInstance.close(router), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]).controller("ManageGatewayModalCtrl", ["$scope", "$modalInstance", "$q", "router", "NetworkingNetworkAPI", function($scope, $modalInstance, $q, router, NetworkingNetworkAPI) {
    var original;
    $scope.router = angular.copy(router), original = angular.copy($scope.router), $scope.revert = function() {
        $scope.router = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.router, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.router, original)
    }, $scope.submit = function(router) {
        router.external_gateway_info = {
            network_id: router.select_external_gateway_info.id
        }, $modalInstance.close(router), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([NetworkingNetworkAPI.query().$promise]).then(function(result) {
            return $scope.networks = result[0]
        })
    }, $scope.init()
}]).controller("CreateInterfaceModalCtrl", ["$scope", "$modalInstance", "$q", "router", "NetworkingSubnetAPI", function($scope, $modalInstance, $q, router, NetworkingSubnetAPI) {
    var original;
    $scope.router_interface = {
        router_id: router.id,
        router_name: router.name,
        ip_address: ""
    }, $scope.subnets = [], original = angular.copy($scope.router_interface), $scope.revert = function() {
        $scope.router = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.router_interface, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.router_interface, original)
    }, $scope.submit = function(router_interface) {
        $modalInstance.close(router_interface), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([NetworkingSubnetAPI.query().$promise]).then(function(result) {
            return $scope.subnets = result[0]
        })
    }, $scope.init()
}]);
