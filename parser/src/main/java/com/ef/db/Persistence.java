package com.ef.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class Persistence {
	private MysqlDataSource myDataSource;

	public Persistence() {
		this.myDataSource = new MysqlDataSource();
		myDataSource.setUser("root"); // change this value
		myDataSource.setPassword("Q26BydDmy"); // change this value
		myDataSource.setServerName("localhost");
		myDataSource.setDatabaseName("parser");
	}

	public List<Object[]> executeSelect(String query) {
		ArrayList<Object[]> resp = new ArrayList<Object[]>();
		try {
			final Connection con = myDataSource.getConnection();
			final Statement stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery(query);

			while (rs.next())
				resp.add(new String[] { rs.getString("ip") });
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return resp;
	}

	public void insertFile(File accesslog) throws IOException {
		final FileReader fr = new FileReader(accesslog);
		final BufferedReader br = new BufferedReader(fr);
		try {
			final Connection con = myDataSource.getConnection();
			final Statement stmt = con.createStatement();

			String line;
			while ((line = br.readLine()) != null) {
				String values[] = line.split("\\|");
				line = "INSERT INTO requests VALUES ('" + values[0] + "','" + values[1] + "'," + values[2] + ","
						+ values[3] + "," + values[4] + ");";
				stmt.executeUpdate(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}

	public List<String> searchViolations(String startDate, int threshold, String duration) {
		String query = "SELECT counter.ip,counter.attempts FROM (SELECT ip,count(ip) AS attempts FROM requests WHERE date BETWEEN '"
				+ startDate + "' AND DATE_ADD('" + startDate + "', INTERVAL 1 "
				+ (duration.equals("daily") ? "DAY" : "HOUR") + ") GROUP BY ip) counter WHERE counter.attempts >= "
				+ threshold;

		String ipBlocked = "";
		Integer attempts = 0;

		ArrayList<String> resp = new ArrayList<String>();
		ArrayList<String> blockedQueries = new ArrayList<String>();

		try {
			final Connection con = myDataSource.getConnection();
			final Statement stmt = con.createStatement();
			final ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				ipBlocked = rs.getString("ip");
				attempts = rs.getInt("attempts");
				resp.add(ipBlocked);
				blockedQueries.add("INSERT INTO blocked VALUES (NOW(),'" + ipBlocked + "'," + attempts + ",'" + duration
						+ "','The IP:" + ipBlocked + " has been blocked due to " + attempts
						+ " attempts during a period of one " + ((duration.equals("daily") ? "day" : "hour")) + "')");
			}
			stmt.close();
			con.close();

			block(blockedQueries);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	private void block(List<String> blockedQueries) {
		try {
			final Connection con = myDataSource.getConnection();
			final Statement stmt = con.createStatement();

			for (String query : blockedQueries) {
				stmt.executeUpdate(query);
			}
			stmt.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
