package eu.city4ageproject.delivery;

import org.universAAL.middleware.container.ModuleActivator;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.utils.LogUtils;

public class ProjectActivator implements ModuleActivator {

	static ModuleContext context;
	private HTTPService servlet;
	
	public void start(ModuleContext ctxt) throws Exception {	
		context = ctxt;
		LogUtils.logDebug(context, getClass(), "start", "Starting.");
		/*
		 * uAAL stuff
		 */
		
		servlet = new HTTPService(context);
		
		servlet.register();
		
		LogUtils.logDebug(context, getClass(), "start", "Started.");
	}


	public void stop(ModuleContext ctxt) throws Exception {
		LogUtils.logDebug(context, getClass(), "stop", "Stopping.");
		/*
		 * close uAAL stuff
		 */
		servlet.unregister();
		
		LogUtils.logDebug(context, getClass(), "stop", "Stopped.");

	}

}
