package sqlData;
import java.sql.Date;


public class Case {
    int caseId;
    Date caseDate;
    String caseType;
    String caseDetail;
    String finished;
    

    public Case(int caseId, Date caseDate, String caseType, String caseDetail, String finished) {
    	this.caseId = caseId;
    	this.caseDate = caseDate;
    	this.caseType = caseType;
    	this.caseDetail = caseDetail;
    	this.finished = finished;
    }
    
    public Case(int caseId, String finished) {
    	this.caseId = caseId;
    	this.finished = finished;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public void setCaseDate(Date caseDate) {
        this.caseDate = caseDate;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public void setCaseDetail(String caseDetail) {
        this.caseDetail = caseDetail;
    }

    public void setfinished(String finished) {
        this.finished = finished;
    }

    public int getCaseId() {
        return this.caseId;
    }

    public Date getCaseDate() {
        return this.caseDate;
    }

    public String getCaseType() {
        return this.caseType;
    }

    public String getCaseDetail() {
        return this.caseDetail;
    }

    public String isFinished() {
        return this.finished;
    }
    
    
    
}
