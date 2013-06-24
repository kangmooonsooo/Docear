package org.freeplane.core.ui.ribbon;

import java.util.Properties;

public class RibbonSeparatorContributorFactory implements IRibbonContributorFactory {

	/***********************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************/

	/***********************************************************************************
	 * METHODS
	 **********************************************************************************/

	/***********************************************************************************
	 * REQUIRED METHODS FOR INTERFACES
	 **********************************************************************************/
	public ARibbonContributor getContributor(Properties attributes) {
		return new ARibbonContributor() {
			
			public String getKey() {
				return "separator_"+hashCode();
			}
			
			public void contribute(RibbonBuildContext context, ARibbonContributor parent) {
				parent.addChild(new RibbonSeparator(), null);
			}
			
			public void addChild(Object child, Object properties) {
			}
		};
	}
	
	public static class RibbonSeparator {
	}
}
