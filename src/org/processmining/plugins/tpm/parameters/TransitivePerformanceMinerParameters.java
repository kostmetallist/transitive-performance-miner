package org.processmining.plugins.tpm.parameters;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.log.parameters.ClassifierParameter;
import org.processmining.log.utils.XUtils;

public class TransitivePerformanceMinerParameters extends PluginParametersImpl implements ClassifierParameter {

	private XEventClassifier classifier;

	public TransitivePerformanceMinerParameters(XLog log) {

		super();
		setClassifier(XUtils.getDefaultClassifier(log));
		setTryConnections(true);
	}

	public TransitivePerformanceMinerParameters(TransitivePerformanceMinerParameters parameters) {

		super(parameters);
		setClassifier(parameters.getClassifier());
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	public XEventClassifier getClassifier() {
		return classifier;
	}

	public boolean equals(Object object) {
		if (object instanceof TransitivePerformanceMinerParameters) {
			TransitivePerformanceMinerParameters parameters = (TransitivePerformanceMinerParameters) object;
			return super.equals(parameters) && getClassifier().equals(parameters.getClassifier());
		}
		return false;
	}
}
