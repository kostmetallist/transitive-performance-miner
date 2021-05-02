package org.processmining.plugins.tpm.model;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.AttributeMap.ArrowType;
import org.processmining.models.graphbased.directed.AbstractDirectedGraphEdge;
import org.processmining.plugins.tpm.model.weights.TpmClusterNetEdgeWeightCharacteristic;

public class TpmMarkedClusterNetEdge extends AbstractDirectedGraphEdge<TpmMarkedClusterNetNode, TpmMarkedClusterNetNode> {
	
	// TODO move to TpmMarkedClusterNetElement?
	private TpmClusterNetEdgeWeightCharacteristic wChar;

	public TpmMarkedClusterNetEdge(TpmMarkedClusterNetNode source, TpmMarkedClusterNetNode target,
			TpmClusterNetEdgeWeightCharacteristic wChar) {

		super(source, target);
		this.wChar = wChar;
		getAttributeMap().put(AttributeMap.EDGEEND, ArrowType.ARROWTYPE_TECHNICAL);
	}
	
	public TpmClusterNetEdgeWeightCharacteristic getWChar() {
		return this.wChar;
	}
}
