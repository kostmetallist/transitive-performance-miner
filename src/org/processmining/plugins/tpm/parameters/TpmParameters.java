package org.processmining.plugins.tpm.parameters;

import java.util.Set;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.log.parameters.ClassifierParameter;
import org.processmining.log.utils.XUtils;

public class TpmParameters extends PluginParametersImpl implements ClassifierParameter {
	
	public static enum AnomaliesDetectionMethod {
		THREE_SIGMA,
		INTER_QUARTILE,
	}

	private XEventClassifier classifier;

	private XAttribute groupingAttr;
	// TODO change to Comparable<?>
	private XAttributeLiteral fromValue;
	private XAttributeLiteral toValue;
	private Set<Set<XAttributeLiteral>> fromToUnorderedPairs;

	private XAttributeTimestamp measurementAttr;
	private int solverTimeout; // in seconds
	private boolean fullAnalysisEnabled;
	private boolean isTrivialAnalysis;

	private AnomaliesDetectionMethod anomaliesDetectionMethod;
	private int anomaliesDetectionMinDataItems;
	private boolean anomaliesDetectionEnabled;

	public TpmParameters() {
		super();
		setTryConnections(true);
	}

	public TpmParameters(
			XLog log,
			XAttribute groupingAttr,
			XAttributeLiteral fromValue,
			XAttributeLiteral toValue,
			Set<Set<XAttributeLiteral>> fromToUnorderedPairs,
			XAttributeTimestamp measurementAttr,
			int solverTimeout,
			boolean fullAnalysisEnabled,
			boolean isTrivialAnalysis,
			AnomaliesDetectionMethod anomaliesDetectionMethod,
			int anomaliesDetectionMinDataItems,
			boolean anomaliesDetectionEnabled) {

		super();
		setClassifier(XUtils.getDefaultClassifier(log));
		setTryConnections(true);
		
		setGroupingAttr(groupingAttr);
		setFromValue(fromValue);
		setToValue(toValue);
		setFromToUnorderedPairs(fromToUnorderedPairs);
		
		setMeasurementAttr(measurementAttr);
		setSolverTimeout(solverTimeout);
		setFullAnalysisEnabled(fullAnalysisEnabled);
		setTrivialAnalysis(isTrivialAnalysis);

		setAnomaliesDetectionMethod(anomaliesDetectionMethod);
		setAnomaliesDetectionMinDataItems(anomaliesDetectionMinDataItems);
		setAnomaliesDetectionEnabled(anomaliesDetectionEnabled);
	}

	public TpmParameters(TpmParameters parameters) {

		super(parameters);
		setClassifier(parameters.getClassifier());
		
		setGroupingAttr(parameters.getGroupingAttr());
		setFromValue(parameters.getFromValue());
		setToValue(parameters.getToValue());
		setFromToUnorderedPairs(parameters.getFromToUnorderedPairs());

		setMeasurementAttr(parameters.getMeasurementAttr());
		setSolverTimeout(parameters.getSolverTimeout());
		setFullAnalysisEnabled(parameters.isFullAnalysisEnabled());
		setTrivialAnalysis(parameters.isTrivialAnalysis());

		setAnomaliesDetectionMethod(parameters.getAnomaliesDetectionMethod());
		setAnomaliesDetectionMinDataItems(parameters.getAnomaliesDetectionMinDataItems());
		setAnomaliesDetectionEnabled(parameters.isAnomaliesDetectionEnabled());
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
					&& getSolverTimeout() == parameters.getSolverTimeout()
					&& isFullAnalysisEnabled() == parameters.isFullAnalysisEnabled()
					&& isTrivialAnalysis() == parameters.isTrivialAnalysis()
					&& getAnomaliesDetectionMethod().equals(parameters.getAnomaliesDetectionMethod())
					&& getAnomaliesDetectionMinDataItems() == parameters.getAnomaliesDetectionMinDataItems()
					&& isAnomaliesDetectionEnabled() == parameters.isAnomaliesDetectionEnabled();
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
	
	public Set<Set<XAttributeLiteral>> getFromToUnorderedPairs() {
		return fromToUnorderedPairs;
	}

	public void setFromToUnorderedPairs(Set<Set<XAttributeLiteral>> fromToUnorderedPairs) {
		this.fromToUnorderedPairs = fromToUnorderedPairs;
	}
	
	public XAttributeTimestamp getMeasurementAttr() {
		return measurementAttr;
	}

	public void setMeasurementAttr(XAttributeTimestamp measurementAttr) {
		this.measurementAttr = measurementAttr;
	}
	
	public int getSolverTimeout() {
		return solverTimeout;
	}

	public void setSolverTimeout(int solverTimeout) {
		this.solverTimeout = solverTimeout;
	}
	
	public boolean isTrivialAnalysis() {
		return isTrivialAnalysis;
	}

	public void setTrivialAnalysis(boolean isTrivialAnalysis) {
		this.isTrivialAnalysis = isTrivialAnalysis;
	}
	
	public boolean isFullAnalysisEnabled() {
		return fullAnalysisEnabled;
	}

	public void setFullAnalysisEnabled(boolean fullAnalysisEnabled) {
		this.fullAnalysisEnabled = fullAnalysisEnabled;
	}
	
	public AnomaliesDetectionMethod getAnomaliesDetectionMethod() {
		return anomaliesDetectionMethod;
	}

	public void setAnomaliesDetectionMethod(AnomaliesDetectionMethod anomaliesDetectionMethod) {
		this.anomaliesDetectionMethod = anomaliesDetectionMethod;
	}
	
	public int getAnomaliesDetectionMinDataItems() {
		return anomaliesDetectionMinDataItems;
	}

	public void setAnomaliesDetectionMinDataItems(int anomaliesDetectionMinDataItems) {
		this.anomaliesDetectionMinDataItems = anomaliesDetectionMinDataItems;
	}

	public boolean isAnomaliesDetectionEnabled() {
		return anomaliesDetectionEnabled;
	}

	public void setAnomaliesDetectionEnabled(boolean anomaliesDetectionEnabled) {
		this.anomaliesDetectionEnabled = anomaliesDetectionEnabled;
	}
}
