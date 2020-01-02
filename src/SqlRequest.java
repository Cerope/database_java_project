import java.sql.*;

public class SqlRequest {
	private Connection driver;
	static final String CONN_URL = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
    private String USER;
    private String PASSWD;
	
	public SqlRequest(Connection driver) {
		this.driver = driver;
		//this.drop(); allow to drop the table at the end of the script if it's needed
		try {
			this.initTables();			
		}
		catch(SQLSyntaxErrorException e){
			
		}
	}
	
	public SqlRequest(String USER, String PASSWD){
		this.USER = USER;
		this.PASSWD = PASSWD;
		this.connection();
		try {
			this.initTables();			
		}
		catch(SQLSyntaxErrorException e){

		}
		//this.drop(); allow to drop the table at the end of the script if it's needed
		//this.closeConnection();
	}
	
	public boolean request(String pre_stmt) { // execute une requete sans retourner de résultat
		try {
			PreparedStatement stmt = driver.prepareStatement(pre_stmt);
			ResultSet rset = stmt.executeQuery();
			rset.close();
			stmt.close();
			return true;

		} catch (SQLException e) {
			return false;
			//System.err.println("Failed");
			//e.printStackTrace(System.err);
		}

	}
	
	public ResultSet requestReturn(String pre_stmt) { // id mais retourne un resultat
		try {
			PreparedStatement stmt = driver.prepareStatement(pre_stmt);
			ResultSet rset = stmt.executeQuery();
			return rset;

		} catch (SQLException e) {
			return null;
			//System.err.println("Failed");
			//e.printStackTrace(System.err);
		}
	}
	
	private void connection() {
        try {
	    // Enregistrement du driver Oracle
	    System.out.print("Loading Oracle driver... ");
	    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        System.out.println("loaded");

	    // Etablissement de la connection
	    System.out.print("Connecting to the database... "); 
	    this.driver = DriverManager.getConnection(CONN_URL, USER, PASSWD);
        System.out.println("connected");
        } catch (SQLException e) {
        	System.err.println("failed");
            e.printStackTrace(System.err);
        }
	}
	
	public void closeConnection() {
		System.out.print("Closing connection...");
		try {
			this.driver.close();
			System.out.println("connection closed");
			
		} catch (SQLException e) {
			System.err.println("failed to close connection");
        	e.printStackTrace(System.err);
		}
	}
	
	public void initTables() throws SQLSyntaxErrorException {
		
		String pre_stmt = "CREATE TABLE ARTISTE"
		        + "(idArtiste INTEGER PRIMARY KEY NOT NULL, "
		        + "nom VARCHAR(100), "
		        + "prenom VARCHAR(100), "
		        + "dateNaissance DATE, "
		        + "cirqueOrigine VARCHAR(100), "
		        + "numeroTel VARCHAR(10))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE THEME"
		        + "(theme VARCHAR(100) PRIMARY KEY NOT NULL)";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE PSEUDOS"
		        + "(pseudo VARCHAR(100) PRIMARY KEY NOT NULL)";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE NUMEROCANDIDAT"
		        + "(codeNumeroCandidat INTEGER PRIMARY KEY NOT NULL, "
		        + "titreNumero VARCHAR(100), "
		        + "resume VARCHAR(500), "
		        + "dureeNumero INTEGER, "
		        + "nombreArtistes INTEGER CHECK (nombreArtistes > 0), "
		        + "artistePrincipal INTEGER, "
		        + "theme VARCHAR(100) NOT NULL REFERENCES THEME, "
		        + "CONSTRAINT check_duree CHECK (dureeNumero>=10 AND dureeNumero<=30))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE SPECTACLE"
		        + "(idSpectacle INTEGER PRIMARY KEY NOT NULL, "
		        + "jourSpectacle DATE, "
		        + "heureDebut INTEGER, "
		        + "prixSpectacle INTEGER, "
		        + "CONSTRAINT check_prix CHECK (prixSpectacle>=0), "
		        + "CONSTRAINT check_time CHECK (heureDebut=9 OR heureDebut=14))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE A_PRESENTE_S"
		        + "(idArtiste INTEGER NOT NULL REFERENCES ARTISTE, "
		        + "idSpectacle INTEGER PRIMARY KEY NOT NULL REFERENCES SPECTACLE)";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE NC_PRESENTEDANS_S"
		        + "(codeNumeroCandidat INTEGER PRIMARY KEY NOT NULL REFERENCES NUMEROCANDIDAT, "
		        + "idSpectacle INTEGER NOT NULL REFERENCES SPECTACLE)";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE A_PARTICIPEA_NC"
				+ "(idArtiste INTEGER NOT NULL REFERENCES ARTISTE, "
				+ "codeNumeroCandidat INTEGER NOT NULL REFERENCES NUMEROCANDIDAT, "
				+ "PRIMARY KEY (idArtiste, codeNumeroCandidat))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE A_APOUR_P"
		        + "(idArtiste INTEGER NOT NULL REFERENCES ARTISTE, "
		        + "pseudo VARCHAR(100) NOT NULL REFERENCES PSEUDOS, "
		        + "PRIMARY KEY (idArtiste, pseudo))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE A_APOUR_T"
		        + "(idArtiste INTEGER NOT NULL REFERENCES ARTISTE, "
		        + "theme VARCHAR(100) NOT NULL REFERENCES THEME, "
				+ "PRIMARY KEY (idArtiste, theme))";
		this.request(pre_stmt);
		
		pre_stmt = "CREATE TABLE EVALUER"
		        + "(idArtiste INTEGER NOT NULL REFERENCES ARTISTE, "
		        + "codeNumeroCandidat INTEGER NOT NULL REFERENCES NUMEROCANDIDAT, "
		        + "evaluation VARCHAR(255), "
		        + "note INTEGER CHECK (note>=0 AND note < 11), "
		        + "PRIMARY KEY (idArtiste, codeNumeroCandidat))";
		this.request(pre_stmt);
	}

	public boolean verifID(int id, String table) throws SQLException {
		String pre_stmt = "SELECT COUNT(*) FROM " + table + " WHERE (IDARTISTE = " + id + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		if (res.getInt(1) != 1) {
			return false;
		}
		return true;
	}
	
//	public boolean verifSpectacle(int id) throws SQLException {
//		String pre_stmt = "SELECT COUNT(*) FROM SPECTACLE WHERE (idSpectacle = " + id + ")";
//		ResultSet res = this.requestReturn(pre_stmt);
//		res.next();
//		if (res.getInt(1) != 1) {
//			return false;
//		}
//		return true;
//	}
//	
	public boolean verifNC(String id) throws SQLException {
		String pre_stmt = "SELECT COUNT(*) FROM NUMEROCANDIDAT WHERE (codeNumeroCandidat = " + id + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		if (res.getInt(1) != 1) {
			return false;
		}
		return true;
	}
	
	public boolean verifTheme(String theme, String idArtiste) throws SQLException {
		String pre_stmt = "SELECT COUNT(*) FROM A_APOUR_T WHERE (THEME = " + theme + " AND idArtiste = " + idArtiste +")";
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		if (res.getInt(1) != 1) {
			return false;
		}
		return true;
	}
	
	public boolean checkNC_hasSpectacle(String id) throws SQLException {
		String pre_stmt = "SELECT IDSPECTACLE FROM NC_PRESENTEDANS_S WHERE CODENUMEROCANDIDAT = "+id;
		ResultSet res = this.requestReturn(pre_stmt);
		if(!res.next()) { // Si la reponse est vide ie si aucun spectacle associé au codenumerocandidat
			return false;
		}
		return true;
	}
	
	public boolean presentatorIsNumberArtist(String idArtiste, String idSpectacle) throws SQLException {
		String codeNumeroCandidat;
		String pre_stmt = "SELECT CODENUMEROCANDIDAT FROM NC_PRESENTEDANS_S WHERE IDSPECTACLE = "+idSpectacle;
		ResultSet res = this.requestReturn(pre_stmt);
		if(!res.next())
		{
			//Ne devrait pas arriver, juste au cas où
		    System.out.println("No Data Found");
		}
		else{
			do
			{
			   codeNumeroCandidat = res.getString(1);
			   if(artisteInNum(idArtiste, codeNumeroCandidat)) {
				   return true;
			   }
			} while(res.next());
		}
		
		return false;
	}
	public String getCirque(String idArtiste) throws SQLException {
		String pre_stmt = "SELECT CIRQUEORIGINE FROM ARTISTE WHERE (IDARTISTE = " + idArtiste + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		return res.getString(1);
	}
	
	public String getTheme(String codeNumeroCandidat) throws SQLException {
		String pre_stmt = "SELECT THEME FROM NUMEROCANDIDAT WHERE CODENUMEROCANDIDAT = "+codeNumeroCandidat;
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		return res.getString(1);
	}
	
	public void getThemeArtiste(String idArtiste) throws SQLException {
		// Renvoie les thèmes de l'artiste
		String pre_stmt = "SELECT THEME FROM A_APOUR_T WHERE IDARTISTE = " + idArtiste;
		ResultSet res = this.requestReturn(pre_stmt);
		this.dumpResultSet(res);
	}
	
	public int dureeNumeroCandidat(String id) throws SQLException{
		String pre_stmt = "SELECT DUREENUMERO FROM NUMEROCANDIDAT WHERE CODENUMEROCANDIDAT = "+id;
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		return res.getInt(1);
	}
	public boolean artisteInNum(String idArtiste, String numeroCandidat) throws SQLException {
		String pre_stmt = "SELECT COUNT(*) FROM A_PARTICIPEA_NC WHERE (IDARTISTE = " + idArtiste + " AND CODENUMEROCANDIDAT = " + numeroCandidat + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		res.next();
		if (res.getInt(1) != 1) {
			return false;
		}
		return true;
	}
	
	public boolean nbrSuffisant(String theme, String cirque, String n) throws SQLException {
		String pre_stmt = "SELECT DISTINCT IDARTISTE FROM A_APOUR_T WHERE (THEME = "+ theme + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		int nbr = 0;
		String idArtiste = "";
		while(res.next()) {
			idArtiste = res.getString(1);
			if (this.getCirque(idArtiste).equals(cirque)) nbr += 1;
		}
 		if (nbr < Integer.parseInt(n)) {
			return false;
		}
		return true;
	}
	
	public void afficherArtistesPossibles(String theme, String cirque) throws SQLException{
		String pre_stmt = "SELECT DISTINCT IDARTISTE FROM A_APOUR_T WHERE (THEME = "+ theme + ")";
		ResultSet res = this.requestReturn(pre_stmt);
		ResultSet res2;
		String idArtiste = "";
		while(res.next()) {
			idArtiste = res.getString(1);
			if (this.getCirque(idArtiste).equals(cirque)) {
				res2 = this.requestReturn("SELECT * FROM ARTISTE WHERE (IDARTISTE = " + idArtiste + ")");
				this.dumpResultSet(res2);
			}
		}
	}
	
	public void affichageTable(String table) throws SQLException {
		String pre_stmt = "SELECT * FROM " + table;
		ResultSet rset = this.requestReturn(pre_stmt);
		this.dumpResultSet(rset);
	}
	
	public void affichageTable(String table, String condition) throws SQLException {
		String pre_stmt = "SELECT * FROM " + table + " WHERE (" + condition +")";
		ResultSet rset = this.requestReturn(pre_stmt);
		this.dumpResultSet(rset);
	}
	
	public void afficheArtisteDispo(String idSpectacle) throws SQLException{
		String pre_stmt = "SELECT IDARTISTE, NOM, PRENOM FROM ARTISTE " + 
				"WHERE IDARTISTE NOT IN " + 
				"(SELECT IDARTISTE " + 
				"FROM A_PARTICIPEA_NC " + 
				"INNER JOIN NC_PRESENTEDANS_S " + 
				"ON A_PARTICIPEA_NC.CODENUMEROCANDIDAT = NC_PRESENTEDANS_S.CODENUMEROCANDIDAT " + 
				"WHERE IDSPECTACLE = "+ idSpectacle+   ")";
		ResultSet rset = this.requestReturn(pre_stmt);
		this.dumpResultSet(rset);
	}
	
	public void drop() {
		String pre_stmt = "DROP TABLE A_PRESENTE_S";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE A_PARTICIPEA_NC";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE A_APOUR_T";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE A_APOUR_P";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE NC_PRESENTEDANS_S";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE PSEUDOS";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE SPECTACLE";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE EVALUER";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE NUMEROCANDIDAT";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE THEME";
		this.request(pre_stmt);
		pre_stmt = "DROP TABLE ARTISTE";
		this.request(pre_stmt);
	}
	
	public boolean insertion(String table_name, String... values) {
		String requete = "INSERT INTO " + table_name + " ";
		switch (table_name) {
		case "ARTISTE":
			requete += "(idArtiste, nom, prenom, dateNaissance, cirqueOrigine, numeroTel) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "A_PARTICIPEA_NC":
			requete += "(idArtiste, codeNumeroCandidat) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "NUMEROCANDIDAT":
			requete += "(codeNumeroCandidat, titreNumero, resume, dureeNumero, nombreArtistes,"
					+ " artistePrincipal, theme) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "SPECTACLE":
			requete += "(idSpectacle, jourSpectacle, heureDebut, prixSpectacle) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "A_PRESENTE_S":
			requete += "(idArtiste, idSpectacle) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;	
		case "NC_PRESENTEDANS_S":
			requete += "(codeNumeroCandidat, idSpectacle) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "PSEUDOS":
			requete += "(pseudo) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "A_APOUR_P":
			requete += "(idArtiste, pseudo) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "THEME":
			requete += "(theme) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;
		case "A_APOUR_T":
			requete += "(idArtiste, theme) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}

			break;
		case "EVALUER":
			requete += "(idartiste, codeNumeroCandidat, evaluation, note) VALUES (" + values[0];
			for (int i = 1; i < values.length; i++) {
				requete += ", " + values[i];
			}
			break;

		}
		requete += ")";
		//System.out.println("la requete envoyee : " + requete);
		return this.request(requete);
	}

	public String getFirstIdOpen(String table_name, String attribut) throws SQLException {
		int id_max = 0;
		String pre_stmt = "SELECT MAX(" + attribut + ") FROM " + table_name;
		
		ResultSet res = this.requestReturn(pre_stmt);
		
		try {
			if(res.next()) {
				id_max = res.getInt(1);
			}
		} catch (SQLException e) {
			
		}
	    id_max ++; 
	    String str = "" + id_max;
		return str;
	}
	
	public Connection getDriver() {
		return this.driver;
	}
	
	public void dumpResultSet(ResultSet rset) throws SQLException{
		try {
			ResultSetMetaData rsetmd = rset.getMetaData();
			int i = rsetmd.getColumnCount();
			while (rset.next()) {
				for (int j = 1; j <= i; j++) {
					System.out.print(rset.getString(j) + "\t");
					}
				System.out.println();
			}
		} catch (SQLException e) {
            System.err.println("failed");
            e.printStackTrace(System.err);
        }
	}

}
