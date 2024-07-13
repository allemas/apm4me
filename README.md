# APM4ME

**Never use this in production**

APM 4 me is an experiment on how APM works in JAVA. In this project I'm looking for understand how bytecode instrumentation works and how this is interpreted in the JVM.

I started with a small project to understand how to load an APM into a java application and discover all the steps involved in linking and instrumentation.

I then went on to learn how to export metrics into a java application. I'm not quite finished yet

In parallel, I made a few attempts to instrument a java class with ByteBuddy and learned about all the concepts that exist around instrumentation via bytecode. I've managed to export traces of method calls in an application, and to re-transform the classloader and classes to integrate the opentelemetry SDK into the application in which the APM is linked.

This is not the first project I've done on APMs and JVM Internal. It follows on from my first discoveries here: https://github.com/allemas/thread-park/blob/master/DISCOVERED.md

## Concepts learned
- premain / agentmain : OK
- Understand bytebuddy and how it's works: OK
- Export basic metrics from the JVM : src/main/java/org/apm4me/instrumentation/JMXMetricsUtils.java
- Linking OTEL SDK by retransformation instrumentation : OK
- Create **native** tracing context and trace all method called in the application :
  Actually not really done, method calling is traced correctly, but not in a tracing context. _without specific class instrumentation maybe not possible_
- Export flamegraph on a port : not totally adone
  - this should come with concept and explanations 
- Instrumentation example: log appender to quickwit : not started


## QuickWit println interceptor !

❯ docker run --rm --platform linux/amd64 -v $(pwd)/qwdata:/quickwit/qwdata -p 127.0.0.1:7280:7280 quickwit/quickwit run
❯ curl -XPOST http://127.0.0.1:7280/api/v1/indexes --header "content-type: application/yaml" --data-binary @<>/apm4me/quickwit-index.yaml
❯ curl "http://127.0.0.1:7280/api/v1/stackoverflow/search?query=*"
❯ java -javaagent:/Users/sebastienallemand/Documents/apm4me/target/apm-1.0-SNAPSHOT-jar-with-dependencies.jar -jar ./target/emptyProject-1.0-SNAPSHOT.jar


## Interesting to read
- redefining VS rebase : https://github.com/raphw/byte-buddy/issues/996
- SuperMethodCall for overriden methods with a covariant return type https://github.com/raphw/byte-buddy/issues/1311