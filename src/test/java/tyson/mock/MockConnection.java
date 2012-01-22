package tyson.mock;

import tyson.Connection;
import tyson.lang.NotImplementedYet;

import java.net.Socket;

public class MockConnection implements Connection {

    @Override
    public Socket getSocket() {
        throw new NotImplementedYet();
    }

}
