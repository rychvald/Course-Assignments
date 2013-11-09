import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Hypercube {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to the HyperCube programme");
		Boolean[] source = ask("Please enter the source node: ");
		Boolean[] dest = ask("Please enter the destination node: ");
		
		Node sourceNode = new Node(source[0] , source[1] , source [2]);
		sourceNode.sendPacketTo(dest[0], dest[1], dest[2]);
	}
	
	static Boolean[] ask(String question) {
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
		
		boolean thirddigit = (pw.charAt(0)=='1') ? true : false;
		boolean seconddigit = (pw.charAt(1)=='1') ? true : false;
		boolean firstdigit = (pw.charAt(2)=='1') ? true : false;
		
		Boolean[] retVal = {thirddigit , seconddigit , firstdigit};
		
		return retVal;
	}

}
