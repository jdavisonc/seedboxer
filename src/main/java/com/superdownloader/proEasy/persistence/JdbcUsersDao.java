package com.superdownloader.proEasy.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author harley
 *
 */
@Repository
public class JdbcUsersDao extends SimpleJdbcDaoSupport implements UsersDao {

	@Autowired
	public void setProEasyDataSource(DataSource proEasyDataSource) {
		setDataSource(proEasyDataSource);
	}

	@Override
	public boolean isValidUser(String username, String password) {
		String sql = "SELECT id " +
						"FROM users " +
						"WHERE username = :username AND password = MD5(:password) " +
						"LIMIT 1;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("username", username);
		args.addValue("password", password);

		return (getSimpleJdbcTemplate().queryForInt(sql, args) == 0);
	}

	@Override
	public Map<String, String> getUserConfigs(String username) {
		String sql = "SELECT c.name as name, c.value as value " +
						"FROM configurations c, users u " +
						"WHERE c.id_user = u.id AND u.username = :username;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("username", username);

		List<Config> configs = getSimpleJdbcTemplate().query(sql, new ConfigsMapper(), args);

		Map<String, String> configMap = new HashMap<String, String>();
		for (Config c : configs) {
			configMap.put(c.name, c.value);
		}

		return configMap;
	}

	private class Config {

		public String name;
		public String value;

	}

	public class ConfigsMapper implements RowMapper<Config> {

		@Override
		public Config mapRow(ResultSet rs, int rowNum) throws SQLException {
			Config c = new Config();
			c.name = rs.getString("name");
			c.value = rs.getString("value");
			return c;
		}

	}

}
