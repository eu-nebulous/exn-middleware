package eu.nebulouscloud.exn.modules.sal.repository.scale

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

@Repository
class ScaleRepository extends AbstractSalRepository{

    ScaleRepository() {
        super("scale")
    }

    def scale(String body, String jobId, String taskName, String action, HttpHeaders headers, Class responseType){
        post("${jobId}/${taskName}/${action}",body,headers,responseType)
    }
}
