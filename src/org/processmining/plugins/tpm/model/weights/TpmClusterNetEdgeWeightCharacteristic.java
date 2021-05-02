package org.processmining.plugins.tpm.model.weights;

public class TpmClusterNetEdgeWeightCharacteristic {

	private double averageThroughputTime;
	private double minThroughputTime;
	private double maxThroughputTime;

	public double getAverageThroughputTime() {
		return averageThroughputTime;
	}

	public void setAverageThroughputTime(double averageThroughputTime) {
		this.averageThroughputTime = averageThroughputTime;
	}

	public double getMinThroughputTime() {
		return minThroughputTime;
	}

	public void setMinThroughputTime(double minThroughputTime) {
		this.minThroughputTime = minThroughputTime;
	}

	public double getMaxThroughputTime() {
		return maxThroughputTime;
	}

	public void setMaxThroughputTime(double maxThroughputTime) {
		this.maxThroughputTime = maxThroughputTime;
	}	
}
