// Blueprint for sniper bullet
import java.io.*;

public class sniperblueprint{

	// Properties
	public int intWidth;
	public int intHeight;
	public int intSpeed;
	public int intDamage;
	
	// Methods
	
	/** Get sniper bullet width */
	public int getWidth(){
		return this.intWidth;
	}
	
	/** Get sniper bullet height */
	public int getHeight(){
		return this.intHeight;
	}
	
	/** Get sniper bullet speed */
	public int getSpeed(){
		return this.intSpeed;
	}
	
	/** Get sniper bullet damage */
	public int getDamage(){
		return this.intDamage;
	}
	
	/** Construct sniper with integer values of width, height, speed, and damage */
	public sniperblueprint(int intWidth, int intHeight, int intSpeed, int intDamage){
		
		// Variables for reading
		FileReader sniper = null;
		BufferedReader sniperdata = null;
		
		// Sniper (file order: width, height, speed, damage)
		try{
			sniper = new FileReader("sniper.txt");
		}catch(FileNotFoundException e){
			System.out.println("Error! Could not find sniper file.");
		}
		
		sniperdata = new BufferedReader(sniper);
		
		// Read file
		try{
			this.intWidth = Integer.parseInt(sniperdata.readLine());
			this.intHeight = Integer.parseInt(sniperdata.readLine());
			this.intSpeed = Integer.parseInt(sniperdata.readLine());
			this.intDamage = Integer.parseInt(sniperdata.readLine());
		}catch(IOException e){
			System.out.println("Unable to read from sniper file");
		}catch(NumberFormatException e){ //Set default values if a number is missing
			System.out.println("Unable to read from sniper file, setting default values");
			this.intWidth = 10;
			this.intHeight = 10;
			this.intSpeed = 15;
			this.intDamage = 10;
		}
		
		//Close sniper file after reading
		try{
			sniper.close();
		}catch(IOException e){
			System.out.println("Unable to close sniper file");
		}
	}
}
