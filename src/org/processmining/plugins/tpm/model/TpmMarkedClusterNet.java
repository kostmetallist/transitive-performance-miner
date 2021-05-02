package org.processmining.plugins.tpm.model;

import java.util.Set;

public class TpmMarkedClusterNet {

	private Set<TpmEventCluster> clusters;
	private Set<TpmClusterTransition> clusterTransitions;

	public Set<TpmEventCluster> getClusters() {
		return clusters;
	}

	public void setClusters(Set<TpmEventCluster> clusters) {
		this.clusters = clusters;
	}

	public Set<TpmClusterTransition> getClusterTransitions() {
		return clusterTransitions;
	}
	
	public void setClusterTransitions(Set<TpmClusterTransition> clusterTransitions) {
		this.clusterTransitions = clusterTransitions;
	}
}
