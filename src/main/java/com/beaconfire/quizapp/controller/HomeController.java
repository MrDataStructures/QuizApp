package com.beaconfire.quizapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import com.beaconfire.quizapp.service.CategoryService;
import com.beaconfire.quizapp.model.Category;
import com.beaconfire.quizapp.model.Quiz;
import com.beaconfire.quizapp.service.QuizService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;
    private final QuizService quizService;

    @Autowired
    public HomeController(CategoryService categoryService, QuizService quizService) {
        this.categoryService = categoryService;
        this.quizService = quizService;
    }

    @GetMapping("/home")
    public String showHomePage(HttpSession session, Model model) {
        // Get the logged-in user's username
        String username = (String) session.getAttribute("username");

        // Retrieve categories for quiz selection
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        // Retrieve recent quizzes taken by the user
        List<Quiz> recentQuizzes = quizService.getAllQuizzesForUsername(username);
        Collections.reverse(recentQuizzes);
        model.addAttribute("recentQuizzes", recentQuizzes);

        return "home";
    }
}

