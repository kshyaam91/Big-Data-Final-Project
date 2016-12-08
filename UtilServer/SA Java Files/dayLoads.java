
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Gen_Array_Of_DayLoads {
	public ArrayList<Double> Get_Load()
	{
		 ArrayList<Double> LoadArray = new ArrayList<Double>();
		try{
			 String filepath="/home/deekshith/Downloads/Randatabase.txt";
			 File f=new File(filepath);
				if(!f.exists())
				{
					System.out.print("File doesn't exist in the specified path. Please change the filepath to where the db exists");
					System.exit(0);
				}
				
				FileInputStream fis = new FileInputStream(filepath); // Read the   bike database file
				BufferedReader read = new BufferedReader(new InputStreamReader(fis)); 

				String line = null;

				while ((line = read.readLine()) != null) {
					String[] tokens = line.split(" ");
			 double Load=Double.parseDouble(tokens[1]);
			 LoadArray.add(Load);
				}
			 read.close();
				return LoadArray;
				}catch (Exception ex) {
						ex.printStackTrace();
						}
		return null;
}
}
