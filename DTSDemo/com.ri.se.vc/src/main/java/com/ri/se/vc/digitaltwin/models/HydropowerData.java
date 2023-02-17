package com.ri.se.vc.digitaltwin.models;

public class HydropowerData {
	private String reservoirLevel;
	private String precipitation;
	private String temperature;
	private String windSpeed;
	private String windDirection;
	private String energyProduced;
	private String stationID;
	public String getReservoirLevel() {
		return reservoirLevel;
	}
	public void setReservoirLevel(String reservoirLevel) {
		this.reservoirLevel = reservoirLevel;
	}
	public String getPrecipitation() {
		return precipitation;
	}
	public void setPrecipitation(String precipitation) {
		this.precipitation = precipitation;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public String getWindDirection() {
		return windDirection;
	}
	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}
	public String getEnergyProduced() {
		return energyProduced;
	}
	public void setEnergyProduced(String energyProduced) {
		this.energyProduced = energyProduced;
	}
	public String getStationID() {
		return stationID;
	}
	public void setStationID(String stationID) {
		this.stationID = stationID;
	}
	
}
