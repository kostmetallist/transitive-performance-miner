package org.processmining.plugins.tpm.algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.tpm.model.ClusterTransitionIndicator;
import org.processmining.plugins.tpm.model.MarkedClusterNet;
import org.processmining.plugins.tpm.model.TraceSliceEntry;
import org.processmining.plugins.tpm.parameters.TransitivePerformanceMinerParameters;
import org.processmining.plugins.tpm.util.Pair;
import org.processmining.framework.plugin.PluginContext;

public class TransitivePerformanceMinerAlgorithm {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	private static final int SOLVER_TIMEOUT = 100;  // in seconds
	private static final String TRACE_NAME_ATTR = "concept:name";
	
	private static double calculateWeightForIJ(int i, int j) {
		return 1.0 / (j - i);
	}
	
	private Pair<Map<Integer, List<ClusterTransitionIndicator>>, Map<Integer, List<ClusterTransitionIndicator>>> getOutgoingIngoingMapping(
			List<ClusterTransitionIndicator> indicators) {
		
		Map<Integer, List<ClusterTransitionIndicator>> outgoing = new HashMap<>();
		Map<Integer, List<ClusterTransitionIndicator>> ingoing = new HashMap<>();
		
		for (ClusterTransitionIndicator cti : indicators) {
			
			int i = cti.getFromClusterNodeIndex();
			int j = cti.getToClusterNodeIndex();

			if (outgoing.containsKey(i)) {
				outgoing.get(i).add(cti);
			} else {
				List<ClusterTransitionIndicator> content = new ArrayList<>();
				content.add(cti);
				outgoing.put(i, content);
			}
			
			if (ingoing.containsKey(j)) {
				ingoing.get(j).add(cti);
			} else {
				List<ClusterTransitionIndicator> content = new ArrayList<>();
				content.add(cti);
				ingoing.put(j, content);
			}
		}

		LOGGER.debug("Outgoing:");
		for (int nodeIndex : outgoing.keySet()) {

			LOGGER.debug(String.format("  From %d:", nodeIndex));
			for (ClusterTransitionIndicator cti : outgoing.get(nodeIndex)) {

				LOGGER.debug(String.format("    %s:", cti));
			}
		}
		
		LOGGER.debug("Ingoing:");
		for (int nodeIndex : ingoing.keySet()) {

			LOGGER.debug(String.format("  To %d:", nodeIndex));
			for (ClusterTransitionIndicator cti : ingoing.get(nodeIndex)) {

				LOGGER.debug(String.format("    %s:", cti));
			}
		}

		return new Pair<>(outgoing, ingoing);
	}
	
	private List<ClusterTransitionIndicator> selectOptimalTransitions(List<ClusterTransitionIndicator> initialIndicators) {

		List<ClusterTransitionIndicator> filteredIndicators = new ArrayList<>();
		
		SolverFactory factory = new SolverFactoryLpSolve();
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, SOLVER_TIMEOUT);

		Problem problem = new Problem();
		Linear target = new Linear();

		for (ClusterTransitionIndicator cti : initialIndicators) {
			target.add(cti.getSimpleWeightChar().getValue(), cti);
		}

		problem.setObjective(target, OptType.MAX);

		Pair<Map<Integer, List<ClusterTransitionIndicator>>, Map<Integer, List<ClusterTransitionIndicator>>> outgoingAndIngoing =
				getOutgoingIngoingMapping(initialIndicators);

		for (Map.Entry<Integer, List<ClusterTransitionIndicator>> outgoingEntry : outgoingAndIngoing.get_1().entrySet()) {

			Linear constraint = new Linear();
			for (ClusterTransitionIndicator cti : outgoingEntry.getValue()) {
				constraint.add(1, cti);
			}

			problem.add(constraint, "<=", 1);
		}
		
		for (Map.Entry<Integer, List<ClusterTransitionIndicator>> ingoingEntry : outgoingAndIngoing.get_2().entrySet()) {

			Linear constraint = new Linear();
			for (ClusterTransitionIndicator cti : ingoingEntry.getValue()) {
				constraint.add(1, cti);
			}

			problem.add(constraint, "<=", 1);
		}

		// TODO check possibility of binding variable types in previous loops
		for (ClusterTransitionIndicator cti : initialIndicators) {
			problem.setVarType(cti, Boolean.class);
		}

		// The solver is supposed to be used only once for one problem
		Solver solver = factory.get();
		Result result = solver.solve(problem);
		
		for (ClusterTransitionIndicator cti : initialIndicators) {
			if (result.getPrimalValue(cti).intValue() == 1) {
				filteredIndicators.add(cti);
			}
		}

		LOGGER.info("Solver result: " + result);
		return filteredIndicators;
	}
	
	private List<ClusterTransitionIndicator> gatherTransitionIndicators(List<TraceSliceEntry> traceSliceEntries,
			XAttribute attrFrom, XAttribute attrTo) {
		
		List<ClusterTransitionIndicator> indicators = new ArrayList<>();
		Set<TraceSliceEntry> previousFromClusterEntries = new HashSet<>();

		for (TraceSliceEntry entry : traceSliceEntries) {
			if (entry.getGroupId().equals(attrFrom)) {
				previousFromClusterEntries.add(entry);

			} else if (entry.getGroupId().equals(attrTo)) {
				
				int j = entry.getIndex();
				for (TraceSliceEntry previous : previousFromClusterEntries) {
					
					int i = previous.getIndex();
					indicators.add(new ClusterTransitionIndicator(i, j, calculateWeightForIJ(i, j)));
				}
			}
		}
		
		return indicators;
	}

	public MarkedClusterNet apply(PluginContext context, XLog log,
			TransitivePerformanceMinerParameters parameters) {

		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
		String groupingAttrName = parameters.getGroupingAttr().getKey();

		LOGGER.info(String.format("Processing log with %d traces and %d events...",
				logInfo.getNumberOfTraces(), logInfo.getNumberOfEvents()));
		
		// TODO print out relative frequency of tracked attribute @code{groupingAttrName}

		Map<String, List<TraceSliceEntry>> slicesByTraces = new HashMap<>();
		ProgressBarBuilder pbb = new ProgressBarBuilder();
		pbb.setStyle(ProgressBarStyle.ASCII);

		for (XTrace trace : ProgressBar.wrap(log, pbb)) {

			List<TraceSliceEntry> matchedEventsWithPositions = new ArrayList<>();
			slicesByTraces.put(trace.getAttributes().get(TRACE_NAME_ATTR).toString(), matchedEventsWithPositions);

			for (int i = 0; i < trace.size(); i++) {
				
				XEvent event = trace.get(i);
				XAttributeMap eventAttributes = event.getAttributes();
				
				if (eventAttributes.containsKey(groupingAttrName) &&
						(eventAttributes.get(groupingAttrName).equals(parameters.getFromValue()) ||
						 eventAttributes.get(groupingAttrName).equals(parameters.getToValue()))) {

					matchedEventsWithPositions.add(new TraceSliceEntry(event, eventAttributes.get(groupingAttrName), i));
				}
			}
		}

		if (LOGGER.getLevel().isLessSpecificThan(Level.DEBUG)) {

			LOGGER.debug("Dumping collected events:");
			for (Map.Entry<String, List<TraceSliceEntry>> entry : slicesByTraces.entrySet()) {

				LOGGER.debug(String.format("  Trace %s", entry.getKey()));
				for (TraceSliceEntry tse : entry.getValue()) {
					LOGGER.debug(String.format("    %s", tse));
				}
			}
		}
		
		LOGGER.debug("Starting gathering and filtering transition indicators...");
		for (Map.Entry<String, List<TraceSliceEntry>> entry : slicesByTraces.entrySet()) {

			LOGGER.debug(String.format("  For trace %s", entry.getKey()));

			List<ClusterTransitionIndicator> initial = gatherTransitionIndicators(
					entry.getValue(), parameters.getFromValue(), parameters.getToValue());
			List<ClusterTransitionIndicator> filtered = selectOptimalTransitions(initial);

			LOGGER.debug("  Initial:");
			for (ClusterTransitionIndicator cti : initial) {
				LOGGER.debug(String.format("    %s", cti.toString()));
			}
			
			LOGGER.debug("  Filtered:");
			for (ClusterTransitionIndicator cti : filtered) {
				LOGGER.debug(String.format("    %s", cti.toString()));
			}
		}

		return new MarkedClusterNet();
	}
}
