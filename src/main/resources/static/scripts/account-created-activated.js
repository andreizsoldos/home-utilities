$(window).on("load", function() {
  var progressBar = document.getElementById("progressBar");
  $(".check-icon").hide();
  setTimeout(function () {
    $(".check-icon").show();
    smoothProgress(progressBar);
  }, 1000);
});

function smoothProgress(e) {
    var id = $("#" + e.id),
    dur = parseFloat(id.attr("data-duration")) * 1000,
    seq = 50,
    max = parseInt(id.attr("max"), 10),
    min = 0,
    chunk = max / dur * seq;
    id.val(max);
    var loop = setInterval(function() {
        if(id.val() > min)
            id.val(id.val() - chunk);
        else {
            clearInterval(loop);
            window.location.href = "/login";
        }
    }, seq);
}
