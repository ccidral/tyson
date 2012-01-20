package tyson;

public interface ConnectionProducer {

    void start();
    void stop();
    void addConsumer(ConnectionConsumer consumer);

}