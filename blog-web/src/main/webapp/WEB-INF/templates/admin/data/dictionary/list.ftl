<#assign title="数据字典">
<#assign ptitle="数据">

<#assign type = RequestParameters.type!'' />
<#assign value = RequestParameters.value!'' />

<@override name="actions">
<a href="${ctx}/admin/data/dictionary/create" class="btn btn-sm btn-primary" data-backdrop="static" data-toggle="modal"
   data-target="#myModal">添加</a>
</@override>

<@override name="content">
<form class="form-inline items-form" method="get" novalidate>
    <div class="form-group">
        <select name="type" class="form-control">
            <option value="">全部类型</option>
            <#list types as e>
                <option value="${e.getType()}" <#if type=='${e.getType()}'>selected</#if>>${e.getName()}</option>
            </#list>
        </select>
    </div>
    <div class="form-group">
        <input type="text" class="form-control" name="value" value="${value}" placeholder="名称"/>
    </div>

    <button class="btn btn-sm btn-purple">
        搜索
        <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
    </button>
</form>

<div class="space-10"></div>

<table id="dictionary-table" class="table table-striped table-bordered table-hover">
    <thead>
    <tr>
        <th>字典代码</th>
        <th>类型</th>
        <th>名称</th>
        <th>排序</th>
        <th>逻辑删除</th>
        <th>创建时间</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
        <#if page.list?size gt 0>
            <#list page.list as dictionary>
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
    <@c.pagination url="${ctx}/admin/data/dictionary" param="type=${type}&value=${value}"/>
</@override>

<@override name="script">
<script src="${ctx}/static/app/js/admin/data/dictionary/list.js"></script>
</@override>

<@extends name="../../layout.ftl"/>