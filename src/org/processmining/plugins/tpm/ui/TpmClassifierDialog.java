package org.processmining.plugins.tpm.ui;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;

import org.processmining.log.dialogs.ClassifierPanel;
import org.processmining.log.utils.XUtils;
import org.processmining.plugins.tpm.parameters.TpmParameters;

public class TpmClassifierDialog extends TpmWizardStep {

	private static final long serialVersionUID = -1060558585823462314L;
	private final XLog log;
	private final TpmParameters parameters;

	public TpmClassifierDialog(XLog log, TpmParameters parameters) {

		this.log = log;
		this.parameters = parameters;
		initComponents();
	}
	
	protected void initComponents() {
		
		double size[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL } };
		setLayout(new TableLayout(size));
		List<XEventClassifier> availableClassifiers = new ArrayList<XEventClassifier>();
		availableClassifiers.addAll(XUtils.getStandardAndLogDefinedEventClassifiers(log));

		if (availableClassifiers.isEmpty()) {
			availableClassifiers.add(new XEventAndClassifier(new XEventNameClassifier(), new XEventLifeTransClassifier()));
			availableClassifiers.add(new XEventNameClassifier());
		}

		add(new ClassifierPanel(availableClassifiers, parameters), "0, 0");
	}
	
	public void fillSettings() {
		if (parameters.getClassifier() == null) {
			parameters.setClassifier(XUtils.getDefaultClassifier(log));
		}
	}
}
