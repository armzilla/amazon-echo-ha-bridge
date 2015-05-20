upnp.response.port=50000
upnp.config.address={Enter your local ip here}
server.port=8080
application.smartthings.app.clientId={Enter your smart things app client id here}
application.smartthings.app.clientSecret={Enter your smart things app client secret here}
application.smartthings.app.externalIP={enter your router external ip here}

# Note: You need to port forward "server.port" to your local ip "upnp.config.address"
#       on your router with external ip "application.smartthings.app.externalIP"
#
java -jar target/amazon-echo-bridge-0.1.2.jar --upnp.response.port=${upnp.response.port} --server.port=${server.port} --upnp.config.address=${upnp.config.address} --application.smartthings.app.clientId={application.smartthings.app.clientId} --application.smartthings.app.clientSecret=${application.smartthings.app.clientSecret} --application.smartthings.app.externalIP=${application.smartthings.app.externalIP}
