import java.sql.Connection;
import java.util.Random;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.lang.Enum;

public class CreateTables {
	static int IDArtiste = 0;
	static int IDNumero = 0;

	static public enum Prenoms {
		Gabriel, Raphaël, Léo, Louis, Emma, Lucas, Jade, Adam, Louise, Arthur, Jules, Hugo, Maël, Ethan, Alice, Chloé, Liam, Inès, Paul, Nathan, Lina, Léa, Gabin, Sacha, Rose, Noah, Léna, Anna, Mila, Tom, Mohamed, Mia, Ambre, Elena, Julia, Théo, Aaron, Eden, Noé, Timéo, Manon, Juliette, Victor, Martin, Mathis, Lou, Zoé, Nolan, Enzo, Camille;
	}

	static public enum Cirques {
		Zavatta, Imag, Cirque_du_Soleil, Cirque_de_Pekin;

	}

	static public enum Noms {
		Martin, Bernard, Thomas, Petit, Robert, Richard, Durand, Dubois, Moreau, Laurent, Simon, Michel, Lefebvre, Leroy, Roux, David, Bertrand, Morel, Fournier, Girard, Bonnet, Dupont, Lambert, Fontaine, Rousseau, Vincent, Muller, Lefevre, Faure, Andre, Mercier, Blanc, Guerin, Boyer, Garnier, Chevalier, Francois, Legrand, Gauthier, Garcia, Perrin, Robin, Clement, Morin, Nicolas, Henry, Roussel, Mathieu, Gautier, Masson, Marchand, Duval, Denis, Dumont, Marie, Lemaire, Noel, Meyer, Dufour, Meunier, Brun, Blanchard, Giraud, Joly, Riviere, Lucas, Brunet, Gaillard, Barbier, Arnaud, Martinez, Gerard, Roche, Renard, Schmitt, Roy, Leroux, Colin, Vidal, Caron, Picard, Roger, Fabre, Aubert, Lemoine, Renaud, Dumas, Lacroix, Olivier, Philippe, Bourgeois, Pierre, Benoit, Rey, Leclerc, Payet, Rolland, Leclercq, Guillaume, Lecomte, Lopez, Jean, Dupuy, Guillot, Hubert, Berger, Carpentier, Sanchez, Dupuis, Moulin, Louis, Deschamps, Huet, Vasseur, Perez, Boucher, Fleury, Royer, Klein, Jacquet, Adam, Paris, Poirier, Marty, Aubry, Guyot, Carre, Charles, Renault, Charpentier, Menard, Maillard, Baron, Bertin, Bailly, Herve, Schneider, Fernandez, LeGall, Collet, Leger, Bouvier, Julien, Prevost, Millet, Perrot, Daniel, LeRoux, Cousin, Germain, Breton, Besson, Langlois, Remy, LeGoff, Pelletier, Leveque, Perrier, Leblanc, Barre, Lebrun, Marchal, Weber, Mallet, Hamon, Boulanger, Jacob, Monnier, Michaud, Rodriguez, Guichard, Gillet, Etienne, Grondin, Poulain, Tessier, Chevallier, Collin, Chauvin, DaSilva, Bouchet, Gay, Lemaitre, Benard, Marechal, Humbert, Reynaud, Antoine, Hoarau, Perret, Barthelemy, Cordier, Pichon, Lejeune, Gilbert, Lamy, Delaunay, Pasquier, Carlier, Laporte;

	}

	static public enum Specialite {
		Acrobatie, Dressage, Clown;
	}

	static public enum Lorem {
		Lorem_ipsum_dolor_sit_amet__consectetur_adipisici_elit__sed_eiusmod_tempor_incidunt_ut_labore_et_dolore_magna_aliqua, Ut_enim_ad_minim_veniam__quis_nostrud_exercitation_ullamco_laboris_nisi_ut_aliquid_ex_ea_commodi_consequat, Quis_aute_iure_reprehenderit_in_voluptate_velit_esse_cillum_dolore_eu_fugiat_nulla_pariatur, Excepteur_sint_obcaecat_cupiditat_non_proident__sunt_in_culpa_qui_officia_deserunt_mollit_anim_id_est_laborum, Duis_autem_vel_eum_iriure_dolor_in_hendrerit_in_vulputate_velit_esse_molestie_consequat__vel_illum_dolore_eu_feugiat_nulla_facilisis_at_vero_eros_et_accumsan_et_iusto_odio_dignissim_qui_blandit_praesent_luptatum_zzril_delenit_augue_duis_dolore_te_feugait_nulla_facilisi, Lorem_ipsum_dolor_sit_amet__consectetuer_adipiscing_elit__sed_diam_nonummy_nibh_euismod_tincidunt_ut_laoreet_dolore_magna_aliquam_erat_volutpat, Ut_wisi_enim_ad_minim_veniam__quis_nostrud_exerci_tation_ullamcorper_suscipit_lobortis_nisl_ut_aliquip_ex_ea_commodo_consequat, Nam_liber_tempor_cum_soluta_nobis_eleifend_option_congue_nihil_imperdiet_doming_id_quod_mazim_placerat_facer_possim_assum, Duis_autem_vel_eum_iriure_dolor_in_hendrerit_in_vulputate_velit_esse_molestie_consequat__vel_illum_dolore_eu_feugiat_nulla_facilisis, At_vero_eos_et_accusam_et_justo_duo_dolores_et_ea_rebum, Stet_clita_kasd_gubergren__no_sea_takimata_sanctus_est_Lorem_ipsum_dolor_sit_amet, Lorem_ipsum_dolor_sit_amet__consetetur_sadipscing_elitr__sed_diam_nonumy_eirmod_tempor_invidunt_ut_labore_et_dolore_magna_aliquyam_erat__sed_diam_voluptua, Lorem_ipsum_dolor_sit_amet__consetetur_sadipscing_elitr__At_accusam_aliquyam_diam_diam_dolore_dolores_duo_eirmod_eos_erat__et_nonumy_sed_tempor_et_et_invidunt_justo_labore_Stet_clita_ea_et_gubergren__kasd_magna_no_rebum, sanctus_sea_sed_takimata_ut_vero_voluptua, est_Lorem_ipsum_dolor_sit_ame, Lorem_ipsum_dolor_sit_amet__consetetur_sadipscing_elitr__sed_diam_nonumy_eirmod_tempor_invidunt_ut_labore_et_dolore_magna_aliquyam_erat, Consetetur_sadipscing_elitr__sed_diam_nonumy_eirmod_tempor_invidunt_ut_labore_et_dolore_magna_aliquyam_erat__sed_diam_voluptua, end;
	}

	static public enum Adjectif {
		ébouriffant, ésotérique, étonnant, beau, cabalistique, captivant, enchanté, enchanteur, enivrant, envoûtant, extraordinaire, fabuleux, féerique, fantasmagorique, fantastique, fascinant, fascinateur, formidable, incroyable, magnétique, merveilleuse, merveilleux, miraculeux, mystérieux, occulte, prestigieux, prodigieux, sensationnel, subjuguant, sublime, surnaturel, surprenant;
	}

	public void generate(Connection conn, int nbrArtiste) {

		try {

			// init table est dans le constructeur
			SqlRequest Tables = new SqlRequest(conn);
			Tables.drop();
			Tables.initTables();

			String[] artiste;
			String[] themes;
			String[] pseudos;
			for (int nb = 0; nb < nbrArtiste; nb++) {
				artiste = randomArtiste();
				int id = nb+1;
				String ids = "" + id; 
				themes = randomThemes();
				pseudos = randomPseudos();
				
				for (int i = 0; i < 6; i++) {
					//System.out.println(artiste[i]);
				}
				Tables.insertion("ARTISTE", artiste);
				for(int i=0; i<themes.length; i++) {
					Tables.insertion("THEME", "'" + themes[i] + "'");
					Tables.insertion("A_APOUR_T", ids, "'" + themes[i] + "'");
				}
				for(int i=0; i<pseudos.length; i++) {
					Tables.insertion("PSEUDOS", pseudos[i]);
					Tables.insertion("A_APOUR_P", ids, pseudos[i]);
				}
				
			}

			conn.close();

		} catch (SQLException e) {
			System.err.println("failed");
			e.printStackTrace(System.err);
		}
	}

	public static String[] randomArtiste() {
		String[] artiste = new String[6];
		artiste[0] = randomIDArtiste();
		artiste[1] = randomNom();
		artiste[2] = randomPrenom();
		artiste[3] = randomDate();
		artiste[4] = randomCirque();
		artiste[5] = randomTel();
		return artiste;
	}
	
	public static String[] randomThemes() {
		int nbr = new Random().nextInt(2);
		String themes[] = new String[nbr+1];
		for (int i=0; i<nbr+1; i++) {
			themes[i] = randomTheme();
		}
		return themes;
	}
	
	public static String[] randomPseudos() {
		int nbr = new Random().nextInt(2);
		String pseudos[] = new String[nbr + 1];
		for (int i=0; i<nbr+1; i++) {
			pseudos[i] = randomNom();
		}
		return pseudos;
	}

	public static String randomIDArtiste() {
		IDArtiste++;
		return "" + IDArtiste;
	}

	public static String randomPrenom() {
		int index = (new Random()).nextInt(Prenoms.Camille.ordinal());
		return "'" + Prenoms.values()[index].toString() + "'";
	}

	public static String randomTel() {
		String tmp = "" + (new Random()).nextInt(1000000000);
		while (tmp.length() < 10) {
			tmp = "0" + tmp;
		}
		return tmp;
	}

	public static String randomDate() {
		Random rand = new Random();
		String month = "" + (rand.nextInt(12) + 1);

		if (month.length() == 1) {
			month = "0" + month;
		}

		String day = "" + (rand.nextInt(28) +1);
		if (day.length() == 1) {
			day = "0" + day;
		}

		String year = "" + (2019 - rand.nextInt(100));

		// return "" + year + "-" + month + '-' + day;
		return "TO_DATE('" + day + "/" + month + "/" + year + "', 'DD/MM/YYYY')";
	}

	public static String randomCirque() {
		int index = (new Random()).nextInt(Cirques.Cirque_de_Pekin.ordinal()+1);
		return "'" + Cirques.values()[index].toString() + "'";
	}

	public static String randomNom() {
		int index = (new Random()).nextInt(Noms.Laporte.ordinal());
		return "'" + Noms.values()[index].toString() + "'";
	}

	public static String randomIDNumero() {
		IDNumero++;
		return "" + IDNumero;
	}

	public static String randomTheme() {
		int index = (new Random()).nextInt(Specialite.Clown.ordinal() + 1);
		return Specialite.values()[index].toString();
	}

	public static String randomDuree() {
		return "" + ((new Random()).nextInt(21) + 10);
	}

	public static String randomNote() {
		return "" + (new Random()).nextInt(11);
	}

	public static String randomCommentaire() {
		Random rand = new Random();
		int nbComments = rand.nextInt(5) + 1;
		String comment = "";
		for (int i = 0; i < nbComments; i++) {
			comment += Lorem.values()[rand.nextInt(Lorem.end.ordinal() - 1)].toString();
		}
		return comment;
	}

	public static String randomTitre() {
		int index = (new Random()).nextInt(Adjectif.surprenant.ordinal());
		String adj = Adjectif.values()[index].toString();
		return randomTheme() + " " + adj;
	}

}
