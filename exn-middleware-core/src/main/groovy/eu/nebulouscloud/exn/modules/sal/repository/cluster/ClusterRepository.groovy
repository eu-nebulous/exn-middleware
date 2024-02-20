package eu.nebulouscloud.exn.modules.sal.repository.cluster

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Repository

@Repository
class ClusterRepository extends AbstractSalRepository{

    ClusterRepository() {
        super('cluster')
    }

    private final Map<String,String> SCALE_ACTION_MAPPING = [
            'scaleout' : 'out',
            'scalein' : 'in'
    ]

    def postAction(String body, String action, HttpHeaders headers, Class responseType){
        post(action,body,headers,responseType)
    }

    def scale(String body, String action, HttpHeaders headers, Class responseType){
        post("scale/${SCALE_ACTION_MAPPING[action]}",body,headers,responseType)
    }
}
