package tech.otter.merchant.util;

import com.badlogic.gdx.ApplicationLogger;

import java.util.ArrayList;
import java.util.List;

public class MultiLogger implements ApplicationLogger {
    private List<ApplicationLogger> loggers = new ArrayList<>();

    public MultiLogger addLogger(ApplicationLogger logger) {
        this.loggers.add(logger);
        return this;
    }

    public void removeLogger(ApplicationLogger logger) {
        this.loggers.remove(logger);
    }

    @Override
    public void log(String tag, String message) {
        loggers.forEach(l -> l.log(tag, message));
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        loggers.forEach(l -> l.log(tag, message, exception));
    }

    @Override
    public void error(String tag, String message) {
        loggers.forEach(l -> l.error(tag, message));
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        loggers.forEach(l -> l.error(tag, message, exception));
    }

    @Override
    public void debug(String tag, String message) {
        loggers.forEach(l -> l.debug(tag, message));
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        loggers.forEach(l -> l.debug(tag, message, exception));
    }
}
