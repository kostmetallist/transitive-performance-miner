package org.processmining.plugins.tpm;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;
import org.processmining.plugins.tpm.ui.visualize.TpmVisualizerPanel;

@Plugin(
		name = "@0 Visualize Marked Cluster Net",
		level = PluginLevel.PeerReviewed,
		parameterLabels = { "Marked Cluster Net" },
		returnLabels = { "Visualized Marked Cluster Net" },
		returnTypes = { JComponent.class },
		userAccessible = true)
@Visualizer
public class TpmMarkedClusterNetVisualizationPlugin implements ChangeListener, ItemListener {

	PluginContext context;
	TpmMarkedClusterNet mcn;

	JPanel mainPanel;
	JComponent display;
	TpmVisualizerPanel panel;

	@PluginVariant(requiredParameterLabels = { 0 })
	public JComponent visualize(PluginContext context, TpmMarkedClusterNet mcn) {

		this.mcn = mcn;
		this.context = context;

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		display = ProMJGraphVisualizer.instance().visualizeGraph(context, mcn);
		mainPanel.add(display, BorderLayout.CENTER);

		panel = new TpmVisualizerPanel(context, this);
		mainPanel.add(panel, BorderLayout.EAST);
		return mainPanel;
	}

	public void stateChanged(ChangeEvent e) {

		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			generateView();
		}
	}

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		generateView();
	}

	private void generateView() {

		mainPanel.remove(display);
		display = ProMJGraphVisualizer.instance().visualizeGraph(context, mcn);

		mainPanel.add(display, BorderLayout.CENTER);
		mainPanel.validate();
		mainPanel.repaint();
	}
}