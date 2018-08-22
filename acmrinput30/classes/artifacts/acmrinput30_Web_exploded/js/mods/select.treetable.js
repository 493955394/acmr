define(function(require, exports, module) {
	'use strict';
	var $ = require('jquery'),
		checktreetable = require('checktreetable'),
		Selecttreetable,
		$doc = $(document);
	Selecttreetable = function(cfg) {
		this.config = {
			allBtn: cfg.allBtn || '',
			singleBtn: cfg.singleBtn || '',
			oprArray: cfg.oprArray,
			randomID: cfg.randomID
		}
		this.init();
	}
	Selecttreetable.prototype = {
		constructor: Selecttreetable,
		init: function() {
			var self = this,
				checkState;
			//targetCKs, checkState, oprArray
			$(self.config.singleBtn).off().on('click', function() {
				checkState = $(this).prop('checked');
				checktreetable.oprAll(self.config.allBtn, checkState, self.config.oprArray);
			});
			//targetCheckbox, checkState, oprArray
			$(self.config.allBtn).off().on('click', function() {
				
				var treetableObj = $('#' + self.config.randomID).data('treetable');
				
				var currentId = $(this).val();
				checkState = $(this).prop('checked');
				
				execution(currentId, checkState);
				
				function execution(currentId, currentStatus) {
					var children,
						len;
					children = treetableObj["tree"][currentId].children || [];
					len = children.length || 0;
					checktreetable.oprOnce('input[type="checkbox"][value=' + currentId + ']', checkState, self.config.oprArray);
					for (var i = len - 1; i >= 0; i--) {
						// var itemObj = treetableObj["tree"][children[i].id]
						execution(children[i].id, currentStatus)
					};
				}
			})
		}
	}
	module.exports = Selecttreetable;
});