Transfer to server (${.vars["TransferUrl"]}) fail.

Files:
	* ${.vars["FileName"]}

The error was: 

${Cause!}

<#if Cause??>
Caused by: 
	${Cause.cause!} 
</#if>

--
This is a automatic notification send by SeedBoxer