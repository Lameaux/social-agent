package com.euromoby.social.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatusController implements AgentController {
   
    @RequestMapping("/statuses")
    public String statuses(ModelMap model) {
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "Status Updates");
    	return "statuses";
    }     

    @RequestMapping("/statuses/update")
    public String updateStatus(ModelMap model) {
    	model.put(MENU_ACTIVE, "actions");
    	model.put(PAGE_TITLE, "Update status");
    	return "statuses_update";
    }     
    
    
}
