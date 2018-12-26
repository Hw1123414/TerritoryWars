import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class territorywar implements ActionListener, KeyListener{

	// Properties
	JFrame theframe = new JFrame("Territory War");
	territorypanel thepanel = new territorypanel();
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
			//thepanel.blnPlayerUp = false;
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
			//thepanel.intBombX = thepanel.intPlayerX;
			//thepanel.intBombY = thepanel.intPlayerY;
			thepanel.blnThrowBomb = true;
		}
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
	
		
	}
	
	// Main Method
	public static void main(String[] args){
		new territorywar();
	}


}
