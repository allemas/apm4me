package org.apm4me.apm;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.apm4me.Premain;
import org.apm4me.instrumentation.InstrumentMethodTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class APM4MeAgent {

    private static Logger logger = LoggerFactory.getLogger(APM4MeAgent.class.getName());

    public static net.bytebuddy.agent.builder.AgentBuilder install() {
        ClassLoader loader = APM4MeAgent.class.getClassLoader();
        logger.info("Start instrumentation");

        return new net.bytebuddy.agent.builder.AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                //  .with(net.bytebuddy.agent.builder.AgentBuilder.Listener.StreamWriting.toSystemError())
                .type(ElementMatchers.nameContains("AddMethod"))
                //  .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.method(ElementMatchers.any())
                                .intercept(Advice.to(InstrumentMethodTimer.class))
                )
                ;
    }

}
