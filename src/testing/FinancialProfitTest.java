package testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import application.DBConnection;
import controllers.FinancialReportController;

public class FinancialProfitTest {

	@Test
	public void test() throws SQLException 
	{
		DBConnection.connectToDB();

		FinancialReportController controller = new FinancialReportController();
		double result = controller.getWholeProfit(3, 2023);
		assertEquals(54787.92, result, 0.001);

		DBConnection.disconnectToDB();
		
	}

}
