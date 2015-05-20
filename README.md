![codeship status](https://codeship.com/projects/998e16f0-ca03-0132-6689-76c03995407a/status?branch=master)


# amazon-echo-ha-bridge-smart-things
This is a fork of armzilla amazon-echo-ha-bridge modifed for integration with Smart Things

configure values in curly braces { } found in src/main/resources/application.properties
then start with 
startEchoStBridge.sh

The folder you run the shell from will then contain your database in a folder called "data" if you have issues and want to start over you can safely remove this folder and everything below to start from scratch

For the smart app OAuth to work you need to port forward from your router to the ip of the machine you are running this server.
You need forward the port configured in application.properties  "server.port"
You need to forward to the internal IP configured in "upnp.config.address"
Your routers external ip should be configured in "application.smartthings.app.externalIP"

You also need to configure your Smart App Oath Values
application.smartthings.app.clientId={config your smart app cliendid}
application.smartthings.app.clientSecret={config your smart app cliendSecret}

To Install Smart App do the following
1) Login to IDE https://graph.api.smartthings.com
2) Select "My Smart Apps"
3) Select "+New Smart App"
4) Fill in Data
    Name: Echo Smarthings App
    Author: Ronald Gouldner
    Description: Anything paste will replace anyway
5) Click "Enable OAuth in Smart App" and enter data (Optional, diplays when you use OAuth Page)
   Enter "OAuth Client Display Name: "Echo Smart Things Hue Emulator"
   OAuth Client Display Link: "Echo Smart Things"
6) Click Create
7) Copy/Paste code from SmartApp/EchoSmartThingsEndpoint.groovy into the ide replacing all the existing code for this app
8) Click Save
9) Click Publish
The Smart App should now be listed on your phone.  You can try opening it and selecting switches if you like.

NOTE: This version deletes the device list and recreates each time you authenticate/add/remove devices.
Therefore you must open the echo web portal and clear your current devices after an update
settings->connected home->FORGET

Then reimport your data
"Alexa discover devices"

If you fail to forget you will see duplicate devices, just forget and reimport to fix this.

I plan to work on a version which keeps unchanged devices, removes devices removed and adds devices added.
Then you will only need to discover, not FORGET first.
