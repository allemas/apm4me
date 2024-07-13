package org.apm4me;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.apm4me.apm.APM4MeAgent;
import org.apm4me.instrumentation.JMXMetricsUtils;
import org.apm4me.instrumentation.Transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Premain {
    private static Logger logger = LoggerFactory.getLogger(Premain.class.getName());

    private static Instrumentation instrumentation;

    /**
     * JVM hook to dynamically load javaagent at runtime.
     * <p>
     * The agent class may have an agentmain method for use when the agent is
     * started after VM startup.
     *
     * @param args
     * @param inst
     * @throws Exception
     */
    public static void agentmain(String args, Instrumentation inst) {
        Thread jmxMetrics = new Thread(() -> {
            while (true) {
                long heapMemory = Runtime.getRuntime().availableProcessors();
                long heapMemory2 = Runtime.getRuntime().freeMemory();

                logger.info("AGENT APM : JMX processors available : " + heapMemory + " and freememory is : " + heapMemory2);
                JMXMetricsUtils.getManagementFactory();

                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });

        jmxMetrics.setDaemon(true);
        jmxMetrics.setName("memory-profiler");
        //  jmxMetrics.start();


        inst.addTransformer(new Transformer());

    }


    public static void premain(String agentArgs, Instrumentation inst) throws URISyntaxException, InterruptedException {

        logger.info("Start premain agent..");
        Resource resource = Resource.getDefault().toBuilder().put(ResourceAttributes.SERVICE_NAME, "APM4me")
                .put(ResourceAttributes.SERVICE_VERSION, "0.1.1")
                .build();
        ;

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(OtlpGrpcSpanExporter.builder().build()))
                .setResource(resource)
                .build();


        SdkMeterProvider sdkMeterProvider =
                SdkMeterProvider.builder()
                        .setResource(resource)
                        .registerMetricReader(
                                PeriodicMetricReader.builder(OtlpGrpcMetricExporter.getDefault())
                                        .setInterval(Duration.ofMillis(100))
                                        .build())
                        .build();

        SdkLoggerProvider loggerProvider = SdkLoggerProvider.builder()
                .addLogRecordProcessor(
                        BatchLogRecordProcessor.builder(
                                        OtlpGrpcLogRecordExporter.builder()
                                                .setEndpoint("http://localhost:4317")
                                                .build())
                                .build())
                .build();
        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setMeterProvider(sdkMeterProvider)
                .setTracerProvider(sdkTracerProvider)
                .setLoggerProvider(loggerProvider)
                .setPropagators(ContextPropagators.create(TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())))
                .buildAndRegisterGlobal();

        Thread.sleep(200);

        openTelemetry.getLogsBridge().get("dfdf").logRecordBuilder().setBody("COUUUUCOUUUUU : ) ").emit();


        Meter meter = openTelemetry.meterBuilder("dice-server")
                .setInstrumentationVersion("0.1.0")
                .build();

        meter.counterBuilder("d").build().add(1);
        meter.counterBuilder("d").build().add(1);
        meter.counterBuilder("d").build().add(1);
        meter.counterBuilder("d").build().add(1);


        Thread.sleep(200);

        APM4MeAgent.install()
                .installOn(inst);


        Thread loop = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);

                    Thread.interrupted();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
//        loop.start();


        Thread loopTrace = new Thread(() -> {
            Tracer tracer2 = GlobalOpenTelemetry.get().getTracerProvider().get("instrumentation-scope-name", "instrumentation-scope-version");
            while (true) {

                Thread.getAllStackTraces().forEach((thread, stackTraceElements) -> {
                    if (!thread.isDaemon()) { // exclude daemon threads

                        String payload = Arrays.stream(thread.getStackTrace()).map(Object::toString).collect(Collectors.joining(", "));
                        Span span2 = tracer2.spanBuilder(thread.getName()).startSpan();

                        span2.setStatus(StatusCode.ERROR);
                        span2.setAttribute("stacktraceElement", payload);
                        span2.end();


                        //    logger.info(thread.getName() + " " + Arrays.stream(thread.getStackTrace()).map(Object::toString).collect(Collectors.joining(", ")));
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
            }
        });

//        loopTrace.start();

        //       inst.addTransformer(new Transformer());




        /*
        var rs = new RecordingStream();
        rs.enable("jdk.JavaMonitorWait");
        rs.enable("jdk.JVMInformation");
        rs.onEvent("jdk.JavaMonitorWait", event -> {
            System.out.println(event.getStackTrace());
            System.out.println(event.getDuration());
       //     System.out.println(event.getEventType());

            System.out.println(event.<RecordedClass>getValue("monitorClass").getName());
        });

        rs.onEvent("jdk.JVMInformation", event -> {
            System.out.println(event.getDuration());
        });




        rs.startAsync();

         */

    }

    public static void initialize(Instrumentation ins, File javaAgent) {


    }


}
//https://github.com/parttimenerd/tiny-profiler/tree/main