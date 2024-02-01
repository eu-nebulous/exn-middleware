package eu.nebulouscloud.exn.modules.sal.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

import java.time.Duration

@Configuration
class RestTemplateConf {

    @Bean
    RestTemplate restTemplateInit(RestTemplateBuilder builder){

        builder.setReadTimeout(Duration.ofMinutes(3))
            .setConnectTimeout(Duration.ofMinutes(1))
            .build()
    }
}
