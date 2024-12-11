package com.beaconfire.quizapp.repository;

import com.beaconfire.quizapp.model.Question;
import com.beaconfire.quizapp.model.Quiz;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class QuizRepository {

    private final JdbcTemplate jdbcTemplate;

    public QuizRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createQuiz(Quiz quiz) {
        String sql = "INSERT INTO Quiz (user_id, category_id) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"quiz_id"});
            ps.setInt(1, quiz.getUser_id());
            ps.setInt(2, quiz.getCategory_id());
            return ps;
        }, keyHolder);

        // Retrieve and set the generated quiz_id on the Quiz object
        if (keyHolder.getKey() != null) {
            quiz.setQuiz_id(keyHolder.getKey().intValue());
        }
    }

    public Optional<Quiz> getQuizById(int quizId) {
        String sql = "SELECT * FROM Quiz WHERE quiz_id = ?";
        try {
            Quiz quiz = jdbcTemplate.queryForObject(sql, new QuizRowMapper(), quizId);
            return Optional.ofNullable(quiz);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Quiz> getAllQuizzes() {
        String sql = "SELECT * FROM Quiz";
        return jdbcTemplate.query(sql, new QuizRowMapper());
    }

    public void updateQuiz(Quiz quiz) {
        String sql = "UPDATE Quiz SET score = ?, end_time = ?, user_id = ?, category_id = ? WHERE quiz_id = ?";
        jdbcTemplate.update(sql, quiz.getScore(), quiz.getEnd_time(), quiz.getUser_id(), quiz.getCategory_id(), quiz.getQuiz_id());
    }

    public List<Map<String, Object>> getPaginatedQuizResults(Integer categoryId, Integer userId, int limit, int offset) {
        String sql = "SELECT q.quiz_id, q.end_time AS taken_time, c.name AS category, u.firstname AS user_firstname, u.lastname AS user_lastname, " +
                "COUNT(qq.question_id) AS num_questions, q.score " +
                "FROM Quiz q " +
                "JOIN Category c ON q.category_id = c.category_id " +
                "JOIN Users u ON q.user_id = u.user_id " +
                "JOIN QuizQuestions qq ON q.quiz_id = qq.quiz_id " +
                "WHERE (? IS NULL OR q.category_id = ?) " +
                "AND (? IS NULL OR q.user_id = ?) " +
                "GROUP BY q.quiz_id " +
                "ORDER BY q.end_time DESC " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.queryForList(sql, categoryId, categoryId, userId, userId, limit, offset);
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

    public boolean deleteQuiz(int quizId) {
        String checkSql = "SELECT COUNT(*) FROM Quiz WHERE quiz_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, quizId);

        if (count != null && count > 0) {
            String deleteSql = "DELETE FROM Quiz WHERE quiz_id = ?";
            jdbcTemplate.update(deleteSql, quizId);
            return true; //successful deletion
        } else {
            return false; //quiz does not exist
        }
    }

    public List<Quiz> getQuizzesByUserId(int userId) {
        String sql = "SELECT * FROM Quiz WHERE user_id = ?";
        return jdbcTemplate.query(sql, new QuizRowMapper(), userId);
    }

    private static class QuizRowMapper implements RowMapper<Quiz> {
        @Override
        public Quiz mapRow(ResultSet rs, int rowNum) throws SQLException {
            Quiz quiz = new Quiz();
            quiz.setQuiz_id(rs.getInt("quiz_id"));
            quiz.setScore(rs.getInt("score"));
            quiz.setStart_time(rs.getTimestamp("start_time"));
            quiz.setEnd_time(rs.getTimestamp("end_time"));
            quiz.setUser_id(rs.getInt("user_id"));
            quiz.setCategory_id(rs.getInt("category_id"));
            return quiz;
        }
    }
}