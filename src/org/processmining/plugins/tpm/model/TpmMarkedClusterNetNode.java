package org.processmining.plugins.tpm.model;

import java.awt.Dimension;

import javax.swing.SwingConstants;

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
		getAttributeMap().put(AttributeMap.SIZE, new Dimension(90, 70));
		getAttributeMap().put(AttributeMap.LABEL, clusterName);
		getAttributeMap().put(AttributeMap.LABELVERTICALALIGNMENT, SwingConstants.CENTER);
	}

	public AbstractDirectedGraph<?, ?> getGraph() {
		return graph;
	}
}
