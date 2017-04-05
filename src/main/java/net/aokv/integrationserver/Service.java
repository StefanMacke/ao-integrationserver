package net.aokv.integrationserver;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import com.wm.app.b2b.util.GenUtil;
import com.wm.data.IData;

import net.aokv.idataconverter.IDataConversionException;
import net.aokv.idataconverter.ObjectConversionException;

/**
 * Base class for all service calls to Integration Server.
 *
 * @param <TInput> The input POJO for the service.
 * @param <TOutput> The output POJO for the service.
 */
public abstract class Service<TInput extends ServiceInput, TOutput extends ServiceOutput>
		extends AbstractService<TInput, TOutput>
{
	private String targetHost;
	private String username;
	private String password;

	private final String serviceNamespace;
	private final String serviceName;
	private boolean debugModeEnabled;

	private static final Configuration CONFIGURATION = new Configuration();

	/**
	 * Main constructor. Target server and user credentials are read from properties file and may be
	 * overwritten later by calling the corresponding setters.
	 */
	protected Service()
	{
		this.username = CONFIGURATION.getAdminUsername();
		this.password = CONFIGURATION.getAdminPassword();
		this.targetHost = CONFIGURATION.getTargetHost();
		this.serviceNamespace = getNamespace();
		this.serviceName = getName();
		this.debugModeEnabled = false;
	}

	/**
	 * Calls the service with POJOs.
	 *
	 * @param input The input for the service call, i.e. an object derived from ServiceInput.
	 * @return The output of the service call, i.e. an object derived from ServiceOutput.
	 * @throws ObjectConversionException Thrown if input could not be converted to IData.
	 * @throws IDataConversionException Thrown if output could not be converted to POJO.
	 * @throws ServiceException Thrown if something went wrong on Integration Server.
	 */
	public TOutput call(final TInput input)
			throws ObjectConversionException, IDataConversionException, ServiceException
	{
		if (isDebugModeEnabled())
		{
			System.out.println("Input POJO");
			System.out.println(input.toString());
		}
		final IData inputIData = OBJECT_CONVERTER.convertToIData(input);
		final TOutput output = convertToOutput(call(inputIData));
		if (isDebugModeEnabled())
		{
			System.out.println("Output POJO");
			System.out.println(output.toString());
		}
		return output;
	}

	/**
	 * Calls the service with IData.
	 *
	 * @param input The input for the service call, i.e. an IData object.
	 * @return The output of the service call, i.e. an IData object.
	 * @throws ServiceException Thrown if something went wrong on Integration Server.
	 */
	public IData call(final IData input)
			throws ServiceException
	{
		if (isDebugModeEnabled())
		{
			GenUtil.printRec(input, "Input IData");
		}
		final IData output = invokeService(input);
		if (isDebugModeEnabled())
		{
			GenUtil.printRec(output, "Output IData");
		}
		return output;
	}

	/**
	 * Converts IData to POJO (ServiceOutput).
	 *
	 * @param iData The IData to convert.
	 * @return The POJO (ServiceOutput).
	 * @throws IDataConversionException Thrown if IData could not be converted to POJO.
	 */
	public final TOutput convertToOutput(final IData iData)
			throws IDataConversionException
	{
		return IDATA_CONVERTER.convertToObject(iData, getOutputClass());
	}

	/**
	 * Converts IData to POJO (ServiceInput).
	 *
	 * @param iData The IData to convert.
	 * @return The POJO (ServiceInput).
	 * @throws IDataConversionException Thrown if IData could not be converted to POJO.
	 */
	public final TInput convertToInput(final IData iData)
			throws IDataConversionException
	{
		return IDATA_CONVERTER.convertToObject(iData, getInputClass());
	}

	/**
	 * Returns the Java base package of the service, e.g. "net.aokv.integrationserver.wmpublic" for
	 * a service in "Wmnet.aokv.integrationserver.wmpublic.pub.math". This is used to automatically
	 * generate the service namespace.
	 *
	 * @return The Java base package.
	 */
	public abstract String getJavaBasePackage();

	/**
	 * Returns the name of the IS package of the service, e.g. "WmPublic". This is used to
	 * automatically generate the service namespace.
	 *
	 * @return The IS package.
	 */
	public String getPackage()
	{
		final String basePackage = getJavaBasePackage();
		final String[] basePackageParts = basePackage.split("\\.");
		return ServiceNameHelper.capitalize(basePackageParts[basePackageParts.length - 1]);
	}

	/**
	 * Returns the namespace of the service, e.g. "pub.math". Override if service package does not
	 * end with namespace, e.g. service package "com.example.services.pub" and namespace
	 * "services.pub2".
	 *
	 * @return The namespace.
	 */
	public String getNamespace()
	{
		return ServiceNameHelper.extractSubpackage(
				getClass().getPackage().getName(), getJavaBasePackage());
	}

	/**
	 * Returns the name of the service, e.g. "max". Override if class name != service name, e.g.
	 * class name "Max" and service name "max".
	 *
	 * @return The name of the service.
	 */
	public String getName()
	{
		return getClass().getSimpleName();
	}

	/**
	 * Sets the new target server.
	 *
	 * @param targetServer The new target server.
	 */
	public void setTargetServer(final TargetServers targetServer)
	{
		this.targetHost = CONFIGURATION.getTargetHost(targetServer);
	}

	/**
	 * Sets the new target host.
	 *
	 * @param targetHost The new target host.
	 */
	public void setTargetHost(final String targetHost)
	{
		this.targetHost = targetHost;
	}

	/**
	 * Sets the new username.
	 *
	 * @param username The new username.
	 */
	public void setUsername(final String username)
	{
		this.username = username;
	}

	/**
	 * Sets the new password.
	 *
	 * @param password The new password.
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * Returns the target host of the service.
	 *
	 * @return The target host.
	 */
	public String getTargetHost()
	{
		return targetHost;
	}

	/**
	 * Enables debug output before and after each service call.
	 */
	public final void enableDebugMode()
	{
		debugModeEnabled = true;
	}

	/**
	 * Disables debug output before and after each service call.
	 */
	public final void disableDebugMode()
	{
		debugModeEnabled = false;
	}

	private final boolean isDebugModeEnabled()
	{
		return debugModeEnabled;
	}

	private Context connectToServer() throws ServiceException
	{
		final Context context = new Context();
		context.connect(
				targetHost,
				username,
				password);
		return context;
	}

	private IData invokeService(final IData input) throws ServiceException
	{
		Context context = null;
		try
		{
			context = connectToServer();
			return context.invoke(serviceNamespace, serviceName, input);
		}
		finally
		{
			if (context != null)
			{
				context.disconnect();
			}
		}
	}
}