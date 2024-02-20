package eu.nebulouscloud.exn.modules.sal.processors.impl


import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.repository.node.NodeRegistrar
import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class NodeProcessor extends AbstractProcessor{

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Autowired
    Map<String,NodeRegistrar> nodeRegistrarMap

    @Override
    Map create(Map metaData, String o){

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Registering node {}',metaData?.user, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store in middleware db?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        String nodeType = metaData.type
        NodeRegistrar nodeRegistrarRepository = nodeRegistrarMap.get(nodeType+'NodeRepository')

        if(!nodeRegistrarRepository){
            return [
                    "status": HttpStatus.NOT_IMPLEMENTED.value(),
                    "body": ["key":"type-not-support","message":"Node type "+ nodeType +"is not supported"]
            ]
        }

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        def response = nodeRegistrarRepository.register(metaData.jobId as String, o, headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": normalizeResponse(response)
        ]

    }

    @Override
    Map get(Map metaData, String o) {

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Getting node for Job {}',metaData?.user, metaData.jobId)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        String nodeType = metaData.type

        NodeRegistrar nodeRegistrarRepository = nodeRegistrarMap.get(nodeType+'NodeRepository')

        if(!nodeRegistrarRepository){
            return [
                    "status": HttpStatus.NOT_IMPLEMENTED.value(),
                    "body": ["key":"type-not-support","message":"Node type "+ nodeType +"is not supported"]
            ]
        }

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        Object response = nodeRegistrarRepository.getById(metaData.jobId as String, headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map delete(Map metaData, String o) {

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Deleting nodes for Job {}',metaData?.user, metaData.jobId)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        String nodeType = metaData.type

        NodeRegistrar nodeRegistrarRepository = nodeRegistrarMap.get(nodeType+'NodeRepository')

        if(!nodeRegistrarRepository){
            return [
                    "status": HttpStatus.NOT_IMPLEMENTED.value(),
                    "body": ["key":"type-not-support","message":"Node type "+ nodeType +"is not supported"]
            ]
        }

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        Object response = nodeRegistrarRepository.deleteById(metaData.jobId as String, headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": normalizeResponse(response)
        ]

    }

    @Override
    Map update(Map metaData, String o){

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Assigning node {} to job with payload: {}',metaData?.user, metaData.jobId, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store in middleware db?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        String nodeType = metaData.type
        NodeRegistrar nodeRegistrarRepository = nodeRegistrarMap.get(nodeType+'NodeRepository')

        if(!nodeRegistrarRepository){
            return [
                    "status": HttpStatus.NOT_IMPLEMENTED.value(),
                    "body": ["key":"type-not-support","message":"Node type "+ nodeType +"is not supported"]
            ]
        }

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        def response = nodeRegistrarRepository.assign(metaData.jobId as String, o, headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": ["success": response == 0]
        ]

    }


}
