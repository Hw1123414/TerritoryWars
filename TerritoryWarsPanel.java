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
	double dblPlayerX[];
	double dblPlayerY[];
	
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

	boolean blnStartGame=false;
	double dblOrigin[];
	int intX[];
	int intY[];
	int intDisplacement;
	int intPlayer = 0;
	boolean blnPlayerOne = false;
	boolean blnPlayerTwo = true;

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
				
				
				intX[1] = (int)Math.round(dblPlayerX[1])/40;
				intY[1] = (int)Math.round(dblPlayerY[1])/40;  
			
				try{ 
					if(strMap[intY[1]+1][intX[1]].equals("s")){	
						blnPlayerDown = true;
					}
					else if(strMap[intY[1]][intX[1]].equals("g")){ 
						blnPlayerUp = true; 
					}
					else if(strMap[intY[1]-1][intX[1]].equals("g")){  
						blnPlayerUp = false;
						blnPlayerDown = true;
					}

				}
				catch(ArrayIndexOutOfBoundsException e){
					//Die
					dblPlayerY[1]+=10; 
				}

				if(blnPlayerDown){
					dblPlayerY[1]+=10;
				}
				g.setColor(Color.blue);
			}
			
			
			
			else if(blnPlayerTwo){
				intPlayer = 1;
				g.setColor(Color.blue);
				g.fillRect((int)Math.round(dblPlayerX[0]), (int)Math.round(dblPlayerY[0]), intPlayerWidth, intPlayerHeight);
				
				
				intX[0] = (int)Math.round(dblPlayerX[0])/40;
				intY[0] = (int)Math.round(dblPlayerY[0])/40;  
			
				try{ 
					if(strMap[intY[0]+1][intX[0]].equals("s")){	
						blnPlayerDown = true;
					}
					else if(strMap[intY[0]][intX[0]].equals("g")){ 
						blnPlayerUp = true; 
					}
					else if(strMap[intY[0]-1][intX[0]].equals("g")){  
						blnPlayerUp = false;
						blnPlayerDown = true;
					}

				}
				catch(ArrayIndexOutOfBoundsException e){
					//Die
					dblPlayerY[0]+=10; 
				}

				if(blnPlayerDown){
					dblPlayerY[0]+=10;
				}
				g.setColor(Color.pink);
			}
			
		// Player 
				g.fillRect((int)Math.round(dblPlayerX[intPlayer]), (int)Math.round(dblPlayerY[intPlayer]), intPlayerWidth, intPlayerHeight);
				// Movement
				if(blnPlayerRight){
					dblPlayerX[intPlayer]+=intPlayerSpeed;
				}
				if(blnPlayerLeft){
					dblPlayerX[intPlayer]-=intPlayerSpeed;
					
				}
				if(blnPlayerUp){
					dblPlayerY[intPlayer]-=intPlayerJump; 
					blnPlayerUp = false; 
				}
				if(blnPlayerDown){
					dblPlayerY[intPlayer]+=10;
					blnPlayerDown = false;
				}

				// Movement restrictions
				if(dblPlayerX[intPlayer] < 0){
					dblPlayerX[intPlayer] = 0;
				}
				if(dblPlayerX[intPlayer] > (1280 - intPlayerWidth)){
					dblPlayerX[intPlayer] = 1280 - intPlayerWidth;
				}
				if(dblPlayerY[intPlayer] < 0){
					dblPlayerY[intPlayer] = 0;
				}
				
				intX[intPlayer] = (int)Math.round(dblPlayerX[intPlayer])/40;
				intY[intPlayer] = (int)Math.round(dblPlayerY[intPlayer])/40;  
			
				try{ 
					if(strMap[intY[intPlayer]+1][intX[intPlayer]].equals("s")){	
						blnPlayerDown = true;
					}else if(strMap[intY[intPlayer]][intX[intPlayer]].equals("g")){ 
						blnPlayerUp = true; 
					}
					else if(strMap[intY[intPlayer]-1][intX[intPlayer]].equals("g")){  
						blnPlayerUp = false;
						blnPlayerDown = true;
					}
					
				}
				catch(ArrayIndexOutOfBoundsException e){
					//Die
					dblPlayerY[intPlayer]+=10; 
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
						dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY[intPlayer], dblMouseX-dblPlayerX[intPlayer]);
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

		dblPlayerX = new double[2];
		dblPlayerY = new double[2];
		dblOrigin = new double[2];
		intX = new int[2];
		intY = new int[2];
		
		// Player one starting position
		dblPlayerX[0] = 50;
		dblPlayerY[0] = 100;
		dblOrigin[0] = dblPlayerX[0];
		
		// Player two starting position
		dblPlayerX[1] = 1230;
		dblPlayerY [1] = 100;
		dblOrigin[1] = dblPlayerX[1];
		
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
