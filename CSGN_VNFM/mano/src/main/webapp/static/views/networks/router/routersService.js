
"use strict";
angular.module("app.topology", []).factory("horizon_topology", ["logger", "ComputeServersAPI", "NetworkingRouterAPI", "NetworkingRouterInterfaceAPI", function(logger, ComputeServersAPI, NetworkingRouterAPI, NetworkingRouterInterfaceAPI) {
    return {
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
            var self;
            self = this, $(self.svg_container).spin({
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
                var vnc_window;
                e.preventDefault(), vnc_window = window.open($(this).attr("href"), vnc_window, "width=760,height=560"), self.delete_balloon()
            }).click(function() {
                self.delete_balloon()
            }), $(".toggleView > .btn").click(function() {
                self.draw_mode = $(this).data("value"), $("g.network").remove(), $.cookie("ntp_draw_mode", self.draw_mode), self.data_convert()
            }), $(window).on("message", function(e) {
                var message;
                message = $.parseJSON(e.originalEvent.data), self.previous_message !== message.message && (horizon.alert(message.type, message.message), horizon.autoDismissAlerts(), self.previous_message = message.message, self.delete_post_message(message.iframe_id), self.load_network_info(), setTimeout(function() {
                    self.previous_message = null
                }, 1e4))
            }), self.load_network_info())
        },
        load_network_info2: function() {
            var self;
            self = this, 0 !== $("#networktopology").length && $.getJSON(self.networktopology_url + "?" + $.now(), function(data) {
                self.model = data, self.data_convert()
            })
        },
        load_network_info: function() {
            var self;
            self = this, 0 !== $("#networktopology").length && $.getJSON(self.networktopology_url + "?" + $.now(), function(data) {
                self.model = data, self.data_convert(), setTimeout(function() {
                    self.load_network_info()
                }, self.reload_duration)
            })
        },
        select_draw_mode: function() {
            var draw_mode, self;
            self = this, draw_mode = $.cookie("ntp_draw_mode"), !draw_mode || "normal" !== draw_mode && "small" !== draw_mode ? (self.draw_mode = self.model.networks.length * self.element_properties.normal.network_width > $("#topologyCanvas").width() ? "small" : "normal", $.cookie("ntp_draw_mode", self.draw_mode)) : self.draw_mode = draw_mode, $(".toggleView > .btn").each(function() {
                var $this;
                $this = $(this), $this.hasClass(self.draw_mode) ? $this.addClass("active") : $this.removeClass("active")
            })
        },
        data_convert: function() {
            var element_properties, model, self;
            self = this, model = self.model, $.each(model.networks, function(index, network) {
                self.network_index[network.id] = index, $.each(model.subnets, function(index2, subnet) {
                    var tmpSubnets;
                    network.id === subnet.network_id && (tmpSubnets = network.subnets, network.subnets = [], $.each(tmpSubnets, function(index3, subnet_id) {
                        (subnet_id === subnet.id || subnet_id.cidr === subnet.cidr) && network.subnets.push({
                            cidr: subnet.cidr
                        })
                    }))
                })
            }), self.select_draw_mode(), element_properties = self.element_properties[self.draw_mode], self.network_height = element_properties.top_margin, $.each([{
                model: model.routers,
                type: "router"
            }, {
                model: model.servers,
                type: "instance"
            }], function(index, devices) {
                var model, type;
                type = devices.type, model = devices.model, $.each(model, function(index, device) {
                    var hasports, height;
                    device.type = type, device.ports = self.select_port(device.id), hasports = device.ports.length <= 0 ? !1 : !0, device.parent_network = hasports ? self.select_main_port(device.ports).network_id : self.model.networks[0].id, height = element_properties.port_margin * (device.ports.length - 1), device.height = "normal" === self.draw_mode && height > element_properties.default_height ? height : element_properties.default_height, device.pos_y = self.network_height, device.port_height = "small" === self.draw_mode && height > device.height ? 1 : element_properties.port_height, device.port_margin = "small" === self.draw_mode && height > device.height ? device.height / device.ports.length : element_properties.port_margin, self.network_height += device.height + element_properties.margin
                })
            }), $.each(model.networks, function(index, network) {
                network.devices = [], $.each([model.routers, model.servers], function(index, devices) {
                    $.each(devices, function(index, device) {
                        network.id === device.parent_network && network.devices.push(device);

                    })
                })
            }), self.network_height += element_properties.top_margin, self.network_height = self.network_height > element_properties.network_min_height ? self.network_height : element_properties.network_min_height, self.draw_topology()
        },
        draw_topology: function() {
            var device, device_enter, element_properties, network, network_enter, port, port_enter, self, svg;
            return self = this, $(self.svg_container).spin(!1), $(self.svg_container).removeClass("noinfo"), self.model.networks.length <= 0 ? ($("g.network").remove(), void $(self.svg_container).addClass("noinfo")) : (svg = d3.select(self.svg), element_properties = self.element_properties[self.draw_mode], svg.attr("width", self.model.networks.length * element_properties.network_width).attr("height", self.network_height), network = svg.selectAll("g.network").data(self.model.networks), network_enter = network.enter().append("g").attr("class", "network").each(function(d) {
                var $this;
                this.appendChild(d3.select(self.network_tmpl[self.draw_mode]).node().cloneNode(!0)), $this = d3.select(this).select(".network-rect"), d.url ? $this.on("mouseover", function() {
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
            }), network.attr("id", function(d) {
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
                var cidr;
                return "undefined" == typeof d.subnets && (d.subnets = []), cidr = $.map(d.subnets, function(n) {
                    return n.cidr
                }), cidr.join(", ")
            }), network.exit().remove(), device = network.selectAll("g.device").data(function(d) {
                return d.devices
            }), device_enter = device.enter().append("g").attr("class", "device").each(function(d) {
                var device_template;
                device_template = self[d.type + "_tmpl"][self.draw_mode], this.appendChild(d3.select(device_template).node().cloneNode(!0))
            }), device_enter.on("mouseenter", function(d) {
                var $this;
                $this = $(this), self.show_balloon(d, $this)
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
                var $this;
                "BUILD" === d.status ? d3.select(this).classed("loading", !0) : "deleting" === d.task ? (d3.select(this).classed("loading", !0), "bl_" + d.id === self.balloon_id && self.delete_balloon()) : (d3.select(this).classed("loading", !1), "bl_" + d.id === self.balloon_id && ($this = $(this), self.show_balloon(d, $this)))
            }), device.exit().each(function(d) {
                "bl_" + d.id === self.balloon_id && self.delete_balloon()
            }).remove(), port = device.select("g.ports").selectAll("g.port").data(function(d) {
                return d.ports
            }), port_enter = port.enter().append("g").attr("class", "port").attr("id", function(d) {
                return "id_" + d.id
            }), port_enter.append("line").attr("class", "port_line"), port_enter.append("text").attr("class", "port_text"), device.select("g.ports").each(function(d) {
                this._portdata = {}, this._portdata.ports_length = d.ports.length, this._portdata.parent_network = d.parent_network, this._portdata.device_height = d.height, this._portdata.port_height = d.port_height, this._portdata.port_margin = d.port_margin, this._portdata.left = 0, this._portdata.right = 0, $(this).mouseenter(function(e) {
                    e.stopPropagation()
                })
            }), port.each(function(d) {
                var index_diff;
                index_diff = self.get_network_index(this.parentNode._portdata.parent_network) - self.get_network_index(d.network_id), this._index_diff = index_diff = index_diff >= 0 ? ++index_diff : index_diff, this._direction = this._index_diff < 0 ? "right" : "left", this._index = this.parentNode._portdata[this._direction]++
            }), port.attr("transform", function() {
                var distance, ports_length, x, y;
                return x = "left" === this._direction ? 0 : element_properties.device_width, ports_length = this.parentNode._portdata[this._direction], distance = this.parentNode._portdata.port_margin, y = (this.parentNode._portdata.device_height - (ports_length - 1) * distance) / 2 + this._index * distance, "translate(" + x + "," + y + ")"
            }), port.select(".port_line").attr("stroke-width", function() {
                return this.parentNode.parentNode._portdata.port_height
            }).attr("stroke", function(d) {
                return self.get_network_color(d.network_id)
            }).attr("x1", 0).attr("y1", 0).attr("y2", 0).attr("x2", function() {
                var parent, width;
                return parent = this.parentNode, width = (Math.abs(parent._index_diff) - 1) * element_properties.network_width + element_properties.port_width, "left" === parent._direction ? -1 * width : width
            }), port.select(".port_text").attr("x", function() {
                var parent;
                return parent = this.parentNode, "left" === parent._direction ? (d3.select(this).classed("left", !0), -1 * element_properties.port_text_margin.x) : (d3.select(this).classed("left", !1), element_properties.port_text_margin.x)
            }).attr("y", function() {
                return element_properties.port_text_margin.y
            }).text(function(d) {
                var ip_label;
                return ip_label = [], $.each(d.fixed_ips, function() {
                    ip_label.push(this.ip_address)
                }), ip_label.join(",")
            }), void port.exit().remove())
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
            var MAX_INT, main_port_index, min_port_length, _self;
            return _self = this, main_port_index = 0, MAX_INT = 4294967295, min_port_length = MAX_INT, $.each(ports, function(index, port) {
                var port_length;
                port_length = _self.sum_port_length(port.network_id, ports), min_port_length > port_length && (min_port_length = port_length, main_port_index = index)
            }), ports[main_port_index]
        },
        sum_port_length: function(network_id, ports) {
            var base_index, self, sum_port_length;
            return self = this, sum_port_length = 0, base_index = self.get_network_index(network_id), $.each(ports, function(index, port) {
                sum_port_length += base_index - self.get_network_index(port.network_id)
            }), sum_port_length
        },
        string_truncate: function(string) {
            var bytes, i, max_size, self, str, suffix;
            for (self = this, str = string, max_size = self.element_properties.device_name_max_size, suffix = self.element_properties.device_name_suffix, bytes = 0, i = 0; i < str.length;) {
                if (bytes += str.charCodeAt(i) <= 255 ? 1 : 2, bytes > max_size) {
                    str = str.substr(0, i) + suffix;
                    break
                }
                i++
            }
            return str
        },
        delete_device: function(type, device_id) {
            var router, self, server, _i, _j, _len, _len1, _ref, _ref1;
            if (self = this, "instance" === type) {
                if (self.model && self.model.servers)
                    for (_ref = self.model.servers, _i = 0, _len = _ref.length; _len > _i; _i++)
                        if (server = _ref[_i], device_id === server.id) {
                            ComputeServersAPI["delete"](server, function(response) {
                                response.success ? logger.log("삭제 되었습니다.") : logger.logError("삭제를 실패하였습니다. " + response.fault)
                            });
                            break
                        }
            } else if ("router" === type && self.model && self.model.routers)
                for (_ref1 = self.model.routers, _j = 0, _len1 = _ref1.length; _len1 > _j; _j++)
                    if (router = _ref1[_j], device_id === router.id) {
                        NetworkingRouterAPI["delete"](router, function(response) {
                            response.success ? logger.log("삭제 되었습니다.") : logger.logError("삭제를 실패하였습니다. " + response.fault)
                        });
                        break
                    }
        },
        delete_port: function(router_id, port_id) {
            var param, port, self, _i, _len, _ref;
            if (self = this, self.model && self.model.ports)
                for (_ref = self.model.ports, _i = 0, _len = _ref.length; _len > _i; _i++)
                    if (port = _ref[_i], port_id === port.id) {
                        param = angular.copy(port), param.router_id = router_id, NetworkingRouterInterfaceAPI["delete"](param, function(response) {
                            return response.success ? logger.logSuccess("인터페이스가 삭제되었습니다.") : logger.logError("인터페이스 삭제시 오류가 발생하였습니다. <br/>" + response.message)
                        });
                        break
                    }
        },
        show_balloon: function(d, element) {
            var $balloon, balloon_id, balloon_tmpl, device_position, device_tmpl, element_properties, html, html_data, port_tmpl, ports, self, x, y;
            if (self = this, element_properties = self.element_properties[self.draw_mode], self.balloon_id && self.delete_balloon(), balloon_tmpl = self.balloon_tmpl, device_tmpl = self.balloon_device_tmpl, port_tmpl = self.balloon_port_tmpl, balloon_id = "bl_" + d.id, ports = [], $.each(d.ports, function(i, port) {
                    var device_owner, e, ip_address, object;
                    object = {}, object.id = port.id, object.router_id = port.device_id, object.url = port.url, object.port_status = port.status, object.port_status_css = "ACTIVE" === port.status ? "active" : "down", ip_address = "";
                    try {
                        ip_address = port.fixed_ips[0].ip_address
                    } catch (_error) {
                        e = _error, ip_address = self.gettext("None")
                    }
                    device_owner = "";
                    try {
                        device_owner = port.device_owner.replace("network:", "")
                    } catch (_error) {
                        e = _error, device_owner = self.gettext("None")
                    }
                    object.ip_address = ip_address, object.device_owner = device_owner, object.is_interface = "router_interface" === device_owner, ports.push(object)
                }), html_data = {
                    balloon_id: balloon_id,
                    id: d.id,
                    url: d.url,
                    name: d.name,
                    type: d.type,
                    delete_label: self.gettext("Delete"),
                    status: d.status,
                    status_class: "ACTIVE" === d.status ? "active" : "down",
                    status_label: self.gettext("STATUS"),
                    id_label: self.gettext("ID"),
                    interfaces_label: self.gettext("Interfaces"),
                    delete_interface_label: self.gettext("Delete Interface"),
                    open_console_label: self.gettext("Open Console"),
                    view_details_label: self.gettext("View Details")
                }, "router" === d.type) html_data.delete_label = self.gettext("Delete Router"), html_data.view_details_label = self.gettext("View Router Details"), html_data.port = ports, html_data.add_interface_url = d.url + "addinterface", html_data.add_interface_label = self.gettext("Add Interface"), html = balloon_tmpl.render(html_data, {
                table1: device_tmpl,
                table2: ports.length > 0 ? port_tmpl : null
            });
            else {
                if ("instance" !== d.type) return;
                html_data.delete_label = self.gettext("Terminate Instance"), html_data.view_details_label = self.gettext("View Instance Details"), html_data.console_id = d.id, html_data.console = d.console, html = balloon_tmpl.render(html_data, {
                    table1: device_tmpl
                })
            }
            $(self.svg_container).append(html), device_position = element.find(".frame"), x = device_position.position().left + element_properties.device_width + element_properties.balloon_margin.x, y = device_position.position().top + element_properties.balloon_margin.y, $("#" + balloon_id).css({
                left: x + "px",
                top: y + "px"
            }).show(), $balloon = $("#" + balloon_id), $balloon.offset().left + $balloon.outerWidth() > $(window).outerWidth() && $balloon.css({
                left: "0px"
            }).css({
                left: device_position.position().left - $balloon.outerWidth() - element_properties.balloon_margin.x + "px"
            }).addClass("leftPosition"), $balloon.find(".delete-device").click(function() {
                var $this;
                $this = $(this), $this.prop("disabled", !0), d3.select("#id_" + $this.data("device-id")).classed("loading", !0), self.delete_device($this.data("type"), $this.data("device-id"))
            }), $balloon.find(".delete-port").click(function() {
                var $this;
                $this = $(this), self.delete_port($this.data("router-id"), $this.data("port-id"))
            }), self.balloon_id = balloon_id
        },
        delete_balloon: function() {
            var self;
            self = this, self.balloon_id && ($("#" + self.balloon_id).remove(), self.balloon_id = null)
        },
        post_message: function(id, url, message) {
            var iframe, iframe_id, self;
            self = this, iframe_id = "ifr_" + id, iframe = $('<iframe width="500" height="300" />').attr("id", iframe_id).attr("src", url).appendTo(self.post_messages), iframe.on("load", function() {
                $(this).get(0).contentWindow.postMessage(JSON.stringify(message, null, 2), "*")
            })
        },
        delete_post_message: function(id) {
            $("#" + id).remove()
        },
        gettext: function(text) {
            return text
        }
    }
}]).controller("TopologyCtrl", ["$scope", "$modal", "logger", "$q", "$timeout", "CONST_RESTFUL_API", "ComputeServersAPI", "NetworkingNetworkAPI", "NetworkingRouterAPI", "horizon_topology", function($scope, $modal, logger, $q, $timeout, CONST_RESTFUL_API, ComputeServersAPI, NetworkingNetworkAPI, NetworkingRouterAPI, horizon_topology) {
    var createInstance, createNetwork, createRouter;
    $scope.networktopology = CONST_RESTFUL_API.NETWORKING_URL.NETWORK_TOPOLOGY, $scope.init = function() {
        return horizon_topology.init($scope.networktopology), $q.all([]).then(function() {})
    }, createInstance = function(instance) {
        return ComputeServersAPI.save(instance, function() {
            logger.logSuccess("인스턴스가 생성되었습니다."), horizon_topology.load_network_info2()
        })
    }, createNetwork = function(network) {
        return NetworkingNetworkAPI.save(network, function(response) {
            response.success ? (logger.logSuccess("네트워크가 생성되었습니다."), horizon_topology.load_network_info2()) : logger.logError("네트워크 생성에 실패하였습니다.<br/>" + response.message)
        })
    }, createRouter = function(router) {
        return NetworkingRouterAPI.save(router, function() {
            logger.logSuccess("라우터가 생성되었습니다."), horizon_topology.load_network_info2()
        })
    }, $scope.createInstanceForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/instances/create.html",
            controller: "CreateInstanceModalCtrl",
            size: "lg"
        }), modalInstance.result.then(function(instance) {
            createInstance(instance)
        }, function() {})
    }, $scope.createNetworkForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/networks/create.html",
            controller: "CreateNetworkModalCtrl"
        }), modalInstance.result.then(function(network) {
            createNetwork(network)
        }, function() {})
    }, $scope.createRouterForm = function() {
        var modalInstance;
        modalInstance = $modal.open({
            templateUrl: "views/networks/router/create.html",
            controller: "CreateRouterModalCtrl"
        }), modalInstance.result.then(function(router) {
            createRouter(router)
        }, function() {})
    }
}]);
