package org.processmining.plugins.tpm;

import java.util.Collection;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;
import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;  // TODO check availability in org.processmining.contexts.uitopia
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.tpm.algorithms.TransitivePerformanceMinerAlgorithm;
import org.processmining.plugins.tpm.connections.TransitivePerformanceMinerConnection;
import org.processmining.plugins.tpm.model.MarkedClusterNet;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

@Plugin(name = "Run Transitive Performance Miner",
    parameterLabels = { "Event Log", "Parameters" },
    returnLabels = { "Visualized Marked Cluster Net" },
    returnTypes = { MarkedClusterNet.class },
    help = TransitivePerformanceMinerHelp.TEXT)
public class TransitivePerformanceMiner extends TransitivePerformanceMinerAlgorithm {
	
	private MarkedClusterNet runConnection(PluginContext context, XLog log,
			TransitivePerformanceMinerParameters parameters) {

		if (parameters.isTryConnections()) {
			Collection<TransitivePerformanceMinerConnection> connections;
			try {
				connections = context.getConnectionManager().getConnections(
						TransitivePerformanceMinerConnection.class, context, log);

				for (TransitivePerformanceMinerConnection connection : connections) {
					if (connection.getObjectWithRole(TransitivePerformanceMinerConnection.LOG).equals(log)
							&& connection.getParameters().equals(parameters)) {
						return connection.getObjectWithRole(TransitivePerformanceMinerConnection.MCN);
					}
				}
			} catch (ConnectionCannotBeObtained e) {}
		}

		MarkedClusterNet mcn = this.apply(context, log, parameters);

		if (parameters.isTryConnections()) {
			context.getConnectionManager().addConnection(
					new TransitivePerformanceMinerConnection(log, mcn, parameters));
		}

		return mcn;
	}
	
	@UITopiaVariant(affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0 })
    public MarkedClusterNet buildMarkedClusterNetUI(
    		final UIPluginContext context,
    		final XLog log) {

		TransitivePerformanceMinerParameters parameters = new TransitivePerformanceMinerParameters(log);
		TransitivePerformanceMinerDialog dialog = new TransitivePerformanceMinerDialog(log, parameters);
		InteractionResult iResult = context.showWizard("Set parameters for the miner (classifier)", true, true, dialog);
		
		if (iResult != InteractionResult.FINISHED) {
			return null;
		}

        return runConnection(context, log, parameters);
    }

	// TODO Check set method as static
	@UITopiaVariant(affiliation = "ISPRAS",
	        author = "Konstantin Kukushkin",
	        email = "kukushkin@ispras.ru")
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public MarkedClusterNet buildMarkedClusterNet(
    		final PluginContext context,
    		final XLog log,
    		final TransitivePerformanceMinerParameters parameters) {

    	System.out.println("With context: " + context);

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

    	TransitivePerformanceMinerParameters parameters = new TransitivePerformanceMinerParameters(
    			log,
    			new XAttributeLiteralImpl(groupingAttrName, new String()),
    			new XAttributeLiteralImpl(groupingAttrName, fromGroup),
    			new XAttributeLiteralImpl(groupingAttrName, toGroup),
    			new XAttributeTimestampImpl(measurementAttrName, 0));

        return buildMarkedClusterNet(context, log, parameters);
    }
}
