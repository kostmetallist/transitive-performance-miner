package org.processmining.plugins.tpm;

import java.util.HashSet;
import java.util.Set;


public class EventCluster {
	private Set<Event> items;
	
	public EventCluster() {}

	public EventCluster(Set<Event> items) {
		this.items = new HashSet<Event>(items);
	}

	public Set<Event> getItems() {
		return items;
	}

	public void setItems(Set<Event> items) {
		this.items = new HashSet<Event>(items);
	}
}
