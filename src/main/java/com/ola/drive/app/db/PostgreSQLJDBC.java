package com.ola.drive.app.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * Singleton class which facilitates/manages connections to underlying Postgres 
 * database using DBCP connection pooling
 * 
 * @author aarishramesh
 *
 */
public class PostgreSQLJDBC {
	
	private static final String SERVER = "jdbc:postgresql://driveapp.cbq6nrdioaos.us-east-2.rds.amazonaws.com:5432/ola";
	private static final String USER = "driveapp";
	private static final String PASSWORD = "driveapp55";
	
	private static PostgreSQLJDBC INSTANCE = new PostgreSQLJDBC();

	public static PostgreSQLJDBC getInstance() {
		return INSTANCE;
	}

	private BasicDataSource datasource;

	public PostgreSQLJDBC() {
		initDataSource();
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		getInstance().connect();
	}
	
	private void initDataSource() {
		datasource = new BasicDataSource();
		datasource.setDriverClassName("org.postgresql.Driver");
		datasource.setUrl(SERVER);
		datasource.setInitialSize(5);
		datasource.setMaxActive(20);
		datasource.setMaxWait(2000);
		datasource.setUsername(USER);
		datasource.setPassword(PASSWORD);
		datasource.setTestOnBorrow(true);
		datasource.setTestOnReturn(true);
	}
	
	public Connection connect() throws ClassNotFoundException, SQLException {
		return datasource.getConnection();
	}
}
