import java.io.File;


public class Heimdall {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String exceptions;
		try {
			exceptions = args[0];
		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("No Exceptions");
			exceptions = "";
		}
		File root = new File("/Users/mars/Desktop/bla");
		String password = "testpassword";
		
		Encryptor encryptor = new Encryptor(exceptions, root, password);
		encryptor.index();
		
		System.out.println("finished!");
	}

}
