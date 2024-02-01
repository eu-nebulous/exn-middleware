package eu.nebulouscloud.exn.modules.sal.repository.cloud

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.stereotype.Repository

@Repository
class CloudRepository extends AbstractSalRepository{

    CloudRepository() {
        super('clouds')
    }


}
