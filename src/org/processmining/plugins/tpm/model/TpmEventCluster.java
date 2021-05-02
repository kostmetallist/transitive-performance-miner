package org.processmining.plugins.tpm.model;

import java.util.HashSet;
import java.util.Set;
import org.deckfour.xes.model.XEvent;

public class TpmEventCluster {
	private Set<XEvent> items;
	private String label;
	
	public TpmEventCluster() {}

	public TpmEventCluster(Set<XEvent> items, String label) {
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
