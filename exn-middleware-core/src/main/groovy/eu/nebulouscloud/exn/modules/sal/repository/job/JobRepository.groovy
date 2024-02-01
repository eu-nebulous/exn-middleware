package eu.nebulouscloud.exn.modules.sal.repository.job

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.stereotype.Repository

@Repository
class JobRepository extends AbstractSalRepository{

    JobRepository() {
        super('jobs')
    }
}
