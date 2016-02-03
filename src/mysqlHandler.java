
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class mysqlHandler {

	private static Connection connection;

	static void init() {
		try {
			connection = DriverManager
					.getConnection("jdbc:mysql://mastacademy.ddns.net:1234/school?user=counselor&password=mastacademy");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// static int ID = 0, USERNAME=1, PASSWORD=2,FIRST_NAME=3, LAST_NAME =4,
	// EMAIL=5, PERIOD_1=6, PERIOD_2=7, PERIOD_3=8, PERIOD_4=9, PERIOD_5=10,
	// PERIOD_6=11, PERIOD_7=12;

	static boolean addStudentLogin(String in[]) {
		// String msg = "insert into login (id,
		// password,firstname,lastname,email) values (?,?,?,?,?)";
		// PreparedStatement statement;
		boolean exists = false;
		try {

			String msg = "SELECT * FROM login WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);
			statement.setString(1, in[0]);
			ResultSet result = statement.executeQuery();

			String out[] = new String[2];

			while (result.next()) {
				out[0] = result.getString(1);
				out[1] = result.getString(2);
			}

			statement.close();
			result.close();

			if (out[1] != null)
				exists = true;
			else
				exists = false;

			if (!exists) {
				msg = "insert into login (id,password,firstname,lastname,email) values (?,?,?,?,?)";
				statement = connection.prepareStatement(msg);
				statement.setString(1, in[0]);
				statement.setString(2, (String) in[1]);
				statement.setString(3, (String) in[2]);
				statement.setString(4, (String) in[3]);
				statement.setString(5, (String) in[4]);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	static boolean addStudentStudents(String in[]) {
		boolean exists = false;
		try {
			String msg = "SELECT * FROM students WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);
			statement.setString(1, in[0]);
			ResultSet result = statement.executeQuery();

			String out[] = new String[2];

			while (result.next()) {
				out[0] = result.getString(1);
				out[1] = result.getString(2);
			}

			statement.close();
			result.close();

			if (out[1] != null)
				exists = true;
			else
				exists = false;

			if (!exists) {
				msg = "insert into students (id, name, grade) values (?,?,?)";
				statement = connection.prepareStatement(msg);
				statement.setString(1, in[0]);
				statement.setString(2, (String) in[2]);
				statement.setString(3, in[5]);
				statement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exists;
	}

	static void setClassStudents(String info[]) {
		try {
			String msg = "update students set math = ?, science = ?, social = ?, language = ?, art = ?, pe = ?, english = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);

			statement.setString(1, (String) info[1]);
			statement.setString(2, (String) info[2]);
			statement.setString(3, (String) info[3]);
			statement.setString(4, (String) info[4]);
			statement.setString(5, (String) info[5]);
			statement.setString(6, (String) info[6]);
			statement.setString(7, (String) info[7]);

			statement.setString(8, info[0]);

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void setClass(String info[]) {
		try {
			String msg = "update login set pd1 = ?, pd2 = ?, pd3 = ?, pd4 = ?, pd5 = ?, pd6 = ?, pd7 = ? WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);

			statement.setString(1, (String) info[1]);
			statement.setString(2, (String) info[2]);
			statement.setString(3, (String) info[3]);
			statement.setString(4, (String) info[4]);
			statement.setString(5, (String) info[5]);
			statement.setString(6, (String) info[6]);
			statement.setString(7, (String) info[7]);

			statement.setString(8, info[0]);

			statement.executeUpdate();

			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static boolean studentExists(String id) {
		try {
			String msg = "SELECT * FROM students WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();

			String out[] = new String[2];

			while (result.next()) {
				out[0] = result.getString(1);
				out[1] = result.getString(2);
			}

			statement.close();
			result.close();

			if (out[1] != null)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	static ArrayList[] getClasses() {
		try {
		String msg = "SELECT * FROM classes";
		PreparedStatement statement = connection.prepareStatement(msg);
		ResultSet result = statement.executeQuery();

		String out[] = new String[13];
		
		ArrayList<String> classes[] = new ArrayList[7];
		
		classes[0] = new ArrayList<>();
		classes[0].add("science");
		
		classes[1] = new ArrayList<>();
		classes[1].add("english");
		
		classes[2] = new ArrayList<>();
		classes[2].add("math");
		
		classes[3] = new ArrayList<>();
		classes[3].add("social");
		
		classes[4] = new ArrayList<>();
		classes[4].add("art");
		
		classes[5] = new ArrayList<>();
		classes[5].add("language");
		
		classes[6] = new ArrayList<>();
		classes[6].add("pe");
		
		while (result.next()) {
			String className = result.getString(1);
			String classType = result.getString(2);
			
			loop:
			for(ArrayList<String> s:classes) {
				if(s.get(0).equals(classType)) {
					s.add(className);
					break loop;
				}
			}
		}
		
		//System.out.println(classes[0].size());
		
		return classes;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	static String[] getStudentLogin(String id) {
		try {
			String msg = "SELECT * FROM login WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();

			String out[] = new String[13];

			while (result.next()) {
				out[0] = result.getString(1);
				out[1] = result.getString(2);
				out[2] = result.getString(3);
				out[3] = result.getString(4);
				out[4] = result.getString(5);
				out[5] = result.getString(6);
				out[6] = result.getString(7);
				out[7] = result.getString(8);
				out[8] = result.getString(9);
				out[9] = result.getString(10);
				out[10] = result.getString(11);
				out[11] = result.getString(12);
			}

			/*
			 * for(int i = 0; i < out.length; i++) { System.out.println(out[i]);
			 * }
			 */

			statement.close();
			result.close();

			return out;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	static String[] getStudentStudents(String id) {
		try {
			String msg = "SELECT * FROM students WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(msg);
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();

			String out[] = new String[10];

			while (result.next()) {
				out[0] = result.getString(1);
				out[1] = result.getString(2);
				out[2] = result.getString(3);
				out[3] = result.getString(4);
				out[4] = result.getString(5);
				out[5] = result.getString(6);
				out[6] = result.getString(7);
				out[7] = result.getString(8);
				out[8] = result.getString(9);
				out[9] = result.getString(10);
			}

			/*
			 * for(int i = 0; i < out.length; i++) { System.out.println(out[i]);
			 * }
			 */

			statement.close();
			result.close();

			return out;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	static void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
