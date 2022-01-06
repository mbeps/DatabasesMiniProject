import java.io.*;
import java.sql.*;
import java.util.*;

public class App {
	public static void main(String[] argv) throws SQLException {
		//^ Establish Connection
		String user = "zjac268";
		String password = "roosah";
		// String database = "teachdb.cs.rhul.ac.uk";
		String database = "localhost";
		// END
		
		Connection connection = connectToDatabase(user, password, database);
		if (connection != null) {
			System.out.println("SUCCESS: You made it!"
					+ "\n\t You can now take control of your database!\n");
		} else {
			System.out.println("ERROR: \tFailed to make connection!");
			System.exit(1);
		}
		//£ Now we're ready to use the DB. You may add your code below this line.
		//^ Return from Database
		String query = "SELECT * FROM branch;";
		ResultSet rs = executeQuery(connection, query);
		try {
			while (rs.next()) {
				System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		rs.close();

		//^ Dropping Tables
		System.out.println("Dropping Table");
		dropTable(connection, "test");

		//^ Creating Table 
		System.out.println("Creating Table");
		// createTable(connection, "test (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
	}

	public static void dropTable(Connection connection, String table) {
		try {
			Statement st = connection.createStatement();
			st.execute("DROP TABLE IF EXISTS " + table); //£ Do not drop table if it does not exist
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public static void createTable(Connection connection, String tableDescription) {
		try {
			Statement st = connection.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS " + tableDescription); //£ Do not drop table if it does not exist
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	public static ResultSet executeQuery(Connection connection, String query) {
		System.out.println("DEBUG: Executing Query");
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ADVANCED: This method is for advanced users only. You should not need to change this!
	public static Connection connectToDatabase(String user, String password, String database) {
		System.out.println("------ Testing PostgreSQL JDBC Connection ------");
		Connection connection = null;
		try {
			String protocol = "jdbc:postgresql://";
			String dbName = "/CS2855/";
			String fullURL = protocol + database + dbName + user;
			connection = DriverManager.getConnection(fullURL, user, password);
		} catch (SQLException e) {
			String errorMsg = e.getMessage();
			if (errorMsg.contains("authentication failed")) {
				System.out.println("ERROR: \tDatabase password is incorrect. Have you changed the password string above?");
				System.out.println("\n\tMake sure you are NOT using your university password.\n"
						+ "\tYou need to use the password that was emailed to you!");
			} else {
				System.out.println("Connection failed! Check output console.");
				e.printStackTrace();
			}
		}
		return connection;
	}
}