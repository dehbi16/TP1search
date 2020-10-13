import java.util.ArrayList;
import java.util.List;
//Test
public class Agent {
	protected int x;
	protected int y;
	protected Game g;
	private State[][] L; 
	protected static List <List<Direction>> solution; // "solution" est une file de type FIFO
	protected int nbaspirer = 0;
	protected int nbbijoux = 0;
	protected int cout = 0;
	protected int erreur = 0;
	protected int mode = 1;

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
		init();		
	}

	/*
	 * MaFonction : run()
	 * Rôle : 1) vérifier si l'agent a trouvé un chemin qui permet de réaliser son but, si c'est le cas, l'agent change 
	 * de position ensuite, on teste si l'environnement est propre ou non si oui on arrête le programme sinon on initialise 
	 * la liste "solution" par les directions possibles.
	 * Sinon, on parcours toute les éléments de la liste "solution" et on ajoute des nouveaux noeuds dans la liste 
	 * "solutions" pour chaque chemin, 
	 */
	protected void run() {
		int index;
		switch(mode) {
		case 0:
			index = goal();
			if(index>=0) { 
				move(index);
				init();
				if (isclean())	g.isRunning = false;
			}
			else {
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
		case 1:
			index = -1;
			if(index>=0 || indexBis>=0) {
				indexBis=-1;
				System.out.println(solutionCoord);
				moveAetoile();
				init();
				if (isclean()) {
					g.isRunning = false;
				}
			}
			else {
				//on commence par savoir où il y a de la poussiere
				coordPoussiere = new ArrayList<List<Integer>>();
				trouverPoussiere();
				System.out.println("coordonnées poussière : " + coordPoussiere);
				solution();
			}
			break;

		}

	}

	private void solution() {
		int ligneInitialeAgent = this.y;
		int colonneInitialeAgent = this.x;
		Cl = new ArrayList<List<Integer>>();
		solutionCoord = new ArrayList<List<Integer>>();
		coordAgent = new ArrayList<Integer>(); //On remplit la liste Cl avec le point de depart et on l ajoute aussi a la liste des solutions
		coordAgent.add(ligneInitialeAgent);
		coordAgent.add(colonneInitialeAgent);
		System.out.println("coordAgent : " + coordAgent);

		Cl.add(coordAgent);
		System.out.println("Cl : " + Cl);

		solutionCoord.add(coordAgent);
		System.out.println("solutionCoord : " + solutionCoord);

		frontiere = new ArrayList<List<List<Integer>>>();
		System.out.println("frontiere : " + frontiere);

		newNoeud = new ArrayList<List<List<Integer>>>();
		System.out.println("newNoeud : " + newNoeud);

		b = new ArrayList<List<Integer>>(); //permet de créer la liste newNoeud ;
		for(int i=0;i<solution.size();i++) { //initialisation et expansion du noeud de depart a partir de l init ; on a une frontiere avec plusieurs nouveaux noeuds 
			for(int j=0;j<solution.get(i).size();j++) {
				if(solution.get(i).get(j)==Direction.gauche) {

					coordAgent = new ArrayList<Integer>();
					fonctionEvaluation = new ArrayList<Integer>();
					a = new ArrayList<List<Integer>>();

					coordAgent.add(ligneInitialeAgent); // nouvelles coordonnees
					coordAgent.add(colonneInitialeAgent-1); //idem
					System.out.println("coordAgent : " + coordAgent);

					heuristique=calculHeuristique(coordAgent);
					coutNoeud=calculCoutNoeud(coordAgent);
					fonctionEvaluation.add(coutNoeud);
					fonctionEvaluation.add(heuristique);
					b.add(coordAgent);

					a.add(coordAgent);
					a.add(fonctionEvaluation);

					frontiere.add(a); //on ajoute a la frontiere les coord de l agent et la fonction evaluation soit coutNoeud et heuristique
					System.out.println("frontiere : " + frontiere);
				}
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
		newNoeud.add(b);
		System.out.println("newNoeud : " + newNoeud);
		newCoord = new ArrayList<Integer>();
		newCoord = calculFonctionEvaluation(); // on a la valeur des coord du noeud qu on va dvlp
		System.out.println("newCoord : " + newCoord);
		if(calculHeuristique(newCoord)==0) { // on est arrivé au point voulu 
			solutionCoord.add(newCoord);
			indexBis = 1;
		}
		// fin initialisation, la suite permet de det tout le chemin
		while(indexBis==-1) {
			solutionCoord.add(newCoord); // on ajoute a la liste des solutions les nouvelles coord
			System.out.println("solutionCoord : " + solutionCoord);
			indexBis=expansionNoeud();
		}
	}

	private int expansionNoeud() {
		int fin=0; //variable qui dit si on tombe sur l element dont l heuristique est nulle
		List <Integer> coordFin;
		coordFin = new ArrayList<Integer>();
	Cl.add(newCoord); // on ajoute a la closed list les nouvelles coord
		System.out.println("Cl : " + Cl);
		
		List<Direction> d;
		List<Direction> position;
		d = new ArrayList<Direction>();
		position = new ArrayList<Direction>();
		b = new ArrayList<List<Integer>>();
		int deplacementLigne;
		int deplacementColonne;
		deplacementLigne = newCoord.get(0)-solutionCoord.get(solutionCoord.size()-2).get(0); // on calcule dans quelle direction on est allé (le dernier element est les nouvelles coord donc on prend l'avant dernier
		deplacementColonne = newCoord.get(1)-solutionCoord.get(solutionCoord.size()-2).get(1);
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
		position = positions(d); //on a les differentes directions (gauche, droite, etc) quon peut faire depuis nos nouvelles coordonnees
		System.out.println(position);
		for(int i=0;i<position.size();i++) {
			if(position.get(i)==Direction.gauche) {

				coordAgent = new ArrayList<Integer>();
				fonctionEvaluation = new ArrayList<Integer>();
				a = new ArrayList<List<Integer>>();


				coordAgent.add(newCoord.get(0)); 
				coordAgent.add(newCoord.get(1)-1);
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
		System.out.println("frontiere : " + frontiere);
		// on a la nouvelle frontiere ; on ajoute les nouveaux noeud de la frontiere dans la liste des newNoeud
		newNoeud.add(b);
		System.out.println("newNoeud : " + newNoeud);

		newCoord = new ArrayList<Integer>();
		newCoord = calculFonctionEvaluation(); // on a la valeur des coord du noeud qu on va dvlp
		
		System.out.println("newCoord : " + newCoord);
		

		if(fin==1) { // on est arrivé au point voulu 
			solutionCoord.add(coordFin);
			return 1;
		}
		else {
			// verification de savoir si le noeud suivant est dans la continuite du noeud precedent ou si cest une nouvelle branche
			testNoeud(); // modifie la solution en fonction
			return -1;
		}
	}


	private void testNoeud() {
		int testAjoutCoord=0;
		int testCoordInt;
		List <List<Integer>> listeCoordInt;
		List <Integer> coordInt;
		listeCoordInt = new ArrayList<List<Integer>>();
		coordInt = new ArrayList<Integer>();
		coordInt=newCoord;
		for(int i=newNoeud.size()-1;i>=0;i--) {
			for(int j=0;j<newNoeud.get(i).size();j++) {
				if(newCoord==newNoeud.get(i).get(j) || coordInt==newNoeud.get(i).get(j)) {
					testAjoutCoord++;
					coordInt=Cl.get(i);
					testCoordInt=0;
					for(int k=0;k<solutionCoord.size();k++) {
						if(coordInt==solutionCoord.get(k)) {
							testCoordInt++;
						}
					}
					if(testCoordInt==0) {//pas besoin de mettre le point de depart de l agent
						listeCoordInt.add(coordInt);
					}					
				}	
			}
			if(testAjoutCoord==0) {
				// on calcule le cout noeud du dernier element de la solution, si cest 1, on ne retire plus d'element
				if(calculCoutNoeud(solutionCoord.get(solutionCoord.size()-1))>=1) {
				solutionCoord.remove(solutionCoord.size()-1); // on retire le dernier element de la solution
				}
			}
		}
		for(int h=listeCoordInt.size()-1;h>=0;h--) {
			solutionCoord.add(listeCoordInt.get(h));
		}
		System.out.println("solutionCoord : " + solutionCoord);
	}




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
				heuristique = heuristiqueInter;			}
		}
		return heuristique;
	}

	private int calculCoutNoeud(List <Integer> noeud) {
		int coutNoeud=0;
		int deplacementLigne;
		int deplacementColonne;
		deplacementLigne = Math.abs(noeud.get(0)-this.y);
		deplacementColonne = Math.abs(noeud.get(1)-this.x);
		coutNoeud = deplacementColonne + deplacementLigne;
		return coutNoeud;
	}
	private void trouverPoussiere(){ //mise en place de boucle while
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

	/*
	 * MaFonction : positions
	 * Attribut : a : une liste de direction que l'agent peut faire à partir de sa position actuelle Ex:a=[Droite, Haut, Haut]
	 * Rôle : l'agent suit le chemin indiquer dans a, et à partir de sa position finale il retourne une liste qui contient 
	 * les directions qu'il peut choisir
	 *  
	 */
	private List<Direction> positions(List<Direction> a) {
		// créer des nouveaux noeuds pour chaque état
		int newl = this.y; 
		int newc = this.x; 
		// partie algo inf
		switch(mode) {
		case 0:
			int [] resultat;
			List<Direction> directionPossible = new ArrayList<Direction>();
			int i;
			for (i=0; i<a.size(); i++) {
				resultat = calculeP(a.get(i),newl, newc);
				newl = resultat[0];
				newc = resultat[1];
			}
			i--;
			if (newc!=0 && a.get(i) != Direction.droite) directionPossible.add(Direction.gauche);
			if (newc!=4 && a.get(i) != Direction.gauche) directionPossible.add(Direction.droite);
			if (newl!=0 && a.get(i) != Direction.bas) directionPossible.add(Direction.haut);
			if (newl!=4 && a.get(i) != Direction.haut) directionPossible.add(Direction.bas);
			

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
	 * Rôle : calculer la nouvelle position du robot s'il suit une direction donnée et retourner un tableau de deux éléments 
	 * qui contient la nouvelle coordonnée de l'agent 
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
	 * Role: parcourir la liste "solution" et tester si l'agent suit un chemin dans "solution" il arrive à une case qui 
	 * contient la poussière, des bijoux ou bien les deux.
	 * Si "solution" contient un chemin qui permet de réaliser le but, la fonction retourne l'indice du chemin dans "solution" 
	 * sinon il retourne -1.
	 */
	private int goal() {
		// le but est de trouver une poussière
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

	/*
	 * MaFonction : move
	 * attribut : l'indice du chemin dans "solution" que le robot doit suivre pour réaliser son but
	 * Rôle : changer la position du robot en suivant un chemin précis
	 */
	private void move(int index) {
		// extraire la solution de la liste et appliquer les mouvements pour faire bouger l'agent.
		g.env.L[this.y][this.x] = State.empty;
		int newl = this.y;
		int newc = this.x;
		int [] resultat;
		for (int i=0; i<solution.get(index).size(); i++) {
			resultat = calculeP(solution.get(index).get(i), newl, newc);
			newl = resultat[0];
			newc = resultat[1];
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

	private void moveAetoile() {
		g.env.L[this.y][this.x] = State.empty;
		int newc = this.x;
		int newl = this.y;
		for (int i=1; i<solutionCoord.size(); i++) { //i ne part pas de 0 car le premier element de la liste solution est le point de départ
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
	 * Rôle : 1) Pour commencer, l'agent garde une copie de l'environnement 
	 * 		  2) initialiser la liste de direction "solution" : Pour cela, à partir de la position actuelle de l'agent, 
	 * 			on ajoute les directions possibles que l'agent peut faire. Ex: solution = [[Droite], [Gauche], [Haut], [Bas]]
	 * 			certaines directions sont interdites Ex: si l'agent se retrouve dans les frontières 
	 */
	private void init(){
		this.L = g.env.L;

		solution = new ArrayList<List<Direction>>();
		List <Direction> a;
		if (this.x != 0) {
			a = new ArrayList<Direction>();
			a.add(Direction.gauche);
			solution.add(a);
		}
		if (this.x != 4) {
			a = new ArrayList<Direction>();
			a.add(Direction.droite);
			solution.add(a);
		}
		if (this.y != 0) {
			a = new ArrayList<Direction>();
			a.add(Direction.haut);
			solution.add(a);
		}
		if (this.y != 4) {
			a = new ArrayList<Direction>();
			a.add(Direction.bas);
			solution.add(a);
		}

	}



}
