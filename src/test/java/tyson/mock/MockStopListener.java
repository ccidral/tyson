package tyson.mock;

import tyson.StopListener;
import tyson.util.Silently;

public class MockStopListener implements StopListener {

    private boolean stopped;

    public synchronized void waitForStop() {
        if (!stopped)
            Silently.wait(this);
    }

    @Override
    public synchronized void onStop() {
        stopped = true;
        notify();
    }

}
