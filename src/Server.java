
import java.util.*;
import java.text.SimpleDateFormat; 
import java.net.*;
import java.io.*;

public class Server {
	
	// For each connection, you need to have unique ID
	private static int ID;
	
	// ArrayList to keep all clients
	private static ArrayList <ClientThread> clientList;
	
	// Display the time of the message
	private static SimpleDateFormat time;
	
	// Port connection
	private int port;
	
	// Check to see if the server is still running
	private boolean serverIsAlive;
	
	// Notification
	private String notification = "****"; 

	public Server (int port) {
		this.port =port;
		time = new SimpleDateFormat("HH:MM");
		clientList = new ArrayList <ClientThread>();
		
	}
	
    // to stop the server
    protected void stop() {
    	serverIsAlive = false;
    	
        try {
            new Socket("localhost", port);
        }
        catch(Exception e) {
        }
    }
    
    public static int getID () {
    	return ID;
    }
    
    // Display an event to the console
    public static void display(String msg) {
        String newTime = time.format(new Date()) + " " + msg;
        System.out.println(newTime);
    }
    
    // Broadcast a message to all Clients
    public static synchronized boolean broadcast(String message) {
        // add timestamp to the message
        String newtime = time.format(new Date());

        // to check if message is private i.e. client to client message
        String[] privateMessage = message.split(" ",3);

        boolean isPrivate = false;
        if(privateMessage[1].charAt(0) == '@')
            isPrivate=true;


        // if private message, send message to mentioned username only
        if (isPrivate == true)
        {
            String toCheck = privateMessage[1].substring(1, privateMessage[1].length());

            message = privateMessage[0]+ privateMessage[2];
            String messageLf = time + " " + message + "\n";
            boolean found = false;
            
            // we loop in reverse order to find the mentioned username
            for(int y = clientList.size(); --y >= 0;)
            {
                ClientThread clientThread1 = clientList.get(y);
                String check = clientThread1.getUsername();
                
                if(check.equals(toCheck))
                {
                    // try to write to the Client if it fails remove it from the list
                    if(!clientThread1.writeMessage(messageLf)) {
                    	clientList.remove(y);
                        display("Disconnected Client " + clientThread1.username + " removed from list.");
                    }
                    
                    // username found and delivered the message
                    found = true;
                    break;
                }

            }
            
            // mentioned user not found, return false
            if(found != true)
                return false;
        }
        else // if message is a broadcast message
        {
            String messageLf = time + " " + message + "\n";
            // display message
            System.out.print(messageLf);

            // we loop in reverse order in case we would have to remove a Client
            // because it has disconnected
            for(int i = clientList.size(); --i >= 0;) {
                ClientThread clientThread2 = clientList.get(i);
                
                // try to write to the Client if it fails remove it from the list
                if(!clientThread2.writeMessage(messageLf)) {
                    clientList.remove(i);
                    display("Disconnected Client " + clientThread2.username + " is removed from list.");
                }
            }
        }
        return true;

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
				
				ClientThread client = new ClientThread(socket);
	            clientList.add(client);
	            
	            client.run(); // execute the runnable thread's code
			}
			
			// Close the server and close all client threads
			try {
				serverSocket.close();
				for (int x = 0; x < clientList.size(); x++) {
					ClientThread temp = clientList.get(x);
					
					try {
						temp.input.close();
						temp.output.close();
						temp.socket.close();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		catch (IOException e) {
			String msg = time.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
			System.out.println(msg);
		}
		finally {

		}
	}
	
	
    public static void main(String[] args) {
        // start server on port 1500 unless a PortNumber is specified
        int portNumber = 1500;
        
        switch (args.length) {
        	case 0:
        		System.out.println("Starting server with deafault values....");
        		break;
            case 1:
                try {
                    portNumber = Integer.parseInt(args[0]);
                }
                catch(Exception e) {
                    System.out.println("Invalid port number.");
                    System.out.println("Usage is: > java Server [portNumber]");
                    return;
                }
            default:
                System.out.println("Usage is: > java Server [portNumber]");
                return;

        }
        
        // Creating and starting the server
        Server server = new Server(portNumber);
        server.start();
    }
	
	
}
