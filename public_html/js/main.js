'use strict';

// GLOBALS
// Si ALERT_ERROR es TRUE se mostrará todos los errores de validación en un Alert
var ALERT_ERROR = false;

// Si GROWL_STATIC es TRUE se mostrará los mensajes(OK, ERROR, NOTIFICATION) al principio del contenedor principal
var GROWL_STATIC = true;

// Config Jquery
(function($) {
    $.ajaxSetup({
        dataFilter : function (response, type) {
            Anb.loading.hide();
            if (Anb.isJson(response)) {
                var json = JSON.parse(response);
                if (json.state == 'ERROR') {
                    Anb.load.resetSave();
                    Anb.message.error(json.data);
                }
                if (json.state == 'WARNING') {
                    Anb.load.resetSave();
                    Anb.message.notification(json.data, null, 0, '¡Advertencia!');
                }                
                /*if (json.state == 'OK' || json.state == 'SUCCESS') {
                    Anb.message.ok(json.data);
                }*/
            } else {
                if (response === 'ERROR') {
                    Anb.load.resetSave();
                    Anb.message.error('Se produjo un error al momento de procesar su solicitud');
                }
                if (response == 'WARNING') {
                    Anb.load.resetSave();
                    Anb.message.notification('El sistema no está disponible por ahora', null, 0, '¡Advertencia!');
                }
                /*if (response === 'OK' || response === 'SUCCESS') {
                    Anb.message.ok();
                }*/
            }
            if (response == 'SESSION_EXPIRED' || response == 'ERROR_PERMISSION') {
                Anb.modal.hide();
                Anb.modal.hide("second-modal");
                $('.dataTables_processing').hide();
                if (response == 'SESSION_EXPIRED') {
                    Anb.$alert_modal.load('loginAjaxForm.do', function () {
                        Anb.$alert_modal.modal({
                            keyboard : false
                        }).modal('show')
                        .off('click.dismiss.bs.modal');
                    });
                    throw new Error('Session expired.');
                } else {
                    Anb.message.error("No tiene los permisos necesarios");
                    throw new Error('Error permission.');
                }
            }
            return response;
        },
        beforeSend : function () {
            if (Anb.loading.$container.css('display') == 'none') {
                Anb.loading.show();
            }
            Anb.message.hideAll();
        }
    });
    //Datepicker
    if ($.fn.datepicker) {
        $.fn.datepicker.dates['es'] = {
            days: ["Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"],
            daysShort: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"],
            daysMin: ["Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"],
            months: ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
            monthsShort: ["Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"],
            today: "Hoy"
        };
    }
    //Datatable
    if ($.fn.dataTable) {
        $.extend( true, $.fn.dataTable.defaults, {
            "sDom" : "<'row'<'col-xs-12 col-md-4'><'col-xs-12 col-md-8 text-right dt-filter'fl>r>t<'row'<'col-xs-12 col-md-5'i><'col-xs-12 col-md-7'p>>", 
            "bFilter" : false, 
            "sPaginationType" : "full_numbers", 
            //"aoColumnDefs" : [{"bVisible" : false, "aTargets" : [0]}], 
            "aLengthMenu" : [[10, 25, 50,  - 1], [10, 25, 50, "Todos"]], 
            "oLanguage" : {
                "sSearch" : "Buscar",
                "sLengthMenu" : "Registros _MENU_", 
                "sZeroRecords" : "No existe registros.", 
                "sInfo" : "Mostrando _START_ al _END_ de _TOTAL_ registros", 
                "sInfgetoEmpty" : "Mostrando 0 al 0 de 0 registros", 
                "sProcessing" : "Cargando registros",
                "sEmptyTable" : "No existe registros para mostrar",
                "sInfoEmpty": "",
                "sInfoFiltered": "",
                "oPaginate" : {
                    "sNext" : "&raquo;", "sPrevious" : "&laquo;", "sFirst" : "Primero", "sLast" : "Último"
                }
            }
        });
    }

}(jQuery));

// Main Module
var Anb = (function (anb, $) {

    var tmpl_alert = $('#tmpl-alert').html();
    var tmpl_print = $('#tmpl-print').html();
    var $alert_modal = $("#alert-modal");
    var nano = function (template, data) {
        return template.replace(/\{([\w\.]*)\}/g, function (str, key) {
            var keys = key.split("."), v = data[keys.shift()];
            for (var i = 0, l = keys.length;i < l;i++)
                v = v[keys[i]];
            return (typeof v !== "undefined" && v !== null) ? v : "";
        });
    }

    var isIE = function () {
        if (navigator.appName == 'Microsoft Internet Explorer') {
            var re = new RegExp("MSIE ([0-9]{1,}[.0-9]{0,})");
            if (re.exec(navigator.userAgent) != null) {
                return parseFloat( RegExp.$1 );
            }
        }
        return -1;
    }
    
    anb.types = ['text', 'textarea', 'file', 'password',  'email', 'search', 'number'];
    anb.$alert_modal = $alert_modal;
    anb.nano = nano;
    anb.ie = isIE();
    
    anb.toType = function (obj) {
        return ({}).toString.call(obj).match(/\s([a-zA-Z]+)/)[1].toLowerCase();
    }
    
    anb.isJson = function (text) {
        return /^[\],:{}\s]*$/.test(text.replace(/\\["\\\/bfnrtu]/g, '@').
               replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
               replace(/(?:^|:|,)(?:\s*\[)+/g, ''));
    }
    
    anb.alert = function (message, callback_ok, title) {
        $alert_modal.html(nano(tmpl_alert, {
            title : title || 'Alerta', 
            message : message, 
            button_cancel : 'hide'
        })).modal();
        if (callback_ok) {
            $alert_modal.find('.modal-footer .btn-primary').on('click', callback_ok);
        }
    }

    anb.confirm = function (message, callback_ok, callback_cancel, title, label_ok, label_cancel) {
        $alert_modal.html(nano(tmpl_alert, {
            title : title || 'Confirmar', 
            message : message, 
            button_cancel : ''
        })).modal();

        var $btn_ok = $alert_modal.find('.modal-footer .btn-primary'),
            $btn_cancel = $alert_modal.find('.modal-footer .btn-default');

        $btn_ok.children('span').html(label_ok || 'Aceptar');
        $btn_cancel.children('span').html(label_cancel || 'Cancelar');
        if (callback_ok) {
            $btn_ok.on('click', callback_ok);
        }
        if (callback_cancel) {
            $btn_cancel.on('click', callback_cancel);
        }
    }

    anb.question = function (message, callback_yes, callback_no, title) {
        Anb.confirm(message, callback_yes, callback_no, title || 'Pregunta', 'Si', 'No');
    }
    
    anb.getParams = function (p, args) {
        var first = p.first.defaultValue,
            second = p.second.defaultValue;
            
        p.first.position = p.first.position || 0;
        p.second.position = p.second.position || 1;
        
        var type = Anb.toType(args[p.first.position]);
        if (type != 'undefined') {          
            if (type == p.first.type) {
                first = args[p.first.position];
                second = Anb.toType(args[p.second.position]) == p.second.type ? args[p.second.position] : p.second.defaultValue;
            } else {
                if (type == p.second.type) {
                    first = Anb.toType(args[p.second.position]) == p.first.type ? args[p.second.position] : p.first.defaultValue;
                    second = args[p.first.position];
                }
            }
        }
        return {
            first : first,
            second : second
        };
    }

    anb.print = function (html, css) {
        if (typeof css == 'string') {
            Anb.loading.show('Imprimiendo');
            $.get(css, function (response) {
                var popup = window.open('', 'print');
                popup.document.write(Anb.nano(tmpl_print, {body : html, css : response}));
                popup.document.close();
                popup.focus();
                popup.print();
                popup.close();
            });
        } else {
            var popup = window.open('', 'print');
            popup.document.write(Anb.nano(tmpl_print, {body : html, css : css}));
            popup.document.close();
            popup.focus();
            popup.print();
            popup.close();
        }

        return true;
    }
    
    anb.fullscreen = function () {
        if (!document.fullscreenElement &&    // alternative standard method
        !document.mozFullScreenElement && !document.webkitFullscreenElement && !document.msFullscreenElement ) {  // current working methods
            if (document.documentElement.requestFullscreen) {
                document.documentElement.requestFullscreen();
            } else if (document.documentElement.msRequestFullscreen) {
                document.documentElement.msRequestFullscreen();
            } else if (document.documentElement.mozRequestFullScreen) {
                document.documentElement.mozRequestFullScreen();
            } else if (document.documentElement.webkitRequestFullscreen) {
                document.documentElement.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
            }
        } else {
            if (document.exitFullscreen) {
                document.exitFullscreen();
            } else if (document.msExitFullscreen) {
                document.msExitFullscreen();
            } else if (document.mozCancelFullScreen) {
                document.mozCancelFullScreen();
            } else if (document.webkitExitFullscreen) {
                document.webkitExitFullscreen();
            }
        }
    }

    return anb;
}(Anb || {}, jQuery));

// Loading
Anb.loading = (function () {

    var $container = $('#loading-ajax');

    var loading = {};

    loading.$container = $container;
    
    loading.show = function (text) {
        $container.html('<i class="fa fa-cog fa-spin"></i>' + (text || 'Cargando...')).css({
            marginLeft : '-' + $container.outerWidth() / 2 + 'px'
        }).fadeIn(200);
    }

    loading.hide = function () {
        $container.fadeOut(200);
    }

    return loading;

}());

// Selector
Anb.selector = (function () {

    var selector = {};

    selector.visible = function (query, node) {
        var arr = [],
            el = null,
            nodes = node ? node.querySelectorAll(query) : document.querySelectorAll(query);
        for (var i = nodes.length - 1; i >= 0; i--) {
            el = nodes.item(i);
            if (el.type != 'hidden' && el.style.display != 'none' && el.style.visibility != 'hidden' && el.parentNode.style.display != 'none' && el.parentNode.style.visibility != 'hidden'  && el.parentNode.parentNode.style.display != 'none' && el.parentNode.parentNode.style.visibility != 'hidden') {
                arr.push(el);
            }
        }
        return arr;
    }

    return selector;

}());

// Backup
Anb.backup = function (form, btn) {

    var backup = [];
    var $form = null;
    var $btn_submit = null;

    var change = function (obj) {
        for (var i = 0, l = backup.length; i < l; i++) {
            if (obj.name == backup[i].name) {
                backup[i].change = backup[i].checked ? obj.checked.toString() != backup[i].checked : obj.value != backup[i].value;
                break;
            }
        }
    }

    var isChange = function () {
        for (var i = 0, l = backup.length; i < l; i++) 
            if (backup[i].change) 
                return false;
        return true;
    }
    
    var setChanges = function (obj) { 
        change(obj);
        $btn_submit.prop({disabled: isChange()});
    }

    var init = function () {
        
        $form = $(form);
        $btn_submit = btn ? $(btn) : $form.find('[type=submit]');

        $btn_submit.prop('disabled', true);
        $form.find('textarea, [type=text], [type=file], [type=password], [type=email], [type=search], [type=number]').each(function () {
            backup.push({
                name   : this.name,
                value  : this.value,
                change : false
            });
        }).on('paste cut', function () {
            var el = this;
            setTimeout(function () {
                setChanges(el);
            }, 1);
        }).on('keyup blur select', function () {
            setChanges(this);
        });
        $form.find("select").each(function () {
            backup.push({
                name : this.name,
                value : this.value,
                change : false
            });
        }).on("change blur", function () {
            setChanges(this);
        });
        $form.find('[type=checkbox]').each(function () {
            backup.push({
                name    : this.name,
                value   : this.value,
                checked : this.checked.toString(),
                change  : false
            });    
        }).on('click blur', function () {
            setChanges(this);
        });

        var radios = [], 
            checkeds = [];
        var elements = $form[0].querySelectorAll('[type=radio]');
        for (var i = elements.length - 1; i >= 0; i--) {
            radios[elements[i].name] = '';
        }
        elements = $form[0].querySelectorAll('[type=radio]:checked');
        for (var i = elements.length - 1; i >= 0; i--) {
            checkeds[elements[i].name] = elements[i].value;
        }
        for (var name in radios) {
            backup.push({
                name   : name,
                value  : checkeds[name] || null,
                change : false
            });
        }
        $form.find('[type=radio]').on("click", function () {
            setChanges(this);
        }).on('blur', function () {
            setChanges(this);
        });
    }

    init ();

    return backup;
};

// Modal
Anb.modal = (function () {

    var main = 'main-modal';
    var second = 'second-modal';
    var modal = {};

    modal.main = main; 
   
    // Parameters url, size, callback
    modal.show = function (url) {
        var $modal = $('#' + main + ' .modal-content');        
        var parameters = Anb.getParams({
            first : {
                type : 'string',
                position : 1
            },
            second : {
                type : 'function',
                position : 2
            }
        }, arguments);
        var size = parameters.first, 
            callback = parameters.second,
            show = typeof arguments[3] == 'undefined';
                     
        if (size && size.length) {
            $modal.parent().addClass('modal-' + size);
        } else {
            $modal.parent().removeClass('modal-lg').removeClass('modal-sm');
        }
        $.get(url, function (response) {
            if (response.state && response.state == "ERROR") {
                return false;
            }
            $modal.html(response);
            $modal = $('#' + main);
            if (show) {
                $modal.modal({
                    backdrop : 'static'
                }); //Show modal
            }
            var input = $modal.find('input[type=text], textarea').get(0);
            if (input && !input.classList.contains('datepicker')) {
                if (show) {
                    setTimeout(function () {input.focus()}, 500);
                } else {
                    input.focus();
                }
            }           

            Anb.load.render('#' + main);

            if ($modal.find("form").length) {
                new Anb.backup($modal.find("form")[0]);
            }

            if (callback) {
                setTimeout(function () {
                    callback.apply(window);
                }, 200);
            }
        });
    }

    modal.update = function (url, callback) {
        modal.show(url, callback, null, false);
    }
    
    // Parameters url, size, callback
    modal.secondShow = function (url) {
        
        var $modal = $('#' + second + ' .modal-content');        
        var parameters = Anb.getParams({
            first : {
                type : 'string',
                position : 1
            },
            second : {
                type : 'function',
                position : 2
            }
        }, arguments);
        var size = parameters.first, callback = parameters.second;
                     
        if (size && size.length) {
            $modal.parent().addClass('modal-' + size);
        } else {
            $modal.parent().removeClass('modal-lg').removeClass('modal-sm');
        }
        $.get(url, function (response) {
            if (response.state) {
                if (response.state == "ERROR") {
                    Anb.message.error(response.data || "Se produjo un error al momento de procesar su solicitud.");
                }
                return false;
            }
            $modal.html(response);
            $modal = $('#' + second);
            
            var input = $modal.modal().find('input[type=text], textarea').get(0);
            if (input) {
                setTimeout(function () {input.focus()}, 500);
            }
            if (callback) {
                setTimeout(function () {
                    callback.apply(window);
                }, 200);
            }
        });
    }

    modal.hide = function (id) {
        $('#' + (id || main)).modal('hide');
    }
    
    modal.secondHide = function () {
        $('#' + second).modal('hide');
    }

    return modal;

}());

// Filters
Anb.filter = (function () {

    var exceptions = {
        integer      : [46, 8, 9, 27, 13, 110, 190, 173],
        natural      : [46, 8, 9, 27, 13, 110, 190],
        decimal      : [46, 8, 9, 27, 13, 110, 188, 190, 173],
        numeric      : [46, 8, 9, 27, 13],
        alpha        : [46, 8, 9, 27, 13],
        alpha_dash   : [46, 8, 9, 27, 13, 173]
    }
    exceptions.alpha_numeric = exceptions.alpha;
    
    var evaluate = {
        numeric : function (e) {
            return (e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105);
        },
        alpha : function (e) {
            return e.shiftKey || (e.keyCode < 65 || e.keyCode > 90);
        }
    }
    evaluate.integer = evaluate.numeric;
    evaluate.decimal = evaluate.numeric;
    evaluate.natural = evaluate.numeric;
    evaluate.alpha_numeric = function (e) {
        return evaluate.alpha(e) && evaluate.numeric(e);
    };
    evaluate.alpha_dash = evaluate.alpha_numeric;
    
    var filterBase = function (e, type) {
        if (exceptions[type].indexOf(e.keyCode) !== -1 || (e.keyCode == 86 && e.ctrlKey === true) || (e.keyCode == 65 && e.ctrlKey === true) || (e.keyCode >= 35 && e.keyCode <= 39)) {
            return;
        }   
        if (evaluate[type](e)) {
            e.preventDefault();
        }
    }
    
    var filter = {};

    filter.isNumber = function(o) {
        return typeof o === 'number' && isFinite(o);
    }
    
    filter.isFloat = function (value) {
        return value % 1 != 0;
    }
    
    filter.integer = function (e) {
        filterBase(e, 'integer');
    }
    
    filter.isInteger = function (value) {
        if (/[a-zA-Z]+/g.test(value) || !/^-?[0-9.]*$/g.test(value)) {
            return false;
        }
        value = Anb.number.convert(value);
        if (value === 'NaN' || !Anb.filter.isNumber(value)) {
            return false;
        }
        return !Anb.filter.isFloat(value);
    }
    
    filter.decimal = function (e) {
        filterBase(e, 'decimal');
    }
    
    filter.isDecimal = function (value) {
        if (/[a-zA-Z]+/g.test(value) || !/^-?[0-9.]+\,?[0-9]*$/g.test(value)) {
            return false;
        }
        value = Anb.number.convert(value);
        if (value === 'NaN') {
            return false;
        }
        return Anb.filter.isNumber(value);
    }
    
    filter.natural = function (e) {
        filterBase(e, 'natural');
    }

    filter.isNatural = function (value) {
        if (/[a-zA-Z]+/g.test(value) || !/^[0-9]*$/g.test(value)) {
            return false;
        }
        value = Anb.number.convert(value);
        if (value === 'NaN' || !Anb.filter.isNumber(value)) {
            return false;
        }
        return !Anb.filter.isFloat(value) && value >= 0;
    }
    
    filter.numeric = function (e) {
        filterBase(e, 'numeric');
    }
    
    filter.isNumeric = function (value) {
        return /^([0-9])*$/.test(value);
    }
    
    filter.alpha = function (e) {
        filterBase(e, 'alpha');
    }
    
    filter.isAlpha = function (value) {
        return /^[\u00F1A-Za-z]*$/.test(value);
    }
    
    filter.alphaNumeric = function (e) {
        filterBase(e, 'alpha_numeric');
    }
    
    filter.isAlphaNumeric = function (value) {
        return /^[\u00F1A-Za-z0-9]*$/.test(value);
    }
    
    filter.alphaDash = function (e) {
        filterBase(e, 'alpha_dash');
    }
    
    filter.isAlphaDash = function (value) {
        return /^[\u00F1A-Za-z-_0-9]*$/.test(value);
    }
    
    filter.removeTagHTML = function (text) {
        return text.replace(/<([^ >]+)[^>]*>.*?<\/\1>|<[^\/]+\/>/gi, "");
    }

    filter.empty = function (value) {
        return value == null || value.length == 0 || /^\s+$/.test(value);
    }

    filter.isEmail = function (value) {
        return /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(value); 
    }

    return filter;

}());

// Form
Anb.form = (function () {

    var counter = 0;
    var RESPONSE = ['OK', 'AJAX', 'CREATE', 'CREATED', 'CREATE-AJAX', 'CREATED-AJAX', 'UPDATE', 'UPDATE-AJAX', 'MAIL', 'CREATE-AND-MAIL', 'UPDATE-AND-MAIL' ];
    var CREATE = ['OK', 'CREATE', 'CREATED', 'CREATE-AJAX'];

    var inputBase = function (query, type) {
        $(query).on('keydown', function (e) {
            Anb.filter[type](e, this.value);
        }).on('paste', function () {
            var el = this;
            setTimeout(function () {
                if (!Anb.filter['is' + type.substr(0,1).toUpperCase() + type.substr(1)](el.value)) {
                    el.value = '';
                }
            }, 1);
        });
    }
    var form = {};

    form.integer = function (query) {
        inputBase(query, 'integer');
    }
    form.decimal = function (query) {
        inputBase(query, 'decimal');
    }
    form.numeric = function (query) {
        inputBase(query, 'numeric');
    }
    form.natural = function (query) {
        inputBase(query, 'natural');
    }
    form.alpha = function (query) {
        inputBase(query, 'alpha');
    }
    form.alphaNumeric = function (query) {
        inputBase(query, 'alphaNumeric');
    }
    form.alphaDash = function (query) {
        inputBase(query, 'alphaDash');
    }

    form.maxlength = function (query) {
        counter = $('.counter').length;
        $((query || '') + ' [data-maxlength]').each(function () {
            var $this = $(this),
                maxlength = $this.data('maxlength');
            counter++;
            $this.prop('maxlength', maxlength).attr('data-counter', counter);
            $this.after('<span class="counter" id="counter-' + counter + '">' + (maxlength - this.value.length) + '</span>');  
        }).on('keyup', function() {
            var el = this,
                $this = $(this),
                maxlength = $this.data('maxlength'),
                length = el.value.length;
            $('#counter-' + $this.data('counter')).html(maxlength - length);
            if(Anb.ie > 0 && Anb.ie < 10 && length > maxlength) {
                el.value = el.value.substr(0, maxlength);
            }
        }).on('blur', function () {
            this.value = this.value.replace(/\n/g, ' ');
        }).parent().css({overflow : 'hidden'});
    }

    /*
     * @param form
     * @param url
     * @param callback
     */
    form.ajax = function () {
        var params = Anb.getParams({
            first : {
                type : 'htmlformelement',
                defaultValue : 'main-table'
            },
            second : {
                type : 'string',
                defaultValue : ''
            }
        }, arguments);
        
        var form = params.first, 
            url = params.second, 
            callback = arguments[2] || {},
            $form = $(form),
            $button = $form.find('[type=submit]').prop({disabled : true});
        
        $button.find('> span:first').hide();
        $button.find('> span:last').show();

        if ($form.find('[type=file]').length) {
            $.ajax({
                url         : url,
                data        : new FormData(form),
                cache       : false,
                contentType : false,
                processData : false,
                type        : 'POST',
                success     : function(response){callback.apply(window, [response])}
            });
        } else {
            $.post(url, $form.serialize(), function(response) {callback.apply(window, [response])});
        }
        return false;
    }
    
    form.submit = function (form, callback) {
        var $form = $(form),
            $btn_submit = $form.find('[type=submit]'),
            form = $form[0],
            backup = new Anb.backup(form);            

        $form.on('submit', function (e) {
            e.preventDefault();
            callback.apply(window, [form]);
        }).find('[data-form=clean]').on('click', function () {
            Anb.form.clean(form);
            $btn_submit.prop({disabled : true});            
        });
        $form.find('[type=reset]').on('click', function () {
            Anb.form.cleanErrors(form);
            $btn_submit.prop({disabled : true});
            var elements = form.querySelectorAll('textarea[maxLength]');
            setTimeout(function () {
                for (var i=0, l = elements.length; i < l; i++) {
                    elements[i].nextSibling.innerHTML = elements[i].maxLength - elements[i].value.length;
                }          
            }, 100);
        });;
    }
    
    form.login = function (form) {
        Anb.loading.show('Ingresando al sistema');
        $(form.submit).prop({disabled : true});
        $.post('loginAjax.do', $(form).serialize(), function (response) {
            var $modal = Anb.$alert_modal;
            if (response == 'OK') {
                $modal.modal('hide');
            } else {
                Anb.$alert_modal.html(response);
                if ($modal.find('.input-error').length) {
                    $modal.find('input').on('keypress', function () {
                        $(this).parent().next().fadeOut();
                    });
                }
            }
        });
        return false;
    }
    
    form.clean = function (form) {
        Anb.form.cleanErrors(form);
        Anb.form.cleanData(form);
    }

    form.cleanErrors = function (form) {
        var $form = $(form);
        $form.find('.input-error').hide();
        $form.find('.form-group').removeClass('has-feedback').removeClass('has-error').removeClass('has-success');
        $form.find('.form-group .glyphicon-remove').removeClass('.glyphicon-remove');
    }
        
    form.cleanData = function (form) {
        var elements = form.elements;  
        for (var i in elements) {
            var el = elements[i],
                type = el.type;

            if ($.inArray(type, Anb.types) != -1) {
                el.value = "";
                if (type == 'textarea' && el.maxLength) {
                    el.nextSibling.innerHTML = el.maxLength;
                }
            } else if (type == 'checkbox' || type == 'radio') {
               el.checked = false;
            } else if (type == 'select-one' || type == 'select-multiple') {
               el.selectedIndex = 0;
            }
        }
    }

    return form;

}());

// Validate
Anb.validate = (function () {

    var messages = {
        required               : 'El campo <em>{0}</em> es requerido.',
        email                  : 'El campo <em>{0}</em> no es un email válido.',
        nit                    : 'El campo <em>{0}</em> no es un NIT válido.',
        date                   : 'El campo <em>{0}</em> no es una fecha válida.',
        date_less_than         : 'El campo <em>{0}</em> debe contener una fecha <em>menor</em> a la fecha actual.',
        date_greater_than      : 'El campo <em>{0}</em> debe contener una fecha <em>mayor</em> a la fecha actual.',
        data_date_less_than    : 'El campo <em>{0}</em> debe contener una fecha <em>no menor</em> a <em>{1}</em> días.',        
        data_date_greater_than : 'El campo <em>{0}</em> debe contener una fecha <em>no mayor</em> a <em>{1}</em> días.',
        data_minlength         : 'El campo <em>{0}</em> debe contener un mínimo de <em>{1}</em> carácteres.',
        data_min               : 'El campo <em>{0}</em> debe contener un valor mayor o igual a <em>{1}</em>.',
        data_max               : 'El campo <em>{0}</em> debe contener un valor menor o igual a <em>{1}</em>.',
        data_exactlength       : 'El campo <em>{0}</em> debe tener exactamente <em>{1}</em> carácteres.',
        data_matches           : 'El campo <em>{0}</em> no concuerda con el campo <em>{1}</em>.',
        alpha                  : 'El campo <em>{0}</em> sólo puede contener carácteres alfabéticos.',
        alpha_numeric          : 'El campo <em>{0}</em> sólo puede contener carácteres alfanuméricos.',
        alpha_dash             : 'El campo <em>{0}</em> sólo puede contener carácteres alfanuméricos, guiones bajos \'_\' o guiones \'-\'.',
        numeric                : 'El campo <em>{0}</em> sólo puede contener números.',
        integer                : 'El campo <em>{0}</em> debe contener un número entero.',
        matches                : 'El campo <em>{0}</em> no concuerda con el campo <em>{1}</em> .',
        is_natural             : 'El campo <em>{0}</em> debe contener sólo números positivos.',     
        no_zero                : 'El campo <em>{0}</em> debe contener un número mayor a cero.',
        decimal                : 'El campo <em>{0}</em> debe contener un número decimal.'
    }

    var ERRORS = [];

    var isNit = {
        verhoeff : function (x, y) {
            var m = ['0123456789', '1234067895', '2340178956', '3401289567', '4012395678', '5987604321', '6598710432', '7659821043', '8765932104', '9876543210'];
            if (m[x] && m[x][y]) {
                return parseInt(m[x][y]);
            }
            return -1;
        },
        
        permutations : function (x, y)  {
            var m = ['0123456789', '1576283094', '5803796142', '8916043527', '9453126870', '4286573901', '2793806415', '7046913258'];
            if (m[x] && m[x][y]) {
                return parseInt(m[x][y]);
            }
            return -1;
        },
        
        evaluate : function (num) {
            var check = 0, x, y, z;
        
            if (typeof num == 'string' && isNaN(num)) {
                return false;
            }
            num = parseInt(num);
            for (var i = 0, l = num.toString().length; i < l; i++) {
                x = i % 8;
                y = num % 10;
                num = Math.floor(num/10);
                z = isNit.permutations(x, y);
                check = isNit.verhoeff(check, z);
            }
        
            return check == 0;
        }
    }
    
    var getMessage = function ($el, data) {
        var value = '', el = $el[0];
        if (el.getAttribute('data-' + data)) {
            value = el.getAttribute('data-' + data);
            if (data == 'matches') {
                var $elem = $(el.form[value]),
                    $label = $elem.prev('label');
                if ($label.length == 0) {
                    $label = $elem.parent().prev('label');
                }
                value = $label.length ? $label[0].innerHTML.replace(/:/gi, '') : value;
            }
        }
        return value;
    }

    var renderError = function (message, $el) {
        var el = $el[0],
            type = el.type, 
            datepicker = el.classList.contains('datepicker-input'),
            parent = type == 'checkbox' || type == 'radio' || datepicker, 
            counter = el.getAttribute('data-maxlength'),
            $error = parent ? $el.parent().nextAll('.input-' + message) : $el.nextAll('.input-' + message);
        if ($error.length) {
            $error.show();
        } else {
            var $elem = parent ? $el.parent() : $el,
                $label = $elem.prev('label');
                if ($label.length == 0) {
                    $label = $elem.parent().prev('label');
                }
            var data = message.replace(/data-/gi, ''),
                msg = el.getAttribute('data-label-' + message) 
                   || el.getAttribute('data-label') 
                   || messages[message.replace(/-/gi, '_')].replace(/\{0\}/gi, $label.length ? $label[0].innerHTML.replace(/:/gi, '') : '').replace(/\{1\}/gi, getMessage($el, data));

            if (!ALERT_ERROR) {
                var divError = '<div class="input-error input-' + message +'">' + msg + '</div>';
                if (counter) {
                    $elem.next().after(divError);
                } else {
                    $elem.after(divError);
                }    
            } else {
                ERRORS.push(msg);
            }
        }
        $el.closest('.form-group').addClass('has-error');
        if (!datepicker) {
            $el.focus();
        }
    }

    var removeError = function (el, className) {
        var $el = $(el).closest('.form-group');
        $el.removeClass('has-error');
        $el.find(className || '.input-required').fadeOut();
    }

    var addRuleText = function (form, rule, types, fn_validate) {
        var isData = rule.indexOf('data-') == 0,
            elements = Anb.selector.visible('input' + (isData ? '[' + rule + ']' : '.' + rule), form),
            valid = true;
        if (elements.length) {
            $(form).on('keyup', isData ? '[' + rule + ']' : '.' + rule, function () {
                removeError(this, '.input-' + rule);
            });
            for (var i = elements.length - 1; i >= 0; i--) {
                var el = elements[i];
                if (!Anb.filter.empty(el.value)) {
                    var data = el.getAttribute(rule) || false;
                    if ((Anb.toType(types) == 'array' ? types.indexOf(el.type) != -1 : el.type == types) && !(data ? fn_validate(el.value, data, form) : fn_validate(el.value))) {
                        renderError(rule, $(el));
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    var required = function (form) {
        var elements = Anb.selector.visible('.required', form),
            valid = true,
            radios = [];
        if (elements.length) {
            $(form).on('keyup', 'input.required, textarea.required', function () {
                removeError(this);
            });
            $(form).on('change', '[type=file].required, select.required', function () {
                removeError(this);
            });
            $(form).on('click', '[type=checkbox].required, [type=radio].required', function () {
                removeError(this);
            });        
            for (var i = elements.length - 1; i >= 0; i--) {
                var el = elements[i],
                    $el = $(el),
                    type = el.type;
                if ((Anb.types.indexOf(type) != -1 && Anb.filter.empty(el.value)) ||
                    (type == 'select-one' && (el.selectedIndex == null || el.selectedIndex == 0)) ||
                    (type == 'checkbox' && !el.checked)) {
                    renderError('required', $el);
                    valid = false;
                }
                if (type == 'radio') {
                    radios[el.name] = el.name;
                }
                if ($el.hasClass('ms-ctn') && $el.find('.ms-sel-item').length == 0) {
                    $el.addClass('ms-inv');
                    renderError('required', $el);
                    valid = false;
                }
                if ($el.hasClass('input-daterange')) {
                    var $inputs = $el.find('input');
                    if (Anb.filter.empty($inputs[0].value) && Anb.filter.empty($inputs[1].value)) {
                        renderError('required', $el);
                        valid = false;    
                    }
                }
            }
            for (var name in radios) {
                var options = form.querySelectorAll('[name=' + name + ']'),
                    checked = false,
                    label = null;
                for(var i = 0, l = options.length; i < l; i++) {    
                    if(options[i].checked) {
                        checked = true;
                        break;
                    }
                    if (options[i].getAttribute('data-label')) {
                        label = options[i].getAttribute('data-label');
                    }
                }
                if (label) {
                    options[l-1].setAttribute('data-label', label);
                }
                if(!checked) {
                    renderError('required', $(options[l-1]));
                    valid = false;
                }
            }
        }
        return valid;
    }

    var email = function (form) {
        return addRuleText(form, 'email', ['text', 'email'], Anb.filter.isEmail);
    }

    var nit = function (form) {
        return addRuleText(form, 'nit', ['text', 'number'], Anb.validate.isNit);
    }
    
    var matches = function (form) {
        return addRuleText(form, 'data-matches', ['text', 'email', 'password', 'number'], Anb.validate.equals);
    }

    var date = function (form) {
        return addRuleText(form, 'date', ['text', 'date'], Anb.datetime.isDate);
    }

    var dateLessThan = function (form) {
        return addRuleText(form, 'date-less-than', ['text', 'date'], Anb.datetime.isLessThan);
    }

    var dateLessThanDays = function (form) {
        return addRuleText(form, 'data-date-less-than', ['text', 'date'], Anb.datetime.isLessThanDays);
    }

    var dateGreaterThan = function (form) {
        return addRuleText(form, 'date-greater-than', ['text', 'date'], Anb.datetime.isGreaterThan);
    }

    var dateGreaterThanDays = function (form) {
        return addRuleText(form, 'data-date-greater-than', ['text', 'date'], Anb.datetime.isGreaterThanDays);
    }
    
    var minlength = function (form) {
        return addRuleText(form, 'data-minlength', ['text', 'number'], Anb.validate.minlength);
    }
    
    var min = function (form) {
        return addRuleText(form, 'data-min', ['text', 'number'], Anb.validate.min);
    }
    
    var max = function (form) {
        return addRuleText(form, 'data-max', ['text', 'number'], Anb.validate.max);
    }
    
    var exactlength = function (form) {
        return addRuleText(form, 'data-exactlength', ['text', 'number'], Anb.validate.exactlength);
    }

    var alpha = function (form) {
        return addRuleText(form, 'alpha', ['text', 'number'], Anb.filter.isAlpha);
    }

    var alphaNumeric = function (form) {
        return addRuleText(form, 'alpha-numeric', ['text', 'number'], Anb.filter.isAlphaNumeric);
    }

    var alphaDash = function (form) {
        return addRuleText(form, 'alpha-dash', ['text', 'number'], Anb.filter.isAlphaDash);
    }

    var numeric = function (form) {
        return addRuleText(form, 'numeric', ['text', 'number'], Anb.filter.isNumeric);
    }

    var integer = function (form) {
        return addRuleText(form, 'integer', ['text', 'number'], Anb.filter.isInteger);
    }

    var decimal = function (form) {
        return addRuleText(form, 'decimal', ['text', 'number'], Anb.filter.isDecimal);
    }    

    var natural = function (form) {
        return addRuleText(form, 'natural', ['text', 'number'], Anb.filter.isNatural);
    }
    
    var noZero = function (form) {
        return addRuleText(form, 'no-zero', ['text', 'number'], Anb.validate.isNoZero);
    }

    var validate = {};

    validate.run = function (form) {
           
        var valid = true;
        ERRORS = [];
        
        if (Anb.toType(form) == 'string') {
            form = document.getElementById(form);
        }
        
        var button = form.querySelector('[type=submit]');
        if (typeof button == 'undefined') {
            return false;
        }
        button.disabled = true;
        
        Anb.validate.trim(form);
        
        valid = alpha(form) && valid;
        valid = alphaNumeric(form) && valid;
        valid = alphaDash(form) && valid;
        valid = numeric(form) && valid;
        valid = integer(form) && valid;
        valid = decimal(form) && valid;
        valid = natural(form) && valid;
        valid = noZero(form) && valid;
        valid = minlength(form) && valid;
        valid = min(form) && valid;
        valid = max(form) && valid;
        valid = exactlength(form) && valid;
        valid = dateLessThan(form) && valid;
        valid = dateLessThanDays(form) && valid;
        valid = dateGreaterThan(form) && valid;
        valid = dateGreaterThanDays(form) && valid;
        valid = date(form) && valid;
        valid = email(form) && valid;
        valid = nit(form) && valid;
        valid = matches(form) && valid;
        valid = required(form) && valid;

        button.disabled = false;
        if (ALERT_ERROR && !valid) {
            var message = '';
            for (var i = ERRORS.length - 1; i >= 0; i--) {
                message += ERRORS[i] + '<br>';
            }
            Anb.alert('<div class="alert alert-danger alert-errors">' + message + '</div>');
            return false;
        }
        return valid;
    }

    validate.isNit = function (value) {
        return isNit.evaluate(value);
    }
    
    validate.isNoZero = function (value) {
        return value > 0;
    }

    validate.minlength = function (value, minlength) {
        return value.length >= minlength;
    }
    
    validate.min = function (value, min) {
        return parseFloat(value) >= parseFloat(min);
    }
    
    validate.max = function (value, max) {
        return parseFloat(value) <= parseFloat(max);
    }
    
    validate.exactlength = function (value, exactlength) {
        return value.length == minlength;
    }
    
    validate.equals = function (value, data, form) {
        return value == form[data].value;
    }
    
    validate.removeError = removeError;
    
    validate.trim = function (form) {
        var elements = form.elements;  
        for (var i in elements) {
            if (Anb.types.indexOf(elements[i].type) != -1) {
               elements[i].value = elements[i].value.trim();
            }
        }
    }

    return validate;

}());

// Growl message
Anb.message = (function () {

    var $growls = $('#anb-messages'),
        tmpl_growl = $('#anb-message-tmpl').html(),
        DELAY = GROWL_STATIC ? 60000 : 6000;
    
    if (GROWL_STATIC) {
        $('body').addClass("static");
    }
    var message = {};
    
    var render = function (data) {
        $growls.html('');
        var $growl = $(Anb.nano(tmpl_growl, data));
        if (GROWL_STATIC) {
            if (document.getElementById("main-modal") && document.getElementById("main-modal").style.display == 'block') {
                var body = document.querySelector("#main-modal .modal-body");
                body.insertBefore( $growl[0], body.firstChild );
            } else {
                $growls.append($growl);    
            }
        } else {
            $growls.append($growl);
        }
        
        $growl.animate({opacity : .95}, function () {
            var stop = false;
            $growl.find('.close').on('click', function () {
                $growl.off('mouseover').off('mouseout');
                hide($growl);
            });
            $growl.on('mouseover', function () {
                stop = true;
                $growl.stop().css({opacity : .95});    
            });
            $growl.on('mouseout', function () {
                stop = false;
                setTimeout(function () {
                    if (!stop) {
                        hide($growl);
                    }
                }, data.delay || delay);
            });
            setTimeout(function () {
                if (!stop) {
                    hide($growl);
                }
            }, data.delay || delay);
        });
        
        if (data.callback) {
            data.callback.apply(window, [$growl[0]]);
        }
    }
    
    var hide = function ($growl) {
        $growl.fadeOut(1500, function () {
            setTimeout(function () {
                $growl.remove();
            }, 1000);
        });
    }

    var getParams = function (args) {
        var params = Anb.getParams({
            first : {
                type : 'function',
                position : 1
            },
            second : {
                type : 'number',
                position : 2
            }
        }, args);
        return {
            callback : params.first,
            delay : params.second,
            title : args[3] || null
        }
    }
     
    // arguments : msg, callback, delay  
    message.error = function (msg) {
        var args = getParams(arguments);
        setTimeout(function () {
            render({
                type : 'error', 
                title : args.title || '¡Error!', 
                message : msg || 'Ha ocurrido un error.', 
                delay : DELAY,
                icon : 'fa-times',
                callback : args.callback || null
            });
        }, args.delay || 0);
    }
    
    message.ok = function (msg) {
        var args = getParams(arguments);
        setTimeout(function () {
            render({
                type : 'success', 
                title : args.title || '¡Correcto!', 
                message : msg || 'La operación se realizó correctamente.', 
                delay : DELAY,
                icon : 'fa-check',
                callback : args.callback || null
            });
        }, args.delay || 0);
    }

    message.mail = function (msg) {
        var args = getParams(arguments);
        setTimeout(function () {
            render({
                type : 'info', 
                title : args.title || '¡Correo!', 
                message : msg || 'Correo electrónico enviado.', 
                delay : DELAY,
                icon : 'fa-envelope-o',
                callback : args.callback || null
            });
        }, args.delay || 0);
    }
    
    message.notification = function (msg) {
        var args = getParams(arguments);
        setTimeout(function () {
            render({
                type : 'warning', 
                title : args.title || '¡Notificación!', 
                message : msg || 'Hay nuevas notificaciones.', 
                delay : DELAY,
                icon : 'fa-exclamation-triangle',
                callback : args.callback || null
            });
        }, args.delay || 0);
    }
    
    message.hideAll = function (){
        $('.anb-message').remove();
    }

    return message;

}());

// Datatable
Anb.datatable = function () {

    var oTable = null,
        selected = [],
        settings = {deferRender : true},
        $table = null,
        $checkAll = null;

    var checkbox = {
        fnInitComplete : function () {
            var that = this, firstTH = $(that).find('th')[0], th = document.createElement('th');
            th.innerHTML = "<i></i>";
            th.className = "check-all";
            th.onclick = function () {
                var toggle = (that.$('tr').length == that.$('tr.active').length ? 'remove' : 'add') + 'Class';
                $(firstTH).parent()[toggle]('all-selected');
                that.$('tr')[toggle]('active');
                var now = that.$('tr.active').length;
                $($(that).parent().find('.row .col-md-9')[0]).html(now ? '<span class="label label-warning"><strong>' + now + '</strong> ' + "Fila" + (now > 1 ? 's' : '') + ' ' + 'Seleccionada' + (now > 1 ? 's' : '') + '.</span>' : '');
            }
            $(firstTH).parent().prepend(th);
            if (that.$('tr').length == 0) {
                $(that).find('td').prop('colspan', $(that).find('td').prop('colspan') + 1);
            }
            that.$('tr').each(function () {
                var firstTD = this;
                var td = document.createElement('td');
                td.innerHTML = "<i></i>";
                td.className = "check-item";
                td.onclick = function () {
                    $(firstTD).toggleClass('active');
                    var now = that.$('tr.active').length;
                    $(firstTH).parent()[(that.$('tr').length == now ? 'add' : 'remove') + 'Class']('all-selected');
                    $($(that).parent().find('.row .col-md-9')[0]).html(now ? '<span class="label label-warning"><strong>' + now + '</strong> Fila' + (now > 1 ? 's' : '') + ' Seleccionada' + (now > 1 ? 's' : '') + '.</span>' : '');
                }
                $(firstTD).prepend(td);
            })
        }
    };
    
    // Set id_table and options   
    var parameters = Anb.getParams({
        first : {
            type : 'string',
            defaultValue : 'main-table'
        },
        second : {
            type : 'object',
            defaultValue : {}
        }
    }, arguments);
    var id_table = parameters.first, options = parameters.second;
       
    var init = function () {
        if (options.total) {
           options.total.coin = options.total.coin || 'Bs.';
           $.extend(settings, {
               "footerCallback": function ( row, data, start, end, display ) {
                   var that = this;
                   var api = that.api(), total, pageTotal;
        
                   // Remove the formatting to get integer data for summation
                   var intVal = function ( i ) {
                       return typeof i === 'string' ?
                           i.replace(/[\$,]/g, '')*1 :
                           typeof i === 'number' ?
                               i : 0;
                   };
        
                   if (that.$('tr').length) {
                       // Total over all pages
                       total = api
                           .column( options.total.column )
                           .data()
                           .reduce( function (a, b) {
                               return intVal(a) + intVal(b);
                           });
            
                       // Total over this page
                       pageTotal = api
                           .column( options.total.column, { page: 'current'} )
                           .data()
                           .reduce( function (a, b) {
                               return intVal(a) + intVal(b);
                           }, 0);
            
                       // Update footer
                       $( api.column( options.total.column ).footer() ).html(
                           options.total.coin + ' ' + pageTotal +' ( ' + options.total.coin + ' ' + total +' total)'
                       );
                   }
               }
           });
        }
        if (options.ajax) {
            settings = $.extend(settings,{
                "processing" : true,
                "serverSide" : true,
                "rowCallback": function( row, data ) {
                    if ( $.inArray(data.DT_RowId, selected) !== -1 ) {
                        $(row).addClass('active');
                    }
                 },
                "aoColumnDefs" : [
                    {"bVisible": true, "aTargets": [ 0 ]}
                ]
            });
            options.checkbox = false;
        }
        if (typeof options.checkbox !== 'undefined' && options.checkbox === true) {
            settings = $.extend({}, settings, checkbox);
            delete options.checkbox;
        }
        if (options.noSortable) {
            settings = $.extend(settings, {
                "aoColumnDefs" : [
                    {"bVisible": false, "aTargets": [ 0 ]}, 
                    {"bSortable": false, "aTargets": options.noSortable }
                ]
            });
            delete options.noSortable;
        }
        
        settings = $.extend({}, settings, options);
        $table = $('#' + id_table);
        oTable = $table.dataTable(settings);
        
        if (options.ajax) {
            $checkAll = $table.find(".check-all");
            $checkAll.on('click', function () {
                var toggle = ($checkAll.parent().hasClass("all-selected") ? 'remove' : 'add') + 'Class';
                if (toggle == "addClass") {
                    Anb.loading.show("Seleccionando los registros, espere por favor");
                    $.get(options.ajax, {all : true}, function(response) {
                        selected = response;
                    }, "json");
                } else {
                    selected = [];
                }
                $checkAll.parent()[toggle]('all-selected');
                oTable.$('tr')[toggle]('active');
                var now = toggle == 'addClass' ? oTable.$('table').dataTableSettings[0]._iRecordsTotal : 0;
                $($table.parent().find('.row .col-md-9')[0]).html(now ? '<span class="label label-warning"><strong>' + now + '</strong> ' + "Fila" + (now > 1 ? 's' : '') + ' ' + 'Seleccionada' + (now > 1 ? 's' : '') + '.</span>' : '');
            });
            $table.find('tbody').on( 'click', 'tr td.check-item', function () {           
                var id = this.parentNode.id;
                var index = $.inArray(id, selected);
                index === -1 ? selected.push(id) : selected.splice(index, 1);
                var now = selected.length;
                var total = oTable.$('table').dataTableSettings[0]._iRecordsTotal;
                $checkAll.parent()[(total == now ? 'add' : 'remove') + 'Class']('all-selected');
                $($table.parent().find('.row .col-md-9')[0]).html(now ? '<span class="label label-warning"><strong>' + now + '</strong> Fila' + (now > 1 ? 's' : '') + ' Seleccionada' + (now > 1 ? 's' : '') + '.</span>' : '');
                $(this).parent().toggleClass('active');
            });
        }
        var $tableContainer = $('#' + id_table + '_wrapper');
        $tableContainer.find('input, select').addClass('form-control');
        $tableContainer.find('.dataTables_filter input').attr('placeholder', options.filterPlaceholder || 'Escriba su búsqueda aquí...')
    }
    
    init();
    
    var delete_row = function (obj, url, refresh) {
        Anb.confirm("¿Deséa eliminar el registro?", function () {
            Anb.loading.show("Eliminando registro");
            $.post(url, function (response) {
                if (response == 'DELETE') {
                    Anb.message.ok("¡Registro eliminado!");
                    oTable.fnDeleteRow( $(obj).parents('tr') )
                    if (refresh) {
                        setTimeout(function () {
                            window.location = '';
                        }, 500);
                    }
                } else {
                    Anb.message.error(response);
                    Anb.loading.show("Recargando página");
                    setTimeout(function () {
                        window.location = '';
                    }, 4000);
                }
            });
        });
    }

    var datatable = {};
    
    datatable.delete_row = delete_row;
    
    datatable.selected = function () {
         var pks = [];
        if (options.ajax) {
            for (var i = 0, l = selected.length; i < l; i++) {
                pks.push(selected[i].split("_")[1]);
            }
        } else {
            oTable.$('tr.active').each(function () {
                pks.push(oTable.fnGetData(this)[0]);
            });
        }
        return pks;
    }
    
    datatable.btnEdit = function (url) {
        var parameters = Anb.getParams({
            first : {
                type : 'string',
                position : 1
            },
            second : {
                type : 'function',
                position : 2
            }
        }, arguments);
        var size = parameters.first, callback_function = parameters.second;
        oTable.$('.btn-edit').on('click', function (e) {
            e.preventDefault();
            Anb.modal.show(url + '?id=' + $(this).data('role'), size || callback_function, callback_function && size ? callback_function : size, arguments[3]);
        });
    }
    
    datatable.btnDelete = function (url, refresh) {
        oTable.$('.btn-delete').on('click', function (e) {
            e.preventDefault();
            delete_row(this, url + '?id=' + $(this).data('role'), refresh);
        });
    }
    
    datatable.btnView = function (search, url) {
        var parameters = Anb.getParams({
            first : {
                type : 'string',
                position : 2
            },
            second : {
                type : 'function',
                position : 3
            }
        }, arguments);
        var size = parameters.first, callback_function = parameters.second;
        oTable.$(search).on('click', function (e) {
            e.preventDefault();
            Anb.modal.show(url + '?id=' + $(this).data('role'), size || callback_function, callback_function && size ? callback_function : size);
        });
    }
    
    datatable.btnViewSecond = function (query, url) {
        var parameters = Anb.getParams({
            first : {
                type : 'string',
                position : 2
            },
            second : {
                type : 'function',
                position : 3
            }
        }, arguments);
        var size = parameters.first, callback_function = parameters.second;
        oTable.$(query).on('click', function (e) {
            e.preventDefault();
            Anb.modal.secondShow(url + '?id=' + $(this).data('role'), size || callback_function, callback_function && size ? callback_function : size);
        });
    }
    
    datatable.$ = function (query) {
        return oTable.$(query);
    }

    return datatable;

};

// Autocomplete
Anb.autocomplete = function (query, settings) {
    var defaults = {}, data = {},
        $el = $(query), 
        attributes = $el[0].attributes,
        classes = $el[0].className.split(/\s+/),
        ms = null; // object MagicSuggest

    var init = function () {
        for (var i = 0, l = attributes.length; i < l; i++) {
            var value = attributes[i].value;
            data[attributes[i].name.replace(/data-/gi, '')] = value === 'true' || (value === 'false' ? false : value);
        }
        for (var i = 0, l = classes.length; i < l; i++) {
            data[classes[i]] = true;
            settings.cls = (settings.cls || '') + ' ' + classes[i];
        }
        settings = $.extend(defaults, data, settings || {});
        if (settings.multiple) {
            settings.hideTrigger = true;
        }
        if (typeof settings.search != 'undefined' && !settings.search) {
            settings.cls = (settings.cls || '') + ' no-search';
        }
        ms = $el.magicSuggest(settings);
        if (settings.integer) {
            $(ms).on('keydown', function (e,m,v) {
                Anb.filter.integer(v);
            });
        }
        if (settings.decimal) {
            $(ms).on('keydown', function (e,m,v) {
                Anb.filter.decimal(v);
            });
        }
        if (settings.required) {
            $(ms).on('selectionchange', function(e,m) {
                var $el = $(query);
                $el.closest('div.form-group').removeClass('has-error').addClass('has-success');
                $el.nextAll('.input-required').fadeOut();
            });    
        }
    }

    init();
    
    ms.disabled = function (bool) {
        bool ? ms.disable() : ms.enable();
    }

    return ms;

};

// Array
Anb.array = (function () {

    var array = {};
    
    array.replace = function (text, find, replace) {
        for (var i = 0, l = find.length, regex; i < l; i++) {
            regex = new RegExp(find[i], "g");
            text = text.replace(regex, replace[i]);
        }
        return text;
    };
    
    array.insert = function (arr, index, item) {
      return arr.splice(index, 0, item);
    };

    return array;

}());

// number
Anb.number = (function () {

    var currency = 'BOB';
    var decimalNumber = 2;
    var decimalSeparator = ',';
    var thousandsSeparator = '.';
    
    var number = {};
    
    number.convert = function (value) {
        return parseFloat(isNaN(value) ? value.replace(/\./g, '').replace(',', '.') : value);
    }
    
    number.format = function (num, decimal, isCurrency) {
        decimal = decimal || decimal == 0 ? 0 : decimalNumber;
        var error = (0).toFixed(decimal).replace(/\./g, ',');
        if (typeof num == 'undefined' || num.length == 0) {
            return error;
        }
        if (Anb.toType(num) == 'string') {
            num = Anb.number.converter(num).toFixed(decimal);
        } else {
            num = num.toFixed(decimal);
        }
        if (num == 'NaN') {
            return error;
        }
        if (decimal == 0) {
            return num.toString().replace(new RegExp("(\d)(?=(\d{3})+(?!\d))", "g"), "$1" + thousandsSeparator) + isCurrency ? currency : '';
        } else {
            num = num.split(thousandsSeparator);            
            return typeof num[1] == 'undefined' ? error : num[0].toString().replace(new RegExp("(\d)(?=(\d{3})+(?!\d))", "g"), "$1" + thousandsSeparator) + decimalSeparator + num[1] + isCurrency ? currency : '';
        }
    };
    
    number.format2 = function(n, x, s, c) {
        var re = '\\d(?=(\\d{' + (x || 3) + '})+' + (n > 0 ? '\\D' : '$') + ')',
            num = this.toFixed(Math.max(0, ~~n));
    
        return (c ? num.replace('.', c) : num).replace(new RegExp(re, 'g'), '$&' + (s || ','));
    };
    
    number.formatNumber = function  (num) {
        return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
    }
    
    number.currencyFormat = function  (num) {
        return "$" + num.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
    }
    
    number.currencyFormatDE = function  (num) {
        return num
           .toFixed(2) // always two decimal digits
           .replace(".", ",") // replace decimal point character with ,
           .replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.") + " €" // use . as a separator
    }

    return number;

}());


// Datetime
Anb.datetime = (function () {

    var formatDate = 'dd/MM/YYYY',
        months = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"],
        days = ["dom", "lun", "mar", "mié" , "jue", "vie" , "sáb"],
        dayslong = ["Domingo", "Lunes", "Martes", "Miércoles" , "Jueves", "Viernes" , "Sábado"];
    var tmpl_date = $('#tmpl-date').html();
    var datetime = {};

    datetime.isDate = function (date) {
        if (Anb.toType(date) == 'string') {
            date = Anb.datetime.convert(date);
        }
        return Anb.datetime.isValid(date);
    }

    datetime.isValid = function (date) {
        if (Anb.toType(date) != 'date')
            return false;
        return !isNaN(date.getTime());
    }

    datetime.isLessThan = function (date) {
        return Anb.datetime.milliseconds(date) < (new Date()).getTime();
    }

    datetime.isLessThanDays = function (date, data) {
        return Anb.datetime.milliseconds(date) > Anb.datetime.subtractDays(new Date(), data);
    }

    datetime.isGreaterThan = function (date) {
        return Anb.datetime.milliseconds(date) > (new Date()).getTime();
    }

    datetime.isGreaterThanDays = function (date, data) {
        return Anb.datetime.milliseconds(date) < Anb.datetime.addDays(new Date(), data);
    }

    datetime.addDays = function (date, days) {
        return Anb.datetime.milliseconds(date) + (days || 0)*24*60*60*1000;
    }

    datetime.subtractDays = function (date, days) {
        return Anb.datetime.milliseconds(date) - (days || 0)*24*60*60*1000;
    }

    datetime.diff = function (date2, date1) {
        return Anb.datetime.milliseconds(date2) - Anb.datetime.milliseconds(date1);
    }

    datetime.milliseconds = function (date) {
        if (Anb.toType(date) == 'string') {
            date = Anb.datetime.convert(date);
        }
        return date.getTime();
    }

    datetime.convert = function (date) {
        date = date.split('/');
        return formatDate == 'dd/MM/YYYY' ? new Date(date[2], date[1]-1, date[0]) : 
               formatDate == 'MM/dd/YYYY' ? new Date(date[2], date[0]-1, date[1]) : 
               new Date(date[0], date[1]-1, date[2]);
    }

    datetime.setFormatDate = function (format) {
        formatDate = format;
    }

    datetime.dateRange = function (query, settings) {
        var defaults = {
            format: formatDate.toLowerCase(),
            language: "es",
            todayHighlight: true,
            autoclose: true
        };
        settings = $.extend(defaults, settings || {});
        $(query).each(function () {
            var $el = $(this),
                el = this,
                $btn_submit = $(el.form).find("[type=submit]"),
                backup = el.value;

            if ($el.hasClass('date-less-than')) {
                settings.endDate = Anb.datetime.now();
            }
            if (el.hasAttribute('data-date-less-than')) {
                settings.startDate = Anb.datetime.replace(Anb.datetime.subtractDays(new Date(), el.getAttribute('data-date-less-than')));
            }
            if (el.hasAttribute('data-date-start')) {
                settings.startDate = el.getAttribute("data-date-start");
            }
            if ($el.hasClass('date-greater-than')) {
                settings.startDate = Anb.datetime.now();
            }
            if (el.hasAttribute('data-date-greater-than')) {
                settings.endDate = Anb.datetime.replace(Anb.datetime.addDays(new Date(), el.getAttribute('data-date-greater-than')));
            }
            if (el.hasAttribute('data-date-end')) {
                settings.endDate = el.getAttribute("data-date-end");
            }
            
            $el.find('input').attr('maxLength',10).attr('autocomplete','off').on('blur', function () {
            	if (!Anb.datetime.isDate(this.value)) {
                    this.value = Anb.datetime.now();
            	}
            });

            $el.datepicker(settings).on('changeDate', function () {
                if (el.form.classList.contains('required')) {
                    Anb.validate.removeError(this, '.input-error');
                }
                if (!el.form.classList.contains('no-backup')) {
                    $btn_submit.prop('disabled', el.value == backup);
                }
                $btn_submit.prop('disabled', $el[0].value == backup);
            });
        });
    }
    
    datetime.date = function (query, settings) {
        var defaults = {
            format: formatDate.toLowerCase(),
            language: "es",
            todayHighlight: true,
            autoclose: true
        };
        settings = $.extend(defaults, settings || {});
        
        $(query).each(function () {
            var $el = $(this),
                el = this,
                $tmpl = $(tmpl_date),
                $btn_submit = $(el.form).find("[type=submit]"),
                backup = this.value;

            $tmpl.insertAfter($el);
            $el.prependTo($tmpl);
            $el.removeClass('datepicker').addClass('datepicker-input');    
            
            if ($el.hasClass('date-less-than')) {
                settings.endDate = Anb.datetime.now();
            }
            if (el.hasAttribute('data-date-less-than')) {
                settings.startDate = Anb.datetime.replace(Anb.datetime.subtractDays(new Date(), el.getAttribute('data-date-less-than')));
            }
            if (el.hasAttribute('data-date-start')) {
                settings.startDate = el.getAttribute("data-date-start");
            }
            if ($el.hasClass('date-greater-than')) {
                settings.startDate = Anb.datetime.now();
            }
            if (el.hasAttribute('data-date-greater-than')) {
                settings.endDate = Anb.datetime.replace(Anb.datetime.addDays(new Date(), el.getAttribute('data-date-greater-than')));
            }
            if (el.hasAttribute('data-date-end')) {
                settings.endDate = el.getAttribute("data-date-end");
            }
            
            el.maxLength = 10;
            el.setAttribute('autocomplete', 'off');
            $tmpl.datepicker(settings).on('changeDate', function (a, b) {                
                if (el.disabled) {
                    el.value = backup;
                }
                if (el.form.classList.contains('required')) {
                    Anb.validate.removeError(this, '.input-error');
                }
                if (!el.form.classList.contains('no-backup')) {
                    $btn_submit.prop('disabled', el.value == backup);
                }
            });
            $el.on('blur', function () {
            	if (!Anb.datetime.isDate(this.value)) {
                    this.value = Anb.datetime.now();
            	}
            });
            delete settings['endDate'];
            delete settings['startDate'];
        });
    }
    
    datetime.now = function (format) {
        return Anb.datetime.replace(new Date(), format || formatDate);
    }

    datetime.dateLiteral = function (date) {
        return datetime.format(date, 'dddd dd de MMM del YYYY');
    }
    
    datetime.timeLiteral = function (date) {
        return datetime.format(date, 'HH:mm');
    }

    datetime.datetimeLiteral = function (date) {
        return datetime.format(date, 'dddd dd de MMM del YYYY a la(s) HH:mm');
    }
    
    datetime.format = function (date, format) {
        var d = new Date(date);
        if (Anb.datetime.isDate(d)) {
            return Anb.datetime.replace(d, format || formatDate);
        }
        return date;
    }

    datetime.standar = function (date, format) {
        var type = Anb.toType(date);
        if (type == 'date') {
            return date;
        }
        if (type == 'string') {
            format = format || formatDate;
            var separator = date.indexOf('/') != -1 ? '/' : '-';
            date = date.split(separator);
            if (format == 'dd/MM/YYYY' || format == 'dd-MM-YYYY') {
                date = date[1] + separator + date[0] + separator + date[2];    
            }
            if (format == 'YYYY/dd/MM' || format == 'YYYY-dd-MM') {
                date = date[0] + separator + date[1] + separator + date[2];
            }
        }
        return new Date(date);
    }

    datetime.replace = function (date, format) {
        format = format || formatDate;
        date = Anb.datetime.standar(date, format);
        var monthLiteral = format.indexOf('MMM') != -1;
        return Anb.array.replace(format, [
            "dddd", "ddd", "dd", monthLiteral ? 'MMM' : 'MM', 'YYYY', 'HH', 'mm', 'ss'
        ], [
            dayslong[date.getDay()],
            days[date.getDay()],
            (date.getDate() < 10 ? '0' : '') + date.getDate(), 
            monthLiteral ? months[date.getMonth()] : ((date.getMonth() + 1 < 10 ? '0' : '') + (date.getMonth() + 1)), 
            date.getFullYear(), 
            (date.getHours() < 10 ? '0' : '') + date.getHours(), 
            (date.getMinutes() < 10 ? '0' : '') + date.getMinutes(), 
            (date.getSeconds() < 10 ? '0' : '') + date.getSeconds()
        ]);
    }

    datetime.last = function (date) {
        var now = new Date();
        var days = Anb.datetime.daysBetweenTwoDates(date, now);
        if (days > 31) {
            return Anb.datetime.dateLiteral(date);
        } else {
            if (days > 7) {
                var weeks = parseInt(days / 7);
                return 'Hace ' + weeks + ' semana' + (weeks > 1 ? 's' : '');
            } else {
                if (days >= 1) {
                    return Anb.datetime.format(date, 'ddd a la(s) HH:mm');
                } else {
                    var hours = Anb.datetime.hoursBetweenTwoDates(date, now);
                    if (Anb.datetime.isYesterday(date, hours)) {
                        return 'Ayer a la(s) ' + Anb.datetime.timeLiteral(date);
                    } else {
                        if (hours >= 1) {
                            return 'Hace ' + hours + ' hora' + (hours > 1 ? 's' : '');
                        } else {
                            var minutes = Anb.datetime.minutesBetweenTwoDates(date, now);
                            if (minutes >= 1) {
                                return 'Hace ' + minutes + ' minuto' + (minutes > 1 ? 's' : ''); 
                            } else {
                                var seconds = Anb.datetime.secondsBetweenTwoDates(date, now);
                                return 'Hace ' + seconds + ' segundo' + (seconds > 1 ? 's' : '');
                            }
                        }
                    }
                }
            }        
        }
    }

    datetime.secondsBetweenTwoDates = function (date1, date2, absolute) {
        return Anb.datetime.betweenTwoDates(date1, date2, "s", absolute);
    }

    datetime.minutesBetweenTwoDates = function (date1, date2, absolute) {
        return Anb.datetime.betweenTwoDates(date1, date2, "i", absolute);
    }

    datetime.hoursBetweenTwoDates = function (date1, date2, absolute) {
        return Anb.datetime.betweenTwoDates(date1, date2, "h", absolute);
    }

    datetime.daysBetweenTwoDates = function (date1, date2, absolute) {
        return Anb.datetime.betweenTwoDates(date1, date2, "d", absolute);
    }

    datetime.betweenTwoDates = function (date1, date2, type, absolute) {
        var types = {s : 1000, i : 60*1000, h : 60*60*1000, d : 24*60*60*1000};
        var diff = parseInt((Anb.datetime.standar(date2).getTime() - Anb.datetime.standar(date1).getTime())/types[type]);
        
        if (typeof absolute !== 'undefined' && absolute !== false) {
            return Math.abs(diff);
        }
        return diff;
    }
    
    datetime.isYesterday = function (date, hours) {
        if (Anb.datetime.isDate(date)) {
            date = date.split(' ');
            var time = date[1].split(':');
            return hours >= parseInt(time[0]);    
        }
        return false;
    }

    return datetime;

}());

Anb.load = (function () {

    function init() {
        var $buttonCollapse = $('.toggle-menu');
        var $asideActive = $('#sidebar .active').parent().parent();
        var $leftPanel = $('#sidebar');
        var mobile = $(window).width() < 768;

        render('body');

        if($asideActive.hasClass('has-submenu')) {
            $asideActive.addClass('active');
        }
        
        $buttonCollapse.on('click', function() {
            $leftPanel.toggleClass('collapsed');
            var collapse = $leftPanel.hasClass('collapsed');
            $('header, footer').toggleClass('collapsed');
            if (!mobile) {
                $('.has-submenu.active > ul')[collapse ? 'hide' : 'show']();
                localStorage.setItem('anb-navbar', collapse ? 'yes' : 'no');
            } else {
                $('.has-submenu.active > ul').show();
            }
            if (collapse) {
                $('.has-submenu .list-unstyled').hide();
            }
        });

        if (!mobile && localStorage.getItem('anb-navbar') == 'yes') {
            $leftPanel.addClass('collapsed');
            $('header, footer').addClass('collapsed');
        }
        
        $leftPanel.find(".navigation > ul > li:has(ul) > a").on('click', function() {
            
            if( $leftPanel.hasClass('collapsed') == false || $(window).width() < 768 ) {
                $leftPanel.find(".navigation > ul > li > ul").slideUp(300);
                $leftPanel.find(".navigation > ul > li").removeClass('active');
            
                if(!$(this).next().is(":visible")) {
                    $(this).next().slideToggle(300);
                    $(this).closest('li').addClass('active');
                }
            
                return false;
            }   
        });

        $leftPanel.find(".navigation li ul").each(function () {
            var $this = $(this);
            if ($this.find('li').length == 0) {
                $this.remove();
            }
        });
        
        var $document = $(document),
            $container = $('#container'),
            $btnBanner = $('#btn-banner'),
            $btnFS = $('#btn-fullscreen'),
            $btnScroll = $('#btn-scroll-top');
            
        $btnFS.on('click', function (e) {
            e.preventDefault();
            Anb.fullscreen();
            $btnFS.toggleClass('active');
            $btnFS.tooltip('destroy').prop('title', $btnFS.hasClass('active') ? 'Salir pantalla completa' : 'Pantalla completa');
            setTimeout(function () {$btnFS.tooltip()}, 300);
        });
        
        var exitFullScreen = function () {
            $btnFS.removeClass('active');
            $btnFS.tooltip('destroy').prop('title', 'Pantalla completa');
            setTimeout(function () {$btnFS.tooltip()}, 300);
        }

        $document.on('scroll', function () {
            $btnScroll[$document.scrollTop() > 200 ? 'fadeIn' : 'fadeOut']();
        });
        
        $btnScroll.on('click', function(e) {
            e.preventDefault();
            $("html, body").animate({ scrollTop: 0 }, 800);
        });
        
        $document.on('keyup', function(e) {
            if (e.keyCode == 27) {
                exitFullScreen();
            }
        });
        
        document.addEventListener("mozfullscreenchange", function () {
            if (!document.mozFullScreen) {
                exitFullScreen();
            }
        }, false);
                
        if (localStorage.getItem('anb-banner') == 'yes' || localStorage.getItem('anb-banner') == null) {
            $btnBanner.addClass('active');
        } else {
            $btnBanner.tooltip('destroy').prop('title', 'Mostrar banner');
            setTimeout(function () {$btnBanner.tooltip()}, 300);
            $container.addClass('logo-off');
            $('#anb-messages').css({top: 50})
        }
        
        $btnBanner.on('click', function (e) {
            e.preventDefault();
            var active = $btnBanner.hasClass('active');
            $container.toggleClass('logomain-off');
            $btnBanner.toggleClass('active');
            $btnBanner.tooltip('destroy').prop('title', active ? 'Mostrar banner' : 'Ocultar banner');
            setTimeout(function () {$btnBanner.tooltip()}, 300);
            localStorage.setItem('anb-banner', active ? 'no' : 'yes');
            $('#anb-messages').css({top: active ? 125 : 50});
        });

        if (Anb.ie > 0 && Anb.ie < 10) {
            alert("Usted tiene la versión " + Anb.ie + " de Internet Explorer, el sistema no es compatible con esta versión, por favor use Chrome o Firefox, o una versión mayor o igual a Internet Explorer 10.");
            throw new Error('Navegador no soportado');
        }
    }
    
    var resetSave = function () {
        $('.btn-save').each(function () {
            $(this).button('reset');
        });
    }

    var render = function (container) {
        $(container + ' .required').each(function () {
            var $el = $(this),
                $label = $el.prev('label');
            if ($label.length == 0) {
                $label = $el.parent().prev('label');
            }
            $label.addClass('label-required');
        });
        Anb.form.integer(container + ' .integer');
        Anb.form.decimal(container + ' .decimal');
        Anb.form.numeric(container + ' .numeric');
        Anb.form.alpha(container + ' .alpha');
        Anb.form.alphaNumeric(container + ' .alpha-numeric');
        Anb.form.alphaDash(container + ' .alpha-dash');
        Anb.form.maxlength(container);
        Anb.datetime.date(container + ' .datepicker');
        Anb.datetime.dateRange(container + ' .input-daterange');
        
        $('.number-format').each(function () {
            this.innerHTML = this.innerHTML.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1.");
        });
        
        $('.date-literal').each(function () {
            this.innerHTML = Anb.datetime.dateLiteral(this.innerHTML);
        });
        
        $('.datetime-literal').each(function () {
            this.innerHTML = Anb.datetime.datetimeLiteral(this.innerHTML);
        });
        
        $('.btn-save').on('click', function () {
            $(this).button('loading');
        });

        if ($.fn.tooltip) {
            $(container + ' [data-toggle="tooltip"]').tooltip();
        }
        if ($.fn.popover) {
            $(container + ' [data-toggle="popover"]').popover();
        }
    }

    init(); // begin load

    var load = {};

    load.render = render;
    load.resetSave = resetSave;

    return load;

}());