package com.euromoby.social.core.twitter.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import twitter4j.TwitterException;
import twitter4j.User;

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterActionFollow;

@Component
public class TwitterFollowTask {

	private static final Logger log = LoggerFactory.getLogger(TwitterFollowTask.class);

	@Autowired
	private TwitterManager twitterManager;
	@Autowired
	private TwitterProvider twitterProvider;

	@Scheduled(fixedDelay = 5000)
	public void execute() {
		log.debug("started");
		List<TwitterActionFollow> followActions = twitterManager.getNewFollowActions();
		for (TwitterActionFollow followAction : followActions) {
			TwitterAccount twitterAccount = twitterManager.getAccountByScreenName(followAction.getScreenName());
			if (twitterAccount == null) {
				log.debug("Account {} not found", followAction.getScreenName());
				followAction.setStatus(TwitterActionFollow.STATUS_ERROR);
				followAction.setErrorText("Account not found");
				twitterManager.updateFollowingAction(followAction);
				continue;
			}
			
			try {
				User user = twitterProvider.follow(twitterAccount, followAction.getTargetScreenName());
				log.debug("{} started following {}", twitterAccount.getScreenName(), user.getScreenName());
				followAction.setStatus(TwitterActionFollow.STATUS_OK);
			} catch (TwitterException e) {
				followAction.setStatus(TwitterActionFollow.STATUS_ERROR);
				followAction.setErrorText(e.getErrorMessage());
			}
			twitterManager.updateFollowingAction(followAction);
		}
	}

}
