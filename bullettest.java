import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class bullettest implements ActionListener, MouseListener{
	JFrame f;
	bulletpanel p;
	Timer t;
                   
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == t){
			p.repaint();
		}
	}
	
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	// When mouse is clicked
	public void mousePressed(MouseEvent evt){
		if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720){
			//Reset Ball (change to reset to player position later)
			p.dblBulletX=500;
			p.dblBulletY=500;

			p.dblMouseX=evt.getX();
			p.dblMouseY=evt.getY();
			p.blnFire=true;
			p.blnGetSlope=true;
		}
		
	}
	public void mouseClicked(MouseEvent evt){}
	

	
	public bullettest(){
		f = new JFrame("bullet");
		p = new bulletpanel();
		p.setLayout(null);
		p.setPreferredSize(new Dimension(1280,720));
		p.addMouseListener(this);
		
		f.setContentPane(p);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.pack();
		f.setVisible(true);
		
		t = new Timer(1000/60, this);
		t.start();
		
	}
	
	public static void main(String[] args){
		new bullettest();
	}
}
