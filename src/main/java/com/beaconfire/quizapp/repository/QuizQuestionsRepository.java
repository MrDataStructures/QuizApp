package com.beaconfire.quizapp.repository;

import com.beaconfire.quizapp.model.QuizQuestion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class QuizQuestionsRepository {

    private final JdbcTemplate jdbcTemplate;

    public QuizQuestionsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addQuizQuestion(QuizQuestion quizQuestion) {
        String sql = "INSERT INTO QuizQuestions (quiz_id, question_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, quizQuestion.getQuiz_id(), quizQuestion.getQuestion_id());
    }

    public void updateQuizQuestion(QuizQuestion quizQuestion) {
        String sql = "UPDATE QuizQuestions SET user_choice_id = ? WHERE qq_id = ?";
        jdbcTemplate.update(sql, quizQuestion.getUser_choice_id(), quizQuestion.getQq_id());
    }

    public List<QuizQuestion> getQuizQuestionsByQuizId(int quizId) {
        String sql = "SELECT * FROM QuizQuestions WHERE quiz_id = ?";
        return jdbcTemplate.query(sql, new QuizQuestionRowMapper(), quizId);
    }

    public Optional<QuizQuestion> getQuizQuestionById(int qqId) {
        String sql = "SELECT * FROM QuizQuestions WHERE qq_id = ?";
        try {
            QuizQuestion quizQuestion = jdbcTemplate.queryForObject(sql, new QuizQuestionRowMapper(), qqId);
            return Optional.ofNullable(quizQuestion);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // Delete all quiz-question entries for a given quiz ID, if they exist
    public boolean deleteQuizQuestionsByQuizId(int quizId) {
        String sql = "DELETE FROM QuizQuestions WHERE quiz_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, quizId);
        return rowsAffected > 0; // Returns true if any rows were deleted
    }

    // Mapper to convert database rows into QuizQuestion objects
    private static class QuizQuestionRowMapper implements RowMapper<QuizQuestion> {
        @Override
        public QuizQuestion mapRow(ResultSet rs, int rowNum) throws SQLException {
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setQq_id(rs.getInt("qq_id"));
            quizQuestion.setQuiz_id(rs.getInt("quiz_id"));
            quizQuestion.setQuestion_id(rs.getInt("question_id"));
            quizQuestion.setUser_choice_id(rs.getInt("user_choice_id"));
            return quizQuestion;
        }
    }
}