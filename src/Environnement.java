
public class Environnement {
	protected State [][] L; 
	private int count = 0;

	public Environnement() {
		L = new State[5][5];
		for (int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				L[i][j] = State.empty;
			}
		}
	}
	
	protected void run() {
		// on peut remplacer le compteur par des probabilit�s d'apparition 
		if (count == 10) {
			int a = (int)(Math.random()*5+1) - 1;
			int b = (int)(Math.random()*5+1) - 1;
			if (L[a][b]==State.empty) L[a][b] = State.dust;
		}
		else count++;
	}
	
}
