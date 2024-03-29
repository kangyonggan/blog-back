<#assign ctx="${(rca.contextPath)!''}">
<#assign title = RequestParameters.title!'' />

<div class="page-header">
    <h1>
        文章列表
        <small class="pull-right">
            <a href="#user/article/create" class="btn btn-sm btn-inverse">发布</a>
        </small>
    </h1>
</div>

<div class="space-10"></div>

<form class="form-inline" method="get">
    <div class="form-group">
        <input type="text" class="form-control" name="title" value="${title}" placeholder="标题" autocomplete="off"/>
    </div>

    <button class="btn btn-sm btn-inverse" data-toggle="search-submit">
        搜索
        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
    </button>
</form>

<div class="space-10"></div>

<table id="article-table" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th>标题</th>
        <th>标签</th>
        <th>逻辑删除</th>
        <th>发布时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <#if page.list?size gt 0>
        <#list page.list as article>
            <#include "table-tr.ftl"/>
        </#list>
    <#else>
    <tr>
        <td colspan="20">
            <div class="empty">暂无查询记录</div>
        </td>
    </tr>
    </#if>
    </tbody>
</table>
<@c.pagination url="#user/article" param="title=${title}"/>


<script src="${ctx}/static/app/js/dashboard/user/article/list.js"></script>
