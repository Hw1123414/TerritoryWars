import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TerritoryWars implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	// Networking Variables
	String strName;
	boolean blnHost=true;
	
	// Properties
	JFrame frame = new JFrame("Territory Wars");
	TerritoryWarsPanel panel = new TerritoryWarsPanel();
	JTextArea area = new JTextArea();
	JScrollPane scroll = new JScrollPane(area);
	JTextField field = new JTextField();
	JTextField inputIP = new JTextField();
	SuperSocketMaster ssm;
	JButton host = new JButton("Host Server");
	JButton client = new JButton("Join Server");
	JLabel hostIP = new JLabel("host");
	JButton OK = new JButton("OK");
	JLabel entername = new JLabel("Your Name:");
	JTextField namefield = new JTextField();
	JLabel enterhostIP = new JLabel("Host IP:");
	JButton start = new JButton("Start Game");
	Timer timer;
	
	
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == timer){
			panel.repaint();
		}
	
		// Host button Clicked
		if(evt.getSource()==host || evt.getSource()==client){
			if(evt.getSource()==client){
				blnHost=false;
				// Show IP input field
				enterhostIP.setSize(300,25);
				enterhostIP.setLocation(420,500);
				panel.add(enterhostIP);
				inputIP.setSize(300,25);
				inputIP.setLocation(500,500);
				inputIP.addActionListener(this);
				panel.add(inputIP);
			}
			
			// Remove Buttons
			panel.remove(host);
			panel.remove(client);
			panel.validate();
			panel.repaint();
			
			// Ask for name
			entername.setSize(100,25);
			entername.setLocation(410,550);
			panel.add(entername);
			namefield.setSize(300,25);
			namefield.setLocation(500,550);
			namefield.addActionListener(this);
			panel.add(namefield);
			
			// OK Button
			OK.setSize(100,50);
			OK.setLocation(580, 600);
			OK.addActionListener(this);
			panel.add(OK);
		}
		
		// 'OK' button pressed
		if(evt.getSource()==OK){
			// Get username
			strName = namefield.getText();
			// Host
			if(blnHost){
				// Show IP
				hostIP.setSize(400,50);
				hostIP.setLocation(500,500);
				panel.add(hostIP);
				ssm = new SuperSocketMaster(6112, this);
				ssm.connect();
				hostIP.setText("Started server, your IP address is: "+ssm.getMyAddress());
			// Client
			}else{
				ssm = new SuperSocketMaster(inputIP.getText(),6112,this);
				ssm.connect();
			}
			panel.remove(OK);
			panel.remove(namefield);
			panel.remove(entername);
			panel.validate();
			panel.repaint();
			// Show 'Start Game' button
			start.setSize(100,50);
			start.setLocation(580,600);
			start.addActionListener(this);
			panel.add(start);
		}
		if(evt.getSource()==start){
			panel.remove(hostIP);
			panel.remove(start);
			panel.remove(inputIP);
			panel.validate();
			panel.repaint();
			panel.blnStartGame=true;
		}
		
		//Chat
		if(evt.getSource()==field){
			ssm.sendText(strName+": "+field.getText());
			area.append(strName+": "+field.getText()+"\n");
			field.setText("");
		}else if(evt.getSource()==ssm){
			String strData = ssm.readText();
			area.append(strData+"\n");
		}
	}
	
	
	// MouseListener
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	// When mouse is clicked
	public void mousePressed(MouseEvent evt){
		if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720){
			panel.dblBulletX=panel.dblPlayerX;
			panel.dblBulletY=panel.dblPlayerY;

			panel.dblMouseX=evt.getX();
			panel.dblMouseY=evt.getY();
			panel.blnFire=true;
			panel.blnGetSlope=true;
		}
	}

	// MouseMotionListener
	public void mouseMoved(MouseEvent evt){
		panel.dblMouseX=evt.getX();
		panel.dblMouseY=evt.getY();	
	}
	public void mouseDragged(MouseEvent evt){}
	public void mouseClicked(MouseEvent evt){}
	
	// KeyListener
	public void keyReleased(KeyEvent evt){
		switch(evt.getKeyCode()){
			case 37: panel.blnPlayerLeft=false;
				break;
			case 38: panel.blnPlayerUp=false;
				break;
			case 39: panel.blnPlayerRight=false;
				break;
		}
	}
	
	public void keyPressed(KeyEvent evt){
		switch(evt.getKeyCode()){
			case 37: panel.blnPlayerLeft=true;
				break;
			case 38: panel.blnPlayerUp=true;
				break;
			case 39: panel.blnPlayerRight=true;
				break;	
		}
		// Chat box
		if(panel.blnStartGame && evt.getKeyCode()==10){
			scroll.setSize(400,400);
			scroll.setLocation(0,0);
			panel.add(scroll);
			area.setEnabled(false);
			
			field.setSize(400,100);
			field.setLocation(0,400);
			field.addActionListener(this);
			panel.add(field);
		}
	}
	public void keyTyped(KeyEvent evt){}
	
	// Constructor
	public TerritoryWars(){
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));
		
		host.setSize(100,50);
		host.setLocation(500,500);
		host.addActionListener(this);
		panel.add(host);
		
		client.setSize(100,50);
		client.setLocation(700,500);
		client.addActionListener(this);
		panel.add(client);

		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));
		
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.setFocusable(true);
        panel.requestFocus();
		panel.addKeyListener(this);
		
		frame.setResizable(false);
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		timer = new Timer(1000/60, this);
		timer.start();
	}
	
	// Main method
	public static void main(String[] args){
		new TerritoryWars();
	}
}
