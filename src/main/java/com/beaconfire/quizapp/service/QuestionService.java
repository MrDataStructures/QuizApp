package com.beaconfire.quizapp.service;

import com.beaconfire.quizapp.model.Question;
import com.beaconfire.quizapp.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public boolean hasEnoughQuestions(int categoryId) {
        return questionRepository.countQuestionsByCategoryId(categoryId) >= 3;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.getAllQuestions();
    }

    public void updateQuestionDescription(int questionId, String description) {
        questionRepository.updateQuestionDescription(questionId, description);
    }

    public void addNewQuestion(int categoryId, String description) {
        Question question = new Question();
        question.setCategory_id(categoryId);
        question.setDescription(description);
        question.setIs_active(true); // default to active when adding a new question
        questionRepository.createQuestion(question);
    }

    public int addNewQuestionAndGetId(int categoryId, String description) {
        if (questionRepository.existsByDescription(description)) {
            throw new IllegalArgumentException("A question with the same description already exists.");
        }

        Question question = new Question();
        question.setCategory_id(categoryId);
        question.setDescription(description);
        question.setIs_active(true); // default to active when adding a new question
        return questionRepository.createQuestionAndGetId(question); // Return the generated question ID
    }

    public void toggleQuestionStatus(int questionId) {
        Question question = questionRepository.getQuestionById(questionId).orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));
        question.setIs_active(!question.isIs_active());
        questionRepository.updateQuestionStatus(question);
    }

    public List<Question> getQuestionsByCategoryId(int categoryId) { //making a quiz is dependant on this
        return questionRepository.getQuestionsByCategoryId(categoryId);
    }

    public Optional<Question> getQuestionById(int questionId) {
        return questionRepository.getQuestionById(questionId);
    }

    public boolean isCorrectAnswer(int questionId, int choiceId) {
        return questionRepository.isCorrectAnswer(questionId, choiceId);
    }


}
