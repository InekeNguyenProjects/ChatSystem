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
        catch (IOException e) {
            display("Exception writing to server: " + e);
        }
    }
    
    private void disconnectStreams () {
        try {
            if(input != null) 
            	input.close();
        }
        catch (Exception e) {
        	e.getMessage();
        }
        
        try {
            if(output != null) 
            	output.close();
        }
        catch (Exception e) {
        	e.getMessage();
        }
        
        try {
            if(socket != null) 
            	socket.close();
        }
        catch (Exception e) {
        	e.getMessage();
        }
    }
    
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket (server, port);
        }
        // exception handler if it failed
        catch (Exception ec) {
            display ("Error connecting to server:" + ec);
            return false;
        }

        display ("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        /* Creating both Data Stream */
        try
        {
            input  = new ObjectInputStream (socket.getInputStream());
            output = new ObjectOutputStream (socket.getOutputStream());
        }
        catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        ListenerThread listen = new ListenerThread (input);
        listen.start();
        
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try
        {
            output.writeObject(username);
        }
        catch (IOException eIO) {
            display("Exception doing login: " + eIO);
            disconnectStreams();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }
    
    
    public static void main(String[] args) {
        // default values if not entered
        int portNumber =11926;
        String serverAddress = "2.tcp.ngrok.io";
        String userName = "Anonymous";
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter your username: ");
        userName = scan.nextLine();

        // different case according to the length of the arguments.
        switch(args.length) {
        	case 0:
        		// for > java Client
        		break;
            case 1:
                // for > javac Client username
                userName = args[0];
            case 2:
                // for > javac Client username portNumber
                try {
                    portNumber = Integer.parseInt(args[1]);
                }
                catch(Exception e) {
                    System.out.println("Invalid port number.\nUsage is: > java Client [username] [portNumber] [serverAddress]");
                    return;
                }
            case 3:
                // for > javac Client username portNumber serverAddr
                serverAddress = args[2];
            default: // if number of arguments are invalid
                System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
                return;
        }
        
        // create the Client object
        Client client = new Client(serverAddress, portNumber, userName);
        // try to connect to the server and return if not connected
        if(!client.start())
            return;

        System.out.println("\nHello.! Welcome to the chatroom.");
        System.out.println("Instructions:");
        System.out.println("1. Simply type the message to send broadcast to all active clients");
        System.out.println("2. Type '@username<space>yourmessage' without quotes to send message to desired client");
        System.out.println("3. Type 'WHOISIN' without quotes to see list of active clients");
        System.out.println("4. Type 'LOGOUT' without quotes to logoff from server");

        // infinite loop to get the input from the user
        while(true) {
            System.out.print("> ");
            // read message from user
            String message = scan.nextLine();
            
            // logout if message is LOGOUT
            if(message.equalsIgnoreCase("LOGOUT")) {
                client.sendMessageToServer(new ChatMessage(ChatMessage.getLogout(), ""));
                break;
            }
            // message to check who are present in chatroom
            else if(message.equalsIgnoreCase("WHO")) {
                client.sendMessageToServer(new ChatMessage(ChatMessage.getWho(), ""));
            }
            // regular text message
            else {
                client.sendMessageToServer(new ChatMessage(ChatMessage.getMsg(), message));
            }
        }
        
        // close resource
        scan.close();
        // client completed its job. disconnect client.
        client.disconnectStreams();
    }
    
    
}
