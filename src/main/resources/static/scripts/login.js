const event = document.createEvent('Event');
event.initEvent('input', true, true);
var pass = document.getElementById("password");
var togglePass = document.getElementById("togglePassword");

pass.addEventListener('input', function() {
    if (pass.value.length > 0) {
        togglePass.style.display = "block";
    } else {
        togglePass.style.display = "none";
    }
}, false);
pass.dispatchEvent(event);

$(window).on("load", function() {
    $('[title]').each(function() {
        $(this).attr('data-bs-toggle', 'tooltip');
    });

    $('[data-bs-toggle=tooltip]').tooltip('dispose').tooltip();

    var exceptionErrorId = document.getElementById("exceptionError");
    if (exceptionErrorId != null) {
        var counterErrorId = document.getElementById("counterError");
        var countDownDate = null;
        if (counterErrorId != null) {
            if (counterErrorId.getAttribute("data-counter") != null) {
                countDownDate = counterErrorId.getAttribute("data-counter");
            } else {
                countDownDate = new Date().getTime();
            }
            countDownFailedLogin(counterErrorId, exceptionErrorId, countDownDate);
        } else {
            countDownDate = new Date().getTime();
            countDownFailedLogin(counterErrorId, exceptionErrorId, countDownDate);
        }
    }
})

function countDownFailedLogin(counterErrorId, exceptionErrorId, countDownDate) {
    if (countDownDate != null && counterErrorId != null) {
        var hideCount = 0;
        var updateCount = setInterval(function() {
            var now = new Date().getTime();
            var distance = countDownDate - now;

            // var days = Math.floor(distance / (1000 * 60 * 60 * 24));
            var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
            var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
            var seconds = Math.floor((distance % (1000 * 60)) / 1000);

            counterErrorId.innerHTML = (hours <= 9 ? "0" + hours : hours) + ":"
                         + (minutes <= 9 ? "0" + minutes : minutes) + ":"
                         + (seconds <= 9 ? "0" + seconds : seconds);

            hideCount++;
            if (hideCount > 10 || distance < 0) {
                clearInterval(updateCount);
                counterErrorId.innerHTML = "";
                counterErrorId.style.display = "none";
                exceptionErrorId.innerHTML = "";
                exceptionErrorId.style.display = "none";
            }
        }, 1000);
    } else if (exceptionErrorId.innerHTML == "* E-mail or Password are incorrect" || exceptionErrorId.innerHTML == "* E-mail sau Parola sunt greșite") {
        var hideCount = 0;
        var updateCount = setInterval(function() {
            hideCount++;
            if (hideCount > 10) {
                clearInterval(updateCount);
                exceptionErrorId.innerHTML = "";
                exceptionErrorId.style.display = "none";
            }
        }, 1000);
    }
}
