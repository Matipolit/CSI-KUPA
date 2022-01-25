package KUPA;

import CSI.CSI;
import CSI.Sensor.Location;

import java.util.List;
import java.util.Scanner;

public class KUPA implements Runnable{

    private User user;

    private static void locationListPrint(List<Location> locationList){
        for(Location location: locationList){
            System.out.print(locationList.indexOf(location)+1 +". ");
            System.out.print(location.getName());
            System.out.print(" | temperature: " + location.measuresTemperature());
            System.out.print(", humidity: " + location.measuresHumidity());
            System.out.print(", pressure: " + location.measuresPressure() + "\n");
        }
    }

    private static void subscribeToLocation(User user){
        user.stopNotifs();
        System.out.println("Select a location to monitor:");
        locationListPrint(CSI.getAvailableLocations());
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        int selected = Integer.parseInt(scanner.nextLine())-1;
        if(selected<=CSI.getAvailableLocations().size() && selected>-1){
            CSI.subscribeToSensorByLocation(user, CSI.getAvailableLocations().get(selected));
            System.out.println("Selected location " + CSI.getAvailableLocations().get(selected).getName());
            user.resumeNotifs();
        }else{
            System.out.println("Wrong index. Try again.");
            subscribeToLocation(user);
        }

    }

    private static void unsubscribeFromLocation(User user){
        user.stopNotifs();
        System.out.println("Select a location to unsubscribe from:");
        locationListPrint(CSI.getLocationsSubscribedByUser(user));
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        int selected = Integer.parseInt(scanner.nextLine())-1;
        if(selected<=CSI.getLocationsSubscribedByUser(user).size() && selected>-1){
            System.out.println("Unsubscribed from location " + CSI.getLocationsSubscribedByUser(user).get(selected).getName());
            CSI.unsubscribeToSensorByLocation(user, CSI.getLocationsSubscribedByUser(user).get(selected));
            user.resumeNotifs();
        }else{
            System.out.println("Wrong index. Try again.");
            unsubscribeFromLocation(user);
        }
    }

    public static void printHelp(){
        System.out.print("\nKeys:\nh: display this message\nq: stop printing notifications\nr: resume printing notifications\ns: subscribe to a new location\nu: unsubscribe from a location\n");
    }

    public static void main(String[] args){

        User user = new User();
        KUPA kupa = new KUPA();
        kupa.user = user;

        CSI csi = new CSI();
        System.out.println("Welcome to KUPA!\nAdd your first location to monitor.\n\n");
        CSI.initTestLocations();
        csi.run();
        subscribeToLocation(kupa.user);
        kupa.run();
    }


    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            final String input = scanner.nextLine();
            switch (input) {
                case "h" -> {
                    printHelp();
                }
                case "q" -> {
                    user.stopNotifs();
                }
                case "r" -> {
                    user.resumeNotifs();
                }
                case "s" -> {
                    subscribeToLocation(this.user);
                }
                case "u" -> {
                    unsubscribeFromLocation(this.user);
                }
            }
        }

    }

}
