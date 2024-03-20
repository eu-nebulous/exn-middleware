package eu.nebulouscloud.exn.modules.sal.middleware.config


import eu.nebulouscloud.exn.modules.sal.middleware.handlers.connection.EXNConnectorHandler
import eu.nebulouscloud.exn.Connector
import eu.nebulouscloud.exn.core.Consumer
import eu.nebulouscloud.exn.core.Publisher
import eu.nebulouscloud.exn.modules.sal.configuration.AppConfig
import eu.nebulouscloud.exn.modules.sal.middleware.handlers.consumer.AMQPSalMessageHandler
import eu.nebulouscloud.exn.settings.StaticExnConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix='application.exn')
class ConfigureEXNConnector {

    AppConfig config

    @Autowired
    AMQPSalMessageHandler amqpSalMessageHandler

    @Bean
    Connector configEXNConnector(){

        Connector c = new Connector(
                "exn.sal",
                new EXNConnectorHandler(),
                [
                        new Publisher("cloud.create","cloud.create.reply",true,false),
                        new Publisher("cloud.get","cloud.get.reply",true,false),
                        new Publisher("cloud.delete","cloud.delete.reply",true,false),
                        new Publisher("nodecandidate.rank","nodecandidate.rank.reply",true,false),
                        new Publisher("nodecandidate.get","nodecandidate.get.reply",true,false),
                        new Publisher("node.create","node.create.reply",true,false),
                        new Publisher("node.assign","node.assign.reply",true,false),
                        new Publisher("node.get","node.get.reply",true,false),
                        new Publisher("node.delete","node.delete.reply",true,false),
                        new Publisher("job.get","job.get.reply",true,false),
                        new Publisher("job.submit","job.submit.reply",true,false),
                        new Publisher("job.create","job.create.reply",true,false),
                        new Publisher("job.delete","job.delete.reply",true,false),
                        new Publisher("job.kill","job.kill.reply",true,false),
                        new Publisher("job.stop","job.stop.reply",true,false),
                        new Publisher("scale.in","scale.in.reply",true,false),
                        new Publisher("scale.out","scale.out.reply",true,false),
                        new Publisher("cluster.get","cluster.get.reply",true,false),
                        new Publisher("cluster.define","cluster.define.reply",true,false),
                        new Publisher("cluster.deploy","cluster.deploy.reply",true,false),
                        new Publisher("cluster.scaleout","cluster.scaleout.reply",true,false),
                        new Publisher("cluster.scalein","cluster.scalein.reply",true,false),
                        new Publisher("cluster.label","cluster.label.reply",true,false),
                        new Publisher("cluster.deployapplication","cluster.deployapplication.reply",true,false),
                        new Publisher("cluster.delete","cluster.delete.reply",true,false)
                ],
                [
                        new Consumer("cloud","cloud.>", amqpSalMessageHandler,true,false),
                        new Consumer("nodecandidate","nodecandidate.>", amqpSalMessageHandler,true,false),
                        new Consumer("node","node.>", amqpSalMessageHandler,true,false),
                        new Consumer("job","job.>", amqpSalMessageHandler,true,false),
                        new Consumer("scale","scale.>", amqpSalMessageHandler,true,false),
                        new Consumer("cluster","cluster.>", amqpSalMessageHandler,true,false)
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
