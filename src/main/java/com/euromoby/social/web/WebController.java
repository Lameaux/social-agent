package com.euromoby.social.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/")
    public String list(ModelMap model) {
        return "list";
    }	

    @RequestMapping("/edit")
    public String edit(ModelMap model) {
        return "edit";
    }    
    
}
