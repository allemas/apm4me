package org.apm4me.instrumentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {

    private static Logger LOGGER = LoggerFactory.getLogger(Transformer.class.getName());

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        byte[] byteCode = classfileBuffer;





        return byteCode;
    }

}
