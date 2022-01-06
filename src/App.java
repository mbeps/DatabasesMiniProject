import java.sql.*;

public class App {
	//! ADVANCED: This method is for advanced users only. You should not need to change this!
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
	
	/**
	 * Establishes connection with the database. 
	 * If the connection is successful, success massage is displayed and the connection is returned. 
	 * If the connection is unsuccessful, error messaged is returned and null is returned. 
	 * @return (Connection): connection to the database
	 */
	public static Connection establishConnection() {
		String user = "zjac268";
		String password = "roosah";
		// String database = "teachdb.cs.rhul.ac.uk";
		String database = "localhost";
		
		Connection connection = connectToDatabase(user, password, database);
		if (connection != null) {
			System.out.println("SUCCESS: You made it!"
					+ "\n\t You can now take control of your database!\n");
			return connection;
		} else {
			System.out.println("ERROR: \tFailed to make connection!");
			System.exit(1);
			return null; // Invalid connection
		}
	}

	/**
	 * Drops table if it already exists in the database. 
	 * @param connection (Connection): connection to database
	 * @param table (String): name of the table to be dropped
	 */
	public static void dropTable(Connection connection, String table) {
		try {
			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE IF EXISTS " + table); 
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Creates a new table if it does not exist already in the database.
	 * @param connection (Connection): connection to database
	 * @param tableDescription (String): layout of the table in SQL 
	 */
	public static void createTable(Connection connection, String tableDescription) {
		try {
			Statement st = connection.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS " + tableDescription); 
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Executes SQL query that is passed to the method.   
	 * @param connection (Connection): connection to database
	 * @param query (String): query to be executed
	 * @return (ResultSet): returns result set to the query
	 */
	public static ResultSet executeQuery(Connection connection, String query) {
		System.out.println("DEBUG: Executing Query");
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] argv) throws SQLException {
		//^ Establish Connection
		Connection connection = establishConnection();
		
		//£ Now we're ready to use the DB. You may add your code below this line.
		//^ Return from Database
		String query = "SELECT * FROM branch;";
		ResultSet resultSet = executeQuery(connection, query);
		//^ Printing Table 
		try {
			while (resultSet.next()) {
				System.out.printf("%s %s %s %n", resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		resultSet.close();

		//^ Creating Table 
		System.out.println("Creating Table");
		createTable(connection, "test (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
		createTable(connection, "new (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
		
		//^ Dropping Tables
		System.out.println("Dropping Table");
		dropTable(connection, "new");
	}
}