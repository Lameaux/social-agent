package com.euromoby.social.core.twitter.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.euromoby.social.core.twitter.model.TwitterActionStatus;


@Component
public class TwitterActionStatusDao {

	private DataSource dataSource;

	private static final TwitterActionStatusRowMapper ROW_MAPPER = new TwitterActionStatusRowMapper();

	@Autowired
	public TwitterActionStatusDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TwitterActionStatus findById(Integer id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject("select * from twitter_action_status where id = ?", ROW_MAPPER, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TwitterActionStatus> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from twitter_action_status order by id desc", ROW_MAPPER);
	}

	public List<TwitterActionStatus> findAllNew() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from twitter_action_status where status = ? order by id", ROW_MAPPER, TwitterActionStatus.STATUS_NEW);
	}	

	public int countByScreenNameAndText(String screenName, String message) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.queryForObject("select count(*) from twitter_action_status where screen_name = ? and message = ?", Integer.class, screenName, message);
	}	
	
	
	
	
	public void save(TwitterActionStatus actionStatus) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into twitter_action_status(screen_name, message, status) values (?,?,?)", 
				actionStatus.getScreenName(), actionStatus.getMessage(), actionStatus.getStatus());
	}

	public void update(TwitterActionStatus actionStatus) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update twitter_action_status set status = ?, error_text = ? where id = ?", actionStatus.getStatus(), actionStatus.getErrorText(), actionStatus.getId());
	}

	public void delete(TwitterActionStatus actionStatus) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from twitter_action_status where id = ?", actionStatus.getId());
	}	
	
	static class TwitterActionStatusRowMapper implements RowMapper<TwitterActionStatus> {
		@Override
		public TwitterActionStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
			TwitterActionStatus twitterActionStatus = new TwitterActionStatus();
			twitterActionStatus.setId(rs.getInt("id"));
			twitterActionStatus.setScreenName(rs.getString("screen_name"));
			twitterActionStatus.setMessage(rs.getString("message"));
			twitterActionStatus.setStatus(rs.getInt("status"));
			twitterActionStatus.setErrorText(rs.getString("error_text"));
			return twitterActionStatus;
		}
	}
}
