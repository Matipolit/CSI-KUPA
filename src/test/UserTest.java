package test;

import CSI.CSI;
import KUPA.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    @BeforeAll
    static void init(){
        CSI.initTestLocations();
    }

    @Test
    public void saveFileTest(){
        User tester = new User();
        tester.saveFile("testFile.json");
        File f = new File("testFile.json");
        assertTrue(f.exists() && !f.isDirectory());
    }



}
