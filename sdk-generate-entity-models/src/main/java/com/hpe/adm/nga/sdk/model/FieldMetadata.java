package com.hpe.adm.nga.sdk.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by brucesp on 26-Jun-17.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FieldMetadata {
    boolean required() default false;
    boolean filterable() default true;
    boolean sortable() default true;
    long minValue() default Long.MIN_VALUE;
    long maxValue() default Long.MAX_VALUE;
    long maxLength() default Integer.MAX_VALUE;
}
