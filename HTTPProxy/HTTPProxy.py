#!/usr/bin/env python
import os
import urllib
import urlparse
import SimpleHTTPServer

class HTTPProxy(SimpleHTTPServer.SimpleHTTPRequestHandler):

	def do_GET(self):
		print self.path
		self.copyfile(urllib.urlopen(self.path), self.wfile)
		self.blockedurl()

	def blockedurl(self):
		print "Returning blocked URL"
		retVal = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\nContent blocked by proxy\r\n"
		self.wfile.write(retVal)

	def blocked_path(self):
		return

	def load_blacklist(self , blacklist):
		self.blacklist = blacklist
		print self.blacklist
		return
