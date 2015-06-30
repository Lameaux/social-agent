package com.euromoby.social.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import twitter4j.User;
import twitter4j.auth.AccessToken;

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.twitter.model.TwitterGroup;
import com.euromoby.social.web.exception.ResourceNotFoundException;

@Controller
public class TwitterController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
	
	@Autowired
	private TwitterProvider twitterProvider;
	
    @RequestMapping("/twitter")
    public String viewAccounts(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Accounts");
    	
    	Map<Integer, String> groupsMap = new HashMap<Integer, String>();
    	for (TwitterGroup group : twitterManager.getGroups()) {
    		groupsMap.put(group.getId(), group.getTitle());
    	}
    	model.put("groups", groupsMap);     	
    	model.put("twitters", twitterManager.getAccounts());
    	
    	return "twitter_accounts";
    }    

    @RequestMapping(value="/twitter/{id}/profile", method=RequestMethod.GET)
    public String editProfileFrom(ModelMap model, @PathVariable("id") String id) {

    	TwitterAccount twitterAccount = twitterManager.getAccountById(id);
    	if (twitterAccount == null) {
    		throw new ResourceNotFoundException();
    	}
    	
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Profile");    	
    	model.put("twitter", twitterAccount);
    	model.put("groups", twitterManager.getGroups());
    	return "twitter_profile";
    }    

    @RequestMapping(value="/twitter/{id}/profile", method=RequestMethod.POST)
    public String editProfilePost(@Valid @ModelAttribute("twitter") TwitterAccount twitterAccount, BindingResult result, @PathVariable("id") String id, ModelMap model) {
    	TwitterAccount currentTwitterAccount = twitterManager.getAccountById(id);
    	if (currentTwitterAccount == null) {
    		throw new ResourceNotFoundException();
    	}

    	if (!result.hasErrors()) {
    		currentTwitterAccount.setDescription(twitterAccount.getDescription());
    		currentTwitterAccount.setGroups(twitterAccount.getGroups());
    		twitterManager.updateAccount(currentTwitterAccount);
    		return "redirect:/twitter";
    	}

    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Profile");
    	model.put("twitter", twitterAccount);
    	model.put("groups", twitterManager.getGroups());    	
    	return "twitter_profile";
    }    
 
    @RequestMapping(value="/twitter/{id}/statistics")
    public String profileStatistics(ModelMap model, @PathVariable("id") String id) throws Exception {

    	TwitterAccount twitterAccount = twitterManager.getAccountById(id);
    	if (twitterAccount == null) {
    		throw new ResourceNotFoundException();
    	}

    	List<User> friends = twitterProvider.getFriends(twitterAccount);
    	List<User> followers = twitterProvider.getFollowers(twitterAccount);
    	
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Statistics");    	
    	model.put("twitter", twitterAccount);
    	model.put("friends", friends);
    	model.put("followers", followers);
    	
    	return "twitter_statistics";
    }    

    @RequestMapping("/twitter/{screenName}/following/{targetScreenName}/unfollow")
    public String unfollow(ModelMap model, @PathVariable("screenName") String screenName, @PathVariable("targetScreenName") String targetScreenName) throws Exception {
    	TwitterAccount twitterAccount = twitterManager.getAccountByScreenName(screenName);
    	if (twitterAccount == null) {
    		throw new ResourceNotFoundException();
    	}
    	twitterProvider.unfollow(twitterAccount, targetScreenName);
    	return "redirect:/twitter/" + twitterAccount.getId() + "/statistics";
    }    
    
    
    @RequestMapping("/twitter/connect")
    public String connectTwitterAccount(ModelMap model) throws Exception {
    	return "redirect:" + twitterProvider.getAuthorizationUrl();
    }     
    
    @RequestMapping("/twitter/oauth")
    public String oAuthTwitterAccount(ModelMap model, @RequestParam("oauth_token") String oAuthToken, @RequestParam("oauth_verifier") String oAuthVerifier)  throws Exception {
		AccessToken accessToken = twitterProvider.getAccessToken(oAuthToken, oAuthVerifier);
		TwitterAccount twitterAccount = twitterManager.saveAccessToken(accessToken);
    	return "redirect:/twitter/" + twitterAccount.getId() + "/profile";
    }     
    
}
