import java.util.ArrayList;
import java.util.List;

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
			if(goal()) {
				move();
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
			}
		}
	}
	
	private List<Direction> positions(List<Direction> a) {
		// créer des nouveaux noeuds pour chaque état
		int newl = y;
		int newc = x;
		int [] resultat;
		List<Direction> directionPossible = new ArrayList<Direction>();
		int i;
		for (i=0; i<a.size(); i++) {
			resultat = calculeP(a.get(i),newl, newc);
			newl = resultat[0];
			newc = resultat[1];
		}
		if (newl!=0 && a.get(i) != Direction.bas) directionPossible.add(Direction.haut);
		if (newl!=4 && a.get(i) != Direction.haut) directionPossible.add(Direction.bas);
		if (newc!=0 && a.get(i) != Direction.droite) directionPossible.add(Direction.gauche);
		if (newl!=4 && a.get(i) != Direction.gauche) directionPossible.add(Direction.droite);

		
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

	private void move() {
		// extraire la solution de la liste et appliquer les mouvements pour faire bouger l'agent.
	}

	private boolean goal() {
		// le but est de trouver une poussière
		return false;
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
