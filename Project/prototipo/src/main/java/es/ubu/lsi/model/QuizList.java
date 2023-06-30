package es.ubu.lsi.model;

import java.util.List;

public class QuizList {
	private List<Quiz> quizzes;
	private List<Object> warnings;
	
	
	public List<Quiz> getQuizzes() {
		return quizzes;
	}
	
	public void setQuizzes(List<Quiz> quizzes) {
		this.quizzes = quizzes;
	}
	
	public List<Object> getWarnings() {
		return warnings;
	}
	
	public void setWarnings(List<Object> warnings) {
		this.warnings = warnings;
	}
	

}
