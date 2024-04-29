import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable {
    Socket connection;
    PrintWriter output;
    BufferedReader input;
    String clientName;
    
    
    public ServerHandler (String clientName, Socket connection, PrintWriter output, BufferedReader input) {
        this.connection = connection;
        this.output = output;
        this.input = input;
        this.clientName = clientName;
    }
	
    public String getClientName () {
    	return clientName;
    }
    
    public String receive() throws IOException {
        return input.readLine();
    }


   public void send(String msg) {
        output.println(msg);
    }

    
    public void closeStreams() throws IOException {
        output.close();
        input.close();
        connection.close();
        ServerChat.sockets.remove(this);
        System.out.println("Current Client Count- "+ ServerChat.sockets.size());
    }
    
 
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
            while (true) {
                String message = receive();

                if (message.endsWith("Client_wants_to_end_the_connection")) {
                    message = message.substring(0,message.indexOf('-'))+" has left the server";
                    for(ServerHandler c : ServerChat.sockets) {
                        if(!c.getClientName().equals(clientName)) c.send(message);
                    }
                    closeStreams();
                    break;
                }

                for(ServerHandler c : ServerChat.sockets) {
                    if(!c.clientName.equals(clientName))  c.send(message);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
		
	}

}
