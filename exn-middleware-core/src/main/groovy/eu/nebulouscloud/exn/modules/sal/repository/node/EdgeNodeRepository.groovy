package eu.nebulouscloud.exn.modules.sal.repository.node

import org.springframework.stereotype.Repository

@Repository
class EdgeNodeRepository extends AbstractNodeRepository{

    EdgeNodeRepository() {
        super('edge')
    }
}
