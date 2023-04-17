package testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import application.DBConnection;
import controllers.AccountantController;

public class AccountantCheckStaffFailedTest {

	@Test
	public void test() throws SQLException 
	{
		DBConnection.connectToDB();

		AccountantController controller = new AccountantController();
		boolean result = controller.checkStaffId("10500");
		assertEquals(true, result);
		
		DBConnection.disconnectToDB();
	}

}
