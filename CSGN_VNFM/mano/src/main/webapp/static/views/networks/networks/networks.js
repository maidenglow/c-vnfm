
"use strict";
angular.module("app.networks", []).controller("NetworksCtrl", ["$scope", "$modal", "logger", "$q", "$timeout", "NetworkingNetworkAPI", "NetworkingSubnetAPI", "NetworkingPortAPI", function($scope, $modal, logger, $q, $timeout, NetworkingNetworkAPI, NetworkingSubnetAPI, NetworkingPortAPI) {
    var createNetwork, createSubnet, deleteNetwork, deleteSubnet, findIndex, getNetwork, getNetworks, getPort, getPorts, refreshNetwork, updateNetwork, updatePort, updateSubnet;
    $scope.init = function() {
        return $q.all([getNetworks().$promise, getPorts().$promise]).then(function(result) {
            var network, networks, port, ports, _i, _j, _len, _len1;
            for (ports = result[1], networks = $scope.networks, _i = 0, _len = networks.length; _len > _i; _i++)
                for (network = networks[_i], network.ports = [], _j = 0, _len1 = ports.length; _len1 > _j; _j++) port = ports[_j], network.id === port.network_id && network.ports.push(port);
            return networks && networks.length > 0 ? $scope.selectItem(networks[0]) : void 0
        })
    }, getNetworks = function(network) {
        return NetworkingNetworkAPI.query(network, function(response) {
            $scope.networks = response
        })
    }, getNetwork = function(network) {
        return NetworkingNetworkAPI.get(network, function(response) {
            var idx;
            $scope.network = response, idx = findIndex(network), idx > -1 ? (network = response, network.$$hashKey = $scope.networks[idx].$$hashKey, $scope.networks[idx] = network) : $scope.networks.splice(0, 0, response)
        })
    }, createNetwork = function(network) {
//    	network.networkType = 'VLAN';
//    	network.physicalNetwork = 'provider';
//    	network.tenantId = "admin";
//    	network.adminStatUp = true;
//    	network.isShared = true;
//    	network.segmentId = '102'
        return NetworkingNetworkAPI.save(network, function(response) {
            response.success ? (logger.logSuccess("네트워크가 생성되었습니다."), getNetwork({
                id: response.data.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshNetwork(response.data.id)
                }, 1e3)
            })) : logger.logError("네트워크 생성에 실패하였습니다.<br/>" + response.message)
        })
    }, updateNetwork = function(network) {
        return NetworkingNetworkAPI.update(network, function(response) {
            logger.logSuccess("네트워크가 수정되었습니다."), getNetwork({
                id: response.id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshNetwork(response.id)
                }, 1e3)
            })
        })
    }, deleteNetwork = function(network) {
        return NetworkingNetworkAPI["delete"](network, function(response) {
            var idx;
            response.success ? (logger.log("삭제 되었습니다."), idx = findIndex(network), $scope.networks.splice(idx, 1), $scope.networks.length > 0 && $scope.selectItem($scope.networks[0])) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, refreshNetwork = function(id) {
        return NetworkingNetworkAPI.get({
            id: id
        }, function(response) {
            var idx, network;
            "BUILD" === response.status ? $timeout(function() {
                return refreshNetwork(id)
            }, 2500) : (idx = findIndex(response), idx > -1 && (network = response, network.$$hashKey = $scope.networks[idx].$$hashKey, $scope.networks[idx] = network, $scope.selectItem($scope.networks[idx])))
        })
    }, findIndex = function(network) {
        var idx, item, _i, _len, _ref;
        for (_ref = $scope.networks, idx = _i = 0, _len = _ref.length; _len > _i; idx = ++_i)
            if (item = _ref[idx], item.id === network.id) return idx;
        return -1
    }, createSubnet = function(subnet) {
        return NetworkingSubnetAPI.save(subnet, function(response) {
            response.success ? (logger.logSuccess("서브넷이 생성되었습니다."), getNetwork({
                id: response.data.network_id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshNetwork(response.data.network_id)
                }, 1e3)
            })) : logger.logError("서브넷 생성에 실패하였습니다.<br/>" + response.message)
        })
    }, updateSubnet = function(subnet) {
        return NetworkingSubnetAPI.update(subnet, function(response) {
            response.NeutronError ? logger.logError("서브넷 정보 수정에 실패하였습니다.<br/>" + response.NeutronError) : (logger.logSuccess("서브넷 정보가 수정되었습니다."), getNetwork({
                id: response.subnet.network_id
            }).$promise.then(function() {
                return $timeout(function() {
                    return refreshNetwork(response.subnet.network_id)
                }, 1e3)
            }))
        })
    }, deleteSubnet = function(subnet) {
        return NetworkingSubnetAPI["delete"](subnet, function(response) {
            response.success ? (logger.log("삭제 되었습니다."), getNetwork({
                id: subnet.network_id
            })) : logger.logError("삭제를 실패하였습니다. " + response.fault)
        })
    }, getPorts = function() {
        return NetworkingPortAPI.query({}, function(response) {
            return response
        })
    }, getPort = function(port) {
        return NetworkingPortAPI.get(port, function(response) {
            return response
        })
    }, updatePort = function(port) {
        return NetworkingPortAPI.update(port, function(response) {
            var idx, iport, seq, _i, _len, _ref;
            if (logger.logSuccess("포트정보가 수정되었습니다."), port = response, idx = findIndex({
                    id: port.network_id
                }), idx > -1)
                for (_ref = $scope.networks[idx].ports, seq = _i = 0, _len = _ref.length; _len > _i; seq = ++_i) iport = _ref[seq], port.id === iport.id && ($scope.networks[idx].ports[seq] = port)
        })
    }, $scope.networks, $scope.itemStatusClass = function(network) {
        var clazz;
        return clazz = "ACTIVE" === network.status ? "label label-success" : "label label-danger"
    }, $scope.network, $scope.selectItem = function(network) {
        return $scope.network = network
    }, $scope.isSelected = function(network) {
        return $scope.network === network
    }, $scope.createNetworkForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/create.html",
            controller: "CreateNetworkModalCtrl"
        }), modalInstance.result.then(function(network) {
            createNetwork(network)
        }, function() {})
    }, $scope.editNetworkForm = function(network) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/edit_network.html",
            controller: "EditNetworkModalCtrl",
            resolve: {
                network: function() {
                    return network
                }
            }
        }), modalInstance.result.then(function(network) {
            updateNetwork(network)
        }, function() {})
    }, $scope.deleteNetworkForm = function(network) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/confirm.html",
            controller: function($scope, $modalInstance, network) {
                $scope.ok = function() {
                    return $modalInstance.close(network)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                network: function() {
                    return network
                }
            }
        }), modalInstance.result.then(function(network) {
            deleteNetwork(network)
        }, function() {})
    }, $scope.createSubnetForm = function(network) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/create_subnet.html",
            controller: "CreateSubnetModalCtrl",
            resolve: {
                network: function() {
                    return network
                }
            }
        }), modalInstance.result.then(function(subnet) {
            createSubnet(subnet)
        }, function() {})
    }, $scope.editSubnetForm = function(subnet) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/edit_subnet.html",
            controller: "EditSubnetModalCtrl",
            resolve: {
                subnet: function() {
                    return subnet
                }
            }
        }), modalInstance.result.then(function(subnet) {
            updateSubnet(subnet)
        }, function() {})
    }, $scope.deleteSubnetForm = function(subnet) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/confirm.html",
            controller: function($scope, $modalInstance, subnet) {
                var subnet_name;
                subnet_name = subnet.name ? subnet.name : subnet.id.substring(0, 8), $scope.msg = "선택한 서브넷 (" + subnet_name + ")을", $scope.ok = function() {
                    return $modalInstance.close(subnet)
                }, $scope.cancel = function() {
                    return $modalInstance.dismiss("Cancel")
                }
            },
            resolve: {
                subnet: function() {
                    return subnet
                }
            }
        }), modalInstance.result.then(function(subnet) {
            deleteSubnet(subnet)
        }, function() {})
    }, $scope.editPortForm = function(port) {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/edit_port.html",
            controller: "EditPortModalCtrl",
            resolve: {
                port: function() {
                    return port
                }
            }
        }), modalInstance.result.then(function(port) {
            updatePort(port)
        }, function() {})
    }
}]).controller("CreateNetworkModalCtrl", ["$scope", "$modalInstance", "$q", function($scope, $modalInstance, $q) {
    var original;
    $scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/, $scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/,
        $scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.stage = "step1", $scope.network = {
            name: null,
            admin_state_up: !0,
            create_subnet: !0,
            enable_dhcp: !0,
            allocation_pools_text: "",
            dns_nameservers_text: "",
            host_routes_text: ""
        }, original = angular.copy($scope.network), $scope.revert = function() {
            $scope.network = angular.copy(original), $scope.form.$setPristine()
        }, $scope.canRevert = function() {
            return !angular.equals($scope.network, original) || !$scope.form.$pristine
        }, $scope.canSubmit = function() {
            return "step2" !== $scope.stage ? !0 : $scope.form.$valid && !angular.equals($scope.network, original)
        }, $scope.submit = function(network) {
            var dns_nameserver, host_route, item, pool, _i, _j, _k, _len, _len1, _len2, _ref, _ref1, _ref2;
            if ("step1" === $scope.stage) return void($scope.stage = "step2");
            if ("step2" === $scope.stage) return void($scope.form.$valid && ($scope.stage = "step3"));
            if (network.create_subnet) {
                for (network.neutronSubnets = [], network.neutronSubnets[0] = {
                        cidr: network.cidr,
                        allocation_pools: [],
                        dns_nameservers: [],
                        enable_dhcp: network.enable_dhcp,
                        gateway_ip: network.gateway_ip,
                        host_routes: [],
                        ip_version: network.ip_version,
                        name: network.subnet_name
                    }, network.no_gateway && (network.neutronSubnets[0].gateway_ip = null), _ref = network.allocation_pools_text.split("\n"), _i = 0, _len = _ref.length; _len > _i; _i++) pool = _ref[_i], pool && (item = pool.split(","), network.neutronSubnets[0].allocation_pools.push({
                    start: $.trim(item[0]),
                    end: $.trim(item[1])
                }));
                for (_ref1 = network.dns_nameservers_text.split("\n"), _j = 0, _len1 = _ref1.length; _len1 > _j; _j++) dns_nameserver = _ref1[_j], dns_nameserver && network.neutronSubnets[0].dns_nameservers.push(dns_nameserver);
                for (_ref2 = network.host_routes_text.split("\n"), _k = 0, _len2 = _ref2.length; _len2 > _k; _k++) host_route = _ref2[_k], host_route && (item = host_route.split(","), network.neutronSubnets[0].host_routes.push({
                    destination: $.trim(item[0]),
                    nexthop: $.trim(item[1])
                }))
            }
            $modalInstance.close(network), $scope.revert()
        }, $scope.cancel = function() {
            $modalInstance.dismiss("cancel")
        }, $scope.init = function() {
            return $q.all([]).then(function() {
                return $scope.network.ip_version = "4", $scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
            })
        }, $scope.init(), $scope.changeIpVersion = function() {
            "4" === $scope.network.ip_version ? $scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/ : "6" === $scope.network.ip_version && ($scope.cidr_pattern = /^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*(\/(d|dd|1[0-1]d|12[0-8]))$/)
        }, $scope.setStage = function(stage) {
            $scope.stage = "step3" === $scope.stage ? stage : "step3" === stage ? $scope.form.$valid ? stage : "step2" : stage
        }, $scope.next = function() {
            "step1" === $scope.stage ? $scope.stage = "step2" : "step2" === $scope.stage && $scope.form.$valid && ($scope.stage = "step3")
        }
}]).controller("EditNetworkModalCtrl", ["$scope", "$modalInstance", "$q", "network", function($scope, $modalInstance, $q, network) {
    var original;
    $scope.network = angular.copy(network), original = angular.copy($scope.network), $scope.revert = function() {
        $scope.network = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.network, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.network, original)
    }, $scope.submit = function(network) {
        $modalInstance.close(network), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]).controller("CreateSubnetModalCtrl", ["$scope", "$modalInstance", "$q", "network", function($scope, $modalInstance, $q, network) {
    var original;
    $scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/, $scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.stage = "step1", $scope.subnet = {
        name: null,
        network_id: network.id,
        enable_dhcp: !0,
        allocation_pools: [],
        dns_nameservers: [],
        host_routes: [],
        allocation_pools_text: "",
        dns_nameservers_text: "",
        host_routes_text: ""
    }, original = angular.copy($scope.subnet), $scope.revert = function() {
        $scope.network = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.network, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return "step1" !== $scope.stage ? !0 : $scope.form.$valid && !angular.equals($scope.network, original)
    }, $scope.submit = function(subnet) {
        var dns_nameserver, host_route, item, pool, _i, _j, _k, _len, _len1, _len2, _ref, _ref1, _ref2;
        if ("step1" === $scope.stage) return void($scope.form.$valid && ($scope.stage = "step2"));
        for (subnet.allocation_pools = [], subnet.dns_nameservers = [], subnet.host_routes = [], subnet.no_gateway && (subnet.gateway_ip = null), _ref = subnet.allocation_pools_text.split("\n"), _i = 0, _len = _ref.length; _len > _i; _i++) pool = _ref[_i], pool && (item = pool.split(","), subnet.allocation_pools.push({
            start: $.trim(item[0]),
            end: $.trim(item[1])
        }));
        for (_ref1 = subnet.dns_nameservers_text.split("\n"), _j = 0, _len1 = _ref1.length; _len1 > _j; _j++) dns_nameserver = _ref1[_j], dns_nameserver && subnet.dns_nameservers.push(dns_nameserver);
        for (_ref2 = subnet.host_routes_text.split("\n"), _k = 0, _len2 = _ref2.length; _len2 > _k; _k++) host_route = _ref2[_k], host_route && (item = host_route.split(","), subnet.host_routes.push({
            destination: $.trim(item[0]),
            nexthop: $.trim(item[1])
        }));
        $modalInstance.close(subnet), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {
            return $scope.subnet.ip_version = "4", $scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/
        })
    }, $scope.init(), $scope.changeIpVersion = function() {
        "4" === $scope.subnet.ip_version ? $scope.cidr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))$/ : "6" === $scope.subnet.ip_version && ($scope.cidr_pattern = /^s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]d|1dd|[1-9]?d)(.(25[0-5]|2[0-4]d|1dd|[1-9]?d)){3}))|:)))(%.+)?s*(\/(d|dd|1[0-1]d|12[0-8]))$/)
    }, $scope.setStage = function(stage) {
        $scope.stage = "step2" === $scope.stage ? stage : "step2" === stage ? $scope.form.$valid ? stage : "step1" : stage
    }, $scope.next = function() {
        "step1" === $scope.stage && $scope.form.$valid && ($scope.stage = "step3")
    }
}]).controller("EditSubnetModalCtrl", ["$scope", "$modalInstance", "$q", "subnet", function($scope, $modalInstance, $q, subnet) {
    var host_route, host_route_array, original, _i, _len, _ref;
    for ($scope.ipaddr_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/, $scope.pool_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.dns_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.router_pattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\n(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\/([0-9]|[1-2][0-9]|3[0-2]))\,\s*(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))*$/, $scope.stage = "step1", $scope.subnet = angular.copy(subnet), $scope.subnet.allocation_pools = null, $scope.subnet.no_gateway = subnet.gateway_ip ? !1 : !0, $scope.subnet.dns_nameservers_text = $scope.subnet.dns_nameservers.join("\n"), host_route_array = [], _ref = $scope.subnet.host_routes, _i = 0, _len = _ref.length; _len > _i; _i++) host_route = _ref[_i], host_route_array.push(host_route.destination + ", " + host_route.nexthop);
    $scope.subnet.host_routes_text = host_route_array.join("\n"), original = angular.copy($scope.subnet), $scope.revert = function() {
        $scope.network = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.network, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return "step1" !== $scope.stage ? !0 : $scope.form.$valid && !angular.equals($scope.network, original)
    }, $scope.submit = function(subnet) {
        var dns_nameserver, item, _j, _k, _len1, _len2, _ref1, _ref2;
        if ("step1" === $scope.stage) return void($scope.form.$valid && ($scope.stage = "step2"));
        for (subnet.dns_nameservers = [], subnet.host_routes = [], subnet.no_gateway && (subnet.gateway_ip = null), _ref1 = subnet.dns_nameservers_text.split("\n"), _j = 0, _len1 = _ref1.length; _len1 > _j; _j++) dns_nameserver = _ref1[_j], dns_nameserver && subnet.dns_nameservers.push(dns_nameserver);
        for (_ref2 = subnet.host_routes_text.split("\n"), _k = 0, _len2 = _ref2.length; _len2 > _k; _k++) host_route = _ref2[_k], host_route && (item = host_route.split(","), subnet.host_routes.push({
            destination: $.trim(item[0]),
            nexthop: $.trim(item[1])
        }));
        $modalInstance.close(subnet), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init(), $scope.setStage = function(stage) {
        $scope.stage = "step2" === $scope.stage ? stage : "step2" === stage ? $scope.form.$valid ? stage : "step1" : stage
    }, $scope.next = function() {
        "step1" === $scope.stage && $scope.form.$valid && ($scope.stage = "step3")
    }
}]).controller("EditPortModalCtrl", ["$scope", "$modalInstance", "$q", "port", function($scope, $modalInstance, $q, port) {
    var original;
    $scope.port = angular.copy(port), original = angular.copy($scope.port), $scope.revert = function() {
        $scope.network = angular.copy(original), $scope.form.$setPristine()
    }, $scope.canRevert = function() {
        return !angular.equals($scope.network, original) || !$scope.form.$pristine
    }, $scope.canSubmit = function() {
        return $scope.form.$valid && !angular.equals($scope.network, original)
    }, $scope.submit = function(port) {
        $modalInstance.close(port), $scope.revert()
    }, $scope.cancel = function() {
        $modalInstance.dismiss("cancel")
    }, $scope.init = function() {
        return $q.all([]).then(function() {})
    }, $scope.init()
}]);
