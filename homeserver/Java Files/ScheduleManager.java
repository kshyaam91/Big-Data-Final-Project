/*
* TourManager.java
* Holds the cities of a tour
*/


import java.util.ArrayList;

public class ScheduleManager {

    // Holds our cities
    private static ArrayList<Appliance>destinationCities = new ArrayList<>();

    // Adds a destination city
    public static void addAppliance(Appliance appliance) {
        destinationCities.add(appliance);
    }
    
    // Get a city
    public static Appliance getAppliance(int index){
        return (Appliance)destinationCities.get(index);
    }
    
    // Get the number of destination cities
    public static int numberOfAppliance(){
        return destinationCities.size();
    }
    
}