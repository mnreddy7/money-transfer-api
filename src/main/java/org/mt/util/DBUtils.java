package org.mt.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.mt.dao.MTDao;
import org.mt.model.Account;

public class DBUtils {

	//"jdbc:h2:~/mtapiDB2;DB_CLOSE_ON_EXIT=FALSE"
	public static final String CRT_URL = "jdbc:h2:mem:mtapiDB007;INIT=RUNSCRIPT FROM 'classpath:h2db.sql'";
	public static final String URL = "jdbc:h2:mem:mtapiDB007";
	public static final String USER = "sa";
	public static final String PASSWORD = "";
	public static final String DRIVER = "org.h2.Driver";

	private static Connection conn = null;

	public static Connection getConnection() {

		try {
			if (null == conn || conn.isClosed())

			{  
				try {
					DbUtils.loadDriver(DRIVER);
					conn = DriverManager.getConnection(URL, USER, PASSWORD);
				} catch (SQLException e) {

					try {
						Thread.sleep(5000);
					} catch (InterruptedException e2) {
						e2.printStackTrace();
					}

					try {
						conn = DriverManager.getConnection(URL, USER, PASSWORD);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;

	}

	public static void main(String... args) {

		try {
			DbUtils.loadDriver(DRIVER);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			QueryRunner runner = new QueryRunner();

//			ScalarHandler<Long> scalarHandler = new ScalarHandler<Long>();
//			long count = runner.query(conn, "SELECT COUNT(*) FROM ACCOUNT", scalarHandler);
//			System.out.println("count = " + count);

//			Account account = runner.query(conn, "select * from Account where accountNumber='50100200879179'",
//					new BeanHandler<Account>(Account.class));

			Account account = runner.query(conn, MTDao.GET_ACCT_QUERY, new BeanHandler<Account>(Account.class),
					"50100200879179");
			System.out.println(account);
			int count = runner.update(conn, MTDao.UPDATE_ACCT_QUERY, "146769.54", "50100200879179");
			System.out.println("updated row count = " + count);
			Account updated = runner.query(conn, MTDao.GET_ACCT_QUERY, new BeanHandler<Account>(Account.class),
					"50100200879179");
			System.out.println(updated);

//			int count = runner.update(conn,MTDao.TXN_INSERT_QUERY,"C","50100200879179","50100200315840","25000","Y");
//			System.out.println("count = " + count);			
//			Transaction txn = runner.query(conn, "select * from Transaction where debitAccNo = ? ", new BeanHandler<Transaction>(Transaction.class), "50100200879179");
//			System.out.println(txn);

		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	// ;IFEXISTS=TRUE;IGNORECASE=TRUE;DB_CLOSE_DELAY=-1
}
