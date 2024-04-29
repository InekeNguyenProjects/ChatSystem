import java.util.*;
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
        ID = Server.getID();
        this.socket = socket;
        
        //Creating both Data Stream
        System.out.println("Thread trying to create Object Input/Output Streams");
        try
        {
            output = new ObjectOutputStream(socket.getOutputStream());
            input  = new ObjectInputStream(socket.getInputStream());
            // read the username
            username = (String) input.readObject();
            Server.broadcast(username + " has joined the chat room.");
        }
        catch (IOException e) {
            Server.display("Exception creating new Input/output Streams: " + e);
            return;
        }
        catch (ClassNotFoundException e) {
        	e.printStackTrace();
        }
        
        date = new Date().toString() + "\n";
    }
	
	
    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    
    public boolean writeMessage(String msg) {
        // if Client is still connected send the message to it
        if(!socket.isConnected()) {
            close();
            return false;
        }
        // write the message to the stream
        try {
            output.writeObject(msg);
        }
        // if an error occurs, do not abort just inform the user
        catch(IOException e) {
            System.out.println("Error sending message to " + username) ;
            e.toString();
        }
        return true;
    }
    
    private void close() {
        try {
            if (output != null) 
            	output.close();
            
            if (input != null) 
            	input.close();
            
            if (socket != null) 
            	socket.close();
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    

}
