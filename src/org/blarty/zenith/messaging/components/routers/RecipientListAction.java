package org.jini.projects.zenith.messaging.components.routers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jini.projects.zenith.messaging.channels.PublishingChannel;
import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.messages.ObjectMessage;
import org.jini.projects.zenith.messaging.system.ControllableChannelAction;
import org.jini.projects.zenith.messaging.system.MessagingManager;

public class RecipientListAction implements ControllableChannelAction {

        List<MessageRule> rules = new ArrayList<MessageRule>();

        Logger log = Logger.getLogger("org.jini.projects.zenith.messaging.routers");

        public void controlDispatch(Message m) {
                if (m instanceof ObjectMessage) {
                        Object data = m.getMessageContent();
                        if (data instanceof MessageRule) {
                                rules.add((MessageRule) data);
                        }
                }
        }

        public boolean dispatch(Message m) {
                try {
                        for (int i = 0; i < rules.size(); i++) {
                                MessageRule dispatcher = (MessageRule) rules.get(i);
                                if (dispatcher.evaluate(m)) {
                                        PublishingChannel ch = MessagingManager.getManager().getSendingChannel(dispatcher.getDestination());
                                        ch.getPublishingQConnector().sendMessage(m);
                                        return true;
                                }

                        }
                } catch (Exception ex) {
                        log.log(Level.SEVERE, "Error sending Message", ex);
                }
                return false;
        }

}
