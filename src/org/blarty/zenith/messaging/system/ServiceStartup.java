/*
* apollo2 : apollo.messaging.system
*
*
* ServiceStartup.java
* Created on 26-Feb-2004
*
* ServiceStartup
*
*/
package org.jini.projects.zenith.messaging.system;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.server.ExportException;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.DiscoveryEvent;
import net.jini.discovery.DiscoveryListener;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.export.Exporter;
import net.jini.id.Uuid;
import net.jini.id.UuidFactory;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.http.HttpServerEndpoint;
import net.jini.lookup.JoinManager;
import net.jini.lookup.entry.Name;

import org.jini.glyph.chalice.DefaultExporterManager;
import org.jini.projects.zenith.messaging.system.constrainable.MessagingServiceProxy;

/**
* @author calum
*/
public class ServiceStartup implements DiscoveryListener{
	/* @see net.jini.discovery.DiscoveryListener#discarded(net.jini.discovery.DiscoveryEvent)
	*/
	
	MessagingService svc;
	boolean joined;
	LookupDiscoveryManager ldm;
	JoinManager jm;
	
	public ServiceStartup(){
		super();
		
		svc = new MessagingServiceImpl();
		// TODO Complete constructor stub for RouterStartup
		try {
			
			ldm = new LookupDiscoveryManager(new String[] {"uk.co.cwa.jini2"}, null, this);
			synchronized (this) {
				wait(0);
			}
		} catch (IOException e) {
			// TODO Handle IOException
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Handle InterruptedException
			e.printStackTrace();
		}
	}
	
	public void discarded(DiscoveryEvent e) {
		// TODO Complete method stub for discarded
	}
	/* @see net.jini.discovery.DiscoveryListener#discovered(net.jini.discovery.DiscoveryEvent)
	*/
	public void discovered(DiscoveryEvent e) {
		Exporter exp = new BasicJeriExporter(HttpServerEndpoint.getInstance(0), new BasicILFactory());
		try {
			if(!joined){
				
				joined=true;
				Remote r = exp.export(svc);
				Uuid id = UuidFactory.generate();
				MessagingService svc = MessagingServiceProxy.create((MessagingService) r, id);
				ServiceID svcID = new ServiceID(id.getMostSignificantBits(), id.getLeastSignificantBits());
				MessagingManager.getManager().setMessagingServiceID(svcID);
				
				Name name = new Name("Messaging Service");
				jm = new JoinManager(svc, new Entry[] {name}, svcID, ldm, null);
				Runtime.getRuntime().addShutdownHook(new Thread(new TermThread()));
			}
		} catch (ExportException e1) {
			// TODO Handle ExportException
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Handle IOException
			e1.printStackTrace();
		}
		
	}
	
	class TermThread implements Runnable {
		
		/* (non-Javadoc)
		* @see java.lang.Runnable#run()
		*/
		public void run() {
			// TODO Complete method stub for run
			System.out.println("Terminating");
			jm.terminate();
			ldm.terminate();
            System.out.println("Termininating MessagingService");
		}
	}
	
	public static void main(String[] args) {
		
		if (System.getSecurityManager()==null)
			System.setSecurityManager(new RMISecurityManager());
		DefaultExporterManager.getManager("exportmgr.config");
		new ServiceStartup();
	}
}
