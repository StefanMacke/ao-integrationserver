package net.aokv.integrationserver;

import com.wm.app.b2b.server.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

import net.aokv.idataconverter.IDataConversionException;
import net.aokv.idataconverter.ObjectConversionException;

/**
 * Base class for all Java implementations of IS-Services.
 *
 * @param <TInput> The input POJO for the service.
 * @param <TOutput> The output POJO for the service.
 */
public abstract class JavaService<TInput extends ServiceInput, TOutput extends ServiceOutput>
		extends AbstractService<TInput, TOutput>
{
	/**
	 * Gets called from a Java service in IS, e.g.
	 *
	 * <pre>
	 * public static final void MyJavaServiceInIS(IData pipeline)
	 * 		throws ServiceException
	 * {
	 * 	new MyJavaImplementation().call(pipeline);
	 * }
	 * </pre>
	 *
	 * @param pipeline The pipeline (IData) from IS.
	 * @throws ServiceException In case anything goes wrong.
	 */
	public final void call(final IData pipeline) throws ServiceException
	{
		final TInput input = readInputFromPipeline(pipeline);
		final TOutput output = call(input);
		writeOutputToPipeline(pipeline, output);
	}

	/**
	 * Does the actual work. Gets called by the base class automatically.
	 *
	 * @param input The input POJO from the IS pipeline.
	 * @return The output POJO to write to the IS pipeline.
	 * @throws ServiceException In case anything goes wrong.
	 */
	public abstract TOutput call(final TInput input)
			throws ServiceException;

	/**
	 * Reads the service's input from the pipeline and converts it to a POJO.
	 *
	 * @param pipeline The IS pipeline.
	 * @return The converted TInput POJO.
	 * @throws ServiceException In case anything goes wrong.
	 */
	protected final TInput readInputFromPipeline(final IData pipeline)
			throws ServiceException
	{
		try
		{
			return IDATA_CONVERTER.convertToObject(pipeline, getInputClass());
		}
		catch (final IDataConversionException e)
		{
			throw new ServiceException(e);
		}
	}

	/**
	 * Clears the pipeline and writes the service's output to the pipeline.
	 *
	 * @param pipeline The IS pipeline.
	 * @param output The TOutput to write to the pipeline.
	 * @throws ServiceException In case anything goes wrong.
	 */
	protected final void writeOutputToPipeline(final IData pipeline, final TOutput output)
			throws ServiceException
	{
		try
		{
			final IData outputIData = OBJECT_CONVERTER.convertToIData(output);
			clearPipeline(pipeline);
			IDataUtil.merge(outputIData, pipeline);
		}
		catch (final ObjectConversionException e)
		{
			throw new ServiceException(e);
		}
	}

	private void clearPipeline(final IData pipeline)
	{
		final IDataCursor cursor = pipeline.getCursor();
		while (cursor.next())
		{
			IDataUtil.remove(cursor, cursor.getKey());
		}
	}
}
