package net.aokv.integrationserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Configuration
{
	private static final String PROPERTIES_FILENAME = "IntegrationServer";
	private static final String PROPERTIES_FILENAME_WITH_EXT = String.format("%s.properties", PROPERTIES_FILENAME);
	private static final File PROPERTIES_FILE = new File("./" + PROPERTIES_FILENAME_WITH_EXT);

	private static final String PROPERTY_ADMIN_USERNAME = "integrationserver.admin_username";
	private static final String PROPERTY_ADMIN_PASSWORD = "integrationserver.admin_password";
	private static final String PROPERTY_TARGET_SERVER = "integrationserver.target_server";
	private final Map<TargetServers, String> hostProperties;

	private static final String DEFAULT_ADMIN_USERNAME = "Administrator";
	private static final String DEFAULT_ADMIN_PASSWORD = "manage";
	private static final String DEFAULT_HOST = "localhost:5555";

	private final Properties properties;

	public Configuration()
	{
		properties = readProperties();
		hostProperties = initializeHostProperties();
	}

	private Properties readProperties()
	{
		final Properties props = new Properties();
		try (final InputStream stream = Service.class.getResourceAsStream("/" + PROPERTIES_FILENAME_WITH_EXT))
		{
			props.load(stream);
			createLocalPropertiesFileIfNotExists();
			addPropertiesFromLocalPropertiesFile(props);
			addPropertiesFromSystem(props);
			return props;
		}
		catch (final IOException e)
		{
			throw new RuntimeException(
					String.format("Error while reading properties file <%s>: %s",
							PROPERTIES_FILENAME_WITH_EXT,
							e.getMessage()),
					e);
		}
	}

	private void createLocalPropertiesFileIfNotExists() throws IOException
	{
		final List<String> lines = allDefaultProperties().collect(Collectors.toList());

		if (!PROPERTIES_FILE.exists())
		{
			Files.write(PROPERTIES_FILE.toPath(), lines, StandardOpenOption.CREATE_NEW);
		}
	}

	private void addPropertiesFromLocalPropertiesFile(final Properties props) throws IOException
	{
		final Properties localProps = new Properties();
		final InputStream stream = new FileInputStream(PROPERTIES_FILE);
		localProps.load(stream);
		localProps.keySet()
				.forEach(key -> props.setProperty(key.toString(), localProps.get(key).toString()));
	}

	private void addPropertiesFromSystem(final Properties props)
	{
		props.putAll(System.getProperties());
	}

	private Map<TargetServers, String> initializeHostProperties()
	{
		final Map<TargetServers, String> hosts = new HashMap<>();
		hosts.put(TargetServers.Local, "integrationserver.host_local");
		hosts.put(TargetServers.Development, "integrationserver.host_development");
		hosts.put(TargetServers.Test, "integrationserver.host_test");
		hosts.put(TargetServers.Production, "integrationserver.host_production");
		return hosts;
	}

	private Stream<String> allDefaultProperties()
	{
		return Stream.concat(
				Arrays.asList(
						String.format("%s=%s", PROPERTY_ADMIN_USERNAME, DEFAULT_ADMIN_USERNAME),
						String.format("%s=%s", PROPERTY_ADMIN_PASSWORD, DEFAULT_ADMIN_PASSWORD),
						String.format("%s=%s", PROPERTY_TARGET_SERVER, "Local")).stream(),
				initializeHostProperties().values().stream()
						.map(v -> String.format("%s=%s", v, DEFAULT_HOST)));
	}

	public TargetServers getTargetServer()
	{
		final String targetServerSetting =
				(String) properties.getOrDefault(PROPERTY_TARGET_SERVER, TargetServers.Local.toString());
		return TargetServers.valueOf(targetServerSetting);
	}

	public String getTargetHost(final TargetServers targetServer)
	{
		final String property = hostProperties.get(targetServer);
		return (String) properties.getOrDefault(property, DEFAULT_HOST);
	}

	public String getTargetHost()
	{
		return getTargetHost(getTargetServer());
	}

	public String getAdminUsername()
	{
		return properties.getProperty(PROPERTY_ADMIN_USERNAME, DEFAULT_ADMIN_USERNAME);
	}

	public String getAdminPassword()
	{
		return properties.getProperty(PROPERTY_ADMIN_PASSWORD, DEFAULT_ADMIN_PASSWORD);
	}
}
