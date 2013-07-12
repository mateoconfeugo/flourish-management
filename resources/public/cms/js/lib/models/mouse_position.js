define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var mouse_position = Backbone.Model.extend({
	       urlRoot: 'heatmap',
	   });
	   return mouse_position;
       });
