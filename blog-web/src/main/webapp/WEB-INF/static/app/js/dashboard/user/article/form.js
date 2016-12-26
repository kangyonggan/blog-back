$(function () {
    $("#DASHBOARD_USER").addClass('active open');
    $("#DASHBOARD_USER_ARTICLE").addClass('active');

    /**
     * 初始化select2
     */
    $('.select2').select2({allowClear: true});

    /**
     * 初始化markdown编辑器
     */
    $("#content-md").markdown({resize: 'vertical'});

    /**
     * 附件
     */
    $('input[type=file]').ace_file_input()
        .closest('.ace-file-input')
        .addClass('width-90 inline')
        .wrap('<div class="form-group file-input-container"><div class="col-sm-7"></div></div>');

    $('#id-add-attachment')
        .on('click', function () {
            var file = $('<input type="file" name="attachment[]" />').appendTo('#form-attachments');
            file.ace_file_input();

            file.closest('.ace-file-input')
                .addClass('width-90 inline')
                .wrap('<div class="form-group file-input-container"><div class="col-sm-7"></div></div>')
                .parent().append('<div class="action-buttons pull-right col-xs-1">\
            <a href="#" data-action="delete" class="middle">\
                <i class="ace-icon fa fa-trash-o red bigger-130 middle"></i>\
            </a>\
        </div>')
                .find('a[data-action=delete]').on('click', function (e) {
                e.preventDefault();
                $(this).closest('.file-input-container').hide(300, function () {
                    $(this).remove()
                });
            });
        });

    /**
     * 提交
     */
    $("#submit").click(function () {
        var tags = $("#tags").val();

        if (!tags) {
            Notify.error("请选择标签");
            return false;
        }

        return true;
    });

    var $form = $('#article-form');

    $form.validate({
        rules: {
            title: {
                required: true,
                maxlength: 64
            }
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        },
        errorElement: "div",
        errorClass: "error"
    });
});