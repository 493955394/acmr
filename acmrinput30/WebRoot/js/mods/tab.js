define(function(require, exports, module) {
  'use strict';

  var $ = require('jquery'),
  Tab = function(element) {
    this.element = $(element)
  }

  Tab.VERSION = '3.2.0'

  Tab.TRANSITION_DURATION = 150

  Tab.prototype.show = function() {
    var $this = this.element
    var $ul = $this.closest('ul:not(.dropdown-menu)')
    var selector = $this.data('target')

    if (!selector) {
      selector = $this.attr('href')
      selector = selector && selector.replace(/.*(?=#[^\s]*$)/, '') // strip for ie7
    }

    if ($this.parent('li').hasClass('active')) return

    var previous = $ul.find('.active:last a')[0]
    var e = $.Event('show.bs.tab', {
      relatedTarget: previous
    })

    $this.trigger(e)

    if (e.isDefaultPrevented()) return

    var $target = $(selector)

    this.activate($this.closest('li'), $ul)
    this.activate($target, $target.parent(), function() {
      $this.trigger({
        type: 'shown.bs.tab',
        relatedTarget: previous
      })
    })
  }

  Tab.prototype.activate = function(element, container, callback) {
    var $active = container.find('> .active')
    var transition = callback && $.support.transition && (($active.length && $active.hasClass('fade')) || !!container.find('> .fade').length)

    function next() {
      $active
        .removeClass('active')
        .find('> .dropdown-menu > .active')
        .removeClass('active')

      element.addClass('active')

      if (transition) {
        element[0].offsetWidth // reflow for transition
        element.addClass('in')
      } else {
        element.removeClass('fade')
      }

      if (element.parent('.dropdown-menu')) {
        element.closest('li.dropdown').addClass('active')
      }

      callback && callback()
    }

    $active.length && transition ?
      $active
      .one('bsTransitionEnd', next)
      .emulateTransitionEnd(Tab.TRANSITION_DURATION) :
      next()

    $active.removeClass('in')
  }


  // TAB PLUGIN DEFINITION
  // =====================

  function Plugin(option) {
    return this.each(function() {
      var $this = $(this)
      var data = $this.data('bs.tab')

      if (!data) $this.data('bs.tab', (data = new Tab(this)))
      if (typeof option == 'string') data[option]()
    })
  }

  var old = $.fn.tab

  $.fn.tab = Plugin
  $.fn.tab.Constructor = Tab


  // TAB NO CONFLICT
  // ===============

  $.fn.tab.noConflict = function() {
    $.fn.tab = old
    return this
  }


  // TAB DATA-API
  // ============

  $(document).on('click.bs.tab.data-api', '[data-toggle="tab"], [data-toggle="pill"]', function(e) {
    e.preventDefault()
    Plugin.call($(this), 'show')
  })
  

	require('cookie');

	/*菜单cookie*/
	if( $.cookie("tabs") ){
		$("#nav-tabs a[href='#"+ $.cookie("tabs") +"']").parent().addClass("active");
		$("#"+ $.cookie("tabs")).addClass("active");
		
	}else{
		$("#nav-tabs-box .tab-pane").eq(0).addClass("active");
		$("#nav-tabs a[data-toggle='tab']").eq(0).parent().addClass("active");
	}
	$("#nav-tabs a[data-toggle='tab']").click(function() {
		var cookiestr = $(this).attr("tabid");
		$.cookie("tabs",cookiestr);
	});

});