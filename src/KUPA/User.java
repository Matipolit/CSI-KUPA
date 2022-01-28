package KUPA;

import CSI.CSI;
import CSI.Sensor.Location;
import CSI.Sensor.SensorResponse;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class User {

    private boolean printsNotifs;
    private User user;

    private HashMap<String, List<SensorResponse>> responseHistory;

    public User(){
        this.printsNotifs = true;
        this.responseHistory = new HashMap<>();
    }

    public void sendNotification(SensorResponse response, String location){
        if(printsNotifs){
            System.out.println("Received response from location " + location +". Data: " + response.toString());
            if(!responseHistory.containsKey(location)) {
                responseHistory.put(location, new ArrayList<SensorResponse>());
            }
            responseHistory.get(location).add(response);
        }

    }

    public List<SensorResponse> getLocationHistory(String location){
        return responseHistory.get(location);
    }

    public void stopNotifs(){
        System.out.println("Stopped printing notifications. To resume press r.\n");
        printsNotifs = false;
    }

    public void resumeNotifs(){
        System.out.println("Resumed printing notifications. To stop press q.\n");
        printsNotifs = true;
    }

    public void saveFile(String filename){
        Gson gson = new Gson();
        String saveString = gson.toJson(responseHistory);
        //System.out.println(saveString);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            writer.write(saveString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while saving file");
            e.printStackTrace();
        }

    }


}
