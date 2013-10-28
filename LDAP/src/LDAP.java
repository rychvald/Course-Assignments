import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;


public class LDAP {

	static String host = "98.211.110.141" , cn = "admin" , dc = "security" , dc2 = "edu";
	
	public static void main(String[] args) throws NamingException {
		Helper helper = new Helper();
		helper.list();
	}

}
