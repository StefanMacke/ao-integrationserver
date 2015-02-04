package net.aokv.integrationserver;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ServiceNameHelperShould
{
	@Test
	public void capitalizeWords() throws Exception
	{
		assertThat(ServiceNameHelper.capitalize(null), nullValue());
		assertThat(ServiceNameHelper.capitalize(""), is(""));
		assertThat(ServiceNameHelper.capitalize("asdf"), is("Asdf"));
		assertThat(ServiceNameHelper.capitalize("ASDF"), is("ASDF"));
		assertThat(ServiceNameHelper.capitalize("AsDf"), is("AsDf"));
		assertThat(ServiceNameHelper.capitalize("asDf"), is("AsDf"));
	}

	@Test
	public void extractSubpackagesFromPackages() throws Exception
	{
		final String basePackage = "net.aokv.insurance";
		assertThat(ServiceNameHelper.extractSubpackage(null, null), nullValue());
		assertThat(ServiceNameHelper.extractSubpackage(null, "asdf"), nullValue());
		assertThat(ServiceNameHelper.extractSubpackage("asdf", null), is("asdf"));
		assertThat(ServiceNameHelper.extractSubpackage("net.aokv", "not.found"), is("net.aokv"));

		assertThat(ServiceNameHelper.extractSubpackage(basePackage, basePackage), is(""));
		assertThat(ServiceNameHelper.extractSubpackage("net.aokv.insurance.sub", basePackage),
				is("sub"));
		assertThat(ServiceNameHelper.extractSubpackage("net.aokv.insurance.sub.subsub", basePackage),
				is("sub.subsub"));
	}

	@Test
	public void extractNamespacesFromPackages() throws Exception
	{
		final String basePackage = "net.aokv.insurance";
		assertThat(ServiceNameHelper.extractNamespace(null, null), nullValue());
		assertThat(ServiceNameHelper.extractNamespace(null, "net.aokv"), is("Aokv"));
		assertThat(ServiceNameHelper.extractNamespace("net.aokv", null), is("Net.Aokv"));
		assertThat(ServiceNameHelper.extractNamespace("net.aokv", "not.found"), is("Net.Aokv"));

		assertThat(ServiceNameHelper.extractNamespace(basePackage, basePackage), is("Insurance"));
		assertThat(ServiceNameHelper.extractNamespace("net.aokv.insurance.sub", basePackage),
				is("Insurance.Sub"));
		assertThat(ServiceNameHelper.extractNamespace("net.aokv.insurance.sub.subsub", basePackage),
				is("Insurance.Sub.Subsub"));
	}

	@Test
	public void convertPackagesToNamespaces() throws Exception
	{
		assertThat(ServiceNameHelper.convertToNamespace(null), nullValue());
		assertThat(ServiceNameHelper.convertToNamespace(""), is(""));
		assertThat(ServiceNameHelper.convertToNamespace("folder"), is("Folder"));
		assertThat(ServiceNameHelper.convertToNamespace("folder."), is("Folder"));
		assertThat(ServiceNameHelper.convertToNamespace(".folder"), is("Folder"));
		assertThat(ServiceNameHelper.convertToNamespace(".folder."), is("Folder"));
		assertThat(ServiceNameHelper.convertToNamespace("folder.subfolder"), is("Folder.Subfolder"));
		assertThat(ServiceNameHelper.convertToNamespace("folder.subfolder.subsub"), is("Folder.Subfolder.Subsub"));
	}

	@Test
	public void extractIsPackageNamesFromPackages() throws Exception
	{
		assertThat(ServiceNameHelper.extractIsPackageName(null), nullValue());
		assertThat(ServiceNameHelper.extractIsPackageName(""), is(""));
		assertThat(ServiceNameHelper.extractIsPackageName("folder"), is("Folder"));
		assertThat(ServiceNameHelper.extractIsPackageName("folder.subfolder"), is("Subfolder"));
		assertThat(ServiceNameHelper.extractIsPackageName("folder.subfolder.subsub"), is("Subsub"));
	}
}
