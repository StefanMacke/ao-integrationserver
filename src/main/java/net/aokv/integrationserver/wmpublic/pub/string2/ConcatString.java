package net.aokv.integrationserver.wmpublic.pub.string2;

import net.aokv.integrationserver.ServiceInput;
import net.aokv.integrationserver.ServiceOutput;
import net.aokv.integrationserver.wmpublic.WmPublicService;

/**
 * Example for service name != class name and package does not end with service namespace.
 */
@SuppressWarnings("PMD")
public class ConcatString extends WmPublicService<ConcatString.Input, ConcatString.Output>
{
	public static class Input extends ServiceInput
	{
		public String inString1;
		public String inString2;
	}

	public static class Output extends ServiceOutput
	{
		public String value;
	}

	@Override
	public String getNamespace()
	{
		return "pub.string";
	}

	@Override
	public String getName()
	{
		return "concat";
	}
}
