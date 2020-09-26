
public class Environnement {
	protected State [][] L; 

	public Environnement() {
		L = new State[5][5];
		for (int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				L[i][j] = State.empty;
			}
		}
	}
	
	protected void run() {
		// on peut remplacer le compteur par des probabilités d'apparition 
		double r = Math.random();
		if (r <= 0.6) {
			int a = (int)(Math.random()*5+1) - 1;
			int b = (int)(Math.random()*5+1) - 1;
			if (L[a][b]==State.empty) L[a][b] = State.dust;
			else if (L[a][b]==State.jewelry) L[a][b] = State.dustjewelry;
		}
		if (r>=0.3 && r<=0.8) {
			int a = (int)(Math.random()*5+1) - 1;
			int b = (int)(Math.random()*5+1) - 1;
			if (L[a][b]==State.empty) L[a][b] = State.jewelry;
			else if (L[a][b]==State.dust) L[a][b] = State.dustjewelry;
		}
	}
	
}
