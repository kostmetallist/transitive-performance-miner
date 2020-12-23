package org.processmining.plugins.tpm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Event {
    private String activity;
    private Date timestamp;
    private Map<String, String> extra;

    public Event(String activity, Date timestamp, Map<String, String> extra) {
        this.activity = new String(activity);
        this.timestamp = new Date(timestamp.getTime());
        this.extra = new HashMap<String, String>(extra);
    }

    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity) {
        this.activity = new String(activity);
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    public Map<String, String> getExtra() {
        return this.extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = new HashMap<String, String>(extra);
    }
}
