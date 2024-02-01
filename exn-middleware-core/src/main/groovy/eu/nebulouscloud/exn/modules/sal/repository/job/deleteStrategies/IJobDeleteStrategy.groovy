package eu.nebulouscloud.exn.modules.sal.repository.job.deleteStrategies

import org.springframework.http.HttpHeaders

interface IJobDeleteStrategy {

    def handleDelete(String jobId, String body, HttpHeaders headers)
}