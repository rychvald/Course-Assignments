
public class Hermes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args[0].equals("server")) {
			Server server = new Server();
		} else if (args[0].equals("client")) {
			Client client = new Client();
		}
	}

}
