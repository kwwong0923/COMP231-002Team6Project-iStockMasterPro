package testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import application.DBConnection;
import controllers.CheckInOutController;

public class CheckInAndOutTest {

	@Test
	public void test() throws SQLException 
	{
		DBConnection.connectToDB();
		
		CheckInOutController controller = new CheckInOutController();
		int count = controller.addCheckInOrOutRecord("10021", true);
		assertEquals(1, count);
		
		DBConnection.disconnectToDB();
	}

}
