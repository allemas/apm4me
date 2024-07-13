package org.apm4me.apm;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

public class AgentBuilder {

    public static net.bytebuddy.agent.builder.AgentBuilder install() {
        return new net.bytebuddy.agent.builder.AgentBuilder.Default()
//                .ignore(ElementMatchers.nameStartsWith("sun.reflect."))
                //              .ignore(ElementMatchers.nameStartsWith("jdk.internal."))
                //            .ignore(ElementMatchers.nameStartsWith("java.util."))
                //.ignore(ElementMatchers.nameStartsWith("io.opentelemetry"))
                //.disableClassFormatChanges()
//                .with(AgentBuilder.TypeStrategy.Default.DECORATE)
                //              .disableClassFormatChanges()
                .with(net.bytebuddy.agent.builder.AgentBuilder.Listener.StreamWriting.toSystemError())
                // .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .type(ElementMatchers.nameContains("AddMethod"))
                //  .type(ElementMatchers.any())
                .transform((builder, typeDescription, classLoader, module) ->
                        builder.method(ElementMatchers.any())
                                .intercept(Advice.to(TimerExecutor.class))
                )
                ;
    }

}
