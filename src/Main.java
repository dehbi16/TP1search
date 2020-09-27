import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;



public class Main {
	static List <Integer> solution= new ArrayList<Integer>();
	static int nbsolution;
	
	
   

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		//int n=10;
		new Game();
		//breathFirst(n);
		//backTracking(10, 0);
		//System.out.println(nbsolution);
		
	}
	
	public static void breathFirst(int n) {
		nbsolution=0;
		List <List<Integer>> L = new ArrayList<List<Integer>>();
		List <Integer> a;
		ArrayList<Integer> position;
		for (int i=0; i<n; i++) {
			a = new ArrayList<Integer>();
			a.add(i);
			L.add(a);
		}
		
		while (L.size()!=0) {
			a = L.get(0);
			L.remove(0);
			if (a.size()==n) {
				nbsolution++;
				System.out.print("[");
				for (int i=0;i<a.size()-1;i++) {
					System.out.print(a.get(i)+",");
				}
				System.out.println(a.get(n-1)+"]");
			}
			else {
				position=positions(a,n);
				for(int i=0; i<position.size(); i++) {
					List <Integer> b = new ArrayList<Integer>();
					b.addAll(a);
					//for (int j=0; j<a.size(); j++) b.add(a.get(j)); 
					b.add(position.get(i));
					L.add(b);
				}
			}
		}
	}
	
	public static ArrayList<Integer> positions (List<Integer> a, int n){
		ArrayList <Integer> positionpossible = new ArrayList <Integer>();
		for (int i=0;i<n;i++) {
			boolean possible=true;
			if (!a.contains(i)) {
				for (int j =0; j<a.size();j++) {
					if (Math.abs(a.get(j)-i)==Math.abs(a.size()-j)) {
						possible=false;
						break;
					} 
				}
				if(possible) {
					positionpossible.add(i);
				}  
			}
		}
		return positionpossible;
		
		
	}

	public static void backTracking(int n,int col) {
		if (solution.size()==n) {
			nbsolution++;
			System.out.print("[");
			for (int i=0;i<solution.size()-1;i++) {
				System.out.print(solution.get(i)+",");
			}
			System.out.println(solution.get(n-1)+"]");
		}
		else {
			ArrayList<Integer> position=positions(n,col);
			for(int i=0;i<position.size();i++) {
				solution.add(position.get(i));
				backTracking(n,col+1);
				solution.remove(solution.size()-1);
			}
		}
	}
	
	public static ArrayList<Integer> positions (int n, int col ){
		ArrayList <Integer> positionpossible = new ArrayList <Integer>();
		for (int i=0;i<n;i++) {
			boolean possible=true;
			if (!solution.contains(i)) {
				for (int j =0; j<solution.size();j++) {
					if (Math.abs(solution.get(j)-i)==Math.abs(col-j)) {
						possible=false;
						break;
					} 
				}
				if(possible) {
					positionpossible.add(i);
				}  
			}
		}
		return positionpossible;
	}
}
