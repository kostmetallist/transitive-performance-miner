package org.processmining.plugins.tpm;

import java.util.HashSet;
import java.util.Set;
import org.deckfour.xes.model.XEvent;

public class EventCluster {
	private Set<XEvent> items;
	private String label;
	
	public EventCluster() {}

	public EventCluster(Set<XEvent> items, String label) {
		this.items = new HashSet<XEvent>(items);
		this.label = label;
	}

	public Set<XEvent> getItems() {
		return items;
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setItems(Set<XEvent> items) {
		this.items = new HashSet<XEvent>(items);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
}
