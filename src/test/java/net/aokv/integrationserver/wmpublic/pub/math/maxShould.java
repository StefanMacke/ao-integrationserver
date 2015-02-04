package net.aokv.integrationserver.wmpublic.pub.math;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.wm.app.b2b.client.ServiceException;

public class maxShould
{
	private max sut;
	private max.Input input;

	@Before
	public void setup() throws IOException
	{
		sut = new max();
		input = new max.Input();
	}

	@Test
	public void throwExceptionIfListIsEmpty() throws Exception
	{
		input.numList = null;
		try
		{
			sut.call(input);
			fail("missing parameter should throw an exception");
		}
		catch (final ServiceException e)
		{
			assertThat(e.getMessage(), containsString("Missing Parameter: numList"));
		}
	}

	@Test
	public void returnNullForEmptyList() throws Exception
	{
		input.numList = new String[0];
		final max.Output output = sut.call(input);
		assertThat(output.maxValue, nullValue());
	}

	@Test
	public void returnElementForListWithOneElement() throws Exception
	{
		input.numList = new String[]
				{ "1" };
		final max.Output output = sut.call(input);
		assertThat(output.maxValue, is("1.0"));
	}

	@Test
	public void returnMaxElementForListWithTwoElements() throws Exception
	{
		input.numList = new String[]
				{ "1", "2" };
		final max.Output output = sut.call(input);
		assertThat(output.maxValue, is("2.0"));
	}

	@Test
	public void returnMaxElementForListWithMoreThanTwoElements() throws Exception
	{
		input.numList = new String[]
				{ "2", "4", "3", "1" };
		final max.Output output = sut.call(input);
		assertThat(output.maxValue, is("4.0"));
	}
}
