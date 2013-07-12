define(['jquery', 'underscore', 'backbone', 'models/config'],
       function($, _, Backbone, Config) {
	   return Backbone.View.extend({
//	       el: 'browser-javascript-viewer',
	       initialize: function(){
		   this.config = Config.attributes.views.browserJavascript;
		   this.config.url = this.config.jsBrowserURI
		   _.bindAll(this, 'render', 'loadBrowserJavascript', 'close', 'onSuccess'); 
	       },
	       loadBrowserJavascript: function(adUnitType) {
		   var url =  this.config.url;
		   $.get(url, function(data) {
		       $('#browser-javascript-viewer').text(data);
		   });
	       },
	       render: function () {
		   var html = Jemplate.process('browser_javascript_viewer.tt');
		   this.html = html;
		   $(this.el).empty();
		   $(this.el).append(html);
		   this.loadBrowserJavascript();
		   return this;
	       },
	       close: function(){
		   $(this.el).empty();
		   this.unbind();
	       },
	       onSuccess: function() {
		   this.render();
	       }

	   });
       });
