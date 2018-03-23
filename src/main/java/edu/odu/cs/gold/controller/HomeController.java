package edu.odu.cs.gold.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/login")
    public String login() {
        return "home/login";
    }

    // Login form with error
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "home/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
