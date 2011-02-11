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
package org.blarty.zenith.messaging.broker;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.server.ExportException;

import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.core.lookup.ServiceTemplate;
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
import net.jini.lookup.LookupCache;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.lookup.entry.Name;

import org.blarty.zenith.messaging.broker.constrainable.MessagingBrokerProxy;

/**
* @author calum
*/
public class BrokerStartup implements DiscoveryListener{
	/* @see net.jini.discovery.DiscoveryListener#discarded(net.jini.discovery.DiscoveryEvent)
	*/
	
	MessageBrokerImpl svc;
	
	LookupDiscoveryManager ldm;
	JoinManager jm;
	ServiceDiscoveryManager sdm;
	LookupCache cache;
	boolean joined;
	public BrokerStartup(String group){
		super();
		svc = new MessageBrokerImpl();
		// TODO Complete constructor stub for RouterStartup
		try {
			
			ldm = new LookupDiscoveryManager(new String[] {group}, null, this);
			
			sdm = new ServiceDiscoveryManager(ldm, null);
			cache = sdm.createLookupCache(new ServiceTemplate(null, null, null), null, svc);
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
				MessageBroker rsvc = MessagingBrokerProxy.create((MessageBroker) r, id);
				ServiceID svcID = new ServiceID(id.getMostSignificantBits(), id.getLeastSignificantBits());
				Name name = new Name("Zenith Message Broker");
				
				jm = new JoinManager(rsvc, new Entry[] {name, new MessageBrokerServiceType()}, svcID, ldm, null);
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
			
		}
	}
	
	public static void main(String[] args) {
		
		if (System.getSecurityManager()==null)
			System.setSecurityManager(new RMISecurityManager());
		new BrokerStartup(args[0]);
	}
}
