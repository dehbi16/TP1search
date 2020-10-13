import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
//Test
public class Agent implements Runnable {
	protected int x;
	protected int y;
	protected Game g;
	private State[][] L; 
	protected Thread t;
	protected static List <List<Direction>> solution; // "solution" est une file de type FIFO
	protected static List<Integer> Lheuristique;
	protected int h;
	protected int hmax;
	protected int nbaspirer = 0;
	protected int nbbijoux = 0;
	protected int cout = 0;
	protected int erreur = 0;
	protected int mode = 2;

	protected static List <List<List<Integer>>> frontiere;
	protected static List <List<Integer>> Cl;
	protected static List <Integer> coordAgent;
	protected static List <Integer> fonctionEvaluation;
	protected static List <List<Integer>> a;
	protected static List <List<Integer>> b;
	protected static List <Integer> newCoord;
	protected static List <List<Integer>> solutionCoord;
	protected static List <List<List<Integer>>> newNoeud;
	protected static List <List<Integer>> noeudExp;
	protected static List <List<Integer>> coordPoussiere;

	protected int coutNoeud;
	protected int heuristique;
	protected int indexBis=-1;

	public Agent(int x, int y, Game g) {
		this.x = x;
		this.y = y;
		this.g = g;
		t = new Thread(this);
		init();	
		t.start();
	}

	/*
	 * MaFonction : run()
	 * R�le : 1) v�rifier si l'agent a trouv� un chemin qui permet de r�aliser son but, si c'est le cas, l'agent change 
	 * de position ensuite, on teste si l'environnement est propre ou non si oui on arr�te le programme sinon on initialise 
	 * la liste "solution" par les directions possibles.
	 * Sinon, on parcours toute les �l�ments de la liste "solution" et on ajoute des nouveaux noeuds dans la liste 
	 * "solutions" pour chaque chemin, 
	 */
	public void run() {
		while(g.isRunning) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			int index;
			switch(mode) {
			case 0: // on effectue l'algorithme de recherche en largeur
				index = goal();
				if(index>=0) { // d�placement du robot apr�s avoir trouv� le chemin qui m�ne jusqu'� la poussi�re ou un bijou
					move(index);
					init();
					if (isclean())	g.isRunning = false;
				}
				else { // on construit le chemin qui m�ne jusqu'� une poussi�re ou un bijou
					List <Direction> a;
					List <Direction> position;
					int size = solution.size();

					for (int i=0; i<size; i++) {
						a = solution.get(0);
						solution.remove(0);
						position = positions(a);
						for (int j=0; j<position.size(); j++) {
							List <Direction> b = new ArrayList<Direction>();
							b.addAll(a);
							b.add(position.get(j));
							solution.add(b);
						}
					}

				}
				break;
			case 1: // on effectue l'algorithme de recherche A*
				if(indexBis>=0) { // d�placement du robot apr�s avoir trouv� le chemin qui m�ne jusqu'� la poussi�re ou un bijou
					indexBis=-1;
					System.out.println(solutionCoord);
					moveAetoile();
					init();
					if (isclean()) {
						g.isRunning = false;
					}
				}
				else { // on construit le chemin qui m�ne jusqu'� une poussi�re ou un bijou
					coordPoussiere = new ArrayList<List<Integer>>();
					trouverPoussiere(); // on commence par savoir o� il y a de la poussiere
					System.out.println("coordonn�es poussi�re : " + coordPoussiere);
					solution(); // on rentre dans l'algorithme
				}
				break;
			case 2:
				index = goal1();
				if (index>= 0) {
					move(index); 
					init();
					if (isclean()) g.isRunning=false;
				}
				else {
					List <Direction> a;
					List <Direction> position;
					int size = solution.size();
					int x;
					for (int i=0; i<size; i++) {
						a = solution.get(0);
						solution.remove(0);
						Lheuristique.remove(0); 
						position = positions(a);
						for (int j=0; j<position.size(); j++) {
							List <Direction> b = new ArrayList<Direction>();
							b.addAll(a);
							b.add(position.get(j));
							x = calculH(b);
							if (x>=h) {
								solution.add(b);
								Lheuristique.add(x);
								h = x;
							}	
						}
					}
				}
				break; 
			}
		}
	}

	/*
	 * MaFonction : solution()
	 * R�le : application de l'algorithme A* pour trouver la meilleure solution pour atteindre une poussi�re ou un bijou
	 */

	private void solution() {
		int ligneInitialeAgent = this.y;
		int colonneInitialeAgent = this.x;

		Cl = new ArrayList<List<Integer>>();
		solutionCoord = new ArrayList<List<Integer>>();
		coordAgent = new ArrayList<Integer>();

		// on d�termine les coordonn�es de l'agent aspirateur
		coordAgent.add(ligneInitialeAgent);
		coordAgent.add(colonneInitialeAgent);
		System.out.println("coordAgent : " + coordAgent); 

		// on commence � remplir la closed liste Cl avec les coordonn�es initiales de l'agent
		Cl.add(coordAgent);
		System.out.println("Cl : " + Cl); 

		// la liste des coordonn�es initiales de l'agent est le premier �l�ment de la solution
		solutionCoord.add(coordAgent);
		System.out.println("solutionCoord : " + solutionCoord); 

		// on cr�� notre fronti�re
		frontiere = new ArrayList<List<List<Integer>>>(); 
		System.out.println("frontiere : " + frontiere);

		// on cr�� une liste qui, pour chaque noeud qu'on d�veloppe, se remplit avec la liste des nouveaux noeuds cr��s � partir du noeud d�velopp�
		newNoeud = new ArrayList<List<List<Integer>>>(); 
		System.out.println("newNoeud : " + newNoeud);

		b = new ArrayList<List<Integer>>(); 

		// initialisation et expansion du noeud de d�part, on transforme les directions en coordonn�es � partir de la liste solution compos�e des directions
		for(int i=0;i<solution.size();i++) { 
			for(int j=0;j<solution.get(i).size();j++) {
				if(solution.get(i).get(j)==Direction.gauche) {

					coordAgent = new ArrayList<Integer>();
					fonctionEvaluation = new ArrayList<Integer>();
					a = new ArrayList<List<Integer>>();

					// on calcule les coordonn�es du nouveaux noeuds selon la direction
					coordAgent.add(ligneInitialeAgent); 
					coordAgent.add(colonneInitialeAgent-1); // d�placement vers la gauche
					System.out.println("coordAgent : " + coordAgent);

					// calcul de l'heuristique et du cout de ce nouveau noeud
					heuristique=calculHeuristique(coordAgent);
					coutNoeud=calculCoutNoeud(coordAgent);

					// on ajoute ces donn�es � la liste fonctionEvaluation
					fonctionEvaluation.add(coutNoeud);
					fonctionEvaluation.add(heuristique);

					// ajout des coordonn�es du noeud � une liste interm�diaire b qui permettra de remplir la liste newNoeud
					b.add(coordAgent);

					// liste interm�diaire avant d'ajouter � la fronti�re
					a.add(coordAgent);
					a.add(fonctionEvaluation);

					// on ajoute a la frontiere les coordonn�es de l'agent et la fonction d'�valuation via la liste a
					frontiere.add(a); 
					System.out.println("frontiere : " + frontiere);
				}

				// m�me processus que pr�c�demment avec la direction droite
				if(solution.get(i).get(j)==Direction.droite) {

					coordAgent = new ArrayList<Integer>();
					fonctionEvaluation = new ArrayList<Integer>();
					a = new ArrayList<List<Integer>>();



					coordAgent.add(ligneInitialeAgent);
					coordAgent.add(colonneInitialeAgent+1);
					System.out.println("coordAgent : " + coordAgent);

					heuristique=calculHeuristique(coordAgent);
					coutNoeud=calculCoutNoeud(coordAgent);
					fonctionEvaluation.add(coutNoeud);
					fonctionEvaluation.add(heuristique);

					b.add(coordAgent);

					a.add(coordAgent);
					a.add(fonctionEvaluation);

					frontiere.add(a);
					System.out.println("frontiere : " + frontiere);
				}

				// m�me processus que pr�c�demment avec la direction haut
				if(solution.get(i).get(j)==Direction.haut) {

					coordAgent = new ArrayList<Integer>();
					fonctionEvaluation = new ArrayList<Integer>();
					a = new ArrayList<List<Integer>>();


					coordAgent.add(ligneInitialeAgent-1);
					coordAgent.add(colonneInitialeAgent);
					System.out.println("coordAgent : " + coordAgent);

					heuristique=calculHeuristique(coordAgent);
					coutNoeud=calculCoutNoeud(coordAgent);
					fonctionEvaluation.add(coutNoeud);
					fonctionEvaluation.add(heuristique);

					b.add(coordAgent);

					a.add(coordAgent);
					a.add(fonctionEvaluation);

					frontiere.add(a);
					System.out.println("frontiere : " + frontiere);
				}

				// m�me processus que pr�c�demment avec la direction bas
				if(solution.get(i).get(j)==Direction.bas) {

					coordAgent = new ArrayList<Integer>();
					fonctionEvaluation = new ArrayList<Integer>();
					a = new ArrayList<List<Integer>>();



					coordAgent.add(ligneInitialeAgent+1);
					coordAgent.add(colonneInitialeAgent);
					System.out.println("coordAgent : " + coordAgent);

					heuristique=calculHeuristique(coordAgent);
					coutNoeud=calculCoutNoeud(coordAgent);
					fonctionEvaluation.add(coutNoeud);
					fonctionEvaluation.add(heuristique);

					b.add(coordAgent);

					a.add(coordAgent);
					a.add(fonctionEvaluation);

					frontiere.add(a);
					System.out.println("frontiere : " + frontiere);
				}
			}

		}
		System.out.println("frontiere : " + frontiere);

		// on ajoute la liste b compos� des noeuds qu'on vient de cr�er � la liste des nouveaux noeuds
		newNoeud.add(b);
		System.out.println("newNoeud : " + newNoeud);

		// on recherche les coordonn�es qui ont la fonction d'�valuation la plus faible, dans l'ordre d'apparition dans la fronti�re
		newCoord = new ArrayList<Integer>();
		newCoord = calculFonctionEvaluation(); 
		System.out.println("newCoord : " + newCoord);

		// on est arriv� au point voulu si l'heuristique est nulle
		if(calculHeuristique(newCoord)==0) { 
			solutionCoord.add(newCoord);
			indexBis = 1;
		}

		// l'heuristique n'est pas nulle apr�s l'initialisation, on d�veloppe le noeud newCoord en fonction de la valeur indexBis renvoy� par la fonction expansionNoeud()
		while(indexBis==-1) {

			// ce nouveau noeud est � ajouter � la solution des coordonn�es
			solutionCoord.add(newCoord); 
			System.out.println("solutionCoord : " + solutionCoord);

			// on proc�de au d�veloppement de ce noeud newCoord
			indexBis=expansionNoeud(); 

		}
	}

	/*
	 * Ma fonction : expansionNoeud()
	 * R�le : d�velopp� le noeud qui a la fonction d'�valuation la plus faible dans l'ordre d'apparition dans la fronti�re
	 * Elle modifie la liste des solutions solutionCoord et renvoie 1 si on est arriv� � une case avec de la poussi�re ou bijou, elle renvoie -1 sinon et on continue
	 */

	private int expansionNoeud() {
		int fin=0; //variable qui dit si on tombe sur l'�lement dont l'heuristique est nulle
		List <Integer> coordFin;
		coordFin = new ArrayList<Integer>();

		Cl.add(newCoord); // on ajoute � la closed list les nouvelles coord
		System.out.println("Cl : " + Cl);

		List<Direction> d;
		List<Direction> position;
		d = new ArrayList<Direction>();
		position = new ArrayList<Direction>();
		b = new ArrayList<List<Integer>>();
		int deplacementLigne;
		int deplacementColonne;

		// on calcule dans quelle direction on est all� depuis le noeud pr�c�dent vers ce nouveau noeud newCoord
		deplacementLigne = newCoord.get(0)-solutionCoord.get(solutionCoord.size()-2).get(0); // comparaison avec l'avant dernier jeu de coordonn�es ajout� � la solution
		deplacementColonne = newCoord.get(1)-solutionCoord.get(solutionCoord.size()-2).get(1);

		// selon les r�sultats, on a la direction vers laquelle on est all� pour pouvoir utiliser la fonction position
		if(deplacementLigne==0) {
			if(deplacementColonne==1) {
				d.add(Direction.droite);
			}
			else{
				d.add(Direction.gauche);
			}
		}
		if(deplacementColonne==0) {
			if(deplacementLigne==1) {
				d.add(Direction.bas);
			}
			else{
				d.add(Direction.haut);
			}
		}
		System.out.println("d : " + d);

		// on calcule les differentes directions (gauche, droite, etc) qu'on peut faire depuis nos nouvelles coordonnees newCoord
		position = positions(d); 
		System.out.println(position);

		// selon la direction o� on peut aller, on cr�er de nouveaux noeuds de la m�me mani�re que pr�c�demment
		for(int i=0;i<position.size();i++) {
			if(position.get(i)==Direction.gauche) {

				coordAgent = new ArrayList<Integer>();
				fonctionEvaluation = new ArrayList<Integer>();
				a = new ArrayList<List<Integer>>();


				coordAgent.add(newCoord.get(0)); 
				coordAgent.add(newCoord.get(1)-1);
				System.out.println("coordAgent : " + coordAgent);

				heuristique=calculHeuristique(coordAgent);

				// on v�rifie si le noeud cr�� est le noeud final (poussi�re ou bijou)
				if(heuristique == 0) {
					fin=1;
					coordFin=coordAgent;
				}

				coutNoeud=calculCoutNoeud(coordAgent);
				fonctionEvaluation.add(coutNoeud);
				fonctionEvaluation.add(heuristique);

				b.add(coordAgent);
				a.add(coordAgent);
				a.add(fonctionEvaluation);

				frontiere.add(a);
				System.out.println("frontiere : " + frontiere);

			}
			if(position.get(i)==Direction.droite) {

				coordAgent = new ArrayList<Integer>();
				fonctionEvaluation = new ArrayList<Integer>();
				a = new ArrayList<List<Integer>>();


				coordAgent.add(newCoord.get(0)); 
				coordAgent.add(newCoord.get(1)+1);
				System.out.println("coordAgent : " + coordAgent);

				heuristique=calculHeuristique(coordAgent);
				if(heuristique == 0) {
					fin=1;
					coordFin=coordAgent;
				}
				coutNoeud=calculCoutNoeud(coordAgent);
				fonctionEvaluation.add(coutNoeud);
				fonctionEvaluation.add(heuristique);

				b.add(coordAgent);
				a.add(coordAgent);
				a.add(fonctionEvaluation);

				frontiere.add(a);
				System.out.println("frontiere : " + frontiere);

			}
			if(position.get(i)==Direction.haut) {

				coordAgent = new ArrayList<Integer>();
				fonctionEvaluation = new ArrayList<Integer>();
				a = new ArrayList<List<Integer>>();


				coordAgent.add(newCoord.get(0)-1); 
				coordAgent.add(newCoord.get(1));
				System.out.println("coordAgent : " + coordAgent);

				heuristique=calculHeuristique(coordAgent);
				if(heuristique == 0) {
					fin=1;
					coordFin=coordAgent;
				}
				coutNoeud=calculCoutNoeud(coordAgent);
				fonctionEvaluation.add(coutNoeud);
				fonctionEvaluation.add(heuristique);

				b.add(coordAgent);
				a.add(coordAgent);
				a.add(fonctionEvaluation);

				frontiere.add(a);
				System.out.println("frontiere : " + frontiere);


			}
			if(position.get(i)==Direction.bas) {

				coordAgent = new ArrayList<Integer>();
				fonctionEvaluation = new ArrayList<Integer>();
				a = new ArrayList<List<Integer>>();



				coordAgent.add(newCoord.get(0)+1); 
				coordAgent.add(newCoord.get(1));
				System.out.println("coordAgent : " + coordAgent);

				heuristique=calculHeuristique(coordAgent);
				if(heuristique == 0) {
					fin=1;
					coordFin=coordAgent;
				}
				coutNoeud=calculCoutNoeud(coordAgent);
				fonctionEvaluation.add(coutNoeud);
				fonctionEvaluation.add(heuristique);

				b.add(coordAgent);
				a.add(coordAgent);
				a.add(fonctionEvaluation);

				frontiere.add(a);
				System.out.println("frontiere : " + frontiere);

			}
		}
		// on a la nouvelle frontiere
		System.out.println("frontiere : " + frontiere);

		// on ajoute les nouveaux noeud de la frontiere dans la liste newNoeud
		newNoeud.add(b);
		System.out.println("newNoeud : " + newNoeud);

		// on recherche les coordonn�es qui ont la fonction d'�valuation la plus faible, dans l'ordre d'apparition dans la fronti�re
		newCoord = new ArrayList<Integer>();
		newCoord = calculFonctionEvaluation(); 

		System.out.println("newCoord : " + newCoord);


		// si on est arriv� au point voulu 
		if(fin==1) { 
			solutionCoord.add(coordFin);
			return 1;
		}
		else {
			// on v�rifie si le noeud suivant est dans la continuit� du noeud pr�c�dent ou si c'est une nouvelle branche
			testNoeud(); 
			return -1;
		}
	}

	/*
	 * MaFonction : testNoeud()
	 * R�le : elle regarde si le noeud newCoord est dans la continuit� du noeud pr�c�dent ou pas
	 * S'il ne l'est pas, il faut par exemple enlever des �l�ment de la liste solutionCoord et en ajouter d'autres
	 * S'il l'est tout va bien, il n'y a rien � ajouter ou enlever
	 */

	private void testNoeud() {
		int testAjoutCoord=0;
		int testCoordInt;
		List <List<Integer>> listeCoordInt;
		List <Integer> coordInt;
		listeCoordInt = new ArrayList<List<Integer>>();
		coordInt = new ArrayList<Integer>();
		coordInt=newCoord;

		// on parcourt les �l�ments de la liste newNoeud et notamment les sous-liste contenant les coordon�es des noeuds
		for(int i=newNoeud.size()-1;i>=0;i--) {
			for(int j=0;j<newNoeud.get(i).size();j++) {

				/*
				 * on regarde dans quel sous liste de newNoeud se situe le newNoeud qu'on d�veloppe en commencant par la fin (premi�re condition du "if")
				 * si le nouveau noeud est dans la derni�re sous-liste de la liste newNoeud, alors il est dans la continuit� du noeud pr�c�dent
				 * si jamais il faut remonter plusieurs noeuds en arri�re, on a la deuxi�me condition du "if" qui permet de s'assurer qu'on r��crit bien les �l�ments dans la liste solutionCoord via la liste listeCoordInt
				 */
				if(newCoord==newNoeud.get(i).get(j) || coordInt==newNoeud.get(i).get(j)) {
					testAjoutCoord++;
					coordInt=Cl.get(i); // la liste de coordonn�es interm�diaires prend la valeur de l'�l�menent de la closed liste correspondant au noeud qui a permis d'obtenir la liste des nouveaux noeuds
					testCoordInt=0;

					// on regarde si les coordonn�es interm�diaires ne sont pas d�j� dans la solution
					for(int k=0;k<solutionCoord.size();k++) {
						if(coordInt==solutionCoord.get(k)) {
							testCoordInt++;
						}
					}

					// on l'ajoute � une liste interm�diaire de coordonn�es s'il n'est pas dans la solution
					if(testCoordInt==0) {
						listeCoordInt.add(coordInt);
					}					
				}	
			}

			// si les nouvelles coordonn�es ne sont pas dans la sous-liste courante de newNoeud, on retire l'�l�ment dans solution
			if(testAjoutCoord==0) {
				// on calcule le cout noeud du dernier �l�ment de la solution, si c'est 1, on ne retire plus d'�l�ment
				if(calculCoutNoeud(solutionCoord.get(solutionCoord.size()-1))>=1) {
					solutionCoord.remove(solutionCoord.size()-1); // on retire le dernier �l�ment de la solution
				}
			}
		}

		// on met � jour la liste solutionCoord avec les modifications �ffectu�es
		for(int h=listeCoordInt.size()-1;h>=0;h--) {
			solutionCoord.add(listeCoordInt.get(h));
		}
		System.out.println("solutionCoord : " + solutionCoord);
	}



	/*
	 * MaFonction : calculFonctionEvaluation
	 * R�le : d�terminer dans la fronti�re le noeud qui a la fonction d'�valuation la plus faible dans l'ordre d'apparition des noeuds dans la fronti�re
	 * Elle renvoie la liste des coordonn�es du noeud en question
	 */
	private List<Integer> calculFonctionEvaluation() {
		int resultat=100;
		int indice=0;
		newCoord = new ArrayList<Integer>();
		int j=1; // pour prendre le deuxieme element de la sous liste
		for(int i=0;i<frontiere.size();i++) {
			int resultatInter=frontiere.get(i).get(j).get(0)+frontiere.get(i).get(j).get(1);
			if(resultatInter<resultat) {
				resultat=resultatInter;
				indice=i;
			}

		}
		newCoord=frontiere.get(indice).get(0);
		System.out.println("newCoord : " + newCoord);

		frontiere.remove(indice); // une fois qu'on a les nouvelles coord, on enleve l element de la frontiere
		System.out.println("frontiere : " + frontiere);
		return newCoord; // renvoit les coord du point qui a la fonction evaluation la plus faible
	}

	/* MaFonction : calculHeuristique
	 * R�le : calculer l'heuristique entre un noeud quelconque et la case o� il y a de la poussi�re ou un bijou la plus proche
	 * Elle renvoie la valeur de l'heuristique
	 */

	private int calculHeuristique(List <Integer> noeud) {
		int heuristique = 100; 
		int heuristiqueInter = 0;
		int deplacementLigne = 100;
		int deplacementColonne = 100;
		for(int i=0;i<coordPoussiere.size();i++) {
			deplacementLigne = Math.abs(noeud.get(0)-coordPoussiere.get(i).get(0));
			deplacementColonne = Math.abs(noeud.get(1)-coordPoussiere.get(i).get(1));
			heuristiqueInter = deplacementColonne + deplacementLigne;
			if(heuristiqueInter < heuristique) {
				heuristique = heuristiqueInter;			
			}
		}
		return heuristique;
	}

	/*
	 * MaFonction : calculCoutNoeud 
	 * R�le : calculer � partir d'un noeud, le cout par rapport � l'emplacement initial de l'agent
	 * Elle renvoie la valeur de ce co�t
	 */

	private int calculCoutNoeud(List <Integer> noeud) {
		int coutNoeud=0;
		int deplacementLigne;
		int deplacementColonne;
		deplacementLigne = Math.abs(noeud.get(0)-this.y);
		deplacementColonne = Math.abs(noeud.get(1)-this.x);
		coutNoeud = deplacementColonne + deplacementLigne;
		return coutNoeud;
	}

	/*
	 * MaFonction : trouverPoussiere
	 * R�le : remplir la liste coordPoussiere avec les coordonn�es des endroits o� il y a de la poussi�re ou des bijoux ou les deux
	 */
	private void trouverPoussiere(){
		List<Integer> remplissagePoussiere;
		for(int i=0;i<5;i++) {
			for(int j=0;j<5;j++) {
				if (this.L[i][j] == State.dust || this.L[i][j] == State.jewelry || this.L[i][j] == State.dustjewelry ) {
					remplissagePoussiere = new ArrayList<Integer>();
					remplissagePoussiere.add(i);
					remplissagePoussiere.add(j);
					coordPoussiere.add(remplissagePoussiere);
				}
			}
		}
	}

	private boolean contains(List<int[]> visited, int col, int lig) {
		for(int i=0; i<visited.size(); i++) {
			if (visited.get(i)[0]== lig && visited.get(i)[1]==col) {
				return true;
			}
		}


		return false;
	}

	/*
	 * MaFonction : positions
	 * Attribut : a : une liste de direction que l'agent peut faire � partir de sa position actuelle Ex:a=[Droite, Haut, Haut]
	 * R�le : l'agent suit le chemin indiquer dans a, et � partir de sa position finale il retourne une liste qui contient 
	 * les directions qu'il peut choisir
	 *  
	 */
	private List<Direction> positions(List<Direction> a) {
		// cr�er des nouveaux noeuds pour chaque �tat
		int newl = this.y; 
		int newc = this.x; 
		// partie algo inf
		switch((mode%2)) {
		case 0:
			int [] resultat = new int [2];
			resultat[0] = newl;
			resultat[1] = newc;
			List<Direction> directionPossible = new ArrayList<Direction>();
			List<int[]> visited = new ArrayList<int[]>();
			visited.add(resultat);
			int i;
			for (i=0; i<a.size(); i++) {
				resultat = calculeP(a.get(i),newl, newc);
				newl = resultat[0];
				newc = resultat[1];
				visited.add(resultat);
			}
			i--;
			if (newl!=0 && !contains(visited, newc, newl-1)) directionPossible.add(Direction.haut);
			if (newl!=4 && !contains(visited, newc, newl+1)) directionPossible.add(Direction.bas);
			if (newc!=0 && !contains(visited, newc-1, newl)) directionPossible.add(Direction.gauche);
			if (newc!=4 && !contains(visited, newc+1, newl)) directionPossible.add(Direction.droite);


			return directionPossible;

		case 1:
			if(a.get(0)==Direction.gauche) {
				newl = newCoord.get(0);
				newc = newCoord.get(1)+1; 
			}
			else if(a.get(0)==Direction.droite) {
				newl = newCoord.get(0);
				newc = newCoord.get(1)-1;
			}
			else if(a.get(0)==Direction.haut) {
				newl = newCoord.get(0)+1;
				newc = newCoord.get(1);
			}
			else if(a.get(0)==Direction.bas) {
				newl = newCoord.get(0)-1;
				newc = newCoord.get(1);
			}
			// fin partie algo inf
			int [] resultatBis;
			List<Direction> directionPossibleBis = new ArrayList<Direction>();
			int iBis;
			for (iBis=0; iBis<a.size(); iBis++) {
				resultatBis = calculeP(a.get(iBis),newl, newc);
				newl = resultatBis[0];
				newc = resultatBis[1];
			}
			iBis--;
			if (newc!=0 && a.get(iBis) != Direction.droite) directionPossibleBis.add(Direction.gauche);
			if (newc!=4 && a.get(iBis) != Direction.gauche) directionPossibleBis.add(Direction.droite);
			if (newl!=0 && a.get(iBis) != Direction.bas) directionPossibleBis.add(Direction.haut);
			if (newl!=4 && a.get(iBis) != Direction.haut) directionPossibleBis.add(Direction.bas);
			return directionPossibleBis;

		}
		return a;
	}

	/*
	 * MaFonction : calculeP 
	 * R�le : calculer la nouvelle position du robot s'il suit une direction donn�e et retourner un tableau de deux �l�ments 
	 * qui contient la nouvelle coordonn�e de l'agent 
	 */
	private int[] calculeP(Direction direction, int newl, int newc) {
		int [] resultat = new int [2];
		if (direction == Direction.bas && newl != 4) newl++;
		else if (direction == Direction.droite && newc != 4) newc++;
		else if (direction == Direction.gauche && newc != 0) newc--;
		else if (direction == Direction.haut && newl != 0) newl--; 
		resultat[0] = newl;
		resultat[1] = newc;
		return resultat;
	}

	/*
	 * MaFonction : goal()
	 * Role: parcourir la liste "solution" et tester si l'agent suit un chemin dans "solution" il arrive � une case qui 
	 * contient la poussi�re, des bijoux ou bien les deux.
	 * Si "solution" contient un chemin qui permet de r�aliser le but, la fonction retourne l'indice du chemin dans "solution" 
	 * sinon il retourne -1.
	 */
	private int goal() {
		// le but est de trouver une poussi�re
		int [] resultat;
		int newl;
		int newc;
		for(int i=0; i<solution.size(); i++) {
			newl = this.y;
			newc = this.x;
			for (int j=0; j<solution.get(i).size(); j++) {
				resultat = calculeP(solution.get(i).get(j), newl, newc);
				newl = resultat[0];
				newc = resultat[1];
			}
			if (this.L[newl][newc] == State.dust || this.L[newl][newc] == State.jewelry || this.L[newl][newc] == State.dustjewelry ) {
				return i;
			}
		}
		return -1;
	}

	private int calculH(List<Direction> b) {
		int hbis=0;
		int positionx = this.x;
		int positiony = this.y;
		int [] resultat;
		for(int i=0; i<b.size(); i++) {
			resultat = calculeP(b.get(i), positiony, positionx);
			positiony = resultat[0];
			positionx = resultat[1];
			if (this.L[positiony][positionx] == State.dust || this.L[positiony][positionx] == State.dustjewelry || this.L[positiony][positionx] == State.jewelry ) {
				hbis++;
			}
		}
		return hbis;
	}


	private int goal1() {
		for(int i=0; i<Lheuristique.size(); i++) {
			if(Lheuristique.get(i) == hmax || Lheuristique.get(i) == 5) {
				return i;  
			}
		}
		return -1;
	}

	/*
	 * MaFonction : move
	 * attribut : l'indice du chemin dans "solution" que le robot doit suivre pour r�aliser son but
	 * R�le : changer la position du robot en suivant un chemin pr�cis
	 */
	private void move(int index) {
		// extraire la solution de la liste et appliquer les mouvements pour faire bouger l'agent.
		g.env.L[this.y][this.x] = State.empty;
		int newl = this.y;
		int newc = this.x;
		int [] resultat;
		for (int i=0; i<solution.get(index).size(); i++) {
			g.env.L[newl][newc] = State.empty;
			resultat = calculeP(solution.get(index).get(i), newl, newc);
			newl = resultat[0];
			newc = resultat[1];
			cout++;
			if (this.L[newl][newc] == State.dust) {
				nbaspirer++;
				cout++;
				if (g.env.L[newl][newc] == State.jewelry || g.env.L[newl][newc] == State.dustjewelry) erreur++;
				g.env.L[newl][newc] = State.empty;
			}
			else if (this.L[newl][newc] == State.jewelry) {
				nbbijoux++;
				cout++;
				g.env.L[newl][newc] = State.empty;
			}
			else if (this.L[newl][newc] == State.dustjewelry) {
				nbbijoux++;
				nbaspirer++;
				cout+=2;
				g.env.L[newl][newc] = State.empty; 
			}
			g.env.L[newl][newc] = State.robot;
			try {
				TimeUnit.MILLISECONDS.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		this.y = newl;
		this.x = newc;
	}

	/*
	 * MaFonction : moveAetoile()
	 * R�le : d�placer le robot en suivant les coordonn�es de la liste solutionCoord
	 */
	private void moveAetoile() {
		g.env.L[this.y][this.x] = State.empty;
		int newc = this.x;
		int newl = this.y;
		for (int i=1; i<solutionCoord.size(); i++) { //i ne part pas de 0 car le premier element de la liste solution est le point de d�part
			newl = solutionCoord.get(i).get(0);
			newc = solutionCoord.get(i).get(1);
			cout++;

		}
		this.y = newl;
		this.x = newc;
		if (this.L[this.y][this.x] == State.dust) {
			nbaspirer++;
			cout++;
			if (g.env.L[this.y][this.x] != State.dust) erreur++;
			g.env.L[this.y][this.x] = State.robot;
		}
		else if (this.L[this.y][this.x] == State.jewelry) {
			nbbijoux++;
			cout++;
		}
		else if (this.L[this.y][this.x] == State.dustjewelry) {
			nbbijoux++;
			nbaspirer++;
			cout+=2;
		}
		g.env.L[this.y][this.x] = State.robot;
	}

	private boolean isclean() {
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if (this.L[i][j] == State.dust || this.L[i][j] == State.jewelry || this.L[i][j] == State.dustjewelry) return false;
			}
		}
		return true;
	}

	/*
	 * MaFonction : init
	 * R�le : 1) Pour commencer, l'agent garde une copie de l'environnement 
	 * 		  2) initialiser la liste de direction "solution" : Pour cela, � partir de la position actuelle de l'agent, 
	 * 			on ajoute les directions possibles que l'agent peut faire. Ex: solution = [[Droite], [Gauche], [Haut], [Bas]]
	 * 			certaines directions sont interdites Ex: si l'agent se retrouve dans les fronti�res 
	 */
	private void init(){
		this.L = new State[5][5];
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				this.L[i][j] = g.env.L[i][j];
			}
		}

		if (mode==2) {
			hmax = 0;
			for(int i=0; i<5; i++) {
				for(int j=0; j<5; j++) {
					if (this.L[i][j] == State.dust || this.L[i][j] == State.jewelry || this.L[i][j] == State.dustjewelry) {
						hmax++;
					}
				}
			}
		}


		solution = new ArrayList<List<Direction>>();
		if (mode==2) Lheuristique = new ArrayList<Integer>();
		h=0;

		List <Direction> a;
		if (this.x != 0) {
			a = new ArrayList<Direction>();
			a.add(Direction.gauche);
			solution.add(a);
			if (mode==2) Lheuristique.add(0);
		}
		if (this.x != 4) {
			a = new ArrayList<Direction>();
			a.add(Direction.droite);
			solution.add(a);
			if (mode==2) Lheuristique.add(0);
		}
		if (this.y != 0) {
			a = new ArrayList<Direction>();
			a.add(Direction.haut);
			solution.add(a);
			if (mode==2) Lheuristique.add(0);
		}
		if (this.y != 4) {
			a = new ArrayList<Direction>();
			a.add(Direction.bas);
			solution.add(a);
			if (mode==2) Lheuristique.add(0);
		}

	}



}
