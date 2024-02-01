package eu.nebulouscloud.exn.modules.sal.processors.impl


import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import eu.nebulouscloud.exn.modules.sal.repository.cloud.CloudRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class CloudProcessor extends AbstractProcessor{

    @Autowired
    CloudRepository cloudRepository

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Override
    Map post(Map metaData, String o){

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Registering cloud {}',metaData.user, o)

//      User Credentials for connecting to ProActive Server.
//      SAL is a REST interface to PWS. Get it from UI or store in middleware db?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

//      Deserialization should happen from calling component e.g. UI and not the proxying one
//      We just proxy the json payload, which has already been serialized by the calling component

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        Integer response = cloudRepository.save(o,headers,Integer.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": ["success": response == 0]
        ]

    }

    @Override
    Map get(Map metaData, String o) {

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Getting clouds {}',metaData.user, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        //Check jobId mentioned above
        List response = cloudRepository.getAll(headers, List.class)

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

        logger.info('{} - Deleting clouds {}',metaData.user, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        //Check jobId mentioned above
        Boolean response = cloudRepository.deleteByIds('remove',o,headers,Boolean.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": ["success":response]
        ]

    }


}
