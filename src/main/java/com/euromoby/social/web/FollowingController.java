package com.euromoby.social.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.web.model.FollowingAction;

@Controller
public class FollowingController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
	
	@Autowired
	private TwitterProvider twitterProvider;	
	
    @RequestMapping("/followings")
    public String followings(ModelMap model) {
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "Followings");
    	return "followings";
    }     

    @RequestMapping(value="/followings/new", method=RequestMethod.GET)
    public String newFollowingGet(ModelMap model) {
    	model.put("dto", new FollowingAction());
    	model.put("groups", twitterManager.getGroups());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "New followings");
    	return "followings_new";
    }     
    
    @RequestMapping(value="/followings/new", method=RequestMethod.POST)
    public String newFollowingPost(@Valid @ModelAttribute("dto") FollowingAction dto, BindingResult result, ModelMap model) {

    	if (!result.hasErrors()) {
    		
    	}
    	
    	model.put("dto", dto);
    	model.put("groups", twitterManager.getGroups());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "New followings");
    	return "followings_new";
    }
    
}
