package tech.otter.merchant.util;

import com.badlogic.gdx.ApplicationLogger;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 11/6/16.
 */
public class GameLogger implements ApplicationLogger {
    private List<LogWidget> widgets = new ArrayList<>();

    public void addWidget(LogWidget widget) {
        if(!widgets.contains(widget)) this.widgets.add(widget);
    }
    public void removeWidget(LogWidget widget) {
        widgets.remove(widget);
    }

    @Override
    public void log(String tag, String message) {
        widgets.forEach(l -> l.log(tag, message));
    }

    @Override
    @Deprecated
    public void log(String tag, String message, Throwable exception) {
        // Not implemented.
    }

    @Override
    @Deprecated
    public void error(String tag, String message) {

    }

    @Override
    @Deprecated
    public void error(String tag, String message, Throwable exception) {

    }

    @Override
    @Deprecated
    public void debug(String tag, String message) {

    }

    @Override
    @Deprecated
    public void debug(String tag, String message, Throwable exception) {

    }
}
