package org.processmining.plugins.tpm.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.tpm.MarkedClusterNet;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class TransitivePerformanceMinerAlgorithm {

	protected MarkedClusterNet apply(PluginContext context, XLog log,
			TransitivePerformanceMinerParameters parameters) {

		return new MarkedClusterNet();
	}
}
