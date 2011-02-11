/*
 * zenith : org.jini.projects.zenith.messaging.endpoints
 * 
 * 
 * StoringEndpoint.java
 * Created on 19-Apr-2005
 * 
 * StoringEndpoint
 *
 */

package org.jini.projects.zenith.messaging.endpoints;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.jini.id.Uuid;
import net.jini.id.UuidFactory;

import org.jini.projects.zenith.messaging.channels.ReceiverChannel;
import org.jini.projects.zenith.messaging.system.ChannelException;
import org.jini.projects.zenith.messaging.system.MessagingListener;

/**
 * @author calum
 */
public class StoringEndpoint {
	public StoringEndpoint(ReceiverChannel channel, File directory) {
		if (!directory.exists())
			directory.mkdirs();
		try {
			channel.setReceivingListener(new StorageListener(directory));
		} catch (ChannelException e) {
			// TODO Handle ChannelException
			e.printStackTrace();
		}
	}
	class StorageListener implements MessagingListener{
		private File storeTo;
		public StorageListener(File directory){
			storeTo = directory;
		}
		
		public void messageReceived(org.jini.projects.zenith.messaging.messages.Message m) {
			System.out.println("Received a message");
			Uuid id = UuidFactory.generate();
			File f = new File(storeTo, id.toString()+".msg");
			ObjectOutputStream oos = null;
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(f)));				
				oos.writeObject(m);
				oos.flush();
				oos.close();
			} catch (FileNotFoundException e) {
				// TODO Handle FileNotFoundException
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Handle IOException
				//e.printStackTrace();
			} finally {
				if(oos!=null)
					try {
						oos.close();
					} catch (IOException e) {
						// TODO Handle IOException
						e.printStackTrace();
					}
			}
			
		}

	}
}
