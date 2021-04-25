package org.processmining.plugins.tpm.model;

import org.deckfour.xes.model.XEvent;

public class TraceSliceEntry {
	
	private static String COMMON_EVENT_ATTRIBUTE_NAME = "concept:name";
	private XEvent event;
	private Comparable<String> groupId;
	private int index;

	public TraceSliceEntry(XEvent event, Comparable<String> groupId, int index) {
		this.event = event;
		this.groupId = groupId;
		this.index = index;
	}

	public XEvent getEvent() {
		return event;
	}

	public void setEvent(XEvent event) {
		this.event = event;
	}

	public Comparable<String> getGroupId() {
		return groupId;
	}

	public void setGroupId(Comparable<String> groupId) {
		this.groupId = groupId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return String.format("<%s>: <%s>: <%d>", this.event.getAttributes().get(COMMON_EVENT_ATTRIBUTE_NAME),
				this.groupId, this.index);
	}
}
