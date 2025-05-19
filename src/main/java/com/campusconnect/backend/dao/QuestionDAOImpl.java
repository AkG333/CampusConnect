package com.campusconnect.backend.dao;

import com.campusconnect.backend.dao.QuestionDAO;
import com.campusconnect.backend.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class QuestionDAOImpl implements QuestionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<Question> rowMapper = (rs, rowNum) -> {
        Question q = new Question();
        q.setId(rs.getLong("id"));
        q.setTitle(rs.getString("title"));
        q.setDescription(rs.getString("description"));
        q.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        q.setUserId(rs.getLong("user_id"));
        return q;
    };

    @Override
    public void saveQuestion(Question question) {
        String sql = "INSERT INTO questions (title, description, created_at, user_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, question.getTitle(), question.getDescription(), question.getCreatedAt(), question.getUserId());
    }

    @Override
    public Question getQuestionById(Long id) {
        String sql = "SELECT * FROM questions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public List<Question> getAllQuestions() {
        String sql = "SELECT * FROM questions ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void deleteQuestion(Long id) {
        String sql = "DELETE FROM questions WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Question> getQuestionsByUserId(Long userId) {
        String sql = "SELECT * FROM questions WHERE user_id = ?";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }
}
