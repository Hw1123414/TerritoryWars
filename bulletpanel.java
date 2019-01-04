import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class bulletpanel extends JPanel{
	
	boolean blnFire=false;
	double dblBulletX=500;
	double dblBulletY=500;
	int intBulletX;
	int intBulletY;
	double dblMouseX;
	double dblMouseY;
	double dblAngle; //Angle in Radians
	boolean blnGetSlope=true; //to get the slope once per click only
	double dblRise;
	double dblRun;
	int intSpeed=2;
	
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
	
	int intPlayerX = 0;
	int intPlayerY = 370;
	int intPlayerWidth = 20;
	int intPlayerHeight = 30;
	int intPlayerSpeed = 20;
	boolean blnPlayerRight = false;
	boolean blnPlayerLeft = false;
	boolean blnPlayerUp = false;
	boolean blnPlayerDown = false;
	
	boolean blnReadMap = false;
		
	public void paintComponent(Graphics g){
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1280, 800); 
		
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
					try{
						wood = ImageIO.read(new File("wood.jpg"));
						g.drawImage(wood, (intColumn*40), (intRow*40), null);
					}catch(IOException e){
						System.out.println("Unable to load wood image");
					}
					
				}else if(strMap[intRow][intColumn].equalsIgnoreCase("s")){ // Draw sky
					try{
						sky = ImageIO.read(new File("water.jpg"));
						g.drawImage(sky, (intColumn*40), (intRow*40), null);
					}catch(IOException e){
						System.out.println("Unable to load sky image");
					}
				}
			}
		}
		

		// Player 
		g.setColor(Color.blue);
		g.fillRect(intPlayerX, intPlayerY, intPlayerWidth, intPlayerHeight);
		
		if(blnPlayerRight){
			intPlayerX = intPlayerX + intPlayerSpeed;
		}
		if(blnPlayerLeft){
			intPlayerX = intPlayerX - intPlayerSpeed;
		}
		if(blnPlayerUp){
			intPlayerY = intPlayerY - intPlayerSpeed;
		}
		
		// Bullet
		if(blnFire){
			g.setColor(Color.red);
			intBulletX = (int)dblBulletX;
			intBulletY = (int)dblBulletY;
			g.fillOval(intBulletX,intBulletY,10,10);
			if(blnGetSlope){
				/*
				 * Imaginary Triangle: 
				 * 	- Hypotenuse: character to crosshair
				 * 	- Angle between horizontal and the hypotenuse is found using arctangent
				 * 	- Rise is found using sine
				 * 	- Run is found using cosine 
				 *	- This method ensures that the bullet will travel at the same speed at all angles
				 * 		(hypotenuse is constant for any slope)
				 */
				dblAngle=Math.atan2(dblMouseY-dblBulletY, dblMouseX-dblBulletX);
				dblRise = intSpeed*Math.sin(dblAngle);
				dblRun = intSpeed*Math.cos(dblAngle);
				blnGetSlope=false; //Only get slope again when the next click happens
			}	
			
			dblBulletX+=dblRun;
			dblBulletY+=dblRise;
		}		
	}

	public bulletpanel(){
		super();
	}
}
