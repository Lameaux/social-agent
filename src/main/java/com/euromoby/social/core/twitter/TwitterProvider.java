package com.euromoby.social.core.twitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import twitter4j.PagableResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.euromoby.social.core.Config;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.utils.StringUtils;

@Component
public class TwitterProvider {

	private Config config;
	
	private Map<String, String> requestTokens =  Collections.synchronizedMap(new LRUMap<String, String>());
	
	@Autowired
	public TwitterProvider(Config config) {
		this.config = config;
	}

	protected Twitter getTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		//cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(config.getTwitterKey());
		cb.setOAuthConsumerSecret(config.getTwitterSecret());

		if (!StringUtils.nullOrEmpty(config.getProxyHost())) {
			cb.setHttpProxyHost(config.getProxyHost());
			cb.setHttpProxyPort(config.getProxyPort());
		}
		cb.setHttpConnectionTimeout(config.getClientTimeout());
		cb.setHttpReadTimeout(config.getClientTimeout());
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}

	public String getAuthorizationUrl() throws Exception {
		RequestToken requestToken = getTwitter().getOAuthRequestToken();
		requestTokens.put(requestToken.getToken(), requestToken.getTokenSecret());
		return requestToken.getAuthorizationURL();
	}

	public AccessToken getAccessToken(String oauthToken, String oauthVerifier) throws Exception {
		String oauthTokenSecret = requestTokens.get(oauthToken);
		if (StringUtils.nullOrEmpty(oauthTokenSecret)) {
			throw new Exception("Token is invalid");
		}
		RequestToken requestToken = new RequestToken(oauthToken, oauthTokenSecret);
		AccessToken accessToken = getTwitter().getOAuthAccessToken(requestToken, oauthVerifier);
		return accessToken;
	}
	
	public Status sendMessage(TwitterAccount twitterAccount, String text) throws Exception {
		AccessToken accessToken = new AccessToken(twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret(), Long.parseLong(twitterAccount.getId()));
		
		Twitter twitter = getTwitter();
		twitter.setOAuthAccessToken(accessToken);
		
		StatusUpdate statusUpdate = new StatusUpdate(text);
		return twitter.updateStatus(statusUpdate);		
	}
	
	public List<User> getFriends(TwitterAccount twitterAccount) throws Exception {
		AccessToken accessToken = new AccessToken(twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret(), Long.parseLong(twitterAccount.getId()));
		
		Twitter twitter = getTwitter();
		twitter.setOAuthAccessToken(accessToken);
		
		PagableResponseList<User> followingUsers;
		List<User> following = new ArrayList<User>();
		long cursor = -1;
		while (cursor != 0) {
			followingUsers = twitter.getFriendsList(twitterAccount.getScreenName(), cursor, 200); 
			for (User user : followingUsers) {
				following.add(user);
			}
			cursor = followingUsers.getNextCursor();
		} 
		
		return following;		
	}	

	public User follow(TwitterAccount twitterAccount, String screenName) throws Exception {
		AccessToken accessToken = new AccessToken(twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret(), Long.parseLong(twitterAccount.getId()));
		
		Twitter twitter = getTwitter();
		twitter.setOAuthAccessToken(accessToken);
		
		return twitter.createFriendship(screenName);
	}	
	
	public User unfollow(TwitterAccount twitterAccount, String screenName) throws Exception {
		AccessToken accessToken = new AccessToken(twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret(), Long.parseLong(twitterAccount.getId()));
		
		Twitter twitter = getTwitter();
		twitter.setOAuthAccessToken(accessToken);
		
		return twitter.destroyFriendship(screenName);
	}	
	
}
