package org.processmining.plugins.tpm.model;

import org.processmining.plugins.tpm.model.weights.TpmWeightCharacteristic;

public class TpmClusterTransition<T> {

	private T fromCluster, toCluster;
	private TpmWeightCharacteristic weight;
	
	public T getFromCluster() {
		return fromCluster;
	}

	public void setFromCluster(T fromCluster) {
		this.fromCluster = fromCluster;
	}

	public T getToCluster() {
		return toCluster;
	}

	public void setToCluster(T toCluster) {
		this.toCluster = toCluster;
	}

	public TpmWeightCharacteristic getWeight() {
		return weight;
	}

	public void setWeight(TpmWeightCharacteristic weight) {
		this.weight = weight;
	}
}
