package com.hpe.adm.nga.sdk.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds useful metadata information to the method such as whether the field is required or sortable
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FieldMetadata {
    /**
     * Is the field required
     * @return required
     */
    boolean required() default false;
    /**
     * Is the field filterable
     * @return filterable
     */
    boolean filterable() default true;
    /**
     * Is the field sortable
     * @return sortable
     */
    boolean sortable() default true;
    /**
     * The minimum value for the field (where relevant)
     * @return minvalue
     */
    long minValue() default Long.MIN_VALUE;
    /**
     * The maximum value for the field (where relevant)
     * @return maxvalue
     */
    long maxValue() default Long.MAX_VALUE;

    /**
     * The maximum length for the string field (where relevant)
     * @return max length
     */
    long maxLength() default Integer.MAX_VALUE;
}
