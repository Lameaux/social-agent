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

import com.euromoby.social.core.twitter.model.TwitterActionFollow;


@Component
public class TwitterActionFollowDao {

	private DataSource dataSource;

	private static final TwitterActionFollowRowMapper ROW_MAPPER = new TwitterActionFollowRowMapper();

	@Autowired
	public TwitterActionFollowDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TwitterActionFollow findById(Integer id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject("select * from twitter_action_follow where id = ?", ROW_MAPPER, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TwitterActionFollow> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from twitter_action_follow order by id desc", ROW_MAPPER);
	}

	public List<TwitterActionFollow> findAllNew() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from twitter_action_follow where status = ? order by id", ROW_MAPPER, TwitterActionFollow.STATUS_NEW);
	}	
	
	public void save(TwitterActionFollow actionFollow) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into twitter_action_follow(screen_name, target_screen_name, status) values (?,?,?)", 
				actionFollow.getScreenName(), actionFollow.getTargetScreenName(), actionFollow.getStatus());
	}

	public void update(TwitterActionFollow actionFollow) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update twitter_action_follow set status = ?, error_text = ? where id = ?", actionFollow.getStatus(), actionFollow.getErrorText(), actionFollow.getId());
	}

	public void delete(TwitterActionFollow actionFollow) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from twitter_action_follow where id = ?", actionFollow.getId());
	}	
	
	static class TwitterActionFollowRowMapper implements RowMapper<TwitterActionFollow> {
		@Override
		public TwitterActionFollow mapRow(ResultSet rs, int rowNum) throws SQLException {
			TwitterActionFollow twitterActionFollow = new TwitterActionFollow();
			twitterActionFollow.setId(rs.getInt("id"));
			twitterActionFollow.setScreenName(rs.getString("screen_name"));
			twitterActionFollow.setTargetScreenName(rs.getString("target_screen_name"));
			twitterActionFollow.setStatus(rs.getInt("status"));
			twitterActionFollow.setErrorText(rs.getString("error_text"));
			return twitterActionFollow;
		}
	}
}
