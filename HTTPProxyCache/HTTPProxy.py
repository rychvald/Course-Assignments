#!/usr/bin/env python
import os
import urllib
import urlparse
import fnmatch
import SimpleHTTPServer

class HTTPProxy(SimpleHTTPServer.SimpleHTTPRequestHandler):

	def do_GET(self):
		print self.path
		if self.blocked_path(self.getfilters("blacklist.txt")):
			self.blockedurl()
		else:
			self.copyfile(urllib.urlopen(self.path), self.wfile)

	def cache(self , path):
		print "caching" , path
		retVal = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\nContent blocked by proxy\r\n"
		self.wfile.write(retVal)

	def is_cached(self):
		servername = urlparse.urlparse(self.path)
		servername = servername.netloc
		print servername
		retVal = 0
		for blackstring in blacklist :
			print blackstring
			if fnmatch.fnmatch(servername , blackstring): retVal = 1
		return retVal
