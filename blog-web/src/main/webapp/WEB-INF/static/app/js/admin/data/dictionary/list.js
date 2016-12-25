$(function () {
    $("#ADMIN_DATA").addClass('active open');
    $("#ADMIN_DATA_DICTIONARY").addClass('active');

    var $table = $('#dictionary-table');

    $table.on('click', 'a[data-role=dictionary-delete]', function () {
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