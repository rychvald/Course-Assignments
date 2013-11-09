#Python
import sys
import asyncore
from Relayer import *

print "Hello"

listener = Relayer(('127.0.0.1' , 10000) , None)
listener.add_virusfilter()

asyncore.loop()
