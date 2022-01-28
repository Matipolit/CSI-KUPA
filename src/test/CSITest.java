package test;

import CSI.CSI;
import CSI.Sensor.Location;


import CSI.Sensor.Sensor;
import KUPA.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CSITest {

    @BeforeAll
    static void init(){
        CSI.initTestLocations();
    }

    @Test
    public void testGetLocationsFromFile(){
        assertEquals(7, CSI.getAvailableLocations().size());
    }

    @Test
    public void testSubscribeUserToSensor() {
        Sensor sensor = mock(Sensor.class);
        User user = new User();
        doNothing().when(sensor).subscribeUser(user);
        CSI.subscribeToSensorByLocation(user, CSI.getAvailableLocations().get(0));
        verify(sensor, times(1)).subscribeUser(user);
    }









}
