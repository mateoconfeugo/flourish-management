define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var model = Backbone.Model.extend({
	       url: "/clientconfig/clientconfig",
	   });
	   return model;
       });
