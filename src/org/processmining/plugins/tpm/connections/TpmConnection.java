package org.processmining.plugins.tpm.connections;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;
import org.processmining.plugins.tpm.parameters.TpmParameters;

public class TpmConnection extends AbstractConnection {

	public final static String LOG = "Log";
	public final static String MCN = "MarkedClusterNet";

	private TpmParameters parameters;

	public TpmConnection(XLog log, TpmMarkedClusterNet mcn,
			TpmParameters parameters) {

		super("Construct Marked Cluster Net from Event Log Connection");
		put(LOG, log);
		put(MCN, mcn);
		this.parameters = new TpmParameters(parameters);
	}

	public TpmParameters getParameters() {
		return parameters;
	}
}
