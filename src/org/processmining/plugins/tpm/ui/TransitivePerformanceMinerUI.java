package org.processmining.plugins.tpm.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;

public class TransitivePerformanceMinerUI {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	
	private TransitivePerformanceMinerParameters parameters;
	private UIPluginContext context;

	private Map<Integer, WizardStep> wizardSteps;
	private int wizardStepsNumber;
	private int currentStep;

	public TransitivePerformanceMinerUI(UIPluginContext context, XLog log) {

		this.parameters = new TransitivePerformanceMinerParameters();
		this.context = context;

		this.wizardSteps = new HashMap<>();
		this.wizardSteps.put(0, new TransitivePerformanceMinerClassifierDialog(log, parameters));
		this.wizardSteps.put(1, new TransitivePerformanceMinerClusterizationAndFilteringDialog(log, parameters));
		this.wizardStepsNumber = wizardSteps.size();
		this.currentStep = 0;
	}
	
	// TODO replace position with enum
	private int go(int direction) {

		if (currentStep == 0 && direction == -1 ||
				currentStep == (wizardStepsNumber - 1) && direction == 1) {

			return currentStep;
		}

		currentStep += direction;
		return currentStep;
	}
	
	public TransitivePerformanceMinerParameters gatherParameters() {

		InteractionResult result = InteractionResult.NEXT;
		
		while (true) {
//			if (currentStep < 0) {
//				currentStep = 0;
//			}
//			if (currentStep >= wizardStepsNumber) {
//				currentStep = wizardStepsNumber - 1;
//			}

			LOGGER.info("Current step: " + currentStep);

			WizardStep ws = wizardSteps.get(currentStep);
			result = context.showWizard(String.format("Transitive Performance Miner configuration step %d", currentStep),
					currentStep == 0, currentStep == wizardStepsNumber - 1, ws);
			
			switch (result) {

			case NEXT:
				go(1);
				break;

			case PREV:
				go(-1);
				break;

			case FINISHED:
				ws.fillSettings();
//				if(!checkGEDScores(activitySet)){
//					JOptionPane.showMessageDialog(new JFrame(), "<HTML>Generic Edit Distance has been chosen as the distance metric type. <BR>There is a problem in scoring files provided as input.<BR> Either the files are missing/corrupt <BR>or the content of the file doesn't comply with the format <BR>or the set of activities in the file does not match with the activities in the log file <BR></HTML>");
//				}
				return parameters;

			default:
				context.getFutureResult(0).cancel(true);
				context.getFutureResult(1).cancel(true);
				return null;
			}
		}
	}
}
