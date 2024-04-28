import java.net.*;
import java.io.*;
import java.util.*;

public class Client  {

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	private String username;
	private String server; //server address
	
	private int port;

	public Client (String server, int port, String username) {
		this.server = server;
		this.port = port;
		this.username = username;
	}
	
	////////////////////////////////// Accessors
	public String getUsername () {
		return username;
	}
	
	public String getServer () {
		return server;
	}

	public int getPort () {
		return port;
	}
	
	public Socket getSocket () {
		return socket;
	}
	
	public ObjectInputStream getInputStream () {
		return input;
	}
	
	public ObjectOutputStream getOutStream () {
		return output;
	}
	////////////////////////////////////////////
	
	/////////////////////////////////// Mutators
	public void setUsername (String username) {
		this.username = username;
	}
	
	public void setServer (String server) {
		this.server = server;
	}
	
	public void setPort (int port) {
		this.port = port;
	}
	
	public void setSocket (Socket socket) {
		this.socket = socket;
	}
	
	public void setInputStream (ObjectInputStream input) {
		this.input = input;
	}
	
	public void setOutputStream (ObjectOutputStream output) {
		this.output = output;
	}
	///////////////////////////////////////////
	
    public void display (String message) {

        System.out.println(message);

    }
    
    void sendMessageToServer (ChatMessage message) {
        try {
            output.writeObject(message);
        }
        catch(IOException e) {
            display("Exception writing to server: " + e);
        }
    }
    
    private void disconnectStreams () {
        try {
            if(input != null) 
            	input.close();
        }
        catch(Exception e) {
        	e.getMessage();
        }
        
        try {
            if(output != null) 
            	output.close();
        }
        catch(Exception e) {
        	e.getMessage();
        }
        
        try {
            if(socket != null) 
            	socket.close();
        }
        catch(Exception e) {
        	e.getMessage();
        }
    }
}
