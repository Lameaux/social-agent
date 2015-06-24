package com.euromoby.social.core.model;

public class TwitterAccount {

	private int id;
	private String screenName;
	private String secret1;
	private String secret2;

	public TwitterAccount(int id, String screenName) {
		this.id = id;
		this.screenName = screenName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

}
