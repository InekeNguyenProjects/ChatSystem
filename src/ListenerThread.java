import java.io.IOException;
import java.io.ObjectInputStream;

public class ListenerThread extends Thread {
	
	private ObjectInputStream input;
	
	public ListenerThread (ObjectInputStream input) {
		this.input = input;
	}
		
	
	public void run() {
        while(true) {
            try {
                // read the message form the input datastream
                String message = (String) input.readObject();
                // print the message
                System.out.println(message);
                System.out.print("> ");
            }
            catch(IOException e) {
                System.out.println("Server has closed the connection: " + e);
                break;
            }
            catch(ClassNotFoundException e2) {
            	e2.getMessage();
            }
        }
    }
}


