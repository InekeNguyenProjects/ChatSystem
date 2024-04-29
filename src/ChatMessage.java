import java.io.*;

public class ChatMessage implements Serializable {
	
    private static final long serialVersionUID = 1L;
    private static final int WHO = 0, MESSAGE = 1, LOGOUT = 2;
    
    private int type;
    private String message;

	public ChatMessage(int type, String message) {
		this.type =type;
		this.message = message;
	}
	
	public int getType () {
		return type;
	}
	
	public String getMessage () {
		return message;
	}
	
	public void message (int type, String message) {
		this.type =type;
		this.message = message;
	}
	
	public static int getWho () {
		return WHO;
	}
	
	public static int getMsg () {
		return MESSAGE;
	}
	
	public static int getLogout() {
		return LOGOUT;
	}
}
