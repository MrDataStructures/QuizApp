package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.Quiz;
import com.mysql.cj.conf.ConnectionUrl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.beaconfire.quizapp.service.UserService;
import com.beaconfire.quizapp.service.CategoryService;
import com.beaconfire.quizapp.service.QuizService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import com.beaconfire.quizapp.model.User;
import com.beaconfire.quizapp.model.Category;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminManagementController {

    private final UserService userService;
    private final QuizService quizService;
    private final CategoryService categoryService;

    @Autowired
    public AdminManagementController(UserService userService, QuizService quizService, CategoryService categoryService) {
        this.userService = userService;
        this.quizService = quizService;
        this.categoryService = categoryService;
    }

    @GetMapping("/quiz-result-management")
    public String showQuizResultManagementPage(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(required = false) Integer categoryId,
                                               @RequestParam(required = false) Integer userId,
                                               Model model) {
        int pageSize = 5;
        List<Map<String, Object>> quizResults = quizService.getPaginatedQuizResults(page, pageSize, categoryId, userId);
        List<Category> categories = categoryService.getAllCategories();
        List<User> users = userService.getAllUsers(); // This method should exclude admins if needed

        model.addAttribute("quizResults", quizResults);
        model.addAttribute("categories", categories);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedUserId", userId);

        System.out.println("quizResults: " + quizResults);
        System.out.println("categories: " + categories);
        System.out.println("users: " + users);

        return "quizResultManagement";
    }

    @GetMapping("/quiz-result-detail")
    public String showQuizResultDetail(@RequestParam(value = "quizId", required = true) Integer quizId, Model model) {
        if (quizId == null) {
            model.addAttribute("errorMessage", "Quiz ID is missing.");
            return "questionManagement";  // Redirect to an error page or handle the error
        }

        Optional<Quiz> quiz = quizService.getQuizById(quizId);

        if (quiz.isPresent()) {
            Quiz quizObj = quiz.get();
            Optional<User> user = userService.getUserById(quizObj.getUser_id());

            //user.ifPresent(value -> model.addAttribute("user", value));
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
                model.addAttribute("username", user.get().getUsername()); // Pass the username directly
            }

            List<Map<String, Object>> quizBreakdown = quizService.getQuizBreakdown(quizId);
            model.addAttribute("quiz", quizObj);
            model.addAttribute("quizBreakdown", quizBreakdown);
        } else {
            model.addAttribute("errorMessage", "Quiz result not found.");
        }

        return "quizResult";
    }

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.isIs_admin();
    }
}