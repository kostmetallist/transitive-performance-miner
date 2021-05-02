package org.processmining.plugins.tpm.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.tpm.parameters.Parameters;

public class UI {
	
	private enum WizardDirection {
		PREV,
		NEXT
	}
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	private Parameters parameters;
	private UIPluginContext context;

	private Map<Integer, WizardStep> wizardSteps;
	private int wizardStepsNumber;
	private int currentStep;

	public UI(UIPluginContext context, XLog log) {

		this.parameters = new Parameters();
		this.context = context;

		this.wizardSteps = new HashMap<>();
		this.wizardSteps.put(0, new ClassifierDialog(log, parameters));
		this.wizardSteps.put(1, new ClusterizationAndAnomaliesDialog(log, parameters));
		this.wizardStepsNumber = wizardSteps.size();
		this.currentStep = 0;
	}

	private int go(WizardDirection direction) {

		if (currentStep == 0 && direction == WizardDirection.PREV ||
				currentStep == (wizardStepsNumber - 1) && direction == WizardDirection.NEXT) {

			return currentStep;
		}

		currentStep += (direction == WizardDirection.NEXT)? 1: -1;
		return currentStep;
	}
	
	public Parameters gatherParameters() {

		InteractionResult result = InteractionResult.NEXT;
		
		while (true) {

			LOGGER.debug(String.format("Current wizard step: %d", currentStep));

			WizardStep ws = wizardSteps.get(currentStep);
			result = context.showWizard(String.format("Transitive Performance Miner configuration step %d", currentStep + 1),
					currentStep == 0, currentStep == wizardStepsNumber - 1, ws);
			
			switch (result) {

			case NEXT:
				go(WizardDirection.NEXT);
				break;

			case PREV:
				go(WizardDirection.PREV);
				break;

			case FINISHED:
				ws.fillSettings();
				LOGGER.info(String.format("parameters.classifier      : %s", parameters.getClassifier()));
				LOGGER.info(String.format("parameters.groupingAttr    : %s", parameters.getGroupingAttr().getKey()));
				LOGGER.info(String.format("parameters.fromValue       : %s", parameters.getFromValue()));
				LOGGER.info(String.format("parameters.toValue         : %s", parameters.getToValue()));
				LOGGER.info(String.format("parameters.measurementAttr : %s", parameters.getMeasurementAttr().getKey()));

				return parameters;

			default:
				context.getFutureResult(0).cancel(true);
				context.getFutureResult(1).cancel(true);
				return null;
			}
		}
	}
}
