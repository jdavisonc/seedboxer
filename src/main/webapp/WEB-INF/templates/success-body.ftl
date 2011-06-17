Transfer to server (${.vars["proEasy_ftp_url"]}) completed.

Files: <#list .vars["proEasy_files"] as item>${item}</#list>

<b>Start Date:</b> ${.vars["proEasy_start"]?datetime}
<b>End Date:</b> ${.vars["proEasy_end"]?datetime}

--
This is a automatic notification send by proEasy