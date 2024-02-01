package eu.nebulouscloud.exn.modules.sal.repository.job.deleteStrategies

import eu.nebulouscloud.exn.modules.sal.repository.job.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class JobStopStrategy implements IJobDeleteStrategy{

    @Autowired
    JobRepository jobRepository

    @Override
    def handleDelete(String jobId, String body, HttpHeaders headers) {

        if(!jobId){
            return
        }

        return jobRepository.update(jobId+'/stop',body,headers)

    }
}
