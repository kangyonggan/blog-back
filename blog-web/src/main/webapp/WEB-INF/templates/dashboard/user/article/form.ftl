<#assign title="${article.id???string('编辑文章', '添加文章')}">
<#assign ptitle="我的">
<#assign p = RequestParameters.p!'1' />

<@override name="style">
<link rel="stylesheet" href="${ctx}/static/ace/dist/css/dropzone.min.css"/>
<link rel="stylesheet" href="${ctx}/static/libs/bootstrap/css/bootstrap-markdown.min.css"/>
<link rel="stylesheet" href="${ctx}/static/ace/dist/css/select2.min.css"/>
</@override>

<@override name="content">
<form id="article-form" method="post" enctype="multipart/form-data" class="form-horizontal"
      action="${ctx}/dashboard/user/article/${article.id???string('update', 'save')}?p=${p}">

    <#if article.id??>
        <input type="hidden" name="id" value="${article.id}"/>
    </#if>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right">文章标题<span class="red">*</span></label>
        <div class="col-xs-12 col-sm-5">
            <@spring.formInput "article.title" 'class="form-control" placeholder="简单描述一下文章,最多64字"'/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label no-padding-right">标签</label>
        <div class="col-xs-12 col-sm-5">
            <select multiple="" name="tagCodes" id="tagCodes" class="select2 width-100" data-placeholder="标签">
                <#list dictionaries?? && dictionaries as tag>
                    <option value="${tag.code}"
                            <#if tags?? && tags?seq_contains(tag.code)>selected</#if>>${tag.name}</option>
                </#list>
            </select>
        </div>
    </div>

    <div class="form-group" id="markdown-content">
        <label class="col-xs-2 control-label pull-left">内容<span class="red">*</span></label>
        <div class="col-xs-10 col-xs-offset-1">
                <textarea required name="content" class="width-100" id="content-md" rows="13"
                          placeholder="请输入文章内容">${article.content!''}</textarea>
        </div>
    </div>

    <div class="hr hr-18 dotted"></div>

    <div class="form-group">
        <label class="col-xs-10 col-xs-offset-1 pull-left">附件</label>

        <div class="col-xs-10 col-xs-offset-1">
            <div id="form-attachments">
                <#if article.id??>
                    <div class="center">编辑时不允许修改原有的附件，但可以添加新的附件</div>
                    <div class="space-10"></div>
                </#if>
                <input type="file" name="attachment[]"/>
            </div>
        </div>
    </div>

    <div class="form-group">
        <div class="center">
            <button id="id-add-attachment" type="button" class="btn btn-sm btn-danger">
                <i class="ace-icon fa fa-paperclip bigger-140"></i>
                添加更多
            </button>
        </div>
    </div>

    <div class="clearfix form-actions">
        <div class="center">
            <input id="submit" type="submit" class="btn btn-info width-10" data-toggle="form-submit"
                   data-loading-text="正在提交..."
                   value="<@spring.message "app.button.save"/>"/>
        </div>
    </div>
</form>
</@override>

<@override name="script">
<script type="text/javascript" src="${ctx}/static/ace/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${ctx}/static/libs/bootstrap/js/marked.min.js"></script>
<script type="text/javascript" src="${ctx}/static/libs/bootstrap/js/bootstrap-markdown.min.js"></script>
<script src="${ctx}/static/app/js/dashboard/user/article/form.js"></script>
</@override>

<@extends name="../../layout.ftl"/>