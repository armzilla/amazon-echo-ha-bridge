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

I will add instructions on how to install the Smart App with OAuth enabled and acquire these values