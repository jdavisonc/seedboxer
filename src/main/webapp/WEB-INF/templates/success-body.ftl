Transfer to server (${.vars["proEasy_ftp_url"]}) completed.

Files: 
<#list .vars["proEasy_files"] as item>
	* ${item}
</#list>

Start Date: ${.vars["proEasy_start"]?datetime}
End Date: ${.vars["proEasy_end"]?datetime}

--
This is a automatic notification send by proEasy