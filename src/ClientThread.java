import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientThread extends Thread{

	Socket client;
	BlockingQueue<Action> queue;
	
	public ClientThread(Socket client, BlockingQueue<Action> queue) {
		this.client = client;
		this.queue = queue;
	}
	
	@Override
	public void run() {
		//read client input and decide what to do
		try {
			OutputStream out = client.getOutputStream();
			InputStream in = client.getInputStream();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			String message = "";
			String inputLine = "";
			
			while((inputLine = reader.readLine()) != null) {
				message += inputLine;
				//System.out.println(inputLine);
				
				String normal = message;
				
				if(inputLine.toLowerCase().contains("language")) {
					//read message
					
					if(message.toLowerCase().contains("get")) {
						int get = message.toLowerCase().indexOf("get");
						int http = message.toLowerCase().indexOf("http");
						
						String request = normal.substring(get+5, http-1);
						
						queue.add(new Action(request, client));
					}
					
					break;
				} else {
					if(message.contains("serverStatus:")) {
						queue.add(new Action(normal, client));
					}
					/*if(message.contains("on") || message.contains("off")) {
						queue.add(new Action(message, client));
					}*/
					//client.close();
				}
				
			}
			
			//System.out.println("CLOSING");
			//client.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
