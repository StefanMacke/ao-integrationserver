package net.aokv.integrationserver.wmpublic.pub.string2;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ConcatStringShould
{
	private ConcatString sut;
	private ConcatString.Input input;

	@Before
	public void setup() throws IOException
	{
		sut = new ConcatString();
		input = new ConcatString.Input();
	}

	@Test
	public void returnConcatenatedStrings() throws Exception
	{
		input.inString1 = "first";
		input.inString2 = "second";
		final ConcatString.Output output = sut.call(input);
		assertThat(output.value, is("firstsecond"));
	}
}
