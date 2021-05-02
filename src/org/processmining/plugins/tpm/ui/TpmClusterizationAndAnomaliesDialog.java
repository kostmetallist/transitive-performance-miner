package org.processmining.plugins.tpm.ui;

import com.fluxicon.slickerbox.factory.SlickerFactory;

//import info.clearthought.layout.TableLayout;
//import info.clearthought.layout.TableLayoutConstants;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

	private JPanel clusterizationPanel;
	private JPanel anomaliesPanel;

	private JComboBox<String> groupingAttributeComboBox, measurementAttributeComboBox;

	private JLabel groupingAttributeLabel, fromGroupingValueLabel, toGroupingValueLabel, measurementAttributeLabel;
	private JTextField fromGroupingValueTextField, toGroupingValueTextField;
	
	private JLabel enableAnomaliesDetectionLabel;
	private JCheckBox enableAnomaliesDetectionCheckBox;

	public TpmClusterizationAndAnomaliesDialog(XLog log,
			TpmParameters parameters) {

		this.log = log;
		this.parameters = parameters;
		initComponents();
	}
	
	protected void initComponents() {

		TpmScrollableGridLayout rootLayout = new TpmScrollableGridLayout(this, 2, 5, 0, 0);
		rootLayout.setRowFixed(0, true);
		rootLayout.setRowFixed(1, true);
		this.setLayout(rootLayout);

		JLabel headerLabel = SlickerFactory.instance().createLabel("<html><h1>Configuration Step 2</h1>");
		rootLayout.setPosition(headerLabel, 0, 0);
		add(headerLabel);

		buildClusterizationPanel();
		rootLayout.setPosition(clusterizationPanel, 0, 1);
		add(clusterizationPanel);

		buildAnomaliesPanel();
		rootLayout.setPosition(anomaliesPanel, 0, 4);
		add(anomaliesPanel);	
	}
	
	@SuppressWarnings("unchecked")
	private void buildClusterizationPanel() {

		clusterizationPanel = SlickerFactory.instance().createRoundedPanel();
		clusterizationPanel.setBorder(BorderFactory.createTitledBorder("Configure Main Algorithm Parameters"));

		TpmScrollableGridLayout clusterizationLayout = new TpmScrollableGridLayout(clusterizationPanel, 4, 2, 0, 0);
		clusterizationLayout.setRowFixed(0, true);
		clusterizationLayout.setRowFixed(1, true);

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
		
		groupingAttributeLabel = SlickerFactory.instance().createLabel("Grouping attribute:");
		clusterizationLayout.setPosition(groupingAttributeLabel, 0, 0);
		clusterizationPanel.add(groupingAttributeLabel);

		groupingAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		groupingAttributeComboBox.setSelectedItem(defaultGroupingAttribute);
		clusterizationLayout.setPosition(groupingAttributeComboBox, 0, 1);
		clusterizationPanel.add(groupingAttributeComboBox);
		
		fromGroupingValueLabel = SlickerFactory.instance().createLabel("From value:");
		clusterizationLayout.setPosition(fromGroupingValueLabel, 1, 0);
		clusterizationPanel.add(fromGroupingValueLabel);
		
		fromGroupingValueTextField = new JTextField("Department A");
		clusterizationLayout.setPosition(fromGroupingValueTextField, 1, 1);
		clusterizationPanel.add(fromGroupingValueTextField);
		
		toGroupingValueLabel = SlickerFactory.instance().createLabel("To value:");
		clusterizationLayout.setPosition(toGroupingValueLabel, 2, 0);
		clusterizationPanel.add(toGroupingValueLabel);
		
		toGroupingValueTextField = new JTextField("Department B");
		clusterizationLayout.setPosition(toGroupingValueTextField, 2, 1);
		clusterizationPanel.add(toGroupingValueTextField);
		
		measurementAttributeLabel = SlickerFactory.instance().createLabel("Measurement attribute:");
		clusterizationLayout.setPosition(measurementAttributeLabel, 3, 0);
		clusterizationPanel.add(measurementAttributeLabel);

		measurementAttributeComboBox = SlickerFactory.instance().createComboBox(attributesMapping.keySet()
				.toArray(new String[attributesMapping.size()]));
		measurementAttributeComboBox.setSelectedItem(defaultMeasurementAttribute);
		clusterizationLayout.setPosition(measurementAttributeComboBox, 3, 1);
		clusterizationPanel.add(measurementAttributeComboBox);
	}
	
	private void buildAnomaliesPanel(){

		anomaliesPanel = SlickerFactory.instance().createRoundedPanel();
		anomaliesPanel.setBorder(BorderFactory.createTitledBorder("Define Anomalies Detection Options"));
	
		TpmScrollableGridLayout anomaliesLayout = new TpmScrollableGridLayout(anomaliesPanel, 4, 2, 0, 0);
		anomaliesLayout.setRowFixed(0, true);
		anomaliesLayout.setRowFixed(1, true);
		anomaliesPanel.setLayout(anomaliesLayout);
		
		enableAnomaliesDetectionCheckBox = SlickerFactory.instance().createCheckBox("Enable anomalies detection", true);
		anomaliesLayout.setPosition(enableAnomaliesDetectionCheckBox, 0, 0);
		anomaliesPanel.add(enableAnomaliesDetectionCheckBox);
		
		enableAnomaliesDetectionLabel = SlickerFactory.instance().createLabel("Enable anomalies detection");
		anomaliesLayout.setPosition(enableAnomaliesDetectionLabel, 0, 1);
		anomaliesPanel.add(enableAnomaliesDetectionLabel);
	}

	public void fillSettings() {

		XAttribute groupingAttr = attributesMapping.get(groupingAttributeComboBox.getSelectedItem());

		parameters.setGroupingAttr(groupingAttr);
		parameters.setFromValue(new XAttributeLiteralImpl(groupingAttr.getKey(), fromGroupingValueTextField.getText()));
		parameters.setToValue(new XAttributeLiteralImpl(groupingAttr.getKey(), toGroupingValueTextField.getText()));
		parameters.setMeasurementAttr((XAttributeTimestamp) attributesMapping.get(measurementAttributeComboBox.getSelectedItem()));

		if (enableAnomaliesDetectionCheckBox.isSelected()) {
			// TODO stuff
		}
	}
}
