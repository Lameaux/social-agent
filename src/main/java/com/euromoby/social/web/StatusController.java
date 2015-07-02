package com.euromoby.social.web;

import javax.validation.Valid;

import org.apache.velocity.tools.generic.EscapeTool;
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
import com.euromoby.social.core.twitter.model.TwitterActionStatus;
import com.euromoby.social.web.exception.ResourceNotFoundException;
import com.euromoby.social.web.model.StatusAction;

@Controller
public class StatusController implements AgentController {

	@Autowired
	private TwitterManager twitterManager;
	
	@Autowired
	private TwitterProvider twitterProvider;	
	
    @RequestMapping("/statuses")
    public String statuses(ModelMap model) {
    	model.put("actions", twitterManager.getStatusActions());    	
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "Status Updates");
    	model.put("escape", new EscapeTool());    	
    	return "statuses";
    }     
    
    
    @RequestMapping(value="/statuses/update", method=RequestMethod.GET)
    public String updateStatus(ModelMap model) {
    	model.put("dto", new StatusAction());
    	model.put("groups", twitterManager.getGroups());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "New Status Update");
    	return "statuses_update";
    }     
    
    @RequestMapping(value="/statuses/update", method=RequestMethod.POST)
    public String updateStatusPost(@Valid @ModelAttribute("dto") StatusAction dto, BindingResult result, ModelMap model) {
    	if (!result.hasErrors()) {
    		twitterManager.saveStatusAction(dto);
    		return "redirect:/statuses";
    	}
    	model.put("dto", dto);
    	model.put("groups", twitterManager.getGroups());
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "New Status Update");
    	return "statuses_update";
    }

    @RequestMapping("/statuses/{id}/delete")
    public String deleteStatus(ModelMap model, @PathVariable("id") Integer id) {

    	TwitterActionStatus twitterActionStatus = twitterManager.getStatusActionById(id);
    	if (twitterActionStatus == null) {
    		throw new ResourceNotFoundException();
    	}
    	twitterManager.deleteStatusAction(twitterActionStatus);
    	
    	return "redirect:/statuses";
    }      
    
    
}
