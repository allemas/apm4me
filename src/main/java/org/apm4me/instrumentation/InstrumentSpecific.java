package org.apm4me.instrumentation;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import net.bytebuddy.asm.Advice;

import java.time.Instant;

public class InstrumentSpecific {
    private long startTime;


    @Advice.OnMethodEnter()
    static long onEnter() {
        return System.currentTimeMillis();
    }

    @Advice.OnMethodExit(onThrowable = Throwable.class)
    static void onExit(@Advice.Enter long startTime, @Advice.Origin("#t.#m") String method) throws InterruptedException {
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("OKAYYYYY????");
    }
}