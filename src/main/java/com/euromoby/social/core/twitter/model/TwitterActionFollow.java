package com.euromoby.social.core.twitter.model;

public class TwitterActionFollow {

	public static final int STATUS_NEW = 0;
	public static final int STATUS_OK = 1;
	public static final int STATUS_ERROR = 2;

	private Integer id;
	private String screenName;
	private String targetScreenName;
	private int status = STATUS_NEW;
	private String errorText;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getTargetScreenName() {
		return targetScreenName;
	}

	public void setTargetScreenName(String targetScreenName) {
		this.targetScreenName = targetScreenName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

}
