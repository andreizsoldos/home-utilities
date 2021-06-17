$(document).ready(function () {
    var currentTabId;
    $("button").click(function() {
        currentTabId = this.id.substr(3).substr(0, this.id.substr(3).length - 3);
        sessionStorage.setItem("currentActiveTab", currentTabId);
        if($(this).find("a").hasClass("active")) {
            $(this).find("a").removeClass("active");
            $(this).find("a").hide();
        } else {
            $("button a").removeClass("active");
            $("button a").hide();
            $(this).find("a").addClass("active");
            $(this).find("a").show();
        }
    });

    var clientId;
    $("button a").click(function() {
        var buttonId = this.id;
        if(buttonId.startsWith("delete")) {
            if($(this).hasClass("active")) {
                clientId = buttonId.substr(6);
                $("#deleteBackdrop").modal("show");
            }
        } else if(buttonId.startsWith("put")) {
            if($(this).hasClass("active")) {
                clientId = buttonId.substr(3);
                $(this).attr("href", "/user/dashboard/electricity/client-code-edit/" + clientId);
            }
        }
    });

    $("#setDelete").click(function() {
        deleteClient("electricity", clientId);
    });

    $(window).on("load", function() {
        windowPath = window.location.href;
        if(windowPath.indexOf("#") == -1) {
            activateTab();
        } else {
            var tabId = windowPath.substr(windowPath.indexOf("#") + 1, windowPath.length - windowPath.indexOf("#") + 1);
            sessionStorage.setItem("currentActiveTab", tabId);
            window.location.href = "/user/dashboard/electricity";
            activateTab();
        }
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

function activateTab() {
    var tabId = sessionStorage.getItem("currentActiveTab");
    if (tabId != null) {
        $('.nav-tabs button[data-bs-target="#nav' + tabId + '"]').tab('show');
    }
};
