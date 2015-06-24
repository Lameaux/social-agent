package com.euromoby.social.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MessageController implements AgentController {
   
    @RequestMapping("/messages")
    public String list(ModelMap model) {
    	model.put(MENU_ACTIVE, "messages");
    	model.put(PAGE_TITLE, "Messages");
    	return "messages";
    }     

    @RequestMapping("/messages/new")
    public String sendNewMessage(ModelMap model) {
    	model.put(MENU_ACTIVE, "messages");
    	model.put(PAGE_TITLE, "Send new message");
    	return "messages_new";
    }     
    
    
}
