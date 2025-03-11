package eu.nebulouscloud.exn.modules.sal.repository.job

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

@Repository
class JobRepository extends AbstractSalRepository{

    JobRepository() {
        super('job')
    }

    def getById(String id, String action, HttpHeaders headers){
        if(!action) {
            return get(id, headers, Object.class)
        }
        return get(id+'/'+action,headers,Object.class)
    }
}
