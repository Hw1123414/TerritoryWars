import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TerritoryWars implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	// Networking Variables
	String strName;
	boolean blnHost=true;
	String strSplit[] = new String[2];
	
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
	JButton sniper = new JButton("Sniper"); 
	JButton grenade = new JButton("Grenade"); 
	JButton pistol = new JButton("Pistol"); 
	JButton rocket = new JButton("Rocket Launcher"); 
	JButton start = new JButton("Start Game");
	JButton chat = new JButton("Chat");
	JButton play = new JButton("Play");
	JButton quit = new JButton("Quit");
	JButton stopbut = new JButton("Stop");
	String strData;
	Timer timer;
	JLabel waiting = new JLabel("Waiting for host to start game...");
	boolean blnShoot = false;
	boolean blnEndturn = false; 
	boolean blnChat = false;
	
	
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == timer){
			panel.repaint();
			if(panel.blnStartGame){
				ssm.sendText("p"+(int)Math.round(panel.dblPlayerX[0])+","+(int)Math.round(panel.dblPlayerY[0]));
			}
		}
		
		//Play button clicked
		if(evt.getSource() == play){
			panel.remove(play);
			panel.remove(quit);
			panel.validate();
			panel.repaint();
			
			host.setSize(100,50);
			host.setLocation(500,500);
			host.addActionListener(this);
			panel.add(host);
			
			client.setSize(100,50);
			client.setLocation(700,500);
			client.addActionListener(this);
			panel.add(client);
		}
		
		//Quit button clicked
		if(evt.getSource() == quit){
			 System.exit(0);
		}
		
		// Host button Clicked
		if(evt.getSource()==host || evt.getSource()==client){
			if(evt.getSource()==client){
				blnHost=false;
				panel.blnHost=false;
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
			panel.remove(OK);
			panel.remove(namefield);
			panel.remove(entername);
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
				// Show 'Start Game' button
				start.setSize(100,50);
				start.setLocation(580,600);
				start.addActionListener(this);
				panel.add(start);
			// Client
			}else{
				ssm = new SuperSocketMaster(inputIP.getText(),6112,this);
				ssm.connect();
				panel.remove(enterhostIP);
				panel.remove(inputIP);
				waiting.setSize(500,50);
				waiting.setLocation(520,400);
				panel.add(waiting);
			}
		}
		// Host Clicks start button
		if(evt.getSource()==start){
			panel.remove(hostIP);
			panel.remove(start);
			panel.remove(inputIP);
			panel.remove(enterhostIP);
			panel.validate();
			panel.repaint();
			panel.blnStartGame=true;
			// Tells client that game has been started
			ssm.sendText("s");
			
			// Chat button
			chat.setLocation(1200,680); 
			chat.setSize(80,40); 
			chat.addActionListener(this);
			panel.add(chat);
			
			// Stop button
			stopbut.setSize(100, 50);
			stopbut.setLocation(600,100);
			stopbut.addActionListener(this);
			panel.add(stopbut);
			
			// sniper button
			sniper.setSize(100, 50);
			sniper.setLocation(540, 25);
			sniper.addActionListener(this);
			panel.add(sniper);	
			sniper.setEnabled(false);
			
			// Grenade button
			grenade.setSize(100, 50);
			grenade.setLocation(640, 25);
			grenade.addActionListener(this);
			panel.add(grenade);
			grenade.setEnabled(false);
		}
		
		// "Chat" button
		if(evt.getSource()==chat){
			if(blnChat==false){ 
				blnChat=true; 
				scroll.setSize(400,400);
				scroll.setLocation(0,0);
				panel.add(scroll);
				area.setEnabled(false);

				field.setSize(400,100);
				field.setLocation(0,400);
				field.addActionListener(this);
				panel.add(field);
			}else{
				blnChat=false;
				panel.remove(scroll); 
				panel.remove(field); 
				panel.requestFocus(); 
				panel.validate();
				panel.repaint();
			}
		}
		
		//Send Chat
		if(evt.getSource()==field){
			ssm.sendText("c"+strName+": "+field.getText());
			area.append(strName+": "+field.getText()+"\n");
			field.setText("");
		}
		
		// Receive Data
		// First character of string is used to determine type of data
		if(evt.getSource()==ssm){
			strData=ssm.readText();
			//System.out.println(strData);
			// Chat data
			if(strData.substring(0,1).equals("c")){
				area.append(strData.substring(1,strData.length())+"\n");
			// Position data
			}else if(strData.substring(0,1).equals("p")){
				strData=strData.substring(1,strData.length());
				this.strSplit=strData.split(",");
				panel.intOppX=Integer.parseInt(strSplit[0]);
				panel.intOppY=Integer.parseInt(strSplit[1]);
				System.out.println(panel.intOppX+", "+panel.intOppY);
			// Game Start data
			}else if(strData.equals("s") && blnHost==false){
				panel.blnStartGame=true;
				panel.remove(waiting);
				chat.setLocation(1200,680); 
				chat.setSize(80,40); 
				chat.addActionListener(this);
				panel.add(chat);
				panel.validate();
				panel.repaint();
			}
			//To-do: data for health, bullet
		}
		
		// sniper button
		if(evt.getSource() == sniper){
			panel.blnsniper = true;
			panel.blnGrenade = false;
			grenade.setEnabled(false);
			stopbut.setEnabled(false);
			panel.requestFocus();
		// Grenade button
		}else if(evt.getSource() == grenade){
			panel.blnGrenade = true;
			panel.blnsniper = false;
			sniper.setEnabled(false);
			stopbut.setEnabled(false);
			panel.requestFocus();
		}	
		
		if(evt.getSource() == stopbut && blnEndturn == false){
			grenade.setEnabled(true);
			sniper.setEnabled(true);
			blnEndturn = true;
		}
		
		// If stop button is pressed, player switch turns
		else if(evt.getSource() == stopbut && blnEndturn){
			if(panel.blnPlayerOne){
				panel.blnPlayerOne = false;
				panel.dblOrigin[0] = panel.dblPlayerX[0];
			}else{
				panel.blnPlayerOne = true;
				panel.dblOrigin[1] = panel.dblPlayerX[1];
			}
			panel.intDisplacement = 0;
			panel.requestFocus(); 
			panel.blnsniper = false;
			panel.blnGrenade = false;
			blnEndturn = false;
			sniper.setEnabled(false);
			grenade.setEnabled(false);
		}	
		
		if(blnShoot){
			if(panel.dblBulletX < 0 || panel.dblBulletX > 1280 || panel.dblBulletY < 0 || panel.dblBulletY > 720){
				if(panel.blnPlayerOne){
					panel.blnPlayerOne = false;
					panel.dblOrigin[0] = panel.dblPlayerX[0];
				}else{
					panel.blnPlayerOne = true;
					panel.dblOrigin[1] = panel.dblPlayerX[1];
				}
				panel.intDisplacement = 0;
				panel.requestFocus(); 
				sniper.setEnabled(false); 
				grenade.setEnabled(false);  
				stopbut.setEnabled(true);
				blnShoot = false;
				blnEndturn = false;
				panel.blnsniper = false;
				panel.blnGrenade = false;
			}
		}
		
	}
		
	// MouseListener
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	// When mouse is clicked
	public void mousePressed(MouseEvent evt){
		if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720){
			panel.dblBulletX=panel.dblPlayerX[0];
			panel.dblBulletY=panel.dblPlayerY[0];
			panel.dblMouseX=evt.getX();
			panel.dblMouseY=evt.getY();
			panel.blnFire=true;
			panel.blnGetSlope=true;
			panel.blnFireReady = false;
			panel.requestFocus();
			blnShoot = true;
			sniper.setEnabled(false); 
			grenade.setEnabled(false);  
			stopbut.setEnabled(false);
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
			case 38: 
				break;
			case 39: panel.blnPlayerRight=false;
				break;
		}
	
	}
	
	public void keyPressed(KeyEvent evt){
		
		switch(evt.getKeyCode()){
			case 37:
				if(panel.intDisplacement < 300 || panel.dblOrigin[0] - panel.dblPlayerX[0] < 0){
					panel.blnPlayerLeft=true;
				}
				break;
			case 38: panel.blnJump=true;
				break;
			case 39: 
				if(panel.intDisplacement < 300 || panel.dblOrigin[0] - panel.dblPlayerX[0] > 0){
					panel.blnPlayerRight=true;
				}
				break;	
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
		
		sniper.setSize(200,200); 
		sniper.setLocation(0,520); 
		sniper.addMouseListener(this); 
		
		pistol.setSize(200,200); 
		pistol.setLocation(0,0); 
		pistol.addMouseListener(this); 
		
		rocket.setSize(200,200); 
		rocket.setLocation(0,0); 
		rocket.addMouseListener(this); 
		
		grenade.setSize(200,200); 
		grenade.setLocation(0,0); 
		grenade.addMouseListener(this); 

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
