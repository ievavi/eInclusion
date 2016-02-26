(function($) {

	$.fn.ddTableFilterColor = function(options) {
		options = $.extend(true, $.fn.ddTableFilterColor.defaultOptions, options);

		return this.each(function() {
			if ($(this).hasClass('ddtfc-processed')) {
				refreshFilters(this);
				return;
			}
			var table = $(this);
			var start = new Date();

			$('th:visible', table).each(
					function(index) {
						if ($(this).hasClass('skip-filter'))
							return;
						if ($(this).hasClass('colored')){
							var selectbox = $('<select>');
							var values = [];
							var opts = [];
							selectbox.append('<option value="--all--">'
									+ $(this).text() + '</option>');
							selectbox.append('<option value="green">green</option>');
							selectbox.append('<option value="yellow">yellow</option>');
							selectbox.append('<option value="red">red</option>');
							selectbox.append('<option value="gray">gray</option>');
	
							var col = $(
									'tr:not(.skip-filter) td:nth-child('
											+ (index + 1) + ')', table).each(
									function() {
										var cellVal = 'test';
										if ($(this).hasClass('green')) {
											cellVal = 'green';
										}
										if ($(this).hasClass('yellow')) {
											cellVal = 'yellow';
										}
										if ($(this).hasClass('red')) {
											cellVal = 'red';
										}
										if ($(this).hasClass('gray')) {
											cellVal = 'gray';
										}
					
										if (cellVal.length == 0) {
											cellVal = '--empty--';
										}
										$(this).attr('ddtfc-value', cellVal);
	
										if ($.inArray(cellVal, values) === -1) {
											var cellText = options.textCallback
													.apply(this);
											if (cellText.length == 0) {
												cellText = options.emptyText;
											}
											values.push(cellVal);
											opts.push({
												val : cellVal,
												text : cellText
											});
										}
									});
							if (opts.length < options.minOptions) {
								return;
							}
							if (options.sortOpt) {
								opts.sort(options.sortOptCallback);
							}
//							$.each(opts, function() {
//								$(selectbox).append(
//										'<option value="' + this.val + '">'
//												+ this.text + '</option>')
//							});
	
							$(this).wrapInner('<div style="display:none">');
							$(this).append(selectbox);
	
							selectbox.bind('change', {
								column : col
							}, function(event) {
								var changeStart = new Date();
								var value = $(this).val();
	
								event.data.column.each(function() {
									if ($(this).attr('ddtfc-value') === value
											|| value == '--all--') {
										$(this).removeClass('ddtfc-filtered');
									} else {
										$(this).addClass('ddtfc-filtered');
									}
								});
								var changeStop = new Date();
								if (options.debug) {
									console.log('Search: '
											+ (changeStop.getTime() - changeStart
													.getTime()) + 'ms');
								}
								refreshFilters(table);
	
							});
						};
						table.addClass('ddtfc-processed');
						if ($.isFunction(options.afterBuild)) {
							options.afterBuild.apply(table);
						}
					});

			function refreshFilters(table) {
				var refreshStart = new Date();
				$('tr', table).each(
						function() {
							var row = $(this);
							if ($('td.ddtfc-filtered', row).length > 0) {
								options.transition.hide.apply(row,
										options.transition.options);
							} else {
								options.transition.show.apply(row,
										options.transition.options);
							}
						});

				if ($.isFunction(options.afterFilter)) {
					options.afterFilter.apply(table);
				}

				if (options.debug) {
					var refreshEnd = new Date();
					console.log('Refresh: '
							+ (refreshEnd.getTime() - refreshStart.getTime())
							+ 'ms');
				}
			}

			if (options.debug) {
				var stop = new Date();
				console.log('Build: ' + (stop.getTime() - start.getTime())
						+ 'ms');
			}
		});
	};

	$.fn.ddTableFilterColor.defaultOptions = {
		valueCallback : function() {
			return encodeURIComponent($.trim($(this).text()));
		},
		textCallback : function() {
			return $.trim($(this).text());
		},
		sortOptCallback : function(a, b) {
			return a.text.toLowerCase() > b.text.toLowerCase();
		},
		afterFilter : null,
		afterBuild : null,
		transition : {
			hide : $.fn.hide,
			show : $.fn.show,
			options : []
		},
		emptyText : '--Empty--',
		sortOpt : true,
		debug : false,
		minOptions : 2
	}

})(jQuery);
