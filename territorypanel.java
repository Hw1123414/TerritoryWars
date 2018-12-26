import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class territorypanel extends JPanel{

	// Properties
	int intPlayerX = 100;
	int intPlayerY = 580;
	int intSpeed = 10;
	boolean blnPlayerUp;
	boolean blnPlayerDown;
	boolean blnPlayerLeft;
	boolean blnPlayerRight;
	
	boolean blnThrowBomb;
	int intBombX = 0; 
	int intBombY = 0; 
	
	// Methods
	public void paintComponent(Graphics g){
		
		// White Rectangle Background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1280, 800); 
		
		//Green Grass
		g.setColor(Color.GREEN);
		g.fillRect(0, 600, 1280, 200);
				
		// Player One 
		g.setColor(Color.BLACK);
		g.fillRect(intPlayerX, intPlayerY, 20, 20);
		
		// Player One Movements
		if(blnPlayerLeft){
			intPlayerX = intPlayerX - intSpeed;
		}
		if(blnPlayerRight){
			intPlayerX = intPlayerX + intSpeed;
		}
		if(blnPlayerUp){
			intPlayerY = intPlayerY - intSpeed;
			intPlayerY = intPlayerY + intSpeed; 
		}
		if(blnPlayerDown){
			intPlayerY = intPlayerY + intSpeed;
		}
		
		// Movement Restrictions
		if(intPlayerY > 580){
			intPlayerY = 580;
		}
		if(intPlayerX < 0){
			intPlayerX = 0;
		}
		if(intPlayerX > 1260){
			intPlayerX = 1260;
		}
		
		// Bomb
		if(blnThrowBomb && intBombY < 600){
				g.setColor(Color.BLUE);
				g.fillOval(intBombX, intBombY, 10, 10);
				intBombX = intBombX + 1;
				intBombY = intBombY + 1; 
				//intBombY = (int)(0.005*Math.pow((intBombX), 2) + 100);
			}
		}
	
	// Constructor
	public territorypanel(){
		super();
	}

}
