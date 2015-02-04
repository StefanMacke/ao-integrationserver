package net.aokv.integrationserver.wmpublic.pub.math;

import net.aokv.integrationserver.ServiceInput;
import net.aokv.integrationserver.ServiceOutput;
import net.aokv.integrationserver.wmpublic.WmPublicService;

/**
 * Default example: class name == service name and package ends with service namespace.
 */
@SuppressWarnings("PMD")
public class max extends WmPublicService<max.Input, max.Output>
{
	public static class Input extends ServiceInput
	{
		public String[] numList;
	}

	public static class Output extends ServiceOutput
	{
		public String maxValue;
	}
}
