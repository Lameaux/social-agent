package com.euromoby.social.core.twitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import twitter4j.auth.AccessToken;

import com.euromoby.social.core.twitter.dao.TwitterAccountDao;
import com.euromoby.social.core.twitter.dao.TwitterGroupDao;
import com.euromoby.social.core.twitter.dao.TwitterMessageDao;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterGroup;
import com.euromoby.social.core.twitter.model.TwitterMessage;
import com.euromoby.social.web.model.FollowingAction;

@Component
public class TwitterManager {

	@Autowired
	private TwitterAccountDao twitterAccountDao;
	@Autowired
	private TwitterMessageDao twitterMessageDao;
	@Autowired
	private TwitterGroupDao twitterGroupDao;	
	

	@Transactional(readOnly=true)	
	public List<TwitterAccount> getAccounts() {
		return twitterAccountDao.findAll();
	}	

	@Transactional(readOnly=true)	
	public List<TwitterGroup> getGroups() {
		return twitterGroupDao.findAll();
	}	
	
	@Transactional(readOnly=true)
	public TwitterAccount getAccountById(String id) {
		return twitterAccountDao.findById(id);
	}	

	@Transactional(readOnly=true)
	public TwitterGroup getGroupById(Integer id) {
		return twitterGroupDao.findById(id);
	}	
	
	@Transactional
	public void updateAccount(TwitterAccount twitterAccount) {
		twitterAccountDao.update(twitterAccount);
	}

	@Transactional
	public void saveGroup(TwitterGroup twitterGroup) {
		twitterGroupDao.save(twitterGroup);
	}	

	@Transactional
	public void updateGroup(TwitterGroup twitterGroup) {
		twitterGroupDao.update(twitterGroup);
	}	

	@Transactional
	public void deleteGroup(TwitterGroup twitterGroup) {
		twitterGroupDao.delete(twitterGroup);
	}	
	
	@Transactional	
	public void saveFollowingAction(FollowingAction followingAction) {
		
		Set<String> sourceScreenNames = new HashSet<String>();
		Set<String> targetScreenNames = new HashSet<String>();
		
	}
	
	
	@Transactional	
	public TwitterAccount saveAccessToken(AccessToken accessToken) {
		String userId = String.valueOf(accessToken.getUserId());
		TwitterAccount account = twitterAccountDao.findById(userId);
		if (account == null) {
			account = new TwitterAccount();
			account.setId(userId);
			account.setScreenName(accessToken.getScreenName().toLowerCase());
			account.setDescription("");
			account.setAccessToken(accessToken.getToken());
			account.setAccessTokenSecret(accessToken.getTokenSecret());
			twitterAccountDao.save(account);
		} else {
			account.setAccessToken(accessToken.getToken());
			account.setAccessTokenSecret(accessToken.getTokenSecret());
			twitterAccountDao.update(account);
		}
		return account;
	}

	@Transactional
	public void scheduleMessageSending(List<String> accountIds, String messageText) {
		List<TwitterMessage> messages = new ArrayList<TwitterMessage>(accountIds.size());
		for (String accountId : accountIds) {
			TwitterMessage twitterMessage = new TwitterMessage();
			twitterMessage.setAccountId(accountId);
			twitterMessage.setMessageText(messageText);
			messages.add(twitterMessage);
		}
		twitterMessageDao.saveAll(messages);
	}
	
	@Transactional(readOnly=true)
	public List<TwitterMessage> getScheduledMessages(int limit) {
		return twitterMessageDao.findAll(limit);
	}
	
	@Transactional
	public void deleteScheduledMessages(List<TwitterMessage> twitterMessages) {
		twitterMessageDao.deleteAll(twitterMessages);
	}
	
}
