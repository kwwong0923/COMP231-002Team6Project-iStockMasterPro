package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AccountantCheckStaffFailedTest.class, AccountantCheckStaffId.class, CheckInAndOutTest.class,
		CheckOutTest.class, FinancialProfitTest.class })
public class AllTests {

}
