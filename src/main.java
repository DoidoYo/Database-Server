import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class main {

	static Server server;
	
	
	
	public static void main(String[] args) {
		
		//creates window
		JFrame frame = new JFrame("Server");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setVisible(true);
		
		frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	//System.out.println("Stopping Server");
	        	//stops server
	        	server.close();
	        }

	    });
		
		//starts server @ port 80
		server = new Server(80);
		server.run();
		
	}
	
	
	
}
