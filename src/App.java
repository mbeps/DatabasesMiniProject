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
			statement.execute(String.format("DROP TABLE IF EXISTS %s", table)); 
			System.out.printf("SUCCESS: Table Dropped \n%s\n\n", table);
		} catch (SQLException e) {
			System.out.printf("ERROR: Table Not Dropped \n%s\n\n", table);
			e.printStackTrace();
		} 
	}

	/**
	 * Creates a new table if it does not exist already in the database.
	 * @param connection (Connection): connection to database
	 * @param table (String): name of the table
	 * @param description (String): layout of the table in SQL 
	 */
	public static void createTable(Connection connection, String table, String description) {
		try {
			Statement statement = connection.createStatement();
			statement.execute(String.format("CREATE TABLE IF NOT EXISTS %s %s", table, description)); 
			System.out.printf("SUCCESS: Table Created \n%s %s \n\n", table, description);
		} catch (SQLException e) {
			System.out.printf("ERROR: Table Not Created \n%s %s \n\n", table, description);
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
			System.out.printf("SUCCESS: Statement Executed \n%s\n\n", query);
			return resultSet;
		} catch (SQLException e) {
			System.out.printf("ERROR: Statement Not Executed \n%s\n\n", query);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Inserts data into desired table. 
	 * @param connection (Connection): connection to the database
	 * @param table (String): name of the table where data needs to inserted
	 * @param data (String): data to be inserted separated by ','
	 */
	public static void insert(Connection connection, String table, String data) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(String.format("INSERT INTO %s VALUES (%s);", table, data));
			System.out.printf("SUCCESS: Data Inserted\n%s\n\n", data);
		} catch (SQLException e) {
			System.out.printf("ERROR: Data Not Inserted\n%s\n\n", data);
			e.printStackTrace();
		}
	}

	/**
	 * Returns the number of columns from a given result set. 
	 * If the number of columns cannot be determined, -1 is returned.
	 * @param resultSet (ResultSet): result set from which the number of columns is computed
	 * @return (int): the number of columns
	 */
	public static int getColumnCount(ResultSet resultSet) {
		try {
			ResultSetMetaData metadata = resultSet.getMetaData();
			int columns = metadata.getColumnCount();
			return columns;
		} catch (SQLException e) {
			System.out.println("ERROR: Could Not Determine Number of Columns");
			e.printStackTrace();
			return -1; // Invalid 
		}
	}

	/**
	 * Prints the data in a table given a query. 
	 * Data is printed row by row. 
	 * @param connection (Connection): connection to database
	 * @param query (String): query for which table is printed
	 */
	public static void printTableQuery(Connection connection, String query) {
		try {
			ResultSet resultSet = executeQuery(connection, query);
			
			String data = "";
			while (resultSet.next()) {
				for (int i = 1; i <= getColumnCount(resultSet); i++) {
					data = data + resultSet.getString(i) + ", ";
				}
				data = data + "\n"; // New line for each row
			}
			System.out.println(data);
			resultSet.close();
			System.out.println("SUCCESS: Table Printed");
			// return data // All rows are concatenated into 1 variable incase it ever needs to be returned
		} catch (SQLException e) {
			System.out.println("ERROR: Table Not Printed");
			e.printStackTrace();
		}
	}

	/**
	 * Prints the data for a given table. 
	 * Data is printed row by row. 
	 * Methods depends on {@link #printTableQuery(Connection, String)}.
	 * @param connection (Connection): connection to the database
	 * @param table (String): name of the table
	 */
	public static void printTable(Connection connection, String table) {
		String query = "SELECT * FROM " + table;
		printTableQuery(connection, query);
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

			// System.out.println(description);
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

			// System.out.println(description);
			DatabaseManagement.insert(connection, "delayedFlights", description);
			description = ""; // Reset description 
		}
	}

	public static void main(String[] argv) {
		Connection connection = DatabaseManagement.establishConnection();
		
		// DatabaseManagement.dropTable(connection, "delayedFlights");
		// DatabaseManagement.dropTable(connection, "airports");
		
		// createAirportsTable(connection);
		// createDelayedFlightsTable(connection);

		// insertAirportsTable(connection);
		// insertDelayedFlightsTable(connection);

		DatabaseManagement.printTable(connection, "airports");
	}
}