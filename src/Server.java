
import java.util.*;
import java.text.SimpleDateFormat; 
import java.net.*;
import java.io.*;

public class Server {
	
	// For each connection, you need to have unique ID
	private static int ID;
	
	// ArrayList to keep all clients
	private ArrayList <Client> clientList;
	
	// Display the time of the message
	private SimpleDateFormat time;
	
	// Port connection
	private int port;
	
	// Check to see if the server is still running
	private boolean serverIsAlive;
	
	// Notification
	private String notification = "****"; 

	public Server (int port) {
		this.port =port;
		time = new SimpleDateFormat("HH:MM:ss");
		clientList = new ArrayList <Client>();
		
	}
	
	public void start () {
		serverIsAlive = true;
		
		try {
			// Establish a socket for the server connection on the request port
			ServerSocket serverSocket = new ServerSocket(port);
			
			while (serverIsAlive) {
				System.out.println("Server waiting for Clients on port " + port + "."); 
				Socket socket = serverSocket.accept(); //if requested from client, the connection is accepted
				
				// Check if the server got disconnected from trying to process the client's request
				if (!serverIsAlive) {
					System.out.println("Oh no! Server got disconnected. Exiting the system.");
					break;
				}
				
				Client client = new Client(socket);
	            clientList.add(client);
	            
	            client.run(); // execute the runnable thread's code
			}
		}
		catch (IOException e) {
			String msg = time.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			System.out.println(msg);
		}
		finally {
			
		}
	}
	
	
}
