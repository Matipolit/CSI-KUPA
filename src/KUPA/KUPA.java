package KUPA;

import CSI.CSI;
import CSI.Sensor.Location;

import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class KUPA implements Runnable{

    private User user;

    public KUPA(User user){
        this.user = user;
    }

    private void locationListPrint(List<Location> locationList){
        for(Location location: locationList){
            System.out.print(locationList.indexOf(location)+1 +". ");
            System.out.print(location.getName());
            System.out.print(" | temperature: " + location.measuresTemperature());
            System.out.print(", humidity: " + location.measuresHumidity());
            System.out.print(", pressure: " + location.measuresPressure() + "\n");
        }
    }

    private void subscribeToLocation(){
        user.stopNotifs();
        System.out.println("Select a location to monitor:");
        locationListPrint(CSI.getAvailableLocations());
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        try{
            int selected = Integer.parseInt(scanner.nextLine())-1;
            if(selected<=CSI.getAvailableLocations().size() && selected>-1){
                CSI.subscribeToSensorByLocation(user, CSI.getAvailableLocations().get(selected));
                System.out.println("Selected location " + CSI.getAvailableLocations().get(selected).getName());
                user.resumeNotifs();
            }else{
                System.out.println("Wrong index. Try again.");
                subscribeToLocation();
            }
        }catch (NumberFormatException nfe) {
            System.out.println("Index is not a number. Try again.");
            unsubscribeFromLocation();
        }


    }

    private void unsubscribeFromLocation(){
        user.stopNotifs();
        System.out.println("Select a location to unsubscribe from:");
        locationListPrint(CSI.getLocationsSubscribedByUser(user));
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        String selectedString = scanner.nextLine();
        try{
            int selected = Integer.parseInt(scanner.nextLine())-1;
            if(selected<=CSI.getLocationsSubscribedByUser(user).size() && selected>-1){
                System.out.println("Unsubscribed from location " + CSI.getLocationsSubscribedByUser(user).get(selected).getName());
                CSI.unsubscribeToSensorByLocation(user, CSI.getLocationsSubscribedByUser(user).get(selected));
                user.resumeNotifs();
            }else{
                System.out.println("Wrong index. Try again.");
                unsubscribeFromLocation();
            }
        }catch (NumberFormatException nfe) {
            System.out.println("Index is not a number. Try again.");
            unsubscribeFromLocation();
        }


    }

    private OptionalDouble averageFromLocation(Location location, String dataType){
        switch(dataType){
            case "temperature" -> {
                if(location.measuresTemperature()){
                    return user.getLocationHistory(
                            location.getName()).stream()
                            .mapToDouble(sr -> Double.parseDouble(sr.getTemperature()))
                            .average();
                }
            }
            case "humidity" -> {
                if(location.measuresHumidity()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getHumidity())).average();
                }
            }
            case "pressure" -> {
                if(location.measuresPressure()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getPressure())).average();
                }
            }
        }
        return OptionalDouble.empty();
    }

    private OptionalDouble minFromLocation(Location location, String dataType){
        switch(dataType){
            case "temperature" -> {
                if(location.measuresTemperature()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getTemperature())).min();
                }
            }
            case "humidity" -> {
                if(location.measuresHumidity()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getHumidity())).min();
                }
            }
            case "pressure" -> {
                if(location.measuresPressure()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getPressure())).min();
                }
            }
        }
        return OptionalDouble.empty();
    }

    private OptionalDouble maxFromLocation(Location location, String dataType){
        switch(dataType){
            case "temperature" -> {
                if(location.measuresTemperature()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getTemperature())).max();
                }
            }
            case "humidity" -> {
                if(location.measuresHumidity()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getHumidity())).max();
                }
            }
            case "pressure" -> {
                if(location.measuresPressure()){
                    return user.getLocationHistory(location.getName()).stream().mapToDouble(sr -> Double.parseDouble(sr.getPressure())).max();
                }
            }
        }
        return OptionalDouble.empty();
    }

    private void analyseData(){
        this.user.stopNotifs();
        System.out.println("Choose location to analyse: ");
        locationListPrint(CSI.getLocationsSubscribedByUser(user));
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        try{
            int selected = Integer.parseInt(scanner.nextLine())-1;
            if(selected<=CSI.getLocationsSubscribedByUser(user).size() && selected>-1){
                Location selectedLocation = CSI.getLocationsSubscribedByUser(user).get(selected);
                System.out.print("Choose data type: \nType 'temperature', 'humidity', or 'pressure':\n");
                String dataType = scanner.nextLine();
                if(dataType.equals("temperature") || dataType.equals("humidity") || dataType.equals("pressure")){
                    if(selectedLocation.measuresDataFromString(dataType)){
                        System.out.print("Choose analysis type: \nType 'avg', 'max', or 'min':\n");
                        String analType = scanner.nextLine();
                        switch (analType) {

                            case "avg" -> {System.out.println(averageFromLocation(CSI.getLocationsSubscribedByUser(user).get(selected), dataType).getAsDouble());user.resumeNotifs();}
                            case "max" -> {System.out.println(maxFromLocation(CSI.getLocationsSubscribedByUser(user).get(selected), dataType).getAsDouble());user.resumeNotifs();}
                            case "min" -> {System.out.println(minFromLocation(CSI.getLocationsSubscribedByUser(user).get(selected), dataType).getAsDouble());user.resumeNotifs();}
                            default -> {System.out.println("Wrong analysis type. Try again.");analyseData();}
                        }
                    }else{
                        System.out.println("Selected location does not measure this data type. Try again.");
                        analyseData();
                    }

                }else{
                    System.out.println("Wrong data type. Try again.");
                    analyseData();
                }

            }else{
                System.out.println("Wrong index. Try again.");
                analyseData();
            }
        }catch (NumberFormatException nfe) {
            System.out.println("Index is not a number. Try again.");
            analyseData();
        }


    }

    public static void printHelp(){
        System.out.print("\nKeys:\nh: display this message\nq: stop printing notifications\nr: resume printing notifications\n" +
                "s: subscribe to a new location\nu: unsubscribe from a location\na: analyse data from selected location\n");
    }

    public static void main(String[] args){

        User user = new User();
        KUPA kupa = new KUPA(user);

        CSI csi = new CSI();
        System.out.println("Welcome to KUPA!\nAdd your first location to monitor.\n\n");
        CSI.initTestLocations();
        csi.run();
        kupa.run();
    }


    @Override
    public void run() {
        subscribeToLocation();
        printHelp();
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
                    subscribeToLocation();
                }
                case "u" -> {
                    unsubscribeFromLocation();
                }
                case "a" -> {
                    analyseData();
                }
                case "v" -> {
                    this.user.saveFile("userData.json");
                }
            }
        }

    }

}
