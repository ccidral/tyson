package tyson.impl;

public interface ConnectionProducer {

    void start();
    void stop();
    void addConsumer(ConnectionConsumer consumer);

}
