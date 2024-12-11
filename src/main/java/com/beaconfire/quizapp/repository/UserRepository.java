package com.beaconfire.quizapp.repository;
import com.beaconfire.quizapp.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUser(User user) {
        String sql = "INSERT INTO Users (user_id, username, email, password, firstname, lastname, is_active, is_admin) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUser_id(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstname(), user.getLastname(), user.isIs_active(), user.isIs_admin());
    }

    public Optional<User> getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<User> getUsersWithPagination(int offset, int limit) {
        String sql = "SELECT * FROM Users LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), limit, offset);
    }


    public Optional<User> getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE email = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), email);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void inactivateUserByUsername(String username) {
        String sql = "UPDATE Users SET is_active = false WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }

    public void activateUserByUsername(String username) {
        String sql = "UPDATE Users SET is_active = true WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM Users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    public void updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, email = ?, password = ?, firstname = ?, lastname = ?, is_active = ?, is_admin = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstname(), user.getLastname(), user.isIs_active(), user.isIs_admin(), user.getUser_id());
    }

    public boolean deleteUser(int userId) {
        String checkSql = "SELECT COUNT(*) FROM Users WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, userId);

        if (count != null && count > 0) {
            String deleteSql = "DELETE FROM Users WHERE user_id = ?";
            jdbcTemplate.update(deleteSql, userId);
            return true; // Indicating successful deletion
        } else {
            return false; // Indicating user does not exist
        }
    }

    public Optional<Integer> getUserIDWithUsername(String username) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try {
            Integer userId = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return Optional.ofNullable(userId);
        } catch (Exception e) {
            return Optional.empty(); // Return empty if user not found
        }
    }

    public void inactivateUserById(int userId) {
        String sql = "UPDATE Users SET is_active = false WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public void activateUserById(int userId) {
        String sql = "UPDATE Users SET is_active = true WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public List<User> getNonAdminUsers(int offset, int pageSize) {
        String sql = "SELECT * FROM Users WHERE is_admin = false LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, new UserRowMapper(), pageSize, offset);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUser_id(rs.getInt("user_id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstname(rs.getString("firstname"));
            user.setLastname(rs.getString("lastname"));
            user.setIs_active(rs.getBoolean("is_active"));
            user.setIs_admin(rs.getBoolean("is_admin"));
            return user;
        }
    }
}