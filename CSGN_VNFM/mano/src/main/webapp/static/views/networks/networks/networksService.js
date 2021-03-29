
"use strict";
angular.module("app.networksService", []).factory("NetworkingNetworkAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.NETWORK, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("NetworkingSubnetAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.SUBNET, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("NetworkingPortAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.PORT, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("NetworkingRouterAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.ROUTER, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("NetworkingRouterGatewayAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.ROUTER_GATEWAY, {
        id: "@id"
    }, {
        "delete": {
            method: "delete"
        }
    })
}]).factory("NetworkingRouterInterfaceAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.ROUTER_INTERFACE, {
        id: "@router_id"
    }, {
        "delete": {
            method: "PUT"
        }
    })
}]).factory("NetworkingFloatingIpsAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.FLOATING_IP, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
    }, {
        "delete": {
            method: "delete"
        }
    })
}]).factory("NetworkingQuotasAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.NETWORKING_URL.QUOTAS, {
        tenantId: CONST_RESTFUL_API.COMPUTE_URL.TENANT_ID
    })
}])    
 //livemigration
    .factory("LiveMigrationInstanceListAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.LIVEMIGRATION_URL.INSTANCE_LIST, {
    	"zoneName" : "@zoneName"
    })
}]) 
    .factory("LiveMigrationRequestAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.LIVEMIGRATION_URL.LIVEMIGRATION_REQUEST, {
    	"instanceId": "@instanceId",
    	"hyperId": "@hyperId",
    	"blockmigration": "@blockmigration"
    })
}]) 
