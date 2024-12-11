package com.beaconfire.quizapp.repository;

import com.beaconfire.quizapp.model.Choice;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ChoiceRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChoiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createChoice(Choice choice) {
        String sql = "INSERT INTO Choices (question_id, description, is_correct) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, choice.getQuestion_id(), choice.getDescription(), choice.isIs_correct());
    }

    public Choice getChoiceById(int choiceId) {
        String sql = "SELECT * FROM Choices WHERE choice_id = ?";
        return jdbcTemplate.queryForObject(sql, new ChoicesRowMapper(), choiceId);
    }

    public List<Choice> getAllChoices() {
        String sql = "SELECT * FROM Choices";
        return jdbcTemplate.query(sql, new ChoicesRowMapper());
    }

    public void updateChoice(Choice choice) {
        String sql = "UPDATE Choices SET question_id = ?, description = ?, is_correct = ? WHERE choice_id = ?";
        jdbcTemplate.update(sql, choice.getQuestion_id(), choice.getDescription(), choice.isIs_correct(), choice.getChoice_id());
    }

    public void deleteChoice(int choiceId) {
        String sql = "DELETE FROM Choices WHERE choice_id = ?";
        jdbcTemplate.update(sql, choiceId);
    }

    public List<Choice> getChoicesByQuestionId(int questionId) {
        String sql = "SELECT * FROM Choices WHERE question_id = ?";
        return jdbcTemplate.query(sql, new ChoicesRowMapper(), questionId);
    }

    private static class ChoicesRowMapper implements RowMapper<Choice> {
        @Override
        public Choice mapRow(ResultSet rs, int rowNum) throws SQLException {
            Choice choice = new Choice();
            choice.setChoice_id(rs.getInt("choice_id"));
            choice.setQuestion_id(rs.getInt("question_id"));
            choice.setDescription(rs.getString("description"));
            choice.setIs_correct(rs.getBoolean("is_correct"));
            choice.setIs_selected(rs.getBoolean("is_selected"));
            return choice;
        }
    }
}
