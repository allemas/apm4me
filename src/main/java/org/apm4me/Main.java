package org.apm4me;

import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.Locale;
import java.util.Optional;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {
        System.out.println("???");
        System.out.println("???");
        System.out.println("???");
        System.out.println("???");
        System.out.println("???");

        String pid = args[0];
        String options = "";
        if (args.length > 1) options = args[1];
        loadAgent(pid, options);
    }


    static void loadAgent(String pid, String options) throws Exception {
        File agentFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        try {
            VirtualMachine vm = VirtualMachine.attach(pid);
            vm.loadAgent(agentFile.getPath(), "");
            System.out.println("Agent has been injected in running JVM running with process id " + pid);
            vm.detach();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}