import java.io.IOException;

public class ListenerThread extends Thread {
	
	private Client client;
	
	public ListenerThread (Client client) {
		this.client = client;
	}
		
	
	public void run() {
        while(true) {
            try {
                // read the message form the input datastream
                String msg = (String) client.getInputStream().readObject();
                // print the message
                System.out.println(msg);
                System.out.print("> ");
            }
            catch(IOException e) {
                client.display("Server has closed the connection: " + e );
                break;
            }
            catch(ClassNotFoundException e2) {
            }
        }
    }
}


