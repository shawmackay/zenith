package org.blarty.zenith.messaging.components.routers;

import java.io.Serializable;

public class ChannelFilterLink implements Serializable {

        private static final long serialVersionUID = 7968076760574600738L;

        private String channelName;

        private Filter filter;

        public ChannelFilterLink() {

        }

        public ChannelFilterLink(String channelName, Filter filter) {
                super();
                this.channelName = channelName;
                this.filter = filter;
        }

        public String getChannelName() {
                return channelName;
        }

        public void setChannelName(String channelName) {
                this.channelName = channelName;
        }

        public Filter getFilter() {
                return filter;
        }

        public void setFilter(Filter filter) {
                this.filter = filter;
        }

}
