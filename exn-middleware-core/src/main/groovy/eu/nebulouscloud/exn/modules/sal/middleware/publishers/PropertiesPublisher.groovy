package eu.nebulouscloud.exn.modules.sal.middleware.publishers

import eu.nebulouscloud.exn.core.Publisher
import groovy.util.logging.Slf4j
import org.apache.qpid.protonj2.client.Message
import org.apache.qpid.protonj2.client.Tracker

@Slf4j
class PropertiesPublisher extends Publisher{

    PropertiesPublisher(String key, String address, boolean Topic, boolean FQDN) {
        super(key, address, Topic, FQDN)
    }

    public send(Map body, Map<String,String> amqpProperties){

        log.debug("{} Sending {}", this.address, body)
        def message = this.prepareMessage(body)
        amqpProperties.each {
            String property, String value ->
                if(!value){
                    return
                }
                switch (property) {
                    case 'reply-to':
                        message.replyTo(value)
                        break
                    case 'correlation-id':
                        message.correlationId(value)
                        break
                    default:
                        //this method does not work for the two above as
                        // it sets the application properties of the AMQP message,
                        // which reply and correlation are not part of in the AMQP spec
                        message.property(key,value)
                }
        }
        Tracker tracker = this.link.send(message)
        tracker.awaitSettlement()

    }

}
