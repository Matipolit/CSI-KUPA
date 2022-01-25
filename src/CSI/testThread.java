package CSI;

import java.util.Scanner;

public class testThread {

    public static void main(String[] args) {
        new Thread(new ReadRunnable()).start();
        new Thread(new PrintRunnable()).start();
    }


    static class ReadRunnable implements Runnable {

        @Override
        public void run() {
            final Scanner in = new Scanner(System.in);
            while(in.hasNext()) {
                final String line = in.nextLine();
                System.out.println("Input line: " + line);
                if ("end".equalsIgnoreCase(line)) {
                    System.out.println("Ending one thread");
                    break;
                }
            }
        }

    }

    static class PrintRunnable implements Runnable {

        static void printShit(String shit){
            System.out.println(shit);
        }

        @Override
        public void run() {
            int i = 50;
            while(i>0) {
                printShit("Beep: " + i --);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    throw new IllegalStateException(ex);
                }
            }
            System.out.println("!!!! BOOM !!!!");
        }
    }
}
