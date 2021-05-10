package org.processmining.plugins.tpm.ui;

import com.fluxicon.slickerbox.factory.SlickerFactory;

import com.google.common.collect.Sets;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventAttributeClassifier;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
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
	private Set<Set<String>> fromToUnorderedPairs;
	private boolean isTrivialAnalysisExpected = false;

	private JPanel clusterizationPanel, clusterizationDetails, anomaliesPanel;

	private JComboBox<String> groupingAttributeComboBox, measurementAttributeComboBox;

	private JLabel groupingAttributeLabel, fromGroupingValueLabel, toGroupingValueLabel,
			measurementAttributeLabel, solverTimeoutLabel;
	private JTextField fromGroupingValueTextField, toGroupingValueTextField;
	
	private JSpinner solverTimeoutSpinner;
	
	private JRadioButton interClusterModeRadio, fullAnalysisModeRadio;
	private ButtonGroup clusterizationModeButtons;
	
	private JCheckBox enableAnomaliesDetectionCheckBox;
	private JRadioButton anomaliesDetectionThreeSigmaRadio, anomaliesDetectionInterQuartileRadio;
	private ButtonGroup anomaliesDetectionModesButtons;
	private JLabel minCasesPerTransitionForAnomalyDetectionLabel;
	private JSpinner minCasesPerTransitionForAnomalyDetectionSpinner;

	public TpmClusterizationAndAnomaliesDialog(XLog log, TpmParameters parameters) {

		this.log = log;
		this.parameters = parameters;
		initComponents();
	}
	
	protected void initComponents() {

		TpmScrollableGridLayout rootLayout = new TpmScrollableGridLayout(this, 2, 4, 0, 0);
		rootLayout.setRowFixed(0, true);
		rootLayout.setRowFixed(1, true);
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
	
	@SuppressWarnings({ "unchecked", "serial" })
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

		groupingAttributeLabel = SlickerFactory.instance().createLabel("  Grouping attribute:");
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 0;
		clusterizationLayoutConstraints.weightx = 0.4;
		clusterizationLayoutConstraints.ipady = 10;
		clusterizationLayoutConstraints.anchor = GridBagConstraints.LINE_START;
		clusterizationPanel.add(groupingAttributeLabel, clusterizationLayoutConstraints);
		
		groupingAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		groupingAttributeComboBox.setSelectedItem(defaultGroupingAttribute);
		
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 1;
		clusterizationPanel.add(groupingAttributeComboBox, clusterizationLayoutConstraints);

		measurementAttributeLabel = SlickerFactory.instance().createLabel("  Measurement attribute:");
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 2;
		clusterizationPanel.add(measurementAttributeLabel, clusterizationLayoutConstraints);
		
		measurementAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		measurementAttributeComboBox.setSelectedItem(defaultMeasurementAttribute);
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 3;
		clusterizationPanel.add(measurementAttributeComboBox, clusterizationLayoutConstraints);
		
		solverTimeoutLabel = SlickerFactory.instance().createLabel("  Solver timeout (in seconds):");
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 4;
		clusterizationPanel.add(solverTimeoutLabel, clusterizationLayoutConstraints);

		solverTimeoutSpinner = new JSpinner(new SpinnerNumberModel(120, 30, 1800, 30));
		solverTimeoutSpinner.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
		clusterizationLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		clusterizationLayoutConstraints.gridx = 0;
		clusterizationLayoutConstraints.gridy = 5;
		clusterizationPanel.add(solverTimeoutSpinner, clusterizationLayoutConstraints);
		
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
		
		interClusterModeRadio = SlickerFactory.instance().createRadioButton("Inter-cluster analysis");
		interClusterModeRadio.setBorder(BorderFactory.createEmptyBorder(15, 10, 0, 0));
		interClusterModeRadio.setSelected(true);
		clusterizationDetails.add(interClusterModeRadio);
		
		fullAnalysisModeRadio = SlickerFactory.instance().createRadioButton("Full analysis (pairwise dependencies)");
		fullAnalysisModeRadio.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 10));
		fullAnalysisModeRadio.setSelected(false);
		clusterizationDetails.add(fullAnalysisModeRadio);
		
		clusterizationModeButtons = new ButtonGroup();
		clusterizationModeButtons.add(interClusterModeRadio);
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
		
		groupingAttributeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				XEventClassifier classifier = new XEventAttributeClassifier(
						"temporary",
						new String[]{groupingAttributeComboBox.getSelectedItem().toString()});

				Set<String> uniques = new HashSet<>();
				for (XTrace trace : log) {
					for (XEvent event : trace) {
						uniques.add(classifier.getClassIdentity(event));
					}
				}

				String[] prefillData = new String[2];

				if (uniques.size() < 2) {

					isTrivialAnalysisExpected = true;
					fromToUnorderedPairs = null;
					
					String valueToFill = (uniques.isEmpty())? "UNDEFINED": uniques.stream().limit(1).toArray(String[]::new)[0];
					prefillData[0] = prefillData[1] = valueToFill;

				} else {

					isTrivialAnalysisExpected = false;
					fromToUnorderedPairs = Sets.combinations(uniques, 2);

					prefillData = uniques.stream().limit(2).toArray(String[]::new);
				}

				fromGroupingValueTextField.setText(prefillData[0]);
				toGroupingValueTextField.setText(prefillData[1]);
				revalidate();
				repaint();
			}
		});
		
		interClusterModeRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (interClusterModeRadio.isSelected()) {

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
		
		// Explicitly triggering an action to pre-populate text fields
		for (ActionListener a: groupingAttributeComboBox.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
		}
	}
	
	@SuppressWarnings("serial")
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
		
		GridBagLayout anomaliesLayout = new GridBagLayout();
		GridBagConstraints anomaliesLayoutConstraints = new GridBagConstraints();
		anomaliesPanel.setLayout(anomaliesLayout);
		
		enableAnomaliesDetectionCheckBox = SlickerFactory.instance().createCheckBox("Enable anomalies detection", true);
		enableAnomaliesDetectionCheckBox.setSelected(false);
		anomaliesLayoutConstraints.fill = GridBagConstraints.VERTICAL;
		anomaliesLayoutConstraints.gridx = 0;
		anomaliesLayoutConstraints.gridy = 0;
		anomaliesLayoutConstraints.anchor = GridBagConstraints.LINE_START;
		anomaliesPanel.add(enableAnomaliesDetectionCheckBox, anomaliesLayoutConstraints);

		anomaliesDetectionThreeSigmaRadio = SlickerFactory.instance().createRadioButton("3σ area selection");
		anomaliesDetectionThreeSigmaRadio.setSelected(true);
		anomaliesLayoutConstraints.gridx = 0;
		anomaliesLayoutConstraints.gridy = 1;
		anomaliesLayoutConstraints.weightx = 0.3;
		anomaliesLayoutConstraints.insets = new Insets(15, 15, 5, 0);
		anomaliesPanel.add(anomaliesDetectionThreeSigmaRadio, anomaliesLayoutConstraints);
		
		anomaliesDetectionInterQuartileRadio = SlickerFactory.instance().createRadioButton("Inter-quartile range selection");
		anomaliesDetectionInterQuartileRadio.setSelected(false);
		anomaliesLayoutConstraints.gridx = 0;
		anomaliesLayoutConstraints.gridy = 2;
		anomaliesPanel.add(anomaliesDetectionInterQuartileRadio, anomaliesLayoutConstraints);
		
		anomaliesDetectionModesButtons = new ButtonGroup();
		anomaliesDetectionModesButtons.add(anomaliesDetectionThreeSigmaRadio);
		anomaliesDetectionModesButtons.add(anomaliesDetectionInterQuartileRadio);
		
		minCasesPerTransitionForAnomalyDetectionLabel = SlickerFactory.instance().createLabel(
				"<html><body>Min number of cases per cluster transition<br>to apply anomalies detection:<body></html>");
		anomaliesLayoutConstraints.gridx = 1;
		anomaliesLayoutConstraints.gridy = 1;
		anomaliesPanel.add(minCasesPerTransitionForAnomalyDetectionLabel, anomaliesLayoutConstraints);
		
		minCasesPerTransitionForAnomalyDetectionSpinner = new JSpinner(new SpinnerNumberModel(100, 1, Integer.MAX_VALUE, 1));
		minCasesPerTransitionForAnomalyDetectionSpinner.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 15));
		anomaliesLayoutConstraints.gridx = 1;
		anomaliesLayoutConstraints.gridy = 2;
		anomaliesPanel.add(minCasesPerTransitionForAnomalyDetectionSpinner, anomaliesLayoutConstraints);
		
		enableAnomaliesDetectionCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (enableAnomaliesDetectionCheckBox.isSelected()) {

					anomaliesDetectionThreeSigmaRadio.setEnabled(true);
					anomaliesDetectionThreeSigmaRadio.setForeground(Color.BLACK);
					anomaliesDetectionInterQuartileRadio.setEnabled(true);
					anomaliesDetectionInterQuartileRadio.setForeground(Color.BLACK);
					minCasesPerTransitionForAnomalyDetectionLabel.setEnabled(true);
					minCasesPerTransitionForAnomalyDetectionSpinner.setEnabled(true);

				} else {

					anomaliesDetectionThreeSigmaRadio.setEnabled(false);
					anomaliesDetectionThreeSigmaRadio.setForeground(Color.GRAY);
					anomaliesDetectionInterQuartileRadio.setEnabled(false);
					anomaliesDetectionInterQuartileRadio.setForeground(Color.GRAY);
					minCasesPerTransitionForAnomalyDetectionLabel.setEnabled(false);
					minCasesPerTransitionForAnomalyDetectionSpinner.setEnabled(false);					
				}

				revalidate();
				repaint();
			}
		});
		
		// Explicitly triggering an action to deactivate components
		for (ActionListener a: enableAnomaliesDetectionCheckBox.getActionListeners()) {
		    a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null) {});
		}
	}

	public void fillSettings() {

		XAttribute groupingAttr = attributesMapping.get(groupingAttributeComboBox.getSelectedItem());

		parameters.setGroupingAttr(groupingAttr);
		parameters.setFromValue(new XAttributeLiteralImpl(groupingAttr.getKey(), fromGroupingValueTextField.getText()));
		parameters.setToValue(new XAttributeLiteralImpl(groupingAttr.getKey(), toGroupingValueTextField.getText()));

		if (fromToUnorderedPairs == null) {
			parameters.setFromToUnorderedPairs(null);

		} else {

			Set<Set<XAttributeLiteral>> convertedPairs = new HashSet<>();
			for (Set<String> stringPair : fromToUnorderedPairs) {
				convertedPairs.add(stringPair.stream().map(x -> new XAttributeLiteralImpl(groupingAttr.getKey(), x)).collect(Collectors.toSet()));
			}

			parameters.setFromToUnorderedPairs(convertedPairs);
		}

		parameters.setMeasurementAttr((XAttributeTimestamp) attributesMapping.get(measurementAttributeComboBox.getSelectedItem()));
		parameters.setSolverTimeout((int) solverTimeoutSpinner.getValue());
		parameters.setTrivialAnalysis(isTrivialAnalysisExpected);
		parameters.setFullAnalysisEnabled(fullAnalysisModeRadio.isSelected());

		parameters.setAnomaliesDetectionMethod((anomaliesDetectionThreeSigmaRadio.isSelected())?
				TpmParameters.AnomaliesDetectionMethod.THREE_SIGMA: TpmParameters.AnomaliesDetectionMethod.INTER_QUARTILE);
		parameters.setAnomaliesDetectionMinDataItems((int) minCasesPerTransitionForAnomalyDetectionSpinner.getValue());
		parameters.setAnomaliesDetectionEnabled(enableAnomaliesDetectionCheckBox.isSelected());
	}
}
