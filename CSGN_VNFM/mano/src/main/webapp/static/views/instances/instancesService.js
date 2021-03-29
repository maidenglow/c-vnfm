
"use strict";
angular.module("app.instancesService", []
	).factory("ComputeServersAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.SERVERS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("ComputeServersGroupAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
        return $resource(CONST_RESTFUL_API.COMPUTE_URL.SERVERGROUP, {
            tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
            id: "@id"
        }, {
            update: {
                method: "PUT"
            }
        })
}]).factory("ComputeServersSnapShotAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
	    return $resource(CONST_RESTFUL_API.COMPUTE_URL.SNAPSHOT, {
	        title: "@title",
	        name: "@name"	
	    }, {
	        update: {
	            method: "PUT"
	        }
	    })
}]).factory("ComputeConsoleAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.VNFD_URL.CONSOLE_VNC, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("ComputeServerActionsAPI", ["$http", "$resource", "CONST_RESTFUL_API", function($http, $resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.SERVER_ACTIONS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    }, {
        action: {
            method: "POST"
        }
    })
}]).factory("ComputeFlavorsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.FLAVORS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("ComputeImagesAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.IMAGES, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        id: "@id"
    })
}]).factory("ComputeLimitAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.LIMIT)
}]).factory("InstanceLimitService", ["$rootScope", "ComputeLimitAPI", function() {
    var init, instances, limit, percentage, ram, vcpus;
    return limit = null, instances = {
        max: 0,
        used: 0,
        percent: function() {
            var percent;
            return percent = percentage(this.max, this.used), isNaN(percent) ? 0 : percent
        }
    }, vcpus = {
        max: 0,
        used: 0,
        percent: function() {
            var percent;
            return percent = percentage(this.max, this.used), isNaN(percent) ? 0 : percent
        }
    }, ram = {
        max: 0,
        used: 0,
        percent: function() {
            var percent;
            return percent = percentage(this.max, this.used), isNaN(percent) ? 0 : percent
        }
    }, init = function(limit) {
        this.limit = limit, instances.max = limit.absolute.maxTotalInstances, instances.used = limit.absolute.totalInstancesUsed, vcpus.max = limit.absolute.maxTotalCores, vcpus.used = limit.absolute.totalCoresUsed, ram.max = limit.absolute.maxTotalRAMSize, ram.used = limit.absolute.totalRAMUsed
    }, percentage = function(max, used) {
        var error;
        try {
            return Math.round(used / max * 100, 2)
        } catch (_error) {
            return error = _error, 0
        }
    }, {
        init: init,
        limit: limit,
        instances: instances,
        vcpus: vcpus,
        ram: ram
    }
}]).factory("ComputeAvailabilityZonesAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.AVAILABILITY_ZONES, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
    })
}]).factory("ComputeFloatingIpPoolsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.COMPUTE_URL.FLOATING_IP_POOLS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID,
        poolName: "@poolName"
    })
}]);
