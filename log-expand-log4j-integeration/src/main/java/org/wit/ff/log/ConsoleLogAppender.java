package org.wit.ff.log;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by F.Fang on 2015/11/10.
 * Version :2015/11/10
 */
public class ConsoleLogAppender extends AppenderSkeleton {

    private static final String COMMON_LOG_QUEUE = "demo.business.log";

    @Override
    public void activateOptions() {
        System.out.println("init");
    }

    @Override
    protected void append(LoggingEvent event) {
        System.out.println(layout.format(event));
    }

    @Override
    public void close() {
        System.out.println("close!!!");
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
