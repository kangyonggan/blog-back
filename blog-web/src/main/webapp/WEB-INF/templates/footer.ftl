<div class="space-30"></div>
<div class="footer">
    <div class="footer-inner">
        <div class="footer-content">
			<span class="bigger-120">
				<span class="grey bolder"><@spring.message "app.name"/></span>
                © 2016
                <img src="${ctx}/static/app/images/ba.png">
            <em class="hidden-xs"><@spring.message "app.ba.no"/></em>
				Build By <@spring.message "app.author"/>
			</span>
        <@apps>
            <a href="${ftpUrl}/rss/blog.xml" target="_blank">
                <i class="ace-icon fa fa-rss-square orange bigger-150"></i>
            </a>
        </@apps>
        </div>
    </div>
</div>