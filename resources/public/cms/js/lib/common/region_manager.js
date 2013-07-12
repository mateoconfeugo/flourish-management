/*
  This object is solely responsible for managing the content of a specific DOM element, 
  displaying what needs to be displayed and cleaning up anything that no longer needs to be there.
*/
define(['jquery', 'underscore', 'backbone' ], function($, _, Backbone) {
    return Backbone.Model.extend ({
	currentView: 0,
	'initialize': function(options) {
	    _.bindAll(this, 'openView', 'closeView', 'show');
	    this.config = options.config;
	},
	closeView: function (view) {
            if (view && view.close) {
		view.close();
            }
	},
	openView: function (viewType, el, cfg, template) {
	    if(viewType) {
		var view = new viewType({el: el, config: cfg, template: template});
		this.currentView = view;
		if(view.renderOnInitialize ) {
		   view.render();
		}
	    }
	},
	show: function (options) {
	    var view = options.view;
	    var el = options.el;
	    var cfg = options.config;
	    var template = options.template;
            this.closeView(this.currentView);
            this.openView(view, el, cfg, template);
	}
    });
});
