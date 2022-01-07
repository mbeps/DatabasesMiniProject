import java.sql.*;


class TestDatabaseManagement {
	public static void databaseManagementTest() {
		Connection connection = DatabaseManagement.establishConnection();
		
		databaseManagementTestSelect(connection);
		databaseManagementTestCreate(connection);
		databaseManagementTestDrop(connection);
		databaseManagementTestInsert(connection);
	}

	public static void databaseManagementTestSelect(Connection connection) {
		//^ Return from Database
		String query = "SELECT * FROM branch;";
		ResultSet resultSet = DatabaseManagement.executeQuery(connection, query);
		
		//^ Printing Table 
		DatabaseManagement.printTable(resultSet);
	}

	public static void databaseManagementTestCreate(Connection connection) {
		System.out.println("Creating Table");
		DatabaseManagement.createTable(connection, "test (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
		DatabaseManagement.createTable(connection, "new (id SERIAL NOT NULL PRIMARY KEY, DATA VARCHAR(20));");
	}

	public static void databaseManagementTestDrop(Connection connection) {
		System.out.println("Dropping Table");
		DatabaseManagement.dropTable(connection, "test");
	}

	public static void databaseManagementTestInsert(Connection connection) {
		System.out.println("Inserting Data");
		String dataDescription = "1, 'test'";
		DatabaseManagement.insert(connection, "new", dataDescription);
	}

}

public class TestCode {
	public static void main(String[] args) {
		
	}	
}
