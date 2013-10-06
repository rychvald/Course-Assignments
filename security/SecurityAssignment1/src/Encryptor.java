import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;


public class Encryptor {

	Mac hmac;
	
	public Encryptor(String exceptions, String secretKey) throws NoSuchAlgorithmException {
			this.hmac = Mac.getInstance("HmacMD5");
		
	}
	
	public void index() {
		
	}
	
	public void returnKey() {
		
	}
	
}
