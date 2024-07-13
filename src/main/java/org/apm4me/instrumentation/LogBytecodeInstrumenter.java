package org.apm4me.instrumentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;


import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

public class LogBytecodeInstrumenter {
    @Advice.OnMethodExit()
    public static void after(@Advice.AllArguments Object[] args) {
        System.out.print("---------------- \n" +
                "houston we ve got a print attempt ! \n" +
                args[0] + "\n" +
                "---------------- \n");
        try {
            URI uri = new URI("http://127.0.0.1:7280/api/v1/stackoverflow/ingest?commit=force");

            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String body = args[0].toString();
            String jsonInputString = "{\"body\": \"" + body + "\"}";


            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int statusCode = connection.getResponseCode();
            try (java.io.InputStream is = connection.getInputStream();
                 java.util.Scanner scanner = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
                String responseBody = scanner.useDelimiter("\\A").next();

                System.out.print("Status Code: " + statusCode);
                System.out.print("Response Body: " + responseBody);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
