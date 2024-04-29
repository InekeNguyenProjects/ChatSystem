import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class ClientChat {
    private JFrame outerFrame, innerFrame;;
    private JTextField userText;
    private JTextArea chatWindow;
    private PrintWriter output;
    private BufferedReader input;
    private String message;
    private String serverIP;
    private Socket connection;
    private String name;
    
    public void setup (String host) {
    	
    }
    
    public void run () {
    	
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientChat clientChat = new ClientChat();
		
		clientChat.setup("6.tcp.ngrok.io");

	}

}
