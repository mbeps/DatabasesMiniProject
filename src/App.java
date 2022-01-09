import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Manages database by providing functionalities. 
 * Method included to connect to database. 
 * Methods included for manipulating database.
 */
class DatabaseManagement {
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
	public static void createTable(Connection connection, String tableName, String tableDescription) {
		try {
			Statement st = connection.createStatement();
			st.execute("CREATE TABLE IF NOT EXISTS " + tableName + tableDescription); 
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

	/**
	 * Inserts data into desired table. 
	 * @param connection (Connection): connection to the database
	 * @param tableName (String): name of the table where data needs to inserted
	 * @param dataDescription (String): data to be inserted separated by ','
	 */
	public static void insert(Connection connection, String tableName, String dataDescription) {
		try {
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO " + tableName + " VALUES (" + dataDescription + ");";
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			System.out.println("ERROR: Could not insert data");
			e.printStackTrace();
		}
	}

	public static void printTable(ResultSet resultSet) {
		try {
			while (resultSet.next()) {
				System.out.printf("%s %s %s %n", resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

/**
 * Provides functionalities to read files and split them. 
 */
class Parse {
	/**
	 * Reads string and splits every time there is a comma `,`.
	 * 
	 * @param text (String): string that needs to be split
	 */
	public static String[] split(String text, String symbol) {
		return text.split(symbol);
	}

	/**
	 * Reads a file line.
	 * Each line is added to an array which is then returned.
	 * 
	 * @param filename (String): name of the to be read
	 * @return (ArrayList): array where all the lines in the file are stored
	 */
	public static ArrayList<String> readFileStore(String filename) {
		try {
			File file = new File(filename);
			Scanner myReader = new Scanner(file);
			ArrayList<String> line = new ArrayList<String>();

			while (myReader.hasNextLine()) {
				// Add the next line into the array
				line.add(myReader.nextLine());
			}
			myReader.close();
			return line;
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generates a description suitable for SQL. 
	 * Each section (entry in array) is added to a string separated by commas and speech marks are added.
	 * @param sections (String[]): array which is changed to description
	 * @return (String): description of the array
	 */
	public static String description(String[] sections) {
		String description = "'";

		for (int j = 0; j < sections.length - 1; j++) {
			description = description + sections[j] + "'";
			description = description + ", '";
		}
		description = description + sections[sections.length - 1] + "'";
		return description;
	}
}

public class App {
	public static void createAirportsTable(Connection connection) {
		String tableDescription = "(airportCode VARCHAR(3) PRIMARY KEY, airportName VARCHAR(100), city VARCHAR(50), state VARCHAR(2));";
		DatabaseManagement.createTable(connection, "airports",tableDescription);
	}

	public static void insertAirportsTable(Connection connection) {
		String file = "src/airport";
		for (int i = 0; i < Parse.readFileStore(file).size(); i++) { // Each line
			String sections[] = Parse.split(Parse.readFileStore(file).get(i), ","); // Each section of a line
			
			//^ Creates a description suitable for SQL
			String description = "'"; 
			for (int j = 0; j < sections.length - 1; j++) {
				description = description + sections[j] + "'";
				description = description + ", '";
			}
			description = description + sections[sections.length - 1] + "'";

			System.out.println(description);
			DatabaseManagement.insert(connection, "airports", description);
			description = ""; // Reset description 
		}
	}
	
	public static void createDelayedFlightsTable(Connection connection) {
		String tableDescription = "(ID INT PRIMARY KEY, Month INT, DayofMonth INT, DayOfWeek INT, DepTime INT, ScheduledDepTime INT, ArrTime INT, ScheduledArrTime INT, UniqueCarrier VARCHAR(2), FlightNum INT, ActualFlightTime INT, ScheduledFlightTime INT, AirTime INT, ArrDelay INT, DepDelay INT, Orig VARCHAR(3), Dest VARCHAR(3), Distance INT, FOREIGN KEY (Orig) REFERENCES airports(airportCode), FOREIGN KEY (Dest) REFERENCES airports(airportCode));";
		DatabaseManagement.createTable(connection, "delayedFlights",tableDescription);
	}

	public static void insertDelayedFlightsTable(Connection connection) {
		String file = "src/delayedFlights";
		for (int i = 0; i < Parse.readFileStore(file).size(); i++) { // Each line
			String sections[] = Parse.split(Parse.readFileStore(file).get(i), ","); // Each section of a line
			
			String description = String.format("%d, %d, %d, %d, %d, %d, %d, %d, '%s', %d, %d, %d, %d, %d, %d, '%s', '%s', %d", 
			Integer.parseInt(sections[0]), Integer.parseInt(sections[1]),Integer.parseInt(sections[2]),Integer.parseInt(sections[3]),Integer.parseInt(sections[4]),Integer.parseInt(sections[5]),Integer.parseInt(sections[6]),Integer.parseInt(sections[7]),sections[8],Integer.parseInt(sections[9]),Integer.parseInt(sections[10]),Integer.parseInt(sections[11]),Integer.parseInt(sections[12]),Integer.parseInt(sections[13]),Integer.parseInt(sections[14]),sections[15],sections[16],Integer.parseInt(sections[17]));

			System.out.println(description);
			DatabaseManagement.insert(connection, "delayedFlights", description);
			description = ""; // Reset description 
		}
	}

	public static void main(String[] argv) {
		Connection connection = DatabaseManagement.establishConnection();
		
		DatabaseManagement.dropTable(connection, "airports");
		DatabaseManagement.dropTable(connection, "delayedFlights");
		
		createAirportsTable(connection);
		insertAirportsTable(connection);

		createDelayedFlightsTable(connection);
		insertDelayedFlightsTable(connection);
	}
}