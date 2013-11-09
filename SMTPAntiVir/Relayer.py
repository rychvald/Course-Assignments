#Class Relayer.py
import smtpd
import smtplib
import getpass
import pyclamd

class Relayer(smtpd.SMTPServer):
	def process_message(self , peer , mailfrom , recipients, data):
		print 'Receiving message from:', peer
		print 'Message addressed from:', mailfrom
		print 'Message addressed to  :', recipients
		print 'Message length        :', len(data)
		print 'Message content       :', data
		print self.dropfilter(data)
		#if self.dropfilter(data)!=-1: return
		newData = self.textfilter(data)
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

	def add_textfilter(self , filters):
		self.filters = filters
		return
	def add_dropfilter(self , filters):
		self.dropfilters = filters
		return

	def add_virusfilter(self):
		self.cd = pyclamd.ClamdUnixSocket()
		self.cd.reload()

	def textfilter(self , message):
		for textfilter in self.filters:
			message=message.replace(textfilter , '')
		return message

	def dropfilter(self , message):
		for dropfilter in self.dropfilters:
			retVal=message.find(dropfilter)
		return retVal

	def virusfilter(self  , message):
		return message
