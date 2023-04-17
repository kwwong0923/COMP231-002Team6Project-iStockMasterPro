package testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import application.DBConnection;
import controllers.CheckInOutController;

public class CheckOutTest {

	@Test
	public void test() throws SQLException 
	{
		DBConnection.connectToDB();
		
		CheckInOutController controller = new CheckInOutController();
		int count = controller.addCheckInOrOutRecord("10021", false);
		assertTrue(count>0);
		
		DBConnection.disconnectToDB();
	}

}
