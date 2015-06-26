package com.euromoby.social.core.mail;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.euromoby.social.core.mail.dao.MailAccountDao;
import com.euromoby.social.core.mail.dao.MailMessageDao;
import com.euromoby.social.core.mail.model.MailAccount;
import com.euromoby.social.core.mail.model.MailMessage;
import com.euromoby.social.core.model.Tuple;


@Component
public class MailManager {

	private static final Logger log = LoggerFactory.getLogger(MailManager.class);

	private MailAccountDao mailAccountDao;
	private MailMessageDao mailMessageDao;
	private MailFileProvider mailFileProvider;

	@Autowired
	public MailManager(MailAccountDao mailAccountDao, MailMessageDao mailMessageDao, MailFileProvider mailFileProvider) {
		this.mailAccountDao = mailAccountDao;
		this.mailMessageDao = mailMessageDao;
		this.mailFileProvider = mailFileProvider;
	}

	@Transactional(readOnly=true)	
	public MailAccount findAccount(Integer id) {
			return mailAccountDao.findById(id);
	}	
	
	@Transactional(readOnly=true)	
	public MailAccount findAccount(Tuple<String, String> loginDomain) {
			return mailAccountDao.findByLoginAndDomain(loginDomain.getFirst(), loginDomain.getSecond());
	}

	@Transactional(readOnly=true)	
	public MailMessage findMessage(Integer accountId, Integer id) {
		return mailMessageDao.findByAccountIdAndId(accountId, id);
	}	

	@Transactional	
	public void saveAccount(MailAccount mailAccount) {
		mailAccountDao.save(mailAccount);
	}

	@Transactional	
	public void updateAccount(MailAccount mailAccount) {
		mailAccountDao.update(mailAccount);
	}	
	
	@Transactional(readOnly=true)	
	public List<MailAccount> getAccounts() {
		return mailAccountDao.findAll();
	}

	@Transactional(readOnly=true)	
	public List<MailAccount> getActiveAccounts() {
		return mailAccountDao.findActive();
	}	
	
	@Transactional(readOnly=true)	
	public List<MailMessage> getMessages() {
		return mailMessageDao.findAll();
	}	
	
	@Transactional(readOnly=true)	
	public List<MailMessage> getMessages(Integer accountId) {
		return mailMessageDao.findByAccountId(accountId);
	}

	public void deleteMessage(Integer accountId, Integer messageId) {
		MailAccount account = mailAccountDao.findById(accountId);
		if (account == null) {
			log.error("Account {} not found", accountId);
			return;
		}

		mailMessageDao.deleteById(messageId);
		try {
			File targetFile = mailFileProvider.getMessageFile(Tuple.of(account.getLogin(), account.getDomain()), messageId);
			targetFile.delete();
		} catch (Exception e) {
			log.error("Internal Error", e);
		}		
		
	}
	
	public void saveMessage(MailSession mailSession) {
		Tuple<String, String> recipient = mailSession.getRecipient();
		
		MailMessageFileReader messageReader = new MailMessageFileReader(mailSession.getTempFile());
		MimeMessageParser parser = messageReader.parseMessage();
		if (parser == null) {
			log.error("Error parsing message");
			return;
		}		
		
		try {
			MailAccount account = mailAccountDao.findByLoginAndDomain(recipient.getFirst(), recipient.getSecond());
			MailMessage mailMessage = new MailMessage();
			mailMessage.setAccountId(account.getId());
			mailMessage.setSender(mailSession.getSender().joinString("@"));
			mailMessage.setSize(mailSession.getRealMailSize());
			mailMessage.setCreated(new Date());
			mailMessage.setSubject(parser.getSubject());
			mailMessageDao.save(mailMessage);
			
			File targetFile = mailFileProvider.getMessageFile(recipient, mailMessage.getId());
			FileUtils.copyFile(mailSession.getTempFile(), targetFile);
		} catch (Exception e) {
			log.error("Internal Error", e);
		}

	}
	
	@Transactional(readOnly=true)		
	public File getMessageFile(Integer accountId, Integer messageId) {
		
		MailAccount account = mailAccountDao.findById(accountId);
		if (account == null) {
			return null;
		}
		
		try {
			return mailFileProvider.getMessageFile(Tuple.of(account.getLogin(), account.getDomain()), messageId);
		} catch (Exception e) {
			return null;
		}
	}
	
}
