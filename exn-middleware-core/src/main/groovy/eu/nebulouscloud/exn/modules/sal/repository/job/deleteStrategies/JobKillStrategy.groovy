package eu.nebulouscloud.exn.modules.sal.repository.job.deleteStrategies

import eu.nebulouscloud.exn.modules.sal.repository.job.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class JobKillStrategy implements IJobDeleteStrategy{

    @Autowired
    JobRepository jobRepository

    @Override
    def handleDelete(String jobId, String body, HttpHeaders headers) {

        //Kill all
        if(!jobId){
            return jobRepository.update('kill',body,headers)
        }

        //Kill by id
        return jobRepository.update(jobId+'/kill',body,headers)

    }
}
