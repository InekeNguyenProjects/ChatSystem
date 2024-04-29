import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

public class ClientChatWindow {
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
    	outerFrame = new JFrame ("Chat System");
    	outerFrame.setSize(400, 600);
    	
    	JPanel panel = new JPanel();  
    	outerFrame.add(panel);
    	
    	serverIP = host;
    	
    	JLabel labelName = new JLabel ("Enter your name: ");
    	JTextField textFieldName = new JTextField ("Name");
    	textFieldName.setBounds(50,50,150,20);
    	JButton submitButton = new JButton ("Submit");
    	
    	panel.add(labelName);
    	panel.add(textFieldName);
    	panel.add(submitButton);
    	
    	submitButton.addActionListener (new ActionListener () {
    		public void actionPerformed(ActionEvent e) {
                name = textFieldName.getText();
                outerFrame.setVisible(false);
                outerFrame.dispose();
                innerFrame.setVisible(true);
            }
    	});

    	outerFrame.setVisible(true);
    	
    	innerFrame = new JFrame();
        chatWindow=new JTextArea();
        userText=new JTextField();
        Button end = new Button("End");
        userText.setEditable(true);
        chatWindow.setEditable(false);
        userText.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		sendMessage(e.getActionCommand());
                userText.setText("");
            }
        });
        
        end.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 sendMessage("Client_wants_to_end_the_connection");
                 closeStreams();
                 innerFrame.dispose();
                 System.exit(0);
             }
         });
    	
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
        mainPanel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        bottomPanel.add(userText, BorderLayout.CENTER);
        bottomPanel.add(end, BorderLayout.LINE_END);
        
        innerFrame.getContentPane().add(mainPanel);
        innerFrame.setSize(400,600);
        innerFrame.setVisible(false);
        innerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private void sendMessage(String message){
    	try {
    		output.println(name + " - " + message);
            showMessage("\n" + name + " - " + message);
        }
    	catch(Exception e) {
            chatWindow.append("\n Message not sent,some error occurred");
        }

    }
    
    private void showMessage(final String s) {
        SwingUtilities.invokeLater (new Runnable() {
        	@Override
        	public void run() {
        		chatWindow.append(s);
            }
       });
    }
    
    
    private void closeStreams () {
    	try {
    		output.close();
            input.close();
            connection.close();
        }
    	catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() throws IOException{
        showMessage("Attempting Connection..\n");
        connection =  new Socket(InetAddress.getByName(serverIP),14205);
        showMessage("Connected to: "+ connection.getInetAddress().getHostName());
    }
    
    private void setupStreams() throws IOException{
        output = new PrintWriter(connection.getOutputStream(),true);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        showMessage("\n Input and Output streams are good to go. Have fun chatting! \n");
    }
    
    private void displayChatWhileTalking () throws IOException {
    	while (true) {
    		try {
    			message = (String) input.readLine() + "\n";
    			showMessage (message);
    		}
    		catch (Exception e) {
    			showMessage("Error: There is an issue showing chat message on screen.");
    		}
    	}
    }

    
    // Main method that runs the entire chat interface
    public void run () {
    	try {
            connectToServer();
            setupStreams();
            displayChatWhileTalking();		
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
    		closeStreams ();
    	}
    	
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientChatWindow clientChat = new ClientChatWindow();
		
		clientChat.setup("0.tcp.ngrok.io");
		clientChat.run();

	}

}
