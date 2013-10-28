import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;

import java.util.Hashtable;


public class Helper {

	DirContext dirContext;
	
	public Helper() {
		this("edu.security" , "ldap://198.211.110.141:389/dc=edu,dc=security,ou=students");
	}
	
	public Helper(String context , String server) {
		Hashtable<String , String> environment = new Hashtable<String , String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY , "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL , server);
		environment.put(Context.SECURITY_AUTHENTICATION , "simple");
		environment.put(Context.SECURITY_PRINCIPAL , "cn=admin,dc=security,dc=edu");
		environment.put(Context.SECURITY_CREDENTIALS , "security");
		try {
			this.dirContext = new InitialDirContext(environment);
		} catch (NamingException e) {
			System.out.println("Error: Could not create Context. Name not valid.");
			e.printStackTrace();
		}
		System.out.println("Successfully connected to LDAP Server :)");
	}
	
	public void list() {
		try {
			System.out.println(this.dirContext.getNameInNamespace());
			//NamingEnumeration<String> nameEnum = this.dirContext.list("");
			System.out.println(nameEnum.nextElement());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void add(String newEntry) {
		
	}
	
	public void remove(String entry) {
		
	}
	
}
