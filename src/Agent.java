import java.util.ArrayList;
import java.util.List;
//Test
public class Agent {
	protected int x;
	protected int y;
	protected Game g;
	protected static List <List<Direction>> solution;
	
	public Agent(int x, int y, Game g) {
		this.x = x;
		this.y = y;
		this.g = g;
		init();		
	}
	
	protected void run() {
		if (isclean()) {
			g.isRunning = false;
		}
		else {
			int index = goal();
			if(index>=0) {
				System.out.print("solution: ");
				for(int i=0; i<solution.get(index).size(); i++) {
					System.out.print(solution.get(index).get(i)+" ");
				}
				System.out.println("\n");
				move(index);
				init();
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
				for(int i=0; i<solution.size(); i++) {
					for (int k=0; k<solution.get(i).size(); k++) {
						System.out.print(solution.get(i).get(k)+" ");
					}
					System.out.println();
				}
				System.out.println();
			}
		}
	}
	
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
			if (g.env.L[newl][newc] == 1) {
				return i;
			}
		}
		return -1;
	}
	
	private void move(int index) {
		// extraire la solution de la liste et appliquer les mouvements pour faire bouger l'agent.
		g.env.L[this.y][this.x] = 0;
		int newl = this.y;
		int newc = this.x;
		int [] resultat;
		for (int i=0; i<solution.get(index).size(); i++) {
			resultat = calculeP(solution.get(index).get(i), newl, newc);
			newl = resultat[0];
			newc = resultat[1];
		}
		this.y = newl;
		this.x = newc;
		g.env.L[this.y][this.x] = 2;
	}

	private boolean isclean() {
		for(int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				if (g.env.L[i][j] == 1 || g.env.L[i][j] == 5) return false;
			}
		}
		return true;
	}
	
	private void init(){
		for(int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				System.out.print(g.env.L[i][j]+" ");
			}
			System.out.println();
		}
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
		for(int i=0; i<solution.size(); i++) {
			for (int k=0; k<solution.get(i).size(); k++) {
				System.out.print(solution.get(i).get(k)+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	
	
}
