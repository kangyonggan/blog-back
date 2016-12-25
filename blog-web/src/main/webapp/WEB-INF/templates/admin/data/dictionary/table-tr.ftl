<#assign ctx="${(rca.contextPath)!''}">

<tr id="dictionary-${dictionary.id}">
    <td>${dictionary.code}</td>
    <td><#include "type.ftl"></td>
    <td>${dictionary.name}</td>
    <td><#include "delete.ftl"></td>
    <td><@c.relative_date datetime=dictionary.createdTime/></td>
    <td>
        <div class="btn-group">
            <a data-toggle="modal" class="btn btn-xs btn-inverse"
               href="${ctx}/admin/data/dictionary/${dictionary.id}/edit"
               data-target="#myModal">编辑</a>
        </div>
    </td>
</tr>