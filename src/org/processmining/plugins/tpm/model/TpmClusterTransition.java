package org.processmining.plugins.tpm.model;

import org.processmining.plugins.tpm.model.weights.TpmWeightCharacteristic;

public class TpmClusterTransition {

	private TpmEventCluster fromCluster, toCluster;
	private TpmWeightCharacteristic weight;
	
	public TpmEventCluster getFromCluster() {
		return fromCluster;
	}

	public void setFromCluster(TpmEventCluster fromCluster) {
		this.fromCluster = fromCluster;
	}

	public TpmEventCluster getToCluster() {
		return toCluster;
	}

	public void setToCluster(TpmEventCluster toCluster) {
		this.toCluster = toCluster;
	}

	public TpmWeightCharacteristic getWeight() {
		return weight;
	}

	public void setWeight(TpmWeightCharacteristic weight) {
		this.weight = weight;
	}
}
