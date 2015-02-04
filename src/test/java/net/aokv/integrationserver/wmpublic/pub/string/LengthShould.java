package net.aokv.integrationserver.wmpublic.pub.string;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class LengthShould
{
	private Length sut;
	private Length.Input input;

	@Before
	public void setup() throws IOException
	{
		sut = new Length();
		input = new Length.Input();
	}

	@Test
	public void returnNullForNull() throws Exception
	{
		input.inString = null;
		final Length.Output output = sut.call(input);
		assertThat(output.value, nullValue());
	}

	@Test
	public void return0ForEmptyString() throws Exception
	{
		input.inString = "";
		final Length.Output output = sut.call(input);
		assertThat(output.value, is("0"));
	}

	@Test
	public void returnCorrectLengthForString() throws Exception
	{
		input.inString = "I am a String";
		final Length.Output output = sut.call(input);
		assertThat(output.value, is("13"));
	}
}
