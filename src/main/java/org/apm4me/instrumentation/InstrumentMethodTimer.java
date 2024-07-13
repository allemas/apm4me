package org.apm4me.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
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
import org.apm4me.internal.Instrumenter;
import org.w3c.dom.css.Counter;

import java.time.Instant;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

public class InstrumentMethodTimer {
    private long startTime;


    @Advice.OnMethodEnter()
    static long onEnter() {
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onExit(@Advice.Enter long startTime, @Advice.Origin("#t.#m") String method) throws InterruptedException {
        long duration = System.currentTimeMillis() - startTime;

        Tracer tracer = GlobalOpenTelemetry.getTracerProvider().get("instrumentation-scope-name", "instrumentation-scope-version");
        Span s = tracer.spanBuilder(method).startSpan();
        s.setAttribute("function-name", method);
        s.addEvent("RAISED");
        s.setStatus(StatusCode.OK);
        s.end(Instant.ofEpochMilli(duration));

        Thread.sleep(200);

    }
}