package org.apm4me.internal;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class AgentLoader {

    public static void agentmain(String argument,
                                 Instrumentation instrumentation) throws UnmodifiableClassException {
        long heapSize = Runtime.getRuntime().totalMemory();

    }
}
