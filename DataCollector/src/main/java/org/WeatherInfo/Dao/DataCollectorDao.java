package org.WeatherInfo.Dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.WeatherInfo.Model.WeatherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DataCollectorDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/* Returns the list of all zipcodes from the Database */
	public List<String> getZipCodes() {
		List<String> zipCodes = null;
		String sql = "select ZIPCODE from WEATHER_DATA";
		try {
			zipCodes = jdbcTemplate.queryForList(sql, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zipCodes;
	}

	/*
	 * Update the weather data table with the weather information corresponding to a
	 * zipcode
	 */
	public synchronized void storeWeatherData(WeatherData data) {
		String sql = "UPDATE WEATHER_DATA SET `CONDITION` = ?, LOW_TEMP = ?, HIGH_TEMP = ? WHERE ZIPCODE = ?;";
		try {
			jdbcTemplate.update(sql, new Object[] { data.getText(), data.getLow(), data.getHigh(), data.getZipCode() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* Get Weather data corresponding to a zipcode */
	public WeatherData getWeatherData(String zipCode) {
		WeatherData data = null;
		String sql = "select * from WEATHER_DATA where ZIPCODE=?";
		try {
			data = jdbcTemplate.queryForObject(sql, new Object[] { zipCode }, new WeatherDataMapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	private static final class WeatherDataMapper implements RowMapper<WeatherData> {

		@Override
		public WeatherData mapRow(ResultSet resultSet, int arg1) throws SQLException {
			WeatherData data = new WeatherData();
			data.setZipCode(resultSet.getString("ZIPCODE"));
			data.setHigh(resultSet.getInt("HIGH_TEMP"));
			data.setLow(resultSet.getInt("LOW_TEMP"));
			data.setText(resultSet.getString("CONDITION"));
			return data;
		}

	}

}
