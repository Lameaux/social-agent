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

import com.euromoby.social.core.twitter.model.TwitterAccount;
import com.euromoby.social.core.utils.StringUtils;


@Component
public class TwitterAccountDao {

	private DataSource dataSource;

	private static final TwitterAccountRowMapper ROW_MAPPER = new TwitterAccountRowMapper();

	@Autowired
	public TwitterAccountDao(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public TwitterAccount findById(String id) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject("select a.*, GROUP_CONCAT(ag.group_id) as groups from twitter_account a left join twitter_account_in_group ag on a.id = ag.account_id where id = ? group by a.id", ROW_MAPPER, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public TwitterAccount findByScreenName(String screenName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		try {
			return jdbcTemplate.queryForObject("select a.*, GROUP_CONCAT(ag.group_id) as groups from twitter_account a left join twitter_account_in_group ag on a.id = ag.account_id where screen_name = ? group by a.id", ROW_MAPPER, screenName);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}	
	
	public List<TwitterAccount> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select a.*, GROUP_CONCAT(ag.group_id) as groups from twitter_account a left join twitter_account_in_group ag on a.id = ag.account_id group by a.id order by a.screen_name", ROW_MAPPER);
	}

	public List<TwitterAccount> findByGroupId(Integer groupId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate.query("select a.*, ag.group_id as groups from twitter_account a join twitter_account_in_group ag on a.id = ag.account_id where ag.group_id = ? order by a.screen_name", ROW_MAPPER, groupId);
	}	
	
	public void save(TwitterAccount twitterAccount) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("insert into twitter_account(id, screen_name, description, access_token, access_token_secret) values (?,?,?,?,?)", 
				twitterAccount.getId(), twitterAccount.getScreenName(), twitterAccount.getDescription(),
				twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret());
	}

	public void update(TwitterAccount twitterAccount) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update("update twitter_account set screen_name = ?, description = ?, access_token = ?, access_token_secret = ? where id = ?", 
				twitterAccount.getScreenName(), twitterAccount.getDescription(),
				twitterAccount.getAccessToken(), twitterAccount.getAccessTokenSecret(), twitterAccount.getId());
		
		jdbcTemplate.update("delete from twitter_account_in_group where account_id = ?", twitterAccount.getId());
		for (Integer groupId : twitterAccount.getGroups()) {
			jdbcTemplate.update("insert into twitter_account_in_group (account_id, group_id) values (?,?)", twitterAccount.getId(), groupId);
		}
	}

	static class TwitterAccountRowMapper implements RowMapper<TwitterAccount> {
		@Override
		public TwitterAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
			TwitterAccount twitterAccount = new TwitterAccount();
			twitterAccount.setId(rs.getString("id"));
			twitterAccount.setScreenName(rs.getString("screen_name"));
			twitterAccount.setDescription(rs.getString("description"));
			twitterAccount.setAccessToken(rs.getString("access_token"));
			twitterAccount.setAccessTokenSecret(rs.getString("access_token_secret"));
			List<Integer> groupIds = new ArrayList<Integer>();
			twitterAccount.setGroups(groupIds);
			String groups = rs.getString("groups");
			if (!StringUtils.nullOrEmpty(groups)) {
				String[] groupsArray = groups.split(",");
				for (String groupId : groupsArray) {
					groupIds.add(Integer.parseInt(groupId));
				}
			}
			return twitterAccount;
		}
	}
}
