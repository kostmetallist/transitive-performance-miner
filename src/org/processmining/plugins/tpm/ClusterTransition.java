package org.processmining.plugins.tpm;

public class ClusterTransition {

	private EventCluster fromCluster, toCluster;
	private WeightCharacteristic weight;
	
	public EventCluster getFromCluster() {
		return fromCluster;
	}

	public void setFromCluster(EventCluster fromCluster) {
		this.fromCluster = fromCluster;
	}

	public EventCluster getToCluster() {
		return toCluster;
	}

	public void setToCluster(EventCluster toCluster) {
		this.toCluster = toCluster;
	}

	public WeightCharacteristic getWeight() {
		return weight;
	}

	public void setWeight(WeightCharacteristic weight) {
		this.weight = weight;
	}
}
