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
import java.util.Random;

public class Schedule{

    
    private ArrayList<Appliance> schedule = new ArrayList<Appliance>();
    private ArrayList<Appliance> day_ahead_cost=new ArrayList<Appliance>();
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
			String csvFile = "/home/deekshith/tmp/20141108-da.csv";
			BufferedReader br = null;
			String line = "";
			String cvsSplitBy = "//s";
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
					
							day_ahead_cost.add(new Appliance(hr*100,Double.parseDouble(cost[hr])));
						}
						}
						
						}
						
					}
				}
				
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	System.out.println(day_ahead);
    	}
    

    
    // Constructs a tour from another tour
    public Schedule(ArrayList<Appliance> schedule){
        this.schedule = (ArrayList<Appliance>) schedule.clone();
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
            // Loop through our tour's cities
            for (int appIndex=0; appIndex < scheduleSize(); appIndex++)
            {
             			double sche_end_time,start_time,end_time,opt_time;
             			int start_index,sche_end_index,opt_index,end_index=287;
            
             			int sr_no=((Appliance) schedule.get(appIndex)).get_sr_no();
             			String name=((Appliance) schedule.get(appIndex)).get_name();
             			String constraint=((Appliance) schedule.get(appIndex)).get_constraint();
             			opt_time=((Appliance) schedule.get(appIndex)).get_opt_time();
             			start_time=((Appliance) schedule.get(appIndex)).get_start_time(); 
             			end_time=((Appliance) schedule.get(appIndex)).get_end_time();
             			double energy=((Appliance) schedule.get(appIndex)).get_energy();
             			if(constraint.toUpperCase().equals("SOFT"))
             			{
             				end_time=1439.00/60.00;
             			}
          	    	 //Randomly picks start time between given schedule time of the load
          			 double RandomStartTime=pickRandomTimeForEachJob(opt_time,start_time,end_time);
          			 ((Appliance) schedule.get(appIndex)).set_sche_start(RandomStartTime);
          			 ((Appliance) schedule.get(appIndex)).set_sche_end(RandomStartTime,opt_time);
          			  Currentcost=getCost(energy,RandomStartTime,opt_time);
          		//	Currentcost=CheckIfExceedingLoadAndCalcCost(energy,opt_time,RandomStartTime,Currentcost);
          			  ScheduleCost=Currentcost ;
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
				cost=energy*ScheduleTimeLeft*costbyhour[timetoCalcCost]/(12000000.00);
			}
			
			else
			{
				while(ScheduleTimeLeft>0)
				{
					cost=energy*(NextHour-intValofRandomTime)*costbyhour[timetoCalcCost]/(12000000.00);
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
    public void clearSchedule(){
    	this.schedule.clear();
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