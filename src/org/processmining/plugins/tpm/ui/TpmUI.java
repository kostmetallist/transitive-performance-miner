package org.processmining.plugins.tpm.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.tpm.parameters.TpmParameters;

public class TpmUI {
	
	private enum WizardDirection {
		PREV,
		NEXT
	}
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	private TpmParameters parameters;
	private UIPluginContext context;

	private Map<Integer, TpmWizardStep> wizardSteps;
	private int wizardStepsNumber;
	private int currentStep;

	public TpmUI(UIPluginContext context, XLog log) {

		this.parameters = new TpmParameters();
		this.context = context;

		this.wizardSteps = new HashMap<>();
		this.wizardSteps.put(0, new TpmClassifierDialog(log, parameters));
		this.wizardSteps.put(1, new TpmClusterizationAndAnomaliesDialog(log, parameters));
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
	
	public TpmParameters gatherParameters() {

		InteractionResult result = InteractionResult.NEXT;
		
		while (true) {

			LOGGER.debug(String.format("Current wizard step: %d", currentStep));

			TpmWizardStep ws = wizardSteps.get(currentStep);
			result = context.showWizard(String.format("Transitive Performance Miner configuration step %d", currentStep + 1),
					currentStep == 0, currentStep == wizardStepsNumber - 1, ws);
			ws.fillSettings();
			
			switch (result) {
				case NEXT:
					go(WizardDirection.NEXT);
					break;
	
				case PREV:
					go(WizardDirection.PREV);
					break;
	
				case FINISHED:
					LOGGER.info(String.format("parameters.classifier                : %s", parameters.getClassifier()));
					LOGGER.info(String.format("parameters.groupingAttr              : %s", parameters.getGroupingAttr().getKey()));
					LOGGER.info(String.format("parameters.fullAnalysisEnabled       : %s", parameters.isFullAnalysisEnabled()));
					LOGGER.info(String.format("parameters.fromValue                 : %s", parameters.getFromValue()));
					LOGGER.info(String.format("parameters.toValue                   : %s", parameters.getToValue()));
					LOGGER.info(String.format("parameters.measurementAttr           : %s", parameters.getMeasurementAttr().getKey()));
					LOGGER.info(String.format("parameters.solverTimeout             : %s", parameters.getSolverTimeout()));
					LOGGER.info(String.format("parameters.anomaliesDetectionEnabled : %s", parameters.isAnomaliesDetectionEnabled()));
					LOGGER.info(String.format("parameters.anomaliesDetectionMethod  : %s", parameters.getAnomaliesDetectionMethod().name()));
	
					return parameters;
	
				default:
					context.getFutureResult(0).cancel(true);
					context.getFutureResult(1).cancel(true);
					return null;
			}
		}
	}
}
