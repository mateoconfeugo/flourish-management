/* 
   Component Name: thank you page
   Description:  the page that guides them out of the lead gen process does the conversion tracking
*/

define(
    ["jquery", "underscore", "backbone"]
    function($, _, Backbone) {
	var thank_you_page =  Backbone.View.extend({
	    initialize: function(options) {
		_.bindAll(this, "render", "close");
		this.render();
		return this;
	    },
	    render: function(e) {
		this.$el.html(Jemplate.process("thank_you.tt"));
		$("#lead_form").validate();
		return this;
	    },
	    close: function(){
		this.$el.empty();
		this.unbind();
		return this;
	    }
	});
	return thank_you_page;
    });

