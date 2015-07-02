package com.euromoby.social.core.rss.task;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.velocity.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.euromoby.social.core.http.HttpClientProvider;
import com.euromoby.social.core.rss.RSSFeedParser;
import com.euromoby.social.core.rss.model.Feed;
import com.euromoby.social.core.rss.model.FeedMessage;
import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterActionStatus;
import com.euromoby.social.core.twitter.model.TwitterGroup;

@Component
public class RssBroadcastTask {

	private static final Logger log = LoggerFactory.getLogger(RssBroadcastTask.class);

	public static final int TWITTER_LIMIT = 140;
	
	@Autowired
	private TwitterManager twitterManager;
	@Autowired
	private TwitterProvider twitterProvider;
	@Autowired
	private HttpClientProvider httpClientProvider;

	@Scheduled(fixedDelay = 5000) // 600000
	public void execute() {
		List<TwitterGroup> twitterGroups = twitterManager.getGroupsWithBroadcastRss();
		for (TwitterGroup twitterGroup : twitterGroups) {

			try {
				String rssContent = loadUrl(twitterGroup.getRssUrl());
				
				RSSFeedParser rssParser = new RSSFeedParser();
				Feed feed = rssParser.readFeed(rssContent);
				List<FeedMessage> feedMessages = feed.getMessages();
				if (feedMessages.isEmpty()) {
					continue;
				}
				FeedMessage feedMessage = feedMessages.get(0);
				
				String statusText = createTweetText(feedMessage);
				log.info(statusText);
				
				List<TwitterAccount> twitterAccounts = twitterManager.getAccountsByGroupId(twitterGroup.getId());
				for (TwitterAccount twitterAccount : twitterAccounts) {
					
					// check duplicate tweets
					int duplicates = twitterManager.countStatusActionsByScreenNameAndText(twitterAccount.getScreenName(), statusText);
					if (duplicates > 0) {
						continue;
					}
					
					TwitterActionStatus twitterActionStatus = new TwitterActionStatus();
					twitterActionStatus.setScreenName(twitterAccount.getScreenName());
					twitterActionStatus.setMessage(statusText);
					twitterManager.saveStatusAction(twitterActionStatus);
				}
				
				
				
			} catch (Exception e) {
				log.error("Error processing for group " + twitterGroup.getRssUrl(), e);
				continue;
			}

		}
	}

	private String loadUrl(String url) throws Exception {

		HttpGet request = new HttpGet(url);
		RequestConfig.Builder requestConfigBuilder = httpClientProvider.createRequestConfigBuilder();
		request.setConfig(requestConfigBuilder.build());
		CloseableHttpResponse response = httpClientProvider.executeRequest(request);
		try {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
				EntityUtils.consumeQuietly(response.getEntity());
				throw new Exception(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
			}

			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity);
			EntityUtils.consumeQuietly(entity);
			return content;
		} finally {
			response.close();
		}
	}

	private String createTweetText(FeedMessage feedMessage) {
		String url = StringUtils.nullTrim(feedMessage.getLink());
		String title = StringUtils.nullTrim(feedMessage.getTitle());
		if (url.length() > TWITTER_LIMIT) {
			return limitLength(title, TWITTER_LIMIT);
		}
		int titleLimit = TWITTER_LIMIT - 1 /*space*/ - url.length();
		return limitLength(title, titleLimit) + " " + url;
	}
	
	private String limitLength(String s, int limit) {
		if (s.length() <= limit) {
			return s;
		}
		return s.substring(0, limit-1);
	}
	
}
