import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Hypercube3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Welcome to the HyperCube programme");
		Integer[] source = ask("Please enter the source node: ");
		Integer[] dest = ask("Please enter the destination node: ");
		
		Node sourceNode = new Node(source[0] , source[1] , source [2]);
		System.out.println(sourceNode);
		sourceNode.sendPacketTo(dest[0], dest[1], dest[2]);
	}
	
	static Integer[] ask(String question) {
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
		
		Integer thirddigit = Integer.valueOf(pw.substring(0, 1));
		Integer seconddigit = Integer.valueOf(pw.substring(1, 2));
		Integer firstdigit = Integer.valueOf(pw.substring(2, 3));
		Integer[] retVal = {thirddigit , seconddigit , firstdigit};
		
		return retVal;
	}

}
