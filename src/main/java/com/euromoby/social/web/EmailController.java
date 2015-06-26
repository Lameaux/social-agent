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

import com.euromoby.social.core.mail.MailManager;
import com.euromoby.social.core.mail.model.MailAccount;
import com.euromoby.social.web.exception.ResourceNotFoundException;

@Controller
public class EmailController implements AgentController {

	@Autowired
	private MailManager mailManager;	
	
    @RequestMapping("/email")
    public String email(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Manage Emails");
    	
    	model.put("emails", mailManager.getAccounts());    	
    	
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
    	
    	if (!result.hasErrors()) {
    		mailManager.saveAccount(mailAccount);
    		return "redirect:/email";
    	}
    	
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Create new email");
    	model.put("mail_account", mailAccount);
    	return "email_add";
    }

    @RequestMapping(value="/email/edit/{id}", method=RequestMethod.GET)
    public String editEmailForm(ModelMap model, @PathVariable("id") Integer id) {
    	
    	MailAccount mailAccount = mailManager.findAccount(id);
    	if (mailAccount == null) {
    		throw new ResourceNotFoundException(); 
    	}
    	
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Edit email");
    	model.put("mail_account", mailAccount);
    	return "email_edit";
    }     

    @RequestMapping(value="/email/edit/{id}", method=RequestMethod.POST)
    public String editEmailSubmit(@Valid @ModelAttribute("mail_account") MailAccount mailAccount, BindingResult result, @PathVariable("id") Integer id, ModelMap model) {

    	MailAccount checkMailAccount = mailManager.findAccount(id);
    	if (checkMailAccount == null) {
    		throw new ResourceNotFoundException(); 
    	}    	
    	
    	if (!result.hasErrors()) {
    		mailManager.updateAccount(mailAccount);
    		return "redirect:/email";
    	}
    	
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Edit email");
    	model.put("mail_account", mailAccount);
    	return "email_edit";
    }    
    
    
    @RequestMapping("/email/inbox")
    public String inbox(ModelMap model) {
    	model.put(MENU_ACTIVE, "email");
    	model.put(PAGE_TITLE, "Inbox");
    	
    	model.put("messages", mailManager.getMessages());     	
    	
    	return "email_inbox";
    }     

    
}
