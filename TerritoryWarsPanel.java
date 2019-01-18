import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class TerritoryWarsPanel extends JPanel{
	
// Variables

	double dblMouseX;
	double dblMouseY;
	
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
	double dblPlayerX[];
	double dblPlayerY[];
	double dblOrigin[];
	int intPlayerWidth = 20;
	int intPlayerHeight = 40;
	int intPlayerSpeed = 2;
	double dblPlayerJump = 6;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnDrop = false;
	boolean blnMove = true;
	int intPlayerTopRow;
	int intPlayerBottomRow;
	int intPlayerLeftEdgeCol;
	int intPlayerRightEdgeCol;
	int intOppX=1200;
	int intOppY=100;
	
	boolean blnStartGame=false;
	int intTurn=0;
	int intDisplacement;
	boolean blnJump=false;
	boolean blnHost=true;
	boolean blnSwitchSides=true;
	
	// Graphics
	public void paintComponent(Graphics g){	
		
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
			
			if(blnSwitchSides && blnHost==false){
				dblPlayerX[0]=1200;
				dblOrigin[0]=1200;
				intOppX=0;
				blnSwitchSides=false;
			}
			
			// Draw Opponent
			g.fillRect(intOppX, intOppY, intPlayerWidth, intPlayerHeight);
			
			// Draw Own Character (controlled by this computer) 
			g.setColor(Color.blue);
			g.fillRect((int)Math.round(dblPlayerX[0]), (int)Math.round(dblPlayerY[0]), intPlayerWidth, intPlayerHeight);
			
			// Move Left
			if(blnPlayerLeft){
				if(strMap[intPlayerTopRow][intPlayerLeftEdgeCol].equals("s")){
					dblPlayerX[0]-=intPlayerSpeed;
				}else{
					blnPlayerLeft=false;
					dblPlayerX[0]+=2;
				}
			}
			
			//Move Right
			if(blnPlayerRight){
				if(strMap[intPlayerTopRow][intPlayerRightEdgeCol].equals("s")){
					dblPlayerX[0]+=intPlayerSpeed;
				}else{
					blnPlayerRight=false;
					dblPlayerX[0]-=2;
				}
			}
			
			if(blnDrop){
				blnJump=false;
			}
			
			// Jumping
			if(blnJump && blnDrop==false){
				if(strMap[intPlayerTopRow][intPlayerLeftEdgeCol].equals("g")
				|| strMap[intPlayerTopRow][intPlayerRightEdgeCol].equals("g")){
					blnJump=false;
					blnDrop=true;
					dblPlayerJump=6;
				}else{
					dblPlayerY[0]-=4*dblPlayerJump;
					dblPlayerJump-=0.5;
				}
			}
			
			intPlayerTopRow=(int)(dblPlayerY[0]/40);
			intPlayerBottomRow=(int)((dblPlayerY[0]+intPlayerHeight)/40);
			intPlayerLeftEdgeCol=(int)(dblPlayerX[0]/40);
			intPlayerRightEdgeCol=(int)((dblPlayerX[0]+intPlayerWidth)/40);
			
			// Land the Jump
			if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
			|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
				blnJump=false;
				dblPlayerJump=6;
				dblPlayerY[0]=(intPlayerBottomRow*40)-intPlayerHeight;
			}
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
			
			// Displacement
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(20, 20, 300, 40);
			
			intDisplacement = (int)(dblOrigin[0] - dblPlayerX[0]);
			
			if(dblOrigin[0] - dblPlayerX[0] < 0){
				intDisplacement = -(int)(dblOrigin[0] - dblPlayerX[0]);
			}
			
			if(intDisplacement > 300){
				intDisplacement = 300;
				if(dblOrigin[0] - dblPlayerX[0] < 0){
					blnPlayerRight = false;
				}else if(dblOrigin[0] - dblPlayerX[0] > 0){
					blnPlayerLeft = false;
				}
			} 
	
			g.setColor(Color.RED);
			g.fillRect(20, 20, intDisplacement, 40);
			
		// Draw laser (constant length of dblLaserLength)
			g.setColor(Color.red);
			// Find angle
			dblLaserAngle = Math.atan2(dblMouseY-dblPlayerY[0], dblMouseX-dblPlayerX[0]);
			// Find rise and run using trig
			dblLaserRise = dblLaserLength*Math.sin(dblLaserAngle);
			dblLaserRun = dblLaserLength*Math.cos(dblLaserAngle);
			// Find coordinates of the laser's endpoint
			dblLaserX = dblPlayerX[0]+dblLaserRun;
			dblLaserY = dblPlayerY[0]+dblLaserRise;
			// Draw
			g.drawLine((int)Math.round(dblPlayerX[0]),(int)Math.round(dblPlayerY[0]),
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
					dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY[0], dblMouseX-dblPlayerX[0]);
					dblBulletRise = -dblPower*Math.sin(dblBulletAngle);
					dblBulletRun = dblPower*Math.cos(dblBulletAngle);
					blnGetSlope=false;
				}
				dblBulletX+=dblBulletRun;
				dblBulletY-=dblBulletRise;
				dblBulletRise-=0.05;		
			}
			
		
	}

	public TerritoryWarsPanel(){
		super();
		dblPlayerX = new double[2];
		dblPlayerY = new double[2];
		dblOrigin = new double[2];
		dblPlayerX[0]=0;
		dblPlayerX[1]=0;
		dblPlayerY[0]=100;
		dblPlayerY[1]=100;
		dblOrigin[0]=dblPlayerX[0];
		
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
			sky = ImageIO.read(new File("sky.png"));	
		}catch(IOException e){
			System.out.println("Unable to load sky image");
		}
	}
	
}
