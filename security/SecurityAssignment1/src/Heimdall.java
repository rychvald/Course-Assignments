import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;


public class Heimdall {

	private static String usage = "Usage: Heimdall (index|analyse) Directory [Exceptions]";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mode = null,directory = null;
		File root = null;
		HashSet<File> exceptions = new HashSet<File>();
		
		try {
			mode = args[0];
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Wrong arguments!");
			System.out.println(usage);
		}
		try {
			directory = args[1];
			root = new File(directory);
			if (!root.exists()) {
				System.out.println("Invalid directory!");
				System.out.println(usage);
				System.exit(0);
			}
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Wrong arguments!");
			System.out.println(usage);
			System.exit(0);
		}
		try {
			exceptions = exceptions(args[2]);
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("No Exceptions");
		}
		String password = enterpassword();
		
		Coder encryptor = new Coder(exceptions, root, password);
		if ( mode.equals("index")) {
			System.out.println("Indexing...");
			encryptor.index();
		} else if (mode.equals("analyse")) {
			System.out.println("Analysing...");
			encryptor.analyse();
		} else {
			System.out.println("Wrong set of arguments!");
			System.out.println(usage);
			System.exit(0);
		}
		System.out.println();
		System.out.println("finished!");
	}
	
	private static HashSet<File> exceptions(String exceptions) {
		HashSet<File> returnSet = new HashSet<File>();
		
		File file = new File(exceptions);
		if (!file.exists()) {
			System.out.println("Invalid exceptions file!");
			System.out.println(usage);
			System.exit(0);
		}
				
		try {
			BufferedReader in = new BufferedReader(new FileReader(exceptions));
			String line;
			while ((line = in.readLine()) != null) {
				returnSet.add(new File(line));
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnSet;
	}
	
	private static String enterpassword() {
		String pw = null;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		do {
			System.out.print("Please enter the password: ");
			try {
				pw = console.readLine();
			} catch (IOException e) {
				System.out.println("Problem reading line!");
				e.printStackTrace();
			}
		} while (pw == null || pw.equals(""));
		
		return pw;
	}

}
