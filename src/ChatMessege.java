import java.io.*;

public class ChatMessege implements Serializable {
	
    static final int WHO = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;
    private String message;

	public ChatMessege(int type, String message) {
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
}
