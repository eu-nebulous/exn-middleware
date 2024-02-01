package eu.nebulouscloud.exn.modules.sal.repository.node

import org.springframework.http.HttpHeaders

interface NodeRegistrar {

    def register(String jobId, String body, HttpHeaders headers)
    def assign(String jobId, String body, HttpHeaders headers)
    def getAll(HttpHeaders headers)
    def getById(String jobId, HttpHeaders headers)
    def deleteById(String jobId, HttpHeaders headers)

}