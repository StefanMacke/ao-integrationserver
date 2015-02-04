package net.aokv.integrationserver;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Provides methods to automatically generate service namespaces etc.
 * if services in Integration Server follow this structure:
 *
 * <pre>Package (e.g. "Insurance", one word starting with an uppercase letter)
 *   Folder (must be equal to Package name and becomes part of the namespace)
 *     Subfolders (optional)
 *       Service</pre>
 *
 * And if the corresponding Java classes follow this structure:
 *
 * <pre>base Java package (e.g. "net.aokv.integrationserver")
 *   package (must be equal to IS package/folder in lowercase)
 *     subpackages (optional, must be equal to IS subfolders in lowercase)
 *       Class (name must be equal to service name)</pre>
 *
 * Example:
 *
 * IS:
 * <pre>Insurance (package)
 *   Insurance (folder)
 *     Health (subfolder)
 *       Services (subfolder)
 *         CalculatePremium (service)</pre>
 *
 * Java:
 * <pre>net.aokv.integrationserver.insurance (base package, only one insurance!)
 *     health (subpackage)
 *       services (subpackage)
 *         CalculatePremium (class)</pre>
 */
public final class ServiceNameHelper
{
	private ServiceNameHelper()
	{}

	public static String capitalize(final String word)
	{
		if (word == null)
		{
			return null;
		}
		if (word.trim().equals(""))
		{
			return "";
		}
		return word.substring(0, 1).toUpperCase(Locale.getDefault()) + word.substring(1);
	}

	public static String extractSubpackage(final String javaPackage, final String javaBasePackage)
	{
		if (javaPackage == null)
		{
			return null;
		}
		if (javaBasePackage == null)
		{
			return javaPackage;
		}
		String subpackage = javaPackage.replace(javaBasePackage, "");
		if (subpackage.startsWith("."))
		{
			subpackage = subpackage.substring(1);
		}
		return subpackage;
	}

	public static String convertToNamespace(String javaPackage)
	{
		if (javaPackage == null)
		{
			return null;
		}
		if (javaPackage.startsWith("."))
		{
			javaPackage = javaPackage.substring(1);
		}
		return Arrays
				.stream(javaPackage.split("\\."))
				.map(ServiceNameHelper::capitalize)
				.collect(Collectors.joining("."));
	}

	public static String extractIsPackageName(final String javaBasePackage)
	{
		if (javaBasePackage == null)
		{
			return null;
		}
		final String[] subpackageParts = javaBasePackage.split("\\.");
		return capitalize(subpackageParts[subpackageParts.length - 1]);
	}

	public static String extractNamespace(final String javaPackage, final String javaBasePackage)
	{
		if (javaPackage == null && javaBasePackage == null)
		{
			return null;
		}

		if (javaBasePackage == null)
		{
			return convertToNamespace(javaPackage);
		}

		if (javaPackage == null)
		{
			return extractIsPackageName(javaBasePackage);
		}

		if (!javaPackage.contains(javaBasePackage))
		{
			return convertToNamespace(javaPackage);
		}

		final String packageName = extractIsPackageName(javaBasePackage);
		final String subpackage = extractSubpackage(javaPackage, javaBasePackage);
		final String namespaceCandidate = packageName + "." + subpackage;

		return convertToNamespace(namespaceCandidate);
	}
}
