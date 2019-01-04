import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;

public class bullettest implements ActionListener, MouseListener, KeyListener{
	JFrame frame;
	bulletpanel panel;
	Timer timer;
	                   
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == timer){
			panel.repaint();
		}
	}
	
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	// When mouse is clicked
	public void mousePressed(MouseEvent evt){
		if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720){
			//Reset Ball (change to reset to player position later)
			panel.dblBulletX=500;
			panel.dblBulletY=500;

			panel.dblMouseX=evt.getX();
			panel.dblMouseY=evt.getY();
			panel.blnFire=true;
			panel.blnGetSlope=true;
		}
		
	}
	public void mouseClicked(MouseEvent evt){}
	
	public void keyReleased(KeyEvent evt){
		if(evt.getKeyCode() == 37){
			panel.blnPlayerLeft = false;
		}else if(evt.getKeyCode() == 38){
			panel.blnPlayerUp = false;
		}else if(evt.getKeyCode() == 39){
			panel.blnPlayerRight = false;
		}else if(evt.getKeyCode() == 40){
			panel.blnPlayerDown = false;
		}	
	}
	
	public void keyPressed(KeyEvent evt){
		if(evt.getKeyCode() == 37){
			panel.blnPlayerLeft = true;
		}else if(evt.getKeyCode() == 38){
			panel.blnPlayerUp = true;
		}else if(evt.getKeyCode() == 39){
			panel.blnPlayerRight = true;
		}else if(evt.getKeyCode() == 40){
			panel.blnPlayerDown = true;
		}
	}

	public void keyTyped(KeyEvent evt){}

		
	public bullettest(){
		frame = new JFrame("bullet");
		panel = new bulletpanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));

		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(false); 
		frame.setVisible(true);
		
		frame.addKeyListener(this);
		panel.addMouseListener(this);
		
		timer = new Timer(1000/60, this);
		timer.start();
	}
	
	public static void main(String[] args){
		new bullettest();	
	}
}
