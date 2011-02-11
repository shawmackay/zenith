package org.jini.projects.zenith.messaging.components.routers.splitters;

import java.net.URL;

public class XSLTSplitPoint
                implements
                SplitPoint {

        private String destination;
        private String stylesheet;
        private URL stylesheetURL;
        
        public XSLTSplitPoint(String destination, String stylesheet){
                this.destination = destination;
                this.stylesheet = stylesheet;
        }
        
        public String getSplitDefinition() {
                // TODO Auto-generated method stub
                return stylesheet;
        }

        public String getDestination() {
                // TODO Auto-generated method stub
                return destination;
        }

    

        public void setSplitDefinition(String boundary) {
                // TODO Auto-generated method stub
                this.stylesheet = boundary;

        }

        public void setDestination(String destination) {
                // TODO Auto-generated method stub
                this.destination = destination;
        }

       
}
