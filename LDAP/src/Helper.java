import javax.naming.*;
import javax.naming.directory.*;

import java.util.Hashtable;


public class Helper {

	DirContext dirContext;
	
	public Helper() {
		this("ldap://198.211.110.141:389/ou=students,dc=security,dc=edu" , "cn=admin,dc=security,dc=edu" , "security");
	}
	
	public Helper(String provider , String login , String password) {
		provider = "ldap://" + provider;
		Hashtable<String , String> environment = new Hashtable<String , String>();
		environment.put(Context.INITIAL_CONTEXT_FACTORY , "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL , provider);
		environment.put(Context.SECURITY_AUTHENTICATION , "simple");
		environment.put(Context.SECURITY_PRINCIPAL , login);
		environment.put(Context.SECURITY_CREDENTIALS , password);
		try {
			this.dirContext = new InitialDirContext(environment);
		} catch (NamingException e) {
			System.out.println("Error: Could not create Context. Name not valid.");
			e.printStackTrace();
		}
		System.out.println("Successfully connected to LDAP Server " + provider);
	}
	
	public void list() {
		try {
			System.out.println("Listing contents of " + this.dirContext.getNameInNamespace());
			NamingEnumeration<NameClassPair> nameEnum = this.dirContext.list("");
			while(nameEnum.hasMore()) {
				System.out.println(nameEnum.next().getName());
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void list(String object) {
		NamingEnumeration<?> nameEnum;
		try {
			nameEnum = this.dirContext.getAttributes(object).getAll();
			System.out.println("Listing attributes of " + object + "," + this.dirContext.getNameInNamespace());
			while(nameEnum.hasMore()) {
				System.out.println(nameEnum.next().toString());
			}
		} catch (NamingException e) {
			System.out.println("Error: no such object!");
			//e.printStackTrace();
		}
	}
	
	public void changeContext(String object) {
		try {
			this.dirContext = (DirContext)this.dirContext.lookup(object);
			System.out.println("New context: " + this.dirContext.getNameInNamespace());
		} catch (NamingException e) {
			System.out.println("Error: no such object!");
			//e.printStackTrace();
		}
	}
	
	public void add(String[] strings) {
		if(strings[0].equals(""))
			System.out.println("Error: You need to provide a compund name!");
		if(strings[1].equals(""))
			System.out.println("Error: You need to provide a surname!");
		BasicAttributes attr = new BasicAttributes();
		attr.put("objectClass", "person");
		String[] attrIDs = {"cn" , "sn" , "userPassword" , "telephoneNumber" , "seeAlso" , "description"};
		String cn = attrIDs[0] + "=" + strings[0];
		for(int i=1 ; i<6 ; i++) {
			if(strings[i].equals(""))
				continue;
			else
				attr.put(attrIDs[i], strings[i]);
		}
		try {
			this.dirContext.bind(cn , null , attr);
		} catch (NamingException e) {
			System.out.println("Error: could not add entry!");
			e.printStackTrace();
		}
	}
	
	public void remove(String object) {
		try {
			this.dirContext.unbind(object);
		} catch (NamingException e) {
			System.out.println("Error: could not remove object " + object + "!");
			//e.printStackTrace();
		}
	}
	
}
