package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.User;
import com.beaconfire.quizapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin/user-management")
public class AdminUserController {

    private final UserService userService;

    @Autowired
    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserManagementPage(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 5;
        List<User> users = userService.getNonAdminUsers(page, pageSize);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        return "userManagement";
    }

    @PostMapping("/toggle-status")
    public String toggleUserStatus(@RequestParam("userId") int userId, @RequestParam("isActive") boolean isActive) {
        if (isActive) {
            userService.suspendUserById(userId);
        } else {
            userService.activateUserById(userId);
        }
        return "redirect:/admin/user-management";
    }
}
