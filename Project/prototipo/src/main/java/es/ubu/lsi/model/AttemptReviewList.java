package es.ubu.lsi.model;

import java.util.List;

public class AttemptReviewList {
	private String grade;
	private List<Attempt> attempts;
    private List<Object> additionaldata;
    private List<Question> questions;
    private List<Object> warnings;
	
    
    public String getGrade() {
		return grade;
	}
	
    public void setGrade(String grade) {
		this.grade = grade;
	}
	
    public List<Attempt> getAttempts() {
		return attempts;
	}
	
    public void setAttempts(List<Attempt> attempts) {
		this.attempts = attempts;
	}
	
    public List<Object> getAdditionaldata() {
		return additionaldata;
	}
	
    public void setAdditionaldata(List<Object> additionaldata) {
		this.additionaldata = additionaldata;
	}
	
    public List<Question> getQuestions() {
		return questions;
	}
	
    public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
    public List<Object> getWarnings() {
		return warnings;
	}
	
    public void setWarnings(List<Object> warnings) {
		this.warnings = warnings;
	}

}
