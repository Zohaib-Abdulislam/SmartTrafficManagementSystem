package model;

public class TrafficSignal {

    private int signalId;
    private String location;
    private String status;

    public TrafficSignal(int signalId, String location, String status) {
        this.signalId = signalId;
        this.location = location;
        this.status = status;
    }

    public int getSignalId() {
        return signalId;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}