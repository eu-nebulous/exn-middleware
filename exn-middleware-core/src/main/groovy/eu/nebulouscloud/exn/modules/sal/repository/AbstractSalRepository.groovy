package eu.nebulouscloud.exn.modules.sal.repository

import com.fasterxml.jackson.databind.ObjectMapper
import eu.nebulouscloud.exn.modules.sal.configuration.SalConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate

abstract class AbstractSalRepository{

    protected final String resource

    AbstractSalRepository(String resource){
        this.resource = resource
    }

    @Autowired
    RestTemplate restTemplate

    @Autowired
    ObjectMapper mapper

    @Autowired
    SalConfiguration configuration

    //POST
    //For session token
    protected String post(String url, Map body, HttpHeaders headers) throws HttpStatusCodeException{

        HttpEntity entity = new HttpEntity(body, headers)

        return restTemplate.postForEntity(baseUrl+'/'+resource+(url?'/'+url:''), entity, String.class).getBody()

    }

    //Not tested -> Created FOR NODES and JOBS that we dont know the payload
    protected <T> T post(String url, String body, HttpHeaders headers) throws HttpStatusCodeException{

        post(url, body, headers, Object.class)

    }

    protected Map post(String body, HttpHeaders headers) throws HttpStatusCodeException{

        post(null,body,headers,Map.class)

    }

    protected <T> T post(String body, HttpHeaders headers, Class responseType) throws HttpStatusCodeException{

        return post(null,body,headers,responseType)

    }

    protected <T> T post(String url, String body, HttpHeaders headers, Class responseType) throws HttpStatusCodeException{

        HttpEntity entity = new HttpEntity(body, headers)

        return restTemplate.postForEntity(baseUrl+'/'+resource+(url?'/'+url:''), entity, responseType).getBody() as T

    }

    //Delete
    protected def delete(String  url, HttpHeaders headers){
        delete(url, null,headers, Object.class)
    }

    protected def delete(String url, String body, HttpHeaders headers, Class responseType) throws HttpStatusCodeException{

        RequestEntity<String> entity = new RequestEntity<String>(body,headers,HttpMethod.DELETE,new URI(baseUrl+'/'+resource+(url? '/'+url:'')))

        return restTemplate.exchange(entity, responseType).getBody()

    }

    //PUT
    protected Object put(String url, String body, HttpHeaders headers) throws HttpStatusCodeException{

        RequestEntity<String> entity = new RequestEntity<String>(body, headers, HttpMethod.PUT, new URI(baseUrl+'/'+resource+(url? '/'+url:'')))

        return restTemplate.exchange(entity, Object.class).getBody()

    }

    //GET

    protected <T> T get(HttpHeaders headers, Class responseType) throws HttpStatusCodeException{
        get(null, headers, responseType)
    }

    protected <T> T get(String url, HttpHeaders headers, Class responseType) throws HttpStatusCodeException{

        RequestEntity<String> entity = new RequestEntity<String>(headers, HttpMethod.GET, new URI(baseUrl+'/'+resource+(url? '/'+url:'')))

        return restTemplate.exchange(entity, responseType).getBody() as T

    }

    def getById(String id, HttpHeaders headers){
        return get(id, headers, Map.class)
    }


    def getAll(HttpHeaders headers){
        return getAll(headers,Map.class)
    }

    def getAll(HttpHeaders headers, Class responseType){
        return get(headers,responseType)
    }

    def save(String body, HttpHeaders headers){
        return save(body, headers, Map.class)
    }

    def save(String url, String body, HttpHeaders headers){
        return post(url, body,headers, Map.class)
    }

    def save(String body, HttpHeaders headers, Class responseType){
        return post(null,body, headers, responseType)
    }

    def save(String url, String body, HttpHeaders headers, Class responseType){
        return post(url, body, headers, responseType)
    }

    def update(String url, String body, HttpHeaders headers){
        return put(url, body, headers)
    }

    String getBaseUrl(){
        return "${configuration.protocol}://${configuration.host}:${configuration.port}/${configuration.api}"
//        return "${configuration.protocol}://${configuration.host}/${configuration.api}"
    }

    def deleteByIds(String url, String body, HttpHeaders headers, Class responseType){
        return delete(url,body,headers,responseType)
    }


}
