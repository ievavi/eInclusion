(function($) {

$.fn.ddTableFilter = function(options) {
<<<<<<< HEAD
  options = $.extend($.fn.ddTableFilter.defaultOptions, options);
  
=======
  options = $.extend(true, $.fn.ddTableFilter.defaultOptions, options);

>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
  return this.each(function() {
    if($(this).hasClass('ddtf-processed')) {
      refreshFilters(this);
      return;
    }
    var table = $(this);
    var start = new Date();
<<<<<<< HEAD
    
=======

>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
    $('th:visible', table).each(function(index) {
      if($(this).hasClass('skip-filter')) return;
      var selectbox = $('<select>');
      var values = [];
<<<<<<< HEAD
      var opts = new Array();
      selectbox.append('<option value="--all--">' + $(this).text() + 
'</option>');
      
      var col = $('td:nth-child(' + (index + 1) + 
')').each(function(index) {
=======
      var opts = [];
      selectbox.append('<option value="--all--">' + $(this).text() + '</option>');

      var col = $('tr:not(.skip-filter) td:nth-child(' + (index + 1) + ')', table).each(function() {
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
        var cellVal = options.valueCallback.apply(this);
        if(cellVal.length == 0) {
          cellVal = '--empty--';
        }
        $(this).attr('ddtf-value', cellVal);
<<<<<<< HEAD
        
        if($.inArray(cellVal, values) === -1) {
          var cellText = options.textCallback.apply(this);
          if(cellText.length == 0) { cellText = options.emptyText; }
=======

        if($.inArray(cellVal, values) === -1) {
          var cellText = options.textCallback.apply(this);
          if(cellText.length == 0) {cellText = options.emptyText;}
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
          values.push(cellVal);
          opts.push({val:cellVal, text:cellText});
        }
      });
      if(opts.length < options.minOptions){
        return;
<<<<<<< HEAD
      } 
=======
      }
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
      if(options.sortOpt) {
        opts.sort(options.sortOptCallback);
      }
      $.each(opts, function() {
<<<<<<< HEAD
        $(selectbox).append('<option value="' + this.val + '">' + 
this.text + '</option>')
=======
        $(selectbox).append('<option value="' + this.val + '">' + this.text + '</option>')
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
      });

      $(this).wrapInner('<div style="display:none">');
      $(this).append(selectbox);
<<<<<<< HEAD
      
      selectbox.bind('change', {column:col}, function(event) {
        var changeStart = new Date();
        var value = $(this).val();
        
        event.data.column.each(function() {
          if($(this).attr('ddtf-value') === value || value == '--all--') 
{
=======

      selectbox.bind('change', {column:col}, function(event) {
        var changeStart = new Date();
        var value = $(this).val();

        event.data.column.each(function() {
          if($(this).attr('ddtf-value') === value || value == '--all--') {
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
            $(this).removeClass('ddtf-filtered');
          }
          else {
            $(this).addClass('ddtf-filtered');
          }
        });
        var changeStop = new Date();
        if(options.debug) {
<<<<<<< HEAD
          console.log('Search: ' + (changeStop.getTime() - 
changeStart.getTime()) + 'ms');
        }
        refreshFilters(table);
        var changeStop = new Date();
        
      });
      table.addClass('ddtf-processed');
    });
    
    
=======
          console.log('Search: ' + (changeStop.getTime() - changeStart.getTime()) + 'ms');
        }
        refreshFilters(table);

      });
      table.addClass('ddtf-processed');
      if($.isFunction(options.afterBuild)) {
        options.afterBuild.apply(table);
      }
    });

>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
    function refreshFilters(table) {
      var refreshStart = new Date();
      $('tr', table).each(function() {
        var row = $(this);
        if($('td.ddtf-filtered', row).length > 0) {
<<<<<<< HEAD
          options.transition.hide.apply(row, 
options.transition.options);
        }
        else {
          options.transition.show.apply(row, 
options.transition.options);
        }
      });
      var refreshEnd = new Date();
      if(options.debug) {
        console.log('Refresh: ' + (refreshEnd.getTime() - 
refreshStart.getTime()) + 'ms');
      }
    }
    
    var stop = new Date();
    if(options.debug) {
      console.log('Build: ' + (stop.getTime() - start.getTime()) + 
'ms');
    }
  });
  
}

$.fn.ddTableFilter.defaultOptions = {
  valueCallback:function() {
    return escape($.trim($(this).text()));
=======
          options.transition.hide.apply(row, options.transition.options);
        }
        else {
          options.transition.show.apply(row, options.transition.options);
        }
      });

      if($.isFunction(options.afterFilter)) {
        options.afterFilter.apply(table);
      }

      if(options.debug) {
        var refreshEnd = new Date();
        console.log('Refresh: ' + (refreshEnd.getTime() - refreshStart.getTime()) + 'ms');
      }
    }

    if(options.debug) {
      var stop = new Date();
      console.log('Build: ' + (stop.getTime() - start.getTime()) + 'ms');
    }
  });
};

$.fn.ddTableFilter.defaultOptions = {
  valueCallback:function() {
    return encodeURIComponent($.trim($(this).text()));
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
  },
  textCallback:function() {
    return $.trim($(this).text());
  },
  sortOptCallback: function(a, b) {
<<<<<<< HEAD
    return a.text.toLowerCase() > b.text.toLowerCase(); 
  },
=======
    return a.text.toLowerCase() > b.text.toLowerCase();
  },
  afterFilter: null,
  afterBuild: null,
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
  transition: {
    hide:$.fn.hide,
    show:$.fn.show,
    options: []
  },
  emptyText:'--Empty--',
  sortOpt:true,
  debug:false,
  minOptions:2
}
<<<<<<< HEAD
 
})(jQuery);

;
=======

})(jQuery);
>>>>>>> 0f265998de72e23fc1ffbe1de330c49a865378e5
