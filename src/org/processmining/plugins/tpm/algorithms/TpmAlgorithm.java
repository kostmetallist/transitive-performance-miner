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
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import org.processmining.log.utils.XUtils;
import org.processmining.plugins.tpm.model.TpmClusterTransitionIndicator;
import org.processmining.plugins.tpm.model.TpmMarkedClusterNet;
import org.processmining.plugins.tpm.model.TpmTraceEntry;
import org.processmining.plugins.tpm.model.weights.TpmClusterNetEdgeWeightCharacteristic;
import org.processmining.plugins.tpm.parameters.TpmParameters;
import org.processmining.plugins.tpm.util.TpmPair;
import org.processmining.framework.plugin.PluginContext;

public class TpmAlgorithm {
	
	private static final Logger LOGGER = LogManager.getRootLogger();
	// TODO move to parameters
	private static final int SOLVER_TIMEOUT = 100;  // in seconds
	
	private static double calculateWeightForIJ(int i, int j) {
		return 1.0 / (j - i);
	}
	
	private static double calculateSliceMeasurement(XEvent from, XEvent to, XAttributeTimestamp measurable) {

		XAttributeMap fromAttributes = from.getAttributes(),
					  toAttributes = to.getAttributes();
		
		if (!fromAttributes.containsKey(measurable.getKey()) || !toAttributes.containsKey(measurable.getKey())) {
			LOGGER.error(String.format("Cannot calculate measurement for unexisting attribute %s", measurable.getKey()));
			return 0;
		}
		
		long fromTs = XUtils.getTimestamp(from).getTime();
		long toTs = XUtils.getTimestamp(to).getTime();

		return toTs - fromTs;
	}
	
	private TpmPair<Map<Integer, List<TpmClusterTransitionIndicator>>,
					Map<Integer, List<TpmClusterTransitionIndicator>>> getOutgoingIngoingMapping(
			List<TpmClusterTransitionIndicator> indicators) {
		
		Map<Integer, List<TpmClusterTransitionIndicator>> outgoing = new HashMap<>();
		Map<Integer, List<TpmClusterTransitionIndicator>> ingoing = new HashMap<>();
		
		for (TpmClusterTransitionIndicator cti : indicators) {
			
			int i = cti.getFromClusterNodeIndex();
			int j = cti.getToClusterNodeIndex();

			if (outgoing.containsKey(i)) {
				outgoing.get(i).add(cti);
			} else {
				List<TpmClusterTransitionIndicator> content = new ArrayList<>();
				content.add(cti);
				outgoing.put(i, content);
			}
			
			if (ingoing.containsKey(j)) {
				ingoing.get(j).add(cti);
			} else {
				List<TpmClusterTransitionIndicator> content = new ArrayList<>();
				content.add(cti);
				ingoing.put(j, content);
			}
		}

		if (LOGGER.getLevel().isLessSpecificThan(Level.DEBUG)) {

			LOGGER.debug("Outgoing:");
			for (int nodeIndex : outgoing.keySet()) {

				LOGGER.debug(String.format("  From %d:", nodeIndex));
				for (TpmClusterTransitionIndicator cti : outgoing.get(nodeIndex)) {

					LOGGER.debug(String.format("    %s:", cti));
				}
			}
			
			LOGGER.debug("Ingoing:");
			for (int nodeIndex : ingoing.keySet()) {

				LOGGER.debug(String.format("  To %d:", nodeIndex));
				for (TpmClusterTransitionIndicator cti : ingoing.get(nodeIndex)) {

					LOGGER.debug(String.format("    %s:", cti));
				}
			}
		}

		return new TpmPair<>(outgoing, ingoing);
	}
	
	private List<TpmClusterTransitionIndicator> selectOptimalTransitions(
			List<TpmClusterTransitionIndicator> initialIndicators) {

		List<TpmClusterTransitionIndicator> filteredIndicators = new ArrayList<>();
		
		SolverFactory factory = new SolverFactoryLpSolve();
		factory.setParameter(Solver.VERBOSE, 0);
		factory.setParameter(Solver.TIMEOUT, SOLVER_TIMEOUT);

		Linear target = new Linear();
		Problem problem = new Problem();

		for (TpmClusterTransitionIndicator cti : initialIndicators) {
			target.add(cti.getSimpleWeightChar().getValue(), cti);
			problem.setVarType(cti, Boolean.class);
		}

		problem.setObjective(target, OptType.MAX);

		TpmPair<Map<Integer, List<TpmClusterTransitionIndicator>>, Map<Integer, List<TpmClusterTransitionIndicator>>> outgoingAndIngoing =
				getOutgoingIngoingMapping(initialIndicators);

		for (Map.Entry<Integer, List<TpmClusterTransitionIndicator>> outgoingEntry : outgoingAndIngoing.get_1().entrySet()) {

			Linear constraint = new Linear();
			for (TpmClusterTransitionIndicator cti : outgoingEntry.getValue()) {
				constraint.add(1, cti);
			}

			problem.add(constraint, "<=", 1);
		}
		
		for (Map.Entry<Integer, List<TpmClusterTransitionIndicator>> ingoingEntry : outgoingAndIngoing.get_2().entrySet()) {

			Linear constraint = new Linear();
			for (TpmClusterTransitionIndicator cti : ingoingEntry.getValue()) {
				constraint.add(1, cti);
			}

			problem.add(constraint, "<=", 1);
		}

		// The solver is supposed to be used only once per problem
		Solver solver = factory.get();
		Result result = solver.solve(problem);
		LOGGER.debug("Solver result: " + result);
		
		for (TpmClusterTransitionIndicator cti : initialIndicators) {
			if (result.getPrimalValue(cti).intValue() == 1) {
				filteredIndicators.add(cti);
			}
		}

		return filteredIndicators;
	}
	
	private List<TpmClusterTransitionIndicator> gatherTransitionIndicators(
			List<TpmTraceEntry> traceSliceEntries,
			XAttribute attrFrom,
			XAttribute attrTo) {
		
		List<TpmClusterTransitionIndicator> indicators = new ArrayList<>();
		Set<TpmTraceEntry> previousFromClusterEntries = new HashSet<>();

		for (TpmTraceEntry entry : traceSliceEntries) {
			if (entry.getGroupId().equals(attrFrom)) {
				previousFromClusterEntries.add(entry);

			} else if (entry.getGroupId().equals(attrTo)) {
				
				int j = entry.getIndex();
				for (TpmTraceEntry previous : previousFromClusterEntries) {
					
					int i = previous.getIndex();
					indicators.add(new TpmClusterTransitionIndicator(i, j, calculateWeightForIJ(i, j)));
				}
			}
		}
		
		return indicators;
	}

	public TpmMarkedClusterNet buildMCN(
			PluginContext context,
			XLog log,
			TpmParameters parameters) {

		XLogInfo logInfo = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
		String groupingAttrName = parameters.getGroupingAttr().getKey();

		LOGGER.info(String.format("Processing log with %d traces and %d events...",
				logInfo.getNumberOfTraces(), logInfo.getNumberOfEvents()));

		Map<String, List<TpmTraceEntry>> tracesWithMatchedEvents = new HashMap<>();
		Map<Integer, Integer> globalToLocalIndices = new HashMap<>();
//		ProgressBarBuilder pbb = new ProgressBarBuilder();
//		pbb.setStyle(ProgressBarStyle.ASCII);
 
//		if (parameters.isFullAnalysisEnabled()) {
//			// TODO
//
//		// Intra-cluster analysis
//		} else {
//			
//		}

		for (XTrace trace : log) {

			List<TpmTraceEntry> matchedEventsWithPositions = new ArrayList<>();
			tracesWithMatchedEvents.put(XUtils.getConceptName(trace), matchedEventsWithPositions);

			for (int i = 0, j = 0; i < trace.size(); i++) {
				
				XEvent event = trace.get(i);
				XAttributeMap eventAttributes = event.getAttributes();
				
				if (eventAttributes.containsKey(groupingAttrName) &&
						(eventAttributes.get(groupingAttrName).equals(parameters.getFromValue()) ||
						 eventAttributes.get(groupingAttrName).equals(parameters.getToValue()))) {

					matchedEventsWithPositions.add(new TpmTraceEntry(event, eventAttributes.get(groupingAttrName), i));
					globalToLocalIndices.put(i, j++);
				}
			}
		}

		if (LOGGER.getLevel().isLessSpecificThan(Level.DEBUG)) {

			LOGGER.debug("Dumping collected events:");
			for (Map.Entry<String, List<TpmTraceEntry>> entry : tracesWithMatchedEvents.entrySet()) {

				LOGGER.debug(String.format("For trace %s", entry.getKey()));
				for (TpmTraceEntry traceEntry : entry.getValue()) {
					LOGGER.debug(String.format("  %s", traceEntry));
				}
			}
		}
		
		Map<String, Double> estimationsByTraces = new HashMap<>();
		
		LOGGER.info("Starting gathering and filtering transition indicators...");
		for (Map.Entry<String, List<TpmTraceEntry>> entry : tracesWithMatchedEvents.entrySet()) {

			LOGGER.debug(String.format("Processing trace %s", entry.getKey()));

			List<TpmTraceEntry> traceEntries = entry.getValue();
			List<TpmClusterTransitionIndicator> initial = gatherTransitionIndicators(
					traceEntries, parameters.getFromValue(), parameters.getToValue());
			List<TpmClusterTransitionIndicator> filtered = selectOptimalTransitions(initial);
			
			if (LOGGER.getLevel().isLessSpecificThan(Level.DEBUG)) {

				LOGGER.debug("  Initial:");
				if (!initial.isEmpty()) {
					for (TpmClusterTransitionIndicator cti : initial) {
						LOGGER.debug(String.format("    %s", cti.toString()));
					}
				} else {
					LOGGER.debug("    <EMPTY>");
				}
				
				LOGGER.debug("  Filtered:");
				if (!filtered.isEmpty()) {
					for (TpmClusterTransitionIndicator cti : filtered) {
						LOGGER.debug(String.format("    %s", cti.toString()));
					}
				} else {
					LOGGER.debug("    <EMPTY>");
				}
			}

			for (TpmClusterTransitionIndicator cti : filtered) {

				estimationsByTraces.put(entry.getKey(), calculateSliceMeasurement(
						traceEntries.get(globalToLocalIndices.get(cti.getFromClusterNodeIndex())).getEvent(),
						traceEntries.get(globalToLocalIndices.get(cti.getToClusterNodeIndex())).getEvent(),
						parameters.getMeasurementAttr()) / filtered.size());
			}
		}
		
		if (LOGGER.getLevel().isLessSpecificThan(Level.DEBUG)) {

			LOGGER.debug("Calculated estimations:");
			if (!estimationsByTraces.isEmpty()) {
				for (Map.Entry<String, Double> item : estimationsByTraces.entrySet()) {
					LOGGER.debug(String.format("  %s: %f", item.getKey(), item.getValue()));
				}

			} else {
				LOGGER.debug("  <EMPTY>");
			}
		}

		TpmMarkedClusterNet mcn = new TpmMarkedClusterNet();
		mcn.addCluster(parameters.getFromValue().getValue());
		mcn.addCluster(parameters.getToValue().getValue());
		
		TpmClusterNetEdgeWeightCharacteristic wChar = TpmClusterNetEdgeWeightCharacteristic.createTemporalCharacteristic(
				estimationsByTraces.values().stream().mapToDouble(x -> x).min().getAsDouble(),
				estimationsByTraces.values().stream().mapToDouble(x -> x).average().getAsDouble(),
				estimationsByTraces.values().stream().mapToDouble(x -> x).max().getAsDouble()
		);
		mcn.addTransition(parameters.getFromValue().getValue(), parameters.getToValue().getValue(), wChar);

		return mcn;
	}
}
