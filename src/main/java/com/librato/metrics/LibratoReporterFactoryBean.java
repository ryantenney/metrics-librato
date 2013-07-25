package com.librato.metrics;

import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Clock;
import com.codahale.metrics.MetricFilter;
import com.ryantenney.metrics.spring.reporter.AbstractScheduledReporterFactoryBean;

public class LibratoReporterFactoryBean extends AbstractScheduledReporterFactoryBean<LibratoReporter> {

	// Required
	public static final String USERNAME = "username";
	public static final String TOKEN = "token";
	public static final String SOURCE = "source";
	public static final String PERIOD = "period";

	// Optional
	public static final String API_URL = "api-url";
	public static final String TIMEOUT = "timeout";
	public static final String NAME = "name";
	public static final String SANITIZER_REF = "sanitizer-ref";
	public static final String CLOCK_REF = "clock-ref";
	public static final String DURATION_UNIT = "duration-unit";
	public static final String RATE_UNIT = "rate-unit";
	public static final String FILTER_PATTERN = "filter";
	public static final String FILTER_REF = "filter-ref";

	@Override
	public Class<LibratoReporter> getObjectType() {
		return LibratoReporter.class;
	}

	@Override
	protected LibratoReporter createInstance() {
		final String username = getProperty(USERNAME);
		final String token = getProperty(TOKEN);
		final String source = getProperty(SOURCE);

		final LibratoReporter.Builder reporter = LibratoReporter.builder(getMetricRegistry(), username, token, source);

		if (hasProperty(API_URL)) {
			reporter.setApiUrl(getProperty(API_URL));
		}

		if (hasProperty(TIMEOUT)) {
			reporter.setTimeout(convertDurationString(getProperty(TIMEOUT)), TimeUnit.NANOSECONDS);
		}

		if (hasProperty(NAME)) {
			reporter.setName(getProperty(NAME));
		}

		if (hasProperty(SANITIZER_REF)) {
			reporter.setSanitizer(getPropertyRef(SANITIZER_REF, APIUtil.Sanitizer.class));
		}

		if (hasProperty(DURATION_UNIT)) {
			reporter.setDurationUnit(getProperty(DURATION_UNIT, TimeUnit.class));
		}

		if (hasProperty(RATE_UNIT)) {
			reporter.setRateUnit(getProperty(RATE_UNIT, TimeUnit.class));
		}

		if (hasProperty(CLOCK_REF)) {
			reporter.setClock(getPropertyRef(CLOCK_REF, Clock.class));
		}

		if (hasProperty(FILTER_PATTERN)) {
			reporter.setFilter(metricFilterPattern(getProperty(FILTER_PATTERN)));
		}
		else if (hasProperty(FILTER_REF)) {
			reporter.setFilter(getPropertyRef(FILTER_REF, MetricFilter.class));
		}

		return reporter.build();
	}

	protected long getPeriod() {
		return convertDurationString(getProperty(PERIOD));
	}

}
