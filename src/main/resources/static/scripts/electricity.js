$(document).ready(function () {
    $("button").hover(function() {
        $("button").click(function() {
            if($(this).find("a").hasClass("active")) {
                $("button a").hide();
                $("button a").removeClass("active");
            } else {
                $(this).find("a").addClass("active");
                $(this).find("a").show();
            }
        });
    });

    var clientId;
    $("button a").click(function() {
        clientId = this.id;
        if($(this).hasClass("active")) {
            $("#staticBackdrop").modal("show");
        }
    });

    $("#setDelete").click(function() {
        deleteClient("electricity", clientId);
    });
});

function deleteClient(branch, id) {
    var hdr = $("meta[name='_csrf_header']").attr("content");
    var tok = $("meta[name='_csrf']").attr("content");

    return fetch("/user/dashboard/" + branch + "/client-code/" + id, {
        method: 'DELETE',
        headers: {
            [[hdr]]: tok
        }
    })
    .then(response => response.json())
    .then(data => {
        window.location.href = "/user/dashboard/" + branch;
    })
    .catch(err => err);
}
