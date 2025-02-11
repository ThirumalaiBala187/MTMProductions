package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
	private static final String URL="jdbc:mysql://localhost:3306/ZigniteLearningPlatform";
	private static final String user="root";
	private static final String password="password";
	private static Connection connection=null;
	public static Connection getConnection() throws SQLException {
		if(connection==null || connection.isClosed()) {
			connection=DriverManager.getConnection(URL);
		}
		return connection;
	}
}