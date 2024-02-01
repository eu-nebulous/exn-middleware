package eu.nebulouscloud.exn.modules.sal.middleware.handlers.connection

import eu.nebulouscloud.exn.core.Context
import eu.nebulouscloud.exn.handlers.ConnectorHandler
import groovy.util.logging.Slf4j

@Slf4j
class EXNConnectorHandler extends ConnectorHandler{

    @Override
    void onReady(Context context){
        log.info('Exn connector handler is ready')
    }


}
