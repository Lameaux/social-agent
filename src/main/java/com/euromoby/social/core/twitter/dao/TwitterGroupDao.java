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

import com.euromoby.social.core.twitter.model.TwitterGroup;


@Component
public class TwitterGroupDao {

	private DataSource dataSource;

	private static final TwitterGroupRowMapper ROW_MAPPER = new TwitterGroupRowMapper();

	@Autowired
	public TwitterGroupDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TwitterGroup findById(Integer id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject("select * from twitter_group where id = ? order by title", ROW_MAPPER, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TwitterGroup> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select * from twitter_group order by title", ROW_MAPPER);
	}
	
	public void save(TwitterGroup twitterGroup) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into twitter_group(title) values (?)", twitterGroup.getTitle());
	}

	public void update(TwitterGroup twitterGroup) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update twitter_group set title = ? where id = ?", twitterGroup.getTitle(), twitterGroup.getId());
	}

	static class TwitterGroupRowMapper implements RowMapper<TwitterGroup> {
		@Override
		public TwitterGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			TwitterGroup twitterGroup = new TwitterGroup();
			twitterGroup.setId(rs.getInt("id"));
			twitterGroup.setTitle(rs.getString("title"));
			return twitterGroup;
		}
	}
}
