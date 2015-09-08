package opennlp;

import java.net.URL;

public class ClinicalTrial {
	private URL url;
	private String nct_id;
	private String title;
	private String brief_summary;
	private Status overall_status;
	private String start_date;
	private String study_type;
	// eligibility
	private String study_pop;
	private String sampling_method;
	private String inc_criteria;
	private String exc_criteria;
	private Gender gender;
	private String minimum_age;
	private String maximum_age;
	
	public ClinicalTrial(){	}

	
	public void printCT(){
		System.out.format(
				"%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n"
				+ "%17s:\t%s\n",
				"CT Title",title,
				"NCT ID",nct_id,
				"URL",url,
				"Start date",start_date,
				"Study type",study_type,
				"Brief Summary",brief_summary,
				"Criteria", exc_criteria
				);
	}
	
	public URL getUrl() {
		return url;
	}

	public String getNct_id() {
		return nct_id;
	}

	public String getTitle() {
		return title;
	}

	public String getBrief_summary() {
		return brief_summary;
	}

	public Status getOverall_status() {
		return overall_status;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getStudy_type() {
		return study_type;
	}

	public String getStudy_pop() {
		return study_pop;
	}

	public String getSampling_method() {
		return sampling_method;
	}

	public String getInc_criteria() {
		return inc_criteria;
	}

	public String getExc_criteria() {
		return exc_criteria;
	}

	public Gender getGender() {
		return gender;
	}

	public String getMinimum_age() {
		return minimum_age;
	}

	public String getMaximum_age() {
		return maximum_age;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public void setNct_id(String nct_id) {
		this.nct_id = nct_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBrief_summary(String brief_summary) {
		this.brief_summary = brief_summary;
	}

	public void setOverall_status(Status overall_status) {
		this.overall_status = overall_status;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setStudy_type(String study_type) {
		this.study_type = study_type;
	}

	public void setStudy_pop(String study_pop) {
		this.study_pop = study_pop;
	}

	public void setSampling_method(String sampling_method) {
		this.sampling_method = sampling_method;
	}

	public void setInc_criteria(String inc_criteria) {
		this.inc_criteria = inc_criteria;
	}

	public void setExc_criteria(String exc_criteria) {
		this.exc_criteria = exc_criteria;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public void setMinimum_age(String minimum_age) {
		this.minimum_age = minimum_age;
	}

	public void setMaximum_age(String maximum_age) {
		this.maximum_age = maximum_age;
	}
	
	
	
}
