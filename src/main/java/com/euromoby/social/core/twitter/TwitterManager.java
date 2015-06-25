package com.euromoby.social.core.twitter;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.euromoby.social.core.twitter.model.TwitterAccount;

@Service
public class TwitterManager {

	private List<TwitterAccount> accounts = Arrays.asList(
			new TwitterAccount(1, "Account1"),
			new TwitterAccount(2, "Account2"),
			new TwitterAccount(3, "Account3"));
	
	public List<TwitterAccount> getAccounts() {
		return accounts;
	}
	
}
