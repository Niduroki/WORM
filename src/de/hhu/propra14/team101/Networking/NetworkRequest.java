package de.hhu.propra14.team101.Networking;

public class NetworkRequest {
    public String data;
    public boolean waitForAnswer;

    public NetworkRequest(String data, boolean waitForAnswer) {
        this.data = data;
        this.waitForAnswer = waitForAnswer;
    }
}
