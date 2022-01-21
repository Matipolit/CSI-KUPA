package CSI;

import KUPA.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sensor {

    private List<User> subscribedUsers;
    private final String localisation;

    private final Boolean measuresTemperature;
    private final Boolean measuresHumidity;
    private final Boolean measuresPressure;


    public Sensor(String localisation, boolean measuresTemperature, boolean measuresHumidity, boolean measuresPressure){
        this.localisation = localisation;

        this.measuresTemperature = measuresTemperature;
        this.measuresHumidity = measuresHumidity;
        this.measuresPressure = measuresPressure;

        subscribedUsers = new ArrayList<User>();
    }

    private SensorResponse measureData(){
        Random r = new Random();


        String temperature = "unavailable";
        String humidity = "unavailable";
        String pressure = "unavailable";
        if(measuresTemperature){
            temperature = String.valueOf(20 + r.nextDouble() * (30 - 20));
        }
        if(measuresHumidity){
            temperature = String.valueOf(40 + r.nextDouble() * (40 - 100));
        }
        if(measuresPressure){
            temperature = String.valueOf(980 + r.nextDouble() * (980 - 1020));
        }
        return new SensorResponse(temperature, humidity, pressure);
    }

    public void sendData(){
        for(User user: subscribedUsers){
            user.receiveData(measureData());
        }
    }

    public void subscribeUser(User user){
        subscribedUsers.add(user);
    }

    public void unsubscribeUser(User user){
        subscribedUsers.remove(user);
    }

}
