package cdt.app;

/**
 * Use the observer pattern to notify the main thread when new party data is found
 */

public interface RefreshListener {
    void onRefreshEvent(Party p);
}
