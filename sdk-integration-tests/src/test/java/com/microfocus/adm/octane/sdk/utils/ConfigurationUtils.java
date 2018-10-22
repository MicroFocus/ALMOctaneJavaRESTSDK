/*
 * Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microfocus.adm.octane.sdk.utils;

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
