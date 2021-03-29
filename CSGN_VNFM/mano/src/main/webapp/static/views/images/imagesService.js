
"use strict";
angular.module("app.imagesService", ["ngResource"])
.factory("ImageImagesAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.IMAGE_URL.IMAGES, {
        id: "@id"
    }, {
        update: {
            method: "PUT"
        }
    })
}]).factory("ImageUploadAPI", ["$resource", "CONST_RESTFUL_API", function($resource, CONST_RESTFUL_API) {
    return $resource(CONST_RESTFUL_API.IMAGE_URL.IMAGES_UPLOADS,{})
}]);