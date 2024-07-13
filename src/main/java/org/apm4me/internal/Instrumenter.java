package org.apm4me.internal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import java.time.Instant;

public class Instrumenter {
    public OpenTelemetry openTelemetry;
    public Tracer tracer;

    private static Instrumenter INSTRUMENT;

    Instrumenter() {
        tracer = openTelemetry.getTracerProvider().get("instrumentation-scope-name", "instrumentation-scope-version");
    }

    public static Instrumenter instrumenter() {
        if (INSTRUMENT == null) {
            INSTRUMENT = new Instrumenter();
        }
        return INSTRUMENT;
    }

}
