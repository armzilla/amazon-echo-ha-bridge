![codeship status](https://codeship.com/projects/998e16f0-ca03-0132-6689-76c03995407a/status?branch=master)
# amazon-echo-ha-bridge-smart-things

This is a fork of armzilla amazon-echo-ha-bridge modifed for integration with Smart Things

To get things started do the following.

1) On unix edit startEchoStBridge.sh with your configration settings.
NOTE: Sorry, On Windows you are on your own :) you can use the last line in startEchoStBridge.sh for
      the java command replacing the config values in the command. 
YOU MUST CONFIGURE 
export SMART_APP_CLIENTID={enter smart app client id}
export SMART_APP_SECRET={enter smar app secret}
export ROUTER_EXTERNAL_IP={enter external ip of router}
OPTIONALLY you can configure ports used if you have other processes using these ports or if port 8080 is already 
           port forwarded on your router
export UPNP_PORT=50000
export SERVER_PORT=8080

NOTE(Port Forwarding Required):
For the smart app OAuth to work you need to port forward from your router to the ip of the machine you are running this server.
For example if server.port is set to 8080 and your local ip is set to 192.168.1.150
then you need to configure port forwarding from your router port 8080 to 192.168.1.150 port 8080

2) Start your server by running startEchoBridge.sh
	The process will start in the background
	The pid of the process can be found in logs/echoBridge.pid
	The logs for the process can be found in logs/echoBridge.log

	The folder you run the shell from will then contain your database in a folder called "data" if you
	have issues and want to start over you can safely remove this folder and everything below to start from scratch.
	If you do this after syncing with echo you should tell echo to forget at http://echo.amazon.com/#settings/connected-home

3) To Install Smart App do the following
	A) Login to IDE https://graph.api.smartthings.com
	B) Select "My Smart Apps"
	C) Select "+New Smart App"
	D) Fill in Data
    		Name: Echo Smarthings App
    		Author: Ronald Gouldner
    		Description: Echo Bridge App
	E) Click "Enable OAuth in Smart App" and enter data (Optional, diplays when you use OAuth Page)
   		Enter "OAuth Client Display Name: "Echo Smart Things Hue Emulator"
   		OAuth Client Display Link: "Echo Smart Things"
   		(Note: Copy OAuth Client Id and OAuth Client Secret into application.properties)
	F) Click Create
	G) Copy/Paste code from SmartApp/EchoSmartThingsEndpoint.groovy into the ide replacing all the existing code for this app
	H) Click Save
	I) Click Publish
	J) The smart app will be on your phone to be installed or it will install when you configure it from the server.

4) Your server should now be ready.  Visit http:{your local ip}:{your port}  example http://192.168.1.150:8080
    you should see a screen which has three options
		1) Display Configured Device 
			will open in new window and display ugly json data 
			I want to make this nicer
		2) Import Authorized Devices from Smart App
			Will Launch you to SmartThings page to authorize switches you want to control
		3) Clear All Devices (Note: Perform Echo FORGET after doing this)
			This allows you to clear your server devices and re-import

TODO:
0) Add security
1) Add ability to rename devices.  currently the name matchins the Preferences Name in the Smart Things App
2) Nicer GUI
3) Support Multiple ST Hubs (When I have more then one so I can test :)

Know Issue:
	If you select no authorized swtiches and click "Authorize" your emulator will clear all values correctly.
	However for some reason when you return to authorize devices the smart app in the browser will display a message
	"The page at https//graph.api.smartthings.com says: Please select at least one device to authorize"
	Even though you are selectin devices.

Workaround options:
	1) Open the Smart App on your phone, select devices and click done.  Now return the emulator page and authorize and
   	the devices selected in the phone app will display and authorize will work now.
	2) Delete the smart app and re-install (harder but list in case 1 doesn't work) If you do this rememnber to reconfigure
   	your OAuth values in the application.properties

To rename a device you need to
	1) rename the device in your smart app (Dashboard->device->preference->set your name
	2) unauthorize the device
	3) re-authorize the device
	sync with echo (you may want to forget so old name doesn't stay in echo)
