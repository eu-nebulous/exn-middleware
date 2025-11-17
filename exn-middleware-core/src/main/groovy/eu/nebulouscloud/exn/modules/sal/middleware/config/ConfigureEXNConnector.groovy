package eu.nebulouscloud.exn.modules.sal.middleware.config

import eu.nebulouscloud.exn.Connector
import eu.nebulouscloud.exn.core.Consumer
import eu.nebulouscloud.exn.core.Publisher
import eu.nebulouscloud.exn.modules.sal.configuration.ExnConfig
import eu.nebulouscloud.exn.modules.sal.middleware.handlers.connection.EXNConnectorHandler
import eu.nebulouscloud.exn.modules.sal.middleware.handlers.consumer.AMQPSalMessageHandler
import eu.nebulouscloud.exn.modules.sal.middleware.handlers.consumer.ProactiveMessageHandler
import eu.nebulouscloud.exn.settings.StaticExnConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigureEXNConnector {

    @Autowired
    ExnConfig config

    @Autowired
    AMQPSalMessageHandler amqpSalMessageHandler

    @Autowired
    ProactiveMessageHandler proactiveMessageHandler

    @Bean
    Connector configEXNConnector(){

        Connector c = new Connector(
                "exn",
                new EXNConnectorHandler(),
                [
                        new Publisher("proactive.state","proactive.state.reply",true,false),
                        new Publisher("sal.cloud.create","sal.cloud.create.reply",true,false),
                        new Publisher("sal.cloud.get","sal.cloud.get.reply",true,false),
                        new Publisher("sal.cloud.delete","sal.cloud.delete.reply",true,false),
                        new Publisher("sal.nodecandidate.rank","sal.nodecandidate.rank.reply",true,false),
                        new Publisher("sal.nodecandidate.get","sal.nodecandidate.get.reply",true,false),
                        new Publisher("sal.node.create","sal.node.create.reply",true,false),
                        new Publisher("sal.node.assign","sal.node.assign.reply",true,false),
                        new Publisher("sal.node.get","sal.node.get.reply",true,false),
                        new Publisher("sal.node.delete","sal.node.delete.reply",true,false),
                        new Publisher("sal.job.get","sal.job.get.reply",true,false),
                        new Publisher("sal.job.submit","sal.job.submit.reply",true,false),
                        new Publisher("sal.job.create","sal.job.create.reply",true,false),
                        new Publisher("sal.job.delete","sal.job.delete.reply",true,false),
                        new Publisher("sal.job.kill","sal.job.kill.reply",true,false),
                        new Publisher("sal.job.stop","sal.job.stop.reply",true,false),
                        new Publisher("sal.scale.in","sal.scale.in.reply",true,false),
                        new Publisher("sal.scale.out","sal.scale.out.reply",true,false),
                        new Publisher("sal.cluster.get","sal.cluster.get.reply",true,false),
                        new Publisher("sal.cluster.define","sal.cluster.define.reply",true,false),
                        new Publisher("sal.cluster.deploy","sal.cluster.deploy.reply",true,false),
                        new Publisher("sal.cluster.scaleout","sal.cluster.scaleout.reply",true,false),
                        new Publisher("sal.cluster.scalein","sal.cluster.scalein.reply",true,false),
                        new Publisher("sal.cluster.label","sal.cluster.label.reply",true,false),
                        new Publisher("sal.cluster.deployapplication","sal.cluster.deployapplication.reply",true,false),
                        new Publisher("sal.cluster.delete","sal.cluster.delete.reply",true,false),
                        new Publisher("sal.edge.delete","sal.edge.delete.reply",true,false)

                ],
                [
                        new Consumer("proactive","proactive.>", proactiveMessageHandler,true,false),
                        new Consumer("sal.cloud","sal.cloud.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.nodecandidate","sal.nodecandidate.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.node","sal.node.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.job","sal.job.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.scale","sal.scale.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.cluster","sal.cluster.>", amqpSalMessageHandler,true,false),
                        new Consumer("sal.edge","sal.edge.>", amqpSalMessageHandler,true,false)
                ],
                false,
                false,
                new StaticExnConfig(
                        config.url,
                        config.port,
                        config.username,
                        config.password
                )
        )

        c.start()

        return c

    }

}
