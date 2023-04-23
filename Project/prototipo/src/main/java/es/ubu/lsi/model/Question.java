package es.ubu.lsi.model;

import java.util.List;

public class Question {
	private int slot;
	private String type;
	private int page;
	private String html;
	private List<ResponseFileArea> responseFileAreas;
	private int sequencecheck;
	private int lastactiontime;
	private boolean hasautosavedstep;
	private boolean flagged;
	private int number;
	private String state;
	private String status;
	private boolean blockedbyprevious;
	private String mark;
	private double maxmark;
	private String settings;
	
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public String getHtml() {
		return html;
	}
	
	public void setHtml(String html) {
		this.html = html;
	}
	
	public List<ResponseFileArea> getResponseFileAreas() {
		return responseFileAreas;
	}
	
	public void setResponseFileAreas(List<ResponseFileArea> responseFileAreas) {
		this.responseFileAreas = responseFileAreas;
	}
	
	public int getSequencecheck() {
		return sequencecheck;
	}
	
	public void setSequencecheck(int sequencecheck) {
		this.sequencecheck = sequencecheck;
	}
	
	public int getLastactiontime() {
		return lastactiontime;
	}
	
	public void setLastactiontime(int lastactiontime) {
		this.lastactiontime = lastactiontime;
	}
	
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public boolean isHasautosavedstep() {
		return hasautosavedstep;
	}

	public void setHasautosavedstep(boolean hasautosavedstep) {
		this.hasautosavedstep = hasautosavedstep;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	public boolean isBlockedbyprevious() {
		return blockedbyprevious;
	}

	public void setBlockedbyprevious(boolean blockedbyprevious) {
		this.blockedbyprevious = blockedbyprevious;
	}

	public String getMark() {
		return mark;
	}
	
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	public double getMaxmark() {
		return maxmark;
	}
	
	public void setMaxmark(double maxmark) {
		this.maxmark = maxmark;
	}
	
	public String getSettings() {
		return settings;
	}
	
	public void setSettings(String settings) {
		this.settings = settings;
	}
	
	
	
}
