package com.beaconfire.quizapp.repository;

import com.beaconfire.quizapp.model.Question;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class QuestionRepository {

    private final JdbcTemplate jdbcTemplate;

    public QuestionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    //get all questions by category ID
    public List<Question> getQuestionsByCategoryId(int categoryId) {
        String sql = "SELECT * FROM Question WHERE category_id = ?";
        return jdbcTemplate.query(sql, new QuestionRowMapper(), categoryId);
    }

    public int countQuestionsByCategoryId(int categoryId) {
        String sql = "SELECT COUNT(*) FROM Question WHERE category_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, categoryId);
        return (count != null) ? count : 0;
    }

    public boolean existsByDescription(String description) {
        String sql = "SELECT COUNT(*) FROM Question WHERE description = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, description);
        return count != null && count > 0;
    }

    public void createQuestion(Question question) {
        String sql = "INSERT INTO Question (category_id, description, is_active) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, question.getCategory_id(), question.getDescription(), question.isIs_active());
    }

    public void updateQuestionStatus(Question question) {
        String sql = "UPDATE Question SET is_active = ? WHERE question_id = ?";
        jdbcTemplate.update(sql, question.isIs_active(), question.getQuestion_id());
    }

    public Optional<Question> getQuestionById(int questionId) {
        String sql = "SELECT * FROM Question WHERE question_id = ?";
        try {
            Question question = jdbcTemplate.queryForObject(sql, new QuestionRowMapper(), questionId);
            return Optional.ofNullable(question);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Question> getAllQuestions() {
        String sql = "SELECT * FROM Question";
        return jdbcTemplate.query(sql, new QuestionRowMapper());
    }

    public void updateQuestionDescription(int questionId, String description) {
        String sql = "UPDATE Question SET description = ? WHERE question_id = ?";
        jdbcTemplate.update(sql, description, questionId);
    }

    public void updateQuestion(Question question) {
        String sql = "UPDATE Question SET category_id = ?, description = ?, is_active = ? WHERE question_id = ?";
        jdbcTemplate.update(sql, question.getCategory_id(), question.getDescription(), question.isIs_active(), question.getQuestion_id());
    }

    public boolean deleteQuestion(int questionId) {
        String checkSql = "SELECT COUNT(*) FROM Question WHERE question_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, questionId);

        if (count != null && count > 0) {
            String deleteSql = "DELETE FROM Question WHERE question_id = ?";
            jdbcTemplate.update(deleteSql, questionId);
            return true; //successful deletion
        } else {
            return false; //question does not exist
        }
    }

    public int createQuestionAndGetId(Question question) {
        String sql = "INSERT INTO Question (category_id, description, is_active) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"question_id"});
            ps.setInt(1, question.getCategory_id());
            ps.setString(2, question.getDescription());
            ps.setBoolean(3, question.isIs_active());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public boolean isCorrectAnswer(int questionId, int choiceId) {
        String sql = "SELECT is_correct FROM Choices WHERE question_id = ? AND choice_id = ?";
        Boolean isCorrect = jdbcTemplate.queryForObject(sql, Boolean.class, questionId, choiceId);
        return Boolean.TRUE.equals(isCorrect);
    }

    private static class QuestionRowMapper implements RowMapper<Question> {
        @Override
        public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
            Question question = new Question();
            question.setQuestion_id(rs.getInt("question_id"));
            question.setCategory_id(rs.getInt("category_id"));
            question.setDescription(rs.getString("description"));
            question.setIs_active(rs.getBoolean("is_active"));
            return question;
        }
    }
}