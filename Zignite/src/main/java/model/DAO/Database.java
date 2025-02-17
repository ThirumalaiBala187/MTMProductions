package model.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
	private static final String URL="jdbc:mysql://localhost:3306/Zignite_Learning_Platform";
	private static final String USER="root";
	private static final String PASSWORD="bala@2627";
	private static Connection connection = null;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found. Make sure it's in the classpath!", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}