// Territory Wars Game
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;

/** ICS4U1 Final CPT - Territory Wars by James Cahyadi, Raymond Chan, and Holden Wong **/
public class TerritoryWars implements ActionListener, MouseListener, MouseMotionListener, KeyListener{
	//Variables
	String strName;
	String strData;
	String strSplit[] = new String[2];
	boolean blnHost=true;
	boolean blnShoot = false;
	boolean blnEndturn = false; 
	boolean blnChat = false;
	boolean blnOpen=false;

	// Properties
	JFrame frame = new JFrame("Territory Wars");
	TerritoryWarsPanel panel = new TerritoryWarsPanel();
	SuperSocketMaster ssm;
	JTextArea area = new JTextArea();
	JScrollPane scroll = new JScrollPane(area);
	JTextField chatfield = new JTextField();
	JTextField inputIPfield = new JTextField();
	JTextField namefield = new JTextField();
	JLabel hostIPlabel = new JLabel("host");
	JLabel enternamelabel = new JLabel("Your Name:");
	JLabel enterhostIPlabel = new JLabel("Host IP:");
	JLabel waitinglabel = new JLabel("Waiting for host to start game...");
	JButton OKbut = new JButton("OK");
	JButton hostbut = new JButton("Host Server");
	JButton clientbut = new JButton("Join Server");
	JButton sniperbut = new JButton("Sniper"); 
	JButton grenadebut = new JButton("Grenade"); 
	JButton startbut = new JButton("Start Game");
	JButton chatbut = new JButton("Chat");
	JButton playbut = new JButton("Play");
	JButton helpbut = new JButton("Help");
	JButton quitbut = new JButton("Quit");
	JButton backbut = new JButton("Back To Menu");
	JButton stopbut = new JButton("Stop");
	Timer timer;
	
	/** Listens to action events of JComponents **/
	public void actionPerformed(ActionEvent evt){
		if(evt.getSource() == timer){
			panel.repaint();
			if(panel.blnStartGame){
				ssm.sendText("p"+(int)Math.round(panel.dblPlayerX)+","+(int)Math.round(panel.dblPlayerY));
				ssm.sendText("b"+(int)Math.round(panel.dblBulletX)+","+(int)Math.round(panel.dblBulletY));
				ssm.sendText("h"+(int)Math.round(panel.dblOppHealth));
				
				if(panel.blnEnd){
					panel.removeAll();
					panel.add(quitbut);
					panel.requestFocus();
				}	
			}
		}
		
		// Play button clicked
		if(evt.getSource() == playbut){
			panel.remove(playbut);
			panel.remove(quitbut);
			panel.remove(helpbut);
			panel.validate();
			panel.repaint();
			  
			hostbut.setSize(100,50);
			hostbut.setLocation(500,500);
			hostbut.addActionListener(this);
			panel.add(hostbut);
			
			clientbut.setSize(100,50);
			clientbut.setLocation(700,500);
			clientbut.addActionListener(this);
			panel.add(clientbut);
		}
		
		// Help button clicked
		if(evt.getSource() == helpbut){
			panel.removeAll();
			panel.blnHelpMenu = true;
			backbut.setSize(200,100);
			backbut.setLocation(1080,620);
			backbut.addActionListener(this);
			panel.add(backbut);	
		}
		
		// Back buton clicked 
		if(evt.getSource() == backbut){
			panel.removeAll();
			panel.blnHelpMenu = false;
			panel.add(playbut);
			panel.add(helpbut);
			panel.add(quitbut);
			panel.requestFocus();
			panel.validate();
		}
		
		// Quit button clicked
		if(evt.getSource() == quitbut){
			 System.exit(0);
		}
		
		// Host button Clicked
		if(evt.getSource()==hostbut || evt.getSource()==clientbut){
			if(evt.getSource()==clientbut){
				blnHost=false;
				panel.blnHost=false;
				// Show IP input field
				enterhostIPlabel.setSize(300,25);
				enterhostIPlabel.setLocation(420,500);
				panel.add(enterhostIPlabel);
				inputIPfield.setSize(300,25);
				inputIPfield.setLocation(500,500);
				inputIPfield.addActionListener(this);
				panel.add(inputIPfield);
			}
			
			// Remove Buttons
			panel.remove(hostbut);
			panel.remove(clientbut);
			
			// Ask for name
			enternamelabel.setSize(100,25);
			enternamelabel.setLocation(410,550);
			panel.add(enternamelabel);
			namefield.setSize(300,25);
			namefield.setLocation(500,550);
			namefield.addActionListener(this);
			panel.add(namefield);
			
			// OK Button
			OKbut.setSize(100,50);
			OKbut.setLocation(580, 600);
			OKbut.addActionListener(this);
			panel.add(OKbut);
			panel.requestFocus();
		}
		
		// 'OK' button pressed
		if(evt.getSource()==OKbut){
			panel.remove(OKbut);
			panel.remove(namefield);
			panel.remove(enternamelabel);
			// Get username
			strName = namefield.getText();
			// Host
			if(blnHost){
				// Show IP
				hostIPlabel.setSize(400,50);
				hostIPlabel.setLocation(500,500);
				panel.add(hostIPlabel);
				ssm = new SuperSocketMaster(6112, this);
				ssm.connect();
				hostIPlabel.setText("Started server, your IP address is: "+ssm.getMyAddress());
				// Show 'Start Game' button
				startbut.setSize(100,50);
				startbut.setLocation(580,600);
				startbut.addActionListener(this);
				panel.add(startbut);
				startbut.setEnabled(false); // Host can't start unless client joins
				// Host has the first turn
				panel.blnTurn = true;
				
			// Client
			}else{
				ssm = new SuperSocketMaster(inputIPfield.getText(),6112,this);
				ssm.connect();
				panel.remove(enterhostIPlabel);
				panel.remove(inputIPfield);
				waitinglabel.setSize(500,50);
				waitinglabel.setLocation(520,400);
				panel.add(waitinglabel);
				// Tell the host that the client has connected
				ssm.sendText("start");
				// Client doesn't have first turn
				panel.blnTurn = false;
			}
		}
		// Host Clicks start button
		if(evt.getSource()==startbut){
			panel.remove(hostIPlabel);
			panel.remove(startbut);
			panel.remove(inputIPfield);
			panel.remove(enterhostIPlabel);
			panel.validate();
			panel.repaint();
			panel.blnStartGame=true;
			// Tells client that game has been started
			ssm.sendText("s");
			
			// Chat button
			chatbut.setLocation(1200,680); 
			chatbut.setSize(80,40); 
			chatbut.addActionListener(this);
			panel.add(chatbut);
			
			// Stop button
			stopbut.setSize(100, 50);
			stopbut.setLocation(600,100);
			stopbut.addActionListener(this);
			panel.add(stopbut);
			
			// Sniper button
			sniperbut.setSize(100, 50);
			sniperbut.setLocation(540, 25);
			sniperbut.addActionListener(this);
			panel.add(sniperbut);	
			sniperbut.setEnabled(false);
			
			// Grenade button
			grenadebut.setSize(100, 50);
			grenadebut.setLocation(640, 25);
			grenadebut.addActionListener(this);
			panel.add(grenadebut);
			grenadebut.setEnabled(false);
		}
		
		// "Chat" button
		if(evt.getSource()==chatbut){
			if(blnChat==false){ 
				blnChat=true; 
				scroll.setSize(400,400);
				scroll.setLocation(0,0);
				panel.add(scroll);
				area.setEnabled(false);

				chatfield.setSize(400,100);
				chatfield.setLocation(0,400);
				chatfield.addActionListener(this);
				panel.add(chatfield);
				chatfield.grabFocus();
			}else{
				blnChat=false;
				panel.remove(scroll); 
				panel.remove(chatfield); 
				panel.requestFocus(); 
				panel.validate();
				panel.repaint();
			}
		}
		
		//Send Chat
		if(evt.getSource()==chatfield){
			ssm.sendText("c"+strName+": "+chatfield.getText());
			area.append(strName+": "+chatfield.getText()+"\n");
			chatfield.setText("");
		}
		
		// Receive Data
		// First character of string is used to determine type of data
		if(evt.getSource()==ssm){
			strData=ssm.readText();
			//Enable the start button when client has connected
			if(strData.equals("start")){
				startbut.setEnabled(true);
			// Chat data
			}else if(strData.substring(0,1).equals("c")){
				area.append(strData.substring(1,strData.length())+"\n");
			// Position data
			}else if(strData.substring(0,1).equals("p")){
				strData=strData.substring(1,strData.length());
				this.strSplit=strData.split(",");
				panel.intOppX=Integer.parseInt(strSplit[0]);
				panel.intOppY=Integer.parseInt(strSplit[1]);
			// Bullet Position data
			}else if(strData.substring(0,1).equals("b")){
				strData=strData.substring(1,strData.length());
				this.strSplit=strData.split(",");
				panel.intOppBulletX=Integer.parseInt(strSplit[0]);
				panel.intOppBulletY=Integer.parseInt(strSplit[1]);
			// Health Bar data
			}else if(strData.substring(0,1).equals("h")){
				strData=strData.substring(1,strData.length());
				panel.dblHealth=Double.parseDouble(strData);
			// Game Start data
			}else if(strData.equals("s") && blnHost==false){
				panel.blnStartGame=true;
				panel.remove(waitinglabel);
				
				// Chat button
				chatbut.setLocation(1200,680); 
				chatbut.setSize(80,40); 
				chatbut.addActionListener(this);
				panel.add(chatbut);
				
				// Stop button
				stopbut.setSize(100, 50);
				stopbut.setLocation(600,100);
				stopbut.addActionListener(this);
				panel.add(stopbut);
				stopbut.setEnabled(false);
						
			 	// Sniper button
				sniperbut.setSize(100, 50);
				sniperbut.setLocation(540, 25);
				sniperbut.addActionListener(this);
				panel.add(sniperbut);	
				sniperbut.setEnabled(false);
				
				// Grenade button
				grenadebut.setSize(100, 50);
				grenadebut.setLocation(640, 25);
				grenadebut.addActionListener(this);
				panel.add(grenadebut);
				grenadebut.setEnabled(false);
				
				panel.validate();
				panel.repaint();
				
			// For switching turns
			}else if(strData.equals("switch")){
				if(panel.blnTurn){
					panel.blnTurn = false;
					stopbut.setEnabled(false);
				}else{
					panel.blnTurn = true;
					stopbut.setEnabled(true);
				}
			}
		}

		// Sniper button
		if(evt.getSource() == sniperbut){
			panel.blnSniper = true;
			panel.blnGrenade = false;
			grenadebut.setEnabled(false);
			stopbut.setEnabled(false);
			// Transparent 16 x 16 pixel cursor image.
			BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point(0, 0), "blank cursor");
			// Set the blank cursor
			frame.getContentPane().setCursor(blankCursor);
			panel.requestFocus();
			
		// Grenade button
		}else if(evt.getSource() == grenadebut){
			panel.blnGrenade = true;
			panel.blnSniper = false;
			sniperbut.setEnabled(false);
			stopbut.setEnabled(false);
			panel.requestFocus();
		}	
		
		// Hit stop button first time, stop moving
		if(evt.getSource() == stopbut && blnEndturn == false){
			panel.blnPlayerLeft = false;
			panel.blnPlayerRight = false;
			grenadebut.setEnabled(true);
			sniperbut.setEnabled(true);
			blnEndturn = true;
			stopbut.setText("End turn");
			panel.requestFocus();
		}
		
		// If stop button is pressed again, player switch turns
		else if(evt.getSource() == stopbut && blnEndturn){
			panel.dblOrigin = panel.dblPlayerX;
			panel.intDisplacement = 0;
			panel.requestFocus(); 
			panel.blnSniper = false;
			panel.blnGrenade = false;
			blnEndturn = false;
			sniperbut.setEnabled(false);
			grenadebut.setEnabled(false);
			stopbut.setText("Stop");
			panel.requestFocus();
			ssm.sendText("switch");
			if(panel.blnTurn){
					panel.blnTurn = false;
					stopbut.setEnabled(false);
				}else{
					panel.blnTurn = true;
					stopbut.setEnabled(true);
				}
		}	
		
		// Switch turns if player shoots and the bullet disappears
		if(blnShoot){
			if(panel.blnBulletDisappear){
				panel.dblOrigin = panel.dblPlayerX;
				panel.intDisplacement = 0;
				panel.requestFocus(); 
				sniperbut.setEnabled(false); 
				grenadebut.setEnabled(false);  
				stopbut.setEnabled(true);
				blnShoot = false;
				blnEndturn = false;
				panel.blnSniper = false;
				panel.blnGrenade = false;
				stopbut.setText("Stop");
				ssm.sendText("switch");
				panel.setCursor(Cursor.getDefaultCursor());
				if(panel.blnTurn){
					panel.blnTurn = false;
					stopbut.setEnabled(false);
				}else{
					panel.blnTurn = true;
					stopbut.setEnabled(true);
				}
			}
		}
		
	}
		
	/** Empty method, overrides mouseExited method in MouseListener */
	public void mouseExited(MouseEvent evt){}
	/** Empty method, overrides mouseEntered method in MouseListener */
	public void mouseEntered(MouseEvent evt){}
	/** Empty method, overrides mouseReleased method in MouseListener */
	public void mouseReleased(MouseEvent evt){}
	
	/** Listens to mouse presses
	 *  Only shoots bullet if it is your turn and you have not shot already*/
	public void mousePressed(MouseEvent evt){
		if(panel.blnTurn){
			if(panel.blnBulletDisappear){
				if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720  && (panel.blnGrenade || panel.blnSniper)){
					panel.dblBulletX=panel.dblPlayerX;
					panel.dblBulletY=panel.dblPlayerY;
					panel.dblMouseX=evt.getX();
					panel.dblMouseY=evt.getY();
					panel.blnFire=true;
					panel.blnGetSlope=true;
					panel.blnBulletDisappear = false;
					panel.requestFocus();
					blnShoot = true;
					sniperbut.setEnabled(false); 
					grenadebut.setEnabled(false);  
					stopbut.setEnabled(false);
				}
			}
		}
	}

	/** When mouse is moved, position of the mouse is sent to the panel*/
	public void mouseMoved(MouseEvent evt){
		panel.dblMouseX=evt.getX();
		panel.dblMouseY=evt.getY();	
	}
	
	/** Empty method, overrides mouseDragged method in MouseMotionListener*/
	public void mouseDragged(MouseEvent evt){}
	/** Empty method, overrides mouseClicked method in MouseMotionListener*/
	public void mouseClicked(MouseEvent evt){}
	
	/** Listens to mouse releases
	 * 	Stops moving left when left arrow is released
	 *  Stops moving right when right arrow is released */
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
	
	/** Listens to key presses
     *  Up to jump, left arrow to move left, right arrow to move right */
	public void keyPressed(KeyEvent evt){
		if(panel.blnTurn){
			if(blnEndturn ==false && panel.blnBulletDisappear && panel.blnSniper == false && panel.blnGrenade == false){
				switch(evt.getKeyCode()){
					case 37:
						if(panel.intDisplacement < 300 || panel.dblOrigin - panel.dblPlayerX < 0){
							panel.blnPlayerLeft=true;
						}
						break;
					case 38: panel.blnJump=true;
							 break;
					case 39: 
						if(panel.intDisplacement < 300 || panel.dblOrigin - panel.dblPlayerX > 0){
							panel.blnPlayerRight=true;
						}
						break;	
				}
			}
		}
	}
	
	/** Empty method, overrides keyTyped method in KeyListener */
	public void keyTyped(KeyEvent evt){}
	
	// Constructor
	public TerritoryWars(){
		
		// Create a 1280 by 720 pixel panel
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));
		
		// Play button
		playbut.setSize(100,50);
		playbut.setLocation(580,350);
		playbut.addActionListener(this);
		panel.add(playbut);
		
		// Help button
		helpbut.setSize(100,50);
		helpbut.setLocation(580,450);
		helpbut.addActionListener(this);
		panel.add(helpbut);
		
		// Quit button
		quitbut.setSize(100,50);
		quitbut.setLocation(580,550);
		quitbut.addActionListener(this);
		panel.add(quitbut);
		
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(1280,720));

		// Add listeners
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.setFocusable(true);
        panel.requestFocus();
		panel.addKeyListener(this);
		
		frame.setResizable(false); // Don't allow the frame to be resizable
		frame.setContentPane(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		timer = new Timer(1000/60, this);
		timer.start(); // Start timer
	}
	
	/** Main method, calls the constructor to create a "TerritoryWars" object */
	public static void main(String[] args){
		new TerritoryWars();
	}
}
