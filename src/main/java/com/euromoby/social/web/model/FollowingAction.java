package com.euromoby.social.web.model;

import java.util.ArrayList;
import java.util.List;

public class FollowingAction {

	private List<Integer> groups = new ArrayList<Integer>();
	private String screenNames = "";

	private List<Integer> targetGroups = new ArrayList<Integer>();
	private String targetScreenNames = "";

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

	public List<Integer> getTargetGroups() {
		return targetGroups;
	}

	public void setTargetGroups(List<Integer> targetGroups) {
		this.targetGroups = targetGroups;
	}

	public String getTargetScreenNames() {
		return targetScreenNames;
	}

	public void setTargetScreenNames(String targetScreenNames) {
		this.targetScreenNames = targetScreenNames;
	}

}
