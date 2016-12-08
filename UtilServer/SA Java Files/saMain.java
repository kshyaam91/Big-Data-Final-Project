

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
//import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapred.jobcontrol.Job;

//import com.sun.xml.internal.bind.CycleRecoverable.Context;
 	
 	public class saMain {
// 		String[] cost;
 		//---------------------------------------Mapper of Simulated Annealing-------------------------------------------------------//	
 	   public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
 		 //private final static IntWritable one = new IntWritable(1);
 	     private Text user = new Text();
 	     int i=100;
 	    public void map(LongWritable key, Text value, OutputCollector<Text, Text > output, Reporter reporter) throws IOException {
 	       String[] words = value.toString().split(",");
 	        int srno;
 	        String name;
 	        String constraint;
 	        double energy;
 	        double oper_time;
 	        String start;
 	        String end;
 	        String Schestart;
	        String Scheend;
	      
 	        // int start_time;
 	       // int end_time;
 	        double sche_start;
 	        double sche_end;
 	        double appliance_cost;
 	        //if(!(words[0].contains("user")|words[0].trim().isEmpty()))
 	       //{
 	    	  
 	          srno= Integer.parseInt(words[1]);
 	    	  name= words[2];
 	    	  energy=Double.parseDouble(words[3]);
 	    	  oper_time=Double.parseDouble(words[5]);
 	    	  start=(words[6]);
  	          end=(words[7]);
 	    	  constraint=words[9];
 	    	  Schestart=words[11];
 	    	  Scheend=words[12];
 	    	  appliance_cost=Double.parseDouble(words[13]);
 	    	  System.out.println(start);
 	    	  String[] time_splits=start.split(":");
 	    	  double StartTime=Integer.parseInt(time_splits[0])+(Integer.parseInt(time_splits[1]))/60;
 	    	  String[] end_time_splits=end.split(":");
	    	  double EndTime=Integer.parseInt(end_time_splits[0])+(Integer.parseInt(end_time_splits[1]))/60;
	    	  System.out.println("Start:"+StartTime+"end:"+EndTime);
	    	  String[] Sche_time_splits=start.split(":");
 	    	  double ScheStartTime=Integer.parseInt(Sche_time_splits[0])+(Integer.parseInt(Sche_time_splits[1]))/60;
 	    	  String[] Sche_end_time_splits=start.split(":");
	    	  double ScheEndTime=Integer.parseInt(Sche_end_time_splits[0])+(Integer.parseInt(Sche_end_time_splits[1]))/60;
	    	  
	    	  user.set(words[0]);
 	    	  String data = srno +","+name+ ","+ energy+","+ oper_time+","+StartTime+","+EndTime+","+constraint+","+ScheStartTime+","+ScheEndTime+","+appliance_cost;
 	    	 output.collect(new Text("KEY"),new Text(user+","+data));
 	    	 
 	       //}
 	       
 	     }
 	   }
 	   
 	   
//---------------------------------------Reducer of Simulated Annealing-------------------------------------------------------//	
 	  
 	   
 	   
 	   public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {  
 		  double cost, total; ;int new_time,best_time,start_index;
 			Appliance s=new Appliance();
 			double[] costbyhour1=s.ReadDayAhead();
 			String sche_start_in_fmt;
	        String sche_end_in_fmt;
	        //double appliance_cost;
	
 		  public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException  
 	   {
 	 		
 			String user;
 			int srno;
  	        String name;
  	        String constraint;
  	        double energy;
  	        double oper_time;
  	        double start_time;
  	        double end_time;
  	        double sche_start;
	        double sche_end;
	        double appliance_cost;
//	
  	        double temp=1000;
  	        double cooling_rate=0.003;
  	         
 			ArrayList <Appliance> Appl_details= new ArrayList<>() ;
 			while(values.hasNext()){
 			
 				  String[] details = values.next().toString().split(",");
 				  user=	details[0];
 				  srno= Integer.parseInt(details[1]);
 	 	    	  name= details[2];
 	 	    	  energy=Double.parseDouble(details[3]);
 	 	    	  oper_time=Double.parseDouble(details[4]);
 	 	    	  start_time=Double.parseDouble(details[5]);
 	  	          end_time=Double.parseDouble(details[6]);
 	 	    	  constraint=details[7];
 	 	    	  sche_start=Double.parseDouble(details[8]);
 	 	    	  sche_end=Double.parseDouble(details[9]);
 	 	    	  appliance_cost=Double.parseDouble(details[10]);
 	 	        
 	 	    	  Appliance app_det = new Appliance(srno,name,energy,oper_time,start_time,end_time,constraint);
 	 	    	  ScheduleManager.addAppliance(app_det);
 			}
 			   	 
			    Schedule current_solution= new Schedule();
			    current_solution.generateIndividual();
			    
			    System.out.println("Initial Cost: " + current_solution.getCost());
			    Schedule best = new Schedule(current_solution.getSchedule());
			     while (temp>1){
			    	 
			    	Schedule newSolution = new Schedule(current_solution.getSchedule());
			    	 double currentEnergy = current_solution.getCost();
			         double neighbourEnergy = newSolution.getCost();
			     //    System.out.println("neighbour solution cost: " + newSolution.getCost());
			         if (acceptanceProbability(currentEnergy, neighbourEnergy, temp) > Math.random()) {
			        	// current_solution.getAppliance(0).get_start_time();
			        	 current_solution = new Schedule(newSolution.getSchedule());
			   //     	 System.out.println("current solution cost: " + current_solution.getCost());
			         }
			         if (current_solution.getCost() < best.getCost()) {
			                best = new Schedule(current_solution.getSchedule());
			  //              System.out.println("best solution cost: " + best.getCost());
			            }
			         	temp *= 0.9;
			     }
			 	System.out.println("Final solution cost: " + best.getCost());
		         System.out.println("Best Schedule: " + best);
		         System.out.println("SCHEDULE SIZE " + best.scheduleSize());
		         // Text var = new Text();
		        // best.clearSchedule();
		         for(int i=0;i<best.scheduleSize();i++){
		         output.collect(null, new Text(best.getAppliance(i).toString()));
		         }
		         
		}
			     
  	       
 	 		
		private static double acceptanceProbability(double current_energy,
				double new_energy, double temp) {
			  if (new_energy < current_energy) return 1.0;
	      //   If the new solution is worse, calculate an acceptance probability
			  else return Math.exp((current_energy - new_energy) / temp);
			
		}
	    	  
 
 	   } 	    	       


 		public static void main(String[] args) throws Exception {
 			
 			 JobConf j = new JobConf(saMain.class); 
 	 		 j.setJobName("Average");
 	 		 
 	 	     j.setJarByClass(saMain.class);
 	 	     j.setMapperClass(Map.class);
 	 	     j.setReducerClass(Reduce.class);
 	 	     
 	 	    
 	 	     j.setOutputKeyClass(Text.class);
 	 	     j.setOutputValueClass(Text.class);
 	 	     
 	 	     j.setInputFormat(TextInputFormat.class);
 	 	     j.setOutputFormat(TextOutputFormat.class);

 	 	     FileInputFormat.setInputPaths(j, new Path("/user/deekshith/tmp/snip-hdfs"));
 	 	     FileOutputFormat.setOutputPath(j, new Path("/user/deekshith/tmp/snip-output")); 
 	 	     JobClient.runJob(j);    
 	 	    
 		}	

 	}
 	 	
 	   
 	