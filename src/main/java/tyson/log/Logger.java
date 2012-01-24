package tyson.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy HH:mm:ss.SSS");

    private final String loggerName;

    public Logger(Object target) {
        loggerName = target.getClass().getSimpleName();
    }

    public void info(String message) {
        String threadName = Thread.currentThread().getName();
        String now = DATE_FORMAT.format(new Date());
        System.out.format("%s %18s ~ %-18s => %s%n", now, threadName, loggerName, message);
    }

    public void error(Throwable throwable) {
        throwable.printStackTrace();
    }

}
