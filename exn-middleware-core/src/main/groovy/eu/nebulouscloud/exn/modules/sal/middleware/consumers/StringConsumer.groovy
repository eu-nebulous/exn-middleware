package eu.nebulouscloud.exn.modules.sal.middleware.consumers

import eu.nebulouscloud.exn.modules.sal.middleware.handlers.consumer.AMQPSalMessageHandler
import eu.nebulouscloud.exn.core.Consumer
import eu.nebulouscloud.exn.core.Context
import eu.nebulouscloud.exn.core.Handler
import groovy.util.logging.Slf4j
import org.apache.qpid.protonj2.client.Delivery
import org.apache.qpid.protonj2.client.Message

@Slf4j
class StringConsumer extends Consumer{

    StringConsumer(String key, String address, Handler handler, boolean topic, boolean FQDN) {
        super(key, address, handler, topic, FQDN)
    }

    @Override
    protected void onDelivery(Delivery delivery, Context context){
        log.debug("Overridden UI on delivery for delivery for {}",this.linkAddress)
        Message message = delivery.message()

        String body = this.processStringMessage(message, context)
        (this.handler as AMQPSalMessageHandler).onStringMessage(
                this.key,
                this.address,
                body,
                message,
                context
        )
        delivery.accept()
    }

    protected String processStringMessage(Message message, Context context){
        log.debug("Processing message for{}",this.linkAddress)
        return message.body() as String
    }
}
