package net.aokv.integrationserver.wmpublic.pub.string;

import net.aokv.integrationserver.ServiceInput;
import net.aokv.integrationserver.ServiceOutput;
import net.aokv.integrationserver.wmpublic.WmPublicService;

/**
 * Example for service name != class name.
 */
@SuppressWarnings("PMD")
public class Length extends WmPublicService<Length.Input, Length.Output>
{
	public static class Input extends ServiceInput
	{
		public String inString;
	}

	public static class Output extends ServiceOutput
	{
		public String value;
	}

	@Override
	public String getName()
	{
		return "length";
	}
}
