/* 
   Component Name: lead_editor
   Description:  Gather leads
*/

define(
    ["jquery", "underscore", "backbone", "models/lead", "validate"],
    function($, _, Backbone, Lead) {
	var lead_editor =  Backbone.View.extend({
	    events: {
		"click #lead-form-submit": "updateModel",
	    },
	    initialize: function(options) {
		_.bindAll(this, "render", "updateModel", "submit", "gather_data", "on_save_success", "on_save_error");
		this.template = options.template;
		this.router = options.router;
		this.model = new Lead();
		$.validator.addMethod("phoneUS", function(phone_number, element) {
		    phone_number = phone_number.replace(/\s+/g, ""); 
		    return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
		}, "Please specify a valid phone number");
	    },
	    render: function(e) {
		return this;
	    },
	    submit: function(e){
//		e.preventDefault();
	    },
	    gather_data: function(model) {
		var full_name = this.$('#lead_full_name').val();
		this.model.set('full_name', full_name);
		var email = this.$('#lead_email').val();
		this.model.set('email', email);
		var phone = this.$('#lead_phone').val();
		this.model.set('phone', phone);
		try {
		    var postal_code = this.$('#lead_postal_code').val();
		    this.model.set('postal_code', postal_code);
		} catch(err) {}
		return this;
	    },
	    updateModel: function(e) {
		e.preventDefault();
		var options = this.model.validation;
		$("#lead_form").validate(options)
		if(!$("#lead_form").valid()){
		    return this;
		}
		this.gather_data();
		// TODO: figure out why this needs to be hacked like this
		this.model.urlRoot = "/lead";
		this.model.save({},{
				success: this.on_save_success,
				error: this.on_save_error
			       });
		return this;
	    },
	    on_save_success: function(model, response, options) {
		$('.modal-backdrop').remove();
		$('#lead_form_link').remove();
		$('#rootwizard').remove();
		$('aside#side-lead-form').remove();
		window.location.replace("/thank-you/" + this.model.get("full_name"));
	    },
	    on_save_error: function(e, response) {
		var json = JSON.parse(response.responseText);
		var html = Jemplate.process("server_validation_message.tt", {errors: json});
		this.$('.alert-error').show();
		this.$('.alert-error').append(html);
	    },
	    close: function(){
		this.$el.empty();
		this.unbind();
		return this;
    }
	});
	return lead_editor;
    });

