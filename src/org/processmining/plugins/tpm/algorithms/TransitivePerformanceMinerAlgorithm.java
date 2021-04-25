package org.processmining.plugins.tpm.algorithms;

import java.util.ArrayList;
import java.util.List;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.tpm.model.MarkedClusterNet;
import org.processmining.plugins.tpm.model.TraceSliceEntry;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;
import org.processmining.framework.plugin.PluginContext;

public class TransitivePerformanceMinerAlgorithm {
	
	private static final Logger logger = LogManager.getRootLogger();

	public MarkedClusterNet apply(PluginContext context, XLog log,
			TransitivePerformanceMinerParameters parameters) {

		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
		String groupingAttrName = parameters.getGroupingAttr().getKey();

		logger.info(String.format("Processing log with %d traces and %d events...",
				logInfo.getNumberOfTraces(), logInfo.getNumberOfEvents()));
		
		// TODO print out relative frequency of tracked attribute @code{groupingAttrName}
		
		List<TraceSliceEntry> matchedEventsWithPositions = new ArrayList<>();
		ProgressBarBuilder pbb = new ProgressBarBuilder();
		pbb.setStyle(ProgressBarStyle.ASCII);
		
		// FIXME correct ASCII impl for progress bar
		for (XTrace trace : pbb.build().wrap(log, "Primary traces scan...")) {
			for (int i = 0; i < trace.size(); i++) {
				
				XEvent event = trace.get(i);
				XAttributeMap eventAttributes = event.getAttributes();
				
				if (eventAttributes.containsKey(groupingAttrName) &&
						(eventAttributes.get(groupingAttrName).equals(parameters.getFromValue()) ||
						 eventAttributes.get(groupingAttrName).equals(parameters.getToValue()))) {

					matchedEventsWithPositions.add(new TraceSliceEntry(event, eventAttributes.get(groupingAttrName).toString(), i));
				}
			}
		}

		logger.debug("Dumping collected events:");
		for (TraceSliceEntry entry : matchedEventsWithPositions) {
			logger.debug(entry);
		}

		return new MarkedClusterNet();
	}
}

