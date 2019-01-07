import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class bulletpanel extends JPanel{
// Variables
	// General
	double dblMouseX;
	double dblMouseY;
	double dblPlayerX=0;
	double dblPlayerY=100;
	
	// Bullet Travel
	boolean blnFire=false;
	double dblBulletX=500;
	double dblBulletY=500;
	double dblBulletAngle; //Angle in radians
	boolean blnGetSlope=true; //to get the slope once per click only
	double dblBulletRise;
	double dblBulletRun;
	int intSpeed=4;
	
	// Laser
	double dblLaserAngle;
	double dblLaserLength=50;
	double dblLaserRise;
	double dblLaserRun;
	double dblLaserX;
	double dblLaserY;
	
	// Grenade
	double dblPower=5;
	
	// Map
	BufferedImage wood;
	BufferedImage sky;
	int intRow;
	int intColumn;
	String strMap[][];
	String strSplit[];
	String strLine = "";
	boolean blnMapFail = false;
	FileReader map = null;
	BufferedReader mapdata = null;
	boolean blnReadMap = false;
		
	// Character Movement
	int intPlayerWidth = 20;
	int intPlayerHeight = 30;
	int intPlayerSpeed = 20;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnPlayerUp = false;
	
	// New Variables
	boolean blnPlayerDown = false;
	BufferedImage screencapture;
	

	
	public void paintComponent(Graphics g){		
		if(blnReadMap == false){
		strMap = new String[18][32];
			
			//Read map.csv file
			try{
				map = new FileReader("map.csv");
			}catch(FileNotFoundException e){
				System.out.println("Error! Could not find map file.");
				blnMapFail = true;
			}
			
			// Map data can only be read if map file is found
			if(blnMapFail == false){
				mapdata = new BufferedReader(map);
			}

			for(intRow = 0; intRow < 18; intRow++){ 
				try{ 
					strLine = mapdata.readLine();
				}catch(IOException e){
					System.out.println("Can't read line");
				}
				strSplit = strLine.split(",");  
				for(intColumn = 0; intColumn < 32; intColumn++){ 
					strMap[intRow][intColumn] = strSplit [intColumn];   
				}	
			}

		}
		blnReadMap = true;

		for(intRow = 0; intRow < 18; intRow++){
			for(intColumn = 0; intColumn < 32; intColumn++){
				if(strMap[intRow][intColumn].equalsIgnoreCase("g")){ // Draw ground
					g.drawImage(wood, (intColumn*40), (intRow*40), null);
					
					
				}else if(strMap[intRow][intColumn].equalsIgnoreCase("s")){ // Draw sky
					g.drawImage(sky, (intColumn*40), (intRow*40), null);
				
				}
			}
		}
		
		// Player 
		g.setColor(Color.blue);
		g.fillRect((int)Math.round(dblPlayerX), (int)Math.round(dblPlayerY), intPlayerWidth, intPlayerHeight);
		
		if(blnPlayerRight){
			dblPlayerX = dblPlayerX + intPlayerSpeed;
		}
		if(blnPlayerLeft){
			dblPlayerX = dblPlayerX - intPlayerSpeed;
		}
		if(blnPlayerUp){
			dblPlayerY = dblPlayerY - intPlayerSpeed;
		}
		if(blnPlayerDown){
			dblPlayerY = dblPlayerY + intPlayerSpeed;			
		}
		
		
		g.setColor(Color.red);
		
	// Draw laser (constant length of dblLaserLength)
		// Find angle
		dblLaserAngle = Math.atan2(dblMouseY-dblPlayerY, dblMouseX-dblPlayerX);
		// Find rise and run using trig
		dblLaserRise = dblLaserLength*Math.sin(dblLaserAngle);
		dblLaserRun = dblLaserLength*Math.cos(dblLaserAngle);
		// Find coordinates of the laser's endpoint
		dblLaserX = dblPlayerX+dblLaserRun;
		dblLaserY = dblPlayerY+dblLaserRise;
		// Draw
		g.drawLine((int)Math.round(dblPlayerX),(int)Math.round(dblPlayerY),
		(int)Math.round(dblLaserX),(int)Math.round(dblLaserY));
		
		// Fire bullet
		g.fillOval((int)Math.round(dblBulletX)-5,(int)Math.round(dblBulletY)-5,10,10);
		if(blnFire){
			/* Line
			if(blnGetSlope){
				/*
				 * Angle between horizontal and the imaginary line (player to cursor) is found using arctangent
				 * Rise is found using sine
				 * Run is found using cosine
				 * Run and rise are added to the x and y values respectively for every frame
				 * This method ensures that the bullet will travel at the same speed at all angles
				 * hypotenuse (intSpeed) is constant for any slope
				 *
				dblBulletAngle = Math.atan2(dblMouseY-dblBulletY, dblMouseX-dblBulletX);
				dblBulletRise = intSpeed*Math.sin(dblBulletAngle);
				dblBulletRun = intSpeed*Math.cos(dblBulletAngle);
				blnGetSlope=false; //Only get slope again when the next click happens
			}
			dblBulletX+=dblBulletRun;
			dblBulletY+=dblBulletRise;
			*/
			 
			if(blnGetSlope){
				dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY, dblMouseX-dblPlayerX);
				dblBulletRise = -dblPower*Math.sin(dblBulletAngle);
				dblBulletRun = dblPower*Math.cos(dblBulletAngle);
				blnGetSlope=false;
			}
			dblBulletX+=dblBulletRun;
			dblBulletY-=dblBulletRise;
			dblBulletRise-=0.05;		
		}
	}

	public bulletpanel(){
		super();
		try{
			wood = ImageIO.read(new File("wood.jpg"));		
		}catch(IOException e){
			System.out.println("Unable to load wood image");
		}
					
		try{
			sky = ImageIO.read(new File("water.jpg"));	
		}catch(IOException e){
			System.out.println("Unable to load sky image");
		}
	}
	
}
