define([],
       function() {
	   return Backbone.Model.extend({
	       next: null, //reference next node
	       previous: null, //reference previous node
	       view: null,
	       tab: null,
	       initialize: function (view) { 
		   this.view = view.ref; //reference current view
		   this.tab = view.tab;
	       },
	       setPrevious: function (node) { this.previous = node; return this; }, //chainable!
	       getPrevious: function () { return this.previous; },
	       setNext: function (node) { this.next = node; return this; }, //chainable!
	       getNext: function () { return this.next; },
	       getView: function () { return this.view; },
	       getTab: function () { return this.tab; }
	   });
       });
