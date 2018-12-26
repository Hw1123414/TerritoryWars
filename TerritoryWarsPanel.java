import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TerritoryWarsPanel implements ActionListener{
	// Variables
	String strName;
	boolean blnHost=true;
	
	// Properties
	JFrame frame = new JFrame("Territory Wars");
	JPanel panel = new JPanel();
	JTextArea area = new JTextArea();
	JScrollPane scroll = new JScrollPane(area);
	JTextField field = new JTextField();
	JTextField inputIP = new JTextField();
	SuperSocketMaster ssm;
	JButton host = new JButton("Host Server");
	JButton client = new JButton("Join Server");
	JLabel hostIP = new JLabel("host");
	JButton enterbutton = new JButton("Enter");
	JLabel entername = new JLabel("Your Name:");
	JTextField namefield = new JTextField();
	JLabel enterhostIP = new JLabel("Host IP:");
	
	// Methods
	public void actionPerformed(ActionEvent evt){
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
			
			// Enter Button
			enterbutton.setSize(100,50);
			enterbutton.setLocation(570, 600);
			enterbutton.addActionListener(this);
			panel.add(enterbutton);
		}
		
		// Enter button pressed
		if(evt.getSource()==enterbutton){
			strName = namefield.getText();
			if(blnHost){
				// Show IP
				hostIP.setSize(400,50);
				hostIP.setLocation(500,500);
				panel.add(hostIP);
				ssm = new SuperSocketMaster(6112, this);
				ssm.connect();
				hostIP.setText("Started server, your IP address is: "+ssm.getMyAddress());
				System.out.println(ssm.getMyHostname());
			}else{
				ssm = new SuperSocketMaster(inputIP.getText(),6112,this);
				ssm.connect();
			}
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
	
	// Constructors
	public TerritoryWarsPanel(){
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
		
		scroll.setSize(400,400);
		scroll.setLocation(0,0);
		panel.add(scroll);
		
		field.setSize(400,100);
		field.setLocation(0,400);
		field.addActionListener(this);
		panel.add(field);

		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
		
		
	}
	
	// Main Method
	public static void main(String[] args){
		new TerritoryWarsPanel();
	}
}
