package eu.nebulouscloud.exn.modules.sal.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties('application.exn.config')
class ExnConfig {

    String url
    Integer port
    String username
    String password
    Long connectTimeout
    Long readTimeout
}
