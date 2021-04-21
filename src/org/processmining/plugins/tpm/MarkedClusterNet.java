package org.processmining.plugins.tpm;

import java.util.Set;

public class MarkedClusterNet {

	private Set<EventCluster> clusters;
	private Set<ClusterTransition> clusterTransitions;

	public Set<EventCluster> getClusters() {
		return clusters;
	}

	public void setClusters(Set<EventCluster> clusters) {
		this.clusters = clusters;
	}

	public Set<ClusterTransition> getClusterTransitions() {
		return clusterTransitions;
	}
	
	public void setClusterTransitions(Set<ClusterTransition> clusterTransitions) {
		this.clusterTransitions = clusterTransitions;
	}
}
