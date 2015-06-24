package com.euromoby.social.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmailController implements AgentController {
   
    @RequestMapping("/email")
    public String email(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Manage Emails");
    	return "email";
    }     

    @RequestMapping("/email/add")
    public String addNewEmail(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Add new email");
    	return "email_add";
    }     
    
    @RequestMapping("/email/inbox")
    public String inbox(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Inbox");
    	return "email_inbox";
    }     

    
}
