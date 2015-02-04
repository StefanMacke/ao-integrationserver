package net.aokv.integrationserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Configuration
{
	private static final String PROPERTIES_FILENAME = "IntegrationServer";

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
		try (final InputStream stream = Service.class.getResourceAsStream(
				String.format("/%s.properties", PROPERTIES_FILENAME)))
		{
			props.load(stream);
			final ResourceBundle rb = PropertyResourceBundle.getBundle(PROPERTIES_FILENAME);
			if (rb != null)
			{
				Collections.list(rb.getKeys())
				.forEach(key -> props.setProperty(key, rb.getString(key)));
			}
			return props;
		}
		catch (final IOException e)
		{
			throw new RuntimeException(
					String.format("Error while reading properties file <%s>: %s",
							PROPERTIES_FILENAME,
							e.getMessage()),
					e);
		}
	}

	private Map<TargetServers, String> initializeHostProperties()
	{
		final Map<TargetServers, String> hosts = new HashMap<>();
		hosts.put(TargetServers.Development, "integrationserver.host_development");
		hosts.put(TargetServers.Test, "integrationserver.host_test");
		hosts.put(TargetServers.Production, "integrationserver.host_production");
		return hosts;
	}

	public TargetServers getTargetServer()
	{
		final String targetServerSetting =
				(String) properties.getOrDefault(PROPERTY_TARGET_SERVER, "Development");
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
