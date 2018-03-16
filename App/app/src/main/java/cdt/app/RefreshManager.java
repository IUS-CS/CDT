package cdt.app;

import java.util.ArrayList;
import java.util.List;

/**
 * manages listeners that need updated on refreshes
 */

public class RefreshManager {
    private List<RefreshListener> listeners = new ArrayList<RefreshListener>();

    public void addListener(RefreshListener toAdd) {
        listeners.add(toAdd);
    }

    // Notify all listeners of the new data.
    public void notifyRefresh(Party p) {
        for (RefreshListener hl : listeners)
            hl.refresh(p);
    }
}
