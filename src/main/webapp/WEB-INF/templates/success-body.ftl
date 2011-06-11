Transfer to server (${.vars["proEasy.ftp.url"]}) completed.

Files: <#list .vars["proEasy.files"] as item>${item}</#list>

<b>Start Date:</b> ${.vars["proEasy.start"]?datetime}
<b>End Date:</b> ${.vars["proEasy.end"]?datetime}

--
This is a automatic notification send by proEasy