
public class ServerHandler implements Runnable {
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
                    closeStreams();
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
