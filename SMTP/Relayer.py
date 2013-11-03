#Class Relayer.py
import smtpd
import smtplib

class Relayer(smtpd.SMTPServer):
	def process_message(self , peer , mailfrom , rcpttos, data):
		print 'Receiving message from:', peer
		print 'Message addressed from:', mailfrom
		print 'Message addressed to  :', rcpttos
		print 'Message length        :', len(data)
		print 'Message content       :', data
		self.send_message(self , peer , mailfrom , recipients , data)
		return
	
	def send_message(self , peer , mailfrom , recipients , data):
		self.smtplib.SMTP('gmail.com' , )
		try:
			server.sendmail()
		finally:
			server.quit()
		return		
