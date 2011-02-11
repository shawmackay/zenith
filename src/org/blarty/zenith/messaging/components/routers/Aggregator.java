package org.jini.projects.zenith.messaging.components.routers;

import org.jini.projects.zenith.messaging.messages.Message;
import org.jini.projects.zenith.messaging.system.ControllableChannelAction;

public class Aggregator implements ControllableChannelAction {

        // HashMap

        // Need an area to hold the messages prior to recombination

        // What do we do with the sub-messages after they've all arrived - how
        // to we recombine them.

        public Aggregator() {

        }

        public void controlDispatch(Message m) {
                // TODO Auto-generated method stub

        }

        public boolean dispatch(Message m) {
                // TODO Auto-generated method stub
                return false;
        }

}
