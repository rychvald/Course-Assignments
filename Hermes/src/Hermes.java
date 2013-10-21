
public class Hermes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.out.println("Please provide an argument: [server|client]");
			System.exit(0);
		} else if (args[0].equals("both")) {
			Server server = new Server();
			Client client = new Client();
			System.out.println("Started server and client");
		} else if (args[0].equals("server")) {
			Server server = new Server();
			System.out.println("Started server");
		} else if (args[0].equals("client")) {
			Client client = new Client();
			System.out.println("Started client");
		} else {
			System.out.println("Wrong argument!");
		}
	}

}
