#Class Relayer.py
import smtpd
import smtplib
import getpass
import pyclamd
import os

class Relayer(smtpd.SMTPServer):
	def process_message(self , peer , mailfrom , recipients, data):
		print 'Receiving message from:', peer
		print 'Message addressed from:', mailfrom
		print 'Message addressed to  :', recipients
		print 'Message length        :', len(data)
		print 'Message content       :', data
		newData = self.virusfilter(data , mailfrom , recipients)
		self.send_message(mailfrom , recipients , newData)
		return
	
	def send_message(self , mailfrom , recipients , data):
		server = smtplib.SMTP('mail.campus.unibe.ch' , 587)
		server.starttls()
		password=getpass.getpass("enter a password: ")
		server.login("ms07w881" , password)
		try:
			server.sendmail(mailfrom , recipients , data)
		finally:
			server.quit()
		return	

	def add_virusfilter(self):
		self.cd = pyclamd.ClamdUnixSocket()
		self.cd.reload()
		return

	def virusfilter(self  , message , mailfrom , recipients):
		print self.cd.version()
		print message
		msg = self.cd.scan_stream(message)
		print msg
		if msg is None: return message
		text = message.split("\n\n")
		print message
		message = text[0]+"\n\nVirus found in message! Content deleted. Virus found: "+msg.get('stream')[1]
		return message
