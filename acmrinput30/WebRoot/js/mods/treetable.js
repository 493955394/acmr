define(function(require, exports, module) {
	var $ = require('jquery'),
		Node,
		Tree,
		methods;
	methods = {
		init: function(options) {
			var setting = $.extend({
				branchAttr: "ttBranch",
				clickableNodeNames: true,
				column: 1,
				columnElType: "td", // i.e. 'td', 'th' or 'td,th'
				expandable: false,
				expanderTemplate: "<a href='#'>&nbsp;</a>", // next level arraw HTML tempalte
				indent: 19,
				indenterTemplate: "<span class='indenter'></span>", // indenter HTML content template
				initialState: "collapsed", //init status 
				nodeIdAttr: "ttId", // maps to data-tt-id
				parentIdAttr: "ttParentId", // maps to data-tt-parent-id
				stringExpand: "Expand",
				stringCollapse: "Collapse",

				// Events
				onInitialized: null,
				onNodeCollapse: null, // node collapse event
				onNodeExpand: null,
				onNodeInitialized: null
			}, options);
			return this.each(function() {
				var el = $(this), //{table}
					tree;
				if (el.data('treetable') === undefined || el.data('treetable') === null) {
					tree = new Tree(this, setting);
					tree.loadRows(this.rows).render();
					el.addClass('treetable table form-inline').data('treetable', tree);
				}
				return el;
			});
		},
		expandNode: function(id) {
			var node = this.data('treetable').tree[id];
			if (node) {
				if (!node.initialized) {
					node._initialize();
				}
				node.expand();
			} else {
				throw new Error('unknown node "' + id + '"');
			}
		},
		move: function(nodeId, destinationId, oprArray, currentIndex, pId, moveType,partIndex) {
			
			var destination, node, destinationParent,oprArray;
			oprArray = oprArray[partIndex]['part_regin'] 
			node = this.data('treetable').tree[nodeId];
			destination = this.data('treetable').tree[destinationId];
			if (moveType === 'pre') {
				destinationParent = this.data("treetable").tree[this.data("treetable").tree[destinationId].parentId];
			}
			if (moveType === 'inner' && destinationParent === undefined) {
				destinationParent = destination;
			}
			this.data('treetable').move(node, destination, destinationParent, oprArray, currentIndex, pId, moveType);
			return this;
		},
		node: function(id) {
			return this.data('treetable').tree[id];
		}
	}
	Node = (function() {
		function Node(row, tree, setting) {
			var parentId;
			this.row = row;
			this.tree = tree;
			this.setting = setting;
			this.id = this.row.data(this.setting.nodeIdAttr);
			parentId = this.row.data(this.setting.parentIdAttr);
			if (parentId != null && parentId !== '') {
				this.parentId = parentId;
			}
			this.treeCell = $(this.row.children(this.setting.columnElType)[this.setting.column]);
			this.expander = $(this.setting.expanderTemplate);
			this.indenter = $(this.setting.indenterTemplate);
			this.children = [];
			this.initialized = false;
			this.treeCell.prepend(this.indenter);
		}
		Node.prototype.addChild = function(node) {
			return this.children.push(node);
		};
		Node.prototype.render = function() {
			var handler,
				setting = this.setting,
				target,
				eventsObj;
			handler = function(e) {
				$(this).parents("table").treetable("node", $(this).parents("tr").data(setting.nodeIdAttr)).toggle();
				return e.preventDefault();
			};
			target = setting.clickableNodeNames === true ? this.treeCell : this.expander;
			if (setting.expandable === true && this.isBranchNode()) {
				this.indenter.html(this.expander);
				//question : this have some doubt , why must be the firs off , why bind multiplue
				target.off('click').on("click", handler);
				target.off('keydown').on("keydown", function(e) {
					if (e.keyCode == 13) {
						handler.apply(this, [e]);
					}
				});
			}

			eventsObj = $._data(target[0], 'events') || undefined;
			if (!this.isBranchNode() && eventsObj && eventsObj['click'] && eventsObj['keydown']) {
				target.off("click", handler);
				target.off("keydown");
				this.indenter.html('');
			}

			this.indenter[0].style.paddingLeft = "" + (this.level() * setting.indent) + "px";

			return this;
		}
		Node.prototype.updateBranchLeafClass = function() {
			this.row.removeClass('branch');
			this.row.removeClass('leaf');
			this.row.addClass(this.isBranchNode() ? 'branch' : 'leaf');
		}
		Node.prototype.isBranchNode = function() {
			if (this.children.length > 0 || this.row.data(this.setting.branchAttr) === true) {
				return true;
			} else {
				return false;
			}
		}
		Node.prototype.show = function() {
			if (!this.initialized) {
				this._initialize();
			}
			this.row.show();
			if (this.expanded()) {
				this._showChildren();
			}
			return this;
		}
		Node.prototype._initialize = function() {
			var setting = this.setting;
			this.render();
			if (setting.expandable === true && setting.initialState === 'collapsed') {
				this.collapse();
			} else {
				this.expand();
			}
			if (setting.onNodeInitialized != null) {
				setting.onNodeInitialized.apply(this);
			}
			return this.initialized = true;
		}
		Node.prototype.toggle = function() {
			if (this.expanded()) {
				this.collapse();
			} else {
				this.expand();
			}
			return this;
		}
		Node.prototype.level = function() {
			return this.ancestors().length;
		}
		Node.prototype.ancestors = function() {
			var ancestors, node;
			node = this;
			ancestors = [];
			while (node = node.parentNode()) {
				ancestors.push(node);
			}
			return ancestors;
		}
		Node.prototype.parentNode = function() {
			if (this.parentId != null) {
				return this.tree[this.parentId];
			} else {
				return null;
			}
		}
		Node.prototype.collapse = function() {
			if (this.collapsed()) {
				return this;
			}
			this.row.removeClass('expanded').addClass('collapsed');
			this._hideChildren();
			this.expander.attr('title', this.setting.stringExpand);
			if (this.initialized && this.setting.onNodeCollapse != null) {
				this.setting.onNodeCollapse.apply(this);
			}
			return this;
		}
		Node.prototype.collapsed = function() {
			return this.row.hasClass('collapsed');
		}
		Node.prototype._hideChildren = function() {
			var child, _i, _len, _ref, _results;
			_ref = this.children;
			_results = [];
			for (_i = 0, _len = _ref.length; _i < _len; _i++) {
				child = _ref[_i];
				_results.push(child.hide());
			}
			return _results;
		}
		Node.prototype.hide = function() {
			this._hideChildren();
			this.row.hide();
			return this;
		}
		Node.prototype.expand = function() {
			if (this.expanded()) {
				return this;
			}
			this.row.removeClass('collapsed').addClass('expanded');
			if (this.initialized && this.setting.onNodeExpand != null) {
				this.setting.onNodeExpand.apply(this);
			}
			if ($(this.row).is(':visible')) {
				this._showChildren();
			}
			this.expander.attr('title', this.setting.stringCollapse);
			return this;
		}
		Node.prototype.expanded = function() {
			return this.row.hasClass('expanded');
		}
		Node.prototype._showChildren = function() {
			var child, _i, _len, _ref, _results;
			_ref = this.children;
			_results = [];
			for (_i = 0, _len = _ref.length; _i < _len; _i++) {
				child = _ref[_i];
				_results.push(child.show());
			};
			return _results;
		}
		Node.prototype.setParent = function(node) {
			if (this.parentId != null) {
				this.tree[this.parentId].removeChild(this);
			}
			if (node && node.id) {
				this.parentId = node.id;
				this.row.data(this.setting.parentIdAttr, node.id);
				return node.addChild(this);
			} else {
				this.parentId = null;
				this.row.data(this.setting.parentIdAttr, null);
			}
		};
		Node.prototype.removeChild = function(child) {
			var i = $.inArray(child, this.children);
			return this.children.splice(i, 1);
		}
		return Node;
	})();
	Tree = (function() {
		function Tree(table, setting) {
			this.table = table;
			this.setting = setting;
			this.tree = {}; // node tree 
			// nodes , roots for fast display user 
			this.nodes = []; // all nodes
			this.roots = []; // all root node
		}
		Tree.prototype.loadRows = function(rows) {
			var node, row, i;
			if (rows != null) {
				for (i = 0; i < rows.length; i++) {
					row = $(rows[i]);
					if (row.data(this.setting.nodeIdAttr) != null) {
						node = new Node(row, this.tree, this.setting);
						this.nodes.push(node);
						this.tree[node.id] = node;
						if (node.parentId != null && this.tree[node.parentId]) {
							this.tree[node.parentId].addChild(node);
						} else {
							this.roots.push(node);
						}
					}
				}
			}
			for (i = 0; i < this.nodes.length; i++) {
				node = this.nodes[i].updateBranchLeafClass();
			}
			return this;
		};
		Tree.prototype.render = function() {
			var root, _i, _len, _ref;
			_ref = this.roots;
			for (_i = 0, _len = _ref.length; _i < _len; _i++) {
				root = _ref[_i];
				root.show();
			}
			return this;
		}
		Tree.prototype.move = function(node, destination, destinationParent, oprArray, currentIndex, pId, moveType) {
			var nodeParent = node.parentNode(),
				newNodeParent;
			//ensure drag didn't myself and didn't its currentParent
			//question : this condition , bsed on moveType equal 'inner' status 
			if (node !== destination && $.inArray(node, destination.ancestors()) === -1) {
				node.setParent(destinationParent);
				this._moveOprArray(node, destination, destinationParent, oprArray, currentIndex, pId, moveType);
				//move node complete
				this._moveRows(node, destination, moveType, true);
				//render new parentNode effect
				newNodeParent = node.parentNode();
				if (newNodeParent && newNodeParent.children.length === 1) {
					newNodeParent.render();
				}
				if (nodeParent && nodeParent.children.length === 0) {
					nodeParent.render();
				}
			}
			//this setting background for new , old parentNode
			//render old parentNode
			if (nodeParent) {
				nodeParent.updateBranchLeafClass();
			}
			//render new parentNode
			if (newNodeParent) {
				newNodeParent.updateBranchLeafClass();
			}
			//destroy code start
			//this partten has destroy , beacuse of I don't think , when happen node change background
			//
			// node.updateBranchLeafClass();
			// 
			// end
			return this;
		}
		Tree.prototype._moveRows = function(node, destination, moveType, isInitialize) {
			var children = node.children,
				i;
			if (moveType === 'pre' && isInitialize) {
				node.row.insertBefore(destination.row);
			}
			if ((moveType === 'inner' && isInitialize) || !isInitialize) {
				node.row.insertAfter(destination.row);
			}
			node.render();
			for (i = children.length - 1; i >= 0; i--) {
				//it's children always follow node
				this._moveRows(children[i], node, false);
			}
		}
		Tree.prototype._moveOprArray = function(node, destination, destinationParent, oprArray, currentIndex, pId, moveType) {
			var config = {
					currentArray: oprArray[currentIndex].list || [],
					currentPosi: $(node.row).index() || 0,
					targetPosi: moveType == 'inner' ? $(destination.row).index() + 1 : $(destination.row).index() //on inner status , index value need plus 1 , ensure inner
				},
				currentObj,
				len = config.currentArray.length;
			currentObj = config.currentArray.slice(config.currentPosi, config.currentPosi + 1)[0];

			if (destinationParent && destinationParent.id) {
				config.currentArray[config.currentPosi]['pid'] = destinationParent.id
			} else {
				config.currentArray[config.currentPosi]['pid'] = '';
			}
			// position lifting , currentArray insert normal
			// position decreased , currentArray insert positon reduce 1
			config.currentArray.splice(config.currentPosi, 1);
			if (config.currentPosi - config.targetPosi >= 0) {
				config.currentArray.splice(config.targetPosi, 0, currentObj);
			} else {
				config.currentArray.splice(config.targetPosi - 1, 0, currentObj);
			}
		}
		return Tree;
	})();
	$.fn.treetable = function(method) {
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		}
		if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		}
		return $.error('Method' + method + 'does not exist on jQuery.treetable');

	};
	this.TreeTable || (this.TreeTable = {});
	this.TreeTable.Node = Node;
	this.TreeTable.Tree = Tree;
});