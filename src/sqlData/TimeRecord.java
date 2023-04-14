package sqlData;

import java.sql.Date;

public class TimeRecord 
{
	private String staffId;
	private String staffName;
	private Date workingDate;
	private String checkInTime;
	private String checkOutTime;
	
	public TimeRecord (String staffId, String staffName, Date workingDate, String checkInTime, String checkOutTime)
	{
		this.staffId = staffId;
		this.staffName = staffName;
		this.workingDate = workingDate;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public Date getWorkingDate() {
		return workingDate;
	}

	public void setWorkingDate(Date workingDate) {
		this.workingDate = workingDate;
	}

	public String getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}

	public String getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
	@Override
	public String toString()
	{
		return this.staffId + "-" + this.staffName + "-" + this.workingDate + "-" + this.checkInTime + "-" + this.checkOutTime;
	}

}
