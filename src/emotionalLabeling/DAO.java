package emotionalLabeling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DAO {

	private static Connection conn = null;

	public static Connection conn() throws ClassNotFoundException, SQLException {
		final String DRIVER = "org.postgresql.Driver";
		final String DB = "jdbc:postgresql://localhost:5432/textClassify";
		final String USER = "";
		final String PSW = "";
		Class.forName(DRIVER);
		if (conn == null) {
			conn = DriverManager.getConnection(DB, USER, PSW);
			return conn;
		} else {
			return conn;
		}
	}
}
