
const event = document.createEvent('Event');
event.initEvent('input', true, true);
var x = document.getElementById("password");
var y = document.getElementById("togglePassword");

x.addEventListener('input', function() {
    if (x.value.length > 0) {
        document.getElementById("togglePassword").style.display = "block";
    } else {
        x.type = "password";
        y.title = "Show password";
        y.classList.remove("bi-eye-slash-fill");
        y.classList.add("bi-eye");
        document.getElementById("togglePassword").style.display = "none";
    }
}, false);
x.dispatchEvent(event);

function revealPassword() {
    var x = document.getElementById("password");
    if (x.type === "password") {
        x.type = "text";
        x.focus();
        y.title = "Hide password";
        y.classList.remove("bi-eye");
        y.classList.add("bi-eye-slash-fill");
    } else {
        x.type = "password";
        y.title = "Show password";
        y.classList.remove("bi-eye-slash-fill");
        y.classList.add("bi-eye");
    }
}
