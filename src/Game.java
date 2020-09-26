import java.util.concurrent.TimeUnit;

public class Game {
	protected boolean isRunning = false;
	protected Agent agent;
	protected Environnement env;
	public Game() throws InterruptedException {
		init();
		
		
		isRunning = true;
		while(isRunning) {
			TimeUnit.MILLISECONDS.sleep(300);
			for(int i=0; i<5; i++) {
				for (int j=0; j<5; j++) {
					System.out.print(env.L[i][j].tosString()+" ");
				}
				System.out.println();
			}
			System.out.println("nombre aspirer = "+agent.nbaspirer);
			System.out.println("nombre bijoux = "+agent.nbbijoux);
			System.out.println("nombre de coût = "+agent.cout);
			System.out.println("nombre d'erreur = "+agent.erreur);
			System.out.println();
			env.run();
			agent.run();
		}
	}
	
	public void init() {
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
		
		
	}
	
	
}
