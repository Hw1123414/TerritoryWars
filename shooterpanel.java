import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class shooterpanel extends JPanel{

	// Properties
	int intPlayerX = 500;
	int intPlayerY = 400;
	int intPlayerWidth = 40;
	int intPlayerHeight = 40;
	int intPlayerSpeed = 10;
	
	int intBulletX = 0;
	int intBulletY = 0;
	int intBulletWidth = 10;
	int intBulletHeight = 10;
	int intBulletSpeed = 30;
	
	int intCrossHairX;
	int intCrossHairY;
	int intCrossHairWidth = 20;
	int intCrossHairHeight = 20;
	
	int intSlope;
	
	boolean blnPlayerUp;
	boolean blnPlayerDown;
	boolean blnPlayerLeft;
	boolean blnPlayerRight;
	
	boolean blnShoot;
	
	// Methods
	public void paintComponent(Graphics g){
		
		// Green Rectangle Background
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, 1280, 800); 
		
		// Player One
		g.setColor(Color.BLACK);
		g.fillOval(intPlayerX, intPlayerY, intPlayerWidth, intPlayerHeight);
		
		// Player One Movements
		if(blnPlayerLeft){
			intPlayerX = intPlayerX - intPlayerSpeed;
		}
		if(blnPlayerRight){
			intPlayerX = intPlayerX + intPlayerSpeed;
		}
		if(blnPlayerUp){
			intPlayerY = intPlayerY - intPlayerSpeed;
		}
		if(blnPlayerDown){
			intPlayerY = intPlayerY + intPlayerSpeed;
		}
		
		// Border Restrictions
		if(intPlayerX >= (1280 - intPlayerWidth)){
			intPlayerX = 1280 - intPlayerWidth;
		}
		if(intPlayerX <= 0){
			intPlayerX = 0;
		}
		if(intPlayerY >= (800 - intPlayerHeight)){
			intPlayerY = 800 - intPlayerHeight;
		}
		if(intPlayerY <= 0){
			intPlayerY = 0;
		}
		
		// CrossHair
		g.setColor(Color.WHITE);
		g.drawOval(intCrossHairX, intCrossHairY, intCrossHairWidth, intCrossHairHeight);
		
		// Bullet
		g.setColor(Color.RED);
		g.fillOval(intBulletX, intBulletY, intBulletWidth, intBulletHeight);
		
		if(blnShoot){
			intBulletX = intBulletX + 1;
			intBulletY = (int)((intCrossHairY - intPlayerY)/(intCrossHairX - intPlayerX))*(intBulletY);
		}
		
		
		
		
	}
	
	// Constructor
	public shooterpanel(){
		super();
	}




}
