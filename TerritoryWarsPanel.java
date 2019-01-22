// Territory Wars Panel for animations
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

/** Territory Wars Animation Panel **/
public class TerritoryWarsPanel extends JPanel{
	
// Variables
	boolean blnStartGame=false;
	boolean blnEnd = false;
	boolean blnHost=true;
	boolean blnSwitchSides=true;
	boolean blnHelpMenu = false;
	boolean blnTurn;
	double dblMouseX;
	double dblMouseY;
	
	// Bullet
	boolean blnFire=false;
	boolean blnBulletDisappear=false;
	boolean blnGetSlope=true; //to get the slope once per click only
	double dblBulletX=-50;
	double dblBulletY=-50;
	double dblBulletAngle; //Angle in radians
	double dblBulletRise;
	double dblBulletRun;
	int intBulletSpeed;
	int intBulletDamage;
	int intBulletWidth;
	int intBulletHeight;
	int intBulletTopRow;
	int intBulletBottomRow;
	int intBulletLeftEdgeCol;
	int intBulletRightEdgeCol;
	
	// Aiming laser
	double dblLaserAngle;
	double dblLaserLength=50;
	double dblLaserRise;
	double dblLaserRun;
	double dblLaserX=-20;
	double dblLaserY=-20;
	
	// Sniper
	int intSniperWidth;
	int intSniperHeight;
	int intSniperSpeed;
	int intSniperDamage;
	boolean blnSniper = false;

	// Grenade
	double dblPower=5;
	int intGrenadeWidth;
	int intGrenadeHeight;
	int intGrenadeSpeed;
	int intGrenadeDamage;
	boolean blnGrenade = false;
	
	// Map
	BufferedImage ground;
	BufferedImage sky;
	int intRow;
	int intColumn;
	String strMap[][];
	String strSplit[];
	String strLine = "";
	FileReader map = null;
	BufferedReader mapdata = null;
		
	// Character Movement
	double dblPlayerX = 50;
	double dblPlayerY = 100;
	double dblOrigin = dblPlayerX;
	double dblHealth = 100;
	double dblHealthBarWidth = 40;
	double dblHealthBarMultiplier = dblHealthBarWidth/dblHealth;
	double dblPlayerJump = 6;
	int intPlayerWidth = 20;
	int intPlayerHeight = 40;
	int intPlayerSpeed = 2;
	int intPlayerTopRow;
	int intPlayerBottomRow;
	int intPlayerLeftEdgeCol;
	int intPlayerRightEdgeCol;
	int intHealthBarHeight = 5;
	int intDisplacement;
	boolean blnJump=false;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnDrop = false;
	boolean blnMove = true;
	
	//Opponent
	double dblOppHealth = 100;
	int intOppX=1200;
	int intOppY=100;
	int intOppBulletX = -50;
	int intOppBulletY = -50;

	// Images for screens
	BufferedImage menuImg;
	BufferedImage helpImg; 
	BufferedImage victoryImg;
	BufferedImage defeatImg;

	
	// Graphics
	public void paintComponent(Graphics g){	
		
		g.setColor(Color.white);
		g.fillRect(0,0,1280,800);
		
		// Draw help menu if help button is pressed
		if(blnHelpMenu){ 
			g.drawImage(helpImg,0,0, null);
		}
		// Draw menu screen otherwise
		else{ 
			g.drawImage(menuImg,0,0,null); 
		}
		
		// Keep playing game until a player dies
		if(blnStartGame && dblHealth > 0 && dblOppHealth > 0){
			g.setColor(Color.white);
			g.fillRect(0,0,1280,800);
		
			//Draw Map
			for(intRow = 0; intRow < 18; intRow++){
				for(intColumn = 0; intColumn < 32; intColumn++){
					if(strMap[intRow][intColumn].equals("g")){ // Draw ground
						g.drawImage(ground, (intColumn*40), (intRow*40), null);
					}else if(strMap[intRow][intColumn].equals("s")){ // Draw sky
						g.drawImage(sky, (intColumn*40), (intRow*40), null);
					}
				}
			}
			
			if(blnSwitchSides && blnHost==false){
				dblPlayerX=1200;
				dblOrigin=1200;
				intOppX=0;
				blnSwitchSides=false;
			}
			
			// Draw Opponent
			g.setColor(Color.pink);
			g.fillRect(intOppX, intOppY, intPlayerWidth, intPlayerHeight);
			
			// Draw Own Character (controlled by this computer) 
			g.setColor(Color.blue);
			g.fillRect((int)Math.round(dblPlayerX), (int)Math.round(dblPlayerY), intPlayerWidth, intPlayerHeight);
			
			// Character healthbar
			g.setColor(Color.red);
			g.fillRect((int)(Math.round(dblPlayerX) - 10), (int)(Math.round(dblPlayerY) - 10), (int)dblHealthBarWidth, intHealthBarHeight);			
			g.setColor(Color.green);
			g.fillRect((int)(Math.round(dblPlayerX) - 10), (int)(Math.round(dblPlayerY) - 10), (int)(dblHealth*dblHealthBarMultiplier), intHealthBarHeight);
			
			//Opponent healthbar
			g.setColor(Color.red);
			g.fillRect(intOppX - 10,intOppY - 10, (int)dblHealthBarWidth, intHealthBarHeight);			
			g.setColor(Color.green);
			g.fillRect(intOppX - 10, intOppY - 10, (int)(dblOppHealth*dblHealthBarMultiplier), intHealthBarHeight);

			//Healths can't drop below 0
			if(dblHealth <= 0){
				dblHealth = 0;
			}
			if(dblOppHealth <= 0){
				dblOppHealth = 0;
			}
			
			// Movement restrictions
			if(dblPlayerX <= 0){
				dblPlayerX = 0;
			}
			if(dblPlayerX > (1280 - intPlayerWidth)){
				dblPlayerX = 1280 - intPlayerWidth;
			}
			if(dblPlayerY < 0){
				dblPlayerY = 0;
			}
			
			try{
				// Move Left
				if(blnPlayerLeft){
					if(strMap[intPlayerTopRow][intPlayerLeftEdgeCol].equals("s")){
						dblPlayerX-=intPlayerSpeed;
					}else{
						blnPlayerLeft=false;
						dblPlayerX+=2;
					}
				}
				
				//Move Right
				if(blnPlayerRight){
					if(strMap[intPlayerTopRow][intPlayerRightEdgeCol].equals("s")){
						dblPlayerX+=intPlayerSpeed;
					}else{
						blnPlayerRight=false;
						dblPlayerX-=2;
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
						dblPlayerY-=4*dblPlayerJump;
						dblPlayerJump-=0.5;
					}
				}
				
				// Establish variables for collision
				intPlayerTopRow=(int)(dblPlayerY/40); // Row number of the player's head
				intPlayerBottomRow=(int)((dblPlayerY+intPlayerHeight)/40); // Row number of player's feet
				intPlayerLeftEdgeCol=(int)(dblPlayerX/40); // Column number of player's left side
				intPlayerRightEdgeCol=(int)((dblPlayerX+intPlayerWidth)/40); // Column number of player's right side
				
				// Land the Jump
				if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
				|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
					blnJump=false;
					dblPlayerJump=6;
					dblPlayerY=(intPlayerBottomRow*40)-intPlayerHeight;
				}
				// Falling
				if(blnJump==false){
					if(strMap[intPlayerBottomRow][intPlayerLeftEdgeCol].equals("g")
					|| strMap[intPlayerBottomRow][intPlayerRightEdgeCol].equals("g")){
						blnDrop=false;
					}else{
						blnDrop=true;
						dblPlayerY+=10;
					}
				}
			}
			//  If player falls off screen, die
			catch(ArrayIndexOutOfBoundsException e){
				dblPlayerY = 1000;
				dblHealth = 0;
			}
			
			
			// If player falls off screen, die
			if(dblPlayerY>=720){
				dblHealth=0;
			}
			
			// Displacement
			intDisplacement = (int)(dblOrigin - dblPlayerX);
			
			// If displacement is negative, make it positive
			if(dblOrigin - dblPlayerX < 0){
				intDisplacement = -(int)(dblOrigin - dblPlayerX);
			}
			
			// Max displacement is 300 pixels
			if(intDisplacement > 300){
				intDisplacement = 300;
				// Disable right movement if displacement is 300 pixels right
				if(dblOrigin - dblPlayerX < 0){
					blnPlayerRight = false;
				// Disable left movement if displacement is 300 pixels left
				}else if(dblOrigin - dblPlayerX > 0){
					blnPlayerLeft = false;
				}
			} 
		
			// Draw displacement bar
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(20, 20, 300, 40);
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
			if(blnFire){
				// Line
				if(blnGetSlope && blnSniper){
					intBulletWidth = intSniperWidth;
					intBulletHeight = intSniperHeight;
					intBulletSpeed = intSniperSpeed;
					intBulletDamage = intSniperDamage;
					/*
					 * Angle between horizontal and the imaginary line (player to cursor) is found using arctangent
					 * Rise is found using sine
					 * Run is found using cosine
					 * Run and rise are added to the x and y values respectively for every frame
					 * This method ensures that the bullet will travel at the same speed at all angles
					 * hypotenuse (intSpeed) is constant for any slope
					 */
					dblBulletAngle = Math.atan2(dblMouseY-dblBulletY, dblMouseX-dblBulletX);
					dblBulletRise = intBulletSpeed*Math.sin(dblBulletAngle);
					dblBulletRun = intBulletSpeed*Math.cos(dblBulletAngle);
					blnGetSlope=false; //Only get slope again when the next click happens
				}
				// Parabola
				if(blnGetSlope && blnGrenade){
					intBulletWidth = intGrenadeWidth;
					intBulletHeight = intGrenadeHeight;
					intBulletSpeed = intGrenadeSpeed;
					intBulletDamage = intGrenadeDamage;
					dblBulletAngle = Math.atan2(dblMouseY-dblPlayerY, dblMouseX-dblPlayerX);
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
			if(blnBulletDisappear==false){
				g.fillOval((int)Math.round(dblBulletX)-5,(int)Math.round(dblBulletY)-5,intBulletWidth,intBulletHeight);
			}else{
				dblBulletX=-20;
				dblBulletY=-20;
			}
			
			// Bullet collision
			//	Touches character
			if(dblBulletX+intBulletWidth >= intOppX && dblBulletX <= intOppX + intPlayerWidth
			&& dblBulletY+intBulletHeight >= intOppY && dblBulletY <= intOppY + intPlayerHeight){
				dblOppHealth-=intBulletDamage;
				blnBulletDisappear=true;
			}
			
			intBulletTopRow=(int)(dblBulletY/40); // Row number of the top of the bullet
			intBulletBottomRow=(int)((dblBulletY+intBulletHeight)/40); // Row number of the bottom of the bullet
			intBulletLeftEdgeCol=(int)(dblBulletX/40); // Column number of bullet's left side
			intBulletRightEdgeCol=(int)((dblBulletX+intBulletWidth)/40); // Column number of bullet's right side
			
			try{
				//	Touches ground block
				if(strMap[intBulletTopRow][intBulletLeftEdgeCol].equals("g")
				|| strMap[intBulletTopRow][intBulletRightEdgeCol].equals("g")
				|| strMap[intBulletBottomRow][intBulletLeftEdgeCol].equals("g")
				|| strMap[intBulletBottomRow][intBulletRightEdgeCol].equals("g")){
					blnBulletDisappear=true;	
				}
			}
			
			// Bullet out of bounds
			catch(ArrayIndexOutOfBoundsException e){
				blnBulletDisappear = true;
			}
			
			// Draw Opponent's bullet
			g.setColor(Color.RED);
			g.fillOval(intOppBulletX, intOppBulletY, intBulletWidth, intBulletHeight);	
		}
		
		// If player has not health, draw defeat screen
		if(dblHealth == 0){
			g.drawImage(defeatImg,0,0, null);
			blnEnd = true;
		// If opponent has no health, draw victory screen
		}else if(dblOppHealth <= 0 || intOppY >= 720){
			g.drawImage(victoryImg,0,0, null);
			blnEnd = true;
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
		
		// Load map data into strMap array
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
		
		//Close map file after reading
		try{
			map.close();
		}catch(IOException e){
			System.out.println("Unable to close map file");
		}
		
		// Import map images
		try{
			ground = ImageIO.read(new File("ground.png"));		
		}catch(IOException e){
			System.out.println("Unable to load ground image");
		}
					
		try{
			sky = ImageIO.read(new File("sky.jpg"));	
		}catch(IOException e){
			System.out.println("Unable to load sky image");
		}
		
		// Import main menu image
		try{ 
			menuImg = ImageIO.read(new File("Main Menu.jpg")); 
		}catch(IOException e){ 
			System.out.println("Unable to load main menu image"); 
		}
		
		// Import help menu image
		try{ 
			helpImg = ImageIO.read(new File("Help.jpg")); 
		}catch(IOException e){ 
			System.out.println("Unable to load help menu image"); 
		}
		
		// Import victory screen
		try{ 
			victoryImg = ImageIO.read(new File("victory.png")); 
		}catch(IOException e){ 
			System.out.println("Unable to load victory image"); 
		}
		
		// Import defeat screen
		try{ 
			defeatImg = ImageIO.read(new File("defeat.png")); 
		}catch(IOException e){ 
			System.out.println("Unable to load defeat image"); 
		}
	
		// Create sniper object
		sniperblueprint sniper = new sniperblueprint(intSniperWidth, intSniperHeight, intSniperSpeed, intSniperDamage);
		
		intSniperWidth = sniper.getWidth();
		intSniperHeight = sniper.getHeight();
		intSniperSpeed = sniper.getSpeed();
		intSniperDamage = sniper.getDamage();
		
		// Create grenade object
		grenadeblueprint grenade = new grenadeblueprint(intGrenadeWidth, intGrenadeHeight, intGrenadeSpeed, intGrenadeDamage);
		
		intGrenadeWidth = grenade.getWidth();
		intGrenadeHeight = grenade.getHeight();
		intGrenadeSpeed = grenade.getSpeed();
		intGrenadeDamage = grenade.getDamage();	
		
	}
	
}
