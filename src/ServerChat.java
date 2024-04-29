import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerChat implements Runnable {
	
    Socket connection;
    PrintWriter output;
    BufferedReader input;
    String clientName;
    static Vector<ServerChat> sockets = new Vector <ServerChat>();
    
    public ServerChat (String clientName, Socket connection, PrintWriter output, BufferedReader input) {
        this.connection = connection;
        this.output = output;
        this.input = input;
        this.clientName = clientName;
    }
    
    
    String receive() throws IOException {
        return input.readLine();
    }


    void send(String msg) {
        output.println(msg);
    }


    
    void CloseStreams() throws IOException {
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
                    for(ServerChat c:ServerChat.sockets) {
                        if(!c.clientName.equals(clientName)) c.send(message);
                    }
                    CloseStreams();
                    break;
                }

                for(ServerChat c : ServerChat.sockets) {
                    if(!c.clientName.equals(clientName))  c.send(message);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
		
	}

}
