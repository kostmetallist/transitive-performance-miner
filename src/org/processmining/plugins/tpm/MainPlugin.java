package org.processmining.plugins.tpm;

import java.util.Collection;

import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.tpm.algorithms.PerformanceMinerAlgorithm;
import org.processmining.plugins.tpm.connections.Connection;
import org.processmining.plugins.tpm.model.MarkedClusterNet;
import org.processmining.plugins.tpm.parameters.Parameters;
import org.processmining.plugins.tpm.ui.UI;

@Plugin(name = "Run Transitive Performance Miner",
    parameterLabels = { "Event Log", "Parameters" },
    returnLabels = { "Visualized Marked Cluster Net" },
    returnTypes = { MarkedClusterNet.class },
    help = HelpMessage.TEXT)
public class MainPlugin extends PerformanceMinerAlgorithm {
	
	private MarkedClusterNet runConnection(PluginContext context, XLog log,
			Parameters parameters) {

		Progress progress = context.getProgress();
		progress.setMaximum(100);

		progress.setValue(0);
		progress.setCaption("Setting up connection between plugin artifacts...");
		if (parameters.isTryConnections()) {
			Collection<Connection> connections;
			try {
				connections = context.getConnectionManager().getConnections(
						Connection.class, context, log);

				for (Connection connection : connections) {
					if (connection.getObjectWithRole(Connection.LOG).equals(log)
							&& connection.getParameters().equals(parameters)) {
						return connection.getObjectWithRole(Connection.MCN);
					}
				}
			} catch (ConnectionCannotBeObtained e) {}
		}

		progress.setValue(20);
		progress.setCaption("Performing event log processing...");
		MarkedClusterNet mcn = this.apply(context, log, parameters);

		if (parameters.isTryConnections()) {
			context.getConnectionManager().addConnection(
					new Connection(log, mcn, parameters));
		}

		progress.setValue(100);
		progress.setCaption("Done!");
		return mcn;
	}
	
	@UITopiaVariant(affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0 })
    public MarkedClusterNet buildMarkedClusterNetUI(
    		final UIPluginContext context,
    		final XLog log) {

		UI ui = new UI(context, log);
		Parameters parameters = ui.gatherParameters();

        return runConnection(context, log, parameters);
    }

	@UITopiaVariant(affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public MarkedClusterNet buildMarkedClusterNet(
    		final PluginContext context,
    		final XLog log,
    		final Parameters parameters) {

        return runConnection(context, log, parameters);
    }

    /**
     * Constructs a bare-bones cluster net without any edges between items.
     * 
     * @param context
     * 		  Plug-in context to pass forward to underlying methods
     * 
     * @param log
     * 		  An instance of {@code XLog}
     */
    @UITopiaVariant(affiliation = "ISPRAS",
        author = "Konstantin Kukushkin",
        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0 })
    public MarkedClusterNet buildMarkedClusterNet(
    		final PluginContext context,
    		final XLog log) {

    	// TODO setup with extensions
    	String groupingAttrName = "org:resource";
    	String fromGroup = "Pete";
    	String toGroup = "Sara";
    	String measurementAttrName = "time:timestamp";

    	Parameters parameters = new Parameters(
    			log,
    			new XAttributeLiteralImpl(groupingAttrName, new String()),
    			new XAttributeLiteralImpl(groupingAttrName, fromGroup),
    			new XAttributeLiteralImpl(groupingAttrName, toGroup),
    			new XAttributeTimestampImpl(measurementAttrName, 0));

        return buildMarkedClusterNet(context, log, parameters);
    }
}
