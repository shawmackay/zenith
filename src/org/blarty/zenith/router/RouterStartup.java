/*
 * Apollo : org.jini.projects.zenith.router
 * 
 * 
 * RouterStartup.java
 * Created on 29-Jul-2003
 *
 */
package org.jini.projects.zenith.router;

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

import org.jini.projects.zenith.router.constrainable.RouterProxy;

/**
 * @author calum
 */
public class RouterStartup implements DiscoveryListener {
	LookupDiscoveryManager ldm;
	JoinManager jm;
	RouterServiceImpl router;
	/**
	 * 
	 */
	public RouterStartup(String[] args) {
        super();
        router = new RouterServiceImpl();
        // TODO Complete constructor stub for RouterStartup
        try {
            
            ldm = new LookupDiscoveryManager(args, null, this);
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

	public static void main(String[] args) {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		new RouterStartup(args);
	}

	/* (non-Javadoc)
	 * @see net.jini.discovery.DiscoveryListener#discarded(net.jini.discovery.DiscoveryEvent)
	 */
	public void discarded(DiscoveryEvent e) {
		// TODO Complete method stub for discarded

	}

	/* (non-Javadoc)
	 * @see net.jini.discovery.DiscoveryListener#discovered(net.jini.discovery.DiscoveryEvent)
	 */
	public void discovered(DiscoveryEvent e) {
		// TODO Complete method stub for discovered
            System.out.println("Discovered");
		Exporter exp = new BasicJeriExporter(HttpServerEndpoint.getInstance(0), new BasicILFactory());
		try {
			Remote r = exp.export(router);
			Uuid ID = UuidFactory.generate();
			RouterService svc = RouterProxy.create((RouterService) r, ID);
			ServiceID svcID = new ServiceID(ID.getMostSignificantBits(), ID.getLeastSignificantBits());
			Name name = new Name("Message Router");
			jm = new JoinManager(svc, new Entry[] {name}, svcID, ldm, null);
			Runtime.getRuntime().addShutdownHook(new Thread(new TermThread()));
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

}
