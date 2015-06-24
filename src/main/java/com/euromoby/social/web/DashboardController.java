package com.euromoby.social.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController implements AgentController {
	
    @RequestMapping("/")
    public String dashboard(ModelMap model) {
    	model.put(MENU_ACTIVE, "dashboard");
    	model.put(PAGE_TITLE, "Dashboard");
        return "dashboard";
    }	
    
}
