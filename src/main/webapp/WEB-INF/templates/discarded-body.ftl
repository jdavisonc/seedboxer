Transfer to server (${.vars["proEasy_ftp_url"]}) fail.

Files: 
<#list .vars["proEasy_files"] as item>
	* ${item}
</#list>

The error was: 

${Cause} 
Caused by: ${Cause.cause}

--
This is a automatic notification send by proEasy