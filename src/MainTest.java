import java.io.IOException;
import java.sql.SQLException;

public class MainTest {
	
	public static void main(String[] args) throws IOException, SQLException {
		String USER = "charbona";
		String PASSWD = "charbona";

		// CREATION D'UNE BASE DE TEST
		SqlRequest base = new SqlRequest(USER, PASSWD);
		base.drop();
		base.initTables();
		
		
		// ARTISTES
		CreateTables randArtiste = new CreateTables();
		System.out.print("Generating data...");
		randArtiste.generate(base.getDriver(), 100);
		System.out.println(" data generated.");
		
		// FERMETURE BASE
		base.closeConnection();

	}
}
