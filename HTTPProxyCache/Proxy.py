#!/usr/bin/env python
import sys
import SocketServer
from HTTPProxy import *

print "Starting Proxy Server..."


myProxy = SocketServer.ForkingTCPServer(('localhost', 65000), HTTPProxy)
print "Proxy Server started"
myProxy.serve_forever()

