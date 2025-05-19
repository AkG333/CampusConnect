package com.campusconnect.backend.dao;

import com.campusconnect.backend.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AnswerDAOImpl implements AnswerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map each result set to an Answer object
    private Answer mapRow(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setId(rs.getLong("id"));
        answer.setContent(rs.getString("content"));
        answer.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        answer.setUserId(rs.getLong("user_id"));
        answer.setQuestionId(rs.getLong("question_id"));
        return answer;
    }

    @Override
    public void saveAnswer(Answer answer) {
        String sql = "INSERT INTO answers (content, created_at, user_id, question_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, answer.getContent(), answer.getCreatedAt(), answer.getUserId(), answer.getQuestionId());
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        String sql = "SELECT * FROM answers WHERE question_id = ?";
        return jdbcTemplate.query(sql, new Object[]{questionId}, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public Answer getAnswerById(Long id) {
        String sql = "SELECT * FROM answers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> mapRow(rs));
    }
}
