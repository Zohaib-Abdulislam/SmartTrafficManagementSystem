package service;

import model.TrafficSignal;
import java.util.ArrayList;

public class TrafficManager {

    private ArrayList<TrafficSignal> signals;

    public TrafficManager() {
        signals = new ArrayList<>();
    }

    public void addSignal(TrafficSignal signal) {
        signals.add(signal);
    }

    public ArrayList<TrafficSignal> getSignals() {
        return signals;
    }

    public void removeSignal(int signalId) {

        for (int i = 0; i < signals.size(); i++) {

            if (signals.get(i).getSignalId() == signalId) {
                signals.remove(i);
                break;
            }
        }
    }
}