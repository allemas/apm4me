package org.apm4me;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

public class AgentLoader {

    public static void agentmain(String argument,
                                 Instrumentation instrumentation) throws UnmodifiableClassException {

        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(Module module,
                                    ClassLoader loader,
                                    String name,
                                    Class<?> typeIfLoaded,
                                    ProtectionDomain domain,
                                    byte[] buffer) {
                if (typeIfLoaded == null) {
                    System.out.println("Class was loaded: " + name);
                } else {
                    System.out.println("Class was re-loaded: " + name);
                }
                return null;
            }
        }, true);
        instrumentation.retransformClasses(
                instrumentation.getAllLoadedClasses());
    }
}
