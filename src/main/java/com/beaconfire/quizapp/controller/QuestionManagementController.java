package com.beaconfire.quizapp.controller;

import com.beaconfire.quizapp.model.Category;
import com.beaconfire.quizapp.model.Choice;
import com.beaconfire.quizapp.model.Question;
import com.beaconfire.quizapp.service.CategoryService;
import com.beaconfire.quizapp.service.QuestionService;
import com.beaconfire.quizapp.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/question-management")
public class QuestionManagementController {

    private final QuestionService questionService;
    private final CategoryService categoryService;
    private final ChoiceRepository choiceRepository;

    @Autowired
    public QuestionManagementController(QuestionService questionService, CategoryService categoryService, ChoiceRepository choiceRepository) {
        this.questionService = questionService;
        this.categoryService = categoryService;
        this.choiceRepository = choiceRepository;
    }

    @GetMapping
    public String showQuestionManagementPage(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("questions", questions);
        model.addAttribute("categories", categories);
        return "questionManagement"; // Render questionManagement.jsp
    }

    @GetMapping("/edit")
    public String editQuestion(@RequestParam("questionId") int questionId, Model model) {
        Optional<Question> question = questionService.getQuestionById(questionId);

        if (question.isPresent()) {
            model.addAttribute("question", question.get());

            // Fetch choices associated with this question
            List<Choice> choices = choiceRepository.getChoicesByQuestionId(questionId);
            model.addAttribute("choices", choices);

            return "editQuestion"; // Render editQuestion.jsp
        } else {
            model.addAttribute("errorMessage", "Question not found.");
            return "redirect:/admin/question-management";
        }
    }

    @PostMapping("/update")
    public String updateQuestion(@RequestParam("questionId") int questionId,
                                 @RequestParam("description") String description,
                                 @RequestParam("choiceIds") List<Integer> choiceIds,
                                 @RequestParam("choiceDescriptions") List<String> choiceDescriptions,
                                 @RequestParam("correctChoiceId") int correctChoiceId) {

        // Update question description
        questionService.updateQuestionDescription(questionId, description);

        // Update choices
        for (int i = 0; i < choiceIds.size(); i++) {
            int choiceId = choiceIds.get(i);
            String choiceDescription = choiceDescriptions.get(i);

            // Update each choice and set the correct one
            Choice choice = choiceRepository.getChoiceById(choiceId);
            choice.setDescription(choiceDescription);
            choice.setIs_correct(choiceId == correctChoiceId); // Only mark the chosen option as correct
            choiceRepository.updateChoice(choice);
        }

        return "redirect:/admin/question-management";
    }

    @GetMapping("/add")
    public String addQuestion(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "addQuestion"; // Render addQuestion.jsp
    }

    @PostMapping("/save")
    public String saveQuestion(@RequestParam("categoryId") int categoryId,
                               @RequestParam("description") String description,
                               @RequestParam("choiceDescriptions") List<String> choiceDescriptions,
                               @RequestParam("correctChoiceIndex") int correctChoiceIndex,
                               Model model) {

        try {
            // Create the question and get its ID
            int questionId = questionService.addNewQuestionAndGetId(categoryId, description);

            // Add choices to the database, marking the correct one
            for (int i = 0; i < choiceDescriptions.size(); i++) {
                Choice choice = new Choice();
                choice.setQuestion_id(questionId);
                choice.setDescription(choiceDescriptions.get(i));
                choice.setIs_correct(i == correctChoiceIndex); // Set correct choice based on index
                choiceRepository.createChoice(choice);
            }

            return "redirect:/admin/question-management";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "addQuestion"; // Return to addQuestion.jsp with an error message
        }
    }

    @PostMapping("/toggleStatus")
    public String toggleQuestionStatus(@RequestParam("questionId") int questionId) {
        questionService.toggleQuestionStatus(questionId);
        return "redirect:/admin/question-management";
    }
}
