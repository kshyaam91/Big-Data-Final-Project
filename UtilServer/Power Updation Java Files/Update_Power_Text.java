

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class Update_Power_Text {
public void update_Power(ArrayList<Double> PreviousPower,ArrayList<Double> EnergyArray, ArrayList<Integer> StartArray,ArrayList<Integer> EndArray){
	//for(int i=0;i<PreviousPower.size();i++){System.out.println(i+" "+PreviousPower.get(i));}
	for(int i=0;i<EnergyArray.size();i++)
	{
		int j=0;
		int k=StartArray.get(i);
		while(j==0)
		{
			if(k==(EndArray.get(i)))
			{
				j=1;
			}
			
			double oldPower=PreviousPower.get(k);
			double AddPower=EnergyArray.get(i);
			double NewPower=oldPower+AddPower;
			PreviousPower.set(k,NewPower);
			k++;
		}
		
	}
	//for(int i=0;i<PreviousPower.size();i++){System.out.println(i+" "+PreviousPower.get(i));}
	
	try{
		  
	    File fil = new File("/home/deekshith/Desktop/Randatabase.txt");
	    
			// if file doesnt exists, then create it
			if (!fil.exists()) {
				fil.createNewFile();
			}
			
			  FileWriter fw = new FileWriter(fil.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				int i=1;
				while(i<25)
				{
					bw.write(i+" "+PreviousPower.get(i-1));
					bw.newLine();
					i++;
				}
			bw.close();	
			System.out.println("Data updated");
	}catch(IOException e) {
		e.printStackTrace();}
}
}