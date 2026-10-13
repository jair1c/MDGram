package org.telegram.messenger;

import java.net.HttpURLConnection;
import java.util.concurrent.CancellationException;

/** Cancellation shared by a queued batch and its current HTTP connection. */
public final class MDGramTranslationRequest {
    private boolean cancelled;
    private HttpURLConnection connection;

    public synchronized boolean isCancelled() { return cancelled; }

    public synchronized void check() {
        if (cancelled) throw new CancellationException();
    }

    public synchronized void attach(HttpURLConnection value) {
        check();
        connection = value;
    }

    public synchronized void detach(HttpURLConnection value) {
        if (connection == value) connection = null;
    }

    public void cancel() {
        HttpURLConnection active;
        synchronized (this) {
            cancelled = true;
            active = connection;
            connection = null;
        }
        if (active != null) active.disconnect();
    }
}
