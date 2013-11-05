#Class Relayer.py
import smtpd
import smtplib

class Relayer(smtpd.SMTPServer):
	def process_message(self , peer , mailfrom , recipients, data):
		print 'Receiving message from:', peer
		print 'Message addressed from:', mailfrom
		print 'Message addressed to  :', recipients
		print 'Message length        :', len(data)
		print 'Message content       :', data
		newData = self.filter_message(data)
		self.send_message(mailfrom , recipients , newData)
		return
	
	def send_message(self , mailfrom , recipients , data):
		server = smtplib.SMTP('mail.campus.unibe.ch' , 587)
		server.starttls()
		server.login("ms07w881" , "n4schl3daN0u")
		try:
			server.sendmail(mailfrom , recipients , data)
		finally:
			server.quit()
		return	

	def add_filter(self , filters):
		self.filters = filters
		return

	def filter_message(self , message):
		
		return message
