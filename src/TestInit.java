import java.sql.*;

public class TestInit {

	public static void main(String[] arg) {

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			System.out.println("driver ok !");
			String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
			String user = "nobletb";
			String passwd = "nobletb";
			Connection connection = DriverManager.getConnection(url, user, passwd);
			System.out.println("connected successfully ! ");
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1",
					"nobletb", "nobletb");
			System.out.println("connected");

			// Creation de la requete
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM emp");
			// Execution de la requete
			ResultSet rset = stmt.executeQuery();
			dumpResultSet(rset);
			System.out.println("request successful");
			rset.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("request failed");
		}

	}

	public static void dumpResultSet(ResultSet rset) throws SQLException {
		ResultSetMetaData rsetmd = rset.getMetaData();
		int i = rsetmd.getColumnCount();
		while (rset.next()) {
			for (int j = 1; j <= i; j++) {
				System.out.print(rset.getString(j) + "\t");
			}
			System.out.println();
		}
	}

}
