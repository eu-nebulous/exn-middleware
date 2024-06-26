package eu.nebulouscloud.exn.modules.sal.repository.node


import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders

abstract class AbstractNodeRepository extends AbstractSalRepository implements NodeRegistrar {

    AbstractNodeRepository(String resource) {
        super(resource)
    }

    @Override
    //There is a jobId path variable in the endpoint that will be optional in the end by Activeeon
    //This works for both, if one is provided then edge/jobId is used
    //If it is null then edge is posted
    def register(String jobId, String body, HttpHeaders headers){
        post(jobId, body, headers)
    }

    @Override
    def assign(String jobId, String body, HttpHeaders headers){
        put(jobId, body, headers)
    }

    @Override
    def deleteById(String edgeId, HttpHeaders headers){

        return delete(edgeId,headers)
    }
}
