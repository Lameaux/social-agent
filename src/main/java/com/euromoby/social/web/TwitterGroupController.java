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
import com.euromoby.social.core.twitter.model.TwitterGroup;
import com.euromoby.social.web.exception.ResourceNotFoundException;

@Controller
public class TwitterGroupController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
    
    @RequestMapping("/twitter/groups")
    public String manageGroups(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Twitter Groups");
    	model.put("groups", twitterManager.getGroups());
    	return "twitter_groups";
    }    
    

    @RequestMapping(value="/twitter/groups/add", method=RequestMethod.GET)
    public String addNewGroupForm(ModelMap model) {
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Create new Twitter group");
    	model.put("group", new TwitterGroup());
    	return "twitter_groups_add";
    }     

    @RequestMapping(value="/twitter/groups/add", method=RequestMethod.POST)
    public String addNewGroupSubmit(@Valid @ModelAttribute("group") TwitterGroup twitterGroup, BindingResult result, ModelMap model) {
    	
    	if (!result.hasErrors()) {
    		twitterManager.saveGroup(twitterGroup);
    		return "redirect:/twitter/groups";
    	}
    	
    	model.put(MENU_ACTIVE, "group");
    	model.put(PAGE_TITLE, "Create new Twitter group");
    	model.put("group", twitterGroup);
    	return "twitter_groups_add";
    }    

    @RequestMapping(value="/twitter/groups/{id}/edit", method=RequestMethod.GET)
    public String editGroupForm(ModelMap model, @PathVariable("id") Integer id) {
    	
    	TwitterGroup twitterGroup = twitterManager.getGroupById(id);
    	if (twitterGroup == null) {
    		throw new ResourceNotFoundException(); 
    	}
    	
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Edit Twitter Group");
    	model.put("group", twitterGroup);
    	return "twitter_groups_edit";
    }     

    @RequestMapping(value="/twitter/groups/{id}/edit", method=RequestMethod.POST)
    public String editGroupSubmit(@Valid @ModelAttribute("group") TwitterGroup twitterGroup, BindingResult result, @PathVariable("id") Integer id, ModelMap model) {

    	TwitterGroup checkTwitterGroup = twitterManager.getGroupById(id);
    	if (checkTwitterGroup == null) {
    		throw new ResourceNotFoundException(); 
    	}   	
    	
    	if (!result.hasErrors()) {
    		checkTwitterGroup.setTitle(twitterGroup.getTitle());
    		twitterManager.updateGroup(checkTwitterGroup);
    		return "redirect:/twitter/groups";
    	}
    	
    	model.put(MENU_ACTIVE, "twitter");
    	model.put(PAGE_TITLE, "Edit Twitter Group");
    	model.put("group", twitterGroup);
    	return "twitter_groups_edit";
    }      
    
    
}
