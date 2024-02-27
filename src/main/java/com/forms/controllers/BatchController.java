package com.forms.controllers;

import com.forms.dao.PersonDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-batch")
public class BatchController {

    private final PersonDAO personDAO;

    @Autowired
    public BatchController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String index(){

     return "batch/index";
    }

    @GetMapping("/without")
    public String withoutBAtch(){
        personDAO.tesMultipleUpdate();
        return "redirect:/";
    }
    @GetMapping("/with")
    public String testBatchUpdate(){

        personDAO.testBatchUpdate();
        return "redirect:/";
    }
}
