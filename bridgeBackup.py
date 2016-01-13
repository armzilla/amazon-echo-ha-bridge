'''
Echo Echo Bridge Backup/Restore: Script to backup or restore Echo Bridge configuration
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
parser.add_argument('mode', type=str,
                   help='(R)estore or (B)ackup')
parser.add_argument('file', type=str,
                   help='File to restore from or backup to')

args = parser.parse_args()
mode = args.mode.lower()


if (mode == 'b') or (mode == 'backup'):	
	print ('Backing up Amazon Echo Bridge configuration from: ',args.echoBridgeIP,':',args.echoBridgePort,sep="")
	outputDict = csv.DictWriter(open(args.file, 'w'),fieldnames=['name','deviceType','onUrl','offUrl','contentType','contentBody','httpVerb'])
	outputDict.writeheader()
	r = requests.get('http://' + args.echoBridgeIP + ':' + args.echoBridgePort + '/api/devices')
	for entry in r.json()[0]['content']:
		tempDict = {'name':entry['name'],
					'deviceType':entry['deviceType'],
					'onUrl':entry['onUrl'],
					'offUrl':entry['offUrl'],
					'contentType':entry['contentType'],
					'contentBody':entry['contentBody'],
					'httpVerb':entry['httpVerb']
				   }
		outputDict.writerow(tempDict)
		print ('     Backed up item:',entry['name'])
		
if (mode == 'r') or (mode == 'restore'):
	print ('Restoring Amazon Echo Bridge configuration to: ',args.echoBridgeIP,':',args.echoBridgePort,sep="")
	inputDict = csv.DictReader(open (args.file))
	if inputDict.fieldnames != ['name','deviceType','onUrl','offUrl','contentType','contentBody','httpVerb']:
		print (args.input, 'input file formatted incorrectly')
		print ('    First line of input file should be: "name,deviceType,onUrl,offUrl,contentType,contentBody,httpVerb"' )
		print ('    Each line after should contain the Echo item name followed by a comma then the OpenHAB switch to be controlled')
		print ('    Aborting restore')
		sys.exit()
	for entry in inputDict:
		if not entry['contentType']:
			contentType = None
		else:
			contentType=entry['contentType']
		if not entry['contentBody']:
			contentBody = None
		else:
			contentBody=entry['contentBody']
		if not entry['httpVerb']:
			httpVerb = None
		else:
			httpVerb = entry['httpVerb']
							 
		values = {'name':entry['name'],
				  'deviceType':entry['deviceType'],
				  'onUrl':entry['onUrl'],
				  'offUrl':entry['offUrl'],
				  'contentType':contentType,
				  'contentBody':contentBody,
				  'httpVerb':httpVerb
				 }
				 
		r = requests.post('http://' + args.echoBridgeIP + ':' + args.echoBridgePort + '/api/devices', data=json.dumps(values), headers = {'Content-Type':'application/json'})
		if r.status_code == 201:
			print ('     Succesfully restored item:',entry['name'])
		else:
			print ('     Failed to restore item:',entry['name'])	