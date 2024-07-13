package org.apm4me.apm;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.apm4me.instrumentation.LogBytecodeInstrumenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

import static net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.RETRANSFORMATION;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class APM4MeAgent {

    private static Logger logger = LoggerFactory.getLogger(APM4MeAgent.class.getName());

    public static net.bytebuddy.agent.builder.AgentBuilder install() {
        ClassLoader loader = APM4MeAgent.class.getClassLoader();
        logger.info("Start instrumentation with retransformation");

        return new net.bytebuddy.agent.builder.AgentBuilder.Default()
                .disableClassFormatChanges()
                .with(RETRANSFORMATION)
                .with(AgentBuilder.RedefinitionStrategy.Listener.StreamWriting.toSystemError())
                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
                .ignore(none())
                .type(is(PrintStream.class))
                .transform((builder, typeDescription, classLoader, module) ->
                    builder.visit(Advice.to(LogBytecodeInstrumenter.class)
                            .on(ElementMatchers.named("println"))
                     )
                )
                ;
    }


}
