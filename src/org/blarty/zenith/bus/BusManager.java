/*
* Apollo : org.blarty.zenith.bus
*
*
* BusLoader.java Created on 29-Jul-2003
*
*/
package org.blarty.zenith.bus;

import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.config.ConfigurationProvider;
import net.jini.core.constraint.RemoteMethodControl;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.discovery.LookupDiscovery;
import net.jini.discovery.LookupDiscoveryManager;
import net.jini.export.Exporter;
import net.jini.id.UuidFactory;
import net.jini.jeri.BasicILFactory;
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.http.HttpServerEndpoint;
import net.jini.lease.LeaseRenewalManager;
import net.jini.lookup.LookupCache;
import net.jini.lookup.ServiceDiscoveryEvent;
import net.jini.lookup.ServiceDiscoveryListener;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.lookup.ServiceItemFilter;

import org.blarty.zenith.bus.constrainable.RouterJointProxy;
import org.blarty.zenith.router.RouterJoint;
import org.blarty.zenith.router.RouterService;

/**
* @author calum
*/
public class BusManager implements ServiceDiscoveryListener{
	
	Bus mesgBus;
	Logger l = Logger.getLogger("org.blarty.zenith.bus.RouterJoint");
	private Remote r;
	private RouterJoint rj;
	private Exporter exp;
	private RouterService router;
	private Configuration config;
	private LookupDiscoveryManager ldm;
	private ServiceDiscoveryManager sdm;
	private LookupCache cache;
	private List registeredRouters = new ArrayList();
	/**
	*
	*/
	
	private RouterJoint busLink;
	
	public BusManager(Bus mesgBus, RouterJoint joint, Configuration config) {
		super();
		this.config = config;
		try {
			String[] routerLookupGroups =(String[]) config.getEntry("org.blarty.zenith.router","initialLookupGroups",String[].class, LookupDiscovery.NO_GROUPS);
			for (int i = 0; i <routerLookupGroups.length; i++) {
				l.finest("BusManager looking in group: " + routerLookupGroups[i]);
			}
			ldm = new LookupDiscoveryManager(routerLookupGroups,null,null);
			sdm = new ServiceDiscoveryManager(ldm, new LeaseRenewalManager(config), config);
			
			this.mesgBus = mesgBus;
			
			if (joint == null) {
				try {
					busLink = new BusJoint(mesgBus);
				} catch (RemoteException e) {
					// URGENT Handle RemoteException
					e.printStackTrace();
				}
			} else{
				busLink = joint;
				l.finest("Using a " + joint.getClass().getName() + " instance as the router joint");
			}
			createRouterJoint();
			cache = sdm.createLookupCache(new ServiceTemplate(null, new Class[]{RouterService.class},null), new RouterFilter(),this);
		} catch (ExportException e) {
			// TODO Handle ExportException
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		} catch (ConfigurationException e) {
			// TODO Handle ConfigurationException
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Handle IOException
			e.printStackTrace();
		}		//
		
	}
	
	private void createRouterJoint() throws ExportException{
		exp = new BasicJeriExporter(HttpServerEndpoint.getInstance(0), new BasicILFactory());
		
		r = exp.export(busLink);
		
		rj = RouterJointProxy.create((RouterJoint) r, UuidFactory.generate());
		
		if (rj instanceof RemoteMethodControl)
			l.finest("The RouterJoint proxy is secure");
		else
		l.finest("The Router Joint proxy is not secure");
	}
	
	private void registerBusNode(RouterService router) {
		try {
			if (router != null) {
				
				router.createNodePoint(rj);
				l.fine("Message Bus has connected to Router system");
				//buildBaseSubscribers(mesgBus);
				//l.info("Test Subscribers Loaded");
				if(!registeredRouters.contains(router))
					registeredRouters.add(router);
			} else
			l.severe("No Message Router Service available");
		} catch (ExportException e) {
			// TODO Handle ExportException
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		}
	}
	
	public Bus getMessageBus() {
		return mesgBus;
	}
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null)			System.setSecurityManager(new RMISecurityManager());
		try {
			new BusManager(new ApolloBus("global"), null, ConfigurationProvider.getInstance(args));
		} catch (ConfigurationException e) {
			// TODO Handle ConfigurationException
			e.printStackTrace();
		}
		
	}
	
	public void deregisterAll(){
		for(int i=0;i<registeredRouters.size();i++)
			deregister((RouterService) registeredRouters.get(i));
		registeredRouters.clear();
	}
	
	public void deregister(RouterService router) {
		try {
			router.deregisterNodePoint(rj);
			l.fine("Deregistered Node Point");
			exp.unexport(false);
			l.fine("Unexported RouterJoint");
		} catch (RemoteException e) {
			// TODO Handle RemoteException
			e.printStackTrace();
		}
	}
	
	/*
	* (non-Javadoc)
	*
	* @see java.lang.Object#finalize()
	*/
	protected void finalize() throws Throwable {
		// TODO Complete method stub for finalize
		super.finalize();
		System.out.println("BusLoader Finalized");
	}
	
	public void serviceAdded(ServiceDiscoveryEvent event) {
		// TODO Complete method stub for serviceAdded
		ServiceItem item = event.getPostEventServiceItem();
		l.fine("Service " + item.serviceID + " (" + item.service.getClass().getName() + " added");
		registerBusNode((RouterService) item.service);
	}
	
	public void serviceChanged(ServiceDiscoveryEvent event) {
		// TODO Complete method stub for serviceChanged
		ServiceItem item = event.getPostEventServiceItem();
		l.fine("Service " + item.serviceID + " (" + item.getClass().getName() + " changed");
	}
	
	public void serviceRemoved(ServiceDiscoveryEvent event) {
		
		// TODO Complete method stub for serviceRemoved
		ServiceItem item = event.getPreEventServiceItem();
		l.fine("Service " + item.serviceID + " (" + item.getClass().getName() + " removed");
		
	}
	
	private class RouterFilter implements ServiceItemFilter{
		public boolean check(ServiceItem item) {
			if (item.service instanceof RouterService)
				return true;
			return false;
		}
		
	}
	
}
