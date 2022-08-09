$(document).ready(function () {

  $(window).on('load', function () {
    $('[title]').each(function () {
      $(this).attr('data-bs-toggle', 'tooltip');
    });

    $('[data-bs-toggle=tooltip]').tooltip('dispose').tooltip();

    $('#register-section .next').attr('disabled', true);
  });

  $(document).on('input', '#email, #password, #repeatPassword', function (e) {
    if ($('#email').val().length > 0 && $('#password').val().length > 0 && $('#repeatPassword').val().length > 0) {
      $('<span /><span /><span /><span />').appendTo('#register-section .field1 ~ .animated-border');
      $('#register-section .field1 ~ .next').removeAttr('disabled');
    } else {
      $('#register-section .field1 ~ .animated-border span').remove();
      $('#register-section .field1 ~ .next').attr('disabled', true);
    }
  });

  $(document).on('input', '#firstName, #lastName, #gender-male, #gender-female, #gender-other', function (e) {
    if ($('#firstName').val().length > 0 && $('#lastName').val().length > 0 && ($('#gender-male').is(':checked') || $('#gender-female').is(':checked') || $('#gender-other').is(':checked'))) {
      $('<span /><span /><span /><span />').appendTo('#register-section .field2 ~ .animated-border');
      $('#register-section .field2 ~ .next').removeAttr('disabled');
    } else {
      $('#register-section .field2 ~ .animated-border span').remove();
      $('#register-section .field2 ~ .next').attr('disabled', true);
    }
  });

  $(document).on('input', '#terms, #gdpr', function (e) {
    if ($('#terms').is(':checked') && $('#gdpr').is(':checked')) {
      $('<span /><span /><span /><span />').appendTo('#register-section .field3 ~ .animated-border');
      $('#register-section .field3 ~ .next').removeAttr('disabled');
    } else {
      $('#register-section .field3 ~ .animated-border span').remove();
      $('#register-section .field3 ~ .next').attr('disabled', true);
    }
  });

  readEnterKeyboard('#email, #password, #repeatPassword, #firstName, #lastName, #gender-male, #gender-female, #gender-other, #terms, #gdpr');
  readEscapeKeyboard('#firstName, #lastName, #gender-male, #gender-female, #gender-other, #terms, #gdpr');

  $('.submit').on('click', function () {
    let submitCount = 0;
    let submitDelay = setInterval(function () {
      submitCount++;
      if (submitCount > 1) {
        clearInterval(submitDelay);
        $('#msform').submit();
      }
    }, 1000);
  });
});

function setProgressBar(curStep) {
  var steps = $("fieldset").length;
  var percent = parseFloat(100 / steps) * curStep;
  percent = percent.toFixed();
  $(".progress-bar").css("width", percent + "%")
}

function validateEmail(index, field, matchExpression, mustMatch, exceptionMessage) {
  switch (mustMatch) {
    case true:
      return match(field, matchExpression) === true ? true : createErrorDiv(index, field, exceptionMessage);
    case false:
      return match(field, matchExpression) === false ? true : createErrorDiv(index, field, exceptionMessage);
  }
}

function validateMaxLength(index, field, length, exceptionMessage) {
  if (field.val().length > length) {
    return createErrorDiv(index, field, exceptionMessage);
  }
  return true;
}

function validateMinLength(index, field, length, exceptionMessage) {
  if (field.val().length < length) {
    return createErrorDiv(index, field, exceptionMessage);
  }
  return true;
}

function validateEquality(index, field, fieldToMatch, exceptionMessage) {
  if (fieldToMatch.val() !== field.val()) {
    return createErrorDiv(index, field, exceptionMessage);
  }
  return true;
}

function match(field, matchExpression) {
  return field.val().match(matchExpression) !== null;
}

function createErrorDiv(index, field, exceptionMessage) {
  let exceptionId = "exceptionError" + index;
  $("#" + exceptionId).remove();
  $('<div class="errors" id="' + exceptionId + '"></div>').insertAfter('#' + field.attr('id') + ' ~ label');
  let exceptionError = $("#" + exceptionId);
  field.css('border', '1px solid red');
  exceptionError.html(exceptionMessage);
  exceptionError.show();
  field.focus();
  countDownFailedLogin(field, exceptionError, new Date().getTime(), 5);
  return false;
}

function countDownFailedLogin(field, exceptionErrorId, countDownDate, durationInterval) {
  let hideCount = 0;
  let updateCount = setInterval(function () {
    hideCount++;
    if (hideCount > durationInterval) {
      clearInterval(updateCount);
      field.css('border', '');
      exceptionErrorId.html("");
      exceptionErrorId.remove();
    }
  }, 1000);
}

function readEnterKeyboard(selector) {
  $(document).on('keypress', selector, function (e) {
    var keycode = (e.keyCode ? e.keyCode : e.which);
    var nextButton = $(this).closest("fieldset").children("button.next");
    if (!nextButton.attr('disabled')) {
      if (keycode === 13) {
        e.preventDefault();
        nextButton.click();
      }
    }
  });
}

function readEscapeKeyboard(selector) {
  $(document).on('keyup', selector, function (e) {
    var keycode = (e.keyCode ? e.keyCode : e.which);
    var prevField = $(this).closest("fieldset").children("button.previous");
      if (keycode === 27) {
        e.preventDefault();
        prevField.click();
      }
  });
}
