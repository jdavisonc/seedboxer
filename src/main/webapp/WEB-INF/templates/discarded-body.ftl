Transfer to server (${.vars["FtpUrl"]}) fail.

Files: 
<#list .vars["Files"] as item>
	* ${item}
</#list>

The error was: 

${Cause} 
Caused by: ${Cause.cause}

--
This is a automatic notification send by SeedBoxer