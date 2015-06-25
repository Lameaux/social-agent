package com.euromoby.social.core.service;


public interface Service extends Runnable {

	void startService();

	void stopService();

	String getServiceName();

	ServiceState getServiceState();

}
