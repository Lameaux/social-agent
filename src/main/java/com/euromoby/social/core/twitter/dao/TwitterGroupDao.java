package com.euromoby.social.core.twitter.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.euromoby.social.core.twitter.model.TwitterGroup;
import com.euromoby.social.core.utils.StringUtils;


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
			return jdbcTemplate.queryForObject("select g.*, GROUP_CONCAT(ag.account_id) as accounts from twitter_group g left join twitter_account_in_group ag on g.id = ag.group_id where id = ? group by g.id", ROW_MAPPER, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<TwitterGroup> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select g.*, GROUP_CONCAT(ag.account_id) as accounts from twitter_group g left join twitter_account_in_group ag on g.id = ag.group_id group by g.id order by g.title", ROW_MAPPER);
	}
	
	public void save(TwitterGroup twitterGroup) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into twitter_group(title) values (?)", twitterGroup.getTitle());
	}

	public void update(TwitterGroup twitterGroup) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update twitter_group set title = ? where id = ?", twitterGroup.getTitle(), twitterGroup.getId());

		jdbcTemplate.update("delete from twitter_account_in_group where group_id = ?", twitterGroup.getId());
		for (String accountId : twitterGroup.getAccounts()) {
			jdbcTemplate.update("insert into twitter_account_in_group (account_id, group_id) values (?,?)", accountId, twitterGroup.getId());
		}
	}

	public void delete(TwitterGroup twitterGroup) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("delete from twitter_account_in_group where group_id = ?", twitterGroup.getId());
		jdbcTemplate.update("delete from twitter_group where id = ?", twitterGroup.getId());
	}	
	
	static class TwitterGroupRowMapper implements RowMapper<TwitterGroup> {
		@Override
		public TwitterGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
			TwitterGroup twitterGroup = new TwitterGroup();
			twitterGroup.setId(rs.getInt("id"));
			twitterGroup.setTitle(rs.getString("title"));
			List<String> accountIds = new ArrayList<String>();
			twitterGroup.setAccounts(accountIds);
			String accounts = rs.getString("accounts");
			if (!StringUtils.nullOrEmpty(accounts)) {
				String[] accountsArray = accounts.split(",");
				for (String accountId : accountsArray) {
					accountIds.add(accountId);
				}
			}			
			return twitterGroup;
		}
	}
}
