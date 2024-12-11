package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.User;
import com.beaconfire.quizapp.service.UserService;
import com.beaconfire.quizapp.controller.request_body.RegisterRequestBody;
import com.beaconfire.quizapp.controller.request_body.LoginRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    //@Autowired
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("username", user.getUsername());
        return "home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerRequestBody", new RegisterRequestBody());
        return "register"; // This should map to your register.jsp file
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequestBody registerRequestBody, RedirectAttributes redirectAttributes) {
        User user = new User();
        user.setUsername(registerRequestBody.getUsername());
        user.setEmail(registerRequestBody.getEmail());
        user.setPassword(registerRequestBody.getPassword());
        user.setFirstname(registerRequestBody.getFirstname());
        user.setLastname(registerRequestBody.getLastname());
        user.setIs_active(true);

        boolean registrationSuccess = userService.registerUser(user);

        if (registrationSuccess) {
            return "redirect:/user/login"; // Redirect to login page if registration is successful
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed. Username or email may already be taken.");
            return "redirect:/user/register"; // Redirect back to the register page with error message
        }
    }

    // Show the login page
    @GetMapping("/login")
    public String showLoginPage(HttpSession session) {
        return (session.getAttribute("user") != null) ? "redirect:/user/home" : "login";
    }

    // Handle login form submission
    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequestBody loginRequestBody,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {

        Optional<User> user = userService.loginUser(loginRequestBody.getUsername(), loginRequestBody.getPassword());

        if (user.isPresent()) {
            if (!user.get().isIs_active()) {
                return "suspended"; // Display a suspended message page
            }

            HttpSession newSession = request.getSession(true);
            newSession.setAttribute("user", user.get());
            newSession.setAttribute("username", user.get().getUsername());

            if (user.get().isIs_admin()) {
                return "redirect:/admin/home"; //admin home page
            } else {
                return "redirect:/home"; //user home page
            }
        } else {
            //invalid credentials
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password. Try again");
            return "redirect:/user/login"; //Redirect back to login page on failure
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }
}