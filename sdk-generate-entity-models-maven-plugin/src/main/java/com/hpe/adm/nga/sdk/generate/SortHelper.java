/*
 * Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hpe.adm.nga.sdk.generate;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that is used to help with sorting stuff
 */
public final class SortHelper {

	/**
	 * Sorts RequiredFields by given Fieldname ASC
	 *
	 * @param requiredFields
	 *            Collection of FieldLists with Array of field data
	 * @param fieldName
	 *            Name of the field
	 * @return Sorted List
	 */
	public static List<List<String[]>> sortRequiredFieldsByFieldName(final Collection<List<String[]>> requiredFields,
			final String fieldName) {
		return requiredFields.stream()
				.sorted(Comparator.comparing(
						fieldList -> fieldList.stream().map(fields -> String.join("", fields)).sorted().collect(
								Collectors.joining())

				))
				.collect(Collectors.toList());
	}
}
