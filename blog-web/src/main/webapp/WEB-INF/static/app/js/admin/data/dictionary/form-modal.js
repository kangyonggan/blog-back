$(function () {

    var $form = $('#modal-form');

    $form.validate({
        rules: {
            code: {
                required: true,
                isDicCode: true,
                remote: {
                    url: "/validate/dictionary",
                    type: 'post',
                    data: {
                        'code': function () {
                            return $('#code').val()
                        },
                        'oldCode': function () {
                            return $('#old-code').val();
                        }
                    }
                }
            },
            value: {
                required: true,
                rangelength: [1, 64]
            },
            sort: {
                required: true,
                isDicSort: true
            }
        },
        messages: {
            username: {
                remote: "不可用"
            }
        },
        submitHandler: function (form, event) {
            event.preventDefault();
            $(form).ajaxSubmit({
                dataType: 'json',
                success: function (response) {
                    if (response.errCode == 'success') {
                        window.location.reload();
                    } else {
                        Notify.error(response.errMsg);
                    }
                },
                error: function () {
                    Notify.error("服务器内部错误，请稍后再试。");
                }
            });
        },
        errorPlacement: function (error, element) {
            error.appendTo(element.parent());
        },
        errorElement: "div",
        errorClass: "error"
    });
});
