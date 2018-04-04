package cdt.app;

/**
 * manages the listener that need updated on refreshes
 */

public class RefreshManager {
    private static RefreshListener listener;

    // sets the listener to a class implementing RefreshListener
    public static void setListener(RefreshListener newListener) {
        listener = newListener;
    }

    // Notify the listener of the new data.
    public static void notifyRefresh(Party p) {
        listener.onRefreshEvent(p);
    }
}
