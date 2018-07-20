/**
 * jQuery Salid (Simple Validation) Plugin 1.0.0
 *
 * http://www.jqueryin.com/projects/salid-the-simple-jquery-form-validator/
 * http://plugins.jquery.com/project/Salid
 *
 * Copyright (c) 2009 Corey Ballou
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
(function($dsm) {
	var hasErrors = false;
	var errors = [];
	
	/* validate entire form */
	$dsm.fn.salidate = function(elemsToValidate, errorCallback) {
		var $dsmform = $dsm(this);
		if (!$dsmform.is('form')) {
			console.log('The salidate function only works with forms.');
			return false;
		}
		if (typeof errorCallback != 'function') errorCallback = salid_errorHandler;
		$dsmform.submit(function() {
			$dsm.each(elemsToValidate, function(field, params) {
				validate(field, params, $dsmform);
			});
			return (hasErrors) ? handleErrors(errorCallback, $dsmform) : true;
		});
	}

	/* validate individual elements */
	$dsm.fn.salid = function(event, params, errorCallback) {
		var tagname = this.get(0).tagName.toLowerCase();
		var validElems = {
			input: ['blur','keydown','keyup'],
			select: ['change','blur'],
			textarea: ['blur','focus','keydown','keyup']
		};
		if (typeof validElems[tagname] == 'undefined') {
			console.log(escape(tagname) + ' cannot be used to for singular validation.');
			return false;
		}
		event = event || 'blur';
		if (!$dsm.inArray(validElems[tagname], event)) {
			console.log('You cannot bind the event ' + escape(event) + ' to the tag ' + escape(tagname));
			return false;
		}
		if (typeof errorCallback != 'function') errorCallback = salid_errorHandler;
		if (typeof params == 'String' && params == 'unbind') {
			this.each(function() { $dsm(this).unbind(event); });
			return false;
		}
		if (typeof params == 'undefined') params = {};
		this.each(function() {
			var $dsmthis = $dsm(this);
			$dsmthis.bind(event, function() {
				if (!validate($dsmthis, params)) {
					handleErrors(errorCallback, $dsmthis);
				}
			});
		});
	}
	
	/* handle validation type */
	function validate(field, params, $dsmform) {
		var valid = true;
		if (typeof params.callbacks != 'undefined' && params.callbacks.constructor == Array) {
			$dsm.each(params.callbacks, function(idx, rule) {
				if (!_validate(field, rule, $dsmform)) valid = false;
			});
			return valid;
		} else return _validate(field, params, $dsmform);
	}
	
	/* run a validation function */
	function _validate(field, params, $dsmform) {
		var fcn, $dsmfield, rules, singlerule = false;
		var obj = (function() { return this; })();
		var defaults = { callback : 'required', callbackParams : null, msg : 'This field is required' };
		if (field instanceof jQuery) {
			singlerule = true;
			$dsmfield = field;
			field = field.attr('id') || field.attr('name');
		} else $dsmfield = getElem($dsmform, field);
		// pass over disabled fields
		if ($dsmfield.attr('disabled') == false) {
			// remove any old preexisting errors
			if ($dsmfield.is(':radio') || $dsmfield.is(':checkbox')) {
				$dsmfield.parent('label').removeClass('error').next('span.error').remove();
			} else {
				$dsmfield.removeClass('error').parent().find('span.error').remove();
			}
			rules = $dsm.extend({}, defaults, params);
			rules = $dsm.metadata ? $dsm.extend({}, rules, $dsmfield.metadata()) : rules;
			fcn = 'salid_' + rules.callback;
			if (typeof obj[fcn] != 'function') {
				console.log('It does not appear as though the validation function ' + fcn + ' exists.');
				return false;
			}
			if (!obj[fcn]($dsmfield, rules.callbackParams)) {
				addError(field, $dsmfield, rules.msg);
				hasErrors = true;
				return false;
			}
		}
		return true;
	}

	/* add an error */
	function addError(field, $dsmfield, msg) {
		var fixed = field.replace(/\[/i, 'OB');
		fixed = fixed.replace(/\]/i, 'CB');
		errors[ errors.length ] = { elem: $dsmfield, field: field, msg: msg };
	}

	/* handle errors by calling a callback function */
	function handleErrors(errorCallback, $dsmelem) {
		var e, i = errors.length;
		while(i--) { e = errors[i]; $dsm(e.elem).addClass('error'); }
		errorCallback($dsmelem, errors);
		hasErrors = false;
		errors = [];
		return false;
	}

	/* Attempt to find the form element by id with fallback on name. */
	function getElem($dsmelem, field) {
		if ($dsmelem.get(0).tagName.toLowerCase() != 'form') return $dsmelem;
		if (field instanceof jQuery) return field;
		field = field.replace(/(\[(.*)?\])/, '');
		var $dsmfound = $dsmelem.find("#"+field);
		if ($dsmfound.length == 0) $dsmfound = $dsmelem.find("*[name^='"+field+"']");
		return $dsmfound;
	}
	
})(jQuery);

/* default error handler popup */
function salid_errorHandler($dsmelem, errors) {
	var isformelem, $dsme, e, i = errors.length;
	isformelem = $dsmelem.get(0).tagName.toLowerCase() != 'form';
	while(i--) {
		e = errors[i];
		$dsme = $dsm(e.elem);
		if ($dsme.is(':radio') || $dsm(e).is(':checkbox')) {
			$dsme.parent('label').addClass('error').after('<span class="error"><span>' + e.msg + '</span></span>');
		} else {
			$dsme.addClass('error').parent().append('<span class="error"><span>' + e.msg + '</span></span>');
		}
	}
	return false;
}

/* required */
function salid_required(field, params) {
	if (field.length == 0 || typeof field == 'undefined') return false;
	if (/radio|checkbox/i.test(field[0].type)) return (field.filter(':checked').length > 0);
	return (field.length > 0 && field.val() != '');
}
/* minlength */
function salid_minlength(field, params) {
	return (!salid_required(field, params) || field.val().length >= params);
}
/* maxlength */
function salid_maxlength(field, params) {
	return (!salid_required(field, params) || field.val().length <= params);
}
/* email */
function salid_email(field, params) {
	return (!salid_required(field, params) || /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$dsm/i.test(field.val()));
}
/* phone */
function salid_phone(field, params) {
	var filter = /(\+\d)*\s*(\(\d{3}\)\s*)*\d{3}(-{0,1}|\s{0,1})\d{2}(-{0,1}|\s{0,1})\d{2}/;
	return (!salid_required(field, params) || filter.test(field.val()));
}
/* url */
function salid_url(field, params) {
	return (!salid_required(field, params) || /(https?|s?ftp):\/\//i.test(field.val()));
}
/* zipcode */
function salid_zipcode(field, params) {
	return (!salid_required(field, params) || /(^\d{5}(-\d{4})?)$dsm/.test(field.val()));
}
/* alpha */
function salid_alpha(field, params) {
	return (!salid_required(field, params) || /^[A-Za-z]+$dsm/.test(field.val()));
}
/* numeric */
function salid_numeric(field, params) {
	return (!salid_required(field, params) || /^[\d]+$dsm/.test(field.val()));
}
/* alpha-numeric */
function salid_alphanumeric(field, params) {
	return (!salid_required(field, params) || /^[A-Za-z0-9]+$dsm/i.test(field.val()));
}
/* alpha-dash (alpha-numeric with dashes and underscores allowed). */
function salid_alphadash(field, params) {
	return (!salid_required(field, params) || /^[A-Za-z0-9-_]+$dsm/i.test(field.val()));
}
/* matching values */
function salid_match(field, params) {
	if (typeof field == 'undefined') return false;
	var field2 = field.siblings('#' + params);
	if (field2.length == 0) field2 = field.siblings("*[name^='"+params+"']");
	if (field2.length == 0) return false;
	return (field.val() == field2.val());
}
