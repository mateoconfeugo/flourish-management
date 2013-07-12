define(['underscore', 'backbone'],function(_, backbone){
    var self;
    var model = backbone.Model.extend({
	url: "/api/rest/user",
    });
    return new model;
});
