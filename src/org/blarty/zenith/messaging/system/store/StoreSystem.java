/*
* StoreSystem.java
*
* Created on 18 October 2004, 13:29
*/

package org.blarty.zenith.messaging.system.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;

import org.blarty.zenith.messaging.messages.Message;
import org.blarty.zenith.messaging.messages.MessageHeader;
import org.blarty.zenith.messaging.messages.StringMessage;

/**
*
* @author calum
*/
public class StoreSystem {
    
        
        Logger l = Logger.getLogger("org.blarty.zenith.messaging.system");
        private File directory;
        public StoreSystem(String directoryName) throws IOException{
                directory = new File(directoryName);
                if(directory.isFile())
                        throw new IOException("Please specify a directory");
                if (!directory.exists())
                        if (!directory.mkdirs())
                                throw new IOException("Storage Directory could not be created");
        }
        
        /** Creates a new instance of StoreSystem */
        public StoreSystem(Configuration config) throws IOException, ConfigurationException {
                String directoryName = (String) config.getEntry("zenith", "guaranteeStoreDir", String.class, "log/storageArea");
                directory = new File(directoryName);
                if(directory.isFile())
                        throw new IOException("Please specify a directory");
                if (!directory.exists())
                        if (!directory.mkdirs())
                                throw new IOException("Storage Directory could not be created");
        }
        
        public void store(String destination, Message m) throws IOException {
                MessageHeader hdr = m.getHeader();
                // File channelDir = new File(directory, ch.getName());
                // if(!channelDir.exists())
                // channelDir.mkdir();
                Integer storeIdx = (Integer) hdr.getArbitraryField("storageIndex");
                if (storeIdx != null) {
                        storeIdx = new Integer(storeIdx.intValue() + 1);
                        hdr.setArbitraryField("storageIndex", storeIdx);
                } else {
                        storeIdx = new Integer(0);
                        hdr.setArbitraryField("storageIndex", storeIdx);
                }
                File storageFile = new File(directory, hdr.getRequestID().toString() + "_" + storeIdx);
                l.finest("FileName to add: " + storageFile.getAbsolutePath());
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile));
                oos.writeObject(m);
                oos.writeObject(destination);
                oos.flush();
                oos.close();
        }
        
        public void storeAll(String destination, Message[] msgs) throws IOException {
                for (int i=0;i<msgs.length;i++){
                        Message m = msgs[i];
                        store(destination, m);
                }
        }
        
        public void remove(Message m) throws IOException {
                MessageHeader hdr = m.getHeader();
                // File channelDir = new File(directory, cgetName());
                // if(!channelDir.exists())
                // channelDir.mkdir();
                Integer storeIdx = (Integer) hdr.getArbitraryField("storageIndex");
                
                File storageFile = new File(directory, hdr.getRequestID().toString() + "_" + (storeIdx.intValue()-1));
                System.out.println("FileName to remove: " + storageFile.getAbsolutePath());
                if (storageFile.exists()) {
                        storageFile.delete();
                } else
                System.out.println("Stored Message does not exist");
        }
        
        public void processed(Message m) throws IOException {
                MessageHeader hdr = m.getHeader();
                
                Integer storeIdx = (Integer) hdr.getArbitraryField("storageIndex");
                
                File storageFile = new File(directory, hdr.getRequestID().toString() + "_" + (storeIdx.intValue()));
                System.out.println("FileName to remove: " + storageFile.getAbsolutePath());
                if (storageFile.exists()) {
                        storageFile.delete();
                } else{
                        System.out.println("Stored Message does not exist");
                }
        }
        
        public static void main(String[] args) {
                try {
                        StoreSystem sys = new StoreSystem("/tmp/storageArea");
                        MessageHeader hdr = new MessageHeader();
                        hdr.setReplyAddress("MyAddress");
                        StringMessage msg = new StringMessage(hdr, "Hello");
                        sys.store(null, msg);
                        System.out.println("Storage complete");
                } catch (Exception e) {
                        System.err.println("Err: " + e.getMessage());
                        e.printStackTrace();
                }
        }
}
