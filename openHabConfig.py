'''
EchOHBridge Configurator: Script to automate configuration of Echo Bridge to interface with OpenHAB
Python Version: >= 3.0
'''
import argparse
import requests
import json
import csv
import sys


parser = argparse.ArgumentParser(description='EchOHBridge Configurator')
parser.add_argument('echoBridgeIP', type=str,
                   help='Echo HA Bridge IP address')
parser.add_argument('echoBridgePort', type=str,
                   help='Echo HA Bridge port')
parser.add_argument('openHabIP', type=str,
                   help='OpenHAB IP address')
parser.add_argument('openHabPort', type=str,
                   help='OpenHAB Port')
parser.add_argument('input', type=str,
                   help='CSV file containing echo name (name) and OpenHAB name (item)')

args = parser.parse_args()

print ('Configuring Amazon Echo Bridge at: ',args.echoBridgeIP,':',args.echoBridgePort,sep="")
inputDict = csv.DictReader(open (args.input))
if inputDict.fieldnames != ['name', 'item']:
	print (args.input, 'input file formatted incorrectly')
	print ('    First line of input file should be: "name,item"' )
	print ('    Each line after should contain the Echo item name followed by a comma then the OpenHAB switch to be controlled')
	sys.exit()

url = 'http://' + args.echoBridgeIP + ':' + args.echoBridgePort + '/api/devices'
headers = {'Content-Type':'application/json'}

for entry in inputDict:
	values = {'name' : entry['name'],
	'deviceType' : 'switch',
	'onUrl' : 'http://' + args.openHabIP + ':' + args.openHabPort + '/CMD?' + entry['item'] + '=ON',
	'offUrl' : 'http://' + args.openHabIP + ':' + args.openHabPort + '/CMD?' + entry['item'] + '=OFF' }
	r = requests.post(url, data=json.dumps(values), headers=headers)
	if r.status_code == 201:
		print('Created item: ', entry['name'])
	else:
		print('Item creation failed: ', entry['name'])