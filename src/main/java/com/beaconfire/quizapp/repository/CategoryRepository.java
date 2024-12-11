package com.beaconfire.quizapp.repository;

import com.beaconfire.quizapp.model.Category;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createCategory(Category category) {
        String sql = "INSERT INTO Category (name) VALUES (?)";
        jdbcTemplate.update(sql, category.getName());
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Category WHERE category_id = ?";
        return jdbcTemplate.queryForObject(sql, new CategoryRowMapper(), categoryId);
    }

    public Optional<Integer> getIdByName(String name) {
        String sql = "SELECT category_id FROM Category WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Integer.class, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
 //int getIdByName(string) this is to
    public List<Category> getAllCategories() {
        String sql = "SELECT * FROM Category";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    public void updateCategory(Category category) {
        String sql = "UPDATE Category SET name = ? WHERE category_id = ?";
        jdbcTemplate.update(sql, category.getName(), category.getCategory_id());
    }

    public void deleteCategory(int categoryId) {
        String sql = "DELETE FROM Category WHERE category_id = ?";
        jdbcTemplate.update(sql, categoryId);
    }

    private static class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setCategory_id(rs.getInt("category_id"));
            category.setName(rs.getString("name"));
            return category;
        }
    }
}
