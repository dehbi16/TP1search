
public class Environnement {
	int [][] L; 
	private int count = 0;

	public Environnement() {
		L = new int[5][5];
		for (int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				L[i][j] = 0;
			}
		}
	}
	
	public void run() {
		// on peut remplacer le compteur par des probabilit�s d'apparition 
		if (count == 5) {
			int a= (int)(Math.random()*5+1) - 1;
			int b = (int)(Math.random()*5+1) - 1;
			L[a][b] = 1;
		}
		else count++;
	}
	
}