package org.apm4me.apm;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
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

import java.time.Instant;

public class InstrumentMethodTimer {
    private long startTime;

    @Advice.OnMethodEnter
    static long onEnter() {
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onExit(@Advice.Enter long startTime, @Advice.Origin("#t.#m") String method) throws InterruptedException {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("COUCOUUUUU " + method);
        Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "APM4me")
                .put(ResourceAttributes.SERVICE_VERSION, "0.1.0")
                .build();
        ;

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(OtlpGrpcSpanExporter.builder().build()))
                .setResource(resource)
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
                .build();


        Tracer tracer = openTelemetry.getTracerProvider().get("instrumentation-scope-name", "instrumentation-scope-version");
        Span s = tracer.spanBuilder(method).startSpan();
        s.setAttribute("function-name", method);
        s.addEvent("RAISED");
        s.setStatus(StatusCode.OK);
        s.end(Instant.ofEpochMilli(duration));

        Thread.sleep(100);

    }
}