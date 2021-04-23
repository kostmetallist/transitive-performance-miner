package org.processmining.plugins.tpm.connections;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.plugins.tpm.MarkedClusterNet;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class TransitivePerformanceMinerConnection extends AbstractConnection {

	public final static String LOG = "Log";
	public final static String MCN = "MarkedClusterNet";

	private TransitivePerformanceMinerParameters parameters;

	public TransitivePerformanceMinerConnection(XLog log, MarkedClusterNet mcn,
			TransitivePerformanceMinerParameters parameters) {

		super("Construct Marked Cluster Net from Event Log Connection");
		put(LOG, log);
		put(MCN, mcn);
		this.parameters = new TransitivePerformanceMinerParameters(parameters);
	}

	public TransitivePerformanceMinerParameters getParameters() {
		return parameters;
	}
}
