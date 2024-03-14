package eu.nebulouscloud.exn.modules.sal.processors.impl

import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.repository.cloud.CloudRepository
import eu.nebulouscloud.exn.modules.sal.repository.cluster.ClusterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class ClusterProcessor extends AbstractProcessor{

    @Autowired
    ClusterRepository clusterRepository

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Override
    Map get(Map metaData, String o) {

        logger.info('{} - Getting cluster {}',metaData.user, metaData.cluster)

        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        def response = clusterRepository.getById(metaData.clusterName as String,headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map create(Map metaData, String o){

        logger.info('{} - Posting cluster action {} with body {}',metaData?.user, metaData.action ,o)

        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        def response = clusterRepository.postAction(o,metaData.action as String,headers,Object.class)

        logger.info('Got response {}',response)
        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map update(Map metaData, String o) {

        logger.info('{} - Scaling cluster with body {}',metaData?.user, o)

        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        def response = clusterRepository.scale(o,metaData.action as String,headers, Object.class)

        logger.info('Got response {}',response)
        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

}
