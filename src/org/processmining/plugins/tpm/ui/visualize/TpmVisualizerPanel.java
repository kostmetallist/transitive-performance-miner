package org.processmining.plugins.tpm.ui.visualize;

import com.fluxicon.slickerbox.components.NiceIntegerSlider;
import com.fluxicon.slickerbox.components.StackedCardsTabbedPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.processmining.framework.plugin.PluginContext;

public class TpmVisualizerPanel extends JPanel {

	private static final long serialVersionUID = 1576135631308495588L;
	private final TpmVisualizerListener listener;

	/*
	 * Originated from PomPomView plug-in
	 */
	public NiceIntegerSlider nodeSignificanceSlider;
	private Font smallFont;
	private final Color COLOR_BG2 = new Color(120, 120, 120);
	private final Color COLOR_FG = new Color(30, 30, 30);

	public TpmVisualizerPanel(PluginContext context, TpmVisualizerListener listener) {
		this.listener = listener;
		setBackground(new Color(240, 240, 240));
		initializeGui();
	}

	private void initializeGui() {
		/*
		 * Copied from Fuzzy miner
		 */
		smallFont = getFont().deriveFont(11f);
		JPanel upperControlPanel = new JPanel();
		upperControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		upperControlPanel.setBackground(COLOR_BG2);
		upperControlPanel.setOpaque(true);
		upperControlPanel.setLayout(new BorderLayout());

		JPanel lowerControlPanel = new JPanel(); // lowerControlPanel is the Edge filter panel
		lowerControlPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		lowerControlPanel.setBackground(COLOR_BG2);
		lowerControlPanel.setOpaque(true);
		lowerControlPanel.setLayout(new BorderLayout());

		JPanel lowerHeaderPanel = new JPanel();
		lowerHeaderPanel.setOpaque(false);
		lowerHeaderPanel.setLayout(new BoxLayout(lowerHeaderPanel, BoxLayout.Y_AXIS));

		JLabel lowerHeaderLabel = new JLabel("Show result as");
		lowerHeaderLabel.setOpaque(false);
		lowerHeaderLabel.setForeground(COLOR_FG);
		lowerHeaderLabel.setFont(smallFont);
		lowerHeaderPanel.add(lowerHeaderLabel);
		lowerHeaderPanel.add(Box.createVerticalStrut(2));
		lowerHeaderPanel.add(Box.createVerticalStrut(5));
		lowerControlPanel.add(lowerHeaderPanel, BorderLayout.NORTH);

		StackedCardsTabbedPane tabPane = new StackedCardsTabbedPane();
		tabPane.addTab("Node filter", upperControlPanel);
		tabPane.addTab("Show Result", lowerControlPanel);
		tabPane.setActive(0);
		tabPane.setMinimumSize(new Dimension(190, 220));
		tabPane.setMaximumSize(new Dimension(190, 10000));
		tabPane.setPreferredSize(new Dimension(190, 10000));
		tabPane.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

		setBorder(BorderFactory.createEmptyBorder());
		setLayout(new BorderLayout());
		setOpaque(false);
		add(tabPane, BorderLayout.CENTER);
	}

	protected JPanel packVerticallyCentered(JComponent component, int width, int height) {

		JPanel boxed = new JPanel();
		boxed.setLayout(new BoxLayout(boxed, BoxLayout.X_AXIS));
		boxed.setBorder(BorderFactory.createEmptyBorder());
		boxed.setOpaque(false);
		Dimension dim = new Dimension(width, height);
		component.setMinimumSize(dim);
		component.setMaximumSize(dim);
		component.setPreferredSize(dim);
		component.setSize(dim);
		boxed.add(Box.createHorizontalGlue());
		boxed.add(component);
		boxed.add(Box.createHorizontalGlue());
		return boxed;
	}
}
