package eu.nebulouscloud.exn.modules.sal.processors

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import org.apache.qpid.protonj2.client.impl.ClientMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpServerErrorException

/**
 * If no license is here then you can whatever you like!
 * and of course I am not liable
 * <p>
 * Created by fotis on 21/02/20.
 */
abstract class AbstractProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(getClass())

    @Autowired
    ObjectMapper mapper

    @Override
    Map process(String destination, ClientMessage message) {

        //Maybe move it to message property(?) and don't blend with application's payload
        Map payload = message.body() as Map
        Map metaData = payload.metaData as Map
        String o = payload.body

        Map ret = [:]

        logger.debug("[{}] Processing {}", metaData, o)
        String method = destination.substring(destination.lastIndexOf(".") + 1)
        try {

            switch (method) {
                case 'get':
                    ret = get(metaData, o)
                    break;
                case 'search':
                    ret = search(metaData, o)
                    break;
                case 'update':
                    ret = update(metaData, o)
                    break;
                case 'delete':
                    ret = delete(metaData, o)
                    break;
                default:
                    ret = post(metaData,o)
            }

        } catch (HttpServerErrorException e) {
            logger.error("[{} -> {}] Exception during gateway request for {}", metaData.user, method, o, e)
            ret.status = HttpStatus.BAD_GATEWAY.value()
            ret.body = ["key": "gateway-exception-error", "message": 'Gateway exception while handling request with reason' + StringUtils.substring(e.getMessage(),0,50)]
        } catch (Exception e) {
            logger.error("[{} -> {}] Exception for {}", metaData.user, method, o, e)
            ret.status = HttpStatus.INTERNAL_SERVER_ERROR.value()
//            ret.body = ["key": "generic-exception-error", "message": 'Generic exception while handling request for user ' + metaData.user + ' and reason:\n' + StringUtils.left(e.getMessage(),100)]
            ret.body = ["key": "generic-exception-error", "message": 'Generic exception while handling request for user ' + StringUtils.substring(e.getMessage(),0,50)]
        }

        metaData.status = ret.status
//        metaData.protocol = 'HTTP'
        ret.remove('status')
        ret.metaData = metaData
        ret.body = mapper.writeValueAsString(ret.body)

        return ret

    }

    Map post(Map metaData, String body) { return noop(metaData, body) }

    Map search(Map metaData, String body) { return noop(metaData, body) }

    Map update(Map metaData, String body) { return noop(metaData, body) }

    Map get(Map metaData, String body) { return noop(metaData, body) }

    Map delete(Map metaData, String body) { return noop(metaData, body) }

    Map noop(Map metaData, String body) {
        return ["status": HttpStatus.ACCEPTED.value(), "body": metaData.user + " { " + body + "}"]
    }

}
