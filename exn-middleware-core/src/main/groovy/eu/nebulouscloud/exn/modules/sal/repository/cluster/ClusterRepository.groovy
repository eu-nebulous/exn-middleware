package eu.nebulouscloud.exn.modules.sal.repository.cluster

import eu.nebulouscloud.exn.modules.sal.repository.AbstractSalRepository
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository

@Repository
class ClusterRepository extends AbstractSalRepository{

    ClusterRepository() {
        super('cluster')
    }

    def postAction(String body, Map metaData, HttpHeaders headers, Class responseType){

        switch (metaData.action){
            case 'define':
                save(body,headers, responseType)
                break
            case 'deploy':
                save(metaData.clusterName,body,headers,responseType)
                break
            case 'label':
                save(metaData.clusterName+'/label',body,headers,responseType)
                break
            case 'deployapplication':
                save(metaData.clusterName+'/app',body,headers,responseType)
                break
            default:
                return [
                "status": HttpStatus.NOT_IMPLEMENTED.value(),
                "body": ["key":"action-not-support","message":"Action type "+ metaData.action +" is not supported"]
            ]
        }
    }

    def scale(String body, Map metaData, HttpHeaders headers, Class responseType){
        post(metaData.clusterName+"/"+metaData.action,body,headers,responseType)
    }
}
