import java.io.File;


public class Heimdall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mode,exceptions;
		
		mode = args[0];
		try {
			exceptions = args[1];
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("No Exceptions");
			exceptions = "";
		}
		File root = new File("/Users/mars/Desktop/bla");
		String password = "testpassword";
		
		Encryptor encryptor = new Encryptor(exceptions, root, password);
		if ( mode.equals("index")) {
			encryptor.index();
		} else if (mode.equals("analyze")) {
			encryptor.analyze();
		}
		
		System.out.println("finished!");
	}

}
