
public class Environnement {
	protected State [][] L; 

	/*
	 * MaFonction : Environnement (constructeur)
	 * Role : Instancier le tableau L et l'initialiser avec des cases vides
	 */
	public Environnement() {
		L = new State[5][5];
		for (int i=0; i<5; i++) {
			for(int j=0; j<5; j++) {
				L[i][j] = State.empty;
			}
		}
	}

	/*
	 * MaFonction : run
	 * Role : ajouter de la poussière et des bijoux dans l'environnement en suivant des probabilités  
	 */
	protected void run() {
		double r = Math.random(); // Math.radom génére un nombre pseudo-aléatoire entre 0 et 1, le nombre suit une loi uniforme
		if (r <= 0.6) { // une probabilité de 1/6 pour ajouter de la poussière
			int a = (int)(Math.random()*5+1) - 1; // placer aléatoirement la poussière dans une case
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
