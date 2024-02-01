package eu.nebulouscloud.exn.modules.sal.processors

import org.apache.qpid.protonj2.client.impl.ClientMessage

/**
 * If no license is here then you can whatever you like!
 * and of course I am not liable
 *
 * Created by fotis on 21/02/20.
 */
interface Processor {

    Map process(String queue, ClientMessage message)
}
