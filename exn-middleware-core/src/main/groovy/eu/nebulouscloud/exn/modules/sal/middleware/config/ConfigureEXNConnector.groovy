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
                        new Publisher("cloud.post","cloud.post.reply",true,false),
                        new Publisher("cloud.get","cloud.get.reply",true,false),
                        new Publisher("cloud.delete","cloud.delete.reply",true,false),
                        new Publisher("nodecandidate.post","nodecandidate.post.reply",true,false),
                        new Publisher("nodecandidate.get","nodecandidate.get.reply",true,false),
                        new Publisher("node.post","node.post.reply",true,false),
                        new Publisher("node.update","node.update.reply",true,false),
                        new Publisher("node.get","node.get.reply",true,false),
                        new Publisher("node.delete","node.delete.reply",true,false),
                        new Publisher("job.get","job.get.reply",true,false),
                        new Publisher("job.update","job.update.reply",true,false),
                        new Publisher("job.post","job.post.reply",true,false),
                        new Publisher("job.delete","job.delete.reply",true,false)
                ],
                [
                        new Consumer("cloud","cloud.>", amqpSalMessageHandler,true,false),
                        new Consumer("nodecandidate","nodecandidate.>", amqpSalMessageHandler,true,false),
                        new Consumer("node","node.>", amqpSalMessageHandler,true,false),
                        new Consumer("job","job.>", amqpSalMessageHandler,true,false)
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
