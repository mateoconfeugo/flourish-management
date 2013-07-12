define(['backbone', 'models/wizard_node'],
       function(Backbone, Node) {
	   return Backbone.Model.extend({
	       _head:  null,
	       _tail: null,
	       _current: null, 
	       first: function () { return this._head; },
	       last: function () { return this._tail; },
	       moveNext: function () {
		   return (this._current !== null) ? this._current = this._current.getNext() : null;
	       }, //set current to next and return current or return null
	       movePrevious: function () {
		   return (this._current !== null) ? this._current = this._current.getPrevious() : null;
	       }, //set current to previous and return current or return null
	       getCurrent: function () { return this._current; },
	       insertView: function (view) {
		   if (this._tail === null) { // list is empty (implied head is null)                    
		       this._current = this._tail = this._head = new Node(view);
		   }
		   else {//list has nodes                    
		       this._tail = this._tail.setNext(new Node(view).setPrevious(this._tail)).getNext();
		   }
	       },
	       setCurrentByTab: function (tab) {
		   var node = this._head;
		   while (node !== null) {
		       if (node.getTab() !== tab) { node = node.getNext(); }
		       else { this._current = node; break; }
		   }
	       }
	   });
       });