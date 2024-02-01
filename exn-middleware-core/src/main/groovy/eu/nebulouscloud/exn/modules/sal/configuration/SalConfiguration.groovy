package eu.nebulouscloud.exn.modules.sal.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = 'application.sal')
class SalConfiguration{

    String protocol
    String host
    String port
    String api
    String username
    String password
}
