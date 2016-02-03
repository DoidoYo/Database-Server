import java.net.Socket;

public class Action{

	String request;
	Socket client;
	
	public Action(String request, Socket client) {
		this.request = request;
		this.client = client;
	}
	
}
