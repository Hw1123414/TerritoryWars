import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class bullettest implements ActionListener, MouseListener{
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
	

	
	public bullettest(){
		frame = new JFrame("bullet");
		panel = new bulletpanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));
		panel.addMouseListener(this);
		
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		timer = new Timer(1000/60, this);
		timer.start();
		
	}
	
	public static void main(String[] args){
		new bullettest();
		String strMap[][];
		strMap = new String[32][18];
		
		
	}
}
