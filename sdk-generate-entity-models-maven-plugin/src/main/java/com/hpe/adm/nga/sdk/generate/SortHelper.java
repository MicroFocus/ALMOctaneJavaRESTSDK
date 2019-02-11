package com.hpe.adm.nga.sdk.generate;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
