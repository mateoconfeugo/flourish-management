define(
    ["jquery", "underscore", "backbone", "models/support_group_lead", "validate"],
    function($, _, Backbone, SupportLead) {
	var support_lead =  Backbone.View.extend({
	    events: {
		"click .support-group-lead-btn": "update_support_lead"
	    },
	    initialize: function(options) {
		_.bindAll(this, "render",  "submit", "gather_data", "update_support_lead", "on_save_success", "on_save_error");
		this.router = options.router;
		this.model = new SupportLead();
		this.model.on("sync", this.render);
		this.render();

		$.validator.addMethod("phoneUS", function(phone_number, element) {
		    phone_number = phone_number.replace(/\s+/g, ""); 
		    return this.optional(element) || phone_number.length > 9 &&
			phone_number.match(/^(1-?)?(\([2-9]\d{2}\)|[2-9]\d{2})-?[2-9]\d{2}-?\d{4}$/);
		}, "Please specify a valid phone number");

		$.validator.addMethod("postalUS", function(value, element) {
		    return this.optional(element) || /\d{5}-\d{4}$|^\d{5}$/.test(value);
		}, "The specified US ZIP Code is invalid");
		return this;
	    },
	    render: function(e) {
		return this;
	    },
	    submit: function(e){
//		e.preventDefault();
	    },
	    gather_data: function(model) {
		var full_name = this.$('#lead_full_name').val();
		model.set('full_name', full_name);
		var email = this.$('#lead_email').val();
		model.set('email', email);
		var phone = this.$('#lead_phone').val();
		model.set('phone', phone);
		try {
		    var postal_code = this.$('#lead_postal_code').val();
		    model.set('postal_code', postal_code);
		} catch(err) {}
		return model;
	    },
	    update_support_lead: function(e) {
		e.preventDefault();
		var options = this.model.validation;
		$("#support-group-form").validate(options)
		if(!$("#support-group-form").valid()){
		    return this;
		}
		this.gather_data(this.model);
		this.model.save({}, {success: this.on_save_success, error: this.on_save_error});
	    },
	    on_save_success: function(model, response, options) {
		$('.modal-backdrop').remove();
		$('#lead_form_link').remove();
		$('#myModal').remove();
		$('#rootwizard').html(Jemplate.process("thank_you.tt"));			
		$('aside#side-lead-form').remove();
	    },
	    on_save_error: function(e, response) {
		var json = JSON.parse(response.responseText);
		var html = Jemplate.process("server_validation_message.tt", {errors: JSON.parse(json.error)});
		this.$('.alert-error').show();
		this.$('.alert-error').append(html);
	    },
	    close: function(){
		this.$el.empty();
		this.unbind();
		return this;
	    }
	});
	return support_lead;
    });

