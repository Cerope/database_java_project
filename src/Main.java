import java.io.IOException;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws IOException, SQLException {
		String USER = "charbona";
		String PASSWD = "charbona";
		SqlRequest base = new SqlRequest(USER, PASSWD);
		user_interface interface_utilisateur = new user_interface();
		interface_utilisateur.menuPrincipal(base);
		base.closeConnection();
	}
}
