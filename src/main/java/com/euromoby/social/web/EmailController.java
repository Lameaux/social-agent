package com.euromoby.social.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.euromoby.social.core.mail.model.MailAccount;
import com.euromoby.social.core.mail.service.MailService;

@Controller
public class EmailController implements AgentController {

	@Autowired
	private MailService emailService;	
	
    @RequestMapping("/email")
    public String email(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Manage Emails");
    	
    	model.put("emails", emailService.getAccounts());    	
    	
    	return "email";
    }     

    @RequestMapping(value="/email/add", method=RequestMethod.GET)
    public String addNewEmailForm(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Create new email");
    	model.put("mail_account", new MailAccount());
    	return "email_add";
    }     

    @RequestMapping(value="/email/add", method=RequestMethod.POST)
    public String addNewEmailSubmit(@Valid @ModelAttribute("mail_account") MailAccount mailAccount, BindingResult result, ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Create new email");
    	model.put("mail_account", mailAccount);
    	return "email_add";
    }
    
    @RequestMapping("/email/inbox")
    public String inbox(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Inbox");
    	return "email_inbox";
    }     

    
}
