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
	boolean blnStartGame=false;
	
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
		
	// Character
	double dblPlayerX[];
	double dblPlayerY[];
	int intPlayerWidth = 20;
	int intPlayerHeight = 40;
	int intPlayerSpeed = 2;
	double dblPlayerJump = 6;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnPlayerUp = false;
	boolean blnPlayerDown = false;
	double dblOrigin[];
	int intDisplacement;
	int intPlayer = 0;
	boolean blnPlayerOne = true;
	boolean blnPlayerTwo = false;
	int intPlayerTopRow;
	int intPlayerBottomRow;
	int intPlayerLeftEdgeCol;
	int intPlayerRightEdgeCol;
	boolean blnDrop = false;
	boolean blnJump=false;
	boolean blnRifle = false;
	boolean blnGrenade = false;
	boolean blnMove = true;
	double dblHealth[];
	double dblHealthBarWidth = 40;
	int intHealthBarHeight = 5;
	double dblHealthBarMultiplier;

	// Graphics
	public void paintComponent(Graphics g){	
		g.setColor(Color.white);
		g.fillRect(0,0,1280,800);
		
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
					
			if(blnPlayerOne){
				intPlayer = 0;
				g.setColor(Color.pink);
				g.fillRect((int)Math.round(dblPlayerX[1]), (int)Math.round(dblPlayerY[1]), intPlayerWidth, intPlayerHeight);
				g.setColor(Color.blue);
				
				intPlayerTopRow=(int)(dblPlayerY[1]/40);
				intPlayerBottomRow=(int)((dblPlayerY[1]+intPlayerHeight)/40);
				intPlayerLeftEdgeCol=(int)(dblPlayerX[1]/40);
				intPlayerRightEdgeCol=(int)((dblPlayerX[1]+intPlayerWidth)/40);
				
					// Falling
				if(blnJump==false){
					if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
					|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
						blnDrop=false;
					}else{
						blnDrop=true;
						dblPlayerY[1]+=10;
					}
				}			
			}
			
			if(blnPlayerTwo){
				intPlayer = 1;
				g.setColor(Color.blue);
				g.fillRect((int)Math.round(dblPlayerX[0]), (int)Math.round(dblPlayerY[0]), intPlayerWidth, intPlayerHeight);
				g.setColor(Color.pink);
				
				intPlayerTopRow=(int)(dblPlayerY[0]/40);
				intPlayerBottomRow=(int)((dblPlayerY[0]+intPlayerHeight)/40);
				intPlayerLeftEdgeCol=(int)(dblPlayerX[0]/40);
				intPlayerRightEdgeCol=(int)((dblPlayerX[0]+intPlayerWidth)/40);
				
				// Falling
				if(blnJump==false){
					if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
					|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
						blnDrop=false;
					}else{
						blnDrop=true;
						dblPlayerY[0]+=10;
					}
				}
				
			}
			
			//40/100
			dblHealthBarMultiplier = dblHealthBarWidth/dblHealth[0];
			
			// Player 
			g.fillRect((int)Math.round(dblPlayerX[intPlayer]), (int)Math.round(dblPlayerY[intPlayer]), intPlayerWidth, intPlayerHeight);
			g.setColor(Color.red);
			g.fillRect((int)Math.round(dblPlayerX[intPlayer]) - 10, (int)Math.round(dblPlayerY[intPlayer]) - 10, (int)dblHealthBarWidth, intHealthBarHeight);	
				
			g.setColor(Color.green);
			g.fillRect((int)Math.round(dblPlayerX[intPlayer]) - 10, (int)Math.round(dblPlayerY[intPlayer]) - 10, (int)(dblHealth[0]*dblHealthBarMultiplier), intHealthBarHeight);
			
			//Health doesn't update on the bar?
			System.out.println((int)Math.round(dblHealth[0]));
			
			
			
			if(dblHealth[0] <= 0){
				dblHealth[0] = 0;
			}
			
			
			// Movement restrictions
			if(dblPlayerX[intPlayer] <= 0){
				dblPlayerX[intPlayer] = 0;
			}
			if(dblPlayerX[intPlayer] > (1280 - intPlayerWidth)){
				dblPlayerX[intPlayer] = 1280 - intPlayerWidth;
			}
			if(dblPlayerY[intPlayer] < 0){
				dblPlayerY[intPlayer] = 0;
			}
				
			// Move Left
			if(blnPlayerLeft){
				if(strMap[intPlayerTopRow][intPlayerLeftEdgeCol].equals("s")){
					dblPlayerX[intPlayer]-=intPlayerSpeed;
				}else{
					blnPlayerLeft=false;
					dblPlayerX[intPlayer]+=2;
				}
			}

			//Move Right
			if(blnPlayerRight){
				if(strMap[intPlayerTopRow][intPlayerRightEdgeCol].equals("s")){
					dblPlayerX[intPlayer]+=intPlayerSpeed;
				}else{
					blnPlayerRight=false;
					dblPlayerX[intPlayer]-=2;
				}
			}

			// Jumping
			if(blnJump && blnDrop==false){
				dblPlayerY[intPlayer]-=4*dblPlayerJump;
				dblPlayerJump-=0.5;
				if(strMap[intPlayerTopRow][intPlayerLeftEdgeCol].equals("g")
				|| strMap[intPlayerTopRow][intPlayerRightEdgeCol].equals("g")){
					blnJump=false;
				}
			}

			intPlayerTopRow=(int)(dblPlayerY[intPlayer]/40);
			intPlayerBottomRow=(int)((dblPlayerY[intPlayer]+intPlayerHeight)/40);
			intPlayerLeftEdgeCol=(int)(dblPlayerX[intPlayer]/40);
			intPlayerRightEdgeCol=(int)((dblPlayerX[intPlayer]+intPlayerWidth)/40);

			// Land the Jump
			if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
			|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
				blnJump=false;
				dblPlayerJump=6;
				dblPlayerY[intPlayer]=(intPlayerBottomRow*40)-intPlayerHeight;
			}
			
			// Falling
			if(blnJump==false){
				if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
				|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
					blnDrop=false;
				}else{
					blnDrop=true;
					dblPlayerY[intPlayer]+=10;
				}
			}
				
				// Displacement
				intDisplacement = (int)(dblOrigin[intPlayer] - dblPlayerX[intPlayer]);
				
				if(dblOrigin[intPlayer] - dblPlayerX[intPlayer] < 0){
					intDisplacement = -(int)(dblOrigin[intPlayer] - dblPlayerX[intPlayer]);
				}
				
				if(intDisplacement >= 300){
					intDisplacement = 300;
					if(dblOrigin[intPlayer] - dblPlayerX[intPlayer] < 0){
						blnPlayerRight = false;
					}else if(dblOrigin[intPlayer] - dblPlayerX[intPlayer] > 0){
						blnPlayerLeft = false;
					}
				} 
				
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(20, 20, 300, 40);
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(960, 20, 300, 40);
					
				if(blnPlayerOne){
					g.setColor(Color.RED);
					g.fillRect(20, 20, intDisplacement, 40);
				}
				if(blnPlayerTwo){
					g.setColor(Color.RED);
					g.fillRect(960, 20, intDisplacement, 40);
				}
				
				// Draw laser (constant length of dblLaserLength)
				g.setColor(Color.red);
				// Find angle
				dblLaserAngle = Math.atan2(dblMouseY-dblPlayerY[intPlayer], dblMouseX-dblPlayerX[intPlayer]);
				// Find rise and run using trig
				dblLaserRise = dblLaserLength*Math.sin(dblLaserAngle);
				dblLaserRun = dblLaserLength*Math.cos(dblLaserAngle);
				// Find coordinates of the laser's endpoint
				dblLaserX = dblPlayerX[intPlayer]+dblLaserRun;
				dblLaserY = dblPlayerY[intPlayer]+dblLaserRise;
				// Draw
				g.drawLine((int)Math.round(dblPlayerX[intPlayer]),(int)Math.round(dblPlayerY[intPlayer]),
				(int)Math.round(dblLaserX),(int)Math.round(dblLaserY));
			
				
				if(blnFire){
					blnMove = false;
				
					//Line
					if(blnGetSlope && blnRifle){					
						/*
						 * Angle between horizontal and the imaginary line (player to cursor) is found using arctangent
						 * Rise is found using sine
						 * Run is found using cosine
						 * Run and rise are added to the x and y values respectively for every frame
						 * This method ensures that the bullet will travel at the same speed at all angles
						 * hypotenuse (intSpeed) is constant for any slope
						 */
						dblBulletAngle = Math.atan2(dblMouseY-dblBulletY, dblMouseX-dblBulletX);
						dblBulletRise = intSpeed*Math.sin(dblBulletAngle);
						dblBulletRun = intSpeed*Math.cos(dblBulletAngle);
						blnGetSlope=false; //Only get slope again when the next click happens
						
					}
					
					if(blnGetSlope && blnGrenade){
						dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY[intPlayer], dblMouseX-dblPlayerX[intPlayer]);
						dblBulletRise = -dblPower*Math.sin(dblBulletAngle);
						dblBulletRun = dblPower*Math.cos(dblBulletAngle);
						blnGetSlope=false;
					}
					
					if(blnRifle){
						dblBulletX+=dblBulletRun;
						dblBulletY+=dblBulletRise;	
					}
					if(blnGrenade){
						dblBulletX+=dblBulletRun;
						dblBulletY-=dblBulletRise;
						dblBulletRise-=0.05;
					}	
								
				}
				// Fire bullet
				g.fillOval((int)Math.round(dblBulletX)-5,(int)Math.round(dblBulletY)-5,10,10);
				
				// Once bullet is fired, player can only move after bullet leaves screen
				//CHANGE TO ONCE BULLET LEAVES SCREEN OR HITS SOMETHING
				//Player can hold a key and shoot!?
				if(dblBulletX < 0 || dblBulletX > 1280 || dblBulletY < 0 || dblBulletY > 720){
					blnMove = true;
				}			
		
		}
		
	}

	public TerritoryWarsPanel(){
		super();

		dblPlayerX = new double[2];
		dblPlayerY = new double[2];
		dblOrigin = new double[2];
		dblHealth = new double[2];
		
		// Player one
		dblPlayerX[0] = 50;
		dblPlayerY[0] = 100;
		dblOrigin[0] = dblPlayerX[0];
		dblHealth[0] = 100;
		
		// Player two
		dblPlayerX[1] = 1230;
		dblPlayerY [1] = 100;
		dblOrigin[1] = dblPlayerX[1];
		dblHealth[1] = 100;
		
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
