package com.euromoby.social.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.euromoby.social.core.service.EmailService;

@Controller
public class EmailController implements AgentController {

	@Autowired
	private EmailService emailService;	
	
    @RequestMapping("/email")
    public String email(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Manage Emails");
    	
    	model.put("emails", emailService.getAccounts());    	
    	
    	return "email";
    }     

    @RequestMapping("/email/add")
    public String addNewEmail(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Create new email");
    	return "email_add";
    }     
    
    @RequestMapping("/email/inbox")
    public String inbox(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Inbox");
    	return "email_inbox";
    }     

    
}
