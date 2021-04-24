package org.processmining.plugins.tpm.algorithms;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;
import org.processmining.plugins.tpm.util.Pair;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.tpm.MarkedClusterNet;

public class TransitivePerformanceMinerAlgorithm {

	public MarkedClusterNet apply(PluginContext context, XLog log,
			TransitivePerformanceMinerParameters parameters) {

		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
		List<Pair<XEvent, Integer>> matchedEventsWithPositions = new ArrayList<>();
		
		for (XTrace trace : log) {
			for (int i = 0; i < trace.size(); i++) {
				
				XEvent event = trace.get(i);
				XAttributeMap eventAttributes = event.getAttributes();
				
				if (eventAttributes.containsKey(parameters.getGroupingAttr().getKey())) {
					matchedEventsWithPositions.add(new Pair<>(event, i));
				}
			}
		}
		
		return new MarkedClusterNet();
	}
}

