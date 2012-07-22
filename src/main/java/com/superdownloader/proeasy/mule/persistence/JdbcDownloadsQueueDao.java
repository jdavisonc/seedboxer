package com.superdownloader.proeasy.mule.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.superdownloader.proeasy.core.types.DownloadQueueItem;

/**
 * @author harley
 *
 */
@Repository
public class JdbcDownloadsQueueDao extends SimpleJdbcDaoSupport implements DownloadsQueueDao {

	@Autowired
	public void setProEasyDataSource(DataSource proEasyDataSource) {
		setDataSource(proEasyDataSource);
	}

	@Override
	public void push(DownloadQueueItem item) {
		String sql = "INSERT INTO downloads_queue (user_id, download, created_date) " +
				"VALUES (:userId, :download, NOW());";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("download", item.getDownload());
		args.addValue("userId", item.getUserId());

		getSimpleJdbcTemplate().update(sql, args);
	}

	@Override
	public void repush(int id) {
		String sql = "UPDATE downloads_queue SET in_progress = 0 WHERE id = :id;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("id", id);

		getSimpleJdbcTemplate().update(sql, args);
	}

	@Override
	public List<DownloadQueueItem> pop(int maxDownloadPerUser) {
		String sql = "SELECT id, user_id, download, in_progress " +
				"FROM downloads_queue d " +
				"WHERE ( SELECT count(*) FROM downloads_queue as f WHERE f.user_id = d.user_id AND f.id < d.id ) " +
				"<= :maxDownloadPerUser;";


		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("maxDownloadPerUser", maxDownloadPerUser);
		return getSimpleJdbcTemplate().query(sql, new ItemsMapper(), args);
	}

	@Override
	public void setInProgress(List<Integer> idsToUpdate) {
		String updateSql = "UPDATE downloads_queue SET in_progress = 1 WHERE id IN (:ids);";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("ids", idsToUpdate);
		getSimpleJdbcTemplate().update(updateSql, args);
	}

	@Override
	public void remove(int id) {
		String updateSql = "DELETE FROM downloads_queue WHERE id = :id;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("id", id);
		getSimpleJdbcTemplate().update(updateSql, args);
	}

	@Override
	public List<DownloadQueueItem> queue(int userId) {
		String sql = "SELECT id, user_id, download, in_progress " +
				"FROM downloads_queue " +
				"WHERE user_id = :userId AND in_progress = 0;";

		MapSqlParameterSource args = new MapSqlParameterSource();
		args.addValue("userId", userId);
		return getSimpleJdbcTemplate().query(sql, new ItemsMapper(), args);
	}

	public class ItemsMapper implements RowMapper<DownloadQueueItem> {

		@Override
		public DownloadQueueItem mapRow(ResultSet rs, int rowNum) throws SQLException {
			DownloadQueueItem c = new DownloadQueueItem();
			c.setDownload(rs.getString("download"));
			c.setId(rs.getInt("id"));
			c.setUserId(rs.getInt("user_id"));
			c.setInProgress(rs.getBoolean("in_progress"));
			return c;
		}
	}

}