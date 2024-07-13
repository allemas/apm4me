package org.apm4me.internal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import net.bytebuddy.asm.Advice;

import java.time.Instant;

public class Instrument {

    MetricsStore store;
    public OpenTelemetry openTelemetry;
    public Tracer tracer;

    Instrument(MetricsStore store) {
        this.store = store;
        tracer = openTelemetry.getTracerProvider().get("instrumentation-scope-name", "instrumentation-scope-version");
    }

    public MetricsStore getStore() {
        return this.store;
    }

    public void start(String name, long delay, long startTime) {
        Span span = tracer.spanBuilder(name).setStartTimestamp(Instant.ofEpochMilli(startTime)).startSpan();
        span.end(Instant.ofEpochMilli(delay));
    }

}
