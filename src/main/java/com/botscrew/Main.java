package com.botscrew;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.List;

/**
 * Main class
 *
 * @author Michael Rudyy
 * @version 1.1
 */
public class Main {

    public static void main(String[] args) {
        disableLoggers();
        ConsoleApplication.run();
    }

    private static void disableLoggers() {
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }
    }
}
