import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread{

	ServerSocket serverSocket;
	DataProcessorThread processor;

	BlockingQueue<Action> dataQueue = new ArrayBlockingQueue<Action>(1024);

	boolean running = true;
	
	//init variabldes
	public Server(int port) {

		try {
			//init server
			serverSocket = new ServerSocket(port); // creates server
			
			//one thread to proccess requests
			processor = new DataProcessorThread(dataQueue);
			processor.start();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void run() {
		
		//make a new thread for every client so that there are no delays
		while (running) {
			Socket client;
			try {
				client = serverSocket.accept();
				(new ClientThread(client, dataQueue)).start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // blocking function that
		}
		
	}
	
	//stop the loop and shutdown thread
	public void close() {
		running = false;
		processor.close();
	}

}
