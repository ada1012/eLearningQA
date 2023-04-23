package es.ubu.lsi.model;

public class File {
	private String filename;
    private String filepath;
    private int filesize;
    private String fileurl;
    private int timemodified;
    private String mimetype;
    private boolean isexternalfile;
    private String repositorytype;
	
    
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

	public String getRepositorytype() {
		return repositorytype;
	}
	
    public void setRepositorytype(String repositorytype) {
		this.repositorytype = repositorytype;
	}
    
}
