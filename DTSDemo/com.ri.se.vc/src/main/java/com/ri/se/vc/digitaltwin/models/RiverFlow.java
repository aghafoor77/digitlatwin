package com.ri.se.vc.digitaltwin.models;

public class RiverFlow {

	private String flowID;
	private String inflow;
	private String outflow;
	private String level;
	private String quality;
	private String sessionID;

	public String getFlowID() {
		return flowID;
	}

	public void setFlowID(String flowID) {
		this.flowID = flowID;
	}

	public String getInflow() {
		return inflow;
	}

	public void setInflow(String inflow) {
		this.inflow = inflow;
	}

	public String getOutflow() {
		return outflow;
	}

	public void setOutflow(String outflow) {
		this.outflow = outflow;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
