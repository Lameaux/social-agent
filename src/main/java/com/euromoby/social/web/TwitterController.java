package com.euromoby.social.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.euromoby.social.core.twitter.TwitterManager;

@Controller
public class TwitterController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
	
    @RequestMapping("/twitter")
    public String manageAccounts(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Accounts");
    	
    	model.put("accounts", twitterManager.getAccounts());
    	
    	return "twitter_accounts";
    }    

    @RequestMapping("/twitter/groups")
    public String manageGroups(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Groups");
    	
    	return "twitter_groups";
    }    
    
    
    @RequestMapping("/twitter/add")
    public String addTwitterAccount(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Connect new Twitter account");
    	
    	return "twitter_add";
    }     
    
}
