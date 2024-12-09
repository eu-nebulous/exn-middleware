package eu.nebulouscloud.exn.modules.sal.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

import java.time.Duration

@Configuration
class RestTemplateConf {

    @Autowired
    ExnConfig config

    @Bean
    RestTemplate restTemplateInit(RestTemplateBuilder builder){

        builder.setConnectTimeout(Duration.ofMinutes(config.connectTimeout))
            .setReadTimeout(Duration.ofMinutes(config.readTimeout))
            .build()
    }
}
