Transfer to server (${.vars["proeasy_ftp_url"]}) completed.

Files: 
<#list .vars["proeasy_files"] as item>
	* ${item}
</#list>

Start Date: ${.vars["proeasy_start"]?datetime}
End Date: ${.vars["proeasy_end"]?datetime}

--
This is a automatic notification send by proeasy