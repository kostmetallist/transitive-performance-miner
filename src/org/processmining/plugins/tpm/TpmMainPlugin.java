package org.processmining.plugins.tpm;

import java.util.Collection;

import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.tpm.connections.TpmConnection;
import org.processmining.plugins.tpm.engine.TpmEngine;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;
import org.processmining.plugins.tpm.parameters.TpmParameters;
import org.processmining.plugins.tpm.ui.TpmUI;

@Plugin(
		name = "Run Transitive Performance Miner",
	    parameterLabels = { "Event Log", "Parameters" },
	    returnLabels = { "Marked Cluster Net" },
	    returnTypes = { TpmMarkedClusterNet.class },
	    help = TpmHelpMessage.TEXT)
public class TpmMainPlugin {
	
	private TpmMarkedClusterNet runConnection(
			PluginContext context,
			XLog log,
			TpmParameters parameters) {

		Progress progress = context.getProgress();
		progress.setMaximum(100);

		progress.setValue(0);
		progress.setCaption("Setting up connection between plugin artifacts...");
		if (parameters.isTryConnections()) {
			Collection<TpmConnection> connections;
			try {
				connections = context.getConnectionManager().getConnections(
						TpmConnection.class, context, log);

				for (TpmConnection connection : connections) {
					if (connection.getObjectWithRole(TpmConnection.LOG).equals(log)
							&& connection.getParameters().equals(parameters)) {
						return connection.getObjectWithRole(TpmConnection.MCN);
					}
				}
			} catch (ConnectionCannotBeObtained e) {}
		}

		progress.setValue(20);
		progress.setCaption("Performing event log processing...");
		
		TpmEngine engine = new TpmEngine(log, parameters);
		TpmMarkedClusterNet mcn = engine.buildMCN();

		if (parameters.isTryConnections()) {
			context.getConnectionManager().addConnection(
					new TpmConnection(log, mcn, parameters));
		}

		progress.setValue(100);
		progress.setCaption("Done!");
		return mcn;
	}

	/**
     * Constructs a marked cluster net for given log in an interactive mode.
     * 
     * @param context
     * 		  UI plug-in context to pass forward to underlying methods
     * 
     * @param log
     * 		  An instance of {@code XLog}
     */
	@UITopiaVariant(
			affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0 })
    public TpmMarkedClusterNet runUI(
    		final UIPluginContext context,
    		final XLog log) {

		TpmUI ui = new TpmUI(context, log);
		TpmParameters parameters = ui.gatherParameters();

        return runConnection(context, log, parameters);
    }

	/**
     * Constructs a marked cluster net for given log and parameters.
     * 
     * @param context
     * 		  Plug-in context to pass forward to underlying methods
     * 
     * @param log
     * 		  An instance of {@code XLog}
     * 
     * @param parameters
     * 		  {@code TpmParameters} class object to get mandatory analysis settings
     */
	@UITopiaVariant(
			affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public TpmMarkedClusterNet run(
    		final PluginContext context,
    		final XLog log,
    		final TpmParameters parameters) {

        return runConnection(context, log, parameters);
    }
}
