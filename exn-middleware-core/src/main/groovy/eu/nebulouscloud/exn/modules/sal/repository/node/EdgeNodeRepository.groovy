package eu.nebulouscloud.exn.modules.sal.repository.node

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

@Repository
class EdgeNodeRepository extends AbstractNodeRepository{

    EdgeNodeRepository() {
        super('edge')
    }

    @Override
    def register(String jobId, String body, HttpHeaders headers){
        post('register', body, headers)
    }
}
