// Filename: main.js
require.config({
    shim: {
	'underscore': {
	    exports: '_'
	},
	"backbone": {
            deps: ["underscore", "jquery"],
            exports: "Backbone" //attaches "Backbone" to the window object
	},
	"socketio": {
	    deps: ["jquery"],
	    exports: "io"
	},
	"bootstrap": {
	    deps: ["jquery"]
	},
	"bootstrapWizard": {
	    deps: ["jquery", "bootstrap"],
	    exports: "jQuery.bootstrapWizard"
	},
	"validate": {
	    deps: ["jquery"],
	    exports: "jQuery.fn.validate"
	},
	'log4javascript': {
	    exports: 'getDefaultLogger log'
	},
	"shim": {
	    "jquery-gentleSelect" : {
		deps: ["jquery"],
		exports: "jQuery.fn.gentleSelect"
	    }
	}
    },
    paths: {
	jquery: '/js/lib/jquery.min',
	underscore: '/js/lib/underscore-min',
	backbone: '/js/lib/backbone-min',
	bootstrapWizard: '/js/lib/jquery.bootstrap.wizard.min',
	socketio: '/js/lib/socket.io',
	bootstrap: '/js/lib/bootstrap',
	log4javascript: '/js/lib/log4javascript',
	validate: 'jquery.validate.min'
    },
    text: {
	useXhr: function (url, protocol, hostname, port) {
	    // allow cross-domain requests
	    // remote server allows CORS
	    return true;
	}
    },

});

require(['jquery', 'underscore', 'backbone', 'routers/desktop_router', 'views/lead_editor', 'views/support_group_lead',  'models/mouse_position', 'bootstrapWizard'], 
	function($, _, Backbone, Desktop, LeadEditor, SupportLead, MousePosition) {

	    var setup_heatmapping = function(cfg) {
		$("body").mousemove(function(event) {
		    var coord = new MousePosition({});
		    coord.set("x", event.pageX);
		    coord.set("y", event.pageY);
		    coord.save()});
	    };

	    var fetchSuccess = function(cfg) {
		var router = new Desktop({config: cfg});
		cfg.router = router;
		Backbone.history.start();
		$('#rootwizard').bootstrapWizard({
		    'class': 'nav nav-tabs',
		    onNext: function(event) { 
			var index = $('#rootwizard').bootstrapWizard('currentIndex');
			var size = $('#rootwizard').bootstrapWizard('navigationLength');
			if(index >= size) {
			    $('#lead-form-link').trigger('click');
			}
			return true;
		    }
		});
		var pager_cntls = $(".pager wizard");
		$('.hero-unit').append($('#nav-controls'));
		var support_lead  = new SupportLead({ el: '#support-group-form',  router: router});
		var ham = 1;
//		setup_heatmapping(cfg);
	    };
	    
	    // Get configuration and tie to application
	    var config = new Backbone.Model();
	    config.url = "/clientconfig";
	    config.fetch({success: fetchSuccess});
	});
