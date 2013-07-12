define(['jquery', 'underscore', 'backbone', 'models/plugin'],
       function($, _, Backbone, Plugin) {
	   var plugins = Backbone.Collection.extend({
	       urlRoot: 'plugin',
               model: Plugin,
	       parse: function (response) {
		   return response;
	       }
	   });
	   return plugins;
       });
