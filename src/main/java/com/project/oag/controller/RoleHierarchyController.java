package com.project.oag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RoleHierarchyController {

    @GetMapping("/roleHierarchy")
    public ModelAndView roleHierarcy() {
        ModelAndView model = new ModelAndView();
        model.addObject("managerMessage","Manager content available");
        model.addObject("customerMessage","Customer content available");
        model.addObject("artistMessage","Artist content available");
        model.addObject("AdminMessage","Admin content available");
        model.addObject("organizationMessage","Organization content available");
        model.setViewName("roleHierarchy");
        return model;
    }

}
