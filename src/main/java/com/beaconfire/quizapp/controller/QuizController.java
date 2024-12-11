package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.Category;
import com.beaconfire.quizapp.model.Quiz;
import com.beaconfire.quizapp.model.Question;
import com.beaconfire.quizapp.model.Choice;
import com.beaconfire.quizapp.service.QuizService;
import com.beaconfire.quizapp.service.QuestionService;
import com.beaconfire.quizapp.service.CategoryService;
import com.beaconfire.quizapp.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final QuestionService questionService;
    private final ChoiceRepository choiceRepository; // Use ChoiceRepository directly
    private final CategoryService categoryService;

    @Autowired
    public QuizController(QuizService quizService, CategoryService categoryService, QuestionService questionService, ChoiceRepository choiceRepository) {
        this.quizService = quizService;
        this.questionService = questionService;
        this.choiceRepository = choiceRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/start")
    public String startQuiz(@RequestParam("categoryId") int categoryId, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        //Check if the category has at least three questions
        if (!questionService.hasEnoughQuestions(categoryId)) {
            model.addAttribute("errorMessage", "Cannot take quiz. Not enough questions for this category.");

            // Add categories and recent quizzes to keep the home page intact
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);

            List<Quiz> recentQuizzes = quizService.getAllQuizzesForUsername(username);
            Collections.reverse(recentQuizzes);
            model.addAttribute("recentQuizzes", recentQuizzes);

            return "home"; // Go back to the homepage with the error message
        }

        // Retrieve quiz and selected questions
        Map<String, Object> quizData = quizService.createQuiz(username, categoryId);
        Quiz quiz = (Quiz) quizData.get("quiz");

        @SuppressWarnings("unchecked")
        List<Question> selectedQuestions = (List<Question>) quizData.get("selectedQuestions");

        // Attach choices to each selected question
        selectedQuestions.forEach(question -> {
            List<Choice> choices = choiceRepository.getChoicesByQuestionId(question.getQuestion_id());
            question.setChoices(choices);
        });

        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", selectedQuestions);

        return "quiz";
    }

    // The rest of QuizController remains the same as previously outlined

    @PostMapping("/submit")
    public String submitQuiz(@RequestParam("quizId") int quizId,
                             @RequestParam Map<String, String> params, HttpSession session, Model model) {
        int score = quizService.submitQuizAndCalculateScore(quizId, params); // Updated to handle answers in the quiz service

        Quiz quiz = quizService.getQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found when submitting. quiz controller /submit"));
        Category category = categoryService.getCategoryById(quiz.getCategory_id()).orElseThrow(() -> new RuntimeException("Category not found when submitting"));
        quiz.setCategory(category);

        List<Map<String, Object>> quizBreakdown = quizService.getQuizBreakdown(quizId);
        quizBreakdown.forEach(item -> {
            System.out.println("Question Description: " + item.get("questionDescription"));
            System.out.println("Choice Descriptions: " + item.get("choiceDescriptions"));
            System.out.println("User Choice: " + item.get("userChoiceDescription"));
            System.out.println("Correct Choice: " + item.get("correctChoiceDescription"));
        });

        model.addAttribute("quiz", quiz);
        model.addAttribute("score", score);
        model.addAttribute("result", score >= 2 ? "Pass" : "Fail");
        model.addAttribute("quizBreakdown", quizBreakdown);

        System.out.println("quizBreakdown: " + quizBreakdown);

        return "quizResult"; // Maps to quizResult.jsp
    }

    @GetMapping("/result")
    public String viewQuizResult(@RequestParam("quizId") int quizId, Model model) {
        Quiz quiz = quizService.getQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<Map<String, Object>> quizBreakdown = quizService.getQuizBreakdown(quizId);

        model.addAttribute("quiz", quiz);
        model.addAttribute("quizBreakdown", quizBreakdown);
        model.addAttribute("score", quiz.getScore());
        model.addAttribute("result", quiz.getScore() >= 2 ? "Pass" : "Fail");


        System.out.println("Viewing result for quizId: " + quizId);
        System.out.println("Quiz: " + quiz);
        System.out.println("Quiz Breakdown: " + quizBreakdown);

        return "quizResult";
    }
}