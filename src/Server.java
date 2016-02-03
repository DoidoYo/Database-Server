import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {

	ServerSocket serverSocket;
	DataProcessorThread processor;

	BlockingQueue<Action> dataQueue = new ArrayBlockingQueue<Action>(1024);

	public Server(int port) {

		try {
			serverSocket = new ServerSocket(port); // creates server
			
			processor = new DataProcessorThread(dataQueue);
			processor.start();
			
			while (true) {
				Socket client = serverSocket.accept(); // blocking function that
														// waits for connection
				(new ClientThread(client, dataQueue)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
