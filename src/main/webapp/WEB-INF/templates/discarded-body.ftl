Transfer to server (${.vars["proeasy_ftp_url"]}) fail.

Files: 
<#list .vars["proeasy_files"] as item>
	* ${item}
</#list>

The error was: 

${Cause} 
Caused by: ${Cause.cause}

--
This is a automatic notification send by proeasy