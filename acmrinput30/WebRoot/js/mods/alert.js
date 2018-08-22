define(function(require, exports, module) {
	var $ = require('jquery'),ui;
    ui = window.ui || {
        success: function(message, error) {
            var style = error ? (error === 1 ? "iBox iError" : "iBox iAlert") : "iBox";
            var html = '<div id="messBox" style="display:none">' + '<div class="iCon" id="messContent">&nbsp;</div></div>';
            var init = 0;

            var showMessage = function(message) {
                if (!init) {
                    $('body').append(html);
                    init = 1;
                }

                $('#messContent').html(message);
                $('#messBox').attr('class', style);

                var v = ui.box._viewport();

                $('<div class="iBlackout"></div>')
                    .css($.extend(ui.box._cssForOverlay(), {
                        zIndex: 9998,
                        opacity: 0.4
                    })).appendTo(document.body);


                $('#messBox').css({
                    left: (v.left + v.width / 2 - $('#messBox').outerWidth() / 2) + "px",
                    top: (v.top + v.height / 2 - $('#messBox').outerHeight() / 2) + "px"
                });

                $('#messBox').fadeIn("slow");
            }


            var closeMessage = function() {
                setTimeout(function() {
                    $('#messBox').fadeOut("fast", function() {
                        $('.iBlackout').remove();
                        $(this).remove();
                    });
                }, 1500);
            }

            showMessage(message);
            closeMessage();

        },

        alert: function(message) {
            ui.success(message, 2);
        },

        error: function(message) {
            ui.success(message, 1);
        }

    }

    $.extend(ui, {
        box: function(element, options) {}
    });

    $.extend(ui.box, {
        _viewport: function() {
            var d = document.documentElement,
                b = document.body,
                w = window;
            return $.extend(
                $.browser.msie ? {
                    left: b.scrollLeft || d.scrollLeft,
                    top: b.scrollTop || d.scrollTop
                } : {
                    left: w.pageXOffset,
                    top: w.pageYOffset
                }, !ui.box._u(w.innerWidth) ? {
                    width: w.innerWidth,
                    height: w.innerHeight
                } :
                (!ui.box._u(d) && !ui.box._u(d.clientWidth) && d.clientWidth != 0 ? {
                    width: d.clientWidth,
                    height: d.clientHeight
                } : {
                    width: b.clientWidth,
                    height: b.clientHeight
                }));
        },

        _u: function() {
            for (var i = 0; i < arguments.length; i++)
                if (typeof arguments[i] != 'undefined') return false;
            return true;
        },

        _cssForOverlay: function() {
            if (ui.box.IE6) {
                return ui.box._viewport();
            } else {
                return {
                    width: '100%',
                    height: $(document).height()
                };
            }
        }

    });
    module.exports = ui;
});