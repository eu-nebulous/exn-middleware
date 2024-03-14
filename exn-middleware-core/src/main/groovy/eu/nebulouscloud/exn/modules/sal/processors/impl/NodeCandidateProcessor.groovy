package eu.nebulouscloud.exn.modules.sal.processors.impl


import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.repository.node.NodeRegistrar
import eu.nebulouscloud.exn.modules.sal.repository.nodecandidate.NodeCandidateRepository
import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service('nodecandidateProcessor')
class NodeCandidateProcessor extends AbstractProcessor{

    @Autowired
    NodeCandidateRepository nodeCandidateRepository

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Autowired
    Map<String,NodeRegistrar> nodeRegistrarMap

    @Override
    Map get(Map metaData, String o) {

        logger.info('{} - Getting node candidates {}',metaData?.user, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

//        List response = nodeCandidateRepository.findCandidates(o,headers,List.class)
        List response = nodeCandidateRepository.save(o,headers,List.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map update(Map metaData, String o) {

        logger.info('{} - Ranking node candidates {}',metaData?.user, o)

        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        def response = nodeCandidateRepository.rankCandidates(o,headers,Object.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

}
