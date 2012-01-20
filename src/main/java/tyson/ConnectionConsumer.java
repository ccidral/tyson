package tyson;

public interface ConnectionConsumer {

    void consumeConnection(Connection connection, ConnectionProducer producer);

}
