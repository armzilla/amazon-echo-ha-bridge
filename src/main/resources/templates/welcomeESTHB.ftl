<!DOCTYPE html>

<html lang="en">

<body>
<h1>${message}</h1>


<br>
<br>
<a href="/api/devices" target="_blank">Display Configured Devices</a>
<br>
<br>
<a href="/ui/importAuthorizedDevices" target="_blank">Import Authorized Devices from Smart App</a>
<br>
<br>
<a href="/ui/clearRepository" target="_blank">Clear All Devices (Note: Perform Echo FORGET after doing this)</a>
<br>
Echo Forget can be done <a href="http://echo.amazon.com/#settings/connected-home" target="_blank">HERE</a>
<br>
<br>
Date: ${time?date}  Time: ${time?time}
<br>
</body>

</html>