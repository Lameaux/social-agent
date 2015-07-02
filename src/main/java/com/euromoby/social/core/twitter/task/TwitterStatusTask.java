package com.euromoby.social.core.twitter.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.TwitterException;

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterActionFollow;
import com.euromoby.social.core.twitter.model.TwitterActionStatus;

@Component
public class TwitterStatusTask {

	private static final Logger log = LoggerFactory.getLogger(TwitterStatusTask.class);

	@Autowired
	private TwitterManager twitterManager;
	@Autowired
	private TwitterProvider twitterProvider;

	@Scheduled(fixedDelay = 5000) // 60000
	public void execute() {
		List<TwitterActionStatus> statusActions = twitterManager.getNewStatusActions();
		for (TwitterActionStatus statusAction : statusActions) {
			TwitterAccount twitterAccount = twitterManager.getAccountByScreenName(statusAction.getScreenName());
			if (twitterAccount == null) {
				log.debug("Account {} not found", statusAction.getScreenName());
				statusAction.setStatus(TwitterActionFollow.STATUS_ERROR);
				statusAction.setErrorText("Account not found");
				twitterManager.updateStatusAction(statusAction);
				continue;
			}
			
			try {
				Status status = twitterProvider.status(twitterAccount, statusAction.getMessage());
				log.debug("{} updated status {}", twitterAccount.getScreenName(), status.getId());
				statusAction.setStatus(TwitterActionFollow.STATUS_OK);
			} catch (TwitterException e) {
				statusAction.setStatus(TwitterActionFollow.STATUS_ERROR);
				statusAction.setErrorText(e.getErrorMessage());
			}
			twitterManager.updateStatusAction(statusAction);
		}
	}

}
