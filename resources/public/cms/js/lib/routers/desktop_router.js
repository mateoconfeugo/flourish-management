define(
    ['jquery', 'underscore', 'backbone', 'common/region_manager', 'views/lead_editor', 'views/support_group_lead', 'log4javascript'], 
    function($, _, Backbone, RegionManager, LeadEditor, SupportLead) {
	var log = log4javascript.getDefaultLogger();
	var router = Backbone.Router.extend({
	    initialize: function(options) {
		this.config = options.config;
		this.manager = new RegionManager({config: options.config});
		this.manager.show({view: LeadEditor, el: '#side-lead-form',  config: this.config, template: "lead_editor.tt", router: this});
	    },
            'routes': {
		'lead_form': 'lead_editor',
		'support_group' : 'support_lead'
            },
	    'lead_editor':  function() {
		this.manager.show({view: LeadEditor, el: '#side-lead-form',  config: this.config, template: "lead_editor.tt"});
	    },
	    'support_lead': function() {
		this.manager.show({view: SupportLead, el: '#support-group-form',  config: this.config});
	    }

	});
	return router;
    });
