/**
 * 
 */
package org.blarty.zenith.messaging.test.items.cbr;

import org.blarty.zenith.messaging.components.routers.Filter;
import org.blarty.zenith.messaging.messages.EventMessage;

public class CBRFilter3 implements Filter {
        int count =0;
        public boolean check(org.blarty.zenith.messaging.messages.Message m) {
                if (m instanceof EventMessage){
                        count++;
                        return true;
                }
                return false;
        }
        
        public String toString(){
                return getClass().getName() + ": There have been " + count + " invocations";
        }
}