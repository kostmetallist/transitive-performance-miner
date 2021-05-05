package org.processmining.plugins.tpm.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.log.parameters.ClassifierParameter;
import org.processmining.log.utils.XUtils;

public class TpmParameters extends PluginParametersImpl implements ClassifierParameter {

	private XEventClassifier classifier;
	private XAttribute groupingAttr;
	// TODO change to Comparable<?>
	private XAttributeLiteral fromValue;
	private XAttributeLiteral toValue;
	private XAttributeTimestamp measurementAttr;
	private boolean fullAnalysisEnabled;

	public TpmParameters() {
		super();
		setTryConnections(true);
	}

	public TpmParameters(
			XLog log,
			XAttribute groupingAttr,
			XAttributeLiteral fromValue,
			XAttributeLiteral toValue,
			XAttributeTimestamp measurementAttr,
			boolean fullAnalysisEnabled) {

		super();
		setClassifier(XUtils.getDefaultClassifier(log));
		setTryConnections(true);
		
		setGroupingAttr(groupingAttr);
		setFromValue(fromValue);
		setToValue(toValue);
		setMeasurementAttr(measurementAttr);
		setFullAnalysisEnabled(fullAnalysisEnabled);
	}

	public TpmParameters(TpmParameters parameters) {

		super(parameters);
		setClassifier(parameters.getClassifier());
		
		setGroupingAttr(parameters.getGroupingAttr());
		setFromValue(parameters.getFromValue());
		setToValue(parameters.getToValue());
		setMeasurementAttr(parameters.getMeasurementAttr());
		setFullAnalysisEnabled(parameters.isFullAnalysisEnabled());
	}

	public boolean equals(Object object) {
		if (object instanceof TpmParameters) {
			TpmParameters parameters = (TpmParameters) object;
			return super.equals(parameters)
					&& getClassifier().equals(parameters.getClassifier())
					&& getGroupingAttr().equals(parameters.getGroupingAttr())
					&& getFromValue().equals(parameters.getFromValue())
					&& getToValue().equals(parameters.getToValue())
					&& getMeasurementAttr().equals(parameters.getMeasurementAttr())
					&& isFullAnalysisEnabled() == parameters.isFullAnalysisEnabled();
		}
		return false;
	}
	
	public XEventClassifier getClassifier() {
		return classifier;
	}
	
	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}
	
	public XAttribute getGroupingAttr() {
		return groupingAttr;
	}

	public void setGroupingAttr(XAttribute groupingAttr) {
		this.groupingAttr = groupingAttr;
	}
	
	public XAttributeLiteral getFromValue() {
		return fromValue;
	}

	public void setFromValue(XAttributeLiteral fromValue) {
		this.fromValue = fromValue;
	}
	
	public XAttributeLiteral getToValue() {
		return toValue;
	}

	public void setToValue(XAttributeLiteral toValue) {
		this.toValue = toValue;
	}
	
	public XAttributeTimestamp getMeasurementAttr() {
		return measurementAttr;
	}

	public void setMeasurementAttr(XAttributeTimestamp measurementAttr) {
		this.measurementAttr = measurementAttr;
	}
	
	public boolean isFullAnalysisEnabled() {
		return fullAnalysisEnabled;
	}

	public void setFullAnalysisEnabled(boolean fullAnalysisEnabled) {
		this.fullAnalysisEnabled = fullAnalysisEnabled;
	}
}
