package es.ubu.lsi.model;

import java.util.List;

public class MoodleObject {
    private String intro;
    private int introformat;
    private List<Introfile> introfiles;
    private int id;
    private int coursemodule;
    private int course;
    private String name;
    private int timemodified;
    private int section;
    private boolean visible;
    private int groupmode;
    private int groupingid;
    private Object overrideby;
    private boolean valueused;
    private boolean hascompletion;
    private boolean isautomatic;
    private boolean istrackeduser;
    private boolean uservisible;
    private List<Detail> details;
    private String filename;
    private String filepath;
    private int filesize;
    private String fileurl;
    private String mimetype;
    private boolean isexternalfile;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAssignmentid(int id) {
        setId(id);
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

    public List<Introfile> getIntrofiles() {
        return introfiles;
    }

    public void setIntrofiles(List<Introfile> introfiles) {
        this.introfiles = introfiles;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getTimemodified() {
        return timemodified;
    }

    public void setTimemodified(int timemodified) {
        this.timemodified = timemodified;
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

    public Object getOverrideby() {
        return overrideby;
    }

    public void setOverrideby(Object overrideby) {
        this.overrideby = overrideby;
    }

    public boolean isValueused() {
        return valueused;
    }

    public void setValueused(boolean valueused) {
        this.valueused = valueused;
    }

    public boolean isHascompletion() {
        return hascompletion;
    }

    public void setHascompletion(boolean hascompletion) {
        this.hascompletion = hascompletion;
    }

    public boolean isIsautomatic() {
        return isautomatic;
    }

    public void setIsautomatic(boolean isautomatic) {
        this.isautomatic = isautomatic;
    }

    public boolean isIstrackeduser() {
        return istrackeduser;
    }

    public void setIstrackeduser(boolean istrackeduser) {
        this.istrackeduser = istrackeduser;
    }

    public boolean isUservisible() {
        return uservisible;
    }

    public void setUservisible(boolean uservisible) {
        this.uservisible = uservisible;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public boolean isIsexternalfile() {
        return isexternalfile;
    }

    public void setIsexternalfile(boolean isexternalfile) {
        this.isexternalfile = isexternalfile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
