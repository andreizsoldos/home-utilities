function focusInputBox() {
  document.getElementById("newIndex").focus();
}

$(document).ready(function () {
  $(window).on("load", function() {
    $("#staticBackdrop").modal("show");
  });
});
