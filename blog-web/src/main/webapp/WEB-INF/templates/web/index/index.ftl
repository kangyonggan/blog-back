<#assign title="首页">

<#assign tag = RequestParameters.tag!'' />

<#assign color = ['btn-success', '', 'btn-warning', 'btn-danger', 'btn-info', 'btn-inverse'] />

<@override name="app-content">
<div class="space-10"></div>
<div class="row">
    <div class="col-xs-10 col-xs-offset-1 center">
        <#list tags as tag>
            <a class="btn ${color[tag_index%color?size]} btn-sm tag"
               href="${ctx}/?tag=${tag.code}">${tag.value}</a>
        </#list>
    </div>
</div>

<div class="hr"></div>

<div class="row">
    <div class="col-xs-10 col-xs-offset-1">
        <#if page.list?size gt 0>
            <#list page.list as article>
                <#include "item.ftl"/>
            </#list>
            <@c.pagination url="${ctx}/" param="tag=${tag}"/>
        <#else>
            <div class="center"><h3>没有符合条件的文章</h3></div>
        </#if>
    </div>
<div>

    <div class="clearfix"></div>
</@override>

<@extends name="../layout.ftl"/>