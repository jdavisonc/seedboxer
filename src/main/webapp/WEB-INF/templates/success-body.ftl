Upload completed successfully to @homeServer (${.vars["proEasy.ftp.url"]})

Files: <#list .vars["proEasy.files"] as item>${item}</#list>

Start Date: ${.vars["proEasy.start"]?datetime}
End Date: ${.vars["proEasy.end"]?datetime}

--
This is a automatic notification send by proEasy