package org.processmining.plugins.tpm;

import java.util.HashSet;
import java.util.Set;


public class EventCluster {
	private Set<Event> items;
	private String label;
	
	public EventCluster() {}

	public EventCluster(Set<Event> items, String label) {
		this.items = new HashSet<Event>(items);
		this.label = label;
	}

	public Set<Event> getItems() {
		return items;
	}
	
	public String getLabel() {
		return this.label;
	}

	public void setItems(Set<Event> items) {
		this.items = new HashSet<Event>(items);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
}
