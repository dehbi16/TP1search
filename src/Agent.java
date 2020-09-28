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
	 * "solutions" pour chaque chemins, 
	 */
	protected void run() {
		int index = goal();
		if(index>=0) {
			/*
			System.out.print("solution: ");
			for(int i=0; i<solution.get(index).size(); i++) {
				System.out.print(solution.get(index).get(i)+" ");
			}
			System.out.println();
			*/
			move(index);
			init();
			if (isclean()) {
				g.isRunning = false;
			}
		}
		else {
			List <Direction> a;
			List <Direction> position;
			int size = solution.size();

			for (int i=0; i<size; i++) {
				a = solution.get(0); // a contient le premier chemin de la liste "solution"
				solution.remove(0); // supprimer le premier chemin
				position = positions(a); // position contient les nouveaux noeuds du chemin a 
				for (int j=0; j<position.size(); j++) { // pour chaque noeud on crée un nouveau chemin et on l'ajoute dans la fin de "solution"
					List <Direction> b = new ArrayList<Direction>();
					b.addAll(a);
					b.add(position.get(j));
					solution.add(b);
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
		int [] resultat;
		List<Direction> directionPossible = new ArrayList<Direction>();
		int i;
		for (i=0; i<a.size(); i++) {
			resultat = calculeP(a.get(i),newl, newc);
			newl = resultat[0];
			newc = resultat[1];
		}
		i--;
		if (newl!=0 && a.get(i) != Direction.bas) directionPossible.add(Direction.haut);
		if (newl!=4 && a.get(i) != Direction.haut) directionPossible.add(Direction.bas);
		if (newc!=0 && a.get(i) != Direction.droite) directionPossible.add(Direction.gauche);
		if (newc!=4 && a.get(i) != Direction.gauche) directionPossible.add(Direction.droite);


		return directionPossible;
	}

	/*
	 * MaFonction : calculeP 
	 * Rôle : calculer la nouvelle position du robot s'il suit une direction donnée et retourner un tableau de deux éléments 
	 * qui contient la nouvelle coordonnée de l'agent 
	 */
	private int[] calculeP(Direction direction, int newl, int newc) {
		int [] resultat = new int [2];
		if (direction == Direction.bas) newl++;
		else if (direction == Direction.droite) newc++;
		else if (direction == Direction.gauche) newc--;
		else newl--; 
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
