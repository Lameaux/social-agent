package com.euromoby.social.core.mail.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class MailAccount {

	private Integer id;

    @NotNull(message="Login is empty")
    @Size(min=3, max=255, message="Login should be 3-255 characters")
	private String login;
    
    @NotNull(message="Domain is empty")
    @Size(min=3, max=255, message="Domain should be 3-255 characters")
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
