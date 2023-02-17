package com.ri.se.citizen;

import java.util.ArrayList;

public class RequestedAttributes {

	private ArrayList<String> requestedAtt;
	private String requester;
	public ArrayList<String> getRequestedAtt() {
		return requestedAtt;
	}
	public void setRequestedAtt(ArrayList<String> requestedAtt) {
		this.requestedAtt = requestedAtt;
	}
	public String getRequester() {
		return requester;
	}
	public void setRequester(String requester) {
		this.requester = requester;
	}
}
