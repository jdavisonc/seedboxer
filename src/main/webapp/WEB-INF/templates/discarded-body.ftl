Transfer to server (${.vars["FtpUrl"]}) fail.

Files: 
<#list .vars["Files"] as item>
	* ${item}
</#list>

The error was: 

${Cause!}

<#if Cause??>
Caused by: 
	${Cause.cause!} 
</#if>

--
This is a automatic notification send by SeedBoxer