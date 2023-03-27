package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
	// static member variable
	// can be access directly
	private static final String DRIVER = "oracle.jdbc.OracleDriver";
	private static final String DATABASE_URL = "jdbc:oracle:thin:@199.212.26.208:1521:SQLD";
	private static final String USERNAME = "COMP231_W23_hl_6";
	private static final String PASSWORD = "password";
	public static Connection connection;
	
	// defend instance of DBConnection class
	private DBConnection() {}
	
	public static void connectToDB()
	{
		// loading driver
		try 
		{
			Class.forName(DRIVER);
			System.out.println("Driver loaded");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("Error - Failed to load the driver");
			e.printStackTrace();
		}
		
		// connecting database
		try 
		{
			connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
			System.out.println("Database connected");
		}
		catch (SQLException e)
		{
			System.out.println("Error - Failed to connect the database");
			e.printStackTrace();
		}
	}
	
	public static void disconnectToDB()
	{
		try
		{
			connection.close();
			if (connection.isClosed())
			{
				System.out.println("Database disconnected");
			}
			else
			{
				System.out.println("Error - Failed to disconnect database");
			}
		}
		catch (NullPointerException npe)
		{
			System.out.println("Error - Failed to disconnect database");
			npe.printStackTrace();
		}
		catch (SQLException e)
		{
			System.out.println("Error - Failed to disconnect database");
			e.printStackTrace();
		}
	}
}
