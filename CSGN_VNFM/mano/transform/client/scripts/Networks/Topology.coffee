'use strict'

angular.module('app.topology', [])

.factory('horizon_topology', [
	'logger'
	'ComputeServersAPI'
	'NetworkingRouterAPI'
	'NetworkingRouterInterfaceAPI'
	(
		logger
		ComputeServersAPI
		NetworkingRouterAPI
		NetworkingRouterInterfaceAPI
	) ->
	    networktopology_url: null
	    model: null
	    svg: '#topology_canvas'
	    svg_container: '#topologyCanvasContainer'
	    post_messages: '#topologyMessages'
	    network_tmpl:
	      small: '#topology_template > .network_container_small'
	      normal: '#topology_template > .network_container_normal'
	    router_tmpl:
	      small: '#topology_template > .router_small'
	      normal: '#topology_template > .router_normal'
	    instance_tmpl:
	      small: '#topology_template > .instance_small'
	      normal: '#topology_template > .instance_normal'
	    balloon_tmpl: null
	    balloon_device_tmpl: null
	    balloon_port_tmpl: null
	    network_index: {}
	    balloon_id: null
	    reload_duration: 10000
	    draw_mode: 'normal'
	    network_height: 0
	    previous_message: null
	    element_properties:
	      normal:
	        network_width: 270
	        network_min_height: 400
	        top_margin: 80
	        default_height: 50
	        margin: 20
	        device_x: 98.5
	        device_width: 90
	        port_margin: 16
	        port_height: 6
	        port_width: 82
	        port_text_margin:
	          x: 6
	          y: -4
	        texts_bg_y: 32
	        type_y: 46
	        balloon_margin:
	          x: 12
	          y: -12
	      small:
	        network_width: 100
	        network_min_height: 400
	        top_margin: 50
	        default_height: 20
	        margin: 30
	        device_x: 47.5
	        device_width: 20
	        port_margin: 5
	        port_height: 3
	        port_width: 32.5
	        port_text_margin:
	          x: 0
	          y: 0
	        texts_bg_y: 0
	        type_y: 0
	        balloon_margin:
	          x: 12
	          y: -30
	      cidr_margin: 5
	      device_name_max_size: 9
	      device_name_suffix: '..'
	    init: (networktopology_url) ->
	      self = this
	      $(self.svg_container).spin
	        lines: 10
	        length: 15
	        width: 4
	        radius: 10
	        color: '#000'
	        speed: 0.8
	        trail: 50
	      if $('#networktopology').length == 0
	        return
	      self.networktopology_url = networktopology_url
	      self.color = d3.scale.category10()
	      self.balloon_tmpl = Hogan.compile($('#balloon_container').html())
	      self.balloon_device_tmpl = Hogan.compile($('#balloon_device').html())
	      self.balloon_port_tmpl = Hogan.compile($('#balloon_port').html())
	      $(document).on('click', 'a.closeTopologyBalloon', (e) ->
	        e.preventDefault()
	        self.delete_balloon()
	        return
	      ).on('click', '.topologyBalloon', (e) ->
	        e.stopPropagation()
	        return
	      ).on('click', 'a.vnc_window', (e) ->
	        e.preventDefault()
	        vnc_window = window.open($(this).attr('href'), vnc_window, 'width=760,height=560')
	        self.delete_balloon()
	        return
	      ).click ->
	        self.delete_balloon()
	        return
	      $('.toggleView > .btn').click ->
	        self.draw_mode = $(this).data('value')
	        $('g.network').remove()
	        $.cookie 'ntp_draw_mode', self.draw_mode
	        #horizon.cookies.put('ntp_draw_mode',self.draw_mode);
	        self.data_convert()
	        return
	      $(window).on 'message', (e) ->
	        message = $.parseJSON(e.originalEvent.data)
	        if self.previous_message != message.message
	          horizon.alert message.type, message.message
	          horizon.autoDismissAlerts()
	          self.previous_message = message.message
	          self.delete_post_message message.iframe_id
	          self.load_network_info()
	          setTimeout (->
	            self.previous_message = null
	            return
	          ), 10000
	        return
	      self.load_network_info()
	      return
	    load_network_info2: ->
	      self = this
	      if $('#networktopology').length == 0
	        return
	      $.getJSON self.networktopology_url + '?' + $.now(), (data) ->
	        self.model = data
	        self.data_convert()
	        return
	      return
	    load_network_info: ->
	      self = this
	      if $('#networktopology').length == 0
	        return
	      $.getJSON self.networktopology_url + '?' + $.now(), (data) ->
	        self.model = data
	        self.data_convert()
	        setTimeout (->
	          self.load_network_info()
	          return
	        ), self.reload_duration
	        return
	      return
	    select_draw_mode: ->
	      self = this
	      #var draw_mode = 'normal';//horizon.cookies.get('ntp_draw_mode');
	      draw_mode = $.cookie('ntp_draw_mode')
	      if draw_mode and (draw_mode == 'normal' or draw_mode == 'small')
	        self.draw_mode = draw_mode
	      else
	        if self.model.networks.length * self.element_properties.normal.network_width > $('#topologyCanvas').width()
	          self.draw_mode = 'small'
	        else
	          self.draw_mode = 'normal'
	        $.cookie 'ntp_draw_mode', self.draw_mode
	        #horizon.cookies.put('ntp_draw_mode',self.draw_mode);
	      $('.toggleView > .btn').each ->
	        $this = $(this)
	        if $this.hasClass(self.draw_mode)
	          $this.addClass 'active'
	        else
	          $this.removeClass 'active'
	        return
	      return
	    data_convert: ->
	      self = this
	      model = self.model
	      $.each model.networks, (index, network) ->
	        self.network_index[network.id] = index
	        $.each model.subnets, (index2, subnet) ->
	        	if network.id == subnet.network_id
	        		tmpSubnets = network.subnets
	        		network.subnets = []
	        		$.each tmpSubnets, (index3, subnet_id) ->
	        			if subnet_id == subnet.id || subnet_id["cidr"] == subnet.cidr
	        				network.subnets.push {cidr : subnet.cidr}
	        			return
	        	return
	        return
	      self.select_draw_mode()
	      element_properties = self.element_properties[self.draw_mode]
	      self.network_height = element_properties.top_margin
	      $.each [
	        {
	          model: model.routers
	          type: 'router'
	        }
	        {
	          model: model.servers
	          type: 'instance'
	        }
	      ], (index, devices) ->
	        `var model`
	        type = devices.type
	        model = devices.model
	        $.each model, (index, device) ->
	          device.type = type
	          device.ports = self.select_port(device.id)
	          hasports = if device.ports.length <= 0 then false else true
	          device.parent_network = if hasports then self.select_main_port(device.ports).network_id else self.model.networks[0].id
	          height = element_properties.port_margin * (device.ports.length - 1)
	          device.height = if self.draw_mode == 'normal' and height > element_properties.default_height then height else element_properties.default_height
	          device.pos_y = self.network_height
	          device.port_height = if self.draw_mode == 'small' and height > device.height then 1 else element_properties.port_height
	          device.port_margin = if self.draw_mode == 'small' and height > device.height then device.height / device.ports.length else element_properties.port_margin
	          self.network_height += device.height + element_properties.margin
	          return
	        return
	      $.each model.networks, (index, network) ->
	        network.devices = []
	        $.each [
	          model.routers
	          model.servers
	        ], (index, devices) ->
	          $.each devices, (index, device) ->
	            if network.id == device.parent_network
	              network.devices.push device
	            return
	          return
	        return
	      self.network_height += element_properties.top_margin
	      self.network_height = if self.network_height > element_properties.network_min_height then self.network_height else element_properties.network_min_height
	      self.draw_topology()
	      return
	    draw_topology: ->
	      self = this
	      $(self.svg_container).spin false
	      $(self.svg_container).removeClass 'noinfo'
	      if self.model.networks.length <= 0
	        $('g.network').remove()
	        $(self.svg_container).addClass 'noinfo'
	        return
	      svg = d3.select(self.svg)
	      element_properties = self.element_properties[self.draw_mode]
	      svg.attr('width', self.model.networks.length * element_properties.network_width).attr 'height', self.network_height
	      network = svg.selectAll('g.network').data(self.model.networks)
	      network_enter = network.enter().append('g').attr('class', 'network').each((d, i) ->
	        @appendChild d3.select(self.network_tmpl[self.draw_mode]).node().cloneNode(true)
	        $this = d3.select(this).select('.network-rect')
	        if d.url
	          $this.on('mouseover', ->
	            $this.transition().style 'fill', ->
	              d3.rgb(self.get_network_color(d.id)).brighter 0.5
	            return
	          ).on('mouseout', ->
	            $this.transition().style 'fill', ->
	              self.get_network_color d.id
	            return
	          ).on 'click', ->
	            window.location.href = d.url
	            return
	        else
	          $this.classed 'nourl', true
	        return
	      )
	      network.attr('id', (d) ->
	        'id_' + d.id
	      ).attr('transform', (d, i) ->
	        'translate(' + element_properties.network_width * i + ',' + 0 + ')'
	      ).select('.network-rect').attr('height', (d) ->
	        self.network_height
	      ).style 'fill', (d) ->
	        self.get_network_color d.id
	      network.select('.network-name').attr('x', (d) ->
	        self.network_height / 2
	      ).text (d) ->
	        d.name
	      network.select('.network-cidr').attr('x', (d) ->
	        self.network_height - self.element_properties.cidr_margin
	      ).text (d) ->
	        if(typeof d.subnets is 'undefined')
	          d.subnets = []
	        cidr = $.map(d.subnets, (n, i) ->
	          n.cidr
	        )
	        cidr.join ', '
	      network.exit().remove()
	      device = network.selectAll('g.device').data((d) ->
	        d.devices
	      )
	      device_enter = device.enter().append('g').attr('class', 'device').each((d, i) ->
	        device_template = self[d.type + '_tmpl'][self.draw_mode]
	        @appendChild d3.select(device_template).node().cloneNode(true)
	        return
	      )
	      device_enter.on('mouseenter', (d) ->
	        $this = $(this)
	        self.show_balloon d, $this
	        return
	      ).on 'click', ->
	        d3.event.stopPropagation()
	        return
	      device.attr('id', (d) ->
	        'id_' + d.id
	      ).attr('transform', (d, i) ->
	        'translate(' + element_properties.device_x + ',' + d.pos_y + ')'
	      ).select('.frame').attr 'height', (d) ->
	        d.height
	      device.select('.texts_bg').attr 'y', (d) ->
	        element_properties.texts_bg_y + d.height - element_properties.default_height
	      device.select('.type').attr 'y', (d) ->
	        element_properties.type_y + d.height - element_properties.default_height
	      device.select('.name').text (d) ->
	        self.string_truncate d.name
	      device.each (d) ->
	        if d.status == 'BUILD'
	          d3.select(this).classed 'loading', true
	        else if d.task == 'deleting'
	          d3.select(this).classed 'loading', true
	          if 'bl_' + d.id == self.balloon_id
	            self.delete_balloon()
	        else
	          d3.select(this).classed 'loading', false
	          if 'bl_' + d.id == self.balloon_id
	            $this = $(this)
	            self.show_balloon d, $this
	        return
	      device.exit().each((d) ->
	        if 'bl_' + d.id == self.balloon_id
	          self.delete_balloon()
	        return
	      ).remove()
	      port = device.select('g.ports').selectAll('g.port').data((d) ->
	        d.ports
	      )
	      port_enter = port.enter().append('g').attr('class', 'port').attr('id', (d) ->
	        'id_' + d.id
	      )
	      port_enter.append('line').attr 'class', 'port_line'
	      port_enter.append('text').attr 'class', 'port_text'
	      device.select('g.ports').each (d, i) ->
	        @_portdata = {}
	        @_portdata.ports_length = d.ports.length
	        @_portdata.parent_network = d.parent_network
	        @_portdata.device_height = d.height
	        @_portdata.port_height = d.port_height
	        @_portdata.port_margin = d.port_margin
	        @_portdata.left = 0
	        @_portdata.right = 0
	        $(this).mouseenter (e) ->
	          e.stopPropagation()
	          return
	        return
	      port.each (d, i) ->
	        index_diff = self.get_network_index(@parentNode._portdata.parent_network) - self.get_network_index(d.network_id)
	        @_index_diff = index_diff = if index_diff >= 0 then ++index_diff else index_diff
	        @_direction = if @_index_diff < 0 then 'right' else 'left'
	        @_index = @parentNode._portdata[@_direction]++
	        return
	      port.attr 'transform', (d, i) ->
	        x = if @_direction == 'left' then 0 else element_properties.device_width
	        ports_length = @parentNode._portdata[@_direction]
	        distance = @parentNode._portdata.port_margin
	        y = (@parentNode._portdata.device_height - (ports_length - 1) * distance) / 2 + @_index * distance
	        'translate(' + x + ',' + y + ')'
	      port.select('.port_line').attr('stroke-width', (d, i) ->
	        @parentNode.parentNode._portdata.port_height
	      ).attr('stroke', (d, i) ->
	        self.get_network_color d.network_id
	      ).attr('x1', 0).attr('y1', 0).attr('y2', 0).attr 'x2', (d, i) ->
	        parent = @parentNode
	        width = (Math.abs(parent._index_diff) - 1) * element_properties.network_width + element_properties.port_width
	        if parent._direction == 'left' then -1 * width else width
	      port.select('.port_text').attr('x', (d) ->
	        parent = @parentNode
	        if parent._direction == 'left'
	          d3.select(this).classed 'left', true
	          element_properties.port_text_margin.x * -1
	        else
	          d3.select(this).classed 'left', false
	          element_properties.port_text_margin.x
	      ).attr('y', (d) ->
	        element_properties.port_text_margin.y
	      ).text (d) ->
	        ip_label = []
	        $.each d.fixed_ips, ->
	          ip_label.push @ip_address
	          return
	        ip_label.join ','
	      port.exit().remove()
	      return
	    get_network_color: (network_id) ->
	      @color @get_network_index(network_id)
	    get_network_index: (network_id) ->
	      @network_index[network_id]
	    select_port: (device_id) ->
	      $.map @model.ports, (port, index) ->
	        if port.device_id == device_id
	          return port
	        return
	    select_main_port: (ports) ->
	      _self = this
	      main_port_index = 0
	      MAX_INT = 4294967295
	      min_port_length = MAX_INT
	      $.each ports, (index, port) ->
	        port_length = _self.sum_port_length(port.network_id, ports)
	        if port_length < min_port_length
	          min_port_length = port_length
	          main_port_index = index
	        return
	      ports[main_port_index]
	    sum_port_length: (network_id, ports) ->
	      self = this
	      sum_port_length = 0
	      base_index = self.get_network_index(network_id)
	      $.each ports, (index, port) ->
	        sum_port_length += base_index - self.get_network_index(port.network_id)
	        return
	      sum_port_length
	    string_truncate: (string) ->
	      self = this
	      str = string
	      max_size = self.element_properties.device_name_max_size
	      suffix = self.element_properties.device_name_suffix
	      bytes = 0
	      i = 0
	      while i < str.length
	        bytes += if str.charCodeAt(i) <= 255 then 1 else 2
	        if bytes > max_size
	          str = str.substr(0, i) + suffix
	          break
	        i++
	      str
	    delete_device: (type, device_id) ->
	      self = this
	      #console.log self.model
	      #message = id: device_id
	      #self.post_message device_id, type, message
	      if type == 'instance'
	      	if self.model && self.model.servers
	      		for server in self.model.servers
	      			if device_id is server.id
	      				ComputeServersAPI.delete server,
	      					(response) ->
	      						if response.success
	      							logger.log '삭제 되었습니다.'
	      						else
	      							logger.logError '삭제를 실패하였습니다. ' + response.fault
	      						return
	      				break;
	      else if type == 'router'
	      	if self.model && self.model.routers
	      		for router in self.model.routers
	      			if device_id is router.id
	      				NetworkingRouterAPI.delete router,
	      					(response) ->
	      						if response.success
	      							logger.log '삭제 되었습니다.'
	      						else
	      							logger.logError '삭제를 실패하였습니다. ' + response.fault
	      						return
	      				break;
	      return
	    delete_port: (router_id, port_id) ->
	      self = this
	      #console.log self.model
	      if self.model && self.model.ports
	      	for port in self.model.ports
	      		if port_id is port.id
	      			param = angular.copy(port)
	      			param.router_id = router_id
	      			NetworkingRouterInterfaceAPI.delete param,
	      				(response) ->
	      					if response.success
	      						logger.logSuccess "인터페이스가 삭제되었습니다."
	      					else
	      						logger.logError "인터페이스 삭제시 오류가 발생하였습니다. <br/>" + response.message
	      			break;

	      return
	    show_balloon: (d, element) ->
	      self = this
	      element_properties = self.element_properties[self.draw_mode]
	      if self.balloon_id
	        self.delete_balloon()
	      balloon_tmpl = self.balloon_tmpl
	      device_tmpl = self.balloon_device_tmpl
	      port_tmpl = self.balloon_port_tmpl
	      balloon_id = 'bl_' + d.id
	      ports = []
	      $.each d.ports, (i, port) ->
	        object = {}
	        object.id = port.id
	        object.router_id = port.device_id
	        object.url = port.url
	        object.port_status = port.status
	        object.port_status_css = if port.status == 'ACTIVE' then 'active' else 'down'
	        ip_address = ''
	        try
	          ip_address = port.fixed_ips[0].ip_address
	        catch e
	          ip_address = self.gettext('None')
	        device_owner = ''
	        try
	          device_owner = port.device_owner.replace('network:', '')
	        catch e
	          device_owner = self.gettext('None')
	        object.ip_address = ip_address
	        object.device_owner = device_owner
	        object.is_interface = device_owner == 'router_interface'
	        ports.push object
	        return
	      html_data =
	        balloon_id: balloon_id
	        id: d.id
	        url: d.url
	        name: d.name
	        type: d.type
	        delete_label: self.gettext('Delete')
	        status: d.status
	        status_class: if d.status == 'ACTIVE' then 'active' else 'down'
	        status_label: self.gettext('STATUS')
	        id_label: self.gettext('ID')
	        interfaces_label: self.gettext('Interfaces')
	        delete_interface_label: self.gettext('Delete Interface')
	        open_console_label: self.gettext('Open Console')
	        view_details_label: self.gettext('View Details')
	      if d.type == 'router'
	        html_data.delete_label = self.gettext('Delete Router')
	        html_data.view_details_label = self.gettext('View Router Details')
	        html_data.port = ports
	        html_data.add_interface_url = d.url + 'addinterface'
	        html_data.add_interface_label = self.gettext('Add Interface')
	        html = balloon_tmpl.render(html_data,
	          table1: device_tmpl
	          table2: if ports.length > 0 then port_tmpl else null)
	      else if d.type == 'instance'
	        html_data.delete_label = self.gettext('Terminate Instance')
	        html_data.view_details_label = self.gettext('View Instance Details')
	        html_data.console_id = d.id
	        html_data.console = d.console
	        html = balloon_tmpl.render(html_data, table1: device_tmpl)
	      else
	        return
	      $(self.svg_container).append html
	      device_position = element.find('.frame')
	      x = device_position.position().left + element_properties.device_width + element_properties.balloon_margin.x
	      y = device_position.position().top + element_properties.balloon_margin.y
	      $('#' + balloon_id).css(
	        'left': x + 'px'
	        'top': y + 'px').show()
	      $balloon = $('#' + balloon_id)
	      if $balloon.offset().left + $balloon.outerWidth() > $(window).outerWidth()
	        $balloon.css('left': 0 + 'px').css('left': device_position.position().left - $balloon.outerWidth() - element_properties.balloon_margin.x + 'px').addClass 'leftPosition'
	      $balloon.find('.delete-device').click (e) ->
	        $this = $(this)
	        $this.prop 'disabled', true
	        d3.select('#id_' + $this.data('device-id')).classed 'loading', true
	        self.delete_device $this.data('type'), $this.data('device-id')
	        return
	      $balloon.find('.delete-port').click (e) ->
	        $this = $(this)
	        self.delete_port $this.data('router-id'), $this.data('port-id')
	        return
	      self.balloon_id = balloon_id
	      return
	    delete_balloon: ->
	      self = this
	      if self.balloon_id
	        $('#' + self.balloon_id).remove()
	        self.balloon_id = null
	      return
	    post_message: (id, url, message) ->
	      self = this
	      iframe_id = 'ifr_' + id
	      iframe = $('<iframe width="500" height="300" />').attr('id', iframe_id).attr('src', url).appendTo(self.post_messages)
	      iframe.on 'load', ->
	        $(this).get(0).contentWindow.postMessage JSON.stringify(message, null, 2), '*'
	        return
	      return
	    delete_post_message: (id) ->
	      $('#' + id).remove()
	      return
	    gettext : (text) ->
	    	return text;
])

.controller('TopologyCtrl', [
	'$scope'
	'$modal'
	'logger'
	'$q'
	'$timeout'
	'CONST_RESTFUL_API'
	'ComputeServersAPI'
	'NetworkingNetworkAPI'
	'NetworkingRouterAPI'
	'horizon_topology'
	(
		$scope
		$modal
		logger
		$q
		$timeout
		CONST_RESTFUL_API
		ComputeServersAPI
		NetworkingNetworkAPI
		NetworkingRouterAPI
		horizon_topology
	) ->

		$scope.networktopology =  CONST_RESTFUL_API.NETWORKING_URL.NETWORK_TOPOLOGY;

		#========================================
		# Initialization
		#========================================
		$scope.init = (network_topology) ->
			horizon_topology.init($scope.networktopology);
			return $q.all([

			]).then((result) ->

			)

		#========================================
		# Functions
		#========================================

		########################
		# create Instance
		########################
		createInstance = (instance) ->
			ComputeServersAPI.save instance,
				(response) ->
					logger.logSuccess "인스턴스가 생성되었습니다."
					horizon_topology.load_network_info2()
					return

		########################
		# create Network
		########################
		createNetwork = (network) ->
			NetworkingNetworkAPI.save network,
				(response) ->
					if response.success
						logger.logSuccess "네트워크가 생성되었습니다."
						horizon_topology.load_network_info2()
					else
						logger.logError "네트워크 생성에 실패하였습니다.<br/>" + response.message
					return

		########################
		# create Router
		########################
		createRouter = (router) ->
			NetworkingRouterAPI.save router,
				(response) ->
					logger.logSuccess "라우터가 생성되었습니다."
					horizon_topology.load_network_info2()
					return

		#========================================
		# $Scope
		#========================================



		#========================================
		# Button Event
		#========================================

		########################
		# Create Instance Form
		########################
		$scope.createInstanceForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/instances/create.html'
				controller: 'CreateInstanceModalCtrl'
				size: 'lg'
			)

			modalInstance.result.then(
				(instance) ->
					createInstance instance
					return
				, ->
					return
			)

			return # // end create Instance Form

		########################
		# Create Network Form
		########################
		$scope.createNetworkForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/networks/create.html'
				controller: 'CreateNetworkModalCtrl'
			)

			modalInstance.result.then(
				(network) ->
					createNetwork network
					return
				, ->
					return
			)

			return # // end create Network Form

		########################
		# Create Router Form
		########################
		$scope.createRouterForm = ->
			modalInstance = $modal.open(
				templateUrl: 'views/networks/router/create.html'
				controller: 'CreateRouterModalCtrl'
			)

			modalInstance.result.then(
				(router) ->
					createRouter router
					return
				, ->
					return
			)

			return # // end create Router Form

		return	# end of RouterCtrl
])


