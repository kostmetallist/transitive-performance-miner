package org.processmining.plugins.tpm.model;

import org.processmining.plugins.tpm.model.weights.TpmSimpleWeightCharacteristic;

public class TpmClusterTransitionIndicator {

	private int fromClusterNodeIndex;
	private int toClusterNodeIndex;
	private TpmSimpleWeightCharacteristic simpleWeightChar;
		
	public TpmClusterTransitionIndicator(int fromClusterNodeIndex, int toClusterNodeIndex,
			TpmSimpleWeightCharacteristic simpleWeightChar) {

		this.fromClusterNodeIndex = fromClusterNodeIndex;
		this.toClusterNodeIndex = toClusterNodeIndex;
		this.simpleWeightChar = simpleWeightChar;
	}
	
	public TpmClusterTransitionIndicator(int fromClusterNodeIndex, int toClusterNodeIndex,
			double simpleWeightCharacteristicValue) {

		this.fromClusterNodeIndex = fromClusterNodeIndex;
		this.toClusterNodeIndex = toClusterNodeIndex;
		this.simpleWeightChar = new TpmSimpleWeightCharacteristic(simpleWeightCharacteristicValue);
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

	public TpmSimpleWeightCharacteristic getSimpleWeightChar() {
		return simpleWeightChar;
	}

	public void setSimpleWeightChar(TpmSimpleWeightCharacteristic simpleWeightChar) {
		this.simpleWeightChar = simpleWeightChar;
	}
	
	@Override
	public String toString() {
		return String.format("%d -> %d: <%f>", fromClusterNodeIndex, toClusterNodeIndex, simpleWeightChar.getValue());
	}
}
