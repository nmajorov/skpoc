package ch.skyguide.ei.prototype.test;

import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.ServiceHelper;
import org.apache.camel.util.StopWatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Need to have the local JBoss-Fuse instance already running including our karaf features in place.
 */
public class UDPProduceConsumeEndToEndRunner {

    private static final Logger LOG = LoggerFactory.getLogger(UDPProduceConsumeEndToEndRunner.class);
    private static final int MESSAGE_COUNT = 10;

    public static void main(final String[] args) throws Exception {
        final CamelContext camelContext = new DefaultCamelContext();
        camelContext.start();

        // we need first to start the netty consumer async BEFORE pushing any messages into UDP through the outflow route
        final ConsumerTemplate consumer = camelContext.createConsumerTemplate();
        final ExecutorService executorService = camelContext.getExecutorServiceManager().newFixedThreadPool(null, "async-hook", 1);
        final Future<List<Object>> future = executorService.submit(new Callable<List<Object>>() {
            @Override
            public List<Object> call() throws Exception {
                final List<Object> objects = new ArrayList<Object>(MESSAGE_COUNT);
                final StopWatch stopWatch = new StopWatch();
                for (int i = 0; i < MESSAGE_COUNT; i++) {
                    final Object object = consumer.receiveBody("netty:udp://0.0.0.0:25002?broadcast=true&sync=false");
                    objects.add(object);
                }
                final long taken = stopWatch.taken();
                LOG.info("Took {} milliseconds to receive {} messages", taken, MESSAGE_COUNT);

                return objects;
            }
        });

        // make sure the async consumer has already started before we go any further inside the "main" thread here
        Thread.sleep(1000);

        final byte[] data = camelContext.getTypeConverter().mandatoryConvertTo(byte[].class, new FileInputStream("src/test/resources/62_65_message.bin"));
        final ProducerTemplate producer = camelContext.createProducerTemplate();
        final StopWatch stopWatch = new StopWatch();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            producer.sendBody("netty:udp://localhost:25001?broadcast=true&sync=false", data);
        }
        final long taken = stopWatch.taken();
        LOG.info("Took {} milliseconds to send {} messages", taken, MESSAGE_COUNT);

        // wait until we've consumed all the UDP packages
        future.get();

        ServiceHelper.stopAndShutdownServices(consumer, producer, camelContext);

        // need to explicitly exit the JVM here as the Netty (non-daemon) worker-threads seem to need a long time to terminate (something around 30 seconds)
        System.exit(0);
    }

}
