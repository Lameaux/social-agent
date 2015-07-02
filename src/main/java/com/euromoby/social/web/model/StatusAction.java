package com.euromoby.social.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StatusAction {

	private List<Integer> groups = new ArrayList<Integer>();
	private String screenNames = "";

	@NotNull(message = "Message is empty")
	@Size(min = 3, max = 140, message = "Message should be 3-140 characters")
	private String message = "";

	public List<Integer> getGroups() {
		return groups;
	}

	public void setGroups(List<Integer> groups) {
		this.groups = groups;
	}

	public String getScreenNames() {
		return screenNames;
	}

	public void setScreenNames(String screenNames) {
		this.screenNames = screenNames;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
