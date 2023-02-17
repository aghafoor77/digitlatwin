package org.acreo.common.entities;

public class NeghibourCO {

	
	public long resourceId;
	public String name;
	public String mobile;
	public String phone;
	
	public NeghibourCO() {
		// TODO Auto-generated constructor stub
	}

	
	public NeghibourCO(long resourceId, String name, String mobile, String phone) {
		super();
		this.resourceId = resourceId;
		this.name = name;
		this.mobile = mobile;
		this.phone = phone;
	}


	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
	

}
