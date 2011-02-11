/**
 * 
 */
package org.jini.projects.zenith.messaging.test.items.cbr;

import org.jini.projects.zenith.messaging.components.routers.Filter;
import org.jini.projects.zenith.messaging.messages.ObjectMessage;

public class CBRFilter1 implements Filter {
        
        int count = 0;
        
        public boolean check(org.jini.projects.zenith.messaging.messages.Message m) {
                if (m instanceof ObjectMessage){
                        count++;
                        return true;
                }
                return false;
        }
        
        public String toString(){
                return getClass().getName() + ": There have been " + count + " invocations";
        }
}