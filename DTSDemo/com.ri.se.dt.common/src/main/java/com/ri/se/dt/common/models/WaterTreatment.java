package com.ri.se.dt.common.models;

public class WaterTreatment {

	private String inflow;
	private String waterTreated;
	private String treatmentProcess;
	private String effluent;
	private String residualRemovingPercentage;
	private String totoalDisolvedSolids;
	private String stationId;
	public String getInflow() {
		return inflow;
	}
	public void setInflow(String inflow) {
		this.inflow = inflow;
	}
	public String getWaterTreated() {
		return waterTreated;
	}
	public void setWaterTreated(String waterTreated) {
		this.waterTreated = waterTreated;
	}
	public String getTreatmentProcess() {
		return treatmentProcess;
	}
	public void setTreatmentProcess(String treatmentProcess) {
		this.treatmentProcess = treatmentProcess;
	}
	public String getEffluent() {
		return effluent;
	}
	public void setEffluent(String effluent) {
		this.effluent = effluent;
	}
	public String getResidualRemovingPercentage() {
		return residualRemovingPercentage;
	}
	public void setResidualRemovingPercentage(String residualRemovingPercentage) {
		this.residualRemovingPercentage = residualRemovingPercentage;
	}
	public String getTotoalDisolvedSolids() {
		return totoalDisolvedSolids;
	}
	public void setTotoalDisolvedSolids(String totoalDisolvedSolids) {
		this.totoalDisolvedSolids = totoalDisolvedSolids;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
}
