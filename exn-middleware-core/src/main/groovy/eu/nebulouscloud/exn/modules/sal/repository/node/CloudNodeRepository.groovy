package eu.nebulouscloud.exn.modules.sal.repository.node

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

//Not sure but probably this is the cloud only [before byon and edge was supported?],
//that needs to be provided with a candidateId fetched through nodecandidates repository
//Ask Michael
@Repository
class CloudNodeRepository extends AbstractNodeRepository{

    CloudNodeRepository() {
        super('node')
    }

    @Override
    def assign(String jobId, String body, HttpHeaders headers){
        post(jobId, body, headers)
    }
}
