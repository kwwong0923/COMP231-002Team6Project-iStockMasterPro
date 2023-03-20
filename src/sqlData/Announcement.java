package sqlData;

import java.sql.Date;

public class Announcement 
{
	private String announcementId;
	private String content;
	private Date publishDate;
	
	public Announcement (String announcementId, String content, Date publishDate)
	{
		this.announcementId = announcementId;
		this.content = content;
		this.publishDate = publishDate;
	}
	
	// setter, getter
	public String getAnnouncementId() 
	{
		return announcementId;
	}
	
	public void setAnnouncementId(String announcementId) 
	{
		this.announcementId = announcementId;
	}
	
	public String getContent() 
	{
		return content;
	}
	
	public void setContent(String content) 
	{
		this.content = content;
	}
	
	public Date getPublishDate() 
	{
		return publishDate;
	}
	
	public void setPublishDate(Date publishDate) 
	{
		this.publishDate = publishDate;
	}
	
	@Override
	public String toString()
	{
		return this.announcementId + "-" + this.content + "-" + this.publishDate;
	}
	
}
