package edu.odu.cs.gold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @GetMapping({"","/","/index"})
    public String index() {
        return "home/index";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings/index";
    }

    @RequestMapping("/login")
    public String login() {
        return "home/login";
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "home/login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

}
