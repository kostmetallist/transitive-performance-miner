package org.processmining.plugins.tpm.ui;

import com.fluxicon.slickerbox.factory.SlickerFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;

import org.processmining.plugins.tpm.parameters.TpmParameters;

public class TpmClusterizationAndAnomaliesDialog extends TpmWizardStep {

	private static final long serialVersionUID = -2130094545823451112L;
	private static final String STANDARD_GROUPING_ATTRIBUTE = "resource";
	private static final String STANDARD_MEASUREMENT_ATTRIBUTE = "timestamp";
	private static final Logger LOGGER = LogManager.getRootLogger(); 
	
	private final XLog log;
	private final TpmParameters parameters;
	
	private Map<String, XAttribute> attributesMapping;

	private JPanel clusterizationPanel, clusterizationDetails, anomaliesPanel;

	private JComboBox<String> groupingAttributeComboBox, measurementAttributeComboBox;

	private JLabel groupingAttributeLabel, fromGroupingValueLabel, toGroupingValueLabel, measurementAttributeLabel;
	private JTextField fromGroupingValueTextField, toGroupingValueTextField;
	
	private JRadioButton intraClusterModeRadio, fullAnalysisModeRadio;
	private ButtonGroup clusterizationModeButtons;
	
	private JCheckBox enableAnomaliesDetectionCheckBox;
	private JLabel anomaliesDetectionThresholdLabel;
	private JSlider anomaliesDetectionThreshold;

	public TpmClusterizationAndAnomaliesDialog(XLog log, TpmParameters parameters) {

		this.log = log;
		this.parameters = parameters;
		initComponents();
	}
	
	protected void initComponents() {

		TpmScrollableGridLayout rootLayout = new TpmScrollableGridLayout(this, 2, 4, 0, 0);
		rootLayout.setRowFixed(0, true);
		// TODO investigate changes
//		rootLayout.setRowFixed(1, true);
		this.setLayout(rootLayout);

		JLabel headerLabel = SlickerFactory.instance().createLabel("<html><h2>Clusterization and Anomalies</h2>");
		rootLayout.setPosition(headerLabel, 0, 0);
		add(headerLabel);

		buildClusterizationPanel();
		rootLayout.setPosition(clusterizationPanel, 0, 1);
		add(clusterizationPanel);

		buildAnomaliesPanel();
		rootLayout.setPosition(anomaliesPanel, 0, 2);
		add(anomaliesPanel);	
	}
	
	@SuppressWarnings("unchecked")
	private void buildClusterizationPanel() {

		clusterizationPanel = SlickerFactory.instance().createRoundedPanel();
		clusterizationPanel.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createCompoundBorder(
							BorderFactory.createEmptyBorder(30, 15, 15, 15),
							BorderFactory.createEtchedBorder(EtchedBorder.RAISED)),
						"Core Algorithm Options",
						TitledBorder.DEFAULT_JUSTIFICATION,
						TitledBorder.CENTER,
						new Font(Font.SANS_SERIF, Font.BOLD, 14)));

//		TpmScrollableGridLayout clusterizationLayout = new TpmScrollableGridLayout(clusterizationPanel, 4, 4, 0, 0);
//		clusterizationLayout.setRowFixed(0, true);
//		clusterizationLayout.setRowFixed(1, true);
//		clusterizationPanel.setLayout(clusterizationLayout);
		
		GridBagLayout clusterizationLayout = new GridBagLayout();
		GridBagConstraints clusterizationLayoutConstraints = new GridBagConstraints();
		clusterizationPanel.setLayout(clusterizationLayout);

		final List<XAttribute> availableAttributes = log.getGlobalEventAttributes();
		attributesMapping = availableAttributes.stream().collect(Collectors.toMap(XAttribute::getKey, attr -> attr));

		String defaultGroupingAttribute = null, standardGroupingAttribute = STANDARD_GROUPING_ATTRIBUTE,
				defaultMeasurementAttribute = null, standardMeasurementAttribute = STANDARD_MEASUREMENT_ATTRIBUTE;

		int defaultGroupingAttributeDist = Integer.MAX_VALUE,
				defaultMeasurementAttributeDist = Integer.MAX_VALUE;
		LevenshteinDistance levenDistance = new LevenshteinDistance();

		LOGGER.debug("Available attribute names for the grouping one:");
		for (Map.Entry<String, XAttribute> entry : attributesMapping.entrySet()) {

			String attrName = entry.getKey();
			int groupingDist = levenDistance.apply(attrName, standardGroupingAttribute),
					measurementDist = levenDistance.apply(attrName, standardMeasurementAttribute);

			LOGGER.debug(String.format("%s: %s", attrName, entry.getValue()));

			if (defaultGroupingAttribute == null ||
					groupingDist < defaultGroupingAttributeDist) {

				defaultGroupingAttribute = attrName;
				defaultGroupingAttributeDist = groupingDist;
			}
			
			if (defaultMeasurementAttribute == null ||
					measurementDist < defaultMeasurementAttributeDist) {

				defaultMeasurementAttribute = attrName;
				defaultMeasurementAttributeDist = measurementDist;
			}
		}
		
//		groupingAttributeLabel = SlickerFactory.instance().createLabel("  Grouping attribute:");
//		clusterizationDetailsLayout.setPosition(groupingAttributeLabel, 0, 0);
//		clusterizationDetails.add(groupingAttributeLabel);
		
		groupingAttributeLabel = SlickerFactory.instance().createLabel("  Grouping attribute:");
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 0;
		clusterizationLayoutConstraints.weightx = 0.4;
		clusterizationLayoutConstraints.ipady = 10;
		clusterizationLayoutConstraints.anchor = GridBagConstraints.LINE_START;
		clusterizationPanel.add(groupingAttributeLabel, clusterizationLayoutConstraints);

//		groupingAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
//				.toArray(new String[attributesMapping.size()]));
//		groupingAttributeComboBox.setSelectedItem(defaultGroupingAttribute);
//		clusterizationLayout.setPosition(groupingAttributeComboBox, 0, 1);
//		clusterizationPanel.add(groupingAttributeComboBox);
		
		groupingAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		groupingAttributeComboBox.setSelectedItem(defaultGroupingAttribute);
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 1;
		clusterizationPanel.add(groupingAttributeComboBox, clusterizationLayoutConstraints);
		
//		measurementAttributeLabel = SlickerFactory.instance().createLabel("  Measurement attribute:");
//		clusterizationLayout.setPosition(measurementAttributeLabel, 0, 2);
//		clusterizationPanel.add(measurementAttributeLabel);
		
		measurementAttributeLabel = SlickerFactory.instance().createLabel("  Measurement attribute:");
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 2;
		clusterizationPanel.add(measurementAttributeLabel, clusterizationLayoutConstraints);

//		measurementAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
//				.toArray(new String[attributesMapping.size()]));
//		measurementAttributeComboBox.setSelectedItem(defaultMeasurementAttribute);
//		clusterizationLayout.setPosition(measurementAttributeComboBox, 0, 3);
//		clusterizationPanel.add(measurementAttributeComboBox);
		
		measurementAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		measurementAttributeComboBox.setSelectedItem(defaultMeasurementAttribute);
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 3;
		clusterizationPanel.add(measurementAttributeComboBox, clusterizationLayoutConstraints);
		
		clusterizationDetails = SlickerFactory.instance().createRoundedPanel();
		clusterizationDetails.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(10, 10, 10, 10),
						BorderFactory.createLineBorder(Color.DARK_GRAY, 1)),
					"Clusterization Mode and Details",
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.CENTER,
					new Font(Font.SANS_SERIF, Font.BOLD, 14)));

		clusterizationDetails.setLayout(new GridLayout(3, 2, 6, 2));
		
		intraClusterModeRadio = SlickerFactory.instance().createRadioButton("Intra-cluster analysis");
		intraClusterModeRadio.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 0));
		intraClusterModeRadio.setSelected(true);
		clusterizationDetails.add(intraClusterModeRadio);
		
		fullAnalysisModeRadio = SlickerFactory.instance().createRadioButton("Full analysis (pairwise dependencies)");
		fullAnalysisModeRadio.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 10));
		fullAnalysisModeRadio.setSelected(false);
		clusterizationDetails.add(fullAnalysisModeRadio);
		
		clusterizationModeButtons = new ButtonGroup();
		clusterizationModeButtons.add(intraClusterModeRadio);
		clusterizationModeButtons.add(fullAnalysisModeRadio);
		
		fromGroupingValueLabel = SlickerFactory.instance().createLabel("From value:");
		fromGroupingValueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
		clusterizationDetails.add(fromGroupingValueLabel);
		
		toGroupingValueLabel = SlickerFactory.instance().createLabel("To value:");
		toGroupingValueLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
		clusterizationDetails.add(toGroupingValueLabel);
		
		fromGroupingValueTextField = new JTextField("Department A");
		fromGroupingValueTextField.setBorder(BorderFactory.createCompoundBorder(
				fromGroupingValueTextField.getBorder(), 
		        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
		clusterizationDetails.add(fromGroupingValueTextField);
		
		toGroupingValueTextField = new JTextField("Department B");
		toGroupingValueTextField.setBorder(BorderFactory.createCompoundBorder(
				toGroupingValueTextField.getBorder(), 
		        BorderFactory.createEmptyBorder(0, 8, 0, 8)));
		clusterizationDetails.add(toGroupingValueTextField);
		
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridheight = 4;
		clusterizationLayoutConstraints.gridx = 1;
		clusterizationLayoutConstraints.gridy = 0;
		clusterizationLayoutConstraints.weightx = 0;
		clusterizationLayoutConstraints.ipady = 0;
		clusterizationPanel.add(clusterizationDetails, clusterizationLayoutConstraints);
		
		intraClusterModeRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (intraClusterModeRadio.isSelected()) {
					fromGroupingValueTextField.setEditable(true);
					toGroupingValueTextField.setEditable(true);
					revalidate();
					repaint();
				}
			}
		});
		
		fullAnalysisModeRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fullAnalysisModeRadio.isSelected()) {
					fromGroupingValueTextField.setEditable(false);
					toGroupingValueTextField.setEditable(false);
					revalidate();
					repaint();
				}
			}
		});
	}
	
	private void buildAnomaliesPanel(){

		anomaliesPanel = SlickerFactory.instance().createRoundedPanel();
		anomaliesPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(30, 15, 15, 15),
					BorderFactory.createEtchedBorder(EtchedBorder.RAISED)),
				"Anomalies Detection Options",
				TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.CENTER,
				new Font(Font.SANS_SERIF, Font.BOLD, 14)));
	
		TpmScrollableGridLayout anomaliesLayout = new TpmScrollableGridLayout(anomaliesPanel, 1, 3, 0, 0);
		anomaliesLayout.setRowFixed(0, true);
		anomaliesLayout.setRowFixed(1, true);
		anomaliesPanel.setLayout(anomaliesLayout);
		
		enableAnomaliesDetectionCheckBox = SlickerFactory.instance().createCheckBox("Enable anomalies detection", true);
		anomaliesLayout.setPosition(enableAnomaliesDetectionCheckBox, 0, 0);
		anomaliesPanel.add(enableAnomaliesDetectionCheckBox);
		
		anomaliesDetectionThresholdLabel = SlickerFactory.instance().createLabel("Anomalies relative value threshold");
		anomaliesLayout.setPosition(anomaliesDetectionThresholdLabel, 0, 1);
		anomaliesPanel.add(anomaliesDetectionThresholdLabel);
		
		anomaliesDetectionThreshold = SlickerFactory.instance().createSlider(JSlider.HORIZONTAL);
		anomaliesLayout.setPosition(anomaliesDetectionThreshold, 0, 2);
		anomaliesPanel.add(anomaliesDetectionThreshold);
	}

	public void fillSettings() {

		XAttribute groupingAttr = attributesMapping.get(groupingAttributeComboBox.getSelectedItem());

		parameters.setGroupingAttr(groupingAttr);
		parameters.setFromValue(new XAttributeLiteralImpl(groupingAttr.getKey(), fromGroupingValueTextField.getText()));
		parameters.setToValue(new XAttributeLiteralImpl(groupingAttr.getKey(), toGroupingValueTextField.getText()));
		parameters.setMeasurementAttr((XAttributeTimestamp) attributesMapping.get(measurementAttributeComboBox.getSelectedItem()));
		parameters.setFullAnalysisEnabled(fullAnalysisModeRadio.isSelected());

		if (enableAnomaliesDetectionCheckBox.isSelected()) {
			// TODO stuff
		}
	}
}
