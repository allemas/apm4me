package org.apm4me.instrumentation;

import java.lang.instrument.Instrumentation;
import java.lang.management.*;
import java.util.List;

public class JMXMetricsUtils {


    public static void getManagementFactory() {
        var beans = ManagementFactory.getOperatingSystemMXBean();
        System.out.println("getName :  " + beans.getName());
        System.out.println("getSystemLoadAverage " + beans.getSystemLoadAverage());
        System.out.println("getAvailableProcessors :  " + beans.getAvailableProcessors());
        ManagementFactory.getMemoryManagerMXBeans().stream().forEach(map -> {
            System.out.println(map.getName());
            System.out.println(map.getName());
        });

        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        System.out.println("Thread count : " + threadMXBean.getThreadCount());

        final MemoryMXBean platformMXBean = ManagementFactory.getPlatformMXBean(MemoryMXBean.class);
        System.out.println("jvm.memory.heap : " + platformMXBean.getHeapMemoryUsage());
        System.out.println("jvm.memory.non_heap : " + platformMXBean.getNonHeapMemoryUsage());
        System.out.println("jvm.memory.non_heap : " + platformMXBean.getNonHeapMemoryUsage());


        List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        for (final MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            String type = memoryPoolMXBean.getType().name().toLowerCase();

            System.out.println(String.format("jvm.memory.%s.pool", type) + ":  [" + memoryPoolMXBean.getName() + "] : " + memoryPoolMXBean.getUsage());
        }

        final List<GarbageCollectorMXBean> garbageCollectorMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (final GarbageCollectorMXBean garbageCollectorMXBean : garbageCollectorMXBeans) {

            System.out.println(String.format("jvm.gc.%s.count", garbageCollectorMXBean.getName()) + " : " + garbageCollectorMXBean.getCollectionCount());
            System.out.println(String.format("jvm.gc.%s.time", garbageCollectorMXBean.getName()) + " : " + garbageCollectorMXBean.getCollectionTime());
            System.out.println(String.format("jvm.gc.%s.alloc", garbageCollectorMXBean.getName()) + " : " + garbageCollectorMXBean.getCollectionTime());
        }


        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] infos = bean.dumpAllThreads(true, true);

        for (ThreadInfo info : infos) {
            StackTraceElement[] elems = info.getStackTrace();
            for (StackTraceElement elm : elems) {
                System.out.println(elm);
            }
        }
    }



}
