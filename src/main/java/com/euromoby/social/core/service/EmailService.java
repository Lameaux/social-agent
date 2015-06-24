package com.euromoby.social.core.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.euromoby.social.core.model.EmailAccount;

@Service
public class EmailService {

	private List<EmailAccount> accounts = Arrays.asList(
			new EmailAccount("email1@server.com"),
			new EmailAccount("email2@server.com"),
			new EmailAccount("email3@server.com"));
	
	public List<EmailAccount> getAccounts() {
		return accounts;
	}
	
}
