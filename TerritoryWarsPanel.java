import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
//figure out how to fix the shooting, then switch turns and should be all goood
public class TerritoryWarsPanel extends JPanel{
	
// Variables

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
	boolean blnFireReady = true;

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
	boolean blnSniper = false;
	boolean blnGrenade = false;
	double dblHealth[];
	double dblHealthBarWidth = 40;
	int intHealthBarHeight = 5;
	double dblHealthBarMultiplier;
	double dblOppHealth;
	
	int intTurn=0;
	int intDisplacement;
	boolean blnJump=false;
	boolean blnHost=true;
	boolean blnSwitchSides=true;
	
	BufferedImage menu;
	BufferedImage help; 
	boolean blnHelpMenu = false; 
	
	// Graphics
	public void paintComponent(Graphics g){	
		g.setColor(Color.white);
		g.fillRect(0,0,1280,800);
		
		if(blnHelpMenu){ 
			g.drawImage(help,0,0, null);
		}
		else{ 
			g.drawImage(menu,0,0,null); 
		}
		
		if(blnStartGame){
		//	System.out.println(dblPlayerY[0]);
			g.setColor(Color.white);
			g.fillRect(0,0,1280,800);
		
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
			g.setColor(Color.pink);
			g.fillRect(intOppX, intOppY, intPlayerWidth, intPlayerHeight);
			//System.out.println(intOppX+", "+intOppY);
			
			// Draw Own Character (controlled by this computer) 
			g.setColor(Color.blue);
			g.fillRect((int)Math.round(dblPlayerX[0]), (int)Math.round(dblPlayerY[0]), intPlayerWidth, intPlayerHeight);
			
			// Character healthbar
			g.setColor(Color.red);
			g.fillRect((int)(Math.round(dblPlayerX[0]) - 10), (int)(Math.round(dblPlayerY[0]) - 10), (int)dblHealthBarWidth, intHealthBarHeight);			
			g.setColor(Color.green);
			g.fillRect((int)(Math.round(dblPlayerX[0]) - 10), (int)(Math.round(dblPlayerY[0]) - 10), (int)(dblHealth[0]*dblHealthBarMultiplier), intHealthBarHeight);
			
			//Opponent healthbar
			g.setColor(Color.red);
			g.fillRect(intOppX - 10,intOppY - 10, (int)dblHealthBarWidth, intHealthBarHeight);			
			g.setColor(Color.green);
			g.fillRect(intOppX - 10, intOppY - 10, (int)(dblOppHealth*dblHealthBarMultiplier), intHealthBarHeight);

			//Health can't drop below 0
			//**add opponent's
			if(dblHealth[0] <= 0){
				dblHealth[0] = 0;
			}
			
			// Movement restrictions
			if(dblPlayerX[0] <= 0){
				dblPlayerX[0] = 0;
			}
			if(dblPlayerX[0] > (1280 - intPlayerWidth)){
				dblPlayerX[0] = 1280 - intPlayerWidth;
			}
			if(dblPlayerY[0] < 0){
				dblPlayerY[0] = 0;
			}

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
			
			// Establish variables for collision
			intPlayerTopRow=(int)(dblPlayerY[0]/40); // Row number of the player's head
			intPlayerBottomRow=(int)((dblPlayerY[0]+intPlayerHeight)/40); // Row number of player's feet
			intPlayerLeftEdgeCol=(int)(dblPlayerX[0]/40); // Column number of player's left side
			intPlayerRightEdgeCol=(int)((dblPlayerX[0]+intPlayerWidth)/40); // Column number of player's right side
			
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
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(960, 20, 300, 40);
			
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
			if(blnFire){
				// Line
				if(blnGetSlope && blnSniper){
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
					dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY[0], dblMouseX-dblPlayerX[0]);
					dblBulletRise = -dblPower*Math.sin(dblBulletAngle);
					dblBulletRun = dblPower*Math.cos(dblBulletAngle);
					blnGetSlope=false;
				}
				if(blnSniper){
					dblBulletX+=dblBulletRun;
					dblBulletY+=dblBulletRise;	
				
				}
				if(blnGrenade){
					dblBulletX+=dblBulletRun;
					dblBulletY-=dblBulletRise;
					dblBulletRise-=0.05;
				}
			}
				// Fire bullet, keep drawing unless it hits a player	
				g.fillOval((int)Math.round(dblBulletX)-5,(int)Math.round(dblBulletY)-5,10,10);
				
				// Once bullet is fired, player can only move after bullet leaves screen
				// CHANGE TO ONCE BULLET LEAVES SCREEN OR HITS SOMETHING
				// Player can hold a key and shoot!?
				if(dblBulletX < 0 || dblBulletX > 1280 || dblBulletY < 0 || dblBulletY > 720){
					blnFireReady = true;
				}	
				
				//Bullet collisions
				if(dblBulletX >= intOppX && dblBulletX <= intOppX + intPlayerWidth
				&& dblBulletY >= intOppY && dblBulletY <= intOppY + intPlayerHeight){
					dblOppHealth = dblOppHealth - 5;
				}

			
		}
	}

	public TerritoryWarsPanel(){
		super();
		
		dblPlayerX = new double[3];
		dblPlayerY = new double[3];
		dblOrigin = new double[3];
		dblHealth = new double[3];
		
		dblPlayerX[0]=50;
		dblPlayerY[0]=100;
		dblOrigin[0]=dblPlayerX[0]; // Should this be in the loop?
		dblHealth[0] = 100;
		
		dblOppHealth = dblHealth[0]; // Make all starting healths the same
		
		dblHealthBarMultiplier = dblHealthBarWidth/dblHealth[0];
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
		// import main menu 
		try{ 
			menu = ImageIO.read(new File("Main Menu.jpg")); 
		}catch(IOException e){ 
			System.out.println("Unable to load main menu image"); 
		}
		
		// import help menu 
		try{ 
			help = ImageIO.read(new File("Help.jpg")); 
		}catch(IOException e){ 
			System.out.println("Unable to load help menu image"); 
		}
	}
	
}
