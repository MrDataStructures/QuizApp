package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHomeController {

    @GetMapping("/admin/home")
    public String showAdminHomePage(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/user/login";
        }

        User user = (User) session.getAttribute("user");
        model.addAttribute("username", user.getUsername());
        return "adminHome";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.isIs_admin();
    }
}