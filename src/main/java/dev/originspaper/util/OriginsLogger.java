package dev.originspaper.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Centralised logger for OriginsPaper with an optional debug mode. */
public final class OriginsLogger {

    private final Logger delegate;
    private boolean debugEnabled;

    public OriginsLogger(Logger delegate, boolean debugEnabled) {
        this.delegate = delegate;
        this.debugEnabled = debugEnabled;
    }

    public void setDebug(boolean enabled) {
        this.debugEnabled = enabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void info(String msg) {
        delegate.info(msg);
    }

    public void warn(String msg) {
        delegate.warning(msg);
    }

    public void warn(String msg, Throwable t) {
        delegate.log(Level.WARNING, msg, t);
    }

    public void severe(String msg, Throwable t) {
        delegate.log(Level.SEVERE, msg, t);
    }

    /** Only logs when debug mode is on (config: debug: true). */
    public void debug(String msg) {
        if (debugEnabled) {
            delegate.info("[DEBUG] " + msg);
        }
    }
}
