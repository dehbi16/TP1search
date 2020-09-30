


import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Manoir extends JFrame 
{ 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton[][] mesChambres=new JButton[5][5];  
	private ImageIcon JEWEL = new ImageIcon(getClass().getResource("images/gem.png"));
	private ImageIcon DUST = new ImageIcon(getClass().getResource("images/dust.png"));
	private ImageIcon ROBOT = new ImageIcon(getClass().getResource("images/robot2.png"));
	private ImageIcon DUSTJEWERLY = new ImageIcon(getClass().getResource("images/dujew.png"));
	
	
	public Manoir ()     { 
	        setTitle("Manoir"); 
	        setSize(600, 600);   
	        setLayout(new GridLayout(5,5)); 
	        Container c = getContentPane();             
	        for(int i =0;i<5;i++) {
	      	   for(int j = 0;j<5;j++) {
	      		 mesChambres[i][j]= new JButton();
	      		 c.add(mesChambres[i][j]);
	      	   }
	      	  
	        }
      
	        this.repaint();
	      
	        setLocationRelativeTo(null);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	        this.setVisible(true);
    } 
	
	/*
	 * NomFonction : placementD_J_DJ_R
	 * Attribut:
	 * Rôle : sert à placer sur le manoir le robot, le bijou ou la poussière
	 * 
	 * 
	 */
	public void placementD_J_DJ_R(int indice_i, int indice_j, String typeD_J_DJ_R) {
		mesChambres[indice_i][indice_j].setIcon(retourneImage(typeD_J_DJ_R));
		try {
			mesChambres[indice_i][indice_j].setIcon(retourneImage(typeD_J_DJ_R));
		}catch(Exception ex) {
			
		}
		this.repaint();				
	}
	
	
	public ImageIcon retourneImage(String typeD_J_DJ_R) {
		ImageIcon img = new ImageIcon();
		/*
		empty("0"),
		dust("1"),
		jewelry("2"),
		dustjewelry("3"),
		robot("4");*/
		if(typeD_J_DJ_R.matches("1"))
			img=DUST;
		if(typeD_J_DJ_R.matches("2"))
			img=JEWEL;
		if(typeD_J_DJ_R.matches("3"))
			img=DUSTJEWERLY;
		if(typeD_J_DJ_R.matches("4"))
			img=ROBOT;
		return img;
	}
	
	public void miseAjourSurLeManoirInfoAgent(String nbAspiration,String nbBijouxAspires,String cout,String erreur) {
		setTitle("MANOIR"+" | AGENT { Aspiration : "+nbAspiration +" Bijoux : "+nbBijouxAspires 
				+" Cout: "+cout +" Erreur : "+erreur+ " }"); 
	}
      
  
} 


