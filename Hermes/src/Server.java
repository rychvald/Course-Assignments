import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class Server {
	
	private int listenerPort = 20000 , maxConnections = 1 , senderPort;
	ServerSocket listenerSocket = null;
	Socket senderSocket = null;
	boolean goOn = true;
	BufferedReader fromServer = null;
	PrintWriter toServer = null;
	
	public Server() {
		try {
			this.listenerSocket = new ServerSocket(listenerPort , maxConnections);
			this.open();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host when creating listenerSocket for Client");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException when creating listenerSocket for Client");
			e.printStackTrace();
		}
	}
	
	private void open() {
		assert this.goOn;
		try {
			while (goOn) {
				Socket clientSocket = this.listenerSocket.accept();
				this.listenTo(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Failed to open listenerSocket for Client!");
			e.printStackTrace();
		}
	}
	
	private void listenTo (Socket client) {
		try {	
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			String lines = null , line = null;
			while((line = in.readLine()) != null) {
				if (line.length() == 0) {
					break;
				} else {
					lines = lines + "\n" + line;
				}
			}
			System.out.println(lines);
		} catch (IOException e) {
			System.out.println("Failed listening to client!");
			e.printStackTrace();
		}
	}
}
