package eu.nebulouscloud.exn.modules.sal.repository.nodecandidate

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

@Repository
class NodeCandidateRepository extends AbstractSalRepository{

    NodeCandidateRepository() {
        super('nodecandidates')
    }

    List findCandidates(String body, HttpHeaders headers, Class responseType){
        post('filter',body,headers,responseType)
    }


}
