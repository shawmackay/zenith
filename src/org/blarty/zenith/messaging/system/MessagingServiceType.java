/*
 * zenith : org.blarty.zenith.messaging.broker
 * 
 * 
 * ZenithServiceInfo.java
 * Created on 08-Mar-2005
 * 
 * ZenithServiceInfo
 *
 */
package org.blarty.zenith.messaging.system;

import java.awt.Image;

import javax.swing.ImageIcon;

import net.jini.lookup.entry.ServiceType;

/**
 * @author calum
 */
public class MessagingServiceType extends ServiceType{

	
	public String getDisplayName() {
		// TODO Complete method stub for getDisplayName
		return "Zenith Messaging Connector";
	}
	
	public java.awt.Image getIcon(int param) {
        ImageIcon imic = new ImageIcon(this.getClass().getResource("mesgsvc.png"));
        ImageIcon imicmono = new ImageIcon(this.getClass().getResource("mesgsvc.png"));
        if (param == java.beans.BeanInfo.ICON_COLOR_16x16) {
            return imic.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        }
        if (param == java.beans.BeanInfo.ICON_COLOR_32x32) {
            return imic.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT);
        }

        if (param == java.beans.BeanInfo.ICON_MONO_16x16) {
            return imicmono.getImage().getScaledInstance(16, 16, Image.SCALE_DEFAULT);
        }
        if (param == java.beans.BeanInfo.ICON_MONO_32x32) {
            return imicmono.getImage().getScaledInstance(32, 32, Image.SCALE_DEFAULT);
        }
        return imic.getImage();
    }

	
	public String getShortDescription() {
		// TODO Complete method stub for getShortDescription
		return "Maintains lists of channels and which services they are in";
	}

}
