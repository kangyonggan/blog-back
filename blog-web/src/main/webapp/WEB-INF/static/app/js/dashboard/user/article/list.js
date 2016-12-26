$(function () {
    $("#DASHBOARD_USER").addClass('active open');
    $("#DASHBOARD_USER_ARTICLE").addClass('active');

    var $table = $('#article-table');

    $table.on('click', 'a[data-role=article-delete]', function () {
        var $trigger = $(this);
        var url = $trigger.data('url');
        var title = $trigger.attr("title");

        $.messager.confirm("提示", "确定" + title + "吗?", function () {
            $.get(url).success(function (html) {
                var $tr = $(html);
                $('#' + $tr.attr('id')).replaceWith($tr);
                Notify.success("操作成功");
            }).error(function () {
                Notify.error("网络错误，请稍后重试");
            })
        });
    });
});