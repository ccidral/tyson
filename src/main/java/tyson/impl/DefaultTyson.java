package tyson.impl;

import tyson.Tyson;

public class DefaultTyson implements Tyson, ConnectionConsumer {

    private final ConnectionProducer localServer;
    private final ConnectionProducer holePuncher;

    public DefaultTyson(ConnectionProducer localServer, ConnectionProducer holePuncher) {
        this.localServer = localServer;
        this.holePuncher = holePuncher;

        localServer.addConsumer(this);
        holePuncher.addConsumer(this);
    }

    @Override
    public void punchHoles() {
        localServer.start();
        holePuncher.start();
    }

    @Override
    public void consumeConnection(ConnectionProducer producer) {
        if(producer == localServer)
            holePuncher.stop();
        else
            localServer.stop();
    }
}
