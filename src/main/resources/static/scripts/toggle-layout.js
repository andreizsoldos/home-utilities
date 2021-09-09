$(document).ready(function () {
    $('#sidebarCollapse').click(function (e) {
        $('#sidebar').toggle();
        if ($(this).find('i').hasClass('bi-arrow-left-square')) {
            $(this).find('i').removeClass('bi-arrow-left-square');
            $(this).find('i').addClass('bi-arrow-right-square');
            $(this).removeClass('active');
        } else {
            $(this).find('i').removeClass('bi-arrow-right-square');
            $(this).find('i').addClass('bi-arrow-left-square');
            $(this).addClass('active');
        }
    });
})
