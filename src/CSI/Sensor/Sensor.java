package CSI.Sensor;

import KUPA.User;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sensor {

    private List<User> subscribedUsers;
    private final Location location;
    private boolean shouldContinue = true;
    private Thread t;
    private Object usersSemaphore = new Object();
    private SensorResponse data;


    public Sensor(Location location){
        t = new Thread(this::runInternal);

        this.location = location;

        subscribedUsers = new ArrayList<User>();
    }

    private SensorResponse measureData(){
        Random r = new Random();


        String temperature = "unavailable";
        String humidity = "unavailable";
        String pressure = "unavailable";
        DecimalFormat df = new DecimalFormat("#.#");
        if(location.measuresTemperature()){
            temperature = df.format(20 + r.nextDouble() * (30 - 20));
        }
        if(location.measuresHumidity()){
            humidity = df.format( 40 + r.nextDouble() * (100 - 40));
        }
        if(location.measuresPressure()){
            pressure = df.format( 980 + r.nextDouble() * (1020 - 980));
        }
        return new SensorResponse(temperature, humidity, pressure);
    }

    private void sendData(){
        synchronized (usersSemaphore) {
            for(User user: subscribedUsers){
                user.sendNotification(data, location.getName());
            }
        }
    }

    private void runInternal() {
        while(shouldContinue) {
            this.data = measureData();
            sendData();
            //postUpdate()... aktualizacja, sprzatniae
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void subscribeUser(User user){
        subscribedUsers.add(user);
    }

    public void unsubscribeUser(User user){
        subscribedUsers.remove(user);
    }

    public boolean isUserSubscribed (User user){
        return subscribedUsers.contains(user);
    }

    public Location getLocation() {
        return location;
    }

    public void startObservable() {
        t.start();
    }

    public void stopObservable() {
        shouldContinue = false;
    }

    public void waitFinish() {
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
