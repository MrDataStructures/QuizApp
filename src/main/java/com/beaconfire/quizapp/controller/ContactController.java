package com.beaconfire.quizapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact"; // Render contact.jsp
    }

    @PostMapping("/contact")
    public String handleContactForm(@RequestParam("message") String message, Model model) {
        model.addAttribute("messageSent", true);
        return "contact";
    }
}
