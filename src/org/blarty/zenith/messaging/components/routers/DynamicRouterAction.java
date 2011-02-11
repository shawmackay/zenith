/*
 * Apollo : apollo.messaging.test.router.simple
 * 
 * 
 * DynamicRouterImpl.java
 * Created on 23-Feb-2004
 * 
 * DynamicRouterImpl
 *
 */

package org.blarty.zenith.messaging.components.routers;

import java.util.HashMap;
import java.util.Map;

import org.blarty.zenith.messaging.channels.connectors.PublishingQConnector;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.system.ControllableChannelAction;
import org.blarty.zenith.messaging.system.MessagingManager;

/**
 * @author calum
 */
public class DynamicRouterAction implements ControllableChannelAction {
        /*
         * @see apollo.messaging.routers.DynamicRouter#manageControlMsg(apollo.messaging.messages.Message)
         */

        private Map<String,PublishingQConnector> routingTable = new HashMap<String,PublishingQConnector>();

        public DynamicRouterAction() {
 
        }

        public void controlDispatch(Message m) {
                // TODO Auto-generated method stub
                System.out.println("Control Message received");
                String s = m.getMessageContentAsString();
                String[] parts = s.split(":");
                try {
                        routingTable.put(parts[0], MessagingManager.getManager().getPublishingConnector(parts[1]));
                } catch (Throwable e) {
                        // URGENT Handle ChannelException
                        e.printStackTrace();
                }
        }

        public boolean dispatch(Message m) {
                // TODO Complete method stub for route
                String key = m.getMessageContentAsString().substring(0, 1);
                if (routingTable.containsKey(key)) {
                        ((PublishingQConnector) routingTable.get(key)).sendMessage(m);
                        return true;
                } else
                        return false;
        }

}
