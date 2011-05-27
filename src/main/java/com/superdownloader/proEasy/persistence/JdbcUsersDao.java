package com.superdownloader.proEasy.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * @author harley
 *
 */
@Repository
public class JdbcUsersDao extends JdbcDaoSupport implements UsersDao {

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

		return (getJdbcTemplate().queryForInt(sql, args) == 0);
	}

	@Override
	public Map<String, String> getUserConfigs(String username) {
		String sql = "SELECT c.name as name, c.value as value " +
						"FROM configurations c, users u " +
						"WHERE c.id_user = u.id AND u.username = :username;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("username", username);

		return getJdbcTemplate().query(sql, new MapExtractor(), args);
	}

	public class MapExtractor implements ResultSetExtractor<Map<String, String>> {

		@Override
		public Map<String, String>  extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				String name = rs.getString("name");
				String value = rs.getString("value");
				map.put(name, value);
			}
			return map;
		}

	}

}
