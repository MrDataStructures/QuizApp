package com.beaconfire.quizapp.service;

import com.beaconfire.quizapp.model.*;
import com.beaconfire.quizapp.repository.QuizRepository;
import com.beaconfire.quizapp.repository.QuizQuestionsRepository;
import com.beaconfire.quizapp.repository.QuestionRepository;
import com.beaconfire.quizapp.repository.ChoiceRepository;
import com.beaconfire.quizapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final QuizQuestionsRepository quizQuestionsRepository;
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final ChoiceRepository choiceRepository;

    public QuizService(QuizRepository quizRepository, QuizQuestionsRepository quizQuestionsRepository,
                       QuestionRepository questionRepository, UserService userService, CategoryService categoryService, UserRepository userRepository, ChoiceRepository choiceRepository) {
        this.quizRepository = quizRepository;
        this.quizQuestionsRepository = quizQuestionsRepository;
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
    }

    public Map<String, Object> createQuiz(String username, int categoryId) {
        Quiz quiz = new Quiz();

        int userId = userService.getUserIdByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found when creating quiz"));
        quiz.setUser_id(userId);
        quiz.setCategory_id(categoryId);
        quiz.setStart_time(new Timestamp(System.currentTimeMillis()));

        Category category = categoryService.getCategoryById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found when creating quiz"));
        quiz.setCategory(category);

        quizRepository.createQuiz(quiz);
        if (quiz.getQuiz_id() == 0) {
            throw new RuntimeException("Failed to retrieve Quiz ID after creation");
        }

        List<Question> questions = questionRepository.getQuestionsByCategoryId(categoryId);
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.subList(0, Math.min(3, questions.size()));

        selectedQuestions.forEach(question -> {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz_id(quiz.getQuiz_id());
            quizQuestion.setQuestion_id(question.getQuestion_id());
            quizQuestionsRepository.addQuizQuestion(quizQuestion);
        });

        // Create a map to return both quiz and selected questions
        Map<String, Object> result = new HashMap<>();
        result.put("quiz", quiz);
        result.put("selectedQuestions", selectedQuestions);

        return result;
    }


    public int submitQuizAndCalculateScore(int quizId, Map<String, String> params) {
        List<QuizQuestion> quizQuestions = quizQuestionsRepository.getQuizQuestionsByQuizId(quizId);
        int score = 0;

        for (QuizQuestion quizQuestion : quizQuestions) {
            int questionId = quizQuestion.getQuestion_id();
            int selectedChoiceId = Integer.parseInt(params.get("choice_" + questionId));
            boolean isCorrect = questionRepository.isCorrectAnswer(questionId, selectedChoiceId);

            quizQuestion.setUser_choice_id(selectedChoiceId);
            quizQuestionsRepository.updateQuizQuestion(quizQuestion);

            if (isCorrect) score++;
        }

        Quiz quiz = quizRepository.getQuizById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found for the given quiz ID when submitting"));
        quiz.setEnd_time(new Timestamp(System.currentTimeMillis()));

        quiz.setScore(score);
        Category category = categoryService.getCategoryById(quiz.getCategory_id())
                .orElseThrow(() -> new RuntimeException("Category not found for quiz when submitting"));
        quiz.setCategory(category);

        quizRepository.updateQuiz(quiz);

        return score;
    }

    private List<Question> selectRandomQuestions(List<Question> questions, int numQuestions) {
        List<Question> shuffledQuestions = new ArrayList<>(questions);

        Collections.shuffle(shuffledQuestions, new Random());

        List<Question> selectedQuestions = new ArrayList<>();
        for (int i = 0; i < numQuestions && i < shuffledQuestions.size(); i++) {
            selectedQuestions.add(shuffledQuestions.get(i));
        }
        return selectedQuestions;
    }

    public Optional<Quiz> getQuizById(int quizId) {
        return quizRepository.getQuizById(quizId);
    }

    public boolean recordAnswer(int quizId, int questionId, int choiceId) {
        Optional<Question> question = questionRepository.getQuestionById(questionId);
        if (question.isPresent()) {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQuiz_id(quizId);
            quizQuestion.setQuestion_id(questionId);
            quizQuestion.setUser_choice_id(choiceId);
            quizQuestionsRepository.addQuizQuestion(quizQuestion);
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> getQuizBreakdown(int quizId) {
        List<QuizQuestion> quizQuestions = quizQuestionsRepository.getQuizQuestionsByQuizId(quizId);
        List<Map<String, Object>> breakdown = new ArrayList<>();

        for (QuizQuestion quizQuestion : quizQuestions) {
            Map<String, Object> questionDetails = new HashMap<>();

            // Get question description
            String questionDescription = questionRepository.getQuestionById(quizQuestion.getQuestion_id())
                    .map(Question::getDescription)
                    .orElse("Question not found");
            questionDetails.put("questionDescription", questionDescription);

            // Get choices descriptions
            List<String> choiceDescriptions = choiceRepository.getChoicesByQuestionId(quizQuestion.getQuestion_id())
                    .stream()
                    .map(Choice::getDescription)
                    .collect(Collectors.toList());
            questionDetails.put("choiceDescriptions", choiceDescriptions);

            //Get user-selected choice description, handle null
            Choice userChoice = choiceRepository.getChoiceById(quizQuestion.getUser_choice_id());
            String userChoiceDescription = (userChoice != null) ? userChoice.getDescription() : "No answer selected";
            questionDetails.put("userChoiceDescription", userChoiceDescription);

            //Get correct choice description
            String correctChoiceDescription = choiceRepository.getChoicesByQuestionId(quizQuestion.getQuestion_id())
                    .stream()
                    .filter(Choice::isIs_correct)
                    .map(Choice::getDescription)
                    .findFirst()
                    .orElse("No correct answer found");
            questionDetails.put("correctChoiceDescription", correctChoiceDescription);

            // Debug print
            System.out.println("Question: " + questionDescription);
            System.out.println("Choices: " + choiceDescriptions);
            System.out.println("User Choice: " + userChoiceDescription);
            System.out.println("Correct Choice: " + correctChoiceDescription);

            breakdown.add(questionDetails);
        }

        return breakdown;
    }


    public List<Quiz> getAllQuizzesForUsername(String username) {
        int userId = userRepository.getUserIDWithUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found when getting all quizzes by username"));
        List<Quiz> quizzes = quizRepository.getQuizzesByUserId(userId);

        // Ensure each quiz has its category loaded
        quizzes.forEach(quiz -> {
            Category category = categoryService.getCategoryById(quiz.getCategory_id())
                    .orElseThrow(() -> new RuntimeException("Category not found for quiz"));
            quiz.setCategory(category);
        });

        return quizzes;
    }


    public void submitQuiz(int quizId) {
        Optional<Quiz> optionalQuiz = quizRepository.getQuizById(quizId);
        if (!optionalQuiz.isPresent()) {
            throw new IllegalArgumentException("Quiz not found for the given quiz ID when submitting");
        }

        Quiz quiz = optionalQuiz.get();
        quiz.setEnd_time(new Timestamp(System.currentTimeMillis()));

        // Calculate and set the score
        int score = calculateScore(quizId);
        quiz.setScore(score);

        quizRepository.updateQuiz(quiz);
    }

    public int calculateScore(int quizId) {
        List<QuizQuestion> quizQuestions = quizQuestionsRepository.getQuizQuestionsByQuizId(quizId);
        int score = 0;

        for (QuizQuestion quizQuestion : quizQuestions) {
            int questionId = quizQuestion.getQuestion_id();
            int userChoiceId = quizQuestion.getUser_choice_id();

            // Check if the selected answer is correct
            if (questionRepository.isCorrectAnswer(questionId, userChoiceId)) {
                score++;
            }
        }
        return score;
    }

    public List<QuizQuestion> getQuizQuestionsForQuiz(int quizId) {
        return quizQuestionsRepository.getQuizQuestionsByQuizId(quizId);
    }

    public List<Map<String, Object>> getPaginatedQuizResults(int page, int pageSize, Integer categoryId, Integer userId) {
        if (categoryId == null && userId == null) {
            return quizRepository.getPaginatedQuizResults(null, null, pageSize, page * pageSize);
        } else if (categoryId == null) {
            return quizRepository.getPaginatedQuizResults(null, userId, pageSize, page * pageSize);
        } else if (userId == null) {
            return quizRepository.getPaginatedQuizResults(categoryId, null, pageSize, page * pageSize);
        } else {
            return quizRepository.getPaginatedQuizResults(categoryId, userId, pageSize, page * pageSize);
        }
    }

}