package org.processmining.plugins.tpm.model;

import org.processmining.plugins.tpm.model.weights.SimpleWeightCharacteristic;

public class ClusterTransitionIndicator {

	private int fromClusterNodeIndex;
	private int toClusterNodeIndex;
	private SimpleWeightCharacteristic simpleWeightChar;
		
	public ClusterTransitionIndicator(int fromClusterNodeIndex, int toClusterNodeIndex,
			SimpleWeightCharacteristic simpleWeightChar) {

		this.fromClusterNodeIndex = fromClusterNodeIndex;
		this.toClusterNodeIndex = toClusterNodeIndex;
		this.simpleWeightChar = simpleWeightChar;
	}
	
	public ClusterTransitionIndicator(int fromClusterNodeIndex, int toClusterNodeIndex,
			double simpleWeightCharacteristicValue) {

		this.fromClusterNodeIndex = fromClusterNodeIndex;
		this.toClusterNodeIndex = toClusterNodeIndex;
		this.simpleWeightChar = new SimpleWeightCharacteristic(simpleWeightCharacteristicValue);
	}

	public int getFromClusterNodeIndex() {
		return fromClusterNodeIndex;
	}

	public void setFromClusterNodeIndex(int fromClusterNodeIndex) {
		this.fromClusterNodeIndex = fromClusterNodeIndex;
	}

	public int getToClusterNodeIndex() {
		return toClusterNodeIndex;
	}

	public void setToClusterNodeIndex(int toClusterNodeIndex) {
		this.toClusterNodeIndex = toClusterNodeIndex;
	}

	public SimpleWeightCharacteristic getSimpleWeightChar() {
		return simpleWeightChar;
	}

	public void setSimpleWeightChar(SimpleWeightCharacteristic simpleWeightChar) {
		this.simpleWeightChar = simpleWeightChar;
	}
	
	@Override
	public String toString() {
		return String.format("i=%d j=%d: <%f>", fromClusterNodeIndex, toClusterNodeIndex, simpleWeightChar.getValue());
	}
}
