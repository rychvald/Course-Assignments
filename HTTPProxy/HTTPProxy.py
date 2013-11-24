#!/usr/bin/env python
import os
import urllib
import urlparse
import fnmatch
import SimpleHTTPServer

class HTTPProxy(SimpleHTTPServer.SimpleHTTPRequestHandler):

	def getfilters(self , filterfile):
		with open(filterfile) as f:
			filters = f.readlines()
		newfilters=[]
		for dropfilter in filters:
			newfilters.append(dropfilter.replace('\n' , ''))
		return newfilters

	def do_GET(self):
		print self.path
		if self.blocked_path(self.getfilters("blacklist.txt")):
			self.blockedurl()
		else:
			self.copyfile(urllib.urlopen(self.path), self.wfile)

	def blockedurl(self):
		print "Returning blocked URL"
		retVal = "HTTP/1.1 403 Forbidden\r\nContent-Type: text/plain; charset=UTF-8\r\n\r\nContent blocked by proxy\r\n"
		self.wfile.write(retVal)

	def blocked_path(self , blacklist):
		servername = urlparse.urlparse(self.path)
		servername = servername.netloc
		print servername
		retVal = 0
		for blackstring in blacklist :
			print blackstring
			if fnmatch.fnmatch(servername , blackstring): retVal = 1
		return retVal
