package com.euromoby.social.web;

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

import twitter4j.auth.AccessToken;

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterAccount;
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
    	
    	model.put("twitters", twitterManager.getAccounts());
    	
    	return "twitter_accounts";
    }    

    @RequestMapping(value="/twitter/{id}/profile", method=RequestMethod.GET)
    public String editProfileFrom(ModelMap model, @PathVariable("id") String id) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Profile");
    	
    	TwitterAccount twitterAccount = twitterManager.getAccountById(id);
    	if (twitterAccount == null) {
    		throw new ResourceNotFoundException();
    	}
    	
    	model.put("twitter", twitterAccount);
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
    		twitterManager.updateAccount(currentTwitterAccount);
    		return "redirect:/twitter";
    	}

    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Profile");
    	model.put("twitter", twitterAccount);
    	return "twitter_profile";
    }    
    
    @RequestMapping("/twitter/groups")
    public String manageGroups(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Groups");
    	
    	return "twitter_groups";
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
