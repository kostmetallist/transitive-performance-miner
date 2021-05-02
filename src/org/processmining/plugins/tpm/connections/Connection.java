package org.processmining.plugins.tpm.connections;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.plugins.tpm.model.MarkedClusterNet;
import org.processmining.plugins.tpm.parameters.Parameters;

public class Connection extends AbstractConnection {

	public final static String LOG = "Log";
	public final static String MCN = "MarkedClusterNet";

	private Parameters parameters;

	public Connection(XLog log, MarkedClusterNet mcn,
			Parameters parameters) {

		super("Construct Marked Cluster Net from Event Log Connection");
		put(LOG, log);
		put(MCN, mcn);
		this.parameters = new Parameters(parameters);
	}

	public Parameters getParameters() {
		return parameters;
	}
}
