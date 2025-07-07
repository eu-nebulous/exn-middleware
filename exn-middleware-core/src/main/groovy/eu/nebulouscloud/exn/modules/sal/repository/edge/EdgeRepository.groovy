package eu.nebulouscloud.exn.modules.sal.repository.edge

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.stereotype.Repository

@Repository
class EdgeRepository extends AbstractSalRepository{

    EdgeRepository() {
        super('edge')
    }


}
