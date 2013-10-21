import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.CharBuffer;


public class Client {
	
	int listenerPort = 8080, maxConnections = 1 , senderPort = 20000;
	String serverAddress = "localhost";
	ServerSocket listenerSocket = null;
	Socket senderSocket = null;
	boolean goOn = true;
	BufferedReader fromServer = null;
	PrintWriter toServer = null;
	
	public Client() {
		try {
			this.setUpSenderSocket();
			this.listenerSocket = new ServerSocket(listenerPort , maxConnections , InetAddress.getLocalHost());
			this.open();
		} catch (UnknownHostException e) {
			System.out.println("Unknown host when creating listenerSocket for Client");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException when creating listenerSocket for Client");
			e.printStackTrace();
		}
	}
	
	private void setUpSenderSocket() throws UnknownHostException, IOException {
		this.senderSocket = new Socket(this.serverAddress , this.senderPort);
		InputStream in = this.senderSocket.getInputStream();
		this.fromServer = new BufferedReader(new InputStreamReader(in));
		OutputStream out = this.senderSocket.getOutputStream();
		this.toServer = new PrintWriter(new OutputStreamWriter(out));
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

	public void close() {
		this.goOn = false;
		try {
			this.listenerSocket.close();
			this.toServer.close();
			this.fromServer.close();
			this.senderSocket.close();
		} catch (IOException e) {
			System.out.println("Failed to close listenerSocket for Client!");
			e.printStackTrace();
		}
	}
	
	//listens for inputs from the client ports
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
			this.request(lines);
		} catch (IOException e) {
			System.out.println("Failed listening to client!");
			e.printStackTrace();
		}
	}
	
	//responsible for forwarding the request to the server application
	private void request(String request) {
		this.toServer.print(request);
		this.toServer.flush();
	}
}
