import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerChat {
	
    static Vector <ServerHandler> sockets = new Vector <ServerHandler>();
    

    public static void main (String args[]) throws IOException
    {
        ServerSocket socket = new ServerSocket(2020); //Server socket running at port 2020
        int i = 0;
        
        System.out.println("Server is waiting for connections");
        
        while (true) {
            System.out.println("Current Client Count - "+ sockets.size());
            
            Socket connection = socket.accept();
            String name = "Client - " + i;
            
            PrintWriter output = new PrintWriter(connection.getOutputStream(),true);
            BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ServerHandler handler = new ServerHandler(name, connection, output, input);
            
            sockets.add(handler);
            Thread myThread = new Thread(handler);
            myThread.start();
            
            ++i;
        }
    }


}
