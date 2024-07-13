package org.apm4me.apm;


import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class Premain {


    public void premain(String arg, Instrumentation instrumentation) {
//       inst.addTransformer(new Transformer());
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader(),
                        ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
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

}
//https://github.com/parttimenerd/tiny-profiler/tree/main