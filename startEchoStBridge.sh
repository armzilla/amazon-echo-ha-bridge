#
# Replace all values with {} below rmoving the curly braces
#
export LOCAL_IP={Enter this servers local ip}

export UPNP_PORT=50000
export SERVER_PORT=8080
export SMART_APP_CLIENTID={enter smart app client id}
export SMART_APP_SECRET={enter smar app secret}
# Note in brower enter "what is my ip" to find this value
export ROUTER_EXTERNAL_IP={enter external ip of router}

# Note: You need to port forward "server.port" to your local ip "upnp.config.address"
#       on your router with external ip "application.smartthings.app.externalIP"
#
java -jar amazon-echo-bridge-smart-things-0.1.2.1.jar --upnp.response.port=${UPNP_PORT} --server.port=${SERVER_PORT} --upnp.config.address=${LOCAL_IP} --application.smartthings.app.clientId=${SMART_APP_CLIENTID} --application.smartthings.app.clientSecret=${SMART_APP_SECRET} --application.smartthings.app.externalIP=${ROUTER_EXTERNAL_IP}
