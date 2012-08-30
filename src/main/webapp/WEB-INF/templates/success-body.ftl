Transfer to server (${.vars["FtpUrl"]}) completed.

Files: 
<#list .vars["Files"] as item>
	* ${item}
</#list>

Start Date: ${.vars["Start"]?datetime}
End Date: ${.vars["End"]?datetime}

--
This is a automatic notification send by proeasy