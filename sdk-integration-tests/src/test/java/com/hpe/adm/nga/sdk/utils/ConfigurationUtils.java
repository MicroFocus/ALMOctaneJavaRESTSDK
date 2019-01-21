package com.hpe.adm.nga.sdk.utils;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.SystemConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * Created by brucesp on 06/06/2016.
 */
public class ConfigurationUtils {
	private static final ConfigurationUtils INSTANCE = new ConfigurationUtils();

	final CombinedConfiguration combinedConfiguration;

	private ConfigurationUtils() {
		final Configurations configurations = new Configurations();
		combinedConfiguration = new CombinedConfiguration();
		try {
			combinedConfiguration.addConfiguration(new SystemConfiguration());
			combinedConfiguration.addConfiguration(configurations.properties("configuration.properties"));
		} catch (ConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static final ConfigurationUtils getInstance() {
		return INSTANCE;
	}

	public final String getString(final String property) {
		return combinedConfiguration.getString(property);
	}
}
