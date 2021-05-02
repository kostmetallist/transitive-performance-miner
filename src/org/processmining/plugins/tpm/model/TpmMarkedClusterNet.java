package org.processmining.plugins.tpm.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraph;
import org.processmining.models.graphbased.directed.DirectedGraphNode;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.DirectedGraphElement;
import org.processmining.plugins.tpm.model.weights.TpmClusterNetEdgeWeightCharacteristic;

public class TpmMarkedClusterNet extends AbstractDirectedGraph<TpmMarkedClusterNetNode, TpmMarkedClusterNetEdge> {

	private static final Logger LOGGER = LogManager.getRootLogger();
	private Map<String, TpmMarkedClusterNetNode> nodes;
	private Map<TpmMarkedClusterNetNode, Set<TpmMarkedClusterNetEdge>> edges;
	private Map<TpmMarkedClusterNetEdge, TpmClusterNetEdgeWeightCharacteristic> weights;

	public TpmMarkedClusterNet() {
		this.nodes = new HashMap<>();
		this.edges = new HashMap<>();
		this.weights = new HashMap<>();
	}
	
	public void addCluster(String clusterName) {
		// Won't add if such cluster already exists
		if (nodes.keySet().stream().anyMatch(x -> x.equals(clusterName))) {
			return;
		}
		nodes.put(clusterName, new TpmMarkedClusterNetNode(this, clusterName));
	}
	
	public void addTransition(String clusterNameFrom, String clusterNameTo, TpmClusterNetEdgeWeightCharacteristic wChar) {

		if (!nodes.containsKey(clusterNameFrom) || !nodes.containsKey(clusterNameTo)) {
			LOGGER.error(String.format("Cannot add transition: either %s or %s is not present in the graph",
					clusterNameFrom, clusterNameTo));
			return;
		}
		addTransition(nodes.get(clusterNameFrom), nodes.get(clusterNameTo), wChar);
	}
	
	public void addTransition(TpmMarkedClusterNetNode clusterNetNodeFrom,
			TpmMarkedClusterNetNode clusterNetNodeTo, TpmClusterNetEdgeWeightCharacteristic wChar) {

		if (edges.containsKey(clusterNetNodeFrom)) {
			Set<TpmMarkedClusterNetEdge> outgoing = edges.get(clusterNetNodeFrom);
			if (outgoing.stream().anyMatch(x -> x.getTarget().getLabel().equals(clusterNetNodeTo.getLabel()))) {
				return;

			} else {
				outgoing.add(new TpmMarkedClusterNetEdge(clusterNetNodeFrom, clusterNetNodeTo, wChar));
			}

		} else {
			Set<TpmMarkedClusterNetEdge> outgoing = new HashSet<>();
			outgoing.add(new TpmMarkedClusterNetEdge(clusterNetNodeFrom, clusterNetNodeTo, wChar));
			edges.put(clusterNetNodeFrom, outgoing);
		}
	}

	public Set<TpmMarkedClusterNetNode> getNodes() {
		return new HashSet<TpmMarkedClusterNetNode>(nodes.values());
	}

	public Set<TpmMarkedClusterNetEdge> getEdges() {
		
		Set<TpmMarkedClusterNetEdge> result = new HashSet<>();
		for (Map.Entry<TpmMarkedClusterNetNode, Set<TpmMarkedClusterNetEdge>> entry : edges.entrySet()) {
			result.addAll(entry.getValue());
		}
		return result;
	}
	
	public Set<TpmClusterNetEdgeWeightCharacteristic> getWeights() {
		return new HashSet<TpmClusterNetEdgeWeightCharacteristic>(weights.values());
	} 

	public void removeNode(DirectedGraphNode cell) {}
	
	@SuppressWarnings("rawtypes")
	public void removeEdge(DirectedGraphEdge edge) {}

	protected AbstractDirectedGraph<TpmMarkedClusterNetNode, TpmMarkedClusterNetEdge> getEmptyClone() {
		return new TpmMarkedClusterNet();
	}

	protected Map<DirectedGraphElement, DirectedGraphElement> cloneFrom(
			DirectedGraph<TpmMarkedClusterNetNode, TpmMarkedClusterNetEdge> graph) {

		TpmMarkedClusterNet clone = new TpmMarkedClusterNet();
		Map<DirectedGraphElement, DirectedGraphElement> map = new HashMap<DirectedGraphElement, DirectedGraphElement>();
		
		for (TpmMarkedClusterNetNode node : graph.getNodes()) {

			String cloneClusterName = new String(node.getLabel());
			TpmMarkedClusterNetNode cloneNode = new TpmMarkedClusterNetNode(clone, cloneClusterName);

			clone.nodes.put(cloneClusterName, cloneNode);
			map.put(node, cloneNode);
		}

		return map;
	}
}
