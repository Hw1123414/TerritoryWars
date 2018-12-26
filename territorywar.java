import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class territorywar implements ActionListener, KeyListener, MouseListener, MouseMotionListener{

	// Properties
	JFrame theframe = new JFrame("Territory War");
	shooterpanel thepanel = new shooterpanel();
	Timer thetimer = new Timer(1000/60, this); 
	
	
	// Methods
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == thetimer){ 
				thepanel.repaint();
		}
	}
	
	public void keyReleased(KeyEvent evt){
		if(evt.getKeyCode() == 37){
			thepanel.blnPlayerLeft = false;
		}else if(evt.getKeyCode() == 38){
			thepanel.blnPlayerUp = false;
			thepanel.blnPlayerDown = false;
		}else if(evt.getKeyCode() == 39){
			thepanel.blnPlayerRight = false;
		}else if(evt.getKeyCode() == 40){
			thepanel.blnPlayerDown = false;
		}	
	}
	
	public void keyPressed(KeyEvent evt){
		if(evt.getKeyCode() == 37){
			thepanel.blnPlayerLeft = true;
		}else if(evt.getKeyCode() == 38){
			thepanel.blnPlayerUp = true;
		}else if(evt.getKeyCode() == 39){
			thepanel.blnPlayerRight = true;
		}else if(evt.getKeyCode() == 40){
			thepanel.blnPlayerDown = true;
		}
	}

	public void keyTyped(KeyEvent evt){
		if(evt.getKeyChar() == 'q'){
			//thepanel.intBulletX = thepanel.intPlayerX;
			//thepanel.intBulletY = thepanel.intPlayerY;
			thepanel.blnShoot = true;
		}
	}
	
	public void mouseExited(MouseEvent evt){
	}
	
	public void mouseEntered(MouseEvent evt){
	}
	
	public void mousePressed(MouseEvent evt){
		thepanel.intBulletX = thepanel.intPlayerX;
		thepanel.intBulletY = thepanel.intPlayerY;
		thepanel.blnShoot = true;
	}
	
	public void mouseClicked(MouseEvent evt){
	
	}
	
	public void mouseReleased(MouseEvent evt){
		
	}
	
	public void mouseMoved(MouseEvent evt){
		thepanel.intCrossHairX = evt.getX();
		thepanel.intCrossHairY = evt.getY();
	}
	
	public void mouseDragged(MouseEvent evt){
		thepanel.intCrossHairX = evt.getX();
		thepanel.intCrossHairY = evt.getY();
		thepanel.blnShoot = true;
	}
	
	
	// Constructor
	public territorywar(){
	
	thepanel.setLayout(null);
	thepanel.setPreferredSize(new Dimension(1280, 800));

	theframe.setContentPane(thepanel);
	theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	theframe.pack();
	theframe.setResizable(false); // Disable resizing
	theframe.setVisible(true);

	thetimer.start();
	theframe.addKeyListener(this);
	thepanel.addMouseMotionListener(this);
	thepanel.addMouseListener(this);
		
	}
	
	// Main Method
	public static void main(String[] args){
		new territorywar();
	}


}
