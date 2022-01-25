package KUPA;

import CSI.CSI;
import CSI.Sensor.Location;
import CSI.Sensor.SensorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class User {

    private boolean printsNotifs;
    private User user;

    private HashMap<String, List<SensorResponse>> responseHistory;

    public void sendNotification(SensorResponse response, String location){
        if(printsNotifs){
            System.out.println("Received response from location " + location +". Data: " + response.toString());
        }
        if(!responseHistory.containsKey(location)){
            responseHistory.put(location, new ArrayList<SensorResponse>());
        }else{
            responseHistory.get(location).add(response);
        }
    }

    public User(){
        this.printsNotifs = true;
        this.responseHistory = new HashMap<>();
    }

    public void stopNotifs(){
        System.out.println("Stopped printing notifications. To resume press r.\n");
        printsNotifs = false;
    }

    public void resumeNotifs(){
        System.out.println("Resumed printing notifications. To stop press q.\n");
        printsNotifs = true;
    }


}
