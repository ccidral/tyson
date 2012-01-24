package tyson;

public interface Startable {

    void start();
    void stop();
    void addStopListener(StopListener listener);

}
