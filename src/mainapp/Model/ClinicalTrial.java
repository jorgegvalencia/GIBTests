package mainapp.Model;

import java.net.URL;

/**
 * @author jorgegvalencia
 */
public class ClinicalTrial {

    private URL url;
    private String nctId;
    private String title;
    private String briefSummary;
    private String overallStatus;
    private String startDate;
    private String studyType;
    private String studyPop;
    private String samplingMethod;
    private String criteria;
    private String minimumAge;
    private String maximumAge;
    private Gender gender;
    
    public ClinicalTrial() {
    }
    
    public void printCT() {
    	System.out.format(
				"%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n",
				"CT Title",title,
				"NCT ID",nctId,
				"URL",url,
				"Start date",startDate,
				"Study type",studyType,
				"Brief Summary",briefSummary,
				"Criteria", criteria
				);
    }
	
    public URL getUrl() {
		return url;
	}
	public String getNctId() {
		return nctId;
	}
	public String getTitle() {
		return title;
	}
	public String getBriefSummary() {
		return briefSummary;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getStudyType() {
		return studyType;
	}
	public String getStudyPop() {
		return studyPop;
	}
	public String getSamplingMethod() {
		return samplingMethod;
	}
	public String getCriteria() {
		return criteria;
	}
	public String getMinimumAge() {
		return minimumAge;
	}
	public String getMaximumAge() {
		return maximumAge;
	}
	public Gender getGender() {
		return gender;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	public void setNctId(String nctId) {
		this.nctId = nctId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setBriefSummary(String briefSummary) {
		this.briefSummary = briefSummary;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}
	public void setStudyPop(String studyPop) {
		this.studyPop = studyPop;
	}
	public void setSamplingMethod(String samplingMethod) {
		this.samplingMethod = samplingMethod;
	}
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	public void setMinimumAge(String minimumAge) {
		this.minimumAge = minimumAge;
	}
	public void setMaximumAge(String maximumAge) {
		this.maximumAge = maximumAge;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
    
    

}