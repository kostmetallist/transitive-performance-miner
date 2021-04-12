package org.processmining.plugins.tpm;

import org.processmining.contexts.uitopia.*;
import org.processmining.contexts.uitopia.annotations.*;
import org.processmining.framework.plugin.*;
import org.processmining.framework.plugin.annotations.*;

import java.util.HashSet;
import java.util.Set;


@Plugin(name = "TransitivePerformanceMiner",
    parameterLabels = { "Clusters", "Transitions" },
    returnLabels = { "ReturnLabel" },
    returnTypes = { MarkedClusterNet.class })
public class TransitivePerformanceMiner {

    @UITopiaVariant(affiliation = "ISPRAS",
        author = "Konstantin Kukushkin",
        email = "kukushkin@ispras.ru",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0, 1 })
    public static MarkedClusterNet buildClusterNet(
    		final PluginContext context,
    		final Set<EventCluster> clusters,
    		final Set<ClusterTransition> transitions) {

    	System.out.println("With context: " + context);

        MarkedClusterNet mcn = new MarkedClusterNet();
        mcn.setClusters(clusters);
        mcn.setClusterTransitions(transitions);
        return mcn;
    }

    /**
     * Constructs a bare-bones cluster net without any edges between items.
     * 
     * @param context
     * 		  Plug-in context to pass forward to underlying methods
     * 
     * @param clusters
     * 		  Set of {@code EventCluster}s
     */
    @UITopiaVariant(affiliation = "ISPRAS",
        author = "Konstantin Kukushkin",
        email = "kukushkin@ispras.ru",
        uiLabel = UITopiaVariant.USEPLUGIN)
    @PluginVariant(requiredParameterLabels = { 0 })
    public static MarkedClusterNet buildClusterNet(
    		final UIPluginContext context,
    		final Set<EventCluster> clusters) {

        Set<ClusterTransition> transitions = new HashSet<ClusterTransition>();
        return buildClusterNet(context, clusters, transitions);
    }
}
