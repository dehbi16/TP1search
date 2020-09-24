import java.util.concurrent.TimeUnit;

public class Game {
	protected boolean isRunning = false;
	protected Agent agent;
	protected Environnement env;
	public Game() throws InterruptedException {
		init();
		
		
		isRunning = true;
		while(isRunning) {
			TimeUnit.SECONDS.sleep(1);
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
			env.L[a][b] = 1;
		}
		a = (int)(Math.random()*5+1) - 1;
		b = (int)(Math.random()*5+1) - 1;
		
		env.L[a][b] = 2;
		agent = new Agent(b, a, this);
		
		
	}
	
	
}
