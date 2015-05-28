sudo cp echoStBridgeService /etc/init.d
sudo chmod 755 /etc/init.d/echoStBridgeService 
sudo update-rc.d echoStBridgeService defaults

echo "installed service echoBridgeService"
echo "which will auto start on reboot"
/etc/init.d/echoStBridgeService help
echo "you can start service now with /etc/init.d/startStBridgeService start"
