package org.processmining.plugins.tpm.visual;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;

import org.processmining.plugins.tpm.EventCluster;
import org.processmining.plugins.tpm.MarkedClusterNet;
import org.processmining.plugins.tpm.util.Pair;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


public class MarkedClusterNetInterpreter {

	public static Pair<Dot, TIntObjectMap<DotNode>> visualize(MarkedClusterNet mcn) {

		Dot result = new Dot();
		TIntObjectMap<DotNode> activity2dotNode = new TIntObjectHashMap<>(10, 0.5f, -1);

		for (EventCluster cluster : mcn.getClusters()) {

			DotNode node = result.addNode(cluster.getLabel());
			// activity2dotNode.put(clusterIndex, node);

			node.setOption("shape", "box");
			node.setOption("style", "filled");
			node.setOption("fillcolor", "white:green");
		}

//		for (long edgeIndex : mcn.getEdges()) {
//			int source = mcn.getEdgeSource(edgeIndex);
//			int target = mcn.getEdgeTarget(edgeIndex);
//			result.addEdge(activity2dotNode.get(source), activity2dotNode.get(target));
//		}


		return new Pair<Dot, TIntObjectMap<DotNode>>(result, activity2dotNode);
	}
}
