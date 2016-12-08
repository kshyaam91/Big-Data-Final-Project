
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Gen_Array_After_Commit {
	public static void main(String args[])
	{
		 ArrayList<Double> EnergyArray = new ArrayList<Double>();
		 ArrayList<Integer> StartArray = new ArrayList<Integer>();
		 ArrayList<Integer> EndArray = new ArrayList<Integer>();
		try{
			 String filepath="/home/deekshith/tmp/newtimestamp2.txt";
			 File f=new File(filepath);
				if(!f.exists())
				{
					System.out.print("File sdoesn't exist in the specified path. Please change the filepath to where the db exists");
					System.exit(0);
				}
				
				FileInputStream fis = new FileInputStream(filepath); // Read the   bike database file
				BufferedReader read = new BufferedReader(new InputStreamReader(fis)); 

				String line = null;

				while ((line = read.readLine()) != null) {
					String[] tokens = line.split(",");
			 double Energy=Double.parseDouble(tokens[0]);
			 String[] S_Time=tokens[1].split(":");
			 int StartTime=Integer.parseInt(S_Time[0]);
			 String[] E_Time=tokens[2].split(":");
			 int EndTime=Integer.parseInt(E_Time[0]);
			 
			 EnergyArray.add(Energy);
			 StartArray.add(StartTime);
			 EndArray.add(EndTime);
			 
				}
			 read.close();
				}catch (Exception ex) {
						ex.printStackTrace();
						}
		Gen_Array_Of_DayLoads objDayLoads=new Gen_Array_Of_DayLoads();
		 ArrayList<Double> PreviousPower=objDayLoads.Get_Load();
		 Update_Power_Text objUpdate=new Update_Power_Text();
		 objUpdate.update_Power(PreviousPower, EnergyArray, StartArray, EndArray);
		 
}
}