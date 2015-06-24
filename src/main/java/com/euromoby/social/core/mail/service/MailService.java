package com.euromoby.social.core.mail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.euromoby.social.core.mail.dao.MailAccountDao;
import com.euromoby.social.core.mail.model.MailAccount;

@Service
public class MailService {

	@Autowired
	private MailAccountDao accountDao;
	
	@Transactional(readOnly=true)
	public List<MailAccount> getAccounts() {
		return accountDao.findAll();
	}
	
}
