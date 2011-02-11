package org.jini.projects.zenith.messaging.test;

import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingManager;

public class SendReceiveTest {

    ReceiverChannel inChannel;
    
    public SendReceiveTest(){
        try {
            MessagingManager.getManager().createChannel("SendRecTest");
            inChannel = MessagingManager.getManager().getReceivingChannel("SendRecTest");
        } catch (ChannelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void getSomething(){
      System.out.println("Request coming from Thread: " + Thread.currentThread().getName());
      //MessagingManager mgr = new MessagingManager();
      //ReceiverChannel rec=mgr.createTemporaryChannel();
      //rec.setReceivingListener()
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
