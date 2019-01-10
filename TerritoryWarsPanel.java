import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class TerritoryWarsPanel extends JPanel{
	
// Variables
	// General
	double dblMouseX;
	double dblMouseY;
	double dblPlayerX=0;
	double dblPlayerY=100;
	
	// Bullet Travel
	boolean blnFire=false;
	double dblBulletX;
	double dblBulletY;
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
	FileReader map = null;
	BufferedReader mapdata = null;
		
	// Character Movement
	int intPlayerWidth = 20;
	int intPlayerHeight = 40;
	int intPlayerSpeed = 2;
	int intPlayerJump = 100;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnPlayerUp = false;
	boolean blnPlayerDown = false;
	boolean blnMove = true;
	
	boolean blnStartGame=true;
	double dblOrigin = dblPlayerX;
	int intX;
	int intY;
	int intDisplacement;
	
	// Graphics
	public void paintComponent(Graphics g){	
		if(blnStartGame){	
			//Draw Map
			for(intRow = 0; intRow < 18; intRow++){
				for(intColumn = 0; intColumn < 32; intColumn++){
					if(strMap[intRow][intColumn].equals("g")){ // Draw ground
						g.drawImage(wood, (intColumn*40), (intRow*40), null);
					}else if(strMap[intRow][intColumn].equals("s")){ // Draw sky
						g.drawImage(sky, (intColumn*40), (intRow*40), null);
					
					}
				}
			}
			
			// Player 
			g.setColor(Color.blue);
			g.fillRect((int)Math.round(dblPlayerX), (int)Math.round(dblPlayerY), intPlayerWidth, intPlayerHeight);
			if(blnPlayerRight){
				dblPlayerX+=intPlayerSpeed;
			}
			if(blnPlayerLeft){
				dblPlayerX-=intPlayerSpeed;
			}
			if(blnPlayerUp){
				dblPlayerY-=intPlayerJump; 
				blnPlayerUp = false; 
			}
			if(blnPlayerDown){
				dblPlayerY+=10;
				blnPlayerDown = false;
			}


			intX = (int)Math.round(dblPlayerX)/40;
			intY = (int)Math.round(dblPlayerY)/40;  
		
			try{ 
				if(strMap[intY+1][intX].equals("s")){	
					blnPlayerDown = true;
				}
			
				else if(strMap[intY][intX].equals("g")){ 
					blnPlayerUp = true; 
				}
				
				else if(strMap[intY-1][intX].equals("g")){
					blnPlayerUp = false;
					blnPlayerDown = true;
				}
				
				
			//if(intX*40 < dblPlayerX && intX *40 > dblPlayerX+40){
			//if(intY*40 < dblPlayerY && intY*40 > dblPlayerY+40){
			
				
			}
			catch(ArrayIndexOutOfBoundsException e){
				//Die
				dblPlayerY+=10; 
			}
			
			// Displacement
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(20, 20, 300, 40);
			
			intDisplacement = (int)(dblOrigin - dblPlayerX);
			
			if(dblOrigin - dblPlayerX < 0){
				intDisplacement = -(int)(dblOrigin - dblPlayerX);
			}
			
			if(intDisplacement > 300){
				intDisplacement = 300;
				if(dblOrigin - dblPlayerX < 0){
					blnPlayerRight = false;
				}else if(dblOrigin - dblPlayerX > 0){
					blnPlayerLeft = false;
				}

			} 
	
			g.setColor(Color.RED);
			g.fillRect(20, 20, intDisplacement, 40);
			
		// Draw laser (constant length of dblLaserLength)
			g.setColor(Color.red);
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
	}

	public TerritoryWarsPanel(){
		super();
		
		// Load map csv
		strMap = new String[18][32];
		
		try{
			map = new FileReader("map.csv");
		}catch(FileNotFoundException e){
			System.out.println("Error! Could not find map file.");
		}
		
		mapdata = new BufferedReader(map);

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
		
		// import map images
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
