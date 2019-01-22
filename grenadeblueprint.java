import java.io.*;

// Blueprint for grenade
public class grenadeblueprint{

	// Properties
	public int intWidth;
	public int intHeight;
	public int intSpeed;
	public int intDamage;
	
	// Methods
	
	/** Get grenade width */
	public int getWidth(){
		return this.intWidth;
	}
	
	/** Get grenade height */
	public int getHeight(){
		return this.intHeight;
	}
	
	/** Get grenade speed */
	public int getSpeed(){
		return this.intSpeed;
	}
	
	/** Get grenade damage */
	public int getDamage(){
		return this.intDamage;
	}
	
	/** Construct grenade with integer values of width, height, speed, and damage */
	public grenadeblueprint(int intWidth, int intHeight, int intSpeed, int intDamage){
		
		// Variables for reading
		FileReader grenade = null;
		BufferedReader grenadedata = null;
		
		// Grenade (file order: width, height, speed, damage)
		try{
			grenade = new FileReader("grenade.txt");
		}catch(FileNotFoundException e){
			System.out.println("Error! Could not find grenade file.");
		}
		
		grenadedata = new BufferedReader(grenade);
		
		// Read file
		try{
			this.intWidth = Integer.parseInt(grenadedata.readLine());
			this.intHeight = Integer.parseInt(grenadedata.readLine());
			this.intSpeed = Integer.parseInt(grenadedata.readLine());
			this.intDamage = Integer.parseInt(grenadedata.readLine());
		}catch(IOException e){
			System.out.println("Unable to read from grenade file");
		}catch(NumberFormatException e){ //Set default values if a number is missing
			System.out.println("Unable to read from grenade file, setting default values");
			this.intWidth = 15;
			this.intHeight = 15;
			this.intSpeed = 10;
			this.intDamage = 20;
		}
		
		//Close grenade file after reading
		try{
			grenade.close();
		}catch(IOException e){
			System.out.println("Unable to close grenade file");
		}
	}
}
