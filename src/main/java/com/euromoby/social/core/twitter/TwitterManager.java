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
import com.euromoby.social.core.twitter.dao.TwitterActionFollowDao;
import com.euromoby.social.core.twitter.dao.TwitterActionStatusDao;
import com.euromoby.social.core.twitter.dao.TwitterGroupDao;
import com.euromoby.social.core.twitter.dao.TwitterMessageDao;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterActionFollow;
import com.euromoby.social.core.twitter.model.TwitterActionStatus;
import com.euromoby.social.core.twitter.model.TwitterGroup;
import com.euromoby.social.core.twitter.model.TwitterMessage;
import com.euromoby.social.core.utils.StringUtils;
import com.euromoby.social.web.model.FollowingAction;
import com.euromoby.social.web.model.StatusAction;

@Component
public class TwitterManager {

	@Autowired
	private TwitterAccountDao twitterAccountDao;
	@Autowired
	private TwitterMessageDao twitterMessageDao;
	@Autowired
	private TwitterGroupDao twitterGroupDao;	
	@Autowired
	private TwitterActionFollowDao twitterActionFollowDao;	
	@Autowired
	private TwitterActionStatusDao twitterActionStatusDao;	

	@Transactional(readOnly=true)	
	public List<TwitterAccount> getAccounts() {
		return twitterAccountDao.findAll();
	}	

	@Transactional(readOnly=true)	
	public List<TwitterAccount> getAccountsByGroupId(Integer groupId) {
		return twitterAccountDao.findByGroupId(groupId);
	}	
	
	
	@Transactional(readOnly=true)
	public TwitterAccount getAccountById(String id) {
		return twitterAccountDao.findById(id);
	}	

	@Transactional(readOnly=true)
	public TwitterAccount getAccountByScreenName(String screenName) {
		return twitterAccountDao.findByScreenName(screenName);
	}	


	@Transactional(readOnly=true)	
	public List<TwitterGroup> getGroups() {
		return twitterGroupDao.findAll();
	}	

	@Transactional(readOnly=true)	
	public List<TwitterGroup> getGroupsWithBroadcastRss() {
		return twitterGroupDao.findAllWithBroadcastRss();
	}	
	
	@Transactional(readOnly=true)
	public TwitterGroup getGroupById(Integer id) {
		return twitterGroupDao.findById(id);
	}	

	@Transactional(readOnly=true)
	public List<TwitterActionFollow> getFollowActions() {
		return twitterActionFollowDao.findAll();
	}	

	@Transactional(readOnly=true)
	public TwitterActionFollow getFollowActionById(Integer id) {
		return twitterActionFollowDao.findById(id);
	}	
	
	@Transactional(readOnly=true)
	public List<TwitterActionFollow> getNewFollowActions() {
		return twitterActionFollowDao.findAllNew();
	}	

	@Transactional(readOnly=true)
	public List<TwitterActionStatus> getStatusActions() {
		return twitterActionStatusDao.findAll();
	}	

	@Transactional(readOnly=true)
	public TwitterActionStatus getStatusActionById(Integer id) {
		return twitterActionStatusDao.findById(id);
	}	
	
	@Transactional(readOnly=true)
	public List<TwitterActionStatus> getNewStatusActions() {
		return twitterActionStatusDao.findAllNew();
	}	

	@Transactional(readOnly=true)
	public int countStatusActionsByScreenNameAndText(String screenName, String message) {
		return twitterActionStatusDao.countByScreenNameAndText(screenName, message);
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
	public void deleteFollowAction(TwitterActionFollow twitterActionFollow) {
		twitterActionFollowDao.delete(twitterActionFollow);
	}	
	
	@Transactional	
	public void updateFollowingAction(TwitterActionFollow followAction) {
		twitterActionFollowDao.update(followAction);
	}
	
	@Transactional	
	public void saveFollowingAction(FollowingAction followingAction) {
		Set<String> sourceSet = getScreenNames(followingAction.getScreenNames(), followingAction.getGroups());
		Set<String> targetSet = getScreenNames(followingAction.getTargetScreenNames(), followingAction.getTargetGroups());
		
		for (String source : sourceSet) {
			for (String target : targetSet) {
				if (source.equals(target)) {
					continue;
				}
				TwitterActionFollow actionFollow = new TwitterActionFollow();
				actionFollow.setScreenName(source);
				actionFollow.setTargetScreenName(target);
				actionFollow.setStatus(TwitterActionFollow.STATUS_NEW);
				twitterActionFollowDao.save(actionFollow);
			}
		}
		
	}

	@Transactional
	public void deleteStatusAction(TwitterActionStatus twitterActionStatus) {
		twitterActionStatusDao.delete(twitterActionStatus);
	}	
	
	@Transactional	
	public void updateStatusAction(TwitterActionStatus twitterActionStatus) {
		twitterActionStatusDao.update(twitterActionStatus);
	}

	@Transactional	
	public void saveStatusAction(TwitterActionStatus twitterActionStatus) {
		twitterActionStatusDao.save(twitterActionStatus);
	}	
	
	@Transactional
	public void saveStatusAction(StatusAction statusAction) {
		Set<String> sourceSet = getScreenNames(statusAction.getScreenNames(), statusAction.getGroups());
		for (String source : sourceSet) {
			TwitterActionStatus actionStatus = new TwitterActionStatus();
			actionStatus.setScreenName(source);
			actionStatus.setMessage(statusAction.getMessage());
			actionStatus.setStatus(TwitterActionStatus.STATUS_NEW);
			twitterActionStatusDao.save(actionStatus);
		}

	}	
	
	private Set<String> getScreenNames(String screenNamesString, List<Integer> groupIds) {
		Set<String> set = new HashSet<String>();
		String sourceScreenNamesString = screenNamesString.toLowerCase().trim();
		if (!StringUtils.nullOrEmpty(sourceScreenNamesString)) {
			for (String sourceScreenName : sourceScreenNamesString.split("\\s+")) {
				set.add(sourceScreenName);
			}
		}
		for (Integer groupId : groupIds) {
			List<TwitterAccount> accounts = twitterAccountDao.findByGroupId(groupId);
			for (TwitterAccount account : accounts) {
				set.add(account.getScreenName().toLowerCase());
			}
		}		
		return set;
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
