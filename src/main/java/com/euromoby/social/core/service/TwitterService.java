package com.euromoby.social.core.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.euromoby.social.core.model.TwitterAccount;

@Service
public class TwitterService {

	public List<TwitterAccount> getAccounts() {
		return Arrays.asList(new TwitterAccount());
	}
	
}
