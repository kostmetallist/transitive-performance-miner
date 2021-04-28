package org.processmining.plugins.tpm.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XLog;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.log.parameters.ClassifierParameter;
import org.processmining.log.utils.XUtils;

public class TransitivePerformanceMinerParameters extends PluginParametersImpl implements ClassifierParameter {

	private XEventClassifier classifier;
	private XAttribute groupingAttr;
	private XAttributeLiteral fromValue;
	private XAttributeLiteral toValue;
	private XAttributeContinuous measurementAttr;

	public TransitivePerformanceMinerParameters(XLog log, XAttribute groupingAttr,
			XAttributeLiteral fromValue, XAttributeLiteral toValue, XAttributeContinuous measurementAttr) {

		super();
		setClassifier(XUtils.getDefaultClassifier(log));
		setTryConnections(true);
		
		setGroupingAttr(groupingAttr);
		setFromValue(fromValue);
		setToValue(toValue);
		setMeasurementAttr(measurementAttr);
	}

	public TransitivePerformanceMinerParameters(TransitivePerformanceMinerParameters parameters) {

		super(parameters);
		setClassifier(parameters.getClassifier());
		
		setGroupingAttr(parameters.getGroupingAttr());
		setFromValue(parameters.getFromValue());
		setToValue(parameters.getToValue());
		setMeasurementAttr(parameters.getMeasurementAttr());
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public XEventClassifier getClassifier() {
		return classifier;
	}
	
	public XAttribute getGroupingAttr() {
		return groupingAttr;
	}

	public void setGroupingAttr(XAttribute groupingAttr) {
		this.groupingAttr = groupingAttr;
	}

	public boolean equals(Object object) {
		if (object instanceof TransitivePerformanceMinerParameters) {
			TransitivePerformanceMinerParameters parameters = (TransitivePerformanceMinerParameters) object;
			return super.equals(parameters) && getClassifier().equals(parameters.getClassifier());
		}
		return false;
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
	
	public XAttributeContinuous getMeasurementAttr() {
		return measurementAttr;
	}

	public void setMeasurementAttr(XAttributeContinuous measurementAttr) {
		this.measurementAttr = measurementAttr;
	}
}
