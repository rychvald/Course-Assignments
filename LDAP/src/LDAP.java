import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


public class LDAP {

	static String host = "98.211.110.141" , cn = "admin" , dc = "security" , dc2 = "edu";
	
	public static void main(String[] args) throws NamingException {
		Hashtable server = new Hashtable(); 
		server.put("cn", cn);
		server.put("dc", dc);
		server.put("dc", dc2);

		InitialLdapContext lctx = new InitialLdapContext();
	}

}
