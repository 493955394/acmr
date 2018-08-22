 define(function(require, exports, module) {
    var $ = require('$');
/*!
 * # Semantic UI 2.1.6 - Dropdown
 * http://github.com/semantic-org/semantic-ui/
 *
 *
 * Copyright 2015 Contributors
 * Released under the MIT license
 * http://opensource.org/licenses/MIT
 *
 */

;
(function($, window, document, undefined) {

  "use strict";

  $.fn.uidropdown = function(parameters) {
    var
      $allModules = $(this),
      $document = $(document),

      moduleSelector = $allModules.selector || '',

      hasTouch = ('ontouchstart' in document.documentElement),
      time = new Date().getTime(),
      performance = [],

      query = arguments[0],
      methodInvoked = (typeof query == 'string'),
      queryArguments = [].slice.call(arguments, 1),
      returnedValue;

    $allModules
      .each(function(elementIndex) {
        var
          settings = ($.isPlainObject(parameters)) ? $.extend(true, {}, $.fn.uidropdown.settings, parameters) : $.extend({}, $.fn.uidropdown.settings),

          className = settings.className,
          message = settings.message,
          fields = settings.fields,
          keys = settings.keys,
          metadata = settings.metadata,
          namespace = settings.namespace,
          regExp = settings.regExp,
          selector = settings.selector,
          error = settings.error,
          templates = settings.templates,

          eventNamespace = '.' + namespace,
          moduleNamespace = 'module-' + namespace,

          $module = $(this),
          $context = $(settings.context),
          $text = $module.find(selector.text),
          $search = $module.find(selector.search),
          $input = $module.find(selector.input),
          $icon = $module.find(selector.icon),

          $combo = ($module.prev().find(selector.text).length > 0) ? $module.prev().find(selector.text) : $module.prev(),

          $menu = $module.children(selector.menu),
          $item = $menu.find(selector.item),

          activated = false,
          itemActivated = false,
          internalChange = false,
          element = this,
          instance = $module.data(moduleNamespace),

          initialLoad,
          pageLostFocus,
          elementNamespace,
          id,
          selectObserver,
          menuObserver,
          module;

        module = {

          initialize: function() {
            module.debug('Initializing dropdown', settings);

            if (module.is.alreadySetup()) {
              module.setup.reference();
            } else {
              module.setup.layout();

              module.refreshData();

              module.save.defaults();
              module.restore.selected();

              module.create.id();
              module.bind.events();

              module.observeChanges();
              module.instantiate();
            }

          },

          instantiate: function() {
            module.verbose('Storing instance of dropdown', module);
            instance = module;
            $module
              .data(moduleNamespace, module);
          },

          destroy: function() {
            module.verbose('Destroying previous dropdown', $module);
            module.remove.tabbable();
            $module
              .off(eventNamespace)
              .removeData(moduleNamespace);
            $menu
              .off(eventNamespace);
            $document
              .off(elementNamespace);
            if (selectObserver) {
              selectObserver.disconnect();
            }
            if (menuObserver) {
              menuObserver.disconnect();
            }
          },

          observeChanges: function() {
            if ('MutationObserver' in window) {
              selectObserver = new MutationObserver(function(mutations) {
                module.debug('<select> modified, recreating menu');
                module.setup.select();
              });
              menuObserver = new MutationObserver(function(mutations) {
                module.debug('Menu modified, updating selector cache');
                module.refresh();
              });
              if (module.has.input()) {
                selectObserver.observe($input[0], {
                  childList: true,
                  subtree: true
                });
              }
              if (module.has.menu()) {
                menuObserver.observe($menu[0], {
                  childList: true,
                  subtree: true
                });
              }
              module.debug('Setting up mutation observer', selectObserver, menuObserver);
            }
          },

          create: {
            id: function() {
              id = (Math.random().toString(16) + '000000000').substr(2, 8);
              elementNamespace = '.' + id;
              module.verbose('Creating unique id for element', id);
            },
            userChoice: function(values) {
              var
                $userChoices,
                $userChoice,
                isUserValue,
                html;
              values = values || module.get.userValues();
              if (!values) {
                return false;
              }
              values = $.isArray(values) ? values : [values];
              $.each(values, function(index, value) {
                if (module.get.item(value) === false) {
                  html = settings.templates.addition(module.add.variables(message.addResult, value));
                  $userChoice = $('<div />')
                    .html(html)
                    .attr('data-' + metadata.value, value)
                    .attr('data-' + metadata.text, value)
                    .addClass(className.addition)
                    .addClass(className.item);
                  $userChoices = ($userChoices === undefined) ? $userChoice : $userChoices.add($userChoice);
                  module.verbose('Creating user choices for value', value, $userChoice);
                }
              });
              return $userChoices;
            },
            userLabels: function(value) {
              var
                userValues = module.get.userValues();
              if (userValues) {
                module.debug('Adding user labels', userValues);
                $.each(userValues, function(index, value) {
                  module.verbose('Adding custom user value');
                  module.add.label(value, value);
                });
              }
            },
            menu: function() {
              $menu = $('<div />')
                .addClass(className.menu)
                .appendTo($module);
            }
          },

          search: function(query) {
            query = (query !== undefined) ? query : module.get.query();
            module.verbose('Searching for query', query);
            module.filter(query);
          },

          select: {
            firstUnfiltered: function() {
              module.verbose('Selecting first non-filtered element');
              module.remove.selectedItem();
              $item
                .not(selector.unselectable)
                .eq(0)
                .addClass(className.selected);
            },
            nextAvailable: function($selected) {
              $selected = $selected.eq(0);
              var
                $nextAvailable = $selected.nextAll(selector.item).not(selector.unselectable).eq(0),
                $prevAvailable = $selected.prevAll(selector.item).not(selector.unselectable).eq(0),
                hasNext = ($nextAvailable.length > 0);
              if (hasNext) {
                module.verbose('Moving selection to', $nextAvailable);
                $nextAvailable.addClass(className.selected);
              } else {
                module.verbose('Moving selection to', $prevAvailable);
                $prevAvailable.addClass(className.selected);
              }
            }
          },

          setup: {
            api: function() {
              var
                apiSettings = {
                  debug: settings.debug,
                  on: false
                };
              module.verbose('First request, initializing API');
              $module
                .api(apiSettings);
            },
            layout: function() {
              if ($module.is('select')) {
                module.setup.select();
                module.setup.returnedObject();
              }
              if (!module.has.menu()) {
                module.create.menu();
              }
              if (module.is.search() && !module.has.search()) {
                module.verbose('Adding search input');
                $search = $('<input />')
                  .addClass(className.search)
                  .prop('autocomplete', 'off')
                  .insertBefore($text);
              }
              if (settings.allowTab) {
                module.set.tabbable();
              }
            },
            select: function() {
              var
                selectValues = module.get.selectValues();
              module.debug('Dropdown initialized on a select', selectValues);
              if ($module.is('select')) {
                $input = $module;
              }
              // see if select is placed correctly already
              if ($input.parent(selector.dropdown).length > 0) {
                module.debug('UI dropdown already exists. Creating dropdown menu only');
                $module = $input.closest(selector.dropdown);
                if (!module.has.menu()) {
                  module.create.menu();
                }
                $menu = $module.children(selector.menu);
                module.setup.menu(selectValues);
              } else {
                module.debug('Creating entire dropdown from select');
                $module = $('<div />')
                  .attr('class', $input.attr('class'))
                  .addClass(className.selection)
                  .addClass(className.dropdown)
                  .html(templates.dropdown(selectValues))
                  .insertBefore($input);
                if ($input.hasClass(className.multiple) && $input.prop('multiple') === false) {
                  module.error(error.missingMultiple);
                  $input.prop('multiple', true);
                }
                if ($input.is('[multiple]')) {
                  module.set.multiple();
                }
                if ($input.prop('disabled')) {
                  module.debug('Disabling dropdown');
                  $module.addClass(className.disabled);
                }
                $input
                  .removeAttr('class')
                  .detach()
                  .prependTo($module);
              }
              module.refresh();
            },
            menu: function(values) {
              var $searchMenu = $(selector.searchMenu);
              $searchMenu.html(templates.menu(values, fields));
              $searchMenu.transition('show');
              $item = $searchMenu.find(selector.item);
            },
            reference: function() {
              module.debug('Dropdown behavior was called on select, replacing with closest dropdown');
              // replace module reference
              $module = $module.parent(selector.dropdown);
              module.refresh();
              module.setup.returnedObject();
              // invoke method in context of current instance
              if (methodInvoked) {
                instance = module;
                module.invoke(query);
              }
            },
            returnedObject: function() {
              var
                $firstModules = $allModules.slice(0, elementIndex),
                $lastModules = $allModules.slice(elementIndex + 1);
              // adjust all modules to use correct reference
              $allModules = $firstModules.add($module).add($lastModules);
            }
          },

          refresh: function() {
            module.refreshSelectors();
            module.refreshData();
          },

          refreshSelectors: function() {
            module.verbose('Refreshing selector cache');
            $text = $module.find(selector.text);
            $search = $module.find(selector.search);
            $input = $module.find(selector.input);
            $icon = $module.find(selector.icon);
            $combo = ($module.prev().find(selector.text).length > 0) ? $module.prev().find(selector.text) : $module.prev();
            $menu = $module.children(selector.menu);
            $item = $menu.find(selector.item);
          },

          refreshData: function() {
            module.verbose('Refreshing cached metadata');
            $item
              .removeData(metadata.text)
              .removeData(metadata.value);
            $module
              .removeData(metadata.defaultText)
              .removeData(metadata.defaultValue)
              .removeData(metadata.placeholderText);
          },

          toggle: function() {
            module.verbose('Toggling menu visibility');
            if (!module.is.active()) {
              module.show();
            } else {
              module.hide();
            }
          },

          show: function(callback) {
            callback = $.isFunction(callback) ? callback : function() {};
            if (module.can.show() && !module.is.active()) {
              module.debug('Showing dropdown');
              if (module.is.multiple() && !module.has.search() && module.is.allFiltered()) {
                return true;
              }
              if (module.has.message() && !(module.has.maxSelections() || module.has.allResultsFiltered())) {
                module.remove.message();
              }
              if (settings.onShow.call(element) !== false) {
                module.animate.show(function() {
                  if (module.can.click()) {
                    module.bind.intent();
                  }
                  module.set.visible();
                  callback.call(element);
                });
              }
            }
          },

          hide: function(callback) {
            callback = $.isFunction(callback) ? callback : function() {};
            if (module.is.active()) {
              module.debug('Hiding dropdown');
              if (settings.onHide.call(element) !== false) {
                module.animate.hide(function() {
                  module.remove.visible();
                  callback.call(element);
                });
              }
            }
          },

          hideOthers: function() {
            module.verbose('Finding other dropdowns to hide');
            $allModules
              .not($module)
              .has(selector.menu + '.' + className.visible)
              .uidropdown('hide');
          },

          hideMenu: function() {
            module.verbose('Hiding menu  instantaneously');
            module.remove.active();
            module.remove.visible();
            $menu.transition('hide');
          },
          hideSubMenus: function() {
            var
              $subMenus = $menu.children(selector.item).find(selector.menu);
            module.verbose('Hiding sub menus', $subMenus);
            $subMenus.transition('hide');
          },
          hideTreeMenu: function() {
            var $treeMenu = $(selector.treeMenu);
            $treeMenu.transition('hide');
          },
          bind: {
            events: function() {
              if (hasTouch) {
                module.bind.touchEvents();
              }
              module.bind.keyboardEvents();
              module.bind.inputEvents();
              module.bind.mouseEvents();
            },
            touchEvents: function() {
              module.debug('Touch device detected binding additional touch events');
              if (module.is.searchSelection()) {
                // do nothing special yet
              } else if (module.is.single()) {
                $module
                  .on('touchstart' + eventNamespace, module.event.test.toggle);
              }
              $menu
                .on('touchstart' + eventNamespace, selector.item, module.event.item.mouseenter);
            },
            keyboardEvents: function() {
              module.verbose('Binding keyboard events');
              $module
                .on('keydown' + eventNamespace, module.event.keydown);
              if (module.has.search()) {
                $module
                  .on(module.get.inputEvent() + eventNamespace, selector.search, module.event.input);
              }
              if (module.is.multiple()) {
                $document
                  .on('keydown' + elementNamespace, module.event.document.keydown);
              }
            },
            inputEvents: function() {
              module.verbose('Binding input change events');
              $module
                .on('change' + eventNamespace, selector.input, module.event.change);
            },
            mouseEvents: function() {
              module.verbose('Binding mouse events');
              if (module.is.multiple()) {
                $module
                  .on('click' + eventNamespace, selector.label, module.event.label.click)
                  .on('click' + eventNamespace, selector.remove, module.event.remove.click);
              }
              if (module.is.searchSelection()) {
                $module
                  .on('mousedown' + eventNamespace, selector.menu, module.event.menu.mousedown)
                  .on('mouseup' + eventNamespace, selector.menu, module.event.menu.mouseup)
                  .on('click' + eventNamespace, selector.icon, module.event.icon.click)
                  .on('click' + eventNamespace, selector.search, module.show)
                  .on('focus' + eventNamespace, selector.search, module.event.search.focus)
                  .on('blur' + eventNamespace, selector.search, module.event.search.blur)
                  .on('click' + eventNamespace, selector.text, module.event.text.focus);
                if (module.is.multiple()) {
                  $module
                    .on('click' + eventNamespace, module.event.click);
                }
              } else {
                if (settings.on == 'click') {
                  $module
                    .on('click' + eventNamespace, selector.icon, module.event.icon.click)
                    .on('click' + eventNamespace, module.event.test.toggle);
                } else if (settings.on == 'hover') {
                  $module
                    .on('mouseenter' + eventNamespace, module.delay.show)
                    .on('mouseleave' + eventNamespace, module.delay.hide);
                } else {
                  $module
                    .on(settings.on + eventNamespace, module.toggle);
                }
                $module
                  .on('mousedown' + eventNamespace, module.event.mousedown)
                  .on('mouseup' + eventNamespace, module.event.mouseup)
                  .on('focus' + eventNamespace, module.event.focus)
                  .on('blur' + eventNamespace, module.event.blur);
              }
              $menu
                .on('mouseenter' + eventNamespace, selector.item, module.event.item.mouseenter)
                .on('mouseleave' + eventNamespace, selector.item, module.event.item.mouseleave)
                .on('click' + eventNamespace, selector.item, module.event.item.click)
                .on('click' + eventNamespace, selector.located, module.event.located.click)
                .on('mouseenter' + eventNamespace, selector.located, module.event.located.mouseenter)
                .on('mouseleave' + eventNamespace, selector.located, module.event.located.mouseleave);
            },
            intent: function() {
              module.verbose('Binding hide intent event to document');
              if (hasTouch) {
                $document
                  .on('touchstart' + elementNamespace, module.event.test.touch)
                  .on('touchmove' + elementNamespace, module.event.test.touch);
              }
              $document
                .on('click' + elementNamespace, module.event.test.hide);
            }
          },

          unbind: {
            intent: function() {
              module.verbose('Removing hide intent event from document');
              if (hasTouch) {
                $document
                  .off('touchstart' + elementNamespace)
                  .off('touchmove' + elementNamespace);
              }
              $document
                .off('click' + elementNamespace);
            }
          },

          filter: function(query) {
            var
              searchTerm = (query !== undefined) ? query : module.get.query(),
              afterFiltered = function() {
                if (module.is.multiple()) {
                  module.filterActive();
                }
                module.select.firstUnfiltered();
                if (module.has.allResultsFiltered()) {
                  if (settings.onNoResults.call(element, searchTerm)) {
                    if (!settings.allowAdditions) {
                      module.verbose('All items filtered, showing message', searchTerm);
                      module.add.message(message.noResults);
                    }
                  } else {
                    module.verbose('All items filtered, hiding dropdown', searchTerm);
                    module.hideMenu();
                  }
                } else {
                  module.remove.message();
                }
                if (settings.allowAdditions) {
                  module.add.userSuggestion(query);
                }
                if (module.is.searchSelection() && module.can.show() && module.is.focusedOnSearch()) {
                  module.show();
                }
              };
            if (settings.useLabels && module.has.maxSelections()) {
              return;
            }
            if (settings.apiSettings) {
              if (module.can.useAPI()) {
                if (searchTerm) {
                  module.queryRemote(searchTerm, function() {
                    afterFiltered();
                  });
                } else {
                  var $searchMenu = $(selector.searchMenu),
                    $treeMenu = $(selector.treeMenu);
                  if (module.has.message()) {
                    module.remove.message();
                  }
                  $searchMenu.transition('hide');
                  $menu.transition('show');
                  $treeMenu.transition('show');
                }
              } else {
                module.error(error.noAPI);
              }
            } else {
              module.filterItems(searchTerm);
              afterFiltered();
            }
          },

          queryRemote: function(query, callback) {
            var
              apiSettings = {
                errorDuration: false,
                throttle: settings.throttle,
                urlData: {
                  query: query
                },
                onError: function() {
                  module.add.message(message.serverError);
                  callback();
                },
                onFailure: function() {
                  module.add.message(message.serverError);
                  callback();
                },
                onSuccess: function(response) {
                  module.remove.message();
                  module.setup.menu({
                    values: response[fields.remoteValues]
                  });
                  module.hideTreeMenu();
                  callback();
                }
              };
            if (!$module.api('get request')) {
              module.setup.api();
            }
            apiSettings = $.extend(true, {}, apiSettings, settings.apiSettings);
            $module
              .api('setting', apiSettings)
              .api('query', query);
          },

          filterItems: function(query) {
            var
              searchTerm = (query !== undefined) ? query : module.get.query(),
              results = null,
              escapedTerm = module.escape.regExp(searchTerm),
              beginsWithRegExp = new RegExp('^' + escapedTerm, 'igm');
            // avoid loop if we're matching nothing
            if (module.has.query()) {
              results = [];

              module.verbose('Searching for matching values', searchTerm);
              $item
                .each(function() {
                  var
                    $choice = $(this),
                    text,
                    value;
                  if (settings.match == 'both' || settings.match == 'text') {
                    text = String(module.get.choiceText($choice, false));
                    if (text.search(beginsWithRegExp) !== -1) {
                      results.push(this);
                      return true;
                    } else if (settings.fullTextSearch && module.fuzzySearch(searchTerm, text)) {
                      results.push(this);
                      return true;
                    }
                  }
                  if (settings.match == 'both' || settings.match == 'value') {
                    value = String(module.get.choiceValue($choice, text));

                    if (value.search(beginsWithRegExp) !== -1) {
                      results.push(this);
                      return true;
                    } else if (settings.fullTextSearch && module.fuzzySearch(searchTerm, value)) {
                      results.push(this);
                      return true;
                    }
                  }
                });
            }
            module.debug('Showing only matched items', searchTerm);
            module.remove.filteredItem();
            if (results) {
              $item
                .not(results)
                .addClass(className.filtered);
            }
          },

          fuzzySearch: function(query, term) {
            var
              termLength = term.length,
              queryLength = query.length;
            query = query;
            term = term;
            if (queryLength > termLength) {
              return false;
            }
            if (queryLength === termLength) {
              return (query === term);
            }
            search: for (var characterIndex = 0, nextCharacterIndex = 0; characterIndex < queryLength; characterIndex++) {
              var
                queryCharacter = query.charCodeAt(characterIndex);
              while (nextCharacterIndex < termLength) {
                if (term.charCodeAt(nextCharacterIndex++) === queryCharacter) {
                  continue search;
                }
              }
              return false;
            }
            return true;
          },

          filterActive: function() {
            if (settings.useLabels) {
              $item.filter('.' + className.active)
                .addClass(className.filtered);
            }
          },

          focusSearch: function() {
            if (module.is.search() && !module.is.focusedOnSearch()) {
              $search[0].focus();
            }
          },

          forceSelection: function() {
            var
              $currentlySelected = $item.not(className.filtered).filter('.' + className.selected).eq(0),
              $activeItem = $item.not(className.filtered).filter('.' + className.active).eq(0),
              $selectedItem = ($currentlySelected.length > 0) ? $currentlySelected : $activeItem,
              hasSelected = ($selectedItem.size() > 0);
            if (module.has.query()) {
              if (hasSelected) {
                module.debug('Forcing partial selection to selected item', $selectedItem);
                module.event.item.click.call($selectedItem);
                return;
              } else {
                module.remove.searchTerm();
              }
            }
            module.hide();
          },

          event: {
            change: function() {
              if (!internalChange) {
                module.debug('Input changed, updating selection');
                module.set.selected();
              }
            },
            focus: function() {
              if (settings.showOnFocus && !activated && module.is.hidden() && !pageLostFocus) {
                module.show();
              }
            },
            click: function(event) {
              var
                $target = $(event.target);
              // focus search
              if ($target.is($module) && !module.is.focusedOnSearch()) {
                module.focusSearch();
              }
            },
            blur: function(event) {
              pageLostFocus = (document.activeElement === this);
              if (!activated && !pageLostFocus) {
                module.remove.activeLabel();
                module.hide();
              }
            },
            // prevents focus callback from occurring on mousedown
            mousedown: function() {
              activated = true;
            },
            mouseup: function() {
              activated = false;
            },
            search: {
              focus: function() {
                activated = true;
                if (module.is.multiple()) {
                  module.remove.activeLabel();
                }
                if (settings.showOnFocus) {
                  module.search();
                  module.show();
                }
              },
              blur: function(event) {
                pageLostFocus = (document.activeElement === this);
                if (!itemActivated && !pageLostFocus) {
                  if (module.is.multiple()) {
                    module.remove.activeLabel();
                    module.hide();
                  } else if (settings.forceSelection) {
                    module.forceSelection();
                  } else {
                    module.hide();
                  }
                } else if (pageLostFocus) {
                  if (settings.forceSelection) {
                    module.forceSelection();
                  }
                }
              }
            },
            icon: {
              click: function(event) {
                module.toggle();
                event.stopPropagation();
              }
            },
            text: {
              focus: function(event) {
                activated = true;
                module.focusSearch();
              }
            },
            input: function(event) {
              if (module.is.multiple() || module.is.searchSelection()) {
                module.set.filtered();
              }
              clearTimeout(module.timer);
              module.timer = setTimeout(module.search, settings.delay.search);
            },
            label: {
              click: function(event) {
                var
                  $label = $(this),
                  $labels = $module.find(selector.label),
                  $activeLabels = $labels.filter('.' + className.active),
                  $nextActive = $label.nextAll('.' + className.active),
                  $prevActive = $label.prevAll('.' + className.active),
                  $range = ($nextActive.length > 0) ? $label.nextUntil($nextActive).add($activeLabels).add($label) : $label.prevUntil($prevActive).add($activeLabels).add($label);
                if (event.shiftKey) {
                  $activeLabels.removeClass(className.active);
                  $range.addClass(className.active);
                } else if (event.ctrlKey) {
                  $label.toggleClass(className.active);
                } else {
                  $activeLabels.removeClass(className.active);
                  $label.addClass(className.active);
                }
                settings.onLabelSelect.apply(this, $labels.filter('.' + className.active));
              }
            },
            remove: {
              click: function() {
                var
                  $label = $(this).parent();
                if ($label.hasClass(className.active)) {
                  // remove all selected labels
                  module.remove.activeLabels();
                } else {
                  // remove this label only
                  module.remove.activeLabels($label);
                }
              }
            },
            test: {
              toggle: function(event) {
                var
                  toggleBehavior = (module.is.multiple()) ? module.show : module.toggle;
                if (module.determine.eventOnElement(event, toggleBehavior)) {
                  event.preventDefault();
                }
              },
              touch: function(event) {
                module.determine.eventOnElement(event, function() {
                  if (event.type == 'touchstart') {
                    module.timer = setTimeout(function() {
                      module.hide();
                    }, settings.delay.touch);
                  } else if (event.type == 'touchmove') {
                    clearTimeout(module.timer);
                  }
                });
                event.stopPropagation();
              },
              hide: function(event) {
                module.determine.eventInModule(event, module.hide);
              }
            },
            menu: {
              mousedown: function() {
                itemActivated = true;
              },
              mouseup: function() {
                itemActivated = false;
              }
            },
            item: {
              mouseenter: function(event) {
                var
                  $subMenu = $(this).children(selector.menu),
                  $otherMenus = $(this).siblings(selector.item).children(selector.menu);
                if ($subMenu.length > 0) {
                  clearTimeout(module.itemTimer);
                  module.itemTimer = setTimeout(function() {
                    module.verbose('Showing sub-menu', $subMenu);
                    $.each($otherMenus, function() {
                      module.animate.hide(false, $(this));
                    });
                    module.animate.show(false, $subMenu);
                  }, settings.delay.show);
                  event.preventDefault();
                }
              },
              mouseleave: function(event) {
                var
                  $subMenu = $(this).children(selector.menu);
                if ($subMenu.length > 0) {
                  clearTimeout(module.itemTimer);
                  module.itemTimer = setTimeout(function() {
                    module.verbose('Hiding sub-menu', $subMenu);
                    module.animate.hide(false, $subMenu);
                  }, settings.delay.hide);
                }
              },
              touchend: function() {},
              click: function(event) {
                var
                  $choice = $(this),
                  $target = (event) ? $(event.target) : $(''),
                  $subMenu = $choice.find(selector.menu),
                  text = module.get.choiceText($choice),
                  value = module.get.choiceValue($choice, text),
                  hasSubMenu = ($subMenu.length > 0),
                  isBubbledEvent = ($subMenu.find($target).length > 0);
                if (!isBubbledEvent && (!hasSubMenu || settings.allowCategorySelection)) {
                  if (!settings.useLabels) {
                    module.remove.filteredItem();
                    module.remove.searchTerm();
                    module.set.scrollPosition($choice);
                  }
                  module.determine.selectAction.call(this, text, value);
                }
              }
            },
            located: {
              click: function(event) {
                var
                  $self = $(this),
                  query = $self.attr(fields.id),
                  apiSettings = {
                    errorDuration: false,
                    throttle: settings.throttle,
                    url: settings.locatedUrl + query,
                    onError: function() {
                      module.add.message(message.serverError);
                    },
                    onFailure: function() {
                      module.add.message(message.serverError);
                    },
                    onSuccess: function(response) {
                      // console.log(response);
                      var tree,
                        $searchMenu = $(selector.searchMenu),
                        $treeMenu = $(selector.treeMenu),
                        treeSetting = {
                          async: {
                            enable:true,
                            type:'get',
                            url: settings.searchTreeUrl,
                            autoParam:settings.param
                          },
                          data: {
                            simpleData: {
                              enable: true
                            }
                          },
                          callback: {
                            onClick: outArgTreeclick
                          }
                        };
                      $.fn.zTree.init($("#" + settings.treeWidget), treeSetting, response.results);
                      tree = $.fn.zTree.getZTreeObj(settings.treeWidget);
                      tree.selectNode(tree.getNodesByParam(fields.id, query)[0]);

                      function outArgTreeclick(event, treeId, treeNode) {
                        var currentNodeId, node;
                        currentNodeId = treeNode.tId + '_a';
                        node = $('#' + currentNodeId);
                        node.data('value', treeNode.datavalue);
                      }

                      if (module.has.message()) {
                        module.remove.message();
                      }
                      $searchMenu.transition('hide');
                      $menu.transition('show');
                      $treeMenu.transition('show');
                    }
                  };
                if (!$module.api('get request')) {
                  module.setup.api();
                }
                $module
                  .api('setting', apiSettings)
                  .api('query');
              },
              mouseenter: function() {
                $(this).css('color', '#990000');
              },
              mouseleave: function() {
                $(this).css('color', '');
              }
            },
            document: {
              // label selection should occur even when element has no focus
              keydown: function(event) {
                var
                  pressedKey = event.which,
                  isShortcutKey = module.is.inObject(pressedKey, keys);
                if (isShortcutKey) {
                  var
                    $label = $module.find(selector.label),
                    $activeLabel = $label.filter('.' + className.active),
                    activeValue = $activeLabel.data(metadata.value),
                    labelIndex = $label.index($activeLabel),
                    labelCount = $label.length,
                    hasActiveLabel = ($activeLabel.length > 0),
                    hasMultipleActive = ($activeLabel.length > 1),
                    isFirstLabel = (labelIndex === 0),
                    isLastLabel = (labelIndex + 1 == labelCount),
                    isSearch = module.is.searchSelection(),
                    isFocusedOnSearch = module.is.focusedOnSearch(),
                    isFocused = module.is.focused(),
                    caretAtStart = (isFocusedOnSearch && module.get.caretPosition() === 0),
                    $nextLabel;
                  if (isSearch && !hasActiveLabel && !isFocusedOnSearch) {
                    return;
                  }

                  if (pressedKey == keys.leftArrow) {
                    // activate previous label
                    if ((isFocused || caretAtStart) && !hasActiveLabel) {
                      module.verbose('Selecting previous label');
                      $label.last().addClass(className.active);
                    } else if (hasActiveLabel) {
                      if (!event.shiftKey) {
                        module.verbose('Selecting previous label');
                        $label.removeClass(className.active);
                      } else {
                        module.verbose('Adding previous label to selection');
                      }
                      if (isFirstLabel && !hasMultipleActive) {
                        $activeLabel.addClass(className.active);
                      } else {
                        $activeLabel.prev(selector.siblingLabel)
                          .addClass(className.active)
                          .end();
                      }
                      event.preventDefault();
                    }
                  } else if (pressedKey == keys.rightArrow) {
                    // activate first label
                    if (isFocused && !hasActiveLabel) {
                      $label.first().addClass(className.active);
                    }
                    // activate next label
                    if (hasActiveLabel) {
                      if (!event.shiftKey) {
                        module.verbose('Selecting next label');
                        $label.removeClass(className.active);
                      } else {
                        module.verbose('Adding next label to selection');
                      }
                      if (isLastLabel) {
                        if (isSearch) {
                          if (!isFocusedOnSearch) {
                            module.focusSearch();
                          } else {
                            $label.removeClass(className.active);
                          }
                        } else if (hasMultipleActive) {
                          $activeLabel.next(selector.siblingLabel).addClass(className.active);
                        } else {
                          $activeLabel.addClass(className.active);
                        }
                      } else {
                        $activeLabel.next(selector.siblingLabel).addClass(className.active);
                      }
                      event.preventDefault();
                    }
                  } else if (pressedKey == keys.deleteKey || pressedKey == keys.backspace) {
                    if (hasActiveLabel) {
                      module.verbose('Removing active labels');
                      if (isLastLabel) {
                        if (isSearch && !isFocusedOnSearch) {
                          module.focusSearch();
                        }
                      }
                      $activeLabel.last().next(selector.siblingLabel).addClass(className.active);
                      module.remove.activeLabels($activeLabel);
                      event.preventDefault();
                    } else if (caretAtStart && !hasActiveLabel && pressedKey == keys.backspace) {
                      module.verbose('Removing last label on input backspace');
                      $activeLabel = $label.last().addClass(className.active);
                      module.remove.activeLabels($activeLabel);
                    }
                  } else {
                    $activeLabel.removeClass(className.active);
                  }
                }
              }
            },

            keydown: function(event) {
              var
                pressedKey = event.which,
                isShortcutKey = module.is.inObject(pressedKey, keys);
              if (isShortcutKey) {
                var
                  $currentlySelected = $item.not(selector.unselectable).filter('.' + className.selected).eq(0),
                  $activeItem = $menu.children('.' + className.active).eq(0),
                  $selectedItem = ($currentlySelected.length > 0) ? $currentlySelected : $activeItem,
                  $visibleItems = ($selectedItem.length > 0) ? $selectedItem.siblings(':not(.' + className.filtered + ')').andSelf() : $menu.children(':not(.' + className.filtered + ')'),
                  $subMenu = $selectedItem.children(selector.menu),
                  $parentMenu = $selectedItem.closest(selector.menu),
                  inVisibleMenu = ($parentMenu.hasClass(className.visible) || $parentMenu.hasClass(className.animating) || $parentMenu.parent(selector.menu).length > 0),
                  hasSubMenu = ($subMenu.length > 0),
                  hasSelectedItem = ($selectedItem.length > 0),
                  selectedIsSelectable = ($selectedItem.not(selector.unselectable).length > 0),
                  delimiterPressed = (pressedKey == keys.delimiter && settings.allowAdditions && module.is.multiple()),
                  $nextItem,
                  isSubMenuItem,
                  newIndex;
                // visible menu keyboard shortcuts
                if (module.is.visible()) {

                  // enter (select or open sub-menu)
                  if (pressedKey == keys.enter || delimiterPressed) {
                    if (pressedKey == keys.enter && hasSelectedItem && hasSubMenu && !settings.allowCategorySelection) {
                      module.verbose('Pressed enter on unselectable category, opening sub menu');
                      pressedKey = keys.rightArrow;
                    } else if (selectedIsSelectable) {
                      module.verbose('Selecting item from keyboard shortcut', $selectedItem);
                      module.event.item.click.call($selectedItem, event);
                      if (module.is.searchSelection()) {
                        module.remove.searchTerm();
                      }
                    }
                    event.preventDefault();
                  }

                  // left arrow (hide sub-menu)
                  if (pressedKey == keys.leftArrow) {

                    isSubMenuItem = ($parentMenu[0] !== $menu[0]);

                    if (isSubMenuItem) {
                      module.verbose('Left key pressed, closing sub-menu');
                      module.animate.hide(false, $parentMenu);
                      $selectedItem
                        .removeClass(className.selected);
                      $parentMenu
                        .closest(selector.item)
                        .addClass(className.selected);
                      event.preventDefault();
                    }
                  }

                  // right arrow (show sub-menu)
                  if (pressedKey == keys.rightArrow) {
                    if (hasSubMenu) {
                      module.verbose('Right key pressed, opening sub-menu');
                      module.animate.show(false, $subMenu);
                      $selectedItem
                        .removeClass(className.selected);
                      $subMenu
                        .find(selector.item).eq(0)
                        .addClass(className.selected);
                      event.preventDefault();
                    }
                  }

                  // up arrow (traverse menu up)
                  if (pressedKey == keys.upArrow) {
                    $nextItem = (hasSelectedItem && inVisibleMenu) ? $selectedItem.prevAll(selector.item + ':not(' + selector.unselectable + ')').eq(0) : $item.eq(0);
                    if ($visibleItems.index($nextItem) < 0) {
                      module.verbose('Up key pressed but reached top of current menu');
                      event.preventDefault();
                      return;
                    } else {
                      module.verbose('Up key pressed, changing active item');
                      $selectedItem
                        .removeClass(className.selected);
                      $nextItem
                        .addClass(className.selected);
                      module.set.scrollPosition($nextItem);
                    }
                    event.preventDefault();
                  }

                  // down arrow (traverse menu down)
                  if (pressedKey == keys.downArrow) {
                    $nextItem = (hasSelectedItem && inVisibleMenu) ? $nextItem = $selectedItem.nextAll(selector.item + ':not(' + selector.unselectable + ')').eq(0) : $item.eq(0);
                    if ($nextItem.length === 0) {
                      module.verbose('Down key pressed but reached bottom of current menu');
                      event.preventDefault();
                      return;
                    } else {
                      module.verbose('Down key pressed, changing active item');
                      $item
                        .removeClass(className.selected);
                      $nextItem
                        .addClass(className.selected);
                      module.set.scrollPosition($nextItem);
                    }
                    event.preventDefault();
                  }

                  // page down (show next page)
                  if (pressedKey == keys.pageUp) {
                    module.scrollPage('up');
                    event.preventDefault();
                  }
                  if (pressedKey == keys.pageDown) {
                    module.scrollPage('down');
                    event.preventDefault();
                  }

                  // escape (close menu)
                  if (pressedKey == keys.escape) {
                    module.verbose('Escape key pressed, closing dropdown');
                    module.hide();
                  }

                } else {
                  // delimiter key
                  if (delimiterPressed) {
                    event.preventDefault();
                  }
                  // down arrow (open menu)
                  if (pressedKey == keys.downArrow) {
                    module.verbose('Down key pressed, showing dropdown');
                    module.show();
                    event.preventDefault();
                  }
                }
              } else {
                if (module.is.selection() && !module.is.search()) {
                  module.set.selectedLetter(String.fromCharCode(pressedKey));
                }
              }
            }
          },

          trigger: {
            change: function() {
              var
                events = document.createEvent('HTMLEvents'),
                inputElement = $input[0];
              if (inputElement) {
                module.verbose('Triggering native change event');
                events.initEvent('change', true, false);
                inputElement.dispatchEvent(events);
              }
            }
          },

          determine: {
            selectAction: function(text, value) {
              module.verbose('Determining action', settings.action);
              if ($.isFunction(module.action[settings.action])) {
                module.verbose('Triggering preset action', settings.action, text, value);
                module.action[settings.action].call(this, text, value);
              } else if ($.isFunction(settings.action)) {
                module.verbose('Triggering user action', settings.action, text, value);
                settings.action.call(this, text, value);
              } else {
                module.error(error.action, settings.action);
              }
            },
            eventInModule: function(event, callback) {
              var
                $target = $(event.target),
                inDocument = ($target.closest(document.documentElement).length > 0),
                inModule = ($target.closest($module).length > 0);
              callback = $.isFunction(callback) ? callback : function() {};
              if (inDocument && !inModule) {
                module.verbose('Triggering event', callback);
                callback();
                return true;
              } else {
                module.verbose('Event occurred in dropdown, canceling callback');
                return false;
              }
            },
            eventOnElement: function(event, callback) {
              var
                $target = $(event.target),
                $label = $target.closest(selector.siblingLabel),
                notOnLabel = ($module.find($label).length === 0),
                notInMenu = ($target.closest($menu).length === 0);
              callback = $.isFunction(callback) ? callback : function() {};
              if (notOnLabel && notInMenu) {
                module.verbose('Triggering event', callback);
                callback();
                return true;
              } else {
                module.verbose('Event occurred in dropdown menu, canceling callback');
                return false;
              }
            }
          },

          action: {

            nothing: function() {},

            activate: function(text, value) {
              value = (value !== undefined) ? value : text;
              if (module.can.activate($(this))) {
                module.set.selected(value, $(this));
                if (module.is.multiple() && !module.is.allFiltered()) {
                  return;
                } else {
                  module.hideAndClear();
                }
              }
            },

            select: function(text, value) {
              // mimics action.activate but does not select text
              module.action.activate.call(this);
            },

            combo: function(text, value) {
              value = (value !== undefined) ? value : text;
              module.set.selected(value, $(this));
              module.hideAndClear();
            },

            hide: function(text, value) {
              module.set.value(value);
              module.hideAndClear();
            }

          },

          get: {
            id: function() {
              return id;
            },
            defaultText: function() {
              return $module.data(metadata.defaultText);
            },
            defaultValue: function() {
              return $module.data(metadata.defaultValue);
            },
            placeholderText: function() {
              return $module.data(metadata.placeholderText) || '';
            },
            text: function() {
              return $text.text();
            },
            query: function() {
              return $.trim($search.val());
            },
            searchWidth: function(characterCount) {
              return (characterCount * settings.glyphWidth) + 'em';
            },
            selectionCount: function() {
              var
                values = module.get.values(),
                count;
              count = (module.is.multiple()) ? $.isArray(values) ? values.length : 0 : (module.get.value() !== '') ? 1 : 0;
              return count;
            },
            transition: function($subMenu) {
              return (settings.transition == 'auto') ? module.is.upward($subMenu) ? 'slide up' : 'slide down' : settings.transition;
            },
            userValues: function() {
              var
                values = module.get.values();
              if (!values) {
                return false;
              }
              values = $.isArray(values) ? values : [values];
              return $.grep(values, function(value) {
                return (module.get.item(value) === false);
              });
            },
            uniqueArray: function(array) {
              return $.grep(array, function(value, index) {
                return $.inArray(value, array) === index;
              });
            },
            caretPosition: function() {
              var
                input = $search.get(0),
                range,
                rangeLength;
              if ('selectionStart' in input) {
                return input.selectionStart;
              } else if (document.selection) {
                input.focus();
                range = document.selection.createRange();
                rangeLength = range.text.length;
                range.moveStart('character', -input.value.length);
                return range.text.length - rangeLength;
              }
            },
            value: function() {
              var
                value = ($input.length > 0) ? $input.val() : $module.data(metadata.value);
              // prevents placeholder element from being selected when multiple
              if ($.isArray(value) && value.length === 1 && value[0] === '') {
                return '';
              }
              return value;
            },
            values: function() {
              var
                value = module.get.value();
              if (value === '') {
                return '';
              }
              return (!module.has.selectInput() && module.is.multiple()) ? (typeof value == 'string') // delimited string
                ? value.split(settings.delimiter) : '' : value;
            },
            remoteValues: function() {
              var
                values = module.get.values(),
                remoteValues = false;
              if (values) {
                if (typeof values == 'string') {
                  values = [values];
                }
                remoteValues = {};
                $.each(values, function(index, value) {
                  var
                    name = module.read.remoteData(value);
                  module.verbose('Restoring value from session data', name, value);
                  remoteValues[value] = (name) ? name : value;
                });
              }
              return remoteValues;
            },
            choiceText: function($choice, preserveHTML) {
              preserveHTML = (preserveHTML !== undefined) ? preserveHTML : settings.preserveHTML;
              if ($choice) {
                if ($choice.find(selector.menu).length > 0) {
                  module.verbose('Retreiving text of element with sub-menu');
                  $choice = $choice.clone();
                  $choice.find(selector.menu).remove();
                  $choice.find(selector.menuIcon).remove();
                }
                return ($choice.data(metadata.text) !== undefined) ? $choice.data(metadata.text) : (preserveHTML) ? $.trim($choice.html()) : $.trim($choice.text());
              }
            },
            choiceValue: function($choice, choiceText) {
              choiceText = choiceText || module.get.choiceText($choice);
              if (!$choice) {
                return false;
              }
              return ($choice.data(metadata.value) !== undefined) ? String($choice.data(metadata.value)) : (typeof choiceText === 'string') ? $.trim(choiceText) : String(choiceText);
            },
            inputEvent: function() {
              var
                input = $search[0];
              if (input) {
                return (input.oninput !== undefined) ? 'input' : (input.onpropertychange !== undefined) ? 'propertychange' : 'keyup';
              }
              return false;
            },
            selectValues: function() {
              var
                select = {};
              select.values = [];
              $module
                .find('option')
                .each(function() {
                  var
                    $option = $(this),
                    name = $option.html(),
                    disabled = $option.attr('disabled'),
                    value = ($option.attr('value') !== undefined) ? $option.attr('value') : name;
                  if (settings.placeholder === 'auto' && value === '') {
                    select.placeholder = name;
                  } else {
                    select.values.push({
                      name: name,
                      value: value,
                      disabled: disabled
                    });
                  }
                });
              if (settings.placeholder && settings.placeholder !== 'auto') {
                module.debug('Setting placeholder value to', settings.placeholder);
                select.placeholder = settings.placeholder;
              }
              if (settings.sortSelect) {
                select.values.sort(function(a, b) {
                  return (a.name > b.name) ? 1 : -1;
                });
                module.debug('Retrieved and sorted values from select', select);
              } else {
                module.debug('Retreived values from select', select);
              }
              return select;
            },
            activeItem: function() {
              return $item.filter('.' + className.active);
            },
            selectedItem: function() {
              var
                $selectedItem = $item.not(selector.unselectable).filter('.' + className.selected);
              return ($selectedItem.length > 0) ? $selectedItem : $item.eq(0);
            },
            itemWithAdditions: function(value) {
              var
                $items = module.get.item(value),
                $userItems = module.create.userChoice(value),
                hasUserItems = ($userItems && $userItems.length > 0);
              if (hasUserItems) {
                $items = ($items.length > 0) ? $items.add($userItems) : $userItems;
              }
              return $items;
            },
            item: function(value, strict) {
              var
                $selectedItem = false,
                shouldSearch,
                isMultiple;
              value = (value !== undefined) ? value : (module.get.values() !== undefined) ? module.get.values() : module.get.text();
              shouldSearch = (isMultiple) ? (value.length > 0) : (value !== undefined && value !== null);
              isMultiple = (module.is.multiple() && $.isArray(value));
              strict = (value === '' || value === 0) ? true : strict || false;
              if (shouldSearch) {
                $item
                  .each(function() {
                    var
                      $choice = $(this),
                      optionText = module.get.choiceText($choice),
                      optionValue = module.get.choiceValue($choice, optionText);
                    // safe early exit
                    if (optionValue === null || optionValue === undefined) {
                      return;
                    }
                    if (isMultiple) {
                      if ($.inArray(String(optionValue), value) !== -1 || $.inArray(optionText, value) !== -1) {
                        $selectedItem = ($selectedItem) ? $selectedItem.add($choice) : $choice;
                      }
                    } else if (strict) {
                      module.verbose('Ambiguous dropdown value using strict type check', $choice, value);
                      if (optionValue === value || optionText === value) {
                        $selectedItem = $choice;
                        return true;
                      }
                    } else {
                      if (String(optionValue) == String(value) || optionText == value) {
                        module.verbose('Found select item by value', optionValue, value);
                        $selectedItem = $choice;
                        return true;
                      }
                    }
                  });
              }
              return $selectedItem;
            }
          },

          check: {
            maxSelections: function(selectionCount) {
              if (settings.maxSelections) {
                selectionCount = (selectionCount !== undefined) ? selectionCount : module.get.selectionCount();
                if (selectionCount >= settings.maxSelections) {
                  module.debug('Maximum selection count reached');
                  if (settings.useLabels) {
                    $item.addClass(className.filtered);
                    module.add.message(message.maxSelections);
                  }
                  return true;
                } else {
                  module.verbose('No longer at maximum selection count');
                  module.remove.message();
                  module.remove.filteredItem();
                  if (module.is.searchSelection()) {
                    module.filterItems();
                  }
                  return false;
                }
              }
              return true;
            }
          },

          restore: {
            defaults: function() {
              module.clear();
              module.restore.defaultText();
              module.restore.defaultValue();
            },
            defaultText: function() {
              var
                defaultText = module.get.defaultText(),
                placeholderText = module.get.placeholderText;
              if (defaultText === placeholderText) {
                module.debug('Restoring default placeholder text', defaultText);
                module.set.placeholderText(defaultText);
              } else {
                module.debug('Restoring default text', defaultText);
                module.set.text(defaultText);
              }
            },
            defaultValue: function() {
              var
                defaultValue = module.get.defaultValue();
              if (defaultValue !== undefined) {
                module.debug('Restoring default value', defaultValue);
                if (defaultValue !== '') {
                  module.set.value(defaultValue);
                  module.set.selected();
                } else {
                  module.remove.activeItem();
                  module.remove.selectedItem();
                }
              }
            },
            labels: function() {
              if (settings.allowAdditions) {
                if (!settings.useLabels) {
                  module.error(error.labels);
                  settings.useLabels = true;
                }
                module.debug('Restoring selected values');
                module.create.userLabels();
              }
              module.check.maxSelections();
            },
            selected: function() {
              module.restore.values();
              if (module.is.multiple()) {
                module.debug('Restoring previously selected values and labels');
                module.restore.labels();
              } else {
                module.debug('Restoring previously selected values');
              }
            },
            values: function() {
              // prevents callbacks from occuring on initial load
              module.set.initialLoad();
              if (settings.apiSettings) {
                if (settings.saveRemoteData) {
                  module.restore.remoteValues();
                } else {
                  module.clearValue();
                }
              } else {
                module.set.selected();
              }
              module.remove.initialLoad();
            },
            remoteValues: function() {
              var
                values = module.get.remoteValues();
              module.debug('Recreating selected from session data', values);
              if (values) {
                if (module.is.single()) {
                  $.each(values, function(value, name) {
                    module.set.text(name);
                  });
                } else {
                  $.each(values, function(value, name) {
                    module.add.label(value, name);
                  });
                }
              }
            }
          },

          read: {
            remoteData: function(value) {
              var
                name;
              if (window.Storage === undefined) {
                module.error(error.noStorage);
                return;
              }
              name = sessionStorage.getItem(value);
              return (name !== undefined) ? name : false;
            }
          },

          save: {
            defaults: function() {
              module.save.defaultText();
              module.save.placeholderText();
              module.save.defaultValue();
            },
            defaultValue: function() {
              var
                value = module.get.value();
              module.verbose('Saving default value as', value);
              $module.data(metadata.defaultValue, value);
            },
            defaultText: function() {
              var
                text = module.get.text();
              module.verbose('Saving default text as', text);
              $module.data(metadata.defaultText, text);
            },
            placeholderText: function() {
              var
                text;
              if (settings.placeholder !== false && $text.hasClass(className.placeholder)) {
                text = module.get.text();
                module.verbose('Saving placeholder text as', text);
                $module.data(metadata.placeholderText, text);
              }
            },
            remoteData: function(name, value) {
              if (window.Storage === undefined) {
                module.error(error.noStorage);
                return;
              }
              module.verbose('Saving remote data to session storage', value, name);
              sessionStorage.setItem(value, name);
            }
          },

          clear: function() {
            if (module.is.multiple()) {
              module.remove.labels();
            } else {
              module.remove.activeItem();
              module.remove.selectedItem();
            }
            module.set.placeholderText();
            module.clearValue();
          },

          clearValue: function() {
            module.set.value('');
          },

          scrollPage: function(direction, $selectedItem) {
            var
              $currentItem = $selectedItem || module.get.selectedItem(),
              $menu = $currentItem.closest(selector.menu),
              menuHeight = $menu.outerHeight(),
              currentScroll = $menu.scrollTop(),
              itemHeight = $item.eq(0).outerHeight(),
              itemsPerPage = Math.floor(menuHeight / itemHeight),
              maxScroll = $menu.prop('scrollHeight'),
              newScroll = (direction == 'up') ? currentScroll - (itemHeight * itemsPerPage) : currentScroll + (itemHeight * itemsPerPage),
              $selectableItem = $item.not(selector.unselectable),
              isWithinRange,
              $nextSelectedItem,
              elementIndex;
            elementIndex = (direction == 'up') ? $selectableItem.index($currentItem) - itemsPerPage : $selectableItem.index($currentItem) + itemsPerPage;
            isWithinRange = (direction == 'up') ? (elementIndex >= 0) : (elementIndex < $selectableItem.length);
            $nextSelectedItem = (isWithinRange) ? $selectableItem.eq(elementIndex) : (direction == 'up') ? $selectableItem.first() : $selectableItem.last();
            if ($nextSelectedItem.length > 0) {
              module.debug('Scrolling page', direction, $nextSelectedItem);
              $currentItem
                .removeClass(className.selected);
              $nextSelectedItem
                .addClass(className.selected);
              $menu
                .scrollTop(newScroll);
            }
          },

          set: {
            filtered: function() {
              var
                isMultiple = module.is.multiple(),
                isSearch = module.is.searchSelection(),
                isSearchMultiple = (isMultiple && isSearch),
                searchValue = (isSearch) ? module.get.query() : '',
                hasSearchValue = (typeof searchValue === 'string' && searchValue.length > 0),
                searchWidth = module.get.searchWidth(searchValue.length),
                valueIsSet = searchValue !== '';
              if (isMultiple && hasSearchValue) {
                module.verbose('Adjusting input width', searchWidth, settings.glyphWidth);
                $search.css('width', searchWidth);
              }
              if (hasSearchValue || (isSearchMultiple && valueIsSet)) {
                module.verbose('Hiding placeholder text');
                $text.addClass(className.filtered);
              } else if (!isMultiple || (isSearchMultiple && !valueIsSet)) {
                module.verbose('Showing placeholder text');
                $text.removeClass(className.filtered);
              }
            },
            loading: function() {
              $module.addClass(className.loading);
            },
            placeholderText: function(text) {
              text = text || module.get.placeholderText();
              module.debug('Setting placeholder text', text);
              module.set.text(text);
              $text.addClass(className.placeholder);
            },
            tabbable: function() {
              if (module.has.search()) {
                module.debug('Added tabindex to searchable dropdown');
                $search
                  .val('')
                  .attr('tabindex', 0);
                $menu
                  .attr('tabindex', -1);
              } else {
                module.debug('Added tabindex to dropdown');
                if ($module.attr('tabindex') === undefined) {
                  $module
                    .attr('tabindex', 0);
                  $menu
                    .attr('tabindex', -1);
                }
              }
            },
            initialLoad: function() {
              module.verbose('Setting initial load');
              initialLoad = true;
            },
            activeItem: function($item) {
              if (settings.allowAdditions && $item.filter(selector.addition).length > 0) {
                $item.addClass(className.filtered);
              } else {
                $item.addClass(className.active);
              }
            },
            scrollPosition: function($item, forceScroll) {
              var
                edgeTolerance = 5,
                $menu,
                hasActive,
                offset,
                itemHeight,
                itemOffset,
                menuOffset,
                menuScroll,
                menuHeight,
                abovePage,
                belowPage;

              $item = $item || module.get.selectedItem();
              $menu = $item.closest(selector.menu);
              hasActive = ($item && $item.length > 0);
              forceScroll = (forceScroll !== undefined) ? forceScroll : false;
              if ($item && $menu.length > 0 && hasActive) {
                itemOffset = $item.position().top;

                $menu.addClass(className.loading);
                menuScroll = $menu.scrollTop();
                menuOffset = $menu.offset().top;
                itemOffset = $item.offset().top;
                offset = menuScroll - menuOffset + itemOffset;
                if (!forceScroll) {
                  menuHeight = $menu.height();
                  belowPage = menuScroll + menuHeight < (offset + edgeTolerance);
                  abovePage = ((offset - edgeTolerance) < menuScroll);
                }
                module.debug('Scrolling to active item', offset);
                if (forceScroll || abovePage || belowPage) {
                  $menu.scrollTop(offset);
                }
                $menu.removeClass(className.loading);
              }
            },
            text: function(text) {
              if (settings.action !== 'select') {
                if (settings.action == 'combo') {
                  module.debug('Changing combo button text', text, $combo);
                  if (settings.preserveHTML) {
                    $combo.html(text);
                  } else {
                    $combo.text(text);
                  }
                } else {
                  if (text !== module.get.placeholderText()) {
                    $text.removeClass(className.placeholder);
                  }
                  module.debug('Changing text', text, $text);
                  $text
                    .removeClass(className.filtered);
                  if (settings.preserveHTML) {
                    $text.html(text);
                  } else {
                    $text.text(text);
                  }
                }
              }
            },
            selectedLetter: function(letter) {
              var
                $selectedItem = $item.filter('.' + className.selected),
                alreadySelectedLetter = $selectedItem.length > 0 && module.has.firstLetter($selectedItem, letter),
                $nextValue = false,
                $nextItem;
              // check next of same letter
              if (alreadySelectedLetter) {
                $nextItem = $selectedItem.nextAll($item).eq(0);
                if (module.has.firstLetter($nextItem, letter)) {
                  $nextValue = $nextItem;
                }
              }
              // check all values
              if (!$nextValue) {
                $item
                  .each(function() {
                    if (module.has.firstLetter($(this), letter)) {
                      $nextValue = $(this);
                      return false;
                    }
                  });
              }
              // set next value
              if ($nextValue) {
                module.verbose('Scrolling to next value with letter', letter);
                module.set.scrollPosition($nextValue);
                $selectedItem.removeClass(className.selected);
                $nextValue.addClass(className.selected);
              }
            },
            direction: function($menu) {
              if (settings.direction == 'auto') {
                if (module.is.onScreen($menu)) {
                  module.remove.upward($menu);
                } else {
                  module.set.upward($menu);
                }
              } else if (settings.direction == 'upward') {
                module.set.upward($menu);
              }
            },
            upward: function($menu) {
              var $element = $menu || $module;
              $element.addClass(className.upward);
            },
            value: function(value, text, $selected) {
              var
                hasInput = ($input.length > 0),
                isAddition = !module.has.value(value),
                currentValue = module.get.values(),
                stringValue = (value !== undefined) ? String(value) : value,
                newValue;
              if (hasInput) {
                if (stringValue == currentValue) {
                  module.verbose('Skipping value update already same value', value, currentValue);
                  if (!module.is.initialLoad()) {
                    return;
                  }
                }

                if (module.is.single() && module.has.selectInput() && module.can.extendSelect()) {
                  module.debug('Adding user option', value);
                  module.add.optionValue(value);
                }
                module.debug('Updating input value', value, currentValue);
                internalChange = true;
                $input
                  .val(value);
                if (settings.fireOnInit === false && module.is.initialLoad()) {
                  module.debug('Input native change event ignored on initial load');
                } else {
                  module.trigger.change();
                }
                internalChange = false;
              } else {
                module.verbose('Storing value in metadata', value, $input);
                if (value !== currentValue) {
                  $module.data(metadata.value, stringValue);
                }
              }
              if (settings.fireOnInit === false && module.is.initialLoad()) {
                module.verbose('No callback on initial load', settings.onChange);
              } else {
                settings.onChange.call(element, value, text, $selected);
              }
            },
            active: function() {
              $module
                .addClass(className.active);
            },
            multiple: function() {
              $module.addClass(className.multiple);
            },
            visible: function() {
              $module.addClass(className.visible);
            },
            exactly: function(value, $selectedItem) {
              module.debug('Setting selected to exact values');
              module.clear();
              module.set.selected(value, $selectedItem);
            },
            selected: function(value, $selectedItem) {
              var
                isMultiple = module.is.multiple(),
                $userSelectedItem;
              $selectedItem = (settings.allowAdditions) ? $selectedItem || module.get.itemWithAdditions(value) : $selectedItem || module.get.item(value);
              if (!$selectedItem) {
                return;
              }
              module.debug('Setting selected menu item to', $selectedItem);
              if (module.is.single()) {
                module.remove.activeItem();
                module.remove.selectedItem();
              } else if (settings.useLabels) {
                module.remove.selectedItem();
              }
              // select each item
              $selectedItem
                .each(function() {
                  var
                    $selected = $(this),
                    selectedText = module.get.choiceText($selected),
                    selectedValue = module.get.choiceValue($selected, selectedText),

                    isFiltered = $selected.hasClass(className.filtered),
                    isActive = $selected.hasClass(className.active),
                    isUserValue = $selected.hasClass(className.addition),
                    shouldAnimate = (isMultiple && $selectedItem.length == 1);
                  if (isMultiple) {
                    if (!isActive || isUserValue) {
                      if (settings.apiSettings && settings.saveRemoteData) {
                        module.save.remoteData(selectedText, selectedValue);
                      }
                      if (settings.useLabels) {
                        module.add.value(selectedValue, selectedText, $selected);
                        module.add.label(selectedValue, selectedText, shouldAnimate);
                        module.set.activeItem($selected);
                        module.filterActive();
                        module.select.nextAvailable($selectedItem);
                      } else {
                        module.add.value(selectedValue, selectedText, $selected);
                        module.set.text(module.add.variables(message.count));
                        module.set.activeItem($selected);
                      }
                    } else if (!isFiltered) {
                      module.debug('Selected active value, removing label');
                      module.remove.selected(selectedValue);
                    }
                  } else {
                    if (settings.apiSettings && settings.saveRemoteData) {
                      module.save.remoteData(selectedText, selectedValue);
                    }
                    module.set.text(selectedText);
                    module.set.value(selectedValue, selectedText, $selected);
                    $selected
                      .addClass(className.active)
                      .addClass(className.selected);
                  }
                });
            }
          },

          add: {
            label: function(value, text, shouldAnimate) {
              var
                $next = module.is.searchSelection() ? $search : $text,
                $label;
              $label = $('<a />')
                .addClass(className.label)
                .attr('data-value', value)
                .html(templates.label(value, text));
              $label = settings.onLabelCreate.call($label, value, text);

              if (module.has.label(value)) {
                module.debug('Label already exists, skipping', value);
                return;
              }
              if (settings.label.variation) {
                $label.addClass(settings.label.variation);
              }
              if (shouldAnimate === true) {
                module.debug('Animating in label', $label);
                $label
                  .addClass(className.hidden)
                  .insertBefore($next)
                  .transition(settings.label.transition, settings.label.duration);
              } else {
                module.debug('Adding selection label', $label);
                $label
                  .insertBefore($next);
              }
            },
            message: function(message) {
              var
                $message = $menu.children(selector.message),
                html = settings.templates.message(module.add.variables(message));
              if ($message.length > 0) {
                $message
                  .html(html);
              } else {
                $message = $('<div/>')
                  .html(html)
                  .addClass(className.message)
                  .appendTo($menu);
              }
            },
            optionValue: function(value) {
              var
                $option = $input.find('option[value="' + value + '"]'),
                hasOption = ($option.length > 0);
              if (hasOption) {
                return;
              }
              // temporarily disconnect observer
              if (selectObserver) {
                selectObserver.disconnect();
                module.verbose('Temporarily disconnecting mutation observer', value);
              }
              if (module.is.single()) {
                module.verbose('Removing previous user addition');
                $input.find('option.' + className.addition).remove();
              }
              $('<option/>')
                .prop('value', value)
                .addClass(className.addition)
                .html(value)
                .appendTo($input);
              module.verbose('Adding user addition as an <option>', value);
              if (selectObserver) {
                selectObserver.observe($input[0], {
                  childList: true,
                  subtree: true
                });
              }
            },
            userSuggestion: function(value) {
              var
                $addition = $menu.children(selector.addition),
                $existingItem = module.get.item(value),
                alreadyHasValue = $existingItem && $existingItem.not(selector.addition).length,
                hasUserSuggestion = $addition.length > 0,
                html;
              if (settings.useLabels && module.has.maxSelections()) {
                return;
              }
              if (value === '' || alreadyHasValue) {
                $addition.remove();
                return;
              }
              $item
                .removeClass(className.selected);
              if (hasUserSuggestion) {
                html = settings.templates.addition(module.add.variables(message.addResult, value));
                $addition
                  .html(html)
                  .attr('data-' + metadata.value, value)
                  .attr('data-' + metadata.text, value)
                  .removeClass(className.filtered)
                  .addClass(className.selected);
                module.verbose('Replacing user suggestion with new value', $addition);
              } else {
                $addition = module.create.userChoice(value);
                $addition
                  .prependTo($menu)
                  .addClass(className.selected);
                module.verbose('Adding item choice to menu corresponding with user choice addition', $addition);
              }
            },
            variables: function(message, term) {
              var
                hasCount = (message.search('{count}') !== -1),
                hasMaxCount = (message.search('{maxCount}') !== -1),
                hasTerm = (message.search('{term}') !== -1),
                values,
                count,
                query;
              module.verbose('Adding templated variables to message', message);
              if (hasCount) {
                count = module.get.selectionCount();
                message = message.replace('{count}', count);
              }
              if (hasMaxCount) {
                count = module.get.selectionCount();
                message = message.replace('{maxCount}', settings.maxSelections);
              }
              if (hasTerm) {
                query = term || module.get.query();
                message = message.replace('{term}', query);
              }
              return message;
            },
            value: function(addedValue, addedText, $selectedItem) {
              var
                currentValue = module.get.values(),
                newValue;
              if (addedValue === '') {
                module.debug('Cannot select blank values from multiselect');
                return;
              }
              // extend current array
              if ($.isArray(currentValue)) {
                newValue = currentValue.concat([addedValue]);
                newValue = module.get.uniqueArray(newValue);
              } else {
                newValue = [addedValue];
              }
              // add values
              if (module.has.selectInput()) {
                if (module.can.extendSelect()) {
                  module.debug('Adding value to select', addedValue, newValue, $input);
                  module.add.optionValue(addedValue);
                }
              } else {
                newValue = newValue.join(settings.delimiter);
                module.debug('Setting hidden input to delimited value', newValue, $input);
              }

              if (settings.fireOnInit === false && module.is.initialLoad()) {
                module.verbose('Skipping onadd callback on initial load', settings.onAdd);
              } else {
                settings.onAdd.call(element, addedValue, addedText, $selectedItem);
              }
              module.set.value(newValue, addedValue, addedText, $selectedItem);
              module.check.maxSelections();
            }
          },

          remove: {
            active: function() {
              $module.removeClass(className.active);
            },
            activeLabel: function() {
              $module.find(selector.label).removeClass(className.active);
            },
            loading: function() {
              $module.removeClass(className.loading);
            },
            initialLoad: function() {
              initialLoad = false;
            },
            upward: function($menu) {
              var $element = $menu || $module;
              $element.removeClass(className.upward);
            },
            visible: function() {
              $module.removeClass(className.visible);
            },
            activeItem: function() {
              $item.removeClass(className.active);
            },
            filteredItem: function() {
              if (settings.useLabels && module.has.maxSelections()) {
                return;
              }
              if (settings.useLabels && module.is.multiple()) {
                $item.not('.' + className.active).removeClass(className.filtered);
              } else {
                $item.removeClass(className.filtered);
              }
            },
            optionValue: function(value) {
              var
                $option = $input.find('option[value="' + value + '"]'),
                hasOption = ($option.length > 0);
              if (!hasOption || !$option.hasClass(className.addition)) {
                return;
              }
              // temporarily disconnect observer
              if (selectObserver) {
                selectObserver.disconnect();
                module.verbose('Temporarily disconnecting mutation observer', value);
              }
              $option.remove();
              module.verbose('Removing user addition as an <option>', value);
              if (selectObserver) {
                selectObserver.observe($input[0], {
                  childList: true,
                  subtree: true
                });
              }
            },
            message: function() {
              $menu.children(selector.message).remove();
            },
            searchTerm: function() {
              module.verbose('Cleared search term');
              $search.val('');
              module.set.filtered();
            },
            selected: function(value, $selectedItem) {
              $selectedItem = (settings.allowAdditions) ? $selectedItem || module.get.itemWithAdditions(value) : $selectedItem || module.get.item(value);

              if (!$selectedItem) {
                return false;
              }

              $selectedItem
                .each(function() {
                  var
                    $selected = $(this),
                    selectedText = module.get.choiceText($selected),
                    selectedValue = module.get.choiceValue($selected, selectedText);
                  if (module.is.multiple()) {
                    if (settings.useLabels) {
                      module.remove.value(selectedValue, selectedText, $selected);
                      module.remove.label(selectedValue);
                    } else {
                      module.remove.value(selectedValue, selectedText, $selected);
                      if (module.get.selectionCount() === 0) {
                        module.set.placeholderText();
                      } else {
                        module.set.text(module.add.variables(message.count));
                      }
                    }
                  } else {
                    module.remove.value(selectedValue, selectedText, $selected);
                  }
                  $selected
                    .removeClass(className.filtered)
                    .removeClass(className.active);
                  if (settings.useLabels) {
                    $selected.removeClass(className.selected);
                  }
                });
            },
            selectedItem: function() {
              $item.removeClass(className.selected);
            },
            value: function(removedValue, removedText, $removedItem) {
              var
                values = module.get.values(),
                newValue;
              if (module.has.selectInput()) {
                module.verbose('Input is <select> removing selected option', removedValue);
                newValue = module.remove.arrayValue(removedValue, values);
                module.remove.optionValue(removedValue);
              } else {
                module.verbose('Removing from delimited values', removedValue);
                newValue = module.remove.arrayValue(removedValue, values);
                newValue = newValue.join(settings.delimiter);
              }
              if (settings.fireOnInit === false && module.is.initialLoad()) {
                module.verbose('No callback on initial load', settings.onRemove);
              } else {
                settings.onRemove.call(element, removedValue, removedText, $removedItem);
              }
              module.set.value(newValue, removedText, $removedItem);
              module.check.maxSelections();
            },
            arrayValue: function(removedValue, values) {
              if (!$.isArray(values)) {
                values = [values];
              }
              values = $.grep(values, function(value) {
                return (removedValue != value);
              });
              module.verbose('Removed value from delimited string', removedValue, values);
              return values;
            },
            label: function(value, shouldAnimate) {
              var
                $labels = $module.find(selector.label),
                $removedLabel = $labels.filter('[data-value="' + value + '"]');
              module.verbose('Removing label', $removedLabel);
              $removedLabel.remove();
            },
            activeLabels: function($activeLabels) {
              $activeLabels = $activeLabels || $module.find(selector.label).filter('.' + className.active);
              module.verbose('Removing active label selections', $activeLabels);
              module.remove.labels($activeLabels);
            },
            labels: function($labels) {
              $labels = $labels || $module.find(selector.label);
              module.verbose('Removing labels', $labels);
              $labels
                .each(function() {
                  var
                    $label = $(this),
                    value = $label.data(metadata.value),
                    stringValue = (value !== undefined) ? String(value) : value,
                    isUserValue = module.is.userValue(stringValue);
                  if (settings.onLabelRemove.call($label, value) === false) {
                    module.debug('Label remove callback cancelled removal');
                    return;
                  }
                  if (isUserValue) {
                    module.remove.value(stringValue);
                    module.remove.label(stringValue);
                  } else {
                    // selected will also remove label
                    module.remove.selected(stringValue);
                  }
                });
            },
            tabbable: function() {
              if (module.has.search()) {
                module.debug('Searchable dropdown initialized');
                $search
                  .removeAttr('tabindex');
                $menu
                  .removeAttr('tabindex');
              } else {
                module.debug('Simple selection dropdown initialized');
                $module
                  .removeAttr('tabindex');
                $menu
                  .removeAttr('tabindex');
              }
            }
          },

          has: {
            search: function() {
              return ($search.length > 0);
            },
            selectInput: function() {
              return ($input.is('select'));
            },
            firstLetter: function($item, letter) {
              var
                text,
                firstLetter;
              if (!$item || $item.length === 0 || typeof letter !== 'string') {
                return false;
              }
              text = module.get.choiceText($item, false);
              letter = letter;
              firstLetter = String(text).charAt(0);
              return (letter == firstLetter);
            },
            input: function() {
              return ($input.length > 0);
            },
            items: function() {
              return ($item.length > 0);
            },
            menu: function() {
              return ($menu.length > 0);
            },
            message: function() {
              return ($menu.children(selector.message).length !== 0);
            },
            label: function(value) {
              var
                $labels = $module.find(selector.label);
              return ($labels.filter('[data-value="' + value + '"]').length > 0);
            },
            maxSelections: function() {
              return (settings.maxSelections && module.get.selectionCount() >= settings.maxSelections);
            },
            allResultsFiltered: function() {
              return ($item.filter(selector.unselectable).length === $item.length);
            },
            query: function() {
              return (module.get.query() !== '');
            },
            value: function(value) {
              var
                values = module.get.values(),
                hasValue = $.isArray(values) ? values && ($.inArray(value, values) !== -1) : (values == value);
              return (hasValue) ? true : false;
            }
          },

          is: {
            active: function() {
              return $module.hasClass(className.active);
            },
            alreadySetup: function() {
              return ($module.is('select') && $module.parent(selector.dropdown).length > 0 && $module.prev().length === 0);
            },
            animating: function($subMenu) {
              return ($subMenu) ? $subMenu.transition && $subMenu.transition('is animating') : $menu.transition && $menu.transition('is animating');
            },
            disabled: function() {
              return $module.hasClass(className.disabled);
            },
            focused: function() {
              return (document.activeElement === $module[0]);
            },
            focusedOnSearch: function() {
              return (document.activeElement === $search[0]);
            },
            allFiltered: function() {
              return ((module.is.multiple() || module.has.search()) && !module.has.message() && module.has.allResultsFiltered());
            },
            hidden: function($subMenu) {
              return !module.is.visible($subMenu);
            },
            initialLoad: function() {
              return initialLoad;
            },
            onScreen: function($subMenu) {
              var
                $currentMenu = $subMenu || $menu,
                canOpenDownward = true,
                onScreen = {},
                calculations;
              $currentMenu.addClass(className.loading);
              calculations = {
                context: {
                  scrollTop: $context.scrollTop(),
                  height: $context.outerHeight()
                },
                menu: {
                  offset: $currentMenu.offset(),
                  height: $currentMenu.outerHeight()
                }
              };
              onScreen = {
                above: (calculations.context.scrollTop) <= calculations.menu.offset.top - calculations.menu.height,
                below: (calculations.context.scrollTop + calculations.context.height) >= calculations.menu.offset.top + calculations.menu.height
              };
              if (onScreen.below) {
                module.verbose('Dropdown can fit in context downward', onScreen);
                canOpenDownward = true;
              } else if (!onScreen.below && !onScreen.above) {
                module.verbose('Dropdown cannot fit in either direction, favoring downward', onScreen);
                canOpenDownward = true;
              } else {
                module.verbose('Dropdown cannot fit below, opening upward', onScreen);
                canOpenDownward = false;
              }
              $currentMenu.removeClass(className.loading);
              return canOpenDownward;
            },
            inObject: function(needle, object) {
              var
                found = false;
              $.each(object, function(index, property) {
                if (property == needle) {
                  found = true;
                  return true;
                }
              });
              return found;
            },
            multiple: function() {
              return $module.hasClass(className.multiple);
            },
            single: function() {
              return !module.is.multiple();
            },
            selectMutation: function(mutations) {
              var
                selectChanged = false;
              $.each(mutations, function(index, mutation) {
                if (mutation.target && $(mutation.target).is('select')) {
                  selectChanged = true;
                  return true;
                }
              });
              return selectChanged;
            },
            search: function() {
              return $module.hasClass(className.search);
            },
            searchSelection: function() {
              return (module.has.search() && $search.parent(selector.dropdown).length === 1);
            },
            selection: function() {
              return $module.hasClass(className.selection);
            },
            userValue: function(value) {
              return ($.inArray(value, module.get.userValues()) !== -1);
            },
            upward: function($menu) {
              var $element = $menu || $module;
              return $element.hasClass(className.upward);
            },
            visible: function($subMenu) {
              return ($subMenu) ? $subMenu.hasClass(className.visible) : $menu.hasClass(className.visible);
            }
          },

          can: {
            activate: function($item) {
              if (settings.useLabels) {
                return true;
              }
              if (!module.has.maxSelections()) {
                return true;
              }
              if (module.has.maxSelections() && $item.hasClass(className.active)) {
                return true;
              }
              return false;
            },
            click: function() {
              return (hasTouch || settings.on == 'click');
            },
            extendSelect: function() {
              return settings.allowAdditions || settings.apiSettings;
            },
            show: function() {
              return !module.is.disabled();
            },
            useAPI: function() {
              return $.fn.api !== undefined;
            }
          },

          animate: {
            show: function(callback, $subMenu) {
              var
                $currentMenu = $subMenu || $menu,
                start = ($subMenu) ? function() {} : function() {
                  module.hideSubMenus();
                  module.hideOthers();
                  module.set.active();
                },
                transition;
              callback = $.isFunction(callback) ? callback : function() {};
              module.verbose('Doing menu show animation', $currentMenu);
              module.set.direction($subMenu);
              transition = module.get.transition($subMenu);
              if (module.is.selection()) {
                module.set.scrollPosition(module.get.selectedItem(), true);
              }
              if (module.is.hidden($currentMenu) || module.is.animating($currentMenu)) {
                if (transition == 'none') {
                  start();
                  $currentMenu.transition('show');
                  callback.call(element);
                } else if ($.fn.transition !== undefined && $module.transition('is supported')) {
                  $currentMenu
                    .transition({
                      animation: transition + ' in',
                      debug: settings.debug,
                      verbose: settings.verbose,
                      duration: settings.duration,
                      queue: true,
                      onStart: start,
                      onComplete: function() {
                        callback.call(element);
                      }
                    });
                } else {
                  module.error(error.noTransition, transition);
                }
              }
            },
            hide: function(callback, $subMenu) {
              var
                $currentMenu = $subMenu || $menu,
                duration = ($subMenu) ? (settings.duration * 0.9) : settings.duration,
                start = ($subMenu) ? function() {} : function() {
                  if (module.can.click()) {
                    module.unbind.intent();
                  }
                  module.remove.active();
                },
                transition = module.get.transition($subMenu);
              callback = $.isFunction(callback) ? callback : function() {};
              if (module.is.visible($currentMenu) || module.is.animating($currentMenu)) {
                module.verbose('Doing menu hide animation', $currentMenu);

                if (transition == 'none') {
                  start();
                  $currentMenu.transition('hide');
                  callback.call(element);
                } else if ($.fn.transition !== undefined && $module.transition('is supported')) {
                  $currentMenu
                    .transition({
                      animation: transition + ' out',
                      duration: settings.duration,
                      debug: settings.debug,
                      verbose: settings.verbose,
                      queue: true,
                      onStart: start,
                      onComplete: function() {
                        if (settings.direction == 'auto') {
                          module.remove.upward($subMenu);
                        }
                        callback.call(element);
                      }
                    });
                } else {
                  module.error(error.transition);
                }
              }
            }
          },

          hideAndClear: function() {
            module.remove.searchTerm();
            if (module.has.maxSelections()) {
              return;
            }
            if (module.has.search()) {
              module.hide(function() {
                module.remove.filteredItem();
              });
            } else {
              module.hide();
            }
          },

          delay: {
            show: function() {
              module.verbose('Delaying show event to ensure user intent');
              clearTimeout(module.timer);
              module.timer = setTimeout(module.show, settings.delay.show);
            },
            hide: function() {
              module.verbose('Delaying hide event to ensure user intent');
              clearTimeout(module.timer);
              module.timer = setTimeout(module.hide, settings.delay.hide);
            }
          },

          escape: {
            regExp: function(text) {
              text = String(text);
              return text.replace(regExp.escape, '\\$&');
            }
          },

          setting: function(name, value) {
            module.debug('Changing setting', name, value);
            if ($.isPlainObject(name)) {
              $.extend(true, settings, name);
            } else if (value !== undefined) {
              settings[name] = value;
            } else {
              return settings[name];
            }
          },
          internal: function(name, value) {
            if ($.isPlainObject(name)) {
              $.extend(true, module, name);
            } else if (value !== undefined) {
              module[name] = value;
            } else {
              return module[name];
            }
          },
          debug: function() {
            if (settings.debug) {
              if (settings.performance) {
                module.performance.log(arguments);
              } else {
                module.debug = Function.prototype.bind.call(console.info, console, settings.name + ':');
                module.debug.apply(console, arguments);
              }
            }
          },
          verbose: function() {
            if (settings.verbose && settings.debug) {
              if (settings.performance) {
                module.performance.log(arguments);
              } else {
                module.verbose = Function.prototype.bind.call(console.info, console, settings.name + ':');
                module.verbose.apply(console, arguments);
              }
            }
          },
          error: function() {
            module.error = Function.prototype.bind.call(console.error, console, settings.name + ':');
            module.error.apply(console, arguments);
          },
          performance: {
            log: function(message) {
              var
                currentTime,
                executionTime,
                previousTime;
              if (settings.performance) {
                currentTime = new Date().getTime();
                previousTime = time || currentTime;
                executionTime = currentTime - previousTime;
                time = currentTime;
                performance.push({
                  'Name': message[0],
                  'Arguments': [].slice.call(message, 1) || '',
                  'Element': element,
                  'Execution Time': executionTime
                });
              }
              clearTimeout(module.performance.timer);
              module.performance.timer = setTimeout(module.performance.display, 500);
            },
            display: function() {
              var
                title = settings.name + ':',
                totalTime = 0;
              time = false;
              clearTimeout(module.performance.timer);
              $.each(performance, function(index, data) {
                totalTime += data['Execution Time'];
              });
              title += ' ' + totalTime + 'ms';
              if (moduleSelector) {
                title += ' \'' + moduleSelector + '\'';
              }
              if ((console.group !== undefined || console.table !== undefined) && performance.length > 0) {
                console.groupCollapsed(title);
                if (console.table) {
                  console.table(performance);
                } else {
                  $.each(performance, function(index, data) {
                    console.log(data['Name'] + ': ' + data['Execution Time'] + 'ms');
                  });
                }
                console.groupEnd();
              }
              performance = [];
            }
          },
          invoke: function(query, passedArguments, context) {
            var
              object = instance,
              maxDepth,
              found,
              response;
            passedArguments = passedArguments || queryArguments;
            context = element || context;
            if (typeof query == 'string' && object !== undefined) {
              query = query.split(/[\. ]/);
              maxDepth = query.length - 1;
              $.each(query, function(depth, value) {
                var camelCaseValue = (depth != maxDepth) ? value + query[depth + 1].charAt(0).toUpperCase() + query[depth + 1].slice(1) : query;
                if ($.isPlainObject(object[camelCaseValue]) && (depth != maxDepth)) {
                  object = object[camelCaseValue];
                } else if (object[camelCaseValue] !== undefined) {
                  found = object[camelCaseValue];
                  return false;
                } else if ($.isPlainObject(object[value]) && (depth != maxDepth)) {
                  object = object[value];
                } else if (object[value] !== undefined) {
                  found = object[value];
                  return false;
                } else {
                  module.error(error.method, query);
                  return false;
                }
              });
            }
            if ($.isFunction(found)) {
              response = found.apply(context, passedArguments);
            } else if (found !== undefined) {
              response = found;
            }
            if ($.isArray(returnedValue)) {
              returnedValue.push(response);
            } else if (returnedValue !== undefined) {
              returnedValue = [returnedValue, response];
            } else if (response !== undefined) {
              returnedValue = response;
            }
            return found;
          }
        };

        if (methodInvoked) {
          if (instance === undefined) {
            module.initialize();
          }
          module.invoke(query);
        } else {
          if (instance !== undefined) {
            instance.invoke('destroy');
          }
          module.initialize();
        }
      });
    return (returnedValue !== undefined) ? returnedValue : $allModules;
  };

  $.fn.uidropdown.settings = {

    debug: false,
    verbose: false,
    performance: true,

    on: 'click', // what event should show menu action on item selection
    action: 'activate', // action on item selection (nothing, activate, select, combo, hide, function(){})


    apiSettings: false,
    saveRemoteData: true, // Whether remote name/value pairs should be stored in sessionStorage to allow remote data to be restored on page refresh
    throttle: 200, // How long to wait after last user input to search remotely

    context: window, // Context to use when determining if on screen
    direction: 'auto', // Whether dropdown should always open in one direction
    keepOnScreen: true, // Whether dropdown should check whether it is on screen before showing

    match: 'both', // what to match against with search selection (both, text, or label)
    fullTextSearch: false, // search anywhere in value

    placeholder: 'auto', // whether to convert blank <select> values to placeholder text
    preserveHTML: true, // preserve html when selecting value
    sortSelect: false, // sort selection on init

    forceSelection: true, // force a choice on blur with search selection
    allowAdditions: false, // whether multiple select should allow user added values

    maxSelections: false, // When set to a number limits the number of selections to this count
    useLabels: true, // whether multiple select should filter currently active selections from choices
    delimiter: ',', // when multiselect uses normal <input> the values will be delimited with this character

    showOnFocus: true, // show menu on focus
    allowTab: true, // add tabindex to element
    allowCategorySelection: false, // allow elements with sub-menus to be selected

    fireOnInit: false, // Whether callbacks should fire when initializing dropdown values

    transition: 'auto', // auto transition will slide down or up based on direction
    duration: 200, // duration of transition

    glyphWidth: 1.0714, // widest glyph width in em (W is 1.0714 em) used to calculate multiselect input width
    locatedUrl: '',
    searchTreeUrl: '',
    treeWidget:'',
    param:[],
    // label settings on multi-select
    label: {
      transition: 'scale',
      duration: 200,
      variation: false
    },

    // delay before event
    delay: {
      hide: 300,
      show: 200,
      search: 20,
      touch: 50
    },

    /* Callbacks */
    onChange: function(value, text, $selected) {},
    onAdd: function(value, text, $selected) {},
    onRemove: function(value, text, $selected) {},

    onLabelSelect: function($selectedLabels) {},
    onLabelCreate: function(value, text) {
      return $(this);
    },
    onLabelRemove: function(value) {
      return true;
    },
    onNoResults: function(searchTerm) {
      return true;
    },
    onShow: function() {},
    onHide: function() {},

    /* Component */
    name: 'Dropdown',
    namespace: 'dropdown',

    message: {
      addResult: 'Add <b>{term}</b>',
      count: '{count} selected',
      maxSelections: 'Max {maxCount} selections',
      noResults: 'No results found.',
      serverError: 'There was an error contacting the server'
    },

    error: {
      action: 'You called a dropdown action that was not defined',
      alreadySetup: 'Once a select has been initialized behaviors must be called on the created ui dropdown',
      labels: 'Allowing user additions currently requires the use of labels.',
      missingMultiple: '<select> requires multiple property to be set to correctly preserve multiple values',
      method: 'The method you called is not defined.',
      noAPI: 'The API module is required to load resources remotely',
      noStorage: 'Saving remote data requires session storage',
      noTransition: 'This module requires ui transitions <https://github.com/Semantic-Org/UI-Transition>'
    },

    regExp: {
      escape: /[-[\]{}()*+?.,\\^$|#\s]/g,
    },

    metadata: {
      defaultText: 'defaultText',
      defaultValue: 'defaultValue',
      placeholderText: 'placeholder',
      text: 'text',
      value: 'value'
    },

    // property names for remote query
    fields: {
      remoteValues: 'results', // grouping for api results
      values: 'values', // grouping for all dropdown values
      name: 'name', // displayed dropdown text
      value: 'value', // actual dropdown value
      path: 'path',
      id: 'id'
    },

    keys: {
      backspace: 8,
      delimiter: 188, // comma
      deleteKey: 46,
      enter: 13,
      escape: 27,
      pageUp: 33,
      pageDown: 34,
      leftArrow: 37,
      upArrow: 38,
      rightArrow: 39,
      downArrow: 40
    },

    selector: {
      addition: '.addition',
      dropdown: '.ui.dropdown',
      icon: '> .dropdown.icon',
      input: '> input[type="hidden"], > select',
      item: '.item > label', //#demo li a
      label: '> .label',
      remove: '> .label > .delete.icon',
      siblingLabel: '.label',
      menu: '.menu',
      message: '.message',
      menuIcon: '.dropdown.icon',
      search: 'input.search, .menu > .search > input',
      text: '> .text:not(.icon)',
      unselectable: '.disabled, .filtered',
      searchMenu: '.scrolling.menu',
      treeMenu: '.ztree',
      located: '.marker.icon'
    },

    className: {
      active: 'active',
      addition: 'addition',
      animating: 'animating',
      disabled: 'disabled',
      dropdown: 'ui dropdown',
      filtered: 'disabled',
      hidden: 'hidden transition',
      item: 'item',
      label: 'ui label',
      loading: 'loading',
      menu: 'menu',
      message: 'message',
      multiple: 'multiple',
      placeholder: 'default',
      search: 'search',
      selected: 'selected',
      selection: 'selection',
      upward: 'upward',
      visible: 'visible'
    }

  };

  /* Templates */
  $.fn.uidropdown.settings.templates = {

    // generates dropdown from select values
    dropdown: function(select) {
      var
        placeholder = select.placeholder || false,
        values = select.values || {},
        html = '';
      html += '<i class="dropdown icon"></i>';
      if (select.placeholder) {
        html += '<div class="default text">' + placeholder + '</div>';
      } else {
        html += '<div class="text"></div>';
      }
      html += '<div class="menu">';
      $.each(select.values, function(index, option) {
        html += (option.disabled) ? '<div class="disabled item" data-value="' + option.value + '">' + option.name + '</div>' : '<div class="item" data-value="' + option.value + '">' + option.name + '</div>';
      });
      html += '</div>';
      return html;
    },

    // generates just menu from select
    menu: function(response, fields) {
      var
        values = response[fields.values] || {},
        html = '';
      $.each(values, function(index, option) {
        html += '<div class="item" data-value="' + option[fields.value] + '"><i class="marker blue icon" id="' + option[fields.value] + '"></i><i class="linkify right floated blue icon" title="' + option[fields.path] + '"></i><label>' + option[fields.name] + '</label></div>';
      });
      return html;
    },

    // generates label for multiselect
    label: function(value, text) {
      return text + '<i class="delete icon"></i>';
    },


    // generates messages like "No results"
    message: function(message) {
      return message;
    },

    // generates user addition to selection menu
    addition: function(choice) {
      return choice;
    }

  };

})(jQuery, window, document);
/*!
 * # Semantic UI 2.0.0 - Transition
 */
!function(n,e,i,t){"use strict";n.fn.transition=function(){{var a,o=n(this),r=o.selector||"",s=(new Date).getTime(),l=[],u=arguments,d=u[0],c=[].slice.call(arguments,1),m="string"==typeof d;e.requestAnimationFrame||e.mozRequestAnimationFrame||e.webkitRequestAnimationFrame||e.msRequestAnimationFrame||function(n){setTimeout(n,0)}}return o.each(function(e){var f,p,g,v,b,y,h,w,C,A=n(this),S=this;C={initialize:function(){f=C.get.settings.apply(S,u),v=f.className,g=f.error,b=f.metadata,w="."+f.namespace,h="module-"+f.namespace,p=A.data(h)||C,y=C.get.animationEndEvent(),m&&(m=C.invoke(d)),m===!1&&(C.verbose("Converted arguments into settings object",f),f.interval?C.delay(f.animate):C.animate(),C.instantiate())},instantiate:function(){C.verbose("Storing instance of module",C),p=C,A.data(h,p)},destroy:function(){C.verbose("Destroying previous module for",S),A.removeData(h)},refresh:function(){C.verbose("Refreshing display type on next animation"),delete C.displayType},forceRepaint:function(){C.verbose("Forcing element repaint");var n=A.parent(),e=A.next();0===e.length?A.detach().appendTo(n):A.detach().insertBefore(e)},repaint:function(){C.verbose("Repainting element");S.offsetWidth},delay:function(n){var i,a,r=C.get.animationDirection();r||(r=C.can.transition()?C.get.direction():"static"),n=n!==t?n:f.interval,i="auto"==f.reverse&&r==v.outward,a=i||1==f.reverse?(o.length-e)*f.interval:e*f.interval,C.debug("Delaying animation by",a),setTimeout(C.animate,a)},animate:function(n){if(f=n||f,!C.is.supported())return C.error(g.support),!1;if(C.debug("Preparing animation",f.animation),C.is.animating()){if(f.queue)return!f.allowRepeats&&C.has.direction()&&C.is.occurring()&&C.queuing!==!0?C.debug("Animation is currently occurring, preventing queueing same animation",f.animation):C.queue(f.animation),!1;if(!f.allowRepeats&&C.is.occurring())return C.debug("Animation is already occurring, will not execute repeated animation",f.animation),!1;C.debug("New animation started, completing previous early",f.animation),p.complete()}C.can.animate()?C.set.animating(f.animation):C.error(g.noAnimation,f.animation,S)},reset:function(){C.debug("Resetting animation to beginning conditions"),C.remove.animationCallbacks(),C.restore.conditions(),C.remove.animating()},queue:function(n){C.debug("Queueing animation of",n),C.queuing=!0,A.one(y+".queue"+w,function(){C.queuing=!1,C.repaint(),C.animate.apply(this,f)})},complete:function(n){C.debug("Animation complete",f.animation),C.remove.completeCallback(),C.remove.failSafe(),C.is.looping()||(C.is.outward()?(C.verbose("Animation is outward, hiding element"),C.restore.conditions(),C.hide()):C.is.inward()?(C.verbose("Animation is outward, showing element"),C.restore.conditions(),C.show()):C.restore.conditions())},force:{visible:function(){var n=A.attr("style"),e=C.get.userStyle(),i=C.get.displayType(),a=e+"display: "+i+" !important;",o=A.css("display"),r=n===t||""===n;o!==i?(C.verbose("Overriding default display to show element",i),A.attr("style",a)):r&&A.removeAttr("style")},hidden:function(){var n=A.attr("style"),e=A.css("display"),i=n===t||""===n;"none"===e||C.is.hidden()?i&&A.removeAttr("style"):(C.verbose("Overriding default display to hide element"),A.css("display","none"))}},has:{direction:function(e){var i=!1;return e=e||f.animation,"string"==typeof e&&(e=e.split(" "),n.each(e,function(n,e){(e===v.inward||e===v.outward)&&(i=!0)})),i},inlineDisplay:function(){var e=A.attr("style")||"";return n.isArray(e.match(/display.*?;/,""))}},set:{animating:function(n){var e;C.remove.completeCallback(),n=n||f.animation,e=C.get.animationClass(n),C.save.animation(e),C.force.visible(),C.remove.hidden(),C.remove.direction(),C.start.animation(e)},duration:function(n,e){e=e||f.duration,e="number"==typeof e?e+"ms":e,(e||0===e)&&(C.verbose("Setting animation duration",e),A.css({"animation-duration":e}))},direction:function(n){n=n||C.get.direction(),n==v.inward?C.set.inward():C.set.outward()},looping:function(){C.debug("Transition set to loop"),A.addClass(v.looping)},hidden:function(){A.addClass(v.transition).addClass(v.hidden)},inward:function(){C.debug("Setting direction to inward"),A.removeClass(v.outward).addClass(v.inward)},outward:function(){C.debug("Setting direction to outward"),A.removeClass(v.inward).addClass(v.outward)},visible:function(){A.addClass(v.transition).addClass(v.visible)}},start:{animation:function(n){n=n||C.get.animationClass(),C.debug("Starting tween",n),A.addClass(n).one(y+".complete"+w,C.complete),f.useFailSafe&&C.add.failSafe(),C.set.duration(f.duration),f.onStart.call(this)}},save:{animation:function(n){C.cache||(C.cache={}),C.cache.animation=n},displayType:function(n){"none"!==n&&A.data(b.displayType,n)},transitionExists:function(e,i){n.fn.transition.exists[e]=i,C.verbose("Saving existence of transition",e,i)}},restore:{conditions:function(){var n=C.get.currentAnimation();n&&(A.removeClass(n),C.verbose("Removing animation class",C.cache)),C.remove.duration()}},add:{failSafe:function(){var n=C.get.duration();C.timer=setTimeout(function(){A.triggerHandler(y)},n+f.failSafeDelay),C.verbose("Adding fail safe timer",C.timer)}},remove:{animating:function(){A.removeClass(v.animating)},animationCallbacks:function(){C.remove.queueCallback(),C.remove.completeCallback()},queueCallback:function(){A.off(".queue"+w)},completeCallback:function(){A.off(".complete"+w)},display:function(){A.css("display","")},direction:function(){A.removeClass(v.inward).removeClass(v.outward)},duration:function(){A.css("animation-duration","")},failSafe:function(){C.verbose("Removing fail safe timer",C.timer),C.timer&&clearTimeout(C.timer)},hidden:function(){A.removeClass(v.hidden)},visible:function(){A.removeClass(v.visible)},looping:function(){C.debug("Transitions are no longer looping"),C.is.looping()&&(C.reset(),A.removeClass(v.looping))},transition:function(){A.removeClass(v.visible).removeClass(v.hidden)}},get:{settings:function(e,i,t){return"object"==typeof e?n.extend(!0,{},n.fn.transition.settings,e):"function"==typeof t?n.extend({},n.fn.transition.settings,{animation:e,onComplete:t,duration:i}):"string"==typeof i||"number"==typeof i?n.extend({},n.fn.transition.settings,{animation:e,duration:i}):"object"==typeof i?n.extend({},n.fn.transition.settings,i,{animation:e}):"function"==typeof i?n.extend({},n.fn.transition.settings,{animation:e,onComplete:i}):n.extend({},n.fn.transition.settings,{animation:e})},animationClass:function(n){var e=n||f.animation,i=C.can.transition()&&!C.has.direction()?C.get.direction()+" ":"";return v.animating+" "+v.transition+" "+i+e},currentAnimation:function(){return C.cache.animation||!1},currentDirection:function(){return C.is.inward()?v.inward:v.outward},direction:function(){return C.is.hidden()||!C.is.visible()?v.inward:v.outward},animationDirection:function(e){var i;return e=e||f.animation,"string"==typeof e&&(e=e.split(" "),n.each(e,function(n,e){e===v.inward?i=v.inward:e===v.outward&&(i=v.outward)})),i?i:!1},duration:function(n){return n=n||f.duration,n===!1&&(n=A.css("animation-duration")||0),"string"==typeof n?n.indexOf("ms")>-1?parseFloat(n):1e3*parseFloat(n):n},displayType:function(){return f.displayType?f.displayType:(A.data(b.displayType)===t&&C.can.transition(!0),A.data(b.displayType))},userStyle:function(n){return n=n||A.attr("style")||"",n.replace(/display.*?;/,"")},transitionExists:function(e){return n.fn.transition.exists[e]},animationStartEvent:function(){var n,e=i.createElement("div"),a={animation:"animationstart",OAnimation:"oAnimationStart",MozAnimation:"mozAnimationStart",WebkitAnimation:"webkitAnimationStart"};for(n in a)if(e.style[n]!==t)return a[n];return!1},animationEndEvent:function(){var n,e=i.createElement("div"),a={animation:"animationend",OAnimation:"oAnimationEnd",MozAnimation:"mozAnimationEnd",WebkitAnimation:"webkitAnimationEnd"};for(n in a)if(e.style[n]!==t)return a[n];return!1}},can:{transition:function(e){var i,a,o,r,s,l,u,d=f.animation,c=C.get.transitionExists(d);if(c===t||e){if(C.verbose("Determining whether animation exists"),i=A.attr("class"),a=A.prop("tagName"),o=n("<"+a+" />").addClass(i).insertAfter(A),r=o.addClass(d).removeClass(v.inward).removeClass(v.outward).addClass(v.animating).addClass(v.transition).css("animationName"),s=o.addClass(v.inward).css("animationName"),u=o.attr("class",i).removeAttr("style").removeClass(v.hidden).removeClass(v.visible).show().css("display"),C.verbose("Determining final display state",u),C.save.displayType(u),o.remove(),r!=s)C.debug("Direction exists for animation",d),l=!0;else{if("none"==r||!r)return void C.debug("No animation defined in css",d);C.debug("Static animation found",d,u),l=!1}C.save.transitionExists(d,l)}return c!==t?c:l},animate:function(){return C.can.transition()!==t}},is:{animating:function(){return A.hasClass(v.animating)},inward:function(){return A.hasClass(v.inward)},outward:function(){return A.hasClass(v.outward)},looping:function(){return A.hasClass(v.looping)},occurring:function(n){return n=n||f.animation,n="."+n.replace(" ","."),A.filter(n).length>0},visible:function(){return A.is(":visible")},hidden:function(){return"hidden"===A.css("visibility")},supported:function(){return y!==!1}},hide:function(){C.verbose("Hiding element"),C.is.animating()&&C.reset(),S.blur(),C.remove.display(),C.remove.visible(),C.set.hidden(),f.onHide.call(this),f.onComplete.call(this),C.force.hidden()},show:function(n){C.verbose("Showing element",n),C.remove.hidden(),C.set.visible(),f.onShow.call(this),f.onComplete.call(this),C.force.visible()},toggle:function(){C.is.visible()?C.hide():C.show()},stop:function(){C.debug("Stopping current animation"),A.triggerHandler(y)},stopAll:function(){C.debug("Stopping all animation"),C.remove.queueCallback(),A.triggerHandler(y)},clear:{queue:function(){C.debug("Clearing animation queue"),C.remove.queueCallback()}},enable:function(){C.verbose("Starting animation"),A.removeClass(v.disabled)},disable:function(){C.debug("Stopping animation"),A.addClass(v.disabled)},setting:function(e,i){if(C.debug("Changing setting",e,i),n.isPlainObject(e))n.extend(!0,f,e);else{if(i===t)return f[e];f[e]=i}},internal:function(e,i){if(n.isPlainObject(e))n.extend(!0,C,e);else{if(i===t)return C[e];C[e]=i}},debug:function(){f.debug&&(f.performance?C.performance.log(arguments):(C.debug=Function.prototype.bind.call(console.info,console,f.name+":"),C.debug.apply(console,arguments)))},verbose:function(){f.verbose&&f.debug&&(f.performance?C.performance.log(arguments):(C.verbose=Function.prototype.bind.call(console.info,console,f.name+":"),C.verbose.apply(console,arguments)))},error:function(){C.error=Function.prototype.bind.call(console.error,console,f.name+":"),C.error.apply(console,arguments)},performance:{log:function(n){var e,i,t;f.performance&&(e=(new Date).getTime(),t=s||e,i=e-t,s=e,l.push({Name:n[0],Arguments:[].slice.call(n,1)||"",Element:S,"Execution Time":i})),clearTimeout(C.performance.timer),C.performance.timer=setTimeout(C.performance.display,500)},display:function(){var e=f.name+":",i=0;s=!1,clearTimeout(C.performance.timer),n.each(l,function(n,e){i+=e["Execution Time"]}),e+=" "+i+"ms",r&&(e+=" '"+r+"'"),o.length>1&&(e+=" ("+o.length+")"),(console.group!==t||console.table!==t)&&l.length>0&&(console.groupCollapsed(e),console.table?console.table(l):n.each(l,function(n,e){console.log(e.Name+": "+e["Execution Time"]+"ms")}),console.groupEnd()),l=[]}},invoke:function(e,i,o){var r,s,l,u=p;return i=i||c,o=S||o,"string"==typeof e&&u!==t&&(e=e.split(/[\. ]/),r=e.length-1,n.each(e,function(i,a){var o=i!=r?a+e[i+1].charAt(0).toUpperCase()+e[i+1].slice(1):e;if(n.isPlainObject(u[o])&&i!=r)u=u[o];else{if(u[o]!==t)return s=u[o],!1;if(!n.isPlainObject(u[a])||i==r)return u[a]!==t?(s=u[a],!1):!1;u=u[a]}})),n.isFunction(s)?l=s.apply(o,i):s!==t&&(l=s),n.isArray(a)?a.push(l):a!==t?a=[a,l]:l!==t&&(a=l),s!==t?s:!1}},C.initialize()}),a!==t?a:this},n.fn.transition.exists={},n.fn.transition.settings={name:"Transition",debug:!1,verbose:!1,performance:!0,namespace:"transition",interval:0,reverse:"auto",onStart:function(){},onComplete:function(){},onShow:function(){},onHide:function(){},useFailSafe:!0,failSafeDelay:100,allowRepeats:!1,displayType:!1,animation:"fade",duration:!1,queue:!0,metadata:{displayType:"display"},className:{animating:"animating",disabled:"disabled",hidden:"hidden",inward:"in",loading:"loading",looping:"looping",outward:"out",transition:"transition",visible:"visible"},error:{noAnimation:"There is no css animation matching the one you specified. Please make sure your css is vendor prefixed, and you have included transition css.",repeated:"That animation is already occurring, cancelling repeated animation",method:"The method you called is not defined",support:"This browser does not support CSS animations"}}}(jQuery,window,document);

/*!
 * # Semantic UI 2.1.6 - API
 */
!function(e,t,r,n){"use strict";e.api=e.fn.api=function(r){var o,s=e(e.isFunction(this)?t:this),a=s.selector||"",i=(new Date).getTime(),u=[],c=arguments[0],d="string"==typeof c,l=[].slice.call(arguments,1);return s.each(function(){var s,g,f,m,p,b,v=e.isPlainObject(r)?e.extend(!0,{},e.fn.api.settings,r):e.extend({},e.fn.api.settings),h=v.namespace,y=v.metadata,R=v.selector,q=v.error,x=v.className,k="."+h,T="module-"+h,A=e(this),S=A.closest(R.form),j=v.stateContext?e(v.stateContext):A,P=this,w=j[0],D=A.data(T);b={initialize:function(){d||b.bind.events(),b.instantiate()},instantiate:function(){b.verbose("Storing instance of module",b),D=b,A.data(T,D)},destroy:function(){b.verbose("Destroying previous module for",P),A.removeData(T).off(k)},bind:{events:function(){var e=b.get.event();e?(b.verbose("Attaching API events to element",e),A.on(e+k,b.event.trigger)):"now"==v.on&&(b.debug("Querying API endpoint immediately"),b.query())}},decode:{json:function(e){if(e!==n&&"string"==typeof e)try{e=JSON.parse(e)}catch(t){}return e}},read:{cachedResponse:function(e){var r;return t.Storage===n?void b.error(q.noStorage):(r=sessionStorage.getItem(e),b.debug("Using cached response",e,r),r=b.decode.json(r),!1)}},write:{cachedResponse:function(r,o){return o&&""===o?void b.debug("Response empty, not caching",o):t.Storage===n?void b.error(q.noStorage):(e.isPlainObject(o)&&(o=JSON.stringify(o)),sessionStorage.setItem(r,o),void b.verbose("Storing cached response for url",r,o))}},query:function(){if(b.is.disabled())return void b.debug("Element is disabled API request aborted");if(b.is.loading()){if(!v.interruptRequests)return void b.debug("Cancelling request, previous request is still pending");b.debug("Interrupting previous request"),b.abort()}return v.defaultData&&e.extend(!0,v.urlData,b.get.defaultData()),v.serializeForm&&(v.data=b.add.formData(v.data)),g=b.get.settings(),g===!1?(b.cancelled=!0,void b.error(q.beforeSend)):(b.cancelled=!1,f=b.get.templatedURL(),f||b.is.mocked()?(f=b.add.urlData(f),f||b.is.mocked()?(s=e.extend(!0,{},v,{type:v.method||v.type,data:m,url:v.base+f,beforeSend:v.beforeXHR,success:function(){},failure:function(){},complete:function(){}}),b.debug("Querying URL",s.url),b.verbose("Using AJAX settings",s),"local"===v.cache&&b.read.cachedResponse(f)?(b.debug("Response returned from local cache"),b.request=b.create.request(),void b.request.resolveWith(w,[b.read.cachedResponse(f)])):void(v.throttle?v.throttleFirstRequest||b.timer?(b.debug("Throttling request",v.throttle),clearTimeout(b.timer),b.timer=setTimeout(function(){b.timer&&delete b.timer,b.debug("Sending throttled request",m,s.method),b.send.request()},v.throttle)):(b.debug("Sending request",m,s.method),b.send.request(),b.timer=setTimeout(function(){},v.throttle)):(b.debug("Sending request",m,s.method),b.send.request()))):void 0):void b.error(q.missingURL))},should:{removeError:function(){return v.hideError===!0||"auto"===v.hideError&&!b.is.form()}},is:{disabled:function(){return A.filter(R.disabled).length>0},form:function(){return A.is("form")||j.is("form")},mocked:function(){return v.mockResponse||v.mockResponseAsync},input:function(){return A.is("input")},loading:function(){return b.request&&"pending"==b.request.state()},abortedRequest:function(e){return e&&e.readyState!==n&&0===e.readyState?(b.verbose("XHR request determined to be aborted"),!0):(b.verbose("XHR request was not aborted"),!1)},validResponse:function(t){return"json"!==v.dataType&&"jsonp"!==v.dataType||!e.isFunction(v.successTest)?(b.verbose("Response is not JSON, skipping validation",v.successTest,t),!0):(b.debug("Checking JSON returned success",v.successTest,t),v.successTest(t)?(b.debug("Response passed success test",t),!0):(b.debug("Response failed success test",t),!1))}},was:{cancelled:function(){return b.cancelled||!1},succesful:function(){return b.request&&"resolved"==b.request.state()},failure:function(){return b.request&&"rejected"==b.request.state()},complete:function(){return b.request&&("resolved"==b.request.state()||"rejected"==b.request.state())}},add:{urlData:function(t,r){var o,s;return t&&(o=t.match(v.regExp.required),s=t.match(v.regExp.optional),r=r||v.urlData,o&&(b.debug("Looking for required URL variables",o),e.each(o,function(o,s){var a=-1!==s.indexOf("$")?s.substr(2,s.length-3):s.substr(1,s.length-2),i=e.isPlainObject(r)&&r[a]!==n?r[a]:A.data(a)!==n?A.data(a):j.data(a)!==n?j.data(a):r[a];return i===n?(b.error(q.requiredParameter,a,t),t=!1,!1):(b.verbose("Found required variable",a,i),i=v.encodeParameters?b.get.urlEncodedValue(i):i,t=t.replace(s,i),void 0)})),s&&(b.debug("Looking for optional URL variables",o),e.each(s,function(o,s){var a=-1!==s.indexOf("$")?s.substr(3,s.length-4):s.substr(2,s.length-3),i=e.isPlainObject(r)&&r[a]!==n?r[a]:A.data(a)!==n?A.data(a):j.data(a)!==n?j.data(a):r[a];i!==n?(b.verbose("Optional variable Found",a,i),t=t.replace(s,i)):(b.verbose("Optional variable not found",a),t=-1!==t.indexOf("/"+s)?t.replace("/"+s,""):t.replace(s,""))}))),t},formData:function(t){var r,o=e.fn.serializeObject!==n,s=o?S.serializeObject():S.serialize();return t=t||v.data,r=e.isPlainObject(t),r?o?(b.debug("Extending existing data with form data",t,s),t=e.extend(!0,{},t,s)):(b.error(q.missingSerialize),b.debug("Cant extend data. Replacing data with form data",t,s),t=s):(b.debug("Adding form data",s),t=s),t}},send:{request:function(){b.set.loading(),b.request=b.create.request(),b.is.mocked()?b.mockedXHR=b.create.mockedXHR():b.xhr=b.create.xhr(),v.onRequest.call(w,b.request,b.xhr)}},event:{trigger:function(e){b.query(),("submit"==e.type||"click"==e.type)&&e.preventDefault()},xhr:{always:function(){},done:function(t,r,n){var o=this,s=(new Date).getTime()-p,a=v.loadingDuration-s,i=e.isFunction(v.onResponse)?v.onResponse.call(o,e.extend(!0,{},t)):!1;a=a>0?a:0,i&&(b.debug("Modified API response in onResponse callback",v.onResponse,i,t),t=i),a>0&&b.debug("Response completed early delaying state change by",a),setTimeout(function(){b.is.validResponse(t)?b.request.resolveWith(o,[t,n]):b.request.rejectWith(o,[n,"invalid"])},a)},fail:function(e,t,r){var n=this,o=(new Date).getTime()-p,s=v.loadingDuration-o;s=s>0?s:0,s>0&&b.debug("Response completed early delaying state change by",s),setTimeout(function(){b.is.abortedRequest(e)?b.request.rejectWith(n,[e,"aborted",r]):b.request.rejectWith(n,[e,"error",t,r])},s)}},request:{done:function(e,t){b.debug("Successful API Response",e),"local"===v.cache&&f&&(b.write.cachedResponse(f,e),b.debug("Saving server response locally",b.cache)),v.onSuccess.call(w,e,A,t)},complete:function(e,t){var r,n;b.was.succesful()?(n=e,r=t):(r=e,n=b.get.responseFromXHR(r)),b.remove.loading(),v.onComplete.call(w,n,A,r)},fail:function(e,t,r){var o=b.get.responseFromXHR(e),a=b.get.errorFromRequest(o,t,r);"aborted"==t?(b.debug("XHR Aborted (Most likely caused by page navigation or CORS Policy)",t,r),v.onAbort.call(w,t,A,e)):"invalid"==t?b.debug("JSON did not pass success test. A server-side error has most likely occurred",o):"error"==t&&e!==n&&(b.debug("XHR produced a server error",t,r),200!=e.status&&r!==n&&""!==r&&b.error(q.statusMessage+r,s.url),v.onError.call(w,a,A,e)),v.errorDuration&&"aborted"!==t&&(b.debug("Adding error state"),b.set.error(),b.should.removeError()&&setTimeout(b.remove.error,v.errorDuration)),b.debug("API Request failed",a,e),v.onFailure.call(w,o,A,e)}}},create:{request:function(){return e.Deferred().always(b.event.request.complete).done(b.event.request.done).fail(b.event.request.fail)},mockedXHR:function(){var t,r,n,o=!1,s=!1,a=!1;return n=e.Deferred().always(b.event.xhr.complete).done(b.event.xhr.done).fail(b.event.xhr.fail),v.mockResponse?(e.isFunction(v.mockResponse)?(b.debug("Using mocked callback returning response",v.mockResponse),r=v.mockResponse.call(w,v)):(b.debug("Using specified response",v.mockResponse),r=v.mockResponse),n.resolveWith(w,[r,o,{responseText:r}])):e.isFunction(v.mockResponseAsync)&&(t=function(e){b.debug("Async callback returned response",e),e?n.resolveWith(w,[e,o,{responseText:e}]):n.rejectWith(w,[{responseText:e},s,a])},b.debug("Using async mocked response",v.mockResponseAsync),v.mockResponseAsync.call(w,v,t)),n},xhr:function(){var t;return t=e.ajax(s).always(b.event.xhr.always).done(b.event.xhr.done).fail(b.event.xhr.fail),b.verbose("Created server request",t),t}},set:{error:function(){b.verbose("Adding error state to element",j),j.addClass(x.error)},loading:function(){b.verbose("Adding loading state to element",j),j.addClass(x.loading),p=(new Date).getTime()}},remove:{error:function(){b.verbose("Removing error state from element",j),j.removeClass(x.error)},loading:function(){b.verbose("Removing loading state from element",j),j.removeClass(x.loading)}},get:{responseFromXHR:function(t){return e.isPlainObject(t)?"json"==v.dataType||"jsonp"==v.dataType?b.decode.json(t.responseText):t.responseText:!1},errorFromRequest:function(t,r,o){return e.isPlainObject(t)&&t.error!==n?t.error:v.error[r]!==n?v.error[r]:o},request:function(){return b.request||!1},xhr:function(){return b.xhr||!1},settings:function(){var e;return e=v.beforeSend.call(w,v),e&&(e.success!==n&&(b.debug("Legacy success callback detected",e),b.error(q.legacyParameters,e.success),e.onSuccess=e.success),e.failure!==n&&(b.debug("Legacy failure callback detected",e),b.error(q.legacyParameters,e.failure),e.onFailure=e.failure),e.complete!==n&&(b.debug("Legacy complete callback detected",e),b.error(q.legacyParameters,e.complete),e.onComplete=e.complete)),e===n&&b.error(q.noReturnedValue),e!==n?e:v},urlEncodedValue:function(e){var r=t.decodeURIComponent(e),n=t.encodeURIComponent(e),o=r!==e;return o?(b.debug("URL value is already encoded, avoiding double encoding",e),e):(b.verbose("Encoding value using encodeURIComponent",e,n),n)},defaultData:function(){var t={};return e.isWindow(P)||(b.is.input()?t.value=A.val():b.is.form()&&(t.text=A.text())),t},event:function(){return e.isWindow(P)||"now"==v.on?(b.debug("API called without element, no events attached"),!1):"auto"==v.on?A.is("input")?P.oninput!==n?"input":P.onpropertychange!==n?"propertychange":"keyup":A.is("form")?"submit":"click":v.on},templatedURL:function(e){if(e=e||A.data(y.action)||v.action||!1,f=A.data(y.url)||v.url||!1)return b.debug("Using specified url",f),f;if(e){if(b.debug("Looking up url for action",e,v.api),v.api[e]===n&&!b.is.mocked())return void b.error(q.missingAction,v.action,v.api);f=v.api[e]}else b.is.form()&&(f=A.attr("action")||j.attr("action")||!1,b.debug("No url or action specified, defaulting to form action",f));return f}},abort:function(){var e=b.get.xhr();e&&"resolved"!==e.state()&&(b.debug("Cancelling API request"),e.abort())},reset:function(){b.remove.error(),b.remove.loading()},setting:function(t,r){if(b.debug("Changing setting",t,r),e.isPlainObject(t))e.extend(!0,v,t);else{if(r===n)return v[t];v[t]=r}},internal:function(t,r){if(e.isPlainObject(t))e.extend(!0,b,t);else{if(r===n)return b[t];b[t]=r}},debug:function(){v.debug&&(v.performance?b.performance.log(arguments):(b.debug=Function.prototype.bind.call(console.info,console,v.name+":"),b.debug.apply(console,arguments)))},verbose:function(){v.verbose&&v.debug&&(v.performance?b.performance.log(arguments):(b.verbose=Function.prototype.bind.call(console.info,console,v.name+":"),b.verbose.apply(console,arguments)))},error:function(){b.error=Function.prototype.bind.call(console.error,console,v.name+":"),b.error.apply(console,arguments)},performance:{log:function(e){var t,r,n;v.performance&&(t=(new Date).getTime(),n=i||t,r=t-n,i=t,u.push({Name:e[0],Arguments:[].slice.call(e,1)||"","Execution Time":r})),clearTimeout(b.performance.timer),b.performance.timer=setTimeout(b.performance.display,500)},display:function(){var t=v.name+":",r=0;i=!1,clearTimeout(b.performance.timer),e.each(u,function(e,t){r+=t["Execution Time"]}),t+=" "+r+"ms",a&&(t+=" '"+a+"'"),(console.group!==n||console.table!==n)&&u.length>0&&(console.groupCollapsed(t),console.table?console.table(u):e.each(u,function(e,t){console.log(t.Name+": "+t["Execution Time"]+"ms")}),console.groupEnd()),u=[]}},invoke:function(t,r,s){var a,i,u,c=D;return r=r||l,s=P||s,"string"==typeof t&&c!==n&&(t=t.split(/[\. ]/),a=t.length-1,e.each(t,function(r,o){var s=r!=a?o+t[r+1].charAt(0).toUpperCase()+t[r+1].slice(1):t;if(e.isPlainObject(c[s])&&r!=a)c=c[s];else{if(c[s]!==n)return i=c[s],!1;if(!e.isPlainObject(c[o])||r==a)return c[o]!==n?(i=c[o],!1):(b.error(q.method,t),!1);c=c[o]}})),e.isFunction(i)?u=i.apply(s,r):i!==n&&(u=i),e.isArray(o)?o.push(u):o!==n?o=[o,u]:u!==n&&(o=u),i}},d?(D===n&&b.initialize(),b.invoke(c)):(D!==n&&D.invoke("destroy"),b.initialize())}),o!==n?o:this},e.api.settings={name:"API",namespace:"api",debug:!1,verbose:!1,performance:!0,api:{},cache:!0,interruptRequests:!0,on:"auto",stateContext:!1,loadingDuration:0,hideError:"auto",errorDuration:2e3,encodeParameters:!0,action:!1,url:!1,base:"",urlData:{},defaultData:!0,serializeForm:!1,throttle:0,throttleFirstRequest:!0,method:"get",data:{},dataType:"json",mockResponse:!1,mockResponseAsync:!1,beforeSend:function(e){return e},beforeXHR:function(e){},onRequest:function(e,t){},onResponse:!1,onSuccess:function(e,t){},onComplete:function(e,t){},onFailure:function(e,t){},onError:function(e,t){},onAbort:function(e,t){},successTest:!1,error:{beforeSend:"The before send function has aborted the request",error:"There was an error with your request",exitConditions:"API Request Aborted. Exit conditions met",JSONParse:"JSON could not be parsed during error handling",legacyParameters:"You are using legacy API success callback names",method:"The method you called is not defined",missingAction:"API action used but no url was defined",missingSerialize:"jquery-serialize-object is required to add form data to an existing data object",missingURL:"No URL specified for api event",noReturnedValue:"The beforeSend callback must return a settings object, beforeSend ignored.",noStorage:"Caching responses locally requires session storage",parseError:"There was an error parsing your request",requiredParameter:"Missing a required URL parameter: ",statusMessage:"Server gave an error: ",timeout:"Your request timed out"},regExp:{required:/\{\$*[A-z0-9]+\}/g,optional:/\{\/\$*[A-z0-9]+\}/g},className:{loading:"loading",error:"error"},selector:{disabled:".disabled",form:"form"},metadata:{action:"action",url:"url"}}}(jQuery,window,document);

})