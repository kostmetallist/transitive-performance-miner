package org.processmining.plugins.tpm;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;
import org.processmining.plugins.tpm.ui.visualize.TpmVisualizerListener;

@Plugin(
		name = "@0 Visualize Marked Cluster Net",
		level = PluginLevel.PeerReviewed,
		parameterLabels = { "Marked Cluster Net" },
		returnLabels = { "Visualized Marked Cluster Net" },
		returnTypes = { JComponent.class },
		userAccessible = true)
@Visualizer
public class TpmMarkedClusterNetVisualizationPlugin {

	@PluginVariant(requiredParameterLabels = { 0 })
	public JComponent visualize(PluginContext context, TpmMarkedClusterNet mcn) {

		TpmVisualizerListener listener = new TpmVisualizerListener(context, mcn);
		return listener.preparePanel();
	}
}