$(document).ready(function () {
    $(window).on('load', function() {
        $('[title]').each(function() {
            $(this).attr('data-bs-toggle', 'tooltip');
        });

        $('[data-bs-toggle=tooltip]').tooltip('dispose').tooltip();

        $('#nextUsername').attr('disabled', true);
        $('#nextPassword').attr('disabled', true);
        $('#submitKeyCode').attr('disabled', true);

        refreshImage($('.img-keyCode'));
    });

    els = document.querySelectorAll(".possibly-scaled");
    for (let el of els) {
        let xScale = el.clientWidth / el.scrollWidth;
        if (xScale < 1) {
            el.style.transform = "scaleX(" + xScale + ")";
        }
    }

    $(document).on('input', '#email', function (e) {
        if ($(this).val().length > 0) {
            $(this).css('border', '');
            $('<span /><span /><span /><span />').appendTo('.usernameGroup .animated-border');
            $('#nextUsername').removeAttr('disabled');
        } else {
            $('.usernameGroup .animated-border span').remove();
            $('#nextUsername').attr('disabled', true);
        }
    });

    $(document).on('input', '#password', function (e) {
        if ($(this).val().length > 0) {
            $('<span /><span /><span /><span />').appendTo('.passwordGroup .animated-border');
            $('#nextPassword').removeAttr('disabled');
            $('#togglePassword').show();
        } else {
            $('.passwordGroup .animated-border span').remove();
            $('#nextPassword').attr('disabled', true);
            $('#togglePassword').hide();
        }
    });

    $(document).on('input', '#keyCode', function (e) {
        if ($(this).val().length > 0) {
            $('<span /><span /><span /><span />').appendTo('.keyCodeGroup .animated-border');
            $('#submitKeyCode').removeAttr('disabled');
        } else {
            $('.keyCodeGroup .animated-border span').remove();
            $('#submitKeyCode').attr('disabled', true);
        }
    });

    // username group
    $(document).on('keypress', '.userGroup', function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode == '13') {
            e.preventDefault();
            if (!$('#nextUsername').attr('disabled')) {
                $("#nextUsername").click();
            }
        }
    });

    // password group
    $(document).on('keyup', '.passGroup', function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode == '27') {
            $("#backPassword").click();
        }
    });
    $(document).on('keypress', '.passGroup', function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode == '13') {
            e.preventDefault();
            if (!$('#nextPassword').attr('disabled')) {
                $("#nextPassword").click();
            }
        }
    });

    // keycode group
    $(document).on('keyup', '.keyGroup', function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode == '27') {
            $("#backKeyCode").click();
        }
    });
    $(document).on('keypress', '.keyGroup', function (e) {
        var keycode = (e.keyCode ? e.keyCode : e.which);
        if (keycode == '13') {
            if ($('#submitKeyCode').attr('disabled')) {
                e.preventDefault();
            }
        }
    });

    $('#rippleBox').on('click', createRipple);
})

function getNewKeyCode(output) {
    $.get("/login", function(result, status) {
        if(status === 'success') {
            refreshImage(output);
        };
    });
}

function refreshImage(output) {
    $.get("/images/keycode", function(result) {
        output.attr('src', "data:image/png;base64," + result);
    });
}

function createRipple(event) {
    event.preventDefault();
    const button = event.currentTarget;
    const img = document.getElementsByClassName("img-keyCode")[0];

    const circle = document.createElement("span");
    const diameter = img.clientHeight / 2;
    const radius = diameter / 2;

    circle.style.width = circle.style.height = `${diameter}px`;
    circle.style.left = `${event.clientX - button.offsetLeft - diameter}px`;
    circle.style.top = `${event.clientY - button.offsetTop - diameter - radius/4}px`;
    circle.classList.add("ripple");

    const ripple = button.getElementsByClassName("ripple")[0];

    if (ripple) {
        ripple.remove();
    }

    button.appendChild(circle);

    getNewKeyCode($('.img-keyCode'));

    $('#keyCode').focus();
}
