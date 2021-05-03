package org.processmining.plugins.tpm.ui.visualize;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;

public class TpmVisualizerListener implements ChangeListener, ItemListener {

	PluginContext context;
	TpmMarkedClusterNet mcn;

	JPanel mainPanel;
	JComponent display;
	TpmVisualizerPanel panel;
	
	public TpmVisualizerListener(PluginContext context, TpmMarkedClusterNet mcn) {
		super();
		this.context = context;
		this.mcn = mcn;
	}

	public JComponent preparePanel() {

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
