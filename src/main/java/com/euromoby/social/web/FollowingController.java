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

import com.euromoby.social.core.twitter.TwitterManager;
import com.euromoby.social.core.twitter.TwitterProvider;
import com.euromoby.social.core.twitter.model.TwitterActionFollow;
import com.euromoby.social.web.exception.ResourceNotFoundException;
import com.euromoby.social.web.model.FollowingAction;

@Controller
public class FollowingController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
	
	@Autowired
	private TwitterProvider twitterProvider;	
	
    @RequestMapping("/followings")
    public String followings(ModelMap model) {
    	model.put("actions", twitterManager.getFollowActions());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "Follow Actions");
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
    		twitterManager.saveFollowingAction(dto);
    		return "redirect:/followings";
    	}
    	model.put("dto", dto);
    	model.put("groups", twitterManager.getGroups());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "New followings");
    	return "followings_new";
    }

    @RequestMapping("/followings/{id}/delete")
    public String deleteFollowingAction(ModelMap model, @PathVariable("id") Integer id) {

    	TwitterActionFollow twitterActionFollow = twitterManager.getFollowActionById(id);
    	if (twitterActionFollow == null) {
    		throw new ResourceNotFoundException();
    	}
    	twitterManager.deleteFollowAction(twitterActionFollow);
    	
    	return "redirect:/followings";
    }     
    
}
