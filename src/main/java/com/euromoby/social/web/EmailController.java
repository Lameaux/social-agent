package com.euromoby.social.web;

import java.io.File;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.mail.util.MimeMessageParser;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.euromoby.social.core.mail.MailManager;
import com.euromoby.social.core.mail.MailMessageFileReader;
import com.euromoby.social.core.mail.model.MailAccount;
import com.euromoby.social.core.mail.model.MailMessage;
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
    public String inboxAll(ModelMap model) {
    	return inbox(model, null);
    }
    
    @RequestMapping("/email/inbox/{id}")
    public String inbox(ModelMap model, @PathVariable("id") Integer id) {
    	model.put(MENU_ACTIVE, "email");
    	
    	List<MailMessage> messages;
    	if (id == null) {
    		messages = mailManager.getMessages();
        	model.put(PAGE_TITLE, "Inbox");
    	} else {
        	MailAccount selectedAccount = mailManager.findAccount(id);
        	if (selectedAccount == null) {
        		throw new ResourceNotFoundException(); 
        	}     		
        	model.put("selectedAccount", selectedAccount);
        	model.put(PAGE_TITLE, selectedAccount.getLogin() + "@" + selectedAccount.getDomain() + "- Inbox");        	
    		messages = mailManager.getMessages(id);
    	}
    	model.put("messages", messages);     	

    	List<MailAccount> accounts = mailManager.getActiveAccounts();
    	model.put("accounts", accounts);
    	
    	model.put("date", new DateTool());
    	model.put("math", new MathTool());  
    	
    	return "email_inbox";
    }     

    @RequestMapping("/email/inbox/view/{accountId}/{messageId}")
    public String viewInboxMessage(ModelMap model, @PathVariable("accountId") Integer accountId, @PathVariable("messageId") Integer messageId) {
    	model.put(MENU_ACTIVE, "email");

    	MailMessage message = mailManager.findMessage(accountId, messageId);
    	if (message == null) {
    		throw new ResourceNotFoundException();
    	}
    	
    	model.put(PAGE_TITLE, "Message " + messageId + " - Inbox");    	
    	
    	model.put("message", message);     	

    	model.put("date", new DateTool());
    	model.put("math", new MathTool());
    	
		File messageFile = mailManager.getMessageFile(accountId, messageId);
		if (messageFile == null) {
			throw new ResourceNotFoundException();
		}
		
		
		MailMessageFileReader messageReader = new MailMessageFileReader(messageFile);
		MimeMessageParser parser = messageReader.parseMessage();
		if (parser == null) {
			model.put("message_body", "Unable to parse file");
		} else {
			if (parser.hasHtmlContent()) {
				model.put("message_body", parser.getHtmlContent());
			} else {
				model.put("message_body", "<pre>" + parser.getPlainContent() + "</pre>");	
			}
		}
    	
    	return "email_view";
    }    
 
    @RequestMapping("/email/inbox/delete/{accountId}/{messageId}")
    public String deleteInboxMessage(ModelMap model, @PathVariable("accountId") Integer accountId, @PathVariable("messageId") Integer messageId) {

    	MailMessage message = mailManager.findMessage(accountId, messageId);
    	if (message == null) {
    		throw new ResourceNotFoundException();
    	}

    	mailManager.deleteMessage(accountId, messageId);
    	
    	return "redirect:/email/inbox/" + accountId;
    }    
    
}
