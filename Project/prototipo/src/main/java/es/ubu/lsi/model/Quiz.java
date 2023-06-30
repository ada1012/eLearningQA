package es.ubu.lsi.model;

import java.util.List;

public class Quiz {
	private int id;
	private int coursemodule;
	private int course;
	private String name;
	private String intro;
	private int introformat;
	private List<File> introfiles;
	private int section;
	private boolean visible;
	private int groupmode;
	private int groupingid;
	private String lang;
	private int timeopen;
	private int timeclose;
	private int timelimit;
	private String overduehandling;
	private int graceperiod;
	private String preferredbehaviour;
	private int canredoquestions;
	private int attempts;
	private int attemptonlast;
	private int grademethod;
	private int decimalpoints;
	private int questiondecimalpoints;
	private int reviewattempt;
	private int reviewcorrectness;
	private int reviewmarks;
	private int reviewspecificfeedback;
	private int reviewgeneralfeedback;
	private int reviewrightanswer;
	private int reviewoverallfeedback;
	private int questionsperpage;
	private String navmethod;
	private int shuffleanswers;
	private double sumgrades;
	private double grade;
	private int timecreated;
	private int timemodified;
	private String password;
	private String subnet;
	private String browsersecurity;
	private int delay1;
	private int delay2;
	private int showuserpicture;
	private int showblocks;
	private int completionattemptsexhausted;
	private int completionpass;
	private int allowofflineattempts;
	private int autosaveperiod;
	private int hasfeedback;
	private int hasquestions;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCoursemodule() {
		return coursemodule;
	}
	public void setCoursemodule(int coursemodule) {
		this.coursemodule = coursemodule;
	}
	public int getCourse() {
		return course;
	}
	public void setCourse(int course) {
		this.course = course;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public int getIntroformat() {
		return introformat;
	}
	public void setIntroformat(int introformat) {
		this.introformat = introformat;
	}
	public List<File> getIntrofiles() {
		return introfiles;
	}
	public void setIntrofiles(List<File> introfiles) {
		this.introfiles = introfiles;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	
	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public int getGroupmode() {
		return groupmode;
	}
	public void setGroupmode(int groupmode) {
		this.groupmode = groupmode;
	}
	public int getGroupingid() {
		return groupingid;
	}
	public void setGroupingid(int groupingid) {
		this.groupingid = groupingid;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public int getTimeopen() {
		return timeopen;
	}
	public void setTimeopen(int timeopen) {
		this.timeopen = timeopen;
	}
	public int getTimeclose() {
		return timeclose;
	}
	public void setTimeclose(int timeclose) {
		this.timeclose = timeclose;
	}
	public int getTimelimit() {
		return timelimit;
	}
	public void setTimelimit(int timelimit) {
		this.timelimit = timelimit;
	}
	public String getOverduehandling() {
		return overduehandling;
	}
	public void setOverduehandling(String overduehandling) {
		this.overduehandling = overduehandling;
	}
	public int getGraceperiod() {
		return graceperiod;
	}
	public void setGraceperiod(int graceperiod) {
		this.graceperiod = graceperiod;
	}
	public String getPreferredbehaviour() {
		return preferredbehaviour;
	}
	public void setPreferredbehaviour(String preferredbehaviour) {
		this.preferredbehaviour = preferredbehaviour;
	}
	public int getCanredoquestions() {
		return canredoquestions;
	}
	public void setCanredoquestions(int canredoquestions) {
		this.canredoquestions = canredoquestions;
	}
	public int getAttempts() {
		return attempts;
	}
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	public int getAttemptonlast() {
		return attemptonlast;
	}
	public void setAttemptonlast(int attemptonlast) {
		this.attemptonlast = attemptonlast;
	}
	public int getGrademethod() {
		return grademethod;
	}
	public void setGrademethod(int grademethod) {
		this.grademethod = grademethod;
	}
	public int getDecimalpoints() {
		return decimalpoints;
	}
	public void setDecimalpoints(int decimalpoints) {
		this.decimalpoints = decimalpoints;
	}
	public int getQuestiondecimalpoints() {
		return questiondecimalpoints;
	}
	public void setQuestiondecimalpoints(int questiondecimalpoints) {
		this.questiondecimalpoints = questiondecimalpoints;
	}
	public int getReviewattempt() {
		return reviewattempt;
	}
	public void setReviewattempt(int reviewattempt) {
		this.reviewattempt = reviewattempt;
	}
	public int getReviewcorrectness() {
		return reviewcorrectness;
	}
	public void setReviewcorrectness(int reviewcorrectness) {
		this.reviewcorrectness = reviewcorrectness;
	}
	public int getReviewmarks() {
		return reviewmarks;
	}
	public void setReviewmarks(int reviewmarks) {
		this.reviewmarks = reviewmarks;
	}
	public int getReviewspecificfeedback() {
		return reviewspecificfeedback;
	}
	public void setReviewspecificfeedback(int reviewspecificfeedback) {
		this.reviewspecificfeedback = reviewspecificfeedback;
	}
	public int getReviewgeneralfeedback() {
		return reviewgeneralfeedback;
	}
	public void setReviewgeneralfeedback(int reviewgeneralfeedback) {
		this.reviewgeneralfeedback = reviewgeneralfeedback;
	}
	public int getReviewrightanswer() {
		return reviewrightanswer;
	}
	public void setReviewrightanswer(int reviewrightanswer) {
		this.reviewrightanswer = reviewrightanswer;
	}
	public int getReviewoverallfeedback() {
		return reviewoverallfeedback;
	}
	public void setReviewoverallfeedback(int reviewoverallfeedback) {
		this.reviewoverallfeedback = reviewoverallfeedback;
	}
	public int getQuestionsperpage() {
		return questionsperpage;
	}
	public void setQuestionsperpage(int questionsperpage) {
		this.questionsperpage = questionsperpage;
	}
	public String getNavmethod() {
		return navmethod;
	}
	public void setNavmethod(String navmethod) {
		this.navmethod = navmethod;
	}
	public int getShuffleanswers() {
		return shuffleanswers;
	}
	public void setShuffleanswers(int shuffleanswers) {
		this.shuffleanswers = shuffleanswers;
	}
	public double getSumgrades() {
		return sumgrades;
	}
	public void setSumgrades(double sumgrades) {
		this.sumgrades = sumgrades;
	}
	public double getGrade() {
		return grade;
	}
	public void setGrade(double grade) {
		this.grade = grade;
	}
	public int getTimecreated() {
		return timecreated;
	}
	public void setTimecreated(int timecreated) {
		this.timecreated = timecreated;
	}
	public int getTimemodified() {
		return timemodified;
	}
	public void setTimemodified(int timemodified) {
		this.timemodified = timemodified;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubnet() {
		return subnet;
	}
	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}
	public String getBrowsersecurity() {
		return browsersecurity;
	}
	public void setBrowsersecurity(String browsersecurity) {
		this.browsersecurity = browsersecurity;
	}
	public int getDelay1() {
		return delay1;
	}
	public void setDelay1(int delay1) {
		this.delay1 = delay1;
	}
	public int getDelay2() {
		return delay2;
	}
	public void setDelay2(int delay2) {
		this.delay2 = delay2;
	}
	public int getShowuserpicture() {
		return showuserpicture;
	}
	public void setShowuserpicture(int showuserpicture) {
		this.showuserpicture = showuserpicture;
	}
	public int getShowblocks() {
		return showblocks;
	}
	public void setShowblocks(int showblocks) {
		this.showblocks = showblocks;
	}
	public int getCompletionattemptsexhausted() {
		return completionattemptsexhausted;
	}
	public void setCompletionattemptsexhausted(int completionattemptsexhausted) {
		this.completionattemptsexhausted = completionattemptsexhausted;
	}
	public int getCompletionpass() {
		return completionpass;
	}
	public void setCompletionpass(int completionpass) {
		this.completionpass = completionpass;
	}
	public int getAllowofflineattempts() {
		return allowofflineattempts;
	}
	public void setAllowofflineattempts(int allowofflineattempts) {
		this.allowofflineattempts = allowofflineattempts;
	}
	public int getAutosaveperiod() {
		return autosaveperiod;
	}
	public void setAutosaveperiod(int autosaveperiod) {
		this.autosaveperiod = autosaveperiod;
	}
	public int getHasfeedback() {
		return hasfeedback;
	}
	public void setHasfeedback(int hasfeedback) {
		this.hasfeedback = hasfeedback;
	}
	public int getHasquestions() {
		return hasquestions;
	}
	public void setHasquestions(int hasquestions) {
		this.hasquestions = hasquestions;
	}
	

}
