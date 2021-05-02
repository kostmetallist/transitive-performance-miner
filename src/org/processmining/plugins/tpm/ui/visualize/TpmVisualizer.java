package org.processmining.plugins.tpm.ui.visualize;

import javax.swing.JComponent;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;

public class TpmVisualizer {

	public static JComponent visualizeMarkedClusterNet(PluginContext context, TpmMarkedClusterNet mcn) {
		return ProMJGraphVisualizer.instance().visualizeGraph(context, mcn);
	}
}
