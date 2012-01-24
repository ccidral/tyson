package tyson;

public interface ConnectionProducer extends Startable {

    void addConsumer(ConnectionConsumer consumer);

}
