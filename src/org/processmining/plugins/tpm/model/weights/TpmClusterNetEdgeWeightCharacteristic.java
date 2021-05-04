package org.processmining.plugins.tpm.model.weights;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.processmining.plugins.tpm.util.TpmPair;

public class TpmClusterNetEdgeWeightCharacteristic {

	private static final TimeUnit[] scale = {
		TimeUnit.NANOSECONDS,
		TimeUnit.MICROSECONDS,
		TimeUnit.MILLISECONDS,
		TimeUnit.SECONDS,
		TimeUnit.MINUTES,
		TimeUnit.HOURS,
		TimeUnit.DAYS
	};
	
	private static Map<TimeUnit, String> timeUnitsDesignations = ImmutableMap.<TimeUnit, String>builder()
			.put(TimeUnit.NANOSECONDS, "ns")
			.put(TimeUnit.MICROSECONDS, "us")
			.put(TimeUnit.MILLISECONDS, "ms")
			.put(TimeUnit.SECONDS, "s")
			.put(TimeUnit.MINUTES, "m")
			.put(TimeUnit.HOURS, "h")
			.put(TimeUnit.DAYS, "d")
			.build();

	private boolean isTemporal;
	// meaningful only in the case when {@code isTemporal} is true
	private TimeUnit timeUnit;

	private double minIntegralMetric;
	private double averageIntegralMetric;
	private double maxIntegralMetric;
	
	private TpmClusterNetEdgeWeightCharacteristic(
			boolean isTemporal,
			TimeUnit timeUnit,
			double minIntegralMetric, 
			double averageIntegralMetric,
			double maxIntegralMetric) {
		
		this.isTemporal = isTemporal;
		this.timeUnit = timeUnit;
		this.minIntegralMetric = minIntegralMetric;
		this.averageIntegralMetric = averageIntegralMetric;
		this.maxIntegralMetric = maxIntegralMetric;
	}
	
	public static TpmClusterNetEdgeWeightCharacteristic createOrdinaryCharacteristic(
			double minIntegralMetric, 
			double averageIntegralMetric,
			double maxIntegralMetric) {
		
		return new TpmClusterNetEdgeWeightCharacteristic(
				false,
				null,
				minIntegralMetric,
				averageIntegralMetric,
				maxIntegralMetric);
	}
	
	public static TpmClusterNetEdgeWeightCharacteristic createTemporalCharacteristic(
			TimeUnit timeUnit,
			double minIntegralMetric, 
			double averageIntegralMetric,
			double maxIntegralMetric) {
		
		return new TpmClusterNetEdgeWeightCharacteristic(
				true,
				timeUnit,
				minIntegralMetric,
				averageIntegralMetric,
				maxIntegralMetric);
	}
	
	public static TpmClusterNetEdgeWeightCharacteristic createTemporalCharacteristic(
			double minIntegralMetric, 
			double averageIntegralMetric,
			double maxIntegralMetric) {
		
		return new TpmClusterNetEdgeWeightCharacteristic(
				true,
				TimeUnit.MILLISECONDS,
				minIntegralMetric,
				averageIntegralMetric,
				maxIntegralMetric);
	}
	
	public double getMinIntegralMetric() {
		return minIntegralMetric;
	}

	public void setMinIntegralMetric(double minIntegralMetric) {
		this.minIntegralMetric = minIntegralMetric;
	}

	public double getAverageIntegralMetric() {
		return averageIntegralMetric;
	}

	public void setAverageIntegralMetric(double averageIntegralMetric) {
		this.averageIntegralMetric = averageIntegralMetric;
	}

	public double getMaxIntegralMetric() {
		return maxIntegralMetric;
	}

	public void setMaxIntegralMetric(double maxIntegralMetric) {
		this.maxIntegralMetric = maxIntegralMetric;
	}
	
	private static String getTemporalMeasurementRepresentation(
			TimeUnit unit,
			double value) {
		
		List<TpmPair<TimeUnit, Long>> result = new ArrayList<>();
		long rounded = (long) value;
		boolean startedCollecting = false;

		for (TimeUnit tUnit : scale) {
			
			if (tUnit.equals(unit)) {
				startedCollecting = true;
			}
			
			if (startedCollecting) {
				switch (tUnit) {
				case DAYS:
					result.add(0, new TpmPair<>(TimeUnit.DAYS, unit.toDays(rounded)));
					break;
				case HOURS:
					result.add(0, new TpmPair<>(TimeUnit.HOURS, unit.toHours(rounded)));
					break;
				case MINUTES:
					result.add(0, new TpmPair<>(TimeUnit.MINUTES, unit.toMinutes(rounded)));
					break;
				case SECONDS:
					result.add(0, new TpmPair<>(TimeUnit.SECONDS, unit.toSeconds(rounded)));
					break;
				case MILLISECONDS:
					result.add(0, new TpmPair<>(TimeUnit.MILLISECONDS, unit.toMillis(rounded)));
					break;
				case MICROSECONDS:
					result.add(0, new TpmPair<>(TimeUnit.MICROSECONDS, unit.toMicros(rounded)));
					break;
				case NANOSECONDS:
					result.add(0, new TpmPair<>(TimeUnit.NANOSECONDS, unit.toNanos(rounded)));
					break;
				}
			}
		}
		
		return result.stream()
				.filter(x -> x.get_2() != 0)
				.limit(3)
				.map(x -> String.format("%d%s", x.get_2(), timeUnitsDesignations.get(x.get_1())))
				.collect(Collectors.joining(" "));
	}

	@Override
	public String toString() {
		
		if (isTemporal) {
			String
				minRepr = getTemporalMeasurementRepresentation(timeUnit, minIntegralMetric),
				averageRepr = getTemporalMeasurementRepresentation(timeUnit, averageIntegralMetric),
				maxRepr = getTemporalMeasurementRepresentation(timeUnit, maxIntegralMetric);
				
			return String.format("%s;%n%s;%n%s",
					minRepr, averageRepr, maxRepr);

		} else {
			return String.format("%.2f;%n%.2f;%n%.2f",
					minIntegralMetric, averageIntegralMetric, maxIntegralMetric);
		}
	}
}
