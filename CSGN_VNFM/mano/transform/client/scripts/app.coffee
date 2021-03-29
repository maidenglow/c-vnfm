'use strict';

angular.module('app', [
    # Angular modules
    'ngRoute'
    'ngAnimate'
    'ngMessages'

    # 3rd Party Modules
    'ui.bootstrap'
    'easypiechart'
    'mgo-angular-wizard'
    'textAngular'
    'ui.tree'
    'ngMap'
    'ngTagsInput'

    # Custom modules
    'app.controllers'
    'app.directives'
    'app.localization'
    'app.nav'
	'app.ui.directives'
    'app.ui.services'
    'app.task'
	'app.chart.directives'

	'app.filters'

	# Openstack
	'app.interceptors'

	'app.alarms'
	'app.alarmsService'

	'app.dashboard'
	'app.hypervisorService'
	'app.identityService'

    'app.systems'
    'app.systemsService'
    'app.systemsChart'

    'app.telemetrysService'

    'app.instances'
	'app.instancesService'
	'app.instancesChart'

    'app.volumes'
    'app.volumesService'

    'app.networks'
    'app.networksService'
    'app.routers'
    'app.topology'
    
    'app.flavors'

    'app.images'
    'app.imagesService'
])

.constant('CONST_RESTFUL_API', {
	prefixURL: '/mano',
	SYSTEM_URL: {
		SYSTEMS            : '/mano/system/systems/:systemId'
		ALARMS             : '/mano/system/alarms/:id'
	},
	TELEMETRY: {
		INSTANCE           : '/mano/telemetry/instances/:instanceId'
	},
	IDENTITY_URL: {
	    TENANTS            : '/mano/identity/tenants'
	    OS_KSADM_SERVICES  : '/mano/identity/OS-KSADM/services'
	},
	COMPUTE_URL: {
		TENANT_ID          : 'admin'
		SERVERS            : '/mano/compute/:tenantId/servers/:id'
		SERVER_ACTIONS     : '/mano/compute/:tenantId/servers/:id/action'
		FLAVORS            : '/mano/compute/:tenantId/flavors/:id'
		FLAVORS_ACCESS     : '/mano/compute/:tenantId/flavors/:id/os-flavor-access'
		FLAVORS_EXTRAS     : '/mano/compute/:tenantId/flavors/:id/extras/:key'
		IMAGES             : '/mano/compute/:tenantId/images/:id'
		LIMIT              : '/mano/compute/limit'
		AVAILABILITY_ZONES : '/mano/compute/:tenantId/zones'
		HYPERVISOR         : '/mano/compute/:tenantId/os-hypervisors'
		HYPERVISOR_STATS   : '/mano/compute/:tenantId/os-hypervisors/statistics'
		FLOATING_IP_POOLS  : '/mano/compute/:tenantId/os-floating-ip-pools/:poolName'
	},
	IMAGE_URL: {
		IMAGES             : '/mano/image/images/:id'
		IMAGES_UPLOAD      : '/mano/image/images'
	},
	BLOCKSTORAGE_URL: {
		VOLUME             : '/mano/blockStorage/:tenantId/volumes/:id'
		VOLUME_EXTEND      : '/mano/blockStorage/:tenantId/volumes/:id/extend'
		VOLUME_TYPE        : '/mano/blockStorage/:tenantId/volume-types/:id'        
		VOLUME_LIMITS      : '/mano/blockStorage/:tenantId/limits'
		VOLUME_SNAPSHOT    : '/mano/blockStorage/:tenantId/snapshots'
		VOLUME_ATTACHMENT  : '/mano/compute/:tenantId/servers/:serverId/os-volume_attachments'
	},
	NETWORKING_URL: {
		NETWORK             : '/mano/networking/networks/:id'
		ROUTER              : '/mano/networking/routers/:id'  
		ROUTER_GATEWAY      : '/mano/networking/routers/:id/gateway'
		ROUTER_INTERFACE    : '/mano/networking/routers/:id/interface'          
		SUBNET              : '/mano/networking/subnets/:id'
		PORT                : '/mano/networking/ports/:id'
		FLOATING_IP         : '/mano/networking/floatingip/:id'
		QUOTAS              : '/mano/networking/:tenantId/quotas'
		NETWORK_TOPOLOGY    : '/mano/networking/network_topology'
	}
})

.config([
    '$routeProvider'
    ($routeProvider) ->

        routes = [
        	'dashboard/dashboard'
        	'systems/index'
        	'instances/index'
        	'networks/networks/index'
        	'networks/router/index'
        	'networks/topology/index'
        	'volumes/index'
        	'flavors/index'
        	'images/index'
        ]
        
        setRoutes = (route) ->
            url = '/' + route
            config =
                templateUrl: 'views/' + route + '.html'

            $routeProvider.when(url, config)
            return $routeProvider

        routes.forEach( (route) ->
            setRoutes(route)
        )
        $routeProvider
            .when('/', { redirectTo: '/dashboard/dashboard' })
            .when('/404', { templateUrl: 'views/pages/404.html' })
            .otherwise( redirectTo: '/404' )
])

