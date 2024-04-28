import java.util.*;
import java.text.SimpleDateFormat; 
import java.net.*;
import java.io.*;

public class ClientThread extends Thread {
	// the socket to get messages from client
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    // my unique id (easier for disconnection)
    int ID;
    
    // the Username of the Client
    String username;
    
    // message object to receive message and its type
    ChatMessage chatMessage;;
    
    // timestamp
    String date;
	
	public ClientThread (Socket socket) {
        // a unique id
        id = ++uniqueId;
        this.socket = socket;
        //Creating both Data Stream
        System.out.println("Thread trying to create Object Input/Output Streams");
        try
        {
            output = new ObjectOutputStream(socket.getOutputStream());
            input  = new ObjectInputStream(socket.getInputStream());
            // read the username
            username = (String) input.readObject();
            broadcast(username + " has joined the chat room.");
        }
        catch (IOException e) {
            display("Exception creating new Input/output Streams: " + e);
            return;
        }
        catch (ClassNotFoundException e) {
        }
        date = new Date().toString() + "\n";
    }

}
