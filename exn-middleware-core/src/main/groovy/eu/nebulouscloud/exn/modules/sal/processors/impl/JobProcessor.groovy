package eu.nebulouscloud.exn.modules.sal.processors.impl

import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.repository.cloud.CloudRepository
import eu.nebulouscloud.exn.modules.sal.repository.job.JobRepository
import eu.nebulouscloud.exn.modules.sal.repository.job.deleteStrategies.IJobDeleteStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class JobProcessor extends AbstractProcessor{

    @Autowired
    JobRepository jobRepository

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Autowired
    Map<String, IJobDeleteStrategy> deleteStrategies

    @Override
    Map create(Map metaData, String o){

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Creating job {}',metaData?.user, o)

//      User Credentials for connecting to ProActive Server.
//      SAL is a REST interface to PWS. Get it from UI or store in middleware db?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

//      Deserialization should happen from calling component e.g. UI and not the proxying one
//      We just proxy the json payload, which has already been serialized by the calling component

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        Boolean response = jobRepository.save(o,headers,Boolean.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map get(Map metaData, String o) {

        logger.info('{} - Getting jobs {}',metaData?.user, o)

        String action = metaData.action

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)

        //Check jobId mentioned above
        String jobId = metaData.jobId
        def response
        if(!jobId){
            response = jobRepository.getAll(headers, List.class)
        }else{
            response= jobRepository.getById(jobId, action, headers)
        }

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }

    @Override
    Map delete(Map metaData, String o) {

        String jobId = metaData.jobId
        String action = metaData.action

        logger.info('{} - [{}] job {} and payload {}',metaData?.user, action, jobId, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        IJobDeleteStrategy deleteStrategy = deleteStrategies.get('job'+action.capitalize()+'Strategy')

        if(!deleteStrategy){
            return [
                    "status": HttpStatus.NOT_IMPLEMENTED.value(),
                    "body": ["key":"action-not-support","message":"Delete type "+ action +" is not supported"]
            ]
        }


        //Check jobId mentioned above
        def response = deleteStrategy.handleDelete(jobId, o, headers)

        return [
                "status": HttpStatus.OK.value(),
                "body": ["success":response]
        ]

    }

    //submit job
    @Override
    Map update(Map metaData, String o) {

        def ret =[
                "status": HttpStatus.OK.value(),
                "body": {}
        ]

        logger.info('{} - Submitting job {} with body {}',metaData?.user, metaData.jobId, o)

        //User Credentials for connecting to ProActive Server.
        //SAL is a REST interface to PWS. Get it from UI or store behind the scenes ?
        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        //Check jobId mentioned above
        Object response = jobRepository.save((metaData.jobId as String)+'/submit',o,headers, Object.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": response
        ]

    }


}
