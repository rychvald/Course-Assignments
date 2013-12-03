#!/usr/bin/env python
import os
import urllib
import md5
import SimpleHTTPServer

class HTTPProxy(SimpleHTTPServer.SimpleHTTPRequestHandler):

	def do_GET(self):
		print self.path
		digest = './cachefiles/' + md5.new(self.path).hexdigest()
		print digest
		#self.checkheader(digest)
		try:
			with file(digest) as cachefile:
				print "file already exists, reading from cache..."
				self.wfile.write(cachefile.read())
		except IOError:
			self.cache(digest)
		print "finished"
		return

	def cache(self , digest):
		urlfile = urllib.urlopen(self.path) 
		self.copyfile(urlfile , self.wfile)
		header = urlfile.info().getrawheader("Cache-Control")
		print header
		if ((header is None) or ("no-cache" or "no-store" or "max-age=0") not in header):
			#cachefile = open(digest , 'w')
			#cachefile.close()
			#self.copyfile(urlfile , cachefile)
			print "writing content to cache file"
			urllib.urlretrieve(self.path , digest)
			#cachefile.close()
		else:
			print"site not cached: header prevents caching"
		return

	def checkheader(self , digest):
		header = self.headers.getrawheader("Cache-Control")
		print header
		if header is None:
			return
		if (("no-cache" or "no-store" or "max-age=0") in header):
			print "deleting cache file..."
			os.remove(digest)
		return
			
		
