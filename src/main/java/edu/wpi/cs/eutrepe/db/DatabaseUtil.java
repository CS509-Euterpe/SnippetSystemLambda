package edu.wpi.cs.eutrepe.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

	// DB user names and passwords (as well as the db endpoint) should never be stored directly in code.
	//
	// https://docs.aws.amazon.com/lambda/latest/dg/env_variables.html
	//
	// The above link shows how to ensure Lambda function has access to environment as well as local
	public final static String jdbcTag = "jdbc:mysql://";
	public final static String rdsMySqlDatabasePort = "3306";
	public final static String multiQueries = "?allowMultiQueries=true";
	   
	// Make sure matches Schema created from MySQL WorkBench
	// Make sysEnv variable lambdaTesting so we know we are locally testing
	public final static String lambdaTesting = "lambdaTesting";
	public final static String dbName = "Default";
	public final static String testName = "test";
	final static String dbUsername = "admin";
	final static String dbPassword = "Benzonatate";
	final static String rdsMySqlDatabaseUrl = "database-1.cnejvyieslew.us-east-1.rds.amazonaws.com";
	
	// pooled across all usages.
	static Connection conn;
 
	/**
	 * Singleton access to DB connection to share resources effectively across multiple accesses.
	 */
	protected static Connection connect() throws Exception {
		if (conn != null) { return conn; }
		
		// this is resistant to any SQL-injection attack.
		String schemaName = dbName;
		String test = System.getenv("lambdaTesting");
		if (test != null) {
			schemaName = testName;
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			conn = DriverManager.getConnection(
					jdbcTag + rdsMySqlDatabaseUrl + ":" + rdsMySqlDatabasePort + "/" + schemaName + multiQueries,
					dbUsername,
					dbPassword);
			return conn;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.err.println("DB-ERROR:" + schemaName + "," + dbUsername + "," + dbPassword + "," + rdsMySqlDatabaseUrl);
			throw new Exception("Failed in database connection");
		}
	}
}
