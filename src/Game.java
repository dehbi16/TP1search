import java.util.concurrent.TimeUnit;

public class Game {
	protected boolean isRunning = false;
	protected Agent agent;
	protected Environnement env;
	public Manoir manoir;
	public Game() throws InterruptedException {
		init();	
		isRunning = true;
		while(isRunning) {
			TimeUnit.MILLISECONDS.sleep(300);
			afficher();
			/*
			System.out.println("nombre aspirer = "+agent.nbaspirer);
			System.out.println("nombre bijoux = "+agent.nbbijoux);
			System.out.println("nombre de coût = "+agent.cout);
			System.out.println("nombre d'erreur = "+agent.erreur);
			System.out.println();
			*/
			
			env.run();
			agent.run();
		}
	}
	
	/*
	 * MaFonction : init 
	 * Role : 1) Instancier les classes Manoir, Environnement et Agent
	 * 		  2) Ajouter de la poussière dans l'environnement (dans 7 cases)
	 * 		  3) Ajouter le robot dans l'environnement
	 */
	public void init() {
		manoir= new Manoir();
		env = new Environnement();
		int a;
		int b;
		for (int i=0; i<7; i++) {
			a = (int)(Math.random()*5+1) - 1;
			b = (int)(Math.random()*5+1) - 1;
			env.L[a][b] = State.dust;
		}
		
		a = (int)(Math.random()*5+1) - 1;
		b = (int)(Math.random()*5+1) - 1;
		
		env.L[a][b] = State.robot;
		agent = new Agent(b, a, this);
		manoir.placementD_J_DJ_R(a, b, "4");
		
		
	}
	
	private void afficher() {
		for(int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				//System.out.print(env.L[i][j].tosString()+" ");
				manoir.placementD_J_DJ_R(i, j,env.L[i][j].tosString() );
				manoir.miseAjourSurLeManoirInfoAgent(String.valueOf(agent.nbaspirer), 
						String.valueOf(agent.nbbijoux),
						String.valueOf(agent.cout),
						String.valueOf(agent.erreur));
			}
			//System.out.println();
		}
	}
	
	public String etatCase(String text) {
		String monEtat="";
		
		
		
		return monEtat;
	}
	
	
}





 
 
 



