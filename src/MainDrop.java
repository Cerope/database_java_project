import java.io.IOException;
import java.sql.SQLException;

public class MainDrop {
	public static void main(String[] args) throws IOException, SQLException {
		String USER = "charbona";
		String PASSWD = "charbona";

		// DROP
		SqlRequest base = new SqlRequest(USER, PASSWD);
		base.drop();
		base.closeConnection();
	}
}
