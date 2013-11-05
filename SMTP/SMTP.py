#Python
import sys
import asyncore
from Relayer import *
print "Hello"

listener = Relayer(('127.0.0.1' , 1025) , None)

filterfile = sys.argv[0]
f = open(filterfile)
filters = []
for line in f:
	filters.append(line)
listener.add_filter(filters)
	

asyncore.loop()

