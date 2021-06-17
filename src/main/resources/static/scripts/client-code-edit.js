function focusInputBox() {
  document.getElementById("clientNumber").focus();
}

$(document).ready(function () {
  $(window).on("load", function() {
    $("#editBackdrop").modal("show");
  });
});
