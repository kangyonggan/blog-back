$(function () {
    document.title = title;

    $(".markdown-content a").prop("target", "_blank");

    var $pres = $(".markdown-content pre");

    for (var i = 0; i < $pres.length; i++) {
        var $pre = $($pres[i]);
        var $code = $pre.find("code");
        var language = $code.attr("class");
        if (!language) {
            language = "bash";
        }
        var html = $code.html();
        $pre.empty();
        $pre.addClass("brush:" + language);
        $pre.html(html);
    }

    SyntaxHighlighter.all();
});