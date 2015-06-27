package com.euromoby.social.core.twitter.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TwitterGroup {

	private Integer id;
	@NotNull
	@Size(min = 1, max = 255, message = "Title should be 1-255 chars")
	private String title;
	private List<String> accounts = new ArrayList<String>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<String> accounts) {
		this.accounts = accounts;
	}

}
