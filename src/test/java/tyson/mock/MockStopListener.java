package tyson.mock;

import tyson.StopListener;
import tyson.util.Quietly;

public class MockStopListener implements StopListener {

    private boolean stopped;

    public synchronized void waitForStop() {
        if (!stopped)
            Quietly.wait(this);
    }

    @Override
    public synchronized void onStop() {
        stopped = true;
        notify();
    }

    public boolean hasOnStopBeenCalled() {
        return stopped;
    }

}
