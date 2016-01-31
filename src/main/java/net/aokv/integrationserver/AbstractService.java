package net.aokv.integrationserver;

import java.lang.reflect.ParameterizedType;

import net.aokv.idataconverter.IDataConverter;
import net.aokv.idataconverter.ObjectConverter;

public abstract class AbstractService<TInput extends ServiceInput, TOutput extends ServiceOutput>
{
	protected static final ObjectConverter OBJECT_CONVERTER = new ObjectConverter();
	protected static final IDataConverter IDATA_CONVERTER = new IDataConverter();

	@SuppressWarnings("unchecked")
	protected Class<TOutput> getOutputClass()
	{
		return (Class<TOutput>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@SuppressWarnings("unchecked")
	protected Class<TInput> getInputClass()
	{
		return (Class<TInput>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
