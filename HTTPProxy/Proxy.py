#!/usr/bin/env python
import sys
import SocketServer
from HTTPProxy import *

def getfilters(filterfile):
	with open(filterfile) as f:
		filters = f.readlines()
	newfilters=[]
	for dropfilter in filters:
		newfilters.append(dropfilter.replace('\n' , ''))
	return newfilters

if len(sys.argv) < 2:
	print "Missing argument! Provide a blacklist file."
	sys.exit(0)

print "Starting Proxy Server..."

proxyhandler = HTTPProxy()
proxyhandler.load_blacklist(getfilters(sys.argv[1]))
myProxy = SocketServer.ForkingTCPServer(('localhost', 65000), proxyhandler)
print "Proxy Server started"
myProxy.serve_forever()
