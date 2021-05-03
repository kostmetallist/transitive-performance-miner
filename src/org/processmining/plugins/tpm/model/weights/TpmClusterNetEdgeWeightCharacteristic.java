package org.processmining.plugins.tpm.model.weights;

public class TpmClusterNetEdgeWeightCharacteristic {

	private double minThroughputTime;
	private double averageThroughputTime;
	private double maxThroughputTime;
	
	public TpmClusterNetEdgeWeightCharacteristic(double minThroughputTime, 
			double averageThroughputTime, double maxThroughputTime) {
		
		this.minThroughputTime = minThroughputTime;
		this.averageThroughputTime = averageThroughputTime;
		this.maxThroughputTime = maxThroughputTime;
	}

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

	@Override
	public String toString() {
		return String.format("%f;\n%f;\n%f",
				minThroughputTime, averageThroughputTime, maxThroughputTime);
	}
}
