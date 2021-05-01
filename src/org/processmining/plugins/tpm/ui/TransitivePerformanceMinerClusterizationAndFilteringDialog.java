package org.processmining.plugins.tpm.ui;

import com.fluxicon.slickerbox.factory.SlickerFactory;

//import info.clearthought.layout.TableLayout;
//import info.clearthought.layout.TableLayoutConstants;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class TransitivePerformanceMinerClusterizationAndFilteringDialog extends WizardStep {

	private static final long serialVersionUID = -2130094545823451112L;
	private static final Logger LOGGER = LogManager.getRootLogger(); 
	
	private final XLog log;
	// FIXME remove final if any problems with persisting values
	private final TransitivePerformanceMinerParameters parameters;

	private JPanel clusterizationPanel;
	private JPanel anomaliesPanel;

	private JComboBox<XAttribute> groupingAttributeComboBox, measurementAttributeComboBox;

	private JLabel groupingAttributeLabel, fromGroupingValueLabel, toGroupingValueLabel, measurementAttributeLabel;
	private JTextField fromGroupingValueTextField, toGroupingValueTextField;
	
	private JLabel enableAnomaliesDetectionLabel;
	private JCheckBox enableAnomaliesDetectionCheckBox;

	public TransitivePerformanceMinerClusterizationAndFilteringDialog(XLog log,
			TransitivePerformanceMinerParameters parameters) {

		this.log = log;
		this.parameters = parameters;
		initComponents();
	}
	
	private void initComponents() {

		ScrollableGridLayout rootLayout = new ScrollableGridLayout(this, 2, 3, 0, 0);
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
		rootLayout.setPosition(anomaliesPanel, 0, 2);
		add(anomaliesPanel);	
	}
	
	private void buildClusterizationPanel() {

		clusterizationPanel = SlickerFactory.instance().createRoundedPanel();
		clusterizationPanel.setBorder(BorderFactory.createTitledBorder("Configure Main Algorithm Parameters"));

		ScrollableGridLayout clusterizationLayout = new ScrollableGridLayout(clusterizationPanel, 4, 2, 0, 0);
		clusterizationLayout.setRowFixed(0, true);
		clusterizationLayout.setRowFixed(1, true);

		clusterizationPanel.setLayout(clusterizationLayout);

		final List<XAttribute> availableAttributes = log.getGlobalEventAttributes();

		LOGGER.debug("Available attribute names for the grouping one:");
		for (XAttribute attr : availableAttributes) {
			LOGGER.debug(attr.getKey() + ": " + attr);
		}
		
		groupingAttributeLabel = SlickerFactory.instance().createLabel("Grouping attribute:");
		clusterizationLayout.setPosition(groupingAttributeLabel, 0, 0);
		clusterizationPanel.add(groupingAttributeLabel);
		
		// FIXME unchecked conversion issue
		groupingAttributeComboBox = SlickerFactory.instance().createComboBox(availableAttributes.toArray());
		
		// TODO set XAttribute.getKey() for ComboBox entries
//		groupingAttributeComboBox.setModel(new DefaultComboBoxModel<XAttribute>(availableAttributes.toArray()));
		
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
		
		// FIXME unchecked conversion issue
		measurementAttributeComboBox = SlickerFactory.instance().createComboBox(availableAttributes.toArray());
//		groupingAttributeComboBox.setModel(new DefaultComboBoxModel<XAttribute>(availableAttributes.toArray()));
		clusterizationLayout.setPosition(measurementAttributeComboBox, 3, 1);
		clusterizationPanel.add(measurementAttributeComboBox);
	}
	
	private void buildAnomaliesPanel(){

		anomaliesPanel = SlickerFactory.instance().createRoundedPanel();
		anomaliesPanel.setBorder(BorderFactory.createTitledBorder("Define Anomalies Detection Options"));
	
		ScrollableGridLayout anomaliesLayout = new ScrollableGridLayout(anomaliesPanel, 4, 2, 0, 0);
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
		
		XAttribute groupingAttr = (XAttribute) groupingAttributeComboBox.getSelectedItem();

		parameters.setGroupingAttr(groupingAttr);
		parameters.setFromValue(new XAttributeLiteralImpl(groupingAttr.getKey(), fromGroupingValueTextField.getText()));
		parameters.setToValue(new XAttributeLiteralImpl(groupingAttr.getKey(), toGroupingValueTextField.getText()));
		parameters.setMeasurementAttr((XAttributeTimestamp) measurementAttributeComboBox.getSelectedItem());

		if (enableAnomaliesDetectionCheckBox.isSelected()) {
			// TODO stuff
		}
	}
}
