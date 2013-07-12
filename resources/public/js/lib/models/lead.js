define(['jquery', 'underscore', 'backbone'],
       function($, _, Backbone) {
	   var lead = Backbone.Model.extend({
	       validation: {
		   debug: true,
		   rules: {
		       'lead_full_name': {
			   required: true,
			   msg: 'Please enter a name'
		       },
		       'lead_email': {
			   pattern: 'email',
			   required: true,
			   msg: 'Please enter a valid email'
		       },
		       'lead_phone': {
			   msg: 'Please enter a valid phone number',
			   required: true,
			   phoneUS: true
		       }
		   }
	       },
	       urlRoot: '/lead'
	   });
	   return lead;
       });	  
