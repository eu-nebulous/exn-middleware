package eu.nebulouscloud.exn.modules.sal.repository.node

import org.springframework.stereotype.Repository

@Repository
class ByonNodeRepository extends AbstractNodeRepository{

    ByonNodeRepository() {
        super('byon')
    }
}
