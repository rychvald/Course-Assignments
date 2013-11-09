#Python
import sys
import asyncore
from Relayer import *

def getfilters(filterfile):
	with open(filterfile) as f:
		filters = f.readlines()
	newfilters=[]
	for dropfilter in filters:
		newfilters.append(dropfilter.replace('\n' , ''))
	return newfilters

print "Hello"

if len(sys.argv) < 3:
	print "Missing argument! Provide an exlude file."
	sys.exit(0)

listener = Relayer(('127.0.0.1' , 10000) , None)

listener.add_textfilter(getfilters(sys.argv[1]))

listener.add_dropfilter(getfilters(sys.argv[2]))


asyncore.loop()
