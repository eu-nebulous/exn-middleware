package eu.nebulouscloud.exn.modules.sal.processors.impl

import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import eu.nebulouscloud.exn.modules.sal.processors.AbstractProcessor
import eu.nebulouscloud.exn.modules.sal.repository.GatewayRepository
import eu.nebulouscloud.exn.modules.sal.repository.cloud.CloudRepository
import eu.nebulouscloud.exn.modules.sal.repository.scale.ScaleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class ScaleProcessor extends AbstractProcessor{

    @Autowired
    ScaleRepository scaleRepository

    @Autowired
    GatewayRepository gatewayRepository

    @Autowired
    SalConfiguration salConfiguration

    @Override
    Map update(Map metaData, String o) {

        logger.info('{} - Scaling job {} with task {} and action {} with body {}',metaData?.user, metaData.jobId, metaData.taskName, o)

        String sessionId = gatewayRepository.login(salConfiguration.username,salConfiguration.password)

        HttpHeaders headers = new HttpHeaders()
        headers.add('sessionid',sessionId)
        headers.setContentType(MediaType.APPLICATION_JSON)

        String jobId = metaData.jobId
        String taskName = metaData.taskName

        if(!jobId || !taskName){
            return [
                    "status": HttpStatus.BAD_REQUEST.value(),
                    "body": ["key":"not-job-or-task-definition","message":"JobId and TaskName cannot be null"]
            ]
        }

        //Check jobId mentioned above
        Object response = scaleRepository.scale(o,jobId,taskName,metaData.action as String,headers, Object.class)

        return [
                "status": HttpStatus.OK.value(),
                "body": normalizeResponse(response)
        ]

    }

}
