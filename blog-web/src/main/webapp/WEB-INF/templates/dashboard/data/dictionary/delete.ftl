<#if dictionary.isDeleted == 1>
<a href="javascript:" data-role="dictionary-delete" title="恢复字典"
   data-url="${ctx}/dashboard/data/dictionary/${dictionary.code}/undelete">
    <span class="label label-danger arrowed-in">已删除</span>
</a>
<#else>
<a href="javascript:" data-role="dictionary-delete" title="删除字典"
   data-url="${ctx}/dashboard/data/dictionary/${dictionary.code}/delete">
    <span class="label label-success arrowed-in">未删除</span>
</a>
</#if>