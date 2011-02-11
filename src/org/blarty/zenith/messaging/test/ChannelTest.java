/*
 * Created on 22-Feb-2004
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */

package org.blarty.zenith.messaging.test;

import java.rmi.RMISecurityManager;

import net.jini.config.ConfigurationProvider;

import org.blarty.zenith.messaging.system.MessagingManager;

/**
 * @author Calum
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ChannelTest {
        static MessagingManager mgr = MessagingManager.getManager();

        public static void main(String[] args) {

                try {
                        System.out.println("Starting test Harness");
                        MessagingManager.createManager("default", ConfigurationProvider.getInstance(new String[]{"conf/messagingMgr.config"}));
                        mgr = MessagingManager.getManager();
                        System.setProperty("java.security.policy", "d:\\java\\policy.all");
                        System.setProperty("org.blarty.zenith.messaging.system.store.dir", "zenithlogs");
                        System.setSecurityManager(new RMISecurityManager());
                        if (args.length == 0) {
                                System.err.println("Please specify a Test to run");
                                System.exit(1);
                        } else {
                                System.out.println("Mgr: " + mgr);
                                try {
                                        Class cl = Class.forName("org.blarty.zenith.messaging.test.items." + args[0] + "Test");
                                        TestItem item = (TestItem) cl.newInstance();
                                        item.run(mgr);
                                        System.out.println("Waiting 10 secs");
                                        Thread.sleep(20000);
                                        System.exit(0);
                                } catch (Exception ex) {
                                        System.err.println("Caught Exception: " + ex.getClass().getName() + "; Msg: " + ex.getMessage());
                                        ex.printStackTrace();
                                }

                        }
                } catch (Exception ex) {
                        ex.printStackTrace();
                }

                try {
                        Thread.sleep(2000);
                } catch (Exception e) {
                        e.printStackTrace();
                }
                System.out.println("Completed AsyncTest");
                System.exit(0);
        }
}
