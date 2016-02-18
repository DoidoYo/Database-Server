import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class DataProcessorThread extends Thread {

	BlockingQueue<Action> queue;
	boolean receiving = true;

	Connection conn;

	public DataProcessorThread(BlockingQueue<Action> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {

		try {
			
			mysqlHandler.init();
			
			while (true) {
				Action action = queue.take();

				// print request
				System.out.println("request: " + action.request);

				OutputStream out = action.client.getOutputStream();

				File file = null;

				if (receiving) {
					if (action.request.contains("serverStatus:off")) {
						receiving = false;
					} else if (action.request.equals("")) { // return main page
						file = new File("resources\\website\\index.html");
						sendFile(file, out);
					} else if (action.request.contains("fav")) { // return page
																	// specified
						file = new File("resources\\website\\images\\logo.png");
						sendFile(file, out);
					} else if(action.request.contains("get=wantedclasses?")) {
						
						String id = action.request.substring(action.request.indexOf("?")+1);
						
						String wanted[] = mysqlHandler.getStudentStudents(id);
						
						String periods[][] = mysqlHandler.getPeriod();
						
						String msgOut[][] = new String[36][8];
						
						String msg = "";
						
						
						int c=0;
						for(String[] p:periods) {
							for(String s:wanted) {
								
								if(p[0].equals(s)) {
									msgOut[c] = p;
									msg += "?" + p[0];
									for(int i = 1; i < p.length;i++) {
										msg+= "&" + p[i];
									}
									
									c++;
								}
								
							}
						}
						
						msg += "?";
						
						out.write(("HTTP/1.1 200 OK\n\n" + msg).getBytes());
						out.flush();
						
					} else if(action.request.contains("get=change?")) {
						String id = action.request.substring(action.request.indexOf("?")+1);
						
						String data[] = mysqlHandler.getStudentStudents(id);
						
						if(data[3] == null) {
							System.out.println("Not set!");
							
							out.write(("HTTP/1.1 200 OK\n\n" + "false").getBytes());
							out.flush();
							
						} else {
							
							out.write(("HTTP/1.1 200 OK\n\n" + "true").getBytes());
							out.flush();
							System.out.println("Set!");
						}
						
						//System.out.println("yeet: " + id);
					} else if(action.request.contains("setChange=")) {
						String in = action.request.replaceAll("%20", " ");
						
						String classes[] = new String[8];
						int mark = 10;
						
						int c = 0;
						for(int i = 0; i < in.length();i++) {
							if(in.charAt(i) == '&') {
								classes[c] = in.substring(mark, i);
								mark = i+1;
								c++;
							}
						}
						
						String inMsg[] = new String[8];
						
						inMsg[0] = classes[7];
						
						for(int i=0;i<7;i++) {
							inMsg[i+1] = classes[i];
							//System.out.println(classes[i]);
						}
						
						mysqlHandler.setClass(inMsg);
						
						out.write("HTTP/1.1 200 OK\n\n".getBytes());
						out.flush();
						
					} else if(action.request.contains("setwanted=")) {
						
						String in = action.request.replaceAll("%20", " ");
						
						String classes[] = new String[8];
						int mark = 10;
						
						int c = 0;
						for(int i = 0; i < in.length();i++) {
							if(in.charAt(i) == '&') {
								classes[c] = in.substring(mark, i);
								mark = i+1;
								c++;
							}
						}
						
						//for(String s:classes) {
						//	System.out.println(s);
						//}
						
						mysqlHandler.setClassStudents(new String[] {classes[7],classes[2],classes[0],classes[3],classes[5],classes[4],classes[6],classes[1]});
						
						out.write("HTTP/1.1 200 OK\n\n".getBytes());
						out.flush();
						
					}else if (action.request.equals("get=classes")) {
						ArrayList<String> classes[] = mysqlHandler.getClasses();
						
						String msg = "?";
						
						for(ArrayList<String> c:classes) {
							for(int i = 0; i < c.size();i++) {
								if(i != c.size()-1) {
									msg += c.get(i) + "&";
								} else {
									msg += c.get(i) + "?";
								}
							}
						}
						
						//System.out.println(msg);
					
						out.write(("HTTP/1.1 200 OK\n\n" + msg).getBytes());
						out.flush();
						
					} else if(action.request.contains("name=")) {
						String id = action.request.substring(5);
						
						String data[] = mysqlHandler.getStudentStudents(id);
						
						String msg = "" + data[1];
						
						out.write(("HTTP/1.1 200 OK\n\n" + msg).getBytes());
						out.flush();
						
					}else if(action.request.contains("load")) {
						file = new File("resources\\website\\index2.html");
						sendFile(file, out);
					} else if (action.request.contains("login")) {

						String id, password;

						String holder[] = { "", "" };
						int holderC = -1;
						boolean record = false;

						for (int i = 0; i < action.request.length(); i++) {

							if (record) {
								holder[holderC] += action.request.charAt(i);
							}

							if (action.request.charAt(i) == '=') {
								record = true;
								holderC++;
							}
							if (action.request.charAt(i) == '?' || action.request.charAt(i) == '&') {
								record = false;
							}

						}

						for (int i = 0; i < 2; i++) {
							for (int x = 0; x < holder[i].length(); x++) {
								if (holder[i].charAt(x) == '&' || holder[i].charAt(x) == '?') {
									holder[i] = holder[i].substring(0, x) + "" + holder[i].substring(x + 1);
								}
							}
						}

						id = holder[0];
						password = holder[1];

						String[] info = mysqlHandler.getStudentLogin(id);
						
						if(mysqlHandler.studentExists(id) && info[1].equals(password)) {
							out.write("HTTP/1.1 200 OK\n\n Logging in!".getBytes());
							out.flush();
						} else {
							out.write("HTTP/1.1 200 OK\n\n Incorrect Information!".getBytes());
							out.flush();
						}
						
						//System.out.println(id);
						//System.out.println(password);

					} else if (action.request.contains("signup")) {

						String id, pass, first, last, grade, email;

						String holder[] = {"","","","","",""};
						int holderC = -1;
						boolean record = false;

						for (int i = 0; i < action.request.length(); i++) {

							if (record) {
								holder[holderC] += action.request.charAt(i);
							}

							if (action.request.charAt(i) == '=') {
								record = true;
								holderC++;
							}
							if (action.request.charAt(i) == '?' || action.request.charAt(i) == '&') {
								record = false;
							}

						}

						for (int i = 0; i < holder.length; i++) {
							for (int x = 0; x < holder[i].length(); x++) {
								if (holder[i].charAt(x) == '&' || holder[i].charAt(x) == '?') {
									holder[i] = holder[i].substring(0, x) + "" + holder[i].substring(x + 1);
								}
							}
						}

						id = holder[0];
						pass = holder[1];
						first = holder[2];
						last = holder[3];
						email = holder[5];
						grade = holder[4];
						
						boolean cond1 = mysqlHandler.addStudentStudents(new String[]{id,pass,first,last,email,grade});
						boolean cond2 = mysqlHandler.addStudentLogin(new String[]{id,pass,first,last,email,grade});
						
						if(!cond1 || !cond2) {
							out.write("HTTP/1.1 200 OK\n\n ok".getBytes());
							out.flush();
						} else {
							out.write("HTTP/1.1 200 OK\n\n ID Number Already in Use!".getBytes());
							out.flush();
						}
						
						/*for(int i=0;i < holder.length;i++) {
							System.out.println(holder[i]);
						}*/

					} else if (action.request.contains("confirm")) {
						out.write("hello".getBytes());
						out.flush();
					} else {
						file = new File("resources\\website\\" + action.request);
						sendFile(file, out);
					}
				} else {
					if (action.request.contains("serverStatus:on")) {
						receiving = true;
					} else {
						file = new File("resources\\website\\html\\test.html");
						sendFile(file, out);
					}
				}
				out.close();
				action.client.close();

			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

	void sendFile(File file, OutputStream out) {
		if (file.exists()) {
			// send http response
			String response = "HTTP/1.1 200 OK" + "\r\n\r\n";
			byte[] buffer = response.getBytes();
			try {
				out.write(buffer);
				// send file request
				BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
				buffer = new byte[(int) file.length()];
				inStream.read(buffer);
				out.write(buffer);
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Error, no file names: " + file.getName());
		}
	}

}
