package testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import application.DBConnection;
import controllers.AccountantController;

public class AccountantCheckStaffId {

	@Test
	public void test() throws SQLException 
	{
		DBConnection.connectToDB();

		AccountantController controller = new AccountantController();
		boolean result = controller.checkStaffId("10001");
		assertEquals(false, result);
		
		DBConnection.disconnectToDB();
	}

}
