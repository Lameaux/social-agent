package com.euromoby.social.core;

import java.util.List;

public class Config {

	private List<String> autorunServices;
	private String mailHost;
	private String mailStoragePath;
	private int serverTimeout;

	public List<String> getAutorunServices() {
		return autorunServices;
	}

	public void setAutorunServices(List<String> autorunServices) {
		this.autorunServices = autorunServices;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailStoragePath() {
		return mailStoragePath;
	}

	public void setMailStoragePath(String mailStoragePath) {
		this.mailStoragePath = mailStoragePath;
	}

	public int getServerTimeout() {
		return serverTimeout;
	}

	public void setServerTimeout(int serverTimeout) {
		this.serverTimeout = serverTimeout;
	}

}
