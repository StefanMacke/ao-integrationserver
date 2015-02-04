package net.aokv.integrationserver.wmpublic;

import net.aokv.integrationserver.Service;
import net.aokv.integrationserver.ServiceInput;
import net.aokv.integrationserver.ServiceOutput;

/**
 * Helper base class to remove duplication in services in package WmPublic.
 */
public abstract class WmPublicService<TInput extends ServiceInput, TOutput extends ServiceOutput>
extends Service<TInput, TOutput>
{
	@Override
	public String getJavaBasePackage()
	{
		return WmPublicService.class.getPackage().getName();
	}

	@Override
	public String getPackage()
	{
		return "WmPublic";
	}

}
