import java.sql.*;


class TestDatabaseManagement {
	public static void test() {
		Connection connection = DatabaseManagement.establishConnection();
		
		select(connection);
		create(connection);
		drop(connection);
		insert(connection);
	}

	public static void select(Connection connection) {
		//^ Return from Database
		String query = "SELECT * FROM branch;";
		ResultSet resultSet = DatabaseManagement.executeQuery(connection, query);
		
		//^ Printing Table 
		// DatabaseManagement.printTable(resultSet);
	}

	public static void create(Connection connection) {
		System.out.println("Creating Table");
		DatabaseManagement.createTable(connection, "test"," (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
		DatabaseManagement.createTable(connection, "new", " (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
	}

	public static void drop(Connection connection) {
		System.out.println("Dropping Table");
		DatabaseManagement.dropTable(connection, "test");
	}

	public static void insert(Connection connection) {
		System.out.println("Inserting Data");
		String dataDescription = "1, 'test'";
		DatabaseManagement.insert(connection, "new", dataDescription);
		dataDescription = "2, 'new test'";
		DatabaseManagement.insert(connection, "new", dataDescription);
	}

}

public class TestCode {
	public static void main(String[] args) {
	}	
}
