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
	JButton chat = new JButton("Chat");
	boolean blnChat = false;
	String strData;
	Timer timer;
	
	JButton stopbut = new JButton("Stop");
	
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
			panel.remove(enterhostIP);
			panel.blnStartGame=true;
			
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
		}
		
		//Chat
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
			}
			else if(blnChat){ 
				blnChat=false;
				panel.remove(scroll); 
				panel.remove(field); 
				panel.requestFocus(); 
			}
		}

		if(evt.getSource()==field){
			ssm.sendText(strName+": "+field.getText());
			ssm.sendText(panel.dblPlayerX[panel.intPlayer]+"");
			area.append(strName+": "+field.getText()+"\n");
			field.setText("");;
		}
		if(evt.getSource()==ssm){
			strData = ssm.readText();
			area.append(strData+"\n");			
		}

		//If stop button is pressed, switch turns
		if(evt.getSource() == stopbut){
			if(panel.blnPlayerOne){
				panel.blnPlayerOne = false;
				panel.blnPlayerTwo = true;
				panel.dblOrigin[0] = panel.dblPlayerX[0];
			}else if(panel.blnPlayerTwo){
				panel.blnPlayerOne = true;
				panel.blnPlayerTwo = false;
				panel.intDisplacement = 0;
				panel.dblOrigin[1] = panel.dblPlayerX[1];
			}
			panel.intDisplacement = 0;
			panel.requestFocus(); 
		}
		
	}
		
	// MouseListener
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	// When mouse is clicked
	public void mousePressed(MouseEvent evt){
		if(evt.getX()>0 && evt.getX()<1280 && evt.getY()>0 && evt.getY()<720){
			panel.dblBulletX=panel.dblPlayerX[panel.intPlayer];
			panel.dblBulletY=panel.dblPlayerY[panel.intPlayer];

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
			case 38: 
				break;
			case 39: panel.blnPlayerRight=false;
				break;
		}
	
	}
	
	public void keyPressed(KeyEvent evt){
		
		switch(evt.getKeyCode()){
			case 37:
			if(panel.intDisplacement < 300 || panel.dblOrigin[panel.intPlayer] - panel.dblPlayerX[panel.intPlayer] < 0){
				panel.blnPlayerLeft=true;
			}
			break;
			case 38:
				panel.blnPlayerUp=true;
				panel.blnJump=true;
					break;
			case 39: 
			if(panel.intDisplacement < 300 || panel.dblOrigin[panel.intPlayer] - panel.dblPlayerX[panel.intPlayer] > 0){
				panel.blnPlayerRight=true;
			}
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
			field.grabFocus();
			blnChat = true;
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
