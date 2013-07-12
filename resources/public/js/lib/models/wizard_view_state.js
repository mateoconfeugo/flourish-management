define(['backbone', 'models/wizard_node'],
       function(Backbone, Node) {
	   return Backbone.Model.extend({
	       head:  null,
	       tail: null,
	       current: null, 
	       first: function () { return this.head; },
	       last: function () { return this.tail; },
	       moveNext: function () {
		   return (this.current !== null) ? this.current = this.current.getNext() : null;
	       }, 
	       movePrevious: function () {
		   return (this.current !== null) ? this.current = this.current.getPrevious() : null;
	       }, 
	       getCurrent: function () { return this.current; },
	       insertView: function (view) {
		   if (this.tail === null) { 
		       this.current = this.tail = this.head = new Node(view);
		   }
		   else {
		       this.tail = this.tail.setNext(new Node(view).setPrevious(this.tail)).getNext();
		   }
	       },
	       setCurrentByTab: function (tab) {
		   var node = this.head;
		   while (node !== null) {
		       if (node.getTab() !== tab) { node = node.getNext(); }
		       else { this.current = node; break; }
		   }
	       }
	   });
       });