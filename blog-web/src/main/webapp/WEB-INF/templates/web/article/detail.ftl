<#assign ctx="${(rca.contextPath)!''}">

<link rel="stylesheet" href="${ctx}/static/libs/markdown/styles/shCoreDefault.css"/>

<div class="space-20"></div>

<div class="col-xs-10 col-xs-offset-1">
    <div class="widget-box transparent">
        <div class="widget-header widget-header-large">
            <h3 class="widget-title grey lighter">
                <i class="ace-icon fa fa-leaf green"></i>
            ${article.title}
            </h3>

            <div class="widget-toolbar no-border invoice-info">
                <span class="invoice-info-label">来源:</span>
                <span class="red">${article.createFullname}</span>

                <br>
                <span class="invoice-info-label">时间:</span>
                <span class="blue"><@c.relative_date datetime=article.createdTime/></span>
            </div>

            <div class="widget-toolbar hidden-480">
                <a href="javascript:">
                    <i class="ace-icon fa fa-print"></i>
                </a>
            </div>
        </div>

        <div class="space-10"></div>

        <div class="widget-body">
            <div class="widget-main">
                <div class="row markdown-content">
                ${article.content}
                </div>
            </div>
        </div>

        <#if attachments?size gt 0>

            <div class="space-10"></div>

            <div class="hr hr-18 dotted"></div>

            <h4>附件:</h4>

            <div class="space-10"></div>

            <@apps>
                <#list attachments as attachment>
                    <a href="${ftpUrl}/${attachment.path}" target="_blank">${attachment.name}</a>
                    <div class="space-10"></div>
                </#list>
            </@apps>
        </#if>
    </div>
</div>

<script src="${ctx}/static/libs/markdown/scripts/shCore.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushXml.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushSql.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushPowerShell.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushJScript.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushJava.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushCss.js"></script>
<script src="${ctx}/static/libs/markdown/scripts/shBrushBash.js"></script>
<script>
    var title = '${article.title}';
</script>
<script src="${ctx}/static/app/js/web/article/detail.js"></script>
