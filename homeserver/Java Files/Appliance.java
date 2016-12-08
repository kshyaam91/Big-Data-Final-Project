

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.hadoop.io.Text;

	public class Appliance {
	    String start_time_in_fmt;
	    String end_time_in_fmt;
	    String sche_start_time_in_fmt;
	    String sche_end_time_in_fmt;
	    String user;
	    double start_time;
	    double end_time;
	    double operation_time;
	    double app_energy;
	    double cost_hr;
	    String constraint;
	    String name;
	    int sr_no;
	    int hour;
	    double cost;
	    
	    double sche_start;
    	double sche_end;
    	double appliance_cost  ;
    	ArrayList<Appliance> Det=new ArrayList<>();
	    ArrayList<Appliance> dynamic_time = new ArrayList<>();
	    ArrayList<Appliance> day_ahead_cost= new ArrayList<Appliance>() ; 
	    
	    public int get_sr_no(){
	    	return this.sr_no;
	    }
	    
	    public double get_energy(){
	    	return this.app_energy;
	    }
	   	
	    public double get_start_time(){
	    	return this.start_time;
	    }
	   	
	    public double get_end_time(){
	    	return this.end_time;
	    }
	   	
	    
	    public double get_cost_hr(){
	    	return this.cost_hr;
	    }
	    
	    public String get_name(){
	    	return this.name;
	    }
	    
	    public String get_constraint(){
	    	return this.constraint;
	    }
	    
	    public double get_opt_time(){
	        return this.operation_time;
	    }
	 
	    public String get_sche_start_in_fmt(){
	    	return this.sche_start_time_in_fmt;
	    }
	    
	    public String get_sche_end_in_fmt(){
		    return this.sche_end_time_in_fmt;
		}
	    
	    public double get_app_cost(){
		    return this.appliance_cost;
		}
	    
	    public int get_hour(){
	    	return this.hour;
	    }
	   
	    public void set_sche_start(double time){
	    	this.sche_start=time;
	    	double decimal=time%1;
			int intVal=(int)(time-(time%1));
			int decimal1=(int)(decimal*60);
			//this.sche_start_time_in_fmt=intVal+":"+decimal1;
			if(decimal1<10){
				this.sche_start_time_in_fmt=intVal+":0"+decimal1;
				}
				else
				{
					this.sche_start_time_in_fmt=intVal+":"+decimal1;
				}
			}
	    
	    public void set_sche_end(double time, double opt){
	    	//this.sche_end=time;
	    	//double temp_oper = (this.operation_time/60);
	    	double temp_oper = (opt/60.00);
			double dec = (time+temp_oper);
			double decimal=dec%1;
			int intVal=(int)(dec-(dec%1));
			int decimal1=(int) (decimal*60);
			if(decimal1<10){
			this.sche_end_time_in_fmt=intVal+":0"+decimal1;
			}
			else
			{
				this.sche_end_time_in_fmt=intVal+":"+decimal1;
			}
	   }
	   
	    public void set_app_cost(double app){
	    	
	    	this.appliance_cost=app;
	    	
	    }
	   
	    public String get_start_in_fmt(){
	    	double decimal=start_time%1;
			int intVal=(int) (start_time-(start_time%1));
			int decimal1=(int) (decimal*60);
			//this.start_time_in_fmt=intVal+":"+decimal1;
		
			if(decimal1<10){
				this.start_time_in_fmt=intVal+":0"+decimal1;
				}
				else
				{
					this.start_time_in_fmt=intVal+":"+decimal1;
				}
			return this.start_time_in_fmt;
	    
	    }
	   
	    public String get_end_in_fmt(){
	    	double decimal=this.end_time%1;
			int intVal=(int) (this.end_time-(this.end_time%1));
			int decimal1=(int) (decimal*60);
			//this.end_time_in_fmt=intVal+":"+decimal1;
			if(decimal1<10){
				this.end_time_in_fmt=intVal+":0"+decimal1;
				}
				else
				{
					this.end_time_in_fmt=intVal+":"+decimal1;
				}
			return this.end_time_in_fmt;	
   
	    }
    
	    public Appliance(){
	    	for (int i = 0; i < ScheduleManager.numberOfAppliance(); i++)
	    	{
	            dynamic_time.add(null);
	        }
	    }	
	    
	    public int getIndex(double timw){
	    	int index =(int)(((timw)/100)*12);
		    return index;  
	    }
	    
	    
	    public double getDayAheadCost(int index){
	    	double ho = day_ahead_cost.get(index).get_cost_hr();
		    return ho;
	    }	
	    	
	    public double[] ReadDayAhead(){
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
 						double [] costArray=new double[25];
 						costArray[0]=0;
 						for(int hr=1;hr<=24;hr++){
 						{		
 							costArray[hr]= Double.parseDouble(cost[hr]); 
 						}
 					}
 						return costArray;
 						}
 						
 					}
 				}catch (IOException e) {
 					e.printStackTrace();
 				}
 			
	    	return null;
	    }   
		public void GenArrayList(){	
			double decimal=start_time%1;
			int intVal=(int) (start_time-(start_time%1));
			int decimal1=(int) (decimal*60);
			this.start_time_in_fmt=intVal+":"+decimal1;			
				double temp_oper = (this.operation_time/60);
				double dec = (this.sche_start+temp_oper);
				decimal=dec%1;
				intVal=(int)(dec-(dec%1));
				decimal1=(int) (decimal*60);
				this.sche_end_time_in_fmt=intVal+":"+decimal1;				
				decimal=this.end_time%1;
				intVal=(int)(this.end_time-(this.end_time%1));
				decimal1=(int)(decimal*60);
				this.end_time_in_fmt=intVal+":"+decimal1;				
				decimal=this.sche_start%1;
				intVal=(int)(this.sche_start-(this.sche_start%1));
				decimal1=(int)(decimal*60);
				this.sche_start_time_in_fmt=intVal+":"+decimal1;			
				Appliance nextLoad= new Appliance(user,sr_no,name,app_energy,operation_time,start_time_in_fmt,end_time_in_fmt,constraint,sche_start_time_in_fmt,sche_end_time_in_fmt,appliance_cost);
				Det.add(nextLoad);	
		}
	    public Appliance(int hr, double cos){
	    	this.hour=hr;
	    	this.cost_hr=cos;
	    }   
	    public static double acceptanceProbability(int energy, int newEnergy, double temperature) {
	        // If the new solution is better, accept it
	        if (newEnergy < energy) {
	            return 1.0;
	        }
	        // If the new solution is worse, calculate an acceptance probability
	        return Math.exp((energy - newEnergy) / temperature);
	    }   
	    public Appliance(String usr,int srn, String na, double en,double oper, String start_tm, String end_tm,String cons,String sc_tim,String sche_en,double ap_cos)
	    {
	    	this.user=usr;
	    	this.sr_no=srn;
	    	this.name=na;
	    	this.app_energy=en;
	    	this.operation_time=oper;
	    	this.start_time_in_fmt=start_tm;
	    	this.end_time_in_fmt=end_tm;
	    	this.sche_start_time_in_fmt=sc_tim;
	    	this.sche_end_time_in_fmt=sche_en;
	    	this.appliance_cost=ap_cos;
	    	this.constraint=cons;
	    }	    
	    public Appliance(String user,int srno, String name2, double energy,
				double oper_time, double start_time2, double end_time2,
				String constraint2) {
			// TODO Auto-generated constructor stub
	    	this.user=user;
	    	this.sr_no=srno;
	    	this.name=name2;
	    	this.app_energy=energy;
	    	this.operation_time=oper_time;
	    	this.start_time=start_time2;
	    	this.end_time=end_time2;
	    	this.constraint=constraint2;
	    }
		public double getCost(double energy,double cos) {
			double tot_cost = (energy/12000000)*cos;  
			return tot_cost;
		}    
	    @Override
	    public String toString(){
	        return this.user+","+this.name+ "," +get_start_in_fmt()+","+get_end_in_fmt()+","+this.operation_time+","+this.constraint+","+this.sche_start_time_in_fmt+","+this.sche_end_time_in_fmt+","+this.appliance_cost;
	    }
	}

