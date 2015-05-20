<!DOCTYPE html>

<html lang="en">

<body>
<#if clientId == "clientIdMissing">
    Client Id Not Configured.  Click here to configure.....ToDo
<#else>
    <#if code == "" && accessToken == "">
        <a href="https://graph.api.smartthings.com/oauth/authorize?response_type=code&client_id=${clientId}&redirect_uri=${redirectURL}&scope=app">Click Here to Attach To Smart Things ESTHB Smart App</a>
    <#else>
        <#if accessToken == "">
            Code = ${code}
            <a href="https://graph.api.smartthings.com/oauth/token?grant_type=authorization_code&client_id=${clientId}&redirect_uri=${redirectURL}&client_secret=${clientSecret}&code=${code}&scope=app">Step Two Click Here To Complete Authorization</a>
        <#else>
                Done
        </#if>
    </#if>
</#if>

</body>

</html>