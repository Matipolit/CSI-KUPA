package CSI;

import CSI.Sensor.Location;
import CSI.Sensor.Sensor;
import KUPA.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CSI implements Runnable {

    private static List<Sensor> sensors;

    public CSI(){

    }

    private static void initSensorsFromLocations(List<Location> locations){
        sensors = new ArrayList<Sensor>();
        for(Location location: locations){
            sensors.add(new Sensor(location));
        }
    }

    private static List<Location> locationsFromFile(String fileLocation){
        List<Location> locations = new ArrayList<Location>();
        try {
            File file = new File(fileLocation);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String[] splitLine = reader.nextLine().split("/");

                boolean temperature = splitLine[1].contains("T");
                boolean humidity = splitLine[1].contains("H");
                boolean pressure = splitLine[1].contains("P");

                Location location = new Location(splitLine[0], temperature, humidity, pressure);
                locations.add(location);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return locations;
    }

    public static void initTestLocations(){
        initSensorsFromLocations(locationsFromFile("src/locations.txt"));
    }

    public static List<Location> getAvailableLocations(){
        return(sensors.stream().map(Sensor::getLocation).toList());
    }

    public static void subscribeToSensorByLocation(User user, Location location){
        for(Sensor sensor: sensors){
            if(sensor.getLocation().equals(location)){
                sensor.subscribeUser(user);
            }
        }
    }

    public static void unsubscribeToSensorByLocation(User user, Location location){
        for(Sensor sensor: sensors){
            if(sensor.getLocation().equals(location)){
                sensor.unsubscribeUser(user);
            }
        }
    }

    public static List<Location> getLocationsSubscribedByUser(User user){
        return(sensors.stream().filter(sensor -> sensor.isUserSubscribed(user)).map(Sensor::getLocation).toList());
    }

    private void startMeasuring(){

    }

//    public static void main(String[] args){
//        initTestLocations();
//        System.out.println(getAvailableLocations());
//        User user = new User();
//        sensors.get(0).subscribeUser(user);
//        new CSI().run();
//    }

    @Override
    public void run() {
        for(Sensor sensor: sensors){
            sensor.startObservable();
        }
    }
}
