package org.blarty.zenith.messaging.components.routers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.blarty.zenith.messaging.channels.PublishingChannel;
import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.ObjectMessage;
import org.blarty.zenith.messaging.system.ChannelException;
import org.blarty.zenith.messaging.system.ControllableChannelAction;
import org.blarty.zenith.messaging.system.MessagingManager;

public class ContentBasedRouterAction implements ControllableChannelAction {

        List<PublishingChannel> publishers;

        List<Filter> filters;

        private Logger log = Logger.getLogger("org.blarty.zenith.messaging.routers");

        public ContentBasedRouterAction() {
                publishers = new ArrayList<PublishingChannel>();
                filters = new ArrayList<Filter>();
        }

        public void controlDispatch(Message m) {
                // TODO Auto-generated method stub
                addChannelFilter(m);
        }

        private void addChannelFilter(Message m) {
                ObjectMessage obs = (ObjectMessage) m;
                ChannelFilterLink cfl = (ChannelFilterLink) obs.getMessageContent();
                try {
                        publishers.add(MessagingManager.getManager().getSendingChannel(cfl.getChannelName()));
                        filters.add(cfl.getFilter());

                } catch (ChannelException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
        }

        public boolean dispatch(Message m) {
                boolean dispatched = false;
                // boolean consumed = false;

                if (m != null) {
                        // System.out.println(m.getClass().getName());
                        for (int i = 0; i < filters.size(); i++) {
                                Filter theFilter = filters.get(i);
                                if (theFilter.check(m)) {
                                        try {
                                                PublishingChannel ch = publishers.get(i);
                                                ch.getPublishingQConnector().sendMessage(m);
                                                dispatched = true;
                                                break;
                                        } catch (Exception ex) {
                                                log.log(Level.SEVERE, "Error sending Message", ex);
                                        }
                                }
                        }
                        if (!dispatched) {
                                return false;
                        } else
                                return true;
                }

                // TODO Auto-generated method stub
                return false;
        }

}
