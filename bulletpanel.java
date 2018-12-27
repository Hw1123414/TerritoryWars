import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.util.Random;

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
	
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		g.fillRect(0,0,1280,720);
		g.setColor(Color.red);
		intBulletX = (int)dblBulletX;
		intBulletY = (int)dblBulletY;
		g.fillOval(intBulletX,intBulletY,10,10);
		if(blnFire=true){
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
