package org.processmining.plugins.tpm;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;

import org.processmining.log.dialogs.ClassifierPanel;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class TransitivePerformanceMinerDialog extends JPanel {

	private static final long serialVersionUID = -1060558585823462314L;

	public TransitivePerformanceMinerDialog(XLog log, TransitivePerformanceMinerParameters parameters) {
		double size[][] = { { TableLayoutConstants.FILL }, { TableLayoutConstants.FILL } };
		setLayout(new TableLayout(size));
		List<XEventClassifier> availableClassifiers = new ArrayList<XEventClassifier>();
		availableClassifiers.addAll(log.getClassifiers());

		if (availableClassifiers.isEmpty()) {
			availableClassifiers.add(new XEventAndClassifier(new XEventNameClassifier(), new XEventLifeTransClassifier()));
			availableClassifiers.add(new XEventNameClassifier());
		}

		add(new ClassifierPanel(availableClassifiers, parameters), "0, 0");
	}
}
