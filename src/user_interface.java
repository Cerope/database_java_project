import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class user_interface {
	private static boolean exit = false;

	public static boolean getExit() {
		return exit;
	}

	public static void switchExit() {
		exit = !exit;
	}

	public user_interface() {

	}

	public user_interface(SqlRequest base) throws IOException, SQLException {
		this.menuPrincipal(base);
	}

	public void insert_person(SqlRequest base) throws IOException, SQLException {
		
		Connection conn = base.getDriver();
		
		Boolean succeeded = true;
		conn.setAutoCommit(false);
		conn.commit();
		
		// init table est dans le constructeur
		SqlRequest sqlRequest = new SqlRequest(conn);
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));

		// System.out.println("idArtiste(int)");
		String idArtiste = base.getFirstIdOpen("ARTISTE", "IDARTISTE");
		System.out.println("Nom");
		String nom = "'" + ob.readLine() + "'";
		System.out.println("Prenom");
		String prenom = "'" + ob.readLine() + "'";
		System.out.println("dateNaissance(DD/MM/YYYY)");
		String dateNaissance = "TO_DATE('" + ob.readLine() + "', 'DD/MM/YYYY')";
		System.out.println("cirqueOrigine");
		String cirqueOrigine = "'" + ob.readLine() + "'";
		System.out.println("numeroTel");
		String numeroTel = ob.readLine();
		succeeded = succeeded & sqlRequest.insertion("ARTISTE", idArtiste, nom, prenom, dateNaissance, cirqueOrigine, numeroTel);

		System.out.println("Combien de pseudos ?");
		Scanner in = new Scanner(System.in);
		int nb_pseudos = in.nextInt();
		while (nb_pseudos < 1) {
			System.out.println("Erreur, minimum 1 pseudo");
			System.out.println("Combien de pseudos ?");
			in = new Scanner(System.in);
			nb_pseudos = in.nextInt();
		}
		for (int i = 0; i < nb_pseudos; i++) {
			System.out.println("Quel est son pseudo ?");
			String pseudo = "'" + ob.readLine() + "'";
			sqlRequest.insertion("PSEUDOS", pseudo);
			succeeded = succeeded & sqlRequest.insertion("A_APOUR_P", idArtiste, pseudo);
		}
		System.out.println("Combien de thèmes ?");
		in = new Scanner(System.in);
		int nb_themes = in.nextInt();
		while (nb_themes < 1) {
			System.out.println("Erreur, minimum 1 thème");
			System.out.println("Combien de thèmes ?");
			in = new Scanner(System.in);
			nb_themes = in.nextInt();
		}
		for (int i = 0; i < nb_themes; i++) {
			System.out.println("Quel est son thème ?");
			String theme = "'" + ob.readLine() + "'";
			sqlRequest.insertion("THEME", theme);
			succeeded = succeeded & sqlRequest.insertion("A_APOUR_T", idArtiste, theme);
		}
		if (!succeeded) {
			conn.rollback();
			System.out.println("Il y a eu un problème. Veuillez réessayer s'il vous plait.");
		}
		conn.commit();
		conn.setAutoCommit(true);
	}

	public void insert_numero(SqlRequest base) throws IOException, SQLException {
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));

		Connection conn = base.getDriver();
		
		Boolean succeeded = true;
		conn.setAutoCommit(false);
		conn.commit();
		
		
		
		// On demande le titre
		String codeNumeroCandidat = base.getFirstIdOpen("NUMEROCANDIDAT", "CODENUMEROCANDIDAT");
		System.out.println("titreNumero :");
		String titreNumero = "'" + ob.readLine() + "'";

		// On demande le résumé
		System.out.println("resume");
		String resume = "'" + ob.readLine() + "'";

		// On demande la durée et on vérifie la contrainte de durée
		System.out.println("dureeNumero :");
		String dureeNumero = ob.readLine();
		int duree = Integer.parseInt(dureeNumero);
		while (duree > 30 || duree < 10) {
			System.out.println("Entrez un entier compris entre 10 et 30 :");
			dureeNumero = ob.readLine();
			duree = Integer.parseInt(dureeNumero);
		}

		// On demande le nombre d'artiste (>0)
		System.out.println("nombreArtistes :");
		String nombreArtistes = ob.readLine();
		int nbr = Integer.parseInt(nombreArtistes);
		while (nbr < 1) {
			System.out.println("Il doit y avoir plus d'un artiste. Entrez le nombre d'artiste :");
			nombreArtistes = ob.readLine();
			nbr = Integer.parseInt(nombreArtistes);
		}

		// On demande l'artiste principale
		base.affichageTable("ARTISTE");
		System.out.println("\nartistePrincipal :");
		String artistePrincipal = ob.readLine();
		while (base.verifID(Integer.parseInt(artistePrincipal), "ARTISTE") == false) {
			System.out.println("Cet artiste n'existe pas, veuillez recommencer :");
			artistePrincipal = ob.readLine();
		}
		String cirque = base.getCirque(artistePrincipal);

		// On demande le thème du spéctacle
		System.out.println("Thèmes possibles :");
		base.getThemeArtiste(artistePrincipal);
		System.out.println("\ntheme du numéro :");
		String theme = "'" + ob.readLine() + "'";
		while (base.verifTheme(theme, artistePrincipal) == false) {
			System.out.println("Le thème n'est pas valide, veuillez recommencer :");
			theme = "'" + ob.readLine() + "'";
		}

		// On vérifie qu'il y a assez d'artistes compatible
		if (!base.nbrSuffisant(theme, cirque, nombreArtistes)) { // on vérifie si il y a assez d'artistes.
			System.out.println("Il n'y a pas assez d'artistes valide pour faire ce numéro.");
			return;
		}

		// Si il y en a assez, on creer le numéro
		succeeded = succeeded & base.insertion("NUMEROCANDIDAT", codeNumeroCandidat, titreNumero, resume, dureeNumero, nombreArtistes,
				artistePrincipal, theme);

		// On ajoute l'artiste principale
		System.out.println("L'artiste principal est l'artiste numéro 1 du spectacle.");
		succeeded = succeeded & base.insertion("A_PARTICIPEA_NC", artistePrincipal, codeNumeroCandidat);

		// On ajoute les autres artistes
		base.afficherArtistesPossibles(theme, base.getCirque(artistePrincipal));
		for (int i = 1; i < Integer.parseInt(nombreArtistes); i++) {
			System.out.println("idArtiste n°" + (i + 1) + " :");
			String idArtiste = ob.readLine();
			while (base.verifTheme(theme, idArtiste) == false || !base.getCirque(idArtiste).equals(cirque)
					|| base.artisteInNum(idArtiste, codeNumeroCandidat)) {
				System.out.println("Cet artiste n'est pas valide ou n'existe pas. Veuillez recommencer :");
				idArtiste = ob.readLine();
			}
			succeeded = succeeded & base.insertion("A_PARTICIPEA_NC", idArtiste, codeNumeroCandidat);
		}
		if (!succeeded) {
			conn.rollback();
			System.out.println("Il y a eu un problème. Veuillez réessayer s'il vous plait.");
		}
		conn.commit();
		conn.setAutoCommit(true);
	}
	

	public void insert_spectacle(SqlRequest base) throws IOException, SQLException {
		Connection conn = base.getDriver();


		Boolean succeeded = true;
		conn.setAutoCommit(false);
		conn.commit();

		
		// init table est dans le constructeur
		SqlRequest sqlRequest = new SqlRequest(conn);
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));

		String idSpectacle = base.getFirstIdOpen("SPECTACLE", "IDSPECTACLE");
		System.out.println("jourSpectacle (DD/MM/YYYY)");
		String jourSpectacle = "TO_DATE('" + ob.readLine() + "', 'DD/MM/YYYY')";
		System.out.println("heure de début (9 ou 14)");
		String heureDebut = ob.readLine();
		int heure = Integer.parseInt(heureDebut);
		while (heure != 9 && heure != 14) {
			System.out.println("Le spectacle doit commencer à 9h ou 14h");
			System.out.println("heureDebut (int)");
			heureDebut = ob.readLine();
			heure = Integer.parseInt(heureDebut);
		}
		System.out.println("prixSpectacle");
		String prixSpectacle = ob.readLine();
		int prix = Integer.parseInt(prixSpectacle);
		while (prix < 0) {
			System.out.println("Entrez un prix positif ou nul");
			System.out.println("prixSpectacle");
			prixSpectacle = ob.readLine();
			prix = Integer.parseInt(prixSpectacle);
		}
		succeeded = succeeded & sqlRequest.insertion("SPECTACLE", idSpectacle, jourSpectacle, heureDebut, prixSpectacle);

		boolean sortie = false;
		int temps_tot = 0;
		int nombre_nc = 0;
		String theme_commun = null;
		String theme;
		String codeNumeroCandidat;
		System.out.println(
				"Quels sont les codeNumeroCandidat(int) qui vont dans ce spectacle (entrez 'fin' quand vous avez fini)");
		base.affichageTable("NUMEROCANDIDAT");
		System.out.println("\n");
		while (!sortie) {
			codeNumeroCandidat = ob.readLine();
			if (codeNumeroCandidat.equals("fin")) {
				// VERIFICATION DEX CONTRAINTES : UN OU PLUSIEURS NC DANS UN SPECTACLE
				if (nombre_nc == 0) {
					System.out.println("Erreur : il faut un moins un NumeroCandidat dans un spectacle");
				} else {
					sortie = true;
				}

			} else {
				// VERIFICATION DES CONTRAINTES, EXISTENCE
				if (base.verifNC(codeNumeroCandidat) == false) {
					System.out.println("Ce codeNumeroCandidat n'existe pas, veuillez recommencer : ");
				}
				// VERIFICATION DES CONTRAINTES : UN NC PEUT AVOIR AU PLUS UN SPECTACLE
				else if (base.checkNC_hasSpectacle(codeNumeroCandidat)) {
					System.out.println("Ce NumeroCandidat a déjà un spectacle, veuillez recommencer :");
				}
				// VERIFICATION DES CONTRAINTES : LA SOMME DES DUREES DES NC D'UN SPECTACLE <=
				// 180 min
				else if (temps_tot + base.dureeNumeroCandidat(codeNumeroCandidat) > 180) {
					System.out
							.println("Erreur : la somme des durées des numeros depasse 180 min, veuillez recommencer");
				}
				// VERIF DES CONTRAINTES : TOUS LES NC D'UN SPECTACLE ONT LE MEME THEME
				// 2 cas, 1ere insertion devient le theme
				else if (nombre_nc == 0) {
					theme_commun = base.getTheme(codeNumeroCandidat);
					// OK POUR INSERTION :
					succeeded = succeeded & base.insertion("NC_PRESENTEDANS_S", codeNumeroCandidat, idSpectacle);
					nombre_nc += 1;
					System.out.println("OK, 'fin' pour finir.");
				}
				// 2eme cas : il faut comparer les theme
				else if (nombre_nc > 0) {
					theme = base.getTheme(codeNumeroCandidat);
					if (theme.equals(theme_commun)) {
						// OK POUR INSERTION
						succeeded = succeeded & base.insertion("NC_PRESENTEDANS_S", codeNumeroCandidat, idSpectacle);
						nombre_nc += 1;
						System.out.println("OK, 'fin' pour finir");

					} else {
						System.out.println(
								"Erreur : tous les numeroCandidat doivent avoir le meme theme, veuillez recommencer");
					}
				} else {
					// OK POUR INSERTION :
					succeeded = succeeded & base.insertion("NC_PRESENTEDANS_S", codeNumeroCandidat, idSpectacle);
					nombre_nc += 1;
					System.out.println("OK, 'fin' pour finir");

				}
			}

		}

		base.afficheArtisteDispo(idSpectacle);
		System.out.println("\n");
		String idArtiste;
		boolean conditionsVerifiees = false;
		// contraintes : existence et "le presentateur d'un spectacle ne peut pas
		// également être un artiste dans un num de ce spectacle
		while (!conditionsVerifiees) {

			System.out.println("Quel artiste présente ce spectacle ?");
			System.out.println("idArtiste");
			idArtiste = ob.readLine();

			// EXISTENCE
			while (base.verifID(Integer.parseInt(idArtiste), "ARTISTE") == false) {
				System.out.println("Cet artiste n'existe pas, veuillez recommencer :");
				idArtiste = ob.readLine();
			}

			// PRESENTATEUR D'UN SPECTACLE NE PEUT PAS EGALEMENT ETRE UN ARTISTE DANS UN NUM
			// DE CE SPECTACLE
			if (base.presentatorIsNumberArtist(idArtiste, idSpectacle)) {
				System.out.println(
						"Erreur : le presentateur d'un spectacle ne peut pas egalement etre un artiste dans un numero de ce spectacle");
			} else {
				succeeded = succeeded & base.insertion("A_PRESENTE_S", idArtiste, idSpectacle);
				conditionsVerifiees = true;
			}
		}

		// fin contraintes

		if (!succeeded) {
			conn.rollback();
			System.out.println("Il y a eu un problème. Veuillez réessayer s'il vous plait.");
		}
		conn.commit();
		conn.setAutoCommit(true);
	}

	public void insert(SqlRequest base) throws IOException, SQLException {
		Connection conn = base.getDriver();
		System.out.println("Que voulez-vous insérer : ");
		// init table est dans le constructeur
		SqlRequest sqlRequest = new SqlRequest(conn);

		System.out.println("1.ARTISTE");
		System.out.println("2.NUMERO_CANDIDAT");
		System.out.println("3.SPECTACLE");
		String s;
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
		s = ob.readLine();
		if (s.equals("1")) { // ARTISTE
			this.insert_person(base);
		}
		if (s.equals("2")) { // NUMERO_CANDIDAT
			this.insert_numero(base);

		}
		if (s.equals("3")) { // SPECTACLE
			this.insert_spectacle(base);
		}

	}

	public Set<Integer> numerosAEvaluer(Connection conn) throws SQLException {
		String PRE_STMT;

		// numeros avec 1-4 evaluations
		PRE_STMT = "SELECT NUMEROCANDIDAT.codeNumeroCandidat FROM NUMEROCANDIDAT "
				+ "JOIN EVALUER ON NUMEROCANDIDAT.codeNumeroCandidat = EVALUER.codeNumeroCandidat "
				+ "GROUP BY NUMEROCANDIDAT.codeNumeroCandidat HAVING COUNT(NUMEROCANDIDAT.codeNumeroCandidat) < 5";

		Set<Integer> numerosPossibles = queryToKeySet(conn, PRE_STMT, "codeNumeroCandidat");

		// numeros avec 0 evaluations
		PRE_STMT = "SELECT codeNumeroCandidat FROM NUMEROCANDIDAT "
				+ "WHERE codeNumeroCandidat NOT IN (SELECT codeNumeroCandidat FROM EVALUER)";
		numerosPossibles.addAll(queryToKeySet(conn, PRE_STMT, "codeNumeroCandidat"));

		return numerosPossibles;
	}

	public Set<Integer> expertsCompatibles(Connection conn, String numeroSelectionne) throws SQLException {
		String PRE_STMT;
		Set<Integer> expertsPossibles;
		PreparedStatement stmt;
		ResultSet rset;

		// Informations sur le numero

		// cirque du numero
		String cirqueNumero;
		PRE_STMT = "SELECT cirqueOrigine FROM ARTISTE "
				+ "JOIN NUMEROCANDIDAT ON ARTISTE.idArtiste = NUMEROCANDIDAT.artistePrincipal "
				+ "WHERE codeNumeroCandidat = " + numeroSelectionne;
		stmt = conn.prepareStatement(PRE_STMT);
		rset = stmt.executeQuery();
		rset.next(); // pour acceder au premier element
		cirqueNumero = rset.getString(1);

		// theme du numero
		String themeNumero;
		PRE_STMT = "SELECT theme FROM NUMEROCANDIDAT WHERE codeNumeroCandidat = '" + numeroSelectionne + "'";
		stmt = conn.prepareStatement(PRE_STMT);
		rset = stmt.executeQuery();
		rset.next();
		themeNumero = rset.getString(1);

		// nombre d'evalutations deja faites
		int nbEvaluationsFaites = 0;
		PRE_STMT = "SELECT idArtiste FROM EVALUER WHERE codeNumeroCandidat = '" + numeroSelectionne + "'";
		stmt = conn.prepareStatement(PRE_STMT);
		rset = stmt.executeQuery();
		while (rset.next()) {
			nbEvaluationsFaites++;
		}
		// nombre d'evaluations faites par des specialistes
		int nbEvaluationsSpecialistes = 0;
		PRE_STMT = "SELECT A_APOUR_T.idArtiste FROM A_APOUR_T "
				+ "JOIN EVALUER ON EVALUER.idartiste = A_APOUR_T.idartiste " + "WHERE theme = '" + themeNumero + "' "
				+ "AND codeNumeroCandidat = '" + numeroSelectionne + "'";
		stmt = conn.prepareStatement(PRE_STMT);
		rset = stmt.executeQuery();
		while (rset.next()) {
			nbEvaluationsSpecialistes++;
		}

		// etape 2 : trouver les evaluateurs possibles, <15, 3/2, cirque different, pas
		// deja evalue

		// experts avec 1-14 evaluations
		PRE_STMT = "SELECT ARTISTE.idArtiste FROM ARTISTE " + "JOIN EVALUER ON ARTISTE.idArtiste = EVALUER.idArtiste "
				+ "GROUP BY ARTISTE.idArtiste HAVING COUNT(ARTISTE.idArtiste) < 15";
		expertsPossibles = queryToKeySet(conn, PRE_STMT, "idArtiste");

		// experts sans evaluations
		PRE_STMT = "SELECT idArtiste FROM ARTISTE " + "WHERE idArtiste NOT IN (SELECT idArtiste FROM EVALUER)";
		expertsPossibles.addAll(queryToKeySet(conn, PRE_STMT, "idArtiste"));

		// expert donc ne participe a rien
		PRE_STMT = "SELECT idArtiste FROM A_PARTICIPEA_NC";
		expertsPossibles.removeAll(queryToKeySet(conn, PRE_STMT, "idArtiste"));

		// expert ne presente rien
		PRE_STMT = "SELECT artistePrincipal FROM numerocandidat";
		expertsPossibles.removeAll(queryToKeySet(conn, PRE_STMT, "artistePrincipal"));

		
		// un expert n'evalue pas un numero du meme cirque
		PRE_STMT = "SELECT idArtiste FROM ARTISTE " + "WHERE cirqueOrigine = '" + cirqueNumero + "'";
		expertsPossibles.removeAll(queryToKeySet(conn, PRE_STMT, "idArtiste"));

		// un expert n'evalue pas deux fois
		PRE_STMT = "SELECT idArtiste FROM EVALUER " + "WHERE codeNumeroCandidat = " + numeroSelectionne;
		expertsPossibles.removeAll(queryToKeySet(conn, PRE_STMT, "idArtiste"));

		// specialistes
		Set<Integer> specialistes;
		PRE_STMT = "SELECT idArtiste FROM A_APOUR_T WHERE theme = '" + themeNumero + "'";
		specialistes = queryToKeySet(conn, PRE_STMT, "idArtiste");

		// il doit y avoir 3 evaluations de specialistes maximum
		if (nbEvaluationsSpecialistes >= 3) {
			expertsPossibles.removeAll(specialistes); // on retire tous les specialistes
		}
		// il doit y avoir 2 evaluations de non specialistes maximum
		if (nbEvaluationsFaites - nbEvaluationsSpecialistes >= 2) {
			expertsPossibles.retainAll(specialistes); // on ne garde que les specialistes
		}

		return expertsPossibles;
	}

	public String selectionListe(List<Integer> map) throws NumberFormatException, IOException {
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
		String indice = ob.readLine();
		int nbr;
		nbr = Integer.parseInt(indice) - 1;

		while (nbr < 0 || nbr >= map.size()) {
			System.out.println("Ce doit etre un numero dans cette liste");
			indice = ob.readLine();
			nbr = Integer.parseInt(indice) - 1;
		}
		return "" + map.get(nbr);

	}

	public void evaluer(SqlRequest sqlRequest) throws IOException, SQLException {

		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
		String PRE_STMT;
		Connection conn = sqlRequest.getDriver();
		

		Boolean succeeded = true;
		conn.setAutoCommit(false);
		conn.commit();

		
		// etape 1 : trouver les numeros a evaluer
		Set<Integer> numerosPossibles = numerosAEvaluer(conn);

		if (numerosPossibles.isEmpty()) {
			System.out.println("Tous les numeros sont evalues");
			return;
		}

		System.out.println("Les numeros qui necessitent encore des evalutations sont : ");

		PRE_STMT = "SELECT codeNumeroCandidat, titreNumero, resume FROM NUMEROCANDIDAT";
		String[] afficherNumero = { "titreNumero", "resume" };
		List<Integer> map = printTableKeyCondition(conn, PRE_STMT, numerosPossibles, "codeNumeroCandidat",
				afficherNumero);

		String numeroSelectionne;
		try {
			numeroSelectionne = selectionListe(map);
		} catch (NumberFormatException e) {
			System.out.println("retour au menu principal");
			return;
		}

		Set<Integer> expertsPossibles = expertsCompatibles(conn, numeroSelectionne);

		if (expertsPossibles.isEmpty()) {
			System.out.println(
					"Aucun expert n'est compatible pour evaluer ce numero. Veuillez entrer un nouvel expert dans la base");
			return;
		}

		System.out.println("Les experts compatibles sont : ");
		PRE_STMT = "SELECT idArtiste, nom, prenom FROM ARTISTE";

		String[] afficherArtiste = { "nom", "prenom" };
		map = printTableKeyCondition(conn, PRE_STMT, expertsPossibles, "idArtiste", afficherArtiste);

		String expertSelectionne;
		try {
			expertSelectionne = selectionListe(map);
		} catch (NumberFormatException e) {
			System.out.println("retour au menu principal");
			return;
		}

		System.out.println("Evaluation :");
		String evaluation = ob.readLine();
		evaluation = evaluation.replace("'", "''");
		evaluation = "'" + evaluation + "'";
		System.out.println("Note :");
		String note = ob.readLine();
		while (Integer.parseInt(note) > 10 || Integer.parseInt(note) < 0) {
			System.out.println("La note doit être entre 0 et 10 : ");
			note = ob.readLine();
		}

		succeeded = succeeded & sqlRequest.insertion("EVALUER", expertSelectionne, numeroSelectionne, evaluation, note);
		if (!succeeded) {
			conn.rollback();
			System.out.println("Il y a eu un problème. Veuillez réessayer s'il vous plait.");
		}
		conn.commit();
		conn.setAutoCommit(true);
	}

	public void planifier(SqlRequest base) throws IOException, SQLException {
		String PRE_STMT;
		PreparedStatement stmt;
		ResultSet rset;
		ResultSet rset2;
		Connection conn = base.getDriver();
		String theme;
		// PRE_STMT = "SELECT THEME, EVALUER.CODENUMEROCANDIDAT, IDARTISTE, EVALUATION,
		// NOTE \n" +
		// "FROM EVALUER JOIN NUMEROCANDIDAT\n" +
		// "ON EVALUER.CODENUMEROCANDIDAT = NUMEROCANDIDAT.CODENUMEROCANDIDAT\n" +
		// "ORDER BY THEME, EVALUER.CODENUMEROCANDIDAT, NOTE DESC";
		// stmt = conn.prepareStatement(PRE_STMT);
		// System.out.println("Dans cet ordre : idArtiste de l'expert,
		// codeNumeroCandidat, evaluation, note\n");
		// rset = stmt.executeQuery();

		PRE_STMT = "SELECT THEME FROM THEME";
		rset = base.requestReturn(PRE_STMT);
		while (rset.next()) {
			theme = rset.getString(1);
			System.out.println("Theme : " + theme + " (codeNumeroCandidat, idArtiste de l'expert, evaluation, note)");

			PRE_STMT = "SELECT EVALUER.CODENUMEROCANDIDAT, IDARTISTE, EVALUATION, NOTE "
					+ "FROM NUMEROCANDIDAT JOIN EVALUER "
					+ "ON EVALUER.CODENUMEROCANDIDAT = NUMEROCANDIDAT.CODENUMEROCANDIDAT "
					+ "WHERE NUMEROCANDIDAT.THEME = '" + theme + "'";
			rset2 = base.requestReturn(PRE_STMT);
			base.dumpResultSet(rset2);
			rset2.close();

			System.out.println("---------------------------------------------------------------");
		}
		// rset.next();

		base.dumpResultSet(rset);
		// LES WARNINGS
		PRE_STMT = "SELECT COUNT(EVALUER.CODENUMEROCANDIDAT), EVALUER.CODENUMEROCANDIDAT\n"
				+ "FROM EVALUER JOIN NUMEROCANDIDAT\n"
				+ "ON EVALUER.CODENUMEROCANDIDAT = NUMEROCANDIDAT.CODENUMEROCANDIDAT\n"
				+ "GROUP BY EVALUER.CODENUMEROCANDIDAT";
		stmt = conn.prepareStatement(PRE_STMT);
		rset = stmt.executeQuery();
		int codeNumeroCandidat;
		int nb_evaluation;
		System.out.println("");
		while (rset.next()) {
			nb_evaluation = rset.getInt(1);
			codeNumeroCandidat = rset.getInt(2);
			if (nb_evaluation < 5) {
				System.out.println("Attention, le Numero Candidat : " + codeNumeroCandidat + " n'a que " + nb_evaluation
						+ " evaluations, il lui en manque " + (5 - nb_evaluation));
			}
		}
	}

	public Set<Integer> queryToKeySet(Connection conn, String pre_stmt, String cle) throws SQLException {
		try {
			PreparedStatement stmt = conn.prepareStatement(pre_stmt);
			TreeSet<Integer> resultat = new TreeSet<Integer>();
			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {

				resultat.add(Integer.parseInt(rset.getString(cle)));
			}

			stmt.close();
			return resultat;
		} catch (SQLException e) {
			System.out.println("failed query");
			e.printStackTrace();
			return new TreeSet<Integer>();
		}

	}

	public List<Integer> printTableKeyCondition(Connection conn, String pre_stmt, Set<Integer> keys, String keyName,
			String[] affichage) {
		try {
			List<Integer> map = new ArrayList<Integer>();
			PreparedStatement stmt = conn.prepareStatement(pre_stmt);

			ResultSet rset = stmt.executeQuery();
			while (rset.next()) {
				if (keys.contains(Integer.parseInt(rset.getString(keyName)))) {
					map.add(Integer.parseInt(rset.getString(keyName)));
					System.out.print(map.size() + ".");
					for (int j = 0; j < affichage.length; j++) {
						System.out.print(rset.getString(affichage[j]) + " ");

					}
					System.out.println();
				}
			}

			stmt.close();
			return map;

		} catch (SQLException e) {
			System.out.println("failed query");
			e.printStackTrace();
			return new ArrayList<Integer>();
		}

	}

	public void menuPrincipal(SqlRequest base) throws IOException, SQLException {
		// Connection conn = base.getDriver();
		String s;
		BufferedReader ob = new BufferedReader(new InputStreamReader(System.in));
		while (!getExit()) {
			System.out.println("Que voulez-vous faire : ");
			System.out.println("1.Insertion/Inscription");
			System.out.println("2.Evaluation des numéros");
			System.out.println("3.Planification des spectacles");
			System.out.println("4.Quitter");

			s = ob.readLine();
			// System.out.println("You entered String " + s);
			if (s.equals("1")) {
				this.insert(base);

			} else if (s.equals("2")) {
				this.evaluer(base);

			} else if (s.equals("3")) {
				this.planifier(base);

			} else if (s.equals("4")) {
				System.out.println("Au revoir");
				switchExit();

			} else {
				System.out.println("Erreur, veuillez réessayer");
			}
		}
	}

	public static void main(String[] args) throws IOException, SQLException {
		String USER = "nobletb";
		String PASSWD = "nobletb";
		SqlRequest base = new SqlRequest(USER, PASSWD);
		user_interface interface_utilisateur = new user_interface();
		interface_utilisateur.menuPrincipal(base);
		base.closeConnection();
	}
}
