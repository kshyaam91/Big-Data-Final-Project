/*
* Tour.java
* Stores a candidate tour through all cities
*/

//package sa;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Schedule{
	
//Updated
	static Gen_Array_Of_DayLoads DayLoads=new Gen_Array_Of_DayLoads();
	static ArrayList<Double> Load=DayLoads.Get_Load();
//Updated
    // Holds our tour of cities
    private ArrayList schedule = new ArrayList<Appliance>();
    private ArrayList day_ahead_cost=new ArrayList<Appliance>();
    // Cache
    private double cost = 0;
    private double Currentcost;
    // Constructs a blank tour
    public Schedule(){
        for (int i = 0; i < ScheduleManager.numberOfAppliance(); i++) {
            schedule.add(null);
        }
    }
    public void ReadDayAhead(){
			String csvFile = "/home/hduser/Downloads/20141108-da.csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "//s";
		//	double[] day_ahead = null;
			 
			//ArrayList<Appliance> cost_hr = new ArrayList<>();	
				try {
					br = new BufferedReader(new FileReader(csvFile));
				} 
				catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					
					for(int i=0;i<6 & ((line = br.readLine()) != null);i++) {
						
						// use comma as separator
						String[] co = line.split(cvsSplitBy);
						//System.out.println((hr[0]));
						if(i==3){
						String[] cost = co[0].split(",");
						for(int hr=1;hr<=24;hr++){
							for(int resolution = 1; resolution<=12;resolution++)
						{
					//		System.out.println(hr * 100+" "+cost[hr]);
							day_ahead_cost.add(new Appliance(hr*100,Double.parseDouble(cost[hr])));
						}
						}
						//for (Appliance a: day_ahead_cost){
							//System.out.println(a);
						//}
						}
						//System.out.println(cost[0]);
//						if (cost[0].contains("Start of Day Ahead Energy Price Data"))
//						for(int i=1;i<24;i++)
//							day_ahead[i]= Double.parseDouble(cost[i]);
					}
				}
				
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	System.out.println(day_ahead);
    	}
    

    
    // Constructs a tour from another tour
    public Schedule(ArrayList schedule){
        this.schedule = (ArrayList) schedule.clone();
    }
    
    // Returns tour information
    public ArrayList getSchedule(){
        return schedule;
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int appIndex = 0; appIndex < ScheduleManager.numberOfAppliance(); appIndex++) {
          setAppliance(appIndex, ScheduleManager.getAppliance(appIndex));
        }
        // Randomly reorder the tour
      //  Collections.shuffle(schedule);
    }

    // Gets a city from the tour
    public Appliance getAppliance(int SchedulePosition) {
        return (Appliance)schedule.get(SchedulePosition);
    }

    // Sets a city in a certain position within a tour
    public void setAppliance(int SchedulePosition, Appliance appliance) {
        schedule.set(SchedulePosition, appliance);
        // If the tours been altered we need to reset the fitness and distance
        cost= 0;
    }
    
    // Gets the total distance of the tour
    public double getCost()
    {
    	double soft_total=0, hard_total=0;
    	
        if (cost == 0)
        {
            double ScheduleCost = 0;
            double energy=0.00;
            double opt_time=0.00;
            double RandomStartTime=0.00;
            // Loop through our tour's cities
            for (int appIndex=0; appIndex < scheduleSize(); appIndex++)
            {
             			double sche_end_time,start_time,end_time;
             			int start_index,sche_end_index,opt_index,end_index=287;
            
             			int sr_no=((Appliance) schedule.get(appIndex)).get_sr_no();
             			String name=((Appliance) schedule.get(appIndex)).get_name();
             			String constraint=((Appliance) schedule.get(appIndex)).get_constraint();
             			opt_time=((Appliance) schedule.get(appIndex)).get_opt_time();
             			start_time=((Appliance) schedule.get(appIndex)).get_start_time(); 
             			end_time=((Appliance) schedule.get(appIndex)).get_end_time();
             			energy=((Appliance) schedule.get(appIndex)).get_energy();
             			if(constraint.toUpperCase().equals("SOFT"))
             			{
             				end_time=1439.00/60.00;
             			}
          	    	 //Randomly picks start time between given schedule time of the load
          			 RandomStartTime=pickRandomTimeForEachJob(opt_time,start_time,end_time);
          			 ((Appliance) schedule.get(appIndex)).set_sche_start(RandomStartTime);
          			 ((Appliance) schedule.get(appIndex)).set_sche_end(RandomStartTime,opt_time);
          			  Currentcost=getCost(energy,RandomStartTime,opt_time);
          			  //updated
          			  Currentcost=CheckIfExceedingLoadAndCalcCost(energy,opt_time,RandomStartTime,Currentcost);
          			  //updated
          			  ScheduleCost=Currentcost;
          			((Appliance) schedule.get(appIndex)).set_app_cost(ScheduleCost);
            }
            	
            for (int appIndex=0; appIndex < scheduleSize(); appIndex++)
            {
            	cost+=((Appliance) schedule.get(appIndex)).get_app_cost();
            }
           //	cost=ScheduleCost;
            	System.out.println("totalcost= "+cost);
            }
        return cost; 
    }
     
    //updated
    public static double CheckIfExceedingLoadAndCalcCost(double energy,double OperatingTime, double proposedStartTime, double OldCost)
	{
		double Peak=35500.00;
		
		double cost=OldCost;
		double NewCost=0.00;
		double intValofStartTime=proposedStartTime-(proposedStartTime % 1);
		double ScheduleTimeLeft=OperatingTime/60.00;
		double SpotCost=30.00;
		
		int timetoCalcCost=(int)(intValofStartTime);
		double NextHour=intValofStartTime+1.00;
		
		while(ScheduleTimeLeft>0)
		{
			int index=0;
			if(timetoCalcCost==0)
			{
				index=23;
			}
			else{index=timetoCalcCost;}
			if(Peak<(Load.get(index)+energy)){
			NewCost=energy*(NextHour-intValofStartTime)*SpotCost/(1000000); //calculating for an hour
			}
			cost+=NewCost;
			NewCost=0.00;
			ScheduleTimeLeft=ScheduleTimeLeft-(NextHour-intValofStartTime);
			intValofStartTime=NextHour;
			NextHour=NextHour+1.00;
			timetoCalcCost=timetoCalcCost+1;
		}
		return cost;
	}
    //Updated
    
	  private double pickRandomTimeForEachJob(double a, double StartTime, double EndTime)
	  {	
		  double RandomStartTime=-1.00;	
		  double ScheduleTime=a/60.00;


		  if(ScheduleTime<=EndTime-StartTime)
		  {
			  double RanMaxTime=EndTime-ScheduleTime;
			
			  if(!(RanMaxTime==StartTime))
			 {
				RandomStartTime=returnRandomMin(StartTime,RanMaxTime); 
			 }
			 else{
				 RandomStartTime=StartTime;
			 }
		  }
		  return RandomStartTime;
	  }

	  private double returnRandomMin(double a, double b)
	  {
		  ArrayList<Double> timeInterval=new ArrayList<Double>();
		  double j=a;
		  while(j<b)
		  {
			  timeInterval.add(j);
			  j=j+(5.00/60.00);
		  }
		  Random random = new Random();
		  int index = random.nextInt(timeInterval.size());
		  double randomNum=(double) timeInterval.get(index);
		  return randomNum;		
	  }
	  Appliance s=new Appliance();
	  double[] costbyhour=s.ReadDayAhead();
	
	  private double getCost( double energy, double randomStartTime, double ScheduleTime)
		{
			
			double cost=0.00;
			double intValofRandomTime= randomStartTime-(randomStartTime % 1);
			double ScheduleTimeLeft=ScheduleTime/60.00;
			int timetoCalcCost=(int)(intValofRandomTime);
			//System.out.println(intValofRandomTime);
			double NextHour=intValofRandomTime+1.00;
			
			if((randomStartTime+ScheduleTimeLeft)<NextHour)
			{			
				cost=energy*ScheduleTimeLeft*costbyhour[timetoCalcCost]/(1200000.00);
			}
			
			else
			{
				while(ScheduleTimeLeft>0)
				{
					cost=energy*(NextHour-intValofRandomTime)*costbyhour[timetoCalcCost]/(1200000.00);
					ScheduleTimeLeft=ScheduleTimeLeft-(NextHour-intValofRandomTime);
					intValofRandomTime=NextHour;
					NextHour=NextHour+1.00;
					timetoCalcCost=timetoCalcCost+1;
				}
			}
			 return cost;
			
		}

    // Get number of cities on our tour
    public int scheduleSize() {
        return schedule.size();
    }
    
    @Override
    public String toString() {
       String geneString="";
        for (int i = 0; i < scheduleSize(); i++) {
           geneString += getAppliance(i)+"\n";
        }
        return geneString;
    }
}