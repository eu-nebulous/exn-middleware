package eu.nebulouscloud.exn.modules.sal.processors.impl

import eu.nebulouscloud.exn.modules.sal.configuration.ProactiveConfiguration
import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Slf4j
@Component
class ProactiveStateProcessor extends AbstractProcessor {

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ProactiveConfiguration proactiveConfiguration


    private String login() throws HttpClientErrorException.Unauthorized {

        MultiValueMap credentials = new LinkedMultiValueMap<String, String>()

        credentials.add('username', proactiveConfiguration.username)
        credentials.add('password', proactiveConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.MULTIPART_FORM_DATA)
        log.trace('Logging in with username: {} => {}', proactiveConfiguration.host, proactiveConfiguration.username)

        HttpEntity entity = new HttpEntity(credentials, headers)
        String ret = restTemplate.postForEntity(
                proactiveConfiguration.getBaseUrl()+"/common/login", entity, String.class).getBody()

        log.trace('Logged in with sessionId {}', ret)

        return ret

    }

    def Map state(Map metaData, String o) {

        def ret = [
                "status": HttpStatus.OK.value(),
                "body"  : {}
        ]

        logger.info('Logging in {} - {} ', proactiveConfiguration.host, proactiveConfiguration.username)
        String sessionId = login()

        if (StringUtils.isEmpty(sessionId)){
            ret['status'] = HttpStatus.FORBIDDEN.value()
            return ret
        }

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<Map> response = restTemplate.exchange(
                proactiveConfiguration.getBaseUrl()+"/rm/state",
                HttpMethod.GET,
                requestEntity, Map.class);

        return [
                "status": response.getStatusCode().value(),
                "body"  : response.getBody()
        ]

    }


}
