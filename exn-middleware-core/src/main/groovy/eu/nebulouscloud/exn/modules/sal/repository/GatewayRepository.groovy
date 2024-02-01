package eu.nebulouscloud.exn.modules.sal.repository

import groovy.util.logging.Slf4j
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.HttpClientErrorException

@Repository
@Slf4j
class GatewayRepository extends AbstractSalRepository{

    GatewayRepository() {
        super('pagateway')
    }

    String login(String username, String password) throws HttpClientErrorException.Unauthorized{

        MultiValueMap credentials = new LinkedMultiValueMap<String, String>()

        credentials.add('username',username)
        credentials.add('password',password)

        HttpHeaders headers = new HttpHeaders()
        headers.setContentType(MediaType.MULTIPART_FORM_DATA)
        log.trace('Logging in with username: {} and password {}',username,password)
        String sessionId = post('connect', credentials, headers)
        log.trace('Logged in with sessionId {}',sessionId)

        return sessionId

    }
}
