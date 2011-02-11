package org.blarty.zenith.messaging.system;

/**
 * If a messaging component expects a certain type of ChannelAction (for
 * instance. a controlled component, may want to use a
 * ConfigurableChannelAction) and the wrong type is passed, it may throw this
 * exception.
 * 
 * @author calum.mackay
 * 
 */
public class UnsupportedActionException
                extends
                Exception {
        public UnsupportedActionException() {
                super();
                // TODO Auto-generated constructor stub
        }

        public UnsupportedActionException(String message, Throwable cause) {
                super(message, cause);
                // TODO Auto-generated constructor stub
        }

        public UnsupportedActionException(Throwable cause) {
                super(cause);
                // TODO Auto-generated constructor stub
        }

        public UnsupportedActionException(String message) {
                super(message);
        }
}
