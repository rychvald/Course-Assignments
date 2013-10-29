import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.naming.NamingException;


public class LDAP {

	static String host = "98.211.110.141" , cn = "admin" , dc = "security" , dc2 = "edu";
	
	public static void main(String[] args) throws NamingException {
		String provider=null , login=null , password=null;
		if(args.length == 0) {
			System.out.println("Please provide an argument: \nLDAP server-address [DN(login),password]");
			System.exit(0);
		} else if (args.length == 1) {
			provider = args[0];
			login = askLogin();
			password= askPass();
		} else if (args.length == 2) {
			provider = args[0];
			login = args[1];
			password= askPass();
		} else {
			provider = args[0];
			login = args[1];
			password= args[2];
		}
		Helper helper = new Helper(provider,login,password);
		String input = null;
		while(!((input = prompt()).equals("exit"))) {
			String[] string = input.split(" ",2);
			int length = string.length;
			if (length == 0) {
				continue;
			} else if (string[0].equals("ls") && (length == 1)) {
				helper.list();
			} else if (string[0].equals("ls") && (length == 2)) {
				helper.list(string[1]);
			} else if (string[0].equals("cd") && (length == 2)) {
				helper.changeContext(string[1]);
			} else if (string[0].equals("add")) {
				add(helper);
			} else if (string[0].equals("rm") && (length == 2)) {
				helper.remove(string[1]);
			} else {
				System.out.println("Wrong argument!");
				System.out.println("Use: [ list[ subcontext] | remove thisentry | add thisentry]");
			}
		}
		helper.dirContext.close();
		System.out.println("Bye bye!");
	}
	
	static String askLogin() {
		return ask("Please enter the login user: ");
	}
	
	static String askPass() {
		return ask("Please enter the password: ");
	}
	
	static String prompt() {
		return ask(" > ");
	}
	
	static String ask(String question) {
		String pw = null;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.print(question);
			try {
				pw = console.readLine();
			} catch (IOException e) {
				System.out.println("Problem reading line!");
				e.printStackTrace();
			}
		} while (pw == null);
		
		return pw;
	}
	
	static void add(Helper helper) {
		String[] strings = new String[6];
		strings[0] = ask("Name of the new User: ");
		strings[1] = ask("Surname of the new User: ");
		strings[2] = ask("Password of the new User (optional): ");
		strings[3] = ask("Telephone of the new User (optional): ");
		strings[4] = ask("See also (optional): ");
		strings[5] = ask("Description (optional): ");
		helper.add(strings);
	}

}
