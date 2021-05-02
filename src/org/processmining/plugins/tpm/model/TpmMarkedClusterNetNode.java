package org.processmining.plugins.tpm.model;

import java.awt.Color;
import java.awt.Dimension;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.AbstractDirectedGraph;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphNode;
import org.processmining.models.shapes.RoundedRect;

public class TpmMarkedClusterNetNode extends AbstractDirectedGraphNode {

	private TpmMarkedClusterNet graph;
	
	public TpmMarkedClusterNetNode(TpmMarkedClusterNet graph, String clusterName) {
		this.graph = graph;
		getAttributeMap().put(AttributeMap.SHAPE, new RoundedRect());
		getAttributeMap().put(AttributeMap.SQUAREBB, false);
		getAttributeMap().put(AttributeMap.RESIZABLE, true);
		getAttributeMap().put(AttributeMap.SIZE, new Dimension(100, 60));
		getAttributeMap().put(AttributeMap.FILLCOLOR, Color.GREEN);
		getAttributeMap().put(AttributeMap.LABEL, clusterName);
	}

	public AbstractDirectedGraph<?, ?> getGraph() {
		return graph;
	}
}
