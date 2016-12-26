<#assign ctx="${(rca.contextPath)!''}">

<tr id="article-${article.id}">
    <td>
        <a href="${ctx}/article/${article.id}">${article.title}</a>
    </td>
    <td>${article.tags}</td>
    <td><#include "delete.ftl"></td>
    <td><@c.relative_date datetime=article.createdTime/></td>
    <td>
        <div class="btn-group">
            <a class="btn btn-xs btn-inverse" href="${ctx}/article/${article.id}">详情</a>

            <button data-toggle="dropdown" class="btn btn-xs btn-inverse dropdown-toggle">
                <span class="ace-icon fa fa-caret-down icon-only"></span>
            </button>

            <ul class="dropdown-menu dropdown-menu-right dropdown-inverse">
                <li>
                    <a href="${ctx}/dashboard/user/article/${article.id}/edit?p=${p!'1'}">编辑</a>
                </li>
            </ul>
        </div>
    </td>
</tr>