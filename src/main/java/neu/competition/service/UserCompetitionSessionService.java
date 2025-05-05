package neu.competition.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserCompetitionSessionService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveUserCompetitionSession(String userId, int matchId, int teamId) {
        String sql = "INSERT INTO user_competition_session (user_id, match_id, team_id, selected_time) " +
                "VALUES (?, ?, ?, NOW()) " +
                "ON DUPLICATE KEY UPDATE team_id = ?, selected_time = NOW()";
        jdbcTemplate.update(sql, userId, matchId, teamId, teamId);
    }

    public Integer getUserSelectedTeam(String userId, int matchId) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT team_id FROM user_competition_session WHERE user_id = ? AND match_id = ?",
                    Integer.class, userId, matchId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}