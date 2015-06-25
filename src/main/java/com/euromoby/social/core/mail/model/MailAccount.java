package com.euromoby.social.core.mail.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class MailAccount {

	private Integer id;

    @NotNull(message="Login is empty")
    @Min(value=3, message="Login should be at least 3 characters")
	private String login;
    
    @NotNull(message="Domain is empty")
    @Min(value=3, message="Domain should be at least 3 characters")
    private String domain;
    
	private boolean active;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
