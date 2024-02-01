package eu.nebulouscloud.exn.modules.sal.middleware.handlers.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import eu.nebulouscloud.exn.modules.sal.processors.Processor
import eu.nebulouscloud.exn.core.Context
import eu.nebulouscloud.exn.core.Handler
import eu.nebulouscloud.exn.core.Publisher
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.apache.qpid.protonj2.client.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Slf4j
public class AMQPSalMessageHandler extends Handler {

    @Autowired
    Map<String, Processor> processors

    @Autowired
    ObjectMapper mapper

    @Value('${application.jms.topic}')
    String baseQueue

    @Override
    void onMessage(String key, String address, Map body, Message message, Context context) {

        log.info("Received by custom handler {} => {} = {}", key, address, String.valueOf(body))

        try {
            String destination = StringUtils.substringAfter(message.to(), '://') ?: message.to()

            log.trace('Got destination {}', destination)

            if (destination.contains('.reply')) {
                return
            }

            def processor = destination.replaceAll(context.base + ".", "")

            //Remove component from destination
            processor = StringUtils.substringBefore(processor, '.')
            processor = processor + "Processor"

            def p = processors.containsKey(processor) ?
                    processors.get(processor) : processors.get('noOpProcessor')

            Map response = p.process(destination, message)

            log.info('REPLYING {}',response)
            Map amqpProperties =
                    ['correlation-id': message.correlationId()?.toString(),
                     'reply-to'      : message.replyTo()]

            if (message.replyTo()) {

                log.debug("Reply to Queue={} -> Correlation ID = {} ", message.replyTo(), message.correlationId())
                String tempKey = message.replyTo() + message.correlationId()
                Publisher tempPublisher = new Publisher(tempKey, message.replyTo(), true, true)

                tempPublisher.send(response, null, amqpProperties)
                context.unregisterPublisher(tempKey)

                return

            }

            //Async reply to ".reply" topic
            log.trace("Publishing default .reply with optional correlation {}", message.correlationId())

//        Publisher publisher = context.getPublisher(StringUtils.substringAfter(destination,context.base+'.')) as StringPublisher
            Publisher publisher = context.getPublisher(StringUtils.substringAfter(destination, context.base + '.'))

            publisher.send(response, null, amqpProperties)
        } catch (Exception e) {
            log.error('Pre Sent caught exception', e)
        }

    }

}
