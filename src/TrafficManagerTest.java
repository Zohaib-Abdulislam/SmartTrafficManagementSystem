package test;

import model.TrafficSignal;
import service.TrafficManager;

public class TrafficManagerTest {

    public static void main(String[] args) {

        TrafficManager manager = new TrafficManager();

        TrafficSignal signal =
                new TrafficSignal(
                        1,
                        "Mall Road",
                        "Green"
                );

        manager.addSignal(signal);

        if (manager.getSignals().size() == 1) {

            System.out.println("Test Passed");

        } else {

            System.out.println("Test Failed");
        }
    }
}